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
package org.eclipse.mylyn.internal.tuleap.core.repository;

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
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.internal.tuleap.core.TuleapCoreActivator;
import org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClient;
import org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClientManager;
import org.eclipse.mylyn.internal.tuleap.core.client.TuleapClientManager;
import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapInstanceConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapTrackerConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapSelectBox;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapSelectBoxItem;
import org.eclipse.mylyn.internal.tuleap.core.util.ITuleapConstants;
import org.eclipse.mylyn.internal.tuleap.core.util.TuleapMylynTasksMessages;
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

/**
 * This class encapsulates common operation realized on the Tuleap repository.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapRepositoryConnector extends AbstractRepositoryConnector implements ITuleapRepositoryConnector {

	/**
	 * The task data handler.
	 */
	private final TuleapTaskDataHandler taskDataHandler = new TuleapTaskDataHandler(this);

	/**
	 * The Tuleap client manager.
	 */
	private ITuleapClientManager clientManager;

	/**
	 * The repository configuration file.
	 */
	private File repositoryConfigurationFile;

	/**
	 * The cache of the repository configurations.
	 */
	private final Map<String, TuleapInstanceConfiguration> repositoryConfigurations = new HashMap<String, TuleapInstanceConfiguration>();

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
		return TuleapMylynTasksMessages.getString("TuleapRepositoryConnector.Label"); //$NON-NLS-1$
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
		int index = url.indexOf(ITuleapConstants.REPOSITORY_TASK_URL_SEPARATOR);
		String repositoryUrl = null;

		if (index != -1) {
			repositoryUrl = url.substring(0, index);
		}
		return repositoryUrl;
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

		int index = url.indexOf(ITuleapConstants.REPOSITORY_TASK_URL_SEPARATOR);
		String taskId = null;
		if (index != -1) {
			taskId = url.substring(index + ITuleapConstants.REPOSITORY_TASK_URL_SEPARATOR.length());
		}
		return taskId;
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
		// return new TuleapTaskAttachmentHandler();
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#getTaskUrl(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String getTaskUrl(String repositoryUrl, String taskId) {
		// Example: https://demo.tuleap.net/plugins/tracker/?group_id=409&aid=453
		return repositoryUrl + ITuleapConstants.REPOSITORY_TASK_URL_SEPARATOR + taskId;
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
		// TODO Checks if the task has changed
		System.out.println("has task changed?");
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#canSynchronizeTask(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.ITask)
	 */
	@Override
	public boolean canSynchronizeTask(TaskRepository taskRepository, ITask task) {
		// TODO Checks if we can synchronize the task
		System.out.println("can cynshronize task?");
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
	 * @see org.eclipse.mylyn.internal.tuleap.core.repository.ITuleapRepositoryConnector#getClientManager()
	 */
	public ITuleapClientManager getClientManager() {
		if (clientManager == null) {
			clientManager = new TuleapClientManager(this);
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
		System.out.println();
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
	public IStatus performQuery(TaskRepository repository, IRepositoryQuery query,
			TaskDataCollector collector, ISynchronizationSession session, IProgressMonitor monitor) {
		// Populate the collector with the task data resulting from the query
		ITuleapClient client = this.getClientManager().getClient(repository);
		TaskAttributeMapper mapper = this.getTaskDataHandler().getAttributeMapper(repository);
		boolean hitReceived = client.getSearchHits(query, collector, mapper, monitor);
		if (!hitReceived) {
			// Something went wrong?
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
			this.getRepositoryConfiguration(taskRepository, true, monitor);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.repository.ITuleapRepositoryConnector#getRepositoryConfiguration(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public TuleapInstanceConfiguration getRepositoryConfiguration(TaskRepository repository,
			boolean forceRefresh, IProgressMonitor monitor) {
		// TODO Returns and/or update the configuration of the given repository.
		ITuleapClient client = this.getClientManager().getClient(repository);
		client.updateAttributes(monitor, forceRefresh);
		return this.repositoryConfigurations.get(repository.getRepositoryUrl());
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
		TaskAttribute attributeStatus = taskData.getRoot().getMappedAttribute(TaskAttribute.STATUS);
		TaskAttribute attributeProduct = taskData.getRoot().getMappedAttribute(TaskAttribute.PRODUCT);
		if (attributeStatus != null && attributeProduct != null) {
			int trackerId = -1;
			String value = attributeProduct.getValue();
			try {
				trackerId = Integer.valueOf(value).intValue();
				TuleapInstanceConfiguration configuration = this.getRepositoryConfiguration(taskRepository
						.getRepositoryUrl());
				TuleapTrackerConfiguration trackerConfiguration = configuration
						.getTrackerConfiguration(trackerId);

				boolean isCompleted = isTaskCompleted(attributeStatus.getValue(), trackerConfiguration);

				if (isCompleted) {
					if (task.getCompletionDate() == null) {
						task.setCompletionDate(new Date(0));
					}
				} else {
					if (task.getCompletionDate() != null) {
						task.setCompletionDate(null);
					}
				}
			} catch (NumberFormatException e) {
				TuleapCoreActivator.log(e, true);
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
	private boolean isTaskCompleted(String currentStatus, TuleapTrackerConfiguration configuration) {
		if (configuration != null) {
			List<AbstractTuleapField> fields = configuration.getFields();
			for (AbstractTuleapField abstractTuleapField : fields) {
				if (abstractTuleapField instanceof TuleapSelectBox
						&& ((TuleapSelectBox)abstractTuleapField).isSemanticStatus()) {
					TuleapSelectBox tuleapSelectBox = (TuleapSelectBox)abstractTuleapField;
					List<TuleapSelectBoxItem> closedStatus = tuleapSelectBox.getClosedStatus();
					for (TuleapSelectBoxItem tuleapSelectBoxItem : closedStatus) {
						if (currentStatus.equals(tuleapSelectBoxItem.getLabel())) {
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
	 * Returns the artifact ID matching the given task ID.
	 * 
	 * @param taskId
	 *            The task id.
	 * @return The artifact ID matching the given task ID.
	 */
	public static int getArtifactId(String taskId) {
		try {
			return Integer.parseInt(taskId);
		} catch (NumberFormatException e) {
			TuleapCoreActivator.log(e, true);
		}
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.repository.ITuleapRepositoryConnector#putRepositoryConfiguration(java.lang.String,
	 *      org.eclipse.mylyn.internal.tuleap.core.model.TuleapTrackerConfiguration)
	 */
	public void putRepositoryConfiguration(String repositoryUrl, TuleapInstanceConfiguration configuration) {
		this.repositoryConfigurations.put(repositoryUrl, configuration);
	}

	/**
	 * Returns the repository configuration matching the given url.
	 * 
	 * @param repositoryUrl
	 *            The repository url
	 * @return The repository configuration matching the given url.
	 */
	public TuleapInstanceConfiguration getRepositoryConfiguration(String repositoryUrl) {
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
					TuleapInstanceConfiguration item = (TuleapInstanceConfiguration)in.readObject();
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
				Set<TuleapInstanceConfiguration> tempConfigs;
				synchronized(repositoryConfigurations) {
					tempConfigs = new HashSet<TuleapInstanceConfiguration>(repositoryConfigurations.values());
				}
				if (tempConfigs.size() > 0) {
					out = new ObjectOutputStream(new FileOutputStream(repositoryConfigurationFile));
					out.writeInt(tempConfigs.size());
					for (TuleapInstanceConfiguration repositoryConfiguration : tempConfigs) {
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
