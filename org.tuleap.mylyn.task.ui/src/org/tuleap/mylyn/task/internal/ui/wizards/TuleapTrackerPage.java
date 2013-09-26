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
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.ui.util.TuleapMylynTasksUIMessages;
import org.tuleap.mylyn.task.internal.ui.wizards.query.TuleapQueryProjectPage;

/**
 * This page will be used when a new task is created in order to let the user select the tracker for which the
 * task will be created.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapTrackerPage extends WizardPage {

	/**
	 * The hinted height of the viewer containing the list of the tracker.
	 */
	private static final int HEIGHT_HINT = 250;

	/**
	 * The task repository.
	 */
	private TaskRepository repository;

	/**
	 * The widget where the available trackers will be displayed.
	 */
	private FilteredTree trackerTree;

	/**
	 * The constructor.
	 * 
	 * @param taskRepository
	 *            The task repository
	 */
	public TuleapTrackerPage(TaskRepository taskRepository) {
		super(TuleapMylynTasksUIMessages.getString("TuleapTrackerPage.PageName")); //$NON-NLS-1$
		this.setTitle(TuleapMylynTasksUIMessages.getString("TuleapTrackerPage.PageTitle")); //$NON-NLS-1$
		this.setDescription(TuleapMylynTasksUIMessages.getString("TuleapTrackerPage.PageDescription")); //$NON-NLS-1$
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

		trackerTree = new FilteredTree(composite, SWT.SINGLE | SWT.BORDER, new PatternFilter(), true);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.heightHint = HEIGHT_HINT;
		gridData.widthHint = SWT.DEFAULT;

		trackerTree.setLayoutData(gridData);
		TreeViewer viewer = trackerTree.getViewer();
		viewer.setLabelProvider(new TuleapTrackerLabelProvider());
		viewer.setContentProvider(new TuleapTrackerContentProvider());
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				TuleapTrackerConfiguration trackerSelected = TuleapTrackerPage.this.getTrackerSelected();
				if (trackerSelected == null) {
					TuleapTrackerPage.this.setErrorMessage(TuleapMylynTasksUIMessages
							.getString("TuleapTrackerPage.SelectATracker")); //$NON-NLS-1$
				} else {
					TuleapTrackerPage.this.setErrorMessage(null);
					TuleapTrackerPage.this.setMessage(null);
				}
				IWizard wizard = TuleapTrackerPage.this.getWizard();
				wizard.getContainer().updateButtons();
			}
		});

		Dialog.applyDialogFont(composite);

		this.updateTrackersList(false, false);

		setControl(composite);
	}

	/**
	 * Updates the list of the trackers that should be displayed in the widget.
	 * 
	 * @param forceRefresh
	 *            Indicates if we should force the refresh of the list of the trackers.
	 * @param inWizard
	 *            Indicates if we should display the progression in the wizard or in an external progress
	 *            monitor.
	 */
	private void updateTrackersList(final boolean forceRefresh, boolean inWizard) {
		String connectorKind = this.repository.getConnectorKind();
		final AbstractRepositoryConnector repositoryConnector = TasksUi.getRepositoryManager()
				.getRepositoryConnector(connectorKind);
		final List<TuleapTrackerConfiguration> trackersList = new ArrayList<TuleapTrackerConfiguration>();
		if (repositoryConnector instanceof ITuleapRepositoryConnector) {
			final List<TuleapTrackerConfiguration> allTrackerConfigurations = new ArrayList<TuleapTrackerConfiguration>();

			IWizardPage previousPage = TuleapTrackerPage.this.getPreviousPage();

			TuleapProjectConfiguration projectSelected = null;
			if (previousPage instanceof TuleapProjectPage) {
				projectSelected = ((TuleapProjectPage)previousPage).getProjectSelected();
			} else if (previousPage instanceof TuleapQueryProjectPage) {
				projectSelected = ((TuleapQueryProjectPage)previousPage).getProjectSelected();
			}

			if (projectSelected != null) {
				allTrackerConfigurations.addAll(projectSelected.getAllTrackerConfigurations());
			}

			IRunnableWithProgress runnable = new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					for (TuleapTrackerConfiguration tuleapTrackerConfiguration : allTrackerConfigurations) {
						trackersList.add(tuleapTrackerConfiguration);
					}
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

			this.trackerTree.getViewer().setInput(trackersList);
		}
	}

	/**
	 * Returns the tracker where the new task should be created.
	 * 
	 * @return The tracker where the new task should be created.
	 */
	public TuleapTrackerConfiguration getTrackerSelected() {
		IStructuredSelection selection = (IStructuredSelection)this.trackerTree.getViewer().getSelection();
		if (selection.getFirstElement() instanceof TuleapTrackerConfiguration) {
			return (TuleapTrackerConfiguration)selection.getFirstElement();
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
		return this.getTrackerSelected() != null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		this.updateTrackersList(false, true);
		super.setVisible(visible);
	}
}
