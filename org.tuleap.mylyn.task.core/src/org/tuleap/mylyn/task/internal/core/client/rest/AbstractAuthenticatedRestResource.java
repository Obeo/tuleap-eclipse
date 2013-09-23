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

import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;

/**
 * REST Resource to inherit from when authentication is required to access the resource.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractAuthenticatedRestResource extends AbstractRestResource {

	/**
	 * Cached credentials.
	 */
	private ICredentials credentials;

	/**
	 * Constructor that receives credentials in addition to its parent class parameters.
	 * 
	 * @param serverUrl
	 *            The server URL
	 * @param apiVersion
	 *            The API version to use
	 * @param credentials
	 *            The credentials to use for authorization
	 */
	protected AbstractAuthenticatedRestResource(String serverUrl, String apiVersion, ICredentials credentials) {
		super(serverUrl, apiVersion);
		// TODO [Perf] Cache username & password instead of re-reading it each time it is used?
		this.credentials = credentials;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.client.rest.AbstractRestResource#getChallengeResponse()
	 */
	@Override
	protected ChallengeResponse getChallengeResponse() {
		return new ChallengeResponse(ChallengeScheme.HTTP_BASIC, credentials.getUserName(), credentials
				.getPassword());
	}

}
