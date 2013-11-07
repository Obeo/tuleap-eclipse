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
 * The versions of the API of the server.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public interface ITuleapAPIVersions {
	/**
	 * The prefix of the api url.
	 */
	String API_PREFIX = "/api/"; //$NON-NLS-1$

	/**
	 * v1.
	 */
	String V1 = "v1"; //$NON-NLS-1$

	/**
	 * The best version of the API supported by the connector.
	 */
	String BEST_VERSION = V1;

}
