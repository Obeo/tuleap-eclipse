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
package org.eclipse.mylyn.tuleap.tests.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClientManager;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapInstanceConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.repository.ITuleapRepositoryConnector;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.eclipse.mylyn.tasks.core.sync.ISynchronizationSession;

/**
 * A mock of the Tuleap repository connector.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class MockedTuleapRepositoryConnector extends AbstractRepositoryConnector implements ITuleapRepositoryConnector {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.repository.ITuleapRepositoryConnector#getClientManager()
	 */
	public ITuleapClientManager getClientManager() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.repository.ITuleapRepositoryConnector#getRepositoryConfiguration(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public TuleapInstanceConfiguration getRepositoryConfiguration(TaskRepository taskRepository,
			boolean forceRefresh, IProgressMonitor monitor) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.repository.ITuleapRepositoryConnector#putRepositoryConfiguration(java.lang.String,
	 *      org.eclipse.mylyn.internal.tuleap.core.model.TuleapInstanceConfiguration)
	 */
	public void putRepositoryConfiguration(String repositoryUrl, TuleapInstanceConfiguration configuration) {
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#canCreateNewTask(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public boolean canCreateNewTask(TaskRepository repository) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#canCreateTaskFromKey(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public boolean canCreateTaskFromKey(TaskRepository repository) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#getConnectorKind()
	 */
	@Override
	public String getConnectorKind() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#getLabel()
	 */
	@Override
	public String getLabel() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#getRepositoryUrlFromTaskUrl(java.lang.String)
	 */
	@Override
	public String getRepositoryUrlFromTaskUrl(String taskFullUrl) {
		return null;
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
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#getTaskIdFromTaskUrl(java.lang.String)
	 */
	@Override
	public String getTaskIdFromTaskUrl(String taskFullUrl) {
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
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#hasTaskChanged(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.ITask, org.eclipse.mylyn.tasks.core.data.TaskData)
	 */
	@Override
	public boolean hasTaskChanged(TaskRepository taskRepository, ITask task, TaskData taskData) {
		return false;
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
		return null;
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
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#updateTaskFromTaskData(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.ITask, org.eclipse.mylyn.tasks.core.data.TaskData)
	 */
	@Override
	public void updateTaskFromTaskData(TaskRepository taskRepository, ITask task, TaskData taskData) {
		// do nothing
	}

}
