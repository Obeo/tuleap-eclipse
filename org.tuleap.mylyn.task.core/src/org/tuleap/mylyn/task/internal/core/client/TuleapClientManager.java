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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.tasks.core.IRepositoryListener;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.TaskRepositoryLocationFactory;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.client.rest.ITuleapAPIVersions;
import org.tuleap.mylyn.task.internal.core.client.rest.RestResourceFactory;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestConnector;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapClient;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapConnector;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapParser;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonParser;

/**
 * The Tuleap client manager will create new clients for a given Mylyn tasks repository or find existing ones.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapClientManager implements IRepositoryListener {

	/**
	 * The SOAP client cache.
	 */
	private Map<TaskRepository, TuleapSoapClient> soapClientCache = new HashMap<TaskRepository, TuleapSoapClient>();

	/**
	 * The REST client cache.
	 */
	private Map<TaskRepository, TuleapRestClient> restClientCache = new HashMap<TaskRepository, TuleapRestClient>();

	/**
	 * Returns the SOAP client for the given task repository. The reference to the created client should not
	 * be kept by those calling this operation since the client can be re-created if the settings of the
	 * repository are modified.
	 * 
	 * @param taskRepository
	 *            The task repository
	 * @return The SOAP client for the given task repository
	 */
	public TuleapSoapClient getSoapClient(TaskRepository taskRepository) {
		TuleapSoapClient tuleapSoapClient = this.soapClientCache.get(taskRepository);
		if (tuleapSoapClient == null && this.restClientCache.get(taskRepository) == null) {
			this.refreshClients(taskRepository);
		}
		return this.soapClientCache.get(taskRepository);
	}

	/**
	 * Returns the REST client for the given task repository. The reference to the created client should not
	 * be kept by those calling this operation since the client can be re-created if the settings of the
	 * repository are modified.
	 * 
	 * @param taskRepository
	 *            The task repository
	 * @return The REST client for the given task repository
	 */
	public TuleapRestClient getRestClient(TaskRepository taskRepository) {
		TuleapRestClient tuleapRestClient = this.restClientCache.get(taskRepository);
		if (tuleapRestClient == null && this.soapClientCache.get(taskRepository) == null) {
			this.refreshClients(taskRepository);
		}
		return this.restClientCache.get(taskRepository);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.IRepositoryListener#repositoryAdded(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	public void repositoryAdded(TaskRepository taskRepository) {
		this.refreshClients(taskRepository);
	}

	/**
	 * Refresh both REST and SOAP clients for the given task repository.
	 * 
	 * @param taskRepository
	 *            The task repository
	 */
	private void refreshClients(TaskRepository taskRepository) {
		// Create both clients
		final AbstractWebLocation webLocation = new TaskRepositoryLocationFactory()
				.createWebLocation(taskRepository);

		ILog logger = Platform.getLog(Platform.getBundle(TuleapCoreActivator.PLUGIN_ID));

		// Create the SOAP client
		TuleapSoapParser tuleapSoapParser = new TuleapSoapParser();
		TuleapSoapConnector tuleapSoapConnector = new TuleapSoapConnector(webLocation);

		TuleapSoapClient tuleapSoapClient = new TuleapSoapClient(tuleapSoapConnector, tuleapSoapParser);
		this.soapClientCache.put(taskRepository, tuleapSoapClient);

		// Create the REST client
		TuleapJsonParser jsonParser = new TuleapJsonParser();
		TuleapRestConnector tuleapRestConnector = new TuleapRestConnector(webLocation, logger);
		RestResourceFactory restResourceFactory = new RestResourceFactory(webLocation.getUrl(),
				ITuleapAPIVersions.BEST_VERSION, tuleapRestConnector, TuleapCoreActivator.getDefault()
						.getLog());
		TuleapRestClient tuleapRestClient = new TuleapRestClient(restResourceFactory, jsonParser,
				taskRepository, logger);
		this.restClientCache.put(taskRepository, tuleapRestClient);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.IRepositoryListener#repositoryRemoved(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	public void repositoryRemoved(TaskRepository taskRepository) {
		// Force the re-creation of the client if the repository changes
		this.restClientCache.remove(taskRepository);
		this.soapClientCache.remove(taskRepository);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.IRepositoryListener#repositorySettingsChanged(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	public void repositorySettingsChanged(TaskRepository taskRepository) {
		// Force the re-creation of the clients if the repository changes
		this.repositoryAdded(taskRepository);
		this.repositoryRemoved(taskRepository);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.IRepositoryListener#repositoryUrlChanged(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      java.lang.String)
	 */
	public void repositoryUrlChanged(TaskRepository taskRepository, String oldUrl) {
		// Force the re-creation of the clients if the repository changes
		this.repositoryAdded(taskRepository);
		this.repositoryRemoved(taskRepository);
	}
}
