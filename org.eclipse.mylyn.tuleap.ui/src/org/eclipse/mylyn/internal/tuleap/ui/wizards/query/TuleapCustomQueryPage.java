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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.commons.workbench.forms.SectionComposite;
import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapInstanceConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapTrackerConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapDate;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapFloat;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapInteger;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapMultiSelectBox;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapSelectBox;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapSelectBoxItem;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapString;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapText;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapArtifactId;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapComputedValue;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapLastUpdateDate;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapSubmittedBy;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapSubmittedOn;
import org.eclipse.mylyn.internal.tuleap.core.repository.ITuleapRepositoryConnector;
import org.eclipse.mylyn.internal.tuleap.core.repository.TuleapRepositoryConnector;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The second page of the Tuleap query wizard with the form based search page.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @since 1.0
 */
public class TuleapCustomQueryPage extends AbstractRepositoryQueryPage2 {
	/**
	 * The task repository.
	 */
	private TaskRepository repository;

	/**
	 * The tracker.
	 */
	private String tracker;

	/**
	 * The query form graphical elements.
	 */
	private List<TuleapCustomQueryElement> elements = new ArrayList<TuleapCustomQueryElement>();

	/**
	 * The constructor.
	 * 
	 * @param taskRepository
	 *            The Mylyn task repository
	 * @param queryToEdit
	 *            The query to edit
	 * @param selectedTracker
	 *            The selected tracker
	 */
	public TuleapCustomQueryPage(TaskRepository taskRepository, IRepositoryQuery queryToEdit,
			String selectedTracker) {
		this(taskRepository, null);
		this.repository = taskRepository;
		this.tracker = selectedTracker;
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
				if (tuleapTrackerConfiguration.getQualifiedName().equals(tracker)) {
					query.setSummary(this.getQueryTitle());
					query.setAttribute(ITuleapConstants.QUERY_KIND, ITuleapConstants.QUERY_KIND_CUSTOM);
					query.setAttribute(ITuleapConstants.QUERY_TRACKER_ID, Integer.valueOf(
							tuleapTrackerConfiguration.getTrackerId()).toString());
					// For each field set the query attribute
					for (TuleapCustomQueryElement element : elements) {
						String[] values = element.getValue();
						for (String value : values) {
							String attributeValue = value;
							String operation = element.getOperation();
							if (operation != null) {
								attributeValue += ITuleapConstants.QUERY_ATTRIBUTES_SEPARATOR + operation;
							}
							if (!value.isEmpty()) {
								query.setAttribute(String.valueOf(element.getTuleapFieldName()),
										attributeValue);
							}
						}
					}

					break;
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
		// TODO Creates the custom query page
		String connectorKind = this.getTaskRepository().getConnectorKind();
		AbstractRepositoryConnector connector = TasksUi.getRepositoryManager().getRepositoryConnector(
				connectorKind);
		if (!(connector instanceof TuleapRepositoryConnector)) {
			TuleapTasksUIPlugin.log(TuleapMylynTasksUIMessages.getString(
					"TuleapCustomQueryPage.InvalidConnector", this.getTaskRepository().getRepositoryUrl()), //$NON-NLS-1$
					true);
			return;
		}
		TuleapRepositoryConnector repositoryConnector = (TuleapRepositoryConnector)connector;
		TuleapInstanceConfiguration repositoryConfiguration = repositoryConnector
				.getRepositoryConfiguration(this.getTaskRepository().getRepositoryUrl());
		if (repositoryConfiguration != null) {

			Group group = new Group(parent.getContent(), SWT.NONE);
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.grabExcessVerticalSpace = false;
			group.setLayoutData(gridData);
			group.setLayout(new GridLayout(3, false));

			List<TuleapTrackerConfiguration> trackerConfigurations = repositoryConfiguration
					.getAllTrackerConfigurations();
			for (TuleapTrackerConfiguration tuleapTrackerConfiguration : trackerConfigurations) {
				if (tuleapTrackerConfiguration.getQualifiedName().equals(tracker)) {
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
						}
					}
					break;
				}
			}
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
		createGroupContentForString(tuleapString.getLabel(), group, tuleapString.getName());
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
		createGroupContentForString(tuleapInteger.getLabel(), group, tuleapInteger.getName());
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
		createGroupContentForString(tuleapComputedValue.getLabel(), group, tuleapComputedValue.getName());
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
		createGroupContentForString(tuleapFloat.getLabel(), group, tuleapFloat.getName());
	}

