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
package org.eclipse.mylyn.tuleap.core.tests.internal;

import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tuleap.core.internal.repository.ITuleapRepositoryConnector;
import org.eclipse.mylyn.tuleap.core.internal.repository.TuleapRepositoryConnector;
import org.eclipse.mylyn.tuleap.core.internal.util.ITuleapConstants;
import org.junit.Before;

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
		this.connector = new TuleapRepositoryConnector();
		this.repository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, this.getServerUrl());
		this.repository.setCredentials(AuthenticationType.REPOSITORY, new AuthenticationCredentials(
				"admin", "password"), true); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Returns the URL of the remote repository.
	 * 
	 * @return The URL of the remote repository.
	 */
	public abstract String getServerUrl();
}
