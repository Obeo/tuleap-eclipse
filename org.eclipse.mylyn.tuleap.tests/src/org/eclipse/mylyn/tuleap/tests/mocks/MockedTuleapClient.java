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
package org.eclipse.mylyn.tuleap.tests.mocks;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClient;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapArtifact;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapInstanceConfiguration;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;

/**
 * Mock of tuleap client.
 * 
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @since 0.7
 */
public class MockedTuleapClient implements ITuleapClient {
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
	public MockedTuleapClient(TuleapArtifact artifact, TuleapInstanceConfiguration instanceConfiguration) {
		this.tuleapArtifact = artifact;
		this.tuleapInstanceConfiguration = instanceConfiguration;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClient#createArtifact(org.eclipse.mylyn.internal.tuleap.core.model.TuleapArtifact,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public String createArtifact(TuleapArtifact artifact, IProgressMonitor monitor) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClient#updateArtifact(org.eclipse.mylyn.internal.tuleap.core.model.TuleapArtifact,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void updateArtifact(TuleapArtifact artifact, IProgressMonitor monitor) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClient#getArtifact(java.lang.String,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public TuleapArtifact getArtifact(String taskId, IProgressMonitor monitor) {
		return tuleapArtifact;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClient#updateAttributes(org.eclipse.core.runtime.IProgressMonitor,
	 *      boolean)
	 */
	public void updateAttributes(IProgressMonitor monitor, boolean forceRefresh) {
		// Nothing to do
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClient#getRepositoryConfiguration()
	 */
	public TuleapInstanceConfiguration getRepositoryConfiguration() {
		return tuleapInstanceConfiguration;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClient#getSearchHits(org.eclipse.mylyn.tasks.core.IRepositoryQuery,
	 *      org.eclipse.mylyn.tasks.core.data.TaskDataCollector,
	 *      org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public boolean getSearchHits(IRepositoryQuery query, TaskDataCollector collector,
			TaskAttributeMapper mapper, IProgressMonitor monitor) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClient#getTaskRepository()
	 */
	public TaskRepository getTaskRepository() {
		throw new UnsupportedOperationException();
	}

}