	/**
	 * Creates the dedicated type of widget for the Tuleap string kind field.
	 * 
	 ** @param tuleapLabel
	 *            The label
	 * @param group
	 *            The group in which the widget should be created
	 * @param tuleapFieldName
	 *            The tuleap field name
	 */
	private void createGroupContentForString(String tuleapLabel, Group group, String tuleapFieldName) {
		Label label = new Label(group, SWT.NONE);
		label.setText(tuleapLabel);

		Combo combo = new Combo(group, SWT.SINGLE | SWT.READ_ONLY);
		combo.setItems(new String[] {ITuleapQueryOptions.StringOptions.STRING_OPTIONS_CONTAINS, });
		combo.setText(ITuleapQueryOptions.StringOptions.STRING_OPTIONS_CONTAINS);

		Text text = new Text(group, SWT.SINGLE | SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(gridData);

		elements.add(new TuleapCustomQueryElement(text, tuleapFieldName));
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
		createGroupContentForString(tuleapSubmittedBy.getLabel(), group, tuleapSubmittedBy.getName());
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
		createGroupContentForDate(tuleapDate.getLabel(), group, tuleapDate.getName());
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
		createGroupContentForDate(tuleapDate.getLabel(), group, tuleapDate.getName());
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
		createGroupContentForDate(tuleapDate.getLabel(), group, tuleapDate.getName());
	}

	/**
	 * Creates the dedicated type of widget for a date field.
	 * 
	 * @param tuleapLabel
	 *            The label
	 * @param group
	 *            The group in which the widget should be created
	 * @param tuleapFieldName
	 *            The tuleap field name
	 */
	private void createGroupContentForDate(String tuleapLabel, Group group, String tuleapFieldName) {
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
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy"); //$NON-NLS-1$
		Date date = new Date();
		// Initialize field with current date
		text.setText(dateFormat.format(date));

		elements.add(new TuleapCustomQueryElement(text, tuleapFieldName, combo));
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
		createGroupContentForString(tuleapArtifactId.getLabel(), group, tuleapArtifactId.getName());
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
		for (TuleapSelectBoxItem tuleapSelectBoxItem : tuleapSelectBox.getItems()) {
			items.add(tuleapSelectBoxItem.getLabel());
		}
		combo.setItems(items.toArray(new String[items.size()]));
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		combo.setLayoutData(gridData);
		if (tuleapSelectBox.getItems().size() > 0) {
			combo.select(0);
		}

		elements.add(new TuleapCustomQueryElement(combo, tuleapSelectBox.getName()));
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
		for (TuleapSelectBoxItem tuleapSelectBoxItem : tuleapMultiSelectBox.getItems()) {
			items.add(tuleapSelectBoxItem.getLabel());
		}
		list.setItems(items.toArray(new String[items.size()]));
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		list.setLayoutData(gridData);
		if (tuleapMultiSelectBox.getItems().size() > 0) {
			list.setSelection(0);
		}

		elements.add(new TuleapCustomQueryElement(list, tuleapMultiSelectBox.getName()));
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

		elements.add(new TuleapCustomQueryElement(text, tuleapText.getName()));
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
	 * Set the selected tracker.
	 * 
	 * @param selectedTracker
	 *            Tracker
	 */
	public void setTracker(String selectedTracker) {
		this.tracker = selectedTracker;
	}

}
