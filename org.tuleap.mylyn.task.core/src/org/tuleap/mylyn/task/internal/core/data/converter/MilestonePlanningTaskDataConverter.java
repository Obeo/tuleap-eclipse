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
package org.tuleap.mylyn.task.internal.core.data.converter;

import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;

/**
 * Class to convert a milestone to task data and task data to milestone.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class MilestonePlanningTaskDataConverter {

	/**
	 * The configuration of the server.
	 */
	private TuleapServerConfiguration config;

	/**
	 * Constructor.
	 * 
	 * @param config
	 *            The configuration of the server.
	 */
	public MilestonePlanningTaskDataConverter(TuleapServerConfiguration config) {
		this.config = config;
	}

	/**
	 * Fills the task data related to the given milestone POJO.
	 * 
	 * @param taskData
	 *            the task data to fill/update with the given milestone.
	 * @param milestone
	 *            The updated milestone.
	 */
	public void populateTaskData(TaskData taskData, TuleapMilestone milestone) {
		MilestonePlanningWrapper milestonePlanningWrapper = new MilestonePlanningWrapper(taskData.getRoot());
		for (TuleapMilestone subMilestone : milestone.getSubMilestones()) {
			SubMilestoneWrapper subMilestoneWrapper = milestonePlanningWrapper.addSubMilestone();
			subMilestoneWrapper.setId(subMilestone.getId());
			subMilestoneWrapper.setLabel(subMilestone.getLabel());
			subMilestoneWrapper.setStartDate(new java.util.Date(subMilestone.getStartDate()));
			subMilestoneWrapper.setDuration(subMilestone.getDuration());
			subMilestoneWrapper.setCapacity(subMilestone.getCapacity());
		}
		for (TuleapBacklogItem backlogItem : milestone.getBacklogItems()) {
			BacklogItemWrapper backlogItemWrapper = milestonePlanningWrapper.addBacklogItem();
			backlogItemWrapper.setId(backlogItem.getId());
			backlogItemWrapper.setAssignedMilestoneId(milestone.getId());
			backlogItemWrapper.setLabel(backlogItem.getLabel());
			backlogItemWrapper.setInitialEffort(backlogItem.getInitialEffort());
		}
	}

	/**
	 * Creates a milestone POJO from the related task data.
	 * 
	 * @param taskData
	 *            The updated task data.
	 * @return The updated milestone POJO.
	 */
	public TuleapMilestone createMilestonePlanning(TaskData taskData) {
		// TODO
		return null;
	}

}
