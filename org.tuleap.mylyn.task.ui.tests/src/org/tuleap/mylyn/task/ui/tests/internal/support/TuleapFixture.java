/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.ui.tests.internal.support;

import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.commons.repositories.core.auth.UserCredentials;
import org.eclipse.mylyn.commons.sdk.util.AbstractTestFixture;
import org.eclipse.mylyn.commons.sdk.util.CommonTestUtil;
import org.eclipse.mylyn.commons.sdk.util.CommonTestUtil.PrivilegeLevel;
import org.eclipse.mylyn.commons.sdk.util.FixtureConfiguration;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.tuleap.mylyn.task.internal.core.repository.TuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * The fixture will hold the configuration of a Tuleap server.
 * <p>
 * The configuration of the server is retrieved from the mylyn.org server or it can be retrieved locally from
 * the local.json file. To use the local configuration, add the parameter
 * "-Dorg.eclipse.mylyn.tests.ignore.local.services=false" as a VM argument in your launch configuration.
 * </p>
 * <p>
 * The username and password are retrieved from the file "<user_directory>/.mylyn/credentials.properties". In
 * order to run those tests, the basic credentials file should have the following content (with some valid
 * credentials):
 * <ul>
 * <li>pass=myPassword</li>
 * <li>username=myUserName</li>
 * </ul>
 * </p>
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapFixture extends AbstractTestFixture {

	/**
	 * The key of the project id.
	 */
	private static final String PROJECT_ID = "projectId";

	/**
	 * The key of the bug tracker id.
	 */
	private static final String BUG_TRACKER_ID = "bugTrackerId";

	/**
	 * The connector.
	 */
	private TuleapRepositoryConnector connector;

	/**
	 * The constructor is called reflectively by the JUnit runner and it is initialized with a configuration.
	 * 
	 * @param config
	 *            The configuration of the server.
	 */
	public TuleapFixture(FixtureConfiguration config) {
		super(ITuleapConstants.CONNECTOR_KIND, config);
		this.connector = new TuleapRepositoryConnector();
	}

	/**
	 * Returns the Tuleap connector.
	 * 
	 * @return The connector
	 */
	public TuleapRepositoryConnector getConnector() {
		return this.connector;
	}

	/**
	 * Creates and returns the task repository.
	 * 
	 * @return The repository
	 */
	public TaskRepository createRepository() {
		TaskRepository repository = new TaskRepository(connectorKind, repositoryUrl);
		UserCredentials credentials = CommonTestUtil.getCredentials(PrivilegeLevel.USER);
		repository.setCredentials(AuthenticationType.REPOSITORY, new AuthenticationCredentials(credentials
				.getUserName(), credentials.getPassword()), false);
		return repository;
	}

	/**
	 * Returns the project id to use for the fixture.
	 * 
	 * @return The project id.
	 */
	public int getProjectId() {
		String id = this.getProperties().get(PROJECT_ID);
		return Integer.parseInt(id);
	}

	/**
	 * Returns the identifier of the bug tracker.
	 * 
	 * @return The bug tracker id
	 */
	public int getBugTrackerId() {
		String id = this.getProperties().get(BUG_TRACKER_ID);
		return Integer.parseInt(id);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.commons.sdk.util.AbstractTestFixture#getDefault()
	 */
	@Override
	protected AbstractTestFixture getDefault() {
		return null;
	}

}
