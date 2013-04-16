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

import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;

/**
 * The Tuleap client factory is in charge of the creation of the client.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public final class TuleapClientFactory {

	/**
	 * The sole instance.
	 */
	private static TuleapClientFactory instance;

	/**
	 * The constructor.
	 */
	private TuleapClientFactory() {
		// Prevent instantiation
	}

	/**
	 * Returns the sole instance of the client factory.
	 * 
	 * @return The sole instance of the client factory.
	 */
	public static synchronized TuleapClientFactory getDefault() {
		if (instance == null) {
			instance = new TuleapClientFactory();
		}
		return instance;
	}

	/**
	 * Returns a new configured Tuleap client for the given tracker location and the given Tuleap repository
	 * connector.
	 * 
	 * @param repository
	 *            The task repository
	 * @param location
	 *            The location of the Tuleap tracker
	 * @param connector
	 *            the Tuleap tasks repository connector
	 * @return a new configured Tuleap client for the given Tuleap task repository and the given Tuleap
	 *         repository connector
	 */
	public ITuleapClient createClient(TaskRepository repository, AbstractWebLocation location,
			ITuleapRepositoryConnector connector) {
		TuleapClient client = new TuleapClient(repository, location, connector);
		return client;
	}
}
