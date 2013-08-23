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
package org.tuleap.mylyn.task.internal.core.client;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.model.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.net.TuleapSoapConnector;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.repository.TuleapTaskDataHandler;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;

/**
 * The Mylyn Tuleap client is in charge of the connection with the repository and it will realize the request
 * in order to obtain and publish the tasks.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapClient implements ITuleapClient {

	/**
	 * The location of the repository.
	 */
	private AbstractWebLocation location;

	/**
	 * The Tuleap repository connector.
	 */
	private ITuleapRepositoryConnector repositoryConnector;

	/**
	 * The configuration of the repository.
	 */
	private TuleapServerConfiguration configuration;

	/**
	 * The task repository.
	 */
	private TaskRepository taskRepository;

	/**
	 * The constructor.
	 * 
	 * @param repository
	 *            The task repository
	 * @param weblocation
	 *            The location of the tracker
	 * @param connector
	 *            The Tuleap repository connector
	 */
	public TuleapClient(TaskRepository repository, AbstractWebLocation weblocation,
			ITuleapRepositoryConnector connector) {
		this.location = weblocation;
		this.taskRepository = repository;
		this.repositoryConnector = connector;
		this.configuration = new TuleapServerConfiguration(this.location.getUrl());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.client.ITuleapClient#getSearchHits(org.eclipse.mylyn.tasks.core.IRepositoryQuery,
	 *      org.eclipse.mylyn.tasks.core.data.TaskDataCollector,
	 *      org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public boolean getSearchHits(IRepositoryQuery query, TaskDataCollector collector,
			TaskAttributeMapper mapper, IProgressMonitor monitor) {
		// Get the result of the query
		int hit = 0;
		if (this.repositoryConnector instanceof AbstractRepositoryConnector
				&& ((AbstractRepositoryConnector)this.repositoryConnector).getTaskDataHandler() instanceof TuleapTaskDataHandler) {
			AbstractRepositoryConnector abstractRepositoryConnector = (AbstractRepositoryConnector)this.repositoryConnector;
			TuleapSoapConnector trackerSoapConnector = new TuleapSoapConnector(this.location);
			hit = trackerSoapConnector.performQuery(query, collector, mapper,
					(TuleapTaskDataHandler)abstractRepositoryConnector.getTaskDataHandler(), this,
					TaskDataCollector.MAX_HITS, monitor);
		}
		return hit > 0;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.client.ITuleapClient#updateAttributes(org.eclipse.core.runtime.IProgressMonitor,
	 *      boolean)
	 */
	public void updateAttributes(IProgressMonitor monitor, boolean forceRefresh) {
		if (!this.hasAttributes() || forceRefresh) {
			this.updateAttributes(monitor);
			this.configuration.setLastUpdate(System.currentTimeMillis());
		}
	}

	/**
	 * Returns <code>true</code> if attributes have already been set once, <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if attributes have already been set once, <code>false</code> otherwise.
	 */
	public boolean hasAttributes() {
		return this.configuration.getLastUpdate() != 0;
	}

	/**
	 * Updates the known attributes of the Tuleap repository.
	 * 
	 * @param monitor
	 *            The progress monitor
	 */
	private void updateAttributes(IProgressMonitor monitor) {
		TuleapSoapConnector tuleapSoapConnector = new TuleapSoapConnector(location);

		TuleapServerConfiguration newConfiguration = tuleapSoapConnector
				.getTuleapInstanceConfiguration(monitor);
		if (newConfiguration != null) {
			this.configuration = newConfiguration;
		} else {
			TuleapCoreActivator.log(TuleapMylynTasksMessages.getString(
					"TuleapClient.FailToRetrieveTheConfiguration", this.location.getUrl()), false); //$NON-NLS-1$
		}

		this.repositoryConnector.putRepositoryConfiguration(this.location.getUrl(), configuration);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.client.ITuleapClient#getArtifact(java.lang.String,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public TuleapArtifact getArtifact(String taskId, IProgressMonitor monitor) throws CoreException {
		TuleapSoapConnector tuleapSoapConnector = new TuleapSoapConnector(this.location);
		int trackerId = -1;
		int artifactId = Integer.valueOf(taskId).intValue();
		if (artifactId != -1) {
			TuleapArtifact tuleapArtifact;
			try {
				tuleapArtifact = tuleapSoapConnector.getArtifact(trackerId, artifactId, monitor);
			} catch (MalformedURLException e) {
				IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
				throw new CoreException(status);
			} catch (RemoteException e) {
				IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
				throw new CoreException(status);
			} catch (ServiceException e) {
				IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
				throw new CoreException(status);
			}
			return tuleapArtifact;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.client.ITuleapClient#createArtifact(org.tuleap.mylyn.task.internal.core.model.TuleapArtifact,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public String createArtifact(TuleapArtifact artifact, IProgressMonitor monitor) throws CoreException {
		TuleapSoapConnector tuleapSoapConnector = new TuleapSoapConnector(this.location);
		String taskDataId;
		try {
			taskDataId = tuleapSoapConnector.createArtifact(artifact, monitor);
		} catch (RemoteException e) {
			IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		} catch (MalformedURLException e) {
			IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		} catch (ServiceException e) {
			IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		}
		return taskDataId;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.client.ITuleapClient#updateArtifact(org.tuleap.mylyn.task.internal.core.model.TuleapArtifact,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void updateArtifact(TuleapArtifact artifact, IProgressMonitor monitor) throws CoreException {
		TuleapSoapConnector tuleapSoapConnector = new TuleapSoapConnector(this.location);
		try {
			tuleapSoapConnector.updateArtifact(artifact, monitor);
		} catch (MalformedURLException e) {
			IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		} catch (RemoteException e) {
			IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		} catch (ServiceException e) {
			IStatus status = new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.client.ITuleapClient#getRepositoryConfiguration()
	 */
	public TuleapServerConfiguration getRepositoryConfiguration() {
		return this.configuration;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.client.ITuleapClient#getTaskRepository()
	 */
	public TaskRepository getTaskRepository() {
		return this.taskRepository;
	}
}
