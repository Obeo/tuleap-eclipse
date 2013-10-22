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
package org.tuleap.mylyn.task.internal.tests.client.rest;

import com.google.common.collect.Maps;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.client.rest.ITuleapHeaders;
import org.tuleap.mylyn.task.internal.core.client.rest.RestOpGet;
import org.tuleap.mylyn.task.internal.core.client.rest.RestOpPut;
import org.tuleap.mylyn.task.internal.core.client.rest.RestResource;
import org.tuleap.mylyn.task.internal.core.client.rest.RestResourceFactory;
import org.tuleap.mylyn.task.internal.core.client.rest.ServerResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests of {@link RestResourceFactory}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapRestResourceFactoryTest {

	private RestResourceFactory factory;

	private MockRestConnector connector;

	/**
	 * Checks the basic properties of {@link RestResourceFactory#milestone(int)}.
	 */
	@Test
	public void testGetMilestones() {
		RestResource milestone = factory.milestone(123);
		assertNotNull(milestone);
		assertEquals("/milestones/123", milestone.getUrl());
		assertEquals("/server/api/v12.5/milestones/123", milestone.getFullUrl());
	}

	/**
	 * Checks that POST is not supported by the operation returned by
	 * {@link RestResourceFactory#milestone(int)}.
	 * 
	 * @throws CoreException
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testGetMilestonesIsPostForbidden() throws CoreException {
		RestResource milestone = factory.milestone(123);
		milestone.post();
	}

	/**
	 * Checks that PUT is supported by the operation returned by {@link RestResourceFactory#milestone(int)}.
	 * 
	 * @throws CoreException
	 */
	@Test
	public void testGetMilestonesIsPutForbidden() throws CoreException {
		RestResource milestone = factory.milestone(123);
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,PUT");
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));

		RestOpPut put = milestone.put();
		assertNotNull(put);
		assertEquals("PUT", put.getMethodName());
		assertEquals("/server/api/v12.5/milestones/123", put.getUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#milestonesBacklogItems(int)}.
	 */
	@Test
	public void testGetMilestonesBacklogItems() {
		RestResource r = factory.milestonesBacklogItems(123);
		assertNotNull(r);
		assertEquals("/milestones/123/backlog_items", r.getUrl());
		assertEquals("/server/api/v12.5/milestones/123/backlog_items", r.getFullUrl());
	}

	/**
	 * Checks that GET is supported by the operation returned by {@link RestResourceFactory#milestone(int)}.
	 * 
	 * @throws CoreException
	 */
	@Test
	public void testGetMilestonesBacklogItemsGet() throws CoreException {
		RestResource r = factory.milestonesBacklogItems(123);
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,GET");
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));
		RestOpGet get = r.get();
		assertNotNull(get);
		assertEquals("GET", get.getMethodName());
		assertEquals("/server/api/v12.5/milestones/123/backlog_items", get.getUrl());
	}

	/**
	 * Checks that GET is supported by the operation returned by {@link RestResourceFactory#milestone(int)}
	 * but sends an error if the server does not allow GET in the OPTIONS header "allow" property.
	 * 
	 * @throws CoreException
	 */
	@Test(expected = CoreException.class)
	public void testGetMilestonesBacklogItemsGetWhenServerDoesNotAllowGet() throws CoreException {
		RestResource r = factory.milestonesBacklogItems(123);
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS");
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));
		RestOpGet get = r.get();
	}

	/**
	 * Checks that GET is supported by the operation returned by {@link RestResourceFactory#milestone(int)}
	 * but sends an error if the server does not allow GET in the OPTIONS header
	 * "access-control-allow-methods" property.
	 * 
	 * @throws CoreException
	 */
	@Test(expected = CoreException.class)
	public void testGetMilestonesBacklogItemsGetWhenServerDoesNotAllowCorsGet() throws CoreException {
		RestResource r = factory.milestonesBacklogItems(123);
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,GET");
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));
		RestOpGet get = r.get();
	}

	/**
	 * Checks that POST is not supported by the operation returned by
	 * {@link RestResourceFactory#milestone(int)}.
	 * 
	 * @throws CoreException
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testGetMilestonesBacklogItemsIsPostForbidden() throws CoreException {
		RestResource r = factory.milestonesBacklogItems(123);
		r.post();
	}

	/**
	 * Checks that PUT is supported by the operation returned by {@link RestResourceFactory#milestone(int)}.
	 * 
	 * @throws CoreException
	 */
	@Test
	public void testGetMilestonesBacklogItemsIsPutForbidden() throws CoreException {
		RestResource r = factory.milestonesBacklogItems(123);

		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,PUT");
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));

		RestOpPut put = r.put();
		assertNotNull(put);
		assertEquals("PUT", put.getMethodName());
		assertEquals("/server/api/v12.5/milestones/123/backlog_items", put.getUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#milestonesCardwall(int)}.
	 */
	@Test
	public void testGetMilestonesCardwall() {
		RestResource r = factory.milestonesCardwall(123);
		assertNotNull(r);
		assertEquals("/milestones/123/cardwall", r.getUrl());
		assertEquals("/server/api/v12.5/milestones/123/cardwall", r.getFullUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#milestonesSubmilestones(int)}.
	 */
	@Test
	public void testGetMilestonesSubmilestones() {
		RestResource r = factory.milestonesSubmilestones(123);
		assertNotNull(r);
		assertEquals("/milestones/123/submilestones", r.getUrl());
		assertEquals("/server/api/v12.5/milestones/123/submilestones", r.getFullUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#artifacts(int)}.
	 */
	@Test
	public void testGetArtifactsById() {
		RestResource r = factory.artifacts(123);
		assertNotNull(r);
		assertEquals("/artifacts/123", r.getUrl());
		assertEquals("/server/api/v12.5/artifacts/123", r.getFullUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#artifacts()}.
	 */
	@Test
	public void testGetArtifacts() {
		RestResource r = factory.artifacts();
		assertNotNull(r);
		assertEquals("/artifacts", r.getUrl());
		assertEquals("/server/api/v12.5/artifacts", r.getFullUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#cards(int)}.
	 */
	@Test
	public void testGetCardsById() {
		RestResource r = factory.cards(123);
		assertNotNull(r);
		assertEquals("/cards/123", r.getUrl());
		assertEquals("/server/api/v12.5/cards/123", r.getFullUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#projects()}.
	 */
	@Test
	public void testGetProjects() {
		RestResource r = factory.projects();
		assertNotNull(r);
		assertEquals("/projects", r.getUrl());
		assertEquals("/server/api/v12.5/projects", r.getFullUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#projectsBacklogItemTypes(int)}.
	 */
	@Test
	public void testGetProjectBacklogItemTypes() {
		RestResource r = factory.projectsBacklogItemTypes(123);
		assertNotNull(r);
		assertEquals("/projects/123/backlog_item_types", r.getUrl());
		assertEquals("/server/api/v12.5/projects/123/backlog_item_types", r.getFullUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#backlogItemType(int)}.
	 */
	@Test
	public void testGetBacklogItemType() {
		RestResource r = factory.backlogItemType(111);
		assertNotNull(r);
		assertEquals("/backlog_item_types/111", r.getUrl());
		assertEquals("/server/api/v12.5/backlog_item_types/111", r.getFullUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#cardType(int)}.
	 */
	@Test
	public void testGetCardType() {
		RestResource r = factory.cardType(321);
		assertNotNull(r);
		assertEquals("/card_types/321", r.getUrl());
		assertEquals("/server/api/v12.5/card_types/321", r.getFullUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#projectsCardTypes(int)}.
	 */
	@Test
	public void testGetProjectCardTypes() {
		RestResource r = factory.projectsCardTypes(321);
		assertNotNull(r);
		assertEquals("/projects/321/card_types", r.getUrl());
		assertEquals("/server/api/v12.5/projects/321/card_types", r.getFullUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#milestoneType(int)}.
	 */
	@Test
	public void testGetMilestoneType() {
		RestResource r = factory.milestoneType(321);
		assertNotNull(r);
		assertEquals("/milestone_types/321", r.getUrl());
		assertEquals("/server/api/v12.5/milestone_types/321", r.getFullUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#projectsMilestoneTypes(int)}.
	 */
	@Test
	public void testGetProjectMilestoneTypes() {
		RestResource r = factory.projectsMilestoneTypes(321);
		assertNotNull(r);
		assertEquals("/projects/321/milestone_types", r.getUrl());
		assertEquals("/server/api/v12.5/projects/321/milestone_types", r.getFullUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#projectsTopPlannings(int)}.
	 */
	@Test
	public void testGetProjectTopPlannings() {
		RestResource r = factory.projectsTopPlannings(321);
		assertNotNull(r);
		assertEquals("/projects/321/top_plannings", r.getUrl());
		assertEquals("/server/api/v12.5/projects/321/top_plannings", r.getFullUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#projectsTrackers(int)}.
	 */
	@Test
	public void testGetProjectTrackers() {
		RestResource r = factory.projectsTrackers(321);
		assertNotNull(r);
		assertEquals("/projects/321/trackers", r.getUrl());
		assertEquals("/server/api/v12.5/projects/321/trackers", r.getFullUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#topPlannings(int)}.
	 */
	@Test
	public void testGetTopPlanning() {
		RestResource r = factory.topPlannings(321);
		assertNotNull(r);
		assertEquals("/top_plannings/321", r.getUrl());
		assertEquals("/server/api/v12.5/top_plannings/321", r.getFullUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#topPlanningsBacklogItems(int)}.
	 */
	@Test
	public void testGetTopPlanningBacklogItems() {
		RestResource r = factory.topPlanningsBacklogItems(321);
		assertNotNull(r);
		assertEquals("/top_plannings/321/backlog_items", r.getUrl());
		assertEquals("/server/api/v12.5/top_plannings/321/backlog_items", r.getFullUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#topPlanningsMilestones(int)}.
	 */
	@Test
	public void testGetTopPlanningMilestones() {
		RestResource r = factory.topPlanningsMilestones(321);
		assertNotNull(r);
		assertEquals("/top_plannings/321/milestones", r.getUrl());
		assertEquals("/server/api/v12.5/top_plannings/321/milestones", r.getFullUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#user()}.
	 */
	@Test
	public void testGetUser() {
		RestResource r = factory.user();
		assertNotNull(r);
		assertEquals("/user", r.getUrl());
		assertEquals("/server/api/v12.5/user", r.getFullUrl());
	}

	/**
	 * Set up the tests.
	 */
	@Before
	public void setUp() {
		connector = new MockRestConnector();
		factory = new RestResourceFactory("/server", "v12.5", connector);
	}
}
