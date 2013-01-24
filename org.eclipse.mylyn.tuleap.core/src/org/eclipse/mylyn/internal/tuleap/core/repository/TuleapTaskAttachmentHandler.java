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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.internal.tasks.core.TaskRepositoryLocation;
import org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClient;
import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapInstanceConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapTrackerConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapFileUpload;
import org.eclipse.mylyn.internal.tuleap.core.net.TuleapAttachmentDescriptor;
import org.eclipse.mylyn.internal.tuleap.core.net.TuleapSoapConnector;
import org.eclipse.mylyn.internal.tuleap.core.util.ITuleapConstants;
import org.eclipse.mylyn.internal.tuleap.core.util.TuleapUtil;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentHandler;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentSource;
import org.eclipse.mylyn.tasks.core.data.TaskAttachmentMapper;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;

/**
 * The Tuleap task attachement handler will be in charge of manipulating the task attachments.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
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
		AbstractWebLocation abstractWebLocation = new TaskRepositoryLocation(repository);
		TuleapSoapConnector tuleapSoapConnector = new TuleapSoapConnector(abstractWebLocation);
		String taskId = task.getTaskId();
		String attachmentAttributeId = attachmentAttribute.getId();
		int index = attachmentAttributeId.indexOf("---"); //$NON-NLS-1$
		if (index != -1) {
			String id = attachmentAttributeId.substring(index + "---".length()); //$NON-NLS-1$
			int attachmentId = Integer.valueOf(id).intValue();

			int artifactId = TuleapUtil.getArtifactIdFromTaskDataId(taskId);

			TaskAttachmentMapper taskAttachment = TaskAttachmentMapper.createFrom(attachmentAttribute);
			Long length = taskAttachment.getLength();
			int size = length.intValue();
			String filename = taskAttachment.getFileName();

			byte[] content = tuleapSoapConnector.getAttachmentContent(artifactId, attachmentId, filename,
					size, monitor);
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
		AbstractWebLocation abstractWebLocation = new TaskRepositoryLocation(repository);
		TuleapSoapConnector tuleapSoapConnector = new TuleapSoapConnector(abstractWebLocation);

		// Field name (for context, let's take the first one available)
		String fieldname = null;
		if (attachmentAttribute != null) {
			TaskAttribute attribute = attachmentAttribute
					.getAttribute(ITuleapConstants.ATTACHMENT_FIELD_NAME);
			fieldname = attribute.getValue();
		} else {
			// Let's find the first valid value for the attachment field name
			ITuleapClient client = this.connector.getClientManager().getClient(repository);
			TuleapInstanceConfiguration repositoryConfiguration = client.getRepositoryConfiguration();
			int trackerId = TuleapUtil.getTrackerIdFromTaskDataId(task.getTaskId());
			TuleapTrackerConfiguration trackerConfiguration = repositoryConfiguration
					.getTrackerConfiguration(trackerId);
			List<AbstractTuleapField> fields = trackerConfiguration.getFields();
			for (AbstractTuleapField abstractTuleapField : fields) {
				if (abstractTuleapField instanceof TuleapFileUpload) {
					fieldname = ((TuleapFileUpload)abstractTuleapField).getName();
					break;
				}
			}
		}

		// Field label (for context, let's take the first one available)
		String fieldlabel = null;
		if (attachmentAttribute != null) {
			TaskAttribute attribute = attachmentAttribute
					.getAttribute(ITuleapConstants.ATTACHMENT_FIELD_LABEL);
			fieldlabel = attribute.getValue();
		} else {
			// Let's find the first valid value for the attachment field label
			ITuleapClient client = this.connector.getClientManager().getClient(repository);
			TuleapInstanceConfiguration repositoryConfiguration = client.getRepositoryConfiguration();
			int trackerId = TuleapUtil.getTrackerIdFromTaskDataId(task.getTaskId());
			TuleapTrackerConfiguration trackerConfiguration = repositoryConfiguration
					.getTrackerConfiguration(trackerId);
			List<AbstractTuleapField> fields = trackerConfiguration.getFields();
			for (AbstractTuleapField abstractTuleapField : fields) {
				if (abstractTuleapField instanceof TuleapFileUpload) {
					fieldlabel = ((TuleapFileUpload)abstractTuleapField).getLabel();
					break;
				}
			}
		}

		// Description
		String description = ""; //$NON-NLS-1$
		if (attachmentAttribute != null) {
			TaskAttribute attribute = attachmentAttribute.getAttribute(TaskAttribute.ATTACHMENT_DESCRIPTION);
			description = attribute.getValue();
		} else if (CONTEXT_FILENAME.equals(source.getName())) {
			description = CONTEXT_DESCRIPTION;
		}

		int trackerId = TuleapUtil.getTrackerIdFromTaskDataId(task.getTaskId());
		int artifactId = TuleapUtil.getArtifactIdFromTaskDataId(task.getTaskId());

		String filename = source.getName();
		String filetype = source.getContentType();
		Long size = Long.valueOf(source.getLength());
		InputStream inputStream = source.createInputStream(monitor);

		TuleapAttachmentDescriptor tuleapAttachmentDescriptor = new TuleapAttachmentDescriptor(fieldname,
				fieldlabel, filename, filetype, description, size, inputStream);
		tuleapSoapConnector.uploadAttachment(trackerId, artifactId, tuleapAttachmentDescriptor, comment,
				monitor);
	}

}
