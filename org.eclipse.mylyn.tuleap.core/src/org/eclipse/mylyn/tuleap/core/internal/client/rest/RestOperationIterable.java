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
package org.eclipse.mylyn.tuleap.core.internal.client.rest;

import com.google.common.collect.Iterators;
import com.google.gson.JsonElement;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.tuleap.core.internal.TuleapCoreActivator;

/**
 * Iterable over an operation response JSON elements, that takes care of pagination if needed.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class RestOperationIterable implements Iterable<JsonElement> {

	/**
	 * The RESt operation.
	 */
	private RestOperation operation;

	/**
	 * Constructor.
	 *
	 * @param operation
	 *            the REST operation to iterate over.
	 */
	public RestOperationIterable(RestOperation operation) {
		this.operation = operation;
		if (!operation.hasQueryParameter(RestResource.LIMIT)) {
			operation.withQueryParameter(RestResource.LIMIT, RestResource.LIMIT_DEFAULT);
		}
	}

	/**
	 * Provides an iterator that will retrieve all the JSON elements.
	 *
	 * @return An iterator that will provide all the elements, taking care of pagination if needed.
	 */
	public Iterator<JsonElement> iterator() {
		ServerResponse response = operation.run();
		Iterator<JsonElement> it;
		try {
			operation.checkServerError(response);
			Map<String, String> responseHeaders = response.getHeaders();
			// ONLY X-PAGINATION-SIZE needs be checked, other values are not mandatory
			if (responseHeaders.containsKey(RestResource.HEADER_X_PAGINATION_SIZE)) {
				it = new JsonResponsePaginatedIterator(operation, response);
			} else {
				it = new JsonResponseIterator(response);
			}
		} catch (CoreException e) {
			TuleapCoreActivator.log(e.getStatus());
			it = Iterators.emptyIterator();
		}
		return it;
	}
}
