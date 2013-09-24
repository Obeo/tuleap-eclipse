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

import java.util.Collection;
import java.util.List;

import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskMapper;
import org.tuleap.mylyn.task.internal.core.model.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.MultiSelectFieldValue;
import org.tuleap.mylyn.task.internal.core.model.SingleSelectFieldValue;
import org.tuleap.mylyn.task.internal.core.model.TuleapElementComment;
import org.tuleap.mylyn.task.internal.core.model.field.AbstractTuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.util.TuleapUtil;

/**
 * Utility class used to transform a {@link TuleapArtifact} into a {@link TaskData} and vice versa.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class ArtifactTaskDataConverter extends AbstractElementTaskDataConverter<TuleapArtifact, TuleapTrackerConfiguration> {

	/**
	 * The constructor.
	 * 
	 * @param tuleapTrackerConfiguration
	 *            The configuration of the tracker
	 */
	public ArtifactTaskDataConverter(TuleapTrackerConfiguration tuleapTrackerConfiguration) {
		super(tuleapTrackerConfiguration);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.data.converter.AbstractElementTaskDataConverter#populateTaskData(org.eclipse.mylyn.tasks.core.data.TaskData,
	 *      org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableElement)
	 */
	@Override
	public void populateTaskData(TaskData taskData, TuleapArtifact tuleapArtifact) {
		TuleapTaskMapper tuleapTaskMapper = new TuleapTaskMapper(taskData, this.configuration);
		tuleapTaskMapper.initializeEmptyTaskData();

		// Task Key
		String taskKey = TuleapUtil.getTaskDataId(this.configuration.getTuleapProjectConfiguration()
				.getName(), this.configuration.getName(), tuleapArtifact.getId());
		tuleapTaskMapper.setTaskKey(taskKey);

		// URL
		tuleapTaskMapper.setTaskUrl(tuleapArtifact.getHtmlUrl());

		// Dates
		tuleapTaskMapper.setCreationDate(tuleapArtifact.getCreationDate());
		tuleapTaskMapper.setModificationDate(tuleapArtifact.getLastModificationDate());

		// Summary
		TuleapString titleField = this.configuration.getTitleField();
		if (titleField != null) {
			AbstractFieldValue fieldValue = tuleapArtifact.getFieldValue(Integer.valueOf(titleField
					.getIdentifier()));
			if (fieldValue instanceof LiteralFieldValue) {
				LiteralFieldValue literalFieldValue = (LiteralFieldValue)fieldValue;
				tuleapTaskMapper.setSummary(literalFieldValue.getFieldValue());
			}
		}

		// Status
		AbstractTuleapSelectBox statusField = this.configuration.getStatusField();
		if (statusField != null) {
			AbstractFieldValue fieldValue = tuleapArtifact.getFieldValue(Integer.valueOf(statusField
					.getIdentifier()));
			if (fieldValue instanceof SingleSelectFieldValue) {
				SingleSelectFieldValue singleSelectFieldValue = (SingleSelectFieldValue)fieldValue;
				int bindValueId = singleSelectFieldValue.getBindValueId();

				TuleapSelectBoxItem item = statusField.getItem(String.valueOf(bindValueId));
				if (item != null) {
					tuleapTaskMapper.setStatus(item.getLabel());
				}
			} else if (fieldValue instanceof MultiSelectFieldValue) {
				// If we have multiple status, we don't care, we take the first one for now.
				// We will change this if someone complain about it
			}
		}

		// Persons

		// Comments
		List<TuleapElementComment> comments = tuleapArtifact.getComments();
		for (TuleapElementComment comment : comments) {
			tuleapTaskMapper.addComment(comment);
		}

		// Attachments

		// Additional fields
		Collection<AbstractTuleapField> fields = this.configuration.getFields();
		for (AbstractTuleapField abstractTuleapField : fields) {
			// do stuff
		}
	}

	/**
	 * Creates a tuleap artifact POJO from the related task data.
	 * 
	 * @param taskData
	 *            The updated task data.
	 * @return The tuleap artifact POJO.
	 */
	public TuleapArtifact createTuleapArtifact(TaskData taskData) {
		TuleapArtifact tuleapArtifact = new TuleapArtifact();

		// find the configuration of the tuleapArtifact (tracker configuration)
		// tuleapArtifact = this.populateConfigurableFields(tuleapArtifact, taskData, configuration);
		// TODO populate the rest of the fields using the configuration...

		return tuleapArtifact;
	}

}
