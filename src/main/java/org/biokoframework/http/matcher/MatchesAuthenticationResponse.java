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

package org.biokoframework.http.matcher;

import org.biokoframework.system.KILL_ME.commons.GenericFieldNames;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Map;
import java.util.regex.Pattern;

import static org.biokoframework.utils.matcher.Matchers.matchesPattern;
import static org.biokoframework.utils.matcher.Matchers.substringMatchesPattern;

public class MatchesAuthenticationResponse extends TypeSafeMatcher<String> {

	private static final String EXPECTED_TOKEN_PATTERN = "\"authToken\":\"([\\da-f\\-]+)\"";
	private static final String EXPECTED_TOKEN_EXPIRATION_PATTERN = "\"authTokenExpire\":\"\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:Z|(?:\\+|-)\\d{2}:\\d{2})\",";
	private static final String EXPECTED_ROLES_PATTERN = "(?:\"roles\":\"[a-z|]+?\",)?";

	private final Map<String, String> fTokenMap;

	private Matcher<String> fTokenMatcher = substringMatchesPattern(EXPECTED_TOKEN_PATTERN);
	private Matcher<String> fExpirationMatcher = substringMatchesPattern(EXPECTED_TOKEN_EXPIRATION_PATTERN);
	private Matcher<String> fRolesMatcher = substringMatchesPattern(EXPECTED_ROLES_PATTERN);

	public MatchesAuthenticationResponse(Map<String, String> tokenMap) {
		fTokenMap = tokenMap;
	}

	@Override
	public void describeTo(Description description) {
		fTokenMatcher.describeTo(description);
	}

	@Override
	protected boolean matchesSafely(String item) {
		if (fTokenMatcher.matches(item) && fExpirationMatcher.matches(item) && fRolesMatcher.matches(item)) {
            if (fTokenMap != null) {
                java.util.regex.Matcher patternMatcher = Pattern.compile(EXPECTED_TOKEN_PATTERN).matcher(item);
                patternMatcher.find();
                fTokenMap.put(GenericFieldNames.TOKEN_HEADER, patternMatcher.group(1));
            }
			return true;
		}
		return false;
	}

	public static Matcher<String> matchesAuthenticationResponse(Map<String, String> tokenMap) {
		return new MatchesAuthenticationResponse(tokenMap);
	}

    public static Matcher<String> matchesAuthenticationResponse() {
        return new MatchesAuthenticationResponse(null);
    }

}
