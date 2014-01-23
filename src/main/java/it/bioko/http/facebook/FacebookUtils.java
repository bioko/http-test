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
