package it.bioko.http.scenario.mail;

import static it.bioko.http.matcher.MatchesSubjectAndContent.matchesSubjectAndContent;
import it.bioko.system.entity.template.Template;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class MailScenarioFactory {

	public static MailScenarioStep createFromTemplateAndMap(String address, Template template, Map<String, String> fillMap) {
		
		String title = template.get(Template.TITLE);
		String html = template.get(Template.BODY);
		
		for (Entry<String, String> entry : fillMap.entrySet()) {
			title = title.replaceAll(Pattern.quote(entry.getKey()), entry.getValue());
			html = html.replace(entry.getKey(), entry.getValue());
		}
		
		return new MailScenarioStep(address, matchesSubjectAndContent(title, html));
	}

}
