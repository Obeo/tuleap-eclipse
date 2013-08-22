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
package org.tuleap.mylyn.task.internal.tests;

import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.internal.tasks.core.TaskList;
import org.eclipse.mylyn.internal.tasks.core.TaskRepositoryManager;
import org.eclipse.mylyn.internal.tasks.ui.ITasksUiPreferenceConstants;
import org.eclipse.mylyn.internal.tasks.ui.TasksUiPlugin;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tests.util.TestFixture;
import org.junit.After;
import org.junit.Before;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.repository.TuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * Utility parent class for all Mylyn Tuleap unit tests.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
@SuppressWarnings("restriction")
public abstract class AbstractTuleapTests {
	/**
	 * The Tuleap repository connector.
	 */
	protected ITuleapRepositoryConnector connector;

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
	 * Called before every single test.
	 */
	@Before
	public void setUp() {
		TasksUiPlugin.getDefault().getPreferenceStore().setValue(
				ITasksUiPreferenceConstants.REPOSITORY_SYNCH_SCHEDULE_ENABLED, false);
		manager = TasksUiPlugin.getRepositoryManager();

		try {
			TestFixture.resetTaskListAndRepositories();
			// CHECKSTYLE:OFF
		} catch (Exception e) {
			// CHECKSTYLE:ON
			e.printStackTrace();
		}

		this.connector = new TuleapRepositoryConnector();
		this.repository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, this.getServerUrl());
		this.repository.setCredentials(AuthenticationType.REPOSITORY, new AuthenticationCredentials(
				"admin", "password"), true); //$NON-NLS-1$ //$NON-NLS-2$

		TasksUi.getRepositoryManager().addRepository(repository);
	}

	/**
	 * Called after every single test.
	 */
	@After
	public void tearDown() {
		try {
			TestFixture.resetTaskList();
			// CHECKSTYLE:OFF
		} catch (Exception e) {
			// CHECKSTYLE:ON
			e.printStackTrace();
		}
		manager.clearRepositories();
	}

	/**
	 * Returns the URL of the remote repository.
	 * 
	 * @return The URL of the remote repository.
	 */
	public abstract String getServerUrl();
}
