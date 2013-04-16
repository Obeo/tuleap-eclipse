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
package org.tuleap.mylyn.task.internal.ui.wizards.query;

import java.util.List;

import org.eclipse.mylyn.commons.workbench.forms.SectionComposite;
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
import org.tuleap.mylyn.task.internal.core.model.TuleapTrackerReport;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.ui.TuleapTasksUIPlugin;
import org.tuleap.mylyn.task.internal.ui.util.ITuleapUIConstants;
import org.tuleap.mylyn.task.internal.ui.util.TuleapMylynTasksUIMessages;

/**
 * The default queries page.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapDefaultQueriesPage extends AbstractRepositoryQueryPage2 {

	/**
	 * The tracker identifier.
	 */
	private int trackerId;

	/**
	 * The list of the reports available.
	 */
	private Combo reportSelectionCombo;

	/**
	 * The reports available on the tracker.
	 */
	private List<TuleapTrackerReport> trackerReports;

	/**
	 * The radio button used to select the "download all artifacts from the project" query.
	 */
	private Button allArtifactsButton;

	/**
	 * The radio button used to select the "run the selected report" query.
	 */
	private Button reportsButton;

	/**
	 * Title of the query.
	 */
	private String queryTitle;

	/**
	 * The report id associated to the query if the query is {@link ITuleapConstants#QUERY_KIND_REPORT report
	 * kind} query.
	 */
	private int queryReportId = -1;

	/**
	 * The kind of the query. It could be a {@link ITuleapConstants#QUERY_KIND_REPORT report query} or a query
	 * looking for {@link ITuleapConstants#QUERY_KIND_ALL_FROM_TRACKER all artifacts} from a specific tracker.
	 */
	private String queryKind;

	/**
	 * The constructor.
	 * 
	 * @param taskRepository
	 *            The task repository
	 * @param selectedTracker
	 *            The selected tracker
	 * @param reports
	 *            The reports available on the selected tracker.
	 * @param queryToEdit
	 *            The edited query
	 */
	public TuleapDefaultQueriesPage(TaskRepository taskRepository, int selectedTracker,
			List<TuleapTrackerReport> reports, IRepositoryQuery queryToEdit) {
		super(
				TuleapMylynTasksUIMessages.getString("TuleapDefaultQueriesPage.Name"), taskRepository, queryToEdit); //$NON-NLS-1$
		this.trackerId = selectedTracker;
		this.trackerReports = reports;
		this.setTitle(TuleapMylynTasksUIMessages.getString("TuleapDefaultQueriesPage.Title")); //$NON-NLS-1$
		this.setDescription(TuleapMylynTasksUIMessages.getString("TuleapDefaultQueriesPage.Description")); //$NON-NLS-1$
		if (queryToEdit != null) {
			this.queryTitle = queryToEdit.getSummary();
			String reportId = queryToEdit.getAttribute(ITuleapConstants.QUERY_REPORT_ID);
			if (reportId != null) {
				this.queryReportId = Integer.valueOf(reportId).intValue();
			}
			this.queryKind = queryToEdit.getAttribute(ITuleapConstants.QUERY_KIND);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2#createPageContent(org.eclipse.mylyn.commons.workbench.forms.SectionComposite)
	 */
	@Override
	protected void createPageContent(SectionComposite parent) {
		if (this.queryTitle != null) {
			setQueryTitle(this.queryTitle);
		}

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

		// Reports
		reportsButton = new Button(groupComposite, SWT.RADIO);
		reportsButton.setText(TuleapMylynTasksUIMessages.getString("TuleapDefaultQueriesPage.ReportsLabel")); //$NON-NLS-1$
		reportSelectionCombo = new Combo(groupComposite, SWT.SINGLE);
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		reportSelectionCombo.setLayoutData(gd);

		// In case of a new query the selected report will be the first one in the list
		int selectedReportIndex = 0;
		for (TuleapTrackerReport tuleapTrackerReport : this.trackerReports) {
			reportSelectionCombo.add(tuleapTrackerReport.toString());
			// In case of an edited query looks for the edited report in order to select it in the combo
			if (ITuleapConstants.QUERY_KIND_REPORT.equals(queryKind)
					&& tuleapTrackerReport.getId() == queryReportId) {
				selectedReportIndex = this.trackerReports.indexOf(tuleapTrackerReport);
			}
		}

		if (this.trackerReports.size() > 0) {
			// Select the report
			reportSelectionCombo.setText(this.trackerReports.get(selectedReportIndex).toString());
		}

		// Download all button
		allArtifactsButton = new Button(groupComposite, SWT.RADIO);
		allArtifactsButton.setText(TuleapMylynTasksUIMessages
				.getString("TuleapDefaultQueriesPage.ProjectsQueryButton.Name") + this.trackerId); //$NON-NLS-1$
		allArtifactsButton.setSelection(true);
		gd = new GridData();
		gd.horizontalSpan = 2;
		allArtifactsButton.setLayoutData(gd);

		// Select the report by default
		if (ITuleapConstants.QUERY_KIND_ALL_FROM_TRACKER.equals(queryKind)) {
			allArtifactsButton.setSelection(true);
			reportsButton.setSelection(false);
		} else {
			allArtifactsButton.setSelection(false);
			reportsButton.setSelection(true);
		}
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
		query.setSummary(this.getQueryTitle());
		query.setAttribute(ITuleapConstants.QUERY_TRACKER_ID, String.valueOf(this.trackerId));

		if (this.allArtifactsButton.getSelection()) {
			query.setAttribute(ITuleapConstants.QUERY_KIND, ITuleapConstants.QUERY_KIND_ALL_FROM_TRACKER);
		} else if (this.reportsButton.getSelection()) {
			query.setAttribute(ITuleapConstants.QUERY_KIND, ITuleapConstants.QUERY_KIND_REPORT);

			// Report id?
			String reportSelected = this.reportSelectionCombo.getText();
			for (TuleapTrackerReport tuleapTrackerReport : this.trackerReports) {
				if (reportSelected.equals(tuleapTrackerReport.toString())) {
					query.setAttribute(ITuleapConstants.QUERY_REPORT_ID, Integer.valueOf(
							tuleapTrackerReport.getId()).toString());
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
