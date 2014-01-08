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

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.CharacterSet;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Preference;
import org.restlet.data.Protocol;
import org.restlet.data.Warning;
import org.restlet.engine.header.Header;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.util.Series;
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
	 * The serverUrl of the server.
	 */
	private final AbstractWebLocation location;

	/**
	 * The logger.
	 */
	private final ILog logger;

	/**
	 * The HTTP client Restlet.
	 */
	private Client client;

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
	}

	/**
	 * Provides this connector's Restlet client.
	 * 
	 * @return This instance's client, lazily creating it if necessary.
	 */
	protected synchronized Client getClient() {
		if (client == null) {
			client = new Client(Protocol.HTTPS);
		}
		return client;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.client.rest.IRestConnector#sendRequest(java.lang.String,
	 *      java.lang.String, java.util.Map, java.lang.String)
	 */
	public ServerResponse sendRequest(String method, String url, Map<String, String> headers, String data) {
		Request request = new Request(Method.valueOf(method), url);

		// debug mode
		boolean debug = false;
		IEclipsePreferences node = InstanceScope.INSTANCE.getNode(ITuleapConstants.TULEAP_PREFERENCE_NODE);
		if (node != null) {
			debug = node.getBoolean(ITuleapConstants.TULEAP_PREFERENCE_DEBUG_MODE, false);
		}

		Preference<CharacterSet> preferenceCharset = new Preference<CharacterSet>(CharacterSet.UTF_8);
		request.getClientInfo().getAcceptedCharacterSets().add(preferenceCharset);

		// TODO Support for gzipped responses?
		// Preference<Encoding> preferenceEncoding = new Preference<Encoding>(Encoding.GZIP);
		// request.getClientInfo().getAcceptedEncodings().add(preferenceEncoding);

		Preference<MediaType> preferenceMediaType = new Preference<MediaType>(MediaType.APPLICATION_JSON);
		request.getClientInfo().getAcceptedMediaTypes().add(preferenceMediaType);

		request.getClientInfo().setAgent(this.getUserAgent());

		ChallengeResponse challengeResponse = getChallengeResponse();
		if (challengeResponse != null) {
			request.setChallengeResponse(challengeResponse);
		}

		ChallengeResponse proxyChallengeResponse = getProxyChallengeResponse();
		if (proxyChallengeResponse != null) {
			request.setProxyChallengeResponse(proxyChallengeResponse);
		}

		Representation entity = new StringRepresentation(data, MediaType.APPLICATION_JSON,
				Language.ENGLISH_US, CharacterSet.UTF_8);
		request.setEntity(entity);

		// Object object = request.getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
		Series<Header> form = new Series<Header>(Header.class);
		request.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS, form);

		for (Entry<String, String> entry : headers.entrySet()) {
			form.add(entry.getKey(), entry.getValue());
		}

		Response response = getClient().handle(request);
		String responseBody = response.getEntityAsText();
		if (debug) {
			debugRestCall(request, response, responseBody);
		}

		Map<String, String> responseHeader = Maps.<String, String> newHashMap();

		Map<String, Object> attributes = response.getAttributes();
		Object formObject = attributes.get(HeaderConstants.ATTRIBUTE_HEADERS);
		if (formObject instanceof Series) {
			Series<?> series = (Series<?>)formObject;
			responseHeader = series.getValuesMap();
		}
		ServerResponse serverResponse = new ServerResponse(response.getStatus().getCode(), responseBody,
				responseHeader);
		return serverResponse;
	}

	/**
	 * Logs a debug message of the REST request/response.
	 * 
	 * @param request
	 *            The request sent
	 * @param response
	 *            The response received
	 * @param responseBody
	 *            The response body
	 */
	private void debugRestCall(Request request, Response response, String responseBody) {
		org.restlet.data.Status responseStatus = response.getStatus();
		StringBuilder b = new StringBuilder();
		b.append("Performed REST call:\n").append(request.getMethod()); //$NON-NLS-1$
		b.append(" ").append(request.getResourceRef().toString(true, true)); //$NON-NLS-1$
		String requestBody = request.getEntityAsText();
		b.append("\nbody:\n"); //$NON-NLS-1$
		if (requestBody != null) {
			b.append(requestBody.replaceAll("\"password\" *: *\".*\"", "\"password\":\"(hidden in debug)\"")); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			b.append(requestBody);
		}
		b.append("\n__________\nresponse:\n"); //$NON-NLS-1$
		b.append(responseStatus).append("\n"); //$NON-NLS-1$
		List<Warning> warnings = response.getWarnings();
		if (warnings != null && !warnings.isEmpty()) {
			b.append("warnings:\n"); //$NON-NLS-1$
			for (Warning warning : warnings) {
				b.append(warning).append("\n"); //$NON-NLS-1$
			}
		}
		b.append("body:\n"); //$NON-NLS-1$
		b.append(responseBody);
		int status = IStatus.INFO;
		if (!responseStatus.isSuccess()) {
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

	/**
	 * Computes the challenge response for HTTP_BASIC with the available credentials.
	 * 
	 * @return A challenge response to use by Restlet for HTTP_BASIC authentication.
	 */
	protected ChallengeResponse getChallengeResponse() {
		AuthenticationCredentials credentials = location.getCredentials(AuthenticationType.HTTP);
		if (credentials != null) {
			return new ChallengeResponse(ChallengeScheme.HTTP_BASIC, credentials.getUserName(), credentials
					.getPassword());
		}
		return null;
	}

	/**
	 * Computes the challenge response for a proxy server with the available credentials.
	 * 
	 * @return A challenge response to use by Restlet for proxy authentication.
	 */
	protected ChallengeResponse getProxyChallengeResponse() {
		AuthenticationCredentials credentials = location.getCredentials(AuthenticationType.PROXY);
		if (credentials != null) {
			return new ChallengeResponse(ChallengeScheme.HTTP_BASIC, credentials.getUserName(), credentials
					.getPassword());
		}
		return null;
	}

	/**
	 * Disposes this connector's resources.
	 */
	public synchronized void dispose() {
		if (client != null) {
			try {
				client.stop();
				// CHECKSTYLE:OFF
			} catch (Exception e) {
				// CHECKSTYLE:ON
			}
			client = null;
		}
	}
}
