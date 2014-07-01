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
package org.tuleap.mylyn.task.ui.internal.wizards.newCard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.tuleap.mylyn.task.agile.core.ICardMapping;
import org.tuleap.mylyn.task.core.internal.data.TuleapTaskId;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapProject;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapTracker;
import org.tuleap.mylyn.task.core.internal.repository.TuleapCardMapping;
import org.tuleap.mylyn.task.ui.internal.wizards.TuleapPlanningTrackerPage;

/**
 * This wizard is used to create a new card.
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class NewCardWizard extends Wizard {

	/**
	 * The configuration of the project.
	 */
	private TuleapProject project;

	/**
	 * The identifier of the parent card.
	 */
	private String parentCard;

	/**
	 * The mapping to return in order to create the new card.
	 */
	private ICardMapping cardMapping;

	/**
	 * The page used to select a tracker.
	 */
	private TuleapPlanningTrackerPage trackerPage;

	/**
	 * The constructor.
	 *
	 * @param project
	 *            The configuration of the project.
	 * @param parentCardId
	 *            The identifier of the parent card.
	 */
	public NewCardWizard(TuleapProject project, String parentCardId) {
		this.project = project;
		this.parentCard = parentCardId;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		List<TuleapTracker> cardTrackers = new ArrayList<TuleapTracker>();
		int trackerId = TuleapTaskId.forName(parentCard).getTrackerId();
		for (TuleapTracker tracker : project.getAllTrackers()) {
			if (tracker.getParentTracker() != null) {
				if (tracker.getParentTracker().getId() == trackerId) {
					cardTrackers.add(tracker);
				}
			}
		}
		this.trackerPage = new TuleapPlanningTrackerPage(cardTrackers);
		this.addPage(this.trackerPage);
	}

	/**
	 * Returns the mapping used to create the new card.
	 *
	 * @return The mapping used to create the new card
	 */
	public ICardMapping getCardMapping() {
		return this.cardMapping;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		TuleapTracker tracker = this.trackerPage.getTrackerSelected();
		this.cardMapping = new TuleapCardMapping(tracker, this.parentCard);

		return tracker != null;
	}
}
