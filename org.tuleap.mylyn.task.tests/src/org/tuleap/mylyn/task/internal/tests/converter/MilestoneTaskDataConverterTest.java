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
package org.tuleap.mylyn.task.internal.tests.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.internal.core.client.TuleapClientManager;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.data.converter.MilestoneTaskDataConverter;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapPerson;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapProject;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapServer;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapTracker;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.model.data.ArtifactReference;
import org.tuleap.mylyn.task.internal.core.model.data.AttachmentFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.AttachmentValue;
import org.tuleap.mylyn.task.internal.core.model.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapCard;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapCardwall;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapColumn;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapStatus;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapSwimlane;
import org.tuleap.mylyn.task.internal.core.repository.TuleapRepositoryConnector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests of the milestone task data converter.
 * 
 * @author <a href="mailto:firas.bacha">Firas Bacha</a>
 */
public class MilestoneTaskDataConverterTest {

	/**
	 * Separator used in computed ids.
	 */
	public static final char ID_SEPARATOR = '-';

	/**
	 * Id of the milestone list task attribute.
	 */
	public static final String COLUMN_LIST = "mta_col-"; //$NON-NLS-1$

	/**
	 * Id of the milestone list task attribute.
	 */
	public static final String SWIMLANE_PREFIX = "mta_swi-"; //$NON-NLS-1$

	private static final long START_TIME = 30 * 365L * 24L * 3600000L;

	private static final long END_TIME = START_TIME + 20L * 24L * 3600000L;

	private TuleapReference projectRef = new TuleapReference(666, "p/666");

	/**
	 * The wrapped task data.
	 */
	private TaskData taskData;

	/**
	 * The task repository.
	 */
	private TaskRepository taskRepository;

	private TuleapString field1000;

	private TuleapSelectBox field2000;

	private TuleapTracker tracker700;

	private TuleapProject project;

	private TuleapServer server;

	private TuleapRepositoryConnector connector;

	/**
	 * Configure the data for the tests.
	 */
	@Before
	public void setUp() {
		String repositoryUrl = "repository"; //$NON-NLS-1$
		String connectorKind = "kind"; //$NON-NLS-1$
		String taskId = "id"; //$NON-NLS-1$
		server = new TuleapServer(repositoryUrl);

		project = new TuleapProject("The first project", 200); //$NON-NLS-1$
		server.addProject(project);

		tracker700 = new TuleapTracker(700, "700", "Tracker 700", "", "", System.currentTimeMillis());
		project.addTracker(tracker700);

		field1000 = new TuleapString(1000);
		field1000.setLabel("Text");
		tracker700.addField(field1000);

		field2000 = new TuleapSelectBox(2000);
		field2000.setLabel("SelectBox");
		TuleapSelectBoxItem item = new TuleapSelectBoxItem(10);
		item.setLabel("Label 10");
		field2000.addItem(item);
		item = new TuleapSelectBoxItem(20);
		item.setLabel("Label 20");
		field2000.addItem(item);
		item = new TuleapSelectBoxItem(30);
		item.setLabel("Label 30");
		field2000.addItem(item);
		item = new TuleapSelectBoxItem(40);
		item.setLabel("Label 40");
		field2000.addItem(item);
		tracker700.addField(field2000);

		final TuleapClientManager clientManager = new TuleapClientManager() {
			@Override
			public TuleapRestClient getRestClient(TaskRepository pTaskRepository) {
				return new TuleapRestClient(null, null, null, null);
			}
		};

		connector = new TuleapRepositoryConnector() {
			@Override
			public TuleapServer getServer(String pRepositoryUrl) {
				return server;
			}

			@Override
			public TuleapTracker refreshTracker(TaskRepository repo, TuleapTracker tracker,
					IProgressMonitor monitor) throws CoreException {
				return tracker;
			}

			@Override
			public TuleapClientManager getClientManager() {
				return clientManager;
			}
		};
		taskRepository = new TaskRepository(connectorKind, repositoryUrl);
		TaskAttributeMapper mapper = new TaskAttributeMapper(taskRepository);
		taskData = new TaskData(mapper, connectorKind, repositoryUrl, taskId);
	}

