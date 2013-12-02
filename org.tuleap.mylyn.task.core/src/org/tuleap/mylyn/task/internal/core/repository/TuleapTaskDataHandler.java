/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.internal.core.repository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.RepositoryResponse;
import org.eclipse.mylyn.tasks.core.RepositoryResponse.ResponseKind;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.data.AgileTaskKindUtil;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapClient;
import org.tuleap.mylyn.task.internal.core.data.TuleapArtifactMapper;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskIdentityUtil;
import org.tuleap.mylyn.task.internal.core.data.converter.ArtifactTaskDataConverter;
import org.tuleap.mylyn.task.internal.core.data.converter.MilestoneTaskDataConverter;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapProject;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapServer;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapTracker;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapCardwall;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessagesKeys;

/**
 * This class is in charge of the publication and retrieval of the tasks data to and from the repository.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapTaskDataHandler extends AbstractTaskDataHandler {

	/**
	 * The Tuleap repository connector.
	 */
	private ITuleapRepositoryConnector connector;

	/**
	 * The constructor.
	 * 
	 * @param repositoryConnector
	 *            The Tuleap repository connector.
	 */
	public TuleapTaskDataHandler(ITuleapRepositoryConnector repositoryConnector) {
		this.connector = repositoryConnector;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler#postTaskData(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.data.TaskData, java.util.Set,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public RepositoryResponse postTaskData(TaskRepository taskRepository, TaskData taskData,
			Set<TaskAttribute> oldAttributes, IProgressMonitor monitor) throws CoreException {
		RepositoryResponse response = null;

		TuleapArtifactMapper mapper = new TuleapArtifactMapper(taskData, null);

		int projectId = mapper.getProjectId();
		int trackerId = mapper.getTrackerId();

		TuleapServer tuleapServer = this.connector.getServer(taskRepository.getRepositoryUrl());
		TuleapProject project = tuleapServer.getProject(projectId);

		TuleapTracker tuleapTracker = project.getTracker(trackerId);
		tuleapTracker = this.connector.refreshTracker(taskRepository, tuleapTracker, monitor);
		response = this.postArtifactTaskData(tuleapTracker, taskData, taskRepository, monitor);

		return response;
	}

	/**
	 * Posts the given task data representing a Tuleap artifact to the server.
	 * 
	 * @param tuleapTracker
	 *            The tracker
	 * @param taskData
	 *            The task data of the artifact
	 * @param taskRepository
	 *            The task repository
	 * @param monitor
	 *            The progress monitor
	 * @return The repository response indicating the creation / update of the artifact
	 * @throws CoreException
	 *             In case of issues during the communication with the server
	 */
	private RepositoryResponse postArtifactTaskData(TuleapTracker tuleapTracker, TaskData taskData,
			TaskRepository taskRepository, IProgressMonitor monitor) throws CoreException {
		RepositoryResponse response = null;

		ArtifactTaskDataConverter artifactTaskDataConverter = new ArtifactTaskDataConverter(tuleapTracker,
				taskRepository, connector);

		TuleapArtifact artifact = artifactTaskDataConverter.createTuleapArtifact(taskData);
		TuleapSoapClient tuleapSoapClient = this.connector.getClientManager().getSoapClient(taskRepository);
		if (taskData.isNew()) {
			String artifactId = tuleapSoapClient.createArtifact(artifact, monitor);
			response = new RepositoryResponse(ResponseKind.TASK_CREATED, artifactId);
		} else {
			tuleapSoapClient.updateArtifact(artifact, monitor);
			response = new RepositoryResponse(ResponseKind.TASK_UPDATED, taskData.getTaskId());
		}

		return response;
	}

	/**
	 * Posts the given task data representing a Tuleap milestone to the server.
	 * 
	 * @param taskData
	 *            The task data of the milestone
	 * @param taskRepository
	 *            The task repository
	 * @param monitor
	 *            The progress monitor
	 * @return The repository response indicating the creation / update of the artifact
	 * @throws CoreException
	 *             In case of issues during the communication with the server
	 */
	private RepositoryResponse postMilestoneTaskData(TaskData taskData, TaskRepository taskRepository,
			IProgressMonitor monitor) throws CoreException {
		RepositoryResponse response = null;

		MilestoneTaskDataConverter milestoneTaskDataConverter = new MilestoneTaskDataConverter(
				taskRepository, connector);

		TuleapRestClient tuleapRestClient = this.connector.getClientManager().getRestClient(taskRepository);
		if (taskData.isNew()) {
			// TODO See with Enalean how this could be possible. An artifact is needed to create a milestone?
			// String milestoneId = tuleapRestClient.createMilestone(tuleapMilestone, monitor);
			// response = new RepositoryResponse(ResponseKind.TASK_CREATED, milestoneId);
		} else {
			List<TuleapBacklogItem> backlog = milestoneTaskDataConverter.extractBacklog(taskData);
			int milestoneId = milestoneTaskDataConverter.getMilestoneId(taskData);

			List<TuleapMilestone> subMilestones = milestoneTaskDataConverter.extractMilestones(taskData);
			for (TuleapMilestone subMilestone : subMilestones) {
				List<TuleapBacklogItem> content = milestoneTaskDataConverter.extractContent(taskData,
						subMilestone.getId());
				tuleapRestClient.updateMilestoneContent(subMilestone.getId(), content, monitor);
			}

			// Now that all sub-milestones are updated, we can re-order the unassigned backlog items.
			tuleapRestClient.updateMilestoneBacklog(milestoneId, backlog, monitor);

			response = new RepositoryResponse(ResponseKind.TASK_UPDATED, taskData.getTaskId());
		}

		return response;
	}

	/**
	 * Posts the given task data representing a Tuleap backlog item to the server.
	 * 
	 * @param taskData
	 *            The task data of the backlog item
	 * @param taskRepository
	 *            The task repository
	 * @param monitor
	 *            The progress monitpr
	 * @return The repository response indicating the creation / update of the artifact
	 * @throws CoreException
	 *             In case of issues during the communication with the server
	 */
	private RepositoryResponse postBacklogItemTaskData(TaskData taskData, TaskRepository taskRepository,
			IProgressMonitor monitor) throws CoreException {
		RepositoryResponse response = null;

		// TODO SBE uncomment to update / create a backlog item when the converter will be done

		// BacklogItemTaskDataConverter backlogItemTaskDataConverter = new BacklogItemTaskDataConverter(
		// tuleapBacklogItemType);
		//
		// TuleapBacklogItem tuleapBacklogItem =
		// backlogItemTaskDataConverter.createTuleapBacklogItem(taskData);
		// TuleapRestClient tuleapRestClient =
		// this.connector.getClientManager().getRestClient(taskRepository);
		// if (taskData.isNew()) {
		// String backlogItemId = tuleapRestClient.createBacklogItem(tuleapBacklogItem, monitor);
		// response = new RepositoryResponse(ResponseKind.TASK_CREATED, backlogItemId);
		// } else {
		// tuleapRestClient.updateBacklogItem(tuleapBacklogItem, monitor);
		// response = new RepositoryResponse(ResponseKind.TASK_UPDATED, taskData.getTaskId());
		// }

		return response;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler#initializeTaskData(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.data.TaskData, org.eclipse.mylyn.tasks.core.ITaskMapping,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public boolean initializeTaskData(TaskRepository repository, TaskData taskData,
			ITaskMapping initializationData, IProgressMonitor monitor) throws CoreException {
		if (!taskData.isNew()) {
			return false;
		}

		boolean isInitialized = false;
		TuleapServer server = this.connector.getServer(repository.getRepositoryUrl());
		if (server != null) {
			// Sets the creation date and last modification date.
			if (initializationData instanceof TuleapTaskMapping) {
				TuleapTaskMapping tuleapTaskMapping = (TuleapTaskMapping)initializationData;
				TuleapTracker tracker = tuleapTaskMapping.getTracker();

				if (tracker != null) {
					TuleapArtifactMapper tuleapArtifactMapper = new TuleapArtifactMapper(taskData, tracker);
					tuleapArtifactMapper.initializeEmptyTaskData();
					Date now = new Date();
					tuleapArtifactMapper.setCreationDate(now);
					tuleapArtifactMapper.setModificationDate(now);
					tuleapArtifactMapper.setSummary(TuleapMylynTasksMessages.getString(
							TuleapMylynTasksMessagesKeys.defaultNewTitle, tracker.getItemName()));

					isInitialized = true;
				}

				if (initializationData instanceof TuleapMilestoneMapping) {
					TuleapMilestoneMapping milestoneMapping = (TuleapMilestoneMapping)initializationData;
					String parentMilestoneId = milestoneMapping.getParentMilestoneId();
					// TODO SBE Set the identifier of the parent milestone in the task data and
					// synchronize it later...
				}
			}
		}
		return isInitialized;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler#getAttributeMapper(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public TaskAttributeMapper getAttributeMapper(TaskRepository repository) {
		return new TuleapAttributeMapper(repository, connector);
	}

	/**
	 * Returns the Mylyn task data for the task matching the given task id in the given Mylyn task repository.
	 * 
	 * @param taskRepository
	 *            the Mylyn task repository
	 * @param taskId
	 *            the id of the task
	 * @param monitor
	 *            the progress monitor
	 * @return The Mylyn task data for the task matching the given task id in the given Mylyn task repository.
	 * @throws CoreException
	 *             If repository file configuration is not accessible
	 */
	public TaskData getTaskData(TaskRepository taskRepository, String taskId, IProgressMonitor monitor)
			throws CoreException {
		TuleapServer server = this.connector.getServer(taskRepository.getRepositoryUrl());

		int projectId = TuleapTaskIdentityUtil.getProjectIdFromTaskDataId(taskId);
		TuleapProject project = server.getProject(projectId);

		int trackerId = TuleapTaskIdentityUtil.getTrackerIdFromTaskDataId(taskId);

		TaskData taskData = null;
		if (trackerId == TuleapTaskIdentityUtil.IRRELEVANT_ID) {
			// Top Planning
			TaskAttributeMapper attributeMapper = this.getAttributeMapper(taskRepository);
			taskData = new TaskData(attributeMapper, ITuleapConstants.CONNECTOR_KIND, taskRepository
					.getRepositoryUrl(), taskId);
			// Load backlog and milestones into the TaskData
			fetchProjectPlanningData(taskData, taskRepository, monitor);
		} else {
			TuleapTracker tracker = project.getTracker(trackerId);
			tracker = this.connector.refreshTracker(taskRepository, tracker, monitor);
			taskData = this.getArtifactTaskData(taskId, server, taskRepository, monitor);

			if (project.isMilestoneTracker(tracker.getIdentifier())) {
				taskData = this.fetchMilestoneData(taskData, project, tracker, taskRepository, monitor);
			}
		}

		return taskData;
	}

	/**
	 * Retrieves the task data representing the Tuleap artifact with the given task id on the given task
	 * repository.
	 * 
	 * @param taskId
	 *            The identifier of the task
	 * @param server
	 *            The server
	 * @param taskRepository
	 *            The task repository
	 * @param monitor
	 *            The progress monitor
	 * @return The task data representing the Tuleap artifact with the given task id
	 * @throws CoreException
	 *             In case of issues during the download of the artifact data
	 */
	private TaskData getArtifactTaskData(String taskId, TuleapServer server, TaskRepository taskRepository,
			IProgressMonitor monitor) throws CoreException {
		TuleapSoapClient tuleapSoapClient = this.connector.getClientManager().getSoapClient(taskRepository);
		TuleapArtifact tuleapArtifact = tuleapSoapClient.getArtifact(taskId, server, monitor);
		if (tuleapArtifact != null) {
			TuleapTracker tracker = server.getTracker(tuleapArtifact.getTracker().getId());

			ArtifactTaskDataConverter artifactTaskDataConverter = new ArtifactTaskDataConverter(tracker,
					taskRepository, connector);
			TaskAttributeMapper attributeMapper = this.getAttributeMapper(taskRepository);

			TaskData taskData = new TaskData(attributeMapper, ITuleapConstants.CONNECTOR_KIND, taskRepository
					.getRepositoryUrl(), taskId);
			artifactTaskDataConverter.populateTaskData(taskData, tuleapArtifact, monitor);

			return taskData;
		}
		return null;
	}

	/**
	 * Adds in the given {@link TaskData} the milestone-specific data after fetching them from the server.
	 * 
	 * @param taskData
	 *            The taskData that should already contain the artifact data
	 * @param project
	 *            The project
	 * @param tracker
	 *            The tracker of the artifact that backs the milestone
	 * @param taskRepository
	 *            The task repository
	 * @param monitor
	 *            The progress monitor
	 * @return The task data representing the Tuleap artifact with the given task id
	 * @throws CoreException
	 *             In case of issues during the download of the artifact data
	 */
	public TaskData fetchMilestoneData(TaskData taskData, TuleapProject project, TuleapTracker tracker,
			TaskRepository taskRepository, IProgressMonitor monitor) throws CoreException {
		TuleapRestClient restClient = this.connector.getClientManager().getRestClient(taskRepository);
		String taskId = taskData.getTaskId();
		int milestoneId = TuleapTaskIdentityUtil.getElementIdFromTaskDataId(taskId);
		TuleapMilestone milestone = restClient.getMilestone(milestoneId, monitor);
		if (milestone != null) {
			MilestoneTaskDataConverter taskDataConverter = new MilestoneTaskDataConverter(taskRepository,
					connector);
			taskDataConverter.populateTaskData(taskData, milestone, monitor);

			// Fetch planning
			try {
				List<TuleapBacklogItem> backlog = restClient.getMilestoneBacklog(milestoneId, monitor);
				taskDataConverter.populateBacklog(taskData, backlog, monitor);
			} catch (CoreException e) {
				TuleapCoreActivator.log(e, true);
			}

			List<TuleapMilestone> subMilestones = restClient.getSubMilestones(milestoneId, monitor);

			for (TuleapMilestone tuleapMilestone : subMilestones) {
				List<TuleapBacklogItem> content;
				try {
					content = restClient.getMilestoneContent(tuleapMilestone.getId(), monitor);
				} catch (CoreException e) {
					TuleapCoreActivator.log(e, true);
					content = Collections.emptyList();
				}
				taskDataConverter.addSubmilestone(taskData, tuleapMilestone, content, monitor);
			}

			// Fetch cardwall if necessary
			if (project.isCardwallActive(tracker.getIdentifier())) {
				try {
					TuleapCardwall cardwall = restClient.getCardwall(milestoneId, monitor);
					taskDataConverter.populateCardwall(taskData, cardwall, monitor);
				} catch (CoreException e) {
					TuleapCoreActivator.log(e, true);
				}
			}

			return taskData;
		}
		return null;
	}

	/**
	 * Adds in the given {@link TaskData} the milestone-specific data after fetching them from the server.
	 * 
	 * @param taskData
	 *            The taskData that should already contain the artifact data
	 * @param taskRepository
	 *            The task repository
	 * @param monitor
	 *            The progress monitor
	 * @return The task data representing the Tuleap artifact with the given task id
	 * @throws CoreException
	 *             In case of issues during the download of the artifact data
	 */
	public TaskData fetchProjectPlanningData(TaskData taskData, TaskRepository taskRepository,
			IProgressMonitor monitor) throws CoreException {
		TuleapRestClient restClient = this.connector.getClientManager().getRestClient(taskRepository);
		String taskId = taskData.getTaskId();
		int projectId = TuleapTaskIdentityUtil.getProjectIdFromTaskDataId(taskId);
		MilestoneTaskDataConverter taskDataConverter = new MilestoneTaskDataConverter(taskRepository,
				connector);

		// Set the task kind to the relevant value
		AgileTaskKindUtil.setAgileTaskKind(taskData, AgileTaskKindUtil.TASK_KIND_TOP_PLANNING);

		List<TuleapBacklogItem> backlog = restClient.getProjectBacklog(projectId, monitor);
		taskDataConverter.populateBacklog(taskData, backlog, monitor);

		List<TuleapMilestone> milestones = restClient.getProjectMilestones(projectId, monitor);

		for (TuleapMilestone tuleapMilestone : milestones) {
			List<TuleapBacklogItem> content = restClient
					.getMilestoneContent(tuleapMilestone.getId(), monitor);
			taskDataConverter.addSubmilestone(taskData, tuleapMilestone, content, monitor);
		}

		return taskData;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler#canGetMultiTaskData(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public boolean canGetMultiTaskData(TaskRepository repository) {
		// TODO SBE Should we support that to improve performances? maybe but it could require improvements in
		// the REST/SOAP API
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler#canInitializeSubTaskData(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.ITask)
	 */
	@Override
	public boolean canInitializeSubTaskData(TaskRepository repository, ITask task) {
		return false;
	}

}
