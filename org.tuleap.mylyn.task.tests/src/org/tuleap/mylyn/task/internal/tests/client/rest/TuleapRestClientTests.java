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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.client.rest.ICredentials;
import org.tuleap.mylyn.task.internal.core.client.rest.ITuleapAPIVersions;
import org.tuleap.mylyn.task.internal.core.client.rest.ITuleapServerStatus;
import org.tuleap.mylyn.task.internal.core.client.rest.RestCards;
import org.tuleap.mylyn.task.internal.core.client.rest.RestMilestones;
import org.tuleap.mylyn.task.internal.core.client.rest.RestMilestonesBacklogItems;
import org.tuleap.mylyn.task.internal.core.client.rest.RestResources;
import org.tuleap.mylyn.task.internal.core.client.rest.ServerResponse;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestConnector;
import org.tuleap.mylyn.task.internal.core.data.AttachmentFieldValue;
import org.tuleap.mylyn.task.internal.core.data.AttachmentValue;
import org.tuleap.mylyn.task.internal.core.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.TuleapPerson;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapCard;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapCardwall;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapSwimlane;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonParser;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonSerializer;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import static org.junit.Assert.fail;

/**
 * Test class for {@link TuleapRestClient}.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapRestClientTests {
	/**
	 * Test that the milestone update is well done.
	 */
	@Test
	public void testUpdateMilestone() {
		ILog logger = null;
		TuleapJsonParser jsonParser = null;

		String url = "https://tuleap.net"; //$NON-NLS-1$

		TuleapJsonSerializer jsonSerializer = new TuleapJsonSerializer();

		final RestMilestonesBacklogItems milestonesBacklogItems = new RestMilestonesBacklogItems(null, null,
				null, 10) {
			@Override
			public void checkPut(Map<String, String> headers) throws CoreException {
				// do nothing;
			}

			@Override
			public ServerResponse put(Map<String, String> headers, String body) {
				assertThat(body, is(notNullValue()));
				return new ServerResponse(ITuleapServerStatus.OK, null, null);
			}
		};

		final RestMilestones milestonesFields = new RestMilestones(null, null, null, 20) {
			@Override
			public void checkPut(Map<String, String> headers) throws CoreException {
				// do nothing;
			}

			@Override
			public ServerResponse put(Map<String, String> headers, String body) {
				assertThat(body, is(notNullValue()));
				return new ServerResponse(ITuleapServerStatus.OK, null, null);
			}
		};

		final RestCards cards = new RestCards(null, null, null, 30) {
			@Override
			public void checkPut(Map<String, String> headers) throws CoreException {
				// do nothing;
			}

			@Override
			public ServerResponse put(Map<String, String> headers, String body) {
				assertThat(body, is(notNullValue()));
				return new ServerResponse(ITuleapServerStatus.OK, null, null);
			}
		};

		final RestResources restResources = new RestResources(null, null, null) {
			@Override
			public RestMilestonesBacklogItems milestonesBacklogItems(int milestoneId) {
				return milestonesBacklogItems;
			}

			@Override
			public RestMilestones milestone(int milestoneId) {
				return milestonesFields;
			}

			@Override
			public RestCards milestonesCards(int cardId) {
				return cards;
			}

		};

		TuleapRestConnector tuleapRestConnector = new TuleapRestConnector(url,
				ITuleapAPIVersions.BEST_VERSION, logger) {
			@Override
			public RestResources resources(ICredentials credentials) throws CoreException {
				return restResources;
			}
		};

		TaskRepository taskRepository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, url);

		TuleapRestClient tuleapRestClient = new TuleapRestClient(tuleapRestConnector, jsonParser,
				jsonSerializer, taskRepository, logger);

		TuleapMilestone tuleapMilestone = new TuleapMilestone(50, 500, 200, "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", new Date(), new Date()); //$NON-NLS-1$

		TuleapMilestone submilestone = new TuleapMilestone(100, 500, 200, "submilestone100", "URL", //$NON-NLS-1$//$NON-NLS-2$
				"HTML URL", new Date(), new Date()); //$NON-NLS-1$

		tuleapMilestone.addSubMilestone(submilestone);

		// the backlogItem
		TuleapBacklogItem item = new TuleapBacklogItem(200, 1000, 200, "item", null, null, null, null); //$NON-NLS-1$
		item.setAssignedMilestoneId(submilestone.getId());
		tuleapMilestone.addBacklogItem(item);

		// the literal field value
		LiteralFieldValue firstLiteralFieldValue = new LiteralFieldValue(1000, "300, 301, 302"); //$NON-NLS-1$
		tuleapMilestone.addFieldValue(firstLiteralFieldValue);

		// the bound field value
		List<Integer> valueIds = new ArrayList<Integer>();
		valueIds.add(new Integer(1));
		valueIds.add(new Integer(2));
		valueIds.add(new Integer(3));
		BoundFieldValue firstBoundFieldValue = new BoundFieldValue(2000, valueIds);
		tuleapMilestone.addFieldValue(firstBoundFieldValue);

		// the file attachment
		List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		TuleapPerson firstUploadedBy = new TuleapPerson("first username", "first realname", 1, "first email"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attachments.add(new AttachmentValue("100000", "first name", firstUploadedBy, 123456, //$NON-NLS-1$ //$NON-NLS-2$ 
				"first description", "first type")); //$NON-NLS-1$ //$NON-NLS-2$

		TuleapPerson secondUploadedBy = new TuleapPerson("second username", "second realname", 2, //$NON-NLS-1$ //$NON-NLS-2$
				"second email"); //$NON-NLS-1$
		attachments.add(new AttachmentValue("100001", "second name", secondUploadedBy, 789456, //$NON-NLS-1$ //$NON-NLS-2$
				"second description", "second type")); //$NON-NLS-1$ //$NON-NLS-2$

		AttachmentFieldValue fileDescriptions = new AttachmentFieldValue(3000, attachments);
		tuleapMilestone.addFieldValue(fileDescriptions);

		// the milestone cards

		TuleapCardwall cardwall = new TuleapCardwall();
		tuleapMilestone.setCardwall(cardwall);

		TuleapSwimlane firstSwimlane = new TuleapSwimlane();
		TuleapBacklogItem firstBacklogItem = new TuleapBacklogItem(500, 200);
		firstSwimlane.setBacklogItem(firstBacklogItem);
		cardwall.addSwimlane(firstSwimlane);

		TuleapCard firstCard = new TuleapCard(700, 200);
		firstCard.setStatus(10000);
		firstCard.addFieldValue(firstLiteralFieldValue);

		firstSwimlane.addCard(firstCard);

		try {
			tuleapRestClient.updateMilestone(tuleapMilestone, new NullProgressMonitor());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test that the milestone update check is not well done.
	 * 
	 * @throws CoreException
	 *             the thrown exception
	 */
	@Test(expected = CoreException.class)
	public void testUpdateMilestoneFailedCheckPutBacklogItems() throws CoreException {
		ILog logger = null;
		TuleapJsonParser jsonParser = null;

		String url = "https://tuleap.net"; //$NON-NLS-1$

		TuleapJsonSerializer jsonSerializer = new TuleapJsonSerializer();

		final RestMilestonesBacklogItems milestonesBacklogItems = new RestMilestonesBacklogItems(null, null,
				null, 0) {
			@Override
			public void checkPut(Map<String, String> headers) throws CoreException {
				throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
						"CheckPut Failed")); //$NON-NLS-1$
			}

			@Override
			public ServerResponse put(Map<String, String> headers, String body) {
				assertThat(body, is(notNullValue()));
				return new ServerResponse(ITuleapServerStatus.OK, null, null);
			}
		};

		final RestResources restResources = new RestResources(null, null, null) {
			@Override
			public RestMilestonesBacklogItems milestonesBacklogItems(int milestoneId) {
				return milestonesBacklogItems;
			}
		};

		TuleapRestConnector tuleapRestConnector = new TuleapRestConnector(url,
				ITuleapAPIVersions.BEST_VERSION, logger) {
			@Override
			public RestResources resources(ICredentials credentials) throws CoreException {
				return restResources;
			}
		};

		TaskRepository taskRepository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, url);

		TuleapRestClient tuleapRestClient = new TuleapRestClient(tuleapRestConnector, jsonParser,
				jsonSerializer, taskRepository, logger);

		TuleapMilestone tuleapMilestone = new TuleapMilestone(50, 500, 200, "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", new Date(), new Date()); //$NON-NLS-1$

		TuleapMilestone submilestone = new TuleapMilestone(100, 500, 200, "submilestone100", "URL", //$NON-NLS-1$//$NON-NLS-2$
				"HTML URL", new Date(), new Date()); //$NON-NLS-1$

		tuleapMilestone.addSubMilestone(submilestone);

		TuleapBacklogItem item200 = new TuleapBacklogItem(200, 1000, 200, "item200", null, null, null, null); //$NON-NLS-1$
		item200.setAssignedMilestoneId(submilestone.getId());
		tuleapMilestone.addBacklogItem(item200);

		tuleapRestClient.updateMilestone(tuleapMilestone, new NullProgressMonitor());

	}

	/**
	 * Test that the milestone update is not well done.
	 * 
	 * @throws CoreException
	 *             the thrown exception
	 */
	@Test(expected = CoreException.class)
	public void testUpdateMilestoneFailedPutBacklogItems() throws CoreException {
		ILog logger = null;
		TuleapJsonParser jsonParser = null;

		String url = "https://tuleap.net"; //$NON-NLS-1$

		TuleapJsonSerializer jsonSerializer = new TuleapJsonSerializer();

		final RestMilestonesBacklogItems milestonesBacklogItems = new RestMilestonesBacklogItems(null, null,
				null, 0) {
			@Override
			public void checkPut(Map<String, String> headers) throws CoreException {
				// do nothing;
			}

			@Override
			public ServerResponse put(Map<String, String> headers, String body) {
				assertThat(body, is(notNullValue()));
				return new ServerResponse(ITuleapServerStatus.NOT_FOUND, null, null);
			}
		};

		final RestResources restResources = new RestResources(null, null, null) {
			@Override
			public RestMilestonesBacklogItems milestonesBacklogItems(int milestoneId) {
				return milestonesBacklogItems;
			}
		};

		TuleapRestConnector tuleapRestConnector = new TuleapRestConnector(url,
				ITuleapAPIVersions.BEST_VERSION, logger) {
			@Override
			public RestResources resources(ICredentials credentials) throws CoreException {
				return restResources;
			}
		};
		TaskRepository taskRepository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, url);

		TuleapRestClient tuleapRestClient = new TuleapRestClient(tuleapRestConnector, jsonParser,
				jsonSerializer, taskRepository, logger);

		TuleapMilestone tuleapMilestone = new TuleapMilestone(50, 500, 200, "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", new Date(), new Date()); //$NON-NLS-1$

		TuleapMilestone submilestone = new TuleapMilestone(100, 500, 200, "submilestone100", "URL", //$NON-NLS-1$//$NON-NLS-2$
				"HTML URL", new Date(), new Date()); //$NON-NLS-1$

		tuleapMilestone.addSubMilestone(submilestone);

		TuleapBacklogItem item200 = new TuleapBacklogItem(200, 1000, 200, "item200", null, null, null, null); //$NON-NLS-1$
		item200.setAssignedMilestoneId(submilestone.getId());
		tuleapMilestone.addBacklogItem(item200);

		tuleapRestClient.updateMilestone(tuleapMilestone, new NullProgressMonitor());
	}

	/**
	 * Test that the milestone update fields check is not well done.
	 * 
	 * @throws CoreException
	 *             the thrown exception
	 */
	@Test(expected = CoreException.class)
	public void testUpdateMilestoneFailedCheckPutFields() throws CoreException {
		ILog logger = null;
		TuleapJsonParser jsonParser = null;

		String url = "https://tuleap.net"; //$NON-NLS-1$

		TuleapJsonSerializer jsonSerializer = new TuleapJsonSerializer();

		final RestMilestonesBacklogItems milestonesBacklogItems = new RestMilestonesBacklogItems(null, null,
				null, 10) {
			@Override
			public void checkPut(Map<String, String> headers) throws CoreException {
				// do nothing;
			}

			@Override
			public ServerResponse put(Map<String, String> headers, String body) {
				assertThat(body, is(notNullValue()));
				return new ServerResponse(ITuleapServerStatus.OK, null, null);
			}
		};

		final RestMilestones milestonesFields = new RestMilestones(null, null, null, 20) {
			@Override
			public void checkPut(Map<String, String> headers) throws CoreException {
				throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
						"CheckPut Failed")); //$NON-NLS-1$
			}

			@Override
			public ServerResponse put(Map<String, String> headers, String body) {
				assertThat(body, is(notNullValue()));
				return new ServerResponse(ITuleapServerStatus.OK, null, null);
			}
		};

		final RestResources restResources = new RestResources(null, null, null) {
			@Override
			public RestMilestonesBacklogItems milestonesBacklogItems(int milestoneId) {
				return milestonesBacklogItems;
			}

			@Override
			public RestMilestones milestone(int milestoneId) {
				return milestonesFields;
			}
		};

		TuleapRestConnector tuleapRestConnector = new TuleapRestConnector(url,
				ITuleapAPIVersions.BEST_VERSION, logger) {
			@Override
			public RestResources resources(ICredentials credentials) throws CoreException {
				return restResources;
			}
		};

		TaskRepository taskRepository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, url);

		TuleapRestClient tuleapRestClient = new TuleapRestClient(tuleapRestConnector, jsonParser,
				jsonSerializer, taskRepository, logger);

		TuleapMilestone tuleapMilestone = new TuleapMilestone(50, 500, 200, "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", new Date(), new Date()); //$NON-NLS-1$

		TuleapMilestone submilestone = new TuleapMilestone(100, 500, 200, "submilestone100", "URL", //$NON-NLS-1$//$NON-NLS-2$
				"HTML URL", new Date(), new Date()); //$NON-NLS-1$

		tuleapMilestone.addSubMilestone(submilestone);

		// the literal field value
		LiteralFieldValue firstLiteralFieldValue = new LiteralFieldValue(1000, "300, 301, 302"); //$NON-NLS-1$
		tuleapMilestone.addFieldValue(firstLiteralFieldValue);

		// the bound field value
		List<Integer> valueIds = new ArrayList<Integer>();
		valueIds.add(new Integer(1));
		valueIds.add(new Integer(2));
		valueIds.add(new Integer(3));
		BoundFieldValue firstBoundFieldValue = new BoundFieldValue(2000, valueIds);
		tuleapMilestone.addFieldValue(firstBoundFieldValue);

		// the file attachment
		List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		TuleapPerson firstUploadedBy = new TuleapPerson("first username", "first realname", 1, "first email"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attachments.add(new AttachmentValue("100000", "first name", firstUploadedBy, 123456, //$NON-NLS-1$ //$NON-NLS-2$ 
				"first description", "first type")); //$NON-NLS-1$ //$NON-NLS-2$

		TuleapPerson secondUploadedBy = new TuleapPerson("second username", "second realname", 2, //$NON-NLS-1$ //$NON-NLS-2$
				"second email"); //$NON-NLS-1$
		attachments.add(new AttachmentValue("100001", "second name", secondUploadedBy, 789456, //$NON-NLS-1$ //$NON-NLS-2$
				"second description", "second type")); //$NON-NLS-1$ //$NON-NLS-2$

		AttachmentFieldValue fileDescriptions = new AttachmentFieldValue(3000, attachments);
		tuleapMilestone.addFieldValue(fileDescriptions);
		tuleapRestClient.updateMilestone(tuleapMilestone, new NullProgressMonitor());
	}

	/**
	 * Test that the milestone update fields is not well done.
	 * 
	 * @throws CoreException
	 *             the thrown exception
	 */
	@Test(expected = CoreException.class)
	public void testUpdateMilestoneFailedPutFields() throws CoreException {
		ILog logger = null;
		TuleapJsonParser jsonParser = null;

		String url = "https://tuleap.net"; //$NON-NLS-1$

		TuleapJsonSerializer jsonSerializer = new TuleapJsonSerializer();

		final RestMilestonesBacklogItems milestonesBacklogItems = new RestMilestonesBacklogItems(null, null,
				null, 10) {
			@Override
			public void checkPut(Map<String, String> headers) throws CoreException {
				// do nothing;
			}

			@Override
			public ServerResponse put(Map<String, String> headers, String body) {
				assertThat(body, is(notNullValue()));
				return new ServerResponse(ITuleapServerStatus.OK, null, null);
			}
		};

		final RestMilestones milestonesFields = new RestMilestones(null, null, null, 20) {
			@Override
			public void checkPut(Map<String, String> headers) throws CoreException {
				// do nothing;
			}

			@Override
			public ServerResponse put(Map<String, String> headers, String body) {
				assertThat(body, is(notNullValue()));
				return new ServerResponse(ITuleapServerStatus.BAD_REQUEST, null, null);
			}
		};

		final RestResources restResources = new RestResources(null, null, null) {
			@Override
			public RestMilestonesBacklogItems milestonesBacklogItems(int milestoneId) {
				return milestonesBacklogItems;
			}

			@Override
			public RestMilestones milestone(int milestoneId) {
				return milestonesFields;
			}
		};

		TuleapRestConnector tuleapRestConnector = new TuleapRestConnector(url,
				ITuleapAPIVersions.BEST_VERSION, logger) {
			@Override
			public RestResources resources(ICredentials credentials) throws CoreException {
				return restResources;
			}
		};

		TaskRepository taskRepository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, url);

		TuleapRestClient tuleapRestClient = new TuleapRestClient(tuleapRestConnector, jsonParser,
				jsonSerializer, taskRepository, logger);

		TuleapMilestone tuleapMilestone = new TuleapMilestone(50, 500, 200, "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", new Date(), new Date()); //$NON-NLS-1$

		TuleapMilestone submilestone = new TuleapMilestone(100, 500, 200, "submilestone100", "URL", //$NON-NLS-1$//$NON-NLS-2$
				"HTML URL", new Date(), new Date()); //$NON-NLS-1$

		tuleapMilestone.addSubMilestone(submilestone);

		// the backlogItem
		TuleapBacklogItem item = new TuleapBacklogItem(200, 1000, 200, "item", null, null, null, null); //$NON-NLS-1$
		item.setAssignedMilestoneId(submilestone.getId());
		tuleapMilestone.addBacklogItem(item);

		// the literal field value
		LiteralFieldValue firstLiteralFieldValue = new LiteralFieldValue(1000, "300, 301, 302"); //$NON-NLS-1$
		tuleapMilestone.addFieldValue(firstLiteralFieldValue);

		// the bound field value
		List<Integer> valueIds = new ArrayList<Integer>();
		valueIds.add(new Integer(1));
		valueIds.add(new Integer(2));
		valueIds.add(new Integer(3));
		BoundFieldValue firstBoundFieldValue = new BoundFieldValue(2000, valueIds);
		tuleapMilestone.addFieldValue(firstBoundFieldValue);

		// the file attachment
		List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		TuleapPerson firstUploadedBy = new TuleapPerson("first username", "first realname", 1, "first email"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attachments.add(new AttachmentValue("100000", "first name", firstUploadedBy, 123456, //$NON-NLS-1$ //$NON-NLS-2$ 
				"first description", "first type")); //$NON-NLS-1$ //$NON-NLS-2$

		TuleapPerson secondUploadedBy = new TuleapPerson("second username", "second realname", 2, //$NON-NLS-1$ //$NON-NLS-2$
				"second email"); //$NON-NLS-1$
		attachments.add(new AttachmentValue("100001", "second name", secondUploadedBy, 789456, //$NON-NLS-1$ //$NON-NLS-2$
				"second description", "second type")); //$NON-NLS-1$ //$NON-NLS-2$

		AttachmentFieldValue fileDescriptions = new AttachmentFieldValue(3000, attachments);
		tuleapMilestone.addFieldValue(fileDescriptions);

		tuleapRestClient.updateMilestone(tuleapMilestone, new NullProgressMonitor());
	}

	/**
	 * Test that the milestone update cards check is not well done.
	 * 
	 * @throws CoreException
	 *             the thrown exception
	 */
	@Test(expected = CoreException.class)
	public void testUpdateMilestoneFailedCheckPutCards() throws CoreException {
		ILog logger = null;
		TuleapJsonParser jsonParser = null;

		String url = "https://tuleap.net"; //$NON-NLS-1$

		TuleapJsonSerializer jsonSerializer = new TuleapJsonSerializer();

		final RestMilestonesBacklogItems milestonesBacklogItems = new RestMilestonesBacklogItems(null, null,
				null, 10) {
			@Override
			public void checkPut(Map<String, String> headers) throws CoreException {
				// do nothing;
			}

			@Override
			public ServerResponse put(Map<String, String> headers, String body) {
				assertThat(body, is(notNullValue()));
				return new ServerResponse(ITuleapServerStatus.OK, null, null);
			}
		};

		final RestMilestones milestonesFields = new RestMilestones(null, null, null, 20) {
			@Override
			public void checkPut(Map<String, String> headers) throws CoreException {
				// do nothing;
			}

			@Override
			public ServerResponse put(Map<String, String> headers, String body) {
				assertThat(body, is(notNullValue()));
				return new ServerResponse(ITuleapServerStatus.OK, null, null);
			}
		};

		final RestCards cards = new RestCards(null, null, null, 30) {
			@Override
			public void checkPut(Map<String, String> headers) throws CoreException {
				throw new CoreException(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
						"CheckPut Failed")); //$NON-NLS-1$
			}

			@Override
			public ServerResponse put(Map<String, String> headers, String body) {
				assertThat(body, is(notNullValue()));
				return new ServerResponse(ITuleapServerStatus.OK, null, null);
			}
		};

		final RestResources restResources = new RestResources(null, null, null) {
			@Override
			public RestMilestonesBacklogItems milestonesBacklogItems(int milestoneId) {
				return milestonesBacklogItems;
			}

			@Override
			public RestMilestones milestone(int milestoneId) {
				return milestonesFields;
			}

			@Override
			public RestCards milestonesCards(int cardId) {
				return cards;
			}

		};

		TuleapRestConnector tuleapRestConnector = new TuleapRestConnector(url,
				ITuleapAPIVersions.BEST_VERSION, logger) {
			@Override
			public RestResources resources(ICredentials credentials) throws CoreException {
				return restResources;
			}
		};

		TaskRepository taskRepository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, url);

		TuleapRestClient tuleapRestClient = new TuleapRestClient(tuleapRestConnector, jsonParser,
				jsonSerializer, taskRepository, logger);

		TuleapMilestone tuleapMilestone = new TuleapMilestone(50, 500, 200, "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", new Date(), new Date()); //$NON-NLS-1$

		TuleapMilestone submilestone = new TuleapMilestone(100, 500, 200, "submilestone100", "URL", //$NON-NLS-1$//$NON-NLS-2$
				"HTML URL", new Date(), new Date()); //$NON-NLS-1$

		tuleapMilestone.addSubMilestone(submilestone);

		// the milestone cards
		LiteralFieldValue firstLiteralFieldValue = new LiteralFieldValue(1000, "300, 301, 302"); //$NON-NLS-1$
		TuleapCardwall cardwall = new TuleapCardwall();
		tuleapMilestone.setCardwall(cardwall);

		TuleapSwimlane firstSwimlane = new TuleapSwimlane();
		TuleapBacklogItem firstBacklogItem = new TuleapBacklogItem(500, 200);
		firstSwimlane.setBacklogItem(firstBacklogItem);
		cardwall.addSwimlane(firstSwimlane);

		TuleapCard firstCard = new TuleapCard(700, 200);
		firstCard.setStatus(10000);
		firstCard.addFieldValue(firstLiteralFieldValue);

		firstSwimlane.addCard(firstCard);

		tuleapRestClient.updateMilestone(tuleapMilestone, new NullProgressMonitor());
	}

	/**
	 * Test that the milestone update cards is not well done.
	 * 
	 * @throws CoreException
	 *             the thrown exception
	 */
	@Test(expected = CoreException.class)
	public void testUpdateMilestoneFailedPutCards() throws CoreException {
		ILog logger = null;
		TuleapJsonParser jsonParser = null;

		String url = "https://tuleap.net"; //$NON-NLS-1$

		TuleapJsonSerializer jsonSerializer = new TuleapJsonSerializer();

		final RestMilestonesBacklogItems milestonesBacklogItems = new RestMilestonesBacklogItems(null, null,
				null, 10) {
			@Override
			public void checkPut(Map<String, String> headers) throws CoreException {
				// do nothing;
			}

			@Override
			public ServerResponse put(Map<String, String> headers, String body) {
				assertThat(body, is(notNullValue()));
				return new ServerResponse(ITuleapServerStatus.OK, null, null);
			}
		};

		final RestMilestones milestonesFields = new RestMilestones(null, null, null, 20) {
			@Override
			public void checkPut(Map<String, String> headers) throws CoreException {
				// do nothing;
			}

			@Override
			public ServerResponse put(Map<String, String> headers, String body) {
				assertThat(body, is(notNullValue()));
				return new ServerResponse(ITuleapServerStatus.BAD_REQUEST, null, null);
			}
		};

		final RestCards cards = new RestCards(null, null, null, 30) {
			@Override
			public void checkPut(Map<String, String> headers) throws CoreException {
				// do nothing;
			}

			@Override
			public ServerResponse put(Map<String, String> headers, String body) {
				assertThat(body, is(notNullValue()));
				return new ServerResponse(ITuleapServerStatus.BAD_REQUEST, null, null);
			}
		};

		final RestResources restResources = new RestResources(null, null, null) {
			@Override
			public RestMilestonesBacklogItems milestonesBacklogItems(int milestoneId) {
				return milestonesBacklogItems;
			}

			@Override
			public RestMilestones milestone(int milestoneId) {
				return milestonesFields;
			}

			@Override
			public RestCards milestonesCards(int cardId) {
				return cards;
			}

		};

		TuleapRestConnector tuleapRestConnector = new TuleapRestConnector(url,
				ITuleapAPIVersions.BEST_VERSION, logger) {
			@Override
			public RestResources resources(ICredentials credentials) throws CoreException {
				return restResources;
			}
		};

		TaskRepository taskRepository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, url);

		TuleapRestClient tuleapRestClient = new TuleapRestClient(tuleapRestConnector, jsonParser,
				jsonSerializer, taskRepository, logger);

		TuleapMilestone tuleapMilestone = new TuleapMilestone(50, 500, 200, "The first milestone", "URL", //$NON-NLS-1$ //$NON-NLS-2$
				"HTML URL", new Date(), new Date()); //$NON-NLS-1$

		TuleapMilestone submilestone = new TuleapMilestone(100, 500, 200, "submilestone100", "URL", //$NON-NLS-1$//$NON-NLS-2$
				"HTML URL", new Date(), new Date()); //$NON-NLS-1$

		tuleapMilestone.addSubMilestone(submilestone);

		// the milestone cards
		LiteralFieldValue firstLiteralFieldValue = new LiteralFieldValue(1000, "300, 301, 302"); //$NON-NLS-1$
		TuleapCardwall cardwall = new TuleapCardwall();
		tuleapMilestone.setCardwall(cardwall);

		TuleapSwimlane firstSwimlane = new TuleapSwimlane();
		TuleapBacklogItem firstBacklogItem = new TuleapBacklogItem(500, 200);
		firstSwimlane.setBacklogItem(firstBacklogItem);
		cardwall.addSwimlane(firstSwimlane);

		TuleapCard firstCard = new TuleapCard(700, 200);
		firstCard.setStatus(10000);
		firstCard.addFieldValue(firstLiteralFieldValue);

		firstSwimlane.addCard(firstCard);

		tuleapRestClient.updateMilestone(tuleapMilestone, new NullProgressMonitor());
	}

}
