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
package org.eclipse.mylyn.internal.tuleap.core.client;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapArtifact;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapInstanceConfiguration;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;

/**
 * this interface represents a tuleap client.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public interface ITuleapClient {

	/**
	 * Creates the artifact on the server and return the ID of the artifact computed by the tracker.
	 * 
	 * @param artifact
	 *            The artifact to create on the tracker
	 * @param monitor
	 *            The progress monitor
	 * @return The ID of the newly created artifact, computed by the tracker
	 * @throws CoreException
	 *             In case of issues during the creation of the artifact
	 */
	String createArtifact(TuleapArtifact artifact, IProgressMonitor monitor) throws CoreException;

	/**
	 * Updates the Tuleap artifact located on the tracker.
	 * 
	 * @param artifact
	 *            The Tuleap artifact to update
	 * @param monitor
	 *            The progress monitor
	 * @throws CoreException
	 *             In case of issues during the update of the artifact
	 */
	void updateArtifact(TuleapArtifact artifact, IProgressMonitor monitor) throws CoreException;

	/**
	 * Asks the Tuleap tracker for the artifact matching the given ID and return the Tuleap Artifact.
	 * 
	 * @param taskId
	 *            The ID of the artifact
	 * @param monitor
	 *            The progress monitor
	 * @return The Tuleap artifact matching the given task ID.
	 * @throws CoreException
	 *             In case of issues during the retrieval of the artifact
	 */
	TuleapArtifact getArtifact(String taskId, IProgressMonitor monitor) throws CoreException;

	/**
	 * Updates the attributes handled by the Tuleap client.
	 * 
	 * @param monitor
	 *            The progress monitor
	 * @param forceRefresh
	 *            Indicates that we should force the refresh of the attributes
	 */
	void updateAttributes(IProgressMonitor monitor, boolean forceRefresh);

	/**
	 * Returns the configuration of the repository handled by the client.
	 * 
	 * @return The configuration of the repository handled by the client.
	 */
	TuleapInstanceConfiguration getRepositoryConfiguration();

	/**
	 * Execute the given query on the task repository in order to collect a set of tasks. The tasks are
	 * collected in the given task data collector.
	 * 
	 * @param query
	 *            The query to execute
	 * @param collector
	 *            The task data collector in which the tasks will be collected
	 * @param mapper
	 *            The task attribute mapper used to evaluate the attributes of the tasks
	 * @param monitor
	 *            The progress monitor
	 * @return <code>true</code> if the evaluation of the query has returned at least one task,
	 *         <code>false</code> otherwise.
	 */
	boolean getSearchHits(IRepositoryQuery query, TaskDataCollector collector, TaskAttributeMapper mapper,
			IProgressMonitor monitor);

	/**
	 * Returns the task repository on which the client is working.
	 * 
	 * @return The task repository on which the client is working.
	 */
	TaskRepository getTaskRepository();
}
