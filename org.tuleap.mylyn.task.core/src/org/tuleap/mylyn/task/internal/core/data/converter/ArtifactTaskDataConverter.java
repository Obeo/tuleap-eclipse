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

import java.util.Set;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.agile.core.data.AgileTaskKindUtil;
import org.tuleap.mylyn.task.internal.core.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.data.TuleapConfigurableElementMapper;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;

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
		super.populateTaskDataConfigurableFields(taskData, tuleapArtifact);

		AgileTaskKindUtil.setAgileTaskKind(taskData, AgileTaskKindUtil.TASK_KIND_ARTIFACT);
	}

	/**
	 * Creates a tuleap artifact POJO from the related task data.
	 * 
	 * @param taskData
	 *            The updated task data.
	 * @return The tuleap artifact POJO.
	 */
	public TuleapArtifact createTuleapArtifact(TaskData taskData) {
		TuleapConfigurableElementMapper tuleapConfigurableElementMapper = new TuleapConfigurableElementMapper(
				taskData, this.configuration);

		TuleapArtifact tuleapArtifact = null;
		if (taskData.isNew()) {
			int trackerId = tuleapConfigurableElementMapper.getConfigurationId();
			int projectId = tuleapConfigurableElementMapper.getProjectId();
			tuleapArtifact = new TuleapArtifact(trackerId, projectId);
		} else {
			int artifactId = tuleapConfigurableElementMapper.getId();
			int trackerId = tuleapConfigurableElementMapper.getConfigurationId();
			int projectId = tuleapConfigurableElementMapper.getProjectId();
			tuleapArtifact = new TuleapArtifact(artifactId, trackerId, projectId);
		}

		Set<AbstractFieldValue> fieldValues = tuleapConfigurableElementMapper.getFieldValues();
		for (AbstractFieldValue abstractFieldValue : fieldValues) {
			tuleapArtifact.addFieldValue(abstractFieldValue);
		}

		// New comment
		TaskAttribute newCommentTaskAttribute = taskData.getRoot().getAttribute(TaskAttribute.COMMENT_NEW);
		tuleapArtifact.setNewComment(newCommentTaskAttribute.getValue());

		return tuleapArtifact;
	}

}
