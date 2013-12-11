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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.tuleap.mylyn.task.internal.core.client.rest.IRestConnector;
import org.tuleap.mylyn.task.internal.core.client.rest.RestResourceFactory;
import org.tuleap.mylyn.task.internal.core.client.rest.ServerResponse;

/**
 * Mock object for a {@link IRestConnector}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class MockRestConnector implements IRestConnector {

	/**
	 * The factory.
	 */
	protected RestResourceFactory resourceFactory;

	protected final List<ServerRequest> requestsSent = Lists.newArrayList();

	protected int invocationsCount;

	/**
	 * The response this object will return.
	 */
	private ServerResponse response;

	public ServerResponse sendRequest(String method, String url, Map<String, String> headers, String data) {
		requestsSent.add(new ServerRequest(method, url, headers, data));
		invocationsCount++;
		return response;
	}

	/**
	 * The number of times sendRequest was called during this object's lifetime.
	 * 
	 * @return The number of invocations made.
	 */
	public int getInvocationsCount() {
		return invocationsCount;
	}

	/**
	 * Sets the response to return.
	 * 
	 * @param response
	 */
	public void setResponse(ServerResponse response) {
		this.response = response;
	}

	/**
	 * resource factory setter.
	 * 
	 * @param resourceFactory
	 *            the resource factory to set
	 */
	public void setResourceFactory(RestResourceFactory resourceFactory) {
		this.resourceFactory = resourceFactory;
	}

	/**
	 * @return the requestsSent
	 */
	public List<ServerRequest> getRequestsSent() {
		return requestsSent;
	}

	/**
	 * Clears the list of sent requests.
	 */
	public void resetRequestsSent() {
		requestsSent.clear();
	}

	/**
	 * A request sent to the server.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static class ServerRequest {
		// CHECKSTYLE:OFF
		/** Method. */
		public final String method;

		/** URL. */
		public final String url;

		/** Headers (immutable). */
		public final Map<String, String> headers;

		/** Body. */
		public final String body;

		// CHECKSTYLE: ON

		/**
		 * @param method
		 * @param url
		 * @param headers
		 * @param body
		 */
		public ServerRequest(String method, String url, Map<String, String> headers, String body) {
			super();
			this.method = method;
			this.url = url;
			this.headers = Collections.unmodifiableMap(headers);
			this.body = body;
		}

	}
}
