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
package org.tuleap.mylyn.task.internal.ui.wizards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttachmentModel;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tasks.ui.wizards.TaskAttachmentPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.tuleap.mylyn.task.internal.core.client.ITuleapClient;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.TuleapInstanceConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFileUpload;
import org.tuleap.mylyn.task.internal.core.repository.TuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.repository.TuleapTaskMapper;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.ui.util.TuleapMylynTasksUIMessages;

/**
 * This page will be the last shown in the wizard to upload attachments. It will let the user select the field
 * that will receive the attachment.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapTaskAttachmentPage extends TaskAttachmentPage {

	/**
	 * The mapping between the name and the label of the fields.
	 */
	private Map<String, String> name2label = new HashMap<String, String>();

	/**
	 * The constructor.
	 * 
	 * @param model
	 *            The model
	 */
	public TuleapTaskAttachmentPage(TaskAttachmentModel model) {
		super(model);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.wizards.TaskAttachmentPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(GridLayoutFactory.fillDefaults().create());

		super.createControl(composite);

		// Create a section to select the field to use for the upload
		this.createFileUploadFieldSection(composite);
		this.setControl(composite);
	}

	/**
	 * Creates a section where the list of "FILE" fields for the repository will be displayed to select the
	 * field to use for the upload of the data.
	 * 
	 * @param parent
	 *            The parent composite
	 */
	private void createFileUploadFieldSection(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		composite.setLayout(gridLayout);
		setControl(composite);

		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout(3, false));

		new Label(composite, SWT.NONE).setText(TuleapMylynTasksUIMessages
				.getString("TuleapTaskAttachmentPage.AttachmentSelectionLabel")); //$NON-NLS-1$
		final Combo fieldNameCombo = new Combo(composite, SWT.NONE);
		fieldNameCombo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));

		fieldNameCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String name = fieldNameCombo.getText();
				String label = TuleapTaskAttachmentPage.this.name2label.get(name);
				if (label != null) {
					TaskAttribute taskAttribute = TuleapTaskAttachmentPage.this.getModel().getAttribute()
							.createAttribute(ITuleapConstants.ATTACHMENT_FIELD_NAME);
					taskAttribute.setValue(name);

					taskAttribute = TuleapTaskAttachmentPage.this.getModel().getAttribute().createAttribute(
							ITuleapConstants.ATTACHMENT_FIELD_LABEL);
					taskAttribute.setValue(label);
				}

			}
		});

		String[] attachmentFieldsName = this.retrieveAttachmentFieldsName();
		fieldNameCombo.setItems(attachmentFieldsName);
		if (attachmentFieldsName.length > 0) {
			fieldNameCombo.setText(attachmentFieldsName[0]);
		}
	}

	/**
	 * Returns the list of the name of the fields that can be used to upload a file.
	 * 
	 * @return The list of the name of the fields that can be used to upload a file.
	 */
	private String[] retrieveAttachmentFieldsName() {
		TaskRepository taskRepository = this.getModel().getTaskRepository();
		AbstractRepositoryConnector repositoryConnector = TasksUi.getRepositoryConnector(taskRepository
				.getConnectorKind());
		if (repositoryConnector instanceof TuleapRepositoryConnector) {
			TuleapRepositoryConnector tuleapRepositoryConnector = (TuleapRepositoryConnector)repositoryConnector;
			ITuleapClient client = tuleapRepositoryConnector.getClientManager().getClient(taskRepository);
			TuleapInstanceConfiguration repositoryConfiguration = client.getRepositoryConfiguration();

			TaskAttribute attribute = this.getModel().getAttribute();
			TaskData taskData = attribute.getTaskData();
			TuleapTaskMapper tuleapTaskMapper = new TuleapTaskMapper(taskData);
			int trackerId = tuleapTaskMapper.getTrackerId();

			List<String> attachmentFieldsName = new ArrayList<String>();

			TuleapTrackerConfiguration trackerConfiguration = repositoryConfiguration
					.getTrackerConfiguration(trackerId);
			Collection<AbstractTuleapField> fields = trackerConfiguration.getFields();
			for (AbstractTuleapField abstractTuleapField : fields) {
				if (abstractTuleapField instanceof TuleapFileUpload) {
					attachmentFieldsName.add(abstractTuleapField.getName());
					this.name2label.put(abstractTuleapField.getName(), abstractTuleapField.getLabel());
				}
			}
			return attachmentFieldsName.toArray(new String[attachmentFieldsName.size()]);
		}
		return new String[] {};
	}
}
