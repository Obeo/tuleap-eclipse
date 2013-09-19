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
package org.tuleap.mylyn.task.internal.core.server.rest;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.restlet.data.Method;
import org.tuleap.mylyn.task.internal.core.server.ServerResponse;

/**
 * JSON Resource for the {@code /api/<version>/milestones/:milestoneId/backlog_items} URL.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class RestMilestonesBacklogItems extends AbstractAuthenticatedRestResource {

	/**
	 * The milestone id.
	 */
	protected int milestoneId;

	/**
	 * Constructor.
	 * 
	 * @param serverUrl
	 *            URL of the rest API on the server.
	 * @param apiVersion
	 *            Version of the REST API to use.
	 * @param credentials
	 *            The credentials to use.
	 * @param milestoneId
	 *            The id of the milestone.
	 */
	protected RestMilestonesBacklogItems(String serverUrl, String apiVersion, ICredentials credentials,
			int milestoneId) {
		super(serverUrl, apiVersion, credentials);
		this.milestoneId = milestoneId;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.server.rest.AbstractRestResource#getUrl()
	 */
	@Override
	protected String getUrl() {
		return URL.MILESTONES + "/" + milestoneId + URL.BACKLOG_ITEMS; //$NON-NLS-1$
	}

	/**
	 * Sends an GET request to the {@code /api/<version>/milestones/:milestoneId/backlog_items} URL and
	 * returns the response.
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
	 * Sends an OPTIONS request to the {@code /api/<version>/milestones/:milestoneId/backlog_items} URL and
	 * checks that the GET operation is allowed in the response provided by the server.
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
