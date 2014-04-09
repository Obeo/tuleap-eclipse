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
package org.eclipse.mylyn.tuleap.ui.internal.repository;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tuleap.core.internal.client.rest.TuleapRestClient;
import org.eclipse.mylyn.tuleap.core.internal.util.ITuleapConstants;
import org.eclipse.mylyn.tuleap.ui.internal.TuleapTasksUIPlugin;
import org.eclipse.mylyn.tuleap.ui.internal.util.TuleapUIKeys;
import org.eclipse.mylyn.tuleap.ui.internal.util.TuleapUIMessages;

/**
 * The Tuleap validator will be used inside of the repository setting page in orde rto check the
 * login/password and the tracker configuration.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapValidator {

	/**
	 * The location.
	 */
	private AbstractWebLocation location;

	/**
	 * The REST client.
	 */
	private TuleapRestClient tuleapRestClient;

	/**
	 * The task repository.
	 */
	private TaskRepository taskRepository;

	/**
	 * The constructor.
	 *
	 * @param location
	 *            The location
	 * @param tuleapRestClient
	 *            The REST client
	 * @param repository
	 *            The Mylyn task repository
	 */
	public TuleapValidator(AbstractWebLocation location, TuleapRestClient tuleapRestClient,
			TaskRepository repository) {
		this.location = location;
		this.tuleapRestClient = tuleapRestClient;
		this.taskRepository = repository;
	}

	/**
	 * Validate the settings of the repository.
	 *
	 * @param monitor
	 *            The progress monitor
	 * @throws CoreException
	 *             In case of error with the configuration of the repository
	 * @return A status indicating if the validation went well.
	 */
	public IStatus validate(IProgressMonitor monitor) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, TuleapTasksUIPlugin.PLUGIN_ID, TuleapUIMessages
				.getString(TuleapUIKeys.invalidRepositoryConnector));
		if (ITuleapConstants.CONNECTOR_KIND.equals(this.taskRepository.getConnectorKind())) {
			monitor.beginTask(TuleapUIMessages.getString(TuleapUIKeys.validateConnection), 10);
			AuthenticationCredentials credentials = location.getCredentials(AuthenticationType.REPOSITORY);
			if (credentials != null) {
				status = tuleapRestClient.validateConnection(monitor);
			} else {
				// No credentials -> invalid
				status = new Status(IStatus.ERROR, TuleapTasksUIPlugin.PLUGIN_ID, TuleapUIMessages
						.getString(TuleapUIKeys.missingCredentials));
			}

		}
		return status;
	}
}
