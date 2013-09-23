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

import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;

/**
 * This class will be used to establish the connection with the HTTP based Tuleap server.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapRestConnector {

	/**
	 * The serverUrl of the server.
	 */
	private String serverUrl;

	/**
	 * The best API version supported known at development time.
	 */
	private String bestApiVersion;

	/**
	 * The logger.
	 */
	private ILog logger;

	/**
	 * the constructor.
	 * 
	 * @param serverURL
	 *            The URL of the server without any trailing '/'
	 * @param bestApiVersion
	 *            The best API version supported known at development time.
	 * @param logger
	 *            The logger.
	 */
	public TuleapRestConnector(String serverURL, String bestApiVersion, ILog logger) {
		this.serverUrl = serverURL;
		this.bestApiVersion = bestApiVersion;
		this.logger = logger;
	}

	/**
	 * Provides the accessible REST resources with the best version of API possible.
	 * 
	 * @param credentials
	 *            The credentials to use.
	 * @return A new RestResources instance which maps the best known REST API on the server.
	 * @throws CoreException
	 *             If the server of its REST API cannot be accessed for any reason.
	 */
	public RestResources resources(ICredentials credentials) throws CoreException {
		return RestResources.builder(serverUrl, bestApiVersion, credentials);
	}

	/**
	 * Launcher for testing.
	 * 
	 * @param args
	 *            The arguments
	 */
	public static void main(String[] args) {
		// TODO REMOVE THIS§§§!!!!!!!!!
		try {
			TuleapRestConnector connector = new TuleapRestConnector("http://localhost:3001", "v3.14", null); //$NON-NLS-1$//$NON-NLS-2$
			ServerResponse serverResponse = connector.resources(new ICredentials() {

				public String getUserName() {
					return "admin"; //$NON-NLS-1$
				}

				public String getPassword() {
					return "password"; //$NON-NLS-1$
				}
			}).user().get(new HashMap<String, String>(), ""); //$NON-NLS-1$
			System.out.println(serverResponse.getBody());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
