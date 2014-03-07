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
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
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
import org.tuleap.mylyn.task.internal.core.model.config.TuleapServer;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapTracker;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;
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
		client.getUserGroupUsers("200", null);

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
	public void testRetrieveArtifactComments() throws CoreException, ParseException {
		String jsonTrackers = ParserUtil.loadFile("/changesets/changesets.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonTrackers, respHeaders);
		connector.setResponse(response);
		client.getArtifactComments(10, new TuleapServer(repository.getUrl()), null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("/api/v12.3/artifacts/10/changesets", request.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request.method); //$NON-NLS-1$

		request = requestsSent.get(1);
		assertEquals("/api/v12.3/artifacts/10/changesets", request.url); //$NON-NLS-1$
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

		String trackerJson = ParserUtil.loadFile("/trackers/tracker-0.json");
		ServerResponse trackersOptionsResponse = new ServerResponse(ServerResponse.STATUS_OK, "", respHeaders);
		ServerResponse trackersResponse = new ServerResponse(ServerResponse.STATUS_OK, trackerJson,
				respHeaders);

		listConnector.addServerResponse(response401Unauthorized);
		listConnector.addServerResponse(tokenOptionsResponse);
		listConnector.addServerResponse(tokenResponse);
		listConnector.addServerResponse(trackersOptionsResponse);
		listConnector.addServerResponse(trackersResponse);
		// Need to create a new client to use the specific connector.
		client = new TuleapRestClient(restResourceFactory, gson, repository);

		TuleapTracker tracker = client.getTracker(200, null);
		assertNotNull(tracker);
		List<ServerRequest> requestsSent = listConnector.requestsSent;
		assertEquals(5, requestsSent.size());

		ServerRequest req = requestsSent.get(0);
		assertEquals("OPTIONS", req.method);
		assertEquals("/api/v12.3/trackers/200", req.url);

		req = requestsSent.get(1);
		assertEquals("OPTIONS", req.method);
		assertEquals("/api/v12.3/tokens", req.url);

		req = requestsSent.get(2);
		assertEquals("POST", req.method);
		assertEquals("/api/v12.3/tokens", req.url);

		req = requestsSent.get(3);
		assertEquals("OPTIONS", req.method);
		assertEquals("/api/v12.3/trackers/200", req.url);

		req = requestsSent.get(4);
		assertEquals("GET", req.method);
		assertEquals("/api/v12.3/trackers/200", req.url);
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
}
