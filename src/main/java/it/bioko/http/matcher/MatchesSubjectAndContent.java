package it.bioko.http.matcher;

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

	@SuppressWarnings("unchecked")
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
