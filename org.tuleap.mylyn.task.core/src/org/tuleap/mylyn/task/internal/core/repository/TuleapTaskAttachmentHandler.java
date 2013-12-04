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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.TaskRepositoryLocationFactory;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentHandler;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentSource;
import org.eclipse.mylyn.tasks.core.data.TaskAttachmentMapper;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapConnector;
import org.tuleap.mylyn.task.internal.core.data.TuleapArtifactMapper;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskId;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapProject;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapServer;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapTracker;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapFileUpload;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapAttachmentDescriptor;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * The Tuleap task attachement handler will be in charge of manipulating the task attachments.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapTaskAttachmentHandler extends AbstractTaskAttachmentHandler {

	/**
	 * Default description of a valid "mylyn context" attachment. DO NOT CHANGE!!!
	 */
	private static final String CONTEXT_DESCRIPTION = "mylyn/context/zip"; //$NON-NLS-1$

	/**
	 * Default name of a valid "mylyn context" attachment. DO NOT CHANGE!!!
	 */
	private static final String CONTEXT_FILENAME = "mylyn-context.zip"; //$NON-NLS-1$

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
	 * Indicates if the tracker on which the task is contained has a fiel upload field.
	 * 
	 * @param repository
	 *            The task repository
	 * @param task
	 *            The current task
	 * @return <code>true</code> if we can upload or download a file for the given task, <code>false</code>
	 *         otherwise.
	 */
	private boolean hasFileUploadField(TaskRepository repository, ITask task) {
		boolean hasFileUploadField = false;
		TuleapTaskId taskDataId = TuleapTaskId.forName(task.getTaskId());
		TuleapServer tuleapServer = this.connector.getServer(repository.getRepositoryUrl());
		TuleapProject project = tuleapServer.getProject(taskDataId.getProjectId());
		TuleapTracker tracker = project.getTracker(taskDataId.getTrackerId());

		if (tracker != null) {
			hasFileUploadField = tracker.getAttachmentField() != null;
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
		AbstractWebLocation abstractWebLocation = new TaskRepositoryLocationFactory()
				.createWebLocation(repository);
		TuleapSoapConnector tuleapSoapConnector = new TuleapSoapConnector(abstractWebLocation);
		String taskId = task.getTaskId();
		String attachmentAttributeId = attachmentAttribute.getId();
		int index = attachmentAttributeId.indexOf("---"); //$NON-NLS-1$
		if (index != -1) {
			String id = attachmentAttributeId.substring(index + "---".length()); //$NON-NLS-1$
			int attachmentId = Integer.valueOf(id).intValue();

			int artifactId = Integer.valueOf(taskId).intValue();

			TaskAttachmentMapper taskAttachment = TaskAttachmentMapper.createFrom(attachmentAttribute);
			Long length = taskAttachment.getLength();
			int size = length.intValue();
			String filename = taskAttachment.getFileName();

			byte[] content;
			try {
				content = tuleapSoapConnector.getAttachmentContent(artifactId, attachmentId, filename, size,
						monitor);
			} catch (MalformedURLException e) {
				IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
				throw new CoreException(status);
			} catch (RemoteException e) {
				IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
				throw new CoreException(status);
			} catch (ServiceException e) {
				IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
				throw new CoreException(status);
			}

			if (content == null) {
				content = new byte[] {};
			}
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decodeBase64(content));
			return byteArrayInputStream;
		}
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
		AbstractWebLocation abstractWebLocation = new TaskRepositoryLocationFactory()
				.createWebLocation(repository);
		TuleapSoapConnector tuleapSoapConnector = new TuleapSoapConnector(abstractWebLocation);

		TuleapTracker tracker = null;

		TuleapServer server = this.connector.getServer(repository.getRepositoryUrl());

		// If the attachment attribute is available, let's use it
		if (attachmentAttribute != null) {
			TaskData taskData = attachmentAttribute.getTaskData();
			TuleapArtifactMapper tuleapArtifactMapper = new TuleapArtifactMapper(taskData, tracker);

			int trackerId = tuleapArtifactMapper.getTaskId().getTrackerId();

			List<TuleapProject> allProjects = server.getAllProjects();
			for (TuleapProject tuleapProject : allProjects) {
				tracker = tuleapProject.getTracker(trackerId);
			}
		} else {
			TuleapTaskId taskDataId = TuleapTaskId.forName(task.getTaskId());
			TuleapServer tuleapServer = this.connector.getServer(repository.getRepositoryUrl());
			TuleapProject project = tuleapServer.getProject(taskDataId.getProjectId());
			tracker = project.getTracker(taskDataId.getTrackerId());
		}

		// Field name and label (for context, let's take the first one available) and description
		String fieldname = null;
		String fieldlabel = null;
		String description = ""; //$NON-NLS-1$
		if (attachmentAttribute != null) {
			TaskAttribute nameAttribute = attachmentAttribute
					.getAttribute(ITuleapConstants.ATTACHMENT_FIELD_NAME);
			fieldname = nameAttribute.getValue();
			TaskAttribute labelAttribute = attachmentAttribute
					.getAttribute(ITuleapConstants.ATTACHMENT_FIELD_LABEL);
			fieldlabel = labelAttribute.getValue();
			TaskAttribute descriptionAttribute = attachmentAttribute
					.getAttribute(TaskAttribute.ATTACHMENT_DESCRIPTION);
			description = descriptionAttribute.getValue();
		} else {
			// Let's find the first valid value for the attachment field name
			if (tracker != null) {
				TuleapFileUpload fileUploadField = tracker.getAttachmentField();
				if (fileUploadField != null) {
					fieldname = fileUploadField.getName();
					fieldlabel = fileUploadField.getLabel();
				}
			}
			if (CONTEXT_FILENAME.equals(source.getName())) {
				description = CONTEXT_DESCRIPTION;
			}
		}

		int artifactId = Integer.valueOf(task.getTaskId()).intValue();

		String filename = source.getName();
		String filetype = source.getContentType();
		Long size = Long.valueOf(source.getLength());
		InputStream inputStream = source.createInputStream(monitor);

		TuleapAttachmentDescriptor tuleapAttachmentDescriptor = new TuleapAttachmentDescriptor(fieldname,
				fieldlabel, filename, filetype, description, size, inputStream);
		try {
			try {
				tuleapSoapConnector.uploadAttachment(-1, artifactId, tuleapAttachmentDescriptor, comment,
						monitor);
			} finally {
				inputStream.close();
			}
		} catch (ServiceException e) {
			IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		}
	}

}
