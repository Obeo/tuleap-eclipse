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
package org.tuleap.mylyn.task.internal.ui.wizards.query;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.commons.workbench.forms.SectionComposite;
import org.eclipse.mylyn.internal.tasks.core.TaskRepositoryLocation;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapConnector;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerReport;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.ui.TuleapTasksUIPlugin;
import org.tuleap.mylyn.task.internal.ui.util.ITuleapUIConstants;
import org.tuleap.mylyn.task.internal.ui.util.TuleapMylynTasksUIMessages;
import org.tuleap.mylyn.task.internal.ui.wizards.TuleapTrackerPage;

/**
 * The first page of the Tuleap query wizard.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapQueryPage extends AbstractRepositoryQueryPage2 {

	/**
	 * The tracker identifier.
	 */
	private int trackerId = -1;

	/**
	 * The identifier of the project in which the query will be performed.
	 */
	private int groupId = -1;

	/**
	 * The button to search the artifacts of a selected project using a report.
	 */
	private Button reportsButton;

	/**
	 * A table showing the list of all the reports available for the tracker.
	 */
	private TableViewer reportsTableViewer;

	/**
	 * The button to select the search with a custom query.
	 */
	private Button customQueryButton;

	/**
	 * the constructor.
	 * 
	 * @param taskRepository
	 *            The Mylyn tasks repository.
	 */
	public TuleapQueryPage(TaskRepository taskRepository) {
		super(TuleapMylynTasksUIMessages.getString("TuleapQueryPage.Name"), taskRepository, null); //$NON-NLS-1$
		this.setTitle(TuleapMylynTasksUIMessages.getString("TuleapQueryPage.Title")); //$NON-NLS-1$
		this.setDescription(TuleapMylynTasksUIMessages.getString("TuleapQueryPage.Description")); //$NON-NLS-1$
	}

	/**
	 * the constructor.
	 * 
	 * @param taskRepository
	 *            The Mylyn tasks repository.
	 * @param queryToEdit
	 *            The query that will be edited
	 */
	public TuleapQueryPage(TaskRepository taskRepository, IRepositoryQuery queryToEdit) {
		super(TuleapMylynTasksUIMessages.getString("TuleapQueryPage.Name"), taskRepository, queryToEdit); //$NON-NLS-1$
		this.setTitle(TuleapMylynTasksUIMessages.getString("TuleapQueryPage.Title")); //$NON-NLS-1$
		this.setDescription(TuleapMylynTasksUIMessages.getString("TuleapQueryPage.Description")); //$NON-NLS-1$

		String tracker = queryToEdit.getAttribute(ITuleapConstants.QUERY_TRACKER_ID);
		this.trackerId = Integer.valueOf(tracker).intValue();
		String group = queryToEdit.getAttribute(ITuleapConstants.QUERY_GROUP_ID);
		this.groupId = Integer.valueOf(group).intValue();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2#createPageContent(org.eclipse.mylyn.commons.workbench.forms.SectionComposite)
	 */
	@Override
	protected void createPageContent(SectionComposite parent) {
		Composite composite = new Composite(parent.getContent(), SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessVerticalSpace = false;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(1, false));

		this.reportsButton = new Button(composite, SWT.RADIO);
		this.reportsButton.setText(TuleapMylynTasksUIMessages.getString("TuleapQueryPage.ReportButtonLabel")); //$NON-NLS-1$
		this.reportsButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				TuleapQueryPage.this.reportsTableViewer.getTable().setEnabled(true);
				TuleapQueryPage.this.getWizard().getContainer().updateButtons();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				TuleapQueryPage.this.reportsTableViewer.getTable().setEnabled(true);
				TuleapQueryPage.this.getWizard().getContainer().updateButtons();
			}
		});

		this.reportsTableViewer = new TableViewer(composite, SWT.BORDER);
		this.reportsTableViewer.getTable().setLayoutData(gridData);
		this.reportsTableViewer.setContentProvider(new ArrayContentProvider());
		this.reportsTableViewer.setLabelProvider(new LabelProvider() {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				if (element instanceof TuleapTrackerReport) {
					return ((TuleapTrackerReport)element).getName();
				}
				return super.getText(element);
			}
		});
		this.reportsTableViewer.getTable().addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				TuleapQueryPage.this.getWizard().getContainer().updateButtons();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				TuleapQueryPage.this.getWizard().getContainer().updateButtons();
			}
		});

		if (this.getQuery() == null) {
			this.customQueryButton = new Button(composite, SWT.RADIO);
			this.customQueryButton.setText(TuleapMylynTasksUIMessages
					.getString("TuleapQueryPage.CustomQueryButtonLabel")); //$NON-NLS-1$
			this.customQueryButton.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					TuleapQueryPage.this.reportsTableViewer.getTable().setEnabled(false);
					TuleapQueryPage.this.getWizard().getContainer().updateButtons();
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					TuleapQueryPage.this.reportsTableViewer.getTable().setEnabled(false);
					TuleapQueryPage.this.getWizard().getContainer().updateButtons();
				}
			});
		}

		this.updateReportList(false, false);

		Dialog.applyDialogFont(composite);
		this.reportsButton.setSelection(true);
		this.setPageComplete(true);
		this.setControl(composite);
	}

	/**
	 * Updates the list of available reports for the tracker in which we will create the query.
	 * 
	 * @param forceRefresh
	 *            <code>true</code> if we should force the refresh of the report, <code>false</code> otherwise
	 * @param inWizard
	 *            <code>true</code> if we should display the progress in the wizard container,
	 *            <code>false</code> otherwise
	 */
	private void updateReportList(boolean forceRefresh, boolean inWizard) {
		IWizardPage previousPage = this.getPreviousPage();
		if (previousPage instanceof TuleapTrackerPage) {
			TuleapTrackerPage tuleapTrackerPage = (TuleapTrackerPage)previousPage;
			final TuleapTrackerConfiguration tuleapTrackerConfiguration = tuleapTrackerPage
					.getTrackerSelected();
			if (tuleapTrackerConfiguration != null) {
				this.trackerId = tuleapTrackerConfiguration.getIdentifier();
				this.groupId = tuleapTrackerConfiguration.getTuleapProjectConfiguration().getIdentifier();
			}
		}

		final List<TuleapTrackerReport> reports = new ArrayList<TuleapTrackerReport>();

		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				AbstractWebLocation location = new TaskRepositoryLocation(TuleapQueryPage.this
						.getTaskRepository());
				TuleapSoapConnector tuleapSoapConnector = new TuleapSoapConnector(location);
				if (TuleapQueryPage.this.trackerId != -1) {
					reports.addAll(tuleapSoapConnector.getReports(TuleapQueryPage.this.trackerId, monitor));
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

		this.reportsTableViewer.setInput(reports);

		// Reselect the previously selected report
		if (this.getQuery() != null) {
			String reportId = this.getQuery().getAttribute(ITuleapConstants.QUERY_REPORT_ID);
			int report = Integer.valueOf(reportId).intValue();
			for (TuleapTrackerReport tuleapTrackerReport : reports) {
				if (tuleapTrackerReport.getId() == report) {
					IStructuredSelection selection = new StructuredSelection(tuleapTrackerReport);
					this.reportsTableViewer.setSelection(selection);
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	@Override
	public boolean isPageComplete() {
		return this.reportsButton.getSelection() && !this.reportsTableViewer.getSelection().isEmpty()
				&& this.getQueryTitle() != null && this.getQueryTitle().length() > 0;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	@Override
	public boolean canFlipToNextPage() {
		return this.customQueryButton.getSelection() && this.getQueryTitle() != null
				&& this.getQueryTitle().length() > 0;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#getNextPage()
	 */
	@Override
	public IWizardPage getNextPage() {
		TuleapCustomQueryPage tuleapCustomQueryPage = new TuleapCustomQueryPage(this.getTaskRepository(),
				this.getQuery());
		tuleapCustomQueryPage.setWizard(this.getWizard());
		return tuleapCustomQueryPage;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		this.updateReportList(false, true);
		super.setVisible(visible);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2#doRefreshControls()
	 */
	@Override
	protected void doRefreshControls() {
		// do nothing
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
		IWizardPage previousPage = this.getPreviousPage();
		if (this.reportsButton.getSelection() && previousPage instanceof TuleapTrackerPage) {
			TuleapTrackerPage tuleapTrackerPage = (TuleapTrackerPage)previousPage;
			TuleapTrackerConfiguration tuleapTrackerConfiguration = tuleapTrackerPage.getTrackerSelected();
			this.trackerId = tuleapTrackerConfiguration.getIdentifier();
			this.groupId = tuleapTrackerConfiguration.getTuleapProjectConfiguration().getIdentifier();
		}

		query.setSummary(this.getQueryTitle());
		query.setAttribute(ITuleapConstants.QUERY_TRACKER_ID, String.valueOf(this.trackerId));
		query.setAttribute(ITuleapConstants.QUERY_GROUP_ID, String.valueOf(this.groupId));

		query.setAttribute(ITuleapConstants.QUERY_KIND, ITuleapConstants.QUERY_KIND_REPORT);

		// Report id?
		ISelection selection = this.reportsTableViewer.getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection iStructuredSelection = (IStructuredSelection)selection;
			Object element = iStructuredSelection.getFirstElement();
			if (element instanceof TuleapTrackerReport) {
				TuleapTrackerReport tuleapTrackerReport = (TuleapTrackerReport)element;

				query.setAttribute(ITuleapConstants.QUERY_REPORT_ID, Integer.valueOf(
						tuleapTrackerReport.getId()).toString());
			}
		}
	}
}
