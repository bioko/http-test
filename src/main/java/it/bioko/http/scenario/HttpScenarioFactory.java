package it.bioko.http.scenario;

import it.bioko.http.HttpMethodEnum;
import it.bioko.http.matcher.SubstituteBothJsonKeyMatcher;
import it.bioko.http.rest.exception.HttpError;
import it.bioko.system.entity.EntityClassNameTranslator;
import it.bioko.utils.domain.DomainEntity;
import it.bioko.utils.domain.EntityBuilder;
import it.bioko.utils.domain.reflection.DomainEntityReflection;
import it.bioko.utils.fields.Fields;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.json.simple.JSONValue;

public class HttpScenarioFactory<T extends DomainEntity> {

	private String _startingId;

	public HttpScenarioFactory(String startingId) {
		_startingId = startingId;
	}

	private String incrementStartingId() {
		return addToStartingId(1);
	}
	
	private String addToStartingId(int value) {
		Integer.valueOf(_startingId);
		return String.valueOf(Integer.valueOf(_startingId) + value);
	}
	
	public static HttpScenarioStep postSuccessful(String restUrl, Map<String, String> headers, Map<String, String> parameters, String inputBodyJson, Matcher<String> expectedJsonBodyMatcher) throws Exception {
		String httpMethod = HttpMethodEnum.POST.name();
		return successfulHttpMethodInvocation(restUrl, headers, parameters, inputBodyJson, expectedJsonBodyMatcher, httpMethod);
	}

	public static HttpScenarioStep postFailed(String restUrl, Map<String, String> headers, Map<String, String> queryString, String inputBodyJson, int expectedHttpStatus, Matcher<String> expectedBodyJson) {
		String httpMethod = HttpMethodEnum.POST.name();
		return failedHttpMethodInvocation(restUrl, headers, queryString, inputBodyJson, expectedHttpStatus, expectedBodyJson, httpMethod);
	}

	public static HttpScenarioStep getSuccessful(String restUrl, Map<String, String> headers, Map<String, String> queryString, String inputBodyJson, Matcher<String> expectedJsonBodyMatcher) throws Exception {
		String httpMethod = HttpMethodEnum.GET.name();
		return successfulHttpMethodInvocation(restUrl, headers, queryString, inputBodyJson, expectedJsonBodyMatcher, httpMethod);
	}
	
	public static HttpScenarioStep getSuccessful(String restUrl, Map<String, String> headers, Map<String, String> queryString, String inputBodyJson, Matcher<String> expectedJsonBodyMatcher, HashMap<String, String> keyCollector) throws Exception {
		String httpMethod = HttpMethodEnum.GET.name();
		return successfulHttpMethodInvocation(restUrl, headers, queryString, inputBodyJson, expectedJsonBodyMatcher, httpMethod, keyCollector);
	}
	
	public static HttpScenarioStep getEmpty(String restUrl, Map<String, String> headers, Map<String, String> queryString, String inputBodyJson, Matcher<String> expectedJsonBodyMatcher) throws Exception {
		String httpMethod = HttpMethodEnum.GET.name();
		return successfulHttpMethodInvocation(restUrl, headers, queryString, inputBodyJson, expectedJsonBodyMatcher, httpMethod);
	}

	public static HttpScenarioStep getFailed(String restUrl, Map<String, String> headers, Map<String, String> queryString, String inputBodyJson, int expectedHttpStatus, Matcher<String> expectedJsonBodyMatcher) {
		String httpMethod = HttpMethodEnum.GET.name();
		return failedHttpMethodInvocation(restUrl, headers, queryString, inputBodyJson, expectedHttpStatus, expectedJsonBodyMatcher, httpMethod);
	}
	
	public static HttpScenarioStep putSuccessful(String restUrl, Map<String, String> headers, Map<String, String> queryString, String inputBodyJson, Matcher<String> expectedJsonBodyMatcher) {
		String httpMethod = HttpMethodEnum.PUT.name();
		return successfulHttpMethodInvocation(restUrl, headers, queryString, inputBodyJson, expectedJsonBodyMatcher, httpMethod);
	}
	
