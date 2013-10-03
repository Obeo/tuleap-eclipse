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
	private final int id;

	/**
	 * The human-readable label of the element.
	 */
	private final String label;

	/**
	 * The API URL of the element.
	 */
	private final String url;

	/**
	 * The URL of the planning for web browsers.
	 */
	private final String htmlUrl;

	/**
	 * The project id of this top planning.
	 */
	private final int projectId;

	/**
	 * The binding configuration for the planning.
	 */
	private final TuleapPlanningBinding binding;

	/**
	 * The top planning's milestones.
	 */
	private final List<TuleapMilestone> milestones = Lists.newArrayList();

	/**
	 * The top planning's backlog items.
	 */
	private final List<TuleapBacklogItem> backlogItems = Lists.newArrayList();

	/**
	 * The date of creation of the artifact.
	 */
	// private Date creationDate;

	/**
	 * The date of the last modification of the artifact.
	 */
	// private Date lastModificationDate;

	/**
	 * This constructor is used for the creation of the configurable elements that have been synchronized on
	 * the server. Those elements must have an identifier assigned by the server.
	 * 
	 * @param id
	 *            The identifier of the element
	 * @param label
	 *            The label of the element
	 * @param url
	 *            The API URL of the element
	 * @param htmlUrl
	 *            The HTML URL of the element
	 * @param projectId
	 *            The project Id
	 * @param binding
	 *            The planning binding configuration
	 */
	public TuleapTopPlanning(int id, String label, String url, String htmlUrl, int projectId,
			TuleapPlanningBinding binding) {
		this.id = id;
		this.label = label;
		this.url = url;
		this.htmlUrl = htmlUrl;
		this.projectId = projectId;
		this.binding = binding;
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
	 * Returns the API URL of the element.
	 * 
	 * @return The API URL of the element
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Html URL getter.
	 * 
	 * @return the htmlUrl
	 */
	public String getHtmlUrl() {
		return htmlUrl;
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
	 * The project ID.
	 * 
	 * @return the projectId
	 */
	public int getProjectId() {
		return projectId;
	}

	/**
	 * The binding configuration for the planning.
	 * 
	 * @return the binding
	 */
	public TuleapPlanningBinding getBinding() {
		return binding;
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
