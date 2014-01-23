package it.bioko.http.matcher;

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
