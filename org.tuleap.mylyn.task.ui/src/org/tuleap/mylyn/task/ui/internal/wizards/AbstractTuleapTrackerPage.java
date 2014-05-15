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

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapTracker;
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
public abstract class AbstractTuleapTrackerPage extends WizardPage {

	/**
	 * The hinted height of the viewer containing the list of the tracker.
	 */
	private static final int HEIGHT_HINT = 250;

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
	 */
	public AbstractTuleapTrackerPage() {
		super(TuleapUIMessages.getString(TuleapUIKeys.tuleapTrackerPageName));
		this.setTitle(TuleapUIMessages.getString(TuleapUIKeys.tuleapTrackerPageTitle));
		this.setDescription(TuleapUIMessages.getString(TuleapUIKeys.tuleapTrackerPageDescription));
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
				TuleapTracker trackerSelected = AbstractTuleapTrackerPage.this.getTrackerSelected();
				if (trackerSelected == null) {
					AbstractTuleapTrackerPage.this.setErrorMessage(TuleapUIMessages
							.getString(TuleapUIKeys.tuleapTrackerPageSelectATracker));
				} else {
					AbstractTuleapTrackerPage.this.setErrorMessage(null);
					AbstractTuleapTrackerPage.this.setMessage(null);
					String description = trackerSelected.getDescription();
					AbstractTuleapTrackerPage.this.trackerDescriptionLabel.setText(TuleapUIMessages
							.getString(TuleapUIKeys.tuleapTrackerPageDescriptionLabel, description));
				}
				IWizard wizard = AbstractTuleapTrackerPage.this.getWizard();
				wizard.getContainer().updateButtons();
			}
		});

		Dialog.applyDialogFont(composite);

		setControl(composite);
	}

	/**
	 * Retrieve the page tracker.
	 *
	 * @return the list of tracker to display on this page.
	 */
	protected abstract List<TuleapTracker> getTrackers();

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
