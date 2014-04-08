/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.tuleap.core.internal.repository;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tuleap.core.internal.client.TuleapClientManager;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapServer;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker;

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
	TuleapClientManager getClientManager();

	/**
	 * Returns the Tuleap server configuration for the given repository.
	 *
	 * @param taskRepository
	 *            the repository
	 * @return The Tuleap server.
	 */
	TuleapServer getServer(TaskRepository taskRepository);

	/**
	 * Returns a refreshed version of the given tracker. This operation will communicate with the server. The
	 * server will be modified to contain the refreshed version of the tracker.
	 *
	 * @param taskRepository
	 *            the Mylyn task repository
	 * @param tracker
	 *            The tracker to refresh
	 * @param monitor
	 *            the progress monitor
	 * @return A refreshed version of the given tracker
	 * @throws CoreException
	 *             In case of issues during the connection with the server
	 */
	TuleapTracker refreshTracker(TaskRepository taskRepository, TuleapTracker tracker,
			IProgressMonitor monitor) throws CoreException;
}
