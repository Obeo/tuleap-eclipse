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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.mylyn.commons.workbench.forms.SectionComposite;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapInstanceConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapTrackerConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.repository.ITuleapRepositoryConnector;
import org.eclipse.mylyn.internal.tuleap.core.util.ITuleapConstants;
import org.eclipse.mylyn.internal.tuleap.ui.TuleapTasksUIPlugin;
import org.eclipse.mylyn.internal.tuleap.ui.util.ITuleapUIConstants;
import org.eclipse.mylyn.internal.tuleap.ui.util.TuleapMylynTasksUIMessages;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.TasksUi;
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
	 * The task repository.
	 */
	private TaskRepository repository;

	/**
	 * This combo will display the trackers available.
	 */
	private Combo projectSelectionCombo;

	/**
	 * The constructor.
	 * 
	 * @param taskRepository
	 *            The task repository
	 */
	public TuleapDefaultQueriesPage(TaskRepository taskRepository) {
		super(TuleapMylynTasksUIMessages.getString("TuleapDefaultQueriesPage.Name"), taskRepository, null); //$NON-NLS-1$
		this.repository = taskRepository;
		this.setTitle(TuleapMylynTasksUIMessages.getString("TuleapDefaultQueriesPage.Title")); //$NON-NLS-1$
		this.setDescription(TuleapMylynTasksUIMessages.getString("TuleapDefaultQueriesPage.Description")); //$NON-NLS-1$
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
				.getString("TuleapDefaultQueriesPage.DefaultQueriesGroup.Name")); //$NON-NLS-1$
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

		projectSelectionCombo = new Combo(groupComposite, SWT.SINGLE);
		List<String> trackers = getAllAvailableTrackers();
		projectSelectionCombo.setItems(trackers.toArray(new String[trackers.size()]));
		if (projectSelectionCombo.getItemCount() > 0) {
			projectSelectionCombo.setText(projectSelectionCombo.getItem(0));
		}
	}

	/**
	 * Get all available trackers.
	 * 
	 * @return List of trackers
	 */
	private List<String> getAllAvailableTrackers() {
		final List<String> trackersList = new ArrayList<String>();

		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				String connectorKind = repository.getConnectorKind();
				final AbstractRepositoryConnector repositoryConnector = TasksUi.getRepositoryManager()
						.getRepositoryConnector(connectorKind);
				if (repositoryConnector instanceof ITuleapRepositoryConnector) {
					ITuleapRepositoryConnector connector = (ITuleapRepositoryConnector)repositoryConnector;
					final TuleapInstanceConfiguration instanceConfiguration = connector
							.getRepositoryConfiguration(repository, true, monitor);

					List<TuleapTrackerConfiguration> trackerConfigurations = instanceConfiguration
							.getAllTrackerConfigurations();
					for (TuleapTrackerConfiguration tuleapTrackerConfiguration : trackerConfigurations) {
						trackersList.add(tuleapTrackerConfiguration.getQualifiedName());
					}
				}
			}
		};
		try {
			this.getContainer().run(false, false, runnable);
		} catch (InvocationTargetException e) {
			TuleapTasksUIPlugin.log(e, true);
		} catch (InterruptedException e) {
			TuleapTasksUIPlugin.log(e, true);
		}
		return trackersList;
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
		return true;
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
		String connectorKind = repository.getConnectorKind();
		final AbstractRepositoryConnector repositoryConnector = TasksUi.getRepositoryManager()
				.getRepositoryConnector(connectorKind);
		if (repositoryConnector instanceof ITuleapRepositoryConnector) {

			ITuleapRepositoryConnector connector = (ITuleapRepositoryConnector)repositoryConnector;
			final TuleapInstanceConfiguration instanceConfiguration = connector.getRepositoryConfiguration(
					repository, true, new NullProgressMonitor());

			List<TuleapTrackerConfiguration> trackerConfigurations = instanceConfiguration
					.getAllTrackerConfigurations();
			for (TuleapTrackerConfiguration tuleapTrackerConfiguration : trackerConfigurations) {
				if (tuleapTrackerConfiguration.getQualifiedName()
						.equals(this.projectSelectionCombo.getText())) {
					query.setSummary(this.getQueryTitle());
					query.setAttribute(ITuleapConstants.QUERY_KIND,
							ITuleapConstants.QUERY_KIND_ALL_FROM_TRACKER);
					query.setAttribute(ITuleapConstants.QUERY_TRACKER_ID, Integer.valueOf(
							tuleapTrackerConfiguration.getTrackerId()).toString());
					break;
				}
			}
		}
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
