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
package org.tuleap.mylyn.task.internal.tests.client;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMetaData;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskMapper;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapDate;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFloat;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapInteger;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapMultiSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapOpenList;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapText;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.workflow.TuleapWorkflowTransition;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.repository.TuleapAttributeMapper;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests of the new Tuleap Task Mapper.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
@SuppressWarnings("restriction")
public class TuleapTaskMapperTests {

	/**
	 * The task repository.
	 */
	private TaskRepository repository;

	/**
	 * The tracker configuration.
	 */
	private TuleapTrackerConfiguration tuleapTrackerConfiguration;

	/**
	 * The URL of the repository.
	 */
	private String repositoryUrl;

	/**
	 * The kind of the connector.
	 */
	private String connectorKind;

	/**
	 * The id of the tracker.
	 */
	private int trackerId;

	/**
	 * The id of the project.
	 */
	private int projectId;

	/**
	 * The repository connector.
	 */
	private ITuleapRepositoryConnector repositoryConnector;

	/**
	 * The name of the configuration.
	 */
	private String trackerName;

	/**
	 * The configuration of the tuleap instance.
	 */
	private TuleapServerConfiguration tuleapServerConfiguration;

	/**
	 * The configuration of the tuleap project.
	 */
	private TuleapProjectConfiguration tuleapProjectConfiguration;

	/**
	 * The name of the items.
	 */
	private String itemName;

	/**
	 * The name of the project.
	 */
	private String projectName = "Project Name"; //$NON-NLS-1$

	/**
	 * The task data wrapped by the mapper used for tests.
	 */
	private TaskData taskData;

	/**
	 * The mapper used for tests.
	 */
	private TuleapTaskMapper mapper;

	/**
	 * The attribute mapper used by the mapper under test.
	 */
	private TuleapAttributeMapper attributeMapper;

	/**
	 * Verification of basic initialization with an empty configuration just to make sure the default task
	 * attributes are created correctly.
	 */
	@Test
	public void testInitializeEmptyTaskDataWithEmptyTrackerConfiguration() {
		mapper.initializeEmptyTaskData();
		TaskAttribute root = taskData.getRoot();

		TaskAttribute att = root.getAttribute(TaskAttribute.TASK_KIND);
		assertNotNull(att);
		assertEquals(tuleapTrackerConfiguration.getName(), att.getValue());
		att = root.getMappedAttribute(TaskAttribute.DATE_CREATION);
		assertNotNull(att);
		TaskAttributeMetaData metadata = att.getMetaData();
		assertEquals(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.CreationDate"), //$NON-NLS-1$
				metadata.getLabel());
		assertEquals(TaskAttribute.TYPE_DATE, metadata.getType());
		assertFalse(metadata.isReadOnly());

		assertEquals(projectId, mapper.getProjectId());
		assertEquals(trackerId, mapper.getTrackerId());

		// Also check that project id & tracker id task attributes are correctly created since they will be
		// useful to mylyn
		att = root.getAttribute(TuleapTaskMapper.PROJECT_ID);
		assertNotNull(att);
		assertEquals(String.valueOf(projectId), att.getValue());

		att = root.getAttribute(TuleapTaskMapper.TRACKER_ID);
		assertNotNull(att);
		assertEquals(String.valueOf(trackerId), att.getValue());

		// Check that the task_key attribute has been created
		att = root.getAttribute(TaskAttribute.TASK_KEY);
		assertNotNull(att);
		assertTrue(att.getMetaData().isReadOnly());

		att = root.getAttribute(TaskAttribute.DATE_MODIFICATION);
		assertNotNull(att);
		metadata = att.getMetaData();
		assertEquals(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.LastModificationDate"), //$NON-NLS-1$
				metadata.getLabel());
		assertEquals(TaskAttribute.TYPE_DATE, metadata.getType());
		assertFalse(metadata.isReadOnly());

		att = root.getAttribute(TaskAttribute.DATE_COMPLETION);
		assertNotNull(att);
		metadata = att.getMetaData();
		assertEquals(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.CompletionDate"), //$NON-NLS-1$
				metadata.getLabel());
		assertEquals(TaskAttribute.TYPE_DATE, metadata.getType());
		assertFalse(metadata.isReadOnly());

		att = root.getAttribute(TaskAttribute.COMMENT_NEW);
		assertNotNull(att);
		metadata = att.getMetaData();
		assertEquals(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.NewComment"), //$NON-NLS-1$
				metadata.getLabel());
		assertEquals(TaskAttribute.TYPE_LONG_RICH_TEXT, metadata.getType());
		assertFalse(metadata.isReadOnly());

		att = root.getAttribute(TaskAttribute.USER_REPORTER);
		assertNotNull(att);
		metadata = att.getMetaData();
		assertEquals(TaskAttribute.TYPE_PERSON, metadata.getType());
		assertTrue(metadata.isReadOnly());
	}

