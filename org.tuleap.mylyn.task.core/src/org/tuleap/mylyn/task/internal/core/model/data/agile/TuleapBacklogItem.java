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
import org.tuleap.mylyn.task.internal.core.model.data.ArtifactReference;
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
	private ArtifactReference artifact;

	/**
	 * The initial effort.
	 */
	private Float initialEffort;

	/**
	 * The milestone to which the backlog item is assigned. {@code null} indicates that the backlog item is
	 * NOT assigned to any milestone.
	 */
	private TuleapReference assignedMilestone;

	/**
	 * The status of the backlog item.
	 */
	private Status status;

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
	 * Gets the milestone reference this backlog item is assigned to.
	 * 
	 * @return the assignedMilestoneId
	 */
	public TuleapReference getAssignedMilestone() {
		return assignedMilestone;
	}

	/**
	 * Sets the milestone reference this backlog item is assigned to.
	 * 
	 * @param assignedMilestone
	 *            the assigned Milestone reference
	 */
	public void setAssignedMilestone(TuleapReference assignedMilestone) {
		this.assignedMilestone = assignedMilestone;
	}

	/**
	 * Artifact reference.
	 * 
	 * @return the artifact
	 */
	public ArtifactReference getArtifact() {
		return artifact;
	}

	/**
	 * Artifact reference.
	 * 
	 * @param artifact
	 *            the artifact reference to set
	 */
	public void setArtifact(ArtifactReference artifact) {
		this.artifact = artifact;
	}

	/**
	 * Status getter.
	 * 
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * Status setter.
	 * 
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * Indicates whether this backlog item is closed. If no status is set in the backlog item, it is
	 * considered open by default.
	 * 
	 * @return <code>true</code> if and only if the item's status is {@code CLOSED}.
	 */
	public boolean isClosed() {
		return status == Status.CLOSED;
	}

	/**
	 * Backlog item status. Currently, the only statuses that exist are "Open" and "Closed".
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static enum Status {
		/**
		 * Means that the backlog item is closed.
		 */
		CLOSED,
		/**
		 * Means that the backlog item is open.
		 */
		OPEN;

		/**
		 * Provides the enum value for a given JSON string. Captures the knowledge of which value strings
		 * Tuleap provides.
		 * 
		 * @param jsonValue
		 *            The JSON value received from Tuleap.
		 * @return The relevant Status, or <code>null</code> if the given String is unrecognized or null.
		 */
		public static Status fromJsonString(String jsonValue) {
			Status result = null;
			if ("Closed".equalsIgnoreCase(jsonValue)) { //$NON-NLS-1$
				result = CLOSED;
			} else if ("Open".equalsIgnoreCase(jsonValue)) { //$NON-NLS-1$
				result = OPEN;
			}
			return result;

		}
	}

}
