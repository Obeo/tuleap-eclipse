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
package org.eclipse.mylyn.internal.tuleap.core.repository;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;

/**
 * The Tuleap task attribute mapper. This class is in charge of computing and returning the property of each
 * attribute of the tasks.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapAttributeMapper extends TaskAttributeMapper {
	/**
	 * The constructor.
	 * 
	 * @param taskRepository
	 *            The Mylyn task repository.
	 * @param repositoryConnector
	 *            The Tuleap repository connector.
	 */
	public TuleapAttributeMapper(TaskRepository taskRepository, ITuleapRepositoryConnector repositoryConnector) {
		super(taskRepository);
	}

	/**
	 * Indicates if the given task attribute is used internally by Tuleap.
	 * 
	 * @param taskAttribute
	 *            The task attribute
	 * @return <code>true</code> if the task attribute is used internally by Tuleap, <code>false</code>
	 *         otherwise.
	 */
	public static boolean isInternalAttribute(TaskAttribute taskAttribute) {
		return false;
	}

}
