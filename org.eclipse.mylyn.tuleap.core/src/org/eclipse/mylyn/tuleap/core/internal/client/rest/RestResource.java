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
package org.eclipse.mylyn.tuleap.core.internal.client.rest;

import com.google.gson.Gson;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.mylyn.tuleap.core.internal.util.TuleapMylynTasksMessages;
import org.eclipse.mylyn.tuleap.core.internal.util.TuleapMylynTasksMessagesKeys;

/**
 * Generic REST Resource.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class RestResource {
	/**
	 * Location.
	 */
	public static final String LOCATION = "Location"; //$NON-NLS-1$

	/**
	 * Allow.
	 */
	public static final String ALLOW = "Allow"; //$NON-NLS-1$

	/**
	 * Access-Control-Allow-Methods.
	 */
	public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods"; //$NON-NLS-1$

	/**
	 * Authorization.
	 */
	public static final String AUTHORIZATION = "Authorization"; //$NON-NLS-1$

	/**
	 * Header key for the property that describes the pagination limit (the maximum number of elements per
	 * page).
	 */
	public static final String HEADER_X_PAGINATION_LIMIT = "X-PAGINATION-LIMIT"; //$NON-NLS-1$

	/**
	 * Default value for pagination limit when header X-PAGINATION-LIMIT-MAX is not present.
	 */
	public static final int DEFAULT_PAGINATION_LIMIT = 50;

	/**
	 * Header key for the property that describes the pagination offset (index of the first element in the
	 * current page in the whole list of elements).
	 */
	public static final String HEADER_X_PAGINATION_OFFSET = "X-PAGINATION-OFFSET"; //$NON-NLS-1$

	/**
	 * Default value for pagination offset when header X-PAGINATION-OFFSET is not present.
	 */
	public static final int DEFAULT_PAGINATION_OFFSET = 0;

	/**
	 * Header key for the property that describes the number of elements in the paginated list.
	 */
	public static final String HEADER_X_PAGINATION_SIZE = "X-PAGINATION-SIZE"; //$NON-NLS-1$

	/**
	 * Header key for the property that describes the authorized pagination limit (the maximum number of
	 * elements per page that is authorized by the server).
	 */
	public static final String HEADER_X_PAGINATION_LIMIT_MAX = "X-PAGINATION-LIMIT-MAX"; //$NON-NLS-1$

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
					TuleapMylynTasksMessagesKeys.operationNotAllowedOnResource, "DELETE", url)); //$NON-NLS-1$
		}
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
					TuleapMylynTasksMessagesKeys.operationNotAllowedOnResource, "GET", getUrl())); //$NON-NLS-1$
		}
		RestOperation operation = RestOperation.get(url, connector, gson, logger).withAuthenticator(
				authenticator);
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
					TuleapMylynTasksMessagesKeys.operationNotAllowedOnResource, "POST", getUrl())); //$NON-NLS-1$
		}
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
					TuleapMylynTasksMessagesKeys.operationNotAllowedOnResource, "PUT", getUrl())); //$NON-NLS-1$
		}
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
		 * "changesets".
		 */
		String CHANGESETS = "changesets"; //$NON-NLS-1$

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
