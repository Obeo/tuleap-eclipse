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
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeResponse;
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
 * Abstract super class of all JSON resources.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractRestResource {

	/**
	 * String to send in the body when no data needs to be in the request body. {@code null} provokes an
	 * exception in Restlet.
	 */
	public static final String EMPTY_BODY = ""; //$NON-NLS-1$

	/**
	 * Separator to use between URL fragment.
	 */
	public static final String SLASH = "/"; //$NON-NLS-1$

	/**
	 * The serverUrl of the REST API on the server, for example {@code http://localhost:3001}.
	 */
	private String serverUrl;

	/**
	 * The REST API version to use.
	 */
	private String apiVersion;

	/**
	 * The logger.
	 */
	private ILog logger;

	/**
	 * Constructor.
	 * 
	 * @param serverUrl
	 *            URL of the rest API on the server.
	 * @param apiVersion
	 *            Version of the REST API to use.
	 */
	protected AbstractRestResource(String serverUrl, String apiVersion) {
		this.serverUrl = serverUrl;
		this.apiVersion = apiVersion;
	}

	/**
	 * Computes the full URL to use to send the request, by concatenating the server address, the root API
	 * prefix, the API version, and the URL fragment of the resource to access.
	 * 
	 * @return The full URL to use to send the request.
	 */
	private String getFullUrl() {
		return this.serverUrl + ITuleapAPIVersions.API_PREFIX + apiVersion + getUrl();
	}

	/**
	 * Returns the URL fragment of the JSON service to access, which will be appended to the
	 * "api/&lt;version&gt;" prefix.
	 * 
	 * @return the URL fragment of the JSON service to append to the "api/&lt;version&gt;" prefix.
	 */
	protected abstract String getUrl();

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
	protected ServerResponse sendRequest(Method method, Map<String, String> headers, String data) {
		// TODO Support proxies! (AbstractWebLocation in TuleapRestClient?)
		Request request = new Request(method, getFullUrl());

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
			// TODO Utiliser headers pour r�cup�rer les attributs non standards (pagination)
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
	 * Method to override in authenticated APIs.
	 * 
	 * @return The ChallengeResponse to use for authentication.
	 */
	protected ChallengeResponse getChallengeResponse() {
		return null;
	}

	/**
	 * Sends an OPTIONS request to the {@code /api/<version>} URL and returns the response.
	 * 
	 * @param headers
	 *            Headers to use for sending the request, just in case. There is no reason why this map
	 *            shouldn't be empty.
	 * @return The received server response, as is.
	 */
	public ServerResponse options(Map<String, String> headers) {
		return sendRequest(Method.OPTIONS, headers, ""); //$NON-NLS-1$
	}

	/**
	 * Performs an OPTIONS request and returns an object capable of saying if a given HTTP operation is
	 * possible. The header should contain the session hash if a session exists in order to get relevant
	 * accreditations.
	 * 
	 * @param method
	 *            The HTTP method for which we want to check accreditation
	 * @param headers
	 *            HTTP headers to use
	 * @throws CoreException
	 *             if this resource is not accessible with the given headers, whatever the reason.
	 */
	protected void checkAccreditation(final Method method, final Map<String, String> headers)
			throws CoreException {
		final ServerResponse response = options(headers);

		// Check the available operations
		final Map<String, String> respHeaders = response.getHeaders();
		final String allowHeaderEntry = respHeaders.get(ITuleapHeaders.ALLOW);
		final String corsAllowMethodsHeaderEntry = respHeaders
				.get(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS);

		// TODO See how to extract the error message since we don't have the jsonParser here.
		// Throw a specific kind of exception that wraps the body and catch it higher where a json
		// parser is available?
		boolean yesYouCan = allowHeaderEntry != null && allowHeaderEntry.contains(method.toString());
		boolean yesYouAreAllowed = corsAllowMethodsHeaderEntry != null
				&& corsAllowMethodsHeaderEntry.contains(method.toString());

		if (ITuleapServerStatus.OK != response.getStatus()) {
			// Server error?
			String message = response.getBody();
			throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, "Error " //$NON-NLS-1$
					+ response.getStatus() + ": " + message)); //$NON-NLS-1$
		}
		if (!yesYouCan) {
			throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
					TuleapMylynTasksMessages.getString(TuleapMylynTasksMessagesKeys.cannotPerformOperation,
							getUrl(), method)));
		}
		if (!yesYouAreAllowed) {
			throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
					TuleapMylynTasksMessages.getString(
							TuleapMylynTasksMessagesKeys.notAuthorizedToPerformOperation, getUrl(), method)));
		}
	}

	/**
	 * This interface contains the list of all the available URL in the REST api.
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
		 * "/login".
		 */
		String USER = "/user"; //$NON-NLS-1$

		/**
		 * "/projects".
		 */
		String PROJECTS = "/projects"; //$NON-NLS-1$

		/**
		 * "/plannings".
		 */
		String PLANNINGS = "/plannings"; //$NON-NLS-1$

		/**
		 * "/top_plannings".
		 */
		String TOP_PLANNINGS = "/top_plannings"; //$NON-NLS-1$

		/**
		 * "/milestones".
		 */
		String MILESTONES = "/milestones"; //$NON-NLS-1$

		/**
		 * "/submilestones".
		 */
		String SUBMILESTONES = "/submilestones"; //$NON-NLS-1$

		/**
		 * "/backlog_items".
		 */
		String BACKLOG_ITEMS = "/backlog_items"; //$NON-NLS-1$

		/**
		 * "/cards".
		 */
		String CARDS = "/cards"; //$NON-NLS-1$

		/**
		 * "/cardwall".
		 */
		String CARDWALL = "/cardwall"; //$NON-NLS-1$

		/**
		 * "/trackers".
		 */
		String TRACKERS = "/trackers"; //$NON-NLS-1$

		/**
		 * "/groups".
		 */
		String GROUPS = "/groups"; //$NON-NLS-1$

		/**
		 * "/members".
		 */
		String MEMBERS = "/members"; //$NON-NLS-1$

		/**
		 * "/users".
		 */
		String USERS = "/users"; //$NON-NLS-1$

		/**
		 * "/reports".
		 */
		String REPORTS = "/reports"; //$NON-NLS-1$

		/**
		 * "/artifacts".
		 */
		String ARTIFACTS = "/artifacts"; //$NON-NLS-1$

		/**
		 * "/files".
		 */
		String FILES = "/files"; //$NON-NLS-1$

		/**
		 * "/chunked_upload".
		 */
		String CHUNKED_UPLOAD = "chunked_upload"; //$NON-NLS-1$

		/**
		 * "/backlog_item_types".
		 */
		String BACKLOG_ITEM_TYPES = "/backlog_item_types"; //$NON-NLS-1$

		/**
		 * "/milestone_types".
		 */
		String MILESTONE_TYPES = "/milestone_types"; //$NON-NLS-1$

		/**
		 * "/milestone_types".
		 */
		String CARD_TYPES = "/card_types"; //$NON-NLS-1$

	}
}
