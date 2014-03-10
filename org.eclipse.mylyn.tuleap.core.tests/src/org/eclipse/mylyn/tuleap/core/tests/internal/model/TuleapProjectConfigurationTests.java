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
package org.eclipse.mylyn.tuleap.core.tests.internal.model;

import java.util.Collection;
import java.util.Date;

import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapProject;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapResource;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapServer;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapUser;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapUserGroup;
import org.junit.Test;

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

		assertNull(project.getUserGroup("0"));
		assertNull(project.getUserGroup("1"));
		TuleapUserGroup group0 = new TuleapUserGroup("0", "Project Admins");
		project.addGroup(group0);
		assertNotNull(project.getUserGroup("0"));
		assertNull(project.getUserGroup("1"));
		TuleapUserGroup group1 = new TuleapUserGroup("1", "Project Members");
		project.addGroup(group1);
		assertNotNull(project.getUserGroup("0"));
		assertNotNull(project.getUserGroup("1"));

		TuleapUser user0 = new TuleapUser("zero", "John Zero", 0, "john.zero@some.where", null);
		TuleapUser user1 = new TuleapUser("one", "John One", 1, "john.one@some.where", null);
		TuleapUser user2 = new TuleapUser("two", "John Two", 2, "john.two@some.where", null);
		TuleapUser user3 = new TuleapUser("three", "John Three", 3, "john.three@some.where", null);
		project.addUserToUserGroup(group0, user0);
		project.addUserToUserGroup(group0, user1);

		project.addUserToUserGroup(group1, user0);
		project.addUserToUserGroup(group1, user1);
		project.addUserToUserGroup(group1, user2);
		project.addUserToUserGroup(group1, user3);

		TuleapUserGroup g0 = project.getUserGroup("0");
		assertSame(group0, g0);
		TuleapUserGroup g1 = project.getUserGroup("1");
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
		Collection<TuleapUserGroup> groups = project.getAllUserGroups();
		assertEquals(2, groups.size());
		assertTrue(groups.contains(group0));
		assertTrue(groups.contains(group1));
	}
}
