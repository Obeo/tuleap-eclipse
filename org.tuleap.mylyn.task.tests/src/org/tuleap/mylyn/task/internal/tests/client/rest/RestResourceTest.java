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

import java.util.Collections;
import java.util.Map;

import org.apache.commons.httpclient.HttpMethod;
import org.eclipse.core.runtime.CoreException;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.client.rest.ITuleapHeaders;
import org.tuleap.mylyn.task.internal.core.client.rest.RestOperation;
import org.tuleap.mylyn.task.internal.core.client.rest.RestResource;
import org.tuleap.mylyn.task.internal.core.client.rest.ServerResponse;
import org.tuleap.mylyn.task.internal.tests.TestLogger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Tests of {@link RestResource}.
 */
public class RestResourceTest {

	private MockRestConnector connector;

	/**
	 * Checks basic {@link RestResource} manipulation.
	 * 
	 * @throws CoreException
	 */
	@Test
	public void testRestResourceManipulation() throws CoreException {
		RestResource r = new RestResource("/server", "v12.5", "/my/url", RestResource.GET | RestResource.PUT, //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				connector, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,GET,PUT"); //$NON-NLS-1$
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET,PUT"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		RestOperation get = r.get();
		assertNotNull(get);

		// Make sure that an OPTIONS request is not re-sent
		// We set to the connector an invalid response (without the required headers)
		// which would cause a rejection if an OPTIONS request was made again
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", Collections //$NON-NLS-1$
				.<String, String> emptyMap()));
		get = r.get();
		assertNotNull(get);
		RestOperation put = r.put();
		assertNotNull(put);
		try {
			r.post();
			fail("There should have been an exception"); //$NON-NLS-1$
		} catch (UnsupportedOperationException e) {
			// It's ok.
		}
	}

	@Test
	public void testRestResourceWithUrlWithoutTrailingSlash() {
		RestResource r = new RestResource("/server", "v12.5", "my/url", RestResource.GET, //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				connector, new TestLogger());
		assertEquals("/my/url", r.getUrl());
		assertEquals("/api/v12.5/my/url", r.getFullUrl());

	}

	@Test
	public void testDeleteAllowed() throws CoreException {
		RestResource r = new RestResource("/server", "v12.5", "/my/url", RestResource.DELETE, //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				connector, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,DELETE"); //$NON-NLS-1$
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,DELETE"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		RestOperation delete = r.delete();
		assertNotNull(delete);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testDeleteNotAllowedLocal() throws CoreException {
		RestResource r = new RestResource("/server", "v12.5", "/my/url", RestResource.GET, //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				connector, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,DELETE"); //$NON-NLS-1$
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,DELETE"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		r.delete();
	}

	@Test(expected = CoreException.class)
	public void testDeleteNotAllowedDistant() throws CoreException {
		RestResource r = new RestResource("/server", "v12.5", "/my/url", RestResource.DELETE, //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				connector, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		r.delete();
	}

	@Test
	public void testPostAllowed() throws CoreException {
		RestResource r = new RestResource("/server", "v12.5", "/my/url", RestResource.POST, //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				connector, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,POST"); //$NON-NLS-1$
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,POST"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		RestOperation post = r.post();
		assertNotNull(post);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testPostNotAllowedLocal() throws CoreException {
		RestResource r = new RestResource("/server", "v12.5", "/my/url", RestResource.GET, //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				connector, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,POST"); //$NON-NLS-1$
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,POST"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		r.post();
	}

	@Test(expected = CoreException.class)
	public void testPostNotAllowedDistant() throws CoreException {
		RestResource r = new RestResource("/server", "v12.5", "/my/url", RestResource.POST, //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				connector, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		r.post();
	}

	@Test
	public void testPutAllowed() throws CoreException {
		RestResource r = new RestResource("/server", "v12.5", "/my/url", RestResource.PUT, //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				connector, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		RestOperation put = r.put();
		assertNotNull(put);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testPutNotAllowedLocal() throws CoreException {
		RestResource r = new RestResource("/server", "v12.5", "/my/url", RestResource.GET, //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				connector, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		r.put();
	}

	@Test(expected = CoreException.class)
	public void testPutNotAllowedDistant() throws CoreException {
		RestResource r = new RestResource("/server", "v12.5", "/my/url", RestResource.PUT, //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				connector, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		r.put();
	}

	@Test
	public void testGetAllowed() throws CoreException {
		RestResource r = new RestResource("/server", "v12.5", "/my/url", RestResource.GET, //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				connector, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		RestOperation get = r.get();
		assertNotNull(get);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void tesGetNotAllowedLocal() throws CoreException {
		RestResource r = new RestResource("/server", "v12.5", "/my/url", RestResource.PUT, //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				connector, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		r.get();
	}

	@Test(expected = CoreException.class)
	public void testGetNotAllowedDistant() throws CoreException {
		RestResource r = new RestResource("/server", "v12.5", "/my/url", RestResource.GET, //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				connector, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		r.get();
	}

	/**
	 * Test the pagination on GET with HEADER_X_PAGINATION_SIZE set, HEADER_X_PAGINATION_LIMIT_MAX header
	 * attribute set and HEADER_X_PAGINATION_LIMIT not set .
	 * 
	 * @throws CoreException
	 *             The exception to throw
	 */
	@Test
	public void testGetPaginationLimitMaxSetWithoutLimit() throws CoreException {
		RestResource r = new RestResource("/server", "v12.5", "/my/url", RestResource.GET, connector,
				new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,GET");
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET");
		headers.put(ITuleapHeaders.HEADER_X_PAGINATION_SIZE, "3");
		headers.put(ITuleapHeaders.HEADER_X_PAGINATION_LIMIT_MAX, "30");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));
		RestOperation get = r.get();
		HttpMethod method = get.createMethod();
		assertEquals("GET", method.getName());
		assertEquals("/api/v12.5/my/url", method.getPath());
		assertEquals("limit=30", method.getQueryString());

		headers.put(ITuleapHeaders.HEADER_X_PAGINATION_LIMIT_MAX, "10");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));
		get = r.get();
		method = get.createMethod();
		assertEquals("GET", method.getName());
		assertEquals("/api/v12.5/my/url", method.getPath());
		assertEquals("limit=10", method.getQueryString());
	}

	/**
	 * Test the pagination on GET with HEADER_X_PAGINATION_SIZE set, HEADER_X_PAGINATION_LIMIT_MAX header
	 * attribute set and HEADER_X_PAGINATION_LIMIT set.
	 * 
	 * @throws CoreException
	 *             The exception to throw
	 */
	@Test
	public void testGetPaginationLimitMaxSetWithLimit() throws CoreException {
		RestResource r = new RestResource("/server", "v12.5", "/my/url", RestResource.GET, connector,
				new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,GET");
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET");
		headers.put(ITuleapHeaders.HEADER_X_PAGINATION_SIZE, "3");
		headers.put(ITuleapHeaders.HEADER_X_PAGINATION_LIMIT_MAX, "30");
		headers.put(ITuleapHeaders.HEADER_X_PAGINATION_LIMIT, "20");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));
		RestOperation get = r.get();
		HttpMethod method = get.createMethod();
		assertEquals("GET", method.getName());
		assertEquals("/api/v12.5/my/url", method.getPath());
		assertEquals("limit=30", method.getQueryString());

		headers.put(ITuleapHeaders.HEADER_X_PAGINATION_LIMIT_MAX, "10");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));
		get = r.get();
		method = get.createMethod();
		assertEquals("GET", method.getName());
		assertEquals("/api/v12.5/my/url", method.getPath());
		assertEquals("limit=10", method.getQueryString());
	}

	/**
	 * Test the pagination on GEt with HEADER_X_PAGINATION_SIZE set and HEADER_X_PAGINATION_LIMIT_MAX header
	 * attribute not set.
	 * 
	 * @throws CoreException
	 *             The exception to throw
	 */
	@Test
	public void testGetPaginationLimitMaxNotSetSizeSet() throws CoreException {
		RestResource r = new RestResource("/server", "v12.5", "/my/url", RestResource.GET, connector,
				new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,GET");
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET");
		headers.put(ITuleapHeaders.HEADER_X_PAGINATION_SIZE, "3");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));
		RestOperation get = r.get();
		HttpMethod method = get.createMethod();
		assertEquals("GET", method.getName());
		assertEquals("/api/v12.5/my/url", method.getPath());
		assertEquals("limit=" + ITuleapHeaders.DEFAULT_PAGINATION_LIMIT, method.getQueryString());
	}

	/**
	 * Test the pagination on GET with HEADER_X_PAGINATION_SIZE not set.
	 * 
	 * @throws CoreException
	 *             The exception to throw
	 */
	@Test
	public void testGetPaginationSizeNotSet() throws CoreException {
		RestResource r = new RestResource("/server", "v12.5", "/my/url", RestResource.GET, connector,
				new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,GET");
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));
		RestOperation get = r.get();
		HttpMethod method = get.createMethod();
		assertEquals("GET", method.getName());
		assertEquals("/api/v12.5/my/url", method.getPath());
		assertEquals("", method.getQueryString());

		headers.put(ITuleapHeaders.HEADER_X_PAGINATION_LIMIT_MAX, "10");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));
		get = r.get();
		method = get.createMethod();
		assertEquals("GET", method.getName());
		assertEquals("/api/v12.5/my/url", method.getPath());
		assertEquals("", method.getQueryString());
	}

	/**
	 * Set up the tests.
	 */
	@Before
	public void setUp() {
		connector = new MockRestConnector();
	}
}
