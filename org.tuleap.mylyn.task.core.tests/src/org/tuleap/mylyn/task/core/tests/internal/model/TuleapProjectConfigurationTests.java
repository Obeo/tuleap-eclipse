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
package org.tuleap.mylyn.task.core.tests.internal.model;

import java.util.Date;

import org.junit.Test;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapPlanning;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapProject;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapResource;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapTracker;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Unit Tests of Project configuration.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapProjectConfigurationTests {

	/**
	 * Test adding and retrieving trackers to a project.
	 */
	@Test
	public void testTrackers() {
		TuleapProject project = new TuleapProject("Test Project", 42);

		Date date = new Date();
		TuleapTracker tracker = new TuleapTracker(1, "tracker/url", "Tracker", "Item name", "Description",
				date);
		project.addTracker(tracker);

		assertNull(project.getTracker(0));
		TuleapTracker t = project.getTracker(1);
		assertSame(tracker, t);
		assertEquals(1, project.getAllTrackers().size());
	}

	@Test
	public void testResources() {
		TuleapProject project = new TuleapProject("Test Project", 42);
		String resource = "trackers";
		assertFalse(project.hasResource(resource));

		TuleapResource[] resources = new TuleapResource[4];

		for (int i = 0; i < 4; i++) {
			TuleapResource tuleapRessource = new TuleapResource("type" + i, "uri/" + i);
			resources[i] = tuleapRessource;
		}
		project.setProjectResources(resources);
		assertEquals(4, project.getProjectResources().length);

		for (int i = 0; i < 4; i++) {
			assertEquals("type" + i, project.getProjectResources()[i].getType());
			assertEquals("uri/" + i, project.getProjectResources()[i].getUri());
			assertTrue(project.hasResource("type" + i));
		}
	}

	@Test
	public void testPlannings() {
		TuleapProject project = new TuleapProject("Test Project", 42);
		TuleapReference projectRef = new TuleapReference(42, "projects/42");
		TuleapPlanning planning = new TuleapPlanning(100, projectRef);

		TuleapReference reference = new TuleapReference();
		reference.setId(123);
		reference.setUri("trackers/123");
		planning.setMilestoneTracker(reference);

		reference = new TuleapReference();
		reference.setId(321);
		reference.setUri("trackers/321");
		TuleapReference[] trackers = new TuleapReference[2];
		trackers[0] = reference;
		reference = new TuleapReference();
		reference.setId(322);
		reference.setUri("trackers/322");
		trackers[1] = reference;
		planning.setBacklogTrackers(trackers);

		project.addPlanning(planning);

		assertFalse(project.isBacklogTracker(0)); // id that does not exist causes no exception
		assertFalse(project.isBacklogTracker(123));
		assertTrue(project.isBacklogTracker(321));
		assertTrue(project.isBacklogTracker(322));

		assertFalse(project.isMilestoneTracker(0)); // id that does not exist causes no exception
		assertFalse(project.isMilestoneTracker(321));
		assertFalse(project.isMilestoneTracker(322));
		assertTrue(project.isMilestoneTracker(123));

		assertFalse(project.isCardwallActive(0)); // id that does not exist causes no exception
		assertFalse(project.isCardwallActive(123));
		assertFalse(project.isCardwallActive(321));
		assertFalse(project.isCardwallActive(322));
	}

	@Test
	public void testPlanningCardwall() {
		TuleapProject project = new TuleapProject("Test Project", 42);
		TuleapReference projectRef = new TuleapReference(42, "projects/42");
		TuleapPlanning planning = new TuleapPlanning(100, projectRef);

		planning.setCardwallConfigurationUri("some uri");

		TuleapReference reference = new TuleapReference();
		reference.setId(123);
		reference.setUri("trackers/123");
		planning.setMilestoneTracker(reference);

		reference = new TuleapReference();
		reference.setId(321);
		reference.setUri("trackers/321");
		TuleapReference[] trackers = new TuleapReference[2];
		trackers[0] = reference;
		reference = new TuleapReference();
		reference.setId(322);
		reference.setUri("trackers/322");
		trackers[1] = reference;
		planning.setBacklogTrackers(trackers);

		project.addPlanning(planning);

		assertFalse(project.isCardwallActive(0)); // id that does not exist causes no exception
		assertTrue(project.isCardwallActive(123)); // 123 is the id of the milestone tracker, and a cardwall
		// is configured
		assertFalse(project.isCardwallActive(321)); // other ids are not ids of milestone trackers
		assertFalse(project.isCardwallActive(322));
	}

	/**
	 * Test getting and setting project attributes .
	 */
	@Test
	public void testSetAndGetProjectAttributes() {
		TuleapProject project = new TuleapProject("Test Project", 42);
		project.setUri("projects/42");
		assertEquals("projects/42", project.getUri());
		assertEquals(42, project.getIdentifier());
		assertEquals("Test Project", project.getLabel());
		assertNull(project.getServer());
	}

}
