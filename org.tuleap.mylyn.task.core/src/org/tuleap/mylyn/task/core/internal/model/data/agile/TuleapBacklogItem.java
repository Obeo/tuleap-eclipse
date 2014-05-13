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
package org.tuleap.mylyn.task.core.internal.model.data.agile;

import java.util.Date;

import org.tuleap.mylyn.task.core.internal.model.data.AbstractTuleapDetailedElement;
import org.tuleap.mylyn.task.core.internal.model.data.ArtifactReference;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapReference;

/**
 * A backlog item in a milestone.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapBacklogItem extends AbstractTuleapDetailedElement<Integer> {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -9160665953597596100L;

	/**
	 * ID of the artifact that contains this item's details.
	 */
	private ArtifactReference artifact;

	/**
	 * The parent backlogItem reference.
	 */
	private ArtifactReference parent;

	/**
	 * The initial effort.
	 */
	private String initialEffort;

	/**
	 * The type.
	 */
	private String type;

	/**
	 * The status of the backlog item.
	 */
	private TuleapStatus status;

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
		super(Integer.valueOf(id), projectRef);
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
		super(Integer.valueOf(id), projectRef, label, url, htmlUrl, creationDate, lastModificationDate);
	}

	// CHECKSTYLE:ON

	/**
	 * Initial effort.
	 * 
	 * @return the initialEffort
	 */
	public String getInitialEffort() {
		return initialEffort;
	}

	/**
	 * Initial effort setter.
	 * 
	 * @param initialEffort
	 *            the initialEffort to set
	 */
	public void setInitialEffort(String initialEffort) {
		this.initialEffort = initialEffort;
	}

	/**
	 * Type getter.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Tyoe setter.
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	public TuleapStatus getStatus() {
		return status;
	}

	/**
	 * Status setter.
	 * 
	 * @param status
	 *            the status to set
	 */
	public void setStatus(TuleapStatus status) {
		this.status = status;
	}

	/**
	 * Get the parent backlogItem reference.
	 * 
	 * @return the parent
	 */
	public ArtifactReference getParent() {
		return parent;
	}

	/**
	 * Set the parent backlogItem reference.
	 * 
	 * @param parent
	 *            The parent backlogItem reference.
	 */
	public void setParent(ArtifactReference parent) {
		this.parent = parent;
	}

	/**
	 * Indicates whether this backlog item is closed. If no status is set in the backlog item, it is
	 * considered open by default.
	 * 
	 * @return <code>true</code> if and only if the item's status is {@code CLOSED}.
	 */
	public boolean isClosed() {
		return status == TuleapStatus.Closed;
	}
}
