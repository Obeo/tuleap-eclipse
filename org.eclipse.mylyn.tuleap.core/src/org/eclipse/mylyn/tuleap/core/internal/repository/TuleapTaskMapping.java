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
package org.eclipse.mylyn.tuleap.core.internal.repository;

import org.eclipse.mylyn.tasks.core.TaskMapping;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker;

/**
 * The Tuleap task mapping provides additional mapping compared to the regular TaskMapping.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapTaskMapping extends TaskMapping {

	/**
	 * The tracker of the element.
	 */
	private TuleapTracker tracker;

	/**
	 * The constructor.
	 * 
	 * @param tracker
	 *            The tracker of the element
	 */
	public TuleapTaskMapping(TuleapTracker tracker) {
		this.tracker = tracker;
	}

	/**
	 * Returns the tracker in which the task is created.
	 * 
	 * @return The tracker in which the task is created.
	 */
	public TuleapTracker getTracker() {
		return this.tracker;
	}
}