	/**
	 * Check the correct creation of a semantic title attribute.
	 */
	@Test
	public void testInitializeEmptyTaskDataWithSemanticTitle() {
		int id = 401;
		tuleapTrackerConfiguration.addField(newSemanticTitle(id));
		mapper.initializeEmptyTaskData();

		TaskAttribute att = taskData.getRoot().getMappedAttribute(TaskAttribute.SUMMARY);
		assertNotNull(att);
		TaskAttributeMetaData metadata = att.getMetaData();
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, metadata.getType());
		assertEquals(getLabelFromId(id), metadata.getLabel());
	}

	/**
	 * Check the correct creation of a date attribute.
	 */
	@Test
	public void testInitializeEmptyTaskDataWithDate() {
		int id = 402;
		tuleapTrackerConfiguration.addField(newTuleapDate(id));
		mapper.initializeEmptyTaskData();

		TaskAttribute att = taskData.getRoot().getMappedAttribute(String.valueOf(id));
		assertNotNull(att);
		TaskAttributeMetaData metadata = att.getMetaData();
		assertEquals(TaskAttribute.TYPE_DATE, metadata.getType());
		assertEquals(getLabelFromId(id), metadata.getLabel());
	}

	/**
	 * Check the correct creation of a string attribute.
	 */
	@Test
	public void testInitializeEmptyTaskDataWithString() {
		int id = 403;
		tuleapTrackerConfiguration.addField(newTuleapString(id));
		mapper.initializeEmptyTaskData();

		TaskAttribute att = taskData.getRoot().getMappedAttribute(String.valueOf(id));
		assertNotNull(att);
		TaskAttributeMetaData metadata = att.getMetaData();
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, metadata.getType());
		assertEquals(getLabelFromId(id), metadata.getLabel());
	}

	/**
	 * Check the correct creation of a text attribute.
	 */
	@Test
	public void testInitializeEmptyTaskDataWithText() {
		int id = 403;
		tuleapTrackerConfiguration.addField(newTuleapText(id));
		mapper.initializeEmptyTaskData();

		TaskAttribute att = taskData.getRoot().getMappedAttribute(String.valueOf(id));
		assertNotNull(att);
		TaskAttributeMetaData metadata = att.getMetaData();
		assertEquals(TaskAttribute.TYPE_LONG_RICH_TEXT, metadata.getType());
		assertEquals(getLabelFromId(id), metadata.getLabel());
	}

	/**
	 * Check the correct creation of an integer attribute.
	 */
	@Test
	public void testInitializeEmptyTaskDataWithInteger() {
		int id = 403;
		tuleapTrackerConfiguration.addField(newTuleapInteger(id));
		mapper.initializeEmptyTaskData();

		TaskAttribute att = taskData.getRoot().getMappedAttribute(String.valueOf(id));
		assertNotNull(att);
		TaskAttributeMetaData metadata = att.getMetaData();
		assertEquals(TaskAttribute.TYPE_INTEGER, metadata.getType());
		assertEquals(getLabelFromId(id), metadata.getLabel());
	}

	/**
	 * Check the correct creation of a float attribute.
	 */
	@Test
	public void testInitializeEmptyTaskDataWithFloat() {
		int id = 403;
		tuleapTrackerConfiguration.addField(newTuleapFloat(id));
		mapper.initializeEmptyTaskData();

		TaskAttribute att = taskData.getRoot().getMappedAttribute(String.valueOf(id));
		assertNotNull(att);
		TaskAttributeMetaData metadata = att.getMetaData();
		assertEquals(TaskAttribute.TYPE_DOUBLE, metadata.getType());
		assertEquals(getLabelFromId(id), metadata.getLabel());
	}

	/**
	 * Check the correct creation of an open list attribute.
	 */
	@Test
	public void testInitializeEmptyTaskDataWithOpenList() {
		int id = 403;
		tuleapTrackerConfiguration.addField(newTuleapOpenList(id));
		mapper.initializeEmptyTaskData();

		TaskAttribute att = taskData.getRoot().getMappedAttribute(String.valueOf(id));
		assertNotNull(att);
		TaskAttributeMetaData metadata = att.getMetaData();
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, metadata.getType());
		assertEquals(getLabelFromId(id), metadata.getLabel());
	}

	/**
	 * Check the correct creation of a select box attribute without special semantic.
	 */
	@Test
	public void testInitializeEmptyTaskDataWithSemanticStatus() {
		int id = 403;
		tuleapTrackerConfiguration.addField(newSemanticStatus(id));
		mapper.initializeEmptyTaskData();

		TaskAttribute att = taskData.getRoot().getMappedAttribute(String.valueOf(id));
		assertNull(att);
		att = taskData.getRoot().getMappedAttribute(TaskAttribute.STATUS);
		assertNotNull(att);
		assertEquals(5, att.getOptions().size());

		TaskAttributeMetaData metadata = att.getMetaData();
		assertEquals(TaskAttribute.TYPE_SINGLE_SELECT, metadata.getType());
		assertEquals(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.Status"), //$NON-NLS-1$
				metadata.getLabel());
		String lbl = getLabelFromId(0);
		assertEquals(lbl, att.getOption("0")); //$NON-NLS-1$
		lbl = getLabelFromId(1);
		assertEquals(lbl, att.getOption("1")); //$NON-NLS-1$
		lbl = getLabelFromId(2);
		assertEquals(lbl, att.getOption("2")); //$NON-NLS-1$
		lbl = getLabelFromId(3);
		assertEquals(lbl, att.getOption("3")); //$NON-NLS-1$
		assertEquals("", att.getOption(String.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID))); //$NON-NLS-1$ 
	}

	/**
	 * Check the correct creation of a select box attribute without special semantic.
	 */
	@Test
	public void testInitializeEmptyTaskDataWithSemanticStatusWithWorkflow() {
		int id = 403;
		tuleapTrackerConfiguration.addField(newSemanticStatusWithWorkflow(id));
		mapper.initializeEmptyTaskData();

		TaskAttribute att = taskData.getRoot().getMappedAttribute(TaskAttribute.STATUS);
		assertNotNull(att);
		TaskAttributeMetaData metadata = att.getMetaData();
		assertEquals(TaskAttribute.TYPE_SINGLE_SELECT, metadata.getType());
		assertEquals(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.Status"), //$NON-NLS-1$
				metadata.getLabel());

		// No value is assigned to the field
		// => as if status were equal to 100, only 1 state should be accessible, state 0
		// There is a workflow, state 100 is not present in the options.

		assertEquals(1, att.getOptions().size());
		String lbl = getLabelFromId(0);
		assertEquals(lbl, att.getOption("0")); //$NON-NLS-1$
	}

	/**
	 * Check the correct creation of a select box attribute without special semantic.
	 */
	@Test
	public void testInitializeEmptyTaskDataWithSelectBoxWithWorkflow() {
		int id = 403;
		TuleapSelectBox selectBox = newTuleapSelectBox(id);
		// We create 4 transitions:
		// 100 -> 0 -> 1 -> 2 -> 3 & 2 -> 1
		TuleapWorkflowTransition transition = new TuleapWorkflowTransition();
		transition.setFrom(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID);
		transition.setTo(0);
		selectBox.getWorkflow().addTransition(transition);
		transition = new TuleapWorkflowTransition();
		transition.setFrom(0);
		transition.setTo(1);
		selectBox.getWorkflow().addTransition(transition);
		transition = new TuleapWorkflowTransition();
		transition.setFrom(1);
		transition.setTo(2);
		selectBox.getWorkflow().addTransition(transition);
		transition = new TuleapWorkflowTransition();
		transition.setFrom(2);
		transition.setTo(3);
		selectBox.getWorkflow().addTransition(transition);
		transition = new TuleapWorkflowTransition();
		transition.setFrom(2);
		transition.setTo(1);
		selectBox.getWorkflow().addTransition(transition);
		tuleapTrackerConfiguration.addField(selectBox);
		mapper.initializeEmptyTaskData();

		// No value is assigned to the field
		// => as if status were equal to 100, only 1 state should be accessible, state 0

		TaskAttribute att = taskData.getRoot().getMappedAttribute(String.valueOf(id));
		assertNotNull(att);
		TaskAttributeMetaData metadata = att.getMetaData();
		assertEquals(TaskAttribute.TYPE_SINGLE_SELECT, metadata.getType());
		assertEquals(getLabelFromId(id), metadata.getLabel());

		assertEquals(1, att.getOptions().size());
		String lbl = getLabelFromId(0);
		assertEquals(lbl, att.getOption("0")); //$NON-NLS-1$
	}

	/**
	 * Check the correct creation of a multi select box attribute without special semantic.
	 */
	@Test
	public void testInitializeEmptyTaskDataWithMultiSelectBox() {
		int id = 405;
		tuleapTrackerConfiguration.addField(newTuleapMultiSelectBox(id));
		mapper.initializeEmptyTaskData();

		TaskAttribute att = taskData.getRoot().getMappedAttribute(String.valueOf(id));
		assertNotNull(att);
		TaskAttributeMetaData metadata = att.getMetaData();
		assertEquals(TaskAttribute.TYPE_MULTI_SELECT, metadata.getType());
		assertEquals(getLabelFromId(id), metadata.getLabel());
	}

	/**
	 * Check the correct creation of a multi select box attribute without special semantic.
	 */
	@Test
	public void testInitializeEmptyTaskDataWithSemanticContributor() {
		int id = 408;
		tuleapTrackerConfiguration.addField(newSemanticContributorSingle(id));
		mapper.initializeEmptyTaskData();

		TaskAttribute att = taskData.getRoot().getMappedAttribute(TaskAttribute.USER_ASSIGNED);
		assertNotNull(att);
		TaskAttributeMetaData metadata = att.getMetaData();
		assertEquals(TaskAttribute.TYPE_SINGLE_SELECT, metadata.getType());
		assertEquals(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.AssignedToLabel"), metadata //$NON-NLS-1$
				.getLabel());
		assertEquals(TaskAttribute.KIND_PEOPLE, metadata.getKind());
	}

	/**
	 * Test of the setStatus() method. Checks that the completion date becomes set when status becomes closed,
	 * and becomes unset when status becomes open.
	 */
	@Test
	public void testSetStatus() {
		int statusId = 555;
		tuleapTrackerConfiguration.addField(newSemanticStatus(statusId));
		mapper.initializeEmptyTaskData();

		assertEquals("", taskData.getRoot().getAttribute(TaskAttribute.STATUS).getValue()); //$NON-NLS-1$

		String statusOpen0 = "0"; //$NON-NLS-1$
		mapper.setStatus(statusOpen0);

		assertEquals(statusOpen0, taskData.getRoot().getAttribute(TaskAttribute.STATUS).getValue());
		assertEquals(statusOpen0, mapper.getStatus());
		assertNull(mapper.getCompletionDate());

		// go to closed state, completion date must be non-null
		String statusClosed2 = "2"; //$NON-NLS-1$
		mapper.setStatus(statusClosed2);

		assertEquals(statusClosed2, taskData.getRoot().getAttribute(TaskAttribute.STATUS).getValue());
		assertEquals(statusClosed2, mapper.getStatus());
		assertNotNull(mapper.getCompletionDate());

		// Back to open, completion date must be null
		String statusOpen1 = "1"; //$NON-NLS-1$
		mapper.setStatus(statusOpen1);

		assertEquals(statusOpen1, taskData.getRoot().getAttribute(TaskAttribute.STATUS).getValue());
		assertEquals(statusOpen1, mapper.getStatus());
		assertNull(mapper.getCompletionDate());
	}

	/**
	 * Test of the setStatus() method when there is a workflow. Checks that the completion date becomes set
	 * when status becomes closed, and becomes unset when status becomes open, and that available status
	 * change according to current status.
	 */
	@Test
	public void testSetStatusWithWorkflow() {
		int statusId = 556;
		// 100 -> 0 -> 1 <-> 2 -> 3
		tuleapTrackerConfiguration.addField(newSemanticStatusWithWorkflow(statusId));
		mapper.initializeEmptyTaskData();

		assertEquals("", taskData.getRoot().getAttribute(TaskAttribute.STATUS).getValue()); //$NON-NLS-1$

		String statusOpen0 = "0"; //$NON-NLS-1$
		String statusOpen1 = "1"; //$NON-NLS-1$
		String statusClosed2 = "2"; //$NON-NLS-1$
		mapper.setStatus(statusOpen0);

		assertEquals(statusOpen0, taskData.getRoot().getAttribute(TaskAttribute.STATUS).getValue());
		assertEquals(statusOpen0, mapper.getStatus());
		assertNull(mapper.getCompletionDate());
		assertEquals(2, taskData.getRoot().getAttribute(TaskAttribute.STATUS).getOptions().size());
		assertNotNull(taskData.getRoot().getAttribute(TaskAttribute.STATUS).getOption(statusOpen1));
		assertNotNull(taskData.getRoot().getAttribute(TaskAttribute.STATUS).getOption(statusOpen0));

		// go to closed state, completion date must be non-null
		mapper.setStatus(statusOpen1);

		assertEquals(statusOpen1, taskData.getRoot().getAttribute(TaskAttribute.STATUS).getValue());
		assertEquals(statusOpen1, mapper.getStatus());
		assertNull(mapper.getCompletionDate());
		assertEquals(2, taskData.getRoot().getAttribute(TaskAttribute.STATUS).getOptions().size());
		assertNotNull(taskData.getRoot().getAttribute(TaskAttribute.STATUS).getOption(statusOpen1));
		assertNotNull(taskData.getRoot().getAttribute(TaskAttribute.STATUS).getOption(statusClosed2));

		// go to closed state, completion date must be non-null
		mapper.setStatus(statusClosed2);

		assertEquals(statusClosed2, taskData.getRoot().getAttribute(TaskAttribute.STATUS).getValue());
		assertEquals(statusClosed2, mapper.getStatus());
		assertNotNull(mapper.getCompletionDate());
		assertEquals(3, taskData.getRoot().getAttribute(TaskAttribute.STATUS).getOptions().size());
		assertNotNull(taskData.getRoot().getAttribute(TaskAttribute.STATUS).getOption(statusOpen1));
		assertNotNull(taskData.getRoot().getAttribute(TaskAttribute.STATUS).getOption(statusClosed2));
		assertNotNull(taskData.getRoot().getAttribute(TaskAttribute.STATUS).getOption("3")); //$NON-NLS-1$

		// Back to open, completion date must be null
		mapper.setStatus(statusOpen1);

		assertEquals(statusOpen1, taskData.getRoot().getAttribute(TaskAttribute.STATUS).getValue());
		assertEquals(statusOpen1, mapper.getStatus());
		assertNull(mapper.getCompletionDate());
	}

	/**
	 * Test task url getter and setter.
	 */
	@Test
	public void testTaskURLAccessors() {
		// There is no specific field description in the configuration
		mapper.initializeEmptyTaskData();
		assertEquals("", mapper.getTaskUrl()); //$NON-NLS-1$
		String url = "http://www.tuleap.net/some/url/123"; //$NON-NLS-1$
		mapper.setTaskUrl(url);

		assertEquals(url, taskData.getRoot().getAttribute(TaskAttribute.TASK_URL).getValue());
		assertEquals(url, mapper.getTaskUrl());
	}

	/**
	 * Test the assignment of the task with a single select box configuration.
	 */
	@Test
	public void testSetAssignedToSingle() {
		int id = 408;
		tuleapTrackerConfiguration.addField(newSemanticContributorSingle(id));
		mapper.initializeEmptyTaskData();
		mapper.setAssignedTo(Lists.newArrayList(Integer.valueOf(1)));

		assertEquals("1", taskData.getRoot().getAttribute(TaskAttribute.USER_ASSIGNED).getValue()); //$NON-NLS-1$
	}

	/**
	 * Test the assignment of the task with a multiple select box configuration.
	 */
	@Test
	public void testSetAssignedToMultiple() {
		int id = 408;
		tuleapTrackerConfiguration.addField(newSemanticContributorMultiple(id));
		mapper.initializeEmptyTaskData();
		List<Integer> valueIds = new ArrayList<Integer>();
		valueIds.add(Integer.valueOf(117));
		valueIds.add(Integer.valueOf(1023));
		mapper.setAssignedTo(valueIds);

		List<String> values = taskData.getRoot().getAttribute(TaskAttribute.USER_ASSIGNED).getValues();
		if ("117".equals(values.get(0))) { //$NON-NLS-1$
			assertEquals("1023", values.get(1)); //$NON-NLS-1$
		} else if ("1023".equals(values.get(0))) { //$NON-NLS-1$
			assertEquals("117", values.get(1)); //$NON-NLS-1$
		} else {
			fail("Wrong values received"); //$NON-NLS-1$
		}
	}

	/**
	 * Creates a new Tuleap Date Field.
	 * 
	 * @param id
	 *            the id
	 * @return The created field
	 */
	private TuleapDate newTuleapDate(int id) {
		TuleapDate result = new TuleapDate(id);
		setDescriptionAndLabelFromId(result);
		return result;
	}

	/**
	 * Sets the description and label of the given field to a value computed from the given id.
	 * 
	 * @param result
	 *            The field to update
	 */
	private void setDescriptionAndLabelFromId(AbstractTuleapField result) {
		result.setDescription(getDescriptionFromId(result.getIdentifier()));
		result.setLabel(getLabelFromId(result.getIdentifier()));
	}

	/**
	 * Compute a description from an id for tests.
	 * 
	 * @param id
	 *            the id to use
	 * @return a description for tests.
	 */
	private String getDescriptionFromId(int id) {
		return "Description for " + id; //$NON-NLS-1$
	}

	/**
	 * Compute a label from an id for tests.
	 * 
	 * @param id
	 *            id to use
	 * @return A label for tests.
	 */
	private String getLabelFromId(int id) {
		return "Label for " + id; //$NON-NLS-1$
	}

	/**
	 * Creates a new Tuleap float Field.
	 * 
	 * @param id
	 *            the id
	 * @return The created field
	 */
	private TuleapFloat newTuleapFloat(int id) {
		TuleapFloat result = new TuleapFloat(id);
		setDescriptionAndLabelFromId(result);
		return result;
	}

	/**
	 * Creates a new Tuleap integer Field.
	 * 
	 * @param id
	 *            the id
	 * @return The created field
	 */
	private TuleapInteger newTuleapInteger(int id) {
		TuleapInteger result = new TuleapInteger(id);
		setDescriptionAndLabelFromId(result);
		return result;
	}

	/**
	 * Creates a new Tuleap string Field.
	 * 
	 * @param id
	 *            the id
	 * @return The created field
	 */
	private TuleapString newTuleapString(int id) {
		TuleapString result = new TuleapString(id);
		setDescriptionAndLabelFromId(result);
		return result;
	}

	/**
	 * Creates a new Tuleap string Field with the "title" semantic.
	 * 
	 * @param id
	 *            the id
	 * @return The created field
	 */
	private TuleapString newSemanticTitle(int id) {
		TuleapString result = newTuleapString(id);
		result.setSemanticTitle(true);
		setDescriptionAndLabelFromId(result);
		return result;
	}

	/**
	 * Creates a new Tuleap text Field.
	 * 
	 * @param id
	 *            the id
	 * @return The created field
	 */
	private TuleapText newTuleapText(int id) {
		TuleapText result = new TuleapText(id);
		setDescriptionAndLabelFromId(result);
		return result;
	}

	/**
	 * Creates a new Tuleap open list Field (list of strings separated by commas).
	 * 
	 * @param id
	 *            the id
	 * @return The created field
	 */
	private TuleapOpenList newTuleapOpenList(int id) {
		TuleapOpenList result = new TuleapOpenList(id);
		setDescriptionAndLabelFromId(result);
		return result;
	}

	/**
	 * Creates a new Tuleap select box Field with 4 items.
	 * 
	 * @param id
	 *            the id
	 * @return The created field
	 */
	private TuleapSelectBox newTuleapSelectBox(int id) {
		TuleapSelectBox result = new TuleapSelectBox(id);
		setDescriptionAndLabelFromId(result);
		TuleapSelectBoxItem item0 = newTuleapSelectBoxItem(0);
		TuleapSelectBoxItem item1 = newTuleapSelectBoxItem(1);
		TuleapSelectBoxItem item2 = newTuleapSelectBoxItem(2);
		TuleapSelectBoxItem item3 = newTuleapSelectBoxItem(3);
		result.addItem(item0);
		result.addItem(item1);
		result.addItem(item2);
		result.addItem(item3);
		return result;
	}

	/**
	 * Creates a new Tuleap select box Field with the semantic "status" and 4 fields among which the 3 first
	 * are open and the two others are closed.
	 * 
	 * @param id
	 *            the id
	 * @return The created field
	 */
	private TuleapSelectBox newSemanticStatus(int id) {
		TuleapSelectBox result = new TuleapSelectBox(id);
		setDescriptionAndLabelFromId(result);
		TuleapSelectBoxItem item0 = newTuleapSelectBoxItem(0);
		TuleapSelectBoxItem item1 = newTuleapSelectBoxItem(1);
		TuleapSelectBoxItem item2 = newTuleapSelectBoxItem(2);
		TuleapSelectBoxItem item3 = newTuleapSelectBoxItem(3);
		result.addItem(item0);
		result.addItem(item1);
		result.addItem(item2);
		result.addItem(item3);
		result.getOpenStatus().add(item0);
		result.getOpenStatus().add(item1);
		result.getClosedStatus().add(item2);
		result.getClosedStatus().add(item3);
		return result;
	}

	/**
	 * Creates a new Tuleap select box Field with the semantic "status" and 4 fields among which the 3 first
	 * are open and the two others are closed.
	 * 
	 * @param id
	 *            the id
	 * @return The created field
	 */
	private TuleapSelectBox newSemanticStatusWithWorkflow(int id) {
		TuleapSelectBox result = newSemanticStatus(id);

		// 100 -> 0 -> 1 <-> 2 -> 3
		TuleapWorkflowTransition transition = new TuleapWorkflowTransition();
		transition.setFrom(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID);
		transition.setTo(0);
		result.getWorkflow().addTransition(transition);
		transition = new TuleapWorkflowTransition();
		transition.setFrom(0);
		transition.setTo(1);
		result.getWorkflow().addTransition(transition);
		transition = new TuleapWorkflowTransition();
		transition.setFrom(1);
		transition.setTo(2);
		result.getWorkflow().addTransition(transition);
		transition = new TuleapWorkflowTransition();
		transition.setFrom(2);
		transition.setTo(3);
		result.getWorkflow().addTransition(transition);
		transition = new TuleapWorkflowTransition();
		transition.setFrom(2);
		transition.setTo(1);
		result.getWorkflow().addTransition(transition);
		return result;
	}

	/**
	 * Creates a new Tuleap select box Field with the semantic "contributor".
	 * 
	 * @param id
	 *            the id
	 * @return The created field
	 */
	private TuleapSelectBox newSemanticContributorSingle(int id) {
		TuleapSelectBox result = new TuleapSelectBox(id);
		setDescriptionAndLabelFromId(result);
		result.setSemanticContributor(true);
		return result;
	}

	/**
	 * Creates a new Tuleap multi select box Field with the semantic "contributor".
	 * 
	 * @param id
	 *            the id
	 * @return The created field
	 */
	private TuleapMultiSelectBox newSemanticContributorMultiple(int id) {
		TuleapMultiSelectBox result = new TuleapMultiSelectBox(id);
		setDescriptionAndLabelFromId(result);
		result.setSemanticContributor(true);
		return result;
	}

	/**
	 * Creates a new Tuleap select box item with the given id and a computed label and description.
	 * 
	 * @param id
	 *            the id
	 * @return The created field
	 */
	private TuleapSelectBoxItem newTuleapSelectBoxItem(int id) {
		TuleapSelectBoxItem item = new TuleapSelectBoxItem(id);
		item.setDescription(getDescriptionFromId(id));
		item.setLabel(getLabelFromId(id));
		return item;
	}

	/**
	 * Creates a new Tuleap Multi Select Box Field.
	 * 
	 * @param id
	 *            the id
	 * @return The created field
	 */
	private TuleapMultiSelectBox newTuleapMultiSelectBox(int id) {
		TuleapMultiSelectBox result = new TuleapMultiSelectBox(id);
		setDescriptionAndLabelFromId(result);
		TuleapSelectBoxItem item0 = newTuleapSelectBoxItem(0);
		TuleapSelectBoxItem item1 = newTuleapSelectBoxItem(1);
		TuleapSelectBoxItem item2 = newTuleapSelectBoxItem(2);
		TuleapSelectBoxItem item3 = newTuleapSelectBoxItem(3);
		result.addItem(item0);
		result.addItem(item1);
		result.addItem(item2);
		result.addItem(item3);
		return result;
	}

	/**
	 * Setup method.
	 */
	@Before
	public void setUp() {
		this.repositoryUrl = "https://demo.tuleap.net/plugins/tracker/?groupdId=871"; //$NON-NLS-1$
		this.connectorKind = ITuleapConstants.CONNECTOR_KIND;

		this.trackerName = "Tracker Name"; //$NON-NLS-1$
		String repositoryDescription = "Tracker Description"; //$NON-NLS-1$
		this.itemName = "tracker_name"; //$NON-NLS-1$

		this.trackerId = 123;
		this.projectId = 987;

		this.repository = new TaskRepository(connectorKind, repositoryUrl);
		this.tuleapTrackerConfiguration = new TuleapTrackerConfiguration(trackerId, repositoryUrl);
		tuleapTrackerConfiguration.setName(trackerName);
		tuleapTrackerConfiguration.setDescription(repositoryDescription);
		tuleapTrackerConfiguration.setItemName(itemName);

		this.tuleapServerConfiguration = new TuleapServerConfiguration(repositoryUrl);

		this.tuleapProjectConfiguration = new TuleapProjectConfiguration(projectName, projectId);
		this.tuleapProjectConfiguration.addTracker(tuleapTrackerConfiguration);
		tuleapServerConfiguration.addProject(tuleapProjectConfiguration);
		// this.repositoryConnector = new MockedTuleapRepositoryConnector(tuleapServerConfiguration);
		fail("Fix the test ");

		this.attributeMapper = new TuleapAttributeMapper(repository, repositoryConnector);
		this.taskData = new TaskData(attributeMapper, connectorKind, repositoryUrl, "task1"); //$NON-NLS-1$
		this.mapper = new TuleapTaskMapper(taskData, tuleapTrackerConfiguration);
	}
}
