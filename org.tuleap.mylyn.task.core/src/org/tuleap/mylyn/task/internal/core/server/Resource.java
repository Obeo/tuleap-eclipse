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

/**
 * This enumeration contains all the available resources on the server.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public enum Resource {
	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;.
	 */
	API__OPTIONS(URL.API, Operation.OPTIONS),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;.
	 */
	API__GET(URL.API, Operation.GET),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/login.
	 */
	LOGIN__OPTIONS(URL.LOGIN, Operation.OPTIONS),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;/login.
	 */
	LOGIN__GET(URL.LOGIN, Operation.GET),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/logout.
	 */
	LOGOUT__OPTIONS(URL.LOGIN, Operation.OPTIONS),

	/**
	 * POST &lt;domain&gt;/api/&lt;api_version&gt;/logout.
	 */
	LOGOUT__POST(URL.LOGIN, Operation.POST),

	/**
	 * OPTIONS &lt;domain&gt;/api/&lt;api_version&gt;/projects.
	 */
	PROJECTS__OPTIONS(URL.PROJECTS, Operation.OPTIONS),

	/**
	 * GET &lt;domain&gt;/api/&lt;api_version&gt;/projects.
	 */
	PROJECTS__GET(URL.PROJECTS, Operation.GET);

	/**
	 * The URL of the resource.
	 */
	private final String url;

	/**
	 * The operation of the resource.
	 */
	private final Operation operation;

	/**
	 * the constructor.
	 * 
	 * @param url
	 *            The URL
	 * @param operation
	 *            The operation
	 */
	private Resource(String url, Operation operation) {
		this.url = url;
		this.operation = operation;
	}

	/**
	 * Returns the url of the resource.
	 * 
	 * @return The url of the resource
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Returns the operation of the resource.
	 * 
	 * @return The operation of the resource
	 */
	public Operation getOperation() {
		return operation;
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
		String LOGIN = "/login"; //$NON-NLS-1$

		/**
		 * "/logout".
		 */
		String LOGOUT = "/logout"; //$NON-NLS-1$

		/**
		 * "/projects".
		 */
		String PROJECTS = "/projects"; //$NON-NLS-1$
	}

	/**
	 * This enumeration contains the list of all the available operations in the REST api.
	 * 
	 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
	 */
	public enum Operation {
		/**
		 * Request the description of the resource.
		 */
		OPTIONS,

		/**
		 * Request a representation of the resource.
		 */
		GET,

		/**
		 * Create the resource.
		 */
		POST,

		/**
		 * Replace the representation of the resource with a new one.
		 */
		PUT
	}
}
