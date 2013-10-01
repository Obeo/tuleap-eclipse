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
package org.tuleap.mylyn.task.internal.ui.repository;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapClient;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.ui.TuleapTasksUIPlugin;
import org.tuleap.mylyn.task.internal.ui.util.TuleapMylynTasksUIMessages;

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
	 * The SOAP client.
	 */
	private TuleapSoapClient tuleapSoapClient;

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
	 * @param tuleapRestClient
	 *            The REST client
	 * @param tuleapSoapClient
	 *            The SOAP client
	 * @param location
	 *            The location
	 * @param repository
	 *            The Mylyn task repository
	 */
	public TuleapValidator(AbstractWebLocation location, TuleapSoapClient tuleapSoapClient,
			TuleapRestClient tuleapRestClient, TaskRepository repository) {
		this.location = location;
		this.tuleapSoapClient = tuleapSoapClient;
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
		IStatus status = new Status(IStatus.ERROR, TuleapTasksUIPlugin.PLUGIN_ID, TuleapMylynTasksUIMessages
				.getString("TuleapValidator.InvalidRepositoryConnector")); //$NON-NLS-1$ 
		if (ITuleapConstants.CONNECTOR_KIND.equals(this.taskRepository.getConnectorKind())) {
			monitor.beginTask(TuleapMylynTasksUIMessages.getString("TuleapValidator.ValidateConnection"), //$NON-NLS-1$
					10);
			AuthenticationCredentials credentials = location.getCredentials(AuthenticationType.REPOSITORY);
			if (credentials != null) {
				status = tuleapSoapClient.validateConnection(monitor);

				if (status.isOK()) {
					status = tuleapRestClient.validateConnection(monitor);
				}
			} else {
				// No credentials -> invalid
				status = new Status(IStatus.ERROR, TuleapTasksUIPlugin.PLUGIN_ID, TuleapMylynTasksUIMessages
						.getString("TuleapValidator.MissingCredentials")); //$NON-NLS-1$ 
			}

		}
		return status;
	}
}
