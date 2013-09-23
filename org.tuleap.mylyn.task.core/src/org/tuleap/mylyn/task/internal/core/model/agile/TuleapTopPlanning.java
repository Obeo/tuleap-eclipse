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
 * This POJO represents a top planning.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapTopPlanning {

	/**
	 * The identifier.
	 */
	private int id;

	/**
	 * The top planning's milestones.
	 */
	private final List<TuleapMilestone> milestones = Lists.newArrayList();

	/**
	 * The top planning's backlog items.
	 */
	private final List<TuleapBacklogItem> backlogItems = Lists.newArrayList();

	/**
	 * The constructor.
	 * 
	 * @param id
	 *            The identifier
	 */
	public TuleapTopPlanning(int id) {
		this.id = id;
	}

	/**
	 * Returns the id.
	 * 
	 * @return The id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Provides an immutable view of the top planning's milestones.
	 * 
	 * @return the milestones
	 */
	public ImmutableList<TuleapMilestone> getMilestones() {
		return ImmutableList.copyOf(milestones);
	}

	/**
	 * Provides an immutable view of the top planning's backlog items.
	 * 
	 * @return the backlogItems
	 */
	public ImmutableList<TuleapBacklogItem> getBacklogItems() {
		return ImmutableList.copyOf(backlogItems);
	}

	/**
	 * Adds the given milestone to the top planning's list of milestones.
	 * 
	 * @param milestone
	 *            The milestone to add.
	 */
	public void addMilestone(TuleapMilestone milestone) {
		this.milestones.add(milestone);
	}

	/**
	 * Adds the given backlog item to the top planning's list of backlog items.
	 * 
	 * @param backlogItem
	 *            The backlog item to add.
	 */
	public void addBacklogItem(TuleapBacklogItem backlogItem) {
		this.backlogItems.add(backlogItem);
	}
}
