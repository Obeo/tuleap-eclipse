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

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * The configuration of a card wall column in Tuleap.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapCardwallColumnConfiguration {

	/**
	 * The column's id.
	 */
	private int id;

	/**
	 * The column's label.
	 */
	private String label;

	/**
	 * Map of available mappingsPerTrackerId per trackerId.
	 */
	private Map<Integer, TuleapCardwallTrackerMapping> mappingsPerTrackerId = Maps.newLinkedHashMap();

	/**
	 * Id getter.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Id setter.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Label getter.
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Label setter.
	 * 
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Add a mapping to this column.
	 * 
	 * @param mapping
	 *            the mapping to add.
	 */
	public void addMapping(TuleapCardwallTrackerMapping mapping) {
		mappingsPerTrackerId.put(Integer.valueOf(mapping.getTrackerId()), mapping);
	}

	/**
	 * Remove a mapping from this column.
	 * 
	 * @param trackerId
	 *            the tracker id of the mapping to remove.
	 */
	public void removeMapping(int trackerId) {
		mappingsPerTrackerId.remove(Integer.valueOf(trackerId));
	}

	/**
	 * Add a mapping to this column.
	 * 
	 * @param trackerId
	 *            the tracker id of the mapping to get.
	 * @return the relevant mapping, or null if none is found.
	 */
	public TuleapCardwallTrackerMapping getMapping(int trackerId) {
		return mappingsPerTrackerId.get(Integer.valueOf(trackerId));
	}

}
