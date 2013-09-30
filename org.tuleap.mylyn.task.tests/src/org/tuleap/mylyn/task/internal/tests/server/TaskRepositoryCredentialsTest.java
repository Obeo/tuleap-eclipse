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
package org.tuleap.mylyn.task.internal.tests.server;

import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.client.rest.TaskRepositoryCredentials;

import static org.junit.Assert.assertEquals;

/**
 * Tests of the TaskRepositoryCredentials Class.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TaskRepositoryCredentialsTest {

	/**
	 * Test method for
	 * {@link org.tuleap.mylyn.task.internal.core.client.rest.TaskRepositoryCredentials#getUserName()}.
	 */
	@Ignore("To refactor once actual REST communication happens")
	@Test
	public void testGetUserName() {
		// TODO Refactor once a perennial mechanism of credentials is in place
		String user = "azerty"; //$NON-NLS-1$
		String password = "jfdk8!:#j%��"; //$NON-NLS-1$
		TaskRepository repo = new TaskRepository("Tuleap", "http://tuleap.net"); //$NON-NLS-1$ //$NON-NLS-2$
		repo.setCredentials(AuthenticationType.REPOSITORY, new AuthenticationCredentials(user, password),
				false);
		TaskRepositoryCredentials credentials = new TaskRepositoryCredentials(repo);
		assertEquals(user, credentials.getUserName());
		assertEquals(password, credentials.getPassword());
	}

}