	public static HttpScenarioStep putFailed(String restUrl, Map<String, String> headers, Map<String, String> queryString, String inputBodyJson, int expectedHttpStatus, Matcher<String> expectedJsonBodyMatcher) {
		String httpMethod = HttpMethodEnum.PUT.name();
		return failedHttpMethodInvocation(restUrl, headers, queryString, inputBodyJson, expectedHttpStatus, expectedJsonBodyMatcher, httpMethod);
	}

	public static HttpScenarioStep deleteFailed(String restUrl, Map<String, String> headers, Map<String, String> queryString, String inputBodyJson, int expectedHttpStatus, Matcher<String> expectedJsonBodyMatcher) {
		String httpMethod = HttpMethodEnum.DELETE.name();
		return failedHttpMethodInvocation(restUrl, headers, queryString, inputBodyJson, expectedHttpStatus, expectedJsonBodyMatcher, httpMethod);
	}
	
	public static HttpScenarioStep deleteSuccessful(String restUrl, Map<String, String> headers, Map<String, String> queryString, String inputBodyJson, Matcher<String> expectedJsonBodyMatcher) {
		String httpMethod = HttpMethodEnum.DELETE.name();
		return successfulHttpMethodInvocation(restUrl, headers, queryString, inputBodyJson, expectedJsonBodyMatcher, httpMethod);
	}
	
	public static ScenarioStep optionsSuccessful(String restUrl, Map<String, String> headers, Map<String, String> queryString, String inputBodyJson, Matcher<String> expectedJsonBodyMatcher) {
		String httpMethod = HttpMethodEnum.OPTIONS.name();
		return successfulHttpMethodInvocation(restUrl, headers, queryString, inputBodyJson, expectedJsonBodyMatcher, httpMethod);
	}

	private static HttpScenarioStep successfulHttpMethodInvocation(String restUrl, Map<String, String> headers, Map<String, String> queryString, String inputBodyJson, Matcher<String> expectedJsonBodyMatcher, String httpMethod) {
		int expectedHttpStatusCode = 200;
		return new HttpScenarioStep(restUrl, httpMethod, headers, queryString, inputBodyJson, expectedHttpStatusCode, expectedJsonBodyMatcher);
	}
	
	private static HttpScenarioStep successfulHttpMethodInvocation(String restUrl, Map<String, String> headers, Map<String, String> queryString, String inputBodyJson, Matcher<String> expectedJsonBodyMatcher, String httpMethod, HashMap<String, String> keyCollector) {
		int expectedHttpStatusCode = 200;
		return new HttpScenarioStep(restUrl, httpMethod, headers, queryString, inputBodyJson, expectedHttpStatusCode, expectedJsonBodyMatcher, keyCollector);
	}

	private static HttpScenarioStep failedHttpMethodInvocation(String restUrl, Map<String, String> headers, Map<String, String> queryString, String inputBodyJson, int expectedHttpStatusCode, Matcher<String> expectedJsonBodyMatcher, String httpMethod) {
		return new HttpScenarioStep(restUrl, httpMethod, headers, queryString, inputBodyJson, expectedHttpStatusCode, expectedJsonBodyMatcher);
	}
	
	// TODO separate HttpScenarioFactory from HttpScenarioCollectorFactory
	
	public  Scenario readScenarioCollector(Class<T> entityClass, HashMap<String, String> queryString) throws Exception {
		String entityName = EntityClassNameTranslator.toHyphened(entityClass.getSimpleName());
		String restURL = entityName + "/";
		Scenario scenarioCollector = new Scenario("Read " + entityName + " (POST, GET) successful");
		
		JSonRequestFactory<T> jsonRequestFactory = new JSonRequestFactory<T>().createEntity(entityClass);
		String newEntityRequest = jsonRequestFactory.buildNewEntityAsJSON();
		
		scenarioCollector.addScenarioStep("post " + entityName, postSuccessful(restURL, null, null, newEntityRequest, Matchers.equalTo(JSonExpectedResponseBuilder.existingEntity(entityClass, _startingId))));
		
		scenarioCollector.addScenarioStep("get " + entityName, getSuccessful(restURL + _startingId, null, queryString, null, Matchers.equalTo(JSonExpectedResponseBuilder.existingEntity(entityClass, _startingId))));
		return scenarioCollector;
	}

