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

/**
 * The cardwall column.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapColumn {

	/**
	 * The id of the column.
	 */
	private int id;

	/**
	 * The human-readable label of the column.
	 */
	private String label;

	/**
	 * The column color.
	 */
	private String color;

	/**
	 * The constructor used when we are updating an existing card.
	 * 
	 * @param id
	 *            The identifier of the column
	 * @param label
	 *            The label of the column
	 */
	public TuleapColumn(int id, String label) {
		this.id = id;
		this.label = label;
	}

	/**
	 * Id getter.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Label getter.
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Get the column color.
	 * 
	 * @return the column color
	 */
	public String getColor() {
		return this.color;
	}

	/**
	 * Set the column color.
	 * 
	 * @param theColor
	 *            The color to set
	 */
	public void setColor(String theColor) {
		this.color = theColor;
	}

}
