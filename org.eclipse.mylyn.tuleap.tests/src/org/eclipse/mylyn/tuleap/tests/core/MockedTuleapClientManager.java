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
package org.eclipse.mylyn.tuleap.tests.core;

import org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClient;
import org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClientManager;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapArtifact;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapInstanceConfiguration;
import org.eclipse.mylyn.tasks.core.TaskRepository;

/**
 * Mock of tuleap client manager.
 * 
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @since 0.7
 */
public class MockedTuleapClientManager implements ITuleapClientManager {
	/**
	 * Tuleap artifact.
	 */
	private TuleapArtifact tuleapArtifact;

	/**
	 * Tuleap instance configuration.
	 */
	private TuleapInstanceConfiguration tuleapInstanceConfiguration;

	/**
	 * Constructor.
	 * 
	 * @param artifact
	 *            Tuleap Artifact
	 * @param instanceConfiguration
	 *            Tuleap Instance configuration
	 */
	public MockedTuleapClientManager(TuleapArtifact artifact,
			TuleapInstanceConfiguration instanceConfiguration) {
		this.tuleapArtifact = artifact;
		this.tuleapInstanceConfiguration = instanceConfiguration;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClientManager#getClient(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	public ITuleapClient getClient(TaskRepository taskRepository) {
		return new MockedTuleapClient(tuleapArtifact, tuleapInstanceConfiguration);
	}

}
