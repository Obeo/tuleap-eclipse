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

import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItemType;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestoneType;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapTopPlanning;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonParser;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonSerializer;
import org.tuleap.mylyn.task.internal.core.server.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.server.rest.TuleapRestConnector;
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
	 * We will try to connect to the server with valid credentials.
	 */
	@Test
	public void testValidAuthentication() {
		TestLogger logger = new TestLogger();
		TuleapRestConnector tuleapRestConnector = new TuleapRestConnector(this.getServerUrl(), "v3.14", //$NON-NLS-1$
				logger);
		TuleapJsonParser tuleapJsonParser = new TuleapJsonParser();
		TuleapJsonSerializer tuleapJsonSerializer = new TuleapJsonSerializer();

		TuleapRestClient tuleapServer = new TuleapRestClient(tuleapRestConnector, tuleapJsonParser,
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

		TuleapRestClient tuleapServer = new TuleapRestClient(tuleapRestConnector, tuleapJsonParser,
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
	 * Test of top plannings retrieval.
	 */
	@Test
	public void testGetTopPlannings() {
		TestLogger logger = new TestLogger();
		TuleapRestConnector restConnector = new TuleapRestConnector(this.getServerUrl(), "v3.14", logger); //$NON-NLS-1$
		TuleapJsonParser tuleapJsonParser = new TuleapJsonParser();
		TuleapJsonSerializer tuleapJsonSerializer = new TuleapJsonSerializer();

		TuleapRestClient tuleapServer = new TuleapRestClient(restConnector, tuleapJsonParser,
				tuleapJsonSerializer, this.repository, logger);
		try {
			List<TuleapTopPlanning> topPlannings = tuleapServer.getTopPlannings(3, null);
			assertEquals(1, topPlannings.size());
			TuleapTopPlanning topPlanning = topPlannings.get(0);
			assertEquals(30, topPlanning.getId());
			ImmutableList<TuleapMilestone> milestones = topPlanning.getMilestones();
			assertEquals(2, milestones.size());
			ImmutableList<TuleapBacklogItem> backlogItems = topPlanning.getBacklogItems();
			assertEquals(3, backlogItems.size());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test of Milestone Types retrieval.
	 */
	@Test
	public void testGetMilestoneTypes() {
		TestLogger logger = new TestLogger();
		TuleapRestConnector restConnector = new TuleapRestConnector(this.getServerUrl(), "v3.14", logger); //$NON-NLS-1$
		TuleapJsonParser tuleapJsonParser = new TuleapJsonParser();
		TuleapJsonSerializer tuleapJsonSerializer = new TuleapJsonSerializer();

		TuleapRestClient tuleapServer = new TuleapRestClient(restConnector, tuleapJsonParser,
				tuleapJsonSerializer, this.repository, logger);
		try {
			List<TuleapMilestoneType> milestoneTypes = tuleapServer.getMilestoneTypes(3, null);
			assertEquals(2, milestoneTypes.size());

			TuleapMilestoneType firstMilestoneType = milestoneTypes.get(0);
			assertEquals(901, firstMilestoneType.getIdentifier());
			assertEquals("Releases", firstMilestoneType.getName()); //$NON-NLS-1$
			assertEquals("localhost:3001/api/v3.14/milestone_types/901", firstMilestoneType.getUrl()); //$NON-NLS-1$
			Collection<AbstractTuleapField> fields = firstMilestoneType.getFields();
			assertEquals(5, fields.size());

			TuleapMilestoneType secondMilestoneType = milestoneTypes.get(1);
			assertEquals(902, secondMilestoneType.getIdentifier());
			assertEquals("Sprints", secondMilestoneType.getName()); //$NON-NLS-1$
			assertEquals("localhost:3001/api/v3.14/milestone_types/902", secondMilestoneType.getUrl()); //$NON-NLS-1$
			Collection<AbstractTuleapField> theFields = secondMilestoneType.getFields();
			assertEquals(7, theFields.size());

			// All Milestone Type JSON deserializer tests are done in the TuleapMilestoneTypeDeserializerTests
			// class
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test of BacklogItem Types retrieval.
	 */
	@Test
	public void testGetBacklogItemTypes() {
		TestLogger logger = new TestLogger();
		TuleapRestConnector restConnector = new TuleapRestConnector(this.getServerUrl(), "v3.14", logger); //$NON-NLS-1$
		TuleapJsonParser tuleapJsonParser = new TuleapJsonParser();
		TuleapJsonSerializer tuleapJsonSerializer = new TuleapJsonSerializer();

		TuleapRestClient tuleapServer = new TuleapRestClient(restConnector, tuleapJsonParser,
				tuleapJsonSerializer, this.repository, logger);
		try {
			List<TuleapBacklogItemType> backlogItemTypes = tuleapServer.getBacklogitemTypes(3, null);
			assertEquals(2, backlogItemTypes.size());

			TuleapBacklogItemType firstBacklogItemType = backlogItemTypes.get(0);
			assertEquals(801, firstBacklogItemType.getIdentifier());
			assertEquals("Epics", firstBacklogItemType.getName()); //$NON-NLS-1$
			assertEquals("localhost:3001/api/v3.14/backlog_item_types/801", firstBacklogItemType.getUrl()); //$NON-NLS-1$
			Collection<AbstractTuleapField> fields = firstBacklogItemType.getFields();
			assertEquals(4, fields.size());

			TuleapBacklogItemType secondBacklogItemType = backlogItemTypes.get(1);
			assertEquals(802, secondBacklogItemType.getIdentifier());
			assertEquals("User Stories", secondBacklogItemType.getName()); //$NON-NLS-1$
			assertEquals("localhost:3001/api/v3.14/backlog_item_types/802", secondBacklogItemType.getUrl()); //$NON-NLS-1$

			Collection<AbstractTuleapField> theFields = secondBacklogItemType.getFields();
			assertEquals(10, theFields.size());

			// All Milestone Type JSON deserializer tests are done in the
			// TuleapBacklogItemTypeDeserializerTests class

		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test of backlog items retrieval.
	 */
	@Test
	public void testGetMilestones() {
		TestLogger logger = new TestLogger();
		TuleapRestConnector restConnector = new TuleapRestConnector(this.getServerUrl(), "v3.14", logger); //$NON-NLS-1$
		TuleapJsonParser tuleapJsonParser = new TuleapJsonParser();
		TuleapJsonSerializer tuleapJsonSerializer = new TuleapJsonSerializer();

		TuleapRestClient tuleapServer = new TuleapRestClient(restConnector, tuleapJsonParser,
				tuleapJsonSerializer, this.repository, logger);
		try {
			List<TuleapMilestone> milestoneItems = tuleapServer.getSubMilestones(200, null);
			assertEquals(3, milestoneItems.size());

			List<TuleapBacklogItem> backlogItems = tuleapServer.getBacklogItems(200, null);
			assertEquals(0, backlogItems.size());

			milestoneItems = tuleapServer.getSubMilestones(201, null);
			assertEquals(0, milestoneItems.size());

			backlogItems = tuleapServer.getBacklogItems(201, null);
			assertEquals(2, backlogItems.size());
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
