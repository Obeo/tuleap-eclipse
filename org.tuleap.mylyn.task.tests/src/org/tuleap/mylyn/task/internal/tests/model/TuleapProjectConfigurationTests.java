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
import org.tuleap.mylyn.task.internal.core.model.TuleapGroup;
import org.tuleap.mylyn.task.internal.core.model.TuleapPerson;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItemType;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapCardType;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestoneType;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;

import static org.junit.Assert.assertEquals;
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
	 * Test adding configuration elements to a project.
	 */
	@Test
	public void testConfigurationRetrieval() {
		TuleapProjectConfiguration project = new TuleapProjectConfiguration("Test Project", 42);

		Date date = new Date();
		long dateTracker = date.getTime();
		TuleapTrackerConfiguration tracker = new TuleapTrackerConfiguration(1, "tracker/url", "Tracker",
				"Item name", "Description", dateTracker);
		project.addTracker(tracker);

		long dateBI = date.getTime() - 60 * 60 * 1000;
		TuleapBacklogItemType backlogItemType = new TuleapBacklogItemType(100, "bi/url", "User Story",
				"user_story", "Description of user stories", dateBI);
		project.addBacklogItemType(backlogItemType);

		long dateMilestone = date.getTime() - 2 * 60 * 60 * 1000;
		TuleapMilestoneType milestoneType = new TuleapMilestoneType(200, "milestone/url", "Sprint", "sprint",
				"Description of sprints", dateMilestone, "/cardwall/url");
		project.addMilestoneType(milestoneType);

		long dateCard = date.getTime() - 3 * 60 * 60 * 1000;
		TuleapCardType cardType = new TuleapCardType(300, "bi/url", "User Story", "user_story",
				"Description of user stories", dateCard);
		project.addCardType(cardType);

		assertNull(project.getTrackerConfiguration(0));
		TuleapTrackerConfiguration t = project.getTrackerConfiguration(1);
		assertSame(tracker, t);
		assertEquals(1, project.getAllTrackerConfigurations().size());

		assertNull(project.getBacklogItemType(0));
		TuleapBacklogItemType bi = project.getBacklogItemType(100);
		assertSame(backlogItemType, bi);
		assertEquals(1, project.getAllBacklogItemTypes().size());

		assertNull(project.getMilestoneType(0));
		TuleapMilestoneType m = project.getMilestoneType(200);
		assertSame(milestoneType, m);
		assertEquals(1, project.getAllMilestoneTypes().size());

		assertNull(project.getCardType(0));
		TuleapCardType c = project.getCardType(300);
		assertSame(cardType, c);
		assertEquals(1, project.getAllCardTypes().size());
	}

	/**
	 * Checks the regitration of users and user groups.
	 */
	@Test
	public void testUsermanagement() {
		// TODO LDE There is something wrong with the fact that the constructor of ProjectConfiguration does
		// not receive the server config
		TuleapServerConfiguration server = new TuleapServerConfiguration("http://server/url");
		TuleapProjectConfiguration project = new TuleapProjectConfiguration("Test Project", 42);
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
