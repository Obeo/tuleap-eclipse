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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
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
import org.tuleap.mylyn.task.internal.core.data.TuleapArtifactMapper;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskIdentityUtil;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskMapper;
import org.tuleap.mylyn.task.internal.core.model.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapCard;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapCardwall;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapStatus;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapSwimlane;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapTopPlanning;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;

/**
 * Class to convert a milestone to task data and task data to milestone.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class MilestoneTaskDataConverter {

	/**
	 * The value used to indicate that a task data represents the label to use for points ("Story Points" for
	 * example).
	 */
	public static final String SUFFIX_ASSIGNED_MILESTONE_ID = "assigned_id"; //$NON-NLS-1$

	/**
	 * Separator used in computed ids.
	 */
	public static final char ID_SEPARATOR = '-';

	/**
	 * Suffix appended to the ids of Task Attributes representing IDs to display to an end-user.
	 */
	public static final String SUFFIX_DISPLAY_ID = "display_id"; //$NON-NLS-1$

	/**
	 * Id of the backlog items list task attribute.
	 */
	public static final String BACKLOG = "mta_backlog"; //$NON-NLS-1$

	/**
	 * Id of the planning task attribute.
	 */
	public static final String MILESTONE_PLANNING = "mta_planning"; //$NON-NLS-1$

	/**
	 * The task repository.
	 */
	private final TaskRepository taskRepository;

	/**
	 * The connector.
	 */
	private final ITuleapRepositoryConnector connector;

	/**
	 * Constructor.
	 * 
	 * @param taskRepository
	 *            The task repository to use.
	 * @param connector
	 *            The repository connector to use.
	 */
	public MilestoneTaskDataConverter(TaskRepository taskRepository, ITuleapRepositoryConnector connector) {
		this.taskRepository = taskRepository;
		this.connector = connector;
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
		// Task Key
		// TODO Externalize String
		String taskKey = TuleapTaskIdentityUtil.getTaskDataKey(Integer.toString(projectId),
				"General Planning", tuleapTopPlanning.getId()); //$NON-NLS-1$
		TuleapArtifactMapper mapper = new TuleapArtifactMapper(taskData, null);
		TaskAttribute taskKeyAtt = taskData.getRoot().createMappedAttribute(TaskAttribute.TASK_KEY);
		taskKeyAtt.setValue(taskKey);
		mapper.setTaskKey(taskKey);

		AgileTaskKindUtil.setAgileTaskKind(taskData, AgileTaskKindUtil.TASK_KIND_TOP_PLANNING);
	}

	/**
	 * Populate the given task data with milestone data.
	 * 
	 * @param taskData
	 *            Object to populate.
	 * @param milestone
	 *            Milestone POJO that contains the milestone-specific data.
	 * @param monitor
	 *            Monitor
	 */
	public void populateTaskData(TaskData taskData, TuleapMilestone milestone, IProgressMonitor monitor) {
		// TODO Is this the right way to mark a TaskData as a milestone?
		AgileTaskKindUtil.setAgileTaskKind(taskData, AgileTaskKindUtil.TASK_KIND_MILESTONE);
	}

	/**
	 * Populates the planning data in a task data from the given pojo.
	 * 
	 * @param taskData
	 *            The task data to fill.
	 * @param subMilestones
	 *            The sub-milestones.
	 * @param backlogItems
	 *            The backlog items.
	 * @param monitor
	 *            The progress monitor to use
	 */
	public void populatePlanning(TaskData taskData, List<TuleapMilestone> subMilestones,
			List<TuleapBacklogItem> backlogItems, IProgressMonitor monitor) {
		MilestonePlanningWrapper milestonePlanning = new MilestonePlanningWrapper(taskData.getRoot());

		Map<Integer, String> milestoneInternalIdByTuleapId = Maps.newHashMap();
		for (TuleapMilestone subMilestone : subMilestones) {
			String internalMilestoneId = TuleapTaskIdentityUtil.getTaskDataId(subMilestone.getProject()
					.getId(), 0, subMilestone.getId());
			milestoneInternalIdByTuleapId.put(Integer.valueOf(subMilestone.getId()), internalMilestoneId);
			SubMilestoneWrapper subMilestoneWrapper = milestonePlanning.addSubMilestone(internalMilestoneId);
			subMilestoneWrapper.setDisplayId(Integer.toString(subMilestone.getId()));
			subMilestoneWrapper.setLabel(subMilestone.getLabel());
			if (subMilestone.getStartDate() != null) {
				subMilestoneWrapper.setStartDate(subMilestone.getStartDate());
			}
			if (subMilestone.getEndDate() != null) {
				subMilestoneWrapper.setEndDate(subMilestone.getEndDate());
			}
			if (subMilestone.getCapacity() != null) {
				subMilestoneWrapper.setCapacity(subMilestone.getCapacity().floatValue());
			}
		}
		for (TuleapBacklogItem backlogItem : backlogItems) {
			int projectId;
			// TODO LDE remove this? Due to a bug in tuleap that omits project refs in backlog items
			TuleapReference project = backlogItem.getProject();
			if (project != null) {
				projectId = project.getId();
			} else {
				projectId = Integer.parseInt(taskData.getRoot().getAttribute("mtc_project_id").getValue());
			}
			BacklogItemWrapper backlogItemWrapper = milestonePlanning.addBacklogItem(TuleapTaskIdentityUtil
					.getTaskDataId(projectId, 0, backlogItem.getId()));
			// FIXME Replace 0 above
			backlogItemWrapper.setDisplayId(Integer.toString(backlogItem.getId()));
			TuleapReference assignedMilestone = backlogItem.getAssignedMilestone();
			if (assignedMilestone != null) {
				backlogItemWrapper.setAssignedMilestoneId(milestoneInternalIdByTuleapId.get(Integer
						.valueOf(assignedMilestone.getId())));
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
	public void populateCardwall(TaskData taskData, TuleapCardwall cardwall, IProgressMonitor monitor) {
		CardwallWrapper wrapper = new CardwallWrapper(taskData.getRoot());
		for (TuleapStatus column : cardwall.getStatuses()) {
			wrapper.addColumn(Integer.toString(column.getId()), column.getLabel());
		}
		for (TuleapSwimlane swimlane : cardwall.getSwimlanes()) {
			TuleapBacklogItem backlogItem = swimlane.getBacklogItem();
			// FIXME Replace 0 below
			String swimlaneId = TuleapTaskIdentityUtil.getTaskDataId(backlogItem.getProject().getId(), 0,
					backlogItem.getId());
			SwimlaneWrapper swimlaneWrapper = wrapper.addSwimlane(swimlaneId);
			swimlaneWrapper.getSwimlaneItem().setLabel(backlogItem.getLabel());

			// display IDs
			String biDisplayId = Integer.toString(backlogItem.getId());
			swimlaneWrapper.getSwimlaneItem().setDisplayId(biDisplayId);
			swimlaneWrapper.setDisplayId(biDisplayId);

			TuleapReference assignedMilestone = backlogItem.getAssignedMilestone();
			if (assignedMilestone != null) {
				// FIXME Replace 0 below
				swimlaneWrapper.getSwimlaneItem().setAssignedMilestoneId(
						TuleapTaskIdentityUtil.getTaskDataId(backlogItem.getProject().getId(), 0, backlogItem
								.getAssignedMilestone().getId()));
			}
			Float initialEffort = backlogItem.getInitialEffort();
			if (initialEffort != null) {
				swimlaneWrapper.getSwimlaneItem().setInitialEffort(initialEffort.floatValue());
			}
			for (TuleapCard card : swimlane.getCards()) {
				int trackerId = card.getTracker().getId();
				int cardProjectId = card.getProject().getId();
				String cardId = TuleapTaskIdentityUtil.getTaskDataId(cardProjectId, trackerId, card.getId());
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
	 * @deprecated
	 */
	@Deprecated
	public TuleapMilestone createTuleapMilestone(TaskData taskData) {
		TuleapTaskMapper mapper = new TuleapTaskMapper(taskData);

		TuleapMilestone tuleapMilestone = null;

		int projectId = mapper.getProjectId();
		TuleapReference projectRef = new TuleapReference();
		projectRef.setId(projectId);
		projectRef.setUri("projects/" + projectId);
		if (taskData.isNew()) {
			// TODO SBE find the identifier of the parent milestone
			tuleapMilestone = new TuleapMilestone(projectRef);
		} else {
			int id = mapper.getId();
			tuleapMilestone = new TuleapMilestone(id, projectRef);
		}

		// TODO SBE populate the backlog items etc...
		return tuleapMilestone;
	}

	/**
	 * Extracts the list of backlog items from a Milestone {@link TaskData} object.
	 * 
	 * @param taskData
	 *            The milestone's task data
	 * @return The list of backlog items to send to the server for update.
	 */
	public List<TuleapBacklogItem> extractBacklogItems(TaskData taskData) {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		List<TuleapBacklogItem> backlogItems = Lists.newArrayList();
		for (BacklogItemWrapper biWrapper : wrapper.getAllBacklogItems()) {
			int id = TuleapTaskIdentityUtil.getElementIdFromTaskDataId(biWrapper.getId());
			int projectId = TuleapTaskIdentityUtil.getProjectIdFromTaskDataId(biWrapper.getId());
			// TODO mechanism to store TuleapReference in TaskData?
			TuleapReference projectRef = new TuleapReference();
			projectRef.setId(projectId);
			projectRef.setUri("projects/" + projectId); //$NON-NLS-1$
			TuleapBacklogItem bi = new TuleapBacklogItem(id, projectRef);
			String assignedId = biWrapper.getAssignedMilestoneId();
			if (assignedId != null) {
				TuleapReference assignedMilestone = new TuleapReference();
				// The assigned milestone id is the internal id, not the tuleap id!
				int assignedMilestoneId = TuleapTaskIdentityUtil.getElementIdFromTaskDataId(assignedId);
				assignedMilestone.setId(assignedMilestoneId);
				assignedMilestone.setUri("milestones/" + assignedMilestoneId); //$NON-NLS-1$
				bi.setAssignedMilestone(assignedMilestone);
			}
			bi.setInitialEffort(biWrapper.getInitialEffort());
		}
		return backlogItems;
	}

	/**
	 * Retrieves the milestone id from a milestone {@link TaskData} object.
	 * 
	 * @param taskData
	 *            the task data.
	 * @return The milestone id.
	 */
	public int getMilestoneId(TaskData taskData) {
		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		return TuleapTaskIdentityUtil.getElementIdFromTaskDataId(wrapper.getId());
	}

	/**
	 * Populates the milestone backlog with the given list of backlogItems.
	 * 
	 * @param taskData
	 *            The task data to fill.
	 * @param backlog
	 *            The backlogItems list to add to the milestone backlog.
	 * @param monitor
	 *            The progress monitor to use.
	 */
	public void populateBacklog(TaskData taskData, List<TuleapBacklogItem> backlog, IProgressMonitor monitor) {
		// TODO: FIB: Implement this method.
	}

	/**
	 * Add a submilestone with its given list of backlogItems.
	 * 
	 * @param taskData
	 *            The task data to fill.
	 * @param milestone
	 *            The milestone to add.
	 * @param milestoneContent
	 *            The backlogItems list to add to the milestone content.
	 * @param monitor
	 *            The progress monitor to use.
	 */
	public void addSubmilestone(TaskData taskData, TuleapMilestone milestone,
			List<TuleapBacklogItem> milestoneContent, IProgressMonitor monitor) {
		// TODO: FIB: Implement this method.
	}
}
