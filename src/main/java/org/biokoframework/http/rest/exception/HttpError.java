/*
 * Copyright (c) 2014
 * Mikol Faro <mikol.faro@gmail.com>
 * Simone Mangano <simone.mangano@ieee.org>
 * Mattia Tortorelli <mattia.tortorelli@gmail.com>
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

package org.biokoframework.http.rest.exception;

import org.biokoframework.utils.domain.ErrorEntity;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class HttpError {

	private int fHttpStatus;
	private List<ErrorEntity> fErrorEntities = new ArrayList<ErrorEntity>();

	public HttpError(int httpStatus) {
		fHttpStatus = httpStatus;
	}

	public HttpError(int httpStatus, List<ErrorEntity> errorEntities) {
		this(httpStatus);
		fErrorEntities = errorEntities;
	}

	public int status() {
		return fHttpStatus;
	}

	public List<ErrorEntity> body() {
		return fErrorEntities;
	}

	public void setBody(List<ErrorEntity> errors) {
		fErrorEntities = new ArrayList<ErrorEntity>(errors);
	}

	public void setBody(ErrorEntity error) {
		fErrorEntities.add(error);
	}

}