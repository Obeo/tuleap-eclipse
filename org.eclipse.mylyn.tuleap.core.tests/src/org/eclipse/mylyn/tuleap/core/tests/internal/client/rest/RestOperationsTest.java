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
import com.google.gson.JsonElement;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpMethod;
import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.IAuthenticator;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.RestOperation;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.RestOperationIterable;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.RestResource;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.ServerResponse;
import org.eclipse.mylyn.tuleap.core.internal.model.TuleapToken;
import org.eclipse.mylyn.tuleap.core.tests.internal.TestLogger;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests of {@link RestOperation}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class RestOperationsTest {

	private MockRestConnector connector;

	private ServerResponse response401;

	private TestLogger logger;

	private Gson gson;

	@Test
	public void testBasicBehavior() {
		RestOperation op = RestOperation.get("some/url", connector, gson, logger);
		HttpMethod m = op.createMethod();
		assertEquals("GET", m.getName());
		assertEquals("some/url", m.getPath());
		assertEquals("", m.getQueryString());

		op.withQueryParameter("key1", "value1");
		m = op.createMethod();
		assertEquals("GET", m.getName());
		assertEquals("some/url", m.getPath());
		assertEquals("key1=value1", m.getQueryString());

		op.withQueryParameter("key1", "value1b");
		m = op.createMethod();
		assertEquals("GET", m.getName());
		assertEquals("some/url", m.getPath());
		assertEquals("key1=value1b", m.getQueryString());

		op.withQueryParameter("key1", "value1", "value1b");
		m = op.createMethod();
		assertEquals("GET", m.getName());
		assertEquals("some/url", m.getPath());
		assertEquals("key1=value1&key1=value1b", m.getQueryString());

		op.withQueryParameter("key2", "value2");
		m = op.createMethod();
		assertEquals("GET", m.getName());
		assertEquals("some/url", m.getPath());
		assertEquals("key1=value1&key1=value1b&key2=value2", m.getQueryString());

		op.withoutQueryParameters("key1");
		m = op.createMethod();
		assertEquals("GET", m.getName());
		assertEquals("some/url", m.getPath());
		assertEquals("key2=value2", m.getQueryString());

		op.withoutQueryParameter();
		m = op.createMethod();
		assertEquals("GET", m.getName());
		assertEquals("some/url", m.getPath());
		assertEquals("", m.getQueryString());
	}

	/**
	 * Checks that the connector is actually called to execute an request and receive the response.
	 */
	@Test
	public void testExecutionOfGet() {
		RestOperation op = RestOperation.get("some/url", connector, gson, logger);
		Map<String, String> responseHeaders = Maps.newLinkedHashMap();
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, "body", responseHeaders);
		connector.setResponse(response);
		ServerResponse serverResponse = op.run();
		assertEquals(1, connector.getInvocationsCount());
		assertEquals(response, serverResponse);
	}

	@Test
	public void testIteratingOnGetWithJsonObject() {
		RestOperation op = RestOperation.get("some/url", connector, gson, logger);
		Map<String, String> responseHeaders = Maps.newLinkedHashMap();
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, "{'a':'1'}", responseHeaders);
		connector.setResponse(response);
		Iterator<JsonElement> iterator = new RestOperationIterable(op).iterator();
		assertEquals(1, connector.getInvocationsCount());
		assertEquals("{\"a\":\"1\"}", iterator.next().toString());
		assertEquals(1, connector.getInvocationsCount());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testIteratingOnGetWithArrayWithoutPagination() {
		RestOperation op = RestOperation.get("some/url", connector, gson, logger);
		Map<String, String> responseHeaders = Maps.newLinkedHashMap();
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, "[{'a':'1'},{'a':'2'}]",
				responseHeaders);
		connector.setResponse(response);
		Iterator<JsonElement> iterator = new RestOperationIterable(op).iterator();
		assertEquals("{\"a\":\"1\"}", iterator.next().toString());
		assertEquals(1, connector.getInvocationsCount());
		assertEquals("{\"a\":\"2\"}", iterator.next().toString());
		assertEquals(1, connector.getInvocationsCount());
		// No new invocation since 2 answers in the same page
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testIteratingOnGetWithArrayWithPagination() {
		MockPaginatingRestConnector paginatingConnector = new MockPaginatingRestConnector();

		RestOperation op = RestOperation.get("some/url", paginatingConnector, gson, logger);
		Map<String, String> responseHeaders = Maps.newLinkedHashMap();

		responseHeaders.put(RestResource.HEADER_X_PAGINATION_SIZE, "3");
		responseHeaders.put(RestResource.HEADER_X_PAGINATION_LIMIT, "2");
		responseHeaders.put(RestResource.HEADER_X_PAGINATION_OFFSET, "0");
		ServerResponse response = new ServerResponse(ServerResponse.STATUS_OK, "[{'a':'1'},{'a':'2'}]",
				responseHeaders);
		Map<String, String> responseHeaders2 = Maps.newLinkedHashMap();

		responseHeaders2.put(RestResource.HEADER_X_PAGINATION_SIZE, "3");
		responseHeaders2.put(RestResource.HEADER_X_PAGINATION_LIMIT, "2");
		responseHeaders2.put(RestResource.HEADER_X_PAGINATION_OFFSET, "2");
		ServerResponse response2 = new ServerResponse(ServerResponse.STATUS_OK, "[{'a':'3'}]",
				responseHeaders2);

		paginatingConnector.setResponse(response); // The default response
		paginatingConnector.putResponse(0, response);
		paginatingConnector.putResponse(2, response2);

		Iterator<JsonElement> iterator = new RestOperationIterable(op).iterator();
		assertEquals(1, paginatingConnector.getInvocationsCount());
		assertTrue(iterator.hasNext());
		assertEquals("{\"a\":\"1\"}", iterator.next().toString());
		assertEquals(1, paginatingConnector.getInvocationsCount());
		assertTrue(iterator.hasNext());
		assertEquals("{\"a\":\"2\"}", iterator.next().toString());
		assertEquals(1, paginatingConnector.getInvocationsCount());
		assertTrue(iterator.hasNext());
		assertEquals("{\"a\":\"3\"}", iterator.next().toString());
		assertEquals(2, paginatingConnector.getInvocationsCount());
		assertFalse(iterator.hasNext());
	}

	/**
	 * Checks that invoking run() on {@link RestOperation}s attempt to login automatically when they receive a
	 * 401 UNAUTHORIZED response.
	 */
	@Test
	public void testRunAutomaticLoginMechanism() {
		MockListRestConnector listConnector = new MockListRestConnector();
		ServerResponse response200 = new ServerResponse(200, "", Maps.<String, String> newHashMap());
		final TuleapToken token = new TuleapToken();
		token.setToken("token");
		token.setUserId("user_id");
		IAuthenticator authenticator = new IAuthenticator() {
			public void login() throws CoreException {
				// Stub
			}

			public TuleapToken getToken() {
				return token;
			}
		};
		listConnector.addServerResponse(response401);
		listConnector.addServerResponse(response200);
		RestOperation op = RestOperation.get("/some/url", listConnector, gson, logger);
		op.withAuthenticator(authenticator);
		ServerResponse response = op.run();
		assertTrue(response.isOk());
		assertEquals(2, listConnector.getRequestsSent().size());
		assertEquals("GET", listConnector.getRequestsSent().get(0).method);
		assertEquals("GET", listConnector.getRequestsSent().get(1).method);
	}

	/**
	 * Checks that invoking run() on {@link RestOperation}s without an authenticator does not do anything
	 * special when receiving a 401 UNAUTHORIZED response.
	 */
	@Test
	public void testRunAutomaticLoginMechanismFailsWithoutAuthenticator() {
		MockListRestConnector listConnector = new MockListRestConnector();
		ServerResponse response200 = new ServerResponse(200, "", Maps.<String, String> newHashMap());
		final TuleapToken token = new TuleapToken();
		token.setToken("token");
		token.setUserId("user_id");
		listConnector.addServerResponse(response401);
		listConnector.addServerResponse(response200);
		RestOperation op = RestOperation.get("/some/url", listConnector, gson, logger);
		// op.withAuthenticator(null);
		ServerResponse response = op.run();
		assertFalse(response.isOk());
		assertEquals(1, listConnector.getRequestsSent().size());
		assertEquals("GET", listConnector.getRequestsSent().get(0).method);
	}

	/**
	 * Checks that invoking run() on {@link RestOperation}s with an authenticator on an unauthorized resource
	 * attempts to re-login only once.
	 */
	@Test
	public void testRunAutomaticLoginMechanismOnlyOnce() {
		MockListRestConnector listConnector = new MockListRestConnector();
		ServerResponse response200 = new ServerResponse(200, "", Maps.<String, String> newHashMap());
		final TuleapToken token = new TuleapToken();
		token.setToken("token");
		token.setUserId("user_id");
		IAuthenticator authenticator = new IAuthenticator() {
			public void login() throws CoreException {
				// Stub
			}

			public TuleapToken getToken() {
				return token;
			}
		};
		listConnector.addServerResponse(response401);
		listConnector.addServerResponse(response401);
		listConnector.addServerResponse(response200);
		RestOperation op = RestOperation.get("/some/url", listConnector, gson, logger);
		op.withAuthenticator(authenticator);
		ServerResponse response = op.run();
		assertFalse(response.isOk());
		assertEquals(2, listConnector.getRequestsSent().size());
		assertEquals("GET", listConnector.getRequestsSent().get(0).method);
		assertEquals("GET", listConnector.getRequestsSent().get(0).method);
	}

	/**
	 * Checks that invoking checkedRun() on {@link RestOperation}s attempt to login automatically when they
	 * receive a 401 UNAUTHORIZED response.
	 * 
	 * @throws CoreException
	 *             in the test
	 */
	@Test
	public void testCheckedRunAutomaticLoginMechanism() throws CoreException {
		MockListRestConnector listConnector = new MockListRestConnector();
		ServerResponse response200 = new ServerResponse(200, "", Maps.<String, String> newHashMap());
		final TuleapToken token = new TuleapToken();
		token.setToken("token");
		token.setUserId("user_id");
		IAuthenticator authenticator = new IAuthenticator() {
			public void login() throws CoreException {
				// Stub
			}

			public TuleapToken getToken() {
				return token;
			}
		};
		listConnector.addServerResponse(response401);
		listConnector.addServerResponse(response200);
		RestOperation op = RestOperation.get("/some/url", listConnector, gson, logger);
		op.withAuthenticator(authenticator);
		ServerResponse response = op.checkedRun();
		assertTrue(response.isOk());
		assertEquals(2, listConnector.getRequestsSent().size());
		assertEquals("GET", listConnector.getRequestsSent().get(0).method);
		assertEquals("GET", listConnector.getRequestsSent().get(1).method);
	}

	/**
	 * Checks that invoking checkedRun() on {@link RestOperation}s without an authenticator throws a
	 * CoreException 401 UNAUTHORIZED response.
	 * 
	 * @throws CoreException
	 *             in the test
	 */
	@Test(expected = CoreException.class)
	public void testCheckedRunAutomaticLoginMechanismFailsWithoutAuthenticator() throws CoreException {
		MockListRestConnector listConnector = new MockListRestConnector();
		ServerResponse response200 = new ServerResponse(200, "", Maps.<String, String> newHashMap());
		final TuleapToken token = new TuleapToken();
		token.setToken("token");
		token.setUserId("user_id");
		listConnector.addServerResponse(response401);
		listConnector.addServerResponse(response200);
		RestOperation op = RestOperation.get("/some/url", listConnector, gson, logger);
		// op.withAuthenticator(null);
		op.checkedRun();
	}

	/**
	 * Checks that invoking checkedRun() on {@link RestOperation}s with an authenticator on an unauthorized
	 * resource attempts to re-login only once and throws a CoreException if this attempt fails.
	 * 
	 * @throws CoreException
	 *             in the test
	 */
	@Test(expected = CoreException.class)
	public void testCheckedRunAutomaticLoginMechanismOnlyOnce() throws CoreException {
		MockListRestConnector listConnector = new MockListRestConnector();
		ServerResponse response200 = new ServerResponse(200, "", Maps.<String, String> newHashMap());
		final TuleapToken token = new TuleapToken();
		token.setToken("token");
		token.setUserId("user_id");
		IAuthenticator authenticator = new IAuthenticator() {
			public void login() throws CoreException {
				// Stub
			}

			public TuleapToken getToken() {
				return token;
			}
		};
		listConnector.addServerResponse(response401);
		listConnector.addServerResponse(response401);
		listConnector.addServerResponse(response200);
		RestOperation op = RestOperation.get("/some/url", listConnector, gson, logger);
		op.withAuthenticator(authenticator);
		op.checkedRun();
	}

	@Test(expected = AssertionFailedException.class)
	public void testConstructorWithNullUrl() {
		RestOperation.get(null, connector, gson, logger);
	}

	@Test(expected = AssertionFailedException.class)
	public void testConstructorWithNullConnector() {
		RestOperation.get("url", null, gson, logger);
	}

	@Test(expected = AssertionFailedException.class)
	public void testConstructorWithNullLogger() {
		RestOperation.get("url", connector, gson, null);
	}

	@Test
	public void testGetToString() {
		RestOperation op = RestOperation.get("/the/full/url", connector, gson, logger);
		assertEquals("GET /the/full/url", op.toString());
	}

	@Test
	public void testPutToString() {
		RestOperation op = RestOperation.put("/the/full/url", connector, gson, logger);
		assertEquals("PUT /the/full/url", op.toString());
	}

	@Test
	public void testPostToString() {
		RestOperation op = RestOperation.post("/the/full/url", connector, gson, logger);
		assertEquals("POST /the/full/url", op.toString());
	}

	@Test
	public void testDeleteToString() {
		RestOperation op = RestOperation.delete("/the/full/url", connector, gson, logger);
		assertEquals("DELETE /the/full/url", op.toString());
	}

	@Test
	public void testOptionsToString() {
		RestOperation op = RestOperation.options("/the/full/url", connector, gson, logger);
		assertEquals("OPTIONS /the/full/url", op.toString());
	}

	/**
	 * Set up the tests.
	 */
	@Before
	public void setUp() {
		connector = new MockRestConnector();
		logger = new TestLogger();
		response401 = new ServerResponse(401, "{\"error\":{\"code\":401,\"message\":\"unauthorized\"}}", Maps
				.<String, String> newHashMap());
		gson = new Gson();
	}

}
