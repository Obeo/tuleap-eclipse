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
package org.tuleap.mylyn.task.core.internal.client.rest;

import com.google.common.collect.Iterators;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.Iterator;

import org.eclipse.core.runtime.Assert;

/**
 * Iterates over JsonElements in a JsonArray received in a ServerResponse, when no pagination is involved.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class JsonResponseIterator implements Iterator<JsonElement> {

	/**
	 * The JSON array over which to iterate, replaced each time a new page is retrieved.
	 */
	private Iterator<JsonElement> iterator;

	/**
	 * Constructor.
	 *
	 * @param response
	 *            The first response received from the server.
	 */
	public JsonResponseIterator(ServerResponse response) {
		Assert.isNotNull(response);
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse(response.getBody());
		if (jsonElement.isJsonArray()) {
			iterator = jsonElement.getAsJsonArray().iterator();
		} else {
			iterator = Iterators.singletonIterator(jsonElement);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Iterator#next()
	 */
	@Override
	public JsonElement next() {
		return iterator.next();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
