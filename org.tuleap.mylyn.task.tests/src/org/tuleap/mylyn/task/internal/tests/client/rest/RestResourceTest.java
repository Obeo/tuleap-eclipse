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
		assertEquals("GET", get.getMethodName()); //$NON-NLS-1$
		assertEquals("/server/api/v12.5/my/url", get.getUrl()); //$NON-NLS-1$

		// Make sure that an OPTIONS request is not re-sent
		// We set to the connector an invalid response (without the required headers)
		// which would cause a rejection if an OPTIONS request was made again
		connector.setResponse(new ServerResponse(ServerResponse.STATUS_OK, "", Collections //$NON-NLS-1$
				.<String, String> emptyMap()));
		get = r.get();
		assertNotNull(get);
		assertEquals("GET", get.getMethodName()); //$NON-NLS-1$
		assertEquals("/server/api/v12.5/my/url", get.getUrl()); //$NON-NLS-1$
		RestOperation put = r.put();
		assertNotNull(put);
		assertEquals("PUT", put.getMethodName()); //$NON-NLS-1$
		assertEquals("/server/api/v12.5/my/url", put.getUrl()); //$NON-NLS-1$
		try {
			r.post();
			fail("There should have been an exception"); //$NON-NLS-1$
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
	}
}
