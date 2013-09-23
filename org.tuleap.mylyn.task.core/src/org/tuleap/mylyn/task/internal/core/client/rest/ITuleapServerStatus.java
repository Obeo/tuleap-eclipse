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

/**
 * The list of all the possible status from the Tuleap server.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public interface ITuleapServerStatus {
	/**
	 * OK - 200.
	 */
	int OK = 200;

	/**
	 * Moved - 301.
	 */
	int MOVED = 301;

	/**
	 * Bad Request - 400.
	 */
	int BAD_REQUEST = 400;

	/**
	 * Gone - 401.
	 */
	int GONE = 401;

	/**
	 * Forbidden - 403.
	 */
	int FORBIDDEN = 403;

	/**
	 * Not Found - 404.
	 */
	int NOT_FOUND = 404;

	/**
	 * Not Acceptable - 406.
	 */
	int NOT_ACCEPTABLE = 406;

	/**
	 * Unauthorized - 410.
	 */
	int UNAUTHORIZED = 410;

	/**
	 * Requested Range Not Satisfiable - 416.
	 */
	int REQUESTED_RANGE_NOT_SATISFIABLE = 416;

	/**
	 * Internal Server Error - 500.
	 */
	int INTERNAL_SERVER_ERROR = 500;
}
