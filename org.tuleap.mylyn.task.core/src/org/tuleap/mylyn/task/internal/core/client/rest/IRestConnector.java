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

import org.apache.commons.httpclient.HttpMethod;

/**
 * A rest connector can emit requests and return server responses.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface IRestConnector {

	/**
	 * Send a request.
	 * 
	 * @param method
	 *            HTTP method to use (OPTIONS, GET, POST, PUT, ...) including URL, query parameters and
	 *            request headers
	 * @return The received serve response, as is.
	 */
	ServerResponse sendRequest(HttpMethod method);

}
