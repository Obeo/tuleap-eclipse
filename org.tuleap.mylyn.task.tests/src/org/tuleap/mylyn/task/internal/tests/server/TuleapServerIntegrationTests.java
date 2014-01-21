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
package org.tuleap.mylyn.task.internal.tests.server;

import com.google.gson.Gson;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.TaskRepositoryLocationFactory;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.client.rest.RestResourceFactory;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestConnector;
import org.tuleap.mylyn.task.internal.core.parser.TuleapGsonProvider;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.tests.AbstractTuleapTests;
import org.tuleap.mylyn.task.internal.tests.TestLogger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This class will contain integration tests that have to be run against a valid Tuleap REST-based server.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapServerIntegrationTests extends AbstractTuleapTests {

	private TaskRepository taskRepository;

	private AbstractWebLocation location;

	private Gson gson;

	/**
	 * We will try to connect to the server with valid credentials.
	 */
	@Test
	public void testValidAuthentication() {
		TestLogger logger = new TestLogger();
		TuleapRestConnector tuleapRestConnector = new TuleapRestConnector(location, logger);
		RestResourceFactory restResourceFactory = new RestResourceFactory("v3.14", tuleapRestConnector, gson,
				new TestLogger());
		TuleapRestClient tuleapServer = new TuleapRestClient(restResourceFactory, gson, this.repository);
		try {
			IStatus connectionStatus = tuleapServer.validateConnection(new NullProgressMonitor());
			assertEquals(IStatus.OK, connectionStatus.getSeverity());
			assertEquals(0, logger.getStatus().size());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * We will try to connect to the server with invalid credentials.
	 */
	@Test
	public void testInvalidAuthentication() {
		// Setting a wrong password for the test
		this.repository.setCredentials(AuthenticationType.REPOSITORY, new AuthenticationCredentials(
				"admin", "wrong"), false); //$NON-NLS-1$ //$NON-NLS-2$
		TestLogger logger = new TestLogger();
		TuleapRestConnector tuleapRestConnector = new TuleapRestConnector(location, logger);
		RestResourceFactory restResourceFactory = new RestResourceFactory("v3.14", tuleapRestConnector, gson,
				new TestLogger());
		TuleapRestClient tuleapServer = new TuleapRestClient(restResourceFactory, gson, this.repository);
		try {
			tuleapServer.validateConnection(new NullProgressMonitor());
			fail("A CoreException should have been thrown"); //$NON-NLS-1$
		} catch (CoreException e) {
			assertEquals(IStatus.ERROR, e.getStatus().getSeverity());
			assertEquals("Error 401: Unauthorized", e.getMessage()); //$NON-NLS-1$ 
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.tests.AbstractTuleapTests#getServerUrl()
	 */
	@Override
	public String getServerUrl() {
		// TODO Use properties in order to be able to customize the unit test from jenkins/hudson
		return "http://localhost:3001"; //$NON-NLS-1$
	}

	@Override
	@Before
	public void setUp() {
		taskRepository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, "https://this.is.a.test"); //$NON-NLS-1$
		AuthenticationCredentials credentials = new AuthenticationCredentials("admin", "password"); //$NON-NLS-1$//$NON-NLS-2$
		taskRepository.setCredentials(AuthenticationType.REPOSITORY, credentials, true);
		credentials = new AuthenticationCredentials("admin", "password"); //$NON-NLS-1$//$NON-NLS-2$
		taskRepository.setCredentials(AuthenticationType.HTTP, credentials, true);
		location = new TaskRepositoryLocationFactory().createWebLocation(taskRepository);
		gson = TuleapGsonProvider.defaultGson();
	}
}
