/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.internal.tuleap.core.model.workflow;

import java.io.Serializable;

/**
 * The transition in a Tuleap workflow.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapWorkflowTransition implements Serializable {
	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = 2685146873186682145L;

	/**
	 * The state from which we are starting from.
	 */
	private String from;

	/**
	 * The state that we can go to.
	 */
	private String to;

	/**
	 * The constructor.
	 * 
	 * @param transitionFrom
	 *            The state from which we are starting from
	 * @param transitionTo
	 *            The state that we can go to
	 */
	public TuleapWorkflowTransition(String transitionFrom, String transitionTo) {
		this.from = transitionFrom;
		this.to = transitionTo;
	}

	/**
	 * Returns the state from which we are starting from.
	 * 
	 * @return The state from which we are starting from.
	 */
	public String getFrom() {
		return this.from;
	}

	/**
	 * Returns the state that we can go to.
	 * 
	 * @return The state that we can go to.
	 */
	public String getTo() {
		return this.to;
	}
}
