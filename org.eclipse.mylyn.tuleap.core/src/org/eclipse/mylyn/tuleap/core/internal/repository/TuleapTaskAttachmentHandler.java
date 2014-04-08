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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentHandler;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentSource;
import org.eclipse.mylyn.tasks.core.data.TaskAttachmentMapper;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tuleap.core.internal.TuleapCoreActivator;
import org.eclipse.mylyn.tuleap.core.internal.client.TuleapClientManager;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.TuleapRestClient;
import org.eclipse.mylyn.tuleap.core.internal.data.TuleapTaskId;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapProject;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapServer;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapFileUpload;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AttachmentFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AttachmentValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifact;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifactWithAttachment;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapReference;
import org.eclipse.mylyn.tuleap.core.internal.util.TuleapCoreKeys;
import org.eclipse.mylyn.tuleap.core.internal.util.TuleapCoreMessages;

/**
 * The Tuleap task attachement handler will be in charge of manipulating the task attachments.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 * @since 0.7
 */
public class TuleapTaskAttachmentHandler extends AbstractTaskAttachmentHandler {

	/**
	 * The Tuleap buffer size for attachments.
	 */
	public static final int BUFFER_SIZE = 1024 * 1024;

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
	 * The client manager.
	 */
	private TuleapClientManager clientManager;

