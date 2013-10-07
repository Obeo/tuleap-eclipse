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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * The configuration of a card wall in Tuleap.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapCardwall {

	/**
	 * Card wall column configurations.
	 */
	private final List<TuleapCardwallColumn> columns;

	/**
	 * Card wall swimlanes list.
	 */
	private final List<TuleapSwimlane> swimlanes;

	/**
	 * Card wall status list.
	 */
	private final List<TuleapStatus> status;

	/**
	 * Constructor.
	 */
	public TuleapCardwall() {
		this.columns = Lists.newArrayList();
		this.swimlanes = Lists.newArrayList();
		this.status = Lists.newArrayList();
	}

	/**
	 * Add a column configuration to the card wall.
	 * 
	 * @param column
	 *            The column configuration to add.
	 */
	public void addColumn(TuleapCardwallColumn column) {
		columns.add(column);
	}

	/**
	 * Provides the column configurations of this card wall.
	 * 
	 * @return An unmodifiable list view of the columns of this card wall.
	 */
	public List<TuleapCardwallColumn> getColumns() {
		return ImmutableList.copyOf(columns);
	}

	/**
	 * Add a status to the card wall.
	 * 
	 * @param theStatus
	 *            The status
	 */
	public void addStatus(TuleapStatus theStatus) {
		status.add(theStatus);
	}

	/**
	 * Provides the status list of this card wall.
	 * 
	 * @return An unmodifiable list view of the status of this card wall.
	 */
	public List<TuleapStatus> getStatus() {
		return ImmutableList.copyOf(status);
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
