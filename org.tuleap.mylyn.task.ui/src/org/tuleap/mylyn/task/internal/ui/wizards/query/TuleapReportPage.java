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
import org.eclipse.mylyn.commons.workbench.forms.SectionComposite;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.tuleap.mylyn.task.internal.core.client.ITuleapQueryConstants;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapClient;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapTracker;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapTrackerReport;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.ui.TuleapTasksUIPlugin;
import org.tuleap.mylyn.task.internal.ui.util.ITuleapUIConstants;
import org.tuleap.mylyn.task.internal.ui.util.TuleapUIMessages;
import org.tuleap.mylyn.task.internal.ui.util.TuleapUiMessagesKeys;

/**
 * The first page of the Tuleap query wizard.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapReportPage extends AbstractRepositoryQueryPage2 {
	/**
	 * The hinted height of the viewer containing the list of the reports.
	 */
	private static final int HEIGHT_HINT = 250;

	/**
	 * The tracker identifier.
	 */
	private int trackerId = -1;

	/**
	 * The identifier of the project in which the query will be performed.
	 */
	private int projectId = -1;

	/**
	 * A table showing the list of all the reports available for the tracker.
	 */
	private TableViewer reportsTableViewer;

	/**
	 * The title of the query.
	 */
	private String queryTitle;

	/**
	 * the constructor.
	 * 
	 * @param taskRepository
	 *            The Mylyn tasks repository.
	 * @param tuleapTracker
	 *            The Tuleap tracker
	 */
	public TuleapReportPage(TaskRepository taskRepository, TuleapTracker tuleapTracker) {
		super(TuleapUIMessages.getString(TuleapUiMessagesKeys.tuleapReportPageName), taskRepository, null);
		this.setTitle(TuleapUIMessages.getString(TuleapUiMessagesKeys.tuleapReportPageTitle));
		this.setDescription(TuleapUIMessages.getString(TuleapUiMessagesKeys.tuleapReportPageDescription));
		this.trackerId = tuleapTracker.getIdentifier();
		this.projectId = tuleapTracker.getProject().getIdentifier();
	}

	/**
	 * the constructor.
	 * 
	 * @param taskRepository
	 *            The Mylyn tasks repository.
	 * @param queryToEdit
	 *            The query that will be edited
	 */
	public TuleapReportPage(TaskRepository taskRepository, IRepositoryQuery queryToEdit) {
		super(TuleapUIMessages.getString(TuleapUiMessagesKeys.tuleapReportPageName), taskRepository,
				queryToEdit);
		this.setTitle(TuleapUIMessages.getString(TuleapUiMessagesKeys.tuleapReportPageTitle));
		this.setDescription(TuleapUIMessages.getString(TuleapUiMessagesKeys.tuleapReportPageDescription));

		String tracker = queryToEdit.getAttribute(ITuleapQueryConstants.QUERY_TRACKER_ID);
		this.trackerId = Integer.valueOf(tracker).intValue();
		String project = queryToEdit.getAttribute(ITuleapQueryConstants.QUERY_PROJECT_ID);
		this.projectId = Integer.valueOf(project).intValue();

		this.setQueryTitle(queryToEdit.getSummary());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2#createPageContent(org.eclipse.mylyn.commons.workbench.forms.SectionComposite)
	 */
	@Override
	protected void createPageContent(SectionComposite parent) {
		// [SBE] Useless since we are overriding createControl in order to remove the part used to refresh the
		// configuration of the repository and the title of the query since it has already been entered in a
		// previous page
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = HEIGHT_HINT;
		gridData.grabExcessVerticalSpace = false;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(1, false));

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
				TuleapReportPage.this.getWizard().getContainer().updateButtons();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				TuleapReportPage.this.getWizard().getContainer().updateButtons();
			}
		});

		Dialog.applyDialogFont(composite);

		this.setControl(composite);
	}

	/**
	 * Updates the list of available reports for the tracker in which we will create the query.
	 * 
	 * @param inWizard
	 *            <code>true</code> if we should display the progress in the wizard container,
	 *            <code>false</code> otherwise
	 */
	private void updateReportList(boolean inWizard) {
		final List<TuleapTrackerReport> reports = new ArrayList<TuleapTrackerReport>();

		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				TaskRepository taskRepository = TuleapReportPage.this.getTaskRepository();
				String connectorKind = taskRepository.getConnectorKind();
				AbstractRepositoryConnector repositoryConnector = TasksUi.getRepositoryManager()
						.getRepositoryConnector(connectorKind);
				if (repositoryConnector instanceof ITuleapRepositoryConnector) {
					ITuleapRepositoryConnector connector = (ITuleapRepositoryConnector)repositoryConnector;
					TuleapSoapClient soapClient = connector.getClientManager().getSoapClient(taskRepository);
					reports.addAll(soapClient.getReports(trackerId, monitor));
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
			String reportId = this.getQuery().getAttribute(ITuleapQueryConstants.QUERY_REPORT_ID);
			int report = Integer.valueOf(reportId).intValue();
			for (TuleapTrackerReport tuleapTrackerReport : reports) {
				if (tuleapTrackerReport.getId() == report) {
					IStructuredSelection selection = new StructuredSelection(tuleapTrackerReport);
					this.reportsTableViewer.setSelection(selection);
				}
			}
		} else if (reports.size() > 0) {
			IStructuredSelection selection = new StructuredSelection(reports.get(0));
			this.reportsTableViewer.setSelection(selection);
		}
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2#setQueryTitle(java.lang.String)
	 */
	@Override
	public void setQueryTitle(String text) {
		this.queryTitle = text;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2#getQueryTitle()
	 */
	@Override
	public String getQueryTitle() {
		return this.queryTitle;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	@Override
	public boolean isPageComplete() {
		return !this.reportsTableViewer.getSelection().isEmpty() && this.getQueryTitle() != null
				&& this.getQueryTitle().length() > 0;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		this.getControl().setVisible(visible);
		if (visible) {
			this.updateReportList(true);
		}
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
		// true = I have the repository configuration, Mylyn does not have to retrieve it
		// false = Mylyn will call the connector to reload the configuration
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
		query.setAttribute(ITuleapQueryConstants.QUERY_TRACKER_ID, String.valueOf(this.trackerId));
		query.setAttribute(ITuleapQueryConstants.QUERY_PROJECT_ID, String.valueOf(this.projectId));

		query.setAttribute(ITuleapQueryConstants.QUERY_KIND, ITuleapQueryConstants.QUERY_KIND_REPORT);

		// Report id?
		ISelection selection = this.reportsTableViewer.getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection iStructuredSelection = (IStructuredSelection)selection;
			Object element = iStructuredSelection.getFirstElement();
			if (element instanceof TuleapTrackerReport) {
				TuleapTrackerReport tuleapTrackerReport = (TuleapTrackerReport)element;

				query.setAttribute(ITuleapQueryConstants.QUERY_REPORT_ID, Integer.valueOf(
						tuleapTrackerReport.getId()).toString());
			}
		}
	}
}
