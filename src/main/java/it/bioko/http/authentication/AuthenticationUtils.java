package it.bioko.http.authentication;

import static com.jayway.restassured.RestAssured.given;
import it.bioko.system.entity.authentication.Authentication;
import it.bioko.system.entity.authentication.AuthenticationBuilder;
import it.bioko.system.entity.login.LoginBuilder;
import it.bioko.utils.domain.EntityBuilder;

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
