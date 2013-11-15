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
import java.util.Date;
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
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapTopPlanning;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonParser;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.tests.client.rest.MockRestConnector.ServerRequest;
import org.tuleap.mylyn.task.internal.tests.parser.ParserUtil;
import org.tuleap.mylyn.task.internal.tests.parser.TuleapBacklogItemDeserializerTest;
import org.tuleap.mylyn.task.internal.tests.parser.TuleapMilestoneDeserializerTests;
import org.tuleap.mylyn.task.internal.tests.parser.TuleapTopPlanningDeserializerTests;

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
		new TuleapMilestoneDeserializerTests().checkRelease200(milestone);

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
	@Ignore("Fix me when cardwalls are back in the game")
	public void testRetrieveMilestoneWithCardwall() throws CoreException, ParseException {
		MockListRestConnector listConnector = new MockListRestConnector();
		restResourceFactory = new RestResourceFactory(serverUrl, apiVersion, listConnector);
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
		TuleapBacklogItemDeserializerTest.checkUserStory350(bi);

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

	@Test
	@Ignore("Fix me when top plannings are back")
	public void testRetrieveTopPlanning() throws CoreException, ParseException {
		MockListRestConnector listConnector = new MockListRestConnector();
		restResourceFactory = new RestResourceFactory(serverUrl, apiVersion, listConnector);
		listConnector.setResourceFactory(restResourceFactory);
		client = new TuleapRestClient(restResourceFactory, jsonParser, null, repository, null);

		String tp30 = ParserUtil.loadFile("/top_plannings/top_planning_30.json"); //$NON-NLS-1$

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(ITuleapHeaders.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, tp30, respHeaders);
		listConnector.addServerResponse(response).addServerResponse(response);

		String milestones = ParserUtil.loadFile("/top_plannings/milestones.json"); //$NON-NLS-1$

		Map<String, String> respHeaders2 = Maps.newHashMap();
		respHeaders2.put(ITuleapHeaders.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders2.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response2 = new ServerResponse(ServerResponse.STATUS_OK, milestones, respHeaders2);
		listConnector.addServerResponse(response2).addServerResponse(response2);

		String items = ParserUtil.loadFile("/top_plannings/backlog_items.json"); //$NON-NLS-1$

		Map<String, String> respHeaders3 = Maps.newHashMap();
		respHeaders3.put(ITuleapHeaders.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		respHeaders3.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		ServerResponse response3 = new ServerResponse(ServerResponse.STATUS_OK, items, respHeaders3);
		listConnector.addServerResponse(response3).addServerResponse(response3);

		TuleapTopPlanning tp = client.getTopPlanning(30, null);
		new TuleapTopPlanningDeserializerTests().checkTopPlanning30(tp);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = listConnector.getRequestsSent();
		assertEquals(6, requestsSent.size());

		ServerRequest request = requestsSent.get(0);
		assertEquals("https://test/url/api/v12.3/top_plannings/30", request.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request.method); //$NON-NLS-1$

		request = requestsSent.get(1);
		assertEquals("https://test/url/api/v12.3/top_plannings/30", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$

		request = requestsSent.get(2);
		assertEquals("https://test/url/api/v12.3/top_plannings/30/milestones", request.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request.method); //$NON-NLS-1$

		request = requestsSent.get(3);
		assertEquals("https://test/url/api/v12.3/top_plannings/30/milestones", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$

		request = requestsSent.get(4);
		assertEquals("https://test/url/api/v12.3/top_plannings/30/backlog_items", request.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request.method); //$NON-NLS-1$

		request = requestsSent.get(5);
		assertEquals("https://test/url/api/v12.3/top_plannings/30/backlog_items", request.url); //$NON-NLS-1$
		assertEquals("GET", request.method); //$NON-NLS-1$
	}

	/**
	 * Test that the milestone update is well done.
	 * 
	 * @throws CoreException
	 */
	@Test
	@Ignore
	public void testUpdateMilestone() throws CoreException {

		MockListRestConnector listConnector = new MockListRestConnector();
		restResourceFactory = new RestResourceFactory(serverUrl, apiVersion, listConnector);
		listConnector.setResourceFactory(restResourceFactory);
		client = new TuleapRestClient(restResourceFactory, jsonParser, null, repository, null);

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(ITuleapHeaders.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		respHeaders.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, "BacklogItems", respHeaders); //$NON-NLS-1$
		listConnector.addServerResponse(response).addServerResponse(response);

		Map<String, String> respHeaders2 = Maps.newHashMap();
		respHeaders2.put(ITuleapHeaders.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		respHeaders2.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		ServerResponse response2 = new ServerResponse(ServerResponse.STATUS_OK, "Fields", respHeaders2); //$NON-NLS-1$
		listConnector.addServerResponse(response2).addServerResponse(response2);

		Map<String, String> respHeaders3 = Maps.newHashMap();
		respHeaders3.put(ITuleapHeaders.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		respHeaders3.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		ServerResponse response3 = new ServerResponse(ServerResponse.STATUS_OK, "Cards", respHeaders3); //$NON-NLS-1$
		listConnector.addServerResponse(response3).addServerResponse(response3);

		TuleapMilestone tuleapMilestone = this.initializeMilestone();
		// FIXME backlog items ! client.updateMilestone(tuleapMilestone, new NullProgressMonitor());

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = listConnector.getRequestsSent();
		assertEquals(6, requestsSent.size());

		// The milestone backlogItems
		ServerRequest request0 = requestsSent.get(0);
		assertEquals("https://test/url/api/v12.3/milestones/50/backlog_items", request0.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request0.method); //$NON-NLS-1$

		ServerRequest request1 = requestsSent.get(1);
		assertEquals("https://test/url/api/v12.3/milestones/50/backlog_items", request1.url); //$NON-NLS-1$
		assertEquals("PUT", request1.method); //$NON-NLS-1$
		assertEquals("[{\"id\":200,\"assigned_milestone_id\":100}]", request1.body); //$NON-NLS-1$

		// The milestone fields
		ServerRequest request2 = requestsSent.get(2);
		assertEquals("https://test/url/api/v12.3/milestones/50", request2.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request2.method); //$NON-NLS-1$

		ServerRequest request3 = requestsSent.get(3);
		assertEquals("https://test/url/api/v12.3/milestones/50", request3.url); //$NON-NLS-1$
		assertEquals("PUT", request1.method); //$NON-NLS-1$
		assertEquals(
				"{\"values\":[{\"field_id\":1000,\"value\":\"300, 301, 302\"},{\"field_id\":2000,\"bind_value_ids\":[1,2,3]},{\"field_id\":3000,\"file_descriptions\":[{\"file_id\":100000,\"description\":\"first description\"},{\"file_id\":100001,\"description\":\"second description\"}]}],\"milestone_type_id\":500,\"parent_milestone_id\":0,\"id\":50,\"label\":\"The first milestone\",\"configuration_id\":500}", //$NON-NLS-1$
				request3.body);

		// The milestone cards
		ServerRequest request4 = requestsSent.get(4);
		assertEquals("https://test/url/api/v12.3/cards/3001", request4.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request4.method); //$NON-NLS-1$

		ServerRequest request5 = requestsSent.get(5);
		assertEquals("https://test/url/api/v12.3/cards/3001", request5.url); //$NON-NLS-1$
		assertEquals("PUT", request5.method); //$NON-NLS-1$
		assertEquals("{\"id\":3001,\"configuration_id\":700}", request5.body); //$NON-NLS-1$
	}

	/**
	 * Test that the backlogItem update is well done.
	 * 
	 * @throws CoreException
	 */
	@Test
	@Ignore("Need to see whether it's necessary to update a BI, artifact should be enough")
	public void testUpdateBacklogItem() throws CoreException {

		// the backlogItem
		TuleapBacklogItem item = new TuleapBacklogItem(231,
				new TuleapReference(200, "p/200"), "item", null, null, null, null); //$NON-NLS-1$

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(ITuleapHeaders.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		respHeaders.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK,
				"The backlogItem response body", respHeaders); //$NON-NLS-1$

		connector.setResponse(response);
		client.updateBacklogItem(item, new NullProgressMonitor());

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());

		ServerRequest request0 = requestsSent.get(0);
		assertEquals("https://test/url/api/v12.3/backlog_items/231", request0.url); //$NON-NLS-1$
		assertEquals("OPTIONS", request0.method); //$NON-NLS-1$

		ServerRequest request1 = requestsSent.get(1);
		assertEquals("https://test/url/api/v12.3/backlog_items/231", request1.url); //$NON-NLS-1$
		assertEquals("PUT", request1.method); //$NON-NLS-1$
		assertEquals(
				"{\"values\":[{\"field_id\":1000,\"value\":\"300, 301, 302\"},{\"field_id\":2000,\"bind_value_ids\":[1,2,3]},{}],\"id\":231,\"label\":\"item\",\"configuration_id\":1000}", //$NON-NLS-1$
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
		client.login(new NullProgressMonitor());

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

		// TODO: Test the token received from the server when we can access its value
	}

	/**
	 * Initialize the milestone to update.
	 * 
	 * @return the milestone to update
	 */
	private TuleapMilestone initializeMilestone() {
		TuleapMilestone tuleapMilestone = new TuleapMilestone(50,
				new TuleapReference(200, "p/200"), "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", new Date(), new Date()); //$NON-NLS-1$
		return tuleapMilestone;
	}

	@Before
	public void setUp() {
		connector = new MockRestConnector();
		this.restResourceFactory = new RestResourceFactory(serverUrl, apiVersion, connector);
		this.repository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, serverUrl);
		this.repository.setCredentials(AuthenticationType.REPOSITORY, new AuthenticationCredentials("admin",
				"password"), true);
		connector.setResourceFactory(restResourceFactory);
		jsonParser = new TuleapJsonParser();
		client = new TuleapRestClient(restResourceFactory, jsonParser, null, repository, null);
	}
}
