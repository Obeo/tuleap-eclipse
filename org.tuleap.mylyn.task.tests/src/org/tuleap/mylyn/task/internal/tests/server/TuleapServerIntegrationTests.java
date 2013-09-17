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

import com.google.common.collect.ImmutableList;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapTopPlanning;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonParser;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonSerializer;
import org.tuleap.mylyn.task.internal.core.server.TuleapServer;
import org.tuleap.mylyn.task.internal.core.server.rest.TuleapRestConnector;
import org.tuleap.mylyn.task.internal.tests.AbstractTuleapTests;
import org.tuleap.mylyn.task.internal.tests.TestLogger;
import org.tuleap.mylyn.task.internal.tests.mocks.MockedTuleapRepositoryConnector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This class will contain integration tests that have to be run against a valid Tuleap REST-based server.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapServerIntegrationTests extends AbstractTuleapTests {

	/**
	 * We will try to connect to the server with valid credentials.
	 */
	@Test
	public void testValidAuthentication() {
		TestLogger logger = new TestLogger();
		TuleapRestConnector tuleapRestConnector = new TuleapRestConnector(this.getServerUrl(), "v3.14", //$NON-NLS-1$
				logger);
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
	 * We will try to connect to the server with invalid credentials.
	 */
	@Test
	public void testInvalidAuthentication() {
		// Setting a wrong password for the test
		this.repository.setCredentials(AuthenticationType.REPOSITORY, new AuthenticationCredentials(
				"admin", "wrong"), false); //$NON-NLS-1$ //$NON-NLS-2$

		TestLogger logger = new TestLogger();
		TuleapRestConnector tuleapRestConnector = new TuleapRestConnector(this.getServerUrl(), "v3.14", //$NON-NLS-1$
				logger);
		TuleapJsonParser tuleapJsonParser = new TuleapJsonParser();
		TuleapJsonSerializer tuleapJsonSerializer = new TuleapJsonSerializer();

		TuleapServer tuleapServer = new TuleapServer(tuleapRestConnector, tuleapJsonParser,
				tuleapJsonSerializer, this.repository, logger);
		try {
			tuleapServer.validateConnection(new NullProgressMonitor());
			fail("A CoreException should have been thrown"); //$NON-NLS-1$
		} catch (CoreException e) {
			assertEquals(IStatus.ERROR, e.getStatus().getSeverity());
			assertEquals("Error 401: Unauthorized", e.getMessage()); //$NON-NLS-1$ 
		}
	}

	/**
	 * Test the retrieval of an existing artifact.
	 */
	@Test
	public void testGetExistingArtifact() {
		TuleapServerConfiguration config = new TuleapServerConfiguration(getServerUrl());
		// The artifact test data need the project Id 3 and the trackers 0, 1 and 4
		TuleapProjectConfiguration projectConfig = new TuleapProjectConfiguration("Project 3", 3);
		config.addProject(projectConfig);
		TuleapTrackerConfiguration trackerConfig0 = new TuleapTrackerConfiguration(0, getServerUrl());
		projectConfig.addTracker(trackerConfig0);
		TuleapTrackerConfiguration trackerConfig1 = new TuleapTrackerConfiguration(0, getServerUrl());
		projectConfig.addTracker(trackerConfig1);
		TuleapTrackerConfiguration trackerConfig4 = new TuleapTrackerConfiguration(0, getServerUrl());
		projectConfig.addTracker(trackerConfig4);

		this.connector = new MockedTuleapRepositoryConnector(config);

		TestLogger logger = new TestLogger();
		TuleapRestConnector tuleapRestConnector = new TuleapRestConnector(this.getServerUrl(), "v3.14", //$NON-NLS-1$
				logger);
		TuleapJsonParser tuleapJsonParser = new TuleapJsonParser();
		TuleapJsonSerializer tuleapJsonSerializer = new TuleapJsonSerializer();

		TuleapServer tuleapServer = new TuleapServer(tuleapRestConnector, tuleapJsonParser,
				tuleapJsonSerializer, this.repository, logger);
		try {
			TaskData taskData = tuleapServer.getArtifact(1, this.connector, null);
			System.out.println(taskData.getRoot());
			// TODO Check the TaskData content
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test of top plannings retrieval.
	 */
	@Test
	public void testGetTopPlannings() {
		TestLogger logger = new TestLogger();
		TuleapRestConnector connector = new TuleapRestConnector(this.getServerUrl(), "v3.14", logger);
		TuleapJsonParser tuleapJsonParser = new TuleapJsonParser();
		TuleapJsonSerializer tuleapJsonSerializer = new TuleapJsonSerializer();

		TuleapServer tuleapServer = new TuleapServer(connector, tuleapJsonParser, tuleapJsonSerializer,
				this.repository, logger);
		try {
			List<TuleapTopPlanning> topPlannings = tuleapServer.getTopPlannings(3, null);
			assertEquals(1, topPlannings.size());
			TuleapTopPlanning topPlanning = topPlannings.get(0);
			assertEquals(30, topPlanning.getId());
			ImmutableList<TuleapMilestone> milestones = topPlanning.getMilestones();
			assertEquals(2, milestones.size());
		} catch (CoreException e) {
			fail(e.getMessage());
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
}
