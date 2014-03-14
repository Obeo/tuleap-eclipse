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
package org.eclipse.mylyn.tuleap.core.internal.model;

/**
 * Error message received from Tuleap.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapErrorMessage {

	/**
	 * The error part of the message.
	 */
	private TuleapErrorPart error;

	/**
	 * The debug part of the message.
	 */
	private TuleapDebugPart debug;

	/**
	 * The error part of the message.
	 * 
	 * @return the error
	 */
	public TuleapErrorPart getError() {
		return error;
	}

	/**
	 * The error part of the message.
	 * 
	 * @param error
	 *            the error to set
	 */
	public void setError(TuleapErrorPart error) {
		this.error = error;
	}

	/**
	 * The debug part.
	 * 
	 * @return the debug
	 */
	public TuleapDebugPart getDebug() {
		return debug;
	}

	/**
	 * The debug part.
	 * 
	 * @param debug
	 *            the debug to set
	 */
	public void setDebug(TuleapDebugPart debug) {
		this.debug = debug;
	}

}
