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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapMultiSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.parser.TuleapTrackerConfigurationDeserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests the desiarialization of the Tuleap project configuration.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapTrackerConfigurationDeserializerTests extends AbstractConfigurationDeserializerTest<TuleapTrackerConfiguration> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected JsonDeserializer<TuleapTrackerConfiguration> getDeserializer() {
		return new TuleapTrackerConfigurationDeserializer(getProjectConfiguration());
	}

	/**
	 * Test the parsing of the first file.
	 */
	@Test
	public void testTracker0Parsing() {
		String tracker0 = ParserUtil.loadFile("/trackers/tracker-0.json");
		TuleapTrackerConfiguration tuleapTrackerConfiguration = this.parse(tracker0,
				TuleapTrackerConfiguration.class);
		assertNotNull(tuleapTrackerConfiguration);
		assertEquals(0, tuleapTrackerConfiguration.getIdentifier());
		assertEquals("Product", tuleapTrackerConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/0", tuleapTrackerConfiguration.getUrl()); //$NON-NLS-1$
		//		assertEquals("The description of the products tracker", tuleapTrackerConfiguration.getDescription()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tuleapTrackerConfiguration.getFields();
		assertEquals(1, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();

		AbstractTuleapField firstField = iterator.next();

		assertEquals(0, firstField.getIdentifier());
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, firstField.getMetadataType());
		// We don't keep the "short_name" information since it is useless with REST
		//		assertEquals("title", firstField.getName()); //$NON-NLS-1$
		assertEquals("Title", firstField.getLabel()); //$NON-NLS-1$

		assertTrue(firstField.isReadable());
		assertFalse(firstField.isSubmitable());
		assertFalse(firstField.isUpdatable());

	}

	/**
	 * Test the parsing of the second file.
	 */
	@Test
	public void testTracker1Parsing() {
		String tracker1 = ParserUtil.loadFile("/trackers/tracker-1.json");
		TuleapTrackerConfiguration tuleapTrackerConfiguration = this.parse(tracker1,
				TuleapTrackerConfiguration.class);
		assertNotNull(tuleapTrackerConfiguration);
		assertEquals(1, tuleapTrackerConfiguration.getIdentifier());
		assertEquals("Bugs", tuleapTrackerConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/1", tuleapTrackerConfiguration.getUrl()); //$NON-NLS-1$
		//		assertEquals("The description of the bugs tracker", tuleapTrackerConfiguration.getDescription()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tuleapTrackerConfiguration.getFields();
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
		AbstractTuleapField fourthField = iterator.next();

		// testing the field id
		assertEquals(4, fourthField.getIdentifier());

		// testing the field type
		assertEquals(TaskAttribute.TYPE_DOUBLE, fourthField.getMetadataType());
		// We don't keep the "short_name" information since it is useless with REST
		//		assertEquals("difficulty", fourthField.getName()); //$NON-NLS-1$
		assertEquals("Difficulty", fourthField.getLabel()); //$NON-NLS-1$

		// testing the field mermissions
		assertTrue(fourthField.isReadable());
		assertTrue(fourthField.isSubmitable());
		assertFalse(fourthField.isUpdatable());

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
		AbstractTuleapField tenthField = iterator.next();
		TuleapMultiSelectBox multiSelectBoxField = (TuleapMultiSelectBox)tenthField;
		assertTrue(tenthField instanceof TuleapMultiSelectBox);
		assertEquals("users", multiSelectBoxField.getBinding()); //$NON-NLS-1$

		// testing the semantic contributor
		assertTrue(multiSelectBoxField.isSemanticContributor());

		AbstractTuleapField eleventhField = iterator.next();
		assertTrue(eleventhField instanceof TuleapSelectBox);
		TuleapSelectBox theSelectBoxField = (TuleapSelectBox)eleventhField;

		// testing the semantic status
		assertTrue(theSelectBoxField.isSemanticStatus());

		// testing the open status
		assertEquals(3, theSelectBoxField.getOpenStatus().size());

		// testing the workflow
		// TODO Revoir
	}

	/**
	 * Test the parsing of the third file.
	 */
	@Test
	public void testTracker2Parsing() {
		String tracker2 = ParserUtil.loadFile("/trackers/tracker-2.json");
		TuleapTrackerConfiguration tuleapTrackerConfiguration = this.parse(tracker2,
				TuleapTrackerConfiguration.class);
		assertNotNull(tuleapTrackerConfiguration);
		assertEquals(2, tuleapTrackerConfiguration.getIdentifier());
		assertEquals("Release", tuleapTrackerConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/2", tuleapTrackerConfiguration.getUrl()); //$NON-NLS-1$
		//		assertEquals("The description of the releases tracker", tuleapTrackerConfiguration.getDescription()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tuleapTrackerConfiguration.getFields();
		assertEquals(5, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();
		iterator.next();
		iterator.next();
		AbstractTuleapField secondField = iterator.next();

		assertEquals(2, secondField.getIdentifier());
		assertEquals(TaskAttribute.TYPE_DATE, secondField.getMetadataType());
		// We don't keep the "short_name" information since it is useless with REST
		//		assertEquals("end-date", secondField.getName()); //$NON-NLS-1$
		assertEquals("End Date", secondField.getLabel()); //$NON-NLS-1$

		assertTrue(secondField.isReadable());
		assertTrue(secondField.isSubmitable());
		assertTrue(secondField.isUpdatable());

	}

	/**
	 * Test the parsing of the fourth file.
	 */
	@Test
	public void testTracker3Parsing() {
		String tracker3 = ParserUtil.loadFile("/trackers/tracker-3.json");
		TuleapTrackerConfiguration tuleapTrackerConfiguration = this.parse(tracker3,
				TuleapTrackerConfiguration.class);
		assertNotNull(tuleapTrackerConfiguration);
		assertEquals(3, tuleapTrackerConfiguration.getIdentifier());
		assertEquals("Sprint", tuleapTrackerConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/3", tuleapTrackerConfiguration.getUrl()); //$NON-NLS-1$
		//		assertEquals("The description of the sprints tracker", tuleapTrackerConfiguration.getDescription()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tuleapTrackerConfiguration.getFields();
		assertEquals(5, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();
		iterator.next();
		iterator.next();
		AbstractTuleapField thirdField = iterator.next();

		assertEquals(2, thirdField.getIdentifier());
		assertEquals(TaskAttribute.TYPE_SINGLE_SELECT, thirdField.getMetadataType());
		// We don't keep the "short_name" information since it is useless with REST
		//		assertEquals("status", thirdField.getName()); //$NON-NLS-1$
		assertEquals("Status", thirdField.getLabel()); //$NON-NLS-1$

		assertTrue(thirdField.isReadable());
		assertTrue(thirdField.isSubmitable());
		assertTrue(thirdField.isUpdatable());
	}

	/**
	 * Test the parsing of the fifth file.
	 */
	@Test
	public void testTracker4Parsing() {
		String tracker4 = ParserUtil.loadFile("/trackers/tracker-4.json");
		TuleapTrackerConfiguration tuleapTrackerConfiguration = this.parse(tracker4,
				TuleapTrackerConfiguration.class);
		assertNotNull(tuleapTrackerConfiguration);
		assertEquals(4, tuleapTrackerConfiguration.getIdentifier());
		assertEquals("Tests", tuleapTrackerConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/4", tuleapTrackerConfiguration.getUrl()); //$NON-NLS-1$
		// assertEquals("The description of the tests tracker", tuleapTrackerConfiguration.getDescription()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tuleapTrackerConfiguration.getFields();
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
	}

	/**
	 * Test the parsing of the sixth file.
	 */
	@Test
	public void testTracker5Parsing() {
		String tracker5 = ParserUtil.loadFile("/trackers/tracker-5.json");
		TuleapTrackerConfiguration tuleapTrackerConfiguration = this.parse(tracker5,
				TuleapTrackerConfiguration.class);
		assertNotNull(tuleapTrackerConfiguration);
		assertEquals(5, tuleapTrackerConfiguration.getIdentifier());
		assertEquals("User Stories", tuleapTrackerConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/5", tuleapTrackerConfiguration.getUrl()); //$NON-NLS-1$
		// assertEquals(
		//				"The description of the user stories tracker", tuleapTrackerConfiguration.getDescription()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tuleapTrackerConfiguration.getFields();
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
	}

	/**
	 * Test the parsing of the first trackers group file.
	 */
	@Test
	public void testFirstTrackersGroupParsing() {
		String firstTrackersGroup = ParserUtil.loadFile("/trackers/trackers_part_1.json");
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TuleapTrackerConfiguration.class, getDeserializer());

		JsonParser jsonParser = new JsonParser();
		JsonArray asJsonArray = jsonParser.parse(firstTrackersGroup).getAsJsonArray();
		Gson gson = gsonBuilder.create();

		List<TuleapTrackerConfiguration> tracksConfiguration = new ArrayList<TuleapTrackerConfiguration>();
		for (int i = 0; i < asJsonArray.size(); i++) {
			JsonElement jsonElement = asJsonArray.get(i);
			TuleapTrackerConfiguration tuleapTrackerConfiguration = gson.fromJson(jsonElement,
					TuleapTrackerConfiguration.class);
			tracksConfiguration.add(tuleapTrackerConfiguration);
		}

		Iterator<TuleapTrackerConfiguration> iterator = tracksConfiguration.iterator();

		TuleapTrackerConfiguration firstProjectConfiguration = iterator.next();

		assertEquals(0, firstProjectConfiguration.getIdentifier());
		assertEquals("Product", firstProjectConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/0", firstProjectConfiguration.getUrl()); //$NON-NLS-1$
		//		assertEquals("The description of the products tracker", firstProjectConfiguration.getDescription()); //$NON-NLS-1$

		TuleapTrackerConfiguration secondProjectConfiguration = iterator.next();

		assertEquals(1, secondProjectConfiguration.getIdentifier());
		assertEquals("Bugs", secondProjectConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/1", secondProjectConfiguration.getUrl()); //$NON-NLS-1$
		//		assertEquals("The description of the bugs tracker", secondProjectConfiguration.getDescription()); //$NON-NLS-1$

		TuleapTrackerConfiguration thirdProjectConfiguration = iterator.next();

		assertEquals(2, thirdProjectConfiguration.getIdentifier());
		assertEquals("Release", thirdProjectConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/2", thirdProjectConfiguration.getUrl()); //$NON-NLS-1$
		//		assertEquals("The description of the releases tracker", thirdProjectConfiguration.getDescription()); //$NON-NLS-1$

		TuleapTrackerConfiguration fourthProjectConfiguration = iterator.next();

		assertEquals(3, fourthProjectConfiguration.getIdentifier());
		assertEquals("Sprint", fourthProjectConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/3", fourthProjectConfiguration.getUrl()); //$NON-NLS-1$
		//		assertEquals("The description of the sprints tracker", fourthProjectConfiguration.getDescription()); //$NON-NLS-1$

		TuleapTrackerConfiguration fifthProjectConfiguration = iterator.next();

		assertEquals(4, fifthProjectConfiguration.getIdentifier());
		assertEquals("Tests", fifthProjectConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/4", fifthProjectConfiguration.getUrl()); //$NON-NLS-1$
		//		assertEquals("The description of the tests tracker", fifthProjectConfiguration.getDescription()); //$NON-NLS-1$

	}

	/**
	 * Test the parsing of the first trackers group file.
	 */
	@Test
	public void testSecondTrackersGroupParsing() {
		String secondTrackersGroup = ParserUtil.loadFile("/trackers/trackers_part_2.json");
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TuleapTrackerConfiguration.class, getDeserializer());

		JsonParser jsonParser = new JsonParser();
		JsonArray asJsonArray = jsonParser.parse(secondTrackersGroup).getAsJsonArray();
		Gson gson = gsonBuilder.create();

		List<TuleapTrackerConfiguration> tracksConfiguration = new ArrayList<TuleapTrackerConfiguration>();
		for (int i = 0; i < asJsonArray.size(); i++) {
			JsonElement jsonElement = asJsonArray.get(i);
			TuleapTrackerConfiguration tuleapTrackerConfiguration = gson.fromJson(jsonElement,
					TuleapTrackerConfiguration.class);
			tracksConfiguration.add(tuleapTrackerConfiguration);
		}

		Iterator<TuleapTrackerConfiguration> iterator = tracksConfiguration.iterator();

		TuleapTrackerConfiguration sixthProjectConfiguration = iterator.next();

		assertEquals(5, sixthProjectConfiguration.getIdentifier());
		assertEquals("User Stories", sixthProjectConfiguration.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/5", sixthProjectConfiguration.getUrl()); //$NON-NLS-1$
		// assertEquals(
		//				"The description of the user stories tracker", sixthProjectConfiguration.getDescription()); //$NON-NLS-1$
	}
}
