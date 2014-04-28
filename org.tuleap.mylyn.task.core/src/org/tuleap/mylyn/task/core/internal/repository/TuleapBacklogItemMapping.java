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
package org.tuleap.mylyn.task.core.internal.repository;

import org.tuleap.mylyn.task.agile.core.IBacklogItemMapping;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapTracker;

/**
 * The mapping used in order to create a new backlogItem.
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapBacklogItemMapping extends TuleapTaskMapping implements IBacklogItemMapping {

	/**
	 * The parent milestone id.
	 */
	private String parentMilestoneId;

	/**
	 * The constructor.
	 *
	 * @param tracker
	 *            The tracker used by the backlogItem.
	 * @param parentMilestoneId
	 *            The parent milestone id
	 */
	public TuleapBacklogItemMapping(TuleapTracker tracker, String parentMilestoneId) {
		super(tracker);
		this.parentMilestoneId = parentMilestoneId;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.agile.core.IBacklogItemMapping#getParentMilestoneId()
	 */
	@Override
	public String getParentMilestoneId() {
		return this.parentMilestoneId;
	}

}
