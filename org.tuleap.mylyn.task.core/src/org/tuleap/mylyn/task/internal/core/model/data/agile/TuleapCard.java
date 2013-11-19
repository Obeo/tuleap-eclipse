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
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;

/**
 * A card in a cardwall.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapCard extends AbstractTuleapConfigurableElement {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -9085306379796432358L;

	/**
	 * The card color.
	 */
	private String color;

	/**
	 * The card status identifier.
	 */
	private int statusId;

	/**
	 * Default constructor for deserialization.
	 */
	public TuleapCard() {
		// Default constructor for deserialization.
	}

	/**
	 * The constructor used when we are trying to create a new card locally.
	 * 
	 * @param trackerRef
	 *            The tracker
	 * @param projectRef
	 *            The project
	 */
	public TuleapCard(TuleapReference trackerRef, TuleapReference projectRef) {
		super(trackerRef, projectRef);
	}

	/**
	 * The constructor used when we are updating an existing card.
	 * 
	 * @param cardId
	 *            The identifier of the card
	 * @param trackerRef
	 *            The tracker
	 * @param projectRef
	 *            The project
	 */
	public TuleapCard(int cardId, TuleapReference trackerRef, TuleapReference projectRef) {
		super(cardId, trackerRef, projectRef);
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
	public TuleapCard(int cardId, TuleapReference projectRef, String label, String url, String htmlUrl,
			Date creationDate, Date lastModificationDate) {
		super(cardId, projectRef, label, url, htmlUrl, creationDate, lastModificationDate);
	}

	/**
	 * Get the card color.
	 * 
	 * @return the color
	 */
	public String getColor() {
		return this.color;
	}

	/**
	 * Set the card color.
	 * 
	 * @param theColor
	 *            The color
	 */
	public void setColor(String theColor) {
		this.color = theColor;
	}

	/**
	 * Get the card status identifier.
	 * 
	 * @return the card status identifier
	 */
	public int getStatusId() {
		return this.statusId;
	}

	/**
	 * Set the card color.
	 * 
	 * @param theStatusId
	 *            The status identifier
	 */
	public void setStatus(int theStatusId) {
		this.statusId = theStatusId;
	}

}
