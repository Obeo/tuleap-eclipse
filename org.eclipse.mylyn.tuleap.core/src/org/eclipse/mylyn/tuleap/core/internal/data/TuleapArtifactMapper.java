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
package org.eclipse.mylyn.tuleap.core.internal.data;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.mylyn.tasks.core.IRepositoryPerson;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskSchema.Field;
import org.eclipse.mylyn.tasks.core.data.DefaultTaskSchema;
import org.eclipse.mylyn.tasks.core.data.TaskAttachmentMapper;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMetaData;
import org.eclipse.mylyn.tasks.core.data.TaskCommentMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskMapper;
import org.eclipse.mylyn.tuleap.core.internal.TuleapCoreActivator;
import org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapUser;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.AbstractTuleapSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBoxItem;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapString;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AbstractFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AttachmentValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.BoundFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.LiteralFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapElementComment;
import org.eclipse.mylyn.tuleap.core.internal.util.TuleapMylynTasksMessages;
import org.eclipse.mylyn.tuleap.core.internal.util.TuleapMylynTasksMessagesKeys;

/**
 * The Tuleap Configurable Element Mapper will be used to manipulate the task data model from Mylyn with a
 * higher level of abstraction.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapArtifactMapper extends TaskMapper {

	/**
	 * The identifier of the project name task attribute.
	 */
	public static final String PROJECT_ID = "mtc_project_id"; //$NON-NLS-1$

	/**
	 * The identifier of the tracker id task attribute.
	 */
	public static final String TRACKER_ID = "mtc_tracker_id"; //$NON-NLS-1$

	/**
	 * The invalid status id.
	 */
	public static final int INVALID_STATUS_ID = -1;

	/**
	 * List of attribute Ids that need not be sent to the server for update.
	 */
	private static final List<String> ATTRIBUTE_IDS_NOT_TO_SEND = Collections.unmodifiableList(Arrays.asList(
			TaskAttribute.DATE_COMPLETION, TaskAttribute.DATE_CREATION, TaskAttribute.DATE_MODIFICATION,
			TaskAttribute.PREFIX_OPERATION + TaskAttribute.STATUS, TaskAttribute.TASK_KEY,
			TaskAttribute.TASK_KIND, TRACKER_ID, PROJECT_ID));

	/**
	 * The tracker.
	 */
	protected final TuleapTracker tracker;

	/**
	 * The constructor.
	 * 
	 * @param taskData
	 *            The task data
	 * @param tracker
	 *            The tracker.
	 */
	public TuleapArtifactMapper(TaskData taskData, TuleapTracker tracker) {
		super(taskData);
		this.tracker = tracker;
	}

	/**
	 * Initialize an empty task data from the given tracker.
	 */
	public void initializeEmptyTaskData() {
		createTaskKindTaskAttribute();

		// The project id and the tracker id
		setProjectId(tracker.getProject().getIdentifier());
		setTrackerId(tracker.getIdentifier());

		createCreationDateTaskAttribute();
		createLastUpdateDateTaskAttribute();
		createCompletionDateTaskAttribute();
		createNewCommentTaskAttribute();
		createSubmittedByTaskAttribute();
		createTaskKeyAttribute();
		createTaskURLAttribute();

		// Default attributes
		TaskAttribute root = getTaskData().getRoot();
		Collection<AbstractTuleapField> fields = tracker.getFields();
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
		TaskAttribute attribute = getTaskData().getRoot().createMappedAttribute(TaskAttribute.TASK_KEY);
		TaskAttributeMetaData metaData = attribute.getMetaData();
		metaData.setType(TaskAttribute.TYPE_SHORT_TEXT);
		metaData.setReadOnly(true);
	}

	/**
	 * Creates the read-only task attribute representing the task url.
	 */
	private void createTaskURLAttribute() {
		TaskAttribute attribute = getTaskData().getRoot().createMappedAttribute(TaskAttribute.TASK_URL);
		TaskAttributeMetaData metaData = attribute.getMetaData();
		metaData.setType(TaskAttribute.TYPE_SHORT_TEXT);
		metaData.setReadOnly(true);
	}

	/**
	 * Creates the task attribute representing the creation date.
	 */
	private void createCreationDateTaskAttribute() {
		TaskAttribute attribute = getTaskData().getRoot().createMappedAttribute(TaskAttribute.DATE_CREATION);
		TaskAttributeMetaData metaData = attribute.getMetaData();
		metaData.setLabel(TuleapMylynTasksMessages.getString(TuleapMylynTasksMessagesKeys.creationDateLabel));
		metaData.setType(TaskAttribute.TYPE_DATE);
	}

	/**
	 * Creates the task attribute representing the last update date.
	 */
	private void createLastUpdateDateTaskAttribute() {
		TaskAttribute attribute = getTaskData().getRoot().createMappedAttribute(
				TaskAttribute.DATE_MODIFICATION);
		TaskAttributeMetaData metaData = attribute.getMetaData();
		metaData.setLabel(TuleapMylynTasksMessages
				.getString(TuleapMylynTasksMessagesKeys.lastModificationDateLabel));
		metaData.setType(TaskAttribute.TYPE_DATE);
	}

	/**
	 * Creates the task attribute representing the author of the task.
	 */
	private void createSubmittedByTaskAttribute() {
		TaskAttribute attribute = getTaskData().getRoot().createMappedAttribute(TaskAttribute.USER_REPORTER);
		TaskAttributeMetaData metaData = attribute.getMetaData();
		metaData.setType(TaskAttribute.TYPE_PERSON);
		metaData.setReadOnly(true);
	}

	/**
	 * Creates the task attribute representing the completion date.
	 */
	private void createCompletionDateTaskAttribute() {
		TaskAttribute attribute = getTaskData().getRoot()
				.createMappedAttribute(TaskAttribute.DATE_COMPLETION);
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
		TaskAttribute attribute = getTaskData().getRoot().createAttribute(TaskAttribute.COMMENT_NEW);
		TaskAttributeMetaData metaData = attribute.getMetaData();
		metaData.setLabel(TuleapMylynTasksMessages.getString(TuleapMylynTasksMessagesKeys.newCommentLabel));
		metaData.setType(TaskAttribute.TYPE_LONG_RICH_TEXT);
		return attribute;
	}

	/**
	 * Creates the task attribute representing the task kind.
	 */
	private void createTaskKindTaskAttribute() {
		TaskAttribute attribute = getTaskData().getRoot().createAttribute(TaskAttribute.TASK_KIND);
		String name = tracker.getLabel();
		if (name != null) {
			attribute.setValue(name);
		} else {
			attribute.setValue(TuleapMylynTasksMessages
					.getString(TuleapMylynTasksMessagesKeys.defaultTrackerName));
		}
	}

	/**
	 * Sets the identifier of the tracker of the element (tracker id, etc).
	 * 
	 * @param trackerId
	 *            The identifier of the tracker of the element
	 */
	private void setTrackerId(int trackerId) {
		// should not appear in the attribute part so no task attribute type!
		TaskAttribute trackerIdAtt = getTaskData().getRoot().getMappedAttribute(TRACKER_ID);
		if (trackerIdAtt == null) {
			trackerIdAtt = getTaskData().getRoot().createMappedAttribute(TRACKER_ID);
		}
		getTaskData().getAttributeMapper().setIntegerValue(trackerIdAtt, Integer.valueOf(trackerId));
	}

	/**
	 * Sets the identifier of the project.
	 * 
	 * @param projectId
	 *            The identifier of the project
	 */
	private void setProjectId(int projectId) {
		TaskAttribute projectIdAtt = getTaskData().getRoot().getMappedAttribute(PROJECT_ID);
		if (projectIdAtt == null) {
			projectIdAtt = getTaskData().getRoot().createMappedAttribute(PROJECT_ID);
		}
		getTaskData().getAttributeMapper().setIntegerValue(projectIdAtt, Integer.valueOf(projectId));
	}

	/**
	 * Sets the status of the task. If the task is then closed, its completion date is forced to the last
	 * modification date if it was not already present. On the contrary, if the task is not closed, its
	 * completion date is forced to {@code null}, since the closed semantic is managed by mylyn <i>via</i> the
	 * completion date presence.
	 * 
	 * @param statusItemId
	 *            The status value id
	 */
	public void setStatus(int statusItemId) {
		// Sets the value of the read-only field and update the selectbox on which an operation is bound
		TaskAttribute attribute = getStatusTaskAttribute();
		if (attribute != null) {
			attribute.clearValues();
			attribute.setValue(String.valueOf(statusItemId));
			if (tracker.hasClosedStatusMeaning(statusItemId) && getCompletionDate() == null) {
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
			updateStatusSelectBox(statusItemId, attribute);
		}
	}

	/**
	 * Sets the identifier of the artifact.
	 * 
	 * @param taskKey
	 *            The task key
	 */
	@Override
	public void setTaskKey(String taskKey) {
		TaskAttribute taskKeyAtt = getMappedAttribute(TaskAttribute.TASK_KEY);
		taskKeyAtt.setValue(taskKey);
	}

	/**
	 * Returns the identifier of the artifact.
	 * 
	 * @return The identifier of the artifact
	 */
	public TuleapTaskId getTaskId() {
		if (this.getTaskData().isNew()) {
			int projectId = TuleapTaskId.IRRELEVANT_ID;
			TaskAttribute projectIdAtt = getTaskData().getRoot().getMappedAttribute(PROJECT_ID);
			if (projectIdAtt != null) {
				projectId = getTaskData().getAttributeMapper().getIntegerValue(projectIdAtt).intValue();
			}
			int trackerId = TuleapTaskId.IRRELEVANT_ID;
			TaskAttribute trackerIdAtt = getTaskData().getRoot().getMappedAttribute(TRACKER_ID);
			if (trackerIdAtt != null) {
				trackerId = getTaskData().getAttributeMapper().getIntegerValue(trackerIdAtt).intValue();
			}
			return TuleapTaskId.forNewArtifact(projectId, trackerId);
		}
		return TuleapTaskId.forName(getTaskData().getTaskId());
	}

	/**
	 * Provides the wrapped task's key.
	 * 
	 * @return The task key or null if it cannot be found.
	 */
	@Override
	public String getTaskKey() {
		TaskAttribute taskKey = getMappedAttribute(TaskAttribute.TASK_KEY);
		return taskKey.getValue();
	}

	/**
	 * Sets the url of the wrapped task. Nothing happens if the given URL is null.
	 * 
	 * @param url
	 *            the url of the task
	 */
	@Override
	public void setTaskUrl(String url) {
		if (url != null) {
			TaskAttribute attribute = getWriteableAttribute(TaskAttribute.TASK_URL, TaskAttribute.TYPE_URL);
			getTaskData().getAttributeMapper().setValue(attribute, url);
		}
	}

	/**
	 * Provides the wrapped task's URL.
	 * 
	 * @return The wrapped task's URL
	 */
	@Override
	public String getTaskUrl() {
		TaskAttribute attribute = getMappedAttribute(TaskAttribute.TASK_URL);
		if (attribute != null) {
			return getTaskData().getAttributeMapper().getValue(attribute);
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * Sets the summary of the wrapped task.
	 * 
	 * @param value
	 *            The value of the summary
	 */
	@Override
	public void setSummary(String value) {
		TaskAttribute attribute = getTaskData().getRoot().getMappedAttribute(TaskAttribute.SUMMARY);
		if (attribute != null && value != null) {
			getTaskData().getAttributeMapper().setValue(attribute, value);
		}
	}

	/**
	 * Provides access to the completion date if it exists.
	 * 
	 * @return The completion date if it exists or null.
	 */
	@Override
	public Date getCompletionDate() {
		TaskAttribute completionDateAttribute = getMappedAttribute(TaskAttribute.DATE_COMPLETION);
		if (completionDateAttribute != null) {
			return getTaskData().getAttributeMapper().getDateValue(completionDateAttribute);
		}
		return null;
	}

	/**
	 * Sets the completion date in the relevant task attribute.
	 * 
	 * @param completionDate
	 *            The completion date
	 */
	@Override
	public void setCompletionDate(Date completionDate) {
		TaskAttribute completionDateAttribute = getMappedAttribute(TaskAttribute.DATE_COMPLETION);
		if (completionDateAttribute != null) {
			getTaskData().getAttributeMapper().setDateValue(completionDateAttribute, completionDate);
		}
	}

	/**
	 * Provides access to the latest modification date if it exists.
	 * 
	 * @return The latest modification date if it exists or null.
	 */
	@Override
	public Date getModificationDate() {
		TaskAttribute modificationDateAttribute = getMappedAttribute(TaskAttribute.DATE_MODIFICATION);
		if (modificationDateAttribute != null) {
			return getTaskData().getAttributeMapper().getDateValue(modificationDateAttribute);
		}
		return null;
	}

	/**
	 * Sets the latest modification date in the relevant task attribute.
	 * 
	 * @param completionDate
	 *            The latest modification date
	 */
	@Override
	public void setModificationDate(Date completionDate) {
		TaskAttribute modificationDateAttribute = getMappedAttribute(TaskAttribute.DATE_MODIFICATION);
		if (modificationDateAttribute != null) {
			getTaskData().getAttributeMapper().setDateValue(modificationDateAttribute, completionDate);
		}
	}

	/**
	 * Provides access to the creation date if it exists.
	 * 
	 * @return The creation date if it exists or null.
	 */
	@Override
	public Date getCreationDate() {
		TaskAttribute creationDateAttribute = getMappedAttribute(TaskAttribute.DATE_CREATION);
		if (creationDateAttribute != null) {
			return getTaskData().getAttributeMapper().getDateValue(creationDateAttribute);
		}
		return null;
	}

	/**
	 * Sets the creation date in the relevant task attribute.
	 * 
	 * @param creationDate
	 *            The creation date
	 */
	@Override
	public void setCreationDate(Date creationDate) {
		TaskAttribute creationDateAttribute = getMappedAttribute(TaskAttribute.DATE_CREATION);
		if (creationDateAttribute != null) {
			getTaskData().getAttributeMapper().setDateValue(creationDateAttribute, creationDate);
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
			getTaskData().getAttributeMapper().setIntegerValue(attribute, Integer.valueOf(initialEffort));
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
		TaskAttribute attribute = getTaskData().getRoot().createAttribute(
				TaskAttribute.PREFIX_COMMENT + String.valueOf(count));
		attribute.getMetaData().defaults().setReadOnly(true).setType(TaskAttribute.TYPE_COMMENT);
		attribute.getMetaData().putValue(TaskAttribute.META_ASSOCIATED_ATTRIBUTE_ID,
				TaskAttribute.COMMENT_TEXT);
		TaskCommentMapper taskComment = TaskCommentMapper.createFrom(attribute);

		taskComment.setCommentId(String.valueOf(count));
		taskComment.setNumber(Integer.valueOf(count));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(tuleapArtifactComment.getSubmittedOn());
		Date creationDate = calendar.getTime();
		taskComment.setCreationDate(creationDate);
		taskComment.setText(tuleapArtifactComment.getBody());
		TuleapUser submitter = tuleapArtifactComment.getSubmitter();
		if (submitter != null) {
			TaskRepository taskRepository = attribute.getTaskData().getAttributeMapper().getTaskRepository();
			IRepositoryPerson repositoryPerson = taskRepository.createPerson(submitter.getEmail());
			repositoryPerson.setName(submitter.getUserName());
			taskComment.setAuthor(repositoryPerson);
		}
		taskComment.applyTo(attribute);
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
		TaskAttribute attribute = getTaskData().getRoot().createAttribute(
				TaskAttribute.PREFIX_ATTACHMENT + tuleapFieldName + "---" //$NON-NLS-1$
						+ tuleapAttachment.getAttachmentId());
		attribute.getMetaData().defaults().setType(TaskAttribute.TYPE_ATTACHMENT);
		TaskAttachmentMapper taskAttachment = TaskAttachmentMapper.createFrom(attribute);
		taskAttachment.setAttachmentId(tuleapAttachment.getAttachmentId());

		TuleapUser person = tuleapAttachment.getPerson();
		if (person != null) {
			IRepositoryPerson repositoryPerson = getTaskData().getAttributeMapper().getTaskRepository()
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
	 * Counts the number of existing comment attributes in the wrapped task data.
	 * 
	 * @return the number of comment task attributes present in the task data.
	 */
	private int getNumberOfCommentAttributes() {
		Set<String> keys = getTaskData().getRoot().getAttributes().keySet();
		int count = 0;
		for (String key : keys) {
			if (key.startsWith(TaskAttribute.PREFIX_COMMENT)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Update the associated select box according to the workflow if there is a workflow.
	 * 
	 * @param statusItemId
	 *            The new status value
	 * @param attribute
	 *            the task attribute to update
	 */
	private void updateStatusSelectBox(int statusItemId, TaskAttribute attribute) {
		// update the options of the status field
		AbstractTuleapSelectBox field = tracker.getStatusField();
		if (field instanceof TuleapSelectBox) {
			TuleapSelectBox selectBox = (TuleapSelectBox)field;
			if (selectBox.hasWorkflow()) {
				String currentOption = null;
				// We must look into the tracker because the select box only contains initial states at that
				// time
				for (TuleapSelectBoxItem item : field.getItems()) {
					if (item.getIdentifier() == statusItemId) {
						currentOption = item.getLabel();
						break;
					}
				}
				attribute.clearOptions();
				if (currentOption != null) {
					// currentOption can only be null if the workflow forbids the current modification...
					attribute.putOption(String.valueOf(statusItemId), currentOption);
				}
				Collection<TuleapSelectBoxItem> accessibleStates = selectBox.getWorkflow().accessibleStates(
						statusItemId);
				for (TuleapSelectBoxItem item : accessibleStates) {
					attribute.putOption(String.valueOf(item.getIdentifier()), item.getLabel());
				}
			}
		}
	}

	/**
	 * Returns the set of the field values.
	 * 
	 * @return The set of the field values, never null but potentially empty.
	 */
	public List<AbstractFieldValue> getFieldValues() {
		// returns all the tuleap field value in order to send them to the server
		// attachments are not uploaded with the same mechanism so no need to return them here
		// do not return the fields computed by tuleap or mylyn: creation date, completion date, id, etc
		List<AbstractFieldValue> result = new ArrayList<AbstractFieldValue>();
		// For the moment, we return all known values.
		// Later, an improvement will be to return only those values that have changed.
		for (TaskAttribute attribute : getTaskData().getRoot().getAttributes().values()) {
			Collection<AbstractTuleapField> fields = this.tracker.getFields();
			for (AbstractTuleapField field : fields) {
				int fieldId = field.getIdentifier();
				if (String.valueOf(fieldId).equals(attribute.getId()) && shouldBeSentToTheServer(fieldId)) {
					result.add(field.createFieldValue(attribute, fieldId));
				} else if (field instanceof TuleapString && ((TuleapString)field).isSemanticTitle()) {

					if (attribute.getId().equals(TaskAttribute.SUMMARY)) {
						LiteralFieldValue afieldValue = new LiteralFieldValue(tracker.getTitleField()
								.getIdentifier(), attribute.getValue());
						result.add(afieldValue);
					}
				} else if (field instanceof AbstractTuleapSelectBox
						&& ((AbstractTuleapSelectBox)field).isSemanticStatus()) {
					if (attribute.getId().equals(TaskAttribute.STATUS)) {
						// select box or multi select box (or check box)
						List<Integer> valueIds = new ArrayList<Integer>();
						// if (!attribute.getValues().isEmpty()) {
						for (String strValue : attribute.getValues()) {
							try {
								valueIds.add(Integer.valueOf(strValue));
							} catch (NumberFormatException e) {
								TuleapCoreActivator.log(e, false);
							}
						}
						// } else {
						// valueIds.add(Integer.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID));
						// }

						BoundFieldValue boundFieldValue = new BoundFieldValue(this.tracker.getStatusField()
								.getIdentifier(), valueIds);
						result.add(boundFieldValue);
					}
				} else if (field instanceof AbstractTuleapSelectBox
						&& ((AbstractTuleapSelectBox)field).isSemanticContributor()) {
					if (attribute.getId().equals(TaskAttribute.USER_ASSIGNED)) {
						// select box or multi select box (or check box)
						List<Integer> valueIds = new ArrayList<Integer>();
						// if (!attribute.getValues().isEmpty()) {
						for (String strValue : attribute.getValues()) {
							try {
								valueIds.add(Integer.valueOf(strValue));
							} catch (NumberFormatException e) {
								TuleapCoreActivator.log(e, false);
							}
						}
						// } else {
						// valueIds.add(Integer.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID));
						// }
						BoundFieldValue boundFieldValue = new BoundFieldValue(this.tracker
								.getContributorField().getIdentifier(), valueIds);
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
		return getTaskData().getRoot().getMappedAttribute(String.valueOf(fieldId));
	}

	/**
	 * Provides access to the TaskAttribute that contains the actual status value.
	 * 
	 * @return The status task attribute.
	 */
	private TaskAttribute getStatusTaskAttribute() {
		return getTaskData().getRoot().getAttribute(TaskAttribute.STATUS);
	}

	/**
	 * The status of the task.
	 * 
	 * @return the status of the task.
	 */
	public int getStatusAsInt() {
		String status = null;
		TaskAttribute attribute = getTaskData().getRoot().getMappedAttribute(TaskAttribute.STATUS);
		if (attribute != null) {
			status = getTaskData().getAttributeMapper().getValue(attribute);
		}
		if (status != null) {
			try {
				return Integer.parseInt(status);
			} catch (NumberFormatException e) {
				// do not log
			}
		}
		return INVALID_STATUS_ID;
	}

	/**
	 * Returns the existing mapped attribute with the given id, or {@code null} if an attribute with this id
	 * doesn't exist.
	 * 
	 * @param attributeKey
	 *            The task attribute id
	 * @return The existing mapped attribute, or null if it doesn't exist (or is not mapped)
	 */
	protected TaskAttribute getMappedAttribute(String attributeKey) {
		return getTaskData().getRoot().getMappedAttribute(attributeKey);
	}

	/**
	 * Returns the existing mapped attribute with the given id, or {@code null} if an attribute with this id
	 * doesn't exist.
	 * 
	 * @param attributeKey
	 *            The task attribute id
	 * @return The existing mapped attribute, or null if it doesn't exist (or is not mapped)
	 */
	protected TaskAttribute getMappedAttribute(int attributeKey) {
		return getTaskData().getRoot().getMappedAttribute(String.valueOf(attributeKey));
	}

	/**
	 * Creates the attribute with the given key and the given type under the root of the task data.
	 * 
	 * @param attributeKey
	 *            The key of the attribute
	 * @param type
	 *            The type of the attribute
	 * @return The attribute with the given key and the given type under the root of the task data
	 */
	protected TaskAttribute createAttribute(String attributeKey, String type) {
		TaskAttribute attribute;
		Field field = DefaultTaskSchema.getField(attributeKey);
		if (field != null) {
			attribute = field.createAttribute(getTaskData().getRoot());
		} else {
			attribute = getTaskData().getRoot().createMappedAttribute(attributeKey);
			if (type != null) {
				attribute.getMetaData().defaults().setType(type);
			}
		}
		return attribute;
	}

	/**
	 * Returns the writeable attribute with the given key and the given type.
	 * <p>
	 * If an attribute with the given key does not exists, a new one will be created.
	 * </p>
	 * 
	 * @param attributeKey
	 *            The key of the attribute
	 * @param type
	 *            The type of the attribute
	 * @return The writeable attribute with the given key and the given type
	 */
	protected TaskAttribute getWriteableAttribute(String attributeKey, String type) {
		TaskAttribute attribute = getTaskData().getRoot().getMappedAttribute(attributeKey);
		if (attribute == null) {
			attribute = createAttribute(attributeKey, type);
		}
		return attribute;
	}
}
