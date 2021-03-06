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

package org.biokoframework.http.scenario.parametrized;

import org.biokoframework.http.rest.exception.HttpError;
import org.biokoframework.http.scenario.HttpScenarioFactory;
import org.biokoframework.http.scenario.JSonExpectedResponseBuilder;
import org.biokoframework.http.scenario.JSonRequestFactory;
import org.biokoframework.http.scenario.Scenario;
import org.biokoframework.system.entity.EntityClassNameTranslator;
import org.biokoframework.utils.domain.DomainEntity;
import org.biokoframework.utils.domain.EntityBuilder;
import org.hamcrest.Matchers;
import org.json.simple.JSONValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.biokoframework.utils.matcher.Matchers.anyString;

public class CrudScenariosParametrizedFactory {

	public static <T extends DomainEntity> List<Scenario> createFrom(Class<T> anEntityClass, Class<? extends EntityBuilder<T>> anEntityBuilderClass, Map<String, Object> anUpdateEntityMap, String startingId, String queryStringFieldsArray[]) throws Exception {
		JSonRequestFactory<T> entityRequest = new JSonRequestFactory<T>().createEntity(anEntityClass);
		String newEntityRequest = entityRequest.buildNewEntityAsJSON();
		HttpError entityNotFound = JSonExpectedResponseBuilder.entityNotFound(anEntityClass, anEntityBuilderClass, startingId);
		HttpError entityNotComplete = JSonExpectedResponseBuilder.entityNotComplete(anEntityClass, anEntityBuilderClass);

		HttpScenarioFactory<T> scenarioFactory = new HttpScenarioFactory<T>(startingId);
		String entityName = anEntityClass.getSimpleName();

		EntityBuilder<T> entityBuilder = anEntityBuilderClass.newInstance();

		HashMap<String, String> queryString = null;
		if (queryStringFieldsArray != null) {
			queryString = new HashMap<>();
			for (String qsField : queryStringFieldsArray) {
				String qsValue = entityBuilder.loadDefaultExample().get(qsField);
				queryString.put(qsField, qsValue);
			}
		}

		return Arrays.<Scenario>asList(
				scenarioFactory.scenarioAsCollector(anEntityClass, HttpScenarioFactory.postSuccessful(EntityClassNameTranslator.toHyphened(entityName)
				        + "/", null, null, newEntityRequest, Matchers.equalTo(JSonExpectedResponseBuilder.existingEntity(anEntityClass, startingId)))),

                scenarioFactory.readScenarioCollector(anEntityClass, queryString),

				scenarioFactory.updateScenarioCollector(anEntityClass, anUpdateEntityMap, queryString),

                scenarioFactory.deleteScenarioCollector(anEntityClass, anEntityBuilderClass, queryString),

				scenarioFactory.scenarioAsCollector(anEntityClass,
								HttpScenarioFactory.getFailed(composeEntitySpecificURL(anEntityClass, startingId), null, queryString, null,
										entityNotFound.status(), Matchers.equalTo(JSONValue.toJSONString(entityNotFound.body())))),

				scenarioFactory.scenarioAsCollector(anEntityClass, HttpScenarioFactory.putFailed(composeEntitySpecificURL(anEntityClass, startingId),
								null, null, "", entityNotFound.status(), anyString())),

				scenarioFactory.scenarioAsCollector(anEntityClass,
								HttpScenarioFactory.deleteFailed(composeEntitySpecificURL(anEntityClass, startingId), null, queryString, null,
										entityNotFound.status(), Matchers.equalTo(JSONValue.toJSONString(entityNotFound.body())))),

				// TODO casi con piu' di una entità ...
                scenarioFactory.readAllScenarioCollector(anEntityClass, anUpdateEntityMap, queryString),
				// TODO post failure se esiste già una entità con gli stessi
				// mandatory fields
				// {entityName + " create - POST - already exixting - failed",
				// scenarioFactory.createAlreadyExisting(anEntityClass)},
				// TODO post failure se ha un id
				// TODO post failure se non ci sono tutti i mandatory fields
				// TODO update failure ??? che tipo di put puo' fallire

				scenarioFactory.readAllFilteredScenarioCollector(anEntityClass, anUpdateEntityMap, queryString));
	}

	public static <T extends DomainEntity> List<Scenario> createFrom(Class<T> anEntityClass, Class<? extends EntityBuilder<T>> anEntityBuilderClass, Map<String, Object> anUpdateEntityMap, String startingId) throws Exception {
		return createFrom(anEntityClass, anEntityBuilderClass, anUpdateEntityMap, startingId, null);
	}

	private static <T> String composeEntitySpecificURL(Class<T> anEntityClass, String startingId) {
		return EntityClassNameTranslator.toHyphened(anEntityClass.getSimpleName()) + "/" + startingId;
	}
}
