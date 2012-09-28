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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.internal.tuleap.core.client.TuleapClient;
import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapDynamicField;
import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField;
import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapFormElement;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapArtifact;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapFileUpload;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapSelectBox;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapString;
import org.eclipse.mylyn.internal.tuleap.core.model.permission.ITuleapDefaultPermissionGroups;
import org.eclipse.mylyn.internal.tuleap.core.model.permission.TuleapAccessPermission;
import org.eclipse.mylyn.internal.tuleap.core.model.workflow.TuleapWorkflow;
import org.eclipse.mylyn.internal.tuleap.core.util.ITuleapConstants;
import org.eclipse.mylyn.internal.tuleap.core.util.TuleapMylynTasksMessages;
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
import org.eclipse.mylyn.tasks.core.data.TaskMapper;
import org.eclipse.mylyn.tasks.core.data.TaskOperation;

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
				// TODO Compute the default value of the status, null if none
				this.createOperations(taskData, tuleapClient, "Open");

				// Sets the creation date and last modification date.
				ITaskMapping taskMapping = this.connector.getTaskMapping(taskData);
				if (taskMapping instanceof TaskMapper) {
					TaskMapper taskMapper = (TaskMapper)taskMapping;
					taskMapper.setCreationDate(new Date());
					taskMapper.setModificationDate(new Date());
				}
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
		if (configuration == null) {
			return;
		}
		// TODO Configure content to display it in the editor later

		// The kind of the task
		TaskAttribute attribute = taskData.getRoot().createAttribute(TaskAttribute.TASK_KIND);
		attribute.setValue(configuration.getName());

		// Creation date
		attribute = taskData.getRoot().createAttribute(TaskAttribute.DATE_CREATION);
		TaskAttributeMetaData metaData = attribute.getMetaData();
		metaData.setLabel(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.CreationDate")); //$NON-NLS-1$
		metaData.setType(TaskAttribute.TYPE_DATE);

		// Last modification date
		attribute = taskData.getRoot().createAttribute(TaskAttribute.DATE_MODIFICATION);
		metaData = attribute.getMetaData();
		metaData.setLabel(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.LastModificationDate")); //$NON-NLS-1$
		metaData.setType(TaskAttribute.TYPE_DATE);

		// New comment
		attribute = taskData.getRoot().createAttribute(TaskAttribute.COMMENT_NEW);
		metaData = attribute.getMetaData();
		metaData.setLabel(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.NewComment")); //$NON-NLS-1$
		metaData.setType(TaskAttribute.TYPE_LONG_RICH_TEXT);

		// Default attributes
		List<AbstractTuleapFormElement> formElements = configuration.getFormElements();
		for (AbstractTuleapFormElement abstractTuleapStructuralElement : formElements) {
			List<AbstractTuleapField> fields = TuleapRepositoryConfiguration
					.getFields(abstractTuleapStructuralElement);
			for (AbstractTuleapField abstractTuleapField : fields) {
				if (abstractTuleapField instanceof TuleapFileUpload) {
					// TODO Check if this is a good solution to ignore file upload fields entirely
					continue;
				}
				this.createAttribute(taskData, abstractTuleapField);
			}
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
		TaskAttribute attribute = null;

		// Semantic support
		if (tuleapField instanceof TuleapString && ((TuleapString)tuleapField).isSemanticTitle()) {
			attribute = taskData.getRoot().createAttribute(TaskAttribute.SUMMARY);
		} else if (tuleapField instanceof TuleapSelectBox) {
			TuleapSelectBox selectBox = (TuleapSelectBox)tuleapField;
			if (selectBox.isSemanticStatus()) {
				return;
			}

			if (selectBox.isSemanticContributor()) {
				// Create an attribute for the assigned person
				TaskAttribute anAttribute = taskData.getRoot().createAttribute(TaskAttribute.USER_ASSIGNED);
				TaskAttributeMetaData metaData = anAttribute.getMetaData();
				metaData.setLabel(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.AssignedToLabel")); //$NON-NLS-1$
				metaData.setKind(TaskAttribute.KIND_DEFAULT);
				metaData.setType(TaskAttribute.TYPE_PERSON);

				// Create the regular attribute
				attribute = taskData.getRoot().createAttribute(tuleapField.getIdentifier());
			} else {
				attribute = taskData.getRoot().createAttribute(tuleapField.getIdentifier());
			}
		} else {
			attribute = taskData.getRoot().createAttribute(tuleapField.getIdentifier());
		}

		// Attributes
		TaskAttributeMetaData attributeMetadata = attribute.getMetaData();
		attributeMetadata.setType(tuleapField.getMetadataType());
		attributeMetadata.setLabel(tuleapField.getLabel());

		// Set the kind if we do not have a semantic field
		boolean isTitle = tuleapField instanceof TuleapString
				&& ((TuleapString)tuleapField).isSemanticTitle();
		boolean isStatus = tuleapField instanceof TuleapSelectBox
				&& ((TuleapSelectBox)tuleapField).isSemanticStatus();
		boolean isContributor = tuleapField instanceof TuleapSelectBox
				&& ((TuleapSelectBox)tuleapField).isSemanticContributor();

		if (!isTitle && !isStatus && !isContributor) {
			attributeMetadata.setKind(tuleapField.getMetadataKind());
		}

		String group = ITuleapDefaultPermissionGroups.ALL_USERS;
		if (!tuleapField.getPermissions().canSubmit(group)
				&& !(TuleapAccessPermission.UPDATE == tuleapField.getPermissions().getAccessPermission(group))) {
			// TODO Compute the group of the user
			// attributeMetadata.setReadOnly(true);
		}

		// Dynamic fields are read only
		if (tuleapField instanceof AbstractTuleapDynamicField) {
			attributeMetadata.setReadOnly(true);
		}

		// Possible values
		Map<String, String> options = tuleapField.getOptions();
		for (Entry<String, String> entry : options.entrySet()) {
			attribute.putOption(entry.getKey(), entry.getValue());
		}

		// Default values
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
		} else if (defaultValue == null && isTitle) {
			attribute.setValue(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.DefaultNewTitle")); //$NON-NLS-1$
		}

	}

	/**
	 * Creates the operations available on the task data thanks to the configuration of the client while using
	 * the given current status.
	 * 
	 * @param taskData
	 *            The task data
	 * @param tuleapClient
	 *            The tuleap client used to get the configuration of the tracker
	 * @param currentStatus
	 *            The current status
	 */
	private void createOperations(TaskData taskData, TuleapClient tuleapClient, String currentStatus) {
		TuleapSelectBox statusSelectBox = null;

		// Create operations from the status semantic
		TuleapRepositoryConfiguration configuration = tuleapClient.getRepositoryConfiguration();
		List<AbstractTuleapFormElement> formElements = configuration.getFormElements();
		for (AbstractTuleapFormElement abstractTuleapStructuralElement : formElements) {
			List<AbstractTuleapField> fields = TuleapRepositoryConfiguration
					.getFields(abstractTuleapStructuralElement);
			for (AbstractTuleapField abstractTuleapField : fields) {
				if (abstractTuleapField instanceof TuleapSelectBox) {
					TuleapSelectBox selectBox = (TuleapSelectBox)abstractTuleapField;
					if (selectBox.isSemanticStatus()) {
						statusSelectBox = selectBox;
					}
				}
			}
		}

		if (statusSelectBox != null) {
			// Create the default attribute for the operation
			TaskAttribute operationAttribute = taskData.getRoot().getAttribute(TaskAttribute.OPERATION);
			if (operationAttribute == null) {
				operationAttribute = taskData.getRoot().createAttribute(TaskAttribute.OPERATION);
			}

			TaskAttribute attrStatus = taskData.getRoot().createAttribute(
					TaskAttribute.PREFIX_OPERATION + "tuleap.status");

			// Compute the label of the status
			String leaveAsLabel = TuleapMylynTasksMessages.getString(
					"TuleapTaskDataHandler.LeaveAs", currentStatus); //$NON-NLS-1$
			TaskAttribute leaveOperationAttribute = taskData.getRoot().createAttribute(
					ITuleapConstants.LEAVE_OPERATION);

			TaskOperation.applyTo(leaveOperationAttribute, ITuleapConstants.LEAVE_OPERATION, leaveAsLabel);

			// Set as default
			TaskOperation.applyTo(operationAttribute, ITuleapConstants.LEAVE_OPERATION, leaveAsLabel);

			TuleapWorkflow workflow = statusSelectBox.getWorkflow();
			if (workflow != null) {
				List<String> tuleapStatus = workflow.accessibleStates(currentStatus);
				List<String> openedAccessibleStatus = new ArrayList<String>();
				List<String> closedAccessibleStatus = new ArrayList<String>();

				for (String aStatus : tuleapStatus) {
					// Compute if the reachable state is an "opened" state
					if (statusSelectBox.getOpenStatus().contains(aStatus)) {
						openedAccessibleStatus.add(aStatus);
					} else {
						closedAccessibleStatus.add(aStatus);
					}
				}

				if (openedAccessibleStatus.size() > 0) {
					TaskAttribute attrResolved = taskData.getRoot().createAttribute("tuleap.resolution");
					TaskAttribute attrResolvedInput = taskData.getRoot().createAttribute(
							"tuleap.resolution.input");
					attrResolvedInput.getMetaData().setType(TaskAttribute.TYPE_SINGLE_SELECT);
					TaskOperation.applyTo(attrResolved, "tuleap.resolution", "Resolve as");
					attrResolved.getMetaData().putValue(TaskAttribute.META_ASSOCIATED_ATTRIBUTE_ID,
							"tuleap.resolution.input");
					for (String openedStatus : openedAccessibleStatus) {
						attrResolvedInput.putOption(openedStatus, openedStatus);
					}
				}

				if (closedAccessibleStatus.size() > 0) {
					TaskAttribute attrClosed = taskData.getRoot().createAttribute("tuleap.close");
					TaskAttribute attrClosedInput = taskData.getRoot().createAttribute("tuleap.close.input");
					attrClosedInput.getMetaData().setType(TaskAttribute.TYPE_SINGLE_SELECT);
					TaskOperation.applyTo(attrClosed, "tuleap.close", "Close as");
					attrClosed.getMetaData().putValue(TaskAttribute.META_ASSOCIATED_ATTRIBUTE_ID,
							"tuleap.close.input");
					for (String closedStatus : closedAccessibleStatus) {
						attrClosedInput.putOption(closedStatus, closedStatus);
					}
				}
			}
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
	 * @throws CoreException
	 *             If repository file configuration is not accessible
	 */
	public TaskData getTaskData(TaskRepository taskRepository, String taskId, IProgressMonitor monitor)
			throws CoreException {
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
	 * @throws CoreException
	 *             If repository file configuration is not accessible
	 */
	public TaskData downloadTaskData(TaskRepository taskRepository, int taskId, IProgressMonitor monitor)
			throws CoreException {
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
		// Create the default attributes
		TaskData taskData = new TaskData(this.getAttributeMapper(taskRepository),
				ITuleapConstants.CONNECTOR_KIND, taskRepository.getRepositoryUrl(), Integer.valueOf(
						tuleapArtifact.getId()).toString());

		tuleapClient.updateAttributes(monitor, false);
		this.createDefaultAttributes(taskData, tuleapClient, false);
		// TODO Compute the status of the tuleap artifact
		this.createOperations(taskData, tuleapClient, "Open");

		// Convert Tuleap artifact to Mylyn task data
		Set<String> keys = tuleapArtifact.getKeys();
		for (String key : keys) {
			String value = tuleapArtifact.getValue(key);
			TaskAttribute attribute = taskData.getRoot();
			Map<String, TaskAttribute> attributes = attribute.getAttributes();
			TaskAttribute taskAttribute = attributes.get(key);
			if (taskAttribute != null) {
				taskAttribute.setValue(value);
			}
		}
		return taskData;
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
