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
package org.tuleap.mylyn.task.core.internal.repository;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
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
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.TopPlanningMapper;
import org.tuleap.mylyn.task.core.internal.TuleapCoreActivator;
import org.tuleap.mylyn.task.core.internal.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.core.internal.data.TuleapArtifactMapper;
import org.tuleap.mylyn.task.core.internal.data.TuleapTaskId;
import org.tuleap.mylyn.task.core.internal.data.converter.ArtifactTaskDataConverter;
import org.tuleap.mylyn.task.core.internal.data.converter.MilestoneTaskDataConverter;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapProject;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapServer;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapTracker;
import org.tuleap.mylyn.task.core.internal.model.data.ResourceDescription;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapArtifact;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapArtifactWithComment;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapElementComment;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapReference;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapBurndown;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapCard;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapCardwall;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapMilestone;
import org.tuleap.mylyn.task.core.internal.util.ITuleapConstants;
import org.tuleap.mylyn.task.core.internal.util.TuleapCoreKeys;
import org.tuleap.mylyn.task.core.internal.util.TuleapCoreMessages;

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

		TuleapServer tuleapServer = this.connector.getServer(taskRepository);
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

		TuleapRestClient client = this.connector.getClientManager().getRestClient(taskRepository);
		if (taskData.isNew()) {
			TuleapArtifact artifact = artifactTaskDataConverter.createTuleapArtifact(taskData);
			Assert.isTrue(artifact.isNew());
			TuleapTaskId artifactId = client.createArtifact(artifact, monitor);
			response = new RepositoryResponse(ResponseKind.TASK_CREATED, artifactId.toString());
			if (tracker.getProject().isMilestoneTracker(tracker.getIdentifier())) {
				addMilestoneTaskDataToParent(taskData, artifactId, taskRepository, monitor);
			} else if (tracker.getProject().isBacklogTracker(tracker.getIdentifier())) {
				TuleapArtifactMapper tuleapArtifactMapper = new TuleapArtifactMapper(taskData, tracker);
				String parentMilestoneId = tuleapArtifactMapper.getParentId();
				if (parentMilestoneId != null) {
					client.addBacklogItemToMilestoneBacklog(TuleapTaskId.forName(parentMilestoneId)
							.getArtifactId(), artifactId.getArtifactId(), monitor);
				}
			}
		} else {
			TuleapArtifactWithComment artifact = artifactTaskDataConverter
					.createTuleapArtifactWithComment(taskData);
			Assert.isTrue(!artifact.isNew());
			client.updateArtifact(artifact, monitor);
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

		Assert.isTrue(!taskData.isNew(), TuleapCoreMessages
				.getString(TuleapCoreKeys.attemptToCreateNewTopPlanning));

		List<TuleapBacklogItem> backlog = milestoneTaskDataConverter.extractBacklog(taskData);
		int milestoneId = milestoneTaskDataConverter.getMilestoneId(taskData);

		List<TuleapMilestone> subMilestones = milestoneTaskDataConverter.extractMilestones(taskData);
		List<CoreException> exceptions = Lists.newArrayList();
		for (TuleapMilestone subMilestone : subMilestones) {
			TuleapTaskId subMilestoneTaskId = TuleapTaskId.forArtifact(subMilestone.getProject().getId(),
					subMilestone.getArtifact().getTracker().getId(), subMilestone.getArtifact().getId());
			if (milestoneTaskDataConverter.mustUpdate(taskData, subMilestoneTaskId)) {
				List<TuleapBacklogItem> content = milestoneTaskDataConverter.extractContent(taskData,
						subMilestoneTaskId);
				try {
					tuleapRestClient
							.updateMilestoneContent(subMilestone.getId().intValue(), content, monitor);
				} catch (CoreException e) {
					exceptions.add(e);
				}
			}
		}

		// Now that all sub-milestones are updated, we can re-order the unassigned backlog items.
		if (milestoneTaskDataConverter.mustUpdateBacklog(taskData)) {
			try {
				tuleapRestClient.updateMilestoneBacklog(milestoneId, backlog, monitor);
			} catch (CoreException e) {
				exceptions.add(e);
			}
		}

		// Update the Cards
		List<TuleapCard> cards = milestoneTaskDataConverter.extractModifiedCards(taskData);
		for (TuleapCard tuleapCard : cards) {
			try {
				tuleapRestClient.updateCard(tuleapCard, monitor);
			} catch (CoreException e) {
				exceptions.add(e);
			}
		}

		checkSubmission(exceptions);

		response = new RepositoryResponse(ResponseKind.TASK_UPDATED, taskData.getTaskId());

		return response;
	}

	/**
	 * Add the given task data representing a Tuleap milestone to its parent.
	 *
	 * @param taskData
	 *            The task data of the milestone
	 * @param taskId
	 *            the taskId
	 * @param taskRepository
	 *            The task repository
	 * @param monitor
	 *            The progress monitor
	 * @throws CoreException
	 *             In case of issues during the communication with the server
	 */
	private void addMilestoneTaskDataToParent(TaskData taskData, TuleapTaskId taskId,
			TaskRepository taskRepository, IProgressMonitor monitor) throws CoreException {

		TuleapServer server = this.connector.getServer(taskRepository);
		TuleapTracker tracker = server.getTracker(taskId.getTrackerId());
		TuleapArtifactMapper tuleapArtifactMapper = new TuleapArtifactMapper(taskData, tracker);
		String parentMilestoneId = tuleapArtifactMapper.getParentId();

		if (parentMilestoneId != null) {
			TuleapRestClient tuleapRestClient = this.connector.getClientManager().getRestClient(
					taskRepository);
			TuleapTaskId tuleapTaskId = TuleapTaskId.forName(parentMilestoneId);
			if (!tuleapTaskId.isTopPlanning()) {
				int parentMilestoneSimpleId = tuleapTaskId.getArtifactId();
				List<TuleapMilestone> subMilestones = tuleapRestClient.getSubMilestones(TuleapTaskId.forName(
						parentMilestoneId).getArtifactId(), monitor);

				int id = taskId.getArtifactId();
				int projectId = taskId.getProjectId();
				TuleapReference projectRef = new TuleapReference();
				projectRef.setId(projectId);
				TuleapMilestone milestone = new TuleapMilestone(id, projectRef);
				subMilestones.add(milestone);
				tuleapRestClient
						.updateMilestoneSubmilestones(parentMilestoneSimpleId, subMilestones, monitor);
			}
		}
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
		Assert.isTrue(!taskData.isNew(), TuleapCoreMessages
				.getString(TuleapCoreKeys.attemptToCreateNewTopPlanning));
		// The taskData
		List<TuleapBacklogItem> backlog = milestoneTaskDataConverter.extractBacklog(taskData);
		int projectId = milestoneTaskDataConverter.getProjectId(taskData);

		List<TuleapMilestone> subMilestones = milestoneTaskDataConverter.extractMilestones(taskData);
		List<CoreException> exceptions = Lists.newArrayList();
		for (TuleapMilestone subMilestone : subMilestones) {
			TuleapTaskId subMilestoneTaskId = TuleapTaskId.forArtifact(subMilestone.getProject().getId(),
					subMilestone.getArtifact().getTracker().getId(), subMilestone.getArtifact().getId());
			if (milestoneTaskDataConverter.mustUpdate(taskData, subMilestoneTaskId)) {
				List<TuleapBacklogItem> content = milestoneTaskDataConverter.extractContent(taskData,
						subMilestoneTaskId);
				try {
					tuleapRestClient
							.updateMilestoneContent(subMilestone.getId().intValue(), content, monitor);
				} catch (CoreException e) {
					exceptions.add(e);
				}
			}
		}

		// Now that all sub-milestones are updated, we can re-order the unassigned backlog items.
		if (milestoneTaskDataConverter.mustUpdateBacklog(taskData)) {
			try {
				tuleapRestClient.updateTopPlanningBacklog(projectId, backlog, monitor);
			} catch (CoreException e) {
				exceptions.add(e);
			}
		}

		checkSubmission(exceptions);

		response = new RepositoryResponse(ResponseKind.TASK_UPDATED, taskData.getTaskId());

		return response;
	}

	/**
	 * Checks that the given list is empty. If not, logs the contained exceptions and throws an exception to
	 * attempt to guide the user.
	 *
	 * @param exceptions
	 *            The list of exceptions.
	 * @throws TuleapSubmitException
	 *             If the given list is not empty.
	 */
	private void checkSubmission(List<CoreException> exceptions) throws TuleapSubmitException {
		if (!exceptions.isEmpty()) {
			for (CoreException e : exceptions) {
				TuleapCoreActivator.log(e, false);
			}
			throw new TuleapSubmitException(TuleapCoreMessages.getString(
					TuleapCoreKeys.problemsOccurredDuringSubmit, Integer.valueOf(exceptions.size())));
		}
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
		TuleapServer server = this.connector.getServer(repository);
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
					tuleapArtifactMapper.setSummary(TuleapCoreMessages.getString(
							TuleapCoreKeys.defaultNewTitle, tracker.getItemName()));
					isInitialized = true;
					taskData.getRoot().removeAttribute(TaskAttribute.COMMENT_NEW);
				}
				if (initializationData instanceof TuleapMilestoneMapping) {
					TuleapMilestoneMapping milestoneMapping = (TuleapMilestoneMapping)initializationData;
					String parentMilestoneId = milestoneMapping.getParentMilestoneId();
					synchronizeMilestoneParentAttributes(tuleapArtifactMapper, parentMilestoneId, server);
				}
				if (initializationData instanceof TuleapBacklogItemMapping) {
					TuleapBacklogItemMapping backlogItemMapping = (TuleapBacklogItemMapping)initializationData;
					String parentMilestoneId = backlogItemMapping.getParentMilestoneId();
					synchronizeMilestoneParentAttributes(tuleapArtifactMapper, parentMilestoneId, server);
				}
			}
		}
		return isInitialized;
	}

	/**
	 * Add the milestone parent information to its task attribute.
	 *
	 * @param tuleapArtifactMapper
	 *            The artifact mapper
	 * @param parentMilestoneId
	 *            The parent milestone Id
	 * @param server
	 *            The tuleap server
	 */
	private void synchronizeMilestoneParentAttributes(TuleapArtifactMapper tuleapArtifactMapper,
			String parentMilestoneId, TuleapServer server) {
		if (tuleapArtifactMapper != null && !TuleapTaskId.forName(parentMilestoneId).isTopPlanning()) {
			tuleapArtifactMapper.setParentId(parentMilestoneId);
			tuleapArtifactMapper.setParentDisplayId(getMessageTracker(parentMilestoneId, server));
		}
	}

	/**
	 * Create the message indicating a milestone parent id.
	 *
	 * @param taskId
	 *            The task Id
	 * @param server
	 *            The server
	 * @return The message
	 */
	private String getMessageTracker(String taskId, TuleapServer server) {

		int trackerId = TuleapTaskId.forName(taskId).getTrackerId();
		int artifactId = TuleapTaskId.forName(taskId).getArtifactId();
		TuleapTracker tracker = server.getTracker(trackerId);

		return TuleapCoreMessages.getString(TuleapCoreKeys.trackerLabel, tracker.getItemName(), Integer
				.valueOf(artifactId));
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
		TuleapServer server = this.connector.getServer(taskRepository);

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
		TuleapRestClient client = this.connector.getClientManager().getRestClient(taskRepository);
		TuleapArtifact tuleapArtifact = client.getArtifact(taskId.getArtifactId(), server, monitor);
		TuleapTaskId refreshedTaskId = taskId;
		if (tuleapArtifact != null) {
			for (TuleapElementComment comment : client.getArtifactComments(tuleapArtifact.getId().intValue(),
					server, monitor)) {
				tuleapArtifact.addComment(comment);
			}
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

			if (milestone.getResources() != null) {
				fetchMilestoneResources(taskData, monitor, restClient, milestoneId, milestone,
						taskDataConverter);
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

			// Fetch burndown if necessary
			if (milestone.getBurndownUri() != null) {
				try {
					TuleapBurndown burndown = restClient.getMilestoneBurndown(milestoneId, monitor);
					taskDataConverter.populateBurndown(taskData, burndown, monitor);
				} catch (CoreException e) {
					TuleapCoreActivator.log(e, true);
				}
			}

			return taskData;
		}
		return null;
	}

	/**
	 * Fetch milestone available resources.
	 *
	 * @param taskData
	 *            the taskData
	 * @param monitor
	 *            The progress monitor
	 * @param restClient
	 *            The rest client
	 * @param milestoneId
	 *            The milestone Id
	 * @param milestone
	 *            The milestone
	 * @param taskDataConverter
	 *            The milestone task data converter
	 * @throws CoreException
	 *             In case of issues during the download of the artifact data
	 */
	private void fetchMilestoneResources(TaskData taskData, IProgressMonitor monitor,
			TuleapRestClient restClient, int milestoneId, TuleapMilestone milestone,
			MilestoneTaskDataConverter taskDataConverter) throws CoreException {
		// Fetch planning
		MilestonePlanningWrapper milestonePlanning = new MilestonePlanningWrapper(taskData.getRoot());
		try {
			if (milestone.getResources().get(ResourceDescription.BACKLOG) != null) {
				List<TuleapBacklogItem> backlog = restClient.getMilestoneBacklog(milestoneId, monitor);
				taskDataConverter.populateBacklog(taskData, backlog, monitor);
			}
		} catch (CoreException e) {
			TuleapCoreActivator.log(e, true);
		}

		if (milestone.getResources().get(ResourceDescription.MILESTONES) != null) {
			milestonePlanning.setAllowedToHaveSubmilestones(true);
			List<TuleapMilestone> subMilestones = restClient.getSubMilestones(milestoneId, monitor);

			for (TuleapMilestone tuleapMilestone : subMilestones) {
				if (tuleapMilestone.getResources().get(ResourceDescription.CONTENT) != null) {
					List<TuleapBacklogItem> content;
					try {
						content = restClient.getMilestoneContent(tuleapMilestone.getId().intValue(), monitor);

					} catch (CoreException e) {
						TuleapCoreActivator.log(e, true);
						content = Collections.emptyList();
					}
					taskDataConverter.addSubmilestone(taskData, tuleapMilestone, content, monitor);
				}
			}
		}
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
		TuleapProject project = connector.getServer(taskRepository).getProject(projectId);
		if (project != null) {
			mapper.setTaskKey(project.getLabel());
		}
		TaskAttribute attribute = taskData.getRoot().createMappedAttribute(TuleapArtifactMapper.PROJECT_ID);
		TaskAttributeMetaData metaData = attribute.getMetaData();
		metaData.setType(TaskAttribute.TYPE_SHORT_TEXT);
		metaData.setReadOnly(true);
		attribute.setValue(String.valueOf(projectId));
		MilestoneTaskDataConverter taskDataConverter = new MilestoneTaskDataConverter(taskRepository,
				connector);

		List<CoreException> exceptions = new ArrayList<CoreException>();
		try {
			List<TuleapBacklogItem> backlog = restClient.getProjectBacklog(projectId, monitor);
			taskDataConverter.populateBacklog(taskData, backlog, monitor);
		} catch (CoreException e) {
			exceptions.add(e);
		}
		List<TuleapMilestone> milestones;
		try {
			milestones = restClient.getProjectMilestones(projectId, monitor);
		} catch (CoreException e) {
			exceptions.add(e);
			milestones = Collections.emptyList();
		}
		// Add the submilestones attribute to taskData since it is the Top planning
		new MilestonePlanningWrapper(taskData.getRoot()).setAllowedToHaveSubmilestones(true);
		for (TuleapMilestone tuleapMilestone : milestones) {
			try {
				List<TuleapBacklogItem> content = restClient.getMilestoneContent(tuleapMilestone.getId()
						.intValue(), monitor);
				taskDataConverter.addSubmilestone(taskData, tuleapMilestone, content, monitor);
			} catch (CoreException e) {
				exceptions.add(e);
			}
		}

		checkRetrieval(exceptions);

		return taskData;
	}

	/**
	 * Checks that the given list is empty. If not, logs the contained exceptions and throws an exception to
	 * attempt to guide the user.
	 *
	 * @param exceptions
	 *            The list of exceptions.
	 * @throws TuleapRetrieveException
	 *             If the given list is not empty.
	 */
	private void checkRetrieval(List<CoreException> exceptions) throws TuleapRetrieveException {
		if (!exceptions.isEmpty()) {
			for (CoreException e : exceptions) {
				TuleapCoreActivator.log(e, false);
			}
			throw new TuleapRetrieveException(TuleapCoreMessages.getString(
					TuleapCoreKeys.problemsOccurredDuringRetrieve, Integer.valueOf(exceptions.size())));
		}
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
