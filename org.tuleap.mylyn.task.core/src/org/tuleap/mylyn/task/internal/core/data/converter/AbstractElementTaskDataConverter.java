/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.internal.core.data.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.internal.core.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.data.AttachmentFieldValue;
import org.tuleap.mylyn.task.internal.core.data.AttachmentValue;
import org.tuleap.mylyn.task.internal.core.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskMapper;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableElement;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableFieldsConfiguration;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.TuleapElementComment;
import org.tuleap.mylyn.task.internal.core.model.field.AbstractTuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFileUpload;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.util.TuleapUtil;

/**
 * Common ancestor of all the task data converter.
 * 
 * @param <ELEMENT>
 *            The kind of {@link AbstractTuleapConfigurableElement} to use.
 * @param <CONFIGURATION>
 *            The kind of {@link AbstractTuleapConfigurableFieldsConfiguration} to use.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public abstract class AbstractElementTaskDataConverter<ELEMENT extends AbstractTuleapConfigurableElement, CONFIGURATION extends AbstractTuleapConfigurableFieldsConfiguration> {

	/**
	 * The configuration of the kind of elements.
	 */
	protected final CONFIGURATION configuration;

	/**
	 * The constructor.
	 * 
	 * @param configuration
	 *            The configuration
	 */
	public AbstractElementTaskDataConverter(CONFIGURATION configuration) {
		this.configuration = configuration;
	}

	/**
	 * Fills the task data related to the given milestone POJO.
	 * 
	 * @param taskData
	 *            the task data to fill/update with the given milestone.
	 * @param element
	 *            The element to use to populate the task data
	 */
	public abstract void populateTaskData(TaskData taskData, ELEMENT element);

	/**
	 * Utility operation used to populate the task data with all the information from the configurable fields
	 * from an element.
	 * 
	 * @param taskData
	 *            The task data to populate
	 * @param element
	 *            The element containing the data
	 * @return The task data configured
	 */
	protected TaskData populateTaskDataConfigurableFields(TaskData taskData, ELEMENT element) {
		TuleapTaskMapper tuleapTaskMapper = new TuleapTaskMapper(taskData, this.configuration);
		tuleapTaskMapper.initializeEmptyTaskData();

		// Task Key
		String taskKey = TuleapUtil.getTaskDataKey(this.configuration.getTuleapProjectConfiguration()
				.getName(), this.configuration.getName(), element.getId());
		tuleapTaskMapper.setTaskKey(taskKey);

		// URL
		tuleapTaskMapper.setTaskUrl(element.getHtmlUrl());

		// Dates
		tuleapTaskMapper.setCreationDate(element.getCreationDate());
		tuleapTaskMapper.setModificationDate(element.getLastModificationDate());

		// Summary
		TuleapString titleField = this.configuration.getTitleField();
		if (titleField != null) {
			AbstractFieldValue fieldValue = element
					.getFieldValue(Integer.valueOf(titleField.getIdentifier()));
			if (fieldValue instanceof LiteralFieldValue) {
				LiteralFieldValue literalFieldValue = (LiteralFieldValue)fieldValue;
				tuleapTaskMapper.setSummary(literalFieldValue.getFieldValue());
			}
		}

		// Status
		AbstractTuleapSelectBox statusField = this.configuration.getStatusField();
		if (statusField != null) {
			AbstractFieldValue fieldValue = element.getFieldValue(Integer
					.valueOf(statusField.getIdentifier()));
			if (fieldValue instanceof BoundFieldValue) {
				BoundFieldValue boundFieldValue = (BoundFieldValue)fieldValue;
				List<Integer> valueIds = boundFieldValue.getValueIds();

				List<String> valuesString = new ArrayList<String>();
				for (Integer valueId : valueIds) {
					valuesString.add(valueId.toString());
				}

				if (valuesString.size() > 0) {
					// Only support one status
					tuleapTaskMapper.setStatus(valuesString.get(0));
				}
			}
		}

		// Persons
		AbstractTuleapSelectBox contributorField = this.configuration.getContributorField();
		if (contributorField != null) {
			AbstractFieldValue abstractFieldValue = element.getFieldValue(Integer.valueOf(contributorField
					.getIdentifier()));
			if (abstractFieldValue instanceof BoundFieldValue) {
				BoundFieldValue boundFieldValue = (BoundFieldValue)abstractFieldValue;
				tuleapTaskMapper.setAssignedTo(boundFieldValue.getValueIds());
			}
		}

		// Comments
		List<TuleapElementComment> comments = element.getComments();
		for (TuleapElementComment comment : comments) {
			tuleapTaskMapper.addComment(comment);
		}

		// Attachments
		TuleapFileUpload attachmentField = this.configuration.getAttachmentField();
		if (attachmentField != null) {
			AbstractFieldValue attachmentFieldValue = element.getFieldValue(Integer.valueOf(attachmentField
					.getIdentifier()));
			if (attachmentFieldValue instanceof AttachmentFieldValue) {
				AttachmentFieldValue aFieldValue = (AttachmentFieldValue)attachmentFieldValue;
				List<AttachmentValue> attachments = aFieldValue.getAttachments();
				for (AttachmentValue attachment : attachments) {
					tuleapTaskMapper.addAttachment(attachmentField.getName(), attachment);
				}
			}
		}

		// Additional fields
		Collection<AbstractTuleapField> fields = this.configuration.getFields();
		for (AbstractTuleapField abstractTuleapField : fields) {
			boolean isTitle = titleField != null
					&& abstractTuleapField.getIdentifier() == titleField.getIdentifier();
			boolean isStatus = statusField != null
					&& abstractTuleapField.getIdentifier() == statusField.getIdentifier();
			boolean isAttachment = attachmentField != null
					&& abstractTuleapField.getIdentifier() == attachmentField.getIdentifier();
			boolean isContributor = contributorField != null
					&& abstractTuleapField.getIdentifier() == contributorField.getIdentifier();

			if (!isTitle && !isStatus && !isAttachment && !isContributor) {
				AbstractFieldValue fieldValue = element.getFieldValue(Integer.valueOf(abstractTuleapField
						.getIdentifier()));

				if (fieldValue instanceof LiteralFieldValue) {
					LiteralFieldValue literalFieldValue = (LiteralFieldValue)fieldValue;
					tuleapTaskMapper.setValue(literalFieldValue.getFieldValue(), literalFieldValue
							.getFieldId());
				} else if (fieldValue instanceof BoundFieldValue) {
					BoundFieldValue boundFieldValue = (BoundFieldValue)fieldValue;
					List<Integer> bindValueIds = boundFieldValue.getValueIds();

					List<String> values = new ArrayList<String>();
					for (Integer bindValueId : bindValueIds) {
						values.add(bindValueId.toString());
					}

					tuleapTaskMapper.setValues(values, boundFieldValue.getFieldId());
				}
			}
		}

		return taskData;
	}

	/**
	 * Populate the configurable fields for the given element.
	 * 
	 * @param taskData
	 *            The task data to use
	 * @param element
	 *            The element to populate
	 * @return The element to populate
	 */
	protected ELEMENT populateElementConfigurableFields(TaskData taskData, ELEMENT element) {
		// to do
		return element;
	}
}
