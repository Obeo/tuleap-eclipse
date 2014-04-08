/*******************************************************************************
 * Copyright (c) 2004, 2014 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop - initial API and implementation
 *     Obeo - Improvement for Junit 4
 *******************************************************************************/
package org.eclipse.mylyn.tuleap.ui.tests.internal;

import java.util.Collections;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.internal.tasks.ui.ITasksUiPreferenceConstants;
import org.eclipse.mylyn.internal.tasks.ui.TasksUiPlugin;
import org.eclipse.mylyn.internal.tasks.ui.util.TasksUiInternal;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITask.SynchronizationState;
import org.eclipse.mylyn.tasks.core.RepositoryResponse;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.ITaskDataWorkingCopy;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.eclipse.mylyn.tasks.core.sync.SubmitJob;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tuleap.core.internal.data.TuleapArtifactMapper;
import org.eclipse.mylyn.tuleap.core.internal.data.TuleapTaskId;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapProject;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapServer;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker;
import org.eclipse.mylyn.tuleap.core.internal.repository.TuleapRepositoryConnector;
import org.eclipse.mylyn.tuleap.core.internal.repository.TuleapTaskDataHandler;
import org.eclipse.mylyn.tuleap.core.internal.repository.TuleapTaskMapping;
import org.eclipse.mylyn.tuleap.ui.tests.internal.support.TuleapFixture;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Common superclass of all integration tests.
 *
 * @author Mik Kersten
 * @author Rob Elves
 * @author Nathan Hapke
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@RunWith(Parameterized.class)
public abstract class AbstractTuleapUiTests {
	/**
	 * The current fixture to use.
	 */
	protected TuleapFixture fixture;

	/**
	 * The Tuleap repository connector.
	 */
	protected TuleapRepositoryConnector connector;

	/**
	 * The task repository.
	 */
	protected TaskRepository repository;

	/**
	 * The constructor.
	 *
	 * @param fixture
	 *            The current fixture to use
	 */
	public AbstractTuleapUiTests(TuleapFixture fixture) {
		this.fixture = fixture;
		this.connector = this.fixture.getConnector();
		this.repository = this.fixture.createRepository();

		TasksUiPlugin.getDefault().getPreferenceStore().setValue(
				ITasksUiPreferenceConstants.REPOSITORY_SYNCH_SCHEDULE_ENABLED, false);

		TasksUi.getRepositoryManager().addRepository(this.repository);
	}

	/**
	 * Returns the {@link TaskDataModel} for the given task.
	 *
	 * @param task
	 *            The task
	 * @return The TaskDataModel for the given task
	 */
	protected TaskDataModel createModel(ITask task) {
		ITaskDataWorkingCopy taskDataState = getWorkingCopy(task);
		if (taskDataState != null) {
			return new TaskDataModel(repository, task, taskDataState);
		}
		return null;
	}

	/**
	 * Creates a new bug in the tracker with the Tuleap bug tracker id retrieved from the fixture.
	 *
	 * @return The bug created and synchronized
	 */
	protected TaskData createBug() {
		TuleapTaskDataHandler taskDataHandler = this.connector.getTaskDataHandler();
		TaskAttributeMapper attributeMapper = taskDataHandler.getAttributeMapper(repository);
		TaskData taskData = new TaskData(attributeMapper, repository.getConnectorKind(), repository
				.getRepositoryUrl(), "");
		try {
			this.connector.updateRepositoryConfiguration(repository, new NullProgressMonitor());
			TuleapServer server = this.connector.getServer(repository);

			TuleapProject project = server.getProject(this.fixture.getProjectId());
			TuleapTracker tracker = project.getTracker(this.fixture.getBugTrackerId());

			TuleapTaskMapping mapping = new TuleapTaskMapping(tracker);
			taskDataHandler.initializeTaskData(repository, taskData, mapping, new NullProgressMonitor());

			TuleapArtifactMapper mapper = new TuleapArtifactMapper(taskData, tracker);
			mapper.setSummary("New Bug");

			RepositoryResponse repositoryResponse = taskDataHandler.postTaskData(repository, taskData,
					Collections.<TaskAttribute> emptySet(), new NullProgressMonitor());

			return taskDataHandler.getTaskData(repository, TuleapTaskId.forName(repositoryResponse
					.getTaskId()), new NullProgressMonitor());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
		return null;
	}

	/**
	 * Returns the task data working copy for the given task.
	 *
	 * @param task
	 *            The task
	 * @return The task data working copy
	 */
	protected ITaskDataWorkingCopy getWorkingCopy(ITask task) {
		try {
			return TasksUiPlugin.getTaskDataManager().getWorkingCopy(task);
		} catch (CoreException e) {
			return null;
		}
	}

	/**
	 * Submit the given task data model
	 *
	 * @param model
	 *            The model to submit
	 */
	protected void submit(TaskDataModel model) {
		SubmitJob submitJob = TasksUiInternal.getJobFactory().createSubmitTaskJob(connector,
				model.getTaskRepository(), model.getTask(), model.getTaskData(),
				model.getChangedOldAttributes());
		submitJob.schedule();
		try {
			submitJob.join();
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Synchronize the given tasks and check that they all have the given state.
	 *
	 * @param tasks
	 *            The tasks to synchronize
	 * @param state
	 *            The state wanted
	 */
	protected void synchAndAssertState(Set<ITask> tasks, SynchronizationState state) {
		for (ITask task : tasks) {
			TasksUiInternal.synchronizeTask(connector, task, true, null);
			TasksUiPlugin.getTaskDataManager().setTaskRead(task, true);
			assertEquals(task.getSynchronizationState(), state);
		}
	}

	/**
	 * Generate a local task and download it.
	 *
	 * @param id
	 *            The identifier of the task
	 * @return The newly downloaded task
	 */
	public ITask generateLocalTaskAndDownload(String id) {
		ITask task = TasksUi.getRepositoryModel().createTask(repository, id);
		TasksUiPlugin.getTaskList().addTask(task);
		TasksUiInternal.synchronizeTask(connector, task, true, null);
		TasksUiPlugin.getTaskDataManager().setTaskRead(task, true);
		return task;
	}

	/**
	 * Cleans everything.
	 */
	@AfterClass
	public static void staticTearDown() {
		// TuleapFixture.resetTaskList();
		TasksUiPlugin.getRepositoryManager().clearRepositories();
	}
}
