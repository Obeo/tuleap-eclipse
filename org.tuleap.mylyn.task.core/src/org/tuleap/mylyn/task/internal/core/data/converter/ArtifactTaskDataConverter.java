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

import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapArtifact;

/**
 * Utility class used to transform a {@link TuleapArtifact} into a {@link TaskData} and vice versa.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class ArtifactTaskDataConverter extends AbstractElementTaskDataConverter<TuleapArtifact> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.data.converter.AbstractElementTaskDataConverter#populateTaskData(org.eclipse.mylyn.tasks.core.data.TaskData,
	 *      org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableElement)
	 */
	@Override
	public void populateTaskData(TaskData taskData, TuleapArtifact element) {
		// do stuff
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
