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
package org.eclipse.mylyn.internal.tuleap.ui.wizards;

import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.NewTaskWizard;
import org.eclipse.ui.INewWizard;

/**
 * The wizard used to customize the Tuleap tasks editor.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class NewTuleapTaskWizard extends NewTaskWizard implements INewWizard {

	/**
	 * The constructor.
	 * 
	 * @param taskRepository
	 *            The Mylyn tasks repository
	 * @param taskSelection
	 *            The current task selection
	 */
	public NewTuleapTaskWizard(TaskRepository taskRepository, ITaskMapping taskSelection) {
		super(taskRepository, taskSelection);
	}

}
