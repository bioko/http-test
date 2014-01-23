package it.bioko.http.scenario.mail;

import it.bioko.http.scenario.ScenarioStep;

import javax.mail.Message;

import org.hamcrest.Matcher;

public final class MailScenarioStep implements ScenarioStep {

	public String _mailBoxAddress;
	public Matcher<Message> _messageMatcher;
	
	public MailScenarioStep(String mailBoxAddress, Matcher<Message> messageMatcher) {
		_mailBoxAddress = mailBoxAddress;
		_messageMatcher = messageMatcher;
	}
	
}
