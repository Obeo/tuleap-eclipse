/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.ui.internal.wizards.query;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.RepositoryQueryWizard;
import org.tuleap.mylyn.task.core.internal.client.ITuleapQueryConstants;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapProject;
import org.tuleap.mylyn.task.ui.internal.wizards.TuleapTrackerPage;

/**
 * The wizard used to create queries.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapRepositoryQueryWizard extends RepositoryQueryWizard {

	/**
	 * The query to edit in this wizard.
	 */
	private IRepositoryQuery query;

	/**
	 * The query project page.
	 */
	private TuleapQueryProjectPage queryProjectPage;

	/**
	 * The Tuleap tracker page.
	 */
	private TuleapTrackerPage tuleapTrackerPage;

	/**
	 * The constructor.
	 *
	 * @param repository
	 *            The task repository.
	 */
	public TuleapRepositoryQueryWizard(TaskRepository repository) {
		super(repository);
	}

	/**
	 * The constructor.
	 *
	 * @param repository
	 *            The task repository.
	 * @param queryToEdit
	 *            The query to edit
	 */
	public TuleapRepositoryQueryWizard(TaskRepository repository, IRepositoryQuery queryToEdit) {
		super(repository);
		this.query = queryToEdit;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		if (this.query == null) {
			// New query
			queryProjectPage = new TuleapQueryProjectPage(this.getTaskRepository());
			this.addPage(queryProjectPage);

			// For the next pages, see getNextPage(...)
		} else {
			String queryKind = this.query.getAttribute(ITuleapQueryConstants.QUERY_KIND);
			if (ITuleapQueryConstants.QUERY_KIND_CUSTOM.equals(queryKind)) {
				// edit an existing custom query
				this.addPage(new TuleapCustomQueryPage(this.getTaskRepository(), this.query));
			} else {
				// edit an exiting default query
				this.addPage(new TuleapReportPage(this.getTaskRepository(), this.query));
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.wizard.Wizard#getNextPage(org.eclipse.jface.wizard.IWizardPage)
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		IWizardPage nextPage = null;
		if (page instanceof TuleapQueryProjectPage) {
			TuleapQueryProjectPage projectPage = (TuleapQueryProjectPage)page;
			TuleapProject projectSelected = projectPage.getProjectSelected();
			this.tuleapTrackerPage = new TuleapTrackerPage(projectSelected, queryProjectPage
					.getTaskRepository());
			this.tuleapTrackerPage.setWizard(this);
			nextPage = tuleapTrackerPage;
		} else if (page instanceof TuleapTrackerPage) {
			TuleapTrackerPage trackerPage = (TuleapTrackerPage)page;

			if (this.queryProjectPage.isReportQuery()) {
				TuleapReportPage tuleapQueryPage = new TuleapReportPage(getTaskRepository(), trackerPage
						.getTrackerSelected());
				tuleapQueryPage.setQueryTitle(this.queryProjectPage.getQueryTitle());
				tuleapQueryPage.setWizard(this);

				nextPage = tuleapQueryPage;
			} else if (this.queryProjectPage.isCustomQuery()) {
				TuleapCustomQueryPage tuleapCustomQueryPage = new TuleapCustomQueryPage(this
						.getTaskRepository(), trackerPage.getTrackerSelected());
				tuleapCustomQueryPage.setQueryTitle(this.queryProjectPage.getQueryTitle());
				tuleapCustomQueryPage.setWizard(this);
				nextPage = tuleapCustomQueryPage;
			}
		}

		if (nextPage != null) {
			return nextPage;
		}
		return super.getNextPage(page);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.ui.wizards.RepositoryQueryWizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		IWizardPage currentPage = this.getContainer().getCurrentPage();
		boolean result = true;
		if (currentPage instanceof TuleapQueryProjectPage) {
			result = currentPage.isPageComplete();
		} else if (currentPage instanceof TuleapCustomQueryPage) {
			result = currentPage.isPageComplete();
		}
		return result && super.canFinish();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.wizard.Wizard#needsProgressMonitor()
	 */
	@Override
	public boolean needsProgressMonitor() {
		return true;
	}
}
