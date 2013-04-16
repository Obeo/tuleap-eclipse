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
package org.tuleap.mylyn.task.internal.core.model.workflow;

import java.io.Serializable;

/**
 * The transition in a Tuleap workflow.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapWorkflowTransition implements Serializable {
	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = 2685146873186682145L;

	/**
	 * The state from which we are starting from.
	 */
	private int from;

	/**
	 * The state that we can go to.
	 */
	private int to;

	/**
	 * Returns the state from which we are starting from.
	 * 
	 * @return The state from which we are starting from.
	 */
	public int getFrom() {
		return this.from;
	}

	/**
	 * Sets from which state the transition starts.
	 * 
	 * @param transitionStart
	 *            The state from which the transition starts.
	 */
	public void setFrom(int transitionStart) {
		this.from = transitionStart;
	}

	/**
	 * Returns the state that we can go to.
	 * 
	 * @return The state that we can go to.
	 */
	public int getTo() {
		return this.to;
	}

	/**
	 * Sets to which state the transition ends.
	 * 
	 * @param transitionEnd
	 *            The state to which the transition ends.
	 */
	public void setTo(int transitionEnd) {
		this.to = transitionEnd;
	}
}
