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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapMultiSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapString;
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
public class TuleapTrackerConfigurationDeserializerTests {
	/**
	 * The identifier of the server data bundle.
	 */
	private static final String SERVER_DATA_BUNDLE_ID = "org.tuleap.mylyn.task.server.data"; //$NON-NLS-1$

	/**
	 * The prefix of all the individual tracker files.
	 */
	private static final String TRACKER_FILE_PREFIX = "tracker-"; //$NON-NLS-1$

	/**
	 * The extension of the project files.
	 */
	private static final String JSON_EXTENSION = ".json"; //$NON-NLS-1$

	/**
	 * The name of the first trackers group file.
	 */
	private static final String TRACKERS_FILE_NAME_PART1 = "trackers_part_1.json"; //$NON-NLS-1$

	/**
	 * The name of the second trackers group file.
	 */
	private static final String TRACKERS_FILE_NAME_PART2 = "trackers_part_2.json"; //$NON-NLS-1$

	/**
	 * The content of the first tracker json file.
	 */
	private static String tracker0;

	/**
	 * The content of the second tracker json file.
	 */
	private static String tracker1;

	/**
	 * The content of the third tracker json file.
	 */
	private static String tracker2;

	/**
	 * The content of the fourth tracker json file.
	 */
	private static String tracker3;

	/**
	 * The content of the fifth tracker json file.
	 */
	private static String tracker4;

	/**
	 * The content of the sixth tracker json file.
	 */
	private static String tracker5;

	/**
	 * The content of the array of the first trackers group json file.
	 */
	private static String firstTrackersGroup;

	/**
	 * The content of the array of the second trackers group json file.
	 */
	private static String secondTrackersGroup;

	/**
	 * Reads the content of the file at the given url and returns it.
	 * 
	 * @param url
	 *            The url of the file in the bundle.
	 * @return The content of the file
	 */
	private static String readFileFromURL(URL url) {
		String result = ""; //$NON-NLS-1$

		InputStream openStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			openStream = url.openStream();
		} catch (IOException e) {
			// do nothing, openStream is null
		}

		if (openStream != null) {
			inputStreamReader = new InputStreamReader(openStream);
			bufferedReader = new BufferedReader(inputStreamReader);

			StringBuilder stringBuilder = new StringBuilder();

			String line = null;
			try {
				while ((line = bufferedReader.readLine()) != null) {
					stringBuilder.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						openStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			result = stringBuilder.toString();
		}

		return result;
	}

	/**
	 * Loads the json files from the server data tracker into the appropriate variables.
	 */
	@BeforeClass
	public static void staticSetUp() {
		Bundle serverDataBundle = Platform.getBundle(SERVER_DATA_BUNDLE_ID);
		if (serverDataBundle == null) {
			return;
		}
		Enumeration<URL> entries = serverDataBundle.findEntries("/json/trackers", "*.json", true); //$NON-NLS-1$//$NON-NLS-2$
		while (entries.hasMoreElements()) {
			URL url = entries.nextElement();
			String content = readFileFromURL(url);

			if (url.getPath().endsWith(TRACKERS_FILE_NAME_PART1)) {
				firstTrackersGroup = content;
			} else if (url.getPath().endsWith(TRACKERS_FILE_NAME_PART2)) {
				secondTrackersGroup = content;
			} else if (url.getPath().endsWith(TRACKER_FILE_PREFIX + 0 + JSON_EXTENSION)) {
				tracker0 = content;
			} else if (url.getPath().endsWith(TRACKER_FILE_PREFIX + 1 + JSON_EXTENSION)) {
				tracker1 = content;
			} else if (url.getPath().endsWith(TRACKER_FILE_PREFIX + 2 + JSON_EXTENSION)) {
				tracker2 = content;
			} else if (url.getPath().endsWith(TRACKER_FILE_PREFIX + 3 + JSON_EXTENSION)) {
				tracker3 = content;
			} else if (url.getPath().endsWith(TRACKER_FILE_PREFIX + 4 + JSON_EXTENSION)) {
				tracker4 = content;
			} else if (url.getPath().endsWith(TRACKER_FILE_PREFIX + 5 + JSON_EXTENSION)) {
				tracker5 = content;
			}
		}
	}

	/**
	 * Parse the content of the file and return the matching configuration.
	 * 
	 * @param fileContent
	 *            The content of the file
	 * @return The Tuleap project configuration matching the content of the file
	 */
	private TuleapTrackerConfiguration parse(String fileContent) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TuleapTrackerConfiguration.class,
				new TuleapTrackerConfigurationDeserializer());

		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(fileContent).getAsJsonObject();

