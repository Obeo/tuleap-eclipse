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

import org.tuleap.mylyn.task.internal.core.model.TuleapGroup;
import org.tuleap.mylyn.task.internal.core.model.TuleapPerson;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;

/**
 * Super class of tests that deserialize project configuration parts.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class StubProjectProvider {

	/**
	 * The project configuration.
	 */
	protected TuleapProjectConfiguration projectConfiguration;

	/**
	 * Provides the project Configuration, after creating it if necessary. Not thread-safe.
	 * 
	 * @return The project configuration to use for tests.
	 */
	public TuleapProjectConfiguration getProjectConfiguration() {
		if (projectConfiguration == null) {
			TuleapServerConfiguration serverConfiguration = new TuleapServerConfiguration("http://test/url");
			projectConfiguration = new TuleapProjectConfiguration("Test", 3);
			serverConfiguration.addProject(projectConfiguration);
			TuleapGroup group0 = new TuleapGroup(0, "Project Admins");
			projectConfiguration.addUserToUserGroup(group0, new TuleapPerson("jd15", "John Doe", 15,
					"john.doe15@somewhere.com"));
			projectConfiguration.addUserToUserGroup(group0, new TuleapPerson("jd16", "John Doe II", 16,
					"john.doe16@somewhere.com"));

			TuleapGroup group1 = new TuleapGroup(1, "Project Members");
			projectConfiguration.addUserToUserGroup(group1, new TuleapPerson("jd15", "John Doe", 15,
					"john.doe15@somewhere.com"));
			projectConfiguration.addUserToUserGroup(group1, new TuleapPerson("jd16", "John Doe II", 16,
					"john.doe16@somewhere.com"));
			projectConfiguration.addUserToUserGroup(group1, new TuleapPerson("jd17", "John Doe III", 17,
					"john.doe17@somewhere.com"));
			projectConfiguration.addUserToUserGroup(group1, new TuleapPerson("jd18", "John Doe IV", 18,
					"john.doe18@somewhere.com"));
			projectConfiguration.addGroup(group1);
		}
		return projectConfiguration;
	}

}
