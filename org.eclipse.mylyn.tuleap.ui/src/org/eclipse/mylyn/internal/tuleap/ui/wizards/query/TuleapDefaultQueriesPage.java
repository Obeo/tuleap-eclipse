/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.internal.tuleap.ui.wizards.query;

import org.eclipse.mylyn.commons.workbench.forms.SectionComposite;
import org.eclipse.mylyn.internal.tuleap.ui.TuleapTasksUIPlugin;
import org.eclipse.mylyn.internal.tuleap.ui.util.ITuleapUIConstants;
import org.eclipse.mylyn.internal.tuleap.ui.util.TuleapMylynTasksUIMessages;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * The default queries page.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapDefaultQueriesPage extends AbstractRepositoryQueryPage2 {

	/**
	 * The constructor.
	 * 
	 * @param taskRepository
	 *            The task repository
	 */
	public TuleapDefaultQueriesPage(TaskRepository taskRepository) {
		super(TuleapMylynTasksUIMessages.getString("TuleapDefaultQueriesPage.Name"), taskRepository, null);
		this.setTitle(TuleapMylynTasksUIMessages.getString("TuleapDefaultQueriesPage.Title"));
		this.setDescription(TuleapMylynTasksUIMessages.getString("TuleapDefaultQueriesPage.Description"));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2#createPageContent(org.eclipse.mylyn.commons.workbench.forms.SectionComposite)
	 */
	@Override
	protected void createPageContent(SectionComposite parent) {
		Group defaultQueriesGroup = new Group(parent.getContent(), SWT.NONE);
		defaultQueriesGroup.setText(TuleapMylynTasksUIMessages
				.getString("TuleapDefaultQueriesPage.DefaultQueriesGroup.Name"));
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessVerticalSpace = false;
		defaultQueriesGroup.setLayoutData(gridData);
		defaultQueriesGroup.setLayout(new GridLayout(1, false));

		Composite groupComposite = new Composite(defaultQueriesGroup, SWT.NONE);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessVerticalSpace = false;
		groupComposite.setLayoutData(gridData);
		groupComposite.setLayout(new GridLayout(2, false));

		Button projectsQueryButton = new Button(groupComposite, SWT.RADIO);
		projectsQueryButton.setText(TuleapMylynTasksUIMessages
				.getString("TuleapDefaultQueriesPage.ProjectsQueryButton.Name")); //$NON-NLS-1$
		projectsQueryButton.setSelection(true);

		Combo projectSelectionCombo = new Combo(groupComposite, SWT.SINGLE);
		projectSelectionCombo.setItems(new String[] {"All available projects", "Acceleo", "ATL", "EEF",
				"EMF Compare", "Intent", });
		projectSelectionCombo.setText("All available projects");

		Button allQueryButton = new Button(groupComposite, SWT.RADIO);
		allQueryButton.setText(TuleapMylynTasksUIMessages
				.getString("TuleapDefaultQueriesPage.AllQueryButton.Name")); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2#doRefreshControls()
	 */
	@Override
	protected void doRefreshControls() {
		// TODO refresh the configuration of the repository and the controls that go along
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2#hasRepositoryConfiguration()
	 */
	@Override
	protected boolean hasRepositoryConfiguration() {
		return false;
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
		// TODO set the details of the query
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#getImage()
	 */
	@Override
	public Image getImage() {
		return TuleapTasksUIPlugin.getDefault().getImage(ITuleapUIConstants.Icons.WIZARD_TULEAP_LOGO_48);
	}
}
