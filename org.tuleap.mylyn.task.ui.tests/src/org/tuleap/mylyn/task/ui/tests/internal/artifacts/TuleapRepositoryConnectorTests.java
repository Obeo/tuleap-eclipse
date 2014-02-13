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
package org.tuleap.mylyn.task.ui.tests.internal.artifacts;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.commons.sdk.util.Junit4TestFixtureRunner;
import org.eclipse.mylyn.commons.sdk.util.Junit4TestFixtureRunner.FixtureDefinition;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapProject;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapServer;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapTracker;
import org.tuleap.mylyn.task.ui.tests.internal.AbstractTuleapUiTests;
import org.tuleap.mylyn.task.ui.tests.internal.support.TuleapFixture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Basic integration tests for a Tuleap server.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
@RunWith(Junit4TestFixtureRunner.class)
@FixtureDefinition(fixtureClass = TuleapFixture.class, fixtureType = "tuleap")
public class TuleapRepositoryConnectorTests extends AbstractTuleapUiTests {
	/**
	 * The constructor.
	 * 
	 * @param fixture
	 *            The fixture.
	 */
	public TuleapRepositoryConnectorTests(TuleapFixture fixture) {
		super(fixture);
	}

	/**
	 * Test the creation of an artifact with a title. The connector will send the artifact to the Tuleap
	 * server and it will retrieve it from the server to ensure that it has correctly been synchronized.
	 */
	@Test
	public void testCreateArtifact() {
		try {
			this.connector.updateRepositoryConfiguration(this.repository, new NullProgressMonitor());
			TuleapServer server = this.connector.getServer(this.repository.getRepositoryUrl());
			TuleapProject project = server.getProject(this.fixture.getProjectId());
			TuleapTracker tracker = project.getTracker(this.fixture.getBugTrackerId());
			assertEquals("Bugs", tracker.getLabel());

			TaskData taskData = this.createBug();
			assertNotNull(taskData);
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}
}
