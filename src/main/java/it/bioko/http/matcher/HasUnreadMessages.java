package it.bioko.http.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.jvnet.mock_javamail.Mailbox;

public class HasUnreadMessages extends TypeSafeMatcher<Mailbox>{

	@Override
	public void describeTo(Description description) {
		description.appendText("a mailbox with unread messages");
	}

	@Override
	protected boolean matchesSafely(Mailbox item) {
		return item.getNewMessageCount() > 0;
	}
	
	@Override
	protected void describeMismatchSafely(Mailbox item, Description mismatchDescription) {
		mismatchDescription.appendText(item.getAddress().toString() + " had not");
	}

	public static Matcher<Mailbox> hasUnreadMessages() {
		return new HasUnreadMessages();
	}
}

