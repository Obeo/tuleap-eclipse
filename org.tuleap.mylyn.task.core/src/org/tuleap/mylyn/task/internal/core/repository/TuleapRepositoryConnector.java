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
import org.tuleap.mylyn.task.internal.core.data.TuleapConfigurableElementMapper;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskIdentityUtil;
import org.tuleap.mylyn.task.internal.core.data.converter.ArtifactTaskDataConverter;
import org.tuleap.mylyn.task.internal.core.data.converter.MilestoneTaskDataConverter;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableFieldsConfiguration;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapTopPlanning;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;
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
	private final Map<String, TuleapServerConfiguration> repositoryConfigurations = new HashMap<String, TuleapServerConfiguration>();

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
		if (repositoryDate != null && repositoryDate.equals(localDate)) {
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

		TuleapServerConfiguration repositoryConfiguration = this.getRepositoryConfiguration(taskRepository,
				true, monitor);
		TaskAttributeMapper attributeMapper = this.getTaskDataHandler().getAttributeMapper(taskRepository);

		if (ITuleapQueryConstants.QUERY_KIND_ALL_FROM_TRACKER.equals(queryKind)
				|| ITuleapQueryConstants.QUERY_KIND_REPORT.equals(queryKind)
				|| ITuleapQueryConstants.QUERY_KIND_CUSTOM.equals(queryKind)) {
			TuleapSoapClient soapClient = this.getClientManager().getSoapClient(taskRepository);

			int trackerId = Integer.valueOf(query.getAttribute(ITuleapQueryConstants.QUERY_CONFIGURATION_ID))
					.intValue();

			TuleapTrackerConfiguration trackerConfiguration = repositoryConfiguration
					.getTrackerConfiguration(trackerId);

			ArtifactTaskDataConverter artifactTaskDataConverter = new ArtifactTaskDataConverter(
					trackerConfiguration);

			List<TuleapArtifact> artifacts = soapClient.getArtifactsFromQuery(query, repositoryConfiguration,
					trackerConfiguration, monitor);
			for (TuleapArtifact tuleapArtifact : artifacts) {
				String taskDataId = TuleapTaskIdentityUtil.getTaskDataId(trackerConfiguration
						.getTuleapProjectConfiguration().getIdentifier(),
						tuleapArtifact.getConfigurationId(), tuleapArtifact.getId());

				TaskData taskData = new TaskData(attributeMapper, this.getConnectorKind(), taskRepository
						.getRepositoryUrl(), taskDataId);
				artifactTaskDataConverter.populateTaskData(taskData, tuleapArtifact);

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
			MilestoneTaskDataConverter milestoneTaskDataConverter = new MilestoneTaskDataConverter(null);

			TuleapRestClient restClient = this.getClientManager().getRestClient(taskRepository);
			try {
				List<TuleapTopPlanning> topPlannings = restClient.getTopPlannings(projectId, monitor);
				for (TuleapTopPlanning tuleapTopPlanning : topPlannings) {
					String taskDataId = TuleapTaskIdentityUtil.getTaskDataId(projectId,
							TuleapTaskIdentityUtil.IRRELEVANT_ID, tuleapTopPlanning.getId());

					TaskData taskData = new TaskData(attributeMapper, this.getConnectorKind(), taskRepository
							.getRepositoryUrl(), taskDataId);
					milestoneTaskDataConverter.populateTaskData(taskData, tuleapTopPlanning, projectId);
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
		// TODO SBE Redefine the update strategy of the configuration!!!
		if (taskRepository != null) {
			this.getRepositoryConfiguration(taskRepository, true, monitor);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector#getRepositoryConfiguration(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public TuleapServerConfiguration getRepositoryConfiguration(TaskRepository taskRepository,
			boolean forceRefresh, IProgressMonitor monitor) {

		// TODO SBE Redefine the update strategy of the configuration!!!

		boolean shouldRefresh = forceRefresh;

		// If we don't have any value, force the refresh
		if (!shouldRefresh && this.repositoryConfigurations.get(taskRepository.getRepositoryUrl()) == null) {
			shouldRefresh = true;
		}

		if (shouldRefresh) {
			TuleapRestClient tuleapRestClient = this.getClientManager().getRestClient(taskRepository);
			TuleapSoapClient tuleapSoapClient = this.getClientManager().getSoapClient(taskRepository);
			try {
				TuleapServerConfiguration tuleapServerConfigurationRest = tuleapRestClient
						.getTuleapServerConfiguration(monitor);
				TuleapServerConfiguration tuleapServerConfigurationSoap = tuleapSoapClient
						.getTuleapServerConfiguration(monitor);

				// TODO SBE merge configurations!

				// put the configuration in this.repositoryConfigurations
				List<TuleapProjectConfiguration> allProjectConfigurations = tuleapServerConfigurationSoap
						.getAllProjectConfigurations();
				for (TuleapProjectConfiguration tuleapProjectConfiguration : allProjectConfigurations) {
					tuleapServerConfigurationRest.addProject(tuleapProjectConfiguration);
				}

				this.repositoryConfigurations.put(taskRepository.getRepositoryUrl(),
						tuleapServerConfigurationRest);
			} catch (CoreException e) {
				TuleapCoreActivator.log(e, true);
			}

		}
		return this.repositoryConfigurations.get(taskRepository.getRepositoryUrl());
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
		TuleapServerConfiguration configuration = this.getRepositoryConfiguration(taskRepository
				.getRepositoryUrl());

		TuleapProjectConfiguration projectConfiguration = configuration
				.getProjectConfiguration(TuleapTaskIdentityUtil.getProjectIdFromTaskDataId(taskData
						.getTaskId()));
		AbstractTuleapConfigurableFieldsConfiguration tuleapConfigurableFieldsConfiguration = projectConfiguration
				.getConfigurableFieldsConfiguration(TuleapTaskIdentityUtil
						.getConfigurationIdFromTaskDataId(taskData.getTaskId()));

		TuleapConfigurableElementMapper mapper = new TuleapConfigurableElementMapper(taskData,
				tuleapConfigurableFieldsConfiguration);
		int status = mapper.getStatus();
		if (status != TuleapConfigurableElementMapper.INVALID_STATUS_ID) {
			if (isTaskCompleted(status, tuleapConfigurableFieldsConfiguration)) {
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
	 * @param configuration
	 *            The configuration of the repository containing the task
	 * @return <code>true</code> if the current status matches a closed status, <code>false</code> otherwise.
	 */
	private boolean isTaskCompleted(int currentStatus,
			AbstractTuleapConfigurableFieldsConfiguration configuration) {
		if (configuration != null) {
			Collection<AbstractTuleapField> fields = configuration.getFields();
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

			@Override
			public String getTaskUrl() {
				return taskData.getRepositoryUrl();
			}
		};
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector#putRepositoryConfiguration(java.lang.String,
	 *      org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration)
	 */
	public void putRepositoryConfiguration(String repositoryUrl, TuleapServerConfiguration configuration) {
		this.repositoryConfigurations.put(repositoryUrl, configuration);
	}

	/**
	 * Returns the repository configuration matching the given url.
	 * 
	 * @param repositoryUrl
	 *            The repository url
	 * @return The repository configuration matching the given url.
	 */
	public TuleapServerConfiguration getRepositoryConfiguration(String repositoryUrl) {
		// TODO SBE Redefine the update strategy of the configuration!!!
		// TODO SBE Is this even useful???
		this.readRepositoryConfigurationFile();
		return repositoryConfigurations.get(repositoryUrl);
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
					TuleapServerConfiguration item = (TuleapServerConfiguration)in.readObject();
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
				Set<TuleapServerConfiguration> tempConfigs;
				synchronized(repositoryConfigurations) {
					tempConfigs = new HashSet<TuleapServerConfiguration>(repositoryConfigurations.values());
				}
				if (tempConfigs.size() > 0) {
					out = new ObjectOutputStream(new FileOutputStream(repositoryConfigurationFile));
					out.writeInt(tempConfigs.size());
					for (TuleapServerConfiguration repositoryConfiguration : tempConfigs) {
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
