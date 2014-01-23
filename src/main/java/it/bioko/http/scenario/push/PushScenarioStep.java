package it.bioko.http.scenario.push;

import it.bioko.http.scenario.ScenarioStep;

import org.hamcrest.Matcher;

public class PushScenarioStep implements ScenarioStep {

	public String _userToken;
	public Matcher<String> _messageMatcher;

	public PushScenarioStep(String userToken, Matcher<String> messageMatcher) {
		_userToken = userToken;
		_messageMatcher = messageMatcher;
	}

}
