/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.core.internal.model.config.field;

import java.io.Serializable;

/**
 * This class represents an item of a Tuleap (multi) select box field.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapSelectBoxItem implements Serializable {
	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = 9223260684164103221L;

	/**
	 * The identifier of the item.
	 */
	private int identifier;

	/**
	 * The label of the item.
	 */
	private String label;

	/**
	 * The description of the item.
	 */
	private String description;

	/**
	 * The constructor.
	 * 
	 * @param id
	 *            The identifier of the item.
	 */
	public TuleapSelectBoxItem(int id) {
		this.identifier = id;
	}

	/**
	 * Returns the label of the item.
	 * 
	 * @return The label of the item.
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Sets the label of the item.
	 * 
	 * @param itemLabel
	 *            The label of the item
	 */
	public void setLabel(String itemLabel) {
		this.label = itemLabel;
	}

	/**
	 * Returns the description of the item.
	 * 
	 * @return The description of the item.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the description of the item.
	 * 
	 * @param itemDescription
	 *            The description of the item
	 */
	public void setDescription(String itemDescription) {
		this.description = itemDescription;
	}

	/**
	 * Returns the identifier of the item.
	 * 
	 * @return The identifier of the item.
	 */
	public int getIdentifier() {
		return this.identifier;
	}

}
