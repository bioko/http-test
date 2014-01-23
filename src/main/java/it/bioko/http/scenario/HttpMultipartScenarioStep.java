package it.bioko.http.scenario;

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
