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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.client.rest.RestResourceFactory;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapPlanning;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapProject;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapResource;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapTracker;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapWorkflow;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapElementComment;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;
import org.tuleap.mylyn.task.internal.core.parser.TuleapGsonProvider;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.tests.TestLogger;
import org.tuleap.mylyn.task.internal.tests.parser.ParserUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

	/**
	 * Test the parsing of a tracker with a field values that is <code>null</code>.
	 */
	@Test
	public void testTrackerFieldValuesNullParsing() {
		String json = ParserUtil.loadFile("/trackers/tracker-6.json");
		TuleapTracker tracker = gson.fromJson(json, TuleapTracker.class);

		assertNotNull(tracker);
		assertEquals(6, tracker.getIdentifier());
		assertEquals("Product", tracker.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/plugins/tracker/?tracker=6", tracker.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/6", tracker.getUri()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tracker.getFields();
		assertEquals(1, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();

		// testing the semantic title
		AbstractTuleapField firstField = iterator.next();
		TuleapString stringField = (TuleapString)firstField;
		assertTrue(firstField instanceof TuleapString);
		assertTrue(stringField.isSemanticTitle());

		// testing the field permissions
		assertTrue(firstField.isReadable());
		assertFalse(firstField.isSubmitable());
		assertFalse(firstField.isUpdatable());

		assertEquals(1, tracker.getTrackerResources().length);

		TuleapResource tuleapResource = tracker.getTrackerResources()[0];
		assertEquals("reports", tuleapResource.getType()); //$NON-NLS-1$
		assertEquals("trackers/6/tracker_reports", tuleapResource.getUri()); //$NON-NLS-1$

	}

	/**
	 * Test the parsing of a tracker with a binding type that is <code>null</code>.
	 */
	@Test
	public void testTrackerBindingTypeNullParsing() {
		String json = ParserUtil.loadFile("/trackers/tracker-7.json");
		TuleapTracker tracker = gson.fromJson(json, TuleapTracker.class);

		assertNotNull(tracker);
		assertEquals(7, tracker.getIdentifier());
		assertEquals("Product", tracker.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/plugins/tracker/?tracker=7", tracker.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/7", tracker.getUri()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tracker.getFields();
		assertEquals(1, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();

		// testing the semantic title
		AbstractTuleapField firstField = iterator.next();
		TuleapString stringField = (TuleapString)firstField;
		assertTrue(firstField instanceof TuleapString);
		assertTrue(stringField.isSemanticTitle());

		// testing the field permissions
		assertTrue(firstField.isReadable());
		assertFalse(firstField.isSubmitable());
		assertFalse(firstField.isUpdatable());

		assertEquals(1, tracker.getTrackerResources().length);

		TuleapResource tuleapResource = tracker.getTrackerResources()[0];
		assertEquals("reports", tuleapResource.getType()); //$NON-NLS-1$
		assertEquals("trackers/7/tracker_reports", tuleapResource.getUri()); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of a tracker with a status that is <code>null</code>.
	 */
	@Test
	public void testTrackerStatusNullParsing() {
		String json = ParserUtil.loadFile("/trackers/tracker-8.json");
		TuleapTracker tracker = gson.fromJson(json, TuleapTracker.class);

		assertNotNull(tracker);
		assertEquals(8, tracker.getIdentifier());
		assertEquals("Product", tracker.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/plugins/tracker/?tracker=8", tracker.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/8", tracker.getUri()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tracker.getFields();
		assertEquals(1, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();

		// testing the semantic title
		AbstractTuleapField firstField = iterator.next();
		TuleapString stringField = (TuleapString)firstField;
		assertTrue(firstField instanceof TuleapString);
		assertTrue(stringField.isSemanticTitle());

		// testing the field permissions
		assertTrue(firstField.isReadable());
		assertFalse(firstField.isSubmitable());
		assertFalse(firstField.isUpdatable());

		assertEquals(1, tracker.getTrackerResources().length);

		TuleapResource tuleapResource = tracker.getTrackerResources()[0];
		assertEquals("reports", tuleapResource.getType()); //$NON-NLS-1$
		assertEquals("trackers/8/tracker_reports", tuleapResource.getUri()); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of a tracker with a transition that is <code>null</code>.
	 */
	@Test
	public void testTrackerTransitionFromIdNullParsing() {
		String json = ParserUtil.loadFile("/trackers/tracker-9.json");
		TuleapTracker tracker = gson.fromJson(json, TuleapTracker.class);

		assertNotNull(tracker);
		assertEquals(9, tracker.getIdentifier());
		assertEquals("Bugs", tracker.getLabel()); //$NON-NLS-1$
		assertEquals("localhost: 3001/plugins/tracker/?tracker=9", tracker.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/9", tracker.getUri()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tracker.getFields();
		assertEquals(1, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();

		// testing the semantic title
		AbstractTuleapField firstField = iterator.next();

		// testing the field id
		assertEquals(0, firstField.getIdentifier());

		// testing the field permissions
		assertTrue(firstField.isReadable());
		assertTrue(firstField.isSubmitable());
		assertTrue(firstField.isUpdatable());

		assertTrue(firstField instanceof TuleapSelectBox);
		TuleapSelectBox selectBoxField = (TuleapSelectBox)firstField;

		Collection<TuleapSelectBoxItem> items = selectBoxField.getItems();
		assertEquals(3, items.size());

		TuleapWorkflow workflow = selectBoxField.getWorkflow();
		assertNotNull(workflow);
		assertTrue(workflow.hasTransitions());
		Collection<TuleapSelectBoxItem> accessibleStates = workflow
				.accessibleStates(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID);
		assertEquals(1, accessibleStates.size());
		assertEquals(334, accessibleStates.iterator().next().getIdentifier());
		accessibleStates = workflow.accessibleStates(334);
		assertEquals(2, accessibleStates.size());
		assertTrue(accessibleStates.containsAll(Arrays.asList(selectBoxField.getItem("337"), selectBoxField
				.getItem("338"))));
		accessibleStates = workflow.accessibleStates(337);
		assertEquals(1, accessibleStates.size());
		assertEquals(338, accessibleStates.iterator().next().getIdentifier());
		accessibleStates = workflow.accessibleStates(338);
		assertTrue(accessibleStates.isEmpty());

		assertEquals(1, tracker.getTrackerResources().length);

		TuleapResource tuleapResource = tracker.getTrackerResources()[0];
		assertEquals("reports", tuleapResource.getType()); //$NON-NLS-1$
		assertEquals("trackers/9/tracker_reports", tuleapResource.getUri()); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of a tracker with a workflow that is <code>null</code>.
	 */
	@Test
	public void testTrackerWorkflowNullParsing() {
		String json = ParserUtil.loadFile("/trackers/tracker-10.json");
		TuleapTracker tracker = gson.fromJson(json, TuleapTracker.class);

		assertNotNull(tracker);
		assertEquals(10, tracker.getIdentifier());
		assertEquals("Bugs", tracker.getLabel()); //$NON-NLS-1$
		assertEquals("localhost: 3001/plugins/tracker/?tracker=10", tracker.getUrl()); //$NON-NLS-1$
		assertEquals("trackers/10", tracker.getUri()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = tracker.getFields();
		assertEquals(1, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();

		// testing the semantic title
		AbstractTuleapField firstField = iterator.next();
		TuleapString stringField = (TuleapString)firstField;
		assertTrue(firstField instanceof TuleapString);
		assertTrue(stringField.isSemanticTitle());

		// testing the field permissions
		assertTrue(firstField.isReadable());
		assertTrue(firstField.isSubmitable());
		assertTrue(firstField.isUpdatable());

		assertEquals(1, tracker.getTrackerResources().length);

		TuleapResource tuleapResource = tracker.getTrackerResources()[0];
		assertEquals("reports", tuleapResource.getType()); //$NON-NLS-1$
		assertEquals("trackers/10/tracker_reports", tuleapResource.getUri()); //$NON-NLS-1$
	}

	/**
	 * Test parsing a complete changeSet .
	 */
	@Test
	public void testParsingCompleteChangeSetX() {
		String json = ParserUtil.loadFile("/changesets/changeset-1.json");
		TuleapElementComment changeSet = gson.fromJson(json, TuleapElementComment.class);
		assertNotNull(changeSet);
		assertEquals("First comment body", changeSet.getBody()); //$NON-NLS-1$
		assertEquals(1393518580, changeSet.getSubmittedOn());
		assertEquals(101, changeSet.getSubmitter().getId());
		assertEquals("email@obeo.fr", changeSet.getSubmitter().getEmail()); //$NON-NLS-1$
	}

	/**
	 * Test parsing a changeSet with empty body .
	 */
	@Test
	public void testParsingChangeSetWithEmptyBody() {
		String json = ParserUtil.loadFile("/changesets/changeset-2.json");
		TuleapElementComment changeSet = gson.fromJson(json, TuleapElementComment.class);
		assertNotNull(changeSet);
		assertEquals("", changeSet.getBody()); //$NON-NLS-1$
		assertEquals(1393518580, changeSet.getSubmittedOn());
		assertEquals(101, changeSet.getSubmitter().getId());
		assertEquals("email@obeo.fr", changeSet.getSubmitter().getEmail()); //$NON-NLS-1$
	}

	/**
	 * Test parsing a changeSet with body null.
	 */
	@Test
	public void testParsingChangeSetWithBodyNull() {
		String json = ParserUtil.loadFile("/changesets/changeset-3.json");
		TuleapElementComment changeSet = gson.fromJson(json, TuleapElementComment.class);
		assertNotNull(changeSet);
		assertNull(changeSet.getBody());
		assertEquals(1393518580, changeSet.getSubmittedOn());
		assertEquals(101, changeSet.getSubmitter().getId());
		assertEquals("email@obeo.fr", changeSet.getSubmitter().getEmail()); //$NON-NLS-1$
	}

	/**
	 * Test parsing a changeSet with null mail.
	 */
	@Test
	public void testParsingChangeSetWithEmailNull() {
		String json = ParserUtil.loadFile("/changesets/changeset-4.json");
		TuleapElementComment changeSet = gson.fromJson(json, TuleapElementComment.class);
		assertNotNull(changeSet);
		assertEquals("Fourth comment body", changeSet.getBody()); //$NON-NLS-1$
		assertEquals(1393518580, changeSet.getSubmittedOn());
		assertEquals(101, changeSet.getSubmitter().getId());
		assertNull(changeSet.getSubmitter().getEmail());
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
