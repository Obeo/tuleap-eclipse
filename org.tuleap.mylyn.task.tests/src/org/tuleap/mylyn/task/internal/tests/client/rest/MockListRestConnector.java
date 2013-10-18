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

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

import org.tuleap.mylyn.task.internal.core.client.rest.ServerResponse;

/**
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class MockListRestConnector extends MockRestConnector {

	private final List<ServerResponse> responses = Lists.newArrayList();

	/**
	 * Index of the next response that will be returned among the available responses.
	 */
	private int currentIndex;

	public MockListRestConnector addServerResponse(ServerResponse response) {
		responses.add(response);
		return this;
	}

	public MockListRestConnector resetResponses() {
		responses.clear();
		return this;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.tests.client.rest.MockRestConnector#sendRequest(java.lang.String,
	 *      java.lang.String, java.util.Map, java.lang.String)
	 */
	@Override
	public ServerResponse sendRequest(String method, String url, Map<String, String> headers, String data) {
		requestsSent.add(new ServerRequest(method, url, headers, data));
		if (currentIndex < responses.size()) {
			return responses.get(currentIndex++);
		}
		return null;
	}
}
