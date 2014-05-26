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

import org.tuleap.mylyn.task.agile.core.ICardMapping;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapTracker;

/**
 * The mapping used in order to create a new card.
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapCardMapping extends TuleapTaskMapping implements ICardMapping {

	/**
	 * The parent card id.
	 */
	private String parentCard;

	/**
	 * The constructor.
	 *
	 * @param tracker
	 *            The tracker used by the card.
	 * @param parentCardId
	 *            The parent card id
	 */
	public TuleapCardMapping(TuleapTracker tracker, String parentCardId) {
		super(tracker);
		this.parentCard = parentCardId;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.agile.core.ICardMapping#getParentCard()
	 */
	@Override
	public String getParentCard() {
		return this.parentCard;
	}

}
