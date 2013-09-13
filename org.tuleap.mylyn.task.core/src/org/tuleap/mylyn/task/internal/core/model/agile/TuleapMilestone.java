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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;

/**
 * A milestone with its backlog items and its sub-milestones.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapMilestone extends AbstractTuleapTrackerElement {

	/**
	 * The milestone's label.
	 */
	private String title;

	/**
	 * The milestone's start date.
	 */
	private Date startDate;

	/**
	 * The milestone's start date.
	 */
	private float duration;

	/**
	 * The milestone's start date.
	 */
	private float capacity;

	/**
	 * The id of the milestone's type.
	 */
	private int typeId;

	/**
	 * The milestone's sub-milestones.
	 */
	private List<TuleapMilestone> subMilestones = Lists.newArrayList();

	/**
	 * The milestone's backlog items (including those assigned to sub-milestones).
	 */
	private List<TuleapBacklogItem> backlogItems = Lists.newArrayList();

	/**
	 * Title getter.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Title setter.
	 * 
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Start date getter.
	 * 
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Start date setter.
	 * 
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Duration getter.
	 * 
	 * @return the duration
	 */
	public float getDuration() {
		return duration;
	}

	/**
	 * Duration setter.
	 * 
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(float duration) {
		this.duration = duration;
	}

	/**
	 * capacity getter.
	 * 
	 * @return the capacity
	 */
	public float getCapacity() {
		return capacity;
	}

	/**
	 * Capacity setter.
	 * 
	 * @param capacity
	 *            the capacity to set
	 */
	public void setCapacity(float capacity) {
		this.capacity = capacity;
	}

	/**
	 * typeId getter.
	 * 
	 * @return the typeId
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * typeId setter.
	 * 
	 * @param typeId
	 *            the typeId to set
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	/**
	 * Adds a new sub-milestone to this milestone.
	 * 
	 * @param subMilestone
	 *            The sub-milestone to add.
	 */
	public void addSubMilestone(TuleapMilestone subMilestone) {
		this.subMilestones.add(subMilestone);
	}

	/**
	 * Provides the sub-milestones of this milestone.
	 * 
	 * @return An unmodifiable list view of the sub-milestones of this milestone.
	 */
	public List<TuleapMilestone> getSubMilestones() {
		return ImmutableList.copyOf(this.subMilestones);
	}

}
