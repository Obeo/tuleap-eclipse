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
package org.eclipse.mylyn.internal.tuleap.ui.repository;

import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.mylyn.internal.tuleap.core.util.ITuleapConstants;
import org.eclipse.mylyn.internal.tuleap.core.util.TuleapUtil;
import org.eclipse.mylyn.internal.tuleap.ui.util.TuleapMylynTasksUIMessages;
import org.eclipse.mylyn.internal.tuleap.ui.wizards.NewTuleapTaskWizard;
import org.eclipse.mylyn.internal.tuleap.ui.wizards.query.TuleapCustomQueryPage;
import org.eclipse.mylyn.internal.tuleap.ui.wizards.query.TuleapQueryPage;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi;
import org.eclipse.mylyn.tasks.ui.wizards.ITaskRepositoryPage;
import org.eclipse.mylyn.tasks.ui.wizards.ITaskSearchPage;
import org.eclipse.mylyn.tasks.ui.wizards.RepositoryQueryWizard;

/**
 * Utility class managing all the user interface related operations with the Tuleap repository.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapConnectorUi extends AbstractRepositoryConnectorUi {

	/**
	 * The constructor.
	 */
	public TuleapConnectorUi() {
		// nothing
	}

	@Override
	public String getAccountCreationUrl(TaskRepository taskRepository) {
		// Account creation url : https://<domain-name>/account/register.php
		return TuleapUtil.getDomainRepositoryURL(taskRepository.getRepositoryUrl()) + "/account/register.php"; //$NON-NLS-1$
	}

	@Override
	public String getAccountManagementUrl(TaskRepository taskRepository) {
		// Account management url : https://<domain-name>/my/
		return TuleapUtil.getDomainRepositoryURL(taskRepository.getRepositoryUrl()) + "/my/"; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi#findHyperlinks(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.ITask, java.lang.String, int, int)
	 */
	@Override
	public IHyperlink[] findHyperlinks(TaskRepository repository, ITask task, String text, int index,
			int textOffset) {
		// TODO Create a custom hyperlink artifact finder
		return super.findHyperlinks(repository, task, text, index, textOffset);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi#getTaskKindLabel(org.eclipse.mylyn.tasks.core.ITask)
	 */
	@Override
	public String getTaskKindLabel(ITask task) {
		String taskKind = task.getTaskKind();
		if (taskKind == null || taskKind.length() == 0 || taskKind.equals(ITuleapConstants.CONNECTOR_KIND)) {
			// By default, use the name "Artifact" if we don"t have anything, or just "tuleap"
			taskKind = TuleapMylynTasksUIMessages.getString("TuleapConnectorUi.TaskKindLabel"); //$NON-NLS-1$
		}
		return taskKind;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi#getConnectorKind()
	 */
	@Override
	public String getConnectorKind() {
		return ITuleapConstants.CONNECTOR_KIND;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi#hasSearchPage()
	 */
	@Override
	public boolean hasSearchPage() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi#getSearchPage(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public ITaskSearchPage getSearchPage(TaskRepository repository, IStructuredSelection selection) {
		// TODO Create a custom search page
		return super.getSearchPage(repository, selection);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi#getSettingsPage(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public ITaskRepositoryPage getSettingsPage(TaskRepository taskRepository) {
		return new TuleapRepositorySettingsPage(taskRepository);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi#getQueryWizard(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.IRepositoryQuery)
	 */
	@Override
	public IWizard getQueryWizard(TaskRepository taskRepository, IRepositoryQuery queryToEdit) {
		// TODO Subclass repository query wizard to handle next/finish
		RepositoryQueryWizard wizard = new RepositoryQueryWizard(taskRepository);
		if (queryToEdit != null) {
			// edit an existing query
			wizard.addPage(new TuleapCustomQueryPage(taskRepository, queryToEdit));
		} else {
			// new query
			wizard.addPage(new TuleapQueryPage(taskRepository));
		}
		return wizard;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi#getNewTaskWizard(org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.mylyn.tasks.core.ITaskMapping)
	 */
	@Override
	public IWizard getNewTaskWizard(TaskRepository taskRepository, ITaskMapping selection) {
		return new NewTuleapTaskWizard(taskRepository, selection);
	}

}
