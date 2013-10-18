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
import com.google.gson.JsonElement;

import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.client.rest.ITuleapHeaders;
import org.tuleap.mylyn.task.internal.core.client.rest.ITuleapServerStatus;
import org.tuleap.mylyn.task.internal.core.client.rest.RestOpGet;
import org.tuleap.mylyn.task.internal.core.client.rest.ServerResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests of {@link RestOpGet} and other REST operations.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class RestOperationsTest {

	private MockRestConnector connector;

	@Test
	public void testBasicBehavior() {
		RestOpGet op = new RestOpGet("some/url", connector);
		assertEquals("GET", op.getMethodName());
		assertEquals("some/url", op.getUrl());
		assertEquals("some/url", op.getUrlWithQueryParameters());

		op.withQueryParameter("key1", "value1");
		assertEquals("some/url", op.getUrl());
		assertEquals("some/url?key1=value1", op.getUrlWithQueryParameters());

		op.withQueryParameter("key1", "value1b");
		assertEquals("some/url", op.getUrl());
		assertEquals("some/url?key1=value1b", op.getUrlWithQueryParameters());

		op.withQueryParameter("key1", "value1", "value1b");
		assertEquals("some/url", op.getUrl());
		assertEquals("some/url?key1=value1&key1=value1b", op.getUrlWithQueryParameters());

		op.withQueryParameter("key2", "value2");
		assertEquals("some/url", op.getUrl());
		assertEquals("some/url?key1=value1&key1=value1b&key2=value2", op.getUrlWithQueryParameters());

		op.withoutQueryParameters("key1");
		assertEquals("some/url", op.getUrl());
		assertEquals("some/url?key2=value2", op.getUrlWithQueryParameters());

		op.withoutQueryParameter();
		assertEquals("some/url", op.getUrl());
		assertEquals("some/url", op.getUrlWithQueryParameters());
	}

	/**
	 * Checks that the connector is actually called to execute an request and receive the response.
	 */
	@Test
	public void testExecutionOfGet() {
		RestOpGet op = new RestOpGet("some/url", connector);
		Map<String, String> responseHeaders = Maps.newLinkedHashMap();
		ServerResponse response = new ServerResponse(ITuleapServerStatus.OK, "body", responseHeaders);
		connector.setResponse(response);
		ServerResponse serverResponse = op.run();
		assertEquals(response, serverResponse);
	}

	@Test
	public void testIteratingOnGetWithJsonObject() {
		RestOpGet op = new RestOpGet("some/url", connector);
		Map<String, String> responseHeaders = Maps.newLinkedHashMap();
		ServerResponse response = new ServerResponse(ITuleapServerStatus.OK, "{'a':'1'}", responseHeaders);
		connector.setResponse(response);
		Iterator<JsonElement> iterator = op.iterator();
		assertEquals("{\"a\":\"1\"}", iterator.next().toString());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testIteratingOnGetWithArrayWithoutPagination() {
		RestOpGet op = new RestOpGet("some/url", connector);
		Map<String, String> responseHeaders = Maps.newLinkedHashMap();
		ServerResponse response = new ServerResponse(ITuleapServerStatus.OK, "[{'a':'1'},{'a':'2'}]",
				responseHeaders);
		connector.setResponse(response);
		Iterator<JsonElement> iterator = op.iterator();
		assertEquals("{\"a\":\"1\"}", iterator.next().toString());
		assertEquals("{\"a\":\"2\"}", iterator.next().toString());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testIteratingOnGetWithArrayWithPagination() {
		MockPaginatingRestConnector paginatingConnector = new MockPaginatingRestConnector();

		RestOpGet op = new RestOpGet("some/url", paginatingConnector);
		Map<String, String> responseHeaders = Maps.newLinkedHashMap();

		responseHeaders.put(ITuleapHeaders.HEADER_X_PAGINATION_SIZE, "3");
		responseHeaders.put(ITuleapHeaders.HEADER_X_PAGINATION_LIMIT, "2");
		responseHeaders.put(ITuleapHeaders.HEADER_X_PAGINATION_OFFSET, "0");
		ServerResponse response = new ServerResponse(ITuleapServerStatus.OK, "[{'a':'1'},{'a':'2'}]",
				responseHeaders);
		Map<String, String> responseHeaders2 = Maps.newLinkedHashMap();

		responseHeaders2.put(ITuleapHeaders.HEADER_X_PAGINATION_SIZE, "3");
		responseHeaders2.put(ITuleapHeaders.HEADER_X_PAGINATION_LIMIT, "2");
		responseHeaders2.put(ITuleapHeaders.HEADER_X_PAGINATION_OFFSET, "2");
		ServerResponse response2 = new ServerResponse(ITuleapServerStatus.OK, "[{'a':'3'}]", responseHeaders2);

		paginatingConnector.setResponse(response); // The default response
		paginatingConnector.putResponse(0, response);
		paginatingConnector.putResponse(2, response2);

		Iterator<JsonElement> iterator = op.iterator();
		assertTrue(iterator.hasNext());
		assertEquals("{\"a\":\"1\"}", iterator.next().toString());
		assertTrue(iterator.hasNext());
		assertEquals("{\"a\":\"2\"}", iterator.next().toString());
		assertTrue(iterator.hasNext());
		assertEquals("{\"a\":\"3\"}", iterator.next().toString());
		assertFalse(iterator.hasNext());
	}

	/**
	 * Set up the tests.
	 */
	@Before
	public void setUp() {
		connector = new MockRestConnector();
	}

}
