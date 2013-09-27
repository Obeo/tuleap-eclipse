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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.tuleap.mylyn.task.internal.core.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.parser.TuleapMilestoneDeserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

//CHECKSTYLE:OFF

/**
 * Tests the deserialization of the Tuleap milestone.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class TuleapMilestoneDeserializerTests {
	/**
	 * The identifier of the server data bundle.
	 */
	private static final String SERVER_DATA_BUNDLE_ID = "org.tuleap.mylyn.task.server.data"; //$NON-NLS-1$

	/**
	 * The extension of the project files.
	 */
	private static final String JSON_EXTENSION = ".json"; //$NON-NLS-1$

	/**
	 * Data set release milestone 200 (containing 3 sprint milestones).
	 */
	private static final String MILESTONES_DATA_200 = "release200"; //$NON-NLS-1$

	/**
	 * Data set release milestone 201.
	 */
	private static final String MILESTONES_DATA_201 = "release201"; //$NON-NLS-1$

	/**
	 * The content of the release 200 json file.
	 */
	private static String release200;

	/**
	 * The content of the release 201 json file.
	 */
	private static String release201;

	/**
	 * The pattern used to format date following the ISO8601 standard.
	 */
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS"); //$NON-NLS-1$

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
	 * Loads the json files from the server data milestone into the appropriate variables.
	 */
	@BeforeClass
	public static void staticSetUp() {
		Bundle serverDataBundle = Platform.getBundle(SERVER_DATA_BUNDLE_ID);
		if (serverDataBundle == null) {
			return;
		}
		Enumeration<URL> entries = serverDataBundle.findEntries("/json/milestones", "*.json", true); //$NON-NLS-1$//$NON-NLS-2$
		while (entries.hasMoreElements()) {
			URL url = entries.nextElement();
			String content = readFileFromURL(url);

			if (url.getPath().endsWith(MILESTONES_DATA_200 + JSON_EXTENSION)) {
				release200 = content;
			} else if (url.getPath().endsWith(MILESTONES_DATA_201 + JSON_EXTENSION)) {
				release201 = content;
			}
		}
	}

	/**
	 * Parse the content of the file and return the matching milestone.
	 * 
	 * @param fileContent
	 *            The content of the file
	 * @return The Tuleap project configuration matching the content of the file
	 */
	private TuleapMilestone parse(String fileContent) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TuleapMilestone.class, new TuleapMilestoneDeserializer());

		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(fileContent).getAsJsonObject();

		Gson gson = gsonBuilder.create();
		TuleapMilestone tuleapMilestone = gson.fromJson(jsonObject, TuleapMilestone.class);

		return tuleapMilestone;
	}

	/**
	 * Test the parsing of the data set of the release 200.
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testRelease200Parsing() throws ParseException {
		TuleapMilestone tuleapMilestone = this.parse(release200);
		assertNotNull(tuleapMilestone);

		assertEquals(200, tuleapMilestone.getId());
		assertEquals("Release 0.9", tuleapMilestone.getLabel()); //$NON-NLS-1$
		assertEquals(dateFormat.parse("2013-09-23T11:44:18.963Z"), tuleapMilestone.getStartDate());
		assertEquals(50, tuleapMilestone.getDuration(), 0);
		assertEquals(100, tuleapMilestone.getCapacity(), 0);
		assertEquals("/milestones/200", tuleapMilestone.getUrl()); //$NON-NLS-1$
		assertEquals("/milestones?id=200&group_id=3", tuleapMilestone.getHtmlUrl()); //$NON-NLS-1$
		assertEquals(901, tuleapMilestone.getConfigurationId());

		// assertNotNull(tuleapMilestone.getValues());
		//
		// Object value = tuleapMilestone.getValue(Integer.valueOf(950));
		// assertNotNull(value);
		//		assertEquals("Release 0.9", value); //$NON-NLS-1$
		//
		// value = tuleapMilestone.getValue(Integer.valueOf(951));
		// assertNotNull(value);
		// assertEquals(Integer.valueOf(9510), value);
		//
		// value = tuleapMilestone.getValue(Integer.valueOf(952));
		// assertNotNull(value);
		// assertEquals(35f, ((Number)value).floatValue(), 0f);
		//
		// value = tuleapMilestone.getValue(Integer.valueOf(953));
		// assertNotNull(value);
		//		assertEquals("0.9", value); //$NON-NLS-1$
		//
		// value = tuleapMilestone.getValue(Integer.valueOf(954));
		// assertNotNull(value);
		//		assertEquals("300, 301, 302", value); //$NON-NLS-1$
		//
		// List<TuleapMilestone> milestones = tuleapMilestone.getSubMilestones();
		// assertNotNull(milestones);
		// assertEquals(3, milestones.size());
		//
		// // First sub milestone
		// TuleapMilestone subMilestone = milestones.get(0);
		// assertEquals(250, subMilestone.getId());
		//		assertEquals("Sprint 1", subMilestone.getLabel()); //$NON-NLS-1$
		// assertEquals(12345678, subMilestone.getStartDate());
		// assertEquals(15, subMilestone.getDuration(), 0);
		// assertEquals(24.5, subMilestone.getCapacity(), 0);
		//		assertEquals("/milestones/300", subMilestone.getUrl()); //$NON-NLS-1$
		//		assertEquals("/milestones?id=300&group_id=3", subMilestone.getHtmlUrl()); //$NON-NLS-1$
		// assertEquals(902, subMilestone.getTypeId());
		//
		// assertNotNull(subMilestone.getValues());
		//
		// value = subMilestone.getValue(Integer.valueOf(960));
		// assertNotNull(value);
		//		assertEquals("Sprint 1", value); //$NON-NLS-1$
		//
		// value = subMilestone.getValue(Integer.valueOf(961));
		// assertNotNull(value);
		// assertEquals(Integer.valueOf(9612), value);
		//
		// value = subMilestone.getValue(Integer.valueOf(962));
		// assertNotNull(value);
		//		assertEquals("400, 401, 402", value); //$NON-NLS-1$
		//
		// value = subMilestone.getValue(Integer.valueOf(963));
		// assertNotNull(value);
		// assertEquals(12345678, ((Number)value).longValue());
		//
		// value = subMilestone.getValue(Integer.valueOf(964));
		// assertNotNull(value);
		// assertEquals(15, ((Number)value).floatValue(), 0);
		//
		// value = subMilestone.getValue(Integer.valueOf(965));
		// assertNotNull(value);
		// assertEquals(24.5, ((Number)value).floatValue(), 0);
		//
		// value = subMilestone.getValue(Integer.valueOf(966));
		// assertNotNull(value);
		// assertEquals(0, ((Number)value).floatValue(), 0);
		//
		// // Second sub milestone
		// subMilestone = milestones.get(1);
		// assertEquals(251, subMilestone.getId());
		//		assertEquals("Sprint 2", subMilestone.getLabel()); //$NON-NLS-1$
		// assertEquals(12355678, subMilestone.getStartDate());
		// assertEquals(21, subMilestone.getDuration(), 0);
		// assertEquals(28.5, subMilestone.getCapacity(), 0);
		//		assertEquals("/milestones/301", subMilestone.getUrl()); //$NON-NLS-1$
		//		assertEquals("/milestones?id=301&group_id=3", subMilestone.getHtmlUrl()); //$NON-NLS-1$
		// assertEquals(902, subMilestone.getTypeId());
		//
		// assertNotNull(subMilestone.getValues());
		//
		// value = subMilestone.getValue(Integer.valueOf(960));
		// assertNotNull(value);
		//		assertEquals("Sprint 2", value); //$NON-NLS-1$
		//
		// value = subMilestone.getValue(Integer.valueOf(961));
		// assertNotNull(value);
		// assertEquals(Integer.valueOf(9611), value);
		//
		// value = subMilestone.getValue(Integer.valueOf(962));
		// assertNotNull(value);
		//		assertEquals("410, 411, 412", value); //$NON-NLS-1$
		//
		// value = subMilestone.getValue(Integer.valueOf(963));
		// assertNotNull(value);
		// assertEquals(12355678, ((Number)value).longValue());
		//
		// value = subMilestone.getValue(Integer.valueOf(964));
		// assertNotNull(value);
		// assertEquals(21, ((Number)value).floatValue(), 0);
		//
		// value = subMilestone.getValue(Integer.valueOf(965));
		// assertNotNull(value);
		// assertEquals(28.5, ((Number)value).floatValue(), 0);
		//
		// value = subMilestone.getValue(Integer.valueOf(966));
		// assertNotNull(value);
		// assertEquals(12.5, ((Number)value).floatValue(), 0);
		//
		// // Third sub milestone
		// subMilestone = milestones.get(2);
		// assertEquals(252, subMilestone.getId());
		//		assertEquals("Sprint 3", subMilestone.getLabel()); //$NON-NLS-1$
		// assertEquals(12365678, subMilestone.getStartDate());
		// assertEquals(20, subMilestone.getDuration(), 0);
		// assertEquals(26.5, subMilestone.getCapacity(), 0);
		//		assertEquals("/milestones/302", subMilestone.getUrl()); //$NON-NLS-1$
		//		assertEquals("/milestones?id=302&group_id=3", subMilestone.getHtmlUrl()); //$NON-NLS-1$
		// assertEquals(902, subMilestone.getTypeId());
		//
		// assertNotNull(subMilestone.getValues());
		//
		// value = subMilestone.getValue(Integer.valueOf(960));
		// assertNotNull(value);
		//		assertEquals("Sprint 2", value); //$NON-NLS-1$
		//
		// value = subMilestone.getValue(Integer.valueOf(961));
		// assertNotNull(value);
		// assertEquals(Integer.valueOf(9610), value);
		//
		// value = subMilestone.getValue(Integer.valueOf(962));
		// assertNotNull(value);
		//		assertEquals("420, 421, 422", value); //$NON-NLS-1$
		//
		// value = subMilestone.getValue(Integer.valueOf(963));
		// assertNotNull(value);
		// assertEquals(12365678, ((Number)value).longValue());
		//
		// value = subMilestone.getValue(Integer.valueOf(964));
		// assertNotNull(value);
		// assertEquals(20, ((Number)value).floatValue(), 0);
		//
		// value = subMilestone.getValue(Integer.valueOf(965));
		// assertNotNull(value);
		// assertEquals(26.5, ((Number)value).floatValue(), 0);
		//
		// value = subMilestone.getValue(Integer.valueOf(966));
		// assertNotNull(value);
		// assertEquals(26.5, ((Number)value).floatValue(), 0);

	}

	/**
	 * Test the parsing of the data set of the release 300.
	 */
	@SuppressWarnings("restriction")
	@Test
	public void testRelease201Parsing() throws Exception {
		TuleapMilestone tuleapMilestone = this.parse(release201);
		assertNotNull(tuleapMilestone);

		assertEquals(201, tuleapMilestone.getId());
		assertEquals("Release TU", tuleapMilestone.getLabel()); //$NON-NLS-1$
		assertEquals(dateFormat.parse("20130923114418963"), tuleapMilestone.getStartDate());
		assertEquals(50, tuleapMilestone.getDuration(), 0);
		assertEquals(100, tuleapMilestone.getCapacity(), 0);
		assertEquals("/milestones/201", tuleapMilestone.getUrl()); //$NON-NLS-1$
		assertEquals("/milestones?id=201&group_id=3", tuleapMilestone.getHtmlUrl()); //$NON-NLS-1$
		assertEquals(901, tuleapMilestone.getConfigurationId());

		assertNotNull(tuleapMilestone.getFieldValues());

		AbstractFieldValue value = tuleapMilestone.getFieldValue(955);
		assertTrue(value instanceof LiteralFieldValue);
		String fieldValue = ((LiteralFieldValue)value).getFieldValue();
		assertEquals("Bonjour,\nC'est un test unitaire.", fieldValue); //$NON-NLS-1$

		value = tuleapMilestone.getFieldValue(956);
		assertTrue(value instanceof LiteralFieldValue);
		fieldValue = ((LiteralFieldValue)value).getFieldValue();
		assertEquals("je suis une valeur calculee", fieldValue); //$NON-NLS-1$

		value = tuleapMilestone.getFieldValue(957);
		assertTrue(value instanceof BoundFieldValue);
		List<Integer> valueIds = ((BoundFieldValue)value).getValueIds();
		assertEquals(1, valueIds.size());
		assertEquals(9610, valueIds.get(0).intValue());

		value = tuleapMilestone.getFieldValue(958);
		assertTrue(value instanceof BoundFieldValue);
		valueIds = ((BoundFieldValue)value).getValueIds();
		assertEquals(2, valueIds.size());
		assertEquals(0, valueIds.get(0).intValue());
		assertEquals(1, valueIds.get(1).intValue());

		value = tuleapMilestone.getFieldValue(959);
		assertTrue(value instanceof BoundFieldValue);
		valueIds = ((BoundFieldValue)value).getValueIds();
		assertEquals(0, valueIds.get(0).intValue());
		assertEquals(2, valueIds.get(1).intValue());

		value = tuleapMilestone.getFieldValue(960);
		assertTrue(value instanceof LiteralFieldValue);
		fieldValue = ((LiteralFieldValue)value).getFieldValue();
		assertEquals("20120101", fieldValue); //$NON-NLS-1$

		value = tuleapMilestone.getFieldValue(961);
		assertTrue(value instanceof LiteralFieldValue);
		fieldValue = ((LiteralFieldValue)value).getFieldValue();
		assertEquals("first, second, third", fieldValue); //$NON-NLS-1$

		List<TuleapBacklogItem> backlogItems = tuleapMilestone.getBacklogItems();
		assertEquals(2, backlogItems.size());

		// First backlog item
		TuleapBacklogItem bi = backlogItems.get(0);
		assertEquals(300, bi.getId());
		assertEquals("An important Epic", bi.getLabel()); //$NON-NLS-1$
		assertEquals(30F, bi.getInitialEffort(), 0F);
		assertEquals("/backlog_items/300", bi.getUrl()); //$NON-NLS-1$
		assertEquals("/backlog_items?id=300&group_id=3", bi.getHtmlUrl()); //$NON-NLS-1$
		assertEquals(801, bi.getConfigurationId());

		assertEquals(Integer.valueOf(200), bi.getAssignedMilestoneId());
		Collection<AbstractFieldValue> fieldValues = bi.getFieldValues();
		assertEquals(4, fieldValues.size());
		Iterator<AbstractFieldValue> it = fieldValues.iterator();

		value = it.next();
		assertEquals(850, value.getFieldId());
		assertTrue(value instanceof BoundFieldValue);
		BoundFieldValue boundValue = (BoundFieldValue)value;
		valueIds = boundValue.getValueIds();
		assertEquals(1, valueIds.size());
		assertEquals(8502, valueIds.get(0).intValue());

		value = it.next();
		assertEquals(851, value.getFieldId());
		assertTrue(value instanceof LiteralFieldValue);
		assertEquals("0", ((LiteralFieldValue)value).getFieldValue()); //$NON-NLS-1$

		value = it.next();
		assertEquals(852, value.getFieldId());
		assertTrue(value instanceof LiteralFieldValue);
		assertEquals("The summary of an important Epic", ((LiteralFieldValue)value).getFieldValue()); //$NON-NLS-1$

		value = it.next();
		assertEquals(853, value.getFieldId());
		assertTrue(value instanceof LiteralFieldValue);
		assertEquals("350,351", ((LiteralFieldValue)value).getFieldValue()); //$NON-NLS-1$

		// second backlog item
		bi = backlogItems.get(1);
		assertEquals(301, bi.getId());
		assertEquals("Another important Epic", bi.getLabel()); //$NON-NLS-1$
		assertEquals(40.5F, bi.getInitialEffort(), 0F);
		assertEquals("/backlog_items/301", bi.getUrl()); //$NON-NLS-1$
		assertEquals("/backlog_items?id=301&group_id=3", bi.getHtmlUrl()); //$NON-NLS-1$
		assertEquals(801, bi.getConfigurationId());
		assertEquals(Integer.valueOf(201), bi.getAssignedMilestoneId());
	}

}
