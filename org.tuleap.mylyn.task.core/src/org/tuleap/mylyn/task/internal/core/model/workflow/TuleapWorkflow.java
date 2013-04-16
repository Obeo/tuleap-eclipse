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
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the details of the Tuleap workflow of a specific field.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapWorkflow implements Serializable {
	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = -8494847603902677049L;

	/**
	 * The transitions of the workflow.
	 */
	private List<TuleapWorkflowTransition> transitions = new ArrayList<TuleapWorkflowTransition>();

	/**
	 * Returns the transitions of the workflow.
	 * 
	 * @return The transitions of the workflow.
	 */
	public List<TuleapWorkflowTransition> getTransitions() {
		return this.transitions;
	}

	/**
	 * Returns the list of accessible state from the given state.
	 * 
	 * @param from
	 *            The state from which we need to compute the accessible states.
	 * @return The list of accessible state from the given state.
	 */
	public List<Integer> accessibleStates(int from) {
		List<Integer> accessibleStates = new ArrayList<Integer>();

		for (TuleapWorkflowTransition transition : this.transitions) {
			if (from == transition.getFrom()) {
				accessibleStates.add(Integer.valueOf(transition.getTo()));
			}
		}

		return accessibleStates;
	}
}
