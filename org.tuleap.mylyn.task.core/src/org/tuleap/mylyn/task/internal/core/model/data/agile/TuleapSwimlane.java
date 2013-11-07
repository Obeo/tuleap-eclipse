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
package org.tuleap.mylyn.task.internal.core.model.data.agile;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * The cardwall swimlane.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapSwimlane {
	/**
	 * Swimlane backlog Item.
	 */
	private TuleapBacklogItem backlogItem;

	/**
	 * Swimlane cards.
	 */
	private List<TuleapCard> cards = Lists.newArrayList();

	/**
	 * Get the swimlane backlogItem.
	 * 
	 * @return the backlogItem
	 */
	public TuleapBacklogItem getBacklogItem() {
		return this.backlogItem;
	}

	/**
	 * Set the swimlane backlogItem.
	 * 
	 * @param backlogItem
	 *            The backlogItem
	 */
	public void setBacklogItem(TuleapBacklogItem backlogItem) {
		this.backlogItem = backlogItem;
	}

	/**
	 * Add a card to the swimlane.
	 * 
	 * @param card
	 *            The card to add.
	 */
	public void addCard(TuleapCard card) {
		cards.add(card);
	}

	/**
	 * Provides the cards of this swimlane.
	 * 
	 * @return An unmodifiable list view of the cards of this swimlane.
	 */
	public List<TuleapCard> getCards() {
		return ImmutableList.copyOf(cards);
	}
}
