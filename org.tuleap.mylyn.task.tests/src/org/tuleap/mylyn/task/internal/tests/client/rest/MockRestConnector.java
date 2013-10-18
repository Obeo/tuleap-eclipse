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
package org.tuleap.mylyn.task.internal.tests.client.rest;

import java.util.Map;

import org.tuleap.mylyn.task.internal.core.client.rest.IRestConnector;
import org.tuleap.mylyn.task.internal.core.client.rest.ServerResponse;

/**
 * Mock object for a {@link IRestConnector}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class MockRestConnector implements IRestConnector {

	/**
	 * The response this object will return.
	 */
	private ServerResponse response;

	public ServerResponse sendRequest(String method, String url, Map<String, String> headers, String data) {
		return response;
	}

	/**
	 * Sets the response to return.
	 * 
	 * @param response
	 */
	public void setResponse(ServerResponse response) {
		this.response = response;
	}
}
