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

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.mylyn.tasks.core.IRepositoryPerson;
import org.eclipse.mylyn.tasks.core.data.TaskAttachmentMapper;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMetaData;
import org.eclipse.mylyn.tasks.core.data.TaskCommentMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskMapper;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.TuleapArtifactComment;
import org.tuleap.mylyn.task.internal.core.model.TuleapAttachment;
import org.tuleap.mylyn.task.internal.core.model.TuleapPerson;
import org.tuleap.mylyn.task.internal.core.model.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.field.AbstractTuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;

/**
 * The Tuleap Task Mapper will be used to manipulate the task data model from Mylyn with a higher level of
 * abstraction.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapTaskMapper extends AbstractTaskMapper {

	/**
	 * The identifier of an invalid tracker.
	 */
	public static final int INVALID_TRACKER_ID = -1;

	/**
	 * The identifier of an invalid project.
	 */
	public static final int INVALID_PROJECT_ID = -1;

	/**
	 * The identifier of the group id task attribute.
	 */
	public static final String GROUP_ID = "tuleap_task_data_group_id"; //$NON-NLS-1$

	/**
	 * The identifier of the tracker id task attribute.
	 */
	public static final String TRACKER_ID = "tuleap_task_data_tracker_id"; //$NON-NLS-1$

	/**
	 * The identifier of the project name task attribute.
	 */
	public static final String PROJECT_ID = "tuleap_task_data_project_id"; //$NON-NLS-1$

	/**
	 * The identifier of the project name task attribute.
	 */
	public static final String PROJECT_NAME = "tuleap_task_data_project_name"; //$NON-NLS-1$

	/**
	 * The identifier of the tracker name task attribute.
	 */
	public static final String TRACKER_NAME = "tuleap_task_data_tracker_name"; //$NON-NLS-1$

	/**
	 * List of attribute Ids that need not be sent to the server for update.
	 */
	private static final List<String> ATTRIBUTE_IDS_NOT_TO_SEND = Collections.unmodifiableList(Arrays.asList(
			TaskAttribute.DATE_COMPLETION, TaskAttribute.DATE_CREATION, TaskAttribute.DATE_MODIFICATION,
			TaskAttribute.PREFIX_OPERATION + TaskAttribute.STATUS, TaskAttribute.TASK_KEY,
			TaskAttribute.TASK_KIND));

	/**
	 * The tracker configuration.
	 */
	protected final TuleapTrackerConfiguration trackerConfiguration;

	/**
	 * The constructor.
	 * 
	 * @param taskData
	 *            The task data
	 * @param tuleapTrackerConfiguration
	 *            The tracker configuration.
	 */
	public TuleapTaskMapper(TaskData taskData, TuleapTrackerConfiguration tuleapTrackerConfiguration) {
		super(taskData);
		this.trackerConfiguration = tuleapTrackerConfiguration;
	}

	/**
	 * Initialize an empty task data from the given Tuleap tracker configuration.
	 */
	public void initializeEmptyTaskData() {
		createTaskKindTaskAttribute();

		// The group id and the tracker id
		setProjectId(trackerConfiguration.getTuleapProjectConfiguration().getIdentifier());
		// setProjectName(trackerConfiguration.getTuleapProjectConfiguration().getName());
		setTrackerId(trackerConfiguration.getTrackerId());
		// setTrackerName(trackerConfiguration.getName());

		createCreationDateTaskAttribute();
		createLastUpdateDateTaskAttribute();
		createCompletionDateTaskAttribute();
		createNewCommentTaskAttribute();
		createSubmittedByTaskAttribute();
		createTaskKeyAttribute();

		// Default attributes
		TaskAttribute root = taskData.getRoot();
		Collection<AbstractTuleapField> fields = trackerConfiguration.getFields();
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
		metaData.setLabel(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.CreationDate")); //$NON-NLS-1$
		metaData.setType(TaskAttribute.TYPE_DATE);
	}

	/**
	 * Creates the task attribute representing the last update date.
	 */
	private void createLastUpdateDateTaskAttribute() {
		TaskAttribute attribute = taskData.getRoot().createMappedAttribute(TaskAttribute.DATE_MODIFICATION);
		TaskAttributeMetaData metaData = attribute.getMetaData();
		metaData.setLabel(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.LastModificationDate")); //$NON-NLS-1$
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
		metaData.setLabel(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.CompletionDate")); //$NON-NLS-1$
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
		metaData.setLabel(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.NewComment")); //$NON-NLS-1$
		metaData.setType(TaskAttribute.TYPE_LONG_RICH_TEXT);
		return attribute;
	}

	/**
	 * Creates the task attribute representing the task kind.
	 */
	private void createTaskKindTaskAttribute() {
		TaskAttribute attribute = taskData.getRoot().createAttribute(TaskAttribute.TASK_KIND);
		String name = trackerConfiguration.getName();
		if (name != null) {
			attribute.setValue(name);
		} else {
			attribute.setValue(TuleapMylynTasksMessages
					.getString("TuleapTaskDataHandler.DefaultConfigurationName")); //$NON-NLS-1$
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
	public void addAttachment(String tuleapFieldName, TuleapAttachment tuleapAttachment) {
		TaskAttribute attribute = taskData.getRoot().createAttribute(
				TaskAttribute.PREFIX_ATTACHMENT + tuleapFieldName + "---" + tuleapAttachment.getId()); //$NON-NLS-1$
		attribute.getMetaData().defaults().setType(TaskAttribute.TYPE_ATTACHMENT);
		TaskAttachmentMapper taskAttachment = TaskAttachmentMapper.createFrom(attribute);
		taskAttachment.setAttachmentId(tuleapAttachment.getId());

		TuleapPerson person = tuleapAttachment.getPerson();
		IRepositoryPerson iRepositoryPerson = trackerConfiguration.getPerson(person.getEmail());
		if (iRepositoryPerson == null) {
			iRepositoryPerson = taskData.getAttributeMapper().getTaskRepository().createPerson(
					person.getEmail());
			iRepositoryPerson.setName(person.getRealName());
			trackerConfiguration.registerPerson(iRepositoryPerson);
		}
		taskAttachment.setAuthor(iRepositoryPerson);
		taskAttachment.setFileName(tuleapAttachment.getFilename());
		taskAttachment.setLength(tuleapAttachment.getSize());
		taskAttachment.setDescription(tuleapAttachment.getDescription());
		taskAttachment.setContentType(tuleapAttachment.getContentType());
		taskAttachment.applyTo(attribute);
	}

	/**
	 * Sets the identifier of the tracker.
	 * 
	 * @param trackerId
	 *            The identifier of the tracker
	 */
	public void setTrackerId(int trackerId) {
		// should not appear in the attribute part so no task attribute type!
		TaskAttribute trackerIdAtt = taskData.getRoot().getMappedAttribute(TRACKER_ID);
		if (trackerIdAtt == null) {
			trackerIdAtt = taskData.getRoot().createMappedAttribute(TRACKER_ID);
		}
		taskData.getAttributeMapper().setIntegerValue(trackerIdAtt, Integer.valueOf(trackerId));
	}

	/**
	 * Returns the identifier of the tracker or INVALID_TRACKER otherwise.
	 * 
	 * @return The identifier of the tracker or INVALID_TRACKER otherwise.
	 */
	public int getTrackerId() {
		TaskAttribute trackerIdAtt = taskData.getRoot().getMappedAttribute(TRACKER_ID);
		if (trackerIdAtt == null) {
			return INVALID_TRACKER_ID;
		}
		return taskData.getAttributeMapper().getIntegerValue(trackerIdAtt).intValue();
	}

	/**
	 * Sets the identifier of the project.
	 * 
	 * @param projectId
	 *            The identifier of the project
	 */
	public void setProjectId(int projectId) {
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
	public int getArtifactId() {
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
			if (trackerConfiguration.hasClosedStatusMeaning(value) && getCompletionDate() == null) {
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
		AbstractTuleapSelectBox field = trackerConfiguration.getStatusField();
		if (field instanceof TuleapSelectBox) {
			TuleapSelectBox selectBox = (TuleapSelectBox)field;
			if (selectBox.hasWorkflow()) {
				String currentOption = attribute.getOption(value);
				attribute.clearOptions();
				if (currentOption != null
						&& !String.valueOf(ITuleapConstants.TRACKER_FIELD_NONE_BINDING_ID).equals(value)) {
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
	 * Sets the values representing who this task is assigned to.
	 * 
	 * @param valuesId
	 *            The identifier of the field values selected
	 * @param fieldId
	 *            The identifier of the field
	 */
	public void setAssignedTo(Set<Integer> valuesId, int fieldId) {
		throw new UnsupportedOperationException();
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
			if (valueId == ITuleapConstants.TRACKER_FIELD_NONE_BINDING_ID) {
				attribute.clearValues();
			} else {
				attribute.setValue(String.valueOf(valueId));
			}
		}
		// Take the workflow of the select box into account if it exists
		AbstractTuleapField field = trackerConfiguration.getFieldById(fieldId);
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
				if (valueId.intValue() != ITuleapConstants.TRACKER_FIELD_NONE_BINDING_ID) {
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
	public void addComment(TuleapArtifactComment tuleapArtifactComment) {
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
		if (tuleapArtifactComment.getEmail() != null) {
			IRepositoryPerson iRepositoryPerson = trackerConfiguration.getPerson(tuleapArtifactComment
					.getEmail());
			if (iRepositoryPerson == null) {
				iRepositoryPerson = taskData.getAttributeMapper().getTaskRepository().createPerson(
						tuleapArtifactComment.getEmail());
				iRepositoryPerson.setName(tuleapArtifactComment.getName());
				trackerConfiguration.registerPerson(iRepositoryPerson);
			}
			taskComment.setAuthor(iRepositoryPerson);
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
	public Set<AbstractTuleapFieldValue> getFieldValues() {
		// returns all the tuleap field value in order to send them to the server
		// attachments are not uploaded with the same mechanism so no need to return them here
		// do not return the fields computed by tuleap or mylyn: creation date, completion date, id, etc
		Set<AbstractTuleapFieldValue> result = new LinkedHashSet<AbstractTuleapFieldValue>();
		// For the moment, we return all known values.
		// Later, an improvement will be to return only those values that have changed.
		for (TaskAttribute attribute : taskData.getRoot().getAttributes().values()) {
			if (mustBeSentToServer(attribute)) {
				if (attribute.getOptions().isEmpty()) {
					TuleapLiteralFieldValue fieldValue = new TuleapLiteralFieldValue(attribute.getValue(),
							Integer.parseInt(attribute.getId()));
					result.add(fieldValue);
				} else {
					// select box or multi select box (or check box)
					Set<Integer> valueIds = new LinkedHashSet<Integer>();
					for (String strValue : attribute.getValues()) {
						try {
							valueIds.add(Integer.valueOf(strValue));
						} catch (NumberFormatException e) {
							// TODO Add log about non integer value
						}
					}
					TuleapBoundFieldValue boundFieldValue = new TuleapBoundFieldValue(valueIds, Integer
							.parseInt(attribute.getId()));
					result.add(boundFieldValue);
				}
			}
		}
		return result;
	}

	/**
	 * Indicates whether the given Task Attribute needs be sent to the server, which is true for the moment if
	 * the attribute is not a computed field like the creation, modification, or completion date, th id, and
	 * so on.
	 * 
	 * @param attribute
	 *            The candidate attribute
	 * @return {@code true} if and only if the task attribute's modification must be sent to the server.
	 */
	private boolean mustBeSentToServer(TaskAttribute attribute) {
		boolean result = true;
		String attributeId = attribute.getId();
		if (ATTRIBUTE_IDS_NOT_TO_SEND.contains(attributeId)) {
			result = false;
		}
		if (attributeId.startsWith(TaskAttribute.PREFIX_ATTACHMENT)) {
			result = false;
		}
		return result;
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
	 * Provides access to the TaskAttribute that supports the operation to change the status.
	 * 
	 * @return The select box task attribute for the status.
	 */
	private TaskAttribute getStatusSelectBoxTaskAttribute() {
		return taskData.getRoot().getAttribute(TaskAttribute.PREFIX_OPERATION + TaskAttribute.STATUS);
	}

	/**
	 * Sets the project name of the task data.
	 * 
	 * @param projectName
	 *            The project name
	 */
	public void setProjectName(String projectName) {
		TaskAttribute attribute = this.getWriteableAttribute(TuleapTaskMapper.PROJECT_NAME, null);
		if (attribute != null) {
			this.taskData.getAttributeMapper().setValue(attribute, projectName);
		}
	}

	/**
	 * Returns the project name of the task data.
	 * 
	 * @return The project name of the task data
	 */
	public String getProjectName() {
		TaskAttribute attribute = taskData.getRoot().getMappedAttribute(TuleapTaskMapper.PROJECT_NAME);
		if (attribute != null) {
			return taskData.getAttributeMapper().getValueLabel(attribute);
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * Sets the tracker name of the task data.
	 * 
	 * @param trackerName
	 *            The tracker name
	 */
	public void setTrackerName(String trackerName) {
		TaskAttribute attribute = this.getWriteableAttribute(TuleapTaskMapper.TRACKER_NAME, null);
		if (attribute != null) {
			this.taskData.getAttributeMapper().setValue(attribute, trackerName);
		}
	}

	/**
	 * Returns the tracker name of the task data.
	 * 
	 * @return The tracker name of the task data
	 */
	public String getTrackerName() {
		TaskAttribute attribute = taskData.getRoot().getMappedAttribute(TuleapTaskMapper.TRACKER_NAME);
		if (attribute != null) {
			return taskData.getAttributeMapper().getValueLabel(attribute);
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * Sets the group id of the task data.
	 * 
	 * @param groupId
	 *            The group id
	 */
	public void setGroupId(int groupId) {
		this.setIntValue(TuleapTaskMapper.GROUP_ID, groupId);
	}

	/**
	 * Returns the group id of the task data or -1 if it can't be found.
	 * 
	 * @return The group id of the task data or -1 if it can"t be found
	 */
	public int getGroupId() {
		return this.getIntValue(TuleapTaskMapper.GROUP_ID);
	}

	/**
	 * Sets a value of type "int" in the task data for an attribute with the given key. If the attribute does
	 * not exist, it will be created.
	 * 
	 * @param taskAttributeKey
	 *            The key of the task data attribute
	 * @param value
	 *            The int value to set
	 */
	private void setIntValue(String taskAttributeKey, int value) {
		TaskAttribute attribute = this.getWriteableAttribute(taskAttributeKey, null);
		if (attribute != null) {
			this.taskData.getAttributeMapper().setValue(attribute, Integer.valueOf(value).toString());
		}
	}

	/**
	 * Returns the value of type int for the task data attribute with the given key.
	 * 
	 * @param taskAttributeKey
	 *            The key of the task data attribute
	 * @return The value of type int for the task data attribute with the given key
	 */
	private int getIntValue(String taskAttributeKey) {
		int value = -1;

		TaskAttribute attribute = taskData.getRoot().getMappedAttribute(taskAttributeKey);
		if (attribute != null) {
			String valueLabel = taskData.getAttributeMapper().getValueLabel(attribute);
			try {
				return Integer.valueOf(valueLabel).intValue();
			} catch (NumberFormatException e) {
				// do not log
			}
		}
		return value;
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
