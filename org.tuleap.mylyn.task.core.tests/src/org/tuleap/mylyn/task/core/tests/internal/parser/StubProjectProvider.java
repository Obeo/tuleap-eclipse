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
		}
		return projectConfiguration;
	}

}
