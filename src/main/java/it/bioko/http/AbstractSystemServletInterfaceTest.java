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

package it.bioko.http;

import it.bioko.http.rest.WebAppTest;
import it.bioko.utils.domain.DomainEntity;
import it.bioko.utils.fields.Fields;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractSystemServletInterfaceTest extends WebAppTest {

	protected static final String HTTP_LOCALHOST_9090 = "http://localhost:9090/engagedServer/api/";
	protected static final String HTTP_ENGAGED_SERVER_8080 = "http://api.test.engaged.it/api/";
	protected static final String LOCALHOST_9090_URL = HTTP_LOCALHOST_9090;
	protected static final String ENGAGED_SERVER_URL = HTTP_ENGAGED_SERVER_8080;
	protected static final String BUONOBOX_SYSTEM = "buonobox/1.0/";
	protected static final String SYSTEM_A = "systemA/1.0/";
	protected static final String WRONG_PATH = "WRONG_PATH/";
	protected static final String CONTACT = "contact/";
	protected static final String COMMAND_LIST = "command-list";
	protected static final String COMMAND_INVOCATION_INFO = "command-invocation-info/";
	
	protected <T extends DomainEntity> Fields prepareResponse(T entity) {
		List<DomainEntity> entitiesList = new ArrayList<DomainEntity>();
		entity.setId(entity.getId());
		entitiesList.add(entity);
		
		return prepareResponse(entitiesList);
	}
	
	protected Fields prepareResponse(List<DomainEntity> entitiesList) {
	    Fields entities = Fields.empty();
	    entities.put("RESPONSE", entitiesList);
		return entities;
	}
}
