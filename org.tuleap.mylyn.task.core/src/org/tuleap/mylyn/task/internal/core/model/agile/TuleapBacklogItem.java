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

import java.util.Date;

import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableElement;

/**
 * A backlog item in a milestone.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapBacklogItem extends AbstractTuleapConfigurableElement {

	/**
	 * The initial effort.
	 */
	private Float initialEffort;

	/**
	 * The id of the milestone to which the backlog item is assigned. {@code null} indicates that the backlog
	 * item is NOT assigned to any milestone.
	 */
	private Integer assignedMilestoneId;

	/**
	 * The constructor used when we are trying to create a new backlog item locally.
	 * 
	 * @param backlogItemTypeId
	 *            The identifier of the backlog item type
	 * @param projectId
	 *            The identifier of the project
	 */
	public TuleapBacklogItem(int backlogItemTypeId, int projectId) {
		super(backlogItemTypeId, projectId);
	}

	/**
	 * The constructor used when we are updating an existing artifact.
	 * 
	 * @param backlogItemId
	 *            The identifier of the backlog item
	 * @param backlogItemTypeId
	 *            The identifier of the backlog item type
	 * @param projectId
	 *            The identifier of the project
	 */
	public TuleapBacklogItem(int backlogItemId, int backlogItemTypeId, int projectId) {
		super(backlogItemId, backlogItemTypeId, projectId);
	}

	/**
	 * The constructor used to create a backlog item retrieved from the server.
	 * 
	 * @param backlogItemId
	 *            The identifier
	 * @param backlogItemTypeId
	 *            The identifier of the backlog item type
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
	public TuleapBacklogItem(int backlogItemId, int backlogItemTypeId, int projectId, String label,
			String url, String htmlUrl, Date creationDate, Date lastModificationDate) {
		super(backlogItemId, backlogItemTypeId, projectId, label, url, htmlUrl, creationDate,
				lastModificationDate);
	}

	// CHECKSTYLE:ON

	/**
	 * Initial effort.
	 * 
	 * @return the initialEffort
	 */
	public Float getInitialEffort() {
		return initialEffort;
	}

	/**
	 * Initial effort setter.
	 * 
	 * @param initialEffort
	 *            the initialEffort to set
	 */
	public void setInitialEffort(Float initialEffort) {
		this.initialEffort = initialEffort;
	}

	/**
	 * Gets the id of the milestone this backlog item is assigned to.
	 * 
	 * @return the assignedMilestoneId
	 */
	public Integer getAssignedMilestoneId() {
		return assignedMilestoneId;
	}

	/**
	 * Sets the id of the milestone this backlog item is assigned to.
	 * 
	 * @param assignedMilestoneId
	 *            the assignedMilestoneId to set
	 */
	public void setAssignedMilestoneId(Integer assignedMilestoneId) {
		this.assignedMilestoneId = assignedMilestoneId;
	}

}
