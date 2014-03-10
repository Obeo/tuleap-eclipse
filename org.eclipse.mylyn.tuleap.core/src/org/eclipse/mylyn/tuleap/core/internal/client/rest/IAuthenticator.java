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
package org.eclipse.mylyn.tuleap.core.internal.client.rest;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.tuleap.core.internal.model.TuleapToken;

/**
 * Authenticator interface.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface IAuthenticator {

	/**
	 * Logs in on a tuleap server and stores the retrieved token to use for REST dialogs.
	 * 
	 * @throws CoreException
	 *             if the provided credentials are invalid.
	 */
	void login() throws CoreException;

	/**
	 * Provides the token to use.
	 * 
	 * @return The current token, that can be null if no credentials exist or if login() has never been
	 *         invoked.
	 */
	TuleapToken getToken();
}
