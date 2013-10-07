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

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.restlet.data.Method;

/**
 * Resource {@code /cards}, which can optionally use an integer parameter representing the id of a card.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class RestCards extends AbstractAuthenticatedRestResource {

	/**
	 * The card id.
	 */
	private int cardId;

	/**
	 * Constructor with a card id.
	 * 
	 * @param serverUrl
	 *            The server URL.
	 * @param apiVersion
	 *            The API version.
	 * @param credentials
	 *            The credentials for authentication.
	 * @param cardId
	 *            The id of the artifact desired.
	 */
	public RestCards(String serverUrl, String apiVersion, ICredentials credentials, int cardId) {
		super(serverUrl, apiVersion, credentials);
		this.cardId = cardId;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.client.rest.AbstractRestResource#getUrl()
	 */
	@Override
	protected String getUrl() {
		return URL.ARTIFACTS + SLASH + cardId;
	}

	/**
	 * Sends an GET request to the {@code /api/<version>/cards/:id} URL and returns the response.
	 * 
	 * @param headers
	 *            Headers to use for sending the request, just in case. There is no reason why this map
	 *            shouldn't be empty.
	 * @return The received server response, as is.
	 */
	public ServerResponse get(Map<String, String> headers) {
		return sendRequest(Method.GET, headers, EMPTY_BODY);
	}

	/**
	 * Sends an OPTIONS request to the {@code /api/<version>/cards/:id} URL and checks that the GET operation
	 * is allowed in the response provided by the server.
	 * 
	 * @param headers
	 *            Headers to use for sending the request, just in case. There is no reason why this map
	 *            shouldn't be empty.
	 * @throws CoreException
	 *             If the GET operation is not allowed or accessible for any reason.
	 */
	public void checkGet(Map<String, String> headers) throws CoreException {
		checkAccreditation(Method.GET, headers);
	}
}
