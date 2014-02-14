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

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.internal.core.data.TuleapArtifactMapper;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskId;
import org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapTracker;
import org.tuleap.mylyn.task.internal.core.model.config.field.AbstractTuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapFileUpload;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.model.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.AttachmentFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.AttachmentValue;
import org.tuleap.mylyn.task.internal.core.model.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapElementComment;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;

/**
 * Utility class used to transform a {@link TuleapArtifact} into a {@link TaskData} and vice versa.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class ArtifactTaskDataConverter {

	/**
	 * The tracker.
	 */
	protected final TuleapTracker tracker;

	/**
	 * The task repository to use.
	 */
	protected final TaskRepository taskRepository;

	/**
	 * The connector to use.
	 */
	protected final ITuleapRepositoryConnector connector;

	/**
	 * Constructor.
	 * 
	 * @param tracker
	 *            The tracker.
	 * @param taskRepository
	 *            The task repository to use.
	 * @param connector
	 *            The repository connector to use.
	 */
	public ArtifactTaskDataConverter(TuleapTracker tracker, TaskRepository taskRepository,
			ITuleapRepositoryConnector connector) {
		this.tracker = tracker; // Can be null
		Assert.isNotNull(taskRepository);
		Assert.isNotNull(connector);
		this.taskRepository = taskRepository;
		this.connector = connector;
	}

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
	protected TaskData populateTaskDataConfigurableFields(TaskData taskData, TuleapArtifact element) {
		TuleapArtifactMapper tuleapArtifactMapper = new TuleapArtifactMapper(taskData, this.tracker);
		tuleapArtifactMapper.initializeEmptyTaskData();

		// Task Key
		String taskKey = TuleapTaskId.getTaskDataKey(this.tracker.getProject().getLabel(), this.tracker
				.getLabel(), element.getId().intValue());
		tuleapArtifactMapper.setTaskKey(taskKey);

		// URL
		tuleapArtifactMapper.setTaskUrl(element.getHtmlUrl());

		// Dates
		tuleapArtifactMapper.setCreationDate(element.getSubmittedOn());
		tuleapArtifactMapper.setModificationDate(element.getLastModifiedDate());

		// Summary
		TuleapString titleField = this.tracker.getTitleField();
		if (titleField != null) {
			AbstractFieldValue fieldValue = element.getFieldValue(titleField.getIdentifier());
			if (fieldValue instanceof LiteralFieldValue) {
				LiteralFieldValue literalFieldValue = (LiteralFieldValue)fieldValue;
				tuleapArtifactMapper.setSummary(literalFieldValue.getFieldValue());
			}
		}

		// Status
		AbstractTuleapSelectBox statusField = this.tracker.getStatusField();
		if (statusField != null) {
			AbstractFieldValue fieldValue = element.getFieldValue(statusField.getIdentifier());
			if (fieldValue instanceof BoundFieldValue) {
				BoundFieldValue boundFieldValue = (BoundFieldValue)fieldValue;
				List<Integer> valueIds = boundFieldValue.getValueIds();

				if (valueIds.size() > 0) {
					// Only support one status
					tuleapArtifactMapper.setStatus(valueIds.get(0).intValue());
				}
			}
		}

		// Persons
		AbstractTuleapSelectBox contributorField = this.tracker.getContributorField();
		if (contributorField != null) {
			AbstractFieldValue abstractFieldValue = element.getFieldValue(contributorField.getIdentifier());
			if (abstractFieldValue instanceof BoundFieldValue) {
				BoundFieldValue boundFieldValue = (BoundFieldValue)abstractFieldValue;
				tuleapArtifactMapper.setAssignedTo(boundFieldValue.getValueIds());
			}
		}

		// Comments
		List<TuleapElementComment> comments = element.getComments();
		for (TuleapElementComment comment : comments) {
			tuleapArtifactMapper.addComment(comment);
		}

		// Attachments
		TuleapFileUpload attachmentField = this.tracker.getAttachmentField();
		if (attachmentField != null) {
			AbstractFieldValue attachmentFieldValue = element.getFieldValue(attachmentField.getIdentifier());
			if (attachmentFieldValue instanceof AttachmentFieldValue) {
				AttachmentFieldValue aFieldValue = (AttachmentFieldValue)attachmentFieldValue;
				List<AttachmentValue> attachments = aFieldValue.getAttachments();
				for (AttachmentValue attachment : attachments) {
					tuleapArtifactMapper.addAttachment(attachmentField.getName(), attachment);
				}
			}
		}

		// Additional fields
		Collection<AbstractTuleapField> fields = this.tracker.getFields();
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
					tuleapArtifactMapper.setValue(literalFieldValue.getFieldValue(), literalFieldValue
							.getFieldId());
				} else if (fieldValue instanceof BoundFieldValue) {
					BoundFieldValue boundFieldValue = (BoundFieldValue)fieldValue;
					List<Integer> bindValueIds = boundFieldValue.getValueIds();

					List<String> values = new ArrayList<String>();
					for (Integer bindValueId : bindValueIds) {
						values.add(bindValueId.toString());
					}

					tuleapArtifactMapper.setValues(values, boundFieldValue.getFieldId());
				}
			}
		}

		return taskData;
	}

	/**
	 * Fills the task data related to the given milestone POJO.
	 * 
	 * @param taskData
	 *            the task data to fill/update with the given milestone.
	 * @param tuleapArtifact
	 *            The artifact to use to populate the task data
	 * @param monitor
	 *            The progress monitor to use
	 */
	public void populateTaskData(TaskData taskData, TuleapArtifact tuleapArtifact, IProgressMonitor monitor) {
		populateTaskDataConfigurableFields(taskData, tuleapArtifact);
	}

	/**
	 * Creates a tuleap artifact POJO from the related task data.
	 * 
	 * @param taskData
	 *            The updated task data.
	 * @return The tuleap artifact POJO.
	 */
	public TuleapArtifact createTuleapArtifact(TaskData taskData) {
		TuleapArtifactMapper tuleapArtifactMapper = new TuleapArtifactMapper(taskData, this.tracker);

		TuleapArtifact tuleapArtifact = null;
		TuleapTaskId taskId = tuleapArtifactMapper.getTaskId();
		TuleapReference trackerRef = new TuleapReference();
		trackerRef.setId(taskId.getTrackerId());
		TuleapReference projectRef = new TuleapReference();
		projectRef.setId(taskId.getProjectId());
		if (taskData.isNew()) {
			tuleapArtifact = new TuleapArtifact(trackerRef, projectRef);
		} else {
			tuleapArtifact = new TuleapArtifact(taskId.getArtifactId(), trackerRef, projectRef);
		}

		List<AbstractFieldValue> fieldValues = tuleapArtifactMapper.getFieldValues();
		for (AbstractFieldValue abstractFieldValue : fieldValues) {
			tuleapArtifact.addFieldValue(abstractFieldValue);
		}

		// New comment
		String newComment = ""; //$NON-NLS-1$
		TaskAttribute newCommentAttribute = taskData.getRoot().getMappedAttribute(TaskAttribute.COMMENT_NEW);
		if (newCommentAttribute != null) {
			newComment = newCommentAttribute.getValue();
		}
		tuleapArtifact.setNewComment(newComment);
		return tuleapArtifact;
	}

}
