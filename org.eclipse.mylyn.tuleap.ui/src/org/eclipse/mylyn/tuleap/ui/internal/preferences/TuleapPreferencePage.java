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
package org.eclipse.mylyn.tuleap.ui.internal.preferences;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.mylyn.tuleap.core.internal.util.ITuleapConstants;
import org.eclipse.mylyn.tuleap.ui.internal.TuleapTasksUIPlugin;
import org.eclipse.mylyn.tuleap.ui.internal.util.ITuleapUIConstants;
import org.eclipse.mylyn.tuleap.ui.internal.util.TuleapUIMessages;
import org.eclipse.mylyn.tuleap.ui.internal.util.TuleapUiMessagesKeys;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.osgi.service.prefs.BackingStoreException;

/**
 * The preference page of Tuleap.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	/**
	 * The debug mode button.
	 */
	private Button enableDebugModeButton;

	/**
	 * The constructor.
	 */
	public TuleapPreferencePage() {
		super(TuleapUIMessages.getString(TuleapUiMessagesKeys.tuleapPreferencesPageTitle),
				TuleapTasksUIPlugin.getImageDescriptor(ITuleapUIConstants.Icons.TULEAP_LOGO_16X16));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(data);

		enableDebugModeButton = new Button(composite, SWT.CHECK | SWT.WRAP);
		enableDebugModeButton
				.setText(TuleapUIMessages.getString(TuleapUiMessagesKeys.activateDebugModeLabel));
		GridData gd = getButtonGridData(enableDebugModeButton);
		enableDebugModeButton.setLayoutData(gd);
		enableDebugModeButton.setSelection(this.getSelection());
		enableDebugModeButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				setSelection();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				setSelection();
			}
		});

		Dialog.applyDialogFont(composite);
		return composite;
	}

	/**
	 * Sets the selection in the preference of Tuleap.
	 */
	private void setSelection() {
		IEclipsePreferences node = InstanceScope.INSTANCE.getNode(ITuleapConstants.TULEAP_PREFERENCE_NODE);
		if (node != null) {
			node.putBoolean(ITuleapConstants.TULEAP_PREFERENCE_DEBUG_MODE, enableDebugModeButton
					.getSelection());
			try {
				node.flush();
			} catch (BackingStoreException e) {
				TuleapTasksUIPlugin.log(e, true);
			}
		}
	}

	/**
	 * Indicates if the debug mode is activated or not.
	 * 
	 * @return <code>true</code> if the debug mode is activated, <code>false</code> otherwise.
	 */
	private boolean getSelection() {
		IEclipsePreferences node = InstanceScope.INSTANCE.getNode(ITuleapConstants.TULEAP_PREFERENCE_NODE);
		if (node != null) {
			return node.getBoolean(ITuleapConstants.TULEAP_PREFERENCE_DEBUG_MODE, false);
		}
		return false;
	}

	/**
	 * Returns the grid data for the given button.
	 * 
	 * @param button
	 *            The button
	 * @return The grid data for the given button
	 */
	private GridData getButtonGridData(Button button) {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		gd.widthHint = Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		return gd;
	}

}
