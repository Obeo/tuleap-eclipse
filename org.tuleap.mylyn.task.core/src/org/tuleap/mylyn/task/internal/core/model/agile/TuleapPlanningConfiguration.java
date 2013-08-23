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
 * Configuration of a Planning in Tuleap.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapPlanningConfiguration extends AbstractTuleapAgileElement {

	/**
	 * The human readable name of the planning.
	 */
	private String name;

	/**
	 * The id of the project that contains this planning.
	 */
	private int projectId;

	/**
	 * The id of the tracker that contains the milestone artifacts of this planning.
	 */
	private int milestoneTrackerId;

	/**
	 * The id of the tracker that contains the backlog item artifacts of this planning.
	 */
	private int backlogTrackerId;

	/**
	 * The label of the milestone tracker.
	 */
	private String milestoneTrackerTitle;

	/**
	 * The label of the backlog tracker.
	 */
	private String backlogTrackerLabel;

	/**
	 * name getter.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * name setter.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Project id getter.
	 * 
	 * @return the projectId
	 */
	public int getProjectId() {
		return projectId;
	}

	/**
	 * Project Id setter.
	 * 
	 * @param projectId
	 *            the projectId to set
	 */
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	/**
	 * Milestone tracker id getter.
	 * 
	 * @return the milestoneTrackerId
	 */
	public int getMilestoneTrackerId() {
		return milestoneTrackerId;
	}

	/**
	 * Milestone tracker id setter.
	 * 
	 * @param milestoneTrackerId
	 *            the milestoneTrackerId to set
	 */
	public void setMilestoneTrackerId(int milestoneTrackerId) {
		this.milestoneTrackerId = milestoneTrackerId;
	}

	/**
	 * Backlog tracker id getter.
	 * 
	 * @return the backlogTrackerId
	 */
	public int getBacklogTrackerId() {
		return backlogTrackerId;
	}

	/**
	 * Backlog tracker id setter.
	 * 
	 * @param backlogTrackerId
	 *            the backlogTrackerId to set
	 */
	public void setBacklogTrackerId(int backlogTrackerId) {
		this.backlogTrackerId = backlogTrackerId;
	}

	/**
	 * Milestone tracker label getter.
	 * 
	 * @return the milestoneTrackerTitle
	 */
	public String getMilestoneTrackerTitle() {
		return milestoneTrackerTitle;
	}

	/**
	 * Milestone tracker label setter.
	 * 
	 * @param milestoneTrackerTitle
	 *            the milestoneTrackerTitle to set
	 */
	public void setMilestoneTrackerTitle(String milestoneTrackerTitle) {
		this.milestoneTrackerTitle = milestoneTrackerTitle;
	}

	/**
	 * Backlog tracker label getter.
	 * 
	 * @return the backlogTrackerLabel
	 */
	public String getBacklogTrackerLabel() {
		return backlogTrackerLabel;
	}

	/**
	 * Backlog tracker label setter.
	 * 
	 * @param backlogTrackerLabel
	 *            the backlogTrackerLabel to set
	 */
	public void setBacklogTrackerLabel(String backlogTrackerLabel) {
		this.backlogTrackerLabel = backlogTrackerLabel;
	}

}
