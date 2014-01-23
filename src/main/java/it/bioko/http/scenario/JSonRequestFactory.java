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

package it.bioko.http.scenario;


import it.bioko.utils.domain.DomainEntity;
import it.bioko.utils.domain.EntityBuilder;


public class JSonRequestFactory<T extends DomainEntity> {

	private EntityBuilder<T> _entityBuilder= null;

	@SuppressWarnings("unchecked")
	public JSonRequestFactory<T> createEntity(Class<T> aClass) throws Exception {
		Object builder = Class.forName(aClass.getName() + "Builder").getConstructor().newInstance();

		_entityBuilder = (EntityBuilder<T>) builder;

		return this;
	}

	public JSonRequestFactory<T> setEntityParam(String builderMethodName, String parameterValue) throws Exception {
		_entityBuilder.set(builderMethodName, parameterValue);
		return this;
	}

	public JSonRequestFactory<T> setId(String parameterValue) throws Exception {
		_entityBuilder.setId(parameterValue);
		return this;
	}

	public String buildNewEntityAsJSON() {
		return buildNewEntity().toJSONString();

		//		DomainEntity input = buildNewEntity();
		//		String jsonString = input.toJSONString();
		//		String result = new String(jsonString.getBytes(), Charset.forName("UTF-8"));
		//		return result;
	}

	public T buildNewEntity() {
		T input = _entityBuilder.loadDefaultExample().build(false);
		return input;
	}

	public String buildExistingEntityAsJSON() {
		DomainEntity input = buildExistingEntity();
		return input.toJSONString();
	}

	public T buildExistingEntity() {
		T input = _entityBuilder.loadDefaultExample().build(true);
		return input;
	}
}