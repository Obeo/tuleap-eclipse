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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.mylyn.commons.workbench.forms.SectionComposite;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.tuleap.mylyn.task.internal.core.client.ITuleapClient;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.TuleapInstanceConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapArtifactLink;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapDate;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFloat;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapInteger;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapMultiSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapOpenList;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapText;
import org.tuleap.mylyn.task.internal.core.model.field.dynamic.TuleapArtifactId;
import org.tuleap.mylyn.task.internal.core.model.field.dynamic.TuleapComputedValue;
import org.tuleap.mylyn.task.internal.core.model.field.dynamic.TuleapLastUpdateDate;
import org.tuleap.mylyn.task.internal.core.model.field.dynamic.TuleapSubmittedBy;
import org.tuleap.mylyn.task.internal.core.model.field.dynamic.TuleapSubmittedOn;
import org.tuleap.mylyn.task.internal.core.repository.TuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.ui.TuleapTasksUIPlugin;
import org.tuleap.mylyn.task.internal.ui.util.ITuleapUIConstants;
import org.tuleap.mylyn.task.internal.ui.util.TuleapMylynTasksUIMessages;
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
	private int groupId = -1;

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
	private TuleapTrackerConfiguration tuleapTrackerConfiguration;

	/**
	 * The constructor.
	 * 
	 * @param taskRepository
	 *            The task repository
	 */
	public TuleapCustomQueryPage(TaskRepository taskRepository) {
		super(TuleapMylynTasksUIMessages.getString("TuleapCustomQueryPage.Name"), taskRepository, null); //$NON-NLS-1$
		this.setTitle(TuleapMylynTasksUIMessages.getString("TuleapCustomQueryPage.Title")); //$NON-NLS-1$
		this.setDescription(TuleapMylynTasksUIMessages.getString("TuleapCustomQueryPage.Description")); //$NON-NLS-1$

		IWizardPage previousPage = this.getPreviousPage();
		if (previousPage instanceof TuleapQueryPage) {
			previousPage = ((TuleapQueryPage)previousPage).getPreviousPage();
			if (previousPage instanceof TuleapTrackerPage) {
				this.trackerId = ((TuleapTrackerPage)previousPage).getTrackerSelected().getTrackerId();
				this.groupId = ((TuleapTrackerPage)previousPage).getTrackerSelected()
						.getTuleapProjectConfiguration().getIdentifier();
			}
		}
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
		super(TuleapMylynTasksUIMessages.getString("TuleapCustomQueryPage.Name"), taskRepository, queryToEdit); //$NON-NLS-1$
		this.setTitle(TuleapMylynTasksUIMessages.getString("TuleapCustomQueryPage.Title")); //$NON-NLS-1$
		this.setDescription(TuleapMylynTasksUIMessages.getString("TuleapCustomQueryPage.Description")); //$NON-NLS-1$

		// Case existing query to edit, the tracker id is provided by the query attributes
		if (queryToEdit != null) {
			String queryTrackerId = this.getQuery().getAttribute(ITuleapConstants.QUERY_TRACKER_ID);
			this.trackerId = Integer.valueOf(queryTrackerId).intValue();
			String queryGroupId = this.getQuery().getAttribute(ITuleapConstants.QUERY_GROUP_ID);
			this.groupId = Integer.valueOf(queryGroupId).intValue();
			this.queryTitle = queryToEdit.getSummary();
			this.queryAttributes.putAll(this.getQuery().getAttributes());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage2#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		IWizardPage previousPage = this.getPreviousPage();
		if (previousPage instanceof TuleapQueryPage) {
			TuleapQueryPage tuleapQueryPage = (TuleapQueryPage)previousPage;
			this.setQueryTitle(tuleapQueryPage.getQueryTitle());
		}
		super.setVisible(visible);
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
		if (this.trackerId != -1 && this.groupId != -1) {
			query.setSummary(this.getQueryTitle());
			query.setAttribute(ITuleapConstants.QUERY_KIND, ITuleapConstants.QUERY_KIND_CUSTOM);
			query.setAttribute(ITuleapConstants.QUERY_TRACKER_ID, Integer.valueOf(this.trackerId).toString());
			query.setAttribute(ITuleapConstants.QUERY_GROUP_ID, Integer.valueOf(this.groupId).toString());

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

				if (attributeValue != null) {
					query.setAttribute(String.valueOf(element.getTuleapFieldName()), attributeValue);
				}
			}
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

		String connectorKind = this.getTaskRepository().getConnectorKind();
		AbstractRepositoryConnector connector = TasksUi.getRepositoryManager().getRepositoryConnector(
				connectorKind);
		if (!(connector instanceof TuleapRepositoryConnector)) {
			TuleapTasksUIPlugin.log(TuleapMylynTasksUIMessages.getString(
					"TuleapCustomQueryPage.InvalidConnector", this.getTaskRepository().getRepositoryUrl()), //$NON-NLS-1$
					true);
			return;
		}

		IWizardPage previousPage = this.getPreviousPage();
		if (previousPage instanceof TuleapQueryPage) {
			previousPage = ((TuleapQueryPage)previousPage).getPreviousPage();
			if (previousPage instanceof TuleapTrackerPage) {
				this.trackerId = ((TuleapTrackerPage)previousPage).getTrackerSelected().getTrackerId();
				this.groupId = ((TuleapTrackerPage)previousPage).getTrackerSelected()
						.getTuleapProjectConfiguration().getIdentifier();
			}
		}

		if (this.trackerId != -1 && this.groupId != -1) {
			final TuleapRepositoryConnector repositoryConnector = (TuleapRepositoryConnector)connector;
			ITuleapClient client = repositoryConnector.getClientManager().getClient(this.getTaskRepository());
			tuleapTrackerConfiguration = client.getRepositoryConfiguration().getTrackerConfiguration(
					this.trackerId);

			if (tuleapTrackerConfiguration == null) {
				reloadConfiguration(repositoryConnector);
			}

			Group group = new Group(parent.getContent(), SWT.NONE);
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.grabExcessVerticalSpace = false;
			group.setLayoutData(gridData);
			group.setLayout(new GridLayout(3, false));

			List<AbstractTuleapField> fields = tuleapTrackerConfiguration.getFields();
			for (AbstractTuleapField field : fields) {
				if (field instanceof TuleapString) {
					this.createGroupContent((TuleapString)field, group);
				} else if (field instanceof TuleapSelectBox) {
					this.createGroupContent((TuleapSelectBox)field, group);
				} else if (field instanceof TuleapMultiSelectBox) {
					this.createGroupContent((TuleapMultiSelectBox)field, group);
				} else if (field instanceof TuleapText) {
					this.createGroupContent((TuleapText)field, group);
				} else if (field instanceof TuleapArtifactId) {
					this.createGroupContent((TuleapArtifactId)field, group);
				} else if (field instanceof TuleapLastUpdateDate) {
					this.createGroupContent((TuleapLastUpdateDate)field, group);
				} else if (field instanceof TuleapDate) {
					this.createGroupContent((TuleapDate)field, group);
				} else if (field instanceof TuleapSubmittedOn) {
					this.createGroupContent((TuleapSubmittedOn)field, group);
				} else if (field instanceof TuleapSubmittedBy) {
					this.createGroupContent((TuleapSubmittedBy)field, group);
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
	}

	/**
	 * Reload the tracker configuration.
	 * 
	 * @param repositoryConnector
	 *            Repository connector
	 */
	public void reloadConfiguration(final TuleapRepositoryConnector repositoryConnector) {
		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				final TuleapInstanceConfiguration instanceConfiguration = repositoryConnector
						.getRepositoryConfiguration(TuleapCustomQueryPage.this.getTaskRepository(), true,
								monitor);
				tuleapTrackerConfiguration = instanceConfiguration.getTrackerConfiguration(trackerId);
			}
		};
		try {
			PlatformUI.getWorkbench().getProgressService().run(true, false, runnable);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			this.getWizard().getContainer().updateButtons();
		}
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
	 * Creates the dedicated type of widget for the Tuleap string field.
	 * 
	 * @param tuleapSubmittedBy
	 *            The Tuleap string field
	 * @param group
	 *            The group in which the widget should be created
	 */
	private void createGroupContent(TuleapSubmittedBy tuleapSubmittedBy, Group group) {
		// Create a single line text field for each Tuleap submitted by
		createGroupContentForString(tuleapSubmittedBy.getLabel(), group, tuleapSubmittedBy);
	}

	/**
	 * Creates the dedicated type of widget for the Tuleap last update date field.
	 * 
	 * @param tuleapDate
	 *            The Tuleap last update date field
	 * @param group
	 *            The group in which the widget should be created
	 */
	private void createGroupContent(TuleapLastUpdateDate tuleapDate, Group group) {
		createGroupContentForDate(tuleapDate.getLabel(), group, tuleapDate);
	}

	/**
	 * Creates the dedicated type of widget for the Tuleap submitted on field.
	 * 
	 * @param tuleapDate
	 *            The Tuleap submitted on field
	 * @param group
	 *            The group in which the widget should be created
	 */
	private void createGroupContent(TuleapSubmittedOn tuleapDate, Group group) {
		createGroupContentForDate(tuleapDate.getLabel(), group, tuleapDate);
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
	 * Creates the dedicated type of widget for the Tuleap artifact id field.
	 * 
	 * @param tuleapArtifactId
	 *            The Tuleap artifact id field
	 * @param group
	 *            The group in which the widget should be created
	 */
	private void createGroupContent(TuleapArtifactId tuleapArtifactId, Group group) {
		// Create a single line text field for each Tuleap ArtifactId
		createGroupContentForString(tuleapArtifactId.getLabel(), group, tuleapArtifactId);
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

		for (TuleapSelectBoxItem tuleapSelectBoxItem : tuleapSelectBox.getItems()) {
			items.add(tuleapSelectBoxItem.getLabel());
			if (String.valueOf(tuleapSelectBoxItem.getIdentifier()).equals(queryAttributeValues)) {
				selectedItem = tuleapSelectBox.getItems().indexOf(tuleapSelectBoxItem) + 1;
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

		List<TuleapSelectBoxItem> tuleapMultiSelectBoxItems = tuleapMultiSelectBox.getItems();
		List<Integer> selectedItems = new ArrayList<Integer>();

		String queryAttributeValues = null;
		if (queryAttributes.containsKey(tuleapMultiSelectBox.getName())) {
			queryAttributeValues = queryAttributes.get(tuleapMultiSelectBox.getName());
		}

		for (int i = 0; i < tuleapMultiSelectBoxItems.size(); i++) {
			TuleapSelectBoxItem tuleapSelectBoxItem = tuleapMultiSelectBoxItems.get(i);
			items.add(tuleapSelectBoxItem.getLabel());
			if (queryAttributeValues != null && queryAttributeValues.contains(tuleapSelectBoxItem.getLabel())) {
				selectedItems
						.add(Integer.valueOf(tuleapMultiSelectBoxItems.indexOf(tuleapSelectBoxItem) + 1));
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
		return TuleapTasksUIPlugin.getDefault().getImage(ITuleapUIConstants.Icons.WIZARD_TULEAP_LOGO_48);
	}

}
