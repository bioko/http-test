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

package org.biokoframework.http.matcher;

import java.util.HashMap;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SubstituteBothJsonKeyMatcher extends TypeSafeMatcher<String> {

	private String _key;
	private String _expectedJson;
	private String _actualJson;
	private Object _actualIdValue;
	private Object _expectedIdValue;
	private static HashMap<String, String> _keyCollector;

	public SubstituteBothJsonKeyMatcher(String aJson, String aKey) {
		_expectedJson = aJson;
		_key = aKey;
	}

	@Override
	public boolean matchesSafely(String json) {
		_actualJson = json;
		System.out.println("EXPECTED json: " + _expectedJson);
		System.out.println("ACTUAL json: " + _actualJson);
		putKnownIdToBoth(_actualJson, _expectedJson);
		System.out.println("AFTER REMOVE -> EXPECTED json: " + _expectedJson);
		System.out.println("AFTER REMOVE -> ACTUAL   json: " + _actualJson);
		return _expectedJson.equals(_actualJson);
	}

	private void putKnownIdToBoth(String actualJson, String expectedJson) {
		try {
			JSONArray actualJsonArray = (JSONArray) new JSONParser().parse(actualJson);
			JSONObject actualJsonObject = (JSONObject)actualJsonArray.get(0);
			_actualIdValue = actualJsonObject.get(_key);
			
			JSONArray expectedJsonArray = (JSONArray) new JSONParser().parse(expectedJson);
			JSONObject expectedJsonObject = (JSONObject)expectedJsonArray.get(0);
			_expectedIdValue = expectedJsonObject.get(_key);
			
			Object commonIdValue = _actualIdValue + "-vs-" + _expectedIdValue;
			actualJsonObject.put(_key, commonIdValue );
			expectedJsonObject.put(_key, commonIdValue );
			
			actualJsonArray.clear();
			expectedJsonArray.clear();
			
			actualJsonArray.add(actualJsonObject);
			_actualJson = actualJsonArray.toJSONString();
			expectedJsonArray.add(expectedJsonObject);
			_expectedJson = expectedJsonArray.toJSONString();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void describeTo(Description description) {
		description.appendText("a json without key: " + _key);
	}

	@Factory
	public static <T> Matcher<String> equalToExcludingKey(String json, String aKey) {
		return new SubstituteBothJsonKeyMatcher(json, aKey);
	}
}
