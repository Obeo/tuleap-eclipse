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
package org.tuleap.mylyn.task.ui.internal.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttachmentModel;
import org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi;
import org.eclipse.mylyn.tasks.ui.TaskHyperlink;
import org.eclipse.mylyn.tasks.ui.wizards.ITaskRepositoryPage;
import org.eclipse.mylyn.tasks.ui.wizards.ITaskSearchPage;
import org.tuleap.mylyn.task.core.internal.repository.TuleapUrlUtil;
import org.tuleap.mylyn.task.core.internal.util.ITuleapConstants;
import org.tuleap.mylyn.task.ui.internal.util.TuleapUIKeys;
import org.tuleap.mylyn.task.ui.internal.util.TuleapUIMessages;
import org.tuleap.mylyn.task.ui.internal.wizards.attachment.TuleapTaskAttachmentPage;
import org.tuleap.mylyn.task.ui.internal.wizards.newtask.NewTuleapTaskWizard;
import org.tuleap.mylyn.task.ui.internal.wizards.query.TuleapRepositoryQueryWizard;

/**
 * Utility class managing all the user interface related operations with the Tuleap repository.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapConnectorUi extends AbstractRepositoryConnectorUi {

	/**
	 * The constructor.
	 */
	public TuleapConnectorUi() {
		// nothing
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi#getAccountCreationUrl(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public String getAccountCreationUrl(TaskRepository taskRepository) {
		return TuleapUrlUtil.getAccountCreationUrl(taskRepository);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi#getAccountManagementUrl(org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public String getAccountManagementUrl(TaskRepository taskRepository) {
		return TuleapUrlUtil.getAccountManagementUrl(taskRepository);
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
		List<IHyperlink> hyperlinks = new ArrayList<IHyperlink>();
		Matcher matcher = Pattern.compile("(^|[\\s\\(\\)])(([A-Za-z]+):([A-Za-z]+)-\\d+)").matcher(text); //$NON-NLS-1$
		while (matcher.find()) {
			int start = matcher.start(2);
			int length = matcher.end() - start;

			Region region = new Region(textOffset + start, length);
			hyperlinks.add(new TaskHyperlink(region, repository, matcher.group(2)));
		}
		return hyperlinks.toArray(new IHyperlink[hyperlinks.size()]);
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
			taskKind = TuleapUIMessages.getString(TuleapUIKeys.taskKindLabel);
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
		TuleapRepositoryQueryWizard wizard = null;

		if (queryToEdit == null) {
			wizard = new TuleapRepositoryQueryWizard(taskRepository);
		} else {
			wizard = new TuleapRepositoryQueryWizard(taskRepository, queryToEdit);
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

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi#getTaskAttachmentPage(org.eclipse.mylyn.tasks.core.data.TaskAttachmentModel)
	 */
	@Override
	public IWizardPage getTaskAttachmentPage(TaskAttachmentModel model) {
		return new TuleapTaskAttachmentPage(model);
	}
}
