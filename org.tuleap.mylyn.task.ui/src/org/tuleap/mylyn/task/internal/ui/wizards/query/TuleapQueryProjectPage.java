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
package org.tuleap.mylyn.task.internal.ui.wizards.query;

import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.mylyn.commons.workbench.forms.SectionComposite;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.tuleap.mylyn.task.internal.core.client.ITuleapQueryConstants;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapProject;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapServer;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.ui.TuleapTasksUIPlugin;
import org.tuleap.mylyn.task.internal.ui.util.ITuleapUIConstants;
import org.tuleap.mylyn.task.internal.ui.util.TuleapUIMessages;
import org.tuleap.mylyn.task.internal.ui.util.TuleapUiMessagesKeys;
import org.tuleap.mylyn.task.internal.ui.wizards.TuleapProjectContentProvider;
import org.tuleap.mylyn.task.internal.ui.wizards.TuleapProjectLabelProvider;

/**
 * This page will let the user select the project on which the task will be created.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class TuleapQueryProjectPage extends AbstractRepositoryQueryPage2 {

	/**
	 * The widget where the available projects will be displayed.
	 */
	private FilteredTree projectsTree;

	/**
	 * The button used to select to create a query based on a report.
	 */
	private Button reportButton;

	/**
	 * The button used to select to create a custom query.
	 */
	private Button customQueryButton;

	/**
	 * The button used to select to create a top level planning query.
	 */
	private Button topLevelPlanningButton;

	/**
	 * The constructor.
	 * 
	 * @param taskRepository
	 *            The task repository
	 */
	public TuleapQueryProjectPage(TaskRepository taskRepository) {
		super(TuleapUIMessages.getString(TuleapUiMessagesKeys.tuleapProjectPageName), taskRepository, null);
		this.setTitle(TuleapUIMessages.getString(TuleapUiMessagesKeys.tuleapProjectPageTitle));
		this.setDescription(TuleapUIMessages.getString(TuleapUiMessagesKeys.tuleapProjectPageDescription));
	}

	/**
	 * Returns the identifier of the project currently selected in the wizard. -1 if none has been selected.
	 * 
	 * @return The identifier of the project currently selected in the wizard. -1 if none has been selected
	 */
	public TuleapProject getProjectSelected() {
		IStructuredSelection selection = (IStructuredSelection)this.projectsTree.getViewer().getSelection();
		Object firstElement = selection.getFirstElement();
		if (firstElement instanceof TuleapProject) {
			return (TuleapProject)firstElement;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	@Override
	public boolean isPageComplete() {
		return this.getProjectSelected() != null && this.getQueryTitle() != null
				&& this.getQueryTitle().length() > 0;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#getImage()
	 */
	@Override
	public Image getImage() {
		return TuleapTasksUIPlugin.getDefault().getImage(ITuleapUIConstants.Icons.TULEAP_LOGO_WIZARD_75X66);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2#createPageContent(org.eclipse.mylyn.commons.workbench.forms.SectionComposite)
	 */
	@Override
	protected void createPageContent(SectionComposite parent) {
		Composite composite = new Composite(parent.getContent(), SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessVerticalSpace = false;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(1, false));

		projectsTree = new FilteredTree(composite, SWT.SINGLE | SWT.BORDER, new PatternFilter(), true);

		TreeViewer viewer = projectsTree.getViewer();
		viewer.setLabelProvider(new TuleapProjectLabelProvider());
		viewer.setContentProvider(new TuleapProjectContentProvider());
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				TuleapProject projectSelected = TuleapQueryProjectPage.this.getProjectSelected();
				if (projectSelected == null) {
					TuleapQueryProjectPage.this.setErrorMessage(TuleapUIMessages
							.getString(TuleapUiMessagesKeys.tuleapProjectPageSelectAProject));
				} else {
					TuleapQueryProjectPage.this.setErrorMessage(null);
					TuleapQueryProjectPage.this.setMessage(null);
				}
				IWizard wizard = TuleapQueryProjectPage.this.getWizard();
				if (wizard.getContainer().getCurrentPage() != null) {
					wizard.getContainer().updateButtons();
				}
			}
		});

		reportButton = new Button(composite, SWT.RADIO);
		reportButton.setText(TuleapUIMessages
				.getString(TuleapUiMessagesKeys.tuleapQueryProjectPageReportButtonLabel));
		reportButton.setSelection(true);

		reportButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				getWizard().getContainer().updateButtons();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				getWizard().getContainer().updateButtons();
			}
		});

		customQueryButton = new Button(composite, SWT.RADIO);
		customQueryButton.setText(TuleapUIMessages
				.getString(TuleapUiMessagesKeys.tuleapQueryProjectPageCustomQueryButtonLabel));

		customQueryButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				getWizard().getContainer().updateButtons();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				getWizard().getContainer().updateButtons();
			}
		});

		topLevelPlanningButton = new Button(composite, SWT.RADIO);
		topLevelPlanningButton.setText(TuleapUIMessages
				.getString(TuleapUiMessagesKeys.tuleapQueryProjectPageTopLevelPlanningButtonLabel));
		topLevelPlanningButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				getWizard().getContainer().updateButtons();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				getWizard().getContainer().updateButtons();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2#hasRepositoryConfiguration()
	 */
	@Override
	protected boolean hasRepositoryConfiguration() {
		return getServerConfiguration() != null;
	}

	/**
	 * Get the server configuration.
	 * 
	 * @return the server configuration.
	 */
	private TuleapServer getServerConfiguration() {
		return ((ITuleapRepositoryConnector)getConnector()).getTuleapServerConfiguration(getTaskRepository()
				.getRepositoryUrl());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2#restoreState(org.eclipse.mylyn.tasks.core.IRepositoryQuery)
	 */
	@Override
	protected boolean restoreState(IRepositoryQuery query) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage#applyTo(org.eclipse.mylyn.tasks.core.IRepositoryQuery)
	 */
	@Override
	public void applyTo(IRepositoryQuery query) {
		query.setSummary(this.getQueryTitle());
		query.setAttribute(ITuleapQueryConstants.QUERY_PROJECT_ID, String.valueOf(getProjectSelected()
				.getIdentifier()));
		if (this.isTopLevelPlanningQuery()) {
			query.setAttribute(ITuleapQueryConstants.QUERY_KIND,
					ITuleapQueryConstants.QUERY_KIND_TOP_LEVEL_PLANNING);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2#doRefreshControls()
	 */
	@Override
	protected void doRefreshControls() {
		if (!hasRepositoryConfiguration()) {
			refreshConfiguration(true);
		}
		TuleapServer serverConfig = getServerConfiguration();
		if (serverConfig != null) {
			List<TuleapProject> projectConfigs = serverConfig.getAllProjects();
			this.projectsTree.getViewer().setInput(projectConfigs);

			if (projectConfigs.size() > 0) {
				IStructuredSelection selection = new StructuredSelection(projectConfigs.get(0));
				this.projectsTree.getViewer().setSelection(selection);
			}
		}
	}

	/**
	 * Returns <code>true</code> if the user wants to create a top level planning query, <code>false</code>
	 * otherwise.
	 * 
	 * @return <code>true</code> if the user wants to create a top level planning query, <code>false</code>
	 *         otherwise
	 */
	public boolean isTopLevelPlanningQuery() {
		return topLevelPlanningButton.getSelection();
	}

	/**
	 * Returns <code>true</code> if the user wants to create a report based query, <code>false</code>
	 * otherwise.
	 * 
	 * @return <code>true</code> if the user wants to create a report based query, <code>false</code>
	 *         otherwise.
	 */
	public boolean isReportQuery() {
		return reportButton.getSelection();
	}

	/**
	 * Returns <code>true</code> if the user wants to create a custom query, <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if the user wants to create a custom query, <code>false</code> otherwise.
	 */
	public boolean isCustomQuery() {
		return customQueryButton.getSelection();
	}
}
