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

package it.bioko.http.scenario;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matcher;

public final class HttpScenarioStepBinary implements ScenarioStep {

	public String _partialRestURL;
	public String _httpMethod;
	public Map<String, String> _headers;
	public Map<String, String> _parameters;
	public String _requestBodyContentType;
	public byte[] _requestBody;
	public int _expectedHttpStatusCode;
	public Matcher<? extends Object> _expectedJsonBodyMatcher;
	private String _entityKey;
	public String _filePartName;

	public HttpScenarioStepBinary(String completeRestURL, String httpMethod, Map<String, String> headers, 
			Map<String, String> parameters, String filePartName, String requestBodyContentType, 
			byte[] requestBody, int expectedHttpStatusCode, Matcher<? extends Object> expectedJsonBodyMatcher) {
				_partialRestURL = completeRestURL;
				_httpMethod = httpMethod;
				
				_headers = (headers == null) ? _headers = new HashMap<String, String>() : headers;
				_parameters = (parameters == null) ? _parameters = new HashMap<String, String>() : parameters;

				_requestBodyContentType = requestBodyContentType;
				_filePartName = filePartName;
				_requestBody = requestBody;
				
				_expectedHttpStatusCode = expectedHttpStatusCode;
				_expectedJsonBodyMatcher = expectedJsonBodyMatcher;
	}
	
	public void setEntityKey(String entityKey) {
		_entityKey = entityKey;
	}

	public void setBodyMatcher(Matcher<Object> aMatcher) {
		_expectedJsonBodyMatcher = aMatcher;
	}
	
	public String entityKey() {
		return _entityKey;
	}

	public Matcher<?> bodyMatcher() {
		return _expectedJsonBodyMatcher ;
	}
	
}
