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

import java.io.InputStream;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClient;
import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapInstanceConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapTrackerConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapFileUpload;
import org.eclipse.mylyn.internal.tuleap.core.util.TuleapUtil;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentHandler;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentSource;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;

/**
 * The Tuleap task attachement handler will be in charge of manipulating the task attachments.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapTaskAttachmentHandler extends AbstractTaskAttachmentHandler {

	/**
	 * The Tuleap connector.
	 */
	private ITuleapRepositoryConnector connector;

	/**
	 * The constructor.
	 * 
	 * @param tuleapRepositoryConnector
	 *            The connector
	 */
	public TuleapTaskAttachmentHandler(ITuleapRepositoryConnector tuleapRepositoryConnector) {
		this.connector = tuleapRepositoryConnector;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentHandler#canGetContent(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.ITask)
	 */
	@Override
	public boolean canGetContent(TaskRepository repository, ITask task) {
		return this.hasFileUploadField(repository, task);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentHandler#canPostContent(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.ITask)
	 */
	@Override
	public boolean canPostContent(TaskRepository repository, ITask task) {
		return this.hasFileUploadField(repository, task);
	}

	/**
	 * Indicates if the configuration of the tracker on which the task is contained has a fiel upload field.
	 * 
	 * @param repository
	 *            The task repository
	 * @param task
	 *            The current task
	 * @return <code>true</code> if we can upload or download a file for the given task, <code>false</code>
	 *         otherwise.
	 */
	public boolean hasFileUploadField(TaskRepository repository, ITask task) {
		String taskId = task.getTaskId();
		int trackerId = TuleapUtil.getTrackerIdFromTaskDataId(taskId);

		// TODO Check user permissions?

		ITuleapClient client = this.connector.getClientManager().getClient(repository);
		if (client == null || trackerId == -1) {
			return false;
		}

		boolean hasFileUploadField = false;

		TuleapInstanceConfiguration repositoryConfiguration = client.getRepositoryConfiguration();
		if (repositoryConfiguration != null) {
			TuleapTrackerConfiguration trackerConfiguration = repositoryConfiguration
					.getTrackerConfiguration(trackerId);
			if (trackerConfiguration != null) {
				List<AbstractTuleapField> fields = trackerConfiguration.getFields();
				for (AbstractTuleapField abstractTuleapField : fields) {
					if (abstractTuleapField instanceof TuleapFileUpload) {
						hasFileUploadField = true;
						break;
					}
				}
			}
		}

		return hasFileUploadField;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentHandler#getContent(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.ITask, org.eclipse.mylyn.tasks.core.data.TaskAttribute,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public InputStream getContent(TaskRepository repository, ITask task, TaskAttribute attachmentAttribute,
			IProgressMonitor monitor) throws CoreException {
		// TODO Download attachments
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentHandler#postContent(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.ITask,
	 *      org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentSource, java.lang.String,
	 *      org.eclipse.mylyn.tasks.core.data.TaskAttribute, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void postContent(TaskRepository repository, ITask task, AbstractTaskAttachmentSource source,
			String comment, TaskAttribute attachmentAttribute, IProgressMonitor monitor) throws CoreException {
		// TODO Send attachments
	}

}
