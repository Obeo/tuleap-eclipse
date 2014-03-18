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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapProject;
import org.eclipse.mylyn.tuleap.core.internal.parser.TuleapGsonProvider;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests the JSON deserialization of {@link TuleapProject}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapProjectDeserializerTest {

	private Gson gson;

	@Before
	public void setUp() {
		gson = TuleapGsonProvider.defaultGson();
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
}
