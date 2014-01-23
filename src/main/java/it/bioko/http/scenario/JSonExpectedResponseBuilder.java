package it.bioko.http.scenario;


import it.bioko.http.rest.exception.HttpError;
import it.bioko.http.rest.exception.HttpResponseBuilder;
import it.bioko.http.rest.exception.HttpResponseExceptionFactory;
import it.bioko.system.KILL_ME.exception.SystemException;
import it.bioko.system.command.CommandException;
import it.bioko.system.command.ValidationException;
import it.bioko.system.exceptions.CommandExceptionsFactory;
import it.bioko.utils.domain.DomainEntity;
import it.bioko.utils.domain.EntityBuilder;
import it.bioko.utils.domain.ErrorEntity;
import it.bioko.utils.domain.reflection.DomainEntityReflection;
import it.bioko.utils.fields.Fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


public class JSonExpectedResponseBuilder<T extends DomainEntity> {
	private ArrayList<DomainEntity> _hashtable = new ArrayList<DomainEntity>();
	
	@SuppressWarnings("unchecked")
	public static String asJSONArray(Object output) {
		JSONArray result = new JSONArray();
		result.add(output);
		return result.toJSONString();
	}

	public static String asJSONObject(Map<String, Object> output) {
		JSONObject result = new JSONObject(output);
		return result.toJSONString();
	}

	public static String asJSON(Object output) {
		return JSONValue.toJSONString(output);
	}
	
	public static String asArray(String aJSON) {
		return new StringBuffer("[")
			.append(aJSON).append("]").toString();
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends DomainEntity>  String existingEntity(Class<T> aClass, String anId) {
//		Builder entityBuilder = (Builder)DomainEntityReflection.domainEntityBuilderInstance(aClass);
		
//		EntityBuilder<T> entityBuilder = (EntityBuilder<T>) DomainEntityReflection.domainEntityBuilderInstance(aClass);
		Object reflectionEntityBuilder = DomainEntityReflection.domainEntityBuilderInstance(aClass);

		EntityBuilder<T> entityBuilder = (EntityBuilder<T>) reflectionEntityBuilder;			
		DomainEntity input = entityBuilder.loadDefaultExample().setId(anId).build(true);		
		return asJSONArray(input);
	}

	
	@SuppressWarnings("unchecked")
	public JSonExpectedResponseBuilder<T> addExistingEntity(Class<T> aClass, String anId) throws Exception {
//		Builder entityBuilder = (Builder)Class.forName(aClass.getName() + "Builder").getConstructor().newInstance();
		
//		EntityBuilder<T> entityBuilder = (EntityBuilder<T>)Class.forName(aClass.getName() + "Builder").getConstructor().newInstance();
		Object reflectionEntityBuilder = Class.forName(aClass.getName() + "Builder").getConstructor().newInstance();
		
		EntityBuilder<T> entityBuilder = (EntityBuilder<T>) reflectionEntityBuilder;
		
		DomainEntity input = entityBuilder.loadDefaultExample().setId(anId).build(true);
		_hashtable.add(input);
		return this;
		
	}

	public JSonExpectedResponseBuilder<T> addExistingEntity(T anEntity) throws Exception {
		_hashtable.add(anEntity);
		return this;
	}
	
	public static <T extends DomainEntity> HttpError entityNotFound(Class<T> entityClass, Class<? extends EntityBuilder<T>> entityClassBuilder, String entityId) {
		HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder(HttpResponseExceptionFactory.create());
		CommandException notFound = CommandExceptionsFactory.createEntityNotFound(entityClass.getSimpleName(), entityId);
		return httpResponseBuilder.buildFrom(notFound);
	}
	
	public static <T extends DomainEntity> HttpError entityNotFound(Class<T> entityClass, String fieldName, String fieldValue) {
		HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder(HttpResponseExceptionFactory.create());
		CommandException notFound = CommandExceptionsFactory.createEntityNotFound(entityClass.getSimpleName(), fieldName, fieldValue);
		return httpResponseBuilder.buildFrom(notFound);
	}

	public static <T extends DomainEntity> HttpError entityNotComplete(Class<T> entityClass, Class<? extends EntityBuilder<T>> entityClassBuilder) {
		HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder(HttpResponseExceptionFactory.create());
		CommandException notFound = CommandExceptionsFactory.createNotCompleteEntity(entityClass.getSimpleName());
		return httpResponseBuilder.buildFrom(notFound);
	}
	
	public static <T> HttpError entityAlreadyExisting(Class<T> entityClass, Fields alreadyExistingEntity) {
		HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder(HttpResponseExceptionFactory.create());
		CommandException alreadyExisting = CommandExceptionsFactory.createAlreadyExistingEntity(entityClass.getSimpleName());
		return httpResponseBuilder.buildFrom(alreadyExisting);
	}

	public static HttpError commandNotFound(String commandName) {
		SystemException commandNotFound = CommandExceptionsFactory.createCommandNotFoundException(commandName);
		HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder(HttpResponseExceptionFactory.create());
		return httpResponseBuilder.buildFrom(commandNotFound);
	}
	
	public static HttpError authenticationRequired() {
		CommandException authenticationRequired = CommandExceptionsFactory.createTokenNotFoundException();
		HttpResponseBuilder responseBuilder = new HttpResponseBuilder(HttpResponseExceptionFactory.create());
		return responseBuilder.buildFrom(authenticationRequired);
		
	}
	
	public static HttpError  insufficientPrivileges() {
		CommandException insufficientPrivileges = CommandExceptionsFactory.createInsufficientPrivilegesException();
		HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder(HttpResponseExceptionFactory.create());
		return httpResponseBuilder.buildFrom(insufficientPrivileges);
	}
	
	public static HttpError invalidLogin() {
		CommandException invalidLogin = CommandExceptionsFactory.createInvalidLoginException();
		HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder(HttpResponseExceptionFactory.create());
		return httpResponseBuilder.buildFrom(invalidLogin);
	}
	
	
	public static HttpError  validationError(List<ErrorEntity> errors) {
		ValidationException expectedException = new ValidationException(errors);

		HttpResponseBuilder responseBuilder = new HttpResponseBuilder(HttpResponseExceptionFactory.create());
		HttpError expectedError = responseBuilder.buildFrom(expectedException);
		
		return expectedError;
	}
	
	
	
	
	
	
	
	
	
	
	public String build() {
//		return asJSON(_hashtable);
		return JSONValue.toJSONString(_hashtable);
	}

}