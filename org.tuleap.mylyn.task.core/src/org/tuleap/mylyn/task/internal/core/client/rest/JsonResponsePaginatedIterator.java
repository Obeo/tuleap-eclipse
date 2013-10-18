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

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import org.eclipse.core.runtime.Assert;

/**
 * Handles the pagination of a REST dialog.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class JsonResponsePaginatedIterator implements Iterator<JsonElement> {

	/**
	 * The number of elements in the list of elements to iterate over. Corresponds to the
	 * {@code X-PAGINATION-SIZE} HTTP header attribute.
	 */
	private int nbElements; // X-PAGINATION-SIZE

	/**
	 * The number of elements per ServerResponse in the list of elements to iterate over. Corresponds to the
	 * {@code X-PAGINATION-LIMIT} HTTP header attribute.
	 */
	private int nbElementsPerPage; // X-PAGINATION-LIMIT

	/**
	 * The index of the next element that will be retrieved by calling next(). Corresponds to the
	 * {@code X-PAGINATION-OFFSET} HTTP header attribute.
	 */
	private int currentOffset; // X-PAGINATION-OFFSET

	/**
	 * The REST operation to perform.
	 */
	private final IRestOperation operation;

	/**
	 * The headers to send.
	 */
	private final Map<String, String> headers;

	/**
	 * The body to send.
	 */
	private final String body;

	/**
	 * The latest {@link ServerResponse} retrieved from the server, <code>null</code> when no invocation has
	 * been made.
	 */
	private ServerResponse currentResponse;

	/**
	 * The JSON array over which to iterate, replaced each time a new page is retrieved.
	 */
	private Iterator<JsonElement> iterator;

	/**
	 * Constructor.
	 * 
	 * @param operation
	 *            The REST operation to perform, several times if there is pagination involved.
	 * @param headers
	 *            the HTTP headers to send the first time. These headers will NOT be modified, but a copy will
	 *            be used and modified to handle pagination.
	 * @param body
	 *            TYhe body to send, will always be sent unmodified.
	 * @param firstResponse
	 *            The first response received from the server.
	 */
	public JsonResponsePaginatedIterator(IRestOperation operation, Map<String, String> headers,
			String body, ServerResponse firstResponse) {
		Assert.isNotNull(operation);
		Assert.isNotNull(firstResponse);
		this.operation = operation;
		this.headers = Maps.newHashMap();
		this.headers.putAll(headers);
		this.body = body;
		this.currentResponse = firstResponse;
		extractCounters(firstResponse);
	}

	/**
	 * Extracts the counters from the given response and reinitializes the iterator used to iterate over
	 * available elements.
	 * 
	 * @param response
	 *            The server response containing the headers to extract.
	 */
	private void extractCounters(ServerResponse response) {
		Map<String, String> responseHeaders = currentResponse.getHeaders();
		String xPaginationSize = responseHeaders.get(ITuleapHeaders.HEADER_X_PAGINATION_SIZE);
		String xPaginationLimit = responseHeaders.get(ITuleapHeaders.HEADER_X_PAGINATION_LIMIT);
		String xPaginationOffset = responseHeaders.get(ITuleapHeaders.HEADER_X_PAGINATION_OFFSET);
		try {
			nbElements = Integer.parseInt(xPaginationSize);
			if (xPaginationLimit != null) {
				nbElementsPerPage = Integer.parseInt(xPaginationLimit);
			} else {
				nbElementsPerPage = ITuleapHeaders.DEFAULT_PAGINATION_LIMIT;
			}
			if (xPaginationOffset != null) {
				currentOffset = Integer.parseInt(xPaginationOffset);
			} else {
				currentOffset = ITuleapHeaders.DEFAULT_PAGINATION_OFFSET;
			}
		} catch (NumberFormatException e) {
			// TODO Externalize
			throw new IllegalArgumentException(
					"The received server response does not contain the required headers", e);
		}
		iterator = new JsonParser().parse(currentResponse.getBody()).getAsJsonArray().iterator();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return currentOffset < nbElements;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Iterator#next()
	 */
	public JsonElement next() {
		if (currentOffset >= nbElements) {
			throw new NoSuchElementException();
		}
		currentOffset++;
		if (currentOffset % nbElementsPerPage == 0 || !iterator.hasNext()) {
			// TODO Change this, should add query parameters (?)
			headers.put(ITuleapHeaders.HEADER_X_PAGINATION_OFFSET, Integer.toString(currentOffset));
			currentResponse = operation.withHeaders(headers).withBody(body).run();
			extractCounters(currentResponse);
		}
		return iterator.next();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
