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
package org.tuleap.mylyn.task.core.tests.internal.parser;

import org.tuleap.mylyn.task.core.internal.model.config.TuleapProject;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapServer;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapUser;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapUserGroup;

/**
 * Super class of tests that deserialize project configuration parts.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class StubProjectProvider {

	/**
	 * The project configuration.
	 */
	protected TuleapProject projectConfiguration;

	/**
	 * Provides the project Configuration, after creating it if necessary. Not thread-safe.
	 *
	 * @return The project configuration to use for tests.
	 */
	public TuleapProject getProjectConfiguration() {
		if (projectConfiguration == null) {
			TuleapServer serverConfiguration = new TuleapServer("http://test/url");
			projectConfiguration = new TuleapProject("Test", 3);
			serverConfiguration.addProject(projectConfiguration);
			TuleapUserGroup group0 = new TuleapUserGroup("0", "Project Admins");
			TuleapUser user = new TuleapUser(15);
			user.setEmail("john.doe15@somewhere.com");
			user.setRealName("John Doe");
			user.setUsername("jd15");
			projectConfiguration.addUserToUserGroup(group0, user);
			user = new TuleapUser(16);
			user.setEmail("john.doe16@somewhere.com");
			user.setRealName("John Doe II");
			user.setUsername("jd16");
			projectConfiguration.addUserToUserGroup(group0, user);

			TuleapUserGroup group1 = new TuleapUserGroup("1", "Project Members");
			user = new TuleapUser(15);
			user.setEmail("john.doe15@somewhere.com");
			user.setRealName("John Doe");
			user.setUsername("jd15");
			projectConfiguration.addUserToUserGroup(group1, user);
			user = new TuleapUser(16);
			user.setEmail("john.doe16@somewhere.com");
			user.setRealName("John Doe II");
			user.setUsername("jd16");
			projectConfiguration.addUserToUserGroup(group1, user);
			user = new TuleapUser(17);
			user.setEmail("john.doe17@somewhere.com");
			user.setRealName("John Doe III");
			user.setUsername("jd17");
			projectConfiguration.addUserToUserGroup(group1, user);
			user = new TuleapUser(18);
			user.setEmail("john.doe18@somewhere.com");
			user.setRealName("John Doe IV");
			user.setUsername("jd18");
			projectConfiguration.addUserToUserGroup(group1, user);
			projectConfiguration.addGroup(group1);
		}
		return projectConfiguration;
	}

}
