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
 * POJO representing debug info received from Tuleap in error messages.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapDebugPart {

	/**
	 * The source of the message.
	 */
	private String source;

	/**
	 * The stages that have been run.
	 */
	private TuleapStages stages;

	/**
	 * The source.
	 * 
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * The source.
	 * 
	 * @return the stages
	 */
	public TuleapStages getStages() {
		return stages;
	}

}
