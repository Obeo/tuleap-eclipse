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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.internal.tuleap.core.client.TuleapClient;
import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField;
import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapFormElement;
import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapStructuralElement;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapArtifact;
import org.eclipse.mylyn.internal.tuleap.core.model.structural.TuleapFieldSet;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.RepositoryResponse;
import org.eclipse.mylyn.tasks.core.RepositoryResponse.ResponseKind;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMetaData;
import org.eclipse.mylyn.tasks.core.data.TaskData;

/**
 * This class is in charge of the publication and retrieval of the tasks data to and from the repository.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapTaskDataHandler extends AbstractTaskDataHandler {

	/**
	 * The Tuleap repository connector.
	 */
	private TuleapRepositoryConnector connector;

	/**
	 * The constructor.
	 * 
	 * @param repositoryConnector
	 *            The Tuleap repository connector.
	 */
	public TuleapTaskDataHandler(TuleapRepositoryConnector repositoryConnector) {
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
		TuleapClient client = this.connector.getClientManager().getClient(repository);
		if (taskData.isNew()) {
			int artifactId = client.createArtifact(artifact, monitor);
			response = new RepositoryResponse(ResponseKind.TASK_CREATED, Integer.valueOf(artifactId)
					.toString());
		} else {
			client.updateArtifact(artifact, monitor);
			response = new RepositoryResponse(ResponseKind.TASK_UPDATED, Integer.valueOf(artifact.getId())
					.toString());
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
	private static TuleapArtifact getTuleapArtifact(TaskRepository repository, TaskData taskData) {
		// Create the artifact
		TuleapArtifact artifact = null;
		if (taskData.isNew()) {
			artifact = new TuleapArtifact();
		} else {
			artifact = new TuleapArtifact(TuleapRepositoryConnector.getArtifactId(taskData.getTaskId()));
		}

		Collection<TaskAttribute> attributes = taskData.getRoot().getAttributes().values();
		for (TaskAttribute taskAttribute : attributes) {
			// Ignore Tuleap internal attributes
			if (!TuleapAttributeMapper.isInternalAttribute(taskAttribute)) {
				artifact.putValue(taskAttribute.getId(), taskAttribute.getValue());
			}
		}

		// TODO Handle the other attributes (date to parse, status to set, etc...)

		return artifact;
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
		if (taskData.isNew()) {
			TuleapRepositoryConfiguration repositoryConfiguration = connector.getRepositoryConfiguration(
					repository, false, monitor);
			if (repositoryConfiguration != null) {
				// Update the available attributes for the tasks
				TuleapClient tuleapClient = this.connector.getClientManager().getClient(repository);
				tuleapClient.updateAttributes(monitor, false);
				this.createDefaultAttributes(taskData, tuleapClient, false);
			}
		}
		return true;
	}

	/**
	 * Creates the default Tuleap related attributes.
	 * 
	 * @param taskData
	 *            The task data
	 * @param tuleapClient
	 *            The Tuleap client
	 * @param existingTask
	 *            Indicates if we are manipulating a newly created task
	 */
	private void createDefaultAttributes(TaskData taskData, TuleapClient tuleapClient, boolean existingTask) {
		TuleapRepositoryConfiguration configuration = tuleapClient.getRepositoryConfiguration();
		if (configuration != null) {
			// TODO Configure content to display it in the editor later

			// Assigned to
			TaskAttribute attribute = taskData.getRoot().createAttribute(TaskAttribute.USER_ASSIGNED);
			TaskAttributeMetaData metaData = attribute.getMetaData();
			metaData.setLabel("Assigned to");
			metaData.setKind(TaskAttribute.KIND_DEFAULT);
			metaData.setType(TaskAttribute.TYPE_PERSON);
			attribute.setValue("stephane.begaudeau@gmail.com");

			// Priority
			attribute = taskData.getRoot().createAttribute(TaskAttribute.PRIORITY);
			metaData = attribute.getMetaData();
			metaData.setLabel("Priority");
			metaData.setKind(TaskAttribute.KIND_DEFAULT);
			metaData.setType(TaskAttribute.TYPE_SINGLE_SELECT);
			attribute.putOption("P1", "P1");
			attribute.putOption("P2", "P2");
			attribute.putOption("P3", "P3");
			attribute.putOption("P4", "P4");
			attribute.putOption("P5", "P5");
			attribute.setValue("P3");

			// Summary
			attribute = taskData.getRoot().createAttribute(TaskAttribute.SUMMARY);
			metaData = attribute.getMetaData();
			metaData.setLabel("Summary");
			metaData.setKind(TaskAttribute.KIND_DEFAULT);
			metaData.setType(TaskAttribute.TYPE_SHORT_RICH_TEXT);
			attribute.setValue("Basic summary of a new Tuleap task");

			// Creation date
			attribute = taskData.getRoot().createAttribute(TaskAttribute.DATE_CREATION);
			metaData = attribute.getMetaData();
			metaData.setLabel("Created");
			metaData.setKind(TaskAttribute.KIND_DEFAULT);
			metaData.setType(TaskAttribute.TYPE_DATE);
			attribute.setValue("");

			// Last modification date
			attribute = taskData.getRoot().createAttribute(TaskAttribute.DATE_MODIFICATION);
			metaData = attribute.getMetaData();
			metaData.setLabel("Modified");
			metaData.setKind(TaskAttribute.KIND_DEFAULT);
			metaData.setType(TaskAttribute.TYPE_DATE);
			attribute.setValue("");

			// Resolution
			attribute = taskData.getRoot().createAttribute(TaskAttribute.RESOLUTION);
			metaData = attribute.getMetaData();
			metaData.setLabel("Resolution");
			metaData.setKind(TaskAttribute.KIND_DEFAULT);
			metaData.setType(TaskAttribute.TYPE_SINGLE_SELECT);
			attribute.putOption("NEW", "NEW");
			attribute.putOption("ASSIGNED", "ASSIGNED");
			attribute.putOption("SOLVED", "SOLVED");
			attribute.putOption("INVALID", "INVALID");
			attribute.setValue("NEW");

			// Default attributes
			List<AbstractTuleapStructuralElement> formElements = configuration.getFormElements();
			for (AbstractTuleapStructuralElement abstractTuleapStructuralElement : formElements) {
				if (abstractTuleapStructuralElement instanceof TuleapFieldSet) {
					TuleapFieldSet tuleapFieldSet = (TuleapFieldSet)abstractTuleapStructuralElement;
					List<AbstractTuleapFormElement> fieldSetFormElements = tuleapFieldSet.getFormElements();
					for (AbstractTuleapFormElement abstractTuleapFormElement : fieldSetFormElements) {
						if (abstractTuleapFormElement instanceof AbstractTuleapField) {
							this.createAttribute(taskData, (AbstractTuleapField)abstractTuleapFormElement);
						}
					}
				}
			}

			// Additional properties

		}
	}

	/**
	 * Creates the attribute representing the Tuleap field in the given Mylyn task data.
	 * 
	 * @param taskData
	 *            The Mylyn task data
	 * @param tuleapField
	 *            The Tuleap field
	 */
	private void createAttribute(TaskData taskData, AbstractTuleapField tuleapField) {
		TaskAttribute attribute = taskData.getRoot().createAttribute(tuleapField.getIdentifier());

		TaskAttributeMetaData attributeMetadata = attribute.getMetaData();
		attributeMetadata.setType(tuleapField.getMetadataType());
		attributeMetadata.setKind(tuleapField.getMetadataKind());
		// TODO Support advanced permissions (read only / visible)
		// attributeMetadata.setReadOnly(tuleaField)
		attributeMetadata.setLabel(tuleapField.getLabel());

		Map<String, String> options = tuleapField.getOptions();
		for (Entry<String, String> entry : options.entrySet()) {
			attribute.putOption(entry.getKey(), entry.getValue());
		}

		Object defaultValue = tuleapField.getDefaultValue();
		if (defaultValue instanceof String) {
			attribute.setValue((String)defaultValue);
		} else if (defaultValue instanceof List<?>) {
			List<?> list = (List<?>)defaultValue;

			List<String> strList = new ArrayList<String>();
			for (Object object : list) {
				if (object instanceof String) {
					strList.add((String)object);
				}
			}

			attribute.setValues(strList);
		}
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
	 */
	public TaskData getTaskData(TaskRepository taskRepository, String taskId, IProgressMonitor monitor) {
		return downloadTaskData(taskRepository, TuleapRepositoryConnector.getArtifactId(taskId), monitor);
	}

	/**
	 * Downloads the Tuleap artifact and convert it to a Mylyn task data.
	 * 
	 * @param taskRepository
	 *            The Mylyn tasks repository
	 * @param taskId
	 *            The ID of the task to download
	 * @param monitor
	 *            The progress monitor
	 * @return The Mylyn task data with the information obtained from the Tuleap artifact matching the given
	 *         task ID obtained from the Tuleap client.
	 */
	public TaskData downloadTaskData(TaskRepository taskRepository, int taskId, IProgressMonitor monitor) {
		TuleapClient tuleapClient = this.connector.getClientManager().getClient(taskRepository);

		tuleapClient.updateAttributes(monitor, false);
		TuleapArtifact tuleapArtifact = tuleapClient.getArtifact(taskId, monitor);
		TaskData taskData = this.createTaskDataFromArtifact(tuleapClient, taskRepository, tuleapArtifact,
				monitor);

		return taskData;
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
	private TaskData createTaskDataFromArtifact(TuleapClient tuleapClient, TaskRepository taskRepository,
			TuleapArtifact tuleapArtifact, IProgressMonitor monitor) {
		// TODO Convert Tuleap artifact to Mylyn task data
		return null;
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
		// TODO Check with Enalean for subtask support
		return false;
	}

}
