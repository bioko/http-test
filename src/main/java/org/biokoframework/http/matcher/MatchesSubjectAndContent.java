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

import java.util.Arrays;

import javax.mail.Message;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class MatchesSubjectAndContent extends TypeSafeMatcher<Message> {

	private Matcher<String> _subjectMatcher;
	private Matcher<String> _contentMatcher;

	public MatchesSubjectAndContent(Matcher<String> subjectMatcher, Matcher<String> contentMatcher) {
		_subjectMatcher = subjectMatcher;
		_contentMatcher = contentMatcher;
	}

	@Override
	public void describeTo(Description description) {
		description.appendList("(", " " + "and" + " ", ")", 
				Arrays.asList(_subjectMatcher, _contentMatcher));
	}
	
	@Override
	protected void describeMismatchSafely(Message item, Description mismatchDescription) {
		try {
			mismatchDescription.appendText("was ").appendValueList("(", " " + "and"+ " ", ")", Arrays.asList(item.getSubject(), item.getContent()));
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	@Override
	protected boolean matchesSafely(Message item) {

		try {
			if (!_subjectMatcher.matches(item.getSubject())) {
				return false;
			} else if (!_contentMatcher.matches(item.getContent())) {
				return false;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
				
		return true;
	}

	@Factory
	public static Matcher<Message> matchesSubjectAndContent(Matcher<String> subjectMatcher, Matcher<String> contentMatcher) {
		return new MatchesSubjectAndContent(subjectMatcher, contentMatcher);
	}
	
	@Factory
	public static Matcher<Message> matchesSubjectAndContent(String expectedSubject, String expectedContent) {
		return matchesSubjectAndContent(equalTo(expectedSubject), equalTo(expectedContent));
	}
}
