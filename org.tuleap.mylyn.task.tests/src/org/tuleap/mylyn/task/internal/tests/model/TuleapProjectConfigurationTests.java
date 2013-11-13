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
package org.tuleap.mylyn.task.internal.tests.model;

import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapGroup;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapPerson;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapPlanning;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapProject;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapServer;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapTracker;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
		long dateTracker = date.getTime();
		TuleapTracker tracker = new TuleapTracker(1, "tracker/url", "Tracker", "Item name", "Description",
				dateTracker);
		project.addTracker(tracker);

		assertNull(project.getTracker(0));
		TuleapTracker t = project.getTracker(1);
		assertSame(tracker, t);
		assertEquals(1, project.getAllTrackers().size());
	}

	@Test
	public void testServices() {
		TuleapProject project = new TuleapProject("Test Project", 42);

		String service = "trackers";
		assertFalse(project.hasService(service));

		project.addService(service);
		assertTrue(project.hasService(service));
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
	 * Checks the regitration of users and user groups.
	 */
	@Test
	public void testUsermanagement() {
		// TODO LDE There is something wrong with the fact that the constructor of ProjectConfiguration does
		// not receive the server config
		TuleapServer server = new TuleapServer("http://server/url");
		TuleapProject project = new TuleapProject("Test Project", 42);
		server.addProject(project);

		assertNull(project.getGroup(0));
		assertNull(project.getGroup(1));
		TuleapGroup group0 = new TuleapGroup(0, "Project Admins");
		project.addGroup(group0);
		assertNotNull(project.getGroup(0));
		assertNull(project.getGroup(1));
		TuleapGroup group1 = new TuleapGroup(1, "Project Members");
		project.addGroup(group1);
		assertNotNull(project.getGroup(0));
		assertNotNull(project.getGroup(01));

		TuleapPerson user0 = new TuleapPerson("zero", "John Zero", 0, "john.zero@some.where");
		TuleapPerson user1 = new TuleapPerson("one", "John One", 1, "john.one@some.where");
		TuleapPerson user2 = new TuleapPerson("two", "John Two", 2, "john.two@some.where");
		TuleapPerson user3 = new TuleapPerson("three", "John Three", 3, "john.three@some.where");
		project.addUserToUserGroup(group0, user0);
		project.addUserToUserGroup(group0, user1);

		project.addUserToUserGroup(group1, user0);
		project.addUserToUserGroup(group1, user1);
		project.addUserToUserGroup(group1, user2);
		project.addUserToUserGroup(group1, user3);

		TuleapGroup g0 = project.getGroup(0);
		assertSame(group0, g0);
		TuleapGroup g1 = project.getGroup(1);
		assertSame(group1, g1);

		assertEquals(2, g0.getMembers().size());
		assertTrue(g0.getMembers().contains(user0));
		assertTrue(g0.getMembers().contains(user1));
		assertEquals(4, g1.getMembers().size());
		assertTrue(g1.getMembers().contains(user0));
		assertTrue(g1.getMembers().contains(user1));
		assertTrue(g1.getMembers().contains(user2));
		assertTrue(g1.getMembers().contains(user3));

		// Check that users are also registered at the server level
		assertSame(user0, server.getUser(0));
		assertSame(user1, server.getUser(1));
		assertSame(user2, server.getUser(2));
		assertSame(user3, server.getUser(3));

		// Check retrieval of all groups
		Collection<TuleapGroup> groups = project.getAllGroups();
		assertEquals(2, groups.size());
		assertTrue(groups.contains(group0));
		assertTrue(groups.contains(group1));
	}
}
