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

import org.tuleap.mylyn.task.internal.core.model.data.AbstractTuleapConfigurableElement;
import org.tuleap.mylyn.task.internal.core.model.data.ArtifactReference;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;

/**
 * A card in a cardwall.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapCard extends AbstractTuleapConfigurableElement<String> {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -9085306379796432358L;

	/**
	 * The card accent color.
	 */
	private String accentColor;

	/**
	 * The card column identifier.
	 */
	private Integer columnId;

	/**
	 * The card planning identifier.
	 */
	private Integer planningId;

	/**
	 * Reference to the artifact that contains this item's details.
	 */
	private ArtifactReference artifact;

	/**
	 * The status of the card.
	 */
	private TuleapStatus status;

	/**
	 * The allowed column identifiers of the card.
	 */
	private int[] allowedColumnIds;

	/**
	 * Default constructor for deserialization.
	 */
	public TuleapCard() {
		// Default constructor for deserialization.
	}

	/**
	 * The constructor used when we are updating an existing card.
	 * 
	 * @param cardId
	 *            The identifier of the card
	 * @param artifactRef
	 *            The artifact this card represents
	 * @param projectRef
	 *            The project
	 */
	public TuleapCard(String cardId, ArtifactReference artifactRef, TuleapReference projectRef) {
		super(cardId, artifactRef.getTracker(), projectRef);
		this.artifact = artifactRef;
	}

	/**
	 * The configurable constructor used to create a card.
	 * 
	 * @param cardId
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
	public TuleapCard(String cardId, TuleapReference projectRef, String label, String url, String htmlUrl,
			Date creationDate, Date lastModificationDate) {
		super(cardId, projectRef, label, url, htmlUrl, creationDate, lastModificationDate);
	}

	/**
	 * Get the card color.
	 * 
	 * @return the color
	 */
	public String getColor() {
		return this.accentColor;
	}

	/**
	 * Set the card color.
	 * 
	 * @param theColor
	 *            The color
	 */
	public void setColor(String theColor) {
		this.accentColor = theColor;
	}

	/**
	 * Get the card column identifier.
	 * 
	 * @return the card column identifier
	 */
	public Integer getColumnId() {
		return this.columnId;
	}

	/**
	 * Set the card column identifier.
	 * 
	 * @param theColumnId
	 *            The column identifier
	 */
	public void setColumnId(Integer theColumnId) {
		this.columnId = theColumnId;
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
	 * Get the the list of allowed column identifiers.
	 * 
	 * @param columnIds
	 *            he list of allowed column identifiers
	 */
	public void setAllowedColumnIds(int[] columnIds) {
		this.allowedColumnIds = columnIds;
	}

	/**
	 * Get the the list of allowed column identifiers.
	 * 
	 * @return The list of allowed column identifiers.
	 */
	public int[] getAllowedColumnIds() {
		return allowedColumnIds;
	}

}
