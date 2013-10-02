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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.data.AgileTaskKindUtil;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;
import org.tuleap.mylyn.task.internal.core.data.TuleapConfigurableElementMapper;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestoneType;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapTopPlanning;

/**
 * Class to convert a milestone to task data and task data to milestone.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class MilestoneTaskDataConverter extends AbstractElementTaskDataConverter<TuleapMilestone, TuleapMilestoneType> {

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The configuration of the milestones.
	 */
	public MilestoneTaskDataConverter(TuleapMilestoneType configuration) {
		super(configuration);
	}

	/**
	 * Fills the task data related to the given top planning.
	 * 
	 * @param taskData
	 *            The task data
	 * @param tuleapTopPlanning
	 *            The top planning
	 */
	public void populateTaskData(TaskData taskData, TuleapTopPlanning tuleapTopPlanning) {
		this.populatePlanning(taskData, tuleapTopPlanning);

		AgileTaskKindUtil.setAgileTaskKind(taskData, AgileTaskKindUtil.TASK_KIND_TOP_PLANNING);

		// TODO CNO Realize the cardwall of the top planning
		// this.populateCardwall(taskData, tuleapTopPlanning);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.data.converter.AbstractElementTaskDataConverter#populateTaskData(org.eclipse.mylyn.tasks.core.data.TaskData,
	 *      org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableElement)
	 */
	@Override
	public void populateTaskData(TaskData taskData, TuleapMilestone milestone) {
		super.populateTaskDataConfigurableFields(taskData, milestone);
		this.populatePlanning(taskData, milestone);

		AgileTaskKindUtil.setAgileTaskKind(taskData, AgileTaskKindUtil.TASK_KIND_MILESTONE);

		// TODO CNO Realize the cardwall of the milestone
		// this.populateCardwall(taskData, milestone);
	}

	/**
	 * Populates the planning data in a task data from the given pojo.
	 * 
	 * @param taskData
	 *            The task data to fill.
	 * @param pojo
	 *            The pojo.
	 */
	private void populatePlanning(TaskData taskData, Object pojo) {
		MilestonePlanningWrapper milestonePlanningWrapper = new MilestonePlanningWrapper(taskData.getRoot());
		for (TuleapMilestone subMilestone : getSubMilestones(pojo)) {
			SubMilestoneWrapper subMilestoneWrapper = milestonePlanningWrapper.addSubMilestone(Integer
					.toString(subMilestone.getId()));
			subMilestoneWrapper.setLabel(subMilestone.getLabel());
			subMilestoneWrapper.setStartDate(subMilestone.getStartDate());
			if (subMilestone.getDuration() != null) {
				subMilestoneWrapper.setDuration(subMilestone.getDuration().floatValue());
			}
			if (subMilestone.getCapacity() != null) {
				subMilestoneWrapper.setCapacity(subMilestone.getCapacity().floatValue());
			}
		}
		for (TuleapBacklogItem backlogItem : getBacklogItems(pojo)) {
			BacklogItemWrapper backlogItemWrapper = milestonePlanningWrapper.addBacklogItem(Integer
					.toString(backlogItem.getId()));
			Integer assignedMilestoneId = backlogItem.getAssignedMilestoneId();
			if (assignedMilestoneId != null) {
				backlogItemWrapper.setAssignedMilestoneId(assignedMilestoneId.toString());
			}
			backlogItemWrapper.setLabel(backlogItem.getLabel());
			if (backlogItem.getInitialEffort() != null) {
				backlogItemWrapper.setInitialEffort(backlogItem.getInitialEffort().floatValue());
			}
		}
	}

	/**
	 * Get the sub-milestones of the given pojo.
	 * 
	 * @param parent
	 *            The pojo containing the milestones.
	 * @return The list of milestones.
	 */
	private List<TuleapMilestone> getSubMilestones(Object parent) {
		List<TuleapMilestone> milestones = new ArrayList<TuleapMilestone>();
		if (parent instanceof TuleapTopPlanning) {
			milestones.addAll(((TuleapTopPlanning)parent).getMilestones());
		} else if (parent instanceof TuleapMilestone) {
			milestones.addAll(((TuleapMilestone)parent).getSubMilestones());
		}
		return milestones;
	}

	/**
	 * Get the backlog items of the given pojo.
	 * 
	 * @param parent
	 *            The pojo containing the backlog items.
	 * @return The list of back log items.
	 */
	private List<TuleapBacklogItem> getBacklogItems(Object parent) {
		List<TuleapBacklogItem> items = new ArrayList<TuleapBacklogItem>();
		if (parent instanceof TuleapTopPlanning) {
			items.addAll(((TuleapTopPlanning)parent).getBacklogItems());
		} else if (parent instanceof TuleapMilestone) {
			items.addAll(((TuleapMilestone)parent).getBacklogItems());
		}
		return items;
	}

	/**
	 * Creates a milestone POJO from the related task data.
	 * 
	 * @param taskData
	 *            The updated task data.
	 * @return The updated milestone POJO.
	 */
	public TuleapMilestone createTuleapMilestone(TaskData taskData) {
		TuleapConfigurableElementMapper tuleapConfigurableElementMapper = new TuleapConfigurableElementMapper(
				taskData, this.configuration);

		TuleapMilestone tuleapMilestone = null;

		if (taskData.isNew()) {
			int configurationId = tuleapConfigurableElementMapper.getConfigurationId();
			tuleapMilestone = new TuleapMilestone(configurationId);
		} else {
			int id = tuleapConfigurableElementMapper.getId();
			int configurationId = tuleapConfigurableElementMapper.getConfigurationId();
			tuleapMilestone = new TuleapMilestone(id, configurationId);
		}

		// Configurable fields
		tuleapMilestone = this.populateElementConfigurableFields(taskData, tuleapMilestone);

		// TODO SBE populate the backlog items etc...

		return tuleapMilestone;
	}

}
