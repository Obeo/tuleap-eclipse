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
package org.tuleap.mylyn.task.internal.tests.parser;

import org.tuleap.mylyn.task.internal.core.model.config.TuleapProject;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapServer;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapUser;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapUserGroup;

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
			projectConfiguration.addUserToUserGroup(group0, new TuleapUser("jd15", "John Doe", 15,
					"john.doe15@somewhere.com", null));
			projectConfiguration.addUserToUserGroup(group0, new TuleapUser("jd16", "John Doe II", 16,
					"john.doe16@somewhere.com", null));

			TuleapUserGroup group1 = new TuleapUserGroup("1", "Project Members");
			projectConfiguration.addUserToUserGroup(group1, new TuleapUser("jd15", "John Doe", 15,
					"john.doe15@somewhere.com", null));
			projectConfiguration.addUserToUserGroup(group1, new TuleapUser("jd16", "John Doe II", 16,
					"john.doe16@somewhere.com", null));
			projectConfiguration.addUserToUserGroup(group1, new TuleapUser("jd17", "John Doe III", 17,
					"john.doe17@somewhere.com", null));
			projectConfiguration.addUserToUserGroup(group1, new TuleapUser("jd18", "John Doe IV", 18,
					"john.doe18@somewhere.com", null));
			projectConfiguration.addGroup(group1);
		}
		return projectConfiguration;
	}

}
