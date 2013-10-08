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

import java.util.Date;
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
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapClient;
import org.tuleap.mylyn.task.internal.core.data.TuleapConfigurableElementMapper;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskIdentityUtil;
import org.tuleap.mylyn.task.internal.core.data.converter.ArtifactTaskDataConverter;
import org.tuleap.mylyn.task.internal.core.data.converter.MilestoneTaskDataConverter;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableFieldsConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItemType;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestoneType;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapTopPlanning;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;
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

		TuleapConfigurableElementMapper mapper = new TuleapConfigurableElementMapper(taskData, null);

		int projectId = mapper.getProjectId();
		int configurationId = mapper.getConfigurationId();

		TuleapServerConfiguration tuleapServerConfiguration = this.connector
				.getTuleapServerConfiguration(taskRepository.getRepositoryUrl());
		TuleapProjectConfiguration projectConfiguration = tuleapServerConfiguration
				.getProjectConfiguration(projectId);

		AbstractTuleapConfigurableFieldsConfiguration configuration = projectConfiguration
				.getConfigurableFieldsConfiguration(configurationId);

		configuration = this.connector.refreshConfiguration(taskRepository, configuration, monitor);

		if (configuration instanceof TuleapTrackerConfiguration) {
			TuleapTrackerConfiguration tuleapTrackerConfiguration = (TuleapTrackerConfiguration)configuration;
			response = this.postArtifactTaskData(tuleapTrackerConfiguration, taskData, taskRepository,
					monitor);
		} else if (configuration instanceof TuleapMilestoneType) {
			TuleapMilestoneType tuleapMilestoneType = (TuleapMilestoneType)configuration;
			response = this.postMilestoneTaskData(tuleapMilestoneType, taskData, taskRepository, monitor);
		} else if (configuration instanceof TuleapBacklogItemType) {
			TuleapBacklogItemType tuleapBacklogItemType = (TuleapBacklogItemType)configuration;
			response = this.postBacklogItemTaskData(tuleapBacklogItemType, taskData, taskRepository, monitor);
		}

		return response;
	}

	/**
	 * Posts the given task data representing a Tuleap artifact to the server.
	 * 
	 * @param tuleapTrackerConfiguration
	 *            The configuration of the tracker
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
	private RepositoryResponse postArtifactTaskData(TuleapTrackerConfiguration tuleapTrackerConfiguration,
			TaskData taskData, TaskRepository taskRepository, IProgressMonitor monitor) throws CoreException {
		RepositoryResponse response = null;

		ArtifactTaskDataConverter artifactTaskDataConverter = new ArtifactTaskDataConverter(
				tuleapTrackerConfiguration);

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
	 * @param tuleapMilestoneType
	 *            The configuration of the milestone
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
	private RepositoryResponse postMilestoneTaskData(TuleapMilestoneType tuleapMilestoneType,
			TaskData taskData, TaskRepository taskRepository, IProgressMonitor monitor) throws CoreException {
		RepositoryResponse response = null;

		MilestoneTaskDataConverter milestoneTaskDataConverter = new MilestoneTaskDataConverter(
				tuleapMilestoneType);

		TuleapMilestone tuleapMilestone = milestoneTaskDataConverter.createTuleapMilestone(taskData);
		TuleapRestClient tuleapRestClient = this.connector.getClientManager().getRestClient(taskRepository);
		if (taskData.isNew()) {
			String milestoneId = tuleapRestClient.createMilestone(tuleapMilestone, monitor);
			response = new RepositoryResponse(ResponseKind.TASK_CREATED, milestoneId);
		} else {
			tuleapRestClient.updateMilestone(tuleapMilestone, monitor);
			response = new RepositoryResponse(ResponseKind.TASK_UPDATED, taskData.getTaskId());
		}

		return response;
	}

	/**
	 * Posts the given task data representing a Tuleap backlog item to the server.
	 * 
	 * @param tuleapBacklogItemType
	 *            The configuration of the backlog item
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
	private RepositoryResponse postBacklogItemTaskData(TuleapBacklogItemType tuleapBacklogItemType,
			TaskData taskData, TaskRepository taskRepository, IProgressMonitor monitor) throws CoreException {
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
		TuleapServerConfiguration repositoryConfiguration = this.connector
				.getTuleapServerConfiguration(repository.getRepositoryUrl());
		if (repositoryConfiguration != null) {
			// Sets the creation date and last modification date.
			if (initializationData instanceof TuleapTaskMapping) {
				TuleapTaskMapping tuleapTaskMapping = (TuleapTaskMapping)initializationData;

				AbstractTuleapConfigurableFieldsConfiguration configuration = tuleapTaskMapping
						.getConfiguration();

				if (configuration != null) {
					TuleapConfigurableElementMapper tuleapConfigurableElementMapper = new TuleapConfigurableElementMapper(
							taskData, configuration);
					tuleapConfigurableElementMapper.initializeEmptyTaskData();

					// TODO SBE Should we do more for other types of configuration? I don't think so... yet
					// We won't have the other tabs available when we are creating a new item, why not...

					Date now = new Date();
					tuleapConfigurableElementMapper.setCreationDate(now);
					tuleapConfigurableElementMapper.setModificationDate(now);
					tuleapConfigurableElementMapper.setSummary(TuleapMylynTasksMessages.getString(
							TuleapMylynTasksMessagesKeys.defaultNewTitle, configuration.getItemName()));

					isInitialized = true;
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
		TuleapServerConfiguration serverConfiguration = this.connector
				.getTuleapServerConfiguration(taskRepository.getRepositoryUrl());

		int projectId = TuleapTaskIdentityUtil.getProjectIdFromTaskDataId(taskId);
		TuleapProjectConfiguration projectConfiguration = serverConfiguration
				.getProjectConfiguration(projectId);

		int configurationId = TuleapTaskIdentityUtil.getConfigurationIdFromTaskDataId(taskId);

		TaskData taskData = null;
		if (configurationId == TuleapTaskIdentityUtil.IRRELEVANT_ID) {
			// Top Planning
			taskData = this.getTopPlanningTaskData(taskId, serverConfiguration, taskRepository, monitor);
		} else {
			AbstractTuleapConfigurableFieldsConfiguration configuration = projectConfiguration
					.getConfigurableFieldsConfiguration(configurationId);
			configuration = this.connector.refreshConfiguration(taskRepository, configuration, monitor);

			if (configuration instanceof TuleapTrackerConfiguration) {
				taskData = this.getArtifactTaskData(taskId, serverConfiguration, taskRepository, monitor);
			} else if (configuration instanceof TuleapMilestoneType) {
				taskData = this.getMilestoneTaskData(taskId, serverConfiguration, taskRepository, monitor);
			} else if (configuration instanceof TuleapBacklogItemType) {
				// TODO SBE retrieval of the backlog item from the server
				// taskData = this.getBacklogItemTaskData(taskId, serverConfiguration, taskRepository,
				// monitor);
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
	 * @param serverConfiguration
	 *            The configuration of the server
	 * @param taskRepository
	 *            The task repository
	 * @param monitor
	 *            The progress monitor
	 * @return The task data representing the Tuleap artifact with the given task id
	 * @throws CoreException
	 *             In case of issues during the download of the artifact data
	 */
	private TaskData getArtifactTaskData(String taskId, TuleapServerConfiguration serverConfiguration,
			TaskRepository taskRepository, IProgressMonitor monitor) throws CoreException {
		TuleapSoapClient tuleapSoapClient = this.connector.getClientManager().getSoapClient(taskRepository);
		TuleapArtifact tuleapArtifact = tuleapSoapClient.getArtifact(taskId, serverConfiguration, monitor);
		if (tuleapArtifact != null) {
			TuleapTrackerConfiguration trackerConfiguration = serverConfiguration
					.getTrackerConfiguration(tuleapArtifact.getConfigurationId());

			ArtifactTaskDataConverter artifactTaskDataConverter = new ArtifactTaskDataConverter(
					trackerConfiguration);
			TaskAttributeMapper attributeMapper = this.getAttributeMapper(taskRepository);

			TaskData taskData = new TaskData(attributeMapper, ITuleapConstants.CONNECTOR_KIND, taskRepository
					.getRepositoryUrl(), taskId);
			artifactTaskDataConverter.populateTaskData(taskData, tuleapArtifact);

			return taskData;
		}
		return null;
	}

	/**
	 * Retrieves the task data representing the Tuleap top planning with the given task id on the given task
	 * repository.
	 * 
	 * @param taskId
	 *            The identifier of the task
	 * @param serverConfiguration
	 *            The configuration of the server
	 * @param taskRepository
	 *            The task repository
	 * @param monitor
	 *            The progress monitor
	 * @return The task data representing the Tuleap artifact with the given task id
	 * @throws CoreException
	 *             In case of issues during the download of the artifact data
	 */
	private TaskData getTopPlanningTaskData(String taskId, TuleapServerConfiguration serverConfiguration,
			TaskRepository taskRepository, IProgressMonitor monitor) throws CoreException {
		TuleapRestClient restClient = this.connector.getClientManager().getRestClient(taskRepository);
		TuleapTopPlanning topPlanning = restClient.getTopPlanning(TuleapTaskIdentityUtil
				.getElementIdFromTaskDataId(taskId), monitor);
		if (topPlanning != null) {
			MilestoneTaskDataConverter taskDataConverter = new MilestoneTaskDataConverter(null);
			TaskAttributeMapper attributeMapper = this.getAttributeMapper(taskRepository);

			TaskData taskData = new TaskData(attributeMapper, ITuleapConstants.CONNECTOR_KIND, taskRepository
					.getRepositoryUrl(), taskId);
			int projectId = TuleapTaskIdentityUtil.getProjectIdFromTaskDataId(taskId);
			taskDataConverter.populateTaskData(taskData, topPlanning, projectId);

			return taskData;
		}
		return null;
	}

	/**
	 * Retrieves the task data representing the Tuleap top planning with the given task id on the given task
	 * repository.
	 * 
	 * @param taskId
	 *            The identifier of the task
	 * @param serverConfiguration
	 *            The configuration of the server
	 * @param taskRepository
	 *            The task repository
	 * @param monitor
	 *            The progress monitor
	 * @return The task data representing the Tuleap artifact with the given task id
	 * @throws CoreException
	 *             In case of issues during the download of the artifact data
	 */
	private TaskData getMilestoneTaskData(String taskId, TuleapServerConfiguration serverConfiguration,
			TaskRepository taskRepository, IProgressMonitor monitor) throws CoreException {
		TuleapRestClient restClient = this.connector.getClientManager().getRestClient(taskRepository);
		int projectId = TuleapTaskIdentityUtil.getProjectIdFromTaskDataId(taskId);
		int milestoneTypeId = TuleapTaskIdentityUtil.getConfigurationIdFromTaskDataId(taskId);
		TuleapMilestoneType milestoneType = serverConfiguration.getProjectConfiguration(projectId)
				.getMilestoneType(milestoneTypeId);
		TuleapMilestone milestone = restClient.getMilestone(TuleapTaskIdentityUtil
				.getElementIdFromTaskDataId(taskId), milestoneType.hasCardwall(), monitor);
		if (milestone != null) {
			MilestoneTaskDataConverter taskDataConverter = new MilestoneTaskDataConverter(milestoneType);
			TaskAttributeMapper attributeMapper = this.getAttributeMapper(taskRepository);

			TaskData taskData = new TaskData(attributeMapper, ITuleapConstants.CONNECTOR_KIND, taskRepository
					.getRepositoryUrl(), taskId);
			taskDataConverter.populateTaskData(taskData, milestone);

			return taskData;
		}
		return null;
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
