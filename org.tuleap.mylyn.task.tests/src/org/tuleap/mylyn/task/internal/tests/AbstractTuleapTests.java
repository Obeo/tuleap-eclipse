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
import org.eclipse.mylyn.internal.tasks.ui.ITasksUiPreferenceConstants;
import org.eclipse.mylyn.internal.tasks.ui.TasksUiPlugin;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tests.util.TestFixture;
import org.junit.After;
import org.junit.Before;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Utility parent class for all Mylyn Tuleap unit tests.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public abstract class AbstractTuleapTests {
	/**
	 * The Tuleap repository connector.
	 */
	protected ITuleapRepositoryConnector connector;

	/**
	 * The Mylyn tasks repository.
	 */
	protected TaskRepository repository;

	/**
	 * Called before every single test.
	 */
	@Before
	public void setUp() {
		TasksUiPlugin.getDefault().getPreferenceStore().setValue(
				ITasksUiPreferenceConstants.REPOSITORY_SYNCH_SCHEDULE_ENABLED, false);

		try {
			TestFixture.resetTaskListAndRepositories();
			// CHECKSTYLE:OFF
		} catch (Exception e) {
			// CHECKSTYLE:ON
			e.printStackTrace();
		}

		AbstractRepositoryConnector repositoryConnector = TasksUi
				.getRepositoryConnector(ITuleapConstants.CONNECTOR_KIND);
		assertThat(repositoryConnector, is(instanceOf(ITuleapRepositoryConnector.class)));

		this.connector = (ITuleapRepositoryConnector)repositoryConnector;
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
		TasksUiPlugin.getRepositoryManager().clearRepositories();
	}

	/**
	 * Returns the URL of the remote repository.
	 * 
	 * @return The URL of the remote repository.
	 */
	public abstract String getServerUrl();
}
