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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.mylyn.commons.net.WebLocation;
import org.eclipse.mylyn.internal.tuleap.core.repository.ITuleapRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryListener;
import org.eclipse.mylyn.tasks.core.TaskRepository;

/**
 * The Tuleap client manager will create new clients for a given Mylyn tasks repository or find existing ones.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapClientManager implements ITuleapClientManager, IRepositoryListener {

	/**
	 * The Tuleap repository connector.
	 */
	private ITuleapRepositoryConnector repositoryConnector;

	/**
	 * The client cache.
	 */
	private Map<TaskRepository, ITuleapClient> clientCache = new HashMap<TaskRepository, ITuleapClient>();

	/**
	 * The constructor.
	 * 
	 * @param connector
	 *            The Tuleap repository connector.
	 */
	public TuleapClientManager(ITuleapRepositoryConnector connector) {
		this.repositoryConnector = connector;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClientManager#getClient(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	public ITuleapClient getClient(TaskRepository taskRepository) {
		ITuleapClient tuleapClient = this.clientCache.get(taskRepository);
		if (tuleapClient == null) {
			tuleapClient = this.createClient(taskRepository);
			this.clientCache.put(taskRepository, tuleapClient);
		}
		return tuleapClient;
	}

	/**
	 * Creates a new Tuleap client for the given Mylyn tasks repository.
	 * 
	 * @param taskRepository
	 *            The Mylyn tasks repository
	 * @return A new Tuleap client for the given Mylyn tasks repository.
	 */
	protected ITuleapClient createClient(TaskRepository taskRepository) {
		return TuleapClientFactory.getDefault().createClient(taskRepository,
				new WebLocation(taskRepository.getRepositoryUrl()), this.repositoryConnector);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.IRepositoryListener#repositoryAdded(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	public void repositoryAdded(TaskRepository repository) {
		// Do nothing for now
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.IRepositoryListener#repositoryRemoved(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	public void repositoryRemoved(TaskRepository repository) {
		// Force the re-creation of the client if the repository changes
		this.clientCache.remove(repository);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.IRepositoryListener#repositorySettingsChanged(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	public void repositorySettingsChanged(TaskRepository repository) {
		// Force the re-creation of the client if the repository changes
		this.clientCache.remove(repository);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.IRepositoryListener#repositoryUrlChanged(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      java.lang.String)
	 */
	public void repositoryUrlChanged(TaskRepository repository, String oldUrl) {
		// Force the re-creation of the client if the repository changes
		this.clientCache.remove(repository);
	}
}
