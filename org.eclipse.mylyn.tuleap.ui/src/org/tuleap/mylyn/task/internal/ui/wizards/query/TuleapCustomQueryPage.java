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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
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
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapArtifactLink;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapComputedValue;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapDate;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapFloat;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapInteger;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapMultiSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapOpenList;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBoxItem;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapString;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapText;
import org.eclipse.mylyn.tuleap.core.internal.repository.TuleapRepositoryConnector;
import org.eclipse.mylyn.tuleap.core.internal.util.ITuleapConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tuleap.mylyn.task.internal.ui.TuleapTasksUIPlugin;
import org.tuleap.mylyn.task.internal.ui.util.ITuleapUIConstants;
import org.tuleap.mylyn.task.internal.ui.util.TuleapUIMessages;
import org.tuleap.mylyn.task.internal.ui.util.TuleapUiMessagesKeys;
import org.tuleap.mylyn.task.internal.ui.wizards.TuleapTrackerPage;

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
	private List<TuleapCustomQueryElement> elements = new ArrayList<TuleapCustomQueryElement>();

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
		super(TuleapUIMessages.getString(TuleapUiMessagesKeys.tuleapCustomQueryPageName), taskRepository,
				null);
		this.setTitle(TuleapUIMessages.getString(TuleapUiMessagesKeys.tuleapCustomQueryPageTitle));
		this.setDescription(TuleapUIMessages.getString(TuleapUiMessagesKeys.tuleapCustomQueryPageDescription));
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
		super(TuleapUIMessages.getString(TuleapUiMessagesKeys.tuleapCustomQueryPageName), taskRepository,
				queryToEdit);
		this.setTitle(TuleapUIMessages.getString(TuleapUiMessagesKeys.tuleapCustomQueryPageTitle));
		this.setDescription(TuleapUIMessages.getString(TuleapUiMessagesKeys.tuleapCustomQueryPageDescription));

		// Case existing query to edit, the tracker id is provided by the query attributes
		if (queryToEdit != null) {
			String queryTrackerId = this.getQuery()
					.getAttribute(ITuleapQueryConstants.QUERY_TRACKER_ID);
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
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessVerticalSpace = false;
		composite.setLayoutData(gridData);
		composite.setLayout(new GridLayout(1, false));

		String connectorKind = this.getTaskRepository().getConnectorKind();
		AbstractRepositoryConnector connector = TasksUi.getRepositoryManager().getRepositoryConnector(
				connectorKind);
		if (!(connector instanceof TuleapRepositoryConnector)) {
			TuleapTasksUIPlugin.log(TuleapUIMessages
					.getString(TuleapUiMessagesKeys.invalidRepositoryConnector), true);
			return;
		}

		IWizardPage previousPage = this.getPreviousPage();
		if (previousPage instanceof TuleapReportPage) {
			previousPage = ((TuleapReportPage)previousPage).getPreviousPage();
			if (previousPage instanceof TuleapTrackerPage) {
				this.trackerId = ((TuleapTrackerPage)previousPage).getTrackerSelected().getIdentifier();
				this.projectId = ((TuleapTrackerPage)previousPage).getTrackerSelected()
						.getProject().getIdentifier();
			}
		}

		if (this.trackerId != -1 && this.projectId != -1) {
			final TuleapRepositoryConnector repositoryConnector = (TuleapRepositoryConnector)connector;
			TuleapServer repositoryConfiguration = repositoryConnector.getServer(this
					.getTaskRepository().getRepositoryUrl());
			tuleapTracker = repositoryConfiguration.getTracker(this.trackerId);

			Group group = new Group(composite, SWT.NONE);
			gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.grabExcessVerticalSpace = false;
			group.setLayoutData(gridData);
			group.setLayout(new GridLayout(3, false));

			Collection<AbstractTuleapField> fields = tuleapTracker.getFields();
			for (AbstractTuleapField field : fields) {
				if (field instanceof TuleapString) {
					this.createGroupContent((TuleapString)field, group);
				} else if (field instanceof TuleapSelectBox) {
					this.createGroupContent((TuleapSelectBox)field, group);
				} else if (field instanceof TuleapMultiSelectBox) {
					this.createGroupContent((TuleapMultiSelectBox)field, group);
				} else if (field instanceof TuleapText) {
					this.createGroupContent((TuleapText)field, group);
				} else if (field instanceof TuleapDate) {
					this.createGroupContent((TuleapDate)field, group);
				} else if (field instanceof TuleapInteger) {
					this.createGroupContent((TuleapInteger)field, group);
				} else if (field instanceof TuleapFloat) {
					this.createGroupContent((TuleapFloat)field, group);
				} else if (field instanceof TuleapComputedValue) {
					this.createGroupContent((TuleapComputedValue)field, group);
				} else if (field instanceof TuleapOpenList) {
					this.createGroupContent((TuleapOpenList)field, group);
				} else if (field instanceof TuleapArtifactLink) {
					this.createGroupContent((TuleapArtifactLink)field, group);
				}
			}
		}

		Dialog.applyDialogFont(composite);

		this.setControl(composite);
	}

	/**
	 * Creates the dedicated type of widget for the Tuleap string field.
	 * 
	 * @param tuleapString
	 *            The Tuleap string field
	 * @param group
	 *            The group in which the widget should be created
	 */
	private void createGroupContent(TuleapString tuleapString, Group group) {
		// Create a single line text field for each Tuleap string
		createGroupContentForString(tuleapString.getLabel(), group, tuleapString);
	}

	/**
	 * Creates the dedicated type of widget for the Tuleap integer field.
	 * 
	 * @param tuleapInteger
	 *            The Tuleap integer field
	 * @param group
	 *            The group in which the widget should be created
	 */
	private void createGroupContent(TuleapInteger tuleapInteger, Group group) {
		// Create a single line text field for each Tuleap integer
		createGroupContentForString(tuleapInteger.getLabel(), group, tuleapInteger);
	}

	/**
	 * Creates the dedicated type of widget for the Tuleap computed value field.
	 * 
	 * @param tuleapComputedValue
	 *            The Tuleap computed value field
	 * @param group
	 *            The group in which the widget should be created
	 */
	private void createGroupContent(TuleapComputedValue tuleapComputedValue, Group group) {
		// Create a single line text field for each Tuleap computed value
		createGroupContentForString(tuleapComputedValue.getLabel(), group, tuleapComputedValue);
	}

	/**
	 * Creates the dedicated type of widget for the Tuleap float field.
	 * 
	 * @param tuleapFloat
	 *            The Tuleap float field
	 * @param group
	 *            The group in which the widget should be created
	 */
	private void createGroupContent(TuleapFloat tuleapFloat, Group group) {
		// Create a single line text field for each Tuleap float
		createGroupContentForString(tuleapFloat.getLabel(), group, tuleapFloat);
	}

	/**
	 * Creates the dedicated type of widget for the Tuleap string kind field.
	 * 
	 ** @param tuleapLabel
	 *            The label
	 * @param group
	 *            The group in which the widget should be created
	 * @param tuleapField
	 *            The tuleap field
	 */
	private void createGroupContentForString(String tuleapLabel, Group group, AbstractTuleapField tuleapField) {
		Label label = new Label(group, SWT.NONE);
		label.setText(tuleapLabel);

		Combo combo = new Combo(group, SWT.SINGLE | SWT.READ_ONLY);
		combo.setItems(new String[] {ITuleapQueryOptions.StringOptions.STRING_OPTIONS_CONTAINS, });
		combo.setText(ITuleapQueryOptions.StringOptions.STRING_OPTIONS_CONTAINS);

		Text text = new Text(group, SWT.SINGLE | SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(gridData);

		if (queryAttributes.containsKey(tuleapField.getName())) {
			text.setText(queryAttributes.get(tuleapField.getName()));
		}
		elements.add(new TuleapCustomQueryElement(text, tuleapField));
	}

	/**
	 * Creates the dedicated type of widget for the Tuleap date field.
	 * 
	 * @param tuleapDate
	 *            The Tuleap last update date field
	 * @param group
	 *            The group in which the widget should be created
	 */
	private void createGroupContent(TuleapDate tuleapDate, Group group) {
		createGroupContentForDate(tuleapDate.getLabel(), group, tuleapDate);
	}

	/**
	 * Creates the dedicated type of widget for a date field.
	 * 
	 * @param tuleapLabel
	 *            The label
	 * @param group
	 *            The group in which the widget should be created
	 * @param tuleapField
	 *            The tuleap field
	 */
	private void createGroupContentForDate(String tuleapLabel, Group group, AbstractTuleapField tuleapField) {
		Label label = new Label(group, SWT.NONE);
		label.setText(tuleapLabel);

		Combo combo = new Combo(group, SWT.SINGLE | SWT.READ_ONLY);
		combo.setItems(new String[] {ITuleapQueryOptions.StringOptions.STRING_OPTIONS_BEFORE,
				ITuleapQueryOptions.StringOptions.STRING_OPTIONS_DATE_EQUALS,
				ITuleapQueryOptions.StringOptions.STRING_OPTIONS_AFTER, });
		combo.setText(ITuleapQueryOptions.StringOptions.STRING_OPTIONS_BEFORE);

		Text text = new Text(group, SWT.SINGLE | SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(gridData);

		if (queryAttributes.containsKey(tuleapField.getName())) {
			String attributes = queryAttributes.get(tuleapField.getName());
			text.setText(attributes.split(ITuleapConstants.QUERY_ATTRIBUTES_SEPARATOR)[0]);
		}

		elements.add(new TuleapCustomQueryElement(text, tuleapField, combo));
	}

	/**
	 * Creates the dedicated type of widget for the Tuleap select box field.
	 * 
	 * @param tuleapSelectBox
	 *            The Tuleap select box field
	 * @param group
	 *            The group in which the widget should be created
	 */
	private void createGroupContent(TuleapSelectBox tuleapSelectBox, Group group) {
		// Create a single selection combo for each Tuleap select box
		Label label = new Label(group, SWT.NONE);
		label.setText(tuleapSelectBox.getLabel());

		Combo combo = new Combo(group, SWT.SINGLE | SWT.READ_ONLY);
		combo.setItems(new String[] {ITuleapQueryOptions.StringOptions.STRING_OPTIONS_EQUALS, });
		combo.setText(ITuleapQueryOptions.StringOptions.STRING_OPTIONS_EQUALS);

		combo = new Combo(group, SWT.SINGLE | SWT.READ_ONLY);
		List<String> items = new ArrayList<String>();
		items.add(""); //$NON-NLS-1$
		int selectedItem = 0;

		String queryAttributeValues = null;
		if (queryAttributes.containsKey(tuleapSelectBox.getName())) {
			queryAttributeValues = queryAttributes.get(tuleapSelectBox.getName());
		}

		int index = 0;
		for (TuleapSelectBoxItem tuleapSelectBoxItem : tuleapSelectBox.getItems()) {
			items.add(tuleapSelectBoxItem.getLabel());
			index++;
			if (String.valueOf(tuleapSelectBoxItem.getIdentifier()).equals(queryAttributeValues)) {
				selectedItem = index; // tuleapSelectBox.getItems().indexOf(tuleapSelectBoxItem) + 1;
			}
		}

		combo.setItems(items.toArray(new String[items.size()]));
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		combo.setLayoutData(gridData);

		combo.select(selectedItem);

		elements.add(new TuleapCustomQueryElement(combo, tuleapSelectBox));
	}

	/**
	 * Creates the dedicated type of widget for the Tuleap multi select box field.
	 * 
	 * @param tuleapMultiSelectBox
	 *            The Tuleap multi select box field
	 * @param group
	 *            The group in which the widget should be created
	 */
	private void createGroupContent(TuleapMultiSelectBox tuleapMultiSelectBox, Group group) {
		Label label = new Label(group, SWT.NONE);
		label.setText(tuleapMultiSelectBox.getLabel());

		Combo combo = new Combo(group, SWT.SINGLE | SWT.READ_ONLY);
		combo.setItems(new String[] {ITuleapQueryOptions.StringOptions.STRING_OPTIONS_CONTAINS, });
		combo.setText(ITuleapQueryOptions.StringOptions.STRING_OPTIONS_CONTAINS);

		org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(group, SWT.MULTI | SWT.READ_ONLY
				| SWT.V_SCROLL | SWT.BORDER);
		List<String> items = new ArrayList<String>();
		items.add(""); //$NON-NLS-1$

		Collection<TuleapSelectBoxItem> tuleapMultiSelectBoxItems = tuleapMultiSelectBox.getItems();
		List<Integer> selectedItems = new ArrayList<Integer>();

		String queryAttributeValues = null;
		if (queryAttributes.containsKey(tuleapMultiSelectBox.getName())) {
			queryAttributeValues = queryAttributes.get(tuleapMultiSelectBox.getName());
		}

		int index = 0;
		for (TuleapSelectBoxItem tuleapSelectBoxItem : tuleapMultiSelectBoxItems) {
			index++;
			items.add(tuleapSelectBoxItem.getLabel());
			if (queryAttributeValues != null && queryAttributeValues.contains(tuleapSelectBoxItem.getLabel())) {
				selectedItems.add(Integer.valueOf(index));
			}
		}
		list.setItems(items.toArray(new String[items.size()]));
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		list.setLayoutData(gridData);

		if (selectedItems.isEmpty()) {
			list.setSelection(0);
		} else {
			selectItems(list, selectedItems);
		}

		elements.add(new TuleapCustomQueryElement(list, tuleapMultiSelectBox));
	}

	/**
	 * Creates the dedicated type of widget for the Tuleap open list field.
	 * 
	 * @param tuleapOpenList
	 *            The Tuleap open list field
	 * @param group
	 *            The group in which the widget should be created
	 */
	private void createGroupContent(TuleapOpenList tuleapOpenList, Group group) {
		// Create a single line text field for each Tuleap string
		createGroupContentForString(tuleapOpenList.getLabel(), group, tuleapOpenList);
	}

	/**
	 * Creates the dedicated type of widget for the Tuleap artifact link field.
	 * 
	 * @param tuleapArtifactLink
	 *            The Tuleap string field
	 * @param group
	 *            The group in which the widget should be created
	 */
	private void createGroupContent(TuleapArtifactLink tuleapArtifactLink, Group group) {
		// Create a single line text field for each Tuleap string
		createGroupContentForString(tuleapArtifactLink.getLabel(), group, tuleapArtifactLink);
	}

	/**
	 * Select items in a list widget.
	 * 
	 * @param widget
	 *            List widget
	 * @param selectedItems
	 *            Indexes of items to select in the widget
	 */
	private void selectItems(org.eclipse.swt.widgets.List widget, List<Integer> selectedItems) {
		int[] array = new int[selectedItems.size()];
		for (int i = 0; i < selectedItems.size(); i++) {
			array[i] = selectedItems.get(i).intValue();
		}
		widget.setSelection(array);
	}

	/**
	 * Creates the dedicated type of widget for the Tuleap text field.
	 * 
	 * @param tuleapText
	 *            The Tuleap text field
	 * @param group
	 *            The group in which the widget should be created
	 */
	private void createGroupContent(TuleapText tuleapText, Group group) {
		// Create a multi line text for each Tuleap text
		Label label = new Label(group, SWT.NONE);
		label.setText(tuleapText.getLabel());

		Combo combo = new Combo(group, SWT.SINGLE | SWT.READ_ONLY);
		combo.setItems(new String[] {ITuleapQueryOptions.StringOptions.STRING_OPTIONS_CONTAINS, });
		combo.setText(ITuleapQueryOptions.StringOptions.STRING_OPTIONS_CONTAINS);

		Text text = new Text(group, SWT.BORDER | SWT.MULTI);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(gridData);

		if (queryAttributes.containsKey(tuleapText.getName())) {
			text.setText(queryAttributes.get(tuleapText.getName()));
		}

		elements.add(new TuleapCustomQueryElement(text, tuleapText));
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

			// For each field set the query attribute
			for (TuleapCustomQueryElement element : elements) {
				String[] values = element.getValue();
				String attributeValue = ""; //$NON-NLS-1$
				for (int i = 0; i < values.length; i++) {
					if (values[i] != null) {
						attributeValue += values[i];
						if (i < values.length - 1) {
							attributeValue += ITuleapConstants.QUERY_ATTRIBUTES_SEPARATOR;
						}
					}
				}

				String operation = element.getOperation();
				if (operation != null) {
					attributeValue += ITuleapConstants.QUERY_ATTRIBUTES_SEPARATOR + operation;
				}

				query.setAttribute(String.valueOf(element.getTuleapFieldName()), attributeValue);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2#isPageComplete()
	 */
	@Override
	public boolean isPageComplete() {
		return this.getQueryTitle() != null && this.queryTitle.length() > 0;
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
