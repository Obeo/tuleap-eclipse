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

/**
 * The cardwall swimlane.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapStatus {

	/**
	 * The id of the status.
	 */
	private int id;

	/**
	 * The human-readable label of the status.
	 */
	private String label;

	/**
	 * The constructor used when we are updating an existing card.
	 * 
	 * @param id
	 *            The identifier of the card
	 * @param label
	 *            The label of the card
	 */
	public TuleapStatus(int id, String label) {
		this.id = id;
		this.label = label;
	}

	/**
	 * id getter.
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
}
