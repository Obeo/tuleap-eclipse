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
 * This interface holds all the headers used by the Tuleap server.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public interface ITuleapHeaders {
	/**
	 * Location.
	 */
	String LOCATION = "Location"; //$NON-NLS-1$

	/**
	 * Allow.
	 */
	String ALLOW = "Allow"; //$NON-NLS-1$

	/**
	 * Access-Control-Allow-Methods.
	 */
	String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods"; //$NON-NLS-1$

	/**
	 * Authorization.
	 */
	String AUTHORIZATION = "Authorization"; //$NON-NLS-1$

	/**
	 * Header key for the property that describes the pagination limit (the maximum number of elements per
	 * page).
	 */
	String HEADER_X_PAGINATION_LIMIT = "X-PAGINATION-LIMIT"; //$NON-NLS-1$

	/**
	 * Default value for pagination limit when header X-PAGINATION-LIMIT is not present.
	 */
	int DEFAULT_PAGINATION_LIMIT = 10;

	/**
	 * Header key for the property that describes the pagination offset (index of the first element in the
	 * current page in the whole list of elements).
	 */
	String HEADER_X_PAGINATION_OFFSET = "X-PAGINATION-OFFSET"; //$NON-NLS-1$

	/**
	 * Default value for pagination offset when header X-PAGINATION-OFFSET is not present.
	 */
	int DEFAULT_PAGINATION_OFFSET = 0;

	/**
	 * Header key for the property that describes the number of elements in the paginated list.
	 */
	String HEADER_X_PAGINATION_SIZE = "X-PAGINATION-SIZE"; //$NON-NLS-1$
}
