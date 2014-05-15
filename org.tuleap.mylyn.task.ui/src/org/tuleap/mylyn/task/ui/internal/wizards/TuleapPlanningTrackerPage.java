/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.ui.internal.wizards;

import java.util.Collections;
import java.util.List;

import org.tuleap.mylyn.task.core.internal.model.config.TuleapTracker;

/**
 * This page will be used when a new task is created in an agile context (new sub-milestone, or new backlog
 * item).
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapPlanningTrackerPage extends AbstractTuleapTrackerPage {

	/**
	 * The trackers used.
	 */
	private final List<TuleapTracker> trackers;

	/**
	 * The constructor.
	 *
	 * @param trackers
	 *            the trackers to use.
	 */
	public TuleapPlanningTrackerPage(List<TuleapTracker> trackers) {
		super();
		this.trackers = Collections.unmodifiableList(trackers);
	}

	/**
	 * Retrieve the trackers given in the constructor, as an unmodifiable list..
	 *
	 * @return An unmodifiable view of theof trackers given in the constructor.
	 */
	@Override
	protected List<TuleapTracker> getTrackers() {
		return trackers;
	}
}
