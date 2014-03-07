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
import org.tuleap.mylyn.task.internal.core.data.TuleapArtifactMapper;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskId;
import org.tuleap.mylyn.task.internal.core.data.converter.ArtifactTaskDataConverter;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapProject;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapServer;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapTracker;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapArtifactWithComment;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessagesKeys;

/**
 * This class is in charge of the publication and retrieval of the tasks data to and from the repository.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
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
		TuleapTracker tuleapTracker = project.getTracker(taskId.getTrackerId());
		tuleapTracker = this.connector.refreshTracker(taskRepository, tuleapTracker, monitor);
		response = this.postArtifactTaskData(tuleapTracker, taskData, taskRepository, monitor);

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

		TuleapRestClient client = this.connector.getClientManager().getRestClient(taskRepository);
		if (taskData.isNew()) {
			TuleapArtifact artifact = artifactTaskDataConverter.createTuleapArtifact(taskData);
			TuleapTaskId artifactId = client.createArtifact(artifact, monitor);
			response = new RepositoryResponse(ResponseKind.TASK_CREATED, artifactId.toString());
		} else {
			TuleapArtifactWithComment artifact = artifactTaskDataConverter
					.createTuleapArtifactWithComment(taskData);
			client.updateArtifact(artifact, monitor);
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
				TuleapArtifactMapper tuleapArtifactMapper = null;
				if (tracker != null) {
					tuleapArtifactMapper = new TuleapArtifactMapper(taskData, tracker);
					tuleapArtifactMapper.initializeEmptyTaskData();
					Date now = new Date();
					tuleapArtifactMapper.setCreationDate(now);
					tuleapArtifactMapper.setModificationDate(now);
					tuleapArtifactMapper.setSummary(TuleapMylynTasksMessages.getString(
							TuleapMylynTasksMessagesKeys.defaultNewTitle, tracker.getItemName()));
					isInitialized = true;
					taskData.getRoot().removeAttribute(TaskAttribute.COMMENT_NEW);
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

		int trackerId = taskId.getTrackerId();

		TaskData taskData = null;
		if (trackerId == -1 && taskId.getArtifactId() == -1) {
			// Workaround linked artifacts v1.0
			// The taskId's projectId is actually the artifact id
			TuleapTaskId actualTaskId = TuleapTaskId.forArtifact(-1, -1, taskId.getProjectId());
			taskData = this.getArtifactTaskData(actualTaskId, server, taskRepository, true, monitor);
		} else {
			taskData = this.getArtifactTaskData(taskId, server, taskRepository, false, monitor);
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
		TuleapRestClient client = this.connector.getClientManager().getRestClient(taskRepository);
		TuleapArtifact tuleapArtifact = client.getArtifact(taskId.getArtifactId(), server, monitor);
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
