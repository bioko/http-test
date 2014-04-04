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


import org.biokoframework.http.rest.exception.HttpError;
import org.biokoframework.http.rest.exception.HttpResponseBuilder;
import org.biokoframework.http.rest.exception.HttpResponseExceptionFactory;
import org.biokoframework.system.KILL_ME.exception.SystemException;
import org.biokoframework.system.command.CommandException;
import org.biokoframework.system.exceptions.CommandExceptionsFactory;
import org.biokoframework.utils.domain.DomainEntity;
import org.biokoframework.utils.domain.EntityBuilder;
import org.biokoframework.utils.domain.ErrorEntity;
import org.biokoframework.utils.domain.reflection.DomainEntityReflection;
import org.biokoframework.utils.exception.ValidationException;
import org.biokoframework.utils.fields.Fields;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
		CommandException notFound = CommandExceptionsFactory.createEntityNotFound(entityClass, entityId);
		return httpResponseBuilder.buildFrom(notFound);
	}
	
	public static <T extends DomainEntity> HttpError entityNotFound(Class<T> entityClass, String fieldName, String fieldValue) {
		HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder(HttpResponseExceptionFactory.create());
		CommandException notFound = CommandExceptionsFactory.createEntityNotFound(entityClass, fieldName, fieldValue);
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
		SystemException authenticationRequired = CommandExceptionsFactory.createTokenNotFoundException();
		HttpResponseBuilder responseBuilder = new HttpResponseBuilder(HttpResponseExceptionFactory.create());
		return responseBuilder.buildFrom(authenticationRequired);
		
	}
	
	public static HttpError  insufficientPrivileges() {
		SystemException insufficientPrivileges = CommandExceptionsFactory.createInsufficientPrivilegesException();
		HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder(HttpResponseExceptionFactory.create());
		return httpResponseBuilder.buildFrom(insufficientPrivileges);
	}
	
	public static HttpError invalidLogin() {
		SystemException invalidLogin = CommandExceptionsFactory.createInvalidLoginException();
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