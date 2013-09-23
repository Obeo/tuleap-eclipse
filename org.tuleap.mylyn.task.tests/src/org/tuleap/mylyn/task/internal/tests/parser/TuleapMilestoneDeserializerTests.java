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
import java.util.Enumeration;

import org.eclipse.core.runtime.Platform;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.parser.TuleapMilestoneDeserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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
	 */
	@Test
	public void testRelease200Parsing() {
		TuleapMilestone tuleapMilestone = this.parse(release200);
		assertNotNull(tuleapMilestone);

		assertEquals(200, tuleapMilestone.getId());
		assertEquals("Release 0.9", tuleapMilestone.getLabel()); //$NON-NLS-1$
		assertEquals(12345678, tuleapMilestone.getStartDate());
		assertEquals(50, tuleapMilestone.getDuration(), 0);
		assertEquals(100, tuleapMilestone.getCapacity(), 0);
		assertEquals("/milestones/200", tuleapMilestone.getUrl()); //$NON-NLS-1$
		assertEquals("/milestones?id=200&group_id=3", tuleapMilestone.getHtmlUrl()); //$NON-NLS-1$
		assertEquals(901, tuleapMilestone.getTypeId());

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
		fail("Fix the test ");

	}

	/**
	 * Test the parsing of the data set of the release 300.
	 */
	@Test
	public void testRelease201Parsing() {
		TuleapMilestone tuleapMilestone = this.parse(release201);
		assertNotNull(tuleapMilestone);

		assertEquals(201, tuleapMilestone.getId());
		//		assertEquals("Release TU", tuleapMilestone.getLabel()); //$NON-NLS-1$
		// assertEquals(12345678, tuleapMilestone.getStartDate());
		// assertEquals(50, tuleapMilestone.getDuration(), 0);
		// assertEquals(100, tuleapMilestone.getCapacity(), 0);
		//		assertEquals("/milestones/201", tuleapMilestone.getUrl()); //$NON-NLS-1$
		//		assertEquals("/milestones?id=201&group_id=3", tuleapMilestone.getHtmlUrl()); //$NON-NLS-1$
		// assertEquals(901, tuleapMilestone.getTypeId());
		//
		// assertNotNull(tuleapMilestone.getValues());
		//
		// Object value = tuleapMilestone.getValue(Integer.valueOf(955));
		// assertNotNull(value);
		//		assertEquals("Bonjour,\nC'est un test unitaire.", value); //$NON-NLS-1$
		//
		// value = tuleapMilestone.getValue(Integer.valueOf(956));
		// assertNotNull(value);
		//		assertEquals("je suis une valeur calculee", value); //$NON-NLS-1$
		//
		// value = tuleapMilestone.getValue(Integer.valueOf(957));
		// assertNotNull(value);
		// assertEquals(9610, ((Number)value).intValue());
		//
		// value = tuleapMilestone.getValue(Integer.valueOf(958));
		// assertNotNull(value);
		// assertTrue(value instanceof int[]);
		// assertEquals(0, ((int[])value)[0]);
		// assertEquals(1, ((int[])value)[1]);
		//
		// value = tuleapMilestone.getValue(Integer.valueOf(959));
		// assertNotNull(value);
		// assertTrue(value instanceof int[]);
		// assertEquals(0, ((int[])value)[0]);
		// assertEquals(2, ((int[])value)[1]);
		//
		// value = tuleapMilestone.getValue(Integer.valueOf(960));
		// assertNotNull(value);
		// assertEquals(20120101, ((Number)value).longValue());
		//
		// value = tuleapMilestone.getValue(Integer.valueOf(961));
		// assertNotNull(value);
		//		assertEquals("first, second, third", value); //$NON-NLS-1$
		fail("Fix the test ");

		// TODO
		// value = tuleapMilestone.getValue(Integer.valueOf(962));
	}

}
