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
