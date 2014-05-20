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
package org.eclipse.mylyn.tuleap.tests;

import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.internal.tasks.core.TaskList;
import org.eclipse.mylyn.internal.tasks.core.TaskRepositoryManager;
import org.eclipse.mylyn.internal.tasks.ui.ITasksUiPreferenceConstants;
import org.eclipse.mylyn.internal.tasks.ui.TasksUiPlugin;
import org.eclipse.mylyn.internal.tasks.ui.util.TasksUiInternal;
import org.eclipse.mylyn.internal.tuleap.core.client.TuleapClient;
import org.eclipse.mylyn.internal.tuleap.core.repository.TuleapRepositoryConnector;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITask.SynchronizationState;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.ITaskDataWorkingCopy;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.eclipse.mylyn.tasks.core.sync.SubmitJob;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tests.util.TestFixture;
import org.eclipse.mylyn.tuleap.tests.support.TuleapFixture;

/**
 * Utility parent class for all Mylyn Tuleap unit tests.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
@SuppressWarnings("restriction")
public abstract class AbstractTuleapTest extends TestCase {
	/**
	 * The Tuleap repository connector.
	 */
	protected TuleapRepositoryConnector connector;

	/**
	 * The tasks repository manager.
	 */
	protected TaskRepositoryManager manager;

	/**
	 * The Mylyn tasks repository.
	 */
	protected TaskRepository repository;

	/**
	 * The tasks list.
	 */
	protected TaskList taskList;

	/**
	 * The Tuleap client.
	 */
	protected TuleapClient client;

	/**
	 * {@inheritDoc}
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		TasksUiPlugin.getDefault().getPreferenceStore().setValue(
				ITasksUiPreferenceConstants.REPOSITORY_SYNCH_SCHEDULE_ENABLED, false);
		manager = TasksUiPlugin.getRepositoryManager();
		TestFixture.resetTaskListAndRepositories();
		this.client = TuleapFixture.current().client();
		this.connector = (TuleapRepositoryConnector)TuleapFixture.current().connector();
		this.repository = TuleapFixture.current().repository();
		TasksUi.getRepositoryManager().addRepository(repository);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		TestFixture.resetTaskList();
		manager.clearRepositories();
	}

	/**
	 * Creates a task data model.
	 * 
	 * @param task
	 *            The task
	 * @return The task data model
	 * @throws CoreException
	 *             In case of problems
	 */
	protected TaskDataModel createModel(ITask task) throws CoreException {
		ITaskDataWorkingCopy taskDataState = getWorkingCopy(task);
		return new TaskDataModel(repository, task, taskDataState);
	}

	/**
	 * Returns the working copy of the task.
	 * 
	 * @param task
	 *            The task
	 * @return The working copy of the task.
	 * @throws CoreException
	 *             In case of problems
	 */
	protected ITaskDataWorkingCopy getWorkingCopy(ITask task) throws CoreException {
		return TasksUiPlugin.getTaskDataManager().getWorkingCopy(task);
	}

	/**
	 * Submits the task data model.
	 * 
	 * @param model
	 *            The model to be submitted
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
	 * Synchronizes and asserts the state.
	 * 
	 * @param tasks
	 *            The tasks to synchronize
	 * @param state
	 *            The synchronization state
	 */
	protected void synchAndAssertState(Set<ITask> tasks, SynchronizationState state) {
		for (ITask task : tasks) {
			TasksUiInternal.synchronizeTask(connector, task, true, null);
			TasksUiPlugin.getTaskDataManager().setTaskRead(task, true);
			assertEquals(task.getSynchronizationState(), state);
		}
	}

	/**
	 * Generates a local task and download.
	 * 
	 * @param id
	 *            The id of the task to generate and download
	 * @return The task generated
	 * @throws CoreException
	 *             In case of problems
	 */
	public ITask generateLocalTaskAndDownload(String id) throws CoreException {
		ITask task = TasksUi.getRepositoryModel().createTask(repository, id);
		TasksUiPlugin.getTaskList().addTask(task);
		TasksUiInternal.synchronizeTask(connector, task, true, null);
		TasksUiPlugin.getTaskDataManager().setTaskRead(task, true);
		return task;
	}
}
