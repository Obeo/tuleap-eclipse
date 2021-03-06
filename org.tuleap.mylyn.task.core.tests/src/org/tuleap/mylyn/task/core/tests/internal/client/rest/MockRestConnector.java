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
package org.tuleap.mylyn.task.core.tests.internal.client.rest;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.tuleap.mylyn.task.core.internal.client.rest.IRestConnector;
import org.tuleap.mylyn.task.core.internal.client.rest.RestResourceFactory;
import org.tuleap.mylyn.task.core.internal.client.rest.ServerResponse;

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

	@Override
	public ServerResponse sendRequest(HttpMethod method) {
		requestsSent.add(getServerRequest(method));
		invocationsCount++;
		return response;
	}

	protected ServerRequest getServerRequest(HttpMethod method) {
		Map<String, String> header = new LinkedHashMap<String, String>();
		for (Header h : method.getRequestHeaders()) {
			header.put(h.getName(), h.getValue());
		}
		if (method instanceof EntityEnclosingMethod) {
			RequestEntity entity = ((EntityEnclosingMethod)method).getRequestEntity();
			if (entity instanceof StringRequestEntity) {
				return new ServerRequest(method.getName(), method.getPath(), header, method.getQueryString(),
						((StringRequestEntity)entity).getContent());
			}
		}
		return new ServerRequest(method.getName(), method.getPath(), header, method.getQueryString());
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

		/** Body (immutable) */
		public final String body;

		/** Query String (immutable) */
		public final String queryString;

		// CHECKSTYLE: ON

		/**
		 * @param method
		 * @param url
		 * @param headers
		 * @param queryString
		 */
		public ServerRequest(String method, String url, Map<String, String> headers, String queryString) {
			this(method, url, headers, queryString, null);
		}

		/**
		 * @param method
		 * @param url
		 * @param headers
		 * @param queryString
		 * @param body
		 */
		public ServerRequest(String method, String url, Map<String, String> headers, String queryString,
				String body) {
			super();
			this.method = method;
			this.url = url;
			this.headers = Collections.unmodifiableMap(headers);
			this.queryString = queryString;
			this.body = body;
		}

	}
}
