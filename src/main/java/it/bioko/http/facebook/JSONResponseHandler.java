package it.bioko.http.facebook;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class JSONResponseHandler implements ResponseHandler<JSONObject> {
	ResponseHandler<String> _delegatedHandler = new BasicResponseHandler();

	@Override
	public JSONObject handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		return (JSONObject) JSONValue.parse(_delegatedHandler.handleResponse(response));
	}
}