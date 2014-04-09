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
package org.eclipse.mylyn.tuleap.core.tests.internal.data;

import com.google.common.collect.Lists;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMetaData;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tuleap.core.internal.client.TuleapClientManager;
import org.eclipse.mylyn.tuleap.core.internal.data.TuleapArtifactMapper;
import org.eclipse.mylyn.tuleap.core.internal.data.TuleapTaskId;
import org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapProject;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapServer;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapUser;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapWorkflowTransition;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapDate;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapFloat;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapInteger;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapMultiSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapOpenList;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBoxItem;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapString;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapText;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AbstractFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AttachmentValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.BoundFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.LiteralFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.OpenListFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapElementComment;
import org.eclipse.mylyn.tuleap.core.internal.parser.DateIso8601Adapter;
import org.eclipse.mylyn.tuleap.core.internal.repository.ITuleapRepositoryConnector;
import org.eclipse.mylyn.tuleap.core.internal.repository.TuleapAttributeMapper;
import org.eclipse.mylyn.tuleap.core.internal.util.ITuleapConstants;
import org.eclipse.mylyn.tuleap.core.internal.util.TuleapCoreKeys;
import org.eclipse.mylyn.tuleap.core.internal.util.TuleapCoreMessages;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests of the new Tuleap configurable element mapper.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapArtifactMapperTests {

	/**
	 * The identifier of the parent id task attribute.
	 */
	public static final String PARENT_ID = "mtc_parent_id"; //$NON-NLS-1$

	/**
	 * The identifier of the displayed parent id task attribute.
	 */
	public static final String PARENT_DISPLAY_ID = "mtc_parent_display_id"; //$NON-NLS-1$

	/**
	 * The task repository.
	 */
	private TaskRepository repository;

	/**
	 * The tracker configuration.
	 */
	private TuleapTracker tuleapTracker;

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
	private TuleapServer tuleapServer;

	/**
	 * The configuration of the tuleap project.
	 */
	private TuleapProject tuleapProject;

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
	private TuleapArtifactMapper mapper;

	/**
	 * The attribute mapper used by the mapper under test.
	 */
	private TuleapAttributeMapper attributeMapper;

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
		this.tuleapTracker = new TuleapTracker(trackerId, repositoryUrl, trackerName, itemName,
				repositoryDescription, new Date());

		this.tuleapServer = new TuleapServer(repositoryUrl);

		this.tuleapProject = new TuleapProject(projectName, projectId);
		this.tuleapProject.addTracker(tuleapTracker);
		tuleapServer.addProject(tuleapProject);
		this.repositoryConnector = new ITuleapRepositoryConnector() {

			public TuleapClientManager getClientManager() {
				return null;
			}

			public TuleapServer getServer(String url) {
				return tuleapServer;
			}

			public TuleapTracker refreshTracker(TaskRepository taskRepository, TuleapTracker tracker,
					IProgressMonitor monitor) throws CoreException {
				return null;
			}

		};

		this.attributeMapper = new TuleapAttributeMapper(repository, repositoryConnector);
		this.taskData = new TaskData(attributeMapper, connectorKind, repositoryUrl, ""); //$NON-NLS-1$
		this.mapper = new TuleapArtifactMapper(taskData, tuleapTracker);
	}

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
		assertEquals(tuleapTracker.getLabel(), att.getValue());
		att = root.getMappedAttribute(TaskAttribute.DATE_CREATION);
		assertNotNull(att);
		TaskAttributeMetaData metadata = att.getMetaData();
		assertEquals(TuleapCoreMessages.getString(TuleapCoreKeys.creationDateLabel), metadata.getLabel());
		assertEquals(TaskAttribute.TYPE_DATE, metadata.getType());
		assertFalse(metadata.isReadOnly());

		TuleapTaskId taskId = TuleapTaskId.forNewArtifact(projectId, trackerId);
		assertEquals(taskId, mapper.getTaskId());

		// Also check that project id & tracker id task attributes are correctly created since they will be
		// useful to mylyn
		att = root.getAttribute(TuleapArtifactMapper.PROJECT_ID);
		assertNotNull(att);
		assertEquals(String.valueOf(projectId), att.getValue());

		att = root.getAttribute(TuleapArtifactMapper.TRACKER_ID);
		assertNotNull(att);
		assertEquals(String.valueOf(trackerId), att.getValue());

		// Check that the task_key attribute has been created
		att = root.getAttribute(TaskAttribute.TASK_KEY);
		assertNotNull(att);
		assertTrue(att.getMetaData().isReadOnly());

		att = root.getAttribute(TaskAttribute.DATE_MODIFICATION);
		assertNotNull(att);
		metadata = att.getMetaData();
		assertEquals(TuleapCoreMessages.getString(TuleapCoreKeys.lastModificationDateLabel), metadata
				.getLabel());
		assertEquals(TaskAttribute.TYPE_DATE, metadata.getType());
		assertFalse(metadata.isReadOnly());

		att = root.getAttribute(TaskAttribute.DATE_COMPLETION);
		assertNotNull(att);
		metadata = att.getMetaData();
		assertEquals(TuleapCoreMessages.getString(TuleapCoreKeys.completionDateLabel), metadata.getLabel());
		assertEquals(TaskAttribute.TYPE_DATE, metadata.getType());
		assertFalse(metadata.isReadOnly());

		att = root.getAttribute(TaskAttribute.COMMENT_NEW);
		assertNotNull(att);
		metadata = att.getMetaData();
		assertEquals(TuleapCoreMessages.getString(TuleapCoreKeys.newCommentLabel), metadata.getLabel());
		assertEquals(TaskAttribute.TYPE_LONG_RICH_TEXT, metadata.getType());
		assertFalse(metadata.isReadOnly());

		att = root.getAttribute(TaskAttribute.USER_REPORTER);
		assertNotNull(att);
		metadata = att.getMetaData();
		assertEquals(TaskAttribute.TYPE_PERSON, metadata.getType());
		assertTrue(metadata.isReadOnly());
	}

	/**
	 * Verification of the task key.
	 */
	@Test
	public void testTaskKey() {
		mapper.initializeEmptyTaskData();

		assertEquals("", mapper.getTaskKey());
		mapper.setTaskKey("The new task key");
		String value = taskData.getRoot().getAttribute(TaskAttribute.TASK_KEY).getValue();
		assertThat(value, is("The new task key"));
		assertEquals("The new task key", mapper.getTaskKey());
	}

	/**
	 * Verification of the task id.
	 */
	@Test
	public void testTaskId() {
		mapper.initializeEmptyTaskData();
		assertEquals("987:123#N/A", mapper.getTaskId().toString());
		assertEquals(987, mapper.getTaskId().getProjectId());
		assertEquals(123, mapper.getTaskId().getTrackerId());
		assertEquals(-1, mapper.getTaskId().getArtifactId());
	}

	/**
	 * Verification of the task url.
	 */
	@Test
	public void testTaskUrl() {
		mapper.initializeEmptyTaskData();
		assertEquals("", mapper.getTaskUrl());
		mapper.setTaskUrl("url/url");
		String value = taskData.getRoot().getAttribute(TaskAttribute.TASK_URL).getValue();
		assertThat(value, is("url/url"));
		assertEquals("url/url", mapper.getTaskUrl());
	}

	/**
	 * Verification of the artifact creation date.
	 *
	 * @throws ParseException
	 */
	@Test
	public void testCreationDate() throws ParseException {
		mapper.initializeEmptyTaskData();
		assertNull(mapper.getCreationDate());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date1 = simpleDateFormat.parse("11/03/2014");
		mapper.setCreationDate(date1);
		String value = taskData.getRoot().getAttribute(TaskAttribute.DATE_CREATION).getValue();
		assertThat(value, is(Long.toString(date1.getTime())));
	}

	/**
	 * Verification of the task modification date.
	 *
	 * @throws ParseException
	 */
	@Test
	public void testModificationDate() throws ParseException {
		mapper.initializeEmptyTaskData();
		assertNull(mapper.getModificationDate());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date1 = simpleDateFormat.parse("11/03/2014");
		mapper.setModificationDate(date1);
		String value = taskData.getRoot().getAttribute(TaskAttribute.DATE_MODIFICATION).getValue();
		assertThat(value, is(Long.toString(date1.getTime())));
	}

	/**
	 * Verification of adding a comment.
	 *
	 * @throws ParseException
	 */
	@Test
	public void testAddComment() throws ParseException {
		mapper.initializeEmptyTaskData();
		assertNull(mapper.getModificationDate());

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date1 = simpleDateFormat.parse("11/03/2014");
		String commentBody = "This is the first comment"; //$NON-NLS-1$
		TuleapUser commentSubmitter = new TuleapUser("username", "realname", 17, //$NON-NLS-1$ //$NON-NLS-2$
				"email", null); //$NON-NLS-1$

		TuleapElementComment comment = new TuleapElementComment(commentBody, commentSubmitter, date1);
		mapper.addComment(comment);

		TaskAttribute att = taskData.getRoot().getMappedAttribute(TaskAttribute.PREFIX_COMMENT + "0");
		assertNotNull(att);

		TaskAttribute authorAttribute = att.getMappedAttribute(TaskAttribute.COMMENT_AUTHOR);
		assertNotNull(authorAttribute);
		assertEquals("email", authorAttribute.getValue());

		TaskAttribute nameAttribute = authorAttribute.getMappedAttribute(TaskAttribute.PERSON_NAME);
		assertNotNull(nameAttribute);
		assertEquals("username", nameAttribute.getValue());

		TaskAttribute dateAttribute = att.getMappedAttribute(TaskAttribute.COMMENT_DATE);
		assertNotNull(dateAttribute);
		assertEquals(Long.toString(date1.getTime()), dateAttribute.getValue());

		TaskAttribute numberAttribute = att.getMappedAttribute(TaskAttribute.COMMENT_NUMBER);
		assertNotNull(numberAttribute);
		assertEquals("0", numberAttribute.getValue());

		TaskAttribute textAttribute = att.getMappedAttribute(TaskAttribute.COMMENT_TEXT);
		assertNotNull(textAttribute);
		assertEquals("This is the first comment", textAttribute.getValue());
	}

	/**
	 * Verification of adding an attachment.
	 *
	 * @throws ParseException
	 */
	@Test
	public void testAddAttachment() throws ParseException {
		mapper.initializeEmptyTaskData();
		assertNull(mapper.getModificationDate());

		AttachmentValue attachment = new AttachmentValue("id", "name", 17, 123456, "description", "type", "");

		mapper.addAttachment("First field", attachment);

		TaskAttribute att = taskData.getRoot().getMappedAttribute(
				TaskAttribute.PREFIX_ATTACHMENT + "First field" + "---" + "id");
		assertNotNull(att);

		TaskAttribute authorAttribute = att.getMappedAttribute(TaskAttribute.ATTACHMENT_AUTHOR);
		// Author is ignored due to problems with user retrieval in Tuleap API
		// Matters of security and avoiding leak of users list must be addressed
		assertNull(authorAttribute);
		// assertEquals("email", authorAttribute.getValue());
		// TaskAttribute nameAttribute = authorAttribute.getMappedAttribute(TaskAttribute.PERSON_NAME);
		// assertNotNull(nameAttribute);
		// assertEquals("username", nameAttribute.getValue());

		TaskAttribute typeAttribute = att.getMappedAttribute(TaskAttribute.ATTACHMENT_CONTENT_TYPE);
		assertNotNull(typeAttribute);
		assertEquals("type", typeAttribute.getValue());

		TaskAttribute descriptionAttribute = att.getMappedAttribute(TaskAttribute.ATTACHMENT_DESCRIPTION);
		assertNotNull(descriptionAttribute);
		assertEquals("description", descriptionAttribute.getValue());

		TaskAttribute fielNameAttribute = att.getMappedAttribute(TaskAttribute.ATTACHMENT_FILENAME);
		assertNotNull(fielNameAttribute);
		assertEquals("name", fielNameAttribute.getValue());

		TaskAttribute sizeAttribute = att.getMappedAttribute(TaskAttribute.ATTACHMENT_SIZE);
		assertNotNull(sizeAttribute);
		assertEquals("123456", sizeAttribute.getValue());
	}

	/**
	 * Check the correct creation of a semantic title attribute.
	 */
	@Test
	public void testInitializeEmptyTaskDataWithSemanticTitle() {
		int id = 401;
		tuleapTracker.addField(newSemanticTitle(id));
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
		tuleapTracker.addField(newTuleapDate(id));
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
		tuleapTracker.addField(newTuleapString(id));
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
		tuleapTracker.addField(newTuleapText(id));
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
		tuleapTracker.addField(newTuleapInteger(id));
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
		tuleapTracker.addField(newTuleapFloat(id));
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
		tuleapTracker.addField(newTuleapOpenList(id));
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
		tuleapTracker.addField(newSemanticStatus(id));
		mapper.initializeEmptyTaskData();

		TaskAttribute att = taskData.getRoot().getMappedAttribute(String.valueOf(id));
		assertNull(att);
		att = taskData.getRoot().getMappedAttribute(TaskAttribute.STATUS);
		assertNotNull(att);
		assertEquals(5, att.getOptions().size());

		TaskAttributeMetaData metadata = att.getMetaData();
		assertEquals(TaskAttribute.TYPE_SINGLE_SELECT, metadata.getType());
		assertEquals(TuleapCoreMessages.getString(TuleapCoreKeys.statusLabel), metadata.getLabel());
		String lbl = getLabelFromId(0);
		assertEquals(lbl, att.getOption("0")); //$NON-NLS-1$
		lbl = getLabelFromId(1);
		assertEquals(lbl, att.getOption("1")); //$NON-NLS-1$
		lbl = getLabelFromId(2);
		assertEquals(lbl, att.getOption("2")); //$NON-NLS-1$
		lbl = getLabelFromId(3);
		assertEquals(lbl, att.getOption("3")); //$NON-NLS-1$
		assertEquals(
				"None", att.getOption(String.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID))); //$NON-NLS-1$
	}

	/**
	 * Check the correct creation of a select box attribute without special semantic.
	 */
	@Test
	public void testInitializeEmptyTaskDataWithSemanticStatusWithWorkflow() {
		int id = 403;
		tuleapTracker.addField(newSemanticStatusWithWorkflow(id));
		mapper.initializeEmptyTaskData();

		TaskAttribute att = taskData.getRoot().getMappedAttribute(TaskAttribute.STATUS);
		assertNotNull(att);
		TaskAttributeMetaData metadata = att.getMetaData();
		assertEquals(TaskAttribute.TYPE_SINGLE_SELECT, metadata.getType());
		assertEquals(TuleapCoreMessages.getString(TuleapCoreKeys.statusLabel), metadata.getLabel());

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
		tuleapTracker.addField(selectBox);
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
		tuleapTracker.addField(newTuleapMultiSelectBox(id));
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
		tuleapTracker.addField(newSemanticContributorSingle(id));
		mapper.initializeEmptyTaskData();

		TaskAttribute att = taskData.getRoot().getMappedAttribute(TaskAttribute.USER_ASSIGNED);
		assertNotNull(att);
		TaskAttributeMetaData metadata = att.getMetaData();
		assertEquals(TaskAttribute.TYPE_SINGLE_SELECT, metadata.getType());
		assertEquals(TuleapCoreMessages.getString(TuleapCoreKeys.assignedToLabel), metadata.getLabel());
		assertEquals(TaskAttribute.KIND_PEOPLE, metadata.getKind());
	}

	/**
	 * Test of the setStatus() method. Checks that the completion date becomes set when status becomes closed,
	 * and becomes unset when status becomes open.
	 */
	@Test
	public void testSetStatus() {
		int statusId = 555;
		tuleapTracker.addField(newSemanticStatus(statusId));
		mapper.initializeEmptyTaskData();

		assertEquals("", taskData.getRoot().getAttribute(TaskAttribute.STATUS).getValue()); //$NON-NLS-1$

		int statusOpen0 = 0;
		mapper.setStatus(statusOpen0);

		assertEquals(statusOpen0, Integer.parseInt(taskData.getRoot().getAttribute(TaskAttribute.STATUS)
				.getValue()));
		assertEquals(statusOpen0, mapper.getStatusAsInt());
		assertNull(mapper.getCompletionDate());

		// go to closed state, completion date must be non-null
		int statusClosed2 = 2;
		mapper.setStatus(statusClosed2);

		assertEquals(statusClosed2, Integer.parseInt(taskData.getRoot().getAttribute(TaskAttribute.STATUS)
				.getValue()));
		assertEquals(statusClosed2, mapper.getStatusAsInt());
		assertNotNull(mapper.getCompletionDate());

		// Back to open, completion date must be null
		int statusOpen1 = 1;
		mapper.setStatus(statusOpen1);

		assertEquals(statusOpen1, Integer.parseInt(taskData.getRoot().getAttribute(TaskAttribute.STATUS)
				.getValue()));
		assertEquals(statusOpen1, mapper.getStatusAsInt());
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
		tuleapTracker.addField(newSemanticStatusWithWorkflow(statusId));
		mapper.initializeEmptyTaskData();

		assertEquals("", taskData.getRoot().getAttribute(TaskAttribute.STATUS).getValue()); //$NON-NLS-1$

		int statusOpen0 = 0;
		int statusOpen1 = 1;
		int statusClosed2 = 2;
		mapper.setStatus(statusOpen0);

		assertEquals(statusOpen0, Integer.parseInt(taskData.getRoot().getAttribute(TaskAttribute.STATUS)
				.getValue()));
		assertEquals(statusOpen0, mapper.getStatusAsInt());
		assertNull(mapper.getCompletionDate());
		assertEquals(2, taskData.getRoot().getAttribute(TaskAttribute.STATUS).getOptions().size());
		assertNotNull(taskData.getRoot().getAttribute(TaskAttribute.STATUS).getOption(
				String.valueOf(statusOpen1)));
		assertNotNull(taskData.getRoot().getAttribute(TaskAttribute.STATUS).getOption(
				String.valueOf(statusOpen0)));

		// go to closed state, completion date must be non-null
		mapper.setStatus(statusOpen1);

		assertEquals(statusOpen1, Integer.parseInt(taskData.getRoot().getAttribute(TaskAttribute.STATUS)
				.getValue()));
		assertEquals(statusOpen1, mapper.getStatusAsInt());
		assertNull(mapper.getCompletionDate());
		assertEquals(2, taskData.getRoot().getAttribute(TaskAttribute.STATUS).getOptions().size());
		assertNotNull(taskData.getRoot().getAttribute(TaskAttribute.STATUS).getOption(
				String.valueOf(statusOpen1)));
		assertNotNull(taskData.getRoot().getAttribute(TaskAttribute.STATUS).getOption(
				String.valueOf(statusClosed2)));

		// go to closed state, completion date must be non-null
		mapper.setStatus(statusClosed2);

		assertEquals(statusClosed2, Integer.parseInt(taskData.getRoot().getAttribute(TaskAttribute.STATUS)
				.getValue()));
		assertEquals(statusClosed2, mapper.getStatusAsInt());
		assertNotNull(mapper.getCompletionDate());
		assertEquals(3, taskData.getRoot().getAttribute(TaskAttribute.STATUS).getOptions().size());
		assertNotNull(taskData.getRoot().getAttribute(TaskAttribute.STATUS).getOption(
				String.valueOf(statusOpen1)));
		assertNotNull(taskData.getRoot().getAttribute(TaskAttribute.STATUS).getOption(
				String.valueOf(statusClosed2)));
		assertNotNull(taskData.getRoot().getAttribute(TaskAttribute.STATUS).getOption("3")); //$NON-NLS-1$

		// Back to open, completion date must be null
		mapper.setStatus(statusOpen1);

		assertEquals(statusOpen1, Integer.parseInt(taskData.getRoot().getAttribute(TaskAttribute.STATUS)
				.getValue()));
		assertEquals(statusOpen1, mapper.getStatusAsInt());
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
		tuleapTracker.addField(newSemanticContributorSingle(id));
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
		tuleapTracker.addField(newSemanticContributorMultiple(id));
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
	 * Test the creation of the summary task attribute in the task data.
	 */
	@Test
	public void testSetSummary() {
		int id = 408;
		tuleapTracker.addField(newSemanticTitle(id));
		mapper.initializeEmptyTaskData();
		String summary = "Hello World";
		mapper.setSummary(summary);

		String value = taskData.getRoot().getAttribute(TaskAttribute.SUMMARY).getValue();
		assertThat(value, is(summary));
	}

	/**
	 * Test the creation of the list of field values.
	 */
	@Test
	public void testGetFieldValues() {
		initializeTrackerGetFieldValues();

		String title = "Title";
		String semanticContributor = "0";
		String status = "2";
		long date = 123456789000L;
		String floatValue = "3.14157";
		String integerValue = "42";
		List<String> multiSelectBox = Lists.newArrayList("0", "2");
		List<String> openList = Lists.newArrayList("a", "b", "c", "d");
		String selectBox = "2";
		String string = "Hello World";
		String text = "The cake is a lie";

		// Populate the task data
		this.taskData.getRoot().getMappedAttribute(TaskAttribute.SUMMARY).setValue(title);
		this.taskData.getRoot().getMappedAttribute(TaskAttribute.USER_ASSIGNED).setValue(semanticContributor);
		this.taskData.getRoot().getMappedAttribute(TaskAttribute.STATUS).setValue(status);
		this.taskData.getRoot().getMappedAttribute(String.valueOf(3)).setValue(Long.toString(date));
		this.taskData.getRoot().getMappedAttribute(String.valueOf(4)).setValue(floatValue);
		this.taskData.getRoot().getMappedAttribute(String.valueOf(5)).setValue(integerValue);
		this.taskData.getRoot().getMappedAttribute(String.valueOf(6)).setValues(multiSelectBox);
		this.taskData.getRoot().getMappedAttribute(String.valueOf(7)).setValue("a, b, c, d");
		this.taskData.getRoot().getMappedAttribute(String.valueOf(8)).setValue(selectBox);
		this.taskData.getRoot().getMappedAttribute(String.valueOf(9)).setValue(string);
		this.taskData.getRoot().getMappedAttribute(String.valueOf(10)).setValue(text);

		// Tests the value of all the fields
		List<AbstractFieldValue> fieldValues = this.mapper.getFieldValues();
		assertThat(fieldValues.size(), is(11));

		assertThat(fieldValues.get(0).getFieldId(), is(0));
		assertThat(fieldValues.get(0), instanceOf(LiteralFieldValue.class));
		assertThat(((LiteralFieldValue)fieldValues.get(0)).getFieldValue(), is(title));

		assertThat(fieldValues.get(1).getFieldId(), is(1));
		assertThat(fieldValues.get(1), instanceOf(BoundFieldValue.class));
		List<Integer> valueIds = Lists.newArrayList(Integer.valueOf(semanticContributor));
		assertThat(((BoundFieldValue)fieldValues.get(1)).getValueIds(), is(valueIds));

		assertThat(fieldValues.get(2).getFieldId(), is(2));
		assertThat(fieldValues.get(2), instanceOf(BoundFieldValue.class));
		valueIds = Lists.newArrayList(Integer.valueOf(status));
		assertThat(((BoundFieldValue)fieldValues.get(2)).getValueIds(), is(valueIds));

		assertThat(fieldValues.get(3).getFieldId(), is(3));
		assertThat(fieldValues.get(3), instanceOf(LiteralFieldValue.class));
		try {
			assertThat(DateIso8601Adapter.parseIso8601Date(((LiteralFieldValue)fieldValues.get(3))
					.getFieldValue()), is(new Date(date)));
		} catch (ParseException e) {
			fail();
		}

		assertThat(fieldValues.get(4).getFieldId(), is(4));
		assertThat(fieldValues.get(4), instanceOf(LiteralFieldValue.class));
		assertThat(((LiteralFieldValue)fieldValues.get(4)).getFieldValue(), is(floatValue));

		assertThat(fieldValues.get(5).getFieldId(), is(5));
		assertThat(fieldValues.get(5), instanceOf(LiteralFieldValue.class));
		assertThat(((LiteralFieldValue)fieldValues.get(5)).getFieldValue(), is(integerValue));

		assertThat(fieldValues.get(6).getFieldId(), is(6));
		assertThat(fieldValues.get(6), instanceOf(BoundFieldValue.class));
		valueIds = Lists.newArrayList(Integer.valueOf(0), Integer.valueOf(2));
		assertThat(((BoundFieldValue)fieldValues.get(6)).getValueIds(), is(valueIds));

		assertThat(fieldValues.get(7).getFieldId(), is(7));
		assertThat(fieldValues.get(7), instanceOf(OpenListFieldValue.class));
		assertThat(((OpenListFieldValue)fieldValues.get(7)).getValueIds(), is(openList));

		assertThat(fieldValues.get(8).getFieldId(), is(8));
		assertThat(fieldValues.get(8), instanceOf(BoundFieldValue.class));
		valueIds = Lists.newArrayList(Integer.valueOf(2));
		assertThat(((BoundFieldValue)fieldValues.get(8)).getValueIds(), is(valueIds));

		assertThat(fieldValues.get(9).getFieldId(), is(9));
		assertThat(fieldValues.get(9), instanceOf(LiteralFieldValue.class));
		assertThat(((LiteralFieldValue)fieldValues.get(9)).getFieldValue(), is(string));

		assertThat(fieldValues.get(10).getFieldId(), is(10));
		assertThat(fieldValues.get(10), instanceOf(LiteralFieldValue.class));
		assertThat(((LiteralFieldValue)fieldValues.get(10)).getFieldValue(), is(text));
	}

	/**
	 * Initialize the tracker for the test of getFieldValues().
	 */
	private void initializeTrackerGetFieldValues() {
		// Initialize the tracker
		tuleapTracker.addField(this.newSemanticTitle(0));
		tuleapTracker.addField(this.newSemanticContributorSingle(1));
		tuleapTracker.addField(this.newSemanticStatusWithWorkflow(2));
		tuleapTracker.addField(this.newTuleapDate(3));
		tuleapTracker.addField(this.newTuleapFloat(4));
		tuleapTracker.addField(this.newTuleapInteger(5));
		tuleapTracker.addField(this.newTuleapMultiSelectBox(6));
		tuleapTracker.addField(this.newTuleapOpenList(7));
		tuleapTracker.addField(this.newTuleapSelectBox(8));
		tuleapTracker.addField(this.newTuleapString(9));
		tuleapTracker.addField(this.newTuleapText(10));

		mapper.initializeEmptyTaskData();
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
		TuleapSelectBox result = this.newTuleapSelectBox(id);
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
		TuleapMultiSelectBox result = this.newTuleapMultiSelectBox(id);
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
}
