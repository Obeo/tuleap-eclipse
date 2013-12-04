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

import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.TuleapErrorMessage;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapPlanning;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapStatus;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonParser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tests of {@link TuleapJsonParser}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
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
	 * Tests the parsing of epic #301.
	 */
	@Test
	public void testDeserializePlanning400() {
		String planning400 = ParserUtil.loadFile("/plannings/planning400(releases).json");
		TuleapPlanning planning = parser.parsePlanning(planning400);
		checkPlanning400(planning);
	}

	@Test
	public void testDeserializeMilestone200() throws ParseException {
		String release200 = ParserUtil.loadFile("/milestones/release200.json");
		TuleapMilestone milestone = parser.parseMilestone(release200);
		checkRelease200(milestone);
	}

	@Test
	public void testDeserializeEpic300() throws ParseException {
		String epic300 = ParserUtil.loadFile("/backlog_items/epic300.json");
		TuleapBacklogItem item = parser.parseBacklogItem(epic300);
		checkEpic300(item);
	}

	@Test
	public void testParseError() {
		String json = "{\"error\":{\"code\":401,\"message\":\"Unauthorized: X-Auth-Token HTTP header required\"},\"debug\":{\"source\":\"TokenAuthentication.class.php:85 at authenticate stage\",\"stages\":{\"success\":[\"get\",\"route\",\"negotiate\"],\"failure\":[\"authenticate\",\"message\"]}}}";
		TuleapErrorMessage message = parser.getErrorMessage(json);
		assertEquals(401, message.getError().getCode());
		assertEquals("Unauthorized: X-Auth-Token HTTP header required", message.getError().getMessage());
		assertEquals("TokenAuthentication.class.php:85 at authenticate stage", message.getDebug().getSource());
		assertArrayEquals(new String[] {"get", "route", "negotiate" }, message.getDebug().getStages()
				.getSuccess());
		assertArrayEquals(new String[] {"authenticate", "message" }, message.getDebug().getStages()
				.getFailure());
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
		TuleapReference[] trackers = item.getBacklogTrackers();
		assertEquals(1, trackers.length);
		TuleapReference trackerRef = trackers[0];
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
		TuleapReference[] trackers = item.getBacklogTrackers();
		assertEquals(1, trackers.length);
		TuleapReference trackerRef = trackers[0];
		assertEquals(802, trackerRef.getId());
		assertEquals("trackers/802", trackerRef.getUri());
		assertEquals("plannings/401/milestones", item.getMilestonesUri());
		assertNull(item.getCardwallConfigurationUri()); // Will have to change when cardwalls are activated
	}

	/**
	 * Tests the parsing of epic #301.
	 * 
	 * @throws ParseException
	 *             if the test is badly configured
	 */
	@Test
	public void testDeserializeEpic301() throws ParseException {
		String epic = ParserUtil.loadFile("/backlog_items/epic301.json");
		TuleapBacklogItem item = parser.parseBacklogItem(epic);
		checkEpic301(item);
	}

	/**
	 * Checks that the given backlog item corresponds to epic 301.
	 * 
	 * @param item
	 *            The backlog item
	 * @throws ParseException
	 *             if the test is badly configured
	 */
	public static void checkEpic301(TuleapBacklogItem item) throws ParseException {
		assertEquals(301, item.getId());
		assertEquals(301, item.getArtifact().getId());
		assertEquals("artifacts/301", item.getArtifact().getUri());
		assertEquals(801, item.getArtifact().getTracker().getId());
		assertEquals("trackers/801", item.getArtifact().getTracker().getUri());
		assertEquals(302, item.getParent().getId());
		assertEquals("backlog_items/302", item.getParent().getUri());
		assertEquals(801, item.getParent().getTracker().getId());
		assertEquals("trackers/801", item.getParent().getTracker().getUri());
		assertEquals(3, item.getProject().getId());
		assertEquals("projects/3", item.getProject().getUri());
		assertEquals("Another important Epic", item.getLabel());
		assertEquals("backlog_items/301", item.getUri());
		assertEquals("backlog_items?id=301&group_id=3", item.getHtmlUrl());
		assertEquals(1, item.getSubmittedBy());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 23, 11, 44, 18, 963), item.getSubmittedOn());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 24, 15, 33, 18, 523), item.getLastUpdatedOn());
		assertEquals("40.5", item.getInitialEffort());
		assertEquals(TuleapStatus.Open, item.getStatus());
		assertEquals("Epics", item.getType());
	}

	/**
	 * Tests the parsing of epic #304.
	 * 
	 * @throws ParseException
	 *             if the test is badly configured
	 */
	@Test
	public void testDeserializeEpic304() throws ParseException {
		String epic = ParserUtil.loadFile("/backlog_items/epic304.json");
		TuleapBacklogItem item = parser.parseBacklogItem(epic);
		checkEpic304(item);
	}

	/**
	 * Checks that the given backlog item corresponds to epic 304.
	 * 
	 * @param item
	 *            The backlog item
	 * @throws ParseException
	 *             if the test is badly configured
	 */
	public static void checkEpic304(TuleapBacklogItem item) throws ParseException {
		assertEquals(304, item.getId());
		assertEquals(304, item.getArtifact().getId());
		assertEquals("artifacts/304", item.getArtifact().getUri());
		assertEquals(801, item.getArtifact().getTracker().getId());
		assertEquals("trackers/801", item.getArtifact().getTracker().getUri());
		assertEquals(305, item.getParent().getId());
		assertEquals("backlog_items/305", item.getParent().getUri());
		assertEquals(801, item.getParent().getTracker().getId());
		assertEquals("trackers/801", item.getParent().getTracker().getUri());
		assertEquals(3, item.getProject().getId());
		assertEquals("projects/3", item.getProject().getUri());
		assertEquals("Another important Epic", item.getLabel());
		assertEquals("backlog_items/301", item.getUri());
		assertEquals("backlog_items?id=301&group_id=3", item.getHtmlUrl());
		assertEquals(1, item.getSubmittedBy());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 23, 11, 44, 18, 963), item.getSubmittedOn());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 24, 15, 33, 18, 523), item.getLastUpdatedOn());
		assertNull(item.getInitialEffort());
		assertEquals(TuleapStatus.Open, item.getStatus());
	}

	/**
	 * Checks that the given backlog item corresponds to epic 300.
	 * 
	 * @param item
	 *            The backlog item The backlog item
	 * @throws ParseException
	 *             if the test is badly configured
	 */
	public static void checkEpic300(TuleapBacklogItem item) throws ParseException {
		assertEquals(300, item.getId());
		assertEquals(300, item.getArtifact().getId());
		assertEquals("artifacts/300", item.getArtifact().getUri());
		assertEquals(801, item.getArtifact().getTracker().getId());
		assertEquals("trackers/801", item.getArtifact().getTracker().getUri());
		assertEquals(301, item.getParent().getId());
		assertEquals("backlog_items/301", item.getParent().getUri());
		assertEquals(801, item.getParent().getTracker().getId());
		assertEquals("trackers/801", item.getParent().getTracker().getUri());
		assertEquals(3, item.getProject().getId());
		assertEquals("projects/3", item.getProject().getUri());
		assertEquals("An important Epic", item.getLabel());
		assertEquals("backlog_items/300", item.getUri());
		assertEquals("backlog_items?id=300&group_id=3", item.getHtmlUrl());
		assertEquals(1, item.getSubmittedBy());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 23, 11, 44, 18, 963), item.getSubmittedOn());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 24, 15, 33, 18, 523), item.getLastUpdatedOn());
		assertEquals("30", item.getInitialEffort());
		assertEquals(TuleapStatus.Closed, item.getStatus());
		assertEquals("Epics", item.getType());
	}

	/**
	 * Tests the parsing of user story #350.
	 * 
	 * @throws ParseException
	 *             if the test is badly configured
	 */
	@Test
	public void testDeserializeUserStory350() throws ParseException {
		String userStory = ParserUtil.loadFile("/backlog_items/userStory350.json");
		TuleapBacklogItem item = parser.parseBacklogItem(userStory);
		checkUserStory350(item);

	}

	/**
	 * Checks that the given backlog item corresponds to user story 350.
	 * 
	 * @param item
	 *            The backlog item
	 * @throws ParseException
	 *             if the test is badly configured
	 */
	public static void checkUserStory350(TuleapBacklogItem item) throws ParseException {
		assertEquals(350, item.getId());
		assertEquals(350, item.getArtifact().getId());
		assertEquals("artifacts/350", item.getArtifact().getUri());
		assertEquals(802, item.getArtifact().getTracker().getId());
		assertEquals("trackers/802", item.getArtifact().getTracker().getUri());
		assertEquals(351, item.getParent().getId());
		assertEquals("backlog_items/351", item.getParent().getUri());
		assertEquals(802, item.getParent().getTracker().getId());
		assertEquals("trackers/802", item.getParent().getTracker().getUri());
		assertEquals(3, item.getProject().getId());
		assertEquals("projects/3", item.getProject().getUri());
		assertEquals("An important User Story", item.getLabel());
		assertEquals("backlog_items/350", item.getUri());
		assertEquals("backlog_items?id=350&group_id=3", item.getHtmlUrl());
		assertEquals(1, item.getSubmittedBy());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 23, 11, 44, 18, 963), item.getSubmittedOn());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 24, 15, 33, 18, 523), item.getLastUpdatedOn());

		assertEquals("5", item.getInitialEffort());
		assertEquals(TuleapStatus.Open, item.getStatus());
		assertEquals("User stories", item.getType());
	}

	/**
	 * Test the parsing of the data set of the release 200.
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testRelease200Parsing() throws ParseException {
		String release200 = ParserUtil.loadFile("/milestones/release200.json");
		TuleapMilestone tuleapMilestone = parser.parseMilestone(release200);
		checkRelease200(tuleapMilestone);
	}

	/**
	 * Test the parsing of the data set of the release 300.
	 * 
	 * @throws Exception
	 *             If something goes wrong that shouldn't.
	 */
	@Test
	public void testRelease201Parsing() throws Exception {
		String release201 = ParserUtil.loadFile("/milestones/release201.json");
		TuleapMilestone tuleapMilestone = parser.parseMilestone(release201);
		checkRelease201(tuleapMilestone);
	}

	/**
	 * Checks the content of the given milestone corresponds to release 200. Mutualized between several tests.
	 * 
	 * @param tuleapMilestone
	 * @throws ParseException
	 */
	public static void checkRelease200(TuleapMilestone tuleapMilestone) throws ParseException {
		assertEquals(200, tuleapMilestone.getId());
		assertEquals(200, tuleapMilestone.getArtifact().getId());
		assertEquals("artifacts/200", tuleapMilestone.getArtifact().getUri());
		assertEquals(901, tuleapMilestone.getArtifact().getTracker().getId());
		assertEquals("trackers/901", tuleapMilestone.getArtifact().getTracker().getUri());
		assertEquals(201, tuleapMilestone.getParent().getId());
		assertEquals("milestones/201", tuleapMilestone.getParent().getUri());
		assertEquals(901, tuleapMilestone.getParent().getTracker().getId());
		assertEquals("trackers/901", tuleapMilestone.getParent().getTracker().getUri());
		assertEquals(3, tuleapMilestone.getProject().getId());
		assertEquals("projects/3", tuleapMilestone.getProject().getUri());
		assertEquals("Release 0.9", tuleapMilestone.getLabel()); //$NON-NLS-1$
		assertEquals("milestones/200", tuleapMilestone.getUri()); //$NON-NLS-1$
		assertNull(tuleapMilestone.getHtmlUrl());
		assertEquals(1, tuleapMilestone.getSubmittedBy());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 23, 11, 44, 18, 963), tuleapMilestone.getSubmittedOn());
		assertNull(tuleapMilestone.getLastUpdatedOn());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 23, 11, 44, 18, 963), tuleapMilestone.getStartDate());
		assertEquals(ParserUtil.getUTCDate(2013, 9, 23, 11, 44, 18, 963), tuleapMilestone.getEndDate());
		assertEquals("100", tuleapMilestone.getCapacity());
		assertEquals("Done", tuleapMilestone.getStatusValue());
		assertEquals("milestones/200/milestones", tuleapMilestone.getSubMilestonesUri());
		assertEquals("milestones/200/backlog", tuleapMilestone.getBacklogUri());
		assertEquals("milestones/200/content", tuleapMilestone.getContentUri());
	}

	/**
	 * Checks the content of the given milestone corresponds to release 201. Mutualized between several test
	 * cases.
	 * 
	 * @param tuleapMilestone
	 * @throws ParseException
	 */
	public static void checkRelease201(TuleapMilestone tuleapMilestone) throws ParseException {
		assertNotNull(tuleapMilestone);

		assertEquals(201, tuleapMilestone.getId());
		assertEquals(201, tuleapMilestone.getArtifact().getId());
		assertEquals("artifacts/201", tuleapMilestone.getArtifact().getUri());
		assertEquals(901, tuleapMilestone.getArtifact().getTracker().getId());
		assertEquals("trackers/901", tuleapMilestone.getArtifact().getTracker().getUri());
		assertEquals(202, tuleapMilestone.getParent().getId());
		assertEquals("milestones/202", tuleapMilestone.getParent().getUri());
		assertEquals(901, tuleapMilestone.getParent().getTracker().getId());
		assertEquals("trackers/901", tuleapMilestone.getParent().getTracker().getUri());
		assertEquals(3, tuleapMilestone.getProject().getId());
		assertEquals("projects/3", tuleapMilestone.getProject().getUri());
		assertEquals("Release TU", tuleapMilestone.getLabel()); //$NON-NLS-1$
		assertNull(tuleapMilestone.getLastUpdatedOn());
		assertEquals(ParserUtil.getUTCDate(2013, 9, 23, 11, 44, 18, 963), tuleapMilestone.getStartDate());
		assertEquals(ParserUtil.getUTCDate(2013, 10, 23, 11, 44, 18, 963), tuleapMilestone.getEndDate());
		assertEquals("75", tuleapMilestone.getCapacity());
		assertEquals("milestones/201", tuleapMilestone.getUri()); //$NON-NLS-1$
		assertNull(tuleapMilestone.getHtmlUrl());
		assertEquals("Current", tuleapMilestone.getStatusValue());
		assertEquals("milestones/201/milestones", tuleapMilestone.getSubMilestonesUri());
		assertEquals("milestones/201/backlog", tuleapMilestone.getBacklogUri());
		assertEquals("milestones/201/content", tuleapMilestone.getContentUri());
	}

}
