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
 * JSON Resource for the {@code /api/<version>/card_type/:cardTypeId} URL.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class RestCardTypes extends AbstractAuthenticatedRestResource {

	/**
	 * The card type id.
	 */
	protected int cardTypeId;

	/**
	 * Constructor.
	 * 
	 * @param serverUrl
	 *            URL of the rest API on the server.
	 * @param apiVersion
	 *            Version of the REST API to use.
	 * @param credentials
	 *            The credentials to use.
	 * @param cardTypeId
	 *            The identifier of the card type
	 */
	public RestCardTypes(String serverUrl, String apiVersion, ICredentials credentials, int cardTypeId) {
		super(serverUrl, apiVersion, credentials);
		this.cardTypeId = cardTypeId;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.client.rest.AbstractRestResource#getUrl()
	 */
	@Override
	protected String getUrl() {
		return URL.CARD_TYPES + "/" + this.cardTypeId; //$NON-NLS-1$ 
	}

	/**
	 * Sends an GET request to the {@code /api/<version>/card_types/:cardTypeId} URL and returns the response.
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
	 * Sends an OPTIONS request to the {@code /api/<version>/card_types/:cardTypeId} URL and checks that the
	 * GET operation is allowed in the response provided by the server.
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
