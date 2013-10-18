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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessagesKeys;

/**
 * Abstract RESTful operation.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractRestOperation implements IRestOperation {

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
	 * HTTP headers to send.
	 */
	protected final Map<String, String> requestHeaders = Maps.newTreeMap();

	/**
	 * {@link ListMultimap} of query parameters to append to the URL.
	 */
	protected final ListMultimap<String, String> requestParameters = ArrayListMultimap.create();

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
	 * Constructor.
	 * 
	 * @param fullUrl
	 *            The full URL of the resource to connect to.
	 * @param connector
	 *            the connector to use to "task to" the server.
	 */
	public AbstractRestOperation(String fullUrl, IRestConnector connector) {
		this.fullUrl = fullUrl;
		this.connector = connector;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.client.rest.IRestOperation#getUrl()
	 */
	public String getUrl() {
		return fullUrl;
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
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.client.rest.IRestOperation#run()
	 */
	public ServerResponse run() {
		if (body == null) {
			return sendRequest(getMethodName(), requestHeaders, EMPTY_BODY);
		}
		return sendRequest(getMethodName(), requestHeaders, body);
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
		if (ITuleapServerStatus.OK != response.getStatus()) {
			String message = TuleapMylynTasksMessages.getString(
					TuleapMylynTasksMessagesKeys.errorReturnedByServer, getUrl(), getMethodName(),
					getErrorMessage(response.getBody()));
			throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, message));
		}
	}

	/**
	 * Parse the JSON representation of an error and returns its message.
	 * 
	 * @param jsonResponse
	 *            The JSON representation of the error
	 * @return The error message
	 */
	protected String getErrorMessage(String jsonResponse) {
		try {
			JsonParser parser = new JsonParser();
			JsonObject element = parser.parse(jsonResponse).getAsJsonObject();
			return TuleapMylynTasksMessages.getString(TuleapMylynTasksMessagesKeys.jsonErrorMessage, element
					.get("code"), element.get("message")); //$NON-NLS-1$//$NON-NLS-2$
			// CHECKSTYLE:OFF
			// We need to be able to provide something even if the parsing of the error message fails
		} catch (Exception e) {
			// CHECKSTYLE:ON
			TuleapCoreActivator.log(e, false);
		}
		return jsonResponse;
	}

	/**
	 * Sets the body to send in the request.
	 * 
	 * @param someBody
	 *            The body to send.
	 * @return The instance on which this method has been called, for a fluent API.
	 */
	public AbstractRestOperation withBody(String someBody) {
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
	public AbstractRestOperation withHeader(String key, String value) {
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
	public AbstractRestOperation withHeaders(Map<String, String> someHeaders) {
		this.requestHeaders.putAll(requestHeaders);
		return this;
	}

	/**
	 * Adds one query parameter.
	 * 
	 * @param key
	 *            The key of the parameter.
	 * @param value
	 *            The value of the parameter. If there is already one or more entries for this key, a new
	 *            entry is added with this key, existing entries are not modified.
	 * @return The instance on which this method has been called, for a fluent API.
	 */
	public AbstractRestOperation withQueryParameter(String key, String value) {
		requestParameters.put(key, value);
		return this;
	}

	/**
	 * Adds query parameters to this REST resource.
	 * 
	 * @param queryParameters
	 *            The query parameters to add to the resource.
	 * @return The instance on which this method has been called, for a fluent API.
	 */
	public AbstractRestOperation withQueryParameters(Multimap<String, String> queryParameters) {
		requestParameters.putAll(queryParameters);
		return this;
	}

	/**
	 * Clears all query parameters for this resource.
	 * 
	 * @return The instance on which this method has been called, for a fluent API.
	 */
	public AbstractRestOperation withoutQueryParameter() {
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
	public AbstractRestOperation withoutQueryParameters(String key) {
		requestParameters.removeAll(key);
		return this;
	}

	/**
	 * Send a request.
	 * 
	 * @param method
	 *            HTTP method to use (OPTIONS, GET, POST, PUT, ...)
	 * @param headers
	 *            Headers to send
	 * @param data
	 *            Data to send
	 * @return The received serve response, as is.
	 */
	protected ServerResponse sendRequest(String method, Map<String, String> headers, String data) {
		return connector.sendRequest(method, getUrlWithQueryParameters(), headers, data);
	}
}
