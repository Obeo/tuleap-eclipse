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

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.client.rest.ICredentials;
import org.tuleap.mylyn.task.internal.core.client.rest.IRestConnector;
import org.tuleap.mylyn.task.internal.core.client.rest.RestResource;
import org.tuleap.mylyn.task.internal.core.client.rest.RestResourceFactory;
import org.tuleap.mylyn.task.internal.core.client.rest.ServerResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests of {@link RestResourceFactory}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapRestResourceFactoryTest {

	private ICredentials credentials;

	private RestResourceFactory factory;

	/**
	 * Checks the basic properties of {@link RestResourceFactory#milestone(int)}.
	 */
	@Test
	public void testGetMilestones() {
		RestResource milestone = factory.milestone(123);
		assertNotNull(milestone);
		assertEquals("/milestones/123", milestone.getUrl());
		assertEquals("/server/api/v12.5/milestones/123", milestone.getFullUrl());
	}

	/**
	 * Checks that POST is not supported by the operation returned by
	 * {@link RestResourceFactory#milestone(int)}.
	 * 
	 * @throws CoreException
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testGetMilestonesIsPostForbidden() throws CoreException {
		RestResource milestone = factory.milestone(123);
		milestone.post();
	}

	/**
	 * Set up the tests.
	 */
	@Before
	public void setUp() {
		credentials = new ICredentials() {
			public String getUserName() {
				return "John Doe"; //$NON-NLS-1$
			}

			public String getPassword() {
				return "pass"; //$NON-NLS-1$
			}
		};
		factory = new RestResourceFactory("/server", "v12.5", new IRestConnector() {

			public ServerResponse sendRequest(String method, String url, Map<String, String> headers,
					String data) {
				return null;
			}
		});
	}
}
