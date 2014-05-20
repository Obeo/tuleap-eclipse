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

import org.eclipse.mylyn.internal.tuleap.core.repository.TuleapRepositoryConnector;
import org.eclipse.mylyn.tasks.core.TaskRepository;

/**
 * The Tuleap client manager will create new clients for a given Mylyn tasks repository or find existing ones.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapClientManager {

	/**
	 * The Tuleap repository connector.
	 */
	private TuleapRepositoryConnector repositoryConnector;

	/**
	 * The constructor.
	 * 
	 * @param connector
	 *            The Tuleap repository connector.
	 */
	public TuleapClientManager(TuleapRepositoryConnector connector) {
		this.repositoryConnector = connector;
	}

	/**
	 * Returns the Tuleap client matching the given Mylyn tasks repository.
	 * 
	 * @param taskRepository
	 *            The Mylyn tasks repository
	 * @return the Tuleap client matching the given Mylyn tasks repository
	 */
	public TuleapClient getClient(TaskRepository taskRepository) {
		return this.createClient(taskRepository);
	}

	/**
	 * Creates a new Tuleap client for the given Mylyn tasks repository.
	 * 
	 * @param taskRepository
	 *            The Mylyn tasks repository
	 * @return A new Tuleap client for the given Mylyn tasks repository.
	 */
	protected TuleapClient createClient(TaskRepository taskRepository) {
		return TuleapClientFactory.createClient(taskRepository, this.repositoryConnector);
	}
}
