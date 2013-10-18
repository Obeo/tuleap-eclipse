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

/**
 * A rest connector can emit requests and return server responses.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface IRestConnector {

	/**
	 * Provides the {@link RestResourceFactory} to use with this connector.
	 * 
	 * @return The {@link RestResourceFactory} to use with this connector.
	 * @throws CoreException
	 *             if a problem occurs while building the resource factory.
	 */
	RestResourceFactory getResourceFactory() throws CoreException;

	/**
	 * Send a request.
	 * 
	 * @param method
	 *            HTTP method to use (OPTIONS, GET, POST, PUT, ...)
	 * @param url
	 *            The full URL to communicate with, including query parameters.
	 * @param headers
	 *            Headers to send
	 * @param data
	 *            Data to send
	 * @return The received serve response, as is.
	 */
	ServerResponse sendRequest(String method, String url, Map<String, String> headers, String data);

}
