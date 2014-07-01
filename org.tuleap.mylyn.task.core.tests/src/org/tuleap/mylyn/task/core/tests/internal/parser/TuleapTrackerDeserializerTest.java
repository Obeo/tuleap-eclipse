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
package org.tuleap.mylyn.task.core.tests.internal.parser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.core.internal.model.config.AbstractTuleapField;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapResource;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapTracker;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapWorkflow;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapMultiSelectBox;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapSelectBox;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapString;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapReference;
import org.tuleap.mylyn.task.core.internal.parser.TuleapGsonProvider;
import org.tuleap.mylyn.task.core.internal.util.ITuleapConstants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests the JSON deserialization of {@link TuleapTracker}.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapTrackerDeserializerTest {

	private Gson gson;

	@Before
	public void setUp() {
		gson = TuleapGsonProvider.defaultGson();
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
		assertEquals("title", firstField.getName());
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, firstField.getMetadataType());

		assertEquals("Title", firstField.getLabel()); //$NON-NLS-1$

		assertTrue(firstField.isReadable());
		assertFalse(firstField.isSubmitable());
		assertFalse(firstField.isUpdatable());

		assertEquals(1, tuleapTracker.getTrackerResources().length);

		TuleapResource tuleapResource = tuleapTracker.getTrackerResources()[0];
		assertEquals("reports", tuleapResource.getType()); //$NON-NLS-1$
		assertEquals("trackers/0/tracker_reports", tuleapResource.getUri()); //$NON-NLS-1$

		TuleapReference parent = tuleapTracker.getParentTracker();
		assertEquals(82, parent.getId());
		assertEquals("trackers/82", parent.getUri()); //$NON-NLS-1$

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
		assertEquals(14, fields.size());

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

		iterator.next();

		// Radio button
		AbstractTuleapField fieldRadio = iterator.next();
		assertEquals("Radio", fieldRadio.getLabel());
		assertTrue(fieldRadio instanceof TuleapSelectBox);
		TuleapSelectBox sbRadio = (TuleapSelectBox)fieldRadio;
		assertEquals(2, sbRadio.getItems().size());
		assertEquals("Value A", sbRadio.getItem("0").getLabel());
		assertEquals("Value B", sbRadio.getItem("1").getLabel());

		assertEquals(1, tuleapTracker.getTrackerResources().length);

		TuleapResource tuleapResource = tuleapTracker.getTrackerResources()[0];
		assertEquals("reports", tuleapResource.getType()); //$NON-NLS-1$
		assertEquals("trackers/1/tracker_reports", tuleapResource.getUri()); //$NON-NLS-1$

		TuleapReference parent = tuleapTracker.getParentTracker();
		assertNull(parent);
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
	 * Test the parsing of a tracker with a field values that is <code>null</code>.
	 */
	@Test
	public void testTrackerFieldValuesNullParsing() {
		String json = ParserUtil.loadFile("/trackers/tracker-6.json");
		TuleapTracker tracker = gson.fromJson(json, TuleapTracker.class);

		assertNotNull(tracker);
		assertEquals(6, tracker.getIdentifier());
		assertEquals("Product", tracker.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/plugins/tracker/?tracker=6", tracker.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/6", tracker.getUri()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tracker.getFields();
		assertEquals(1, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();

		// testing the semantic title
		AbstractTuleapField firstField = iterator.next();
		TuleapString stringField = (TuleapString)firstField;
		assertTrue(firstField instanceof TuleapString);
		assertTrue(stringField.isSemanticTitle());

		// testing the field permissions
		assertTrue(firstField.isReadable());
		assertFalse(firstField.isSubmitable());
		assertFalse(firstField.isUpdatable());

		assertEquals(1, tracker.getTrackerResources().length);

		TuleapResource tuleapResource = tracker.getTrackerResources()[0];
		assertEquals("reports", tuleapResource.getType()); //$NON-NLS-1$
		assertEquals("trackers/6/tracker_reports", tuleapResource.getUri()); //$NON-NLS-1$

	}

	/**
	 * Test the parsing of a tracker with a binding type that is <code>null</code>.
	 */
	@Test
	public void testTrackerBindingTypeNullParsing() {
		String json = ParserUtil.loadFile("/trackers/tracker-7.json");
		TuleapTracker tracker = gson.fromJson(json, TuleapTracker.class);

		assertNotNull(tracker);
		assertEquals(7, tracker.getIdentifier());
		assertEquals("Product", tracker.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/plugins/tracker/?tracker=7", tracker.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/7", tracker.getUri()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tracker.getFields();
		assertEquals(1, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();

		// testing the semantic title
		AbstractTuleapField firstField = iterator.next();
		TuleapString stringField = (TuleapString)firstField;
		assertTrue(firstField instanceof TuleapString);
		assertTrue(stringField.isSemanticTitle());

		// testing the field permissions
		assertTrue(firstField.isReadable());
		assertFalse(firstField.isSubmitable());
		assertFalse(firstField.isUpdatable());

		assertEquals(1, tracker.getTrackerResources().length);

		TuleapResource tuleapResource = tracker.getTrackerResources()[0];
		assertEquals("reports", tuleapResource.getType()); //$NON-NLS-1$
		assertEquals("trackers/7/tracker_reports", tuleapResource.getUri()); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of a tracker with a status that is <code>null</code>.
	 */
	@Test
	public void testTrackerStatusNullParsing() {
		String json = ParserUtil.loadFile("/trackers/tracker-8.json");
		TuleapTracker tracker = gson.fromJson(json, TuleapTracker.class);

		assertNotNull(tracker);
		assertEquals(8, tracker.getIdentifier());
		assertEquals("Product", tracker.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/plugins/tracker/?tracker=8", tracker.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/8", tracker.getUri()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tracker.getFields();
		assertEquals(1, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();

		// testing the semantic title
		AbstractTuleapField firstField = iterator.next();
		TuleapString stringField = (TuleapString)firstField;
		assertTrue(firstField instanceof TuleapString);
		assertTrue(stringField.isSemanticTitle());

		// testing the field permissions
		assertTrue(firstField.isReadable());
		assertFalse(firstField.isSubmitable());
		assertFalse(firstField.isUpdatable());

		assertEquals(1, tracker.getTrackerResources().length);

		TuleapResource tuleapResource = tracker.getTrackerResources()[0];
		assertEquals("reports", tuleapResource.getType()); //$NON-NLS-1$
		assertEquals("trackers/8/tracker_reports", tuleapResource.getUri()); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of a tracker with a transition that is <code>null</code>.
	 */
	@Test
	public void testTrackerTransitionFromIdNullParsing() {
		String json = ParserUtil.loadFile("/trackers/tracker-9.json");
		TuleapTracker tracker = gson.fromJson(json, TuleapTracker.class);

		assertNotNull(tracker);
		assertEquals(9, tracker.getIdentifier());
		assertEquals("Bugs", tracker.getLabel()); //$NON-NLS-1$
		assertEquals("localhost: 3001/plugins/tracker/?tracker=9", tracker.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/9", tracker.getUri()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tracker.getFields();
		assertEquals(1, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();

		// testing the semantic title
		AbstractTuleapField firstField = iterator.next();

		// testing the field id
		assertEquals(0, firstField.getIdentifier());

		// testing the field permissions
		assertTrue(firstField.isReadable());
		assertTrue(firstField.isSubmitable());
		assertTrue(firstField.isUpdatable());

		assertTrue(firstField instanceof TuleapSelectBox);
		TuleapSelectBox selectBoxField = (TuleapSelectBox)firstField;

		Collection<TuleapSelectBoxItem> items = selectBoxField.getItems();
		assertEquals(3, items.size());

		TuleapWorkflow workflow = selectBoxField.getWorkflow();
		assertNotNull(workflow);
		assertTrue(workflow.hasTransitions());
		Collection<TuleapSelectBoxItem> accessibleStates = workflow
				.accessibleStates(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID);
		assertEquals(1, accessibleStates.size());
		assertEquals(334, accessibleStates.iterator().next().getIdentifier());
		accessibleStates = workflow.accessibleStates(334);
		assertEquals(2, accessibleStates.size());
		assertTrue(accessibleStates.containsAll(Arrays.asList(selectBoxField.getItem("337"), selectBoxField
				.getItem("338"))));
		accessibleStates = workflow.accessibleStates(337);
		assertEquals(1, accessibleStates.size());
		assertEquals(338, accessibleStates.iterator().next().getIdentifier());
		accessibleStates = workflow.accessibleStates(338);
		assertTrue(accessibleStates.isEmpty());

		assertEquals(1, tracker.getTrackerResources().length);

		TuleapResource tuleapResource = tracker.getTrackerResources()[0];
		assertEquals("reports", tuleapResource.getType()); //$NON-NLS-1$
		assertEquals("trackers/9/tracker_reports", tuleapResource.getUri()); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of a tracker with a workflow that is <code>null</code>.
	 */
	@Test
	public void testTrackerWorkflowNullParsing() {
		String json = ParserUtil.loadFile("/trackers/tracker-10.json");
		TuleapTracker tracker = gson.fromJson(json, TuleapTracker.class);

		assertNotNull(tracker);
		assertEquals(10, tracker.getIdentifier());
		assertEquals("Bugs", tracker.getLabel()); //$NON-NLS-1$
		assertEquals("localhost: 3001/plugins/tracker/?tracker=10", tracker.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/10", tracker.getUri()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tracker.getFields();
		assertEquals(1, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();

		// testing the semantic title
		AbstractTuleapField firstField = iterator.next();
		TuleapString stringField = (TuleapString)firstField;
		assertTrue(firstField instanceof TuleapString);
		assertTrue(stringField.isSemanticTitle());

		// testing the field permissions
		assertTrue(firstField.isReadable());
		assertTrue(firstField.isSubmitable());
		assertTrue(firstField.isUpdatable());

		assertEquals(1, tracker.getTrackerResources().length);

		TuleapResource tuleapResource = tracker.getTrackerResources()[0];
		assertEquals("reports", tuleapResource.getType()); //$NON-NLS-1$
		assertEquals("trackers/10/tracker_reports", tuleapResource.getUri()); //$NON-NLS-1$
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
