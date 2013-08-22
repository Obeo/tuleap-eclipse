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
package org.tuleap.mylyn.task.internal.core.model.agile;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * The configuration of state mappings for one tracker in a cardwall column.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapCardwallTrackerMapping {

	/**
	 * The id of the tracker for this mapping.
	 */
	private int trackerId;

	/**
	 * The id of the field that must be used to manage the state value.
	 */
	private int fieldId;

	/**
	 * The list of value ids that correspond to this cardwall column. Each column of a card wall corresponds
	 * to a "state" notion that is mapped to one or several status value ids in the tuleap configuration.
	 */
	private final List<Integer> stateValueIds = Lists.newArrayList();

	/**
	 * Tracker id getter.
	 * 
	 * @return the trackerId
	 */
	public int getTrackerId() {
		return trackerId;
	}

	/**
	 * Tracker id setter.
	 * 
	 * @param trackerId
	 *            the trackerId to set
	 */
	public void setTrackerId(int trackerId) {
		this.trackerId = trackerId;
	}

	/**
	 * Field id getter.
	 * 
	 * @return the fieldId
	 */
	public int getFieldId() {
		return fieldId;
	}

	/**
	 * Field id setter.
	 * 
	 * @param fieldId
	 *            the fieldId to set
	 */
	public void setFieldId(int fieldId) {
		this.fieldId = fieldId;
	}

	/**
	 * State value ids getter.
	 * 
	 * @return An unmodifiable view of the statevalueIds
	 */
	public List<Integer> getStatevalueIds() {
		return Collections.unmodifiableList(stateValueIds);
	}

	/**
	 * Allows to add a state value id.
	 * 
	 * @param statevalueId
	 *            the statevalueId to add
	 */
	public void addStatevalueId(int statevalueId) {
		this.stateValueIds.add(Integer.valueOf(statevalueId));
	}

	/**
	 * Allows to add a list of state value ids.
	 * 
	 * @param statevalueIds
	 *            the statevalueIds to set
	 */
	public void addAllStatevalueIds(List<Integer> statevalueIds) {
		this.stateValueIds.addAll(statevalueIds);
	}
}
