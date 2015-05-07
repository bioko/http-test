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

import static org.biokoframework.utils.matcher.Matchers.matchesPattern;

/**
 * @author Mikol Faro <mikol.faro@gmail.com>
 * @date 2014-07-08
 */
public class CapturingAuthTokenMatcher extends TypeSafeMatcher<String> {


    private static final String EXPECTED_PATTERN = "^([\\da-f\\-]+)$";

    private final Matcher<String> fActualMatcher = matchesPattern(EXPECTED_PATTERN);
    private final Map<String, String> fTokenMap;

    public CapturingAuthTokenMatcher(Map<String, String> tokenMap) {
        fTokenMap = tokenMap;
    }

    @Override
    protected boolean matchesSafely(String actualToken) {
        if (fActualMatcher.matches(actualToken)) {
            if (fTokenMap != null) {
                fTokenMap.put(GenericFieldNames.TOKEN_HEADER, actualToken);
            }
            return true;
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        fActualMatcher.describeTo(description);
    }

    public static Matcher<String> matchesAndCaptureAuthToken(Map<String, String> tokenMap) {
        return new CapturingAuthTokenMatcher(tokenMap);
    }

}
