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
package org.tuleap.mylyn.task.ui.internal.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapProject;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapTracker;
import org.tuleap.mylyn.task.core.internal.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.ui.internal.TuleapTasksUIPlugin;
import org.tuleap.mylyn.task.ui.internal.util.ITuleapUIConstants;
import org.tuleap.mylyn.task.ui.internal.util.TuleapUIKeys;
import org.tuleap.mylyn.task.ui.internal.util.TuleapUIMessages;

/**
 * This page will be used when a new task is created in order to let the user select the tracker for which the
 * task will be created.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 0.7
 */
public class TuleapTrackerPage extends WizardPage {

	/**
	 * The hinted height of the viewer containing the list of the tracker.
	 */
	private static final int HEIGHT_HINT = 250;

	/**
	 * The trackers used.
	 */
	private TuleapProject project;

	/**
	 * The task repository used.
	 */
	private TaskRepository repository;

	/**
	 * The widget where the available trackers will be displayed.
	 */
	private FilteredTree trackerTree;

	/**
	 * The label containing the description of the tracker.
	 */
	private Label trackerDescriptionLabel;

	/**
	 * The constructor.
	 *
	 * @param project
	 *            The project, the trackers of which should be displayed
	 * @param repository
	 *            The task repository to use
	 */
	public TuleapTrackerPage(TuleapProject project, TaskRepository repository) {
		super(TuleapUIMessages.getString(TuleapUIKeys.tuleapTrackerPageName));
		this.setTitle(TuleapUIMessages.getString(TuleapUIKeys.tuleapTrackerPageTitle));
		this.setDescription(TuleapUIMessages.getString(TuleapUIKeys.tuleapTrackerPageDescription));
		this.project = project;
		this.repository = repository;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
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

		List<TuleapTracker> trackers = getTrackers();

		this.trackerTree.getViewer().setInput(trackers);

		trackerDescriptionLabel = new Label(composite, SWT.NONE);

		if (trackers.size() > 0) {
			IStructuredSelection selection = new StructuredSelection(trackers.get(0));
			this.trackerTree.getViewer().setSelection(selection);
			String description = trackers.get(0).getDescription();
			trackerDescriptionLabel.setText(TuleapUIMessages.getString(
					TuleapUIKeys.tuleapTrackerPageDescriptionLabel, description));
		}

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				TuleapTracker trackerSelected = TuleapTrackerPage.this.getTrackerSelected();
				if (trackerSelected == null) {
					TuleapTrackerPage.this.setErrorMessage(TuleapUIMessages
							.getString(TuleapUIKeys.tuleapTrackerPageSelectATracker));
				} else {
					TuleapTrackerPage.this.setErrorMessage(null);
					TuleapTrackerPage.this.setMessage(null);
					String description = trackerSelected.getDescription();
					TuleapTrackerPage.this.trackerDescriptionLabel.setText(TuleapUIMessages.getString(
							TuleapUIKeys.tuleapTrackerPageDescriptionLabel, description));
				}
				IWizard wizard = TuleapTrackerPage.this.getWizard();
				wizard.getContainer().updateButtons();
			}
		});

		Dialog.applyDialogFont(composite);

		setControl(composite);
	}

	/**
	 * Retrieve the project trackers, after fetching them if necessary.
	 *
	 * @return The list of trackers of this page's project.
	 */
	private List<TuleapTracker> getTrackers() {
		final List<TuleapTracker> trackers = project.getAllTrackers();
		String connectorKind = this.repository.getConnectorKind();
		if (trackers.isEmpty()) {
			final AbstractRepositoryConnector repositoryConnector = TasksUi.getRepositoryManager()
					.getRepositoryConnector(connectorKind);
			if (repositoryConnector instanceof ITuleapRepositoryConnector) {
				IRunnableWithProgress runnable = new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException,
							InterruptedException {
						monitor.subTask(TuleapUIMessages.getString(
								TuleapUIKeys.tuleapTrackerPageMsgRetrievingTrackers, project.getLabel()));
						ITuleapRepositoryConnector connector = (ITuleapRepositoryConnector)repositoryConnector;
						try {
							connector.refreshProject(repository, project, monitor);
						} catch (CoreException e) {
							TuleapTasksUIPlugin.log(e, true);
						}
						trackers.addAll(project.getAllTrackers());
					}
				};
				try {
					PlatformUI.getWorkbench().getProgressService().run(true, false, runnable);
					// this.getContainer().run(true, false, runnable);
				} catch (InvocationTargetException e) {
					TuleapTasksUIPlugin.log(e, true);
				} catch (InterruptedException e) {
					// Nothing to do
				} finally {
					this.getWizard().getContainer().updateButtons();
				}
			}
		}
		return trackers;
	}

	/**
	 * Returns the tracker where the new task should be created.
	 *
	 * @return The tracker where the new task should be created.
	 */
	public TuleapTracker getTrackerSelected() {
		IStructuredSelection selection = (IStructuredSelection)this.trackerTree.getViewer().getSelection();
		if (selection.getFirstElement() instanceof TuleapTracker) {
			return (TuleapTracker)selection.getFirstElement();
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
	 * @see org.eclipse.jface.wizard.WizardPage#getImage()
	 */
	@Override
	public Image getImage() {
		return TuleapTasksUIPlugin.getDefault().getImage(ITuleapUIConstants.Icons.TULEAP_LOGO_WIZARD_75X66);
	}
}
