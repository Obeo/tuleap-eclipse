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
package org.eclipse.mylyn.tuleap.core.tests.internal.parser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tuleap.core.internal.model.TuleapErrorMessage;
import org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapPlanning;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapProject;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapResource;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTrackerReport;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapUser;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapUserGroup;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapWorkflow;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapMultiSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBoxItem;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapString;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapReference;
import org.eclipse.mylyn.tuleap.core.internal.model.data.agile.TuleapBacklogItem;
import org.eclipse.mylyn.tuleap.core.internal.model.data.agile.TuleapMilestone;
import org.eclipse.mylyn.tuleap.core.internal.model.data.agile.TuleapStatus;
import org.eclipse.mylyn.tuleap.core.internal.parser.TuleapGsonProvider;
import org.eclipse.mylyn.tuleap.core.internal.util.ITuleapConstants;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests of JSON deserialization.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapJsonParserTest {

	private Gson gson;

	@Before
	public void setUp() {
		gson = TuleapGsonProvider.defaultGson();
	}

	/**
	 * Tests the parsing of epic #301.
	 */
	@Test
	public void testDeserializePlanning400() {
		String json = ParserUtil.loadFile("/plannings/planning400(releases).json");
		TuleapPlanning planning = gson.fromJson(json, TuleapPlanning.class);
		checkPlanning400(planning);
	}

	@Test
	public void testDeserializeMilestone200() throws ParseException {
		String json = ParserUtil.loadFile("/milestones/release200.json");
		TuleapMilestone milestone = gson.fromJson(json, TuleapMilestone.class);
		checkRelease200(milestone);
	}

	@Test
	public void testDeserializeEpic300() throws ParseException {
		String json = ParserUtil.loadFile("/backlog_items/epic300.json");
		TuleapBacklogItem item = gson.fromJson(json, TuleapBacklogItem.class);
		checkEpic300(item);
	}

	@Test
	public void testDeserializeUserGroup() throws ParseException {
		String json = ParserUtil.loadFile("/groups/project_admins.json");
		TuleapUserGroup userGroup = gson.fromJson(json, TuleapUserGroup.class);
		assertEquals("1", userGroup.getId());
		assertEquals("Project Administrators", userGroup.getLabel());
		assertEquals("/user_groups/1", userGroup.getUri());
	}

	@Test
	public void testDeserializeUser() throws ParseException {
		String json = ParserUtil.loadFile("/users/an_user.json");
		TuleapUser aUser = gson.fromJson(json, TuleapUser.class);
		assertEquals(0, aUser.getId());
		assertEquals("John Doe", aUser.getRealName());
		assertEquals("doej", aUser.getUserName());
		assertEquals("john.doe@example.com", aUser.getEmail());
	}

	@Test
	public void testDeserializeTrackerReport() throws ParseException {
		String json = ParserUtil.loadFile("/tracker_reports/tracker_report-2.json");
		TuleapTrackerReport report = gson.fromJson(json, TuleapTrackerReport.class);
		assertEquals(2, report.getId());
		assertEquals("Third report", report.getLabel());
		assertEquals("localhost:3001/api/v3.14/trackers/0/2", report.getReportUrl());
		assertEquals("/tracker_reports/2", report.getReportUri());
	}

	@Test
	public void testParseError() {
		String json = "{\"error\":{\"code\":401,\"message\":\"Unauthorized: X-Auth-Token HTTP header required\"},\"debug\":{\"source\":\"TokenAuthentication.class.php:85 at authenticate stage\",\"stages\":{\"success\":[\"get\",\"route\",\"negotiate\"],\"failure\":[\"authenticate\",\"message\"]}}}";
		TuleapErrorMessage message = gson.fromJson(json, TuleapErrorMessage.class);
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
		assertEquals(400, item.getId().intValue());
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
		assertEquals(401, item.getId().intValue());
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
		String json = ParserUtil.loadFile("/backlog_items/epic301.json");
		TuleapBacklogItem item = gson.fromJson(json, TuleapBacklogItem.class);
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
		assertEquals(301, item.getId().intValue());
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
		assertEquals(ParserUtil.getUTCDate(2013, 8, 24, 15, 33, 18, 523), item.getLastModifiedDate());
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
		String json = ParserUtil.loadFile("/backlog_items/epic304.json");
		TuleapBacklogItem item = gson.fromJson(json, TuleapBacklogItem.class);
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
		assertEquals(304, item.getId().intValue());
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
		assertEquals(ParserUtil.getUTCDate(2013, 8, 24, 15, 33, 18, 523), item.getLastModifiedDate());
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
		assertEquals(300, item.getId().intValue());
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
		assertEquals(ParserUtil.getUTCDate(2013, 8, 24, 15, 33, 18, 523), item.getLastModifiedDate());
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
		String json = ParserUtil.loadFile("/backlog_items/userStory350.json");
		TuleapBacklogItem item = gson.fromJson(json, TuleapBacklogItem.class);
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
		assertEquals(350, item.getId().intValue());
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
		assertEquals(ParserUtil.getUTCDate(2013, 8, 24, 15, 33, 18, 523), item.getLastModifiedDate());

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
		String json = ParserUtil.loadFile("/milestones/release200.json");
		TuleapMilestone tuleapMilestone = gson.fromJson(json, TuleapMilestone.class);
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
		String json = ParserUtil.loadFile("/milestones/release201.json");
		TuleapMilestone tuleapMilestone = gson.fromJson(json, TuleapMilestone.class);
		checkRelease201(tuleapMilestone);
	}

	/**
	 * Checks the content of the given milestone corresponds to release 200. Mutualized between several tests.
	 * 
	 * @param tuleapMilestone
	 * @throws ParseException
	 */
	public static void checkRelease200(TuleapMilestone tuleapMilestone) throws ParseException {
		assertEquals(200, tuleapMilestone.getId().intValue());
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
		assertNull(tuleapMilestone.getLastModifiedDate());
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

		assertEquals(201, tuleapMilestone.getId().intValue());
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
		assertNull(tuleapMilestone.getLastModifiedDate());
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

	/**
	 * Test the parsing of the first file.
	 */
	@Test
	public void testDeserializeProject0() {
		String json = ParserUtil.loadFile("/projects/project-0.json");
		TuleapProject tuleapProject = gson.fromJson(json, TuleapProject.class);
		assertNotNull(tuleapProject);
		assertEquals(101, tuleapProject.getIdentifier());
		assertEquals("Project 0", tuleapProject.getLabel()); //$NON-NLS-1$
		assertEquals("projects/101", tuleapProject.getUri());
		assertEquals(4, tuleapProject.getProjectResources().length);

		assertEquals("trackers", tuleapProject.getProjectResources()[0].getType());
		assertEquals("projects/101/trackers", tuleapProject.getProjectResources()[0].getUri());

		assertEquals("backlog", tuleapProject.getProjectResources()[1].getType());
		assertEquals("projects/101/backlog", tuleapProject.getProjectResources()[1].getUri());

		assertEquals("milestones", tuleapProject.getProjectResources()[2].getType());
		assertEquals("projects/101/milestones", tuleapProject.getProjectResources()[2].getUri());

		assertEquals("plannings", tuleapProject.getProjectResources()[3].getType());
		assertEquals("projects/101/plannings", tuleapProject.getProjectResources()[3].getUri());

		assertTrue(tuleapProject.hasResource("backlog"));
		assertTrue(tuleapProject.hasResource("milestones"));
		assertTrue(tuleapProject.hasResource("plannings"));
		assertTrue(tuleapProject.hasResource("trackers"));
		assertFalse(tuleapProject.hasResource("Another resource"));
	}

	/**
	 * Test the parsing of the second file.
	 */
	@Test
	public void testDeserializeProject1() {
		String json = ParserUtil.loadFile("/projects/project-1.json");
		TuleapProject tuleapProject = gson.fromJson(json, TuleapProject.class);
		assertNotNull(tuleapProject);
		assertEquals(102, tuleapProject.getIdentifier());
		assertEquals("Project 1", tuleapProject.getLabel()); //$NON-NLS-1$
		assertEquals("projects/102", tuleapProject.getUri());
		assertEquals(3, tuleapProject.getProjectResources().length);

		assertEquals("backlog", tuleapProject.getProjectResources()[0].getType());
		assertEquals("projects/102/backlog", tuleapProject.getProjectResources()[0].getUri());

		assertEquals("milestones", tuleapProject.getProjectResources()[1].getType());
		assertEquals("projects/102/milestones", tuleapProject.getProjectResources()[1].getUri());

		assertEquals("plannings", tuleapProject.getProjectResources()[2].getType());
		assertEquals("projects/102/plannings", tuleapProject.getProjectResources()[2].getUri());

		assertTrue(tuleapProject.hasResource("backlog"));
		assertTrue(tuleapProject.hasResource("milestones"));
		assertTrue(tuleapProject.hasResource("plannings"));
		assertFalse(tuleapProject.hasResource("trackers"));

	}

	/**
	 * Test the parsing of the third file.
	 */
	@Test
	public void testDeserializeProject2() {
		String json = ParserUtil.loadFile("/projects/project-2.json");
		TuleapProject tuleapProject = gson.fromJson(json, TuleapProject.class);
		assertNotNull(tuleapProject);
		assertEquals(103, tuleapProject.getIdentifier());
		assertEquals("Project 2", tuleapProject.getLabel()); //$NON-NLS-1$
		assertEquals("projects/103", tuleapProject.getUri());
		assertEquals(1, tuleapProject.getProjectResources().length);

		assertEquals("trackers", tuleapProject.getProjectResources()[0].getType());
		assertEquals("projects/103/trackers", tuleapProject.getProjectResources()[0].getUri());

		assertTrue(tuleapProject.hasResource("trackers"));
		assertFalse(tuleapProject.hasResource("plannings"));
	}

	/**
	 * Test the parsing of the fourth file.
	 */
	@Test
	public void testDeserializeProject3() {
		String json = ParserUtil.loadFile("/projects/project-3.json");
		TuleapProject tuleapProject = gson.fromJson(json, TuleapProject.class);
		assertNotNull(tuleapProject);
		assertEquals(104, tuleapProject.getIdentifier());
		assertEquals("Project 3", tuleapProject.getLabel()); //$NON-NLS-1$
		assertEquals("projects/104", tuleapProject.getUri());
		assertEquals(2, tuleapProject.getProjectResources().length);

		assertEquals("trackers", tuleapProject.getProjectResources()[0].getType());
		assertEquals("projects/104/trackers", tuleapProject.getProjectResources()[0].getUri());

		assertEquals("plannings", tuleapProject.getProjectResources()[1].getType());
		assertEquals("projects/104/plannings", tuleapProject.getProjectResources()[1].getUri());

		assertTrue(tuleapProject.hasResource("trackers"));
	}

	/**
	 * Test the parsing of the fifth file.
	 */
	@Test
	public void testDeserializeProject4() {
		String json = ParserUtil.loadFile("/projects/project-4.json");
		TuleapProject tuleapProject = gson.fromJson(json, TuleapProject.class);
		assertNotNull(tuleapProject);
		assertEquals(105, tuleapProject.getIdentifier());
		assertEquals("Project 4", tuleapProject.getLabel()); //$NON-NLS-1$
		assertEquals("projects/105", tuleapProject.getUri());
		assertEquals(0, tuleapProject.getProjectResources().length);

		assertFalse(tuleapProject.hasResource("trackers"));
	}

	/**
	 * Test the parsing of the projects file.
	 */
	@Test
	public void testDeserializeProjects() {
		String projects = ParserUtil.loadFile("/projects/projects.json");
		JsonParser jsonParser = new JsonParser();
		JsonArray asJsonArray = jsonParser.parse(projects).getAsJsonArray();

		List<TuleapProject> projectsConfiguration = new ArrayList<TuleapProject>();
		for (int i = 0; i < asJsonArray.size(); i++) {
			JsonElement jsonElement = asJsonArray.get(i);
			TuleapProject tuleapProject = gson.fromJson(jsonElement, TuleapProject.class);
			projectsConfiguration.add(tuleapProject);
		}

		Iterator<TuleapProject> iterator = projectsConfiguration.iterator();

		TuleapProject firstProjectConfiguration = iterator.next();

		assertEquals(101, firstProjectConfiguration.getIdentifier());
		assertEquals("Project 0", firstProjectConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("projects/101", firstProjectConfiguration.getUri());
		assertEquals(4, firstProjectConfiguration.getProjectResources().length);

		assertEquals("trackers", firstProjectConfiguration.getProjectResources()[0].getType());
		assertEquals("projects/101/trackers", firstProjectConfiguration.getProjectResources()[0].getUri());

		assertEquals("backlog", firstProjectConfiguration.getProjectResources()[1].getType());
		assertEquals("projects/101/backlog", firstProjectConfiguration.getProjectResources()[1].getUri());

		assertEquals("milestones", firstProjectConfiguration.getProjectResources()[2].getType());
		assertEquals("projects/101/milestones", firstProjectConfiguration.getProjectResources()[2].getUri());

		assertEquals("plannings", firstProjectConfiguration.getProjectResources()[3].getType());
		assertEquals("projects/101/plannings", firstProjectConfiguration.getProjectResources()[3].getUri());

		assertTrue(firstProjectConfiguration.hasResource("backlog"));
		assertTrue(firstProjectConfiguration.hasResource("milestones"));
		assertTrue(firstProjectConfiguration.hasResource("plannings"));
		assertTrue(firstProjectConfiguration.hasResource("trackers"));
		assertFalse(firstProjectConfiguration.hasResource("Another resource"));

		TuleapProject secondProjectConfiguration = iterator.next();

		assertEquals(102, secondProjectConfiguration.getIdentifier());
		assertEquals("Project 1", secondProjectConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("projects/102", secondProjectConfiguration.getUri());
		assertEquals(3, secondProjectConfiguration.getProjectResources().length);

		assertEquals("backlog", secondProjectConfiguration.getProjectResources()[0].getType());
		assertEquals("projects/102/backlog", secondProjectConfiguration.getProjectResources()[0].getUri());

		assertEquals("milestones", secondProjectConfiguration.getProjectResources()[1].getType());
		assertEquals("projects/102/milestones", secondProjectConfiguration.getProjectResources()[1].getUri());

		assertEquals("plannings", secondProjectConfiguration.getProjectResources()[2].getType());
		assertEquals("projects/102/plannings", secondProjectConfiguration.getProjectResources()[2].getUri());

		assertTrue(secondProjectConfiguration.hasResource("backlog"));
		assertTrue(secondProjectConfiguration.hasResource("milestones"));
		assertTrue(secondProjectConfiguration.hasResource("plannings"));
		assertFalse(secondProjectConfiguration.hasResource("trackers"));

		TuleapProject thirdProjectConfiguration = iterator.next();

		assertEquals(103, thirdProjectConfiguration.getIdentifier());
		assertEquals("Project 2", thirdProjectConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("projects/103", thirdProjectConfiguration.getUri());
		assertEquals(1, thirdProjectConfiguration.getProjectResources().length);

		assertEquals("trackers", thirdProjectConfiguration.getProjectResources()[0].getType());
		assertEquals("projects/103/trackers", thirdProjectConfiguration.getProjectResources()[0].getUri());

		assertTrue(thirdProjectConfiguration.hasResource("trackers"));
		assertFalse(thirdProjectConfiguration.hasResource("plannings"));

		TuleapProject fourthProjectConfiguration = iterator.next();

		assertEquals(104, fourthProjectConfiguration.getIdentifier());
		assertEquals("Project 3", fourthProjectConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("projects/104", fourthProjectConfiguration.getUri());
		assertEquals(2, fourthProjectConfiguration.getProjectResources().length);

		assertEquals("trackers", fourthProjectConfiguration.getProjectResources()[0].getType());
		assertEquals("projects/104/trackers", fourthProjectConfiguration.getProjectResources()[0].getUri());

		assertEquals("plannings", fourthProjectConfiguration.getProjectResources()[1].getType());
		assertEquals("projects/104/plannings", fourthProjectConfiguration.getProjectResources()[1].getUri());

		assertTrue(fourthProjectConfiguration.hasResource("trackers"));

		TuleapProject fifthProjectConfiguration = iterator.next();

		assertEquals(105, fifthProjectConfiguration.getIdentifier());
		assertEquals("Project 4", fifthProjectConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("projects/105", fifthProjectConfiguration.getUri());
		assertEquals(0, fifthProjectConfiguration.getProjectResources().length);

		assertFalse(fifthProjectConfiguration.hasResource("trackers"));

	}

	/**
	 * Test the parsing of the first tracker.
	 */
	@Test
	public void testTracker0Parsing() {
		String tracker0 = ParserUtil.loadFile("/trackers/tracker-0.json");
		TuleapTracker tuleapTracker = gson.fromJson(tracker0, TuleapTracker.class);
		assertNotNull(tuleapTracker);
		assertEquals(0, tuleapTracker.getIdentifier());
		assertEquals("Product", tuleapTracker.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/plugins/tracker/?tracker=0", tuleapTracker.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/0", tuleapTracker.getUri()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tuleapTracker.getFields();
		assertEquals(1, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();

		AbstractTuleapField firstField = iterator.next();

		assertEquals(0, firstField.getIdentifier());
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, firstField.getMetadataType());

		assertEquals("Title", firstField.getLabel()); //$NON-NLS-1$

		assertTrue(firstField.isReadable());
		assertFalse(firstField.isSubmitable());
		assertFalse(firstField.isUpdatable());

		assertEquals(1, tuleapTracker.getTrackerResources().length);

		TuleapResource tuleapResource = tuleapTracker.getTrackerResources()[0];
		assertEquals("reports", tuleapResource.getType()); //$NON-NLS-1$
		assertEquals("trackers/0/tracker_reports", tuleapResource.getUri()); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the second tracker.
	 */
	@Test
	public void testTracker1Parsing() {
		String tracker1 = ParserUtil.loadFile("/trackers/tracker-1.json");
		TuleapTracker tuleapTracker = gson.fromJson(tracker1, TuleapTracker.class);
		assertNotNull(tuleapTracker);
		assertEquals(1, tuleapTracker.getIdentifier());
		assertEquals("Bugs", tuleapTracker.getLabel()); //$NON-NLS-1$
		assertEquals("localhost: 3001/plugins/tracker/?tracker=1", tuleapTracker.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/1", tuleapTracker.getUri()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tuleapTracker.getFields();
		assertEquals(13, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();

		// testing the semantic title
		AbstractTuleapField firstField = iterator.next();
		TuleapString stringField = (TuleapString)firstField;
		assertTrue(firstField instanceof TuleapString);
		assertTrue(stringField.isSemanticTitle());

		iterator.next();
		iterator.next();
		iterator.next();
		AbstractTuleapField fieldDifficulty = iterator.next();

		// testing the field id
		assertEquals(4, fieldDifficulty.getIdentifier());
		// testing the field type
		assertEquals(TaskAttribute.TYPE_DOUBLE, fieldDifficulty.getMetadataType());
		assertEquals("Difficulty", fieldDifficulty.getLabel()); //$NON-NLS-1$

		// testing the field permissions
		assertTrue(fieldDifficulty.isReadable());
		assertTrue(fieldDifficulty.isSubmitable());
		assertFalse(fieldDifficulty.isUpdatable());

		// testing the field values
		AbstractTuleapField fifthField = iterator.next();
		assertTrue(fifthField instanceof TuleapSelectBox);
		TuleapSelectBox selectBoxField = (TuleapSelectBox)fifthField;

		Collection<TuleapSelectBoxItem> items = selectBoxField.getItems();
		assertEquals(4, items.size());

		Iterator<TuleapSelectBoxItem> itemsIterator = items.iterator();
		itemsIterator.next();
		itemsIterator.next();

		// testing the item label
		TuleapSelectBoxItem selectBoxItem = itemsIterator.next();
		assertEquals("Network", selectBoxItem.getLabel()); //$NON-NLS-1$

		// testing the field binding
		iterator.next();
		iterator.next();
		iterator.next();
		iterator.next();
		AbstractTuleapField fieldAssignedTo = iterator.next();
		assertTrue(fieldAssignedTo instanceof TuleapMultiSelectBox);
		TuleapMultiSelectBox multiSelectBoxField = (TuleapMultiSelectBox)fieldAssignedTo;
		assertEquals("users", multiSelectBoxField.getBinding()); //$NON-NLS-1$

		// testing the semantic contributor
		assertTrue(multiSelectBoxField.isSemanticContributor());

		AbstractTuleapField fieldStatus = iterator.next();
		assertTrue(fieldStatus instanceof TuleapSelectBox);
		TuleapSelectBox sbStatus = (TuleapSelectBox)fieldStatus;
		// testing the semantic status
		assertTrue(sbStatus.isSemanticStatus());
		// testing the open statuses
		assertEquals(3, sbStatus.getOpenStatus().size());
		// Workflow
		checkWorkflow1(sbStatus, sbStatus);

		assertEquals(1, tuleapTracker.getTrackerResources().length);

		TuleapResource tuleapResource = tuleapTracker.getTrackerResources()[0];
		assertEquals("reports", tuleapResource.getType()); //$NON-NLS-1$
		assertEquals("trackers/1/tracker_reports", tuleapResource.getUri()); //$NON-NLS-1$
	}

	/**
	 * @param selectBoxField
	 * @param sbStatus
	 */
	private void checkWorkflow1(TuleapSelectBox selectBoxField, TuleapSelectBox sbStatus) {
		TuleapWorkflow workflow = sbStatus.getWorkflow();
		Collection<TuleapSelectBoxItem> statesFromInitial = workflow
				.accessibleStates(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID);
		assertEquals(1, statesFromInitial.size());
		assertEquals(0, statesFromInitial.iterator().next().getIdentifier());
		Collection<TuleapSelectBoxItem> statesFrom0 = workflow.accessibleStates(0);
		assertEquals(1, statesFrom0.size());
		assertEquals(1, statesFrom0.iterator().next().getIdentifier());
		Collection<TuleapSelectBoxItem> statesFrom1 = workflow.accessibleStates(1);
		assertEquals(1, statesFrom1.size());
		assertEquals(2, statesFrom1.iterator().next().getIdentifier());
		Collection<TuleapSelectBoxItem> statesFrom2 = workflow.accessibleStates(2);
		assertEquals(3, statesFrom2.size());
		assertTrue(statesFrom2.containsAll(Arrays.asList(selectBoxField.getItem("3"), selectBoxField
				.getItem("4"), selectBoxField.getItem("5"))));
		Collection<TuleapSelectBoxItem> statesFrom3 = workflow.accessibleStates(3);
		assertEquals(1, statesFrom3.size());
		Collection<TuleapSelectBoxItem> statesFrom4 = workflow.accessibleStates(4);
		assertEquals(1, statesFrom4.size());
		Collection<TuleapSelectBoxItem> statesFrom5 = workflow.accessibleStates(5);
		assertEquals(1, statesFrom5.size());
	}

	/**
	 * Test the parsing of the third file.
	 */
	@Test
	public void testTracker2Parsing() {
		String tracker2 = ParserUtil.loadFile("/trackers/tracker-2.json");
		TuleapTracker tuleapTracker = gson.fromJson(tracker2, TuleapTracker.class);
		assertNotNull(tuleapTracker);
		assertEquals(2, tuleapTracker.getIdentifier());
		assertEquals("Release", tuleapTracker.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/plugins/tracker/?tracker=2", tuleapTracker.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/2", tuleapTracker.getUri()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tuleapTracker.getFields();
		assertEquals(5, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();
		iterator.next();
		iterator.next();
		AbstractTuleapField secondField = iterator.next();

		assertEquals(2, secondField.getIdentifier());
		assertEquals(TaskAttribute.TYPE_DATE, secondField.getMetadataType());

		assertEquals("End Date", secondField.getLabel()); //$NON-NLS-1$

		assertTrue(secondField.isReadable());
		assertTrue(secondField.isSubmitable());
		assertTrue(secondField.isUpdatable());

		assertEquals(1, tuleapTracker.getTrackerResources().length);

		TuleapResource tuleapResource = tuleapTracker.getTrackerResources()[0];
		assertEquals("reports", tuleapResource.getType()); //$NON-NLS-1$
		assertEquals("trackers/2/tracker_reports", tuleapResource.getUri()); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the fourth tracker.
	 */
	@Test
	public void testTracker3Parsing() {
		String tracker3 = ParserUtil.loadFile("/trackers/tracker-3.json");
		TuleapTracker tuleapTracker = gson.fromJson(tracker3, TuleapTracker.class);
		assertNotNull(tuleapTracker);
		assertEquals(3, tuleapTracker.getIdentifier());
		assertEquals("Sprint", tuleapTracker.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/plugins/tracker/?tracker=3", tuleapTracker.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/3", tuleapTracker.getUri()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tuleapTracker.getFields();
		assertEquals(5, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();
		iterator.next();
		iterator.next();
		AbstractTuleapField thirdField = iterator.next();

		assertEquals(2, thirdField.getIdentifier());
		assertEquals(TaskAttribute.TYPE_SINGLE_SELECT, thirdField.getMetadataType());

		assertEquals("Status", thirdField.getLabel()); //$NON-NLS-1$

		assertTrue(thirdField.isReadable());
		assertTrue(thirdField.isSubmitable());
		assertTrue(thirdField.isUpdatable());

		assertEquals(1, tuleapTracker.getTrackerResources().length);

		TuleapResource tuleapResource = tuleapTracker.getTrackerResources()[0];
		assertEquals("reports", tuleapResource.getType()); //$NON-NLS-1$
		assertEquals("trackers/3/tracker_reports", tuleapResource.getUri()); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the fifth tracker.
	 */
	@Test
	public void testTracker4Parsing() {
		String tracker4 = ParserUtil.loadFile("/trackers/tracker-4.json");
		TuleapTracker tuleapTracker = gson.fromJson(tracker4, TuleapTracker.class);
		assertNotNull(tuleapTracker);
		assertEquals(4, tuleapTracker.getIdentifier());
		assertEquals("Tests", tuleapTracker.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/plugins/tracker/?tracker=4", tuleapTracker.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/4", tuleapTracker.getUri()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tuleapTracker.getFields();
		assertEquals(3, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();
		iterator.next();
		iterator.next();
		AbstractTuleapField thirdField = iterator.next();

		assertEquals(2, thirdField.getIdentifier());
		assertEquals(TaskAttribute.TYPE_INTEGER, thirdField.getMetadataType());
		// We don't keep the "short_name" information since it is useless with REST
		// assertEquals("initial-effort", thirdField.getName()); //$NON-NLS-1$
		assertEquals("Initial Effort", thirdField.getLabel()); //$NON-NLS-1$

		assertTrue(thirdField.isReadable());
		assertTrue(thirdField.isSubmitable());
		assertTrue(thirdField.isUpdatable());

		assertEquals(1, tuleapTracker.getTrackerResources().length);

		TuleapResource tuleapResource = tuleapTracker.getTrackerResources()[0];
		assertEquals("reports", tuleapResource.getType()); //$NON-NLS-1$
		assertEquals("trackers/4/tracker_reports", tuleapResource.getUri()); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the sixth tracker.
	 */
	@Test
	public void testTracker5Parsing() {
		String tracker5 = ParserUtil.loadFile("/trackers/tracker-5.json");
		TuleapTracker tuleapTracker = gson.fromJson(tracker5, TuleapTracker.class);
		assertNotNull(tuleapTracker);
		assertEquals(5, tuleapTracker.getIdentifier());
		assertEquals("User Stories", tuleapTracker.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/plugins/tracker/?tracker=5", tuleapTracker.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/5", tuleapTracker.getUri()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tuleapTracker.getFields();
		assertEquals(2, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();
		iterator.next();
		AbstractTuleapField secondField = iterator.next();

		assertEquals(1, secondField.getIdentifier());
		assertEquals(TaskAttribute.TYPE_SINGLE_SELECT, secondField.getMetadataType());
		// We don't keep the "short_name" information since it is useless with REST
		//		assertEquals("status", secondField.getName()); //$NON-NLS-1$
		assertEquals("Status", secondField.getLabel()); //$NON-NLS-1$

		assertTrue(secondField.isReadable());
		assertTrue(secondField.isSubmitable());
		assertTrue(secondField.isUpdatable());

		assertEquals(1, tuleapTracker.getTrackerResources().length);

		TuleapResource tuleapResource = tuleapTracker.getTrackerResources()[0];
		assertEquals("reports", tuleapResource.getType()); //$NON-NLS-1$
		assertEquals("trackers/5/tracker_reports", tuleapResource.getUri()); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the first trackers group file.
	 */
	@Test
	public void testFirstTrackersGroupParsing() {
		String firstTrackersGroup = ParserUtil.loadFile("/trackers/trackers_part_1.json");

		JsonParser jsonParser = new JsonParser();
		JsonArray asJsonArray = jsonParser.parse(firstTrackersGroup).getAsJsonArray();

		List<TuleapTracker> trackers = new ArrayList<TuleapTracker>();
		for (int i = 0; i < asJsonArray.size(); i++) {
			JsonElement jsonElement = asJsonArray.get(i);
			TuleapTracker tuleapTracker = gson.fromJson(jsonElement, TuleapTracker.class);
			trackers.add(tuleapTracker);
		}

		Iterator<TuleapTracker> iterator = trackers.iterator();

		TuleapTracker firstTrackerConfiguration = iterator.next();

		assertEquals(0, firstTrackerConfiguration.getIdentifier());
		assertEquals("Product", firstTrackerConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/plugins/tracker/?tracker=0", firstTrackerConfiguration.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/0", firstTrackerConfiguration.getUri()); //$NON-NLS-1$

		TuleapTracker secondTrackerConfiguration = iterator.next();

		assertEquals(1, secondTrackerConfiguration.getIdentifier());
		assertEquals("Bugs", secondTrackerConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("localhost: 3001/plugins/tracker/?tracker=1", secondTrackerConfiguration.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/1", secondTrackerConfiguration.getUri()); //$NON-NLS-1$

		TuleapTracker thirdTrackerConfiguration = iterator.next();

		assertEquals(2, thirdTrackerConfiguration.getIdentifier());
		assertEquals("Release", thirdTrackerConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/plugins/tracker/?tracker=2", thirdTrackerConfiguration.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/2", thirdTrackerConfiguration.getUri()); //$NON-NLS-1$

		TuleapTracker fourthTrackerConfiguration = iterator.next();

		assertEquals(3, fourthTrackerConfiguration.getIdentifier());
		assertEquals("Sprint", fourthTrackerConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/plugins/tracker/?tracker=3", fourthTrackerConfiguration.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/3", fourthTrackerConfiguration.getUri()); //$NON-NLS-1$

		TuleapTracker fifthTrackerConfiguration = iterator.next();

		assertEquals(4, fifthTrackerConfiguration.getIdentifier());
		assertEquals("Tests", fifthTrackerConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/plugins/tracker/?tracker=4", fifthTrackerConfiguration.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/4", fifthTrackerConfiguration.getUri()); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the first trackers group file.
	 */
	@Test
	public void testSecondTrackersGroupParsing() {
		String secondTrackersGroup = ParserUtil.loadFile("/trackers/trackers_part_2.json");

		JsonParser jsonParser = new JsonParser();
		JsonArray asJsonArray = jsonParser.parse(secondTrackersGroup).getAsJsonArray();

		List<TuleapTracker> trackers = new ArrayList<TuleapTracker>();
		for (int i = 0; i < asJsonArray.size(); i++) {
			JsonElement jsonElement = asJsonArray.get(i);
			TuleapTracker tuleapTracker = gson.fromJson(jsonElement, TuleapTracker.class);
			trackers.add(tuleapTracker);
		}

		Iterator<TuleapTracker> iterator = trackers.iterator();

		TuleapTracker sixthTrackerConfiguration = iterator.next();

		assertEquals(5, sixthTrackerConfiguration.getIdentifier());
		assertEquals("User Stories", sixthTrackerConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/plugins/tracker/?tracker=5", sixthTrackerConfiguration.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/5", sixthTrackerConfiguration.getUri()); //$NON-NLS-1$
	}
}
