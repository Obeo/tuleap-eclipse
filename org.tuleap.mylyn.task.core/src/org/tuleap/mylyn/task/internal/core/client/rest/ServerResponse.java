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

/**
 * Utility class used to store the responses of the server.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class ServerResponse {
	/**
	 * The status of the response.
	 */
	private int status;

	/**
	 * The body.
	 */
	private String body;

	/**
	 * The headers.
	 */
	private Map<String, String> headers;

	/**
	 * The constructor.
	 * 
	 * @param status
	 *            The status
	 * @param body
	 *            The body
	 * @param headers
	 *            The headers
	 */
	public ServerResponse(int status, String body, Map<String, String> headers) {
		this.status = status;
		this.body = body;
		this.headers = headers;
	}

	/**
	 * Returns the status.
	 * 
	 * @return The status
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * Returns the body.
	 * 
	 * @return The body
	 */
	public String getBody() {
		return this.body;
	}

	/**
	 * Returns the headers.
	 * 
	 * @return The headers
	 */
	public Map<String, String> getHeaders() {
		return this.headers;
	}
}
