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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matcher;

public class HttpMultipartScenarioStep implements ScenarioStep {
	
	
	
	public String _partialRestURL;
	public String _httpMethod;
	public Map<String, String> _headers;
	public Map<String, String> _parameters;
	public int _expectedHttpStatusCode;
	public Matcher<? extends Object> _expectedJsonBodyMatcher;
	public Map<String, Object> _parts;
	public Map<String, String> _partContentTypes;
	

	public HttpMultipartScenarioStep(String completeRestURL, String httpMethod, Map<String, String> headers, 
			Map<String, String> parameters, int expectedHttpStatusCode, Matcher<? extends Object> expectedJsonBodyMatcher) {
		
		_partialRestURL = completeRestURL;
		_httpMethod = httpMethod;
		
		_headers = (headers == null) ? _headers = new HashMap<String, String>() : headers;
		_parameters = (parameters == null) ? _parameters = new HashMap<String, String>() : parameters;
		
		_expectedHttpStatusCode = expectedHttpStatusCode;
		_expectedJsonBodyMatcher = expectedJsonBodyMatcher;
		
		_parts = new HashMap<String, Object>();
		_partContentTypes = new HashMap<String, String>();
	}

	public void addPart(String partName, String partValue) {
//		addPart(partName, IOUtils.toInputStream(partValue), GenericFieldValues.TEXT_CONTENT_TYPE);
		String s = new String(partValue);
		_parts.put(partName, s);
//		_parts.put(partName, partValue);
	}
	
	
	public void addPart(String partName, InputStream partValue, String contentType) {
		_parts.put(partName, partValue);
		_partContentTypes.put(partName, contentType);
	}

	
	
	
}
