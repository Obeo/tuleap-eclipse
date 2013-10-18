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
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.client.rest.ITuleapHeaders;
import org.tuleap.mylyn.task.internal.core.client.rest.RestResourceFactory;
import org.tuleap.mylyn.task.internal.core.client.rest.ServerResponse;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapTopPlanning;
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
 */
public class TuleapRestClientTest {

	private final String serverUrl = "https://test/url"; //$NON-NLS-1$

	private final String repositoryDescription = "Tracker Description"; //$NON-NLS-1$

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
		respHeaders.put(ITuleapHeaders.ALLOW, "OPTIONS,GET");
		respHeaders.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET");
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, jsonMilestone, respHeaders);
		connector.setResponse(response);
		TuleapMilestone milestone = client.getMilestone(200, false, null);
		new TuleapMilestoneDeserializerTests().checkRelease200(milestone);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());
		ServerRequest request = requestsSent.get(0);
		assertEquals("https://test/url/api/v12.3/milestones/200", request.url);
		assertEquals("OPTIONS", request.method);

		request = requestsSent.get(1);
		assertEquals("https://test/url/api/v12.3/milestones/200", request.url);
		assertEquals("GET", request.method);
	}

	@Test
	public void testRetrieveMilestoneWithCardwall() throws CoreException, ParseException {
		MockListRestConnector listConnector = new MockListRestConnector();
		restResourceFactory = new RestResourceFactory(serverUrl, apiVersion, listConnector);
		listConnector.setResourceFactory(restResourceFactory);
		client = new TuleapRestClient(listConnector, jsonParser, null, repository, null);

		String sprint250 = ParserUtil.loadFile("/milestones/sprint250.json");

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(ITuleapHeaders.ALLOW, "OPTIONS,GET");
		respHeaders.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET");
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, sprint250, respHeaders);
		listConnector.addServerResponse(response).addServerResponse(response);

		String cardwall = ParserUtil.loadFile("/cardwalls/cw_sprint250.json");

		Map<String, String> respHeaders2 = Maps.newHashMap();
		respHeaders2.put(ITuleapHeaders.ALLOW, "OPTIONS,GET");
		respHeaders2.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET");
		ServerResponse response2 = new ServerResponse(ServerResponse.STATUS_OK, cardwall, respHeaders2);
		listConnector.addServerResponse(response2).addServerResponse(response2);

		TuleapMilestone milestone = client.getMilestone(250, true, null);
		assertNotNull(milestone);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = listConnector.getRequestsSent();
		assertEquals(4, requestsSent.size());

		ServerRequest request = requestsSent.get(0);
		assertEquals("https://test/url/api/v12.3/milestones/250", request.url);
		assertEquals("OPTIONS", request.method);

		request = requestsSent.get(1);
		assertEquals("https://test/url/api/v12.3/milestones/250", request.url);
		assertEquals("GET", request.method);

		request = requestsSent.get(2);
		assertEquals("https://test/url/api/v12.3/milestones/250/cardwall", request.url);
		assertEquals("OPTIONS", request.method);

		request = requestsSent.get(3);
		assertEquals("https://test/url/api/v12.3/milestones/250/cardwall", request.url);
		assertEquals("GET", request.method);
	}

	@Test
	@Ignore("Redo when getbacklogItem is implemented right")
	public void testRetrieveBacklogItem() throws CoreException, ParseException {
		String userStory = ParserUtil.loadFile("/backlog_items/userStory350.json");
		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(ITuleapHeaders.ALLOW, "OPTIONS,GET");
		respHeaders.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET");
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, userStory, respHeaders);
		connector.setResponse(response);
		TuleapBacklogItem bi = client.getBacklogItem(350, null);
		TuleapBacklogItemDeserializerTest.checkUserStory350(bi);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = connector.getRequestsSent();
		assertEquals(2, requestsSent.size());
		ServerRequest request0 = requestsSent.get(0);
		assertEquals("https://test/url/api/v12.3/backlog_items/350", request0.url);
		assertEquals("OPTIONS", request0.method);

		ServerRequest request1 = requestsSent.get(1);
		assertEquals("https://test/url/api/v12.3/backlog_items/350", request1.url);
		assertEquals("GET", request1.method);
	}

	@Test
	public void testRetrieveTopPlanning() throws CoreException, ParseException {
		MockListRestConnector listConnector = new MockListRestConnector();
		restResourceFactory = new RestResourceFactory(serverUrl, apiVersion, listConnector);
		listConnector.setResourceFactory(restResourceFactory);
		client = new TuleapRestClient(listConnector, jsonParser, null, repository, null);

		String tp30 = ParserUtil.loadFile("/top_plannings/top_planning_30.json");

		Map<String, String> respHeaders = Maps.newHashMap();
		respHeaders.put(ITuleapHeaders.ALLOW, "OPTIONS,GET");
		respHeaders.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET");
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, tp30, respHeaders);
		listConnector.addServerResponse(response).addServerResponse(response);

		String milestones = ParserUtil.loadFile("/top_plannings/milestones.json");

		Map<String, String> respHeaders2 = Maps.newHashMap();
		respHeaders2.put(ITuleapHeaders.ALLOW, "OPTIONS,GET");
		respHeaders2.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET");
		ServerResponse response2 = new ServerResponse(ServerResponse.STATUS_OK, milestones, respHeaders2);
		listConnector.addServerResponse(response2).addServerResponse(response2);

		String items = ParserUtil.loadFile("/top_plannings/backlog_items.json");

		Map<String, String> respHeaders3 = Maps.newHashMap();
		respHeaders3.put(ITuleapHeaders.ALLOW, "OPTIONS,GET");
		respHeaders3.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET");
		ServerResponse response3 = new ServerResponse(ServerResponse.STATUS_OK, items, respHeaders3);
		listConnector.addServerResponse(response2).addServerResponse(response3);

		TuleapTopPlanning tp = client.getTopPlanning(30, null);
		new TuleapTopPlanningDeserializerTests().checkTopPlanning30(tp);

		// Let's check the requests that have been sent.
		List<ServerRequest> requestsSent = listConnector.getRequestsSent();
		assertEquals(6, requestsSent.size());

		ServerRequest request = requestsSent.get(0);
		assertEquals("https://test/url/api/v12.3/top_plannings/30", request.url);
		assertEquals("OPTIONS", request.method);

		request = requestsSent.get(1);
		assertEquals("https://test/url/api/v12.3/top_plannings/30", request.url);
		assertEquals("GET", request.method);

		request = requestsSent.get(2);
		assertEquals("https://test/url/api/v12.3/top_plannings/30/milestones", request.url);
		assertEquals("OPTIONS", request.method);

		request = requestsSent.get(3);
		assertEquals("https://test/url/api/v12.3/top_plannings/30/milestones", request.url);
		assertEquals("GET", request.method);

		request = requestsSent.get(4);
		assertEquals("https://test/url/api/v12.3/top_plannings/30/backlog_items", request.url);
		assertEquals("OPTIONS", request.method);

		request = requestsSent.get(5);
		assertEquals("https://test/url/api/v12.3/top_plannings/30/backlog_items", request.url);
		assertEquals("GET", request.method);
	}

	@Before
	public void setUp() {
		connector = new MockRestConnector();
		this.restResourceFactory = new RestResourceFactory(serverUrl, apiVersion, connector);
		this.repository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, serverUrl);

		connector.setResourceFactory(restResourceFactory);
		jsonParser = new TuleapJsonParser();
		client = new TuleapRestClient(connector, jsonParser, null, repository, null);
	}
}
