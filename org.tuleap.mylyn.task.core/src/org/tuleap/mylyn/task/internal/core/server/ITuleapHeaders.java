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
 * This interface holds all the headers used by the Tuleap server.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public interface ITuleapHeaders {
	/**
	 * Location.
	 */
	String LOCATION = "Location"; //$NON-NLS-1$

	/**
	 * Allow.
	 */
	String ALLOW = "Allow"; //$NON-NLS-1$

	/**
	 * Access-Control-Allow-Methods.
	 */
	String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods"; //$NON-NLS-1$
}
