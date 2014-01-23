package it.bioko.http.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class Exists<T> extends BaseMatcher<T> {

	@Override
	public boolean matches(Object actualValue) {
		return actualValue != null;
	}

	@Override
	public void describeTo(Description description) {
		description.appendValue("exists");
	}
	
	public static <T> Matcher<T> exists() {
		return new Exists<T>();
	}

}
