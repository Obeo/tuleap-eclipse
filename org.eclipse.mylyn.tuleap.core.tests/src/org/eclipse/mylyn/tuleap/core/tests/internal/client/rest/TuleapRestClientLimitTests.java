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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.text.ParseException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.RestResourceFactory;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.TuleapRestClient;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapPlanning;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapProject;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapReference;
import org.eclipse.mylyn.tuleap.core.internal.parser.TuleapGsonProvider;
import org.eclipse.mylyn.tuleap.core.internal.util.ITuleapConstants;
import org.eclipse.mylyn.tuleap.core.tests.internal.TestLogger;
import org.eclipse.mylyn.tuleap.core.tests.internal.parser.ParserUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests of {@link TuleapRestClient}.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapRestClientLimitTests {

	private final String serverUrl = "https://test/url"; //$NON-NLS-1$

	private String apiVersion = "v12.3"; //$NON-NLS-1$

	private MockRestConnector connector;

	private Gson gson;

	private TaskRepository repository;

	private RestResourceFactory restResourceFactory;

	private TuleapRestClient client;

	@Test
	public void testLoadPlanningsIntoProject() throws CoreException, ParseException {
		TuleapProject project = gson.fromJson(ParserUtil.loadFile("/projects/project-0.json"),
				TuleapProject.class);
		client.loadPlanningsInto(project);

		TuleapPlanning firstPlanning = project.getPlanning(1);

		assertEquals(1, firstPlanning.getId().intValue());
		assertEquals(101, firstPlanning.getProject().getId());
		assertEquals("projects/101", firstPlanning.getProject().getUri());
		assertEquals("Release Planning", firstPlanning.getLabel());
		assertEquals("route-not-yet-implemented", firstPlanning.getUri());
		assertEquals(15, firstPlanning.getMilestoneTracker().getId());
		assertEquals("trackers/15", firstPlanning.getMilestoneTracker().getUri());
		TuleapReference[] trackers = firstPlanning.getBacklogTrackers();
		assertEquals(2, trackers.length);
		TuleapReference trackerRef = trackers[0];
		assertEquals(10, trackerRef.getId());
		assertEquals("trackers/10", trackerRef.getUri());
		assertEquals("plannings/1/milestones", firstPlanning.getMilestonesUri());

		TuleapPlanning secondPlanning = project.getPlanning(2);

		assertEquals(2, secondPlanning.getId().intValue());
		assertEquals(101, secondPlanning.getProject().getId());
		assertEquals("projects/101", secondPlanning.getProject().getUri());
		assertEquals("Sprint Planning", secondPlanning.getLabel());
		assertEquals("route-not-yet-implemented", secondPlanning.getUri());
		assertEquals(16, secondPlanning.getMilestoneTracker().getId());
		assertEquals("trackers/16", secondPlanning.getMilestoneTracker().getUri());
		TuleapReference[] theTrackers = secondPlanning.getBacklogTrackers();
		assertEquals(3, theTrackers.length);
		TuleapReference theTrackerRef = theTrackers[0];
		assertEquals(10, theTrackerRef.getId());
		assertEquals("trackers/10", theTrackerRef.getUri());
		assertEquals("plannings/2/milestones", secondPlanning.getMilestonesUri());

	}

	@Before
	public void setUp() {
		connector = new MockRestConnector();
		gson = TuleapGsonProvider.defaultGson();
		this.restResourceFactory = new RestResourceFactory(apiVersion, connector, gson, new TestLogger());
		this.repository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, serverUrl);
		this.repository.setCredentials(AuthenticationType.REPOSITORY, new AuthenticationCredentials("admin",
				"password"), true);
		connector.setResourceFactory(restResourceFactory);
		client = new TuleapRestClient(restResourceFactory, gson, repository) {
			@Override
			public void loadPlanningsInto(TuleapProject project) throws CoreException {
				// Retrieve the plannings of the project
				String plannings = ParserUtil.loadFile("/top_plannings/plannings.json");
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(plannings);
				JsonArray jasonArray = element.getAsJsonArray();
				for (JsonElement object : jasonArray) {
					TuleapPlanning planning = gson.fromJson(object, TuleapPlanning.class);
					project.addPlanning(planning);
				}
			}
		};
	}
}
