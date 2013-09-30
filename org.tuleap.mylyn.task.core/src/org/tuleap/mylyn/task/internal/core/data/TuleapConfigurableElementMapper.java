/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.internal.core.data;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.mylyn.tasks.core.IRepositoryPerson;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttachmentMapper;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMetaData;
import org.eclipse.mylyn.tasks.core.data.TaskCommentMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskMapper;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableFieldsConfiguration;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.TuleapElementComment;
import org.tuleap.mylyn.task.internal.core.model.TuleapPerson;
import org.tuleap.mylyn.task.internal.core.model.field.AbstractTuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;

/**
 * The Tuleap Configurable Element Mapper will be used to manipulate the task data model from Mylyn with a
 * higher level of abstraction.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapConfigurableElementMapper extends AbstractTaskMapper {
	/**
	 * The identifier of an invalid configuration.
	 */
	public static final int INVALID_CONFIGURATION_ID = -1;

	/**
	 * The identifier of an invalid project.
	 */
	public static final int INVALID_PROJECT_ID = -1;

	/**
	 * The identifier of the configuration id task attribute.
	 */
	public static final String CONFIGURATION_ID = "mtc_configuration_id"; //$NON-NLS-1$

	/**
	 * The identifier of the project name task attribute.
	 */
	public static final String PROJECT_ID = "mtc_project_id"; //$NON-NLS-1$

	/**
	 * List of attribute Ids that need not be sent to the server for update.
	 */
	private static final List<String> ATTRIBUTE_IDS_NOT_TO_SEND = Collections.unmodifiableList(Arrays.asList(
			TaskAttribute.DATE_COMPLETION, TaskAttribute.DATE_CREATION, TaskAttribute.DATE_MODIFICATION,
			TaskAttribute.PREFIX_OPERATION + TaskAttribute.STATUS, TaskAttribute.TASK_KEY,
			TaskAttribute.TASK_KIND, CONFIGURATION_ID, PROJECT_ID));

	/**
	 * The configuration.
	 */
	protected final AbstractTuleapConfigurableFieldsConfiguration tuleapConfigurableFieldsConfiguration;

	/**
	 * The constructor.
	 * 
	 * @param taskData
	 *            The task data
	 * @param tuleapConfigurableFieldsConfiguration
	 *            The configuration.
	 */
	public TuleapConfigurableElementMapper(TaskData taskData,
			AbstractTuleapConfigurableFieldsConfiguration tuleapConfigurableFieldsConfiguration) {
		super(taskData);
		this.tuleapConfigurableFieldsConfiguration = tuleapConfigurableFieldsConfiguration;
	}

	/**
	 * Initialize an empty task data from the given configuration.
	 */
	public void initializeEmptyTaskData() {
		createTaskKindTaskAttribute();

		// The project id and the configuration id
		setProjectId(tuleapConfigurableFieldsConfiguration.getTuleapProjectConfiguration().getIdentifier());
		setConfigurationId(tuleapConfigurableFieldsConfiguration.getIdentifier());

		createCreationDateTaskAttribute();
		createLastUpdateDateTaskAttribute();
		createCompletionDateTaskAttribute();
		createNewCommentTaskAttribute();
		createSubmittedByTaskAttribute();
		createTaskKeyAttribute();

		// Default attributes
		TaskAttribute root = taskData.getRoot();
		Collection<AbstractTuleapField> fields = tuleapConfigurableFieldsConfiguration.getFields();
		for (AbstractTuleapField abstractTuleapField : fields) {
			if (abstractTuleapField.needsTaskAttributeForInitialization()) {
				abstractTuleapField.createTaskAttribute(root);
			}
		}
		// call all the other private method (createXXXX)
		// keep an eye on the permissions -> read only in the metadata
	}

	/**
	 * Creates the read-only task attribute representing the task key.
	 */
	private void createTaskKeyAttribute() {
		TaskAttribute attribute = taskData.getRoot().createMappedAttribute(TaskAttribute.TASK_KEY);
		TaskAttributeMetaData metaData = attribute.getMetaData();
		metaData.setType(TaskAttribute.TYPE_SHORT_TEXT);
		metaData.setReadOnly(true);
	}

	/**
	 * Creates the task attribute representing the creation date.
	 */
	private void createCreationDateTaskAttribute() {
		TaskAttribute attribute = taskData.getRoot().createMappedAttribute(TaskAttribute.DATE_CREATION);
		TaskAttributeMetaData metaData = attribute.getMetaData();
		metaData.setLabel(TuleapMylynTasksMessages.getString("TuleapConfigurableElementMapper.CreationDate")); //$NON-NLS-1$
		metaData.setType(TaskAttribute.TYPE_DATE);
	}

	/**
	 * Creates the task attribute representing the last update date.
	 */
	private void createLastUpdateDateTaskAttribute() {
		TaskAttribute attribute = taskData.getRoot().createMappedAttribute(TaskAttribute.DATE_MODIFICATION);
		TaskAttributeMetaData metaData = attribute.getMetaData();
		metaData.setLabel(TuleapMylynTasksMessages
				.getString("TuleapConfigurableElementMapper.LastModificationDate")); //$NON-NLS-1$
		metaData.setType(TaskAttribute.TYPE_DATE);
	}

	/**
	 * Creates the task attribute representing the author of the task.
	 */
	private void createSubmittedByTaskAttribute() {
		TaskAttribute attribute = taskData.getRoot().createMappedAttribute(TaskAttribute.USER_REPORTER);
		TaskAttributeMetaData metaData = attribute.getMetaData();
		metaData.setType(TaskAttribute.TYPE_PERSON);
		metaData.setReadOnly(true);
	}

	/**
	 * Creates the task attribute representing the completion date.
	 */
	private void createCompletionDateTaskAttribute() {
		TaskAttribute attribute = taskData.getRoot().createMappedAttribute(TaskAttribute.DATE_COMPLETION);
		TaskAttributeMetaData metaData = attribute.getMetaData();
		metaData.setLabel(TuleapMylynTasksMessages
				.getString("TuleapConfigurableElementMapper.CompletionDate")); //$NON-NLS-1$
		metaData.setType(TaskAttribute.TYPE_DATE);
	}

	/**
	 * Creates the task attribute representing the new comment.
	 * 
	 * @return the newly created task attribute
	 */
	private TaskAttribute createNewCommentTaskAttribute() {
		TaskAttribute attribute = taskData.getRoot().createAttribute(TaskAttribute.COMMENT_NEW);
		TaskAttributeMetaData metaData = attribute.getMetaData();
		metaData.setLabel(TuleapMylynTasksMessages.getString("TuleapConfigurableElementMapper.NewComment")); //$NON-NLS-1$
		metaData.setType(TaskAttribute.TYPE_LONG_RICH_TEXT);
		return attribute;
	}

	/**
	 * Creates the task attribute representing the task kind.
	 */
	private void createTaskKindTaskAttribute() {
		TaskAttribute attribute = taskData.getRoot().createAttribute(TaskAttribute.TASK_KIND);
		String name = tuleapConfigurableFieldsConfiguration.getName();
		if (name != null) {
			attribute.setValue(name);
		} else {
			attribute.setValue(TuleapMylynTasksMessages
					.getString("TuleapConfigurableElementMapper.DefaultConfigurationName")); //$NON-NLS-1$
		}
	}

	/**
	 * Add an attachment.
	 * 
	 * @param tuleapFieldName
	 *            The tuleap field name
	 * @param tuleapAttachment
	 *            The attachment
	 */
	public void addAttachment(String tuleapFieldName, AttachmentValue tuleapAttachment) {
		TaskAttribute attribute = taskData.getRoot().createAttribute(
				TaskAttribute.PREFIX_ATTACHMENT + tuleapFieldName + "---" //$NON-NLS-1$
						+ tuleapAttachment.getAttachmentId());
		attribute.getMetaData().defaults().setType(TaskAttribute.TYPE_ATTACHMENT);
		TaskAttachmentMapper taskAttachment = TaskAttachmentMapper.createFrom(attribute);
		taskAttachment.setAttachmentId(tuleapAttachment.getAttachmentId());

		TuleapPerson person = tuleapAttachment.getPerson();
		IRepositoryPerson repositoryPerson = taskData.getAttributeMapper().getTaskRepository().createPerson(
				person.getEmail());
		repositoryPerson.setName(person.getUserName());
		taskAttachment.setAuthor(repositoryPerson);
		taskAttachment.setFileName(tuleapAttachment.getFilename());
		taskAttachment.setLength(Long.valueOf(tuleapAttachment.getSize()));
		taskAttachment.setDescription(tuleapAttachment.getDescription());
		taskAttachment.setContentType(tuleapAttachment.getContentType());
		taskAttachment.applyTo(attribute);
	}

	/**
	 * Sets the identifier of the configuration of the element (tracker id, backlog item type id, etc).
	 * 
	 * @param configurationId
	 *            The identifier of the configuration of the element
	 */
	private void setConfigurationId(int configurationId) {
		// should not appear in the attribute part so no task attribute type!
		TaskAttribute configurationIdAtt = taskData.getRoot().getMappedAttribute(CONFIGURATION_ID);
		if (configurationIdAtt == null) {
			configurationIdAtt = taskData.getRoot().createMappedAttribute(CONFIGURATION_ID);
		}
		taskData.getAttributeMapper().setIntegerValue(configurationIdAtt, Integer.valueOf(configurationId));
	}

	/**
	 * Returns the identifier of the configuration or INVALID_CONFIGURATION_ID otherwise.
	 * 
	 * @return The identifier of the configuration or INVALID_CONFIGURATION_ID otherwise.
	 */
	public int getConfigurationId() {
		TaskAttribute configurationIdAtt = taskData.getRoot().getMappedAttribute(CONFIGURATION_ID);
		if (configurationIdAtt == null) {
			return INVALID_CONFIGURATION_ID;
		}
		return taskData.getAttributeMapper().getIntegerValue(configurationIdAtt).intValue();
	}

	/**
	 * Sets the identifier of the project.
	 * 
	 * @param projectId
	 *            The identifier of the project
	 */
	private void setProjectId(int projectId) {
		// should not appear in the attribute part so no task attribute type!
		TaskAttribute projectIdAtt = taskData.getRoot().getMappedAttribute(PROJECT_ID);
		if (projectIdAtt == null) {
			projectIdAtt = taskData.getRoot().createMappedAttribute(PROJECT_ID);
		}
		taskData.getAttributeMapper().setIntegerValue(projectIdAtt, Integer.valueOf(projectId));
	}

	/**
	 * Returns the identifier of the project.
	 * 
	 * @return The identifier of the project
	 */
	public int getProjectId() {
		TaskAttribute projectIdAtt = taskData.getRoot().getMappedAttribute(PROJECT_ID);
		if (projectIdAtt == null) {
			return INVALID_PROJECT_ID;
		}
		return taskData.getAttributeMapper().getIntegerValue(projectIdAtt).intValue();
	}

	/**
	 * Sets the identifier of the artifact.
	 * 
	 * @param taskKey
	 *            The task key
	 */
	public void setTaskKey(String taskKey) {
		TaskAttribute taskKeyAtt = getMappedAttribute(TaskAttribute.TASK_KEY);
		taskKeyAtt.setValue(taskKey);
	}

	/**
	 * Returns the identifier of the artifact.
	 * 
	 * @return The identifier of the artifact
	 */
	public int getId() {
		// TODO SBE Test with a newly created artifact for potential number format exception
		// TODO SBE Be careful when the identifier will be changed
		return Integer.valueOf(taskData.getTaskId()).intValue();
	}

	/**
	 * Provides the wrapped task's key.
	 * 
	 * @return The task key or null if it cannot be found.
	 */
	public String getTaskKey() {
		TaskAttribute taskKey = getMappedAttribute(TaskAttribute.TASK_KEY);
		return taskKey.getValue();
	}

	/**
	 * Sets the url of the wrapped task.
	 * 
	 * @param url
	 *            the url of the task
	 */
	public void setTaskUrl(String url) {
		TaskAttribute attribute = getWriteableAttribute(TaskAttribute.TASK_URL, TaskAttribute.TYPE_URL);
		taskData.getAttributeMapper().setValue(attribute, url);
	}

	/**
	 * Provides the wrapped task's URL.
	 * 
	 * @return The wrapped task's URL
	 */
	public String getTaskUrl() {
		TaskAttribute attribute = getMappedAttribute(TaskAttribute.TASK_URL);
		if (attribute != null) {
			return taskData.getAttributeMapper().getValue(attribute);
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * Sets the summary of the wrapped task.
	 * 
	 * @param value
	 *            The value of the summary
	 */
	public void setSummary(String value) {
		TaskAttribute attribute = taskData.getRoot().getMappedAttribute(TaskAttribute.SUMMARY);
		if (attribute != null) {
			taskData.getAttributeMapper().setValue(attribute, value);
		}
	}

	/**
	 * Sets the status of the task. If the task is then closed, its completion date is forced to the last
	 * modification date if it was not already present. On the contrary, if the task is not closed, its
	 * completion date is forced to {@code null}, since the closed semantic is managed by mylyn <i>via</i> the
	 * completion date presence.
	 * 
	 * @param value
	 *            The status value id
	 */
	public void setStatus(String value) {
		// Sets the value of the read-only field and update the selectbox on which an operation is bound
		TaskAttribute attribute = getStatusTaskAttribute();
		if (attribute != null) {
			attribute.clearValues();
			attribute.setValue(value);
			if (tuleapConfigurableFieldsConfiguration.hasClosedStatusMeaning(value)
					&& getCompletionDate() == null) {
				// Sets the completion date
				// Hypothesis: the last update date is up to date, which is reasonable since it appears early
				// in the JSON objects.
				Date modificationDate = getModificationDate();
				if (modificationDate != null) {
					setCompletionDate(modificationDate);
				} else {
					setCompletionDate(new Date());
				}
			} else {
				// Remove an existing completion date
				setCompletionDate(null);
			}
			updateStatusSelectBox(value, attribute);
		}
	}

	/**
	 * Update the associated select box according to the workflow if there is a workflow.
	 * 
	 * @param value
	 *            The new status value
	 * @param attribute
	 *            the task attribute to update
	 */
	private void updateStatusSelectBox(String value, TaskAttribute attribute) {
		// update the options of the status field
		AbstractTuleapSelectBox field = tuleapConfigurableFieldsConfiguration.getStatusField();
		if (field instanceof TuleapSelectBox) {
			TuleapSelectBox selectBox = (TuleapSelectBox)field;
			if (selectBox.hasWorkflow()) {
				String currentOption = attribute.getOption(value);
				attribute.clearOptions();
				if (currentOption != null
						&& !String.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID).equals(value)) {
					// currentOption can only be null if the workflow forbids the current modification...
					attribute.putOption(value, currentOption);
				}
				Collection<TuleapSelectBoxItem> accessibleStates = selectBox.getWorkflow().accessibleStates(
						Integer.parseInt(value));
				for (TuleapSelectBoxItem item : accessibleStates) {
					attribute.putOption(String.valueOf(item.getIdentifier()), item.getLabel());
				}
			}
		}
	}

	/**
	 * Provides access to the completion date if it exists.
	 * 
	 * @return The completion date if it exists or null.
	 */
	public Date getCompletionDate() {
		TaskAttribute completionDateAttribute = getMappedAttribute(TaskAttribute.DATE_COMPLETION);
		if (completionDateAttribute != null) {
			return taskData.getAttributeMapper().getDateValue(completionDateAttribute);
		}
		return null;
	}

	/**
	 * Sets the completion date in the relevant task attribute.
	 * 
	 * @param completionDate
	 *            The completion date
	 */
	public void setCompletionDate(Date completionDate) {
		TaskAttribute completionDateAttribute = getMappedAttribute(TaskAttribute.DATE_COMPLETION);
		if (completionDateAttribute != null) {
			taskData.getAttributeMapper().setDateValue(completionDateAttribute, completionDate);
		}
	}

	/**
	 * Provides access to the latest modification date if it exists.
	 * 
	 * @return The latest modification date if it exists or null.
	 */
	public Date getModificationDate() {
		TaskAttribute modificationDateAttribute = getMappedAttribute(TaskAttribute.DATE_MODIFICATION);
		if (modificationDateAttribute != null) {
			return taskData.getAttributeMapper().getDateValue(modificationDateAttribute);
		}
		return null;
	}

	/**
	 * Sets the latest modification date in the relevant task attribute.
	 * 
	 * @param completionDate
	 *            The latest modification date
	 */
	public void setModificationDate(Date completionDate) {
		TaskAttribute modificationDateAttribute = getMappedAttribute(TaskAttribute.DATE_MODIFICATION);
		if (modificationDateAttribute != null) {
			taskData.getAttributeMapper().setDateValue(modificationDateAttribute, completionDate);
		}
	}

	/**
	 * Provides access to the creation date if it exists.
	 * 
	 * @return The creation date if it exists or null.
	 */
	public Date getCreationDate() {
		TaskAttribute creationDateAttribute = getMappedAttribute(TaskAttribute.DATE_CREATION);
		if (creationDateAttribute != null) {
			return taskData.getAttributeMapper().getDateValue(creationDateAttribute);
		}
		return null;
	}

	/**
	 * Sets the creation date in the relevant task attribute.
	 * 
	 * @param creationDate
	 *            The creation date
	 */
	public void setCreationDate(Date creationDate) {
		TaskAttribute creationDateAttribute = getMappedAttribute(TaskAttribute.DATE_CREATION);
		if (creationDateAttribute != null) {
			taskData.getAttributeMapper().setDateValue(creationDateAttribute, creationDate);
		}
	}

	/**
	 * Sets the values representing who this task is assigned to if a relevant field with this semantic for
	 * mylyn exists. Such a field uses {@code TaskAttribute.USER_ASSIGNED} for id.
	 * 
	 * @param valueIds
	 *            The identifier of the field values selected
	 */
	public void setAssignedTo(List<Integer> valueIds) {
		TaskAttribute attribute = getMappedAttribute(TaskAttribute.USER_ASSIGNED);
		if (attribute != null) {
			List<String> values = Lists.newArrayList();
			for (Integer valueId : valueIds) {
				values.add(String.valueOf(valueId));
			}
			attribute.setValues(values);
		}
	}

	/**
	 * Sets the initial effort of the task.
	 * 
	 * @param initialEffort
	 *            The initial effort
	 * @param fieldId
	 *            The identifier of the field
	 */
	public void setInitialEffort(int initialEffort, int fieldId) {
		TaskAttribute attribute = getMappedAttributeById(fieldId);
		if (attribute != null) {
			taskData.getAttributeMapper().setIntegerValue(attribute, Integer.valueOf(initialEffort));
		}
	}

	/**
	 * Sets the value of the literal field with the given field identifier.
	 * 
	 * @param value
	 *            The value of the field
	 * @param fieldId
	 *            The identifier of the field
	 */
	public void setValue(String value, int fieldId) {
		TaskAttribute attribute = getMappedAttributeById(fieldId);
		if (attribute != null) {
			taskData.getAttributeMapper().setValue(attribute, value);
		}
	}

	/**
	 * Sets the value of the literal field with the given field identifier.
	 * 
	 * @param values
	 *            The value of the field
	 * @param fieldId
	 *            The identifier of the field
	 */
	public void setValues(List<String> values, int fieldId) {
		TaskAttribute attribute = getMappedAttributeById(fieldId);
		if (attribute != null && values != null) {
			taskData.getAttributeMapper().setValues(attribute, values);
		}
	}

	/**
	 * Sets the value of the date field with the given field identifier.
	 * 
	 * @param value
	 *            The timestamp representing the date
	 * @param fieldId
	 *            The identifier of the field
	 */
	public void setDateValue(int value, int fieldId) {
		// int <-> long
		TaskAttribute attribute = getMappedAttributeById(fieldId);
		if (attribute != null) {
			taskData.getAttributeMapper().setDateValue(attribute, new Date(1000L * value));
		}
	}

	/**
	 * Sets the value of the select box field with the given field identifier.
	 * 
	 * @param valueId
	 *            The identifier of the selected value
	 * @param fieldId
	 *            The identifier of the field
	 */
	public void setSelectBoxValue(int valueId, int fieldId) {
		// ITuleapConstants -> 100 nothing selected
		TaskAttribute attribute = getMappedAttributeById(fieldId);
		if (attribute != null) {
			if (valueId == ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID) {
				attribute.clearValues();
			} else {
				attribute.setValue(String.valueOf(valueId));
			}
		}
		// Take the workflow of the select box into account if it exists
		AbstractTuleapField field = tuleapConfigurableFieldsConfiguration.getFieldById(fieldId);
		if (field instanceof TuleapSelectBox) {
			TuleapSelectBox selectBox = (TuleapSelectBox)field;
			selectBox.updateOptionsWithWorkflow(attribute);
		}
	}

	/**
	 * Sets the value of the multi select box field with the given field identifier.
	 * 
	 * @param valuesId
	 *            The identifier of the values of the select box selected
	 * @param fieldId
	 *            The identifier of the field
	 */
	public void setMultiSelectBoxValues(Set<Integer> valuesId, int fieldId) {
		// ITuleapConstants -> 100 nothing selected
		TaskAttribute attribute = getMappedAttributeById(fieldId);
		if (attribute != null) {
			attribute.clearValues();
			for (Integer valueId : valuesId) {
				if (valueId.intValue() != ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID) {
					attribute.addValue(String.valueOf(valueId));
				}
			}
		}
	}

	/**
	 * Adds a comment to the task.
	 * 
	 * @param tuleapArtifactComment
	 *            The comment to add
	 */
	public void addComment(TuleapElementComment tuleapArtifactComment) {
		int count = getNumberOfCommentAttributes();
		TaskAttribute attribute = taskData.getRoot().createAttribute(
				TaskAttribute.PREFIX_COMMENT + String.valueOf(count));
		attribute.getMetaData().defaults().setReadOnly(true).setType(TaskAttribute.TYPE_COMMENT);
		attribute.getMetaData().putValue(TaskAttribute.META_ASSOCIATED_ATTRIBUTE_ID,
				TaskAttribute.COMMENT_TEXT);
		TaskCommentMapper taskComment = TaskCommentMapper.createFrom(attribute);

		taskComment.setCommentId(String.valueOf(count));
		taskComment.setNumber(Integer.valueOf(count));
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(Long.valueOf(tuleapArtifactComment.getSubmittedOn()).longValue() * 1000);
		Date creationDate = calendar.getTime();
		taskComment.setCreationDate(creationDate);
		taskComment.setText(tuleapArtifactComment.getBody());
		TuleapPerson submitter = tuleapArtifactComment.getSubmitter();
		if (submitter != null) {
			TaskRepository taskRepository = attribute.getTaskData().getAttributeMapper().getTaskRepository();
			IRepositoryPerson repositoryPerson = taskRepository.createPerson(submitter.getEmail());
			repositoryPerson.setName(submitter.getUserName());
			taskComment.setAuthor(repositoryPerson);
		}
		taskComment.applyTo(attribute);
	}

	/**
	 * Counts the number of existing comment attributes in the wrapped task data.
	 * 
	 * @return the number of comment task attributes present in the task data.
	 */
	private int getNumberOfCommentAttributes() {
		Set<String> keys = taskData.getRoot().getAttributes().keySet();
		int count = 0;
		for (String key : keys) {
			if (key.startsWith(TaskAttribute.PREFIX_COMMENT)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Returns the set of the field values.
	 * 
	 * @return The set of the field values, never null but potentially empty.
	 */
	public Set<AbstractFieldValue> getFieldValues() {
		// FIXME SBE Support full task key in the task dependency fields!!!!!!!
		/*
		 * /!\HACKISH/!\ We may have, as the id of the task, an identifier (ie: 917) or a complex identifier
		 * (ie: MyRepository:MyProject[116] #917 - My Task Name). We will try to parse the value as an
		 * integer, if it fails, then we know that we have a complex identifier, in that case, we will parse
		 * the identifier from this complex identifier and use it.
		 */

		// returns all the tuleap field value in order to send them to the server
		// attachments are not uploaded with the same mechanism so no need to return them here
		// do not return the fields computed by tuleap or mylyn: creation date, completion date, id, etc
		Set<AbstractFieldValue> result = new LinkedHashSet<AbstractFieldValue>();
		// For the moment, we return all known values.
		// Later, an improvement will be to return only those values that have changed.
		for (TaskAttribute attribute : taskData.getRoot().getAttributes().values()) {
			Collection<AbstractTuleapField> fields = this.tuleapConfigurableFieldsConfiguration.getFields();
			for (AbstractTuleapField abstractTuleapField : fields) {
				if (String.valueOf(abstractTuleapField.getIdentifier()).equals(attribute.getId())
						&& shouldBeSentToTheServer(abstractTuleapField.getIdentifier())) {
					if (attribute.getOptions().isEmpty()) {
						LiteralFieldValue fieldValue = new LiteralFieldValue(Integer.parseInt(attribute
								.getId()), attribute.getValue());
						result.add(fieldValue);
					} else {
						// select box or multi select box (or check box)
						List<Integer> valueIds = new ArrayList<Integer>();
						for (String strValue : attribute.getValues()) {
							try {
								valueIds.add(Integer.valueOf(strValue));
							} catch (NumberFormatException e) {
								// TODO Add log about non integer value
							}
						}
						BoundFieldValue boundFieldValue = new BoundFieldValue(Integer.parseInt(attribute
								.getId()), valueIds);
						result.add(boundFieldValue);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Indicates if the attribute with the given identifier should be sent to the server.
	 * 
	 * @param identifier
	 *            The identifier of the attribute
	 * @return <code>true</code> if it should be sent to the server, <code>false</code> otherwise
	 */
	private boolean shouldBeSentToTheServer(int identifier) {
		return !ATTRIBUTE_IDS_NOT_TO_SEND.contains(String.valueOf(identifier));
	}

	/**
	 * Returns the mapped attribute with the given id or {@code null} if it doesn't exist.
	 * 
	 * @param fieldId
	 *            The id of the task attribute being looked for
	 * @return The mapped attribute with the given id or {@code null} if it doesn't exist.
	 */
	private TaskAttribute getMappedAttributeById(int fieldId) {
		return taskData.getRoot().getMappedAttribute(String.valueOf(fieldId));
	}

	/**
	 * Provides access to the TaskAttribute that contains the actual status value.
	 * 
	 * @return The status task attribute.
	 */
	private TaskAttribute getStatusTaskAttribute() {
		return taskData.getRoot().getAttribute(TaskAttribute.STATUS);
	}

	/**
	 * The status of the task.
	 * 
	 * @return the status of the task.
	 */
	public String getStatus() {
		TaskAttribute attribute = taskData.getRoot().getMappedAttribute(TaskAttribute.STATUS);
		if (attribute != null) {
			return attribute.getValue();
		}
		return null;
	}
}
