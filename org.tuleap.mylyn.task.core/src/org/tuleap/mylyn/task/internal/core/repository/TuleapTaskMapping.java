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
package org.tuleap.mylyn.task.internal.core.repository;

import org.eclipse.mylyn.tasks.core.TaskMapping;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapTrackerConfiguration;

/**
 * The Tuleap task mapping provides additional mapping compared to the regular TaskMapping.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapTaskMapping extends TaskMapping {
	/**
	 * Returns the project in which the task is created.
	 * 
	 * @return The project in which the task is created.
	 */
	public TuleapProjectConfiguration getProject() {
		return null;
	}

	/**
	 * Returns the tracker in which the task is created.
	 * 
	 * @return The tracker in which the task is created.
	 */
	public TuleapTrackerConfiguration getTracker() {
		return null;
	}
}
