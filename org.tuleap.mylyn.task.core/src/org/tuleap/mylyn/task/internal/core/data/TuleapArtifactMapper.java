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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMetaData;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.data.AgileTaskKindUtil;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.field.AbstractTuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTracker;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessagesKeys;

/**
 * The Tuleap Configurable Element Mapper will be used to manipulate the task data model from Mylyn with a
 * higher level of abstraction.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapArtifactMapper extends TuleapTaskMapper {

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
	 * Initialize an empty task data from the given configuration.
	 */
	public void initializeEmptyTaskData() {
		createTaskKindTaskAttribute();

		// The project id and the configuration id
		setProjectId(tracker.getTuleapProjectConfiguration().getIdentifier());
		setConfigurationId(tracker.getIdentifier());

		createCreationDateTaskAttribute();
		createLastUpdateDateTaskAttribute();
		createCompletionDateTaskAttribute();
		createNewCommentTaskAttribute();
		createSubmittedByTaskAttribute();
		createTaskKeyAttribute();

		// Default attributes
		TaskAttribute root = taskData.getRoot();
		Collection<AbstractTuleapField> fields = tracker.getFields();
		for (AbstractTuleapField abstractTuleapField : fields) {
			if (abstractTuleapField.needsTaskAttributeForInitialization()) {
				abstractTuleapField.createTaskAttribute(root);
			}
		}

		TuleapProjectConfiguration projectConfiguration = tracker.getTuleapProjectConfiguration();
		if (projectConfiguration.isMilestoneTracker(tracker.getIdentifier())) {
			AgileTaskKindUtil.setAgileTaskKind(taskData, AgileTaskKindUtil.TASK_KIND_MILESTONE);
		} else if (projectConfiguration.isBacklogTracker(tracker.getIdentifier())) {
			// FIXME Is this really useful?
			AgileTaskKindUtil.setAgileTaskKind(taskData, AgileTaskKindUtil.TASK_KIND_BACKLOG_ITEM);
		} else {
			AgileTaskKindUtil.setAgileTaskKind(taskData, AgileTaskKindUtil.TASK_KIND_ARTIFACT);
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
	 */
	private void createTaskKindTaskAttribute() {
		TaskAttribute attribute = taskData.getRoot().createAttribute(TaskAttribute.TASK_KIND);
		String name = tracker.getLabel();
		if (name != null) {
			attribute.setValue(name);
		} else {
			attribute.setValue(TuleapMylynTasksMessages
					.getString(TuleapMylynTasksMessagesKeys.defaultConfigurationName));
		}
	}

	/**
	 * Sets the identifier of the configuration of the element (tracker id, backlog item type id, etc).
	 * 
	 * @param configurationId
	 *            The identifier of the configuration of the element
	 */
	private void setConfigurationId(int configurationId) {
		// should not appear in the attribute part so no task attribute type!
		TaskAttribute configurationIdAtt = taskData.getRoot().getMappedAttribute(TRACKER_ID);
		if (configurationIdAtt == null) {
			configurationIdAtt = taskData.getRoot().createMappedAttribute(TRACKER_ID);
		}
		taskData.getAttributeMapper().setIntegerValue(configurationIdAtt, Integer.valueOf(configurationId));
	}

	/**
	 * Returns the identifier of the configuration or INVALID_CONFIGURATION_ID otherwise.
	 * 
	 * @return The identifier of the configuration or INVALID_CONFIGURATION_ID otherwise.
	 */
	public int getConfigurationId() {
		int configurationId = TuleapTaskIdentityUtil.IRRELEVANT_ID;

		if (this.taskData.isNew()) {
			TaskAttribute configurationIdAtt = taskData.getRoot().getMappedAttribute(TRACKER_ID);
			if (configurationIdAtt != null) {
				configurationId = taskData.getAttributeMapper().getIntegerValue(configurationIdAtt)
						.intValue();
			}
		} else {
			configurationId = TuleapTaskIdentityUtil.getConfigurationIdFromTaskDataId(taskData.getTaskId());
		}

		return configurationId;
	}

	/**
	 * Sets the identifier of the project.
	 * 
	 * @param projectId
	 *            The identifier of the project
	 */
	private void setProjectId(int projectId) {
		TaskAttribute projectIdAtt = taskData.getRoot().getMappedAttribute(PROJECT_ID);
		if (projectIdAtt == null) {
			projectIdAtt = taskData.getRoot().createMappedAttribute(PROJECT_ID);
		}
		taskData.getAttributeMapper().setIntegerValue(projectIdAtt, Integer.valueOf(projectId));
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
				String currentOption = attribute.getOption(String.valueOf(statusItemId));
				attribute.clearOptions();
				if (currentOption != null
						&& ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID != statusItemId) {
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
		AbstractTuleapField field = tracker.getFieldById(fieldId);
		if (field instanceof TuleapSelectBox) {
			TuleapSelectBox selectBox = (TuleapSelectBox)field;
			selectBox.updateOptionsWithWorkflow(attribute);
		}
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
			Collection<AbstractTuleapField> fields = this.tracker.getFields();
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
				} else if (abstractTuleapField instanceof TuleapString
						&& ((TuleapString)abstractTuleapField).isSemanticTitle()) {

					if (attribute.getId().equals(TaskAttribute.SUMMARY)) {
						LiteralFieldValue afieldValue = new LiteralFieldValue(tracker.getTitleField()
								.getIdentifier(), attribute.getValue());
						result.add(afieldValue);
					}

				} else if (abstractTuleapField instanceof AbstractTuleapSelectBox
						&& ((AbstractTuleapSelectBox)abstractTuleapField).isSemanticStatus()) {

					if (attribute.getId().equals(TaskAttribute.STATUS)) {
						// select box or multi select box (or check box)
						List<Integer> valueIds = new ArrayList<Integer>();
						for (String strValue : attribute.getValues()) {
							try {
								valueIds.add(Integer.valueOf(strValue));
							} catch (NumberFormatException e) {
								// TODO Add log about non integer value
							}
						}

						for (String strValue : attribute.getOptions().keySet()) {
							if (!strValue.equals(String
									.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID))) {
								try {
									valueIds.add(Integer.valueOf(strValue));
								} catch (NumberFormatException e) {
									// TODO Add log about non integer value
								}
							}

						}
						BoundFieldValue boundFieldValue = new BoundFieldValue(this.tracker.getStatusField()
								.getIdentifier(), valueIds);
						result.add(boundFieldValue);
					}

				} else if (abstractTuleapField instanceof AbstractTuleapSelectBox
						&& ((AbstractTuleapSelectBox)abstractTuleapField).isSemanticContributor()) {
					if (attribute.getId().equals(TaskAttribute.USER_ASSIGNED)) {
						// select box or multi select box (or check box)
						List<Integer> valueIds = new ArrayList<Integer>();
						for (String strValue : attribute.getValues()) {
							try {
								valueIds.add(Integer.valueOf(strValue));
							} catch (NumberFormatException e) {
								// TODO Add log about non integer value
							}
						}
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
	public int getStatus() {
		TaskAttribute attribute = taskData.getRoot().getMappedAttribute(TaskAttribute.STATUS);
		if (attribute != null && attribute.getValue() != null) {
			try {
				return Integer.parseInt(attribute.getValue());
			} catch (NumberFormatException e) {
				// do not log
			}
		}
		return INVALID_STATUS_ID;
	}
}
