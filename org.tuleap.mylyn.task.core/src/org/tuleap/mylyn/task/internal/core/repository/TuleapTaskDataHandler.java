/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.internal.core.repository;

import java.util.Date;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.RepositoryResponse;
import org.eclipse.mylyn.tasks.core.RepositoryResponse.ResponseKind;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskMapper;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapClient;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskMapper;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.field.AbstractTuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapDate;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;

/**
 * This class is in charge of the publication and retrieval of the tasks data to and from the repository.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapTaskDataHandler extends AbstractTaskDataHandler {

	/**
	 * The Tuleap repository connector.
	 */
	private ITuleapRepositoryConnector connector;

	/**
	 * The constructor.
	 * 
	 * @param repositoryConnector
	 *            The Tuleap repository connector.
	 */
	public TuleapTaskDataHandler(ITuleapRepositoryConnector repositoryConnector) {
		this.connector = repositoryConnector;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler#postTaskData(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.data.TaskData, java.util.Set,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public RepositoryResponse postTaskData(TaskRepository repository, TaskData taskData,
			Set<TaskAttribute> oldAttributes, IProgressMonitor monitor) throws CoreException {
		RepositoryResponse response = null;

		TuleapArtifact artifact = TuleapTaskDataHandler.getTuleapArtifact(repository, taskData);
		TuleapSoapClient tuleapSoapClient = this.connector.getClientManager().getSoapClient(repository);
		if (taskData.isNew()) {
			String artifactId = tuleapSoapClient.createArtifact(artifact, monitor);
			response = new RepositoryResponse(ResponseKind.TASK_CREATED, artifactId);
		} else {
			tuleapSoapClient.updateArtifact(artifact, monitor);
			response = new RepositoryResponse(ResponseKind.TASK_UPDATED, String.valueOf(artifact.getId()));
		}

		return response;
	}

	/**
	 * Returns a Tuleap artifact representing the given Mylyn task data.
	 * 
	 * @param repository
	 *            The Mylyn task repository
	 * @param taskData
	 *            The Mylyn task data
	 * @return A Tuleap artifact representing the given Mylyn task data.
	 */
	public static TuleapArtifact getTuleapArtifact(TaskRepository repository, TaskData taskData) {
		TuleapTaskMapper tuleapTaskMapper = new TuleapTaskMapper(taskData, null);

		// TODO Remove this when removing SOAP Connector
		//		String trackerName = ""; //$NON-NLS-1$
		// int trackerId = tuleapTaskMapper.getTrackerId();
		//
		//		String projectName = ""; //$NON-NLS-1$
		//
		// // Create the artifact
		// TuleapArtifact artifact = null;
		// if (taskData.isNew()) {
		// artifact = new TuleapArtifact();
		// artifact.setTrackerId(trackerId);
		// } else {
		// int artifactId = Integer.valueOf(taskData.getTaskId()).intValue();
		// artifact = new TuleapArtifact(artifactId, trackerId, trackerName, projectName);
		// }
		//
		// Collection<TaskAttribute> attributes = taskData.getRoot().getAttributes().values();
		// for (TaskAttribute taskAttribute : attributes) {
		// if (taskAttribute.getId().startsWith(TaskAttribute.PREFIX_COMMENT)) {
		// // Ignore comments since we won't resubmit old comments
		// } else if (TaskAttribute.TYPE_TASK_DEPENDENCY.equals(taskAttribute.getMetaData().getType())) {
		// // We need to convert the list of task dependencies to use only the id of the task.
		// for (String value : taskAttribute.getValues()) {
		// /*
		// * /!\HACKISH/!\ We may have, as the id of the task, an identifier (ie: 917) or a complex
		// * identifier (ie: MyRepository:MyProject[116] #917 - My Task Name). We will try to parse
		// * the value as an integer, if it fails, then we know that we have a complex identifier,
		// * in that case, we will parse the identifier from this complex identifier and use it.
		// */
		//					StringTokenizer stringTokenizer = new StringTokenizer(value, ","); //$NON-NLS-1$
		// while (stringTokenizer.hasMoreTokens()) {
		// String nextToken = stringTokenizer.nextToken().trim();
		// try {
		// Integer.valueOf(nextToken);
		//
		// // No exception raised -> we have an integer
		// artifact.putValue(taskAttribute.getId(), nextToken);
		// } catch (NumberFormatException e) {
		// // We have a complex URL, that's what we are looking for! Let's convert it
		// int index = nextToken.indexOf(ITuleapConstants.TASK_DATA_ID_SEPARATOR);
		// if (index != -1) {
		// String id = nextToken.substring(index
		// + ITuleapConstants.TASK_DATA_ID_SEPARATOR.length());
		// artifact.putValue(taskAttribute.getId(), id);
		// }
		// }
		// }
		// }
		// } else if (!TuleapAttributeMapper.isInternalAttribute(taskAttribute)) {
		// // Ignore Tuleap internal attributes
		// if (taskAttribute.getValues().size() > 1) {
		// for (String value : taskAttribute.getValues()) {
		// artifact.putValue(taskAttribute.getId(), value);
		// }
		// } else {
		// artifact.putValue(taskAttribute.getId(), taskAttribute.getValue());
		// }
		// }
		// }

		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler#initializeTaskData(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.data.TaskData, org.eclipse.mylyn.tasks.core.ITaskMapping,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public boolean initializeTaskData(TaskRepository repository, TaskData taskData,
			ITaskMapping initializationData, IProgressMonitor monitor) throws CoreException {
		if (!taskData.isNew()) {
			return false;
		}

		boolean isInitialized = false;
		TuleapServerConfiguration repositoryConfiguration = connector.getRepositoryConfiguration(repository,
				true, monitor);
		if (repositoryConfiguration != null) {
			// Sets the creation date and last modification date.
			if (this.connector instanceof AbstractRepositoryConnector
					&& ((AbstractRepositoryConnector)this.connector).getTaskMapping(taskData) instanceof TaskMapper
					&& initializationData instanceof TuleapTaskMapping) {
				TuleapTaskMapping tuleapTaskMapping = (TuleapTaskMapping)initializationData;

				TuleapTrackerConfiguration trackerConfiguration = tuleapTaskMapping.getTracker();
				if (trackerConfiguration != null) {
					TuleapTaskMapper mapper = new TuleapTaskMapper(taskData, trackerConfiguration);
					mapper.initializeEmptyTaskData();

					Date now = new Date();
					mapper.setCreationDate(now);
					mapper.setModificationDate(now);
					mapper.setSummary(TuleapMylynTasksMessages.getString(
							"TuleapTaskDataHandler.DefaultNewTitle", trackerConfiguration.getItemName())); //$NON-NLS-1$

					isInitialized = true;
				}

			}
		}
		return isInitialized;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler#getAttributeMapper(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public TaskAttributeMapper getAttributeMapper(TaskRepository repository) {
		return new TuleapAttributeMapper(repository, connector);
	}

	/**
	 * Returns the Mylyn task data for the task matching the given task id in the given Mylyn task repository.
	 * 
	 * @param taskRepository
	 *            the Mylyn task repository
	 * @param taskId
	 *            the id of the task
	 * @param monitor
	 *            the progress monitor
	 * @return The Mylyn task data for the task matching the given task id in the given Mylyn task repository.
	 * @throws CoreException
	 *             If repository file configuration is not accessible
	 */
	public TaskData getTaskData(TaskRepository taskRepository, String taskId, IProgressMonitor monitor)
			throws CoreException {
		TuleapSoapClient tuleapSoapClient = this.connector.getClientManager().getSoapClient(taskRepository);

		// TODO SBE update the configuration!
		// tuleapSoapClient.updateAttributes(monitor, false);

		TuleapServerConfiguration tuleapServerConfiguration = this.connector
				.getRepositoryConfiguration(taskRepository.getRepositoryUrl());
		TuleapArtifact tuleapArtifact = tuleapSoapClient.getArtifact(taskId, tuleapServerConfiguration,
				monitor);
		if (tuleapArtifact != null) {
			TaskData taskData = this.createTaskDataFromArtifact(tuleapSoapClient, taskRepository,
					tuleapArtifact, monitor);
			return taskData;
		}
		return null;
	}

	/**
	 * Creates the Mylyn task data from the Tuleap artifact obtained from the Tuleap tracker.
	 * 
	 * @param tuleapClient
	 *            The Tuleap client
	 * @param taskRepository
	 *            The Mylyn tasks repository
	 * @param tuleapArtifact
	 *            The Tuleap Artifact
	 * @param monitor
	 *            The progress monitor
	 * @return The Mylyn task data computed from the Tuleap artifact.
	 */
	public TaskData createTaskDataFromArtifact(TuleapSoapClient tuleapClient, TaskRepository taskRepository,
			TuleapArtifact tuleapArtifact, IProgressMonitor monitor) {
		// Create the default attributes
		TaskData taskData = new TaskData(this.getAttributeMapper(taskRepository),
				ITuleapConstants.CONNECTOR_KIND, taskRepository.getRepositoryUrl(), String
						.valueOf(tuleapArtifact.getId()));

		// // Structure
		// TuleapTaskMapper mapper = new TuleapTaskMapper(taskData, trackerConfiguration);
		// mapper.initializeEmptyTaskData();
		// mapper.setTaskKey(tuleapArtifact.getUniqueName());
		// this.createOperations(taskData, trackerConfiguration,
		// tuleapArtifact.getValue(TaskAttribute.STATUS));
		//
		// // Date
		// mapper.setCreationDate(tuleapArtifact.getCreationDate());
		// mapper.setModificationDate(tuleapArtifact.getLastModificationDate());
		//
		// // URL
		// if (this.connector instanceof AbstractRepositoryConnector) {
		// AbstractRepositoryConnector abstractRepositoryConnector =
		// (AbstractRepositoryConnector)this.connector;
		// String taskUrl = abstractRepositoryConnector.getTaskUrl(taskRepository.getRepositoryUrl(),
		// Integer.valueOf(tuleapArtifact.getId()).toString());
		// mapper.setTaskUrl(taskUrl);
		// }
		//
		// // Task key
		// mapper.setTaskKey(Integer.valueOf(tuleapArtifact.getId()).toString());
		//
		// // Comments
		// List<TuleapElementComment> comments = tuleapArtifact.getComments();
		// for (TuleapElementComment comment : comments) {
		// mapper.addComment(comment);
		// }
		//
		// // Attachments
		// this.populateAttachments(tuleapArtifact, mapper);
		//
		// Collection<AbstractTuleapField> fields = trackerConfiguration.getFields();
		// for (AbstractTuleapField abstractTuleapField : fields) {
		// if (abstractTuleapField instanceof TuleapString
		// && ((TuleapString)abstractTuleapField).isSemanticTitle()) {
		// // Look for the summary
		// String value = tuleapArtifact.getValue(abstractTuleapField.getName());
		// if (value != null) {
		// mapper.setSummary(value);
		// }
		// } else if (abstractTuleapField instanceof AbstractTuleapSelectBox) {
		// AbstractTuleapSelectBox box = (AbstractTuleapSelectBox)abstractTuleapField;
		// if (box.isSemanticStatus()) {
		// // Look for the status
		// this.createTaskDataStatusFromArtifact(tuleapArtifact, mapper, abstractTuleapField);
		// } else if (box.isSemanticContributor()) {
		// // Look for the contributors in the select box
		// this.setTaskDataContributors(tuleapArtifact, taskData, box);
		// } else {
		// List<String> values = tuleapArtifact.getValues(box.getName());
		// if (box instanceof TuleapSelectBox) {
		// String value = values.get(0);
		// mapper.setSelectBoxValue(Integer.parseInt(value), box.getIdentifier());
		// } else if (box instanceof TuleapMultiSelectBox) {
		// Set<Integer> intValues = new HashSet<Integer>();
		// for (String strValue : values) {
		// try {
		// intValues.add(Integer.valueOf(strValue));
		// } catch (NumberFormatException e) {
		// // Do nothing
		// }
		// }
		// mapper.setMultiSelectBoxValues(intValues, box.getIdentifier());
		// }
		// }
		// } else if (abstractTuleapField instanceof TuleapDate) {
		// // Look for the date
		// this.createTaskDataDateFromArtifact(tuleapArtifact, mapper, (TuleapDate)abstractTuleapField);
		// } else {
		// // Other fields
		// List<String> values = tuleapArtifact.getValues(abstractTuleapField.getName());
		// // No need to test values != null
		// mapper.setValues(values, abstractTuleapField.getIdentifier());
		// }
		//
		// // Change the options if the select box has a workflow
		// if (abstractTuleapField instanceof TuleapSelectBox
		// && ((TuleapSelectBox)abstractTuleapField).hasWorkflow()) {
		// this.changeOptionsForSelectBoxWorkflow(taskData, (TuleapSelectBox)abstractTuleapField);
		// }
		// }

		return taskData;
	}

	/**
	 * Creates the status in the task data matching the information retrieved from the Tuleap artifact.
	 * 
	 * @param tuleapArtifact
	 *            The Tuleap artifact
	 * @param taskMapper
	 *            The task mapper
	 * @param abstractTuleapField
	 *            The Tuleap field representing the status
	 */
	private void createTaskDataStatusFromArtifact(TuleapArtifact tuleapArtifact, TuleapTaskMapper taskMapper,
			AbstractTuleapField abstractTuleapField) {
		// Look for the status
		// String status = tuleapArtifact.getValue(abstractTuleapField.getName());
		// if (status != null) {
		// taskMapper.setStatus(status);
		// }
	}

	/**
	 * Creates the contributors in the task data from the information retrieved from the Tuleap artifact.
	 * 
	 * @param tuleapArtifact
	 *            The Tuleap artifact
	 * @param taskData
	 *            The task data
	 * @param tuleapSelectBox
	 *            The Tuleap field representing the contributor
	 */
	private void setTaskDataContributors(TuleapArtifact tuleapArtifact, TaskData taskData,
			AbstractTuleapSelectBox tuleapSelectBox) {
		TaskAttribute taskAttribute = taskData.getRoot().getAttributes().get(TaskAttribute.USER_ASSIGNED);
		// Adds the values from the retrieved artifact
		// List<String> values = tuleapArtifact.getValues(tuleapSelectBox.getName());
		// taskAttribute.setValues(values);
	}

	/**
	 * Creates the date in the task data matching the information retrieved from the Tuleap artifact.
	 * 
	 * @param tuleapArtifact
	 *            The Tuleap artifact
	 * @param mapper
	 *            The task mapper
	 * @param tuleapDate
	 *            The Tuleap field representing the date
	 */
	private void createTaskDataDateFromArtifact(TuleapArtifact tuleapArtifact, TuleapTaskMapper mapper,
			TuleapDate tuleapDate) {
		// This value represents the number of seconds from 1/1/1970
		// yes, seconds, not milliseconds!
		// String value = tuleapArtifact.getValue(tuleapDate.getName());
		// try {
		// mapper.setDateValue(Integer.parseInt(value), tuleapDate.getIdentifier());
		// } catch (NumberFormatException e) {
		// // The date is empty or invalid...
		// }
	}

	/**
	 * Changes the options on the task attribute if the Tuleap select box has a workflow.
	 * 
	 * @param taskData
	 *            the task data
	 * @param tuleapSelectBox
	 *            The Tuleap select box
	 */
	private void changeOptionsForSelectBoxWorkflow(TaskData taskData, TuleapSelectBox tuleapSelectBox) {
		// The widget has a workflow

		TaskAttribute attribute = null;
		if (tuleapSelectBox.isSemanticStatus()) {
			attribute = taskData.getRoot().getAttribute(TaskAttribute.STATUS);
		} else {
			attribute = taskData.getRoot().getAttribute(
					Integer.valueOf(tuleapSelectBox.getIdentifier()).toString());
		}

		tuleapSelectBox.updateOptionsWithWorkflow(attribute);
	}

	/**
	 * Populates the attachments of the task data thanks to the attachments from the Tuleap artifact.
	 * 
	 * @param tuleapArtifact
	 *            The Tuleap artifact
	 * @param mapper
	 *            The task mapper
	 */
	private void populateAttachments(TuleapArtifact tuleapArtifact, TuleapTaskMapper mapper) {
		// Set<Entry<String, List<AttachmentFieldValue>>> attachments = tuleapArtifact.getAttachments();
		// for (Entry<String, List<AttachmentFieldValue>> entry : attachments) {
		// String tuleapFieldName = entry.getKey();
		//
		// List<AttachmentFieldValue> attachmentsList = entry.getValue();
		// for (AttachmentFieldValue tuleapAttachment : attachmentsList) {
		// mapper.addAttachment(tuleapFieldName, tuleapAttachment);
		// }
		// }
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler#canGetMultiTaskData(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public boolean canGetMultiTaskData(TaskRepository repository) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler#canInitializeSubTaskData(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.ITask)
	 */
	@Override
	public boolean canInitializeSubTaskData(TaskRepository repository, ITask task) {
		return false;
	}

}
