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
package org.tuleap.mylyn.task.internal.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.ui.util.TuleapMylynTasksUIMessages;

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
		super(TuleapMylynTasksUIMessages.getString("TuleapProjectPage.PageName")); //$NON-NLS-1$
		this.setTitle(TuleapMylynTasksUIMessages.getString("TuleapProjectPage.PageTitle")); //$NON-NLS-1$
		this.setDescription(TuleapMylynTasksUIMessages.getString("TuleapProjectPage.PageDescription")); //$NON-NLS-1$
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
				TuleapProjectConfiguration projectSelected = TuleapProjectPage.this.getProjectSelected();
				if (projectSelected == null) {
					TuleapProjectPage.this.setErrorMessage(TuleapMylynTasksUIMessages
							.getString("TuleapProjectPage.SelectAProject")); //$NON-NLS-1$
				} else {
					TuleapProjectPage.this.setErrorMessage(null);
					TuleapProjectPage.this.setMessage(null);
				}
				IWizard wizard = TuleapProjectPage.this.getWizard();
				wizard.getContainer().updateButtons();
			}
		});

		Button updateButton = new Button(composite, SWT.LEFT | SWT.PUSH);
		updateButton.setText(TuleapMylynTasksUIMessages.getString("TuleapProjectPage.UpdateProjectsList")); //$NON-NLS-1$
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
		final List<TuleapProjectConfiguration> projectsList = new ArrayList<TuleapProjectConfiguration>();
		if (repositoryConnector instanceof ITuleapRepositoryConnector) {
			IRunnableWithProgress runnable = new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					ITuleapRepositoryConnector connector = (ITuleapRepositoryConnector)repositoryConnector;
					final TuleapServerConfiguration instanceConfiguration = connector
							.getRepositoryConfiguration(TuleapProjectPage.this.repository, forceRefresh,
									monitor);
					projectsList.addAll(instanceConfiguration.getAllProjectConfigurations());
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
		}
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
}