		Gson gson = gsonBuilder.create();
		TuleapTrackerConfiguration tuleapTrackerConfiguration = gson.fromJson(jsonObject,
				TuleapTrackerConfiguration.class);

		return tuleapTrackerConfiguration;
	}

	/**
	 * Test the parsing of the first file.
	 */
	@Test
	public void testTracker0Parsing() {
		System.out.println(tracker0);
		TuleapTrackerConfiguration tuleapTrackerConfiguration = this.parse(tracker0);
		assertNotNull(tuleapTrackerConfiguration);
		assertEquals(0, tuleapTrackerConfiguration.getTrackerId());
		assertEquals("Product", tuleapTrackerConfiguration.getName()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/0", tuleapTrackerConfiguration.getUrl()); //$NON-NLS-1$
		assertEquals("The description of the products tracker", tuleapTrackerConfiguration.getDescription()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tuleapTrackerConfiguration.getFields();
		assertEquals(1, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();

		AbstractTuleapField firstField = iterator.next();

		assertEquals(0, firstField.getIdentifier());
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, firstField.getMetadataType());
		assertEquals("title", firstField.getName()); //$NON-NLS-1$
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
		System.out.println(tracker1);
		TuleapTrackerConfiguration tuleapTrackerConfiguration = this.parse(tracker1);
		assertNotNull(tuleapTrackerConfiguration);
		assertEquals(1, tuleapTrackerConfiguration.getTrackerId());
		assertEquals("Bugs", tuleapTrackerConfiguration.getName()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/1", tuleapTrackerConfiguration.getUrl()); //$NON-NLS-1$
		assertEquals("The description of the bugs tracker", tuleapTrackerConfiguration.getDescription()); //$NON-NLS-1$

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
		assertEquals("difficulty", fourthField.getName()); //$NON-NLS-1$
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
		System.out.println(tracker2);
		TuleapTrackerConfiguration tuleapTrackerConfiguration = this.parse(tracker2);
		assertNotNull(tuleapTrackerConfiguration);
		assertEquals(2, tuleapTrackerConfiguration.getTrackerId());
		assertEquals("Release", tuleapTrackerConfiguration.getName()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/2", tuleapTrackerConfiguration.getUrl()); //$NON-NLS-1$
		assertEquals("The description of the releases tracker", tuleapTrackerConfiguration.getDescription()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tuleapTrackerConfiguration.getFields();
		assertEquals(5, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();
		iterator.next();
		iterator.next();
		AbstractTuleapField secondField = iterator.next();

		assertEquals(2, secondField.getIdentifier());
		assertEquals(TaskAttribute.TYPE_DATE, secondField.getMetadataType());
		assertEquals("end-date", secondField.getName()); //$NON-NLS-1$
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
		System.out.println(tracker3);
		TuleapTrackerConfiguration tuleapTrackerConfiguration = this.parse(tracker3);
		assertNotNull(tuleapTrackerConfiguration);
		assertEquals(3, tuleapTrackerConfiguration.getTrackerId());
		assertEquals("Sprint", tuleapTrackerConfiguration.getName()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/3", tuleapTrackerConfiguration.getUrl()); //$NON-NLS-1$
		assertEquals("The description of the sprints tracker", tuleapTrackerConfiguration.getDescription()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tuleapTrackerConfiguration.getFields();
		assertEquals(5, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();
		iterator.next();
		iterator.next();
		AbstractTuleapField thirdField = iterator.next();

		System.out.print(thirdField.getMetadataType());

		assertEquals(2, thirdField.getIdentifier());
		assertEquals(TaskAttribute.TYPE_SINGLE_SELECT, thirdField.getMetadataType());

		assertEquals("status", thirdField.getName()); //$NON-NLS-1$
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
		System.out.println(tracker4);
		TuleapTrackerConfiguration tuleapTrackerConfiguration = this.parse(tracker4);
		assertNotNull(tuleapTrackerConfiguration);
		assertEquals(4, tuleapTrackerConfiguration.getTrackerId());
		assertEquals("Tests", tuleapTrackerConfiguration.getName()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/4", tuleapTrackerConfiguration.getUrl()); //$NON-NLS-1$
		assertEquals("The description of the tests tracker", tuleapTrackerConfiguration.getDescription()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tuleapTrackerConfiguration.getFields();
		assertEquals(3, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();
		iterator.next();
		iterator.next();
		AbstractTuleapField thirdField = iterator.next();

		System.out.print(thirdField.getMetadataType());

		assertEquals(2, thirdField.getIdentifier());
		assertEquals(TaskAttribute.TYPE_INTEGER, thirdField.getMetadataType());
		assertEquals("initial-effort", thirdField.getName()); //$NON-NLS-1$
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
		System.out.println(tracker5);
		TuleapTrackerConfiguration tuleapTrackerConfiguration = this.parse(tracker5);
		assertNotNull(tuleapTrackerConfiguration);
		assertEquals(5, tuleapTrackerConfiguration.getTrackerId());
		assertEquals("User Stories", tuleapTrackerConfiguration.getName()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/5", tuleapTrackerConfiguration.getUrl()); //$NON-NLS-1$
		assertEquals(
				"The description of the user stories tracker", tuleapTrackerConfiguration.getDescription()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tuleapTrackerConfiguration.getFields();
		assertEquals(2, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();
		iterator.next();
		AbstractTuleapField secondField = iterator.next();

		System.out.print(secondField.getMetadataType());

		assertEquals(1, secondField.getIdentifier());
		assertEquals(TaskAttribute.TYPE_SINGLE_SELECT, secondField.getMetadataType());
		assertEquals("status", secondField.getName()); //$NON-NLS-1$
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
		System.out.println(firstTrackersGroup);

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TuleapTrackerConfiguration.class,
				new TuleapTrackerConfigurationDeserializer());

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

		assertEquals(0, firstProjectConfiguration.getTrackerId());
		assertEquals("Product", firstProjectConfiguration.getName()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/0", firstProjectConfiguration.getUrl()); //$NON-NLS-1$
		assertEquals("The description of the products tracker", firstProjectConfiguration.getDescription()); //$NON-NLS-1$

		TuleapTrackerConfiguration secondProjectConfiguration = iterator.next();

		assertEquals(1, secondProjectConfiguration.getTrackerId());
		assertEquals("Bugs", secondProjectConfiguration.getName()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/1", secondProjectConfiguration.getUrl()); //$NON-NLS-1$
		assertEquals("The description of the bugs tracker", secondProjectConfiguration.getDescription()); //$NON-NLS-1$

		TuleapTrackerConfiguration thirdProjectConfiguration = iterator.next();

		assertEquals(2, thirdProjectConfiguration.getTrackerId());
		assertEquals("Release", thirdProjectConfiguration.getName()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/2", thirdProjectConfiguration.getUrl()); //$NON-NLS-1$
		assertEquals("The description of the releases tracker", thirdProjectConfiguration.getDescription()); //$NON-NLS-1$

		TuleapTrackerConfiguration fourthProjectConfiguration = iterator.next();

		assertEquals(3, fourthProjectConfiguration.getTrackerId());
		assertEquals("Sprint", fourthProjectConfiguration.getName()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/3", fourthProjectConfiguration.getUrl()); //$NON-NLS-1$
		assertEquals("The description of the sprints tracker", fourthProjectConfiguration.getDescription()); //$NON-NLS-1$

		TuleapTrackerConfiguration fifthProjectConfiguration = iterator.next();

		assertEquals(4, fifthProjectConfiguration.getTrackerId());
		assertEquals("Tests", fifthProjectConfiguration.getName()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/4", fifthProjectConfiguration.getUrl()); //$NON-NLS-1$
		assertEquals("The description of the tests tracker", fifthProjectConfiguration.getDescription()); //$NON-NLS-1$

	}

	/**
	 * Test the parsing of the first trackers group file.
	 */
	@Test
	public void testSecondTrackersGroupParsing() {
		System.out.println(secondTrackersGroup);

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TuleapTrackerConfiguration.class,
				new TuleapTrackerConfigurationDeserializer());

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

		assertEquals(5, sixthProjectConfiguration.getTrackerId());
		assertEquals("User Stories", sixthProjectConfiguration.getName()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/trackers/5", sixthProjectConfiguration.getUrl()); //$NON-NLS-1$
		assertEquals(
				"The description of the user stories tracker", sixthProjectConfiguration.getDescription()); //$NON-NLS-1$
	}
}
