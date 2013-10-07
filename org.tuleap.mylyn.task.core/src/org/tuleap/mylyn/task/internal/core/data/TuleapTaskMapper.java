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

import java.util.Calendar;
import java.util.Date;
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
import org.tuleap.mylyn.task.internal.core.model.TuleapElementComment;
import org.tuleap.mylyn.task.internal.core.model.TuleapPerson;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessagesKeys;

/**
 * Mapper.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapTaskMapper extends AbstractTaskMapper {

	/**
	 * The identifier of the project name task attribute.
	 */
	public static final String PROJECT_ID = "mtc_project_id"; //$NON-NLS-1$

	/**
	 * The constructor.
	 * 
	 * @param taskData
	 *            The task data
	 */
	public TuleapTaskMapper(TaskData taskData) {
		super(taskData);
	}

	/**
	 * Initialize an empty task data from the given configuration.
	 * 
	 * @param taskKindLabel
	 *            The task kind label;
	 */
	public void initializeEmptyTaskData(String taskKindLabel) {
		createTaskKindTaskAttribute(taskKindLabel);
		createCreationDateTaskAttribute();
		createLastUpdateDateTaskAttribute();
		createCompletionDateTaskAttribute();
		createNewCommentTaskAttribute();
		createSubmittedByTaskAttribute();
		createTaskKeyAttribute();
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
		metaData.setLabel(TuleapMylynTasksMessages.getString(TuleapMylynTasksMessagesKeys.creationDateLabel));
		metaData.setType(TaskAttribute.TYPE_DATE);
	}

	/**
	 * Creates the task attribute representing the last update date.
	 */
	private void createLastUpdateDateTaskAttribute() {
		TaskAttribute attribute = taskData.getRoot().createMappedAttribute(TaskAttribute.DATE_MODIFICATION);
		TaskAttributeMetaData metaData = attribute.getMetaData();
		metaData.setLabel(TuleapMylynTasksMessages
				.getString(TuleapMylynTasksMessagesKeys.lastModificationDateLabel));
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
				.getString(TuleapMylynTasksMessagesKeys.completionDateLabel));
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
		metaData.setLabel(TuleapMylynTasksMessages.getString(TuleapMylynTasksMessagesKeys.newCommentLabel));
		metaData.setType(TaskAttribute.TYPE_LONG_RICH_TEXT);
		return attribute;
	}

	/**
	 * Creates the task attribute representing the task kind.
	 * 
	 * @param taskKindLabel
	 *            The task kind label;
	 */
	private void createTaskKindTaskAttribute(String taskKindLabel) {
		TaskAttribute attribute = taskData.getRoot().createAttribute(TaskAttribute.TASK_KIND);
		if (taskKindLabel != null) {
			attribute.setValue(taskKindLabel);
		} else {
			attribute.setValue(TuleapMylynTasksMessages
					.getString(TuleapMylynTasksMessagesKeys.defaultConfigurationName));
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
		if (person != null) {
			IRepositoryPerson repositoryPerson = taskData.getAttributeMapper().getTaskRepository()
					.createPerson(person.getEmail());
			repositoryPerson.setName(person.getUserName());
			taskAttachment.setAuthor(repositoryPerson);
		}
		taskAttachment.setFileName(tuleapAttachment.getFilename());
		taskAttachment.setLength(Long.valueOf(tuleapAttachment.getSize()));
		taskAttachment.setDescription(tuleapAttachment.getDescription());
		taskAttachment.setContentType(tuleapAttachment.getContentType());
		taskAttachment.applyTo(attribute);
	}

	/**
	 * Returns the identifier of the project.
	 * 
	 * @return The identifier of the project
	 */
	public int getProjectId() {
		int projectId = TuleapTaskIdentityUtil.IRRELEVANT_ID;

		if (this.taskData.isNew()) {
			TaskAttribute projectIdAtt = taskData.getRoot().getMappedAttribute(PROJECT_ID);
			if (projectIdAtt != null) {
				projectId = taskData.getAttributeMapper().getIntegerValue(projectIdAtt).intValue();
			}
		} else {
			projectId = TuleapTaskIdentityUtil.getProjectIdFromTaskDataId(taskData.getTaskId());
		}

		return projectId;
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
		return TuleapTaskIdentityUtil.getElementIdFromTaskDataId(taskData.getTaskId());
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
	 * Returns the mapped attribute with the given id or {@code null} if it doesn't exist.
	 * 
	 * @param fieldId
	 *            The id of the task attribute being looked for
	 * @return The mapped attribute with the given id or {@code null} if it doesn't exist.
	 */
	private TaskAttribute getMappedAttributeById(int fieldId) {
		return taskData.getRoot().getMappedAttribute(String.valueOf(fieldId));
	}
}
