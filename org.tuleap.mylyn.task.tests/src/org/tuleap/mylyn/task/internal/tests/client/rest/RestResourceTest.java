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

import java.util.Collections;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.client.rest.IRestIterableOperation;
import org.tuleap.mylyn.task.internal.core.client.rest.IRestOperation;
import org.tuleap.mylyn.task.internal.core.client.rest.ITuleapHeaders;
import org.tuleap.mylyn.task.internal.core.client.rest.ITuleapServerStatus;
import org.tuleap.mylyn.task.internal.core.client.rest.RestResource;
import org.tuleap.mylyn.task.internal.core.client.rest.RestResourceFactory;
import org.tuleap.mylyn.task.internal.core.client.rest.ServerResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Tests of {@link RestResource}.
 */
public class RestResourceTest {

	private RestResourceFactory factory;

	private MockRestConnector connector;

	/**
	 * Checks that GET is supported by the operation returned by {@link RestResourceFactory#milestone(int)}
	 * but sends an error if the server does not allow GET in the OPTIONS header "allow" property.
	 * 
	 * @throws CoreException
	 */
	@Test
	public void testRestResourceManipulation() throws CoreException {
		RestResource r = new RestResource("/server", "v12.5", "/my/url", RestResource.GET | RestResource.PUT,
				connector);
		Map<String, String> headers = Maps.newTreeMap();
		headers.put(ITuleapHeaders.ALLOW, "OPTIONS,GET,PUT");
		headers.put(ITuleapHeaders.ACCESS_CONTROL_ALLOW_METHODS, "OPTIONS,GET,PUT");
		connector.setResponse(new ServerResponse(ITuleapServerStatus.OK, "", headers));
		IRestIterableOperation<JsonElement> get = r.get();
		assertNotNull(get);
		assertEquals("GET", get.getMethodName());
		assertEquals("/server/api/v12.5/my/url", get.getUrl());

		// Make sure that an OPTIONS request is not re-sent
		// We set to the connector an invalid response (without the required headers)
		// which would cause a rejection if an OPTIONS request was made again
		connector.setResponse(new ServerResponse(ITuleapServerStatus.OK, "", Collections
				.<String, String> emptyMap()));
		get = r.get();
		assertNotNull(get);
		assertEquals("GET", get.getMethodName());
		assertEquals("/server/api/v12.5/my/url", get.getUrl());
		IRestOperation put = r.put();
		assertNotNull(put);
		assertEquals("PUT", put.getMethodName());
		assertEquals("/server/api/v12.5/my/url", put.getUrl());
		try {
			r.post();
			fail("There should have been an exception");
		} catch (UnsupportedOperationException e) {
			// It's ok.
		}
	}

	/**
	 * Set up the tests.
	 */
	@Before
	public void setUp() {
		connector = new MockRestConnector();
		factory = new RestResourceFactory("/server", "v12.5", connector);
	}
}
