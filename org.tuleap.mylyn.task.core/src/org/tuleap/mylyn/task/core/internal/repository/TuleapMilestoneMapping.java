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

import org.tuleap.mylyn.task.agile.core.IMilestoneMapping;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapTracker;

/**
 * The mapping used in order to create a new milestone.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapMilestoneMapping extends TuleapTaskMapping implements IMilestoneMapping {

	/**
	 * The parent milestone id.
	 */
	private String parentMilestoneId;

	/**
	 * The constructor.
	 *
	 * @param tracker
	 *            The tracker used by the milestone.
	 * @param parentMilestoneId
	 *            The parent milestone id
	 */
	public TuleapMilestoneMapping(TuleapTracker tracker, String parentMilestoneId) {
		super(tracker);
		this.parentMilestoneId = parentMilestoneId;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.agile.core.IMilestoneMapping#getParentMilestoneId()
	 */
	@Override
	public String getParentMilestoneId() {
		return this.parentMilestoneId;
	}

}
