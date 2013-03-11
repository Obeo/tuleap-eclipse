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
package org.eclipse.mylyn.internal.tuleap.ui.repository;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.internal.tuleap.core.net.TuleapSoapConnector;
import org.eclipse.mylyn.internal.tuleap.core.repository.TuleapRepositoryConnector;
import org.eclipse.mylyn.internal.tuleap.core.util.ITuleapConstants;
import org.eclipse.mylyn.internal.tuleap.core.util.TuleapMylynTasksMessages;
import org.eclipse.mylyn.internal.tuleap.ui.TuleapTasksUIPlugin;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.TaskRepositoryLocationFactory;
import org.eclipse.mylyn.tasks.ui.TasksUi;

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
		AbstractRepositoryConnector repositoryConnector = TasksUi.getRepositoryManager()
				.getRepositoryConnector(ITuleapConstants.CONNECTOR_KIND);
		if (repositoryConnector instanceof TuleapRepositoryConnector) {
			AbstractWebLocation location = new TaskRepositoryLocationFactory()
					.createWebLocation(taskRepository);
			monitor.beginTask(TuleapMylynTasksMessages.getString("TuleapSoapConnector.ValidateConnection"), //$NON-NLS-1$
					10);
			TuleapSoapConnector trackerSoapConnector = new TuleapSoapConnector(location);
			try {
				status = trackerSoapConnector.validateConnection(monitor);
			} catch (MalformedURLException e) {
				status = new Status(IStatus.ERROR, TuleapTasksUIPlugin.PLUGIN_ID, e.getMessage(), e);
			} catch (RemoteException e) {
				status = new Status(IStatus.ERROR, TuleapTasksUIPlugin.PLUGIN_ID, e.getMessage(), e);
			} catch (ServiceException e) {
				status = new Status(IStatus.ERROR, TuleapTasksUIPlugin.PLUGIN_ID, e.getMessage(), e);
			}
		}
		return status;
	}
}
