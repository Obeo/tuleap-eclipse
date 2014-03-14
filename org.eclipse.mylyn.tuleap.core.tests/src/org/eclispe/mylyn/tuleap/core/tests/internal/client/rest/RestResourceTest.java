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
package org.eclispe.mylyn.tuleap.core.tests.internal.client.rest;

import com.google.common.collect.Maps;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.httpclient.HttpMethod;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.RestOperation;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.RestResource;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.ServerResponse;
import org.eclispe.mylyn.tuleap.core.tests.internal.TestLogger;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Tests of {@link RestResource}.
 */
public class RestResourceTest {

	private MockRestConnector connector;

	private Gson gson;

	/**
	 * Checks basic {@link RestResource} manipulation.
	 * 
	 * @throws CoreException
	 */
	@Test
	public void testRestResourceManipulation() throws CoreException {
		RestResource r = new RestResource("/server/api/v12.5/my/url", RestResource.GET | RestResource.PUT, //$NON-NLS-1$
				connector, gson, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(RestResource.ALLOW, "OPTIONS,GET,PUT"); //$NON-NLS-1$
		headers.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET,PUT"); //$NON-NLS-1$
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
		RestResource r = new RestResource("/server/api/v12.5/my/url", RestResource.GET, //$NON-NLS-1$
				connector, gson, new TestLogger());
		assertEquals("/server/api/v12.5/my/url", r.getUrl());

	}

	@Test
	public void testDeleteAllowed() throws CoreException {
		RestResource r = new RestResource("/server/api/v12.5/my/url", RestResource.DELETE, //$NON-NLS-1$
				connector, gson, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(RestResource.ALLOW, "OPTIONS,DELETE"); //$NON-NLS-1$
		headers.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,DELETE"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		RestOperation delete = r.delete();
		assertNotNull(delete);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testDeleteNotAllowedLocal() throws CoreException {
		RestResource r = new RestResource("/server/api/v12.5/my/url", RestResource.GET, //$NON-NLS-1$
				connector, gson, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(RestResource.ALLOW, "OPTIONS,DELETE"); //$NON-NLS-1$
		headers.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,DELETE"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		r.delete();
	}

	@Test
	public void testPostAllowed() throws CoreException {
		RestResource r = new RestResource("/server/api/v12.5/my/url", RestResource.POST, //$NON-NLS-1$
				connector, gson, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(RestResource.ALLOW, "OPTIONS,POST"); //$NON-NLS-1$
		headers.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,POST"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		RestOperation post = r.post();
		assertNotNull(post);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testPostNotAllowedLocal() throws CoreException {
		RestResource r = new RestResource("/server/api/v12.5/my/url", RestResource.GET, //$NON-NLS-1$
				connector, gson, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(RestResource.ALLOW, "OPTIONS,POST"); //$NON-NLS-1$
		headers.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,POST"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		r.post();
	}

	@Test
	public void testPutAllowed() throws CoreException {
		RestResource r = new RestResource("/server/api/v12.5/my/url", RestResource.PUT, //$NON-NLS-1$
				connector, gson, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(RestResource.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		headers.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		RestOperation put = r.put();
		assertNotNull(put);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testPutNotAllowedLocal() throws CoreException {
		RestResource r = new RestResource("/server/api/v12.5/my/url", RestResource.GET, //$NON-NLS-1$
				connector, gson, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(RestResource.ALLOW, "OPTIONS,PUT"); //$NON-NLS-1$
		headers.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,PUT"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		r.put();
	}

	@Test
	public void testGetAllowed() throws CoreException {
		RestResource r = new RestResource("/server/api/v12.5/my/url", RestResource.GET, //$NON-NLS-1$
				connector, gson, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		headers.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		RestOperation get = r.get();
		assertNotNull(get);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void tesGetNotAllowedLocal() throws CoreException {
		RestResource r = new RestResource("/server/api/v12.5/my/url", RestResource.PUT, //$NON-NLS-1$
				connector, gson, new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(RestResource.ALLOW, "OPTIONS,GET"); //$NON-NLS-1$
		headers.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET"); //$NON-NLS-1$
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers)); //$NON-NLS-1$
		r.get();
	}

	/**
	 * Test the pagination on GET with HEADER_X_PAGINATION_SIZE not set.
	 * 
	 * @throws CoreException
	 *             The exception to throw
	 */
	@Test
	public void testGetPaginationSizeNotSet() throws CoreException {
		RestResource r = new RestResource("/server/api/v12.5/my/url", RestResource.GET, connector, gson,
				new TestLogger());
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(RestResource.ALLOW, "OPTIONS,GET");
		headers.put(RestResource.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));
		RestOperation get = r.get();
		HttpMethod method = get.createMethod();
		assertEquals("GET", method.getName());
		assertEquals("/server/api/v12.5/my/url", method.getPath());
		assertEquals("", method.getQueryString());

		headers.put(RestResource.HEADER_X_PAGINATION_LIMIT_MAX, "10");
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", headers));
		get = r.get();
		method = get.createMethod();
		assertEquals("GET", method.getName());
		assertEquals("/server/api/v12.5/my/url", method.getPath());
		assertEquals("", method.getQueryString());
	}

	/**
	 * Set up the tests.
	 */
	@Before
	public void setUp() {
		connector = new MockRestConnector();
		gson = new Gson();
	}
}
