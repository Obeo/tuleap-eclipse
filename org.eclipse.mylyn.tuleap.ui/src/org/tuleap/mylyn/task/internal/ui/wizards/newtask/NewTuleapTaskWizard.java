/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.internal.ui.wizards.newtask;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.NewTaskWizard;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapProject;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker;
import org.eclipse.mylyn.tuleap.core.internal.repository.TuleapTaskMapping;
import org.eclipse.ui.INewWizard;
import org.tuleap.mylyn.task.internal.ui.util.TuleapUIMessages;
import org.tuleap.mylyn.task.internal.ui.util.TuleapUiMessagesKeys;
import org.tuleap.mylyn.task.internal.ui.wizards.TuleapProjectPage;
import org.tuleap.mylyn.task.internal.ui.wizards.TuleapTrackerPage;

/**
 * The wizard used to customize the Tuleap tasks editor.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class NewTuleapTaskWizard extends NewTaskWizard implements INewWizard {

	/**
	 * The page where the user will select the project in which the task will be created.
	 */
	private TuleapProjectPage tuleapProjectPage;

	/**
	 * The page where the user will select the tracker in which the task will be created.
	 */
	private TuleapTrackerPage tuleapTrackerPage;

	/**
	 * The constructor used when we are creating a new task.
	 * 
	 * @param taskRepository
	 *            The Mylyn tasks repository
	 * @param taskSelection
	 *            The current task selection
	 */
	public NewTuleapTaskWizard(TaskRepository taskRepository, ITaskMapping taskSelection) {
		super(taskRepository, taskSelection);
		this.setWindowTitle(TuleapUIMessages.getString(TuleapUiMessagesKeys.newTuleapWizardWindowTitle));
	}

	/**
	 * The constructor used when we are creating a new submilestone.
	 * 
	 * @param taskRepository
	 *            The Mylyn tasks repository
	 * @param taskSelection
	 *            The current task selection
	 * @param parentMilestoneId
	 *            The parent milestone id
	 */
	public NewTuleapTaskWizard(TaskRepository taskRepository, ITaskMapping taskSelection,
			String parentMilestoneId) {
		super(taskRepository, taskSelection);
		this.setWindowTitle(TuleapUIMessages.getString(TuleapUiMessagesKeys.newTuleapWizardWindowTitle));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.NewTaskWizard#addPages()
	 */
	@Override
	public void addPages() {
		this.tuleapProjectPage = new TuleapProjectPage(this.getTaskRepository());
		this.addPage(tuleapProjectPage);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#needsPreviousAndNextButtons()
	 */
	@Override
	public boolean needsPreviousAndNextButtons() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#getNextPage(org.eclipse.jface.wizard.IWizardPage)
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page instanceof TuleapProjectPage) {
			TuleapProjectPage projectPage = (TuleapProjectPage)page;
			TuleapProject projectSelected = projectPage.getProjectSelected();
			if (projectSelected != null) {
				this.tuleapTrackerPage = new TuleapTrackerPage(projectSelected.getAllTrackers());
				this.tuleapTrackerPage.setWizard(this);
				return this.tuleapTrackerPage;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#getPreviousPage(org.eclipse.jface.wizard.IWizardPage)
	 */
	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {
		if (page instanceof TuleapTrackerPage) {
			return this.tuleapProjectPage;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		IWizardPage currentPage = this.getContainer().getCurrentPage();

		if (currentPage instanceof TuleapTrackerPage && currentPage.isPageComplete()) {
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.NewTaskWizard#getInitializationData()
	 */
	@Override
	protected ITaskMapping getInitializationData() {
		// Returns the initialization data containing the id of the tracker to use while creating the task
		final TuleapTracker trackerSelected = this.tuleapTrackerPage.getTrackerSelected();

		ITaskMapping taskMapping = new TuleapTaskMapping(trackerSelected);
		return taskMapping;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#needsProgressMonitor()
	 */
	@Override
	public boolean needsProgressMonitor() {
		// To display the progress bar while updating the list of available projects
		return true;
	}
}
