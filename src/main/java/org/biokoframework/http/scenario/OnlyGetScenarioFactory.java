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

package org.biokoframework.http.scenario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.biokoframework.system.KILL_ME.commons.GenericFieldNames;
import org.biokoframework.system.KILL_ME.commons.HttpMethod;
import org.biokoframework.utils.domain.DomainEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class OnlyGetScenarioFactory {

	public static final String ONLY_GET = "only-get" + "/";
	
	public static List<Object[]> adaptToOnlyGet(List<Object[]> list) throws Exception {
		List<Object[]> onlyGetList = new ArrayList<Object[]>(list.size());
		for (Object[] array : list) {
			onlyGetList.add(new Object[] { array[0] + " " + ONLY_GET, adaptToOnlyGet((Scenario) array[1]) });
		}
		return onlyGetList;
	}
	
	private static Scenario adaptToOnlyGet(Scenario collector) throws Exception {
		Scenario onlyGetCollector = new Scenario(collector.scenarioName());
		for (Entry<String, ScenarioStep> anEntry : collector.scenarioSteps()) {
			ScenarioStep restScenario = anEntry.getValue();
			if (restScenario instanceof HttpScenarioStep) {
				onlyGetCollector.addScenarioStep(anEntry.getKey(), adaptToOnlyGet((HttpScenarioStep)restScenario));
			} else {
				onlyGetCollector.addScenarioStep(anEntry.getKey(), restScenario);
			}
		}
		
		return onlyGetCollector;
	}

	private static HttpScenarioStep adaptToOnlyGet(HttpScenarioStep restScenario) throws Exception {
				
		HashMap<String, String> parameters = new HashMap<String, String>(restScenario.fParameters);
		parameters.putAll(restScenario.fHeaders);
		
		String[] restUrlSplit = restScenario.fPartialRestURL.split("/");
		String entityHyphened;
		if (restUrlSplit[restUrlSplit.length - 1].matches("\\d+")) {
			entityHyphened = (restUrlSplit[restUrlSplit.length - 2]);
			parameters.put(DomainEntity.ID, restUrlSplit[restUrlSplit.length - 1]);
		} else {
			entityHyphened = (restUrlSplit[restUrlSplit.length - 1]);
		}
		
		String command = new StringBuilder(). 
				append(restScenario.fHttpMethod).
				append('_').
				append(entityHyphened).
				toString();
		parameters.put(GenericFieldNames.COMMAND, command);
		
		if (restScenario.fRequestBodyJson != null && !restScenario.fRequestBodyJson.isEmpty()) {
			parameters.putAll(parseBody(restScenario.fRequestBodyJson));
		}

		HttpScenarioStep onlyGetScenario = new HttpScenarioStep(ONLY_GET, HttpMethod.GET.name(), null,
				parameters, null, restScenario.fExpectedHttpStatusCode, restScenario.fExpectedJsonBodyMatcher);
		
		return onlyGetScenario;
	}
	
	private static Map<String, String> parseBody(String requestBodyJson) throws Exception {		
		JSONParser parser = new JSONParser();
		JSONObject object = (JSONObject) parser.parse(requestBodyJson);
		
		return jsonToMap(object); 
	}

	public static Map<String, String> jsonToMap(JSONObject object) {
		Map<String, String> bodyMap = new HashMap<String, String>();
		for (Object aKey : object.keySet()) {
			Object aValue = object.get(aKey);
			if (aValue instanceof String) {
				bodyMap.put((String) aKey,(String) aValue);
			} else if (aValue instanceof JSONObject) {
				for (Entry<String, String> anEntry : jsonToMap((JSONObject) aValue).entrySet()) {
					bodyMap.put(aKey + "." + anEntry.getKey(), anEntry.getValue());
				}
			} else if (aValue instanceof DomainEntity) {
				DomainEntity entity = (DomainEntity) aValue;
				for (String anEntityField : entity.fields().keys()) {
					bodyMap.put(aKey + "." + anEntityField, entity.get(anEntityField).toString());
				}
			} else if (aValue instanceof JSONArray) {
				JSONArray anArray = (JSONArray) aValue;
				if (anArray.get(0) instanceof String) {
					for (int i = 0; i < anArray.size(); i++) {
						bodyMap.put(aKey + "[" + i + "]", (String) anArray.get(i));
					}
				} else if (anArray.get(0) instanceof JSONObject) {
					for (int i = 0; i < anArray.size(); i++) {
						for (Entry<String, String> anEntry : jsonToMap((JSONObject) anArray.get(i)).entrySet()) {
							bodyMap.put(aKey + "[" + i + "]." + anEntry.getKey(), anEntry.getValue());
						}
					}
				}
			}
		}
		return bodyMap;
	}
	
}
