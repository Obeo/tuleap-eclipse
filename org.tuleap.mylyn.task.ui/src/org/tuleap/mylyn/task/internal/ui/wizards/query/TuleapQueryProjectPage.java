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
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.mylyn.commons.workbench.forms.SectionComposite;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.tuleap.mylyn.task.internal.core.client.ITuleapQueryConstants;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.ui.util.TuleapMylynTasksUIMessages;
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
	 * Flag to know if it is a wizard for artifact or top level planing.
	 */
	private boolean isWizardForArtifacts = true;

	/**
	 * The constructor.
	 * 
	 * @param taskRepository
	 *            The task repository
	 */
	public TuleapQueryProjectPage(TaskRepository taskRepository) {
		super(TuleapMylynTasksUIMessages.getString("TuleapProjectPage.PageName"), taskRepository, null); //$NON-NLS-1$
		this.setTitle(TuleapMylynTasksUIMessages.getString("TuleapProjectPage.PageTitle")); //$NON-NLS-1$
		this.setDescription(TuleapMylynTasksUIMessages.getString("TuleapProjectPage.PageDescription")); //$NON-NLS-1$
	}

	/**
	 * Returns the identifier of the project currently selected in the wizard. -1 if none has been selected.
	 * 
	 * @return The identifier of the project currently selected in the wizard. -1 if none has been selected
	 */
	public TuleapProjectConfiguration getProjectSelected() {
		IStructuredSelection selection = (IStructuredSelection)this.projectsTree.getViewer().getSelection();
		Object firstElement = selection.getFirstElement();
		if (firstElement instanceof TuleapProjectConfiguration) {
			return (TuleapProjectConfiguration)firstElement;
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
		return this.getProjectSelected() != null;
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
				TuleapProjectConfiguration projectSelected = TuleapQueryProjectPage.this.getProjectSelected();
				if (projectSelected == null) {
					TuleapQueryProjectPage.this.setErrorMessage(TuleapMylynTasksUIMessages
							.getString("TuleapProjectPage.SelectAProject")); //$NON-NLS-1$
				} else {
					TuleapQueryProjectPage.this.setErrorMessage(null);
					TuleapQueryProjectPage.this.setMessage(null);
				}
				IWizard wizard = TuleapQueryProjectPage.this.getWizard();
				wizard.getContainer().updateButtons();
			}
		});

		Button artifactsOption = new Button(composite, SWT.RADIO);
		artifactsOption.setText(TuleapMylynTasksUIMessages
				.getString("TuleapQueryProjectPage.OptionArtifactsLabel")); //$NON-NLS-1$
		artifactsOption.setSelection(isWizardForArtifacts);
		artifactsOption.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				isWizardForArtifacts = true;
				getWizard().getContainer().updateButtons();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				isWizardForArtifacts = true;
				getWizard().getContainer().updateButtons();
			}
		});

		Button topLevelPlanningOption = new Button(composite, SWT.RADIO);
		topLevelPlanningOption.setText(TuleapMylynTasksUIMessages
				.getString("TuleapQueryProjectPage.OptionTopLevelPlanningLabel")); //$NON-NLS-1$
		topLevelPlanningOption.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				isWizardForArtifacts = false;
				getWizard().getContainer().updateButtons();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				isWizardForArtifacts = false;
				getWizard().getContainer().updateButtons();
			}
		});

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#getNextPage()
	 */
	@Override
	public IWizardPage getNextPage() {
		if (isWizardForArtifacts) {
			return super.getNextPage();
		}
		return null;
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
	private TuleapServerConfiguration getServerConfiguration() {
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
		if (!isWizardForArtifacts) {
			query.setAttribute(ITuleapQueryConstants.QUERY_KIND,
					ITuleapQueryConstants.QUERY_KIND_TOP_LEVEL_PLANNING);
		} else {
			query.setAttribute(ITuleapQueryConstants.QUERY_KIND,
					ITuleapQueryConstants.QUERY_KIND_ALL_FROM_TRACKER);
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
		TuleapServerConfiguration serverConfig = getServerConfiguration();
		if (serverConfig != null) {
			List<TuleapProjectConfiguration> projectConfigs = serverConfig.getAllProjectConfigurations();
			this.projectsTree.getViewer().setInput(projectConfigs);
		}
	}
}
