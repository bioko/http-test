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

import org.hamcrest.Matcher;

import java.util.HashMap;
import java.util.Map;


public final class HttpScenarioStep implements ScenarioStep {

	public String fPartialRestURL;
	public String fHttpMethod;
	public Map<String, String> fHeaders;
	public Map<String, String> fParameters;
	public String fRequestBodyJson;
	public int fExpectedHttpStatusCode;
	public Matcher<String> fExpectedJsonBodyMatcher;
	private String fEntityKey;

	public HttpScenarioStep(String restURL, String httpMethod,
			Map<String, String> headers, Map<String, String> parameters,
			String requestBodyJson, int expectedHttpStatusCode, Matcher<String> expectedJsonBodyMatcher) {
				fPartialRestURL = restURL;
				fHttpMethod = httpMethod;
				
				fHeaders = (headers == null) ? fHeaders = new HashMap<String, String>() : headers;
				if (!fHeaders.containsKey("Accept")) {
					fHeaders.put("Accept", "application/json");
				}
				
				fParameters = (parameters == null) ? fParameters = new HashMap<String, String>() : parameters;

				fRequestBodyJson = requestBodyJson;
				fExpectedHttpStatusCode = expectedHttpStatusCode;
				fExpectedJsonBodyMatcher = expectedJsonBodyMatcher;
	}
	
	public HttpScenarioStep(String completeRestURL, String httpMethod,
			Map<String, String> headers, Map<String, String> parameters,
			String requestBodyJson, int expectedHttpStatusCode, Matcher<String> expectedJsonBodyMatcher, HashMap<String, String> keyCollector) {
				fPartialRestURL = String.format(completeRestURL, keyCollector.get(0));
				fHttpMethod = httpMethod;
				
				fHeaders = (headers == null) ? fHeaders = new HashMap<String, String>() : headers;
				fParameters = (parameters == null) ? fParameters = new HashMap<String, String>() : parameters;

				fRequestBodyJson = requestBodyJson;
				fExpectedHttpStatusCode = expectedHttpStatusCode;
				fExpectedJsonBodyMatcher = expectedJsonBodyMatcher;
	}

	public void setEntityKey(String entityKey) {
		fEntityKey = entityKey;
	}

	public void setBodyMatcher(Matcher<String> aMatcher) {
		fExpectedJsonBodyMatcher = aMatcher;
	}
	
	public String entityKey() {
		return fEntityKey;
	}

	public Matcher<?> bodyMatcher() {
		return fExpectedJsonBodyMatcher ;
	}
}