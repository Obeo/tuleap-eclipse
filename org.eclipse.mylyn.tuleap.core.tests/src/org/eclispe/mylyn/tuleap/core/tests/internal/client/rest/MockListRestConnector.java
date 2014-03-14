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
package org.eclispe.mylyn.tuleap.core.tests.internal.client.rest;

import com.google.common.collect.Lists;

import java.util.List;

import org.apache.commons.httpclient.HttpMethod;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.ServerResponse;

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

	@Override
	public ServerResponse sendRequest(HttpMethod method) {
		requestsSent.add(getServerRequest(method));
		if (currentIndex < responses.size()) {
			return responses.get(currentIndex++);
		}
		return null;
	}
}
