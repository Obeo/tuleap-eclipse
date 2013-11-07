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
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapProject;
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
public class TuleapProjectConfigurationDeserializerTests extends AbstractDeserializerTest<TuleapProject> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected JsonDeserializer<TuleapProject> getDeserializer() {
		return new TuleapProjectConfigurationDeserializer();
	}

	/**
	 * Test the parsing of the first file.
	 */
	@Test
	public void testProject0Parsing() {
		String project0 = ParserUtil.loadFile("/projects/project-0.json");
		TuleapProject tuleapProject = this.parse(project0,
				TuleapProject.class);
		assertNotNull(tuleapProject);
		assertEquals(0, tuleapProject.getIdentifier());
		assertEquals("Project 0", tuleapProject.getName()); //$NON-NLS-1$

		assertTrue(tuleapProject.hasService("documents")); //$NON-NLS-1$
		assertFalse(tuleapProject.hasService("")); //$NON-NLS-1$
		assertFalse(tuleapProject.hasService("trackers")); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the second file.
	 */
	@Test
	public void testProject1Parsing() {
		String project1 = ParserUtil.loadFile("/projects/project-1.json");
		TuleapProject tuleapProject = this.parse(project1,
				TuleapProject.class);
		assertNotNull(tuleapProject);
		assertEquals(1, tuleapProject.getIdentifier());
		assertEquals("Project 1", tuleapProject.getName()); //$NON-NLS-1$

		assertFalse(tuleapProject.hasService("documents")); //$NON-NLS-1$
		assertFalse(tuleapProject.hasService("")); //$NON-NLS-1$
		assertFalse(tuleapProject.hasService("trackers")); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the third file.
	 */
	@Test
	public void testProject2Parsing() {
		String project2 = ParserUtil.loadFile("/projects/project-2.json");
		TuleapProject tuleapProject = this.parse(project2,
				TuleapProject.class);
		assertNotNull(tuleapProject);
		assertEquals(2, tuleapProject.getIdentifier());
		assertEquals("Project 2", tuleapProject.getName()); //$NON-NLS-1$

		assertTrue(tuleapProject.hasService("documents")); //$NON-NLS-1$
		assertFalse(tuleapProject.hasService("")); //$NON-NLS-1$
		assertFalse(tuleapProject.hasService("trackers")); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the fourth file.
	 */
	@Test
	public void testProject3Parsing() {
		String project3 = ParserUtil.loadFile("/projects/project-3.json");
		TuleapProject tuleapProject = this.parse(project3,
				TuleapProject.class);
		assertNotNull(tuleapProject);
		assertEquals(3, tuleapProject.getIdentifier());
		assertEquals("Project 3", tuleapProject.getName()); //$NON-NLS-1$

		assertTrue(tuleapProject.hasService("trackers")); //$NON-NLS-1$
		assertTrue(tuleapProject.hasService("agile_dashboard")); //$NON-NLS-1$
		assertTrue(tuleapProject.hasService("documents")); //$NON-NLS-1$
		assertTrue(tuleapProject.hasService("wikis")); //$NON-NLS-1$
		assertTrue(tuleapProject.hasService("forums")); //$NON-NLS-1$
		assertTrue(tuleapProject.hasService("lists")); //$NON-NLS-1$
		assertTrue(tuleapProject.hasService("news")); //$NON-NLS-1$
		assertTrue(tuleapProject.hasService("subversion")); //$NON-NLS-1$
		assertTrue(tuleapProject.hasService("files")); //$NON-NLS-1$
		assertTrue(tuleapProject.hasService("instant_messaging")); //$NON-NLS-1$
		assertTrue(tuleapProject.hasService("continuous_integration")); //$NON-NLS-1$
		assertTrue(tuleapProject.hasService("git")); //$NON-NLS-1$
		assertFalse(tuleapProject.hasService("")); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the fifth file.
	 */
	@Test
	public void testProject4Parsing() {
		String project4 = ParserUtil.loadFile("/projects/project-4.json");
		TuleapProject tuleapProject = this.parse(project4,
				TuleapProject.class);
		assertNotNull(tuleapProject);
		assertEquals(4, tuleapProject.getIdentifier());
		assertEquals("Project 4", tuleapProject.getName()); //$NON-NLS-1$

		assertTrue(tuleapProject.hasService("git")); //$NON-NLS-1$
		assertFalse(tuleapProject.hasService("")); //$NON-NLS-1$
		assertFalse(tuleapProject.hasService("documents")); //$NON-NLS-1$
		assertFalse(tuleapProject.hasService("trackers")); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the projects file.
	 */
	@Test
	public void testProjectsParsing() {
		String projects = ParserUtil.loadFile("/projects/projects.json");
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TuleapProject.class,
				new TuleapProjectConfigurationDeserializer());

		JsonParser jsonParser = new JsonParser();
		JsonArray asJsonArray = jsonParser.parse(projects).getAsJsonArray();
		Gson gson = gsonBuilder.create();

		List<TuleapProject> projectsConfiguration = new ArrayList<TuleapProject>();
		for (int i = 0; i < asJsonArray.size(); i++) {
			JsonElement jsonElement = asJsonArray.get(i);
			TuleapProject tuleapProject = gson.fromJson(jsonElement,
					TuleapProject.class);
			projectsConfiguration.add(tuleapProject);
		}

		Iterator<TuleapProject> iterator = projectsConfiguration.iterator();

		TuleapProject firstProjectConfiguration = iterator.next();

		assertEquals(0, firstProjectConfiguration.getIdentifier());
		assertEquals("Project 0", firstProjectConfiguration.getName()); //$NON-NLS-1$
		assertTrue(firstProjectConfiguration.hasService("documents")); //$NON-NLS-1$
		assertFalse(firstProjectConfiguration.hasService("")); //$NON-NLS-1$
		assertFalse(firstProjectConfiguration.hasService("trackers")); //$NON-NLS-1$

		TuleapProject secondProjectConfiguration = iterator.next();

		assertEquals(1, secondProjectConfiguration.getIdentifier());
		assertEquals("Project 1", secondProjectConfiguration.getName()); //$NON-NLS-1$
		assertFalse(secondProjectConfiguration.hasService("documents")); //$NON-NLS-1$
		assertFalse(secondProjectConfiguration.hasService("")); //$NON-NLS-1$
		assertFalse(secondProjectConfiguration.hasService("trackers")); //$NON-NLS-1$

		TuleapProject thirdProjectConfiguration = iterator.next();

		assertEquals(2, thirdProjectConfiguration.getIdentifier());
		assertEquals("Project 2", thirdProjectConfiguration.getName()); //$NON-NLS-1$
		assertTrue(thirdProjectConfiguration.hasService("documents")); //$NON-NLS-1$
		assertFalse(thirdProjectConfiguration.hasService("")); //$NON-NLS-1$
		assertFalse(thirdProjectConfiguration.hasService("trackers")); //$NON-NLS-1$

		TuleapProject fourthProjectConfiguration = iterator.next();

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

		TuleapProject fifthProjectConfiguration = iterator.next();

		assertEquals(4, fifthProjectConfiguration.getIdentifier());
		assertEquals("Project 4", fifthProjectConfiguration.getName()); //$NON-NLS-1$
		assertTrue(fifthProjectConfiguration.hasService("git")); //$NON-NLS-1$
		assertFalse(fifthProjectConfiguration.hasService("")); //$NON-NLS-1$
		assertFalse(fifthProjectConfiguration.hasService("documents")); //$NON-NLS-1$
		assertFalse(fifthProjectConfiguration.hasService("trackers")); //$NON-NLS-1$

	}
}
