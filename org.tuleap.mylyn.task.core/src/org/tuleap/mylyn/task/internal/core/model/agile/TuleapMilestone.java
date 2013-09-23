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

import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableElement;

/**
 * A milestone with its backlog items and its sub-milestones.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapMilestone extends AbstractTuleapConfigurableElement {

	/**
	 * The milestone's start date.
	 */
	private long startDate;

	/**
	 * The milestone's duration.
	 */
	private float duration;

	/**
	 * The milestone's capacity.
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
	 * The constructor.
	 */
	public TuleapMilestone() {
		super();
	}

	/**
	 * The constructor.
	 * 
	 * @param elementId
	 *            The identifier
	 * @param label
	 *            The label
	 * @param url
	 *            The API URL
	 * @param htmlUrl
	 *            The URL
	 * @param creationDate
	 *            The creation date
	 * @param lastModificationDate
	 *            The last modification
	 */
	public TuleapMilestone(int elementId, String label, String url, String htmlUrl, Date creationDate,
			Date lastModificationDate) {
		super(elementId, label, url, htmlUrl, creationDate, lastModificationDate);
	}

	/**
	 * Start date getter.
	 * 
	 * @return the startDate
	 */
	public long getStartDate() {
		return startDate;
	}

	/**
	 * Start date setter.
	 * 
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(long startDate) {
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

	/**
	 * Adds a new sub-milestone to this milestone.
	 * 
	 * @param backlogItem
	 *            The backlog item to add.
	 */
	public void addBacklogItem(TuleapBacklogItem backlogItem) {
		this.backlogItems.add(backlogItem);
	}

	/**
	 * Provides the backlog items of this milestone.
	 * 
	 * @return An unmodifiable list view of the backlog items of this milestone.
	 */
	public List<TuleapBacklogItem> getBacklogItems() {
		return ImmutableList.copyOf(this.backlogItems);
	}

}
