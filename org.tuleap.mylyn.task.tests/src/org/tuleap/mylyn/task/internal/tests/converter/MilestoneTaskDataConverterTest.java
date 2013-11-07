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

import java.util.Date;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.client.TuleapClientManager;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.data.converter.MilestoneTaskDataConverter;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapReference;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTracker;
import org.tuleap.mylyn.task.internal.core.repository.TuleapRepositoryConnector;

import static org.junit.Assert.fail;

/**
 * Tests of the milestone task data converter.
 * 
 * @author <a href="mailto:firas.bacha">Firas Bacha</a>
 */
public class MilestoneTaskDataConverterTest {

	/**
	 * Separator to use to compute the mylyn id of a configurable field {@link TaskAttribute}.
	 */
	public static final String FIELD_SEPARATOR = "_field-"; //$NON-NLS-1$

	/**
	 * Suffix used to compute the mylyn id of the task atribute that represents the column id.
	 */
	public static final String SUFFIX_STATUS_ID = "status_id"; //$NON-NLS-1$

	/**
	 * Suffix used for computing the card list task attribute id.
	 */
	public static final String SUFFIX_CARD_LIST = "cards"; //$NON-NLS-1$

	/**
	 * Suffix appended to the ids of Task Attributes representing labels.
	 */
	public static final String SUFFIX_LABEL = "lbl"; //$NON-NLS-1$

	/**
	 * Suffix appended to the ids of Task Attributes representing IDs.
	 */
	public static final String SUFFIX_ID = "id"; //$NON-NLS-1$

	/**
	 * Suffix used for computing the swimlane item task attribute id.
	 */
	public static final String SUFFIX_SWIMLANE_ITEM = "item"; //$NON-NLS-1$

	/**
	 * Separator used in computed ids.
	 */
	public static final char ID_SEPARATOR = '-';

	/**
	 * Id of the milestone list task attribute.
	 */
	public static final String COLUMN_LIST = "mta_cols"; //$NON-NLS-1$

	/**
	 * Id of the milestone list task attribute.
	 */
	public static final String SWIMLANE_LIST = "mta_lanes"; //$NON-NLS-1$

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

	private TuleapProjectConfiguration projectConfiguration;

	private TuleapServerConfiguration serverConfiguration;

	private TuleapRepositoryConnector connector;

