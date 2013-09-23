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
	private float initialEffort;

	/**
	 * The id of the milestone's type.
	 */
	private int typeId;

	/**
	 * The id of the milestone to which the backlog item is assigned. {@code null} indicates that the backlog
	 * item is NOT assigned to any milestone.
	 */
	private Integer assignedMilestoneId;

	/**
	 * The constructor.
	 */
	public TuleapBacklogItem() {
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
	public TuleapBacklogItem(int elementId, String label, String url, String htmlUrl, Date creationDate,
			Date lastModificationDate) {
		super(elementId, label, url, htmlUrl, creationDate, lastModificationDate);
	}

	/**
	 * Initial effort.
	 * 
	 * @return the initialEffort
	 */
	public float getInitialEffort() {
		return initialEffort;
	}

	/**
	 * Initial effort setter.
	 * 
	 * @param initialEffort
	 *            the initialEffort to set
	 */
	public void setInitialEffort(float initialEffort) {
		this.initialEffort = initialEffort;
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
