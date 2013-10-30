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

import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.data.AgileTaskKindUtil;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.internal.core.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.data.TuleapConfigurableElementMapper;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskIdentityUtil;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItemType;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;

/**
 * Utility class used to transform a {@link TuleapBacklogItem} into a {@link TaskData} and vice versa.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class BacklogItemTaskDataConverter extends AbstractElementTaskDataConverter<TuleapBacklogItem, TuleapBacklogItemType> {

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
	 * The value used to indicate that a task data represents a number of points in a backlog item.
	 */
	public static final String SUFFIX_BACKLOG_ITEM_POINTS = "points"; //$NON-NLS-1$

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The configuration of the backlogItem.
	 * @param taskRepository
	 *            The task repository to use.
	 * @param connector
	 *            The repository connector to use.
	 */
	public BacklogItemTaskDataConverter(TuleapBacklogItemType configuration, TaskRepository taskRepository,
			ITuleapRepositoryConnector connector) {
		super(configuration, taskRepository, connector);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.data.converter.AbstractElementTaskDataConverter#populateTaskData(org.eclipse.mylyn.tasks.core.data.TaskData,
	 *      org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableElement, IProgressMonitor)
	 */
	@Override
	public void populateTaskData(TaskData taskData, TuleapBacklogItem tuleapBacklogItem,
			IProgressMonitor monitor) {
		super.populateTaskDataConfigurableFields(taskData, tuleapBacklogItem);

		MilestonePlanningWrapper milestonePlanning = new MilestonePlanningWrapper(taskData.getRoot());
		int biTypeId = tuleapBacklogItem.getConfigurationId();
		int projectId = tuleapBacklogItem.getProjectId();
		// Let's refresh the submilestone type configuration
		refreshConfiguration(projectId, biTypeId, monitor);
		BacklogItemWrapper backlogItemWrapper = milestonePlanning.addBacklogItem(TuleapTaskIdentityUtil
				.getTaskDataId(tuleapBacklogItem.getProjectId(), tuleapBacklogItem.getConfigurationId(),
						tuleapBacklogItem.getId()));
		backlogItemWrapper.setDisplayId(Integer.toString(tuleapBacklogItem.getId()));
		if (tuleapBacklogItem.getAssignedMilestoneId() != null) {
			backlogItemWrapper.setAssignedMilestoneId(String.valueOf(tuleapBacklogItem
					.getAssignedMilestoneId()));
		}
		backlogItemWrapper.setLabel(tuleapBacklogItem.getLabel());
		if (tuleapBacklogItem.getInitialEffort() != null) {
			backlogItemWrapper.setInitialEffort(tuleapBacklogItem.getInitialEffort().floatValue());
		}
		AgileTaskKindUtil.setAgileTaskKind(taskData, AgileTaskKindUtil.TASK_KIND_BACKLOG_ITEM);
	}

	/**
	 * Creates a tuleap backlogItem POJO from the related task data.
	 * 
	 * @param taskData
	 *            The updated task data.
	 * @return The tuleap BacklogItem POJO.
	 */
	public TuleapBacklogItem createTuleapBacklogItem(TaskData taskData) {
		TuleapConfigurableElementMapper tuleapConfigurableElementMapper = new TuleapConfigurableElementMapper(
				taskData, this.configuration);

		TuleapBacklogItem tuleapBacklogItem = null;
		if (taskData.isNew()) {
			int configurationId = tuleapConfigurableElementMapper.getConfigurationId();
			int projectId = tuleapConfigurableElementMapper.getProjectId();
			tuleapBacklogItem = new TuleapBacklogItem(configurationId, projectId);
		} else {
			int backlogItemId = tuleapConfigurableElementMapper.getId();
			int configurationId = tuleapConfigurableElementMapper.getConfigurationId();
			int projectId = tuleapConfigurableElementMapper.getProjectId();
			tuleapBacklogItem = new TuleapBacklogItem(backlogItemId, configurationId, projectId);
		}

		// Configurable fields
		Set<AbstractFieldValue> fieldValues = tuleapConfigurableElementMapper.getFieldValues();
		for (AbstractFieldValue abstractFieldValue : fieldValues) {
			tuleapBacklogItem.addFieldValue(abstractFieldValue);
		}

		// New comment
		TaskAttribute newCommentTaskAttribute = taskData.getRoot().getAttribute(TaskAttribute.COMMENT_NEW);
		tuleapBacklogItem.setNewComment(newCommentTaskAttribute.getValue());

		// BacklogItem specific information
		TaskAttribute milestoneAtt = taskData.getRoot().getAttribute(MILESTONE_PLANNING);
		TaskAttribute backlogAtt = milestoneAtt.getAttribute(BACKLOG);

		for (TaskAttribute backlogItem : backlogAtt.getAttributes().values()) {
			TaskAttribute backlogItemid = backlogItem
					.getMappedAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + 0 + ID_SEPARATOR
							+ SUFFIX_DISPLAY_ID);
			if (backlogItemid.getValue().equals(String.valueOf(tuleapBacklogItem.getId()))) {
				TaskAttribute assignedMilestoneId = backlogItem
						.getMappedAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + 0 + ID_SEPARATOR
								+ SUFFIX_ASSIGNED_MILESTONE_ID);
				TaskAttribute initialEffort = backlogItem
						.getMappedAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + 0 + ID_SEPARATOR
								+ SUFFIX_BACKLOG_ITEM_POINTS);
				tuleapBacklogItem.setAssignedMilestoneId(Integer.valueOf(assignedMilestoneId.getValue()));
				tuleapBacklogItem.setInitialEffort(Float.valueOf(initialEffort.getValue()));
			}
		}
		return tuleapBacklogItem;
	}
}
