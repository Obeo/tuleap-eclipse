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
import org.junit.Ignore;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.client.rest.ITuleapHeaders;
import org.tuleap.mylyn.task.internal.core.client.rest.RestResourceFactory;
import org.tuleap.mylyn.task.internal.core.client.rest.ServerResponse;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.model.TuleapToken;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonParser;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.tests.TestLogger;
import org.tuleap.mylyn.task.internal.tests.client.rest.MockRestConnector.ServerRequest;
import org.tuleap.mylyn.task.internal.tests.parser.ParserUtil;
import org.tuleap.mylyn.task.internal.tests.parser.TuleapJsonParserTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

	private TuleapJsonParser jsonParser;

	private TaskRepository repository;

	private RestResourceFactory restResourceFactory;

	private TuleapRestClient client;

	@Test
	public void testRetrieveMilestoneWithoutCardwall() throws CoreException, ParseException {
		String jsonMilestone = ParserUtil.loadFile("/milestones/release200.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(ITuleapHeaders.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonMilestone, respHeaders);
		connector.setResponse(response);
		TuleapMilestone milestone = client.getMilestone(200, null);
		TuleapJsonParserTest.checkRelease200(milestone);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("https://test/url/api/v12.3/milestones/200", request.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request.method); //$NON-NLS-1$

		request = requestsSent.get(1);
		assertEquals("https://test/url/api/v12.3/milestones/200", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	@Test
	public void testRetrieveMilestoneBacklog() throws CoreException, ParseException {
		String jsonMilestone = ParserUtil.loadFile("/milestones/release200.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(ITuleapHeaders.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonMilestone, respHeaders);
		connector.setResponse(response);
		client.getMilestoneBacklog(200, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("https://test/url/api/v12.3/milestones/200/backlog", request.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request.method); //$NON-NLS-1$

		request = requestsSent.get(1);
		assertEquals("https://test/url/api/v12.3/milestones/200/backlog", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	@Test
	public void testRetrieveMilestoneContent() throws CoreException, ParseException {
		String jsonMilestone = ParserUtil.loadFile("/milestones/release200.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(ITuleapHeaders.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonMilestone, respHeaders);
		connector.setResponse(response);
		client.getMilestoneContent(200, null);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("https://test/url/api/v12.3/milestones/200/content", request.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request.method); //$NON-NLS-1$

		request = requestsSent.get(1);
		assertEquals("https://test/url/api/v12.3/milestones/200/content", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	@Test
	@Ignore("Fix me when cardwalls are back in the game")
	public void testRetrieveMilestoneWithCardwall() throws CoreException, ParseException {
		MockListRestConnector listConnector = new MockListRestConnector();
		restResourceFactory = new RestResourceFactory(serverUrl, apiVersion, listConnector, new TestLogger());
		listConnector.setResourceFactory(restResourceFactory);
		client = new TuleapRestClient(restResourceFactory, jsonParser, null, repository, null);

		String sprint250 = ParserUtil.loadFile("/milestones/sprint250.json"); //$NON-NLS-1$

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(ITuleapHeaders.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, sprint250, respHeaders);
		listConnector.addServerResponse(response).addServerResponse(response);

		String cardwall = ParserUtil.loadFile("/cardwalls/cw_sprint250.json"); //$NON-NLS-1$

		Map<String, String> respHeaders2 = Maps.newHashMap();
		respHeaders2.put(ITuleapHeaders.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders2.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response2 = new ServerResponse(ServerResponse.STATUS_OK, cardwall, respHeaders2);
		listConnector.addServerResponse(response2).addServerResponse(response2);

		TuleapMilestone milestone = client.getMilestone(250, null);
		assertNotNull(milestone);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = listConnector.getRequestsSent();
		assertEquals(4, requestsSent.size());

		ServerRequest request = requestsSent.get(0);
		assertEquals("https://test/url/api/v12.3/milestones/250", request.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request.method); //$NON-NLS-1$

		request = requestsSent.get(1);
		assertEquals("https://test/url/api/v12.3/milestones/250", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$

		request = requestsSent.get(2);
		assertEquals("https://test/url/api/v12.3/milestones/250/cardwall", request.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request.method); //$NON-NLS-1$

		request = requestsSent.get(3);
		assertEquals("https://test/url/api/v12.3/milestones/250/cardwall", request.url); //$NON-NLS-1$
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
		respHeaders.put(ITuleapHeaders.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, userStory, respHeaders);
		connector.setResponse(response);
		TuleapBacklogItem bi = client.getBacklogItem(350, null);
		TuleapJsonParserTest.checkUserStory350(bi);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());
		ServerRequest request0 = requestsSent.get(0);
		assertEquals("https://test/url/api/v12.3/backlog_items/350", request0.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request0.method); //$NON-NLS-1$

		ServerRequest request1 = requestsSent.get(1);
		assertEquals("https://test/url/api/v12.3/backlog_items/350", request1.url); //$NON-NLS-1$
		assertEquals("GET", request1.method); //$NON-NLS-1$
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
		item0.setInitialEffort(Float.valueOf(201));
		TuleapBacklogItem item1 = new TuleapBacklogItem(231,
				new TuleapReference(200, "p/200"), "item231", null, null, null, null); //$NON-NLS-1$
		item1.setInitialEffort(Float.valueOf(201));
		TuleapBacklogItem item2 = new TuleapBacklogItem(232,
				new TuleapReference(200, "p/200"), "item232", null, null, null, null); //$NON-NLS-1$
		item2.setInitialEffort(Float.valueOf(201));
		TuleapBacklogItem item3 = new TuleapBacklogItem(233,
				new TuleapReference(200, "p/200"), "item233", null, null, null, null); //$NON-NLS-1$
		item3.setInitialEffort(Float.valueOf(201));

		List<TuleapBacklogItem> backlog = new ArrayList<TuleapBacklogItem>();
		backlog.add(item0);
		backlog.add(item1);
		backlog.add(item2);
		backlog.add(item3);

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(ITuleapHeaders.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		respHeaders.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK,
				"The backlogItem response body", respHeaders); //$NON-NLS-1$

		connector.setResponse(response);
		client.updateMilestoneBacklog(50, backlog, new NullProgressMonitor());

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());

		ServerRequest request0 = requestsSent.get(0);
		assertEquals("https://test/url/api/v12.3/milestones/50/backlog", request0.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request0.method); //$NON-NLS-1$

		ServerRequest request1 = requestsSent.get(1);
		assertEquals("https://test/url/api/v12.3/milestones/50/backlog", request1.url); //$NON-NLS-1$
		assertEquals("PUT", request1.method); //$NON-NLS-1$
		assertEquals("[{\"id\":230},{\"id\":231},{\"id\":232},{\"id\":233}]", //$NON-NLS-1$
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
		item0.setInitialEffort(Float.valueOf(201));
		TuleapBacklogItem item1 = new TuleapBacklogItem(231,
				new TuleapReference(200, "p/200"), "item231", null, null, null, null); //$NON-NLS-1$
		item1.setInitialEffort(Float.valueOf(201));
		TuleapBacklogItem item2 = new TuleapBacklogItem(232,
				new TuleapReference(200, "p/200"), "item232", null, null, null, null); //$NON-NLS-1$
		item2.setInitialEffort(Float.valueOf(201));
		TuleapBacklogItem item3 = new TuleapBacklogItem(233,
				new TuleapReference(200, "p/200"), "item233", null, null, null, null); //$NON-NLS-1$
		item3.setInitialEffort(Float.valueOf(201));

		List<TuleapBacklogItem> content = new ArrayList<TuleapBacklogItem>();
		content.add(item0);
		content.add(item1);
		content.add(item2);
		content.add(item3);

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(ITuleapHeaders.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		respHeaders.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK,
				"The backlogItem response body", respHeaders); //$NON-NLS-1$

		connector.setResponse(response);
		client.updateMilestoneContent(50, content, new NullProgressMonitor());

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());

		ServerRequest request0 = requestsSent.get(0);
		assertEquals("https://test/url/api/v12.3/milestones/50/content", request0.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request0.method); //$NON-NLS-1$

		ServerRequest request1 = requestsSent.get(1);
		assertEquals("https://test/url/api/v12.3/milestones/50/content", request1.url); //$NON-NLS-1$
		assertEquals("PUT", request1.method); //$NON-NLS-1$
		assertEquals("[{\"id\":230},{\"id\":231},{\"id\":232},{\"id\":233}]", //$NON-NLS-1$
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
		respHeaders.put(ITuleapHeaders.ALLOW, "OPTIONS,POST"); //$NON-NLS-1$
		respHeaders.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,POST"); //$NON-NLS-1$

		String token = ParserUtil.loadFile("/tokens/token-0.json");
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, token, respHeaders);

		connector.setResponse(response);
		client.login();

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());

		ServerRequest request0 = requestsSent.get(0);
		assertEquals("https://test/url/api/v12.3/tokens", request0.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request0.method); //$NON-NLS-1$

		ServerRequest request1 = requestsSent.get(1);
		assertEquals("https://test/url/api/v12.3/tokens", request1.url); //$NON-NLS-1$
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
		this.restResourceFactory = new RestResourceFactory(serverUrl, apiVersion, listConnector,
				new TestLogger());
		this.repository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, serverUrl);
		this.repository.setCredentials(AuthenticationType.REPOSITORY, new AuthenticationCredentials("admin",
				"password"), true);
		listConnector.setResourceFactory(restResourceFactory);

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(ITuleapHeaders.ALLOW, "OPTIONS,GET,PUT,POST"); //$NON-NLS-1$
		respHeaders.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET,PUT,POST"); //$NON-NLS-1$

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
		client = new TuleapRestClient(restResourceFactory, jsonParser, null, repository, null);

		TuleapMilestone milestone = client.getMilestone(200, null);
		assertNotNull(milestone);
		List<ServerRequest> requestsSent = listConnector.requestsSent;
		assertEquals(5, requestsSent.size());

		ServerRequest req = requestsSent.get(0);
		assertEquals("OPTIONS", req.method);
		assertEquals("https://test/url/api/v12.3/milestones/200", req.url);

		req = requestsSent.get(1);
		assertEquals("OPTIONS", req.method);
		assertEquals("https://test/url/api/v12.3/tokens", req.url);

		req = requestsSent.get(2);
		assertEquals("POST", req.method);
		assertEquals("https://test/url/api/v12.3/tokens", req.url);

		req = requestsSent.get(3);
		assertEquals("OPTIONS", req.method);
		assertEquals("https://test/url/api/v12.3/milestones/200", req.url);

		req = requestsSent.get(4);
		assertEquals("GET", req.method);
		assertEquals("https://test/url/api/v12.3/milestones/200", req.url);
	}

	@Before
	public void setUp() {
		connector = new MockRestConnector();
		this.restResourceFactory = new RestResourceFactory(serverUrl, apiVersion, connector, new TestLogger());
		this.repository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, serverUrl);
		this.repository.setCredentials(AuthenticationType.REPOSITORY, new AuthenticationCredentials("admin",
				"password"), true);
		connector.setResourceFactory(restResourceFactory);
		jsonParser = new TuleapJsonParser();
		client = new TuleapRestClient(restResourceFactory, jsonParser, null, repository, null);
	}
}
