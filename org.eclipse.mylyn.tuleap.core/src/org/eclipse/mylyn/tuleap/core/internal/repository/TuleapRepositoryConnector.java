/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.tuleap.core.internal.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentHandler;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.eclipse.mylyn.tasks.core.data.TaskMapper;
import org.eclipse.mylyn.tasks.core.sync.ISynchronizationSession;
import org.eclipse.mylyn.tuleap.core.internal.TuleapCoreActivator;
import org.eclipse.mylyn.tuleap.core.internal.client.ITuleapQueryConstants;
import org.eclipse.mylyn.tuleap.core.internal.client.TuleapClientManager;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.TuleapRestClient;
import org.eclipse.mylyn.tuleap.core.internal.data.TuleapArtifactMapper;
import org.eclipse.mylyn.tuleap.core.internal.data.TuleapTaskId;
import org.eclipse.mylyn.tuleap.core.internal.data.converter.ArtifactTaskDataConverter;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapProject;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapServer;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapUser;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapUserGroup;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.AbstractTuleapSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBoxItem;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifact;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapElementComment;
import org.eclipse.mylyn.tuleap.core.internal.util.ITuleapConstants;
import org.eclipse.mylyn.tuleap.core.internal.util.TuleapCoreKeys;
import org.eclipse.mylyn.tuleap.core.internal.util.TuleapCoreMessages;

