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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonParser;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonSerializer;
import org.tuleap.mylyn.task.internal.core.server.TuleapRestConnector;
import org.tuleap.mylyn.task.internal.core.server.TuleapServer;
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
	/**
	 * We will try to log in and out of the server.
	 */
	@Test
	public void testValidAuthentification() {
		TestLogger logger = new TestLogger();
		TuleapRestConnector tuleapRestConnector = new TuleapRestConnector(this.getRepositoryUrl(), logger);
		TuleapJsonParser tuleapJsonParser = new TuleapJsonParser();
		TuleapJsonSerializer tuleapJsonSerializer = new TuleapJsonSerializer();

		TuleapServer tuleapServer = new TuleapServer(tuleapRestConnector, tuleapJsonParser,
				tuleapJsonSerializer, this.repository, logger);
		try {
			IStatus connectionStatus = tuleapServer.validateConnection(new NullProgressMonitor());
			assertEquals(IStatus.OK, connectionStatus.getSeverity());
			assertEquals(0, logger.getStatus().size());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * We will try to log in with an invalid user name or password.
	 */
	@Test
	public void testLogInWithInvalidCredentials() {
		fail();
	}

	/**
	 * We will try to log out from the server with an invalid session hash.
	 */
	@Test
	public void testLogOutWithInvalidSessionHash() {
		fail();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.tests.AbstractTuleapTests#getRepositoryUrl()
	 */
	@Override
	public String getRepositoryUrl() {
		// TODO Use properties in order to be able to customize the unit test from jenkins/hudson
		return "localhost:3001"; //$NON-NLS-1$
	}
}
