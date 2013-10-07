/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.internal.tests.repository;

import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.internal.tasks.core.LocalTask;
import org.eclipse.mylyn.internal.tasks.core.RepositoryQuery;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskMapper;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.client.ITuleapQueryConstants;
import org.tuleap.mylyn.task.internal.core.client.TuleapClientManager;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapClient;
import org.tuleap.mylyn.task.internal.core.data.TuleapConfigurableElementMapper;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskIdentityUtil;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableFieldsConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapPlanningBinding;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapTopPlanning;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.repository.TuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.repository.TuleapTaskDataCollector;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link TuleapRepositoryConnector}.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapRepositoryConnectorTests {

	/**
	 * Test the retrieval of the repository url from a given task url.
	 */
	@Test
	public void testGetRepositoryUrlFromTaskUrl() {
		final String taskUrl = "https://tuleap.net/plugins/tracker/?group_id=409&tracker=55&aid=453"; //$NON-NLS-1$

		TuleapRepositoryConnector connector = new TuleapRepositoryConnector();
		String repositoryUrl = connector.getRepositoryUrlFromTaskUrl(taskUrl);
		assertEquals("https://tuleap.net", repositoryUrl); //$NON-NLS-1$
	}

	/**
	 * Test the retrieval of the task id from the url of the task.
	 */
	@Test
	public void testGetTaskIdFromTaskUrl() {
		final String taskUrl = "https://demo.tuleap.net/plugins/tracker/?group_id=409&tracker=55&aid=217"; //$NON-NLS-1$

		TuleapRepositoryConnector connector = new TuleapRepositoryConnector();
		String taskId = connector.getTaskIdFromTaskUrl(taskUrl);
		assertEquals(TuleapTaskIdentityUtil.getTaskDataId(409, 55, 217), taskId);
	}

	/**
	 * Test the creation of the task url from the repository url and the task id.
	 */
	@Test
	public void testGetTaskUrl() {
		final String repositoryUrl = "https://tuleap.net"; //$NON-NLS-1$
		final String taskId = TuleapTaskIdentityUtil.getTaskDataId(409, 31, 821);

		TuleapRepositoryConnector connector = new TuleapRepositoryConnector();
		String taskUrl = connector.getTaskUrl(repositoryUrl, taskId);
		assertEquals("https://tuleap.net/plugins/tracker/?group_id=409&tracker=31&aid=821", taskUrl); //$NON-NLS-1$
	}

	/**
	 * Test if the task has changed.
	 */
	@Test
	public void testHasTaskChanged() {
		TuleapRepositoryConnector tuleapRepositoryConnector = new TuleapRepositoryConnector();

		Date dateModified = new Date();

		ITask task = new LocalTask("https://tuleap.net", "123:456#789");
		task.setModificationDate(dateModified);

		TaskData taskData = new TaskData(new TaskAttributeMapper(new TaskRepository(
				ITuleapConstants.CONNECTOR_KIND, "https://tuleap.net")), ITuleapConstants.CONNECTOR_KIND,
				"https://tuleap.net", "123:456#789");

		TaskMapper mapper = new TaskMapper(taskData, true);
		mapper.setModificationDate(dateModified);

		boolean hasTaskChanged = tuleapRepositoryConnector.hasTaskChanged(null, task, taskData);
		assertThat(hasTaskChanged, is(false));

		TaskData taskData2 = new TaskData(new TaskAttributeMapper(new TaskRepository(
				ITuleapConstants.CONNECTOR_KIND, "https://tuleap.net")), ITuleapConstants.CONNECTOR_KIND,
				"https://tuleap.net", "123:456#789");

		mapper = new TaskMapper(taskData, true);
		mapper.setModificationDate(new Date());

		hasTaskChanged = tuleapRepositoryConnector.hasTaskChanged(null, task, taskData2);
		assertThat(hasTaskChanged, is(true));
	}

	/**
	 * Test the execution of a query to retrieve all the artifacts from a given tracker.
	 */
	@Test
	public void testPerformQueryAllFromTracker() {
		int projectId = 979;
		int trackerId = 42;
		int artifactId = 121;

		final TuleapServerConfiguration tuleapServerConfiguration = new TuleapServerConfiguration(
				"https://tuleap.net");

		TuleapProjectConfiguration tuleapProjectConfiguration = new TuleapProjectConfiguration("", projectId);
		TuleapTrackerConfiguration trackerConfiguration = new TuleapTrackerConfiguration(trackerId, null,
				null, null, null, 0);
		tuleapProjectConfiguration.addTracker(trackerConfiguration);

		tuleapServerConfiguration.addProject(tuleapProjectConfiguration);

		final TuleapArtifact tuleapArtifact = new TuleapArtifact(artifactId, trackerId, projectId, "", "",
				"", new Date(), new Date());

		final TuleapClientManager tuleapClientManager = new TuleapClientManager() {
			@Override
			public TuleapSoapClient getSoapClient(TaskRepository taskRepository) {
				return new TuleapSoapClient(null, null, null, null, null) {
					@Override
					public List<TuleapArtifact> getArtifactsFromQuery(IRepositoryQuery query,
							TuleapServerConfiguration serverConfiguration,
							TuleapTrackerConfiguration tuleapTrackerConfiguration, IProgressMonitor monitor) {
						return Lists.newArrayList(tuleapArtifact);
					}
				};
			}
		};

		TuleapRepositoryConnector tuleapRepositoryConnector = new TuleapRepositoryConnector() {
			@Override
			public TuleapServerConfiguration getTuleapServerConfiguration(String repositoryUrl) {
				return tuleapServerConfiguration;
			}

			@Override
			public AbstractTuleapConfigurableFieldsConfiguration refreshConfiguration(
					TaskRepository taskRepository,
					AbstractTuleapConfigurableFieldsConfiguration configuration, IProgressMonitor monitor)
					throws CoreException {
				return configuration;
			}

			@Override
			public TuleapClientManager getClientManager() {
				return tuleapClientManager;
			}
		};

		TaskRepository taskRepository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND,
				"https://tuleap.net");

		// All from tracker
		TuleapTaskDataCollector collector = new TuleapTaskDataCollector();

		IRepositoryQuery query = new RepositoryQuery(ITuleapConstants.CONNECTOR_KIND, "");
		query.setAttribute(ITuleapQueryConstants.QUERY_KIND,
				ITuleapQueryConstants.QUERY_KIND_ALL_FROM_TRACKER);
		query.setAttribute(ITuleapQueryConstants.QUERY_CONFIGURATION_ID, String.valueOf(trackerId));

		tuleapRepositoryConnector.performQuery(taskRepository, query, collector, null,
				new NullProgressMonitor());

		assertThat(collector.getTaskData().size(), is(1));
		assertThat(collector.getTaskData().iterator().next().getTaskId(), is(TuleapTaskIdentityUtil
				.getTaskDataId(projectId, trackerId, artifactId)));
	}

	/**
	 * Test the execution of a query to retrieve all the artifacts from a given tracker.
	 */
	@Test
	public void testPerformQueryReport() {
		int projectId = 979;
		int trackerId = 42;
		int artifactId = 121;

		final TuleapServerConfiguration tuleapServerConfiguration = new TuleapServerConfiguration(
				"https://tuleap.net");

		TuleapProjectConfiguration tuleapProjectConfiguration = new TuleapProjectConfiguration("", projectId);
		TuleapTrackerConfiguration trackerConfiguration = new TuleapTrackerConfiguration(trackerId, null,
				null, null, null, 0);
		tuleapProjectConfiguration.addTracker(trackerConfiguration);

		tuleapServerConfiguration.addProject(tuleapProjectConfiguration);

		final TuleapArtifact tuleapArtifact = new TuleapArtifact(artifactId, trackerId, projectId, "", "",
				"", new Date(), new Date());

		final TuleapClientManager tuleapClientManager = new TuleapClientManager() {
			@Override
			public TuleapSoapClient getSoapClient(TaskRepository taskRepository) {
				return new TuleapSoapClient(null, null, null, null, null) {
					@Override
					public List<TuleapArtifact> getArtifactsFromQuery(IRepositoryQuery query,
							TuleapServerConfiguration serverConfiguration,
							TuleapTrackerConfiguration tuleapTrackerConfiguration, IProgressMonitor monitor) {
						return Lists.newArrayList(tuleapArtifact);
					}
				};
			}
		};

		TuleapRepositoryConnector tuleapRepositoryConnector = new TuleapRepositoryConnector() {
			@Override
			public TuleapServerConfiguration getTuleapServerConfiguration(String repositoryUrl) {
				return tuleapServerConfiguration;
			}

			@Override
			public AbstractTuleapConfigurableFieldsConfiguration refreshConfiguration(
					TaskRepository taskRepository,
					AbstractTuleapConfigurableFieldsConfiguration configuration, IProgressMonitor monitor)
					throws CoreException {
				return configuration;
			}

			@Override
			public TuleapClientManager getClientManager() {
				return tuleapClientManager;
			}
		};

		TaskRepository taskRepository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND,
				"https://tuleap.net");

		// All from tracker
		TuleapTaskDataCollector collector = new TuleapTaskDataCollector();

		IRepositoryQuery query = new RepositoryQuery(ITuleapConstants.CONNECTOR_KIND, "");
		query.setAttribute(ITuleapQueryConstants.QUERY_KIND, ITuleapQueryConstants.QUERY_KIND_REPORT);
		query.setAttribute(ITuleapQueryConstants.QUERY_CONFIGURATION_ID, String.valueOf(trackerId));

		tuleapRepositoryConnector.performQuery(taskRepository, query, collector, null,
				new NullProgressMonitor());

		assertThat(collector.getTaskData().size(), is(1));
		assertThat(collector.getTaskData().iterator().next().getTaskId(), is(TuleapTaskIdentityUtil
				.getTaskDataId(projectId, trackerId, artifactId)));
	}

	/**
	 * Test the execution of a query to retrieve all the artifacts from a given tracker.
	 */
	@Test
	public void testPerformQueryCustom() {
		int projectId = 979;
		int trackerId = 42;
		int artifactId = 121;

		final TuleapServerConfiguration tuleapServerConfiguration = new TuleapServerConfiguration(
				"https://tuleap.net");

		TuleapProjectConfiguration tuleapProjectConfiguration = new TuleapProjectConfiguration("", projectId);
		TuleapTrackerConfiguration trackerConfiguration = new TuleapTrackerConfiguration(trackerId, null,
				null, null, null, 0);
		tuleapProjectConfiguration.addTracker(trackerConfiguration);

		tuleapServerConfiguration.addProject(tuleapProjectConfiguration);

		final TuleapArtifact tuleapArtifact = new TuleapArtifact(artifactId, trackerId, projectId, "", "",
				"", new Date(), new Date());

		final TuleapClientManager tuleapClientManager = new TuleapClientManager() {
			@Override
			public TuleapSoapClient getSoapClient(TaskRepository taskRepository) {
				return new TuleapSoapClient(null, null, null, null, null) {
					@Override
					public List<TuleapArtifact> getArtifactsFromQuery(IRepositoryQuery query,
							TuleapServerConfiguration serverConfiguration,
							TuleapTrackerConfiguration tuleapTrackerConfiguration, IProgressMonitor monitor) {
						return Lists.newArrayList(tuleapArtifact);
					}
				};
			}
		};

		TuleapRepositoryConnector tuleapRepositoryConnector = new TuleapRepositoryConnector() {
			@Override
			public TuleapServerConfiguration getTuleapServerConfiguration(String repositoryUrl) {
				return tuleapServerConfiguration;
			}

			@Override
			public AbstractTuleapConfigurableFieldsConfiguration refreshConfiguration(
					TaskRepository taskRepository,
					AbstractTuleapConfigurableFieldsConfiguration configuration, IProgressMonitor monitor)
					throws CoreException {
				return configuration;
			}

			@Override
			public TuleapClientManager getClientManager() {
				return tuleapClientManager;
			}
		};

		TaskRepository taskRepository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND,
				"https://tuleap.net");

		// All from tracker
		TuleapTaskDataCollector collector = new TuleapTaskDataCollector();

		IRepositoryQuery query = new RepositoryQuery(ITuleapConstants.CONNECTOR_KIND, "");
		query.setAttribute(ITuleapQueryConstants.QUERY_KIND, ITuleapQueryConstants.QUERY_KIND_CUSTOM);
		query.setAttribute(ITuleapQueryConstants.QUERY_CONFIGURATION_ID, String.valueOf(trackerId));

		tuleapRepositoryConnector.performQuery(taskRepository, query, collector, null,
				new NullProgressMonitor());

		assertThat(collector.getTaskData().size(), is(1));
		assertThat(collector.getTaskData().iterator().next().getTaskId(), is(TuleapTaskIdentityUtil
				.getTaskDataId(projectId, trackerId, artifactId)));
	}

	/**
	 * Test the execution of a query to retrieve all the artifacts from a given tracker.
	 */
	@Test
	public void testPerformQueryTopLevelPlanning() {
		int projectId = 979;
		int topPlanningId = 121;

		final TuleapServerConfiguration tuleapServerConfiguration = new TuleapServerConfiguration(
				"https://tuleap.net");

		TuleapProjectConfiguration tuleapProjectConfiguration = new TuleapProjectConfiguration("", projectId);
		tuleapServerConfiguration.addProject(tuleapProjectConfiguration);

		final TuleapTopPlanning tuleapTopPlanning = new TuleapTopPlanning(topPlanningId, "Top planning",
				"/top/planning", "/planning/top", projectId, new TuleapPlanningBinding(123, 321));

		final TuleapClientManager tuleapClientManager = new TuleapClientManager() {
			@Override
			public TuleapRestClient getRestClient(TaskRepository taskRepository) {
				return new TuleapRestClient(null, null, null, null, null) {
					@Override
					public List<TuleapTopPlanning> getTopPlannings(int id, IProgressMonitor monitor)
							throws CoreException {
						return Lists.newArrayList(tuleapTopPlanning);
					}
				};
			}
		};

		TuleapRepositoryConnector tuleapRepositoryConnector = new TuleapRepositoryConnector() {
			@Override
			public TuleapServerConfiguration getTuleapServerConfiguration(String repositoryUrl) {
				return tuleapServerConfiguration;
			}

			@Override
			public TuleapClientManager getClientManager() {
				return tuleapClientManager;
			}
		};

		TaskRepository taskRepository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND,
				"https://tuleap.net");

		// All from tracker
		TuleapTaskDataCollector collector = new TuleapTaskDataCollector();

		IRepositoryQuery query = new RepositoryQuery(ITuleapConstants.CONNECTOR_KIND, "");
		query.setAttribute(ITuleapQueryConstants.QUERY_KIND,
				ITuleapQueryConstants.QUERY_KIND_TOP_LEVEL_PLANNING);
		query.setAttribute(ITuleapQueryConstants.QUERY_PROJECT_ID, String.valueOf(projectId));

		tuleapRepositoryConnector.performQuery(taskRepository, query, collector, null,
				new NullProgressMonitor());

		assertThat(collector.getTaskData().size(), is(1));
		assertThat(collector.getTaskData().iterator().next().getTaskId(), is(TuleapTaskIdentityUtil
				.getTaskDataId(projectId, TuleapTaskIdentityUtil.IRRELEVANT_ID, topPlanningId)));
	}

	/**
	 * Test the update of the task from the given task data.
	 */
	@Test
	public void testUpdateTaskFromTaskData() {
		TaskRepository taskRepository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND,
				"https://tuleap.net");

		int projectId = 789;
		int configurationId = 456;
		int elementId = 123;
		String taskId = TuleapTaskIdentityUtil.getTaskDataId(projectId, configurationId, elementId);

		ITask task = new LocalTask(taskId, "");

		TaskData taskData = new TaskData(new TaskAttributeMapper(taskRepository),
				ITuleapConstants.CONNECTOR_KIND, "https://tuleap.net", taskId);

		int openStatusId = 0;
		int closedStatusId = 1;

		final TuleapServerConfiguration tuleapServerConfiguration = new TuleapServerConfiguration(
				"https://tuleap.net");
		TuleapProjectConfiguration tuleapProjectConfiguration = new TuleapProjectConfiguration(null,
				projectId);

		TuleapTrackerConfiguration trackerConfiguration = new TuleapTrackerConfiguration(configurationId,
				null, null, null, null, 0);

		TuleapSelectBox tuleapSelectBox = new TuleapSelectBox(0);
		TuleapSelectBoxItem item = new TuleapSelectBoxItem(0);
		item.setLabel("OPEN");
		tuleapSelectBox.addItem(item);
		tuleapSelectBox.getOpenStatus().add(item);

		item = new TuleapSelectBoxItem(1);
		item.setLabel("CLOSED");
		tuleapSelectBox.addItem(item);

		trackerConfiguration.addField(tuleapSelectBox);

		tuleapProjectConfiguration.addTracker(trackerConfiguration);
		tuleapServerConfiguration.addProject(tuleapProjectConfiguration);

		TuleapConfigurableElementMapper mapper = new TuleapConfigurableElementMapper(taskData,
				trackerConfiguration);
		mapper.initializeEmptyTaskData();
		mapper.setStatus(openStatusId);

		TuleapRepositoryConnector tuleapRepositoryConnector = new TuleapRepositoryConnector() {
			@Override
			public TuleapServerConfiguration getTuleapServerConfiguration(String repositoryUrl) {
				return tuleapServerConfiguration;
			}
		};
		tuleapRepositoryConnector.updateTaskFromTaskData(taskRepository, task, taskData);
		assertThat(task.isCompleted(), is(false));
		assertThat(task.getCompletionDate(), nullValue());

		mapper.setModificationDate(new Date());
		mapper.setStatus(closedStatusId);

		tuleapRepositoryConnector.updateTaskFromTaskData(taskRepository, task, taskData);
		assertThat(task.isCompleted(), is(true));
		assertThat(task.getCompletionDate(), notNullValue());
	}
}
