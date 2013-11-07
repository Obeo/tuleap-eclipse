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
package org.tuleap.mylyn.task.internal.core.model.data.agile;

import java.util.Date;

import org.tuleap.mylyn.task.internal.core.model.data.AbstractTuleapDetailedElement;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;

/**
 * A backlog item in a milestone.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapBacklogItem extends AbstractTuleapDetailedElement {

	/**
	 * ID of the artifact that contains this item's details.
	 */
	private TuleapReference artifactRef;

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
	 * Default constructor for deserialization.
	 */
	public TuleapBacklogItem() {
		// Default constructor for deserialization.
	}

	/**
	 * The constructor used when we are trying to create a new backlog item locally.
	 * 
	 * @param projectRef
	 *            The reference to the project
	 */
	public TuleapBacklogItem(TuleapReference projectRef) {
		super(projectRef);
	}

	/**
	 * The constructor used when we are updating an existing artifact.
	 * 
	 * @param id
	 *            The identifier of the backlog item
	 * @param projectRef
	 *            The reference to the project
	 */
	public TuleapBacklogItem(int id, TuleapReference projectRef) {
		super(id, projectRef);
	}

	/**
	 * The constructor used to create a backlog item retrieved from the server.
	 * 
	 * @param id
	 *            The identifier
	 * @param projectRef
	 *            The reference to the project
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
	public TuleapBacklogItem(int id, TuleapReference projectRef, String label, String url, String htmlUrl,
			Date creationDate, Date lastModificationDate) {
		super(id, projectRef, label, url, htmlUrl, creationDate, lastModificationDate);
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

	/**
	 * Artifact reference.
	 * 
	 * @return the artifactRef
	 */
	public TuleapReference getArtifactRef() {
		return artifactRef;
	}

	/**
	 * Artifact reference.
	 * 
	 * @param artifactRef
	 *            the artifact reference to set
	 */
	public void setArtifactRef(TuleapReference artifactRef) {
		this.artifactRef = artifactRef;
	}

}
