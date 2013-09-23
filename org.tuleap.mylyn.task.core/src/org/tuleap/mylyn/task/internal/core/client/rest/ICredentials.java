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

/**
 * Represents credentials that can be used for HTTP Basic authentication.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface ICredentials {

	/**
	 * User name getter.
	 * 
	 * @return The user name.
	 */
	String getUserName();

	/**
	 * Password getter.
	 * 
	 * @return The password.
	 */
	String getPassword();
}
