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

import com.google.gson.Gson;

import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.model.TuleapDebugPart;
import org.tuleap.mylyn.task.internal.core.model.TuleapErrorMessage;
import org.tuleap.mylyn.task.internal.core.model.TuleapErrorPart;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessagesKeys;

/**
 * Generic REST Resource.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class RestResource {

	/**
	 * Constant containing the name of the HTTP method DELETE.
	 */
	public static final String METHOD_DELETE = "DELETE"; //$NON-NLS-1$

	/**
	 * Constant containing the name of the HTTP method GET.
	 */
	public static final String METHOD_GET = "GET"; //$NON-NLS-1$

	/**
	 * Constant containing the name of the HTTP method OPTIONS.
	 */
	public static final String METHOD_OPTIONS = "OPTIONS"; //$NON-NLS-1$

	/**
	 * Constant containing the name of the HTTP method POST.
	 */
	public static final String METHOD_POST = "POST"; //$NON-NLS-1$

	/**
	 * Constant containing the name of the HTTP method PUT.
	 */
	public static final String METHOD_PUT = "PUT"; //$NON-NLS-1$

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
	 * Flag indicating the DELETE method is supported.
	 */
	public static final int DELETE = 1 << 3;

	/**
	 * Query parameter used to transmit the pagination limit.
	 */
	public static final String LIMIT = "limit"; //$NON-NLS-1$

	/**
	 * Separator used for toString().
	 */
	private static final String SEPARATOR = ", "; //$NON-NLS-1$

	/**
	 * The connector to use.
	 */
	private final IRestConnector connector;

	/**
	 * The logger.
	 */
	private final ILog logger;

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
	 * The {@link Gson} to use.
	 */
	private final Gson gson;

	/**
	 * The authenticator to use.
	 */
	private IAuthenticator authenticator;

	/**
	 * Constructor.
	 * 
	 * @param url
	 *            The URL, must no be null.
	 * @param supportedMethods
	 *            Flags for allowed operations, use for instance {@code GET | POST} to allow both get() and
	 *            post() operations.
	 * @param connector
	 *            The connector to use.
	 * @param gson
	 *            the {@link Gson} to use.
	 * @param logger
	 *            The logger
	 */
	public RestResource(String url, int supportedMethods, IRestConnector connector, Gson gson, ILog logger) {
		Assert.isNotNull(url);
		this.url = url;
		this.supportedMethods = supportedMethods;
		Assert.isNotNull(connector);
		this.connector = connector;
		Assert.isNotNull(gson);
		this.gson = gson;
		Assert.isNotNull(logger);
		this.logger = logger;
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
	 * Creates a DELETE operation for this resource after checking it makes sense by sending an OPTIONS call
	 * to this resource's URL.
	 * 
	 * @return A new instance of {@link RestOperation} created with this resource operation factory.
	 * @throws CoreException
	 *             If a problem occurs while checking whether the DELETE method is supported by the server for
	 *             this resource.
	 */
	public RestOperation delete() throws CoreException {
		if ((supportedMethods & DELETE) == 0) {
			throw new UnsupportedOperationException(TuleapMylynTasksMessages.getString(
					TuleapMylynTasksMessagesKeys.operationNotAllowedOnResource, METHOD_DELETE, url));
		}
		checkOptionsAllows(METHOD_DELETE);
		return RestOperation.delete(url, connector, gson, logger).withAuthenticator(authenticator);
	}

	/**
	 * Creates a GET operation for this resource after checking it makes sense by sending an OPTIONS call to
	 * this resource's URL.
	 * 
	 * @return A new instance of {@link RestOperation} created with this resource operation factory.
	 * @throws CoreException
	 *             If a problem occurs while checking whether the GET method is supported by the server for
	 *             this resource.
	 */
	public RestOperation get() throws CoreException {
		if ((supportedMethods & GET) == 0) {
			throw new UnsupportedOperationException(TuleapMylynTasksMessages.getString(
					TuleapMylynTasksMessagesKeys.operationNotAllowedOnResource, METHOD_GET, getUrl()));
		}
		checkOptionsAllows(METHOD_GET);

		RestOperation operation = RestOperation.get(url, connector, gson, logger).withAuthenticator(
				authenticator);
		final Map<String, String> respHeaders = optionsResponse.getHeaders();
		if (respHeaders.containsKey(ITuleapHeaders.HEADER_X_PAGINATION_SIZE)) {
			String limit;
			if (respHeaders.containsKey(ITuleapHeaders.HEADER_X_PAGINATION_LIMIT_MAX)) {
				String limitMax = respHeaders.get(ITuleapHeaders.HEADER_X_PAGINATION_LIMIT_MAX);
				limit = limitMax;
			} else {
				limit = String.valueOf(ITuleapHeaders.DEFAULT_PAGINATION_LIMIT);
			}
			operation.withQueryParameter(LIMIT, limit);
		}
		return operation;
	}

	/**
	 * Creates an OPTIONS operation for this resource.
	 * 
	 * @return A new instance of {@link RestOperation} created with this resource operation factory.
	 */
	public RestOperation options() {
		return RestOperation.options(url, connector, gson, logger).withAuthenticator(authenticator);
	}

	/**
	 * Creates a POST operation for this resource after checking it makes sense by sending an OPTIONS call to
	 * this resource's URL.
	 * 
	 * @return A new instance of {@link RestOperation} created with this resource operation factory.
	 * @throws CoreException
	 *             If a problem occurs while checking whether the GET method is supported by the server for
	 *             this resource.
	 */
	public RestOperation post() throws CoreException {
		if ((supportedMethods & POST) == 0) {
			throw new UnsupportedOperationException(TuleapMylynTasksMessages.getString(
					TuleapMylynTasksMessagesKeys.operationNotAllowedOnResource, METHOD_POST, getUrl()));
		}
		checkOptionsAllows(METHOD_POST);
		return RestOperation.post(url, connector, gson, logger).withAuthenticator(authenticator);
	}

	/**
	 * Creates a PUT operation for this resource after checking it makes sense by sending an OPTIONS call to
	 * this resource's URL.
	 * 
	 * @return A new instance of {@link RestOperation} created with this resource operation factory.
	 * @throws CoreException
	 *             If a problem occurs while checking whether the GET method is supported by the server for
	 *             this resource.
	 */
	public RestOperation put() throws CoreException {
		if ((supportedMethods & PUT) == 0) {
			throw new UnsupportedOperationException(TuleapMylynTasksMessages.getString(
					TuleapMylynTasksMessagesKeys.operationNotAllowedOnResource, METHOD_PUT, getUrl()));
		}
		checkOptionsAllows(METHOD_PUT);
		return RestOperation.put(url, connector, gson, logger).withAuthenticator(authenticator);
	}

	/**
	 * Sets the authenticator to use.
	 * 
	 * @param anAuthenticator
	 *            The authenticator to use.
	 * @return this, for a fluent API.
	 */
	public RestResource withAuthenticator(IAuthenticator anAuthenticator) {
		this.authenticator = anAuthenticator;
		return this;
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
	private void checkOptionsAllows(String method) throws CoreException {
		if (optionsResponse == null) {
			RestOperation options = options();
			optionsResponse = options.run();
		}
		// Check the available operations
		final Map<String, String> respHeaders = optionsResponse.getHeaders();
		String headerAllows = respHeaders.get(ITuleapHeaders.ALLOW);
		// String headerCorsAllows = respHeaders.get(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS);
		if (!optionsResponse.isOk()) {
			String body = optionsResponse.getBody();
			TuleapErrorMessage message = gson.fromJson(body, TuleapErrorMessage.class);
			String msg;
			if (message == null) {
				msg = TuleapMylynTasksMessages.getString(TuleapMylynTasksMessagesKeys.errorReturnedByServer,
						getUrl(), METHOD_OPTIONS, Integer.valueOf(optionsResponse.getStatus()), body);
			} else {
				TuleapErrorPart errorPart = message.getError();
				TuleapDebugPart debugPart = message.getDebug();
				if (errorPart == null) {
					msg = body;
				} else {
					if (debugPart != null) {
						msg = TuleapMylynTasksMessages.getString(
								TuleapMylynTasksMessagesKeys.errorReturnedByServerWithDebug, url,
								METHOD_OPTIONS, Integer.valueOf(errorPart.getCode()), errorPart.getMessage(),
								debugPart.getSource());
					} else {
						msg = TuleapMylynTasksMessages.getString(
								TuleapMylynTasksMessagesKeys.errorReturnedByServer, url, METHOD_OPTIONS,
								Integer.valueOf(errorPart.getCode()), errorPart.getMessage());
					}
				}
			}
			throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, msg));
		}
		if (!headerAllows.contains(method) && !"*".equals(headerAllows)) { //$NON-NLS-1$
			throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
					TuleapMylynTasksMessages.getString(TuleapMylynTasksMessagesKeys.cannotPerformOperation,
							getUrl(), method)));
		}

		// FIXME uncomment when Tuleap supports CORS attributes in headers
		// Only if it's useful to add this constraint!
		// if (!headerCorsAllows.contains(method.toString())) {
		// throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
		// TuleapMylynTasksMessages.getString(
		// TuleapMylynTasksMessagesKeys.notAuthorizedToPerformOperation, getUrl(), method)));
		// }
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(url).append(" ["); //$NON-NLS-1$
		boolean needComma = false;
		if ((supportedMethods & GET) != 0) {
			b.append("GET"); //$NON-NLS-1$
			needComma = true;
		}
		if ((supportedMethods & PUT) != 0) {
			if (needComma) {
				b.append(SEPARATOR);
			}
			b.append("PUT"); //$NON-NLS-1$
			needComma = true;
		}
		if ((supportedMethods & POST) != 0) {
			if (needComma) {
				b.append(SEPARATOR);
			}
			b.append("POST"); //$NON-NLS-1$
		}
		if ((supportedMethods & DELETE) != 0) {
			if (needComma) {
				b.append(SEPARATOR);
			}
			b.append("DELETE"); //$NON-NLS-1$
		}
		b.append("]"); //$NON-NLS-1$
		return b.toString();
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
		 * "backlog".
		 */
		String BACKLOG = "backlog"; //$NON-NLS-1$

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
		 * "content".
		 */
		String CONTENT = "content"; //$NON-NLS-1$

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
		 * "tokens".
		 */
		String TOKENS = "tokens"; //$NON-NLS-1$

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
		 * "user_groups".
		 */
		String USER_GROUPS = "user_groups"; //$NON-NLS-1$

		/**
		 * "tracker_reports".
		 */
		String TRACKER_REPORTS = "tracker_reports"; //$NON-NLS-1$
	}
}
