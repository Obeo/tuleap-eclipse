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
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapClient;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonParser;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonSerializer;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.server.ITuleapAPIVersions;
import org.tuleap.mylyn.task.internal.core.server.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.server.rest.TuleapRestConnector;

/**
 * The Tuleap client manager will create new clients for a given Mylyn tasks repository or find existing ones.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapClientManager implements IRepositoryListener {

	/**
	 * The Tuleap repository connector.
	 */
	private ITuleapRepositoryConnector repositoryConnector;

	/**
	 * The SOAP client cache.
	 */
	private Map<TaskRepository, TuleapSoapClient> soapClientCache = new HashMap<TaskRepository, TuleapSoapClient>();

	/**
	 * The REST client cache.
	 */
	private Map<TaskRepository, TuleapRestClient> restClientCache = new HashMap<TaskRepository, TuleapRestClient>();

	/**
	 * The constructor.
	 * 
	 * @param connector
	 *            The Tuleap repository connector.
	 */
	public TuleapClientManager(ITuleapRepositoryConnector connector) {
		this.repositoryConnector = connector;
	}

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
		return this.restClientCache.get(taskRepository);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.IRepositoryListener#repositoryAdded(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	public void repositoryAdded(TaskRepository taskRepository) {
		// Create both clients
		AbstractWebLocation webLocation = new TaskRepositoryLocationFactory()
				.createWebLocation(taskRepository);

		ILog logger = Platform.getLog(Platform.getBundle(TuleapCoreActivator.PLUGIN_ID));

		// Create the SOAP client
		TuleapSoapClient tuleapSoapClient = new TuleapSoapClient(taskRepository, webLocation);
		this.soapClientCache.put(taskRepository, tuleapSoapClient);

		// Create the REST client
		TuleapJsonParser jsonParser = new TuleapJsonParser();
		TuleapJsonSerializer jsonSerializer = new TuleapJsonSerializer();
		TuleapRestConnector tuleapRestConnector = new TuleapRestConnector(taskRepository.getUrl(),
				ITuleapAPIVersions.V1_0, logger);
		TuleapRestClient tuleapRestClient = new TuleapRestClient(tuleapRestConnector, jsonParser,
				jsonSerializer, taskRepository, logger);
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
