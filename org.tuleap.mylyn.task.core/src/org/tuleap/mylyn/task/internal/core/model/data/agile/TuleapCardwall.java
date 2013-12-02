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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * A card wall in Tuleap, that contains columns (one column is understated and has no id), and a list of
 * swimlanes, each swimlane containing cards.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapCardwall {

	/**
	 * Card wall swimlanes list.
	 */
	private final List<TuleapSwimlane> swimlanes;

	/**
	 * Card wall status list.
	 */
	private final List<TuleapColumn> column;

	/**
	 * Constructor.
	 */
	public TuleapCardwall() {
		this.swimlanes = Lists.newArrayList();
		this.column = Lists.newArrayList();
	}

	/**
	 * Add a status to the card wall.
	 * 
	 * @param theColumn
	 *            The column
	 */
	public void addColumn(TuleapColumn theColumn) {
		column.add(theColumn);
	}

	/**
	 * Provides the cardwall columns list.
	 * 
	 * @return An unmodifiable list view of the columns of this card wall.
	 */
	public List<TuleapColumn> getColumns() {
		return ImmutableList.copyOf(column);
	}

	/**
	 * Add a swimlane to the card wall.
	 * 
	 * @param swimlane
	 *            The swimlane
	 */
	public void addSwimlane(TuleapSwimlane swimlane) {
		swimlanes.add(swimlane);
	}

	/**
	 * Provides the swimlanes list of this card wall.
	 * 
	 * @return An unmodifiable list view of the swimlanes of this card wall.
	 */
	public List<TuleapSwimlane> getSwimlanes() {
		return ImmutableList.copyOf(swimlanes);
	}
}