	public Scenario readAllScenarioCollector(Class<T> entityClass, Map<String, String> anUpdateEntityParamMap, HashMap<String, String> queryString) throws Exception {
		String entityName = EntityClassNameTranslator.toHyphened(entityClass.getSimpleName());
		String restURL = entityName + "/";
		Scenario scenarioCollector = new Scenario("Read " + entityName + " (POST(1), POST(2), GET) successful");
		
		JSonRequestFactory<T> jsonRequestFactory = new JSonRequestFactory<T>().createEntity(entityClass);
		String newEntityRequest = jsonRequestFactory.buildNewEntityAsJSON();
		
		scenarioCollector.addScenarioStep("first post " + entityName, postSuccessful(restURL, null, null, newEntityRequest, Matchers.equalTo(JSonExpectedResponseBuilder.existingEntity(entityClass, _startingId))));
		scenarioCollector.addScenarioStep("first get " + entityName, getSuccessful(restURL + _startingId, null, queryString, null, Matchers.equalTo(JSonExpectedResponseBuilder.existingEntity(entityClass, _startingId))));
		
		String secondId = incrementStartingId();
		JSonRequestFactory<T> updateRequestFactory = updateExistingEntity(anUpdateEntityParamMap, new JSonRequestFactory<T>().createEntity(entityClass));
		String secondRequest = updateRequestFactory.buildNewEntityAsJSON();
		updateRequestFactory.setId(secondId);
		String expectedSecondResponse = updateRequestFactory.buildExistingEntityAsJSON();
		
		scenarioCollector.addScenarioStep("second post " + entityName, postSuccessful(restURL, null, null, secondRequest, Matchers.equalTo(JSonExpectedResponseBuilder.asArray(expectedSecondResponse))));
		scenarioCollector.addScenarioStep("second get " + entityName, getSuccessful(restURL + secondId, null, queryString, null, Matchers.equalTo(JSonExpectedResponseBuilder.asArray(expectedSecondResponse))));
//		
		scenarioCollector.addScenarioStep("get all " + entityName, getSuccessful(restURL, null, queryString, null, Matchers.equalTo(new JSonExpectedResponseBuilder<T>().addExistingEntity(entityClass, _startingId).addExistingEntity(updateRequestFactory.buildExistingEntity()).build())));

		return scenarioCollector;
	}
	
	public Scenario scenarioAsCollector(Class<T> entityClass, HttpScenarioStep aScenario) throws Exception {
		Scenario scenarioCollector = new Scenario(aScenario._httpMethod + " Single Collection On " + entityClass.getSimpleName() + " " );
		scenarioCollector.addScenarioStep("singleScenario", aScenario);
		return scenarioCollector;
	}

	public Object updateScenarioCollector(Class<T> entityClass, Map<String, String> anUpdateEntityParamMap, HashMap<String, String> queryString) throws Exception {
		String entityName = EntityClassNameTranslator.toHyphened(entityClass.getSimpleName());
		String restURL = entityName + "/";
		Scenario scenarioCollector = new Scenario("Update " + entityName + " (POST, PUT, GET) successful");
		
		// TODO potrei passarle da sopra????
		JSonRequestFactory<T> jsonRequestFactory = new JSonRequestFactory<T>().createEntity(entityClass);
		String newContactRequest = jsonRequestFactory.buildNewEntityAsJSON();
		scenarioCollector.addScenarioStep("post " + entityName, postSuccessful(restURL, null, null, newContactRequest, Matchers.equalTo(JSonExpectedResponseBuilder.existingEntity(entityClass, _startingId))));
		
		JSonRequestFactory<T> updateRequestFactory = updateExistingEntity(anUpdateEntityParamMap, jsonRequestFactory);
		updateRequestFactory.setId(_startingId);
		String updateContactRequest = updateRequestFactory.buildExistingEntityAsJSON();
		
		scenarioCollector.addScenarioStep("put " + entityName, putSuccessful(restURL + _startingId, null, null, updateContactRequest, Matchers.equalTo(JSonExpectedResponseBuilder.asArray(updateContactRequest))));
		scenarioCollector.addScenarioStep("get " + entityName, getSuccessful(restURL + _startingId, null, queryString, null, Matchers.equalTo(JSonExpectedResponseBuilder.asArray(updateContactRequest))));
		return scenarioCollector;
	}

