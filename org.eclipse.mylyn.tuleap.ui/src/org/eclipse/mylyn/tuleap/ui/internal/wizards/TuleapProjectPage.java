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
package org.eclipse.mylyn.tuleap.ui.internal.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapProject;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapServer;
import org.eclipse.mylyn.tuleap.core.internal.repository.ITuleapRepositoryConnector;
import org.eclipse.mylyn.tuleap.ui.internal.TuleapTasksUIPlugin;
import org.eclipse.mylyn.tuleap.ui.internal.util.ITuleapUIConstants;
import org.eclipse.mylyn.tuleap.ui.internal.util.TuleapUIKeys;
import org.eclipse.mylyn.tuleap.ui.internal.util.TuleapUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

/**
 * This page will let the user select the project on which the task will be created.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapProjectPage extends WizardPage {
	/**
	 * The hinted height of the viewer containing the list of the projects.
	 */
	private static final int HEIGHT_HINT = 250;

	/**
	 * The task repository.
	 */
	private TaskRepository repository;

	/**
	 * The widget where the available projects will be displayed.
	 */
	private FilteredTree projectsTree;

	/**
	 * The constructor.
	 *
	 * @param taskRepository
	 *            The task repository
	 */
	public TuleapProjectPage(TaskRepository taskRepository) {
		super(TuleapUIMessages.getString(TuleapUIKeys.tuleapProjectPageName));
		this.setTitle(TuleapUIMessages.getString(TuleapUIKeys.tuleapProjectPageTitle));
		this.setDescription(TuleapUIMessages.getString(TuleapUIKeys.tuleapProjectPageDescription));
		this.repository = taskRepository;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout());

		projectsTree = new FilteredTree(composite, SWT.SINGLE | SWT.BORDER, new PatternFilter(), true);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.heightHint = HEIGHT_HINT;
		gridData.widthHint = SWT.DEFAULT;

		projectsTree.setLayoutData(gridData);
		TreeViewer viewer = projectsTree.getViewer();
		viewer.setLabelProvider(new TuleapProjectLabelProvider());
		viewer.setContentProvider(new TuleapProjectContentProvider());
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				TuleapProject projectSelected = TuleapProjectPage.this.getProjectSelected();
				if (projectSelected == null) {
					TuleapProjectPage.this.setErrorMessage(TuleapUIMessages
							.getString(TuleapUIKeys.tuleapProjectPageSelectAProject));
				} else {
					TuleapProjectPage.this.setErrorMessage(null);
					TuleapProjectPage.this.setMessage(null);
				}
				IWizard wizard = TuleapProjectPage.this.getWizard();
				if (wizard.getContainer().getCurrentPage() != null) {
					wizard.getContainer().updateButtons();
				}
			}
		});

		Button updateButton = new Button(composite, SWT.LEFT | SWT.PUSH);
		updateButton.setText(TuleapUIMessages.getString(TuleapUIKeys.tuleapProjectPageUpdateProjectsList));
		updateButton.setLayoutData(new GridData());
		updateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				TuleapProjectPage.this.updateProjectsList(true, true);
			}
		});

		Dialog.applyDialogFont(composite);

		this.updateProjectsList(false, false);

		setControl(composite);
	}

	/**
	 * Updates the list of the projects that should be displayed in the wizard.
	 *
	 * @param forceRefresh
	 *            Indicates if we should force the refresh of the list of the projects.
	 * @param inWizard
	 *            Indicates if we should display the progression in the wizard or in an external progress
	 *            monitor.
	 */
	private void updateProjectsList(final boolean forceRefresh, boolean inWizard) {
		String connectorKind = this.repository.getConnectorKind();
		final AbstractRepositoryConnector repositoryConnector = TasksUi.getRepositoryManager()
				.getRepositoryConnector(connectorKind);
		final List<TuleapProject> projectsList = new ArrayList<TuleapProject>();
		if (repositoryConnector instanceof ITuleapRepositoryConnector) {
			IRunnableWithProgress runnable = new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor) throws InvocationTargetException,
				InterruptedException {
					ITuleapRepositoryConnector connector = (ITuleapRepositoryConnector)repositoryConnector;
					final TuleapServer instanceConfiguration = connector
							.getServer(TuleapProjectPage.this.repository.getRepositoryUrl());
					projectsList.addAll(instanceConfiguration.getAllProjects());
				}
			};

			try {
				if (inWizard) {
					this.getContainer().run(true, false, runnable);
				} else {
					PlatformUI.getWorkbench().getProgressService().run(true, false, runnable);
				}
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				if (inWizard) {
					this.getWizard().getContainer().updateButtons();
				}
			}

			this.projectsTree.getViewer().setInput(projectsList);
			if (projectsList.size() > 0) {
				IStructuredSelection selection = new StructuredSelection(projectsList.get(0));
				this.projectsTree.getViewer().setSelection(selection);
			}
		}
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
		return this.getProjectSelected() != null;
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
}
