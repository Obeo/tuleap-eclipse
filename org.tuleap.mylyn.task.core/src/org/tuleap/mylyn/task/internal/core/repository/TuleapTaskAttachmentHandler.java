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
import org.tuleap.mylyn.task.internal.core.client.ITuleapClient;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.TuleapInstanceConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFileUpload;
import org.tuleap.mylyn.task.internal.core.net.TuleapAttachmentDescriptor;
import org.tuleap.mylyn.task.internal.core.net.TuleapSoapConnector;
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
		boolean hasFileUploadField = false;

		// HACK-ish [SBE]
		TuleapTrackerConfiguration configuration = this.getTrackerConfigurationFromTaskKey(repository, task);

		if (configuration != null) {
			List<AbstractTuleapField> fields = configuration.getFields();
			for (AbstractTuleapField abstractTuleapField : fields) {
				if (abstractTuleapField instanceof TuleapFileUpload) {
					return true;
				}
			}
		}

		return hasFileUploadField;
	}

	/**
	 * Returns the configuration of the tracker from the task key.
	 * <p>
	 * HACK-ish [SBE] - We should rely on the identifier of the tracker but since we only have access to the
	 * task and since we do not have access to the server here (Mylyn workflow limitation), we can only rely
	 * on the name of the field. If the TaskDataManager from Mylyn Core was not
	 * "only accessible from Mylyn UI", this issue would not exists since we could have access then to the
	 * task data. If two projects with the same name have a tracker with the same name and if only one of
	 * those tracker has an upload field, it will allow the support for upload fields on both trackers. Which
	 * would create errors when we try to upload the attachement.
	 * </p>
	 * 
	 * @param repository
	 *            The task repository
	 * @param task
	 *            The task
	 * @return The configuration of the tracker from the task key
	 */
	private TuleapTrackerConfiguration getTrackerConfigurationFromTaskKey(TaskRepository repository,
			ITask task) {
		String projectName = null;
		String trackerName = null;

		String taskKey = task.getTaskKey();
		if (taskKey != null) {
			int startIndex = taskKey.indexOf(':');
			int endIndex = taskKey.indexOf('-');
			if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
				projectName = taskKey.substring(0, startIndex);
				trackerName = taskKey.substring(startIndex + 1, endIndex);
			}
		}

		ITuleapClient client = this.connector.getClientManager().getClient(repository);

		if (client != null && projectName != null && trackerName != null) {
			TuleapInstanceConfiguration repositoryConfiguration = client.getRepositoryConfiguration();
			List<TuleapProjectConfiguration> allProjectConfigurations = repositoryConfiguration
					.getAllProjectConfigurations();
			for (TuleapProjectConfiguration tuleapProjectConfiguration : allProjectConfigurations) {
				if (projectName.equals(tuleapProjectConfiguration.getName())) {
					List<TuleapTrackerConfiguration> allTrackerConfigurations = tuleapProjectConfiguration
							.getAllTrackerConfigurations();
					for (TuleapTrackerConfiguration tuleapTrackerConfiguration : allTrackerConfigurations) {
						if (trackerName.equals(tuleapTrackerConfiguration.getName())) {
							return tuleapTrackerConfiguration;
						}
					}
				}
			}
		}
		return null;
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

		TuleapTrackerConfiguration configuration = null;

		// Let's find the tracker configuration
		ITuleapClient client = this.connector.getClientManager().getClient(repository);
		TuleapInstanceConfiguration repositoryConfiguration = client.getRepositoryConfiguration();

		// If the attachement attribute is available, let's use it
		if (attachmentAttribute != null) {
			TaskData taskData = attachmentAttribute.getTaskData();
			TuleapTaskMapper tuleapTaskMapper = new TuleapTaskMapper(taskData);

			int trackerId = tuleapTaskMapper.getTrackerId();

			List<TuleapProjectConfiguration> allProjectConfigurations = repositoryConfiguration
					.getAllProjectConfigurations();
			for (TuleapProjectConfiguration tuleapProjectConfiguration : allProjectConfigurations) {
				TuleapTrackerConfiguration trackerConfiguration = tuleapProjectConfiguration
						.getTrackerConfiguration(trackerId);
				if (trackerConfiguration != null) {
					configuration = trackerConfiguration;
				}
			}
		} else {
			// HACK-ish [SBE]
			configuration = this.getTrackerConfigurationFromTaskKey(repository, task);
		}

		// Field name (for context, let's take the first one available)
		String fieldname = null;
		if (attachmentAttribute != null) {
			TaskAttribute attribute = attachmentAttribute
					.getAttribute(ITuleapConstants.ATTACHMENT_FIELD_NAME);
			fieldname = attribute.getValue();
		} else {
			// Let's find the first valid value for the attachment field name
			if (configuration != null) {
				List<AbstractTuleapField> fields = configuration.getFields();
				for (AbstractTuleapField abstractTuleapField : fields) {
					if (abstractTuleapField instanceof TuleapFileUpload) {
						fieldname = ((TuleapFileUpload)abstractTuleapField).getName();
						break;
					}
				}
			}
		}

		// Field label (for context, let's take the first one available)
		String fieldlabel = null;
		if (attachmentAttribute != null) {
			TaskAttribute attribute = attachmentAttribute
					.getAttribute(ITuleapConstants.ATTACHMENT_FIELD_LABEL);
			fieldlabel = attribute.getValue();
		} else if (configuration != null) {
			// Let's find the first valid value for the attachment field label
			List<AbstractTuleapField> fields = configuration.getFields();
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

		int artifactId = Integer.valueOf(task.getTaskId()).intValue();

		String filename = source.getName();
		String filetype = source.getContentType();
		Long size = Long.valueOf(source.getLength());
		InputStream inputStream = source.createInputStream(monitor);

		TuleapAttachmentDescriptor tuleapAttachmentDescriptor = new TuleapAttachmentDescriptor(fieldname,
				fieldlabel, filename, filetype, description, size, inputStream);
		try {
			tuleapSoapConnector
					.uploadAttachment(-1, artifactId, tuleapAttachmentDescriptor, comment, monitor);
		} catch (ServiceException e) {
			IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		}
	}

}
