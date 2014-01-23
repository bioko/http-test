/*
 * Copyright (c) 2014																 
 *	Mikol Faro			<mikol.faro@gmail.com>
 *	Simone Mangano		<simone.mangano@ieee.org>
 *	Mattia Tortorelli	<mattia.tortorelli@gmail.com>
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 */

package org.biokoframework.http.scenario;

import static com.jayway.restassured.RestAssured.expect;
import static org.apache.commons.lang3.StringEscapeUtils.escapeJava;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeJava;
import static org.biokoframework.http.matcher.Matchers.exists;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.mail.Message;

import org.apache.commons.beanutils.MethodUtils;
import org.biokoframework.http.HttpMethodEnum;
import org.biokoframework.http.scenario.mail.MailScenarioStep;
import org.biokoframework.http.scenario.push.PushScenarioStep;
import org.biokoframework.system.KILL_ME.commons.HttpMethod;
import org.biokoframework.system.service.push.impl.TestNotificationImplementation;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsCollectionContaining;
import org.jvnet.mock_javamail.Mailbox;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

public class ScenarioRunner {

	private Scenario _scenarioCollector;
	
	public ScenarioRunner(Scenario collector) {
		_scenarioCollector = collector;
		Mailbox.clearAll();
	}
	
	public void test(String baseUrl) throws Exception {
		System.out.println("=== SCENARIO COLLECTOR: " + _scenarioCollector.scenarioName());
		for (Entry<String, ScenarioStep> eachScenario : _scenarioCollector.scenarioSteps()) {
			System.out.println("\t------ SCENARIO: " + eachScenario.getKey() + " --------");
			
			// Polymorphic visitor?
			try {
				MethodUtils.invokeMethod(this, "test",  
						new Object[] {baseUrl, eachScenario.getValue(), }, 
						new Class[] {String.class, eachScenario.getValue().getClass()});
			} catch (NoSuchMethodException exception) {				
				fail("[EASY MAN] '"+_scenarioCollector.scenarioName()+"' not testable, probably wrong class");				
			} catch (InvocationTargetException exception) {
				if (exception.getCause() != null) {
					if (exception.getCause() instanceof Exception) {
						throw (Exception) exception.getCause();
					} else if (exception.getCause() instanceof Error) {
						throw (Error) exception.getCause();
					}
				} else {
					throw exception;
				}
			}			
			System.out.println("\t------ END -------");
		}
		System.out.println("=== END ===");
	}

	public void test(String baseUrl, PushScenarioStep pushScenario) throws Exception {
		System.out.println("\tUser token: " + pushScenario._userToken);
		System.out.println("\t------ EXPECTATION -------");
		System.out.println("\tExpected Message:\n\t" + pushScenario._messageMatcher.toString());
		String scenarioExecutionMessage = scenarioExecution(pushScenario);
		System.out.println("\t------ RESPONSE -------");
		System.out.println("\t" + scenarioExecutionMessage);
	}
	
	private String scenarioExecution(PushScenarioStep pushScenario) {
		List<String> pushMessages = TestNotificationImplementation.getMessagesForUser(pushScenario._userToken);
		assertThat(pushMessages, IsCollectionContaining.<String>hasItem(pushScenario._messageMatcher));		
		for (Iterator<String> it = pushMessages.iterator(); it.hasNext();) {
			String aMessage = it.next();
			if (pushScenario._messageMatcher.matches(aMessage)) {
				it.remove();
				return aMessage;
			}
		}
		// It cannot reach this point, 
		// if there is no match it has already failed
		return null;
	}

	public void test(String baseUrl, MailScenarioStep mailScenario) throws Exception {
		System.out.println("\tMail box address: " + mailScenario._mailBoxAddress);
		System.out.println("\t------ EXPECTATION -------");
		System.out.println("\tExpected Message:\n\t" + mailScenario._messageMatcher.toString());
		Message scenarioExecutionMessage = scenarioExecution(mailScenario);
		System.out.println("\t------ RESPONSE -------");
		System.out.println("\t" + prettify(scenarioExecutionMessage));
	}
	
	private Message scenarioExecution(MailScenarioStep mailScenario) throws Exception {
		Mailbox inspectedBox = Mailbox.get(mailScenario._mailBoxAddress);
		assertThat(inspectedBox, exists());
		assertThat(inspectedBox, IsCollectionContaining.<Message>hasItem(mailScenario._messageMatcher));
		for (Iterator<Message> it = inspectedBox.iterator(); it.hasNext();) {
			Message aMessage = it.next();
			if (mailScenario._messageMatcher.matches(aMessage)) {
				it.remove();
				return aMessage;
			}
		}
		// It cannot reach this point, 
		// if there is no match it has already failed
		return null;
	}

