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

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.restlet.data.Method;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.model.TuleapDebugPart;
import org.tuleap.mylyn.task.internal.core.model.TuleapErrorMessage;
import org.tuleap.mylyn.task.internal.core.model.TuleapErrorPart;
import org.tuleap.mylyn.task.internal.core.model.TuleapToken;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonParser;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessagesKeys;

/**
 * Abstract RESTful operation.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class RestOperation {

	/**
	 * String to send in the body when no data needs to be in the request body. {@code null} provokes an
	 * exception in Restlet.
	 */
	public static final String EMPTY_BODY = ""; //$NON-NLS-1$

	/**
	 * The full URL.
	 */
	protected final String fullUrl;

	/**
	 * The body to send in the request.
	 */
	protected String body;

	/**
	 * Authentication token to use..
	 */
	protected TuleapToken token;

	/**
	 * HTTP headers to send.
	 */
	protected final Map<String, String> requestHeaders = Maps.newTreeMap();

	/**
	 * {@link LinkedHashMultimap} of query parameters to append to the URL.
	 */
	protected final LinkedHashMultimap<String, String> requestParameters = LinkedHashMultimap.create();

	// TODO Inject a logger
	/**
	 * The logger.
	 */
	protected ILog logger;

	/**
	 * The connector to use.
	 */
	protected final IRestConnector connector;

	/**
	 * The HTTP method to use.
	 */
	protected final Method method;

	/**
	 * Constructor.
	 * 
	 * @param fullUrl
	 *            The full URL of the resource to connect to.
	 * @param connector
	 *            the connector to use to "task to" the server.
	 * @param method
	 *            the HTTP method to use.
	 */
	public RestOperation(String fullUrl, IRestConnector connector, Method method) {
		this.fullUrl = fullUrl;
		this.connector = connector;
		this.method = method;
	}

	/**
	 * Instantiates a new GET operation for the given URL.
	 * 
	 * @param fullUrl
	 *            The full URL of the resource to connect to.
	 * @param connector
	 *            the connector to use to "task to" the server.
	 * @return a new REST operation for the GET method.
	 */
	public static RestOperation get(String fullUrl, IRestConnector connector) {
		return new RestOperation(fullUrl, connector, Method.GET);
	}

	/**
	 * Instantiates a new PUT operation for the given URL.
	 * 
	 * @param fullUrl
	 *            The full URL of the resource to connect to.
	 * @param connector
	 *            the connector to use to "task to" the server.
	 * @return a new REST operation for the PUT method.
	 */
	public static RestOperation put(String fullUrl, IRestConnector connector) {
		return new RestOperation(fullUrl, connector, Method.PUT);
	}

	/**
	 * Instantiates a new POST operation for the given URL.
	 * 
	 * @param fullUrl
	 *            The full URL of the resource to connect to.
	 * @param connector
	 *            the connector to use to "task to" the server.
	 * @return a new REST operation for the POST method.
	 */
	public static RestOperation post(String fullUrl, IRestConnector connector) {
		return new RestOperation(fullUrl, connector, Method.POST);
	}

	/**
	 * Instantiates a new OPTIONS operation for the given URL.
	 * 
	 * @param fullUrl
	 *            The full URL of the resource to connect to.
	 * @param connector
	 *            the connector to use to "task to" the server.
	 * @return a new REST operation for the OPTIONS method.
	 */
	public static RestOperation options(String fullUrl, IRestConnector connector) {
		return new RestOperation(fullUrl, connector, Method.OPTIONS);
	}

	/**
	 * Instantiates a new DELETE operation for the given URL.
	 * 
	 * @param fullUrl
	 *            The full URL of the resource to connect to.
	 * @param connector
	 *            the connector to use to "task to" the server.
	 * @return a new REST operation for the DELETE method.
	 */
	public static RestOperation delete(String fullUrl, IRestConnector connector) {
		return new RestOperation(fullUrl, connector, Method.DELETE);
	}

	/**
	 * The resource's URL.
	 * 
	 * @return The resource's URL.
	 */
	public String getUrl() {
		return fullUrl;
	}

	/**
	 * Returns the name of the HTTP method to invoke.
	 * 
	 * @return The name of the HTTP method to invoke.
	 */
	public String getMethodName() {
		return method.getName();
	}

	/**
	 * Computes the full URL to use to send the request, by concatenating the server address, the root API
	 * prefix, the API version, and the URL fragment of the resource to access.
	 * 
	 * @return The full URL to use to send the request.
	 */
	public String getUrlWithQueryParameters() {
		String url = getUrl();
		if (!requestParameters.isEmpty()) {
			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append('?');
			for (Iterator<Entry<String, String>> entries = requestParameters.entries().iterator(); entries
					.hasNext();) {
				Entry<String, String> entry = entries.next();
				queryBuilder.append(entry.getKey()).append('=').append(entry.getValue());
				// TODO encode for HTTP !!!
				if (entries.hasNext()) {
					queryBuilder.append('&');
				}
			}
			url += queryBuilder.toString();
		}
		return url;
	}

	/**
	 * Run this operation by sending the relevant request and returning the received response.
	 * 
	 * @return The response received from the server after sending it the relevant request.
	 */
	public ServerResponse run() {
		String data;
		if (body == null) {
			data = EMPTY_BODY;
		} else {
			data = body;
		}
		if (token != null) {
			requestHeaders.put("X-Auth-Token", token.getToken());
			requestHeaders.put("X-Auth-UserId", token.getUserId());
		}
		return connector.sendRequest(getMethodName(), getUrlWithQueryParameters(), requestHeaders, data);
	}

	/**
	 * Runs this operation by sending the relevant request, and checks the received response.
	 * 
	 * @return The received {@link ServerResponse}.
	 * @throws CoreException
	 *             If the received response status is not 200 OK.
	 */
	public ServerResponse checkedRun() throws CoreException {
		ServerResponse response = run();
		checkServerError(response);
		return response;
	}

	/**
	 * Throws a CoreException that encapsulates useful info about a server error.
	 * 
	 * @param response
	 *            The error response received from the server.
	 * @throws CoreException
	 *             If the given response does not have a status OK (200).
	 */
	protected void checkServerError(ServerResponse response) throws CoreException {
		if (!response.isOk()) {
			TuleapErrorMessage message = new TuleapJsonParser().getErrorMessage(response.getBody());
			TuleapErrorPart errorPart = message.getError();
			TuleapDebugPart debugPart = message.getDebug();
			String msg;
			if (errorPart == null) {
				msg = response.getBody();
			} else {
				if (debugPart != null) {
					msg = TuleapMylynTasksMessages.getString(
							TuleapMylynTasksMessagesKeys.errorReturnedByServerWithDebug, getUrl(),
							getMethodName(), Integer.valueOf(errorPart.getCode()), errorPart.getMessage(),
							debugPart.getSource());
				} else {
					msg = TuleapMylynTasksMessages.getString(
							TuleapMylynTasksMessagesKeys.errorReturnedByServer, getUrl(), getMethodName(),
							Integer.valueOf(errorPart.getCode()), errorPart.getMessage());
				}
			}
			throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, msg));
		}
	}

	/**
	 * Sets the authentication token to use for the request.
	 * 
	 * @param someToken
	 *            The token to use. Can be <code>null</code> if no token is needed.
	 * @return The instance on which this method has been called, for a fluent API.
	 */
	public RestOperation withToken(TuleapToken someToken) {
		this.token = someToken;
		return this;
	}

	/**
	 * Sets the body to send in the request.
	 * 
	 * @param someBody
	 *            The body to send.
	 * @return The instance on which this method has been called, for a fluent API.
	 */
	public RestOperation withBody(String someBody) {
		this.body = someBody;
		return this;
	}

	/**
	 * Adds a header property to send in the request.
	 * 
	 * @param key
	 *            The key of the header property to send.
	 * @param value
	 *            The value of the header property. If there is already one entry for this key, it is
	 *            replaced.
	 * @return The instance on which this method has been called, for a fluent API.
	 */
	public RestOperation withHeader(String key, String value) {
		this.requestHeaders.put(key, value);
		return this;
	}

	/**
	 * Adds properties to the header to send in the request.
	 * 
	 * @param someHeaders
	 *            The headers to add to the resource. Existing entries are replaced if needed but never
	 *            removed. The given map's entries are added to the existing map of headers.
	 * @return The instance on which this method has been called, for a fluent API.
	 */
	public RestOperation withHeaders(Map<String, String> someHeaders) {
		this.requestHeaders.putAll(requestHeaders);
		return this;
	}

	/**
	 * Adds one query parameter.
	 * 
	 * @param key
	 *            The key of the parameter.
	 * @param values
	 *            The values for the parameter. Former entries are removed if there were any.
	 * @return The instance on which this method has been called, for a fluent API.
	 */
	public RestOperation withQueryParameter(String key, String... values) {
		requestParameters.replaceValues(key, Arrays.asList(values));
		return this;
	}

	/**
	 * Adds query parameters to this REST resource.
	 * 
	 * @param queryParameters
	 *            The query parameters to add to the resource.
	 * @return The instance on which this method has been called, for a fluent API.
	 */
	public RestOperation withQueryParameters(Multimap<String, String> queryParameters) {
		requestParameters.putAll(queryParameters);
		return this;
	}

	/**
	 * Clears all query parameters for this resource.
	 * 
	 * @return The instance on which this method has been called, for a fluent API.
	 */
	public RestOperation withoutQueryParameter() {
		requestParameters.clear();
		return this;
	}

	/**
	 * Clears the query parameters with the given key.
	 * 
	 * @param key
	 *            Key to remove from the query parameters, all entries will be removed for this key.
	 * @return The instance on which this method has been called, for a fluent API.
	 */
	public RestOperation withoutQueryParameters(String key) {
		requestParameters.removeAll(key);
		return this;
	}
}
