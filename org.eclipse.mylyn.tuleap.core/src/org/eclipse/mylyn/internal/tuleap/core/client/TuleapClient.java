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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.internal.tuleap.core.TuleapCoreActivator;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapArtifact;
import org.eclipse.mylyn.internal.tuleap.core.net.TrackerConnector;
import org.eclipse.mylyn.internal.tuleap.core.net.TrackerSoapConnector;
import org.eclipse.mylyn.internal.tuleap.core.repository.ITuleapRepositoryConnector;
import org.eclipse.mylyn.internal.tuleap.core.repository.TuleapRepositoryConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.util.TuleapMylynTasksMessages;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;

/**
 * The Mylyn Tuleap client is in charge of the connection with the repository and it will realize the request
 * in order to obtain and publish the tasks.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapClient implements ITuleapClient {

	/**
	 * The location of the repository.
	 */
	private AbstractWebLocation location;

	/**
	 * The Tuleap repository connector.
	 */
	private ITuleapRepositoryConnector repositoryConnector;

	/**
	 * The configuration of the repository.
	 */
	private TuleapRepositoryConfiguration configuration;

	/**
	 * The task repository.
	 */
	private TaskRepository taskRepository;

	// FIXME DELETE LATER §§§§
	/**
	 * A cache.
	 */
	private Map<Integer, TuleapArtifact> cache = new HashMap<Integer, TuleapArtifact>();

	/**
	 * The constructor.
	 * 
	 * @param repository
	 *            The task repository
	 * @param weblocation
	 *            The location of the tracker
	 * @param connector
	 *            The Tuleap repository connector
	 */
	public TuleapClient(TaskRepository repository, AbstractWebLocation weblocation,
			ITuleapRepositoryConnector connector) {
		this.location = weblocation;
		this.taskRepository = repository;
		this.repositoryConnector = connector;
		this.configuration = new TuleapRepositoryConfiguration(this.location.getUrl());
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
		// Get the result of the query
		TrackerSoapConnector trackerSoapConnector = new TrackerSoapConnector(this.location);
		int hit = trackerSoapConnector.performQuery(collector, mapper, TaskDataCollector.MAX_HITS);
		return hit > 0;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClient#updateAttributes(org.eclipse.core.runtime.IProgressMonitor,
	 *      boolean)
	 */
	public void updateAttributes(IProgressMonitor monitor, boolean forceRefresh) {
		if (!this.hasAttributes() || forceRefresh) {
			this.updateAttributes(monitor);
			this.configuration.setLastUpdate(System.currentTimeMillis());
		}
	}

	/**
	 * Returns <code>true</code> if attributes have already been set once, <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if attributes have already been set once, <code>false</code> otherwise.
	 */
	public boolean hasAttributes() {
		return this.configuration.getLastUpdate() != 0;
	}

	/**
	 * Updates the known attributes of the Tuleap repository.
	 * 
	 * @param monitor
	 *            The progress monitor
	 */
	private void updateAttributes(IProgressMonitor monitor) {
		String username = this.taskRepository.getUserName();
		String password = this.taskRepository.getCredentials(AuthenticationType.REPOSITORY).getPassword();

		TrackerConnector trackerConnector = new TrackerConnector(username, password, this.location);

		TuleapRepositoryConfiguration newConfiguration = trackerConnector
				.getTuleapRepositoryConfiguration(monitor);
		if (newConfiguration != null) {
			this.configuration = newConfiguration;
		} else {
			TuleapCoreActivator.log(TuleapMylynTasksMessages.getString(
					"TuleapClient.FailToRetrieveTheConfiguration", this.location.getUrl()), false); //$NON-NLS-1$
		}

		this.repositoryConnector.putRepositoryConfiguration(this.location.getUrl(), configuration);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClient#getArtifact(int,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public TuleapArtifact getArtifact(int taskId, IProgressMonitor monitor) {
		// TODO Obtain the artifact from the server
		return this.cache.get(Integer.valueOf(taskId));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClient#createArtifact(org.eclipse.mylyn.internal.tuleap.core.model.TuleapArtifact,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public int createArtifact(TuleapArtifact artifact, IProgressMonitor monitor) {
		// TODO Create the artifact on the server and return the artifact id computed by the server
		int id = (int)(System.currentTimeMillis() / 1000);
		artifact.setId(id);
		cache.put(Integer.valueOf(id), artifact);
		return id;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClient#updateArtifact(org.eclipse.mylyn.internal.tuleap.core.model.TuleapArtifact,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void updateArtifact(TuleapArtifact artifact, IProgressMonitor monitor) {
		// TODO Update the artifact located on the tracker
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClient#getRepositoryConfiguration()
	 */
	public TuleapRepositoryConfiguration getRepositoryConfiguration() {
		return this.configuration;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClient#getTrackerList(boolean)
	 */
	public List<String> getTrackerList(boolean forceRefresh, IProgressMonitor monitor) {
		List<String> trackers = new ArrayList<String>();
		// FIXME Get the list of the trackers from the server
		trackers.add("First tracker");
		trackers.add("Second tracker");
		trackers.add("Third tracker");
		trackers.add("Fourth tracker");
		return trackers;
	}
}
