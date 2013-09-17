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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.parser.TuleapProjectConfigurationDeserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests the desiarialization of the Tuleap project configuration.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapProjectConfigurationDeserializerTests {

	/**
	 * Folder prefix.
	 */
	private static final String FOLDER_PREFIX = "/json/projects/"; //$NON-NLS-1$

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
	 * Loads the json files from the server data project into the appropriate variables.
	 */
	@BeforeClass
	public static void staticSetUp() {
		projects = ParserUtil.loadFile(FOLDER_PREFIX + PROJECTS_FILE_NAME);
		project0 = ParserUtil.loadFile(FOLDER_PREFIX + PROJECT_FILE_PREFIX + 0 + JSON_EXTENSION);
		project1 = ParserUtil.loadFile(FOLDER_PREFIX + PROJECT_FILE_PREFIX + 1 + JSON_EXTENSION);
		project2 = ParserUtil.loadFile(FOLDER_PREFIX + PROJECT_FILE_PREFIX + 2 + JSON_EXTENSION);
		project3 = ParserUtil.loadFile(FOLDER_PREFIX + PROJECT_FILE_PREFIX + 3 + JSON_EXTENSION);
		project4 = ParserUtil.loadFile(FOLDER_PREFIX + PROJECT_FILE_PREFIX + 4 + JSON_EXTENSION);
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
		TuleapProjectConfiguration tuleapProjectConfiguration = this.parse(project0);
		assertNotNull(tuleapProjectConfiguration);
		assertEquals(0, tuleapProjectConfiguration.getIdentifier());
		assertEquals("Project 0", tuleapProjectConfiguration.getName()); //$NON-NLS-1$

		assertTrue(tuleapProjectConfiguration.hasService("documents")); //$NON-NLS-1$
		assertFalse(tuleapProjectConfiguration.hasService("")); //$NON-NLS-1$
		assertFalse(tuleapProjectConfiguration.hasService("trackers")); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the second file.
	 */
	@Test
	public void testProject1Parsing() {
		TuleapProjectConfiguration tuleapProjectConfiguration = this.parse(project1);
		assertNotNull(tuleapProjectConfiguration);
		assertEquals(1, tuleapProjectConfiguration.getIdentifier());
		assertEquals("Project 1", tuleapProjectConfiguration.getName()); //$NON-NLS-1$

		assertFalse(tuleapProjectConfiguration.hasService("documents")); //$NON-NLS-1$
		assertFalse(tuleapProjectConfiguration.hasService("")); //$NON-NLS-1$
		assertFalse(tuleapProjectConfiguration.hasService("trackers")); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the third file.
	 */
	@Test
	public void testProject2Parsing() {
		TuleapProjectConfiguration tuleapProjectConfiguration = this.parse(project2);
		assertNotNull(tuleapProjectConfiguration);
		assertEquals(2, tuleapProjectConfiguration.getIdentifier());
		assertEquals("Project 2", tuleapProjectConfiguration.getName()); //$NON-NLS-1$

		assertTrue(tuleapProjectConfiguration.hasService("documents")); //$NON-NLS-1$
		assertFalse(tuleapProjectConfiguration.hasService("")); //$NON-NLS-1$
		assertFalse(tuleapProjectConfiguration.hasService("trackers")); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the fourth file.
	 */
	@Test
	public void testProject3Parsing() {
		TuleapProjectConfiguration tuleapProjectConfiguration = this.parse(project3);
		assertNotNull(tuleapProjectConfiguration);
		assertEquals(3, tuleapProjectConfiguration.getIdentifier());
		assertEquals("Project 3", tuleapProjectConfiguration.getName()); //$NON-NLS-1$

		assertTrue(tuleapProjectConfiguration.hasService("trackers")); //$NON-NLS-1$
		assertTrue(tuleapProjectConfiguration.hasService("agile_dashboard")); //$NON-NLS-1$
		assertTrue(tuleapProjectConfiguration.hasService("documents")); //$NON-NLS-1$
		assertTrue(tuleapProjectConfiguration.hasService("wikis")); //$NON-NLS-1$
		assertTrue(tuleapProjectConfiguration.hasService("forums")); //$NON-NLS-1$
		assertTrue(tuleapProjectConfiguration.hasService("lists")); //$NON-NLS-1$
		assertTrue(tuleapProjectConfiguration.hasService("news")); //$NON-NLS-1$
		assertTrue(tuleapProjectConfiguration.hasService("subversion")); //$NON-NLS-1$
		assertTrue(tuleapProjectConfiguration.hasService("files")); //$NON-NLS-1$
		assertTrue(tuleapProjectConfiguration.hasService("instant_messaging")); //$NON-NLS-1$
		assertTrue(tuleapProjectConfiguration.hasService("continuous_integration")); //$NON-NLS-1$
		assertTrue(tuleapProjectConfiguration.hasService("git")); //$NON-NLS-1$
		assertFalse(tuleapProjectConfiguration.hasService("")); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the fifth file.
	 */
	@Test
	public void testProject4Parsing() {
		TuleapProjectConfiguration tuleapProjectConfiguration = this.parse(project4);
		assertNotNull(tuleapProjectConfiguration);
		assertEquals(4, tuleapProjectConfiguration.getIdentifier());
		assertEquals("Project 4", tuleapProjectConfiguration.getName()); //$NON-NLS-1$

		assertTrue(tuleapProjectConfiguration.hasService("git")); //$NON-NLS-1$
		assertFalse(tuleapProjectConfiguration.hasService("")); //$NON-NLS-1$
		assertFalse(tuleapProjectConfiguration.hasService("documents")); //$NON-NLS-1$
		assertFalse(tuleapProjectConfiguration.hasService("trackers")); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the projects file.
	 */
	@Test
	public void testProjectsParsing() {
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
		assertTrue(firstProjectConfiguration.hasService("documents")); //$NON-NLS-1$
		assertFalse(firstProjectConfiguration.hasService("")); //$NON-NLS-1$
		assertFalse(firstProjectConfiguration.hasService("trackers")); //$NON-NLS-1$

		TuleapProjectConfiguration secondProjectConfiguration = iterator.next();

		assertEquals(1, secondProjectConfiguration.getIdentifier());
		assertEquals("Project 1", secondProjectConfiguration.getName()); //$NON-NLS-1$
		assertFalse(secondProjectConfiguration.hasService("documents")); //$NON-NLS-1$
		assertFalse(secondProjectConfiguration.hasService("")); //$NON-NLS-1$
		assertFalse(secondProjectConfiguration.hasService("trackers")); //$NON-NLS-1$

		TuleapProjectConfiguration thirdProjectConfiguration = iterator.next();

		assertEquals(2, thirdProjectConfiguration.getIdentifier());
		assertEquals("Project 2", thirdProjectConfiguration.getName()); //$NON-NLS-1$
		assertTrue(thirdProjectConfiguration.hasService("documents")); //$NON-NLS-1$
		assertFalse(thirdProjectConfiguration.hasService("")); //$NON-NLS-1$
		assertFalse(thirdProjectConfiguration.hasService("trackers")); //$NON-NLS-1$

		TuleapProjectConfiguration fourthProjectConfiguration = iterator.next();

		assertEquals(3, fourthProjectConfiguration.getIdentifier());
		assertEquals("Project 3", fourthProjectConfiguration.getName()); //$NON-NLS-1$
		assertTrue(fourthProjectConfiguration.hasService("trackers")); //$NON-NLS-1$
		assertTrue(fourthProjectConfiguration.hasService("agile_dashboard")); //$NON-NLS-1$
		assertTrue(fourthProjectConfiguration.hasService("documents")); //$NON-NLS-1$
		assertTrue(fourthProjectConfiguration.hasService("wikis")); //$NON-NLS-1$
		assertTrue(fourthProjectConfiguration.hasService("forums")); //$NON-NLS-1$
		assertTrue(fourthProjectConfiguration.hasService("lists")); //$NON-NLS-1$
		assertTrue(fourthProjectConfiguration.hasService("news")); //$NON-NLS-1$
		assertTrue(fourthProjectConfiguration.hasService("subversion")); //$NON-NLS-1$
		assertTrue(fourthProjectConfiguration.hasService("files")); //$NON-NLS-1$
		assertTrue(fourthProjectConfiguration.hasService("instant_messaging")); //$NON-NLS-1$
		assertTrue(fourthProjectConfiguration.hasService("continuous_integration")); //$NON-NLS-1$
		assertTrue(fourthProjectConfiguration.hasService("git")); //$NON-NLS-1$
		assertFalse(fourthProjectConfiguration.hasService("")); //$NON-NLS-1$

		TuleapProjectConfiguration fifthProjectConfiguration = iterator.next();

		assertEquals(4, fifthProjectConfiguration.getIdentifier());
		assertEquals("Project 4", fifthProjectConfiguration.getName()); //$NON-NLS-1$
		assertTrue(fifthProjectConfiguration.hasService("git")); //$NON-NLS-1$
		assertFalse(fifthProjectConfiguration.hasService("")); //$NON-NLS-1$
		assertFalse(fifthProjectConfiguration.hasService("documents")); //$NON-NLS-1$
		assertFalse(fifthProjectConfiguration.hasService("trackers")); //$NON-NLS-1$

	}
}
