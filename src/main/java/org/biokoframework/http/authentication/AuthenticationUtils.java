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

package org.biokoframework.http.authentication;

import static com.jayway.restassured.RestAssured.given;

import org.biokoframework.system.entity.authentication.Authentication;
import org.biokoframework.system.entity.authentication.AuthenticationBuilder;
import org.biokoframework.system.entity.login.LoginBuilder;
import org.biokoframework.utils.domain.EntityBuilder;

import com.jayway.restassured.http.ContentType;

public class AuthenticationUtils {

	private String _authenticationUrl;

	public AuthenticationUtils(String authenticationUrl) {
		_authenticationUrl = authenticationUrl;
	}
	
	public long postValidToken(String validToken) {
		long expire = System.currentTimeMillis() / 1000 + 300; // Expire after NOW
		return postToken(validToken, expire);
	}
	
	public long postToken(String token, long expire) {
		EntityBuilder<Authentication> authenticationBuilder = 
				new AuthenticationBuilder().loadDefaultExample();
		authenticationBuilder.set(Authentication.TOKEN, token);
		authenticationBuilder.set(Authentication.TOKEN_EXPIRE, Long.toString(expire));
		
		given().
		contentType(ContentType.JSON).
		body(
				authenticationBuilder.build(false).toJSONString()
		).
		post(_authenticationUrl);
		return expire;
	}
	
	public static String postNewLoginAndDoCheckIn(LoginBuilder loginBuilder, String _loginUrl, String _checkinUrl) {
		String loginId = given().
		contentType(ContentType.JSON).
		body( 
				loginBuilder.build(false).toJSONString()
		).
		post(_loginUrl).
		jsonPath().getString("id[0]");
		
		loginBuilder.setId(loginId);
		
		String token = given().
		body(
				loginBuilder.build(false).toJSONString()
		).
		post(_checkinUrl).
		jsonPath().getString("authToken[0]");
		
		return token;
	}
}
