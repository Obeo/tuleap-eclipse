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
package org.tuleap.mylyn.task.internal.core.repository;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.tuleap.mylyn.task.internal.core.client.ITuleapClientManager;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;

/**
 * This interface represents a Tuleap repository connector.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public interface ITuleapRepositoryConnector {
	/**
	 * Returns the Tuleap client manager.
	 * 
	 * @return The Tuleap client manager.
	 */
	ITuleapClientManager getClientManager();

	/**
	 * Returns the configuration of the Tuleap repository.
	 * 
	 * @param repositoryUrl
	 *            the URL of the repository
	 * @return The configuration of the Tuleap repository.
	 */
	TuleapServerConfiguration getRepositoryConfiguration(String repositoryUrl);

	/**
	 * Returns the configuration of the Tuleap repository.
	 * 
	 * @param taskRepository
	 *            the Mylyn task repository
	 * @param forceRefresh
	 *            Indicates if the refresh of the configuration is forced
	 * @param monitor
	 *            the progress monitor
	 * @return The configuration of the Tuleap repository.
	 */
	TuleapServerConfiguration getRepositoryConfiguration(TaskRepository taskRepository,
			boolean forceRefresh, IProgressMonitor monitor);

	/**
	 * Puts the repository configuration in the cache.
	 * 
	 * @param repositoryUrl
	 *            The url of the repository
	 * @param configuration
	 *            The configuration
	 */
	void putRepositoryConfiguration(String repositoryUrl, TuleapServerConfiguration configuration);
}
