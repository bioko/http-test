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

package org.biokoframework.http.scenario.mail;

import org.biokoframework.http.scenario.ExecutionScenarioStep;
import org.biokoframework.system.entity.template.Template;
import org.hamcrest.Matcher;
import org.jvnet.mock_javamail.Mailbox;

import javax.mail.Message;
import javax.mail.internet.AddressException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import static org.biokoframework.http.matcher.MatchesSubjectAndContent.matchesSubjectAndContent;
import static org.junit.Assert.assertThat;

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

    public static MailScenarioStep mailReceivedBy(String address, Matcher<Message> emailMatcher) {
        return new MailScenarioStep(address, emailMatcher);
    }

    public static ExecutionScenarioStep mailBox(final String address, final Matcher<Mailbox> mailboxMatcher) {
        return new ExecutionScenarioStep() {
            @Override
            public void execute() throws AddressException {
                Mailbox box = Mailbox.get(address);
                assertThat(box, mailboxMatcher);
            }
        };
    }

}
