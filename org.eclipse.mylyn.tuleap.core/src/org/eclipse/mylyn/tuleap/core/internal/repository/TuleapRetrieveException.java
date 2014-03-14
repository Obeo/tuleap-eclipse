/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.tuleap.core.internal.repository;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.tuleap.core.internal.TuleapCoreActivator;

/**
 * Exception indicating that one or several problems have occurred during the retrieval of data to the Tuleap
 * server.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapRetrieveException extends CoreException {

	/**
	 * The generated serial version UID.
	 */
	private static final long serialVersionUID = 8754355564262309344L;

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            The message to log.
	 */
	public TuleapRetrieveException(String message) {
		super(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, message));
	}

}
