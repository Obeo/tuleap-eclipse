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
package org.tuleap.mylyn.task.internal.tests.parser;

import java.text.ParseException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapPlanning;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapTopPlanning;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests of {@link TuleapJsonParser}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapJsonParserTest {

	/**
	 * The project configuration provider.
	 */
	private StubProjectProvider provider = new StubProjectProvider();

	/**
	 * The parser to test.
	 */
	private TuleapJsonParser parser = new TuleapJsonParser();

	/**
	 * Tests the parsing of backlog items.
	 * 
	 * @throws ParseException
	 *             if the test is badly configured
	 */
	@Test
	public void testParseBacklogItems() throws ParseException {
		String json = ParserUtil.loadFile("/milestones/backlog_items_rel201.json");
		List<TuleapBacklogItem> backlogItems = parser.parseBacklogItems(json);
		assertEquals(2, backlogItems.size());

		TuleapBacklogItem item = backlogItems.get(0);
		TuleapBacklogItemDeserializerTest.checkEpic300(item);

		item = backlogItems.get(1);
		TuleapBacklogItemDeserializerTest.checkEpic301(item);
	}

	/**
	 * Checks the parsing of milestones.
	 * 
	 * @throws ParseException
	 *             If the parsing of a date goes wrong.
	 */
	@Test
	public void testParseMilestones() throws ParseException {
		String json = ParserUtil.loadFile("/milestones/releases.json");
		List<TuleapMilestone> milestones = parser.parseMilestones(json);
		assertEquals(2, milestones.size());

		new TuleapMilestoneDeserializerTests().checkRelease200(milestones.get(0));
		new TuleapMilestoneDeserializerTests().checkRelease201(milestones.get(1));
	}

	/**
	 * Checks the parsing of milestone types.
	 * 
	 * @throws ParseException
	 *             if a parsing error occurs.
	 */
	@Test
	@Ignore("To do when Enalean has chosen about top plannings")
	public void testParseTopPlannings() throws ParseException {
		String json = ParserUtil.loadFile("/top_plannings/top_plannings_prj3.json");
		List<TuleapTopPlanning> plannings = parser.parseTopPlannings(json);
		assertEquals(1, plannings.size());

		TuleapTopPlanning tp = plannings.get(0);
		new TuleapTopPlanningDeserializerTests().checkTopPlanning30(tp);
	}

	/**
	 * Tests the parsing of epic #301.
	 */
	@Test
	public void testDeserializePlanning400() {
		String epic = ParserUtil.loadFile("/plannings/planning400(releases).json");
		TuleapPlanning planning = parser.parsePlanning(epic);
		checkPlanning400(planning);
	}

	/**
	 * Tests the deserialization of a list of plannings.
	 */
	@Test
	public void testDeserializePlanningList() {
		String list = ParserUtil.loadFile("/plannings/prj3_plannings.json");
		List<TuleapPlanning> planningList = parser.parsePlanningList(list);
		assertEquals(2, planningList.size());
		checkPlanning400(planningList.get(0));
		checkPlanning401(planningList.get(1));
	}

	/**
	 * Tests the deserialization of a list of plannings that actually is not a JSON array but directly an
	 * object.
	 */
	@Test
	public void testDeserializePlanningListWhenListIsOnlyAnObject() {
		String epic = ParserUtil.loadFile("/plannings/planning400(releases).json");
		List<TuleapPlanning> planningList = parser.parsePlanningList(epic);
		assertEquals(1, planningList.size());
		checkPlanning400(planningList.get(0));
	}

	/**
	 * Checks that the given backlog item corresponds to epic 301.
	 * 
	 * @param item
	 *            The backlog item
	 */
	public static void checkPlanning400(TuleapPlanning item) {
		assertEquals(400, item.getId());
		assertEquals(3, item.getProject().getId());
		assertEquals("projects/3", item.getProject().getUri());
		assertEquals("Releases Planning", item.getLabel());
		assertEquals("plannings/400", item.getUri());
		assertEquals(901, item.getMilestoneTracker().getId());
		assertEquals("trackers/901", item.getMilestoneTracker().getUri());
		List<TuleapReference> trackers = item.getBacklogTrackers();
		assertEquals(1, trackers.size());
		TuleapReference trackerRef = trackers.get(0);
		assertEquals(801, trackerRef.getId());
		assertEquals("trackers/801", trackerRef.getUri());
		assertEquals("plannings/400/milestones", item.getMilestonesUri());
		assertNull(item.getCardwallConfigurationUri()); // Will have to change when cardwalls are activated
	}

	/**
	 * Checks that the given backlog item corresponds to epic 301.
	 * 
	 * @param item
	 *            The backlog item
	 */
	public static void checkPlanning401(TuleapPlanning item) {
		assertEquals(401, item.getId());
		assertEquals(3, item.getProject().getId());
		assertEquals("projects/3", item.getProject().getUri());
		assertEquals("Sprints Planning", item.getLabel());
		assertEquals("plannings/401", item.getUri());
		assertEquals(902, item.getMilestoneTracker().getId());
		assertEquals("trackers/902", item.getMilestoneTracker().getUri());
		List<TuleapReference> trackers = item.getBacklogTrackers();
		assertEquals(1, trackers.size());
		TuleapReference trackerRef = trackers.get(0);
		assertEquals(802, trackerRef.getId());
		assertEquals("trackers/802", trackerRef.getUri());
		assertEquals("plannings/401/milestones", item.getMilestonesUri());
		assertNull(item.getCardwallConfigurationUri()); // Will have to change when cardwalls are activated
	}
}
