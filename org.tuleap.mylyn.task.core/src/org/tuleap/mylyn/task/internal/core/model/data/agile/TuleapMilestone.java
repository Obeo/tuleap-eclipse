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
 * A milestone with its backlog items and its sub-milestones.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapMilestone extends AbstractTuleapDetailedElement {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = 3813535438833875372L;

	/**
	 * Reference to the artifact that contains this item's details.
	 */
	private ArtifactReference artifact;

	/**
	 * The milestone's start date.
	 */
	private Date startDate;

	/**
	 * The milestone's end date.
	 */
	private Date endDate;

	/**
	 * The milestone's capacity.
	 */
	private Float capacity;

	/**
	 * The parent milestone.
	 */
	private TuleapReference parent;

	/**
	 * The REST URI to get this milestone's sub-milestones.
	 */
	private String subMilestonesUri;

	/**
	 * The REST URI to get this milestone's backlog items.
	 */
	private String backlogItemsUri;

	/**
	 * The milestone's status label.
	 */
	private String statusValue;

	/**
	 * Default constructor for deserialization.
	 */
	public TuleapMilestone() {
		// Default constructor for deserialization.
	}

	/**
	 * The constructor used when we are creating a milestone.
	 * 
	 * @param projectRef
	 *            The reference to the project
	 */
	public TuleapMilestone(TuleapReference projectRef) {
		super(projectRef);
	}

	/**
	 * The constructor used when we are updating a milestone.
	 * 
	 * @param id
	 *            The identifier of the milestone
	 * @param projectRef
	 *            The reference to the project
	 */
	public TuleapMilestone(int id, TuleapReference projectRef) {
		super(id, projectRef);
	}

	/**
	 * The constructor used when we are retrieving a milestone from the server.
	 * 
	 * @param id
	 *            The identifier of the milestone
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
	public TuleapMilestone(int id, TuleapReference projectRef, String label, String url, String htmlUrl,
			Date creationDate, Date lastModificationDate) {
		super(id, projectRef, label, url, htmlUrl, creationDate, lastModificationDate);
	}

	/**
	 * Artifact ref.
	 * 
	 * @return the artifactRef
	 */
	public ArtifactReference getArtifact() {
		return artifact;
	}

	/**
	 * Artifact ref.
	 * 
	 * @param artifact
	 *            the artifact reference
	 */
	public void setArtifact(ArtifactReference artifact) {
		this.artifact = artifact;
	}

	/**
	 * parent milestone.
	 * 
	 * @param parent
	 *            the parentMilestone to set
	 */
	public void setParent(TuleapReference parent) {
		this.parent = parent;
	}

	/**
	 * Returns the parent milestone or <code>null</code> if there is no parent for this milestone.
	 * 
	 * @return The parent milestone or <code>null</code> if there is no parent for this milestone
	 */
	public TuleapReference getParent() {
		return this.parent;
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
	 * End date getter.
	 * 
	 * @return the startDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * End date setter.
	 * 
	 * @param endDate
	 *            the startDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	 * URI for sub-milestones.
	 * 
	 * @return the subMilestonesUri
	 */
	public String getSubMilestonesUri() {
		return subMilestonesUri;
	}

	/**
	 * URI for sub-milestones.
	 * 
	 * @param subMilestonesUri
	 *            the subMilestonesUri to set
	 */
	public void setSubMilestonesUri(String subMilestonesUri) {
		this.subMilestonesUri = subMilestonesUri;
	}

	/**
	 * URI for backlog items.
	 * 
	 * @return the backlogItemsUri
	 */
	public String getBacklogItemsUri() {
		return backlogItemsUri;
	}

	/**
	 * URI for backlog items.
	 * 
	 * @param backlogItemsUri
	 *            the backlogItemsUri to set
	 */
	public void setBacklogItemsUri(String backlogItemsUri) {
		this.backlogItemsUri = backlogItemsUri;
	}

	/**
	 * The status label.
	 * 
	 * @return the status label
	 */
	public String getStatusValue() {
		return statusValue;
	}

	/**
	 * The status label.
	 * 
	 * @param statusValue
	 *            the status label to set
	 */
	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}

}
