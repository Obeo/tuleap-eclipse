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

import org.eclipse.mylyn.tasks.core.TaskRepository;

/**
 * This interface represents a Tuleap client manager.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public interface ITuleapClientManager {
	/**
	 * Returns the Tuleap client matching the given Mylyn tasks repository.
	 * 
	 * @param taskRepository
	 *            The Mylyn tasks repository
	 * @return the Tuleap client matching the given Mylyn tasks repository
	 */
	ITuleapClient getClient(TaskRepository taskRepository);
}
