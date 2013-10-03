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
 * The configuration of a planning, that associates a milestone configuration to a backlog item configuration.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapPlanningBinding {

	/**
	 * The id of the milestone type.
	 */
	private final int milestoneTypeId;

	/**
	 * The id of the backlog item type.
	 */
	private final int backlogItemTypeId;

	/**
	 * The milestone list title.
	 */
	private String milestonesTitle;

	/**
	 * The backlog title.
	 */
	private String backlogItemsTitle;

	/**
	 * Constructor.
	 * 
	 * @param milestoneTypeId
	 *            The milestone type id
	 * @param backlogItemTypeId
	 *            The backlog item type id
	 */
	public TuleapPlanningBinding(int milestoneTypeId, int backlogItemTypeId) {
		this.milestoneTypeId = milestoneTypeId;
		this.backlogItemTypeId = backlogItemTypeId;
	}

	/**
	 * The milestone type id.
	 * 
	 * @return The milestone type id
	 */
	public int getMilestonesTypeId() {
		return milestoneTypeId;
	}

	/**
	 * The backlog items type id.
	 * 
	 * @return The backlog items type id
	 */
	public int getBacklogItemsTypeId() {
		return backlogItemTypeId;
	}

	/**
	 * The milestones title.
	 * 
	 * @return the milestonesTitle
	 */
	public String getMilestonesTitle() {
		return milestonesTitle;
	}

	/**
	 * Milestones title setter.
	 * 
	 * @param milestonesTitle
	 *            the milestonesTitle to set
	 */
	public void setMilestonesTitle(String milestonesTitle) {
		this.milestonesTitle = milestonesTitle;
	}

	/**
	 * The backlog title.
	 * 
	 * @return the backlogItemsTitle
	 */
	public String getBacklogItemsTitle() {
		return backlogItemsTitle;
	}

	/**
	 * backlog title setter.
	 * 
	 * @param backlogItemsTitle
	 *            the backlogItemsTitle to set
	 */
	public void setBacklogItemsTitle(String backlogItemsTitle) {
		this.backlogItemsTitle = backlogItemsTitle;
	}
}
