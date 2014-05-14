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

import java.util.Map;

/**
 * Utility class used to store the responses of the server.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class ServerResponse {

	/**
	 * OK - 200.
	 */
	public static final int STATUS_OK = 200;

	/**
	 * Moved - 301.
	 */
	public static final int STATUS_MOVED = 301;

	/**
	 * Bad Request - 400.
	 */
	public static final int STATUS_BAD_REQUEST = 400;

	/**
	 * Unauthorized - 401.
	 */
	public static final int STATUS_UNAUTHORIZED = 401;

	/**
	 * Forbidden - 403.
	 */
	public static final int STATUS_FORBIDDEN = 403;

	/**
	 * Not Found - 404.
	 */
	public static final int STATUS_NOT_FOUND = 404;

	/**
	 * Method not allowed - 405.
	 */
	public static final int STATUS_METHOD_NOT_ALLOWED = 405;

	/**
	 * Not Acceptable - 406.
	 */
	public static final int STATUS_NOT_ACCEPTABLE = 406;

	/**
	 * Gone - 410.
	 */
	public static final int STATUS_GONE = 410;

	/**
	 * Requested Range Not Satisfiable - 416.
	 */
	public static final int STATUS_REQUESTED_RANGE_NOT_SATISFIABLE = 416;

	/**
	 * Internal Server Error - 500.
	 */
	public static final int STATUS_INTERNAL_SERVER_ERROR = 500;

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

	/**
	 * Indicates whether the status code of this response is OK (200).
	 * 
	 * @return <code>true</code> if and only if the status of this response is OK (200).
	 */
	public boolean isOk() {
		return status == STATUS_OK;
	}
}
