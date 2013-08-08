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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryPerson;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.RepositoryResponse;
import org.eclipse.mylyn.tasks.core.RepositoryResponse.ResponseKind;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler;
import org.eclipse.mylyn.tasks.core.data.TaskAttachmentMapper;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMetaData;
import org.eclipse.mylyn.tasks.core.data.TaskCommentMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskMapper;
import org.eclipse.mylyn.tasks.core.data.TaskOperation;
import org.tuleap.mylyn.task.internal.core.client.ITuleapClient;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.TuleapArtifactComment;
import org.tuleap.mylyn.task.internal.core.model.TuleapAttachment;
import org.tuleap.mylyn.task.internal.core.model.TuleapInstanceConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapPerson;
import org.tuleap.mylyn.task.internal.core.model.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapComputedValue;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapDate;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFileUpload;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapMultiSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.model.workflow.TuleapWorkflow;
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
	 * The label "contributors".
	 */
	private final String personContributorsLabel = TuleapMylynTasksMessages
			.getString("TuleapTaskDataHandler.AssignedToLabel"); //$NON-NLS-1$

	/**
	 * The Tuleap repository connector.
	 */
	private ITuleapRepositoryConnector connector;

	/**
	 * A cache for the repository persons available.
	 */
	private Map<String, IRepositoryPerson> email2person = new HashMap<String, IRepositoryPerson>();

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
		ITuleapClient client = this.connector.getClientManager().getClient(repository);
		if (taskData.isNew()) {
			String artifactId = client.createArtifact(artifact, monitor);
			response = new RepositoryResponse(ResponseKind.TASK_CREATED, artifactId);
		} else {
			client.updateArtifact(artifact, monitor);
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
		TuleapTaskMapper tuleapTaskMapper = new TuleapTaskMapper(taskData);

		String trackerName = tuleapTaskMapper.getTrackerName();
		int trackerId = tuleapTaskMapper.getTrackerId();

		String projectName = tuleapTaskMapper.getProjectName();

		// Create the artifact
		TuleapArtifact artifact = null;
		if (taskData.isNew()) {
			artifact = new TuleapArtifact();
			artifact.setTrackerId(trackerId);
		} else {
			int artifactId = Integer.valueOf(taskData.getTaskId()).intValue();
			artifact = new TuleapArtifact(artifactId, trackerId, trackerName, projectName);
		}

		Collection<TaskAttribute> attributes = taskData.getRoot().getAttributes().values();
		for (TaskAttribute taskAttribute : attributes) {
			if (taskAttribute.getId().startsWith(TaskAttribute.PREFIX_COMMENT)) {
				// Ignore comments since we won't resubmit old comments
			} else if (TaskAttribute.TYPE_TASK_DEPENDENCY.equals(taskAttribute.getMetaData().getType())) {
				// We need to convert the list of task dependencies to use only the id of the task.
				for (String value : taskAttribute.getValues()) {
					/*
					 * /!\HACKISH/!\ We may have, as the id of the task, an identifier (ie: 917) or a complex
					 * identifier (ie: MyRepository:MyProject[116] #917 - My Task Name). We will try to parse
					 * the value as an integer, if it fails, then we know that we have a complex identifier,
					 * in that case, we will parse the identifier from this complex identifier and use it.
					 */
					StringTokenizer stringTokenizer = new StringTokenizer(value, ","); //$NON-NLS-1$
					while (stringTokenizer.hasMoreTokens()) {
						String nextToken = stringTokenizer.nextToken().trim();
						try {
							Integer.valueOf(nextToken);

							// No exception raised -> we have an integer
							artifact.putValue(taskAttribute.getId(), nextToken);
						} catch (NumberFormatException e) {
							// We have a complex URL, that's what we are looking for! Let's convert it
							int index = nextToken.indexOf(ITuleapConstants.TASK_DATA_ID_SEPARATOR);
							if (index != -1) {
								String id = nextToken.substring(index
										+ ITuleapConstants.TASK_DATA_ID_SEPARATOR.length());
								artifact.putValue(taskAttribute.getId(), id);
							}
						}
					}
				}
			} else if (!TuleapAttributeMapper.isInternalAttribute(taskAttribute)) {
				// Ignore Tuleap internal attributes
				if (taskAttribute.getValues().size() > 1) {
					for (String value : taskAttribute.getValues()) {
						artifact.putValue(taskAttribute.getId(), value);
					}
				} else {
					artifact.putValue(taskAttribute.getId(), taskAttribute.getValue());
				}
			}
		}

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
		if (!taskData.isNew()) {
			return false;
		}

		boolean isInitialized = false;
		TuleapInstanceConfiguration repositoryConfiguration = connector.getRepositoryConfiguration(
				repository, false, monitor);
		if (repositoryConfiguration != null) {
			// Update the available attributes for the tasks
			ITuleapClient tuleapClient = this.connector.getClientManager().getClient(repository);
			tuleapClient.updateAttributes(monitor, false);

			// Sets the creation date and last modification date.
			if (this.connector instanceof AbstractRepositoryConnector
					&& ((AbstractRepositoryConnector)this.connector).getTaskMapping(taskData) instanceof TaskMapper
					&& initializationData instanceof TuleapTaskMapping) {
				TuleapTaskMapping tuleapTaskMapping = (TuleapTaskMapping)initializationData;

				TuleapTrackerConfiguration trackerConfiguration = tuleapTaskMapping.getTracker();
				if (trackerConfiguration != null) {
					this.createDefaultAttributes(taskData, trackerConfiguration, false);

					TaskMapper taskMapper = new TaskMapper(taskData);
					taskMapper.setCreationDate(new Date());
					taskMapper.setModificationDate(new Date());
					taskMapper.setSummary(TuleapMylynTasksMessages.getString(
							"TuleapTaskDataHandler.DefaultNewTitle", trackerConfiguration.getItemName())); //$NON-NLS-1$

					// Set the tracker id
					taskData.getRoot().createAttribute(TaskAttribute.PRODUCT).addValue(
							Integer.valueOf(trackerConfiguration.getTrackerId()).toString());

					String status = taskMapper.getStatus();
					this.createOperations(taskData, trackerConfiguration, status);
					this.createPersons(taskData, tuleapClient, trackerConfiguration);

					isInitialized = true;
				}

			}
		}
		return isInitialized;
	}

	/**
	 * Creates the default Tuleap related attributes.
	 * 
	 * @param taskData
	 *            The task data
	 * @param configuration
	 *            The configuration of the tracker where the task will be created.
	 * @param existingTask
	 *            Indicates if we are manipulating a newly created task
	 */
	private void createDefaultAttributes(TaskData taskData, TuleapTrackerConfiguration configuration,
			boolean existingTask) {
		if (configuration == null) {
			return;
		}

		TuleapTaskMapper tuleapTaskMapper = new TuleapTaskMapper(taskData);

		// The kind of the task
		TaskAttribute attribute = taskData.getRoot().createAttribute(TaskAttribute.TASK_KIND);
		String name = configuration.getName();
		if (name != null) {
			attribute.setValue(name);
		} else {
			attribute.setValue(TuleapMylynTasksMessages
					.getString("TuleapTaskDataHandler.DefaultConfigurationName")); //$NON-NLS-1$
		}

		// The group id and the tracker id
		tuleapTaskMapper.setGroupId(configuration.getTuleapProjectConfiguration().getIdentifier());
		tuleapTaskMapper.setProjectName(configuration.getTuleapProjectConfiguration().getName());
		tuleapTaskMapper.setTrackerId(configuration.getTrackerId());
		tuleapTaskMapper.setTrackerName(configuration.getName());

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

		// Completion date
		attribute = taskData.getRoot().createAttribute(TaskAttribute.DATE_COMPLETION);
		metaData = attribute.getMetaData();
		metaData.setLabel(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.CompletionDate")); //$NON-NLS-1$
		metaData.setType(TaskAttribute.TYPE_DATE);

		// New comment
		attribute = taskData.getRoot().createAttribute(TaskAttribute.COMMENT_NEW);
		metaData = attribute.getMetaData();
		metaData.setLabel(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.NewComment")); //$NON-NLS-1$
		metaData.setType(TaskAttribute.TYPE_LONG_RICH_TEXT);

		// Default attributes
		Collection<AbstractTuleapField> fields = configuration.getFields();
		for (AbstractTuleapField abstractTuleapField : fields) {
			if (shouldCreateAttributeFor(abstractTuleapField)) {
				this.createAttribute(taskData, abstractTuleapField);
			}
		}
	}

	/**
	 * Indicates if we should create a mylyn attribute for the given tuleap field.
	 * 
	 * @param abstractTuleapField
	 *            the tuleap field
	 * @return <code>true</code> if we should create a Mylyn attribute for the Tuleap field,
	 *         <code>false</code> otherwise.
	 */
	private boolean shouldCreateAttributeFor(AbstractTuleapField abstractTuleapField) {
		boolean shouldCreateAttributeFor = !(abstractTuleapField instanceof TuleapFileUpload);
		return shouldCreateAttributeFor;
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
		TaskAttribute attribute = this.instantiateAttribute(taskData, tuleapField);
		TaskAttributeMetaData attributeMetaData = attribute.getMetaData();

		// Set the kind if we do not have a semantic field
		boolean isTitle = tuleapField instanceof TuleapString
				&& ((TuleapString)tuleapField).isSemanticTitle();
		boolean isStatus = tuleapField instanceof TuleapSelectBox
				&& ((TuleapSelectBox)tuleapField).isSemanticStatus()
				|| tuleapField instanceof TuleapMultiSelectBox
				&& ((TuleapMultiSelectBox)tuleapField).isSemanticStatus();
		boolean isContributor = tuleapField instanceof TuleapSelectBox
				&& ((TuleapSelectBox)tuleapField).isSemanticContributor()
				|| tuleapField instanceof TuleapMultiSelectBox
				&& ((TuleapMultiSelectBox)tuleapField).isSemanticContributor();

		if (!isTitle && !isStatus && !isContributor) {
			attributeMetaData.setKind(tuleapField.getMetadataKind());
		}

		if (!tuleapField.isSubmitable() && !tuleapField.isUpdatable()) {
			attributeMetaData.setReadOnly(true);
		}

		// Dynamic fields are read only
		if (tuleapField instanceof TuleapComputedValue) {
			attributeMetaData.setReadOnly(true);
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
		}

		// Possible values
		if (tuleapField instanceof TuleapSelectBox
				&& ((TuleapSelectBox)tuleapField).getWorkflow().getTransitions().size() > 0) {
			// The widget has a workflow

			TuleapSelectBox tuleapSelectBox = (TuleapSelectBox)tuleapField;
			TuleapSelectBoxItem item = null;
			String value = attribute.getValue();

			List<TuleapSelectBoxItem> items = tuleapSelectBox.getItems();
			for (TuleapSelectBoxItem tuleapSelectBoxItem : items) {
				if (value != null && value.equals(tuleapSelectBoxItem.getLabel())) {
					item = tuleapSelectBoxItem;
				}
			}

			if (item != null) {
				List<Integer> accessibleStates = tuleapSelectBox.getWorkflow().accessibleStates(
						item.getIdentifier());
				attribute.clearOptions();
				attribute.putOption(value, value);

				for (Integer accessibleState : accessibleStates) {
					for (TuleapSelectBoxItem tuleapSelectBoxItem : items) {
						if (accessibleState.intValue() == tuleapSelectBoxItem.getIdentifier()) {
							attribute.putOption(tuleapSelectBoxItem.getLabel(), tuleapSelectBoxItem
									.getLabel());
						}
					}
				}
			} else {
				// If we start from an empty state, we can go to the state for the item with the identifier 0.
				List<Integer> accessibleStates = tuleapSelectBox.getWorkflow().accessibleStates(
						ITuleapConstants.NEW_ARTIFACT_WORKFLOW_IDENTIFIER);
				attribute.clearOptions();

				for (Integer accessibleState : accessibleStates) {
					for (TuleapSelectBoxItem tuleapSelectBoxItem : items) {
						if (accessibleState.intValue() == tuleapSelectBoxItem.getIdentifier()) {
							attribute.putOption(tuleapSelectBoxItem.getLabel(), tuleapSelectBoxItem
									.getLabel());
						}
					}
				}
			}
		} else {
			Map<String, String> options = tuleapField.getOptions();
			for (Entry<String, String> entry : options.entrySet()) {
				attribute.putOption(entry.getKey(), entry.getValue());
			}
			if (tuleapField.getOptions().size() > 0) {
				attribute.putOption("", ""); //$NON-NLS-1$//$NON-NLS-2$
			}
		}
	}

	/**
	 * Instantiate the attribute.
	 * 
	 * @param taskData
	 *            The task data
	 * @param tuleapField
	 *            The tuleap field
	 * @return The attribute instantiated to represent the given tuleap field in the given task data.
	 */
	private TaskAttribute instantiateAttribute(TaskData taskData, AbstractTuleapField tuleapField) {
		TaskAttribute attribute = null;

		// Semantic support
		if (tuleapField instanceof TuleapString && ((TuleapString)tuleapField).isSemanticTitle()) {
			attribute = taskData.getRoot().createAttribute(TaskAttribute.SUMMARY);
			// Attributes
			TaskAttributeMetaData attributeMetadata = attribute.getMetaData();
			attributeMetadata.setType(tuleapField.getMetadataType());
			attributeMetadata.setLabel(tuleapField.getLabel());
		} else if (tuleapField instanceof TuleapSelectBox) {
			TuleapSelectBox selectBox = (TuleapSelectBox)tuleapField;
			if (selectBox.isSemanticStatus()) {
				// Create an attribute for the status
				attribute = taskData.getRoot().createAttribute(TaskAttribute.STATUS);
				TaskAttributeMetaData metaData = attribute.getMetaData();
				metaData.setLabel(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.Status")); //$NON-NLS-1$
				metaData.setType(TaskAttribute.TYPE_SINGLE_SELECT);
			} else if (selectBox.isSemanticContributor()) {
				// Create an attribute for the assigned person
				attribute = taskData.getRoot().createAttribute(TaskAttribute.USER_ASSIGNED);
				TaskAttributeMetaData metaData = attribute.getMetaData();
				metaData.setLabel(personContributorsLabel);
				metaData.setType(TaskAttribute.TYPE_SINGLE_SELECT);
				metaData.setKind(TaskAttribute.KIND_PEOPLE);
			} else {
				attribute = taskData.getRoot().createAttribute(
						Integer.valueOf(tuleapField.getIdentifier()).toString());
				// Attributes
				TaskAttributeMetaData attributeMetadata = attribute.getMetaData();
				attributeMetadata.setType(tuleapField.getMetadataType());
				attributeMetadata.setLabel(tuleapField.getLabel());
			}
		} else if (tuleapField instanceof TuleapMultiSelectBox) {
			TuleapMultiSelectBox tuleapMultiSelectBox = (TuleapMultiSelectBox)tuleapField;
			if (tuleapMultiSelectBox.isSemanticStatus()) {
				// Create an attribute for the assigned person
				attribute = taskData.getRoot().createAttribute(TaskAttribute.STATUS);
				TaskAttributeMetaData metaData = attribute.getMetaData();
				metaData.setLabel(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.Status")); //$NON-NLS-1$
				metaData.setType(TaskAttribute.TYPE_MULTI_SELECT);
			} else if (tuleapMultiSelectBox.isSemanticContributor()) {
				// Create an attribute for the assigned person
				attribute = taskData.getRoot().createAttribute(TaskAttribute.USER_ASSIGNED);
				TaskAttributeMetaData metaData = attribute.getMetaData();
				metaData.setLabel(personContributorsLabel);
				metaData.setType(TaskAttribute.TYPE_MULTI_SELECT);
				metaData.setKind(TaskAttribute.KIND_PEOPLE);
			} else {
				attribute = taskData.getRoot().createAttribute(
						Integer.valueOf(tuleapField.getIdentifier()).toString());
				// Attributes
				TaskAttributeMetaData attributeMetadata = attribute.getMetaData();
				attributeMetadata.setType(tuleapField.getMetadataType());
				attributeMetadata.setLabel(tuleapField.getLabel());
			}
		} else {
			attribute = taskData.getRoot().createAttribute(
					Integer.valueOf(tuleapField.getIdentifier()).toString());
			// Attributes
			TaskAttributeMetaData attributeMetadata = attribute.getMetaData();
			attributeMetadata.setType(tuleapField.getMetadataType());
			attributeMetadata.setLabel(tuleapField.getLabel());
		}

		return attribute;
	}

	/**
	 * Creates the operations available on the task data thanks to the configuration of the client while using
	 * the given current status.
	 * 
	 * @param taskData
	 *            The task data
	 * @param configuration
	 *            The configuration of the tracker on which the task will be created.
	 * @param currentStatus
	 *            The current status
	 */
	private void createOperations(TaskData taskData, TuleapTrackerConfiguration configuration,
			String currentStatus) {
		AbstractTuleapField statusSelectBox = null;

		// Create operations from the status semantic
		Collection<AbstractTuleapField> fields = configuration.getFields();
		for (AbstractTuleapField abstractTuleapField : fields) {
			if (abstractTuleapField instanceof TuleapSelectBox) {
				TuleapSelectBox selectBox = (TuleapSelectBox)abstractTuleapField;
				if (selectBox.isSemanticStatus()) {
					statusSelectBox = selectBox;
				}
			} else if (abstractTuleapField instanceof TuleapMultiSelectBox) {
				TuleapMultiSelectBox multiSelectBox = (TuleapMultiSelectBox)abstractTuleapField;
				if (multiSelectBox.isSemanticStatus()) {
					statusSelectBox = multiSelectBox;
				}
			}
		}

		if (statusSelectBox == null) {
			// shortcut
			return;
		}

		// Create the default attribute for the operation
		TaskAttribute operationAttribute = taskData.getRoot().getAttribute(TaskAttribute.OPERATION);
		if (operationAttribute == null) {
			operationAttribute = taskData.getRoot().createAttribute(TaskAttribute.OPERATION);
		}
		TaskOperation.applyTo(operationAttribute, TaskAttribute.STATUS, TuleapMylynTasksMessages
				.getString("TuleapTaskDataHandler.MarkAs")); //$NON-NLS-1$

		List<String> tuleapStatus = new ArrayList<String>();

		TuleapWorkflow workflow = null;
		List<TuleapSelectBoxItem> items = new ArrayList<TuleapSelectBoxItem>();
		if (statusSelectBox instanceof TuleapSelectBox) {
			workflow = ((TuleapSelectBox)statusSelectBox).getWorkflow();
			items.addAll(((TuleapSelectBox)statusSelectBox).getItems());
		}
		if (workflow != null && workflow.getTransitions().size() > 0 && currentStatus != null
				&& currentStatus.length() > 0) {
			for (TuleapSelectBoxItem tuleapSelectBoxItem : items) {
				if (tuleapSelectBoxItem.getLabel().equals(currentStatus)) {
					// Only support the reachable state from the current status
					List<Integer> accessibleStates = workflow.accessibleStates(tuleapSelectBoxItem
							.getIdentifier());
					for (Integer accessibleState : accessibleStates) {
						for (TuleapSelectBoxItem item : items) {
							if (item.getIdentifier() == accessibleState.intValue()) {
								tuleapStatus.add(item.getLabel());
							}
						}
					}
				}
			}
		} else {
			for (TuleapSelectBoxItem tuleapSelectBoxItem : items) {
				tuleapStatus.add(tuleapSelectBoxItem.getLabel());
			}
		}

		TaskAttribute attrResolvedInput = taskData.getRoot().createAttribute(
				TaskAttribute.PREFIX_OPERATION + TaskAttribute.STATUS);
		if (statusSelectBox instanceof TuleapSelectBox) {
			attrResolvedInput.getMetaData().setType(TaskAttribute.TYPE_SINGLE_SELECT);
		} else if (statusSelectBox instanceof TuleapMultiSelectBox) {
			attrResolvedInput.getMetaData().setType(TaskAttribute.TYPE_MULTI_SELECT);
		}
		attrResolvedInput.getMetaData().setKind(TaskAttribute.KIND_OPERATION);
		for (String status : tuleapStatus) {
			attrResolvedInput.putOption(status, status);
		}
		TaskOperation.applyTo(attrResolvedInput, TaskAttribute.STATUS, TuleapMylynTasksMessages
				.getString("TuleapTaskDataHandler.MarkAs")); //$NON-NLS-1$

		attrResolvedInput.getMetaData().putValue(TaskAttribute.META_ASSOCIATED_ATTRIBUTE_ID,
				TaskAttribute.STATUS);
	}

	/**
	 * Creates the attributes to manage the persons to which the task is assigned.
	 * 
	 * @param taskData
	 *            The task data
	 * @param tuleapClient
	 *            The tuleap client used to get the configuration of the tracker
	 * @param configuration
	 *            The configuration of the tracker on which the task is created
	 */
	private void createPersons(TaskData taskData, ITuleapClient tuleapClient,
			TuleapTrackerConfiguration configuration) {
		AbstractTuleapField personsSelectBox = null;

		Collection<AbstractTuleapField> fields = configuration.getFields();
		for (AbstractTuleapField abstractTuleapField : fields) {
			if (abstractTuleapField instanceof TuleapSelectBox) {
				TuleapSelectBox selectBox = (TuleapSelectBox)abstractTuleapField;
				if (selectBox.isSemanticContributor()) {
					personsSelectBox = selectBox;
				}
			} else if (abstractTuleapField instanceof TuleapMultiSelectBox) {
				TuleapMultiSelectBox selectBox = (TuleapMultiSelectBox)abstractTuleapField;
				if (selectBox.isSemanticContributor()) {
					personsSelectBox = selectBox;
				}
			}
		}

		if (personsSelectBox instanceof TuleapSelectBox) {
			// Only one possible assignee
			TuleapSelectBox tuleapSelectBox = (TuleapSelectBox)personsSelectBox;

			TaskAttribute attribute = taskData.getRoot().createAttribute(TaskAttribute.USER_ASSIGNED);
			TaskAttributeMetaData metaData = attribute.getMetaData();
			metaData.setKind(TaskAttribute.KIND_PEOPLE);
			metaData.setType(TaskAttribute.TYPE_SINGLE_SELECT);
			metaData.setLabel(personContributorsLabel);

			List<TuleapSelectBoxItem> items = tuleapSelectBox.getItems();
			for (TuleapSelectBoxItem tuleapSelectBoxItem : items) {
				attribute.putOption(tuleapSelectBoxItem.getLabel(), tuleapSelectBoxItem.getLabel());
			}
			if (attribute.getOptions().size() > 0) {
				attribute.putOption("", ""); //$NON-NLS-1$//$NON-NLS-2$
			}
		} else if (personsSelectBox instanceof TuleapMultiSelectBox) {
			// Multiple assignee supported
			TuleapMultiSelectBox tuleapSelectBox = (TuleapMultiSelectBox)personsSelectBox;

			TaskAttribute attribute = taskData.getRoot().createAttribute(TaskAttribute.USER_ASSIGNED);
			TaskAttributeMetaData metaData = attribute.getMetaData();
			metaData.setKind(TaskAttribute.KIND_PEOPLE);
			metaData.setType(TaskAttribute.TYPE_MULTI_SELECT);
			metaData.setLabel(personContributorsLabel);

			List<TuleapSelectBoxItem> items = tuleapSelectBox.getItems();
			for (TuleapSelectBoxItem tuleapSelectBoxItem : items) {
				attribute.putOption(tuleapSelectBoxItem.getLabel(), tuleapSelectBoxItem.getLabel());
			}
			if (attribute.getOptions().size() > 0) {
				attribute.putOption("", ""); //$NON-NLS-1$//$NON-NLS-2$
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
		return downloadTaskData(taskRepository, taskId, monitor);
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
	public TaskData downloadTaskData(TaskRepository taskRepository, String taskId, IProgressMonitor monitor)
			throws CoreException {
		ITuleapClient tuleapClient = this.connector.getClientManager().getClient(taskRepository);

		tuleapClient.updateAttributes(monitor, false);
		TuleapArtifact tuleapArtifact = tuleapClient.getArtifact(taskId, monitor);
		if (tuleapArtifact != null) {
			TaskData taskData = this.createTaskDataFromArtifact(tuleapClient, taskRepository, tuleapArtifact,
					monitor);
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
	public TaskData createTaskDataFromArtifact(ITuleapClient tuleapClient, TaskRepository taskRepository,
			TuleapArtifact tuleapArtifact, IProgressMonitor monitor) {
		// Create the default attributes
		TaskData taskData = new TaskData(this.getAttributeMapper(taskRepository),
				ITuleapConstants.CONNECTOR_KIND, taskRepository.getRepositoryUrl(), String
						.valueOf(tuleapArtifact.getId()));

		TaskAttribute taskKey = taskData.getRoot().createAttribute(TaskAttribute.TASK_KEY);
		taskKey.setValue(tuleapArtifact.getUniqueName());
		taskKey.getMetaData().setType(TaskAttribute.TYPE_SHORT_TEXT);
		taskKey.getMetaData().setReadOnly(true);

		// Structure
		tuleapClient.updateAttributes(monitor, false);
		TuleapInstanceConfiguration repositoryConfiguration = tuleapClient.getRepositoryConfiguration();
		TuleapTrackerConfiguration trackerConfiguration = repositoryConfiguration
				.getTrackerConfiguration(tuleapArtifact.getTrackerId());
		this.createDefaultAttributes(taskData, trackerConfiguration, false);
		this.createOperations(taskData, trackerConfiguration, tuleapArtifact.getValue(TaskAttribute.STATUS));

		// Date
		TaskMapper taskMapper = new TaskMapper(taskData);
		taskMapper.setCreationDate(tuleapArtifact.getCreationDate());
		taskMapper.setModificationDate(tuleapArtifact.getLastModificationDate());

		// URL
		if (this.connector instanceof AbstractRepositoryConnector) {
			AbstractRepositoryConnector abstractRepositoryConnector = (AbstractRepositoryConnector)this.connector;
			String taskUrl = abstractRepositoryConnector.getTaskUrl(taskRepository.getRepositoryUrl(),
					Integer.valueOf(tuleapArtifact.getId()).toString());
			taskMapper.setTaskUrl(taskUrl);
		}

		// Task key
		taskMapper.setTaskKey(Integer.valueOf(tuleapArtifact.getId()).toString());

		// Comments
		taskData = this.populateComments(tuleapArtifact, taskData);

		// Attachments
		taskData = this.populateAttachments(tuleapArtifact, taskData);

		Collection<AbstractTuleapField> fields = trackerConfiguration.getFields();
		for (AbstractTuleapField abstractTuleapField : fields) {
			if (abstractTuleapField instanceof TuleapString
					&& ((TuleapString)abstractTuleapField).isSemanticTitle()) {
				// Look for the summary
				String value = tuleapArtifact.getValue(abstractTuleapField.getName());
				if (value != null) {
					taskMapper.setSummary(value);
				}
			} else if (abstractTuleapField instanceof TuleapSelectBox
					&& ((TuleapSelectBox)abstractTuleapField).isSemanticStatus()) {
				// Look for the status
				this.createTaskDataStatusFromArtifact(tuleapArtifact, taskMapper, abstractTuleapField);
			} else if (abstractTuleapField instanceof TuleapSelectBox
					&& ((TuleapSelectBox)abstractTuleapField).isSemanticContributor()) {
				// Look for the contributors in the select box
				this.createTaskDataContributorsFromSelectBox(tuleapArtifact, taskData,
						(TuleapSelectBox)abstractTuleapField);
			} else if (abstractTuleapField instanceof TuleapMultiSelectBox
					&& ((TuleapMultiSelectBox)abstractTuleapField).isSemanticStatus()) {
				this.createTaskDataStatusFromArtifact(tuleapArtifact, taskMapper, abstractTuleapField);
			} else if (abstractTuleapField instanceof TuleapMultiSelectBox
					&& ((TuleapMultiSelectBox)abstractTuleapField).isSemanticContributor()) {
				// Look for the contributors in the multi select box
				this.createTaskDataContributorsFromMultiSelectbox(tuleapArtifact, taskData,
						(TuleapMultiSelectBox)abstractTuleapField);
			} else if (abstractTuleapField instanceof TuleapDate) {
				// Look for the date
				this.createTaskDataDateFromArtifact(tuleapArtifact, taskData, (TuleapDate)abstractTuleapField);
			} else {
				// Other fields
				List<String> values = tuleapArtifact.getValues(abstractTuleapField.getName());

				TaskAttribute attribute = taskData.getRoot();
				Map<String, TaskAttribute> attributes = attribute.getAttributes();
				TaskAttribute taskAttribute = attributes.get(Integer.valueOf(
						abstractTuleapField.getIdentifier()).toString());
				if (taskAttribute != null && values != null) {
					taskAttribute.setValues(values);
				}
			}

			// Change the options if the select box has a workflow
			if (abstractTuleapField instanceof TuleapSelectBox
					&& ((TuleapSelectBox)abstractTuleapField).getWorkflow().getTransitions().size() > 0) {
				this.changeOptionsForSelectBoxWorkflow(taskData, (TuleapSelectBox)abstractTuleapField);
			}
		}

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
	private void createTaskDataStatusFromArtifact(TuleapArtifact tuleapArtifact, TaskMapper taskMapper,
			AbstractTuleapField abstractTuleapField) {
		// Look for the status
		String status = tuleapArtifact.getValue(abstractTuleapField.getName());
		if (status != null) {
			taskMapper.setStatus(status);
		}

		// If the status matches a "closed" status, set the completion date to the last modification
		// date or the current date
		boolean isClosed = false;
		List<TuleapSelectBoxItem> closedStatus = new ArrayList<TuleapSelectBoxItem>();
		if (abstractTuleapField instanceof TuleapSelectBox) {
			closedStatus.addAll(((TuleapSelectBox)abstractTuleapField).getClosedStatus());
		} else if (abstractTuleapField instanceof TuleapMultiSelectBox) {
			closedStatus.addAll(((TuleapMultiSelectBox)abstractTuleapField).getClosedStatus());
		}
		for (TuleapSelectBoxItem tuleapSelectBoxItem : closedStatus) {
			if (tuleapSelectBoxItem.getLabel().equals(status)) {
				isClosed = true;
				break;
			}
		}
		if (isClosed) {
			// Sets the completion date
			Date lastModificationDate = tuleapArtifact.getLastModificationDate();
			if (lastModificationDate != null) {
				taskMapper.setCompletionDate(lastModificationDate);
			} else {
				taskMapper.setCompletionDate(new Date());
			}
		} else {
			// Remove an existing completion date
			taskMapper.setCompletionDate(null);
		}
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
	private void createTaskDataContributorsFromSelectBox(TuleapArtifact tuleapArtifact, TaskData taskData,
			TuleapSelectBox tuleapSelectBox) {
		TaskAttribute taskAttribute = taskData.getRoot().getAttributes().get(TaskAttribute.USER_ASSIGNED);
		if (taskAttribute == null) {
			taskAttribute = taskData.getRoot().createAttribute(TaskAttribute.USER_ASSIGNED);
			TaskAttributeMetaData metaData = taskAttribute.getMetaData();
			metaData.setKind(TaskAttribute.KIND_PEOPLE);
			metaData.setType(TaskAttribute.TYPE_MULTI_SELECT);
			metaData.setLabel(personContributorsLabel);
		}
		// Put the options
		List<TuleapSelectBoxItem> items = tuleapSelectBox.getItems();
		for (TuleapSelectBoxItem tuleapSelectBoxItem : items) {
			taskAttribute.putOption(tuleapSelectBoxItem.getLabel(), tuleapSelectBoxItem.getLabel());
		}
		// Adds the values from the retrieved artifact
		List<String> values = tuleapArtifact.getValues(tuleapSelectBox.getName());
		taskAttribute.setValues(values);
	}

	/**
	 * Creates the contributors in the task data matching the information retrieved from the Tuleap artifact.
	 * 
	 * @param tuleapArtifact
	 *            The Tuleap artifact
	 * @param taskData
	 *            The task data
	 * @param tuleapMultiSelectBox
	 *            The Tuleap multi select box
	 */
	private void createTaskDataContributorsFromMultiSelectbox(TuleapArtifact tuleapArtifact,
			TaskData taskData, TuleapMultiSelectBox tuleapMultiSelectBox) {
		TaskAttribute taskAttribute = taskData.getRoot().getAttributes().get(TaskAttribute.USER_ASSIGNED);
		if (taskAttribute == null) {
			taskAttribute = taskData.getRoot().createAttribute(TaskAttribute.USER_ASSIGNED);
			TaskAttributeMetaData metaData = taskAttribute.getMetaData();
			metaData.setKind(TaskAttribute.KIND_PEOPLE);
			metaData.setType(TaskAttribute.TYPE_MULTI_SELECT);
			metaData.setLabel(personContributorsLabel);
		}
		// Put the options
		List<TuleapSelectBoxItem> items = tuleapMultiSelectBox.getItems();
		for (TuleapSelectBoxItem tuleapSelectBoxItem : items) {
			taskAttribute.putOption(tuleapSelectBoxItem.getLabel(), tuleapSelectBoxItem.getLabel());
		}
		// Adds the values from the retrieved artifact
		List<String> values = tuleapArtifact.getValues(tuleapMultiSelectBox.getName());
		taskAttribute.setValues(values);
	}

	/**
	 * Creates the date in the task data matching the information retrieved from the Tuleap artifact.
	 * 
	 * @param tuleapArtifact
	 *            The Tuleap artifact
	 * @param taskData
	 *            The task data
	 * @param tuleapDate
	 *            The Tuleap field representing the date
	 */
	private void createTaskDataDateFromArtifact(TuleapArtifact tuleapArtifact, TaskData taskData,
			TuleapDate tuleapDate) {
		// Date need to have their timestamp converted
		String value = tuleapArtifact.getValue(tuleapDate.getName());

		TaskAttribute attribute = taskData.getRoot();
		Map<String, TaskAttribute> attributes = attribute.getAttributes();
		TaskAttribute taskAttribute = attributes.get(Integer.valueOf(tuleapDate.getIdentifier()).toString());
		if (taskAttribute != null && value != null) {
			try {
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(Long.valueOf(value).longValue() * 1000);
				taskAttribute.setValue(Long.valueOf(calendar.getTimeInMillis()).toString());
			} catch (NumberFormatException e) {
				// The date is empty
			}
		}
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
		TuleapSelectBoxItem item = null;

		TaskAttribute attribute = null;
		if (tuleapSelectBox.isSemanticStatus()) {
			attribute = taskData.getRoot().getAttribute(TaskAttribute.STATUS);
		} else {
			attribute = taskData.getRoot().getAttribute(
					Integer.valueOf(tuleapSelectBox.getIdentifier()).toString());
		}

		String value = attribute.getValue();

		List<TuleapSelectBoxItem> items = tuleapSelectBox.getItems();
		for (TuleapSelectBoxItem tuleapSelectBoxItem : items) {
			if (value != null && value.equals(tuleapSelectBoxItem.getLabel())) {
				item = tuleapSelectBoxItem;
			}
		}

		if (item != null) {
			attribute.clearOptions();
			attribute.putOption(value, value);

			List<Integer> accessibleStates = tuleapSelectBox.getWorkflow().accessibleStates(
					item.getIdentifier());
			for (Integer accessibleState : accessibleStates) {
				for (TuleapSelectBoxItem tuleapSelectBoxItem : items) {
					if (accessibleState.intValue() == tuleapSelectBoxItem.getIdentifier()) {
						attribute.putOption(tuleapSelectBoxItem.getLabel(), tuleapSelectBoxItem.getLabel());
					}
				}
			}
		}
	}

	/**
	 * Populates the attachments of the task data thanks to the attachments from the Tuleap artifact.
	 * 
	 * @param tuleapArtifact
	 *            The Tuleap artifact
	 * @param taskData
	 *            The task data
	 * @return The task data
	 */
	private TaskData populateAttachments(TuleapArtifact tuleapArtifact, TaskData taskData) {
		Set<Entry<String, List<TuleapAttachment>>> attachments = tuleapArtifact.getAttachments();
		for (Entry<String, List<TuleapAttachment>> entry : attachments) {
			String tuleapFieldName = entry.getKey();

			List<TuleapAttachment> attachmentsList = entry.getValue();
			for (TuleapAttachment tuleapAttachment : attachmentsList) {
				TaskAttribute attribute = taskData.getRoot().createAttribute(
						TaskAttribute.PREFIX_ATTACHMENT + tuleapFieldName + "---" + tuleapAttachment.getId()); //$NON-NLS-1$
				attribute.getMetaData().defaults().setType(TaskAttribute.TYPE_ATTACHMENT);
				TaskAttachmentMapper taskAttachment = TaskAttachmentMapper.createFrom(attribute);
				taskAttachment.setAttachmentId(tuleapAttachment.getId());

				TuleapPerson person = tuleapAttachment.getPerson();
				IRepositoryPerson iRepositoryPerson = email2person.get(person.getId());
				if (iRepositoryPerson == null) {
					iRepositoryPerson = taskData.getAttributeMapper().getTaskRepository().createPerson(
							person.getEmail());
					iRepositoryPerson.setName(person.getRealName());
					email2person.put(person.getEmail(), iRepositoryPerson);
				}
				taskAttachment.setAuthor(iRepositoryPerson);
				taskAttachment.setFileName(tuleapAttachment.getFilename());
				taskAttachment.setLength(tuleapAttachment.getSize());
				taskAttachment.setDescription(tuleapAttachment.getDescription());
				taskAttachment.setContentType(tuleapAttachment.getContentType());
				taskAttachment.applyTo(attribute);
			}
		}
		return taskData;
	}

	/**
	 * Populate the comments of the task data thanks to the comment from the Tuleap artifact.
	 * 
	 * @param tuleapArtifact
	 *            The Tuleap artifact
	 * @param taskData
	 *            The task data
	 * @return The task data
	 */
	private TaskData populateComments(TuleapArtifact tuleapArtifact, TaskData taskData) {
		int count = 0;
		List<TuleapArtifactComment> comments = tuleapArtifact.getComments();
		for (TuleapArtifactComment tuleapArtifactComment : comments) {
			TaskAttribute attribute = taskData.getRoot().createAttribute(
					TaskAttribute.PREFIX_COMMENT + Integer.valueOf(count).toString());
			attribute.getMetaData().defaults().setReadOnly(true).setType(TaskAttribute.TYPE_COMMENT);
			attribute.getMetaData().putValue(TaskAttribute.META_ASSOCIATED_ATTRIBUTE_ID,
					TaskAttribute.COMMENT_TEXT);
			TaskCommentMapper taskComment = TaskCommentMapper.createFrom(attribute);

			taskComment.setCommentId(Integer.valueOf(count).toString());
			taskComment.setNumber(Integer.valueOf(count));
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(Long.valueOf(tuleapArtifactComment.getSubmittedOn()).longValue() * 1000);
			Date creationDate = calendar.getTime();
			taskComment.setCreationDate(creationDate);
			taskComment.setText(tuleapArtifactComment.getBody());
			if (tuleapArtifactComment.getEmail() != null) {
				IRepositoryPerson iRepositoryPerson = email2person.get(tuleapArtifactComment.getEmail());
				if (iRepositoryPerson == null) {
					iRepositoryPerson = taskData.getAttributeMapper().getTaskRepository().createPerson(
							tuleapArtifactComment.getEmail());
					iRepositoryPerson.setName(tuleapArtifactComment.getName());
					email2person.put(tuleapArtifactComment.getEmail(), iRepositoryPerson);
				}
				taskComment.setAuthor(iRepositoryPerson);
			}
			taskComment.applyTo(attribute);
			count++;
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
