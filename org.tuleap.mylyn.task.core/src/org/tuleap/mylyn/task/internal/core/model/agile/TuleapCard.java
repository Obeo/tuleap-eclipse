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
 * The cardwall swimlane.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapCard extends AbstractTuleapConfigurableElement {

	/**
	 * The card color.
	 */
	private String color;

	/**
	 * The card status identifier.
	 */
	private int statusId;

	/**
	 * The constructor used when we are trying to create a new card locally.
	 * 
	 * @param cardTypeId
	 *            The identifier of the card type
	 * @param projectId
	 *            The identifier of the project
	 */
	public TuleapCard(int cardTypeId, int projectId) {
		super(cardTypeId, projectId);
	}

	/**
	 * The constructor used when we are updating an existing card.
	 * 
	 * @param cardId
	 *            The identifier of the card
	 * @param cardTypeId
	 *            The identifier of the card type
	 * @param projectId
	 *            The identifier of the project
	 */
	public TuleapCard(int cardId, int cardTypeId, int projectId) {
		super(cardId, cardTypeId, projectId);
	}

	/**
	 * The configurable constructor used to create a card.
	 * 
	 * @param cardId
	 *            The identifier
	 * @param cardTypeId
	 *            The identifier of the card type
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
	public TuleapCard(int cardId, int cardTypeId, int projectId, String label, String url, String htmlUrl,
			Date creationDate, Date lastModificationDate) {
		super(cardId, cardTypeId, projectId, label, url, htmlUrl, creationDate, lastModificationDate);
	}

	// CHECKSTYLE:ON

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
