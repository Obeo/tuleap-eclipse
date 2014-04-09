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
package org.eclipse.mylyn.tuleap.ui.internal.wizards;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker;
import org.eclipse.mylyn.tuleap.ui.internal.TuleapTasksUIPlugin;
import org.eclipse.mylyn.tuleap.ui.internal.util.ITuleapUIConstants;
import org.eclipse.mylyn.tuleap.ui.internal.util.TuleapUIKeys;
import org.eclipse.mylyn.tuleap.ui.internal.util.TuleapUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

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
	private List<TuleapTracker> trackers;

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
	 * @param trackers
	 *            The trackers to display
	 */
	public TuleapTrackerPage(List<TuleapTracker> trackers) {
		super(TuleapUIMessages.getString(TuleapUIKeys.tuleapTrackerPageName));
		this.setTitle(TuleapUIMessages.getString(TuleapUIKeys.tuleapTrackerPageTitle));
		this.setDescription(TuleapUIMessages.getString(TuleapUIKeys.tuleapTrackerPageDescription));
		this.trackers = trackers;
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
