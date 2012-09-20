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

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the details of the Tuleap workflow of a specific field.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapWorkflow {
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
	public List<String> accessibleStates(String from) {
		List<String> accessibleStates = new ArrayList<String>();

		for (TuleapWorkflowTransition transition : this.transitions) {
			if (from.equals(transition.getFrom())) {
				accessibleStates.add(transition.getTo());
			}
		}

		return accessibleStates;
	}
}
