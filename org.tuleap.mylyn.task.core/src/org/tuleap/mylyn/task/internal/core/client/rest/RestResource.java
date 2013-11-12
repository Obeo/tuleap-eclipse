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

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessagesKeys;

/**
 * Generic REST Resource.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class RestResource {

	/**
	 * Flag indicating the GET method is supported.
	 */
	public static final int GET = 1;

	/**
	 * Flag indicating the POST method is supported.
	 */
	public static final int POST = 1 << 1;

	/**
	 * Flag indicating the PUT method is supported.
	 */
	public static final int PUT = 1 << 2;

	/**
	 * Label of the GET HTTP method.
	 */
	public static final String GET_LABEL = "GET"; //$NON-NLS-1$

	/**
	 * Label of the POST HTTP method.
	 */
	public static final String POST_LABEL = "POST"; //$NON-NLS-1$

	/**
	 * Label of the PUT HTTP method.
	 */
	public static final String PUT_LABEL = "PUT"; //$NON-NLS-1$

	/**
	 * The serverUrl of the REST API on the server, for example {@code http://localhost:3001}.
	 */
	private final String serverUrl;

	/**
	 * The REST API version to use.
	 */
	private final String apiVersion;

	/**
	 * The connector to use.
	 */
	private final IRestConnector connector;

	/**
	 * The logger.
	 */
	private ILog logger;

	/**
	 * URL of this resource.
	 */
	private final String url;

	/**
	 * Flags indicating allowed operations.
	 */
	private final int supportedMethods;

	/**
	 * Cached ServerResponse received after sending an OPTIONS request to this resource's URL.
	 */
	private ServerResponse optionsResponse;

	/**
	 * Constructor.
	 * 
	 * @param serverUrl
	 *            The server URL.
	 * @param apiVersion
	 *            The API version.
	 * @param url
	 *            The URL, must no be null.
	 * @param supportedMethods
	 *            Flags for allowed operations, use for instance {@code GET | POST} to allow both get() and
	 *            post() operations.
	 * @param connector
	 *            The connector to use.
	 */
	public RestResource(String serverUrl, String apiVersion, String url, int supportedMethods,
			IRestConnector connector) {
		this.serverUrl = serverUrl;
		this.apiVersion = apiVersion;
		Assert.isNotNull(url);
		if (!url.startsWith("/")) { //$NON-NLS-1$
			this.url = '/' + url;
		} else {
			this.url = url;
		}
		this.supportedMethods = supportedMethods;
		Assert.isNotNull(connector);
		this.connector = connector;
	}

	/**
	 * Computes the full URL to use to send the request, by concatenating the server address, the root API
	 * prefix, the API version, and the URL fragment of the resource to access.
	 * 
	 * @return The full URL to use to send the request.
	 */
	public String getFullUrl() {
		return this.serverUrl + ITuleapAPIVersions.API_PREFIX + this.apiVersion + getUrl();
	}

	/**
	 * URL fragment following /api/version, should start with a slash character "/".
	 * 
	 * @return The URL fragment of this resource.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Creates a GET operation for this resource after checking it makes sense by sending an OPTIONS call to
	 * this resource's URL.
	 * 
	 * @return A new instance of {@link RestOpGet} created with this resource operation factory.
	 * @throws CoreException
	 *             If a problem occurs while checking whether the GET method is supported by the server for
	 *             this resource.
	 */
	public RestOpGet get() throws CoreException {
		if ((supportedMethods & GET) == 0) {
			throw new UnsupportedOperationException(TuleapMylynTasksMessages.getString(
					TuleapMylynTasksMessagesKeys.operationNotAllowedOnResource, GET_LABEL, getUrl()));
		}
		checkOptionsAllows(GET_LABEL);
		return new RestOpGet(getFullUrl(), connector);
	}

	/**
	 * Creates an OPTIONS operation for this resource.
	 * 
	 * @return A new instance of {@link RestOpOptions} created with this resource operation factory.
	 */
	public RestOpOptions options() {
		return new RestOpOptions(getFullUrl(), connector);
	}

	/**
	 * Creates a POST operation for this resource after checking it makes sense by sending an OPTIONS call to
	 * this resource's URL.
	 * 
	 * @return A new instance of {@link RestOpPost} created with this resource operation factory.
	 * @throws CoreException
	 *             If a problem occurs while checking whether the GET method is supported by the server for
	 *             this resource.
	 */
	public RestOpPost post() throws CoreException {
		if ((supportedMethods & POST) == 0) {
			throw new UnsupportedOperationException(TuleapMylynTasksMessages.getString(
					TuleapMylynTasksMessagesKeys.operationNotAllowedOnResource, POST_LABEL, getUrl()));
		}
		checkOptionsAllows(POST_LABEL);
		return new RestOpPost(getFullUrl(), connector);
	}

	/**
	 * Creates a PUT operation for this resource after checking it makes sense by sending an OPTIONS call to
	 * this resource's URL.
	 * 
	 * @return A new instance of {@link RestOpPut} created with this resource operation factory.
	 * @throws CoreException
	 *             If a problem occurs while checking whether the GET method is supported by the server for
	 *             this resource.
	 */
	public RestOpPut put() throws CoreException {
		if ((supportedMethods & PUT) == 0) {
			throw new UnsupportedOperationException(TuleapMylynTasksMessages.getString(
					TuleapMylynTasksMessagesKeys.operationNotAllowedOnResource, PUT_LABEL, getUrl()));
		}
		checkOptionsAllows(PUT_LABEL);
		return new RestOpPut(getFullUrl(), connector);
	}

	/**
	 * Performs an OPTIONS request and returns an object capable of saying if a given HTTP operation is
	 * possible. The header should contain the session hash if a session exists in order to get relevant
	 * accreditations.
	 * 
	 * @param method
	 *            The HTTP method for which we want to check accreditation
	 * @throws CoreException
	 *             if this resource is not accessible with the given headers, whatever the reason.
	 */
	protected void checkOptionsAllows(final String method) throws CoreException {
		if (optionsResponse == null) {
			RestOpOptions options = options();
			optionsResponse = options.run();
		}
		// Check the available operations
		final Map<String, String> respHeaders = optionsResponse.getHeaders();
		String headerAllows = respHeaders.get(ITuleapHeaders.ALLOW);
		String headerCorsAllows = respHeaders.get(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS);
		if (!optionsResponse.isOk()) {
			// Server error?
			// TODO See how to extract the error message since we don't have the jsonParser here.
			// Throw a specific kind of exception that wraps the body and catch it higher where a json
			// parser is available?
			String message = optionsResponse.getBody();
			throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
					TuleapMylynTasksMessages.getString(TuleapMylynTasksMessagesKeys.errorReturnedByServer,
							url, "OPTIONS", message))); //$NON-NLS-1$
		}
		if (!headerAllows.contains(method.toString())) {
			throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
					TuleapMylynTasksMessages.getString(TuleapMylynTasksMessagesKeys.cannotPerformOperation,
							getUrl(), method)));
		}
		if (!headerCorsAllows.contains(method.toString())) {
			throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
					TuleapMylynTasksMessages.getString(
							TuleapMylynTasksMessagesKeys.notAuthorizedToPerformOperation, getUrl(), method)));
		}
	}

	/**
	 * This interface contains the list of all the available URL in the REST api of Tuleap.
	 * 
	 * @noextend This class is not intended to be subclassed by clients.
	 * @noinstantiate This class is not intended to be instantiated by clients.
	 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
	 */
	public interface URL {

		/**
		 * "".
		 */
		String API = ""; //$NON-NLS-1$

		/**
		 * "artifacts".
		 */
		String ARTIFACTS = "artifacts"; //$NON-NLS-1$

		/**
		 * "backlog_items".
		 */
		String BACKLOG_ITEMS = "backlog_items"; //$NON-NLS-1$

		/**
		 * "cards".
		 */
		String CARDS = "cards"; //$NON-NLS-1$

		/**
		 * "cardwall".
		 */
		String CARDWALL = "cardwall"; //$NON-NLS-1$

		/**
		 * "chunked_upload".
		 */
		String CHUNKED_UPLOAD = "chunked_upload"; //$NON-NLS-1$

		/**
		 * "files".
		 */
		String FILES = "files"; //$NON-NLS-1$

		/**
		 * "groups".
		 */
		String GROUPS = "groups"; //$NON-NLS-1$

		/**
		 * "members".
		 */
		String MEMBERS = "members"; //$NON-NLS-1$

		/**
		 * "milestones".
		 */
		String MILESTONES = "milestones"; //$NON-NLS-1$

		/**
		 * "plannings".
		 */
		String PLANNINGS = "plannings"; //$NON-NLS-1$

		/**
		 * "projects".
		 */
		String PROJECTS = "projects"; //$NON-NLS-1$

		/**
		 * "reports".
		 */
		String REPORTS = "reports"; //$NON-NLS-1$

		/**
		 * "top_plannings".
		 */
		String TOP_PLANNINGS = "top_plannings"; //$NON-NLS-1$

		/**
		 * "trackers".
		 */
		String TRACKERS = "trackers"; //$NON-NLS-1$

		/**
		 * "user".
		 */
		String USER = "user"; //$NON-NLS-1$

		/**
		 * "users".
		 */
		String USERS = "users"; //$NON-NLS-1$

		/**
		 * "tokens".
		 */

		String TOKENS = "tokens"; //$NON-NLS-1$

	}
}
