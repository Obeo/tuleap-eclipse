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
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.TaskRepositoryLocationFactory;
import org.tuleap.mylyn.task.internal.core.client.rest.ITuleapAPIVersions;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestConnector;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapClient;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapParser;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapSerializer;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonParser;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonSerializer;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;
import org.tuleap.mylyn.task.internal.ui.TuleapTasksUIPlugin;

/**
 * The Tuleap validator will be used inside of the repository setting page in orde rto check the
 * login/password and the tracker configuration.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapValidator {

	/**
	 * The task repository.
	 */
	private TaskRepository taskRepository;

	/**
	 * The constructor.
	 * 
	 * @param repository
	 *            The Mylyn task repository
	 */
	public TuleapValidator(TaskRepository repository) {
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
		IStatus status = new Status(IStatus.ERROR, TuleapTasksUIPlugin.PLUGIN_ID, TuleapMylynTasksMessages
				.getString("TuleapValidator.InvalidRepositoryConnector")); //$NON-NLS-1$ 
		if (ITuleapConstants.CONNECTOR_KIND.equals(this.taskRepository.getConnectorKind())) {
			AbstractWebLocation location = new TaskRepositoryLocationFactory()
					.createWebLocation(taskRepository);
			monitor.beginTask(TuleapMylynTasksMessages.getString("TuleapSoapConnector.ValidateConnection"), //$NON-NLS-1$
					10);

			ILog logger = Platform.getLog(Platform.getBundle(TuleapTasksUIPlugin.PLUGIN_ID));
			TuleapSoapParser tuleapSoapParser = new TuleapSoapParser();
			TuleapSoapSerializer tuleapSoapSerializer = new TuleapSoapSerializer();
			TuleapSoapClient tuleapSoapClient = new TuleapSoapClient(taskRepository, location,
					tuleapSoapParser, tuleapSoapSerializer, logger);
			status = tuleapSoapClient.validateConnection(monitor);

			if (status.isOK()) {
				TuleapJsonParser jsonParser = new TuleapJsonParser();
				TuleapJsonSerializer jsonSerializer = new TuleapJsonSerializer();
				TuleapRestConnector tuleapRestConnector = new TuleapRestConnector(taskRepository.getUrl(),
						ITuleapAPIVersions.V1_0, logger);
				TuleapRestClient tuleapRestClient = new TuleapRestClient(tuleapRestConnector, jsonParser,
						jsonSerializer, taskRepository, logger);

				status = tuleapRestClient.validateConnection(monitor);
			}
		}
		return status;
	}
}
