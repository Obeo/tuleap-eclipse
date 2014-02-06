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
package org.tuleap.mylyn.task.internal.core.repository;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;

/**
 * Exception indicating that one or several problems have occurred during the submission of data to the Tuleap
 * server.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapSubmitException extends CoreException {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -1862121310339744209L;

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            The message to log.
	 */
	public TuleapSubmitException(String message) {
		super(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, message));
	}

}
