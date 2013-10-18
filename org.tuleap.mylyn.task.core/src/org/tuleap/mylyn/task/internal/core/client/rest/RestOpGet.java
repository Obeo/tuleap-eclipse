/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.internal.core.client.rest;

import com.google.common.collect.Iterators;
import com.google.gson.JsonElement;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.restlet.data.Method;

/**
 * GET operation on a REST resource.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class RestOpGet extends AbstractRestOperation implements IRestIterableOperation<JsonElement> {

	/**
	 * Constructor.
	 * 
	 * @param fullUrl
	 *            The resource's URL.
	 * @param connector
	 *            the connector to use to "task to" the server.
	 */
	public RestOpGet(String fullUrl, IRestConnector connector) {
		super(fullUrl, connector);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.client.rest.IRestOperation#getMethodName()
	 */
	public String getMethodName() {
		return Method.GET.getName();
	}

	/**
	 * Provides an iterator that will retrieve all the JSON elements.
	 * 
	 * @return An iterator that will provide all the elements, taking care of pagination if needed.
	 */
	public Iterator<JsonElement> iterator() {
		ServerResponse response = run();
		Iterator<JsonElement> it;
		try {
			checkServerError(response);
			Map<String, String> responseHeaders = response.getHeaders();
			// ONLY X-PAGINATION-SIZE needs be checked, other values are not mandatory
			if (responseHeaders.containsKey(ITuleapHeaders.HEADER_X_PAGINATION_SIZE)) {
				it = new JsonResponsePaginatedIterator(this, requestHeaders, body, response);
			} else {
				it = new JsonResponseIterator(response);
			}
		} catch (CoreException e) {
			logger.log(e.getStatus());
			it = Iterators.emptyIterator();
		}
		return it;
	}
}
