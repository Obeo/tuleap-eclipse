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
package org.eclipse.mylyn.tuleap.core.tests.internal.client.rest;

import com.google.common.collect.Maps;
import com.google.gson.Gson;

import java.util.Map;

import org.apache.commons.httpclient.HttpMethod;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.RestOperation;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.RestResource;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.RestResourceFactory;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.ServerResponse;
import org.eclipse.mylyn.tuleap.core.tests.internal.TestLogger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests of {@link RestResourceFactory}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapRestResourceFactoryTest {

	private RestResourceFactory factory;

	private MockRestConnector connector;

	private Gson gson;

	/**
	 * Checks the basic properties of {@link RestResourceFactory#milestone(int)}.
	 */
	@Test
	public void testGetMilestones() {
		RestResource milestone = factory.milestone(123);
		assertNotNull(milestone);
		assertEquals("/api/v12.5/milestones/123", milestone.getUrl());
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
		headers.put(RestResource.ALLOW, "OPTIONS,PUT");
		headers.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));

		RestOperation put = milestone.put();
		assertNotNull(put);
		HttpMethod method = put.createMethod();
		assertEquals("PUT", method.getName());
		assertEquals("/api/v12.5/milestones/123", method.getPath());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#milestoneBacklog(int)}.
	 */
	@Test
	public void testGetMilestonesBacklog() {
		RestResource r = factory.milestoneBacklog(123);
		assertNotNull(r);
		assertEquals("/api/v12.5/milestones/123/backlog", r.getUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#milestoneContent(int)}.
	 */
	@Test
	public void testGetMilestonesContent() {
		RestResource r = factory.milestoneContent(123);
		assertNotNull(r);
		assertEquals("/api/v12.5/milestones/123/content", r.getUrl());
	}

	/**
	 * Checks that GET is supported by the operation returned by
	 * {@link RestResourceFactory#milestoneBacklog(int)}.
	 * 
	 * @throws CoreException
	 */
	@Test
	public void testGetMilestonesBacklogGet() throws CoreException {
		RestResource r = factory.milestoneBacklog(123);
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(RestResource.ALLOW, "OPTIONS,GET");
		headers.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));
		RestOperation get = r.get();
		assertNotNull(get);
		HttpMethod method = get.createMethod();
		assertEquals("GET", method.getName());
		assertEquals("/api/v12.5/milestones/123/backlog", method.getPath());
	}

	/**
	 * Checks that GET is supported by the operation returned by
	 * {@link RestResourceFactory#milestoneContent(int)}.
	 * 
	 * @throws CoreException
	 */
	@Test
	public void testGetMilestonesContentGet() throws CoreException {
		RestResource r = factory.milestoneContent(123);
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(RestResource.ALLOW, "OPTIONS,GET");
		headers.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));
		RestOperation get = r.get();
		assertNotNull(get);
		HttpMethod method = get.createMethod();
		assertEquals("GET", method.getName());
		assertEquals("/api/v12.5/milestones/123/content", method.getPath());
	}

	/**
	 * Checks that GET is supported by the operation returned by
	 * {@link RestResourceFactory#milestoneBacklog(int)} but sends an error if the server does not allow GET
	 * in the OPTIONS header "access-control-allow-methods" property.
	 * 
	 * @throws CoreException
	 */
	@Test(expected = CoreException.class)
	@Ignore("Reactivate when Tuleap supports CORS header attributes")
	public void testGetMilestonesBacklogGetWhenServerDoesNotAllowCorsGet() throws CoreException {
		RestResource r = factory.milestoneBacklog(123);
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(RestResource.ALLOW, "OPTIONS,GET");
		headers.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));
		r.get();
	}

	/**
	 * Checks that GET is supported by the operation returned by
	 * {@link RestResourceFactory#milestoneBacklog(int)} but sends an error if the server does not allow GET
	 * in the OPTIONS header "access-control-allow-methods" property.
	 * 
	 * @throws CoreException
	 */
	@Test(expected = CoreException.class)
	@Ignore("Reactivate when Tuleap supports CORS header attributes")
	public void testGetMilestonesContentGetWhenServerDoesNotAllowCorsGet() throws CoreException {
		RestResource r = factory.milestoneContent(123);
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(RestResource.ALLOW, "OPTIONS,GET");
		headers.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));
		r.get();
	}

	/**
	 * Checks that POST is not supported by the operation returned by
	 * {@link RestResourceFactory#milestoneBacklog(int)}.
	 * 
	 * @throws CoreException
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testGetMilestonesBacklogIsPostForbidden() throws CoreException {
		RestResource r = factory.milestoneBacklog(123);
		r.post();
	}

	/**
	 * Checks that POST is not supported by the operation returned by
	 * {@link RestResourceFactory#milestoneContent(int)}.
	 * 
	 * @throws CoreException
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testGetMilestonesContentIsPostForbidden() throws CoreException {
		RestResource r = factory.milestoneContent(123);
		r.post();
	}

	/**
	 * Checks that PUT is supported by the operation returned by
	 * {@link RestResourceFactory#milestoneBacklog(int)}.
	 * 
	 * @throws CoreException
	 */
	@Test
	public void testGetMilestonesBacklogIsPutForbidden() throws CoreException {
		RestResource r = factory.milestoneBacklog(123);

		Map<String, String> headers = Maps.newTreeMap();
		headers.put(RestResource.ALLOW, "OPTIONS,PUT");
		headers.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));

		RestOperation put = r.put();
		assertNotNull(put);
		HttpMethod method = put.createMethod();
		assertEquals("PUT", method.getName());
		assertEquals("/api/v12.5/milestones/123/backlog", method.getPath());
	}

	/**
	 * Checks that PUT is supported by the operation returned by
	 * {@link RestResourceFactory#milestoneContent(int)}.
	 * 
	 * @throws CoreException
	 */
	@Test
	public void testGetMilestonesContentIsPutForbidden() throws CoreException {
		RestResource r = factory.milestoneContent(123);

		Map<String, String> headers = Maps.newTreeMap();
		headers.put(RestResource.ALLOW, "OPTIONS,PUT");
		headers.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));

		RestOperation put = r.put();
		HttpMethod method = put.createMethod();
		assertEquals("PUT", method.getName());
		assertEquals("/api/v12.5/milestones/123/content", method.getPath());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#milestoneCardwall(int)}.
	 */
	@Test
	public void testGetMilestonesCardwall() {
		RestResource r = factory.milestoneCardwall(123);
		assertNotNull(r);
		assertEquals("/api/v12.5/milestones/123/cardwall", r.getUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#milestoneSubmilestones(int)}.
	 */
	@Test
	public void testGetMilestonesSubmilestones() {
		RestResource r = factory.milestoneSubmilestones(123);
		assertNotNull(r);
		assertEquals("/api/v12.5/milestones/123/milestones", r.getUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#artifact(int)}.
	 */
	@Test
	public void testGetArtifactsById() {
		RestResource r = factory.artifact(123);
		assertNotNull(r);
		assertEquals("/api/v12.5/artifacts/123", r.getUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#artifacts()}.
	 */
	@Test
	public void testGetArtifacts() {
		RestResource r = factory.artifacts();
		assertNotNull(r);
		assertEquals("/api/v12.5/artifacts", r.getUrl());
	}

	/**
	 * Checks that GET is not supported by the operation returned by {@link RestResourceFactory#artifacts()}.
	 * 
	 * @throws CoreException
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testGetArtifactsForbidden() throws CoreException {
		RestResource r = factory.artifacts();
		r.get();
	}

	/**
	 * Checks that PUT is not supported by the operation returned by {@link RestResourceFactory#artifacts()}.
	 * 
	 * @throws CoreException
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testPutArtifactsForbidden() throws CoreException {
		RestResource r = factory.artifacts();
		r.put();
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#card(String)}.
	 */
	@Test
	public void testGetCardsById() {
		RestResource r = factory.card("2_123");
		assertNotNull(r);
		assertEquals("/api/v12.5/cards/2_123", r.getUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#projects()}.
	 */
	@Test
	public void testGetProjects() {
		RestResource r = factory.projects();
		assertNotNull(r);
		assertEquals("/api/v12.5/projects", r.getUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#projectsTrackers(int)}.
	 */
	@Test
	public void testGetProjectTrackers() {
		RestResource r = factory.projectsTrackers(321);
		assertNotNull(r);
		assertEquals("/api/v12.5/projects/321/trackers", r.getUrl());
	}

	/**
	 * Checks the basic properties of {@link RestResourceFactory#user()}.
	 */
	@Test
	public void testGetUser() {
		RestResource r = factory.user();
		assertNotNull(r);
		assertEquals("/api/v12.5/user", r.getUrl());
	}

	/**
	 * Set up the tests.
	 */
	@Before
	public void setUp() {
		connector = new MockRestConnector();
		gson = new Gson();
		factory = new RestResourceFactory("v12.5", connector, gson, new TestLogger());
	}
}
