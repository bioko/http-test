package it.bioko.http.matcher;

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
	
	public static <T> Matcher<String> equalToExcludingKey(String json, String aKey) {
		return new SubstituteBothJsonKeyMatcher(json, aKey);
	}

	public static <T> Matcher<T> exists() {
		return Exists.exists();
	}

	public static Matcher<Mailbox> hasUnreadMessages() {
		return HasUnreadMessages.hasUnreadMessages();
	}
	
}