	/**
	 * Configure the data for the tests.
	 */
	@Before
	public void setUp() {
		String repositoryUrl = "repository"; //$NON-NLS-1$
		String connectorKind = "kind"; //$NON-NLS-1$
		String taskId = "id"; //$NON-NLS-1$
		serverConfiguration = new TuleapServerConfiguration(repositoryUrl);

		projectConfiguration = new TuleapProjectConfiguration("The first project", 200); //$NON-NLS-1$
		serverConfiguration.addProject(projectConfiguration);

		final TuleapClientManager clientManager = new TuleapClientManager() {
			@Override
			public TuleapRestClient getRestClient(TaskRepository pTaskRepository) {
				return new TuleapRestClient(null, null, null, null, null);
			}
		};

		connector = new TuleapRepositoryConnector() {
			@Override
			public TuleapServerConfiguration getTuleapServerConfiguration(String pRepositoryUrl) {
				return serverConfiguration;
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
		submilestone100.setCapacity(Float.valueOf(123));
		submilestone100.setStartDate(new Date(START_TIME));
		submilestone100.setEndDate(new Date(END_TIME));

		MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(taskRepository, connector);
		converter.populateTaskData(taskData, milestone, null);

		TaskAttribute root = taskData.getRoot();
		fail("Implement the actual test");
	}

	/**
	 * Tests milestone backlogItem conversion.
	 */
	@Test
	@Ignore("Fix me ASAP")
	public void testMilestoneBacklogItem() {
		Date testDate = new Date();

		TuleapMilestone milestone = new TuleapMilestone(50, projectRef, "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$ 
				"HTML URL", testDate, testDate); //$NON-NLS-1$
		milestone.setCapacity(Float.valueOf(123));
		milestone.setStartDate(new Date(2013));
		milestone.setStatusLabel("Done");

		// FIXME Implement the test
		fail("Implement the actual test");
	}

	/**
	 * Tests the swimlanes list.
	 */
	@Test
	@Ignore("TODO when Enalean has managed cardwalls")
	public void testCardwallSwimlanes() {
		// Date testDate = new Date();
		//
		//		TuleapMilestone milestone = new TuleapMilestone(50, PROJECT_ID, "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$ 
		//				"HTML URL", testDate, testDate); //$NON-NLS-1$
		//
		// TuleapCardwall cardwall = new TuleapCardwall();
		// milestone.setCardwall(cardwall);
		//
		// TuleapSwimlane firstSwimlane = new TuleapSwimlane();
		// TuleapBacklogItem firstBacklogItem = new TuleapBacklogItem(700, 200);
		// firstSwimlane.setBacklogItem(firstBacklogItem);
		// cardwall.addSwimlane(firstSwimlane);
		//
		// TuleapSwimlane secondSwimlane = new TuleapSwimlane();
		// TuleapBacklogItem secondBacklogItem = new TuleapBacklogItem(1000, 200);
		// secondSwimlane.setBacklogItem(secondBacklogItem);
		// cardwall.addSwimlane(secondSwimlane);
		//
		// MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(taskRepository, connector);
		// converter.populateTaskData(taskData, milestone, null);
		//
		// TaskAttribute root = taskData.getRoot();
		//
		// TaskAttribute swimlaneList = root.getAttribute(SWIMLANE_LIST);
		// assertNotNull(swimlaneList);
		//
		// int id = 0;
		// // The first swimlane
		// String swimlaneId = SWIMLANE_LIST + ID_SEPARATOR + id++;
		// TaskAttribute firstSwimlaneTA = swimlaneList.getAttribute(swimlaneId);
		// assertNotNull(firstSwimlaneTA);
		//
		// TaskAttribute firstItemTA = firstSwimlaneTA.getAttribute(swimlaneId + ID_SEPARATOR
		// + SUFFIX_SWIMLANE_ITEM);
		//
		// TaskAttribute idFirstItemTA = firstItemTA.getAttribute(swimlaneId + ID_SEPARATOR
		// + SUFFIX_SWIMLANE_ITEM + ID_SEPARATOR + SUFFIX_ID);
		//
		// assertNotNull(idFirstItemTA);
		// assertEquals(TaskAttribute.TYPE_INTEGER, idFirstItemTA.getMetaData().getType());
		//		assertEquals("200:0#700", idFirstItemTA.getValue()); //$NON-NLS-1$
		//
		// // The second swimlane
		// swimlaneId = SWIMLANE_LIST + ID_SEPARATOR + id++;
		// TaskAttribute secondSwimlaneTA = swimlaneList.getAttribute(swimlaneId);
		// assertNotNull(secondSwimlaneTA);
		//
		// TaskAttribute secondItemTA = secondSwimlaneTA.getAttribute(swimlaneId + ID_SEPARATOR
		// + SUFFIX_SWIMLANE_ITEM);
		//
		// TaskAttribute idSecondItemTA = secondItemTA.getAttribute(swimlaneId + ID_SEPARATOR
		// + SUFFIX_SWIMLANE_ITEM + ID_SEPARATOR + SUFFIX_ID);
		//
		// assertNotNull(idSecondItemTA);
		// assertEquals(TaskAttribute.TYPE_INTEGER, idSecondItemTA.getMetaData().getType());
		//		assertEquals("200:0#1000", idSecondItemTA.getValue()); //$NON-NLS-1$
		fail("Implement");

	}

	/**
	 * Tests the columns list.
	 */
	@Test
	@Ignore
	public void testCardwallColumns() {
		// Date testDate = new Date();
		//
		//		TuleapMilestone milestone = new TuleapMilestone(50, PROJECT_ID, "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$ 
		//				"HTML URL", testDate, testDate); //$NON-NLS-1$
		//
		// TuleapCardwall cardwall = new TuleapCardwall();
		// milestone.setCardwall(cardwall);
		//		TuleapStatus firstColumnConfig = new TuleapStatus(600, "first column"); //$NON-NLS-1$
		//
		//		TuleapStatus secondColumnConfig = new TuleapStatus(800, "second column"); //$NON-NLS-1$
		//
		// cardwall.addStatus(firstColumnConfig);
		// cardwall.addStatus(secondColumnConfig);
		//
		// MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(taskRepository, connector);
		// converter.populateTaskData(taskData, milestone, null);
		//
		// TaskAttribute root = taskData.getRoot();
		//
		// TaskAttribute columnList = root.getAttribute(COLUMN_LIST);
		// assertNotNull(columnList);
		//
		// int i = 0;
		// // the first column
		// String attId = COLUMN_LIST + ID_SEPARATOR + i++;
		// TaskAttribute firstColumnTA = columnList.getAttribute(attId);
		//
		// TaskAttribute firstColumnIdTA = firstColumnTA.getAttribute(attId + ID_SEPARATOR + SUFFIX_ID);
		//
		// assertNotNull(firstColumnIdTA);
		// assertEquals(TaskAttribute.TYPE_INTEGER, firstColumnIdTA.getMetaData().getType());
		//		assertEquals("600", firstColumnIdTA.getValue()); //$NON-NLS-1$
		//
		// TaskAttribute firstColumnLabelTA = firstColumnTA.getAttribute(attId + ID_SEPARATOR + SUFFIX_LABEL);
		//
		// assertNotNull(firstColumnLabelTA);
		// assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, firstColumnLabelTA.getMetaData().getType());
		//		assertEquals("first column", firstColumnLabelTA.getValue()); //$NON-NLS-1$
		//
		// // the second column
		// attId = COLUMN_LIST + ID_SEPARATOR + i++;
		// TaskAttribute secondColumnTA = columnList.getAttribute(attId);
		//
		// TaskAttribute secondColumnIdTA = secondColumnTA.getAttribute(attId + ID_SEPARATOR + SUFFIX_ID);
		//
		// assertNotNull(secondColumnIdTA);
		// assertEquals(TaskAttribute.TYPE_INTEGER, secondColumnIdTA.getMetaData().getType());
		//		assertEquals("800", secondColumnIdTA.getValue()); //$NON-NLS-1$
		//
		// TaskAttribute secondColumnLabelTA = secondColumnTA.getAttribute(attId + ID_SEPARATOR +
		// SUFFIX_LABEL);
		//
		// assertNotNull(secondColumnLabelTA);
		// assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, secondColumnLabelTA.getMetaData().getType());
		//		assertEquals("second column", secondColumnLabelTA.getValue()); //$NON-NLS-1$
		fail("Implement");

	}

	/**
	 * Tests the cardwall cards with literal field value.
	 */
	@Test
	@Ignore
	public void testCardwallCardsFieldValue() {
		// Date testDate = new Date();
		//
		//		TuleapMilestone milestone = new TuleapMilestone(50, PROJECT_ID, "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$ 
		//				"HTML URL", testDate, testDate); //$NON-NLS-1$
		//
		// TuleapCardwall cardwall = new TuleapCardwall();
		// milestone.setCardwall(cardwall);
		//
		// TuleapSwimlane firstSwimlane = new TuleapSwimlane();
		// TuleapBacklogItem firstBacklogItem = new TuleapBacklogItem(500, 200);
		// firstSwimlane.setBacklogItem(firstBacklogItem);
		// cardwall.addSwimlane(firstSwimlane);
		//
		// TuleapCard firstCard = new TuleapCard(700, 200);
		// firstCard.setStatus(10000);
		//		LiteralFieldValue firstLiteralFieldValue = new LiteralFieldValue(1000, "300, 301, 302"); //$NON-NLS-1$
		// firstCard.addFieldValue(firstLiteralFieldValue);
		//
		// firstSwimlane.addCard(firstCard);
		//
		// MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(taskRepository, connector);
		// converter.populateTaskData(taskData, milestone, null);
		//
		// TaskAttribute swimlaneList = taskData.getRoot().getAttribute(SWIMLANE_LIST);
		//
		// int id = 0;
		// // The first swimlane
		// String swimlaneId = SWIMLANE_LIST + ID_SEPARATOR + id++;
		// TaskAttribute firstSwimlaneTA = swimlaneList.getAttribute(swimlaneId);
		//
		// TaskAttribute cardsList = firstSwimlaneTA.getMappedAttribute(swimlaneId + ID_SEPARATOR
		// + SUFFIX_CARD_LIST);
		// assertNotNull(cardsList);
		//
		// TaskAttribute firstCardTA = cardsList.getAttribute(swimlaneId + ID_SEPARATOR + SUFFIX_CARD_LIST
		//				+ ID_SEPARATOR + "200:700#0"); //$NON-NLS-1$
		// assertNotNull(firstCardTA);
		//
		// TaskAttribute idFirstCardTA = firstCardTA.getAttribute(swimlaneId + ID_SEPARATOR + SUFFIX_CARD_LIST
		//				+ ID_SEPARATOR + "200:700#0" + ID_SEPARATOR + SUFFIX_ID); //$NON-NLS-1$
		//
		// assertNotNull(idFirstCardTA);
		// assertEquals(TaskAttribute.TYPE_INTEGER, idFirstCardTA.getMetaData().getType());
		//		assertEquals("200:700#0", idFirstCardTA.getValue()); //$NON-NLS-1$
		//
		// TaskAttribute statusIdFirstCardTA = firstCardTA.getAttribute(swimlaneId + ID_SEPARATOR
		//				+ SUFFIX_CARD_LIST + ID_SEPARATOR + "200:700#0" + ID_SEPARATOR //$NON-NLS-1$
		// + SUFFIX_STATUS_ID);
		//
		// assertNotNull(statusIdFirstCardTA);
		// assertEquals(TaskAttribute.TYPE_INTEGER, statusIdFirstCardTA.getMetaData().getType());
		//		assertEquals("10000", statusIdFirstCardTA.getValue()); //$NON-NLS-1$
		//
		// TaskAttribute fieldValueFirstCardTA = firstCardTA.getAttribute(swimlaneId + ID_SEPARATOR
		//				+ SUFFIX_CARD_LIST + ID_SEPARATOR + "200:700#0" + FIELD_SEPARATOR + "1000"); //$NON-NLS-1$ //$NON-NLS-2$
		//
		// assertNotNull(fieldValueFirstCardTA);
		//		assertEquals("300, 301, 302", fieldValueFirstCardTA.getValue()); //$NON-NLS-1$
		fail("Implement");
	}

	/**
	 * Tests the cardwall cards with Bind field values.
	 */
	@Test
	@Ignore
	public void testCardwallCardsBindFieldValues() {
		// Date testDate = new Date();
		//
		//		TuleapMilestone milestone = new TuleapMilestone(50, PROJECT_ID, "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$ 
		//				"HTML URL", testDate, testDate); //$NON-NLS-1$
		//
		// TuleapCardwall cardwall = new TuleapCardwall();
		// milestone.setCardwall(cardwall);
		//
		// TuleapSwimlane firstSwimlane = new TuleapSwimlane();
		// TuleapBacklogItem firstBacklogItem = new TuleapBacklogItem(500, 200);
		// firstSwimlane.setBacklogItem(firstBacklogItem);
		// cardwall.addSwimlane(firstSwimlane);
		//
		// TuleapCard firstCard = new TuleapCard(700, 200);
		// firstCard.setStatus(10000);
		//
		// List<Integer> valueIds = new ArrayList<Integer>();
		// valueIds.add(new Integer(10));
		// valueIds.add(new Integer(20));
		// valueIds.add(new Integer(30));
		// BoundFieldValue firstBoundFieldValue = new BoundFieldValue(2000, valueIds);
		//
		// firstCard.addFieldValue(firstBoundFieldValue);
		//
		// firstSwimlane.addCard(firstCard);
		//
		// MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(taskRepository, connector);
		// converter.populateTaskData(taskData, milestone, null);
		//
		// TaskAttribute swimlaneList = taskData.getRoot().getAttribute(SWIMLANE_LIST);
		//
		// int id = 0;
		// // The first swimlane
		// String swimlaneId = SWIMLANE_LIST + ID_SEPARATOR + id++;
		// TaskAttribute firstSwimlaneTA = swimlaneList.getAttribute(swimlaneId);
		//
		// TaskAttribute cardsList = firstSwimlaneTA.getMappedAttribute(swimlaneId + ID_SEPARATOR
		// + SUFFIX_CARD_LIST);
		// assertNotNull(cardsList);
		//
		// TaskAttribute firstCardTA = cardsList.getAttribute(swimlaneId + ID_SEPARATOR + SUFFIX_CARD_LIST
		//				+ ID_SEPARATOR + "200:700#0"); //$NON-NLS-1$
		// assertNotNull(firstCardTA);
		//
		// TaskAttribute idFirstCardTA = firstCardTA.getAttribute(swimlaneId + ID_SEPARATOR + SUFFIX_CARD_LIST
		//				+ ID_SEPARATOR + "200:700#0" + ID_SEPARATOR + SUFFIX_ID); //$NON-NLS-1$
		//
		// assertNotNull(idFirstCardTA);
		// assertEquals(TaskAttribute.TYPE_INTEGER, idFirstCardTA.getMetaData().getType());
		//		assertEquals("200:700#0", idFirstCardTA.getValue()); //$NON-NLS-1$
		//
		// TaskAttribute statusIdFirstCardTA = firstCardTA.getAttribute(swimlaneId + ID_SEPARATOR
		//				+ SUFFIX_CARD_LIST + ID_SEPARATOR + "200:700#0" + ID_SEPARATOR //$NON-NLS-1$
		// + SUFFIX_STATUS_ID);
		//
		// assertNotNull(statusIdFirstCardTA);
		// assertEquals(TaskAttribute.TYPE_INTEGER, statusIdFirstCardTA.getMetaData().getType());
		//		assertEquals("10000", statusIdFirstCardTA.getValue()); //$NON-NLS-1$
		//
		// TaskAttribute fieldValueFirstCardTA = firstCardTA.getAttribute(swimlaneId + ID_SEPARATOR
		//				+ SUFFIX_CARD_LIST + ID_SEPARATOR + "200:700#0" + FIELD_SEPARATOR + "2000"); //$NON-NLS-1$ //$NON-NLS-2$
		//
		// assertNotNull(fieldValueFirstCardTA);
		// // FIXME manage the type of Bound fields
		// // assertEquals(TaskAttribute.TYPE_SINGLE_SELECT, fieldValueFirstCardTA.getMetaData().getType());
		// List<String> values = fieldValueFirstCardTA.getValues();
		// assertEquals(3, values.size());
		//		assertEquals("10", values.get(0)); //$NON-NLS-1$
		//		assertEquals("20", values.get(1)); //$NON-NLS-1$
		//		assertEquals("30", values.get(2)); //$NON-NLS-1$
		fail("Implement");

	}

	/**
	 * Tests the cardwall cards with file description.
	 */
	@Test
	@Ignore
	public void testCardwallCardsFileDescription() {
		// Date testDate = new Date();
		//
		//		TuleapMilestone milestone = new TuleapMilestone(50, PROJECT_ID, "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$ 
		//				"HTML URL", testDate, testDate); //$NON-NLS-1$
		// TuleapCardwall cardwall = new TuleapCardwall();
		// milestone.setCardwall(cardwall);
		//
		// TuleapSwimlane firstSwimlane = new TuleapSwimlane();
		// TuleapBacklogItem firstBacklogItem = new TuleapBacklogItem(500, 200);
		// firstSwimlane.setBacklogItem(firstBacklogItem);
		// cardwall.addSwimlane(firstSwimlane);
		//
		// TuleapCard firstCard = new TuleapCard(700, 200);
		// firstCard.setStatus(10000);
		//
		// List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		//		TuleapPerson firstUploadedBy = new TuleapPerson("first username", "first realname", 1, "first email"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		//		attachments.add(new AttachmentValue("100000", "first name", firstUploadedBy, 123456, //$NON-NLS-1$ //$NON-NLS-2$ 
		//				"first description", "first type")); //$NON-NLS-1$ //$NON-NLS-2$
		//		TuleapPerson secondUploadedBy = new TuleapPerson("second username", "second realname", 2, //$NON-NLS-1$ //$NON-NLS-2$
		//				"second email"); //$NON-NLS-1$
		//		attachments.add(new AttachmentValue("100001", "second name", secondUploadedBy, 789456, //$NON-NLS-1$ //$NON-NLS-2$
		//				"second description", "second type")); //$NON-NLS-1$ //$NON-NLS-2$
		// AttachmentFieldValue fileDescriptions = new AttachmentFieldValue(3000, attachments);
		//
		// firstCard.addFieldValue(fileDescriptions);
		//
		// firstSwimlane.addCard(firstCard);
		//
		// MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(taskRepository, connector);
		// converter.populateTaskData(taskData, milestone, null);
		//
		// TaskAttribute swimlaneList = taskData.getRoot().getAttribute(SWIMLANE_LIST);
		//
		// int id = 0;
		// // The first swimlane
		// String swimlaneId = SWIMLANE_LIST + ID_SEPARATOR + id++;
		// TaskAttribute firstSwimlaneTA = swimlaneList.getAttribute(swimlaneId);
		//
		// TaskAttribute cardsList = firstSwimlaneTA.getMappedAttribute(swimlaneId + ID_SEPARATOR
		// + SUFFIX_CARD_LIST);
		// assertNotNull(cardsList);
		//
		// TaskAttribute firstCardTA = cardsList.getAttribute(swimlaneId + ID_SEPARATOR + SUFFIX_CARD_LIST
		//				+ ID_SEPARATOR + "200:700#0"); //$NON-NLS-1$
		// assertNotNull(firstCardTA);
		//
		// TaskAttribute idFirstCardTA = firstCardTA.getAttribute(swimlaneId + ID_SEPARATOR + SUFFIX_CARD_LIST
		//				+ ID_SEPARATOR + "200:700#0" + ID_SEPARATOR + SUFFIX_ID); //$NON-NLS-1$
		//
		// assertNotNull(idFirstCardTA);
		// assertEquals(TaskAttribute.TYPE_INTEGER, idFirstCardTA.getMetaData().getType());
		//		assertEquals("200:700#0", idFirstCardTA.getValue()); //$NON-NLS-1$
		//
		// TaskAttribute statusIdFirstCardTA = firstCardTA.getAttribute(swimlaneId + ID_SEPARATOR
		//				+ SUFFIX_CARD_LIST + ID_SEPARATOR + "200:700#0" + ID_SEPARATOR //$NON-NLS-1$
		// + SUFFIX_STATUS_ID);
		//
		// assertNotNull(statusIdFirstCardTA);
		// assertEquals(TaskAttribute.TYPE_INTEGER, statusIdFirstCardTA.getMetaData().getType());
		//		assertEquals("10000", statusIdFirstCardTA.getValue()); //$NON-NLS-1$
		//
		// TaskAttribute fieldValueFirstCardTA = firstCardTA.getAttribute(swimlaneId + ID_SEPARATOR
		//				+ SUFFIX_CARD_LIST + ID_SEPARATOR + "200:700#0" + FIELD_SEPARATOR + "2000"); //$NON-NLS-1$ //$NON-NLS-2$  
		//
		// assertNull(fieldValueFirstCardTA);
		fail("Implement");
	}

}
