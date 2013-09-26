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
import org.tuleap.mylyn.task.internal.core.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskMapper;
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
	}

	/**
	 * Creates a tuleap artifact POJO from the related task data.
	 * 
	 * @param taskData
	 *            The updated task data.
	 * @return The tuleap artifact POJO.
	 */
	public TuleapArtifact createTuleapArtifact(TaskData taskData) {
		TuleapTaskMapper tuleapTaskMapper = new TuleapTaskMapper(taskData, this.configuration);

		int artifactId = tuleapTaskMapper.getArtifactId();
		int trackerId = tuleapTaskMapper.getTrackerId();

		// FIXME SBE This is completely useless but it is needed by the SOAP API§§§§!!!!!!
		TuleapArtifact tuleapArtifact = new TuleapArtifact(artifactId, trackerId, null, null, null, null,
				null);
		Set<AbstractFieldValue> fieldValues = tuleapTaskMapper.getFieldValues();
		for (AbstractFieldValue abstractFieldValue : fieldValues) {
			tuleapArtifact.addFieldValue(abstractFieldValue);
		}

		// New comment
		TaskAttribute newCommentTaskAttribute = taskData.getRoot().getAttribute(TaskAttribute.COMMENT_NEW);
		tuleapArtifact.setNewComment(newCommentTaskAttribute.getValue());

		return tuleapArtifact;
	}

}
