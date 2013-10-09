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
package org.tuleap.mylyn.task.internal.core.repository;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.tuleap.mylyn.task.internal.core.client.TuleapClientManager;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableFieldsConfiguration;
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
	TuleapClientManager getClientManager();

	/**
	 * Returns the configuration of the Tuleap server.
	 * 
	 * @param repositoryUrl
	 *            the URL of the repository
	 * @return The configuration of the Tuleap server.
	 */
	TuleapServerConfiguration getTuleapServerConfiguration(String repositoryUrl);

	/**
	 * Returns a refreshed version of the given configuration. This operation will communicate with the
	 * server. The server configuration will be modified to contain the refreshed version of the
	 * configuration.
	 * 
	 * @param taskRepository
	 *            the Mylyn task repository
	 * @param configuration
	 *            The configuration to refresh
	 * @param monitor
	 *            the progress monitor
	 * @return A refreshed version of the given configuration
	 * @throws CoreException
	 *             In case of issues during the connection with the server
	 */
	AbstractTuleapConfigurableFieldsConfiguration refreshConfiguration(TaskRepository taskRepository,
			AbstractTuleapConfigurableFieldsConfiguration configuration, IProgressMonitor monitor)
			throws CoreException;
}
