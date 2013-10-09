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

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.data.AttachmentFieldValue;
import org.tuleap.mylyn.task.internal.core.data.AttachmentValue;
import org.tuleap.mylyn.task.internal.core.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.data.TuleapConfigurableElementMapper;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskIdentityUtil;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableElement;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfiguration;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.TuleapElementComment;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.field.AbstractTuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFileUpload;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;

/**
 * Common ancestor of all the task data converter.
 * 
 * @param <ELEMENT>
 *            The kind of {@link AbstractTuleapConfigurableElement} to use.
 * @param <CONFIGURATION>
 *            The kind of {@link AbstractTuleapConfiguration} to use.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public abstract class AbstractElementTaskDataConverter<ELEMENT extends AbstractTuleapConfigurableElement, CONFIGURATION extends AbstractTuleapConfiguration> {

	/**
	 * The configuration of the kind of elements.
	 */
	protected final CONFIGURATION configuration;

	/**
	 * The task repository to use.
	 */
	protected final TaskRepository taskRepository;

	/**
	 * The connector to use.
	 */
	protected final ITuleapRepositoryConnector connector;

	/**
	 * Map of refreshed configurations to only refresh them once during the "transaction".
	 */
	private final Map<Integer, AbstractTuleapConfiguration> refreshedConfigurationsById = Maps
			.newHashMap();

	/**
	 * The constructor.
	 * 
	 * @param configuration
	 *            The configuration.
	 * @param taskRepository
	 *            The task repository to use.
	 * @param connector
	 *            The repository connector to use.
	 */
	public AbstractElementTaskDataConverter(CONFIGURATION configuration, TaskRepository taskRepository,
			ITuleapRepositoryConnector connector) {
		this.configuration = configuration; // Can be null
		Assert.isNotNull(taskRepository);
		Assert.isNotNull(connector);
		this.taskRepository = taskRepository;
		this.connector = connector;
	}

	/**
	 * Fills the task data related to the given milestone POJO.
	 * 
	 * @param taskData
	 *            the task data to fill/update with the given milestone.
	 * @param element
	 *            The element to use to populate the task data
	 * @param monitor
	 *            The progress monitor to use
	 */
	public abstract void populateTaskData(TaskData taskData, ELEMENT element, IProgressMonitor monitor);

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
		TuleapConfigurableElementMapper tuleapConfigurableElementMapper = new TuleapConfigurableElementMapper(
				taskData, this.configuration);
		tuleapConfigurableElementMapper.initializeEmptyTaskData();

		// Task Key
		String taskKey = TuleapTaskIdentityUtil.getTaskDataKey(this.configuration
				.getTuleapProjectConfiguration().getName(), this.configuration.getLabel(), element.getId());
		tuleapConfigurableElementMapper.setTaskKey(taskKey);

		// URL
		tuleapConfigurableElementMapper.setTaskUrl(element.getHtmlUrl());

		// Dates
		tuleapConfigurableElementMapper.setCreationDate(element.getCreationDate());
		tuleapConfigurableElementMapper.setModificationDate(element.getLastModificationDate());

		// Summary
		TuleapString titleField = this.configuration.getTitleField();
		if (titleField != null) {
			AbstractFieldValue fieldValue = element.getFieldValue(titleField.getIdentifier());
			if (fieldValue instanceof LiteralFieldValue) {
				LiteralFieldValue literalFieldValue = (LiteralFieldValue)fieldValue;
				tuleapConfigurableElementMapper.setSummary(literalFieldValue.getFieldValue());
			}
		}

		// Status
		AbstractTuleapSelectBox statusField = this.configuration.getStatusField();
		if (statusField != null) {
			AbstractFieldValue fieldValue = element.getFieldValue(statusField.getIdentifier());
			if (fieldValue instanceof BoundFieldValue) {
				BoundFieldValue boundFieldValue = (BoundFieldValue)fieldValue;
				List<Integer> valueIds = boundFieldValue.getValueIds();

				if (valueIds.size() > 0) {
					// Only support one status
					tuleapConfigurableElementMapper.setStatus(valueIds.get(0).intValue());
				}
			}
		}

		// Persons
		AbstractTuleapSelectBox contributorField = this.configuration.getContributorField();
		if (contributorField != null) {
			AbstractFieldValue abstractFieldValue = element.getFieldValue(contributorField.getIdentifier());
			if (abstractFieldValue instanceof BoundFieldValue) {
				BoundFieldValue boundFieldValue = (BoundFieldValue)abstractFieldValue;
				tuleapConfigurableElementMapper.setAssignedTo(boundFieldValue.getValueIds());
			}
		}

		// Comments
		List<TuleapElementComment> comments = element.getComments();
		for (TuleapElementComment comment : comments) {
			tuleapConfigurableElementMapper.addComment(comment);
		}

		// Attachments
		TuleapFileUpload attachmentField = this.configuration.getAttachmentField();
		if (attachmentField != null) {
			AbstractFieldValue attachmentFieldValue = element.getFieldValue(attachmentField.getIdentifier());
			if (attachmentFieldValue instanceof AttachmentFieldValue) {
				AttachmentFieldValue aFieldValue = (AttachmentFieldValue)attachmentFieldValue;
				List<AttachmentValue> attachments = aFieldValue.getAttachments();
				for (AttachmentValue attachment : attachments) {
					tuleapConfigurableElementMapper.addAttachment(attachmentField.getName(), attachment);
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
				AbstractFieldValue fieldValue = element.getFieldValue(abstractTuleapField.getIdentifier());

				if (fieldValue instanceof LiteralFieldValue) {
					LiteralFieldValue literalFieldValue = (LiteralFieldValue)fieldValue;
					tuleapConfigurableElementMapper.setValue(literalFieldValue.getFieldValue(),
							literalFieldValue.getFieldId());
				} else if (fieldValue instanceof BoundFieldValue) {
					BoundFieldValue boundFieldValue = (BoundFieldValue)fieldValue;
					List<Integer> bindValueIds = boundFieldValue.getValueIds();

					List<String> values = new ArrayList<String>();
					for (Integer bindValueId : bindValueIds) {
						values.add(bindValueId.toString());
					}

					tuleapConfigurableElementMapper.setValues(values, boundFieldValue.getFieldId());
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

	/**
	 * Refresh the configuration if it has not already been refreshed.
	 * 
	 * @param projectId
	 *            The project Id
	 * @param configurationId
	 *            The configuration Id
	 * @param monitor
	 *            The progress monitor to use
	 */
	protected void refreshConfiguration(int projectId, int configurationId, IProgressMonitor monitor) {
		AbstractTuleapConfiguration refreshedConfig;
		if (refreshedConfigurationsById.containsKey(Integer.valueOf(configurationId))) {
			refreshedConfig = refreshedConfigurationsById.get(Integer.valueOf(configurationId));
		} else {
			// Let's refresh the configuration
			// First, let's get the element's project config, in case the element comes from a
			// different project, who knows...
			TuleapProjectConfiguration projectConfiguration;
			if (configuration != null) {
				projectConfiguration = configuration.getTuleapProjectConfiguration();
			} else {
				projectConfiguration = connector.getTuleapServerConfiguration(taskRepository.getUrl())
						.getProjectConfiguration(projectId);
			}
			refreshedConfig = projectConfiguration.getConfigurableFieldsConfiguration(configurationId);
			try {
				refreshedConfigurationsById.put(Integer.valueOf(configurationId), connector
						.refreshConfiguration(taskRepository, refreshedConfig, monitor));
			} catch (CoreException e) {
				// TODO Check this is the right way to log
				TuleapCoreActivator.log(e, false);
			}
		}
	}
}
