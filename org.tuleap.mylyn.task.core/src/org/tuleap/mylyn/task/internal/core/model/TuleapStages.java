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
package org.tuleap.mylyn.task.internal.core.model;

/**
 * POJO representing the run stages and their status.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapStages {

	/**
	 * The successful stages that have been run.
	 */
	private String[] success;

	/**
	 * The failed stages that have been run.
	 */
	private String[] failure;

	/**
	 * Successful stages.
	 * 
	 * @return the success
	 */
	public String[] getSuccess() {
		return success;
	}

	/**
	 * Failed stages.
	 * 
	 * @return the failure
	 */
	public String[] getFailure() {
		return failure;
	}

}
