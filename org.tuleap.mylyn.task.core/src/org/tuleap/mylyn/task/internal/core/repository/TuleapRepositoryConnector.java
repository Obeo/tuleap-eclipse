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
package org.tuleap.mylyn.task.internal.core.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
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
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.client.ITuleapQueryConstants;
import org.tuleap.mylyn.task.internal.core.client.TuleapClientManager;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapClient;
import org.tuleap.mylyn.task.internal.core.data.TuleapArtifactMapper;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskIdentityUtil;
import org.tuleap.mylyn.task.internal.core.data.converter.ArtifactTaskDataConverter;
import org.tuleap.mylyn.task.internal.core.data.converter.MilestoneTaskDataConverter;
import org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapProject;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapServer;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapTracker;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapTopPlanning;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessagesKeys;

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
	 * The cache of the repository configurations.
	 */
	private final Map<String, TuleapServer> repositoryConfigurations = new HashMap<String, TuleapServer>();

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
		return TuleapMylynTasksMessages
				.getString(TuleapMylynTasksMessagesKeys.tuleapRepositoryConnectorLabel);
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

		return TuleapUrlUtil.getTaskIdFromTaskUrl(url);
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
		return new TuleapTaskAttachmentHandler(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#getTaskUrl(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String getTaskUrl(String repositoryUrl, String taskId) {
		return TuleapUrlUtil.getTaskUrlFromTaskId(repositoryUrl, taskId);
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
		return this.taskDataHandler.getTaskData(taskRepository, taskId, monitor);
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
	 * @see org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector#getClientManager()
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
		// Populate the collector with the task data resulting from the query
		String queryKind = query.getAttribute(ITuleapQueryConstants.QUERY_KIND);

		TuleapServer repositoryConfiguration = this.getTuleapServerConfiguration(taskRepository
				.getRepositoryUrl());
		TaskAttributeMapper attributeMapper = this.getTaskDataHandler().getAttributeMapper(taskRepository);

		if (ITuleapQueryConstants.QUERY_KIND_ALL_FROM_TRACKER.equals(queryKind)
				|| ITuleapQueryConstants.QUERY_KIND_REPORT.equals(queryKind)
				|| ITuleapQueryConstants.QUERY_KIND_CUSTOM.equals(queryKind)) {
			TuleapSoapClient soapClient = this.getClientManager().getSoapClient(taskRepository);

			int trackerId = Integer.valueOf(query.getAttribute(ITuleapQueryConstants.QUERY_CONFIGURATION_ID))
					.intValue();

			TuleapTracker tracker = repositoryConfiguration.getTracker(trackerId);
			try {
				tracker = this.refreshTracker(taskRepository, tracker, monitor);
			} catch (CoreException e) {
				TuleapCoreActivator.log(e, true);
			}

			ArtifactTaskDataConverter artifactTaskDataConverter = new ArtifactTaskDataConverter(tracker,
					taskRepository, this);

			List<TuleapArtifact> artifacts = soapClient.getArtifactsFromQuery(query, repositoryConfiguration,
					tracker, monitor);
			for (TuleapArtifact tuleapArtifact : artifacts) {
				String taskDataId = TuleapTaskIdentityUtil.getTaskDataId(tracker
						.getTuleapProjectConfiguration().getIdentifier(),
						tuleapArtifact.getTracker().getId(), tuleapArtifact.getId());

				TaskData taskData = new TaskData(attributeMapper, this.getConnectorKind(), taskRepository
						.getRepositoryUrl(), taskDataId);
				artifactTaskDataConverter.populateTaskData(taskData, tuleapArtifact, monitor);

				// If the tracker is a milestone tracker, retrieve milestone-specific informations
				if (tracker.getTuleapProjectConfiguration().isMilestoneTracker(tracker.getIdentifier())) {
					// TODO
				}

				try {
					collector.accept(taskData);
				} catch (IllegalArgumentException exception) {
					// Do not log, the query has been deleted while it was executed, see:
					// org.eclipse.mylyn.internal.tasks.core.TaskList.getValidElement(IRepositoryElement)
				}
			}
		} else if (ITuleapQueryConstants.QUERY_KIND_TOP_LEVEL_PLANNING.equals(queryKind)) {
			int projectId = Integer.valueOf(query.getAttribute(ITuleapQueryConstants.QUERY_PROJECT_ID))
					.intValue();

			// null -> the top plannings do not have a configuration
			MilestoneTaskDataConverter milestoneTaskDataConverter = new MilestoneTaskDataConverter(
					taskRepository, this);

			TuleapRestClient restClient = this.getClientManager().getRestClient(taskRepository);
			try {
				List<TuleapTopPlanning> topPlannings = restClient.getTopPlannings(projectId, monitor);
				for (TuleapTopPlanning tuleapTopPlanning : topPlannings) {
					String taskDataId = TuleapTaskIdentityUtil.getTaskDataId(projectId,
							TuleapTaskIdentityUtil.IRRELEVANT_ID, tuleapTopPlanning.getId());

					TaskData taskData = new TaskData(attributeMapper, this.getConnectorKind(), taskRepository
							.getRepositoryUrl(), taskDataId);
					milestoneTaskDataConverter.populateTaskData(taskData, tuleapTopPlanning, projectId,
							monitor);
					try {
						collector.accept(taskData);
					} catch (IllegalArgumentException exception) {
						// Do not log, the query has been deleted while it was executed, see:
						// org.eclipse.mylyn.internal.tasks.core.TaskList.getValidElement(IRepositoryElement)
					}
				}
			} catch (CoreException e) {
				TuleapCoreActivator.log(e, true);
			}
		}
		return Status.OK_STATUS;
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
			TuleapSoapClient tuleapSoapClient = this.getClientManager().getSoapClient(taskRepository);
			try {
				TuleapServer tuleapServerConfigurationRest = tuleapRestClient
						.getTuleapServerConfiguration(monitor);
				TuleapServer tuleapServerConfigurationSoap = tuleapSoapClient
						.getTuleapServerConfiguration(monitor);

				// put the configuration in this.repositoryConfigurations
				List<TuleapProject> allProjectConfigurations = tuleapServerConfigurationSoap
						.getAllProjects();
				for (TuleapProject tuleapProject : allProjectConfigurations) {
					tuleapServerConfigurationRest.addProject(tuleapProject);
				}

				this.repositoryConfigurations.put(taskRepository.getRepositoryUrl(),
						tuleapServerConfigurationRest);
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
		TuleapServer configuration = this.getTuleapServerConfiguration(taskRepository
				.getRepositoryUrl());

		TuleapProject projectConfiguration = configuration
				.getProjectConfiguration(TuleapTaskIdentityUtil.getProjectIdFromTaskDataId(taskData
						.getTaskId()));
		TuleapTracker tracker = projectConfiguration.getTrackerConfiguration(TuleapTaskIdentityUtil
				.getConfigurationIdFromTaskDataId(taskData.getTaskId()));

		TuleapArtifactMapper mapper = new TuleapArtifactMapper(taskData, tracker);
		int status = mapper.getStatus();
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
	 * Indicates if the given current status of a task matches a closed status of the configuration.
	 * 
	 * @param currentStatus
	 *            The current status of a task
	 * @param tracker
	 *            The configuration of the repository containing the task
	 * @return <code>true</code> if the current status matches a closed status, <code>false</code> otherwise.
	 */
	private boolean isTaskCompleted(int currentStatus, TuleapTracker tracker) {
		if (tracker != null) {
			Collection<AbstractTuleapField> fields = tracker.getFields();
			for (AbstractTuleapField abstractTuleapField : fields) {
				if (abstractTuleapField instanceof TuleapSelectBox
						&& ((TuleapSelectBox)abstractTuleapField).isSemanticStatus()) {
					TuleapSelectBox tuleapSelectBox = (TuleapSelectBox)abstractTuleapField;
					List<TuleapSelectBoxItem> closedStatus = tuleapSelectBox.getClosedStatus();
					for (TuleapSelectBoxItem tuleapSelectBoxItem : closedStatus) {
						if (currentStatus == tuleapSelectBoxItem.getIdentifier()) {
							return true;
						}
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
	 * Returns the repository configuration matching the given url.
	 * 
	 * @param repositoryUrl
	 *            The repository url
	 * @return The repository configuration matching the given url.
	 */
	public TuleapServer getTuleapServerConfiguration(String repositoryUrl) {
		return repositoryConfigurations.get(repositoryUrl);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector#refreshTracker(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.tuleap.mylyn.task.internal.core.model.config.TuleapTracker,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public TuleapTracker refreshTracker(TaskRepository taskRepository, TuleapTracker configuration,
			IProgressMonitor monitor) throws CoreException {
		TuleapProject projectConfiguration = configuration.getTuleapProjectConfiguration();

		TuleapTracker refreshedConfiguration = null;
		TuleapSoapClient tuleapSoapClient = this.getClientManager().getSoapClient(taskRepository);
		refreshedConfiguration = tuleapSoapClient.getTuleapTrackerConfiguration(projectConfiguration,
				configuration.getIdentifier(), monitor);

		if (refreshedConfiguration != null) {
			TuleapServer tuleapServer = this.repositoryConfigurations
					.get(taskRepository.getRepositoryUrl());
			tuleapServer.replaceTracker(projectConfiguration.getIdentifier(),
					refreshedConfiguration);
		}
		return refreshedConfiguration;
	}

	/**
	 * Reads the repository configuration file.
	 */
	public synchronized void readRepositoryConfigurationFile() {
		if (cacheFileRead || repositoryConfigurationFile == null || !repositoryConfigurationFile.exists()) {
			return;
		}

		synchronized(repositoryConfigurations) {
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(repositoryConfigurationFile));
				int size = in.readInt();
				for (int nX = 0; nX < size; nX++) {
					TuleapServer item = (TuleapServer)in.readObject();
					if (item != null) {
						repositoryConfigurations.put(item.getUrl(), item);
					}
				}
				// CHECKSTYLE:OFF (All exceptions are treated equally since we can't do anything about them)
			} catch (Exception e) {
				// CHECKSTYLE:ON
				TuleapCoreActivator.log(e, true);
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
	}

	/**
	 * Writes the repository configuration file.
	 */
	public void writeRepositoryConfigFile() {
		if (repositoryConfigurationFile != null) {
			ObjectOutputStream out = null;
			try {
				Set<TuleapServer> tempConfigs;
				synchronized(repositoryConfigurations) {
					tempConfigs = new HashSet<TuleapServer>(repositoryConfigurations.values());
				}
				if (tempConfigs.size() > 0) {
					out = new ObjectOutputStream(new FileOutputStream(repositoryConfigurationFile));
					out.writeInt(tempConfigs.size());
					for (TuleapServer repositoryConfiguration : tempConfigs) {
						if (repositoryConfiguration != null) {
							out.writeObject(repositoryConfiguration);
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
