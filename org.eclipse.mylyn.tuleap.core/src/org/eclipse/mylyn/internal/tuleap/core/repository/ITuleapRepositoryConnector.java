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
package org.eclipse.mylyn.internal.tuleap.core.repository;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.internal.tuleap.core.client.ITuleapClientManager;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapTrackerConfiguration;
import org.eclipse.mylyn.tasks.core.TaskRepository;

/**
 * This interface represents a Tuleap repository connector.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
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
	 * @param taskRepository
	 *            the Mylyn task repository
	 * @param forceRefresh
	 *            Indicates if the refresh of the configuration is forced
	 * @param monitor
	 *            the progress monitor
	 * @return The configuration of the Tuleap repository.
	 */
	TuleapTrackerConfiguration getRepositoryConfiguration(TaskRepository taskRepository,
			boolean forceRefresh, IProgressMonitor monitor);

	/**
	 * Puts the repository configuration in the cache.
	 * 
	 * @param repositoryUrl
	 *            The url of the repository
	 * @param configuration
	 *            The configuration
	 */
	void putRepositoryConfiguration(String repositoryUrl, TuleapTrackerConfiguration configuration);
}
