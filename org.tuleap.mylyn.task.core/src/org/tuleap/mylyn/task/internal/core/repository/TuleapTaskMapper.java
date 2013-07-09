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
package org.tuleap.mylyn.task.internal.core.repository;

import org.eclipse.mylyn.tasks.core.data.AbstractTaskSchema.Field;
import org.eclipse.mylyn.tasks.core.data.DefaultTaskSchema;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskMapper;

/**
 * The Tuleap Task Mapper is used to manipulate the task data with a higher level of abstraction.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapTaskMapper extends TaskMapper {

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
	public static final String PROJECT_NAME = "tuleap_task_data_project_name"; //$NON-NLS-1$

	/**
	 * The identifier of the tracker name task attribute.
	 */
	public static final String TRACKER_NAME = "tuleap_task_data_tracker_name"; //$NON-NLS-1$

	/**
	 * The task data.
	 */
	private TaskData taskData;

	/**
	 * The constructor.
	 * 
	 * @param data
	 *            The task data
	 */
	public TuleapTaskMapper(TaskData data) {
		super(data);
		this.taskData = data;
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
	 * Sets the tracker if of the task data.
	 * 
	 * @param trackerId
	 *            the tracker id
	 */
	public void setTrackerId(int trackerId) {
		this.setIntValue(TuleapTaskMapper.TRACKER_ID, trackerId);
	}

	/**
	 * Returns the tracker id of the task data or -1 if it can't be found.
	 * 
	 * @return The tracker id of the task data or -1 if it can't be found
	 */
	public int getTrackerId() {
		return this.getIntValue(TuleapTaskMapper.TRACKER_ID);
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
	 * Returns a writeable task data attribute with the given key and the given type.
	 * 
	 * @param attributeKey
	 *            The key of the task data attribute
	 * @param type
	 *            The type of the task data attribute
	 * @return A writeable task data attribute with the given key and the given type
	 */
	private TaskAttribute getWriteableAttribute(String attributeKey, String type) {
		TaskAttribute attribute = taskData.getRoot().getMappedAttribute(attributeKey);
		if (attribute == null) {
			attribute = createAttribute(attributeKey, type);
		}
		return attribute;
	}

	/**
	 * Creates a task data attribute with the given key and the given type.
	 * 
	 * @param attributeKey
	 *            The key of the task data attribute
	 * @param type
	 *            The type of the task data attribute
	 * @return A task data attribute with the given key and the given type
	 */
	private TaskAttribute createAttribute(String attributeKey, String type) {
		TaskAttribute attribute;
		Field field = DefaultTaskSchema.getField(attributeKey);
		if (field != null) {
			attribute = field.createAttribute(taskData.getRoot());
		} else {
			attribute = taskData.getRoot().createMappedAttribute(attributeKey);
			if (type != null) {
				attribute.getMetaData().defaults().setType(type);
			}
		}
		return attribute;
	}
}
