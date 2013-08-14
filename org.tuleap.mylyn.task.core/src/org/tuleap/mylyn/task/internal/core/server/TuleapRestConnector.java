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
package org.tuleap.mylyn.task.internal.core.server;

import com.google.common.collect.Maps;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

/**
 * This class will be used to establish the connection with the HTTP based Tuleap server.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapRestConnector {

	/**
	 * The url of the REST API of the server.
	 */
	private String url;

	/**
	 * The logger.
	 */
	private ILog logger;

	/**
	 * the constructor.
	 * 
	 * @param serverURL
	 *            The URL of the server without any trailing '/'
	 * @param logger
	 *            The logger.
	 */
	public TuleapRestConnector(String serverURL, ILog logger) {
		this.url = serverURL;
		this.logger = logger;
	}

	/**
	 * Sends a HTTP-based request to the server for the given resource with the given headers and the given
	 * data.
	 * 
	 * @param apiVersion
	 *            The version of the API to use
	 * @param resource
	 *            The resource for which we are sending a request
	 * @param headers
	 *            The headers of our request
	 * @param data
	 *            The data sent
	 * @return The response of the server
	 */
	public ServerResponse sendRequest(String apiVersion, Resource resource, Map<String, String> headers,
			String data) {
		// TODO Support proxies! (AbstractWebLocation in TuleapServer?)
		Method method = this.getMethodFromResource(resource);
		Request request = new Request(method, this.url + ITuleapAPIVersions.API_PREFIX + apiVersion
				+ resource.getUrl());

		Preference<CharacterSet> preferenceCharset = new Preference<CharacterSet>(CharacterSet.UTF_8);
		request.getClientInfo().getAcceptedCharacterSets().add(preferenceCharset);

		// TODO Support for gzipped responses?
		// Preference<Encoding> preferenceEncoding = new Preference<Encoding>(Encoding.GZIP);
		// request.getClientInfo().getAcceptedEncodings().add(preferenceEncoding);

		Preference<MediaType> preferenceMediaType = new Preference<MediaType>(MediaType.APPLICATION_JSON);
		request.getClientInfo().getAcceptedMediaTypes().add(preferenceMediaType);

		request.getClientInfo().setAgent(this.getUserAgent());

		String authorizationValue = headers.get(ITuleapHeaders.AUTHORIZATION);
		if (authorizationValue != null) {
			ChallengeResponse challengeResponse = new ChallengeResponse(ChallengeScheme.HTTP_BASIC);
			request.setChallengeResponse(challengeResponse);
		}

		Representation entity = new StringRepresentation(data, MediaType.APPLICATION_JSON,
				Language.ENGLISH_US, CharacterSet.UTF_8);
		request.setEntity(entity);

		Client c = new Client(Protocol.HTTP);
		Object object = request.getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
		if (object == null) {
			object = new Form();
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
	 * Returns the user agent used for the connection.
	 * 
	 * @return The user agent used for the connection
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
	 * Returns a RESTlet method from the Tuleap resource.
	 * 
	 * @param resource
	 *            The resource
	 * @return The method.
	 */
	private Method getMethodFromResource(Resource resource) {
		Method method = null;
		switch (resource.getOperation()) {
			case OPTIONS:
				method = Method.OPTIONS;
				break;
			case GET:
				method = Method.GET;
				break;
			case PUT:
				method = Method.PUT;
				break;
			case POST:
				method = Method.POST;
				break;
			default:
				method = Method.OPTIONS;
				break;
		}
		return method;
	}

	/**
	 * Launcher for testing.
	 * 
	 * @param args
	 *            The arguments
	 */
	public static void main(String[] args) {
		// TODO REMOVE THIS§§§!!!!!!!!!
		TuleapRestConnector connector = new TuleapRestConnector("http://localhost:3001", null);
		ServerResponse serverResponse = connector.sendRequest("v3.14", Resource.LOGIN__GET,
				new HashMap<String, String>(), "{\"user_name\":\"admin\",\"password\":\"password\"}");
		System.out.println(serverResponse.getBody());
	}
}
