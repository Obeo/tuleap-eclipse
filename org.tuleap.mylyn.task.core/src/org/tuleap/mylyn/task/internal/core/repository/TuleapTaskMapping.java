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
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTracker;

/**
 * The Tuleap task mapping provides additional mapping compared to the regular TaskMapping.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapTaskMapping extends TaskMapping {

	/**
	 * The configuration of the element.
	 */
	private TuleapTracker tracker;

	/**
	 * The constructor.
	 * 
	 * @param tracker
	 *            The configuration of the element
	 */
	public TuleapTaskMapping(TuleapTracker tracker) {
		this.tracker = tracker;
	}

	/**
	 * Returns the configuration in which the task is created.
	 * 
	 * @return The configuration in which the task is created.
	 */
	public TuleapTracker getTracker() {
		return this.tracker;
	}
}
