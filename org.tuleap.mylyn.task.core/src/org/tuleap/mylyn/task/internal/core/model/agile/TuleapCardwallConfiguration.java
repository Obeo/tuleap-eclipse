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
public class TuleapCardwallConfiguration {

	/**
	 * flag indicating whether the card wall is enabled.
	 */
	private final boolean enabled;

	/**
	 * Card wall column configurations.
	 */
	private final List<TuleapCardwallColumn> columns;

	/**
	 * Constructor.
	 * 
	 * @param enabled
	 *            Is enabled?
	 */
	public TuleapCardwallConfiguration(boolean enabled) {
		this.enabled = enabled;
		this.columns = Lists.newArrayList();
	}

	/**
	 * Is enabled?
	 * 
	 * @return {@code true} if and only if this cardwall is enabled.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Add a column configuration to the card wall.
	 * 
	 * @param column
	 *            The column to add.
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
}
