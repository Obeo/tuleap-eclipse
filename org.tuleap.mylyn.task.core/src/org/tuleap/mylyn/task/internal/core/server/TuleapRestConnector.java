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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
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
import org.restlet.resource.ClientResource;

/**
 * This class will be used to establish the connection with the HTTP based Tuleap server.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapRestConnector {

	/**
	 * The prefix of the REST API.
	 */
	public static final String API_PREFIX = "/api/"; //$NON-NLS-1$

	/**
	 * The url of the REST API of the server.
	 */
	private String url;

	/**
	 * the constructor.
	 * 
	 * @param serverURL
	 *            The URL of the server without any trailing '/'
	 */
	public TuleapRestConnector(String serverURL) {
		this.url = serverURL + API_PREFIX;
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
	 */
	public void sendRequest(String apiVersion, Resource resource, Map<String, String> headers, Object data) {
		// process the request
		ClientResource client = new ClientResource(this.url + apiVersion + "/projects/3/trackers/");

		// TODO Support proxies!

		switch (resource.getOperation()) {
			case OPTIONS:
				Request request = new Request(Method.GET, this.url + apiVersion + "/projects/3/trackers/");

				Preference<CharacterSet> preferenceCharset = new Preference<CharacterSet>(CharacterSet.UTF_8);
				request.getClientInfo().getAcceptedCharacterSets().add(preferenceCharset);
				// Preference<Encoding> preferenceEncoding = new Preference<Encoding>(Encoding.GZIP);
				// request.getClientInfo().getAcceptedEncodings().add(preferenceEncoding);
				Preference<MediaType> preferenceMediaType = new Preference<MediaType>(
						MediaType.APPLICATION_JSON);
				request.getClientInfo().getAcceptedMediaTypes().add(preferenceMediaType);

				request.getClientInfo().setAgent(
						"Mylyn Connector for Tuleap - 2.0.0; Eclipse 3.8; Windows 7 86x64; Java SE 7u12");

				Representation entity = new StringRepresentation("{\"hello\": \"world\"}",
						MediaType.APPLICATION_JSON, Language.ENGLISH_US, CharacterSet.UTF_8);
				request.setEntity(entity);

				Client c = new Client(Protocol.HTTP);
				Object object = request.getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
				if (object == null) {
					object = new Form();
					request.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS, object);
				}
				if (object instanceof Form) {
					Form form = (Form)object;
					form.add("X-PAGINATION-OFFSET", "5");
				}

				Response response = c.handle(request);
				System.out.println(response.getAttributes());
				try {
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					response.getEntity().write(byteArrayOutputStream);
					String r = new String(byteArrayOutputStream.toByteArray());
					System.out.println(r);
					byteArrayOutputStream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			case GET:
				Representation get = client.get();
				break;
			case POST:
				Representation post = client.post(null);
				break;
			case PUT:
				Representation put = client.put(null);
				break;
			default:
				break;
		}
	}

	/**
	 * Launcher for testing.
	 * 
	 * @param args
	 *            The arguments
	 */
	public static void main(String[] args) {
		TuleapRestConnector connector = new TuleapRestConnector("http://localhost:3001");
		connector.sendRequest("v3.14", Resource.API__OPTIONS, new HashMap<String, String>(), null);
	}
}