	private JSonRequestFactory<T> updateExistingEntity(Map<String, String> anUpdateEntityParamMap, JSonRequestFactory<T> jsonRequestFactory) throws Exception {
		for (Entry<String, String> eachParam : anUpdateEntityParamMap.entrySet()) {
			jsonRequestFactory.setEntityParam(eachParam.getKey(), eachParam.getValue());			
		}
		return jsonRequestFactory;
	}

	public Object deleteScenarioCollector(Class<T> entityClass, Class<? extends EntityBuilder<T>> entityBuilderClass,  HashMap<String, String> queryString) throws Exception {
		String entityName = EntityClassNameTranslator.toHyphened(entityClass.getSimpleName());
		String restURL = entityName + "/";
		Scenario scenarioCollector = new Scenario("Delete " + entityName + " (POST, DELETE, GET) successful");
		
		JSonRequestFactory<T> jsonRequestFactory = new JSonRequestFactory<T>().createEntity(entityClass);
		String newContactRequest = jsonRequestFactory.buildNewEntityAsJSON();
		String existingEntity = JSonExpectedResponseBuilder.existingEntity(entityClass, _startingId);
		HttpError entityNotFound = JSonExpectedResponseBuilder.entityNotFound(entityClass, entityBuilderClass, _startingId);
		
		scenarioCollector.addScenarioStep("post " + entityName, postSuccessful(restURL, null, null, newContactRequest, Matchers.equalTo(existingEntity)));
		scenarioCollector.addScenarioStep("delete " + entityName, deleteSuccessful(restURL + _startingId, null, queryString, null, Matchers.equalTo(existingEntity)));
		scenarioCollector.addScenarioStep("get " + entityName, getFailed(restURL + _startingId, null, queryString, null, entityNotFound.status(), Matchers.equalTo(JSONValue.toJSONString(entityNotFound.body()))));
		return scenarioCollector;
	}
	
	public Scenario createAlreadyExisting(Class<T> entityClass) throws Exception {
		String entityName = EntityClassNameTranslator.toHyphened(entityClass.getSimpleName());
		String restURL = entityName + "/";
		Scenario scenarioCollector = new Scenario("Read " + entityName + " (POST(1), POST(2)) failed");
		
		JSonRequestFactory<T> jsonRequestFactory = new JSonRequestFactory<T>().createEntity(entityClass);
		String newEntityRequest = jsonRequestFactory.buildNewEntityAsJSON();
		HttpError alreadyExisting = JSonExpectedResponseBuilder.entityAlreadyExisting(entityClass, new Fields().fromJson(newEntityRequest));
		
		scenarioCollector.addScenarioStep("first post " + entityName, postSuccessful(restURL, null, null, newEntityRequest, Matchers.equalTo(JSonExpectedResponseBuilder.existingEntity(entityClass, _startingId))));
		scenarioCollector.addScenarioStep("second post " + entityName, postFailed(restURL, null, null, newEntityRequest, alreadyExisting.status(), Matchers.equalTo(JSONValue.toJSONString(alreadyExisting.body()))));
		return scenarioCollector;
	}
	
	public static Matcher<String> substituteBothJsonKeyMatcher(Class<? extends DomainEntity> anEntityClass, String startingId) {
		String keyName = DomainEntityReflection.keyNameInvocation((anEntityClass));
		Matcher<String> bodyExcludingIdMatcher = SubstituteBothJsonKeyMatcher.equalToExcludingKey(JSonExpectedResponseBuilder.existingEntity(anEntityClass, startingId), keyName);
		return bodyExcludingIdMatcher;
	}
	
