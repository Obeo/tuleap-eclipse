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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
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
import org.restlet.data.Form;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Preference;
import org.restlet.data.Protocol;
import org.restlet.engine.http.header.HeaderConstants;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessagesKeys;

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
	 * The best API version supported known at development time.
	 */
	private final String bestApiVersion;

	/**
	 * The logger.
	 */
	private final ILog logger;

	/**
	 * the constructor.
	 * 
	 * @param location
	 *            The abstract web location, to support proxies.
	 * @param bestApiVersion
	 *            The best API version supported known at development time.
	 * @param logger
	 *            The logger.
	 */
	public TuleapRestConnector(AbstractWebLocation location, String bestApiVersion, ILog logger) {
		this.location = location;
		this.bestApiVersion = bestApiVersion;
		this.logger = logger;
	}

	/**
	 * Provides the accessible REST resources with the best version of API possible.
	 * 
	 * @return A new RestResources instance which maps the best known REST API on the server.
	 * @throws CoreException
	 *             If the server of its REST API cannot be accessed for any reason.
	 */
	public RestResourceFactory getResourceFactory() throws CoreException {
		IStatus status = null;

		// Try to send a request to the best supported version of the API
		ServerResponse serverResponse = new RestApi(location.getUrl(), bestApiVersion, this).options().run();

		RestResourceFactory result = null;
		switch (serverResponse.getStatus()) {
			case ITuleapServerStatus.OK:
				// If we receive a 200 OK, the connection is good
				result = new RestResourceFactory(location.getUrl(), bestApiVersion, this);
				break;
			case ITuleapServerStatus.MOVED:
				// If we receive a 301 Moved Permanently, a new compatible version of the API is available
				Map<String, String> headers = serverResponse.getHeaders();
				String newApiLocation = headers.get(ITuleapHeaders.LOCATION);

				// Parse the new location to get the api version segment.
				if (newApiLocation != null) {
					int indexOfAPIPrefix = newApiLocation.indexOf(ITuleapAPIVersions.API_PREFIX);
					if (indexOfAPIPrefix != -1) {
						String newApiVersion = newApiLocation.substring(indexOfAPIPrefix
								+ ITuleapAPIVersions.API_PREFIX.length());
						// TODO Send an OPTIONS request to this new URL to check the response
						result = new RestResourceFactory(location.getUrl(), newApiVersion, this);
					} else {
						// Error, invalid behavior of the server, invalid location
						status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
								TuleapMylynTasksMessages.getString(
										TuleapMylynTasksMessagesKeys.invalidAPILocation, bestApiVersion));
					}
				} else {
					// Error, invalid behavior of the server, no new location
					status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
							TuleapMylynTasksMessages.getString(
									TuleapMylynTasksMessagesKeys.missingAPILocation, bestApiVersion));
				}
				break;
			case ITuleapServerStatus.GONE:
				// If we receive a 410 Gone, the server does not support the required API, the connector
				// cannot work with this server
				// TODO How to deserialize the body here?
				String goneErrorMessage = serverResponse.getBody();
				status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, TuleapMylynTasksMessages
						.getString(TuleapMylynTasksMessagesKeys.missingCompatibleAPI, bestApiVersion,
								goneErrorMessage));
				break;
			case ITuleapServerStatus.NOT_FOUND:
				// If we receive a 404 Not Found, the URL of server is invalid or the server is offline
				status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, TuleapMylynTasksMessages
						.getString(TuleapMylynTasksMessagesKeys.aPINotFound));
				break;
			default:
				// Unknown error, invalid behavior of the server?
				status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, TuleapMylynTasksMessages
						.getString(TuleapMylynTasksMessagesKeys.invalidBehavior, Integer
								.valueOf(serverResponse.getStatus())));
				break;
		}
		if (status != null) {
			throw new CoreException(status);
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.client.rest.IRestConnector#sendRequest(java.lang.String,
	 *      java.lang.String, java.util.Map, java.lang.String)
	 */
	public ServerResponse sendRequest(String method, String url, Map<String, String> headers, String data) {
		// TODO Support proxies! (AbstractWebLocation in TuleapRestClient?)
		Request request = new Request(Method.valueOf(method), url);

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

		Representation entity = new StringRepresentation(data, MediaType.APPLICATION_JSON,
				Language.ENGLISH_US, CharacterSet.UTF_8);
		request.setEntity(entity);

		Client c = new Client(Protocol.HTTP);
		Object object = request.getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
		if (object == null) {
			object = new Form();
			// TODO Use headers to retrieve non-standard attributes (pagination)
			request.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS, object);
		}

		Response response = c.handle(request);
		String responseBody = null;
		try {
			if (response.getEntity() != null) {
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				response.getEntity().write(byteArrayOutputStream);
				responseBody = new String(byteArrayOutputStream.toByteArray());
				byteArrayOutputStream.close();
			}
		} catch (IOException e) {
			// do not propagate
			this.logger.log(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e));
		}

		Map<String, String> responseHeader = Maps.<String, String> newHashMap();

		Map<String, Object> attributes = response.getAttributes();
		Object formObject = attributes.get(HeaderConstants.ATTRIBUTE_HEADERS);
		if (formObject instanceof Form) {
			Form form = (Form)formObject;
			responseHeader = form.getValuesMap();
		}
		ServerResponse serverResponse = new ServerResponse(response.getStatus().getCode(), responseBody,
				responseHeader);
		return serverResponse;
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
}
