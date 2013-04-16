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
package org.tuleap.mylyn.task.tests.mocks;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentHandler;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.eclipse.mylyn.tasks.core.sync.ISynchronizationSession;
import org.tuleap.mylyn.task.internal.core.client.ITuleapClientManager;
import org.tuleap.mylyn.task.internal.core.model.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.TuleapInstanceConfiguration;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * A mock of the Tuleap repository connector.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class MockedTuleapRepositoryConnector extends AbstractRepositoryConnector implements ITuleapRepositoryConnector {
	/**
	 * Tuleap artifact.
	 */
	private TuleapArtifact tuleapArtifact;

	/**
	 * The configuration to be returned.
	 */
	private TuleapInstanceConfiguration configuration;

	/**
	 * The attachement handler.
	 */
	private AbstractTaskAttachmentHandler attachmentHandler;

	/**
	 * The constructor.
	 * 
	 * @param tuleapInstanceConfiguration
	 *            The configuration that will be returned for the unit test.
	 * @param artifact
	 *            Tuleap Artifact
	 */
	public MockedTuleapRepositoryConnector(TuleapInstanceConfiguration tuleapInstanceConfiguration,
			TuleapArtifact artifact) {
		this.configuration = tuleapInstanceConfiguration;
		this.tuleapArtifact = artifact;
	}

	/**
	 * The constructor.
	 * 
	 * @param tuleapInstanceConfiguration
	 *            The configuration that will be returned for the unit test.
	 */
	public MockedTuleapRepositoryConnector(TuleapInstanceConfiguration tuleapInstanceConfiguration) {
		this.configuration = tuleapInstanceConfiguration;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector#getClientManager()
	 */
	public ITuleapClientManager getClientManager() {
		return new MockedTuleapClientManager(tuleapArtifact, configuration);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector#getRepositoryConfiguration(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public TuleapInstanceConfiguration getRepositoryConfiguration(TaskRepository taskRepository,
			boolean forceRefresh, IProgressMonitor monitor) {
		return this.configuration;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector#putRepositoryConfiguration(java.lang.String,
	 *      org.tuleap.mylyn.task.internal.core.model.TuleapInstanceConfiguration)
	 */
	public void putRepositoryConfiguration(String repositoryUrl,
			TuleapInstanceConfiguration tuleapInstanceConfiguration) {
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
		return repositoryUrl + ITuleapConstants.REPOSITORY_TASK_URL_SEPARATOR + taskId;
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

	/**
	 * Sets the attachment handler.
	 * 
	 * @param taskAttachmentHandler
	 *            The attachment handler
	 */
	public void setAttachementHandler(AbstractTaskAttachmentHandler taskAttachmentHandler) {
		this.attachmentHandler = taskAttachmentHandler;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector#getTaskAttachmentHandler()
	 */
	@Override
	public AbstractTaskAttachmentHandler getTaskAttachmentHandler() {
		return this.attachmentHandler;
	}

}
