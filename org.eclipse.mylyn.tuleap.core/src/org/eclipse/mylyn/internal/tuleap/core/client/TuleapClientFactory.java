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
package org.eclipse.mylyn.internal.tuleap.core.client;

import org.eclipse.mylyn.internal.tuleap.core.repository.TuleapRepositoryConnector;
import org.eclipse.mylyn.tasks.core.TaskRepository;

/**
 * The Tuleap client factory is in charge of the creation of the client.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public final class TuleapClientFactory {

	/**
	 * The constructor.
	 */
	private TuleapClientFactory() {
		// Prevent instantiation
	}

	/**
	 * Returns a new configured Tuleap client for the given Mylyn task repository and the given Tuleap
	 * repository connector.
	 * 
	 * @param taskRepository
	 *            The Mylyn tasks repository
	 * @param connector
	 *            the Tuleap tasks repository connector
	 * @return a new configured Tuleap client for the given Tuleap task repository and the given Tuleap
	 *         repository connector
	 */
	public static TuleapClient createClient(TaskRepository taskRepository, TuleapRepositoryConnector connector) {
		TuleapClient client = new TuleapClient(taskRepository, connector);
		return client;
	}
}
