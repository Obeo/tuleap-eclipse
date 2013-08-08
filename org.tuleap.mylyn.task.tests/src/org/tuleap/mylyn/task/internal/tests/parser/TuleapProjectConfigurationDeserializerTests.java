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
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.parser.TuleapProjectConfigurationDeserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests the desiarialization of the Tuleap project configuration.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapProjectConfigurationDeserializerTests {
	/**
	 * The identifier of the server data bundle.
	 */
	private static final String SERVER_DATA_BUNDLE_ID = "org.tuleap.mylyn.task.server.data"; //$NON-NLS-1$

	/**
	 * The prefix of all the individual project files.
	 */
	private static final String PROJECT_FILE_PREFIX = "project-"; //$NON-NLS-1$

	/**
	 * The extension of the project files.
	 */
	private static final String JSON_EXTENSION = ".json"; //$NON-NLS-1$

	/**
	 * The name of the projects file.
	 */
	private static final String PROJECTS_FILE_NAME = "projects.json"; //$NON-NLS-1$

	/**
	 * The content of the first project json file.
	 */
	private static String project0;

	/**
	 * The content of the second project json file.
	 */
	private static String project1;

	/**
	 * The content of the third project json file.
	 */
	private static String project2;

	/**
	 * The content of the fourth project json file.
	 */
	private static String project3;

	/**
	 * The content of the fifth project json file.
	 */
	private static String project4;

	/**
	 * The content of the array of projects json file.
	 */
	private static String projects;

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
	 * Loads the json files from the server data project into the appropriate variables.
	 */
	@BeforeClass
	public static void staticSetUp() {
		Bundle serverDataBundle = Platform.getBundle(SERVER_DATA_BUNDLE_ID);
		if (serverDataBundle == null) {
			return;
		}
		Enumeration<URL> entries = serverDataBundle.findEntries("/json/projects", "*.json", true); //$NON-NLS-1$//$NON-NLS-2$
		while (entries.hasMoreElements()) {
			URL url = entries.nextElement();
			String content = readFileFromURL(url);

			if (url.getPath().endsWith(PROJECTS_FILE_NAME)) {
				projects = content;
			} else if (url.getPath().endsWith(PROJECT_FILE_PREFIX + 0 + JSON_EXTENSION)) {
				project0 = content;
			} else if (url.getPath().endsWith(PROJECT_FILE_PREFIX + 1 + JSON_EXTENSION)) {
				project1 = content;
			} else if (url.getPath().endsWith(PROJECT_FILE_PREFIX + 2 + JSON_EXTENSION)) {
				project2 = content;
			} else if (url.getPath().endsWith(PROJECT_FILE_PREFIX + 3 + JSON_EXTENSION)) {
				project3 = content;
			} else if (url.getPath().endsWith(PROJECT_FILE_PREFIX + 4 + JSON_EXTENSION)) {
				project4 = content;
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
	private TuleapProjectConfiguration parse(String fileContent) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TuleapProjectConfiguration.class,
				new TuleapProjectConfigurationDeserializer());

		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(fileContent).getAsJsonObject();

		Gson gson = gsonBuilder.create();
		TuleapProjectConfiguration tuleapProjectConfiguration = gson.fromJson(jsonObject,
				TuleapProjectConfiguration.class);

		return tuleapProjectConfiguration;
	}

	/**
	 * Test the parsing of the first file.
	 */
	@Test
	public void testProject0Parsing() {
		System.out.println(project0);
		TuleapProjectConfiguration tuleapProjectConfiguration = this.parse(project0);
		assertNotNull(tuleapProjectConfiguration);
		assertEquals(0, tuleapProjectConfiguration.getIdentifier());
		assertEquals("Project 0", tuleapProjectConfiguration.getName()); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the second file.
	 */
	@Test
	public void testProject1Parsing() {
		System.out.println(project1);
		TuleapProjectConfiguration tuleapProjectConfiguration = this.parse(project1);
		assertNotNull(tuleapProjectConfiguration);
		assertEquals(1, tuleapProjectConfiguration.getIdentifier());
		assertEquals("Project 1", tuleapProjectConfiguration.getName()); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the third file.
	 */
	@Test
	public void testProject2Parsing() {
		System.out.println(project2);
		TuleapProjectConfiguration tuleapProjectConfiguration = this.parse(project2);
		assertNotNull(tuleapProjectConfiguration);
		assertEquals(2, tuleapProjectConfiguration.getIdentifier());
		assertEquals("Project 2", tuleapProjectConfiguration.getName()); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the fourth file.
	 */
	@Test
	public void testProject3Parsing() {
		System.out.println(project3);
		TuleapProjectConfiguration tuleapProjectConfiguration = this.parse(project3);
		assertNotNull(tuleapProjectConfiguration);
		assertEquals(3, tuleapProjectConfiguration.getIdentifier());
		assertEquals("Project 3", tuleapProjectConfiguration.getName()); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the fifth file.
	 */
	@Test
	public void testProject4Parsing() {
		System.out.println(project4);
		TuleapProjectConfiguration tuleapProjectConfiguration = this.parse(project4);
		assertNotNull(tuleapProjectConfiguration);
		assertEquals(4, tuleapProjectConfiguration.getIdentifier());
		assertEquals("Project 4", tuleapProjectConfiguration.getName()); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the projects file.
	 */
	@Test
	public void testProjectsParsing() {
		System.out.println(projects);

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TuleapProjectConfiguration.class,
				new TuleapProjectConfigurationDeserializer());

		JsonParser jsonParser = new JsonParser();
		JsonArray asJsonArray = jsonParser.parse(projects).getAsJsonArray();
		Gson gson = gsonBuilder.create();

		List<TuleapProjectConfiguration> projectsConfiguration = new ArrayList<TuleapProjectConfiguration>();
		for (int i = 0; i < asJsonArray.size(); i++) {
			JsonElement jsonElement = asJsonArray.get(i);
			TuleapProjectConfiguration tuleapProjectConfiguration = gson.fromJson(jsonElement,
					TuleapProjectConfiguration.class);
			projectsConfiguration.add(tuleapProjectConfiguration);
		}

		Iterator<TuleapProjectConfiguration> iterator = projectsConfiguration.iterator();

		TuleapProjectConfiguration firstProjectConfiguration = iterator.next();

		assertEquals(0, firstProjectConfiguration.getIdentifier());
		assertEquals("Project 0", firstProjectConfiguration.getName()); //$NON-NLS-1$

		TuleapProjectConfiguration secondProjectConfiguration = iterator.next();

		assertEquals(1, secondProjectConfiguration.getIdentifier());
		assertEquals("Project 1", secondProjectConfiguration.getName()); //$NON-NLS-1$

		TuleapProjectConfiguration thirdProjectConfiguration = iterator.next();

		assertEquals(2, thirdProjectConfiguration.getIdentifier());
		assertEquals("Project 2", thirdProjectConfiguration.getName()); //$NON-NLS-1$

		TuleapProjectConfiguration fourthProjectConfiguration = iterator.next();

		assertEquals(3, fourthProjectConfiguration.getIdentifier());
		assertEquals("Project 3", fourthProjectConfiguration.getName()); //$NON-NLS-1$

		TuleapProjectConfiguration fifthProjectConfiguration = iterator.next();

		assertEquals(4, fifthProjectConfiguration.getIdentifier());
		assertEquals("Project 4", fifthProjectConfiguration.getName()); //$NON-NLS-1$

	}
}
