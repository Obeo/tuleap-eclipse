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

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.util.HttpURLConnection;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.commons.net.WebUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * This class will be used to establish the connection with the HTTP based Tuleap server.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapRestConnector implements IRestConnector {

	/**
	 * Status code used to indicate an IO error during REST communication.
	 */
	public static final int IO_ERROR_STATUS_CODE = 1001;

	/**
	 * The serverUrl of the server.
	 */
	private final AbstractWebLocation location;

	/**
	 * The logger.
	 */
	private final ILog logger;

	/**
	 * The HTTP client.
	 */
	private final HttpClient httpClient;

	/**
	 * The host configuration.
	 */
	private HostConfiguration hostConfiguration;

	/**
	 * the constructor.
	 * 
	 * @param location
	 *            The abstract web location, to support proxies.
	 * @param logger
	 *            The logger.
	 */
	public TuleapRestConnector(AbstractWebLocation location, ILog logger) {
		this.location = location;
		this.logger = logger;
		this.httpClient = new HttpClient(WebUtil.getConnectionManager());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.client.rest.IRestConnector#sendRequest(org.apache.commons.httpclient.HttpMethod,
	 *      java.lang.String)
	 */
	public ServerResponse sendRequest(HttpMethod method) {
		// debug mode
		boolean debug = false;
		IEclipsePreferences node = InstanceScope.INSTANCE.getNode(ITuleapConstants.TULEAP_PREFERENCE_NODE);
		if (node != null) {
			debug = node.getBoolean(ITuleapConstants.TULEAP_PREFERENCE_DEBUG_MODE, false);
		}

		if (hostConfiguration == null) {
			hostConfiguration = WebUtil.createHostConfiguration(httpClient, location, null);
		}

		// TODO Support for gzipped responses?

		method.setRequestHeader("Accept", "application/json"); //$NON-NLS-1$ //$NON-NLS-2$
		method.setRequestHeader("Accept-Charset", "UTF-8"); //$NON-NLS-1$ //$NON-NLS-2$
		method.setRequestHeader("Content-Type", "application/json"); //$NON-NLS-1$ //$NON-NLS-2$

		// request.getClientInfo().setAgent(this.getUserAgent());
		WebUtil.configureHttpClient(httpClient, getUserAgent());

		Header[] responseHeaders = null;
		String responseBody = null;
		ServerResponse serverResponse = null;
		try {
			int code = WebUtil.execute(httpClient, hostConfiguration, method, null);
			responseBody = method.getResponseBodyAsString();
			responseHeaders = method.getResponseHeaders();
			if (debug) {
				debugRestCall(method, responseBody);
			}
			Map<String, String> rHeaders = new LinkedHashMap<String, String>();
			for (Header h : responseHeaders) {
				rHeaders.put(h.getName(), h.getValue());
			}
			serverResponse = new ServerResponse(code, responseBody, rHeaders);
		} catch (IOException e) {
			logger.log(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, "I/O error during " + method
					+ ": " + e.getMessage()));
			serverResponse = new ServerResponse(IO_ERROR_STATUS_CODE, "", Collections
					.<String, String> emptyMap());
		} finally {
			// TODO Hack-ish, cast into HttpMethodBase since releaseConnection accepts the abstract class, not
			// the interface.
			WebUtil.releaseConnection((HttpMethodBase)method, null);
		}

		return serverResponse;
	}

	/**
	 * Logs a debug message of the REST request/response.
	 * 
	 * @param method
	 *            The method executed
	 * @param bodyReceived
	 *            The response body received
	 */
	private void debugRestCall(HttpMethod method, String bodyReceived) {
		int responseStatus = method.getStatusCode();
		StringBuilder b = new StringBuilder();
		b.append(method.getName());
		b.append(" ").append(method.getPath()); //$NON-NLS-1$
		if (method instanceof EntityEnclosingMethod) {
			b.append("\nbody:\n").append(((EntityEnclosingMethod)method).getRequestEntity().toString().replaceAll(//$NON-NLS-1$
											"\"password\" *: *\".*\"", "\"password\":\"(hidden in debug)\"")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		b.append("\n__________\nresponse:\n"); //$NON-NLS-1$
		b.append(method.getStatusLine()).append("\n"); //$NON-NLS-1$
		b.append("body:\n"); //$NON-NLS-1$
		b.append(bodyReceived);
		int status = IStatus.INFO;
		if (responseStatus != HttpURLConnection.HTTP_OK) {
			status = IStatus.ERROR;
		}
		this.logger.log(new Status(status, TuleapCoreActivator.PLUGIN_ID, b.toString()));
	}

	/**
	 * Returns the user agent to use for the connection.
	 * 
	 * @return The user agent to use for the connection
	 */
	private String getUserAgent() {
		// Mylyn Connector for Tuleap v2.0.0; Eclipse v3.8; Windows 7 6.1; Java v1.7.0_17 Oracle Corporation
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Mylyn Connector for Tuleap"); //$NON-NLS-1$

		Bundle bundle = Platform.getBundle(TuleapCoreActivator.PLUGIN_ID);
		if (bundle != null) {
			Version version = bundle.getVersion();
			if (version != null) {
				stringBuilder.append(" v"); //$NON-NLS-1$
				stringBuilder.append(version.getMajor());
				stringBuilder.append('.');
				stringBuilder.append(version.getMinor());
				stringBuilder.append('.');
				stringBuilder.append(version.getMicro());
			}
		}

		final String separator = "; "; //$NON-NLS-1$

		bundle = Platform.getBundle("org.eclipse.core.runtime"); //$NON-NLS-1$
		if (bundle != null) {
			Version version = bundle.getVersion();
			if (version != null) {
				stringBuilder.append(separator);
				stringBuilder.append(" Eclipse v"); //$NON-NLS-1$
				stringBuilder.append(version.getMajor());
				stringBuilder.append('.');
				stringBuilder.append(version.getMinor());
				stringBuilder.append('.');
				stringBuilder.append(version.getMicro());
			}
		}

		stringBuilder.append(separator);

		stringBuilder.append(System.getProperty("os.name")); //$NON-NLS-1$
		stringBuilder.append(' ');
		stringBuilder.append(System.getProperty("os.version")); //$NON-NLS-1$

		stringBuilder.append(separator);

		stringBuilder.append(" Java v"); //$NON-NLS-1$
		stringBuilder.append(System.getProperty("java.version")); //$NON-NLS-1$
		stringBuilder.append(' ');
		stringBuilder.append(System.getProperty("java.vendor")); //$NON-NLS-1$

		return stringBuilder.toString();
	}
}