	public Scenario readAllFilteredScenarioCollector(Class<T> entityClass, Map<String, String> anUpdateEntityParamMap, HashMap<String, String> queryString) throws Exception {
		String entityName = EntityClassNameTranslator.toHyphened(entityClass.getSimpleName());
		String restURL = entityName + "/";
		
		// get first field of updateMap
		String changedFieldName = anUpdateEntityParamMap.keySet().iterator().next();
		JSonRequestFactory<T> jsonRequestFactory = new JSonRequestFactory<T>().createEntity(entityClass);
		
		Scenario scenarioCollector = new Scenario("Filtered get " + entityName + " (POST(1a), POST(1b), POST(2), GET(1), GET(2)) successful");
		
		String entity1aId = _startingId;		
		jsonRequestFactory.setId(entity1aId);
		T entity1aExisting = jsonRequestFactory.buildExistingEntity();
		String originalValue = entity1aExisting.get(changedFieldName);
		
		
		scenarioCollector.addScenarioStep("post 1a " + entityName, postSuccessful(restURL, null, null, 
				jsonRequestFactory.buildNewEntityAsJSON(), Matchers.equalTo("["+jsonRequestFactory.buildExistingEntityAsJSON()+"]")));
		String entity1bId = addToStartingId(1);
		jsonRequestFactory.setId(entity1bId);
		T entity1bExisting = jsonRequestFactory.buildExistingEntity();
		scenarioCollector.addScenarioStep("post 1b " + entityName, postSuccessful(restURL, null, null, 
				jsonRequestFactory.buildNewEntityAsJSON(), Matchers.equalTo("["+jsonRequestFactory.buildExistingEntityAsJSON()+"]")));
		
		String entity2Id = addToStartingId(2);
		String newValue = anUpdateEntityParamMap.get(changedFieldName);
		jsonRequestFactory.setEntityParam(changedFieldName, newValue);
		jsonRequestFactory.setId(entity2Id);
		T enity2Existing = jsonRequestFactory.buildExistingEntity();
		
		
		scenarioCollector.addScenarioStep("post 2 " + entityName, postSuccessful(restURL, null, null, 
				jsonRequestFactory.buildNewEntityAsJSON(), Matchers.equalTo("["+jsonRequestFactory.buildExistingEntityAsJSON()+"]")));
		
		
		scenarioCollector.addScenarioStep("get all " + entityName, getSuccessful(restURL, null, queryString, null, 
				Matchers.equalTo(
						new JSonExpectedResponseBuilder<T>().addExistingEntity(entity1aExisting)			// 1a
								.addExistingEntity(entity1bExisting)											// 1b
								.addExistingEntity(enity2Existing)						// 2
								.build()
					)));
		
// TODO abbiamo commentato perchè non funziona il filtraggio su più parametri richiesto da questo test quando c'è sia 
//      la queryString esterna che quella del test
//		HashMap<String, String> queryString1 = new HashMap<String, String>();
//
//
//		queryString1.put(changedFieldName, originalValue);
//		if (queryString!=null) 
//			queryString1.putAll(queryString);
//		
//		scenarioCollector.addScenarioStep("get only 1a and 1b " + entityName, getSuccessful(restURL, null, queryString1, null, 
//				Matchers.equalTo(
//						new JSonExpectedResponseBuilder<T>().addExistingEntity(entity1aExisting)			// 1a
//						.addExistingEntity(entity1bExisting)											// 1b						
//						.build()
//	 				)));
//
//		
//		// queryString.remove(changedFieldName);
//		HashMap<String, String> queryString2 = new HashMap<String, String>();
//		if (queryString!=null)
//			queryString2.putAll(queryString);
//		
//		
//		queryString2.put(changedFieldName, newValue);		
//		scenarioCollector.addScenarioStep("get only 2 " + entityName, getSuccessful(restURL, null, queryString2, null, 
//				Matchers.equalTo( new JSonExpectedResponseBuilder<T>()					
//						.addExistingEntity(enity2Existing)						// 2
//						.build()
//					)));
		
		
		return scenarioCollector;
	}
	
	
	public static List<Object[]> findScenarios(Class<?>... classes) throws Exception {
		List<Object[]> found = new ArrayList<Object[]>();
		
		for (Class<?> aFactoryClass : classes) {
			for (Method aFactoryMethod : aFactoryClass.getMethods()) {
				if (aFactoryMethod.getReturnType().equals(Scenario.class)) {
					Scenario collector = (Scenario) aFactoryMethod.invoke(null);
					found.add(new Object[] { collector.scenarioName(), collector });
				}
			}
		}
		
		return found;
	}
	
}