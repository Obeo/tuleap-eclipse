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

import com.google.common.collect.Maps;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.data.AgileTaskKindUtil;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;
import org.tuleap.mylyn.task.agile.core.data.cardwall.SwimlaneWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;
import org.tuleap.mylyn.task.internal.core.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.data.TuleapConfigurableElementMapper;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskIdentityUtil;
import org.tuleap.mylyn.task.internal.core.model.agile.IPlanning;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapCard;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapCardwall;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestoneType;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapStatus;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapSwimlane;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapTopPlanning;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;

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
	 * @param taskRepository
	 *            The task repository to use.
	 * @param connector
	 *            The repository connector to use.
	 */
	public MilestoneTaskDataConverter(TuleapMilestoneType configuration, TaskRepository taskRepository,
			ITuleapRepositoryConnector connector) {
		super(configuration, taskRepository, connector);
	}

	/**
	 * Fills the task data related to the given top planning.
	 * 
	 * @param taskData
	 *            The task data
	 * @param tuleapTopPlanning
	 *            The top planning
	 * @param projectId
	 *            the project id
	 * @param monitor
	 *            The progress monitor to use
	 */
	public void populateTaskData(TaskData taskData, TuleapTopPlanning tuleapTopPlanning, int projectId,
			IProgressMonitor monitor) {
		this.populatePlanning(taskData, tuleapTopPlanning, monitor);

		// Task Key
		// TODO Externalize String
		String taskKey = TuleapTaskIdentityUtil.getTaskDataKey(Integer.toString(projectId),
				"General Planning", tuleapTopPlanning.getId()); //$NON-NLS-1$
		TuleapConfigurableElementMapper mapper = new TuleapConfigurableElementMapper(taskData, null);
		TaskAttribute taskKeyAtt = taskData.getRoot().createMappedAttribute(TaskAttribute.TASK_KEY);
		taskKeyAtt.setValue(taskKey);
		mapper.setTaskKey(taskKey);

		AgileTaskKindUtil.setAgileTaskKind(taskData, AgileTaskKindUtil.TASK_KIND_TOP_PLANNING);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.data.converter.AbstractElementTaskDataConverter#populateTaskData
	 *      (TaskData, org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableElement,
	 *      IProgressMonitor)
	 */
	@Override
	public void populateTaskData(TaskData taskData, TuleapMilestone milestone, IProgressMonitor monitor) {
		super.populateTaskDataConfigurableFields(taskData, milestone);

		this.populatePlanning(taskData, milestone, monitor);

		AgileTaskKindUtil.setAgileTaskKind(taskData, AgileTaskKindUtil.TASK_KIND_MILESTONE);

		TuleapCardwall cardwall = milestone.getCardwall();
		if (cardwall != null) {
			populateCardwall(taskData, cardwall, monitor);
		}
	}

	/**
	 * Populates the planning data in a task data from the given pojo.
	 * 
	 * @param taskData
	 *            The task data to fill.
	 * @param planning
	 *            The pojo.
	 * @param monitor
	 *            The progress monitor to use
	 */
	private void populatePlanning(TaskData taskData, IPlanning planning, IProgressMonitor monitor) {
		MilestonePlanningWrapper milestonePlanning = new MilestonePlanningWrapper(taskData.getRoot());

		if (configuration != null) {
			milestonePlanning.setHasCardwall(configuration.hasCardwall());
		}

		Map<Integer, String> milestoneInternalIdByTuleapId = Maps.newHashMap();
		for (TuleapMilestone subMilestone : planning.getSubMilestones()) {
			int submilestoneTypeId = subMilestone.getConfigurationId();
			int projectId = subMilestone.getProjectId();
			// Let's refresh the submilestone type configuration
			refreshConfiguration(projectId, submilestoneTypeId, monitor);
			String internalMilestoneId = TuleapTaskIdentityUtil.getTaskDataId(subMilestone.getProjectId(),
					submilestoneTypeId, subMilestone.getId());
			milestoneInternalIdByTuleapId.put(Integer.valueOf(subMilestone.getId()), internalMilestoneId);
			SubMilestoneWrapper subMilestoneWrapper = milestonePlanning.addSubMilestone(internalMilestoneId);
			subMilestoneWrapper.setDisplayId(Integer.toString(subMilestone.getId()));
			subMilestoneWrapper.setLabel(subMilestone.getLabel());
			subMilestoneWrapper.setStartDate(subMilestone.getStartDate());
			if (subMilestone.getDuration() != null) {
				subMilestoneWrapper.setDuration(subMilestone.getDuration().floatValue());
			}
			if (subMilestone.getCapacity() != null) {
				subMilestoneWrapper.setCapacity(subMilestone.getCapacity().floatValue());
			}
		}
		for (TuleapBacklogItem backlogItem : planning.getBacklogItems()) {
			int biTypeId = backlogItem.getConfigurationId();
			int projectId = backlogItem.getProjectId();
			// Let's refresh the submilestone type configuration
			refreshConfiguration(projectId, biTypeId, monitor);
			BacklogItemWrapper backlogItemWrapper = milestonePlanning.addBacklogItem(TuleapTaskIdentityUtil
					.getTaskDataId(backlogItem.getProjectId(), backlogItem.getConfigurationId(), backlogItem
							.getId()));
			backlogItemWrapper.setDisplayId(Integer.toString(backlogItem.getId()));
			Integer assignedMilestoneId = backlogItem.getAssignedMilestoneId();
			if (assignedMilestoneId != null) {
				backlogItemWrapper.setAssignedMilestoneId(milestoneInternalIdByTuleapId
						.get(assignedMilestoneId));
			}
			backlogItemWrapper.setLabel(backlogItem.getLabel());
			if (backlogItem.getInitialEffort() != null) {
				backlogItemWrapper.setInitialEffort(backlogItem.getInitialEffort().floatValue());
			}
		}
	}

	/**
	 * Populates the planning data in a task data from the given pojo.
	 * 
	 * @param taskData
	 *            The task data to fill.
	 * @param cardwall
	 *            The cardwall pojo.
	 * @param monitor
	 *            The progress monitor to use
	 */
	private void populateCardwall(TaskData taskData, TuleapCardwall cardwall, IProgressMonitor monitor) {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (TuleapStatus column : cardwall.getStatuses()) {
			wrapper.addColumn(Integer.toString(column.getId()), column.getLabel());
		}
		for (TuleapSwimlane swimlane : cardwall.getSwimlanes()) {
			TuleapBacklogItem backlogItem = swimlane.getBacklogItem();
			String swimlaneId = TuleapTaskIdentityUtil.getTaskDataId(backlogItem.getProjectId(), backlogItem
					.getConfigurationId(), backlogItem.getId());
			SwimlaneWrapper swimlaneWrapper = wrapper.addSwimlane(swimlaneId);
			swimlaneWrapper.getSwimlaneItem().setLabel(backlogItem.getLabel());

			// display IDs
			String biDisplayId = Integer.toString(backlogItem.getId());
			swimlaneWrapper.getSwimlaneItem().setDisplayId(biDisplayId);
			swimlaneWrapper.setDisplayId(biDisplayId);

			Integer assignedMilestoneId = backlogItem.getAssignedMilestoneId();
			if (assignedMilestoneId != null) {
				swimlaneWrapper.getSwimlaneItem().setAssignedMilestoneId(
						TuleapTaskIdentityUtil.getTaskDataId(backlogItem.getProjectId(), backlogItem
								.getConfigurationId(), backlogItem.getAssignedMilestoneId().intValue()));
			}
			Float initialEffort = backlogItem.getInitialEffort();
			if (initialEffort != null) {
				swimlaneWrapper.getSwimlaneItem().setInitialEffort(initialEffort.floatValue());
			}
			for (TuleapCard card : swimlane.getCards()) {
				int cardTypeId = card.getConfigurationId();
				int cardProjectId = card.getProjectId();
				refreshConfiguration(cardProjectId, cardTypeId, monitor);
				String cardId = TuleapTaskIdentityUtil.getTaskDataId(cardProjectId, cardTypeId, card.getId());
				CardWrapper cardWrapper = swimlaneWrapper.addCard(cardId);
				populateCard(cardWrapper, card);
			}
		}
	}

	/**
	 * Transfers data form the given card POJO to the given {@link CardWrapper}.
	 * 
	 * @param cardWrapper
	 *            The card wrapper.
	 * @param card
	 *            The card POJO.
	 */
	public void populateCard(CardWrapper cardWrapper, TuleapCard card) {
		cardWrapper.setDisplayId(String.valueOf(card.getId()));
		cardWrapper.setLabel(card.getLabel());
		// ID of assigned status must be computed like in the columnWrapper above
		cardWrapper.setStatusId(Integer.toString(card.getStatusId()));
		for (AbstractFieldValue fieldValue : card.getFieldValues()) {
			// TODO manage other types of fields
			String fieldId = Integer.toString(fieldValue.getFieldId());
			if (fieldValue instanceof LiteralFieldValue) {
				cardWrapper.setFieldValue(fieldId, ((LiteralFieldValue)fieldValue).getFieldValue());
			} else if (fieldValue instanceof BoundFieldValue) {
				BoundFieldValue boundFieldValue = (BoundFieldValue)fieldValue;
				for (Integer boundId : boundFieldValue.getValueIds()) {
					cardWrapper.addFieldValue(fieldId, String.valueOf(boundId));
				}
			}
		}
	}

	/**
	 * Creates a milestone POJO from the related task data.
	 * 
	 * @param taskData
	 *            The updated task data.
	 * @return The updated milestone POJO.
	 */
	public TuleapMilestone createTuleapMilestone(TaskData taskData) {
		TuleapConfigurableElementMapper mapper = new TuleapConfigurableElementMapper(taskData,
				this.configuration);

		TuleapMilestone tuleapMilestone = null;

		if (taskData.isNew()) {
			int configurationId = mapper.getConfigurationId();
			int projectId = mapper.getProjectId();
			// TODO SBE find the identifier of the parent milestone
			tuleapMilestone = new TuleapMilestone(configurationId,
					TuleapMilestone.INVALID_PARENT_MILESTONE_ID, projectId);
		} else {
			int id = mapper.getId();
			int projectId = mapper.getProjectId();
			tuleapMilestone = new TuleapMilestone(id, projectId, this.configuration);
		}

		// Configurable fields
		tuleapMilestone = this.populateElementConfigurableFields(taskData, tuleapMilestone);

		// TODO SBE populate the backlog items etc...

		return tuleapMilestone;
	}

}
