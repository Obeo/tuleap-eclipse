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

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.AbstractBacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;
import org.tuleap.mylyn.task.internal.core.data.AttachmentFieldValue;
import org.tuleap.mylyn.task.internal.core.data.AttachmentValue;
import org.tuleap.mylyn.task.internal.core.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.data.converter.MilestoneTaskDataConverter;
import org.tuleap.mylyn.task.internal.core.model.TuleapPerson;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapCard;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapCardwall;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestoneType;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapStatus;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapSwimlane;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

	/**
	 * The wrapped task data.
	 */
	private TaskData taskData;

	/**
	 * Configure the data for the tests.
	 */
	@Before
	public void setUp() {
		String repositoryUrl = "repository"; //$NON-NLS-1$
		String connectorKind = "kind"; //$NON-NLS-1$
		String taskId = "id"; //$NON-NLS-1$ 
		TaskRepository taskRepository = new TaskRepository(connectorKind, repositoryUrl);
		TaskAttributeMapper mapper = new TaskAttributeMapper(taskRepository);
		taskData = new TaskData(mapper, connectorKind, repositoryUrl, taskId);
	}

	/**
	 * Tests milestone submilestones conversion.
	 */
	@Test
	public void testMilestoneSubmilestones() {
		Date testDate = new Date();
		TuleapProjectConfiguration tuleapProjectConfiguration = new TuleapProjectConfiguration(
				"The first project", 200); //$NON-NLS-1$

		TuleapMilestone milestone = new TuleapMilestone(50, 500, 200, "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$

		TuleapMilestone submilestone100 = new TuleapMilestone(100, 500, 200, "submilestone100", "URL", //$NON-NLS-1$//$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$
		submilestone100.setCapacity(Float.valueOf(123));
		submilestone100.setDuration(Float.valueOf(80));
		submilestone100.setStartDate(new Date(2013));

		milestone.addSubMilestone(submilestone100);

		TuleapBacklogItem item200 = new TuleapBacklogItem(200, 1000, 200, "item200", null, null, null, null); //$NON-NLS-1$
		item200.setInitialEffort(Float.valueOf(201));
		item200.setAssignedMilestoneId(submilestone100.getId());
		milestone.addBacklogItem(item200);

		TuleapMilestoneType tuleapMilestoneType = new TuleapMilestoneType(10000, "The URL", //$NON-NLS-1$
				"The milestone type", "item name", "The description", testDate.getTime(), true); //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
		tuleapProjectConfiguration.addMilestoneType(tuleapMilestoneType);
		MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(tuleapMilestoneType);
		converter.populateTaskData(taskData, milestone);

		TaskAttribute root = taskData.getRoot();
		TaskAttribute planningAtt = root.getAttribute(MilestonePlanningWrapper.MILESTONE_PLANNING);
		assertNotNull(planningAtt);

		TaskAttribute milestoneListAtt = planningAtt.getAttribute(MilestonePlanningWrapper.MILESTONE_LIST);
		assertNotNull(milestoneListAtt);
		TaskAttribute milestone0 = milestoneListAtt.getAttribute(SubMilestoneWrapper.PREFIX_MILESTONE + "0"); //$NON-NLS-1$
		assertNotNull(milestone0);
		assertTrue(milestone0.getMetaData().isReadOnly());

		TaskAttribute capacity = milestone0.getAttribute(SubMilestoneWrapper.PREFIX_MILESTONE + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + SubMilestoneWrapper.SUFFIX_MILESTONE_CAPACITY);
		assertNotNull(capacity);
		assertEquals(Float.toString(123f), capacity.getValue());
		assertEquals(TaskAttribute.TYPE_DOUBLE, capacity.getMetaData().getType());

		TaskAttribute duration = milestone0.getAttribute(SubMilestoneWrapper.PREFIX_MILESTONE + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + SubMilestoneWrapper.SUFFIX_MILESTONE_DURATION);
		assertNotNull(duration);
		assertEquals(Float.toString(80f), duration.getValue());
		assertEquals(TaskAttribute.TYPE_DOUBLE, duration.getMetaData().getType());

		TaskAttribute id = milestone0.getAttribute(SubMilestoneWrapper.PREFIX_MILESTONE + "0" + ID_SEPARATOR //$NON-NLS-1$
				+ AbstractTaskAttributeWrapper.SUFFIX_ID);
		assertNotNull(id);
		assertEquals("200:500#100", id.getValue()); //$NON-NLS-1$
		assertEquals(TaskAttribute.TYPE_INTEGER, id.getMetaData().getType());

		TaskAttribute label = milestone0.getAttribute(SubMilestoneWrapper.PREFIX_MILESTONE + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + AbstractTaskAttributeWrapper.SUFFIX_LABEL);
		assertNotNull(label);
		assertEquals("submilestone100", label.getValue()); //$NON-NLS-1$
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, label.getMetaData().getType());

		TaskAttribute start = milestone0.getAttribute(SubMilestoneWrapper.PREFIX_MILESTONE + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + SubMilestoneWrapper.SUFFIX_START_DATE);
		assertNotNull(start);
		assertEquals("2013", start.getValue()); //$NON-NLS-1$
		assertEquals(TaskAttribute.TYPE_DATETIME, start.getMetaData().getType());
	}

	/**
	 * Tests milestone backlogItem conversion.
	 */
	@Test
	public void testMilestoneBacklogItem() {
		Date testDate = new Date();
		TuleapProjectConfiguration tuleapProjectConfiguration = new TuleapProjectConfiguration(
				"The first project", 200); //$NON-NLS-1$

		TuleapMilestone milestone = new TuleapMilestone(50, 500, 200, "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$ 
				"HTML URL", testDate, testDate); //$NON-NLS-1$

		TuleapMilestone submilestone100 = new TuleapMilestone(100, 500, 200, "submilestone100", "URL", //$NON-NLS //$NON-NLS-1$ //$NON-NLS-2$-1$  
				"HTML URL", testDate, testDate); //$NON-NLS-1$
		submilestone100.setCapacity(Float.valueOf(123));
		submilestone100.setDuration(Float.valueOf(80));
		submilestone100.setStartDate(new Date(2013));

		milestone.addSubMilestone(submilestone100);

		TuleapBacklogItem item200 = new TuleapBacklogItem(200, 1000, 200, "item200", null, null, null, null); //$NON-NLS-1$
		item200.setInitialEffort(Float.valueOf(201));
		item200.setAssignedMilestoneId(submilestone100.getId());
		milestone.addBacklogItem(item200);

		TuleapMilestoneType tuleapMilestoneType = new TuleapMilestoneType(10000, "The URL", //$NON-NLS-1$
				"The milestone type", "item name", "The description", testDate.getTime(), true); //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
		tuleapProjectConfiguration.addMilestoneType(tuleapMilestoneType);

		MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(tuleapMilestoneType);
		converter.populateTaskData(taskData, milestone);

		TaskAttribute root = taskData.getRoot();
		TaskAttribute planningAtt = root.getAttribute(MilestonePlanningWrapper.MILESTONE_PLANNING);
		assertNotNull(planningAtt);

		TaskAttribute backlogAtt = planningAtt.getAttribute(MilestonePlanningWrapper.BACKLOG);
		assertNotNull(backlogAtt);

		TaskAttribute item0 = backlogAtt.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + "0"); //$NON-NLS-1$
		assertNotNull(item0);
		assertTrue(item0.getMetaData().isReadOnly());

		TaskAttribute itemId = item0.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + "0" + ID_SEPARATOR //$NON-NLS-1$
				+ AbstractTaskAttributeWrapper.SUFFIX_ID);
		assertNotNull(itemId);
		assertEquals("200:1000#200", itemId.getValue()); //$NON-NLS-1$
		assertEquals(TaskAttribute.TYPE_INTEGER, itemId.getMetaData().getType());

		TaskAttribute itemLabel = item0.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + AbstractTaskAttributeWrapper.SUFFIX_LABEL);
		assertNotNull(itemLabel);
		assertEquals("item200", itemLabel.getValue()); //$NON-NLS-1$
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, itemLabel.getMetaData().getType());

		TaskAttribute itemEffort = item0.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + AbstractBacklogItemWrapper.SUFFIX_BACKLOG_ITEM_POINTS);
		assertNotNull(itemEffort);
		assertEquals(Float.toString(201), itemEffort.getValue());
		assertEquals(TaskAttribute.TYPE_DOUBLE, itemEffort.getMetaData().getType());
	}

	/**
	 * Tests the swimlanes list.
	 */
	@Test
	public void testCardwallSwimlanes() {
		Date testDate = new Date();
		TuleapProjectConfiguration tuleapProjectConfiguration = new TuleapProjectConfiguration(
				"The first project", 200); //$NON-NLS-1$

		TuleapMilestone milestone = new TuleapMilestone(50, 500, 200, "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$ 
				"HTML URL", testDate, testDate); //$NON-NLS-1$

		TuleapCardwall cardwall = new TuleapCardwall();
		milestone.setCardwall(cardwall);

		TuleapSwimlane firstSwimlane = new TuleapSwimlane();
		TuleapBacklogItem firstBacklogItem = new TuleapBacklogItem(700, 200);
		firstSwimlane.setBacklogItem(firstBacklogItem);
		cardwall.addSwimlane(firstSwimlane);

		TuleapSwimlane secondSwimlane = new TuleapSwimlane();
		TuleapBacklogItem secondBacklogItem = new TuleapBacklogItem(1000, 200);
		secondSwimlane.setBacklogItem(secondBacklogItem);
		cardwall.addSwimlane(secondSwimlane);

		TuleapMilestoneType tuleapMilestoneType = new TuleapMilestoneType(10000, "The URL", //$NON-NLS-1$
				"The milestone type", "item name", "The description", testDate.getTime(), true); //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
		tuleapProjectConfiguration.addMilestoneType(tuleapMilestoneType);

		MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(tuleapMilestoneType);
		converter.populateTaskData(taskData, milestone);

		TaskAttribute root = taskData.getRoot();

		TaskAttribute swimlaneList = root.getAttribute(SWIMLANE_LIST);
		assertNotNull(swimlaneList);

		// The first swimlane
		TaskAttribute firstSwimlaneTA = swimlaneList.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0"); //$NON-NLS-1$
		assertNotNull(firstSwimlaneTA);

		TaskAttribute firstItemTA = firstSwimlaneTA.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + SUFFIX_SWIMLANE_ITEM);

		TaskAttribute idFirstItemTA = firstItemTA.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + SUFFIX_SWIMLANE_ITEM + ID_SEPARATOR + SUFFIX_ID);

		assertNotNull(idFirstItemTA);
		assertEquals(TaskAttribute.TYPE_INTEGER, idFirstItemTA.getMetaData().getType());
		assertEquals("200:700#0", idFirstItemTA.getValue()); //$NON-NLS-1$

		// The second swimlane
		TaskAttribute secondSwimlaneTA = swimlaneList.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "1"); //$NON-NLS-1$
		assertNotNull(secondSwimlaneTA);

		TaskAttribute secondItemTA = secondSwimlaneTA.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "1" //$NON-NLS-1$
				+ ID_SEPARATOR + SUFFIX_SWIMLANE_ITEM);

		TaskAttribute idSecondItemTA = secondItemTA.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "1" //$NON-NLS-1$
				+ ID_SEPARATOR + SUFFIX_SWIMLANE_ITEM + ID_SEPARATOR + SUFFIX_ID);

		assertNotNull(idSecondItemTA);
		assertEquals(TaskAttribute.TYPE_INTEGER, idSecondItemTA.getMetaData().getType());
		assertEquals("200:1000#0", idSecondItemTA.getValue()); //$NON-NLS-1$

	}

	/**
	 * Tests the columns list.
	 */
	@Test
	public void testCardwallColumns() {

		Date testDate = new Date();
		TuleapProjectConfiguration tuleapProjectConfiguration = new TuleapProjectConfiguration(
				"The first project", 200); //$NON-NLS-1$
		TuleapMilestone milestone = new TuleapMilestone(50, 500, 200, "The first milestone", "URL", //$NON-NLS-1$//$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$
		TuleapCardwall cardwall = new TuleapCardwall();
		milestone.setCardwall(cardwall);
		TuleapStatus firstColumnConfig = new TuleapStatus(600, "I am the first column"); //$NON-NLS-1$

		TuleapStatus secondColumnConfig = new TuleapStatus(800, "I am the second column"); //$NON-NLS-1$

		cardwall.addStatus(firstColumnConfig);
		cardwall.addStatus(secondColumnConfig);

		TuleapMilestoneType tuleapMilestoneType = new TuleapMilestoneType(10000, "The URL", //$NON-NLS-1$
				"The milestone type", "item name", "The description", testDate.getTime(), true); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
		tuleapProjectConfiguration.addMilestoneType(tuleapMilestoneType);

		MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(tuleapMilestoneType);
		converter.populateTaskData(taskData, milestone);

		TaskAttribute root = taskData.getRoot();

		TaskAttribute columnList = root.getAttribute(COLUMN_LIST);
		assertNotNull(columnList);

		// the first column
		TaskAttribute firstColumnTA = columnList.getAttribute(COLUMN_LIST + ID_SEPARATOR + "0"); //$NON-NLS-1$

		TaskAttribute firstColumnIdTA = firstColumnTA.getAttribute(COLUMN_LIST + ID_SEPARATOR + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + SUFFIX_ID);

		assertNotNull(firstColumnIdTA);
		assertEquals(TaskAttribute.TYPE_INTEGER, firstColumnIdTA.getMetaData().getType());
		assertEquals("600", firstColumnIdTA.getValue()); //$NON-NLS-1$

		TaskAttribute firstColumnLabelTA = firstColumnTA.getAttribute(COLUMN_LIST + ID_SEPARATOR + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + SUFFIX_LABEL);

		assertNotNull(firstColumnLabelTA);
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, firstColumnLabelTA.getMetaData().getType());
		assertEquals("I am the first column", firstColumnLabelTA.getValue()); //$NON-NLS-1$

		// the second column
		TaskAttribute secondColumnTA = columnList.getAttribute(COLUMN_LIST + ID_SEPARATOR + "1"); //$NON-NLS-1$

		TaskAttribute secondColumnIdTA = secondColumnTA.getAttribute(COLUMN_LIST + ID_SEPARATOR + "1" //$NON-NLS-1$
				+ ID_SEPARATOR + SUFFIX_ID);

		assertNotNull(secondColumnIdTA);
		assertEquals(TaskAttribute.TYPE_INTEGER, secondColumnIdTA.getMetaData().getType());
		assertEquals("800", secondColumnIdTA.getValue()); //$NON-NLS-1$

		TaskAttribute secondColumnLabelTA = secondColumnTA.getAttribute(COLUMN_LIST + ID_SEPARATOR + "1" //$NON-NLS-1$
				+ ID_SEPARATOR + SUFFIX_LABEL);

		assertNotNull(secondColumnLabelTA);
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, secondColumnLabelTA.getMetaData().getType());
		assertEquals("I am the second column", secondColumnLabelTA.getValue()); //$NON-NLS-1$

	}

	/**
	 * Tests the cardwall cards with literal field value.
	 */
	@Test
	public void testCardwallCardsFieldValue() {

		Date testDate = new Date();
		TuleapProjectConfiguration tuleapProjectConfiguration = new TuleapProjectConfiguration(
				"The first project", 200); //$NON-NLS-1$
		TuleapMilestone milestone = new TuleapMilestone(50, 500, 200, "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$
		TuleapCardwall cardwall = new TuleapCardwall();
		milestone.setCardwall(cardwall);

		TuleapSwimlane firstSwimlane = new TuleapSwimlane();
		TuleapBacklogItem firstBacklogItem = new TuleapBacklogItem(500, 200);
		firstSwimlane.setBacklogItem(firstBacklogItem);
		cardwall.addSwimlane(firstSwimlane);

		TuleapCard firstCard = new TuleapCard(700, 200);
		firstCard.setStatus(10000);
		LiteralFieldValue firstLiteralFieldValue = new LiteralFieldValue(1000, "300, 301, 302"); //$NON-NLS-1$
		firstCard.addFieldValue(firstLiteralFieldValue);

		firstSwimlane.addCard(firstCard);

		TuleapMilestoneType tuleapMilestoneType = new TuleapMilestoneType(10000, "The URL", //$NON-NLS-1$
				"The milestone type", "item name", "The description", testDate.getTime(), true); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
		tuleapProjectConfiguration.addMilestoneType(tuleapMilestoneType);

		MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(tuleapMilestoneType);
		converter.populateTaskData(taskData, milestone);

		TaskAttribute swimlaneList = taskData.getRoot().getAttribute(SWIMLANE_LIST);

		// The first swimlane
		TaskAttribute firstSwimlaneTA = swimlaneList.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0"); //$NON-NLS-1$

		TaskAttribute cardsList = firstSwimlaneTA.getMappedAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + SUFFIX_CARD_LIST);
		assertNotNull(cardsList);

		TaskAttribute firstCardTA = cardsList.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0" + ID_SEPARATOR //$NON-NLS-1$
				+ SUFFIX_CARD_LIST + ID_SEPARATOR + "200:700#0"); //$NON-NLS-1$
		assertNotNull(firstCardTA);

		TaskAttribute idFirstCardTA = firstCardTA.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + SUFFIX_CARD_LIST + ID_SEPARATOR + "200:700#0" + ID_SEPARATOR + SUFFIX_ID); //$NON-NLS-1$

		assertNotNull(idFirstCardTA);
		assertEquals(TaskAttribute.TYPE_INTEGER, idFirstCardTA.getMetaData().getType());
		assertEquals("200:700#0", idFirstCardTA.getValue()); //$NON-NLS-1$

		TaskAttribute statusIdFirstCardTA = firstCardTA.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + SUFFIX_CARD_LIST + ID_SEPARATOR + "200:700#0" + ID_SEPARATOR //$NON-NLS-1$
				+ SUFFIX_STATUS_ID);

		assertNotNull(statusIdFirstCardTA);
		assertEquals(TaskAttribute.TYPE_INTEGER, statusIdFirstCardTA.getMetaData().getType());
		assertEquals("10000", statusIdFirstCardTA.getValue()); //$NON-NLS-1$

		TaskAttribute fieldValueFirstCardTA = firstCardTA.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + SUFFIX_CARD_LIST + ID_SEPARATOR + "200:700#0" + FIELD_SEPARATOR + "1000"); //$NON-NLS-1$ //$NON-NLS-2$

		assertNotNull(fieldValueFirstCardTA);
		assertEquals("300, 301, 302", fieldValueFirstCardTA.getValue()); //$NON-NLS-1$

	}

	/**
	 * Tests the cardwall cards with Bind field values.
	 */
	@Test
	public void testCardwallCardsBindFieldValues() {

		Date testDate = new Date();
		TuleapProjectConfiguration tuleapProjectConfiguration = new TuleapProjectConfiguration(
				"The first project", 200); //$NON-NLS-1$
		TuleapMilestone milestone = new TuleapMilestone(50, 500, 200, "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$
		TuleapCardwall cardwall = new TuleapCardwall();
		milestone.setCardwall(cardwall);

		TuleapSwimlane firstSwimlane = new TuleapSwimlane();
		TuleapBacklogItem firstBacklogItem = new TuleapBacklogItem(500, 200);
		firstSwimlane.setBacklogItem(firstBacklogItem);
		cardwall.addSwimlane(firstSwimlane);

		TuleapCard firstCard = new TuleapCard(700, 200);
		firstCard.setStatus(10000);

		List<Integer> valueIds = new ArrayList<Integer>();
		valueIds.add(new Integer(10));
		valueIds.add(new Integer(20));
		valueIds.add(new Integer(30));
		BoundFieldValue firstBoundFieldValue = new BoundFieldValue(2000, valueIds);

		firstCard.addFieldValue(firstBoundFieldValue);

		firstSwimlane.addCard(firstCard);

		TuleapMilestoneType tuleapMilestoneType = new TuleapMilestoneType(10000, "The URL", //$NON-NLS-1$
				"The milestone type", "item name", "The description", testDate.getTime(), true); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		tuleapProjectConfiguration.addMilestoneType(tuleapMilestoneType);

		MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(tuleapMilestoneType);
		converter.populateTaskData(taskData, milestone);

		TaskAttribute swimlaneList = taskData.getRoot().getAttribute(SWIMLANE_LIST);

		// The first swimlane
		TaskAttribute firstSwimlaneTA = swimlaneList.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0"); //$NON-NLS-1$

		TaskAttribute cardsList = firstSwimlaneTA.getMappedAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + SUFFIX_CARD_LIST);
		assertNotNull(cardsList);

		TaskAttribute firstCardTA = cardsList.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0" + ID_SEPARATOR //$NON-NLS-1$
				+ SUFFIX_CARD_LIST + ID_SEPARATOR + "200:700#0"); //$NON-NLS-1$
		assertNotNull(firstCardTA);

		TaskAttribute idFirstCardTA = firstCardTA.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + SUFFIX_CARD_LIST + ID_SEPARATOR + "200:700#0" + ID_SEPARATOR + SUFFIX_ID); //$NON-NLS-1$

		assertNotNull(idFirstCardTA);
		assertEquals(TaskAttribute.TYPE_INTEGER, idFirstCardTA.getMetaData().getType());
		assertEquals("200:700#0", idFirstCardTA.getValue()); //$NON-NLS-1$

		TaskAttribute statusIdFirstCardTA = firstCardTA.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + SUFFIX_CARD_LIST + ID_SEPARATOR + "200:700#0" + ID_SEPARATOR //$NON-NLS-1$
				+ SUFFIX_STATUS_ID);

		assertNotNull(statusIdFirstCardTA);
		assertEquals(TaskAttribute.TYPE_INTEGER, statusIdFirstCardTA.getMetaData().getType());
		assertEquals("10000", statusIdFirstCardTA.getValue()); //$NON-NLS-1$

		TaskAttribute fieldValueFirstCardTA = firstCardTA.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + SUFFIX_CARD_LIST + ID_SEPARATOR + "200:700#0" + FIELD_SEPARATOR + "2000"); //$NON-NLS-1$ //$NON-NLS-2$

		assertNull(fieldValueFirstCardTA);

	}

	/**
	 * Tests the cardwall cards with file description.
	 */
	@Test
	public void testCardwallCardsFileDescription() {

		Date testDate = new Date();
		TuleapProjectConfiguration tuleapProjectConfiguration = new TuleapProjectConfiguration(
				"The first project", 200); //$NON-NLS-1$
		TuleapMilestone milestone = new TuleapMilestone(50, 500, 200, "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", testDate, testDate); //$NON-NLS-1$
		TuleapCardwall cardwall = new TuleapCardwall();
		milestone.setCardwall(cardwall);

		TuleapSwimlane firstSwimlane = new TuleapSwimlane();
		TuleapBacklogItem firstBacklogItem = new TuleapBacklogItem(500, 200);
		firstSwimlane.setBacklogItem(firstBacklogItem);
		cardwall.addSwimlane(firstSwimlane);

		TuleapCard firstCard = new TuleapCard(700, 200);
		firstCard.setStatus(10000);

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

		TuleapMilestoneType tuleapMilestoneType = new TuleapMilestoneType(10000, "The URL", //$NON-NLS-1$
				"The milestone type", "item name", "The description", testDate.getTime(), true); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		tuleapProjectConfiguration.addMilestoneType(tuleapMilestoneType);

		MilestoneTaskDataConverter converter = new MilestoneTaskDataConverter(tuleapMilestoneType);
		converter.populateTaskData(taskData, milestone);

		TaskAttribute swimlaneList = taskData.getRoot().getAttribute(SWIMLANE_LIST);

		// The first swimlane
		TaskAttribute firstSwimlaneTA = swimlaneList.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0"); //$NON-NLS-1$

		TaskAttribute cardsList = firstSwimlaneTA.getMappedAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + SUFFIX_CARD_LIST);
		assertNotNull(cardsList);

		TaskAttribute firstCardTA = cardsList.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0" + ID_SEPARATOR //$NON-NLS-1$
				+ SUFFIX_CARD_LIST + ID_SEPARATOR + "200:700#0"); //$NON-NLS-1$
		assertNotNull(firstCardTA);

		TaskAttribute idFirstCardTA = firstCardTA.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + SUFFIX_CARD_LIST + ID_SEPARATOR + "200:700#0" + ID_SEPARATOR + SUFFIX_ID); //$NON-NLS-1$

		assertNotNull(idFirstCardTA);
		assertEquals(TaskAttribute.TYPE_INTEGER, idFirstCardTA.getMetaData().getType());
		assertEquals("200:700#0", idFirstCardTA.getValue()); //$NON-NLS-1$

		TaskAttribute statusIdFirstCardTA = firstCardTA.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + SUFFIX_CARD_LIST + ID_SEPARATOR + "200:700#0" + ID_SEPARATOR //$NON-NLS-1$
				+ SUFFIX_STATUS_ID);

		assertNotNull(statusIdFirstCardTA);
		assertEquals(TaskAttribute.TYPE_INTEGER, statusIdFirstCardTA.getMetaData().getType());
		assertEquals("10000", statusIdFirstCardTA.getValue()); //$NON-NLS-1$

		TaskAttribute fieldValueFirstCardTA = firstCardTA.getAttribute(SWIMLANE_LIST + ID_SEPARATOR + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + SUFFIX_CARD_LIST + ID_SEPARATOR + "200:700#0" + FIELD_SEPARATOR + "2000"); //$NON-NLS-1$ //$NON-NLS-2$  

		assertNull(fieldValueFirstCardTA);
	}

}
