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
import org.eclipse.mylyn.tuleap.core.internal.parser.TuleapGsonProvider;
import org.eclipse.mylyn.tuleap.core.internal.util.ITuleapConstants;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
		assertEquals(1, tuleapProject.getProjectResources().length);

		assertEquals("trackers", tuleapProject.getProjectResources()[0].getType());
		assertEquals("projects/101/trackers", tuleapProject.getProjectResources()[0].getUri());

		assertTrue(tuleapProject.hasResource("trackers"));
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
		assertEquals(1, tuleapProject.getProjectResources().length);
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