/**
 * This class encapsulates common operation realized on the Tuleap repository.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapRepositoryConnector extends AbstractRepositoryConnector implements ITuleapRepositoryConnector {

	/**
	 * The task data handler.
	 */
	private final TuleapTaskDataHandler taskDataHandler = new TuleapTaskDataHandler(this);

	/**
	 * The Tuleap client manager.
	 */
	private TuleapClientManager clientManager;

	/**
	 * The repository configuration file.
	 */
	private File repositoryConfigurationFile;

	/**
	 * The cache of the repository servers.
	 */
	private final Map<String, TuleapServer> serversByUrl = new HashMap<String, TuleapServer>();

	/**
	 * Indicates that the cache of the repository configuration has been read.
	 */
	private boolean cacheFileRead;

	/**
	 * The constructor.
	 */
	public TuleapRepositoryConnector() {
		if (TuleapCoreActivator.getDefault() != null) {
			TuleapCoreActivator.getDefault().setConnector(this);
			IPath path = TuleapCoreActivator.getDefault().getConfigurationCachePath();
			this.repositoryConfigurationFile = path.toFile();
			this.readRepositoryConfigurationFile();
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#getConnectorKind()
	 */
	@Override
	public String getConnectorKind() {
		return ITuleapConstants.CONNECTOR_KIND;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#getLabel()
	 */
	@Override
	public String getLabel() {
		return TuleapCoreMessages.getString(TuleapCoreKeys.tuleapRepositoryConnectorLabel);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#canCreateNewTask(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public boolean canCreateNewTask(TaskRepository repository) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#canCreateTaskFromKey(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public boolean canCreateTaskFromKey(TaskRepository repository) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#getRepositoryUrlFromTaskUrl(java.lang.String)
	 */
	@Override
	public String getRepositoryUrlFromTaskUrl(String url) {
		if (url == null) {
			return null;
		}
		return TuleapUrlUtil.getRepositoryUrlFromTaskUrl(url);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#getTaskIdFromTaskUrl(java.lang.String)
	 */
	@Override
	public String getTaskIdFromTaskUrl(String url) {
		if (url == null) {
			return null;
		}

		return TuleapTaskId.forTaskUrl(url).toString();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#getTaskDataHandler()
	 */
	@Override
	public TuleapTaskDataHandler getTaskDataHandler() {
		return this.taskDataHandler;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#getTaskAttachmentHandler()
	 */
	@Override
	public AbstractTaskAttachmentHandler getTaskAttachmentHandler() {
		return new TuleapTaskAttachmentHandler(this, this.getClientManager());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#getTaskUrl(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String getTaskUrl(String repositoryUrl, String taskId) {
		return TuleapTaskId.forName(taskId).getTaskUrl(repositoryUrl);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#getTaskData(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      java.lang.String, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public TaskData getTaskData(TaskRepository taskRepository, String taskId, IProgressMonitor monitor)
			throws CoreException {
		return this.taskDataHandler.getTaskData(taskRepository, TuleapTaskId.forName(taskId), monitor);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#hasTaskChanged(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.ITask, org.eclipse.mylyn.tasks.core.data.TaskData)
	 */
	@Override
	public boolean hasTaskChanged(TaskRepository taskRepository, ITask task, TaskData taskData) {
		ITaskMapping scheme = getTaskMapping(taskData);
		Date repositoryDate = scheme.getModificationDate();
		Date localDate = task.getModificationDate();
		if (repositoryDate == null && localDate == null || repositoryDate != null
				&& repositoryDate.equals(localDate)) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#canQuery(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public boolean canQuery(TaskRepository repository) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tuleap.core.internal.repository.ITuleapRepositoryConnector#getClientManager()
	 */
	public TuleapClientManager getClientManager() {
		if (clientManager == null) {
			clientManager = new TuleapClientManager();
		}
		return clientManager;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#updateNewTaskFromTaskData(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.ITask, org.eclipse.mylyn.tasks.core.data.TaskData)
	 */
	@Override
	public void updateNewTaskFromTaskData(TaskRepository taskRepository, ITask task, TaskData taskData) {
		// Let's do the job in the task data handler
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#performQuery(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.IRepositoryQuery,
	 *      org.eclipse.mylyn.tasks.core.data.TaskDataCollector,
	 *      org.eclipse.mylyn.tasks.core.sync.ISynchronizationSession,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus performQuery(TaskRepository taskRepository, IRepositoryQuery query,
			TaskDataCollector collector, ISynchronizationSession session, IProgressMonitor monitor) {
		if (monitor != null) {
			monitor.subTask("Executing query " + query.getSummary()); //$NON-NLS-1$
		}
		IStatus status = Status.OK_STATUS;
		// Populate the collector with the task data resulting from the query
		String queryKind = query.getAttribute(ITuleapQueryConstants.QUERY_KIND);
		if (ITuleapQueryConstants.QUERY_KIND_REPORT.equals(queryKind)
				|| ITuleapQueryConstants.QUERY_KIND_CUSTOM.equals(queryKind)) {
			try {
				performReportOrCustomQuery(taskRepository, query, collector, monitor);
			} catch (CoreException e) {
				status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
			}
		} else {
			status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
					"Invalid state: unknown query kind."); //$NON-NLS-1$
		}
		return status;
	}

	/**
	 * Perform a "report" query.
	 *
	 * @param taskRepository
	 *            The task repository
	 * @param query
	 *            The query to execute
	 * @param collector
	 *            The task data collector
	 * @param monitor
	 *            The progress monitor
	 * @throws CoreException
	 *             If artifact retrieval goes wrong.
	 */
	private void performReportOrCustomQuery(TaskRepository taskRepository, IRepositoryQuery query,
			TaskDataCollector collector, IProgressMonitor monitor) throws CoreException {
		TuleapRestClient client = this.getClientManager().getRestClient(taskRepository);
		int trackerId = Integer.valueOf(query.getAttribute(ITuleapQueryConstants.QUERY_TRACKER_ID))
				.intValue();
		TuleapServer server = this.getServer(taskRepository);
		TuleapTracker tracker = server.getTracker(trackerId);
		try {
			tracker = this.refreshTracker(taskRepository, tracker, monitor);
		} catch (CoreException e) {
			TuleapCoreActivator.log(e, true);
		}
		ArtifactTaskDataConverter artifactTaskDataConverter = new ArtifactTaskDataConverter(tracker,
				taskRepository, this);
		List<TuleapArtifact> artifacts = getRawArtifacts(query, tracker, client, monitor);
		if (artifacts != null) {
			for (TuleapArtifact artifact : artifacts) {
				if (monitor != null && monitor.isCanceled()) {
					return;
				}
				TuleapTaskId taskDataId = TuleapTaskId.forArtifact(tracker.getProject().getIdentifier(),
						artifact.getTracker().getId(), artifact.getId().intValue());
				try {
					List<TuleapElementComment> artifactComments = client.getArtifactComments(artifact.getId()
							.intValue(), server, monitor);
					for (TuleapElementComment comment : artifactComments) {
						artifact.addComment(comment);
					}
				} catch (CoreException e) {
					TuleapCoreActivator.log(e, true);
				}

				TaskAttributeMapper attributeMapper = this.getTaskDataHandler().getAttributeMapper(
						taskRepository);
				TaskData taskData = new TaskData(attributeMapper, this.getConnectorKind(), taskRepository
						.getRepositoryUrl(), taskDataId.toString());
				artifactTaskDataConverter.populateTaskData(taskData, artifact, monitor);
				try {
					collector.accept(taskData);
				} catch (IllegalArgumentException exception) {
					// Do not log, the query has been deleted while it was executed, see:
					// org.eclipse.mylyn.internal.tasks.core.TaskList.getValidElement(IRepositoryElement)
				}
			}
		}
	}

	/**
	 * Retrieves artifacts without comments for the given query.
	 *
	 * @param query
	 *            The query, must be a custom or report query
	 * @param tracker
	 *            The tracker
	 * @param client
	 *            The Client to use.
	 * @param monitor
	 *            The progress monitor
	 * @throws CoreException
	 *             if communication goes wrong.
	 * @return The list of artifacts, without their comments.
	 */
	private List<TuleapArtifact> getRawArtifacts(IRepositoryQuery query, TuleapTracker tracker,
			TuleapRestClient client, IProgressMonitor monitor) throws CoreException {
		List<TuleapArtifact> artifacts = null;
		String queryKind = query.getAttribute(ITuleapQueryConstants.QUERY_KIND);
		if (ITuleapQueryConstants.QUERY_KIND_REPORT.equals(queryKind)) {
			String queryReportId = query.getAttribute(ITuleapQueryConstants.QUERY_REPORT_ID);
			int reportId = Integer.valueOf(queryReportId).intValue();
			artifacts = client.getTrackerReportArtifacts(reportId, monitor);
		} else if (ITuleapQueryConstants.QUERY_KIND_CUSTOM.equals(queryKind)) {
			artifacts = client.getArtifactsFromQuery(query, tracker, monitor);
		} else {
			throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
					"Invalid state: unknown query kind.")); //$NON-NLS-1$
		}
		return artifacts;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#updateRepositoryConfiguration(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void updateRepositoryConfiguration(TaskRepository taskRepository, IProgressMonitor monitor)
			throws CoreException {
		if (taskRepository != null) {
			TuleapRestClient tuleapRestClient = this.getClientManager().getRestClient(taskRepository);
			try {
				TuleapServer tuleapServer = new TuleapServer(taskRepository.getRepositoryUrl());
				tuleapServer.setLastUpdate(new Date().getTime());

				if (monitor != null) {
					monitor.beginTask(TuleapCoreMessages.getString(TuleapCoreKeys.retrieveTuleapServer), 100);
				}

				for (TuleapProject project : tuleapRestClient.getProjects(monitor)) {
					tuleapServer.addProject(project);
					for (TuleapTracker tracker : tuleapRestClient.getProjectTrackers(project.getIdentifier(),
							monitor)) {
						project.addTracker(tracker);
					}

					try {
						for (TuleapUserGroup userGroup : tuleapRestClient.getProjectUserGroups(project
								.getIdentifier(), monitor)) {
							for (TuleapUser tuleapUser : tuleapRestClient.getUserGroupUsers(
									userGroup.getId(), monitor)) {
								tuleapServer.register(tuleapUser);
							}
						}
					} catch (CoreException e) {
						TuleapCoreActivator.log(e, false);
					}
				}
				this.serversByUrl.put(taskRepository.getRepositoryUrl(), tuleapServer);
			} catch (CoreException e) {
				TuleapCoreActivator.log(e, true);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#updateTaskFromTaskData(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.ITask, org.eclipse.mylyn.tasks.core.data.TaskData)
	 */
	@Override
	public void updateTaskFromTaskData(TaskRepository taskRepository, ITask task, TaskData taskData) {
		// Populate the task with the necessary data from the repository using the task data.
		ITaskMapping taskMapping = this.getTaskMapping(taskData);
		if (taskMapping instanceof TaskMapper) {
			TaskMapper mapper = (TaskMapper)taskMapping;
			mapper.applyTo(task);
		}

		// Update the completion date of the task from the status of the task data
		TuleapServer server = this.getServer(taskRepository);

		TuleapTaskId taskDataId = TuleapTaskId.forName(taskData.getTaskId());
		TuleapProject project = server.getProject(taskDataId.getProjectId());
		TuleapTracker tracker = project.getTracker(taskDataId.getTrackerId());

		TuleapArtifactMapper mapper = new TuleapArtifactMapper(taskData, tracker);
		int status = mapper.getStatusAsInt();
		if (status != TuleapArtifactMapper.INVALID_STATUS_ID) {
			if (isTaskCompleted(status, tracker)) {
				if (task.getCompletionDate() == null) {
					task.setCompletionDate(new Date(0));
				}
			} else {
				if (task.getCompletionDate() != null) {
					task.setCompletionDate(null);
				}
			}
		}

		// TODO Update the task with new values from the task data

	}

	/**
	 * Indicates if the given current status of a task matches a closed status of the tracker.
	 *
	 * @param currentStatus
	 *            The current status of a task
	 * @param tracker
	 *            The tracker containing the task
	 * @return <code>true</code> if the current status matches a closed status, <code>false</code> otherwise.
	 */
	private boolean isTaskCompleted(int currentStatus, TuleapTracker tracker) {
		if (tracker != null) {
			AbstractTuleapSelectBox statusField = tracker.getStatusField();
			if (statusField != null) {
				List<TuleapSelectBoxItem> closedStatus = statusField.getClosedStatus();
				for (TuleapSelectBoxItem tuleapSelectBoxItem : closedStatus) {
					if (currentStatus == tuleapSelectBoxItem.getIdentifier()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#getTaskMapping(org.eclipse.mylyn.tasks.core.data.TaskData)
	 */
	@Override
	public ITaskMapping getTaskMapping(final TaskData taskData) {
		return new TaskMapper(taskData) {
			@Override
			public String getTaskKind() {
				String taskKind = taskData.getConnectorKind();
				TaskAttribute attribute = taskData.getRoot().getAttribute(TaskAttribute.TASK_KIND);
				if (attribute != null) {
					taskKind = attribute.getValue();
				}
				return taskKind;
			}
		};
	}

	/**
	 * Returns the server configuration for the given URL.
	 *
	 * @param taskRepository
	 *            The task repository
	 * @return The server matching the given URL, or <code>null</code> if it does not exist.
	 */
	public TuleapServer getServer(TaskRepository taskRepository) {
		TuleapServer server = serversByUrl.get(taskRepository.getUrl());
		if (server == null) {
			// It may happen that if eclipse is killed or terminates abruptly, a recently created task
			// repository is not saved in the configuration file.
			// We try and refresh the server configuration in case this is why the server being looked for
			// doesn't exist.
			try {
				updateRepositoryConfiguration(taskRepository, new NullProgressMonitor());
				server = serversByUrl.get(taskRepository.getUrl());
			} catch (CoreException e) {
				TuleapCoreActivator.log(e, true);
			}
		}
		return server;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tuleap.core.internal.repository.ITuleapRepositoryConnector#refreshTracker(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public TuleapTracker refreshTracker(TaskRepository taskRepository, TuleapTracker tracker,
			IProgressMonitor monitor) throws CoreException {
		TuleapProject project = tracker.getProject();

		TuleapTracker refreshedTracker = null;
		TuleapRestClient client = this.getClientManager().getRestClient(taskRepository);
		refreshedTracker = client.getTracker(tracker.getIdentifier(), monitor);

		if (refreshedTracker != null) {
			TuleapServer tuleapServer = getServer(taskRepository);
			tuleapServer.replaceTracker(project.getIdentifier(), refreshedTracker);
		}
		return refreshedTracker;
	}

	/**
	 * Reads the repository configuration file.
	 */
	public synchronized void readRepositoryConfigurationFile() {
		if (cacheFileRead || repositoryConfigurationFile == null || !repositoryConfigurationFile.exists()) {
			return;
		}

		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(repositoryConfigurationFile));
			int size = in.readInt();
			for (int nX = 0; nX < size; nX++) {
				TuleapServer item = (TuleapServer)in.readObject();
				if (item != null) {
					serversByUrl.put(item.getUrl(), item);
				}
			}
			// CHECKSTYLE:OFF (All exceptions are treated equally since we can't do anything about them)
		} catch (Exception e) {
			// CHECKSTYLE:ON
			TuleapCoreActivator.log(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
					TuleapCoreMessages.getString(TuleapCoreKeys.cannotLoadOldConfiguration), e));
			try {
				if (in != null) {
					in.close();
				}
				if (repositoryConfigurationFile != null && repositoryConfigurationFile.exists()) {
					repositoryConfigurationFile.delete();
				}
				// CHECKSTYLE:OFF (All exceptions are treated equally since we can't do anything about
				// them)
			} catch (Exception ex) {
				// CHECKSTYLE:ON
				TuleapCoreActivator.log(e, true);
			}
		} finally {
			cacheFileRead = true;
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	/**
	 * Writes the repository configuration file.
	 */
	public synchronized void writeRepositoryConfigFile() {
		if (repositoryConfigurationFile != null) {
			ObjectOutputStream out = null;
			try {
				Set<TuleapServer> tempConfigs;
				synchronized(serversByUrl) {
					tempConfigs = new HashSet<TuleapServer>(serversByUrl.values());
				}
				if (tempConfigs.size() > 0) {
					out = new ObjectOutputStream(new FileOutputStream(repositoryConfigurationFile));
					out.writeInt(tempConfigs.size());
					for (TuleapServer server : tempConfigs) {
						if (server != null) {
							out.writeObject(server);
						}
					}
				}
			} catch (IOException e) {
				TuleapCoreActivator.log(e, false);
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						// ignore
					}
				}
			}
		}
	}

	/**
	 * Stops the Tuleap repository connector.
	 */
	public void stop() {
		this.writeRepositoryConfigFile();
	}
}
