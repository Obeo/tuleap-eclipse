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
package org.tuleap.mylyn.task.internal.tests.repository;

import com.google.common.collect.Sets;

import java.util.Date;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.RepositoryResponse;
import org.eclipse.mylyn.tasks.core.RepositoryResponse.ResponseKind;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.AgileTaskKindUtil;
import org.tuleap.mylyn.task.internal.core.client.TuleapClientManager;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapClient;
import org.tuleap.mylyn.task.internal.core.data.TuleapConfigurableElementMapper;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskIdentityUtil;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableFieldsConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItemType;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestoneType;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.repository.TuleapTaskDataHandler;
import org.tuleap.mylyn.task.internal.core.repository.TuleapTaskMapping;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

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
	private TuleapServerConfiguration tuleapServerConfiguration;

	/**
	 * The identifier of the tracker configuration.
	 */
	private int trackerId = 1;

	/**
	 * The identifier of the milestone configuration.
	 */
	private int milestoneTypeId = 2;

	/**
	 * The identifier of the backlog item configuration.
	 */
	private int backlogItemTypeId = 3;

	/**
	 * The identifier of the project.
	 */
	private int projectId = 51;

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
	private int backlogItemId;

	/**
	 * Prepare the Tuleap server configuration and the mock connector.
	 */
	@Before
	public void setUp() {
		String repositoryUrl = "https://tuleap.net";
		this.repository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, repositoryUrl);

		this.tuleapServerConfiguration = new TuleapServerConfiguration(repositoryUrl);
		TuleapProjectConfiguration tuleapProjectConfiguration = new TuleapProjectConfiguration(null,
				projectId);

		TuleapTrackerConfiguration trackerConfiguration = new TuleapTrackerConfiguration(trackerId, null,
				null, null, null, 0);
		tuleapProjectConfiguration.addTracker(trackerConfiguration);

		TuleapMilestoneType milestoneType = new TuleapMilestoneType(milestoneTypeId, null, null, null, null,
				0, false);
		tuleapProjectConfiguration.addMilestoneType(milestoneType);

		TuleapBacklogItemType backlogItemType = new TuleapBacklogItemType(backlogItemTypeId, null, null,
				null, null, 0);
		tuleapProjectConfiguration.addBacklogItemType(backlogItemType);

		this.tuleapServerConfiguration.addProject(tuleapProjectConfiguration);
	}

	/**
	 * Initialize a new task data thanks to the configuration with the given configuration identifier and
	 * check that the dispatch has created a task with the proper task kind.
	 * 
	 * @param configurationId
	 *            The identifier of the configuration
	 * @param taskKind
	 *            The kind of the task
	 */
	private void testNewlyInitializedElementKind(int configurationId, String taskKind) {
		ITuleapRepositoryConnector repositoryConnector = new ITuleapRepositoryConnector() {

			public TuleapServerConfiguration getTuleapServerConfiguration(String repositoryUrl) {
				return TuleapTaskDataHandlerTests.this.tuleapServerConfiguration;
			}

			public TuleapClientManager getClientManager() {
				return null;
			}

			public AbstractTuleapConfigurableFieldsConfiguration refreshConfiguration(
					TaskRepository taskRepository,
					AbstractTuleapConfigurableFieldsConfiguration configuration, IProgressMonitor monitor) {
				return null;
			}
		};

		TuleapTaskDataHandler tuleapTaskDataHandler = new TuleapTaskDataHandler(repositoryConnector);
		AbstractTuleapConfigurableFieldsConfiguration configuration = this.tuleapServerConfiguration
				.getProjectConfiguration(projectId).getConfigurableFieldsConfiguration(configurationId);

		TaskData taskData = new TaskData(tuleapTaskDataHandler.getAttributeMapper(repository),
				ITuleapConstants.CONNECTOR_KIND, this.repository.getRepositoryUrl(), "");

		ITaskMapping initializationData = new TuleapTaskMapping(configuration);

		try {
			boolean isInitialized = tuleapTaskDataHandler.initializeTaskData(repository, taskData,
					initializationData, new NullProgressMonitor());

			assertThat(isInitialized, is(true));
			assertThat(AgileTaskKindUtil.getAgileTaskKind(taskData), is(taskKind));
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

		TaskData taskData = new TaskData(tuleapTaskDataHandler.getAttributeMapper(repository),
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
		testNewlyInitializedElementKind(trackerId, AgileTaskKindUtil.TASK_KIND_ARTIFACT);
	}

	/**
	 * Test the creation of an empty task data representing a Tuleap milestone. We won't test here all the
	 * options used in the creation of said task data since other unit tests will handle it.
	 */
	@Test
	public void initializeTaskDataMilestone() {
		testNewlyInitializedElementKind(milestoneTypeId, AgileTaskKindUtil.TASK_KIND_MILESTONE);
	}

	/**
	 * Test the creation of an empty task data representing a Tuleap backlog item. We won't test here all the
	 * options used in the creation of said task data since other unit tests will handle it.
	 */
	@Test
	public void initializeTaskDataBacklogItem() {
		testNewlyInitializedElementKind(backlogItemTypeId, AgileTaskKindUtil.TASK_KIND_BACKLOG_ITEM);
	}

	/**
	 * Retrieve the artifact with the given task id and check that the task data created has the proper task
	 * kind.
	 * 
	 * @param taskId
	 *            The identifier of the task
	 * @param taskKind
	 *            The kind of the task data created
	 */
	private void testGetTaskData(String taskId, String taskKind) {
		final TuleapArtifact tuleapArtifact = new TuleapArtifact(artifactId, trackerId, projectId, "", "",
				"", new Date(), new Date());
		final TuleapMilestone tuleapMilestone = new TuleapMilestone(milestoneId, this.milestoneTypeId,
				projectId, "", "", "", new Date(), new Date());
		final TuleapBacklogItem tuleapBacklogItem = new TuleapBacklogItem(backlogItemId, backlogItemTypeId,
				projectId, "", "", "", new Date(), new Date());

		// Mock soap client
		final TuleapSoapClient tuleapSoapClient = new TuleapSoapClient(null, null, null, null, null) {
			@Override
			public TuleapArtifact getArtifact(String id, TuleapServerConfiguration serverConfiguration,
					IProgressMonitor monitor) throws CoreException {
				return tuleapArtifact;
			}
		};

		// Mock rest client
		final TuleapRestClient tuleapRestClient = new TuleapRestClient(null, null, null, null, null) {
			@Override
			public TuleapMilestone getMilestone(int id, boolean needsCardwall, IProgressMonitor monitor)
					throws CoreException {
				return tuleapMilestone;
			}

			@Override
			public TuleapBacklogItem getBacklogItem(int id, IProgressMonitor monitor) {
				return tuleapBacklogItem;
			}
		};

		// mock client manager
		final TuleapClientManager tuleapClientManager = new TuleapClientManager() {
			@Override
			public TuleapSoapClient getSoapClient(TaskRepository taskRepository) {
				return tuleapSoapClient;
			}

			@Override
			public TuleapRestClient getRestClient(TaskRepository taskRepository) {
				return tuleapRestClient;
			}
		};

		// mock repository connector
		ITuleapRepositoryConnector repositoryConnector = new ITuleapRepositoryConnector() {

			public TuleapServerConfiguration getTuleapServerConfiguration(String repositoryUrl) {
				return TuleapTaskDataHandlerTests.this.tuleapServerConfiguration;
			}

			public TuleapClientManager getClientManager() {
				return tuleapClientManager;
			}

			public AbstractTuleapConfigurableFieldsConfiguration refreshConfiguration(
					TaskRepository taskRepository,
					AbstractTuleapConfigurableFieldsConfiguration configuration, IProgressMonitor monitor) {
				return configuration;
			}
		};

		TuleapTaskDataHandler tuleapTaskDataHandler = new TuleapTaskDataHandler(repositoryConnector);
		try {
			TaskData taskData = tuleapTaskDataHandler.getTaskData(this.repository, taskId,
					new NullProgressMonitor());

			assertThat(taskData, notNullValue());
			assertThat(AgileTaskKindUtil.getAgileTaskKind(taskData), is(taskKind));
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
		this.testGetTaskData(TuleapTaskIdentityUtil.getTaskDataId(projectId, trackerId, artifactId),
				AgileTaskKindUtil.TASK_KIND_ARTIFACT);
	}

	/**
	 * Test the retrieval of the task data representing an existing Tuleap milestone. We won't test here all
	 * the options used during the creation of said task data since other unit tests will handle it.
	 */
	@Ignore
	@Test
	public void testGetTaskDataMilestone() {
		// TODO SBE remove the ignore when the creation of the milestone is done
		this.testGetTaskData(TuleapTaskIdentityUtil.getTaskDataId(projectId, milestoneTypeId, milestoneId),
				AgileTaskKindUtil.TASK_KIND_MILESTONE);
	}

	/**
	 * Test the retrieval of the task data representing an existing Tuleap backlog item. We won't test here
	 * all the options used during the creation of said task data since other unit tests will handle it.
	 */
	@Ignore
	@Test
	public void testGetTaskDataBacklogItem() {
		// TODO SBE remove the ignore when the creation of the backlog item is done
		this.testGetTaskData(TuleapTaskIdentityUtil
				.getTaskDataId(projectId, backlogItemTypeId, backlogItemId),
				AgileTaskKindUtil.TASK_KIND_BACKLOG_ITEM);
	}

	/**
	 * Create and update the element with the given task data and check the identifier of the server element
	 * created and updated.
	 * 
	 * @param taskData
	 *            The task data
	 * @param taskId
	 *            The identifier of the task created and updated
	 * @param responseKind
	 *            The kind of the response
	 */
	private void testPostTaskData(TaskData taskData, String taskId, ResponseKind responseKind) {
		// Mock soap client
		final TuleapSoapClient tuleapSoapClient = new TuleapSoapClient(null, null, null, null, null) {
			@Override
			public String createArtifact(TuleapArtifact artifact, IProgressMonitor monitor)
					throws CoreException {
				return TuleapTaskIdentityUtil.getTaskDataId(projectId, trackerId, artifactId);
			}

			@Override
			public void updateArtifact(TuleapArtifact artifact, IProgressMonitor monitor)
					throws CoreException {
				// do nothing
			}
		};

		// Mock rest client
		final TuleapRestClient tuleapRestClient = new TuleapRestClient(null, null, null, null, null) {
			@Override
			public String createMilestone(TuleapMilestone tuleapMilestone, IProgressMonitor monitor) {
				return TuleapTaskIdentityUtil.getTaskDataId(projectId, milestoneTypeId, milestoneId);
			}

			@Override
			public void updateMilestone(TuleapMilestone tuleapMilestone, IProgressMonitor monitor) {
				// do nothing
			}
		};

		// mock client manager
		final TuleapClientManager tuleapClientManager = new TuleapClientManager() {
			@Override
			public TuleapSoapClient getSoapClient(TaskRepository taskRepository) {
				return tuleapSoapClient;
			}

			@Override
			public TuleapRestClient getRestClient(TaskRepository taskRepository) {
				return tuleapRestClient;
			}
		};

		// mock repository connector
		ITuleapRepositoryConnector repositoryConnector = new ITuleapRepositoryConnector() {

			public TuleapServerConfiguration getTuleapServerConfiguration(String repositoryUrl) {
				return TuleapTaskDataHandlerTests.this.tuleapServerConfiguration;
			}

			public TuleapClientManager getClientManager() {
				return tuleapClientManager;
			}

			public AbstractTuleapConfigurableFieldsConfiguration refreshConfiguration(
					TaskRepository taskRepository,
					AbstractTuleapConfigurableFieldsConfiguration configuration, IProgressMonitor monitor) {
				return configuration;
			}
		};

		TuleapTaskDataHandler tuleapTaskDataHandler = new TuleapTaskDataHandler(repositoryConnector);
		try {
			RepositoryResponse response = tuleapTaskDataHandler.postTaskData(this.repository, taskData, Sets
					.<TaskAttribute> newHashSet(), new NullProgressMonitor());
			assertThat(response.getReposonseKind(), is(responseKind));
			assertThat(response.getTaskId(), is(taskId));
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
		TaskData taskData = new TaskData(new TaskAttributeMapper(this.repository),
				ITuleapConstants.CONNECTOR_KIND, "", "");

		TuleapConfigurableElementMapper mapper = new TuleapConfigurableElementMapper(taskData,
				this.tuleapServerConfiguration.getProjectConfiguration(projectId)
						.getConfigurableFieldsConfiguration(trackerId));
		mapper.initializeEmptyTaskData();

		String taskId = TuleapTaskIdentityUtil.getTaskDataId(projectId, trackerId, artifactId);
		this.testPostTaskData(taskData, taskId, ResponseKind.TASK_CREATED);
	}

	/**
	 * Test the submission of the new task data representing an existing Tuleap milestone. We won't test here
	 * all the options available in the task data since other unit tests will handle it.
	 */
	@Test
	public void testPostCreateTaskDataMilestone() {
		TaskData taskData = new TaskData(new TaskAttributeMapper(this.repository),
				ITuleapConstants.CONNECTOR_KIND, "", "");

		TuleapConfigurableElementMapper mapper = new TuleapConfigurableElementMapper(taskData,
				this.tuleapServerConfiguration.getProjectConfiguration(projectId)
						.getConfigurableFieldsConfiguration(milestoneTypeId));
		mapper.initializeEmptyTaskData();

		String taskId = TuleapTaskIdentityUtil.getTaskDataId(projectId, milestoneTypeId, milestoneId);
		this.testPostTaskData(taskData, taskId, ResponseKind.TASK_CREATED);
	}

	/**
	 * Test the submission of the new task data representing an existing Tuleap backlog item. We won't test
	 * here all the options available in the task data since other unit tests will handle it.
	 */
	@Ignore
	@Test
	public void testPostCreateTaskDataBacklogItem() {
		TaskData taskData = new TaskData(new TaskAttributeMapper(this.repository),
				ITuleapConstants.CONNECTOR_KIND, "", "");

		TuleapConfigurableElementMapper mapper = new TuleapConfigurableElementMapper(taskData,
				this.tuleapServerConfiguration.getProjectConfiguration(projectId)
						.getConfigurableFieldsConfiguration(backlogItemTypeId));
		mapper.initializeEmptyTaskData();

		String taskId = TuleapTaskIdentityUtil.getTaskDataId(projectId, backlogItemTypeId, backlogItemId);
		this.testPostTaskData(taskData, taskId, ResponseKind.TASK_CREATED);
	}

	/**
	 * Test the submission of the existing task data representing an existing Tuleap artifact. We won't test
	 * here all the options available in the task data since other unit tests will handle it.
	 */
	@Test
	public void testPostUpdateTaskDataArtifact() {
		String taskId = TuleapTaskIdentityUtil.getTaskDataId(projectId, trackerId, artifactId);
		TaskData taskData = new TaskData(new TaskAttributeMapper(this.repository),
				ITuleapConstants.CONNECTOR_KIND, "", taskId);

		TuleapConfigurableElementMapper mapper = new TuleapConfigurableElementMapper(taskData,
				this.tuleapServerConfiguration.getProjectConfiguration(projectId)
						.getConfigurableFieldsConfiguration(trackerId));
		mapper.initializeEmptyTaskData();

		this.testPostTaskData(taskData, taskId, ResponseKind.TASK_UPDATED);
	}

	/**
	 * Test the submission of the existing task data representing an existing Tuleap milestone. We won't test
	 * here all the options available in the task data since other unit tests will handle it.
	 */
	@Test
	public void testPostUpdateTaskDataMilestone() {
		String taskId = TuleapTaskIdentityUtil.getTaskDataId(projectId, milestoneTypeId, milestoneId);
		TaskData taskData = new TaskData(new TaskAttributeMapper(this.repository),
				ITuleapConstants.CONNECTOR_KIND, "", taskId);

		TuleapConfigurableElementMapper mapper = new TuleapConfigurableElementMapper(taskData,
				this.tuleapServerConfiguration.getProjectConfiguration(projectId)
						.getConfigurableFieldsConfiguration(milestoneTypeId));
		mapper.initializeEmptyTaskData();

		this.testPostTaskData(taskData, taskId, ResponseKind.TASK_UPDATED);
	}

	/**
	 * Test the submission of the existing task data representing an existing Tuleap backlog item. We won't
	 * test here all the options available in the task data since other unit tests will handle it.
	 */
	@Ignore
	@Test
	public void testPostUpdateTaskDataBacklogItem() {
		String taskId = TuleapTaskIdentityUtil.getTaskDataId(projectId, backlogItemTypeId, backlogItemId);
		TaskData taskData = new TaskData(new TaskAttributeMapper(this.repository),
				ITuleapConstants.CONNECTOR_KIND, "", taskId);

		TuleapConfigurableElementMapper mapper = new TuleapConfigurableElementMapper(taskData,
				this.tuleapServerConfiguration.getProjectConfiguration(projectId)
						.getConfigurableFieldsConfiguration(backlogItemTypeId));
		mapper.initializeEmptyTaskData();

		this.testPostTaskData(taskData, taskId, ResponseKind.TASK_UPDATED);
	}
}
