package it.bioko.http.facebook;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class FacebookUtils {

	// This method retrieves Facebook test users for the app given
	// see https://developers.facebook.com/docs/test_users/
	public static HashMap<String, String> retrieveTestUsersAccesTokens(String appID, String appAccessToken) 
			throws URISyntaxException, IOException, ClientProtocolException {
		
		HttpClient httpClient = new DefaultHttpClient();
		URIBuilder builder = new URIBuilder().setScheme("https").setHost("graph.facebook.com").
				setPath("/" + appID + "/accounts/test-users").
				setParameter("access_token", appAccessToken);
		
		HttpGet httpGet = new HttpGet(builder.build());
		ResponseHandler<JSONObject> handler = new JSONResponseHandler();
		
		JSONObject responseBody = httpClient.execute(httpGet, handler);
		
		HashMap<String, String> tokenMap = new HashMap<String, String>();
	
		JSONArray data = (JSONArray) responseBody.get("data");
		for (Object anEntry : data) {
			JSONObject aTestUser = (JSONObject) anEntry;
			tokenMap.put((String) aTestUser.get("id"), (String) aTestUser.get("access_token"));
		}
		return tokenMap;
	}
	
}