	public void test(String baseUrl, ExecutionScenarioStep executionScenario) {
		System.out.println("\tRunning ExecutionScenarioStep code");
		executionScenario.execute();
	}
	
	
	public void test(String baseUrl, HttpScenarioStep httpScenario) {
		System.out.println("\tURL: " + httpScenario._partialRestURL);
		System.out.println("\tHTTP Method: " + httpScenario._httpMethod);
		System.out.println("\tHeaders: " + httpScenario._headers);
		System.out.println("\tParameters: " + httpScenario._parameters);
		System.out.println("\tBody:" + httpScenario._requestBodyJson);
		System.out.println("\t------ EXPECTATION -------");
		System.out.println("\tExpected Http Status Code:" + httpScenario._expectedHttpStatusCode);
		System.out.println("\tExpected Body Response:\n\t" + prettify(httpScenario._expectedJsonBodyMatcher));
		Response scenarioExecutionResponse = scenarioExecution(httpScenario, baseUrl);
		System.out.println("\t------ RESPONSE -------");
		System.out.println("\t" + scenarioExecutionResponse.asString());
	}
	
	public void test(String baseUrl, HttpMultipartScenarioStep httpScenario) throws IOException {
		System.out.println("\tURL: " + httpScenario._partialRestURL);
		System.out.println("\tHTTP Method: " + httpScenario._httpMethod);
		System.out.println("\tHeaders: " + httpScenario._headers);
		System.out.println("\tParameters: " + httpScenario._parameters);
		System.out.println("\t# parts:" + httpScenario._parts.size());
		System.out.println("\t------ EXPECTATION -------");
		System.out.println("\tExpected Http Status Code:" + httpScenario._expectedHttpStatusCode);
		System.out.println("\tExpected Body Response:\n\t" + prettify(httpScenario._expectedJsonBodyMatcher));
		Response scenarioExecutionResponse = scenarioExecution(httpScenario, baseUrl);
		System.out.println("\t------ RESPONSE -------");
		System.out.println("\t" + scenarioExecutionResponse.asString());
	}
	
	
	private Response scenarioExecution(HttpMultipartScenarioStep multipartScenario, String baseUrl) throws IOException {
		RequestSpecification specification = expect().
				statusCode(multipartScenario._expectedHttpStatusCode).
				body(multipartScenario._expectedJsonBodyMatcher).
				when().
				given().
				headers(multipartScenario._headers).
				queryParameters(multipartScenario._parameters);
		
		for(Entry<String, Object> part: multipartScenario._parts.entrySet()) {
			
			String name = part.getKey();
			Object value = part.getValue();
			if (value instanceof String) {
				specification.multiPart(name, (String) value);
				//specification.multiPart(name, new StringBody((String) value));
				
				//specification.multiPart(part.getKey(), (String) value, GenericFieldValues.TEXT_CONTENT_TYPE);
//				specification.multiPart(part.getKey(), null, IOUtils.toInputStream((String) value), GenericFieldValues.TEXT_CONTENT_TYPE);
				
				// formParam ha un comportamento del cazzo
//				specification.formParam(name, (String) value);
			}
			else if (value instanceof InputStream) {
				String contentType = multipartScenario._partContentTypes.get(name);
				specification.multiPart(part.getKey(), "", (InputStream) value, contentType);				
			}
			else {
				throw new UnsupportedOperationException("parts can only be String or InputStreams. part: "+name);
			}
				
		}
		
		specification.log().body();
		
		if (multipartScenario._httpMethod.equals(HttpMethod.POST.toString())) { 
			return specification.post(baseUrl + multipartScenario._partialRestURL);
		} else if (multipartScenario._httpMethod.equals(HttpMethod.PUT.toString())) {
			return specification.put(baseUrl + multipartScenario._partialRestURL);
		} else {
			throw new UnsupportedOperationException("Multipart only works with POST and PUT");
		}
		
		
	}
	
	

	private Response scenarioExecution(HttpScenarioStep httpScenario, String baseUrl) {
		switch (HttpMethodEnum.valueOf(httpScenario._httpMethod)) {
		case GET :
			return executeGet(httpScenario, baseUrl);
		case POST :
			return executePost(httpScenario, baseUrl);
		case PUT :
			return executePut(httpScenario, baseUrl);
		case DELETE :
			return executeDelete(httpScenario, baseUrl);
		case OPTIONS :
			return executeOptions(httpScenario, baseUrl);
		}
		return null;
	}

