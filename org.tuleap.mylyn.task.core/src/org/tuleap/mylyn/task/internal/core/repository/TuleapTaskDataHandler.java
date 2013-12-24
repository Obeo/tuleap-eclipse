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
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMetaData;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.data.planning.TopPlanningMapper;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapClient;
import org.tuleap.mylyn.task.internal.core.data.TuleapArtifactMapper;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskId;
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

		TuleapTaskId taskId = mapper.getTaskId();

		TuleapServer tuleapServer = this.connector.getServer(taskRepository.getRepositoryUrl());
		TuleapProject project = tuleapServer.getProject(taskId.getProjectId());
		if (!taskId.isTopPlanning()) {
			TuleapTracker tuleapTracker = project.getTracker(taskId.getTrackerId());
			tuleapTracker = this.connector.refreshTracker(taskRepository, tuleapTracker, monitor);
			response = this.postArtifactTaskData(tuleapTracker, taskData, taskRepository, monitor);
		} else {
			response = this.postTopPlanningTaskData(taskData, taskRepository, monitor);
		}

		return response;
	}

	/**
	 * Posts the given task data representing a Tuleap artifact to the server.
	 * 
	 * @param tracker
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
	private RepositoryResponse postArtifactTaskData(TuleapTracker tracker, TaskData taskData,
			TaskRepository taskRepository, IProgressMonitor monitor) throws CoreException {
		RepositoryResponse response = null;

		ArtifactTaskDataConverter artifactTaskDataConverter = new ArtifactTaskDataConverter(tracker,
				taskRepository, connector);

		TuleapArtifact artifact = artifactTaskDataConverter.createTuleapArtifact(taskData);
		TuleapSoapClient tuleapSoapClient = this.connector.getClientManager().getSoapClient(taskRepository);
		if (taskData.isNew()) {
			TuleapTaskId artifactId = tuleapSoapClient.createArtifact(artifact, monitor);
			response = new RepositoryResponse(ResponseKind.TASK_CREATED, artifactId.toString());
		} else {
			tuleapSoapClient.updateArtifact(artifact, monitor);
			response = new RepositoryResponse(ResponseKind.TASK_UPDATED, taskData.getTaskId());
			if (tracker.getProject().isMilestoneTracker(tracker.getIdentifier())) {
				postMilestoneTaskData(taskData, taskRepository, monitor);
			}
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
						subMilestone.getId().intValue());
				tuleapRestClient.updateMilestoneContent(subMilestone.getId().intValue(), content, monitor);
			}

			// Now that all sub-milestones are updated, we can re-order the unassigned backlog items.
			tuleapRestClient.updateMilestoneBacklog(milestoneId, backlog, monitor);

			response = new RepositoryResponse(ResponseKind.TASK_UPDATED, taskData.getTaskId());
		}

		return response;
	}

	/**
	 * Posts the given task data representing a Tuleap top planning to the server.
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
	private RepositoryResponse postTopPlanningTaskData(TaskData taskData, TaskRepository taskRepository,
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
			int projectId = milestoneTaskDataConverter.getProjectId(taskData);

			List<TuleapMilestone> subMilestones = milestoneTaskDataConverter.extractMilestones(taskData);
			for (TuleapMilestone subMilestone : subMilestones) {
				List<TuleapBacklogItem> content = milestoneTaskDataConverter.extractContent(taskData,
						subMilestone.getId().intValue());
				tuleapRestClient.updateMilestoneContent(subMilestone.getId().intValue(), content, monitor);
			}

			// Now that all sub-milestones are updated, we can re-order the unassigned backlog items.
			tuleapRestClient.updateTopPlanningBacklog(projectId, backlog, monitor);

			response = new RepositoryResponse(ResponseKind.TASK_UPDATED, taskData.getTaskId());
		}

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
	public TaskData getTaskData(TaskRepository taskRepository, TuleapTaskId taskId, IProgressMonitor monitor)
			throws CoreException {
		TuleapServer server = this.connector.getServer(taskRepository.getRepositoryUrl());

		TuleapProject project = server.getProject(taskId.getProjectId());
		int trackerId = taskId.getTrackerId();

		TaskData taskData = null;
		if (taskId.isTopPlanning()) {
			// Top Planning
			TaskAttributeMapper attributeMapper = this.getAttributeMapper(taskRepository);
			taskData = new TaskData(attributeMapper, ITuleapConstants.CONNECTOR_KIND, taskRepository
					.getRepositoryUrl(), taskId.toString());
			// Load backlog and milestones into the TaskData
			fetchProjectPlanningData(taskData, taskRepository, monitor);
		} else if (trackerId == 0) {
			// The tracker information is missing
			taskData = this.getArtifactTaskData(taskId, server, taskRepository, true, monitor);
			TuleapTaskId refreshedId = TuleapTaskId.forName(taskData.getTaskId());
			trackerId = refreshedId.getTrackerId();
			TuleapTracker tracker = project.getTracker(trackerId);
			if (project.isMilestoneTracker(tracker.getIdentifier())) {
				taskData = this.fetchMilestoneData(taskData, project, tracker, taskRepository, monitor);
			}
		} else if (trackerId == -1 && taskId.getArtifactId() == -1) {
			// Workaround linked artifacts v1.0
			// The taskId's projectId is actually the artifact id
			TuleapTaskId actualTaskId = TuleapTaskId.forArtifact(-1, -1, taskId.getProjectId());
			taskData = this.getArtifactTaskData(actualTaskId, server, taskRepository, true, monitor);
			TuleapTaskId refreshedId = TuleapTaskId.forName(taskData.getTaskId());
			trackerId = refreshedId.getTrackerId();
			project = server.getProject(refreshedId.getProjectId());
			TuleapTracker tracker = project.getTracker(trackerId);
			if (project.isMilestoneTracker(tracker.getIdentifier())) {
				taskData = this.fetchMilestoneData(taskData, project, tracker, taskRepository, monitor);
			}
		} else {
			taskData = this.getArtifactTaskData(taskId, server, taskRepository, false, monitor);
			TuleapTracker tracker = project.getTracker(trackerId);
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
	 * @param refreshTracker
	 *            Indicates whether the tracker must be refreshed
	 * @param monitor
	 *            The progress monitor
	 * @return The task data representing the Tuleap artifact with the given task id
	 * @throws CoreException
	 *             In case of issues during the download of the artifact data
	 */
	private TaskData getArtifactTaskData(TuleapTaskId taskId, TuleapServer server,
			TaskRepository taskRepository, boolean refreshTracker, IProgressMonitor monitor)
			throws CoreException {
		TuleapSoapClient tuleapSoapClient = this.connector.getClientManager().getSoapClient(taskRepository);
		TuleapArtifact tuleapArtifact = tuleapSoapClient.getArtifact(taskId, server, monitor);
		TuleapTaskId refreshedTaskId = taskId;
		if (tuleapArtifact != null) {
			TuleapTracker tracker = server.getTracker(tuleapArtifact.getTracker().getId());
			if (refreshTracker) {
				tracker = this.connector.refreshTracker(taskRepository, tracker, monitor);
				// If we refresh the tracker, we also must make sure the taskId does not contain a wrong value
				// for the trackerId
				// If we don't refresh the tracker, we assume the given taskId contains the relevant tracker
				// information
				refreshedTaskId = TuleapTaskId.forArtifact(tuleapArtifact.getProject().getId(), tracker
						.getIdentifier(), refreshedTaskId.getArtifactId());
			}

			ArtifactTaskDataConverter artifactTaskDataConverter = new ArtifactTaskDataConverter(tracker,
					taskRepository, connector);
			TaskAttributeMapper attributeMapper = this.getAttributeMapper(taskRepository);

			TaskData taskData = new TaskData(attributeMapper, ITuleapConstants.CONNECTOR_KIND, taskRepository
					.getRepositoryUrl(), refreshedTaskId.toString());
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
		int milestoneId = TuleapTaskId.forName(taskId).getArtifactId();
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
					content = restClient.getMilestoneContent(tuleapMilestone.getId().intValue(), monitor);
				} catch (CoreException e) {
					TuleapCoreActivator.log(e, true);
					content = Collections.emptyList();
				}
				taskDataConverter.addSubmilestone(taskData, tuleapMilestone, content, monitor);
			}

			// Fetch cardwall if necessary
			if (milestone.getCardwallUri() != null) {
				try {
					TuleapCardwall cardwall = restClient.getCardwall(milestoneId, monitor);
					taskDataConverter.populateCardwall(taskData, cardwall, project, monitor);
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
		int projectId = TuleapTaskId.forName(taskId).getProjectId();
		TopPlanningMapper mapper = new TopPlanningMapper(taskData);
		mapper.initializeEmptyTaskData();
		TuleapProject project = connector.getServer(taskRepository.getUrl()).getProject(projectId);
		if (project != null) {
			mapper.setTaskKey(project.getName());
		}
		TaskAttribute attribute = taskData.getRoot().createMappedAttribute(TuleapArtifactMapper.PROJECT_ID);
		TaskAttributeMetaData metaData = attribute.getMetaData();
		metaData.setType(TaskAttribute.TYPE_SHORT_TEXT);
		metaData.setReadOnly(true);
		attribute.setValue(String.valueOf(projectId));
		MilestoneTaskDataConverter taskDataConverter = new MilestoneTaskDataConverter(taskRepository,
				connector);

		try {
			List<TuleapBacklogItem> backlog = restClient.getProjectBacklog(projectId, monitor);
			taskDataConverter.populateBacklog(taskData, backlog, monitor);
		} catch (CoreException e) {
			TuleapCoreActivator.log(e, true);
		}
		List<TuleapMilestone> milestones;
		try {
			milestones = restClient.getProjectMilestones(projectId, monitor);
		} catch (CoreException e) {
			TuleapCoreActivator.log(e, true);
			milestones = Collections.emptyList();
		}
		for (TuleapMilestone tuleapMilestone : milestones) {
			try {
				List<TuleapBacklogItem> content = restClient.getMilestoneContent(tuleapMilestone.getId()
						.intValue(), monitor);
				taskDataConverter.addSubmilestone(taskData, tuleapMilestone, content, monitor);
			} catch (CoreException e) {
				TuleapCoreActivator.log(e, true);
			}
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
