/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.core.tests.internal.repository;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.internal.tasks.core.TaskTask;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.RepositoryResponse;
import org.eclipse.mylyn.tasks.core.RepositoryResponse.ResponseKind;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.SubMilestoneWrapper;
import org.tuleap.mylyn.task.core.internal.client.TuleapClientManager;
import org.tuleap.mylyn.task.core.internal.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.core.internal.data.TuleapArtifactMapper;
import org.tuleap.mylyn.task.core.internal.data.TuleapTaskId;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapPlanning;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapProject;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapServer;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapTracker;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapUser;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapArtifact;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapArtifactWithComment;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapElementComment;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapReference;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapMilestone;
import org.tuleap.mylyn.task.core.internal.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.core.internal.repository.TuleapTaskDataHandler;
import org.tuleap.mylyn.task.core.internal.repository.TuleapTaskMapping;
import org.tuleap.mylyn.task.core.internal.util.ITuleapConstants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * The tests class for the Tuleap task data handler.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapTaskDataHandlerTests {

	/**
	 * The task repository.
	 */
	private TaskRepository repository;

	/**
	 * The server configuration.
	 */
	private TuleapServer tuleapServer;

	/**
	 * The identifier of a standard tracker configuration.
	 */
	private TuleapReference trackerRef = new TuleapReference(1, "trackers/1");

	/**
	 * The identifier of a milestone tracker configuration.
	 */
	private TuleapReference milestoneTrackerRef = new TuleapReference(2, "trackers/2");

	/**
	 * The identifier of a milestone tracker.
	 */
	private int milestoneTrackerId = 2;

	/**
	 * The identifier of a backlog item configuration.
	 */
	private int backlogItemTrackerId = 3;

	/**
	 * The identifier of the project.
	 */
	private TuleapReference projectRef = new TuleapReference(51, "projects/51");

	/**
	 * The identifier of the artifact.
	 */
	private int artifactId = 42;

	/**
	 * The identifier of the milestone.
	 */
	private int milestoneId = 43;

	/**
	 * The identifier of the backlog item.
	 */
	private int backlogItemId = 1234;

	/**
	 * The task data.
	 */
	private TaskData taskData;

	/**
	 * Prepare the Tuleap server configuration and the mock connector.
	 */
	@Before
	public void setUp() {
		String repositoryUrl = "https://test.url";
		this.repository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, repositoryUrl);

		this.tuleapServer = new TuleapServer(repositoryUrl);
		TuleapProject project = new TuleapProject(null, projectRef.getId());

		TuleapTracker tracker = new TuleapTracker(trackerRef.getId(), null, null, null, null, new Date());
		project.addTracker(tracker);

		TuleapTracker milestoneTracker = new TuleapTracker(milestoneTrackerId, null, null, null, null,
				new Date());
		project.addTracker(milestoneTracker);

		TuleapTracker biTracker = new TuleapTracker(backlogItemTrackerId, null, null, null, null, new Date());
		project.addTracker(biTracker);

		TuleapPlanning planning = new TuleapPlanning(123456, projectRef);
		planning.setMilestoneTracker(new TuleapReference(milestoneTrackerId, ""));
		planning.setBacklogTrackers(new TuleapReference[] {new TuleapReference(backlogItemTrackerId, "") });

		project.addPlanning(planning);

		this.tuleapServer.addProject(project);
	}

	/**
	 * Initialize a new task data thanks to the configuration with the given configuration identifier and
	 * check that the dispatch has created a task with the proper task kind.
	 *
	 * @param configurationId
	 *            The identifier of the configuration
	 */
	private void testNewlyInitializedElementKind(int configurationId) {
		ITuleapRepositoryConnector repositoryConnector = new ITuleapRepositoryConnector() {

			@Override
			public TuleapServer getServer(TaskRepository repo) {
				return TuleapTaskDataHandlerTests.this.tuleapServer;
			}

			@Override
			public TuleapClientManager getClientManager() {
				return null;
			}

			@Override
			public TuleapTracker refreshTracker(TaskRepository taskRepository, TuleapTracker configuration,
					IProgressMonitor monitor) throws CoreException {
				return null;
			}

			@Override
			public void refreshProject(TaskRepository taskRepository, TuleapProject project,
					IProgressMonitor monitor) throws CoreException {
				// Nothing to do here
			}
		};

		TuleapTaskDataHandler tuleapTaskDataHandler = new TuleapTaskDataHandler(repositoryConnector);
		TuleapTracker configuration = this.tuleapServer.getProject(projectRef.getId()).getTracker(
				configurationId);

		taskData = new TaskData(tuleapTaskDataHandler.getAttributeMapper(repository),
				ITuleapConstants.CONNECTOR_KIND, this.repository.getRepositoryUrl(), "");

		ITaskMapping initializationData = new TuleapTaskMapping(configuration);

		try {
			boolean isInitialized = tuleapTaskDataHandler.initializeTaskData(repository, taskData,
					initializationData, new NullProgressMonitor());

			assertThat(isInitialized, is(true));
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test that the initialization of an already initialized task data fails.
	 */
	@Test
	public void initializeAlreadyInitializedTaskData() {
		TuleapTaskDataHandler tuleapTaskDataHandler = new TuleapTaskDataHandler(null);

		taskData = new TaskData(tuleapTaskDataHandler.getAttributeMapper(repository),
				ITuleapConstants.CONNECTOR_KIND, "", "test id");
		try {
			boolean isInitialized = tuleapTaskDataHandler.initializeTaskData(this.repository, taskData, null,
					new NullProgressMonitor());
			assertThat(isInitialized, is(false));
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test the creation of an empty task data representing a Tuleap artifact. We won't test here all the
	 * options used in the creation of said task data since other unit tests will handle it.
	 */
	@Test
	public void initializeTaskDataArtifact() {
		testNewlyInitializedElementKind(trackerRef.getId());
	}

	/**
	 * Test the creation of an empty task data representing a Tuleap milestone. We won't test here all the
	 * options used in the creation of said task data since other unit tests will handle it.
	 */
	@Test
	public void initializeTaskDataMilestone() {
		testNewlyInitializedElementKind(milestoneTrackerId);
	}

	/**
	 * Retrieve the artifact with the given task id and check that the task data created has the proper task
	 * kind.
	 *
	 * @param taskId
	 *            The identifier of the task
	 */
	private void testGetTaskData(TuleapTaskId taskId) {
		final TuleapArtifact tuleapArtifact = new TuleapArtifact(artifactId, projectRef, "", "", "",
				new Date(), new Date());
		tuleapArtifact.setTracker(trackerRef);
		tuleapArtifact.setHtmlUrl("/plugins/tracker/?aid=" + taskId.getArtifactId());
		final TuleapMilestone tuleapMilestone = new TuleapMilestone(milestoneId, projectRef, "", "", "",
				new Date(), new Date());
		final TuleapBacklogItem tuleapBacklogItem = new TuleapBacklogItem(backlogItemId, projectRef, "", "",
				"", new Date(), new Date());

		// Mock rest client
		final TuleapRestClient tuleapRestClient = new TuleapRestClient(null, null, null) {
			@Override
			public TuleapMilestone getMilestone(int id, IProgressMonitor monitor) throws CoreException {
				return tuleapMilestone;
			}

			@Override
			public TuleapBacklogItem getBacklogItem(int id, IProgressMonitor monitor) {
				return tuleapBacklogItem;
			}

			@Override
			public TuleapArtifact getArtifact(int id, TuleapServer server, IProgressMonitor monitor)
					throws CoreException {
				return tuleapArtifact;
			}

			@Override
			public List<TuleapBacklogItem> getMilestoneBacklog(int miId, IProgressMonitor monitor)
					throws CoreException {
				return Collections.emptyList();
			}

			@Override
			public List<TuleapBacklogItem> getMilestoneContent(int miId, IProgressMonitor monitor)
					throws CoreException {
				return Collections.emptyList();
			}

			@Override
			public List<TuleapMilestone> getSubMilestones(int miId, IProgressMonitor monitor)
					throws CoreException {
				return Collections.emptyList();
			}

			@Override
			public List<TuleapElementComment> getArtifactComments(int artifactIdentifier,
					TuleapServer server, IProgressMonitor monitor) throws CoreException {
				List<TuleapElementComment> comments = new ArrayList<TuleapElementComment>();

				// The first comment
				String firstCommentBody = "This is the first comment"; //$NON-NLS-1$
				TuleapUser firstCommentSubmitter = new TuleapUser(17);
				firstCommentSubmitter.setEmail("first-email");
				firstCommentSubmitter.setUsername("first-username");
				firstCommentSubmitter.setRealName("first-realname");
				Date commentSubmitDate = new Date();

				TuleapElementComment firstTuleapElementComment = new TuleapElementComment(firstCommentBody,
						firstCommentSubmitter, commentSubmitDate);
				comments.add(firstTuleapElementComment);

				// The second comment
				String secondCommentBody = "This is the second comment"; //$NON-NLS-1$
				TuleapUser secondCommentSubmitter = new TuleapUser(18);
				secondCommentSubmitter.setEmail("second-email");
				secondCommentSubmitter.setUsername("second-username");
				secondCommentSubmitter.setRealName("second-realname");
				TuleapElementComment secondTuleapElementComment = new TuleapElementComment(secondCommentBody,
						secondCommentSubmitter, commentSubmitDate);

				comments.add(secondTuleapElementComment);
				return comments;
			}
		};

		// mock client manager
		final TuleapClientManager tuleapClientManager = new TuleapClientManager() {
			@Override
			public TuleapRestClient getRestClient(TaskRepository taskRepository) {
				return tuleapRestClient;
			}
		};

		// mock repository connector
		ITuleapRepositoryConnector repositoryConnector = new ITuleapRepositoryConnector() {

			@Override
			public TuleapServer getServer(TaskRepository repo) {
				return TuleapTaskDataHandlerTests.this.tuleapServer;
			}

			@Override
			public TuleapClientManager getClientManager() {
				return tuleapClientManager;
			}

			@Override
			public TuleapTracker refreshTracker(TaskRepository taskRepository, TuleapTracker tracker,
					IProgressMonitor monitor) throws CoreException {
				return tracker;
			}

			@Override
			public void refreshProject(TaskRepository taskRepository, TuleapProject project,
					IProgressMonitor monitor) throws CoreException {
				// Nothing to do here
			}
		};

		TuleapTaskDataHandler tuleapTaskDataHandler = new TuleapTaskDataHandler(repositoryConnector);
		try {
			taskData = tuleapTaskDataHandler.getTaskData(this.repository, taskId, new NullProgressMonitor());

			assertThat(taskData, notNullValue());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test the retrieval of the task data representing an existing Tuleap artifact. We won't test here all
	 * the options used during the creation of said task data since other unit tests will handle it.
	 */
	@Test
	public void testGetTaskDataArtifact() {
		this.testGetTaskData(TuleapTaskId.forArtifact(projectRef.getId(), trackerRef.getId(), artifactId));
	}

	/**
	 * Test the retrieval of the task data representing an existing Tuleap milestone. We won't test here all
	 * the options used during the creation of said task data since other unit tests will handle it.
	 */
	@Test
	public void testGetTaskDataMilestone() {
		this.testGetTaskData(TuleapTaskId.forArtifact(projectRef.getId(), milestoneTrackerId, milestoneId));
	}

	/**
	 * Test the retrieval of the task data representing an existing Tuleap backlog item. We won't test here
	 * all the options used during the creation of said task data since other unit tests will handle it.
	 */
	@Test
	public void testGetTaskDataBacklogItem() {
		this.testGetTaskData(TuleapTaskId
				.forArtifact(projectRef.getId(), backlogItemTrackerId, backlogItemId));
	}

	/**
	 * Test allocating the good URL to an artifact
	 */
	@Test
	public void testAllocateUrlToArtifact() {
		this.testGetTaskData(TuleapTaskId.forArtifact(projectRef.getId(), trackerRef.getId(), artifactId));
		TuleapArtifactMapper mapper = new TuleapArtifactMapper(taskData, this.tuleapServer.getProject(
				projectRef.getId()).getTracker(trackerRef.getId()));
		assertEquals("https://test.url/plugins/tracker/?aid=42", mapper.getTaskUrl());

	}

	/**
	 * Test allocating the good URL to a milestone
	 */
	@Test
	public void testAllocateUrlToMilestone() {
		this.testGetTaskData(TuleapTaskId.forArtifact(projectRef.getId(), milestoneTrackerId, milestoneId));
		TuleapArtifactMapper mapper = new TuleapArtifactMapper(taskData, this.tuleapServer.getProject(
				projectRef.getId()).getTracker(trackerRef.getId()));
		assertEquals("https://test.url/plugins/tracker/?aid=43", mapper.getTaskUrl());
	}

	/**
	 * Test allocating the good URL to a BacklogItem
	 */
	@Test
	public void testAllocateUrlToBacklogItem() {
		this.testGetTaskData(TuleapTaskId
				.forArtifact(projectRef.getId(), backlogItemTrackerId, backlogItemId));
		TuleapArtifactMapper mapper = new TuleapArtifactMapper(taskData, this.tuleapServer.getProject(
				projectRef.getId()).getTracker(trackerRef.getId()));
		assertEquals("https://test.url/plugins/tracker/?aid=1234", mapper.getTaskUrl());
	}

	/**
	 * Create and update the element with the given task data and check the identifier of the server element
	 * created and updated.
	 *
	 * @param data
	 *            The task data
	 * @param taskId
	 *            The identifier of the task created and updated
	 * @param responseKind
	 *            The kind of the response
	 * @param isCreation
	 *            Flag indicating whether we want to perform a creation or an update
	 */
	private void testPostTaskData(TaskData data, final TuleapTaskId taskId, ResponseKind responseKind,
			final boolean isCreation) {
		// Mock rest client
		final TuleapRestClient client;
		if (isCreation) {
			client = mockClientForCreate(taskId);
		} else {
			client = mockClientForUpdate(taskId);
		}

		// mock repository connector
		ITuleapRepositoryConnector repositoryConnector = mockConnector(client);

		TuleapTaskDataHandler tuleapTaskDataHandler = new TuleapTaskDataHandler(repositoryConnector);
		try {
			RepositoryResponse response = tuleapTaskDataHandler.postTaskData(this.repository, data, Sets
					.<TaskAttribute> newHashSet(), new NullProgressMonitor());
			assertThat(response.getReposonseKind(), is(responseKind));
			assertThat(response.getTaskId(), is(taskId.toString()));
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test the submission of the new task data representing an existing Tuleap artifact. We won't test here
	 * all the options available in the task data since other unit tests will handle it.
	 */
	@Test
	public void testPostCreateTaskDataArtifact() {
		taskData = new TaskData(new TaskAttributeMapper(this.repository), ITuleapConstants.CONNECTOR_KIND,
				"", "");

		TuleapArtifactMapper mapper = new TuleapArtifactMapper(taskData, this.tuleapServer.getProject(
				projectRef.getId()).getTracker(trackerRef.getId()));
		mapper.initializeEmptyTaskData();

		TuleapTaskId taskId = TuleapTaskId.forArtifact(projectRef.getId(), trackerRef.getId(), artifactId);
		this.testPostTaskData(taskData, taskId, ResponseKind.TASK_CREATED, true);
	}

	/**
	 * Test the submission of the new task data representing an existing Tuleap milestone. We won't test here
	 * all the options available in the task data since other unit tests will handle it.
	 */
	@Test
	public void testPostCreateTaskDataMilestone() {
		taskData = new TaskData(new TaskAttributeMapper(this.repository), ITuleapConstants.CONNECTOR_KIND,
				"", "");

		TuleapArtifactMapper mapper = new TuleapArtifactMapper(taskData, this.tuleapServer.getProject(
				projectRef.getId()).getTracker(milestoneTrackerId));
		mapper.initializeEmptyTaskData();

		TuleapTaskId taskId = TuleapTaskId.forArtifact(projectRef.getId(), milestoneTrackerId, milestoneId);
		this.testPostTaskData(taskData, taskId, ResponseKind.TASK_CREATED, true);
	}

	/**
	 * Test the submission of the new task data representing an existing Tuleap backlog item. We won't test
	 * here all the options available in the task data since other unit tests will handle it.
	 */
	@Test
	public void testPostCreateTaskDataBacklogItem() {
		taskData = new TaskData(new TaskAttributeMapper(this.repository), ITuleapConstants.CONNECTOR_KIND,
				"", "");

		TuleapArtifactMapper mapper = new TuleapArtifactMapper(taskData, this.tuleapServer.getProject(
				projectRef.getId()).getTracker(backlogItemTrackerId));
		mapper.initializeEmptyTaskData();

		TuleapTaskId taskId = TuleapTaskId.forArtifact(projectRef.getId(), backlogItemTrackerId,
				backlogItemId);
		this.testPostTaskData(taskData, taskId, ResponseKind.TASK_CREATED, true);
	}

	/**
	 * Test the submission of the existing task data representing an existing Tuleap artifact. We won't test
	 * here all the options available in the task data since other unit tests will handle it.
	 */
	@Test
	public void testPostUpdateTaskDataArtifact() {
		TuleapTaskId taskId = TuleapTaskId.forArtifact(projectRef.getId(), trackerRef.getId(), artifactId);
		taskData = new TaskData(new TaskAttributeMapper(this.repository), ITuleapConstants.CONNECTOR_KIND,
				"", taskId.toString());

		TuleapArtifactMapper mapper = new TuleapArtifactMapper(taskData, this.tuleapServer.getProject(
				projectRef.getId()).getTracker(trackerRef.getId()));
		mapper.initializeEmptyTaskData();

		this.testPostTaskData(taskData, taskId, ResponseKind.TASK_UPDATED, false);
	}

	/**
	 * Test the submission of the existing task data representing an existing Tuleap milestone. We won't test
	 * here all the options available in the task data since other unit tests will handle it.
	 */
	@Test
	public void testPostUpdateTaskDataMilestone() {
		TuleapTaskId taskId = TuleapTaskId.forArtifact(projectRef.getId(), milestoneTrackerId, milestoneId);
		taskData = new TaskData(new TaskAttributeMapper(this.repository), ITuleapConstants.CONNECTOR_KIND,
				"", taskId.toString());

		TuleapArtifactMapper mapper = new TuleapArtifactMapper(taskData, this.tuleapServer.getProject(
				projectRef.getId()).getTracker(milestoneTrackerId));
		mapper.initializeEmptyTaskData();

		this.testPostTaskData(taskData, taskId, ResponseKind.TASK_UPDATED, false);
	}

	@Test
	public void testPostTopPlanningTaskDataWithoutSubMilestone() throws CoreException {
		TuleapTaskId taskId = TuleapTaskId.forTopPlanning(projectRef.getId());
		taskData = new TaskData(new TaskAttributeMapper(this.repository), ITuleapConstants.CONNECTOR_KIND,
				repository.getUrl(), taskId.toString());

		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		BacklogItemWrapper story500 = addUserStory(wrapper, 500);
		addUserStory(wrapper, 501);
		BacklogItemWrapper story502 = addUserStory(wrapper, 502);
		wrapper.markReference();
		wrapper.moveItems(Arrays.asList(story502), story500, true, null);
		wrapper.mark(wrapper.getBacklogTaskAttribute(), true);

		final int[] calls = new int[1];
		// Only updateTopPlanningBacklog should be called
		FailingRestClient client = new FailingRestClient(null, null, null) {

			@Override
			public void updateTopPlanningBacklog(int projectId, List<TuleapBacklogItem> backlogItems,
					IProgressMonitor monitor) throws CoreException {
				calls[0]++;
				assertEquals(projectRef.getId(), projectId);
				assertEquals(3, backlogItems.size());
				assertEquals(502, backlogItems.get(0).getId().intValue());
				assertEquals(500, backlogItems.get(1).getId().intValue());
				assertEquals(501, backlogItems.get(2).getId().intValue());
			}
		};
		TuleapTaskDataHandler handler = new TuleapTaskDataHandler(mockConnector(client));

		RepositoryResponse response = handler.postTaskData(repository, taskData, Sets
				.<TaskAttribute> newHashSet(), new NullProgressMonitor());
		assertEquals(ResponseKind.TASK_UPDATED, response.getReposonseKind());
		assertEquals(taskId.toString(), response.getTaskId());
		assertEquals(1, calls[0]);
	}

	@Test
	public void testPostTopPlanningTaskDataWithSubMilestone() throws CoreException {
		TuleapTaskId taskId = TuleapTaskId.forTopPlanning(projectRef.getId());
		taskData = new TaskData(new TaskAttributeMapper(this.repository), ITuleapConstants.CONNECTOR_KIND,
				repository.getUrl(), taskId.toString());

		MilestonePlanningWrapper wrapper = new MilestonePlanningWrapper(taskData.getRoot());
		wrapper.setAllowedToHaveSubmilestones(true);
		BacklogItemWrapper story500 = addUserStory(wrapper, 500);
		addUserStory(wrapper, 501);
		addUserStory(wrapper, 502);
		SubMilestoneWrapper subMilestone = addSubMilestone(wrapper, 200);
		addUserStory(subMilestone, 503);
		BacklogItemWrapper story504 = addUserStory(subMilestone, 504);

		wrapper.markReference();

		wrapper.moveItems(Arrays.asList(story500), story504, true, subMilestone);
		wrapper.mark(wrapper.getBacklogTaskAttribute(), true);
		wrapper.mark(subMilestone.getReferenceWrappedAttribute(), true);

		final int[] calls = new int[2];
		// Only updateTopPlanningBacklog should be called
		FailingRestClient client = new FailingRestClient(null, null, null) {
			@Override
			public void updateTopPlanningBacklog(int projectId, List<TuleapBacklogItem> backlogItems,
					IProgressMonitor monitor) throws CoreException {
				calls[0]++;
				assertEquals(projectRef.getId(), projectId);
				assertEquals(2, backlogItems.size());
				assertEquals(501, backlogItems.get(0).getId().intValue());
				assertEquals(502, backlogItems.get(1).getId().intValue());
			}

			@Override
			public void updateMilestoneContent(int smId, List<TuleapBacklogItem> backlogItems,
					IProgressMonitor monitor) throws CoreException {
				calls[1]++;
				assertEquals(200, smId);
				assertEquals(3, backlogItems.size());
				assertEquals(503, backlogItems.get(0).getId().intValue());
				assertEquals(500, backlogItems.get(1).getId().intValue());
				assertEquals(504, backlogItems.get(2).getId().intValue());
			}
		};
		TuleapTaskDataHandler handler = new TuleapTaskDataHandler(mockConnector(client));

		RepositoryResponse response = handler.postTaskData(repository, taskData, Sets
				.<TaskAttribute> newHashSet(), new NullProgressMonitor());
		assertEquals(ResponseKind.TASK_UPDATED, response.getReposonseKind());
		assertEquals(taskId.toString(), response.getTaskId());
		assertEquals(1, calls[0]);
		assertEquals(1, calls[1]);
	}

	@Test
	public void testPostEmptyTopPlanningTaskData() throws CoreException {
		TuleapTaskId taskId = TuleapTaskId.forTopPlanning(projectRef.getId());
		taskData = new TaskData(new TaskAttributeMapper(this.repository), ITuleapConstants.CONNECTOR_KIND,
				repository.getUrl(), taskId.toString());

		// Client should not be called, so nothing to override
		FailingRestClient client = new FailingRestClient(null, null, null);
		TuleapTaskDataHandler handler = new TuleapTaskDataHandler(mockConnector(client));

		RepositoryResponse response = handler.postTaskData(repository, taskData, Sets
				.<TaskAttribute> newHashSet(), new NullProgressMonitor());
		assertEquals(ResponseKind.TASK_UPDATED, response.getReposonseKind());
		assertEquals(taskId.toString(), response.getTaskId());

	}

	private BacklogItemWrapper addUserStory(MilestonePlanningWrapper wrapper, int id) {
		String itemId = TuleapTaskId.forArtifact(projectRef.getId(), trackerRef.getId(), id).toString();
		BacklogItemWrapper item = wrapper.addBacklogItem(itemId);
		item.setDisplayId(Integer.toString(id));
		item.setInitialEffort("2.5");
		item.setLabel("Label " + id);
		item.setStatus("Open");
		item.setType("User Story");
		return item;
	}

	private BacklogItemWrapper addUserStory(SubMilestoneWrapper wrapper, int id) {
		String itemId = TuleapTaskId.forArtifact(projectRef.getId(), trackerRef.getId(), id).toString();
		BacklogItemWrapper item = wrapper.addBacklogItem(itemId);
		item.setDisplayId(Integer.toString(id));
		item.setInitialEffort("2.5");
		item.setLabel("Label " + id);
		item.setStatus("Open");
		item.setType("User Story");
		return item;
	}

	private SubMilestoneWrapper addSubMilestone(MilestonePlanningWrapper wrapper, int id) {
		String smId = TuleapTaskId.forArtifact(projectRef.getId(), milestoneTrackerRef.getId(), id)
				.toString();
		SubMilestoneWrapper sm = wrapper.addSubMilestone(smId);
		sm.setDisplayId(Integer.toString(id));
		sm.setLabel("Label milestone " + id);
		return sm;
	}

	@Test
	public void testCanGetMultiTaskData() {
		TuleapTaskId taskId = TuleapTaskId.forArtifact(projectRef.getId(), milestoneTrackerId, milestoneId);
		TuleapTaskDataHandler handler = new TuleapTaskDataHandler(mockConnector(mockClientForCreate(taskId)));
		assertFalse(handler.canGetMultiTaskData(repository));
	}

	@Test
	public void testCanInitializeSubTaskData() {
		TuleapTaskId taskId = TuleapTaskId.forArtifact(projectRef.getId(), milestoneTrackerId, milestoneId);
		TuleapTaskDataHandler handler = new TuleapTaskDataHandler(mockConnector(mockClientForCreate(taskId)));
		assertFalse(handler.canInitializeSubTaskData(repository, new TaskTask("test", repository.getUrl(),
				taskId.toString())));
	}

	private TuleapRestClient mockClientForUpdate(final TuleapTaskId taskId) {
		final FailingRestClient restClient = new FailingRestClient(null, null, null) {
			@Override
			public void updateArtifact(TuleapArtifactWithComment artifact, IProgressMonitor monitor)
					throws CoreException {
				assertFalse(artifact.isNew());
				// do nothing
			}
		};
		return restClient;
	}

	private TuleapRestClient mockClientForCreate(final TuleapTaskId taskId) {
		final FailingRestClient restClient = new FailingRestClient(null, null, null) {
			@Override
			public TuleapTaskId createArtifact(TuleapArtifact artifact, IProgressMonitor monitor)
					throws CoreException {
				assertTrue(artifact.isNew());
				return TuleapTaskId.forName(taskId.toString());
			}
		};
		return restClient;
	}

	private ITuleapRepositoryConnector mockConnector(final TuleapRestClient restClient) {
		// mock client manager
		final TuleapClientManager clientManager = mockClientManager(restClient);

		ITuleapRepositoryConnector repositoryConnector = new ITuleapRepositoryConnector() {

			@Override
			public TuleapServer getServer(TaskRepository repo) {
				return TuleapTaskDataHandlerTests.this.tuleapServer;
			}

			@Override
			public TuleapClientManager getClientManager() {
				return clientManager;
			}

			@Override
			public TuleapTracker refreshTracker(TaskRepository taskRepository, TuleapTracker tracker,
					IProgressMonitor monitor) throws CoreException {
				return tracker;
			}

			@Override
			public void refreshProject(TaskRepository taskRepository, TuleapProject project,
					IProgressMonitor monitor) throws CoreException {
				// Nothing to do here
			}
		};
		return repositoryConnector;
	}

	private TuleapClientManager mockClientManager(final TuleapRestClient restClient) {
		final TuleapClientManager clientManager = new TuleapClientManager() {
			@Override
			public TuleapRestClient getRestClient(TaskRepository taskRepository) {
				return restClient;
			}
		};
		return clientManager;
	}
}