	/**
	 * The constructor.
	 *
	 * @param tuleapRepositoryConnector
	 *            The connector
	 * @param clientManager
	 *            The client manager
	 */
	public TuleapTaskAttachmentHandler(ITuleapRepositoryConnector tuleapRepositoryConnector,
			TuleapClientManager clientManager) {
		this.connector = tuleapRepositoryConnector;
		this.clientManager = clientManager;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentHandler#canGetContent(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.ITask)
	 */
	@Override
	public boolean canGetContent(TaskRepository repository, ITask task) {
		TuleapFileUpload uploadField = this.getFileUploadField(repository, task);
		return this.getFileUploadField(repository, task) != null && uploadField.isReadable();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentHandler#canPostContent(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.ITask)
	 */
	@Override
	public boolean canPostContent(TaskRepository repository, ITask task) {
		TuleapFileUpload uploadField = this.getFileUploadField(repository, task);
		return uploadField != null && uploadField.isUpdatable();
	}

	/**
	 * Indicates if the tracker on which the task is contained has a file upload field.
	 *
	 * @param repository
	 *            The task repository
	 * @param task
	 *            The current task
	 * @return <code>true</code> if we can upload or download a file for the given task, <code>false</code>
	 *         otherwise.
	 */
	private TuleapFileUpload getFileUploadField(TaskRepository repository, ITask task) {
		TuleapFileUpload result = null;
		TuleapTaskId taskDataId = TuleapTaskId.forName(task.getTaskId());
		TuleapServer server = this.connector.getServer(repository);
		if (server == null) {
			// Local config inexistent or incompatible with new version
			// and task repository not refreshed
			return null;
		}
		TuleapProject project = server.getProject(taskDataId.getProjectId());
		TuleapTracker tracker = null;
		if (project != null) {
			// Can happen for new tasks
			tracker = project.getTracker(taskDataId.getTrackerId());
		}
		if (tracker != null) {
			result = tracker.getAttachmentField();
		}
		return result;
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
		String attachmentAttributeId = attachmentAttribute.getId();
		int index = attachmentAttributeId.indexOf("---"); //$NON-NLS-1$
		if (index != -1) {
			String id = attachmentAttributeId.substring(index + "---".length()); //$NON-NLS-1$
			int attachmentId = Integer.valueOf(id).intValue();
			TaskAttachmentMapper taskAttachment = TaskAttachmentMapper.createFrom(attachmentAttribute);
			Long length = taskAttachment.getLength();
			int size = length.intValue();
			return new TuleapAttachmentInputStream(size, attachmentId, BUFFER_SIZE, clientManager
					.getRestClient(repository), monitor);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws CoreException
	 * @see org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentHandler#postContent(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.ITask,
	 *      org.eclipse.mylyn.tasks.core.data.AbstractTaskAttachmentSource, java.lang.String,
	 *      org.eclipse.mylyn.tasks.core.data.TaskAttribute, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void postContent(TaskRepository repository, ITask task, AbstractTaskAttachmentSource source,
			String comment, TaskAttribute attachmentAttribute, IProgressMonitor monitor) throws CoreException {
		TuleapServer server = this.connector.getServer(repository);
		if (server == null) {
			// Local config inexistent or incompatible with new version
			// and task repository not refreshed
			throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
					TuleapCoreMessages.getString(TuleapCoreKeys.configUpdateNeeded)));
		}
		TuleapTaskId taskDataId = TuleapTaskId.forName(task.getTaskId());
		TuleapProject project = server.getProject(taskDataId.getProjectId());
		TuleapTracker tracker = project.getTracker(taskDataId.getTrackerId());

		// Field description
		String description = ""; //$NON-NLS-1$
		if (attachmentAttribute != null) {
			TaskAttribute descriptionAttribute = attachmentAttribute
					.getAttribute(TaskAttribute.ATTACHMENT_DESCRIPTION);
			description = descriptionAttribute.getValue();
		} else if (CONTEXT_FILENAME.equals(source.getName())) {
			description = CONTEXT_DESCRIPTION; // Management of mylyn context upload
		}

		int artifactId = TuleapTaskId.forName(task.getTaskId()).getArtifactId();
		String filename = source.getName();
		String filetype = source.getContentType();
		InputStream in = null;
		TuleapRestClient client = clientManager.getRestClient(repository);
		TuleapReference fileReference = null;
		TuleapFileUpload field = tracker.getAttachmentField();
		long length = source.getLength();
		if (field != null) {
			try {
				in = source.createInputStream(monitor);
				// POST /artifacts_files
				byte[] buffer = new byte[BUFFER_SIZE];
				int offset = 2; // offset must be 2 the first time we call update
				while (true) {
					int read = in.read(buffer);
					checkMonitorCancelled(monitor, client, fileReference);
					byte[] toSend;
					if (read == -1) {
						break; // End of input
					} else if (read == BUFFER_SIZE) {
						toSend = buffer;
					} else {
						toSend = new byte[read];
						System.arraycopy(buffer, 0, toSend, 0, read);
					}
					String chunk = Base64.encodeBase64String(toSend);
					if (fileReference == null) {
						if (monitor != null) {
							monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.uploadingAttachment,
									"0", Long.toString(length))); //$NON-NLS-1$
						}
						fileReference = client.createArtifactFile(chunk, filetype, filename, description,
								monitor);
					} else {
						if (monitor != null) {
							monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.uploadingAttachment,
									Integer.toString((offset - 1) * BUFFER_SIZE), Long.toString(length)));
						}
						client.updateArtifactFile(fileReference.getId(), chunk, offset++, monitor);
					}
				}
			} catch (CoreException e) {
				// Something went wrong, a temp file may have been created but is not yet attache dto the
				// artifact. We need to try and delete this temp file otherwise we will reach the maximum
				// allowed number of temporary files
				if (fileReference != null) {
					deleteRemoteTempFile(monitor, client, fileReference);
				}
				throw e;
			} catch (IOException e) {
				throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
						TuleapCoreMessages.getString(TuleapCoreKeys.cannotReadFileContent), e));
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException e) {
					TuleapCoreActivator.log(e, true);
				}
			}
			try {
				Assert.isNotNull(fileReference);
				checkMonitorCancelled(monitor, client, fileReference);
				// Now, we need to associate the uploaded temporary file to the artifact.
				// But for that, we need the current list of attachments of this artifact!
				TuleapArtifactWithAttachment commentedArtifact = new TuleapArtifactWithAttachment();
				commentedArtifact.setId(Integer.valueOf(artifactId));
				commentedArtifact.addField(field);
				// Temporary hack while Tuleap does not provide a better API:
				// We retrieve the artifact to get an up-to-date version of attachments
				TuleapArtifact artifact = client.getArtifact(artifactId, server, monitor);
				AttachmentFieldValue value = (AttachmentFieldValue)artifact.getFieldValue(field
						.getIdentifier());
				// We copy the current attachments and add the new one
				List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
				if (value != null) {
					attachments.addAll(value.getAttachments());
				}
				attachments.add(new AttachmentValue(String.valueOf(fileReference.getId()), filename, 0, 0,
						description, filetype, fileReference.getUri()));
				AttachmentFieldValue fileDescription = new AttachmentFieldValue(field.getIdentifier(),
						attachments);
				// We now add the new attachment
				commentedArtifact.addFieldValue(fileDescription);
				if (source.getDescription() != null) {
					commentedArtifact.setNewComment(source.getDescription());
				}
				client.updateArtifact(commentedArtifact, monitor);
				// CHECKSTYLE:OFF
			} catch (Exception e) {
				// CHECKSTYLE:ON
				TuleapCoreActivator.log(e, true);
				deleteRemoteTempFile(monitor, client, fileReference);
				throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
						TuleapCoreMessages.getString(TuleapCoreKeys.uploadAttachmentFailed), e));
			}
		} else {
			TuleapCoreActivator.log(TuleapCoreMessages.getString(TuleapCoreKeys.missingAttachmentField, task
					.getTaskKey()), true);
		}
	}

	/**
	 * Manages cancellation of progress monitor.
	 *
	 * @param monitor
	 *            The monitor
	 * @param client
	 *            The client
	 * @param fileReference
	 *            The file reference
	 */
	private void checkMonitorCancelled(IProgressMonitor monitor, TuleapRestClient client,
			TuleapReference fileReference) {
		if (monitor != null && monitor.isCanceled()) {
			deleteRemoteTempFile(monitor, client, fileReference);
			throw new OperationCanceledException();
		}
	}

	/**
	 * Delete a remote temporary file (for example to prevent reaching the tuleap server's threshold).
	 *
	 * @param monitor
	 *            The progress monitor to use
	 * @param client
	 *            The client to use
	 * @param fileReference
	 *            The file reference, must not be null
	 */
	private void deleteRemoteTempFile(IProgressMonitor monitor, TuleapRestClient client,
			TuleapReference fileReference) {
		monitor.subTask(TuleapCoreMessages.getString(TuleapCoreKeys.deletingFile));
		try {
			client.deleteArtifactFile(fileReference.getId(), monitor);
		} catch (CoreException e) {
			TuleapCoreActivator.log(e, true);
		}
	}
}