	/**
	 * Tests milestone submilestones conversion.
	 */
	@Test
	@Ignore("Fix me ASAP")
	public void testMilestoneSubmilestones() {
		Date testDate = new Date();

		TuleapMilestone milestone = new TuleapMilestone(50, projectRef, "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$

		TuleapMilestone submilestone100 = new TuleapMilestone(100, projectRef, "submilestone100", "URL", //$NON-NLS-1$//$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$
		submilestone100.setCapacity("123");
		submilestone100.setStartDate(new Date(START_TIME));
		submilestone100.setEndDate(new Date(END_TIME));

		MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(taskRepository, connector);
		converter.populateTaskData(taskData, milestone, null);

		TaskAttribute root = taskData.getRoot();
		fail("Implement the actual test");
	}

	/**
	 * Tests the swimlanes list.
	 */
	@Test
	public void testCardwallSwimlanes() {
		Date testDate = new Date();

		TuleapMilestone milestone = new TuleapMilestone(50,
				new TuleapReference(200, "p/200"), "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$
		TuleapCardwall cardwall = new TuleapCardwall();

		TuleapSwimlane firstSwimlane = new TuleapSwimlane();

		cardwall.addSwimlane(firstSwimlane);

		TuleapSwimlane secondSwimlane = new TuleapSwimlane();

		cardwall.addSwimlane(secondSwimlane);

		MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(taskRepository, connector);
		converter.populateTaskData(taskData, milestone, null);
		converter.populateCardwall(taskData, cardwall, project, null);

		TaskAttribute root = taskData.getRoot();

		int id = 0;
		// The first swimlane
		String swimlaneId = SWIMLANE_PREFIX + id++;
		TaskAttribute firstSwimlaneTA = root.getAttribute(swimlaneId);
		assertNotNull(firstSwimlaneTA);

		// The second swimlane
		swimlaneId = SWIMLANE_PREFIX + id++;
		TaskAttribute secondSwimlaneTA = root.getAttribute(swimlaneId);
		assertNotNull(secondSwimlaneTA);

	}

	/**
	 * Tests the columns list.
	 */
	@Test
	public void testCardwallColumns() {
		Date testDate = new Date();

		TuleapMilestone milestone = new TuleapMilestone(50,
				new TuleapReference(200, "p/200"), "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$

		TuleapCardwall cardwall = new TuleapCardwall();

		TuleapColumn firstColumnConfig = new TuleapColumn(600, "first column"); //$NON-NLS-1$

		TuleapColumn secondColumnConfig = new TuleapColumn(800, "second column"); //$NON-NLS-1$

		cardwall.addColumn(firstColumnConfig);
		cardwall.addColumn(secondColumnConfig);

		MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(taskRepository, connector);
		converter.populateTaskData(taskData, milestone, null);
		converter.populateCardwall(taskData, cardwall, project, null);

		TaskAttribute root = taskData.getRoot();

		int i = 0;
		// the first column
		String attId = COLUMN_LIST + 600;
		TaskAttribute firstColumnTA = root.getAttribute(attId);
		assertEquals("600", firstColumnTA.getValue()); //$NON-NLS-1$

		TaskAttribute firstColumnLabelTA = root.getAttribute(attId + "-lbl");
		assertNotNull(firstColumnLabelTA);
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, firstColumnLabelTA.getMetaData().getType());
		assertEquals("first column", firstColumnLabelTA.getValue()); //$NON-NLS-1$

		// the second column
		attId = COLUMN_LIST + 800;
		TaskAttribute secondColumnTA = root.getAttribute(attId);
		assertEquals("800", secondColumnTA.getValue()); //$NON-NLS-1$

		TaskAttribute secondColumnLabelTA = root.getAttribute(attId + "-lbl");
		assertNotNull(secondColumnLabelTA);
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, secondColumnLabelTA.getMetaData().getType());
		assertEquals("second column", secondColumnLabelTA.getValue()); //$NON-NLS-1$
	}

	/**
	 * Tests the cardwall cards with literal field value.
	 */
	@Test
	public void testCardwallCardsFieldValue() {
		Date testDate = new Date();

		TuleapMilestone milestone = new TuleapMilestone(50,
				new TuleapReference(200, "p/200"), "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$

		TuleapCardwall cardwall = new TuleapCardwall();

		TuleapSwimlane firstSwimlane = new TuleapSwimlane();
		cardwall.addSwimlane(firstSwimlane);

		TuleapCard firstCard = new TuleapCard("2_12345", new ArtifactReference(12345, "A/12345",
				new TuleapReference(700, "t/700")), new TuleapReference(200, "p/200"));
		firstCard.setColumnId(10000);
		LiteralFieldValue firstLiteralFieldValue = new LiteralFieldValue(1000, "300, 301, 302"); //$NON-NLS-1$
		firstCard.addFieldValue(firstLiteralFieldValue);
		int[] columnIds = new int[3];
		for (int i = 0; i < 3; i++) {
			columnIds[i] = i + 10;
		}
		firstCard.setAllowedColumnIds(columnIds);
		firstCard.setStatus(TuleapStatus.valueOf("Open"));

		firstSwimlane.addCard(firstCard);

		MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(taskRepository, connector);
		converter.populateTaskData(taskData, milestone, null);
		converter.populateCardwall(taskData, cardwall, project, null);

		TaskAttribute root = taskData.getRoot();

		int id = 0;
		// The first swimlane
		String swimlaneId = SWIMLANE_PREFIX + id++;
		TaskAttribute firstSwimlaneTA = root.getAttribute(swimlaneId);
		assertNotNull(firstSwimlaneTA);

		TaskAttribute firstCardTA = root.getAttribute(swimlaneId + "-c-2_12345"); //$NON-NLS-1$
		assertNotNull(firstCardTA);
		assertEquals("2_12345", firstCardTA.getValue()); //$NON-NLS-1$

		TaskAttribute att = root.getAttribute(swimlaneId + "-c-2_12345-art_id");
		assertEquals("200:700#12345", att.getValue());

		TaskAttribute statusIdFirstCardTA = root.getAttribute(swimlaneId + "-c-2_12345-col_id");

		assertNotNull(statusIdFirstCardTA);
		assertEquals(TaskAttribute.TYPE_INTEGER, statusIdFirstCardTA.getMetaData().getType());
		assertEquals("10000", statusIdFirstCardTA.getValue()); //$NON-NLS-1$

		TaskAttribute fieldValueFirstCardTA = root.getAttribute(swimlaneId + "-c-2_12345-f-1000"); //$NON-NLS-1$ 

		assertNotNull(fieldValueFirstCardTA);
		assertEquals("300, 301, 302", fieldValueFirstCardTA.getValue()); //$NON-NLS-1$

		TaskAttribute statusFirstCardTA = root.getAttribute(swimlaneId + "-c-2_12345-status");

		assertNotNull(statusFirstCardTA);
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, statusFirstCardTA.getMetaData().getType());
		assertEquals("Open", statusFirstCardTA.getValue()); //$NON-NLS-1$

		TaskAttribute allowedColumnsFirstCardTA = root.getAttribute(swimlaneId + "-c-2_12345-allowed_cols");

		assertNotNull(allowedColumnsFirstCardTA);
		List<String> values = allowedColumnsFirstCardTA.getValues();
		assertEquals(3, values.size());
		assertEquals("10", values.get(0)); //$NON-NLS-1$
		assertEquals("11", values.get(1)); //$NON-NLS-1$
		assertEquals("12", values.get(2)); //$NON-NLS-1$
	}

	/**
	 * Tests the cardwall cards with Bind field values.
	 */
	@Test
	public void testCardwallCardsBindFieldValues() {
		Date testDate = new Date();

		TuleapMilestone milestone = new TuleapMilestone(50,
				new TuleapReference(200, "p/200"), "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$

		TuleapCardwall cardwall = new TuleapCardwall();

		TuleapSwimlane firstSwimlane = new TuleapSwimlane();
		cardwall.addSwimlane(firstSwimlane);

		TuleapCard firstCard = new TuleapCard("2_12345", new ArtifactReference(12345, "A/12345",
				new TuleapReference(700, "t/700")), new TuleapReference(200, "p/200"));
		firstCard.setColumnId(10000);

		int[] columnIds = new int[3];
		for (int i = 0; i < 3; i++) {
			columnIds[i] = i + 10;
		}
		firstCard.setAllowedColumnIds(columnIds);
		firstCard.setStatus(TuleapStatus.valueOf("Open"));

		List<Integer> valueIds = new ArrayList<Integer>();
		valueIds.add(new Integer(10));
		valueIds.add(new Integer(20));
		valueIds.add(new Integer(30));
		BoundFieldValue firstBoundFieldValue = new BoundFieldValue(2000, valueIds);

		firstCard.addFieldValue(firstBoundFieldValue);

		firstSwimlane.addCard(firstCard);

		MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(taskRepository, connector);
		converter.populateTaskData(taskData, milestone, null);
		converter.populateCardwall(taskData, cardwall, project, null);

		TaskAttribute root = taskData.getRoot();

		int id = 0;
		// The first swimlane
		String swimlaneId = SWIMLANE_PREFIX + id++;
		TaskAttribute firstSwimlaneTA = root.getAttribute(swimlaneId);

		String cardPrefix = swimlaneId + "-c-2_12345";
		TaskAttribute firstCardTA = root.getAttribute(cardPrefix);
		assertNotNull(firstCardTA);
		assertEquals("2_12345", firstCardTA.getValue()); //$NON-NLS-1$

		TaskAttribute att = root.getAttribute(cardPrefix + "-art_id");
		assertEquals("200:700#12345", att.getValue());

		TaskAttribute statusIdFirstCardTA = root.getAttribute(cardPrefix + "-col_id");

		assertNotNull(statusIdFirstCardTA);
		assertEquals(TaskAttribute.TYPE_INTEGER, statusIdFirstCardTA.getMetaData().getType());
		assertEquals("10000", statusIdFirstCardTA.getValue()); //$NON-NLS-1$

		TaskAttribute fieldValueFirstCardTA = root.getAttribute(cardPrefix + "-f-2000"); //$NON-NLS-1$

		assertNotNull(fieldValueFirstCardTA);
		// FIXME manage the type of Bound fields
		// assertEquals(TaskAttribute.TYPE_SINGLE_SELECT, fieldValueFirstCardTA.getMetaData().getType());
		List<String> values = fieldValueFirstCardTA.getValues();
		assertEquals(3, values.size());
		assertEquals("10", values.get(0)); //$NON-NLS-1$
		assertEquals("20", values.get(1)); //$NON-NLS-1$
		assertEquals("30", values.get(2)); //$NON-NLS-1$

		TaskAttribute statusFirstCardTA = root.getAttribute(cardPrefix + "-status"); //$NON-NLS-1$

		assertNotNull(statusFirstCardTA);
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, statusFirstCardTA.getMetaData().getType());
		assertEquals("Open", statusFirstCardTA.getValue()); //$NON-NLS-1$

		TaskAttribute allowedColumnsFirstCardTA = root.getAttribute(cardPrefix + "-allowed_cols"); //$NON-NLS-1$

		assertNotNull(allowedColumnsFirstCardTA);
		List<String> allowedColumns = allowedColumnsFirstCardTA.getValues();
		assertEquals(3, allowedColumns.size());
		assertEquals("10", allowedColumns.get(0)); //$NON-NLS-1$
		assertEquals("11", allowedColumns.get(1)); //$NON-NLS-1$
		assertEquals("12", allowedColumns.get(2)); //$NON-NLS-1$
	}

	/**
	 * Tests the cardwall cards with file description.
	 */
	@Test
	public void testCardwallCardsFileDescription() {
		Date testDate = new Date();

		TuleapMilestone milestone = new TuleapMilestone(50,
				new TuleapReference(200, "p/200"), "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$
		TuleapCardwall cardwall = new TuleapCardwall();

		TuleapSwimlane firstSwimlane = new TuleapSwimlane();
		cardwall.addSwimlane(firstSwimlane);

		TuleapCard firstCard = new TuleapCard("2_12345", new ArtifactReference(12345, "A/12345",
				new TuleapReference(700, "t/700")), new TuleapReference(200, "p/200"));
		firstCard.setColumnId(10000);

		int[] columnIds = new int[3];
		for (int i = 0; i < 3; i++) {
			columnIds[i] = i + 10;
		}
		firstCard.setAllowedColumnIds(columnIds);
		firstCard.setStatus(TuleapStatus.valueOf("Open"));

		List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		TuleapPerson firstUploadedBy = new TuleapPerson("first username", "first realname", 1, "first email"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attachments.add(new AttachmentValue("100000", "first name", firstUploadedBy, 123456, //$NON-NLS-1$ //$NON-NLS-2$ 
				"first description", "first type")); //$NON-NLS-1$ //$NON-NLS-2$
		TuleapPerson secondUploadedBy = new TuleapPerson("second username", "second realname", 2, //$NON-NLS-1$ //$NON-NLS-2$
				"second email"); //$NON-NLS-1$
		attachments.add(new AttachmentValue("100001", "second name", secondUploadedBy, 789456, //$NON-NLS-1$ //$NON-NLS-2$
				"second description", "second type")); //$NON-NLS-1$ //$NON-NLS-2$
		AttachmentFieldValue fileDescriptions = new AttachmentFieldValue(3000, attachments);

		firstCard.addFieldValue(fileDescriptions);

		firstSwimlane.addCard(firstCard);

		MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(taskRepository, connector);
		converter.populateTaskData(taskData, milestone, null);
		converter.populateCardwall(taskData, cardwall, project, null);

		TaskAttribute root = taskData.getRoot();

		int id = 0;
		// The first swimlane
		String swimlaneId = SWIMLANE_PREFIX + id++;
		TaskAttribute firstSwimlaneTA = root.getAttribute(swimlaneId);

		String cardPrefix = swimlaneId + "-c-2_12345";
		TaskAttribute firstCardTA = root.getAttribute(cardPrefix);
		assertNotNull(firstCardTA);
		assertEquals("2_12345", firstCardTA.getValue()); //$NON-NLS-1$

		TaskAttribute att = root.getAttribute(cardPrefix + "-art_id");
		assertEquals("200:700#12345", att.getValue());

		TaskAttribute statusIdFirstCardTA = root.getAttribute(cardPrefix + "-col_id");

		assertNotNull(statusIdFirstCardTA);
		assertEquals(TaskAttribute.TYPE_INTEGER, statusIdFirstCardTA.getMetaData().getType());
		assertEquals("10000", statusIdFirstCardTA.getValue()); //$NON-NLS-1$

		TaskAttribute fieldValueFirstCardTA = root.getAttribute(cardPrefix + "-f-2000");
		assertNull(fieldValueFirstCardTA);

		TaskAttribute statusFirstCardTA = root.getAttribute(cardPrefix + "-status");

		assertNotNull(statusFirstCardTA);
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, statusFirstCardTA.getMetaData().getType());
		assertEquals("Open", statusFirstCardTA.getValue()); //$NON-NLS-1$

		TaskAttribute allowedColumnsFirstCardTA = root.getAttribute(cardPrefix + "-allowed_cols");

		assertNotNull(allowedColumnsFirstCardTA);
		List<String> values = allowedColumnsFirstCardTA.getValues();
		assertEquals(3, values.size());
		assertEquals("10", values.get(0)); //$NON-NLS-1$
		assertEquals("11", values.get(1)); //$NON-NLS-1$
		assertEquals("12", values.get(2)); //$NON-NLS-1$
	}

	/**
	 * Tests adding a milestone.
	 */
	@Test
	public void testAddSubmilestones() {
		Date testDate = new Date();

		TuleapMilestone milestone = new TuleapMilestone(50,
				new TuleapReference(200, "p/200"), "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$

		TuleapMilestone submilestone100 = new TuleapMilestone(100,
				new TuleapReference(200, "p/200"), "The first submilestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$

		TuleapBacklogItem item0 = new TuleapBacklogItem(230,
				new TuleapReference(200, "p/200"), "item230", null, null, null, null); //$NON-NLS-1$
		item0.setInitialEffort("201");
		item0.setStatus(TuleapStatus.valueOf("Closed"));

		TuleapBacklogItem item1 = new TuleapBacklogItem(231,
				new TuleapReference(200, "p/200"), "item231", null, null, null, null); //$NON-NLS-1$
		item1.setInitialEffort("201");
		item1.setStatus(TuleapStatus.valueOf("Closed"));

		TuleapBacklogItem item2 = new TuleapBacklogItem(232,
				new TuleapReference(200, "p/200"), "item232", null, null, null, null); //$NON-NLS-1$
		item2.setInitialEffort("201");
		item2.setStatus(TuleapStatus.valueOf("Closed"));

		TuleapBacklogItem item3 = new TuleapBacklogItem(233,
				new TuleapReference(200, "p/200"), "item233", null, null, null, null); //$NON-NLS-1$
		item3.setInitialEffort("201");
		item3.setStatus(TuleapStatus.valueOf("Closed"));

		TuleapBacklogItem item4 = new TuleapBacklogItem(234,
				new TuleapReference(200, "p/200"), "item234", null, null, null, null); //$NON-NLS-1$

		List<TuleapBacklogItem> content = new ArrayList<TuleapBacklogItem>();
		content.add(item0);
		content.add(item1);
		content.add(item2);
		content.add(item3);
		content.add(item4);

		MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(taskRepository, connector);
		converter.populateTaskData(taskData, milestone, null);
		converter.addSubmilestone(taskData, submilestone100, content, null);

		TaskAttribute root = taskData.getRoot();

		TaskAttribute planningAtt = root.getAttribute(MilestonePlanningWrapper.MILESTONE_PLANNING);
		assertNotNull(planningAtt);

		TaskAttribute backlogAtt = root.getAttribute(MilestonePlanningWrapper.BACKLOG);
		assertNotNull(backlogAtt);

		for (int i = 0; i < 4; i++) {
			String internalId = "200:0#" + (230 + i);
			TaskAttribute itemAtt = root.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + internalId);
			assertNotNull(itemAtt);
			assertTrue(itemAtt.getMetaData().isReadOnly());
			assertEquals(internalId, itemAtt.getValue());

			TaskAttribute itemLabel = root.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + internalId
					+ ID_SEPARATOR + AbstractTaskAttributeWrapper.SUFFIX_LABEL);
			assertNotNull(itemLabel);
			assertEquals("item23" + i, itemLabel.getValue()); //$NON-NLS-1$
			assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, itemLabel.getMetaData().getType());

			TaskAttribute itemEffort = root.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + internalId
					+ ID_SEPARATOR + BacklogItemWrapper.SUFFIX_BACKLOG_ITEM_POINTS);
			assertNotNull(itemEffort);
			assertEquals("201", itemEffort.getValue());
			assertEquals(TaskAttribute.TYPE_SHORT_TEXT, itemEffort.getMetaData().getType());

			TaskAttribute itemAssignedMilestone = root.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM
					+ internalId + ID_SEPARATOR + BacklogItemWrapper.SUFFIX_ASSIGNED_MILESTONE_ID);
			assertNotNull(itemAssignedMilestone);
			assertEquals("200:0#100", itemAssignedMilestone.getValue()); //$NON-NLS-1$ 
			assertEquals(TaskAttribute.TYPE_INTEGER, itemAssignedMilestone.getMetaData().getType());

			TaskAttribute itemStatus = root.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + internalId
					+ ID_SEPARATOR + BacklogItemWrapper.SUFFIX_STATUS);
			assertNotNull(itemStatus);
			assertEquals("Closed", itemStatus.getValue());
			assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, itemStatus.getMetaData().getType());
		}

		TaskAttribute lastItemStatus = root.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + "200:0#234"
				+ ID_SEPARATOR + BacklogItemWrapper.SUFFIX_STATUS);
		assertEquals(null, lastItemStatus);

	}

	/**
	 * Tests populating the backlog.
	 */
	@Test
	public void testPopulateBacklog() {
		Date testDate = new Date();

		TuleapMilestone milestone = new TuleapMilestone(50,
				new TuleapReference(200, "p/200"), "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$

		TuleapBacklogItem item0 = new TuleapBacklogItem(230,
				new TuleapReference(200, "p/200"), "item230", null, null, null, null); //$NON-NLS-1$
		item0.setInitialEffort("201");
		TuleapReference trackerRef = new TuleapReference(500, "tracker/500");
		ArtifactReference item0Parent = new ArtifactReference(231, "item/231", trackerRef);
		item0.setParent(item0Parent);
		item0.setType("Epics");
		item0.setStatus(TuleapStatus.valueOf("Closed"));

		TuleapBacklogItem item1 = new TuleapBacklogItem(231,
				new TuleapReference(200, "p/200"), "item231", null, null, null, null); //$NON-NLS-1$
		item1.setInitialEffort("201");
		ArtifactReference item1Parent = new ArtifactReference(232, "item/232", trackerRef);
		item1.setParent(item1Parent);
		item1.setType("Epics");
		item1.setStatus(TuleapStatus.valueOf("Closed"));

		TuleapBacklogItem item2 = new TuleapBacklogItem(232,
				new TuleapReference(200, "p/200"), "item232", null, null, null, null); //$NON-NLS-1$
		item2.setInitialEffort("201");
		ArtifactReference item2Parent = new ArtifactReference(233, "item/233", trackerRef);
		item2.setParent(item2Parent);
		item2.setType("Epics");
		item2.setStatus(TuleapStatus.valueOf("Closed"));

		TuleapBacklogItem item3 = new TuleapBacklogItem(233,
				new TuleapReference(200, "p/200"), "item233", null, null, null, null); //$NON-NLS-1$
		item3.setInitialEffort("201");
		ArtifactReference item3Parent = new ArtifactReference(234, "item/234", trackerRef);
		item3.setParent(item3Parent);
		item3.setType("Epics");
		item3.setStatus(TuleapStatus.valueOf("Closed"));

		TuleapBacklogItem item4 = new TuleapBacklogItem(234,
				new TuleapReference(200, "p/200"), "item234", null, null, null, null); //$NON-NLS-1$

		List<TuleapBacklogItem> backlog = new ArrayList<TuleapBacklogItem>();

		backlog.add(item0);
		backlog.add(item1);
		backlog.add(item2);
		backlog.add(item3);
		backlog.add(item4);
		MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(taskRepository, connector);
		converter.populateTaskData(taskData, milestone, null);
		converter.populateBacklog(taskData, backlog, null);

		TaskAttribute root = taskData.getRoot();

		assertNotNull(root.getAttribute(MilestonePlanningWrapper.BACKLOG));

		for (int i = 0; i < 4; i++) {
			String internalId = "200:0#" + (230 + i);
			TaskAttribute itemAtt = root.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + internalId);
			assertNotNull(itemAtt);
			assertTrue(itemAtt.getMetaData().isReadOnly());

			TaskAttribute itemLabel = root.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + internalId
					+ ID_SEPARATOR + AbstractTaskAttributeWrapper.SUFFIX_LABEL);
			assertNotNull(itemLabel);
			assertEquals("item23" + i, itemLabel.getValue()); //$NON-NLS-1$
			assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, itemLabel.getMetaData().getType());

			TaskAttribute itemEffort = root.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + internalId
					+ ID_SEPARATOR + BacklogItemWrapper.SUFFIX_BACKLOG_ITEM_POINTS);
			assertNotNull(itemEffort);
			assertEquals("201", itemEffort.getValue());
			assertEquals(TaskAttribute.TYPE_SHORT_TEXT, itemEffort.getMetaData().getType());

			TaskAttribute itemParent = root.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + internalId
					+ ID_SEPARATOR + BacklogItemWrapper.SUFFIX_BI_PARENT_ID);
			assertNotNull(itemParent);
			assertEquals("200:0#23" + (i + 1), itemParent.getValue());
			assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, itemParent.getMetaData().getType());

			TaskAttribute itemDisplayParent = root.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM
					+ internalId + ID_SEPARATOR + BacklogItemWrapper.SUFFIX_BI_PARENT_DISPLAY_ID);
			assertNotNull(itemDisplayParent);
			assertEquals("23" + (i + 1), itemDisplayParent.getValue());
			assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, itemDisplayParent.getMetaData().getType());

			TaskAttribute itemType = root.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + internalId
					+ ID_SEPARATOR + BacklogItemWrapper.SUFFIX_TYPE);
			assertNotNull(itemType);
			assertEquals("Epics", itemType.getValue());
			assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, itemType.getMetaData().getType());

			TaskAttribute itemStatus = root.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + internalId
					+ ID_SEPARATOR + BacklogItemWrapper.SUFFIX_STATUS);
			assertNotNull(itemStatus);
			assertEquals("Closed", itemStatus.getValue());
			assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, itemStatus.getMetaData().getType());
		}

		TaskAttribute lastItemStatus = root.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + "200:0#234"
				+ ID_SEPARATOR + BacklogItemWrapper.SUFFIX_STATUS);
		assertEquals(null, lastItemStatus);

	}

	/**
	 * Tests extracting the milestone backlog backlogItems from a taskData.
	 */
	@Test
	public void testExtractBacklog() {
		Date testDate = new Date();

		TuleapMilestone milestone = new TuleapMilestone(50,
				new TuleapReference(200, "p/200"), "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$

		TuleapMilestone submilestone100 = new TuleapMilestone(100,
				new TuleapReference(200, "p/200"), "The first submilestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$

		TuleapBacklogItem item0 = new TuleapBacklogItem(230,
				new TuleapReference(200, "p/200"), "item230", null, null, null, null); //$NON-NLS-1$
		item0.setInitialEffort("201");
		TuleapReference trackerRef = new TuleapReference(500, "tracker/500");
		ArtifactReference item0Parent = new ArtifactReference(231, "item/231", trackerRef);
		item0.setParent(item0Parent);
		item0.setType("User stories");
		item0.setStatus(TuleapStatus.valueOf("Closed"));

		TuleapBacklogItem item1 = new TuleapBacklogItem(231,
				new TuleapReference(200, "p/200"), "item231", null, null, null, null); //$NON-NLS-1$
		item1.setInitialEffort("201");
		ArtifactReference item1Parent = new ArtifactReference(232, "item/232", trackerRef);
		item1.setParent(item1Parent);
		item1.setType("User stories");
		item1.setStatus(TuleapStatus.valueOf("Closed"));

		TuleapBacklogItem item2 = new TuleapBacklogItem(232,
				new TuleapReference(200, "p/200"), "item232", null, null, null, null); //$NON-NLS-1$
		item2.setInitialEffort("201");
		ArtifactReference item2Parent = new ArtifactReference(233, "item/233", trackerRef);
		item2.setParent(item2Parent);
		item2.setType("User stories");
		item2.setStatus(TuleapStatus.valueOf("Closed"));

		TuleapBacklogItem item3 = new TuleapBacklogItem(233,
				new TuleapReference(200, "p/200"), "item233", null, null, null, null); //$NON-NLS-1$
		item3.setInitialEffort("201");
		ArtifactReference item3Parent = new ArtifactReference(234, "item/234", trackerRef);
		item3.setParent(item3Parent);
		item3.setType("User stories");
		item3.setStatus(TuleapStatus.valueOf("Closed"));

		List<TuleapBacklogItem> backlog = new ArrayList<TuleapBacklogItem>();
		backlog.add(item0);
		backlog.add(item1);

		List<TuleapBacklogItem> content = new ArrayList<TuleapBacklogItem>();
		content.add(item2);
		content.add(item3);

		MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(taskRepository, connector);
		converter.populateTaskData(taskData, milestone, null);
		converter.populateBacklog(taskData, backlog, null);
		converter.addSubmilestone(taskData, submilestone100, content, null);

		List<TuleapBacklogItem> extractedBacklog = converter.extractBacklog(taskData);
		assertNotNull(extractedBacklog);
		assertEquals(2, extractedBacklog.size());

		TuleapBacklogItem firstBI = extractedBacklog.get(0);
		assertEquals(230, firstBI.getId().intValue());
		assertEquals("item230", firstBI.getLabel());
		assertEquals("201", firstBI.getInitialEffort());
		assertEquals(231, firstBI.getParent().getId());
		assertEquals(null, firstBI.getParent().getUri());
		assertEquals("User stories", firstBI.getType());
		assertEquals(TuleapStatus.valueOf("Closed"), firstBI.getStatus());

		TuleapBacklogItem secondBI = extractedBacklog.get(1);
		assertEquals(231, secondBI.getId().intValue());
		assertEquals("item231", secondBI.getLabel());
		assertEquals("201", secondBI.getInitialEffort());
		assertEquals(232, secondBI.getParent().getId());
		assertEquals(null, secondBI.getParent().getUri());
		assertEquals("User stories", secondBI.getType());
		assertEquals(TuleapStatus.valueOf("Closed"), secondBI.getStatus());

	}

	/**
	 * Tests extracting a submilestone content backlogItems from a taskData.
	 */
	@Test
	public void testExtractContent() {
		Date testDate = new Date();

		TuleapMilestone milestone = new TuleapMilestone(50,
				new TuleapReference(200, "p/200"), "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$

		TuleapMilestone submilestone100 = new TuleapMilestone(100,
				new TuleapReference(200, "p/200"), "The first submilestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$

		TuleapBacklogItem item0 = new TuleapBacklogItem(230,
				new TuleapReference(200, "p/200"), "item230", null, null, null, null); //$NON-NLS-1$
		item0.setInitialEffort("201");
		TuleapReference trackerRef = new TuleapReference(500, "tracker/500");
		ArtifactReference item0Parent = new ArtifactReference(231, "item/231", trackerRef);
		item0.setParent(item0Parent);
		item0.setStatus(TuleapStatus.valueOf("Closed"));

		TuleapBacklogItem item1 = new TuleapBacklogItem(231,
				new TuleapReference(200, "p/200"), "item231", null, null, null, null); //$NON-NLS-1$
		item1.setInitialEffort("201");
		ArtifactReference item1Parent = new ArtifactReference(232, "item/232", trackerRef);
		item1.setParent(item1Parent);
		item1.setStatus(TuleapStatus.valueOf("Closed"));

		TuleapBacklogItem item2 = new TuleapBacklogItem(232,
				new TuleapReference(200, "p/200"), "item232", null, null, null, null); //$NON-NLS-1$
		item2.setInitialEffort("201");
		ArtifactReference item2Parent = new ArtifactReference(233, "item/233", trackerRef);
		item2.setParent(item2Parent);
		item2.setStatus(TuleapStatus.valueOf("Closed"));

		TuleapBacklogItem item3 = new TuleapBacklogItem(233,
				new TuleapReference(200, "p/200"), "item233", null, null, null, null); //$NON-NLS-1$
		item3.setInitialEffort("201");
		ArtifactReference item3Parent = new ArtifactReference(234, "item/234", trackerRef);
		item3.setParent(item3Parent);
		item3.setStatus(TuleapStatus.valueOf("Closed"));

		List<TuleapBacklogItem> backlog = new ArrayList<TuleapBacklogItem>();
		backlog.add(item0);
		backlog.add(item1);

		List<TuleapBacklogItem> content = new ArrayList<TuleapBacklogItem>();
		content.add(item2);
		content.add(item3);

		MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(taskRepository, connector);
		converter.populateTaskData(taskData, milestone, null);
		converter.populateBacklog(taskData, backlog, null);
		converter.addSubmilestone(taskData, submilestone100, content, null);

		List<TuleapBacklogItem> backlogBacklogItems = converter.extractContent(taskData, 100);
		assertNotNull(backlogBacklogItems);
		assertEquals(2, backlogBacklogItems.size());

		TuleapBacklogItem firstBI = backlogBacklogItems.get(0);
		assertEquals(232, firstBI.getId().intValue());
		assertEquals("item232", firstBI.getLabel());
		assertEquals("201", firstBI.getInitialEffort());
		assertEquals(233, firstBI.getParent().getId());
		assertEquals(null, firstBI.getParent().getUri());
		assertEquals(TuleapStatus.valueOf("Closed"), firstBI.getStatus());

		TuleapBacklogItem secondBI = backlogBacklogItems.get(1);
		assertEquals(233, secondBI.getId().intValue());
		assertEquals("item233", secondBI.getLabel());
		assertEquals("201", secondBI.getInitialEffort());
		assertEquals(234, secondBI.getParent().getId());
		assertEquals(null, secondBI.getParent().getUri());
		assertEquals(TuleapStatus.valueOf("Closed"), secondBI.getStatus());
	}

	/**
	 * Tests extracting submilestones list from a taskData.
	 */
	@Test
	public void testExtractMilestones() {
		Date testDate = new Date();

		TuleapMilestone milestone = new TuleapMilestone(50,
				new TuleapReference(200, "p/200"), "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$

		TuleapMilestone submilestone100 = new TuleapMilestone(100,
				new TuleapReference(200, "p/200"), "The first submilestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$
		submilestone100.setCapacity("55");
		submilestone100.setStartDate(testDate);
		submilestone100.setEndDate(testDate);

		TuleapMilestone submilestone150 = new TuleapMilestone(150,
				new TuleapReference(200, "p/200"), "The second submilestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$
		submilestone150.setCapacity("56");
		submilestone150.setStartDate(testDate);
		submilestone150.setEndDate(testDate);

		TuleapBacklogItem item0 = new TuleapBacklogItem(230,
				new TuleapReference(200, "p/200"), "item230", null, null, null, null); //$NON-NLS-1$
		item0.setInitialEffort("201");
		item0.setStatus(TuleapStatus.valueOf("Closed"));
		TuleapReference trackerRef = new TuleapReference(500, "tracker/500");
		ArtifactReference item0Parent = new ArtifactReference(231, "item/231", trackerRef);
		item0.setParent(item0Parent);
		TuleapBacklogItem item1 = new TuleapBacklogItem(231,
				new TuleapReference(200, "p/200"), "item231", null, null, null, null); //$NON-NLS-1$
		item1.setInitialEffort("201");
		item1.setStatus(TuleapStatus.valueOf("Closed"));
		ArtifactReference item1Parent = new ArtifactReference(232, "item/232", trackerRef);
		item1.setParent(item1Parent);
		TuleapBacklogItem item2 = new TuleapBacklogItem(232,
				new TuleapReference(200, "p/200"), "item232", null, null, null, null); //$NON-NLS-1$
		item2.setInitialEffort("201");
		item2.setStatus(TuleapStatus.valueOf("Closed"));
		ArtifactReference item2Parent = new ArtifactReference(233, "item/233", trackerRef);
		item2.setParent(item2Parent);
		TuleapBacklogItem item3 = new TuleapBacklogItem(233,
				new TuleapReference(200, "p/200"), "item233", null, null, null, null); //$NON-NLS-1$
		item3.setInitialEffort("201");
		item3.setStatus(TuleapStatus.valueOf("Closed"));
		ArtifactReference item3Parent = new ArtifactReference(234, "item/234", trackerRef);
		item3.setParent(item3Parent);

		List<TuleapBacklogItem> content100 = new ArrayList<TuleapBacklogItem>();
		content100.add(item0);
		content100.add(item1);

		List<TuleapBacklogItem> content150 = new ArrayList<TuleapBacklogItem>();
		content150.add(item2);
		content150.add(item3);

		MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(taskRepository, connector);
		converter.populateTaskData(taskData, milestone, null);
		converter.addSubmilestone(taskData, submilestone100, content100, null);
		converter.addSubmilestone(taskData, submilestone150, content150, null);

		List<TuleapMilestone> milestones = converter.extractMilestones(taskData);
		assertNotNull(milestones);
		assertEquals(2, milestones.size());

		TuleapMilestone firstMilestone = milestones.get(0);
		assertEquals(100, firstMilestone.getId().intValue());
		assertEquals("The first submilestone", firstMilestone.getLabel());
		assertEquals("55", firstMilestone.getCapacity());
		assertNotNull(firstMilestone.getStartDate());
		assertNotNull(firstMilestone.getEndDate());

		TuleapMilestone secondMilestone = milestones.get(1);
		assertEquals(150, secondMilestone.getId().intValue());
		assertEquals("The second submilestone", secondMilestone.getLabel());
		assertEquals("56", secondMilestone.getCapacity());
		assertNotNull(secondMilestone.getStartDate());
		assertNotNull(secondMilestone.getEndDate());
	}

}