	public Response executeDelete(HttpScenarioStep httpScenario, String baseUrl) {
		return expect().
		statusCode(httpScenario._expectedHttpStatusCode).
		body(httpScenario._expectedJsonBodyMatcher).
		when().
		given().
		log().
		body().
		request().
		headers(httpScenario._headers).
		parameters(httpScenario._parameters).
		delete(baseUrl + httpScenario._partialRestURL);
	}

	public Response executePut(HttpScenarioStep httpScenario, String baseUrl) {
		return expect().
		statusCode(httpScenario._expectedHttpStatusCode).
		body(httpScenario._expectedJsonBodyMatcher).
		when().
		given().
		log().
		body().
		request().
		headers(httpScenario._headers).
		parameters(httpScenario._parameters).
		body(httpScenario._requestBodyJson).
		put(baseUrl + httpScenario._partialRestURL);
	}

	public Response executePost(HttpScenarioStep httpScenario, String baseUrl) {
		if (httpScenario._requestBodyJson != null) {
			return expect().
			statusCode(httpScenario._expectedHttpStatusCode).
			body(httpScenario.bodyMatcher()).
			when().
			given().
			log().
			body().
			request().
			headers(httpScenario._headers).
			queryParameters(httpScenario._parameters).
			body(httpScenario._requestBodyJson).
			post(baseUrl + httpScenario._partialRestURL);
		} else {
			return expect().
			statusCode(httpScenario._expectedHttpStatusCode).
			body(httpScenario.bodyMatcher()).
			when().
			given().
			log().
			body().
			request().
			headers(httpScenario._headers).
			queryParameters(httpScenario._parameters).
			post(baseUrl + httpScenario._partialRestURL);
		}
	}

	public Response executeGet(HttpScenarioStep httpScenario, String baseUrl) {
		return expect().
		statusCode(httpScenario._expectedHttpStatusCode).
		body(httpScenario._expectedJsonBodyMatcher).
		when().
		given().
		log().
		body().
		request().
		headers(httpScenario._headers).
		parameters(httpScenario._parameters).
		get(baseUrl + httpScenario._partialRestURL);
	}
	
	public Response executeOptions(HttpScenarioStep httpScenario, String baseUrl) {
		return expect().
		statusCode(httpScenario._expectedHttpStatusCode).
		body(httpScenario._expectedJsonBodyMatcher).
		when().
		given().
		log().
		body().
		request().
		headers(httpScenario._headers).
		parameters(httpScenario._parameters).
		options(baseUrl + httpScenario._partialRestURL);
	}
	
	public void test(String baseUrl, HttpScenarioStepBinary binaryScenario) {
		System.out.println("\tURL: " + binaryScenario._partialRestURL);
		System.out.println("\tHTTP Method: " + binaryScenario._httpMethod);
		System.out.println("\tHeaders: " + binaryScenario._headers);
		System.out.println("\tParameters: " + binaryScenario._parameters);
		System.out.println("\t------ EXPECTATION -------");
		System.out.println("\tExpected Http Status Code:" + binaryScenario._expectedHttpStatusCode);
		System.out.println("\tExpected Body Response:\n\t" + prettify(binaryScenario._expectedJsonBodyMatcher));
		scenarioExecution(binaryScenario, baseUrl);
	}
	
	private Response scenarioExecution(HttpScenarioStepBinary binaryScenario, String baseUrl) {
		RequestSpecification specification = expect().
				statusCode(binaryScenario._expectedHttpStatusCode).
				body(binaryScenario._expectedJsonBodyMatcher).
				when().
				given().
				multiPart(binaryScenario._filePartName, "", binaryScenario._requestBody, binaryScenario._requestBodyContentType).
				headers(binaryScenario._headers).
				queryParameters(binaryScenario._parameters);
		
		if (binaryScenario._httpMethod.equals(HttpMethod.POST.toString())) { 
			return specification.post(baseUrl + binaryScenario._partialRestURL);
		} else if (binaryScenario._httpMethod.equals(HttpMethod.PUT.toString())) {
			return specification.put(baseUrl + binaryScenario._partialRestURL);
		} else {
			throw new UnsupportedOperationException("Binary only works with POST and PUT");
		}
	}

	private String prettify(Matcher<? extends Object> matcher) {
		String unescaped = unescapeJava(matcher.toString());
		if (unescaped.matches("^\".+\"$")) {
			unescaped = unescaped.substring(1).substring(0, unescaped.length() - 2);
		}
		return unescaped;
	}
	
	private String prettify(Message scenarioExecutionMessage) throws Exception {
		StringBuilder builder = new StringBuilder();
		builder.append("(\"").append(scenarioExecutionMessage.getSubject()).append("\" and \"").
				append(escapeJava(scenarioExecutionMessage.getContent().toString())).append("\")");
		return builder.toString();
	}
}
