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
package org.eclipse.mylyn.tuleap.ui.internal.wizards.query;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.mylyn.commons.workbench.forms.SectionComposite;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2;
import org.eclipse.mylyn.tuleap.core.internal.client.ITuleapQueryConstants;
import org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapServer;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker;
import org.eclipse.mylyn.tuleap.core.internal.model.data.IQueryCriterion;
import org.eclipse.mylyn.tuleap.core.internal.parser.TuleapGsonProvider;
import org.eclipse.mylyn.tuleap.core.internal.repository.TuleapRepositoryConnector;
import org.eclipse.mylyn.tuleap.ui.internal.TuleapTasksUIPlugin;
import org.eclipse.mylyn.tuleap.ui.internal.util.ITuleapUIConstants;
import org.eclipse.mylyn.tuleap.ui.internal.util.TuleapUIKeys;
import org.eclipse.mylyn.tuleap.ui.internal.util.TuleapUIMessages;
import org.eclipse.mylyn.tuleap.ui.internal.wizards.TuleapTrackerPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * The second page of the Tuleap query wizard with the form based search page.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @since 0.7
 */
public class TuleapCustomQueryPage extends AbstractRepositoryQueryPage2 {

	/**
	 * The tracker identifier.
	 */
	private int trackerId = -1;

	/**
	 * The identifier of the project in which the query will be performed.
	 */
	private int projectId = -1;

	/**
	 * The query form graphical elements.
	 */
	private List<AbstractTuleapCustomQueryElement<?>> elements;

	/**
	 * Title of the query.
	 */
	private String queryTitle;

	/**
	 * Query attributes.
	 */
	private Map<String, String> queryAttributes = new HashMap<String, String>();

	/**
	 * Tracker configuration.
	 */
	private TuleapTracker tuleapTracker;

	/**
	 * The constructor.
	 *
	 * @param taskRepository
	 *            The task repository
	 * @param tuleapTracker
	 *            The Tuleap tracker
	 */
	public TuleapCustomQueryPage(TaskRepository taskRepository, TuleapTracker tuleapTracker) {
		super(TuleapUIMessages.getString(TuleapUIKeys.tuleapCustomQueryPageName), taskRepository, null);
		this.setTitle(TuleapUIMessages.getString(TuleapUIKeys.tuleapCustomQueryPageTitle));
		this.setDescription(TuleapUIMessages.getString(TuleapUIKeys.tuleapCustomQueryPageDescription));
		this.trackerId = tuleapTracker.getIdentifier();
		this.projectId = tuleapTracker.getProject().getIdentifier();
	}

	/**
	 * The constructor.
	 *
	 * @param taskRepository
	 *            The Mylyn task repository
	 * @param queryToEdit
	 *            The query to edit
	 */
	public TuleapCustomQueryPage(TaskRepository taskRepository, IRepositoryQuery queryToEdit) {
		super(TuleapUIMessages.getString(TuleapUIKeys.tuleapCustomQueryPageName), taskRepository, queryToEdit);
		this.setTitle(TuleapUIMessages.getString(TuleapUIKeys.tuleapCustomQueryPageTitle));
		this.setDescription(TuleapUIMessages.getString(TuleapUIKeys.tuleapCustomQueryPageDescription));

		// Case existing query to edit, the tracker id is provided by the query attributes
		if (queryToEdit != null) {
			String queryTrackerId = this.getQuery().getAttribute(ITuleapQueryConstants.QUERY_TRACKER_ID);
			this.trackerId = Integer.valueOf(queryTrackerId).intValue();
			String queryProjectId = this.getQuery().getAttribute(ITuleapQueryConstants.QUERY_PROJECT_ID);
			this.projectId = Integer.valueOf(queryProjectId).intValue();
			this.setQueryTitle(queryToEdit.getSummary());
			this.queryAttributes.putAll(this.getQuery().getAttributes());
		}
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
		ScrolledComposite sc = new ScrolledComposite(parent, SWT.V_SCROLL);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(sc);
		Composite composite = new Composite(sc, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(composite);
		sc.setContent(composite);
		composite.setLayout(new GridLayout(1, false));

		String connectorKind = this.getTaskRepository().getConnectorKind();
		AbstractRepositoryConnector connector = TasksUi.getRepositoryManager().getRepositoryConnector(
				connectorKind);
		if (!(connector instanceof TuleapRepositoryConnector)) {
			TuleapTasksUIPlugin
			.log(TuleapUIMessages.getString(TuleapUIKeys.invalidRepositoryConnector), true);
			return;
		}

		IWizardPage previousPage = this.getPreviousPage();
		if (previousPage instanceof TuleapReportPage) {
			previousPage = ((TuleapReportPage)previousPage).getPreviousPage();
			if (previousPage instanceof TuleapTrackerPage) {
				this.trackerId = ((TuleapTrackerPage)previousPage).getTrackerSelected().getIdentifier();
				this.projectId = ((TuleapTrackerPage)previousPage).getTrackerSelected().getProject()
						.getIdentifier();
			}
		}

		if (this.trackerId != -1 && this.projectId != -1) {
			final TuleapRepositoryConnector repositoryConnector = (TuleapRepositoryConnector)connector;
			TuleapServer repositoryConfiguration = repositoryConnector.getServer(this.getTaskRepository()
					.getRepositoryUrl());
			tuleapTracker = repositoryConfiguration.getTracker(this.trackerId);

			Group group = new Group(composite, SWT.NONE);
			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(group);
			group.setLayout(new GridLayout(3, false));

			Collection<AbstractTuleapField> fields = tuleapTracker.getFields();
			QueryFieldVisitor visitor = new QueryFieldVisitor(group, queryAttributes, TuleapGsonProvider
					.defaultGson(), this);
			for (AbstractTuleapField field : fields) {
				field.accept(visitor);
			}
			this.elements = visitor.getQueryElements();
		}

		Dialog.applyDialogFont(composite);
		sc.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		this.setControl(sc);
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
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2#doRefreshControls()
	 */
	@Override
	protected void doRefreshControls() {
		// nothing yet
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
		if (this.trackerId != -1 && this.projectId != -1) {
			query.setSummary(this.getQueryTitle());
			query.setAttribute(ITuleapQueryConstants.QUERY_KIND, ITuleapQueryConstants.QUERY_KIND_CUSTOM);
			query.setAttribute(ITuleapQueryConstants.QUERY_TRACKER_ID, Integer.valueOf(this.trackerId)
					.toString());
			query.setAttribute(ITuleapQueryConstants.QUERY_PROJECT_ID, Integer.valueOf(this.projectId)
					.toString());
			Map<String, IQueryCriterion<?>> criteria = new HashMap<String, IQueryCriterion<?>>();
			for (AbstractTuleapCustomQueryElement<?> element : elements) {
				try {
					IQueryCriterion<?> criterion = element.getCriterion();
					if (criterion != null) {
						criteria.put(element.getField().getName(), criterion);
					}
				} catch (CoreException e) {
					TuleapTasksUIPlugin.log(e, false);
				}
			}
			query.setAttribute(ITuleapQueryConstants.QUERY_CUSTOM_CRITERIA, TuleapGsonProvider.defaultGson()
					.toJson(criteria));
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2#isPageComplete()
	 */
	@Override
	public boolean isPageComplete() {
		boolean result = this.getQueryTitle() != null && this.queryTitle.length() > 0;
		for (int i = 0; i < elements.size() && result; i++) {
			AbstractTuleapCustomQueryElement<?> element = elements.get(i);
			result = element.validate();
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		this.getControl().setVisible(visible);
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
}
