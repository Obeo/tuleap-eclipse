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
public class TuleapMilestone extends AbstractTuleapConfigurableElement implements IPlanning {

	/**
	 * The value used is there are no parent milestone id.
	 */
	public static final int INVALID_PARENT_MILESTONE_ID = -1;

	/**
	 * The milestone's start date.
	 */
	private Date startDate;

	/**
	 * The milestone's duration.
	 */
	private Float duration;

	/**
	 * The milestone's capacity.
	 */
	private Float capacity;

	/**
	 * The identifier of the parent milestone.
	 */
	private int parentMilestoneId;

	/**
	 * The milestone's cardwall, if present. Can be null.
	 */
	private TuleapCardwall cardwall;

	/**
	 * The milestone's sub-milestones.
	 */
	private List<TuleapMilestone> subMilestones = Lists.newArrayList();

	/**
	 * The milestone's backlog items (including those assigned to sub-milestones).
	 */
	private List<TuleapBacklogItem> backlogItems = Lists.newArrayList();

	/**
	 * The constructor used when we are creating a new milestone.
	 * 
	 * @param milestoneTypeId
	 *            The identifier of the milestone type
	 * @param projectId
	 *            The identifier of the project
	 * @param parentMilestoneId
	 *            The identifier of the parent milestone or INVALID_PARENT_MILESTONE_ID if there are no parent
	 */
	public TuleapMilestone(int milestoneTypeId, int projectId, int parentMilestoneId) {
		super(milestoneTypeId, projectId);
		this.parentMilestoneId = parentMilestoneId;
	}

	/**
	 * The constructor used when we are updating a milestone.
	 * 
	 * @param milestoneId
	 *            The identifier of the milestone
	 * @param projectId
	 *            The identifier of the project
	 * @param milestoneType
	 *            The milestone type
	 */
	public TuleapMilestone(int milestoneId, int projectId, TuleapMilestoneType milestoneType) {
		super(milestoneId, milestoneType.getIdentifier(), projectId);
	}

	/**
	 * The constructor used when we are retrieving a milestone from the server.
	 * 
	 * @param milestoneId
	 *            The identifier of the milestone
	 * @param milestoneTypeId
	 *            The identifier of the milestone type
	 * @param projectId
	 *            The identifier of the project
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
	// CHECKSTYLE:OFF
	public TuleapMilestone(int milestoneId, int milestoneTypeId, int projectId, String label, String url,
			String htmlUrl, Date creationDate, Date lastModificationDate) {
		super(milestoneId, milestoneTypeId, projectId, label, url, htmlUrl, creationDate,
				lastModificationDate);
	}

	// CHECKSTYLE:ON

	/**
	 * Returns the identifier of the parent milestone or INVALID_PARENT_MILESTONE_ID if there is no parent for
	 * this milestone.
	 * 
	 * @return The identifier of the parent milestone or INVALID_PARENT_MILESTONE_ID if there is no parent for
	 *         this milestone
	 */
	public int getParentMilestoneId() {
		return this.parentMilestoneId;
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
	public Float getDuration() {
		return duration;
	}

	/**
	 * Duration setter.
	 * 
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(Float duration) {
		this.duration = duration;
	}

	/**
	 * capacity getter.
	 * 
	 * @return the capacity
	 */
	public Float getCapacity() {
		return capacity;
	}

	/**
	 * Capacity setter.
	 * 
	 * @param capacity
	 *            the capacity to set
	 */
	public void setCapacity(Float capacity) {
		this.capacity = capacity;
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

	/**
	 * Card wall getter, can return null.
	 * 
	 * @return The milestone's cardwall, or null if the milestone has no cardwall;
	 */
	public TuleapCardwall getCardwall() {
		return cardwall;
	}

	/**
	 * Card wall setter.
	 * 
	 * @param cardwall
	 *            The card wall.
	 */
	public void setCardwall(TuleapCardwall cardwall) {
		this.cardwall = cardwall;
	}

}
