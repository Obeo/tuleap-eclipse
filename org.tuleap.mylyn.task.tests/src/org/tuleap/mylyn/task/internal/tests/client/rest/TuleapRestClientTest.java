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
import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.client.rest.RestResource;
import org.tuleap.mylyn.task.internal.core.client.rest.RestResourceFactory;
import org.tuleap.mylyn.task.internal.core.client.rest.ServerResponse;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.model.TuleapToken;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapPlanning;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapUser;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapFileUpload;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapMultiSelectBox;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapOpenList;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.data.ArtifactReference;
import org.tuleap.mylyn.task.internal.core.model.data.AttachmentFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.AttachmentValue;
import org.tuleap.mylyn.task.internal.core.model.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapCard;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapStatus;
import org.tuleap.mylyn.task.internal.core.parser.TuleapGsonProvider;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.tests.TestLogger;
import org.tuleap.mylyn.task.internal.tests.client.rest.MockRestConnector.ServerRequest;
import org.tuleap.mylyn.task.internal.tests.parser.ParserUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tests of {@link TuleapRestClient}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapRestClientTest {

	private final String serverUrl = "https://test/url"; //$NON-NLS-1$

	private String apiVersion = "v12.3"; //$NON-NLS-1$

	private MockRestConnector connector;

	private Gson gson;

	private TaskRepository repository;

	private RestResourceFactory restResourceFactory;

	private TuleapRestClient client;

	@Test
	public void testRetrieveMilestoneWithoutCardwall() throws CoreException, ParseException {
		String jsonMilestone = ParserUtil.loadFile("/milestones/release200.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonMilestone, respHeaders);
		connector.setResponse(response);
		TuleapMilestone milestone = client.getMilestone(200, null);
		checkRelease200(milestone);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/milestones/200", request.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request.method); //$NON-NLS-1$

		request = requestsSent.get(1);
		assertEquals("/api/v12.3/milestones/200", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	@Test
	public void testRetrieveMilestoneBacklog() throws CoreException, ParseException {
		String jsonMilestone = ParserUtil.loadFile("/milestones/release200.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonMilestone, respHeaders);
		connector.setResponse(response);
		client.getMilestoneBacklog(200, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/milestones/200/backlog", request.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request.method); //$NON-NLS-1$

		request = requestsSent.get(1);
		assertEquals("/api/v12.3/milestones/200/backlog", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	@Test
	public void testRetrieveMilestoneContent() throws CoreException, ParseException {
		String jsonMilestone = ParserUtil.loadFile("/milestones/release200.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonMilestone, respHeaders);
		connector.setResponse(response);
		client.getMilestoneContent(200, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/milestones/200/content", request.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request.method); //$NON-NLS-1$

		request = requestsSent.get(1);
		assertEquals("/api/v12.3/milestones/200/content", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	/**
	 * Test the retrieval of a backlog item.
	 * 
	 * @throws CoreException
	 *             The core exception to throw
	 * @throws ParseException
	 *             The parse exception to throw
	 */
	@Test
	public void testRetrieveBacklogItem() throws CoreException, ParseException {
		String userStory = ParserUtil.loadFile("/backlog_items/userStory350.json"); //$NON-NLS-1$
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, userStory, respHeaders);
		connector.setResponse(response);
		TuleapBacklogItem bi = client.getBacklogItem(350, null);
		checkUserStory350(bi);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());
		ServerRequest request0 = requestsSent.get(0);
		assertEquals("/api/v12.3/backlog_items/350", request0.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request0.method); //$NON-NLS-1$

		ServerRequest request1 = requestsSent.get(1);
		assertEquals("/api/v12.3/backlog_items/350", request1.url); //$NON-NLS-1$
		assertEquals("GET", request1.method); //$NON-NLS-1$
	}

	@Test
	public void testRetrieveUserGroups() throws CoreException, ParseException {
		String jsonMilestone = ParserUtil.loadFile("/groups/groups.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonMilestone, respHeaders);
		connector.setResponse(response);
		client.getProjectUserGroups(200, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/projects/200/user_groups", request.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request.method); //$NON-NLS-1$

		request = requestsSent.get(1);
		assertEquals("/api/v12.3/projects/200/user_groups", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	@Test
	public void testRetrieveUsers() throws CoreException, ParseException {
		String jsonMilestone = ParserUtil.loadFile("/users/project_admins.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonMilestone, respHeaders);
		connector.setResponse(response);
		client.getUserGroupUsers(200, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/user_groups/200/users", request.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request.method); //$NON-NLS-1$

		request = requestsSent.get(1);
		assertEquals("/api/v12.3/user_groups/200/users", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	@Test
	public void testRetrieveTrackerReports() throws CoreException, ParseException {
		String jsonMilestone = ParserUtil.loadFile("/tracker_reports/tracker_reports.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonMilestone, respHeaders);
		connector.setResponse(response);
		client.getTrackerReports(200, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/trackers/200/tracker_reports", request.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request.method); //$NON-NLS-1$

		request = requestsSent.get(1);
		assertEquals("/api/v12.3/trackers/200/tracker_reports", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	@Test
	public void testRetrieveProjects() throws CoreException, ParseException {
		String jsonMilestone = ParserUtil.loadFile("/projects/projects.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonMilestone, respHeaders);
		connector.setResponse(response);
		client.getProjects(null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/projects", request.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request.method); //$NON-NLS-1$

		request = requestsSent.get(1);
		assertEquals("/api/v12.3/projects", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	@Test
	public void testRetrieveProjectTrackers() throws CoreException, ParseException {
		String jsonTrackers = ParserUtil.loadFile("/trackers/trackers_part_1.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonTrackers, respHeaders);
		connector.setResponse(response);
		client.getProjectTrackers(101, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/projects/101/trackers", request.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request.method); //$NON-NLS-1$

		request = requestsSent.get(1);
		assertEquals("/api/v12.3/projects/101/trackers", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	@Test
	public void testRetrieveTrackerReportArtifacts() throws CoreException, ParseException {
		String jsonTrackers = ParserUtil.loadFile("/artifacts/artifacts.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonTrackers, respHeaders);
		connector.setResponse(response);
		client.getTrackerReportArtifacts(10, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/tracker_reports/10/artifacts", request.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request.method); //$NON-NLS-1$

		request = requestsSent.get(1);
		assertEquals("/api/v12.3/tracker_reports/10/artifacts", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	@Test
	public void testTracker() throws CoreException, ParseException {
		String jsonTracker = ParserUtil.loadFile("/trackers/tracker-5.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonTracker, respHeaders);
		connector.setResponse(response);
		client.getTracker(102, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/trackers/102", request.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request.method); //$NON-NLS-1$

		request = requestsSent.get(1);
		assertEquals("/api/v12.3/trackers/102", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	/**
	 * Test that a milestone backlog update is well done.
	 * 
	 * @throws CoreException
	 */
	@Test
	public void testUpdateMilestoneBacklog() throws CoreException {

		TuleapBacklogItem item0 = new TuleapBacklogItem(230,
				new TuleapReference(200, "p/200"), "item230", null, null, null, null); //$NON-NLS-1$
		item0.setInitialEffort("201");
		TuleapBacklogItem item1 = new TuleapBacklogItem(231,
				new TuleapReference(200, "p/200"), "item231", null, null, null, null); //$NON-NLS-1$
		item1.setInitialEffort("201");
		TuleapBacklogItem item2 = new TuleapBacklogItem(232,
				new TuleapReference(200, "p/200"), "item232", null, null, null, null); //$NON-NLS-1$
		item2.setInitialEffort("201");
		TuleapBacklogItem item3 = new TuleapBacklogItem(233,
				new TuleapReference(200, "p/200"), "item233", null, null, null, null); //$NON-NLS-1$
		item3.setInitialEffort("201");

		List<TuleapBacklogItem> backlog = new ArrayList<TuleapBacklogItem>();
		backlog.add(item0);
		backlog.add(item1);
		backlog.add(item2);
		backlog.add(item3);

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK,
				"The backlogItem response body", respHeaders); //$NON-NLS-1$

		connector.setResponse(response);
		client.updateMilestoneBacklog(50, backlog, new NullProgressMonitor());

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());

		ServerRequest request0 = requestsSent.get(0);
		assertEquals("/api/v12.3/milestones/50/backlog", request0.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request0.method); //$NON-NLS-1$

		ServerRequest request1 = requestsSent.get(1);
		assertEquals("/api/v12.3/milestones/50/backlog", request1.url); //$NON-NLS-1$
		assertEquals("PUT", request1.method); //$NON-NLS-1$
		assertEquals("[230,231,232,233]", //$NON-NLS-1$
				request1.body);
	}

	/**
	 * Test that a milestone content update is well done.
	 * 
	 * @throws CoreException
	 */
	@Test
	public void testUpdateMilestoneContent() throws CoreException {

		TuleapBacklogItem item0 = new TuleapBacklogItem(230,
				new TuleapReference(200, "p/200"), "item230", null, null, null, null); //$NON-NLS-1$
		item0.setInitialEffort("201");
		TuleapBacklogItem item1 = new TuleapBacklogItem(231,
				new TuleapReference(200, "p/200"), "item231", null, null, null, null); //$NON-NLS-1$
		item1.setInitialEffort("201");
		TuleapBacklogItem item2 = new TuleapBacklogItem(232,
				new TuleapReference(200, "p/200"), "item232", null, null, null, null); //$NON-NLS-1$
		item2.setInitialEffort("201");
		TuleapBacklogItem item3 = new TuleapBacklogItem(233,
				new TuleapReference(200, "p/200"), "item233", null, null, null, null); //$NON-NLS-1$
		item3.setInitialEffort("201");

		List<TuleapBacklogItem> content = new ArrayList<TuleapBacklogItem>();
		content.add(item0);
		content.add(item1);
		content.add(item2);
		content.add(item3);

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK,
				"The backlogItem response body", respHeaders); //$NON-NLS-1$

		connector.setResponse(response);
		client.updateMilestoneContent(50, content, new NullProgressMonitor());

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());

		ServerRequest request0 = requestsSent.get(0);
		assertEquals("/api/v12.3/milestones/50/content", request0.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request0.method); //$NON-NLS-1$

		ServerRequest request1 = requestsSent.get(1);
		assertEquals("/api/v12.3/milestones/50/content", request1.url); //$NON-NLS-1$
		assertEquals("PUT", request1.method); //$NON-NLS-1$
		assertEquals("[230,231,232,233]", //$NON-NLS-1$
				request1.body);
	}

	/**
	 * Test that the token creation on the server is well done.
	 * 
	 * @throws CoreException
	 */
	@Test
	public void testlogin() throws CoreException {

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,POST"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,POST"); //$NON-NLS-1$

		String token = ParserUtil.loadFile("/tokens/token-0.json");
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, token, respHeaders);

		connector.setResponse(response);
		client.login();

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());

		ServerRequest request0 = requestsSent.get(0);
		assertEquals("/api/v12.3/tokens", request0.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request0.method); //$NON-NLS-1$

		ServerRequest request1 = requestsSent.get(1);
		assertEquals("/api/v12.3/tokens", request1.url); //$NON-NLS-1$
		assertEquals("POST", request1.method); //$NON-NLS-1$
		assertEquals("{\"username\":\"admin\",\"password\":\"password\"}", //$NON-NLS-1$
				request1.body);

		TuleapToken token2 = client.getToken();
		assertEquals("Zza25pV0V5eXFhJDJ5JDE1JHpSbU1nMldoRUo1b21QNFQ4eEhDUC4xM0FybW9Ud3FwdmRUejA3R1B2WF",
				token2.getToken());
		assertEquals("539", token2.getUserId());
	}

	/**
	 * Test that checks that a TuleapRestClient that has no token (yet) will automatically login and retrieve
	 * a token, then re-run a request.
	 * 
	 * @throws CoreException
	 *             if something goes wrong.
	 */
	@Test
	public void testAutomaticLoginWhenNoTokenIsKnown() throws CoreException {
		MockListRestConnector listConnector = new MockListRestConnector();
		this.restResourceFactory = new RestResourceFactory(apiVersion, listConnector, gson, new TestLogger());
		this.repository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, serverUrl);
		this.repository.setCredentials(AuthenticationType.REPOSITORY, new AuthenticationCredentials("admin",
				"password"), true);
		listConnector.setResourceFactory(restResourceFactory);

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET,PUT,POST"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET,PUT,POST"); //$NON-NLS-1$

		ServerResponse response401Unauthorized = new ServerResponse(ServerResponse.STATUS_UNAUTHORIZED, "",
				respHeaders);

		String tokenJson = ParserUtil.loadFile("/tokens/token-0.json");
		ServerResponse tokenOptionsResponse = new ServerResponse(ServerResponse.STATUS_OK, "", respHeaders);
		ServerResponse tokenResponse = new ServerResponse(ServerResponse.STATUS_OK, tokenJson, respHeaders);

		String milestoneJson = ParserUtil.loadFile("/milestones/release200.json");
		ServerResponse milestoneOptionsResponse = new ServerResponse(ServerResponse.STATUS_OK, "",
				respHeaders);
		ServerResponse milestoneResponse = new ServerResponse(ServerResponse.STATUS_OK, milestoneJson,
				respHeaders);

		listConnector.addServerResponse(response401Unauthorized);
		listConnector.addServerResponse(tokenOptionsResponse);
		listConnector.addServerResponse(tokenResponse);
		listConnector.addServerResponse(milestoneOptionsResponse);
		listConnector.addServerResponse(milestoneResponse);
		// Need to create a new client to use the specific connector.
		client = new TuleapRestClient(restResourceFactory, gson, repository);

		TuleapMilestone milestone = client.getMilestone(200, null);
		assertNotNull(milestone);
		List<ServerRequest> requestsSent = listConnector.requestsSent;
		assertEquals(5, requestsSent.size());

		ServerRequest req = requestsSent.get(0);
		assertEquals("OPTIONS", req.method);
		assertEquals("/api/v12.3/milestones/200", req.url);

		req = requestsSent.get(1);
		assertEquals("OPTIONS", req.method);
		assertEquals("/api/v12.3/tokens", req.url);

		req = requestsSent.get(2);
		assertEquals("POST", req.method);
		assertEquals("/api/v12.3/tokens", req.url);

		req = requestsSent.get(3);
		assertEquals("OPTIONS", req.method);
		assertEquals("/api/v12.3/milestones/200", req.url);

		req = requestsSent.get(4);
		assertEquals("GET", req.method);
		assertEquals("/api/v12.3/milestones/200", req.url);
	}

	/**
	 * Test updating a cardwall with simple card.
	 * 
	 * @throws CoreException
	 */
	@Test
	public void testUpdateCardwallWithSimpleCard() throws CoreException {
		TuleapCard card = new TuleapCard("2_12345", new ArtifactReference(12345, "A/12345",
				new TuleapReference(700, "t/700")), new TuleapReference(200, "p/200"));
		card.setColumnId(10000);
		card.setLabel("Simple label");

		int[] columnIds = new int[3];
		for (int i = 0; i < 3; i++) {
			columnIds[i] = i + 10;
		}
		card.setAllowedColumnIds(columnIds);
		card.setStatus(TuleapStatus.valueOf("Open"));

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK,
				"The response body", respHeaders); //$NON-NLS-1$

		connector.setResponse(response);
		client.updateCard(card, new NullProgressMonitor());

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());

		ServerRequest request0 = requestsSent.get(0);
		assertEquals("/api/v12.3/cards/2_12345", request0.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request0.method); //$NON-NLS-1$

		ServerRequest request1 = requestsSent.get(1);
		assertEquals("/api/v12.3/cards/2_12345", request1.url); //$NON-NLS-1$
		assertEquals("PUT", request1.method); //$NON-NLS-1$
		assertEquals("{\"label\":\"Simple label\",\"values\":[],\"column_id\":10000}", //$NON-NLS-1$
				request1.body);
	}

	/**
	 * Test updating a cardwall with a card with literal field.
	 * 
	 * @throws CoreException
	 */
	@Test
	public void testUpdateCardwallWithLiteralFieldValue() throws CoreException {
		TuleapCard card = new TuleapCard("2_12345", new ArtifactReference(12345, "A/12345",
				new TuleapReference(700, "t/700")), new TuleapReference(200, "p/200"));
		card.setColumnId(10000);
		card.setLabel("Simple label");

		TuleapOpenList openList = new TuleapOpenList(1000);
		card.addField(openList);
		LiteralFieldValue firstLiteralFieldValue = new LiteralFieldValue(1000, "300, 301, 302"); //$NON-NLS-1$
		card.addFieldValue(firstLiteralFieldValue);
		int[] columnIds = new int[3];
		for (int i = 0; i < 3; i++) {
			columnIds[i] = i + 10;
		}
		card.setAllowedColumnIds(columnIds);
		card.setStatus(TuleapStatus.valueOf("Open"));

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK,
				"The response body", respHeaders); //$NON-NLS-1$

		connector.setResponse(response);
		client.updateCard(card, new NullProgressMonitor());

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());

		ServerRequest request0 = requestsSent.get(0);
		assertEquals("/api/v12.3/cards/2_12345", request0.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request0.method); //$NON-NLS-1$

		ServerRequest request1 = requestsSent.get(1);
		assertEquals("/api/v12.3/cards/2_12345", request1.url); //$NON-NLS-1$
		assertEquals("PUT", request1.method); //$NON-NLS-1$
		assertEquals(
				"{\"label\":\"Simple label\",\"values\":[{\"field_id\":1000,\"value\":\"300, 301, 302\"}],\"column_id\":10000}", //$NON-NLS-1$
				request1.body);
	}

	/**
	 * Test updating a cardwall with a card with bound field.
	 * 
	 * @throws CoreException
	 */
	@Test
	public void testUpdateCardwallWithBoundFieldValue() throws CoreException {
		TuleapCard card = new TuleapCard("2_12345", new ArtifactReference(12345, "A/12345",
				new TuleapReference(700, "t/700")), new TuleapReference(200, "p/200"));
		card.setColumnId(10000);
		card.setLabel("Simple label");

		int[] columnIds = new int[3];
		for (int i = 0; i < 3; i++) {
			columnIds[i] = i + 10;
		}
		card.setAllowedColumnIds(columnIds);
		card.setStatus(TuleapStatus.valueOf("Open"));

		List<Integer> valueIds = new ArrayList<Integer>();
		valueIds.add(new Integer(10));
		valueIds.add(new Integer(20));
		valueIds.add(new Integer(30));
		BoundFieldValue firstBoundFieldValue = new BoundFieldValue(2000, valueIds);

		TuleapMultiSelectBox msb = new TuleapMultiSelectBox(2000);
		msb.addItem(new TuleapSelectBoxItem(10));
		msb.addItem(new TuleapSelectBoxItem(20));
		msb.addItem(new TuleapSelectBoxItem(30));
		msb.addItem(new TuleapSelectBoxItem(40));
		card.addField(msb);
		card.addFieldValue(firstBoundFieldValue);

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK,
				"The response body", respHeaders); //$NON-NLS-1$

		connector.setResponse(response);
		client.updateCard(card, new NullProgressMonitor());

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());

		ServerRequest request0 = requestsSent.get(0);
		assertEquals("/api/v12.3/cards/2_12345", request0.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request0.method); //$NON-NLS-1$

		ServerRequest request1 = requestsSent.get(1);
		assertEquals("/api/v12.3/cards/2_12345", request1.url); //$NON-NLS-1$
		assertEquals("PUT", request1.method); //$NON-NLS-1$
		assertEquals(
				"{\"label\":\"Simple label\",\"values\":[{\"field_id\":2000,\"bind_value_ids\":[10,20,30]}],\"column_id\":10000}", //$NON-NLS-1$
				request1.body);
	}

	/**
	 * Test updating a cardwall with a card with bound field.
	 * 
	 * @throws CoreException
	 */
	@Test
	public void testUpdateCardwallWithFileDescription() throws CoreException {
		TuleapCard card = new TuleapCard("2_12345", new ArtifactReference(12345, "A/12345",
				new TuleapReference(700, "t/700")), new TuleapReference(200, "p/200"));
		card.setColumnId(10000);
		card.setLabel("Simple label");

		int[] columnIds = new int[3];
		for (int i = 0; i < 3; i++) {
			columnIds[i] = i + 10;
		}
		card.setAllowedColumnIds(columnIds);
		card.setStatus(TuleapStatus.valueOf("Open"));

		TuleapFileUpload uploadField = new TuleapFileUpload(3000);
		card.addField(uploadField);
		List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		TuleapUser firstUploadedBy = new TuleapUser(
				"first username", "first realname", 1, "first email", null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attachments.add(new AttachmentValue("100000", "first name", firstUploadedBy, 123456, //$NON-NLS-1$ //$NON-NLS-2$ 
				"first description", "first type")); //$NON-NLS-1$ //$NON-NLS-2$
		TuleapUser secondUploadedBy = new TuleapUser("second username", "second realname", 2, //$NON-NLS-1$ //$NON-NLS-2$
				"second email", null); //$NON-NLS-1$
		attachments.add(new AttachmentValue("100001", "second name", secondUploadedBy, 789456, //$NON-NLS-1$ //$NON-NLS-2$
				"second description", "second type")); //$NON-NLS-1$ //$NON-NLS-2$
		AttachmentFieldValue fileDescriptions = new AttachmentFieldValue(3000, attachments);

		card.addFieldValue(fileDescriptions);

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK,
				"The response body", respHeaders); //$NON-NLS-1$

		connector.setResponse(response);
		client.updateCard(card, new NullProgressMonitor());

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());

		ServerRequest request0 = requestsSent.get(0);
		assertEquals("/api/v12.3/cards/2_12345", request0.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request0.method); //$NON-NLS-1$

		ServerRequest request1 = requestsSent.get(1);
		assertEquals("/api/v12.3/cards/2_12345", request1.url); //$NON-NLS-1$
		assertEquals("PUT", request1.method); //$NON-NLS-1$
		assertEquals(
				"{\"label\":\"Simple label\",\"values\":[{\"field_id\":3000,\"file_descriptions\":[{\"file_id\":100000,\"description\":\"first description\"},{\"file_id\":100001,\"description\":\"second description\"}]}],\"column_id\":10000}", //$NON-NLS-1$
				request1.body);
	}

	/**
	 * Test that a milestone content update is well done.
	 * 
	 * @throws CoreException
	 */
	@Test
	public void testUpdateMilestoneSubmilestones() throws CoreException {

		TuleapReference projectRef = new TuleapReference(123, "p/123");
		TuleapMilestone milestone = new TuleapMilestone(200, projectRef);

		TuleapMilestone submilestone1 = new TuleapMilestone(201, projectRef);
		TuleapMilestone submilestone2 = new TuleapMilestone(202, projectRef);
		TuleapMilestone submilestone3 = new TuleapMilestone(203, projectRef);
		TuleapMilestone submilestone4 = new TuleapMilestone(204, projectRef);

		List<TuleapMilestone> submilestones = new ArrayList<TuleapMilestone>();
		submilestones.add(submilestone1);
		submilestones.add(submilestone2);
		submilestones.add(submilestone3);
		submilestones.add(submilestone4);

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK,
				"The milestone response body", respHeaders); //$NON-NLS-1$

		connector.setResponse(response);
		client.updateMilestoneSubmilestones(milestone.getId(), submilestones, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());

		ServerRequest request0 = requestsSent.get(0);
		assertEquals("/api/v12.3/milestones/200/milestones", request0.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request0.method); //$NON-NLS-1$

		ServerRequest request1 = requestsSent.get(1);
		assertEquals("/api/v12.3/milestones/200/milestones", request1.url); //$NON-NLS-1$
		assertEquals("PUT", request1.method); //$NON-NLS-1$
		assertEquals("[201,202,203,204]", //$NON-NLS-1$
				request1.body);
	}

	@Before
	public void setUp() {
		connector = new MockRestConnector();
		gson = TuleapGsonProvider.defaultGson();
		this.restResourceFactory = new RestResourceFactory(apiVersion, connector, gson, new TestLogger());
		this.repository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, serverUrl);
		this.repository.setCredentials(AuthenticationType.REPOSITORY, new AuthenticationCredentials("admin",
				"password"), true);
		connector.setResourceFactory(restResourceFactory);
		client = new TuleapRestClient(restResourceFactory, gson, repository);
	}

	/**
	 * Checks that the given backlog item corresponds to epic 301.
	 * 
	 * @param item
	 *            The backlog item
	 */
	public static void checkPlanning400(TuleapPlanning item) {
		assertEquals(400, item.getId().intValue());
		assertEquals(3, item.getProject().getId());
		assertEquals("projects/3", item.getProject().getUri());
		assertEquals("Releases Planning", item.getLabel());
		assertEquals("plannings/400", item.getUri());
		assertEquals(901, item.getMilestoneTracker().getId());
		assertEquals("trackers/901", item.getMilestoneTracker().getUri());
		TuleapReference[] trackers = item.getBacklogTrackers();
		assertEquals(1, trackers.length);
		TuleapReference trackerRef = trackers[0];
		assertEquals(801, trackerRef.getId());
		assertEquals("trackers/801", trackerRef.getUri());
		assertEquals("plannings/400/milestones", item.getMilestonesUri());
		assertNull(item.getCardwallConfigurationUri()); // Will have to change when cardwalls are activated
	}

	/**
	 * Checks that the given backlog item corresponds to epic 301.
	 * 
	 * @param item
	 *            The backlog item
	 */
	public static void checkPlanning401(TuleapPlanning item) {
		assertEquals(401, item.getId().intValue());
		assertEquals(3, item.getProject().getId());
		assertEquals("projects/3", item.getProject().getUri());
		assertEquals("Sprints Planning", item.getLabel());
		assertEquals("plannings/401", item.getUri());
		assertEquals(902, item.getMilestoneTracker().getId());
		assertEquals("trackers/902", item.getMilestoneTracker().getUri());
		TuleapReference[] trackers = item.getBacklogTrackers();
		assertEquals(1, trackers.length);
		TuleapReference trackerRef = trackers[0];
		assertEquals(802, trackerRef.getId());
		assertEquals("trackers/802", trackerRef.getUri());
		assertEquals("plannings/401/milestones", item.getMilestonesUri());
		assertNull(item.getCardwallConfigurationUri()); // Will have to change when cardwalls are activated
	}

	/**
	 * Checks that the given backlog item corresponds to epic 301.
	 * 
	 * @param item
	 *            The backlog item
	 * @throws ParseException
	 *             if the test is badly configured
	 */
	public static void checkEpic301(TuleapBacklogItem item) throws ParseException {
		assertEquals(301, item.getId().intValue());
		assertEquals(301, item.getArtifact().getId());
		assertEquals("artifacts/301", item.getArtifact().getUri());
		assertEquals(801, item.getArtifact().getTracker().getId());
		assertEquals("trackers/801", item.getArtifact().getTracker().getUri());
		assertEquals(302, item.getParent().getId());
		assertEquals("backlog_items/302", item.getParent().getUri());
		assertEquals(801, item.getParent().getTracker().getId());
		assertEquals("trackers/801", item.getParent().getTracker().getUri());
		assertEquals(3, item.getProject().getId());
		assertEquals("projects/3", item.getProject().getUri());
		assertEquals("Another important Epic", item.getLabel());
		assertEquals("backlog_items/301", item.getUri());
		assertEquals("backlog_items?id=301&group_id=3", item.getHtmlUrl());
		assertEquals(1, item.getSubmittedBy());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 23, 11, 44, 18, 963), item.getSubmittedOn());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 24, 15, 33, 18, 523), item.getLastModifiedDate());
		assertEquals("40.5", item.getInitialEffort());
		assertEquals(TuleapStatus.Open, item.getStatus());
		assertEquals("Epics", item.getType());
	}

	/**
	 * Checks that the given backlog item corresponds to epic 304.
	 * 
	 * @param item
	 *            The backlog item
	 * @throws ParseException
	 *             if the test is badly configured
	 */
	public static void checkEpic304(TuleapBacklogItem item) throws ParseException {
		assertEquals(304, item.getId().intValue());
		assertEquals(304, item.getArtifact().getId());
		assertEquals("artifacts/304", item.getArtifact().getUri());
		assertEquals(801, item.getArtifact().getTracker().getId());
		assertEquals("trackers/801", item.getArtifact().getTracker().getUri());
		assertEquals(305, item.getParent().getId());
		assertEquals("backlog_items/305", item.getParent().getUri());
		assertEquals(801, item.getParent().getTracker().getId());
		assertEquals("trackers/801", item.getParent().getTracker().getUri());
		assertEquals(3, item.getProject().getId());
		assertEquals("projects/3", item.getProject().getUri());
		assertEquals("Another important Epic", item.getLabel());
		assertEquals("backlog_items/301", item.getUri());
		assertEquals("backlog_items?id=301&group_id=3", item.getHtmlUrl());
		assertEquals(1, item.getSubmittedBy());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 23, 11, 44, 18, 963), item.getSubmittedOn());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 24, 15, 33, 18, 523), item.getLastModifiedDate());
		assertNull(item.getInitialEffort());
		assertEquals(TuleapStatus.Open, item.getStatus());
	}

	/**
	 * Checks that the given backlog item corresponds to epic 300.
	 * 
	 * @param item
	 *            The backlog item The backlog item
	 * @throws ParseException
	 *             if the test is badly configured
	 */
	public static void checkEpic300(TuleapBacklogItem item) throws ParseException {
		assertEquals(300, item.getId().intValue());
		assertEquals(300, item.getArtifact().getId());
		assertEquals("artifacts/300", item.getArtifact().getUri());
		assertEquals(801, item.getArtifact().getTracker().getId());
		assertEquals("trackers/801", item.getArtifact().getTracker().getUri());
		assertEquals(301, item.getParent().getId());
		assertEquals("backlog_items/301", item.getParent().getUri());
		assertEquals(801, item.getParent().getTracker().getId());
		assertEquals("trackers/801", item.getParent().getTracker().getUri());
		assertEquals(3, item.getProject().getId());
		assertEquals("projects/3", item.getProject().getUri());
		assertEquals("An important Epic", item.getLabel());
		assertEquals("backlog_items/300", item.getUri());
		assertEquals("backlog_items?id=300&group_id=3", item.getHtmlUrl());
		assertEquals(1, item.getSubmittedBy());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 23, 11, 44, 18, 963), item.getSubmittedOn());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 24, 15, 33, 18, 523), item.getLastModifiedDate());
		assertEquals("30", item.getInitialEffort());
		assertEquals(TuleapStatus.Closed, item.getStatus());
		assertEquals("Epics", item.getType());
	}

	/**
	 * Checks that the given backlog item corresponds to user story 350.
	 * 
	 * @param item
	 *            The backlog item
	 * @throws ParseException
	 *             if the test is badly configured
	 */
	public static void checkUserStory350(TuleapBacklogItem item) throws ParseException {
		assertEquals(350, item.getId().intValue());
		assertEquals(350, item.getArtifact().getId());
		assertEquals("artifacts/350", item.getArtifact().getUri());
		assertEquals(802, item.getArtifact().getTracker().getId());
		assertEquals("trackers/802", item.getArtifact().getTracker().getUri());
		assertEquals(351, item.getParent().getId());
		assertEquals("backlog_items/351", item.getParent().getUri());
		assertEquals(802, item.getParent().getTracker().getId());
		assertEquals("trackers/802", item.getParent().getTracker().getUri());
		assertEquals(3, item.getProject().getId());
		assertEquals("projects/3", item.getProject().getUri());
		assertEquals("An important User Story", item.getLabel());
		assertEquals("backlog_items/350", item.getUri());
		assertEquals("backlog_items?id=350&group_id=3", item.getHtmlUrl());
		assertEquals(1, item.getSubmittedBy());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 23, 11, 44, 18, 963), item.getSubmittedOn());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 24, 15, 33, 18, 523), item.getLastModifiedDate());
		assertEquals("5", item.getInitialEffort());
		assertEquals(TuleapStatus.Open, item.getStatus());
		assertEquals("User stories", item.getType());
	}

	/**
	 * Checks the content of the given milestone corresponds to release 200. Mutualized between several tests.
	 * 
	 * @param tuleapMilestone
	 * @throws ParseException
	 */
	public static void checkRelease200(TuleapMilestone tuleapMilestone) throws ParseException {
		assertEquals(200, tuleapMilestone.getId().intValue());
		assertEquals(200, tuleapMilestone.getArtifact().getId());
		assertEquals("artifacts/200", tuleapMilestone.getArtifact().getUri());
		assertEquals(901, tuleapMilestone.getArtifact().getTracker().getId());
		assertEquals("trackers/901", tuleapMilestone.getArtifact().getTracker().getUri());
		assertEquals(201, tuleapMilestone.getParent().getId());
		assertEquals("milestones/201", tuleapMilestone.getParent().getUri());
		assertEquals(901, tuleapMilestone.getParent().getTracker().getId());
		assertEquals("trackers/901", tuleapMilestone.getParent().getTracker().getUri());
		assertEquals(3, tuleapMilestone.getProject().getId());
		assertEquals("projects/3", tuleapMilestone.getProject().getUri());
		assertEquals("Release 0.9", tuleapMilestone.getLabel()); //$NON-NLS-1$
		assertEquals("milestones/200", tuleapMilestone.getUri()); //$NON-NLS-1$
		assertNull(tuleapMilestone.getHtmlUrl());
		assertEquals(1, tuleapMilestone.getSubmittedBy());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 23, 11, 44, 18, 963), tuleapMilestone.getSubmittedOn());
		assertNull(tuleapMilestone.getLastModifiedDate());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 23, 11, 44, 18, 963), tuleapMilestone.getStartDate());
		assertEquals(ParserUtil.getUTCDate(2013, 9, 23, 11, 44, 18, 963), tuleapMilestone.getEndDate());
		assertEquals("100", tuleapMilestone.getCapacity());
		assertEquals("Done", tuleapMilestone.getStatusValue());
		assertEquals("milestones/200/milestones", tuleapMilestone.getSubMilestonesUri());
		assertEquals("milestones/200/backlog", tuleapMilestone.getBacklogUri());
		assertEquals("milestones/200/content", tuleapMilestone.getContentUri());
	}

	/**
	 * Checks the content of the given milestone corresponds to release 201. Mutualized between several test
	 * cases.
	 * 
	 * @param tuleapMilestone
	 * @throws ParseException
	 */
	public static void checkRelease201(TuleapMilestone tuleapMilestone) throws ParseException {
		assertNotNull(tuleapMilestone);

		assertEquals(201, tuleapMilestone.getId().intValue());
		assertEquals(201, tuleapMilestone.getArtifact().getId());
		assertEquals("artifacts/201", tuleapMilestone.getArtifact().getUri());
		assertEquals(901, tuleapMilestone.getArtifact().getTracker().getId());
		assertEquals("trackers/901", tuleapMilestone.getArtifact().getTracker().getUri());
		assertEquals(202, tuleapMilestone.getParent().getId());
		assertEquals("milestones/202", tuleapMilestone.getParent().getUri());
		assertEquals(901, tuleapMilestone.getParent().getTracker().getId());
		assertEquals("trackers/901", tuleapMilestone.getParent().getTracker().getUri());
		assertEquals(3, tuleapMilestone.getProject().getId());
		assertEquals("projects/3", tuleapMilestone.getProject().getUri());
		assertEquals("Release TU", tuleapMilestone.getLabel()); //$NON-NLS-1$
		assertNull(tuleapMilestone.getLastModifiedDate());
		assertEquals(ParserUtil.getUTCDate(2013, 9, 23, 11, 44, 18, 963), tuleapMilestone.getStartDate());
		assertEquals(ParserUtil.getUTCDate(2013, 10, 23, 11, 44, 18, 963), tuleapMilestone.getEndDate());
		assertEquals("75", tuleapMilestone.getCapacity());
		assertEquals("milestones/201", tuleapMilestone.getUri()); //$NON-NLS-1$
		assertNull(tuleapMilestone.getHtmlUrl());
		assertEquals("Current", tuleapMilestone.getStatusValue());
		assertEquals("milestones/201/milestones", tuleapMilestone.getSubMilestonesUri());
		assertEquals("milestones/201/backlog", tuleapMilestone.getBacklogUri());
		assertEquals("milestones/201/content", tuleapMilestone.getContentUri());
	}
}
