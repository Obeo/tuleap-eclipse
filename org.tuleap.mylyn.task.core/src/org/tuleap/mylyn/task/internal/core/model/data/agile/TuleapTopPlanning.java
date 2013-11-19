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

import org.tuleap.mylyn.task.internal.core.model.data.AbstractTuleapProjectElement;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;

/**
 * This POJO represents a top planning.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapTopPlanning extends AbstractTuleapProjectElement implements IPlanning {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -2127686516438714551L;

	/**
	 * The top planning's milestones.
	 */
	private final List<TuleapMilestone> milestones = Lists.newArrayList();

	/**
	 * The top planning's backlog items.
	 */
	private final List<TuleapBacklogItem> backlogItems = Lists.newArrayList();

	/**
	 * Default constructor for deserialization.
	 */
	public TuleapTopPlanning() {
		// Default constructor for deserialization.
	}

	/**
	 * This constructor is used for the creation of the configurable elements that have been synchronized on
	 * the server. Those elements must have an identifier assigned by the server.
	 * 
	 * @param id
	 *            The identifier of the element
	 * @param projectRef
	 *            the project
	 * @param label
	 *            The label of the element
	 * @param uri
	 *            The API URL of the element
	 */
	public TuleapTopPlanning(int id, TuleapReference projectRef, String label, String uri) {
		super(id, projectRef, label, uri);
	}

	/**
	 * Provides an immutable view of the top planning's milestones.
	 * 
	 * @return the milestones
	 */
	public List<TuleapMilestone> getSubMilestones() {
		return ImmutableList.copyOf(milestones);
	}

	/**
	 * Provides an immutable view of the top planning's backlog items.
	 * 
	 * @return the backlogItems
	 */
	public List<TuleapBacklogItem> getBacklogItems() {
		return ImmutableList.copyOf(backlogItems);
	}

	/**
	 * Adds the given milestone to the top planning's list of milestones.
	 * 
	 * @param milestone
	 *            The milestone to add.
	 */
	public void addSubMilestone(TuleapMilestone milestone) {
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
