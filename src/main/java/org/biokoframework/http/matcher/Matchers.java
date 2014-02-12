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

import static org.hamcrest.Matchers.equalTo;

import java.util.Map;

import javax.mail.Message;

import org.hamcrest.Matcher;
import org.jvnet.mock_javamail.Mailbox;

public class Matchers {
	
	public static Matcher<String> matchesAuthenticationResponse(Map<String, String> tokenMap) {
		return MatchesAuthenticationResponse.matchesAuthenticationResponse(tokenMap);
	}
	
	public static Matcher<Message> matchesSubjectAndContent(Matcher<String> subjectMatcher, Matcher<String> contentMatcher) {
		return MatchesSubjectAndContent.matchesSubjectAndContent(subjectMatcher, contentMatcher);
	}
	
	public static Matcher<Message> matchesSubjectAndContent(String expectedSubject, String expectedContent) {
		return MatchesSubjectAndContent.matchesSubjectAndContent(equalTo(expectedSubject), equalTo(expectedContent));
	}
	
	public static <T> Matcher<T> exists() {
		return Exists.exists();
	}

	public static Matcher<Mailbox> hasUnreadMessages() {
		return HasUnreadMessages.hasUnreadMessages();
	}
	
}
