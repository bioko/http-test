package it.bioko.http.scenario;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matcher;


public final class HttpScenarioStep implements ScenarioStep {

	public String _partialRestURL;
	public String _httpMethod;
	public Map<String, String> _headers;
	public Map<String, String> _parameters;
	public String _requestBodyJson;
	public int _expectedHttpStatusCode;
	public Matcher<String> _expectedJsonBodyMatcher;
	private String _entityKey;
	private HashMap<String, String> _keyCollector;

	public HttpScenarioStep(String restURL, String httpMethod,
			Map<String, String> headers, Map<String, String> parameters,
			String requestBodyJson, int expectedHttpStatusCode, Matcher<String> expectedJsonBodyMatcher) {
				_partialRestURL = restURL;
				_httpMethod = httpMethod;
				
				_headers = (headers == null) ? _headers = new HashMap<String, String>() : headers;
				_parameters = (parameters == null) ? _parameters = new HashMap<String, String>() : parameters;

				_requestBodyJson = requestBodyJson;
				_expectedHttpStatusCode = expectedHttpStatusCode;
				_expectedJsonBodyMatcher = expectedJsonBodyMatcher;
	}
	
	public HttpScenarioStep(String completeRestURL, String httpMethod,
			Map<String, String> headers, Map<String, String> parameters,
			String requestBodyJson, int expectedHttpStatusCode, Matcher<String> expectedJsonBodyMatcher, HashMap<String, String> keyCollector) {
				_partialRestURL = String.format(completeRestURL, keyCollector.get(0));
				_httpMethod = httpMethod;
				_keyCollector = keyCollector;
				
				_headers = (headers == null) ? _headers = new HashMap<String, String>() : headers;
				_parameters = (parameters == null) ? _parameters = new HashMap<String, String>() : parameters;

				_requestBodyJson = requestBodyJson;
				_expectedHttpStatusCode = expectedHttpStatusCode;
				_expectedJsonBodyMatcher = expectedJsonBodyMatcher;
	}

	public void setEntityKey(String entityKey) {
		_entityKey = entityKey;
	}

	public void setBodyMatcher(Matcher<String> aMatcher) {
		_expectedJsonBodyMatcher = aMatcher;
	}
	
	public String entityKey() {
		return _entityKey;
	}

	public Matcher<?> bodyMatcher() {
		return _expectedJsonBodyMatcher ;
	}
}