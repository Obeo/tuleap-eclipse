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
package org.tuleap.mylyn.task.internal.core.model.config;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

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
	 * Map of transitions.
	 */
	private final Multimap<TuleapSelectBoxItem, TuleapSelectBoxItem> transitions = LinkedHashMultimap
			.create();

	/**
	 * Set of possible initial states.
	 */
	private final Set<TuleapSelectBoxItem> initialStates = Sets.newLinkedHashSet();

	/**
	 * The select box on which the workflow applies.
	 */
	private final TuleapSelectBox selectBox;

	/**
	 * Constructor.
	 * 
	 * @param aSelectBox
	 *            the select box on which this workflow applies.
	 */
	public TuleapWorkflow(TuleapSelectBox aSelectBox) {
		this.selectBox = aSelectBox;
	}

	/**
	 * Adds a transition to the workflow.
	 * 
	 * @param transition
	 *            the transition to add.
	 * @return {@code true} if and only if the transition's ends match existing options of the associated
	 *         select box (or the initial pseudo-state) and that the same transition was not already
	 *         registered, {@code false} otherwise.
	 */
	public boolean addTransition(TuleapWorkflowTransition transition) {
		if (ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID == transition.getFrom()) {
			return addInitialStateTransition(transition.getTo());
		}
		boolean result = false;
		TuleapSelectBoxItem fromItem = selectBox.getItem(String.valueOf(transition.getFrom()));
		TuleapSelectBoxItem toItem = selectBox.getItem(String.valueOf(transition.getTo()));
		if (fromItem != null && toItem != null) {
			result = transitions.put(fromItem, toItem);
		}
		return result;
	}

	/**
	 * Adds a transition to the workflow from the initial pseudo-state to the given state.
	 * 
	 * @param to
	 *            the target of the transition.
	 * @return {@code true} if and only if the transition's ends match existing options of the associated
	 *         select box (or the initial pseudo-state) and that the same transition was not already
	 *         registered, {@code false} otherwise.
	 */
	private boolean addInitialStateTransition(int to) {
		TuleapSelectBoxItem toItem = selectBox.getItem(String.valueOf(to));
		if (toItem != null) {
			return initialStates.add(toItem);
		}
		return false;
	}

	/**
	 * Returns the list of accessible state from the given state.
	 * 
	 * @param from
	 *            The state from which we need to compute the accessible states.
	 * @return The list of accessible state from the given state.
	 */
	public Collection<TuleapSelectBoxItem> accessibleStates(int from) {
		if (ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID == from) {
			return Collections.unmodifiableCollection(initialStates);
		}
		return transitions.get(selectBox.getItem(String.valueOf(from)));
	}

	/**
	 * Indicates whether this workflow really has transitions.
	 * 
	 * @return {@code true} if and only if this workflow has at least one transition.
	 */
	public boolean hasTransitions() {
		return !(transitions.isEmpty() && initialStates.isEmpty());
	}
}
