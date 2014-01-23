package it.bioko.http.matcher;

import static it.bioko.utils.matcher.Matchers.matchesPattern;
import it.bioko.system.KILL_ME.commons.GenericFieldNames;

import java.util.Map;
import java.util.regex.Pattern;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class MatchesAuthenticationResponse extends TypeSafeMatcher<String> {

	private static final String EXPECTED_RESPONSE_PATTERN = "^\\[\\{\"authTokenExpire\":\"\\d+\",(\"roles\":\"[a-z]+?\",)?\"authToken\":\"([\\da-f\\-]+)\"\\}\\]$";
	
	private Matcher<String> _actualMatcher = matchesPattern(EXPECTED_RESPONSE_PATTERN);
	private final Map<String, String> _tokenMap;

	
	public MatchesAuthenticationResponse(Map<String, String> tokenMap) {
		_tokenMap = tokenMap;
	}

	@Override
	public void describeTo(Description description) {
		_actualMatcher.describeTo(description);
	}

	@Override
	protected boolean matchesSafely(String item) {
		if (_actualMatcher.matches(item)) {
			java.util.regex.Matcher patternMatcher = Pattern.compile(EXPECTED_RESPONSE_PATTERN).matcher(item);
			patternMatcher.find();
			if (patternMatcher.groupCount() == 2) {
				_tokenMap.put(GenericFieldNames.TOKEN_HEADER, patternMatcher.group(2));				
			} else {
				_tokenMap.put(GenericFieldNames.TOKEN_HEADER, patternMatcher.group(1));
			}
			return true;
		}
		return false;
	}

	public static Matcher<String> matchesAuthenticationResponse(Map<String, String> tokenMap) {
		return new MatchesAuthenticationResponse(tokenMap);
	}

}
