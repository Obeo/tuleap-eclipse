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

import org.eclipse.mylyn.internal.tuleap.ui.util.TuleapMylynTasksUIMessages;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.TaskMapping;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.NewTaskWizard;
import org.eclipse.ui.INewWizard;

/**
 * The wizard used to customize the Tuleap tasks editor.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class NewTuleapTaskWizard extends NewTaskWizard implements INewWizard {

	/**
	 * The page where the user will select the tracker in which the task will be created.
	 */
	private TuleapTrackerPage tuleapTrackerPage;

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
		this.setWindowTitle(TuleapMylynTasksUIMessages.getString("NewTuleapWizard.WindowTitle")); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.NewTaskWizard#addPages()
	 */
	@Override
	public void addPages() {
		this.tuleapTrackerPage = new TuleapTrackerPage(this.getTaskRepository());
		this.addPage(tuleapTrackerPage);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.NewTaskWizard#getInitializationData()
	 */
	@Override
	protected ITaskMapping getInitializationData() {
		// Returns the initialization data containing the id of the tracker to use while creating the task
		final String trackerSelected = this.tuleapTrackerPage.getTrackerSelected();

		ITaskMapping taskMapping = new TaskMapping() {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.mylyn.tasks.core.TaskMapping#getProduct()
			 */
			@Override
			public String getProduct() {
				return trackerSelected;
			}
		};
		return taskMapping;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#needsProgressMonitor()
	 */
	@Override
	public boolean needsProgressMonitor() {
		// To display the progress bar while updating the list of available trackers
		return true;
	}
}
