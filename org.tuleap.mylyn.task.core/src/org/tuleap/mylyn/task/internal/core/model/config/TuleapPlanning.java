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
package org.tuleap.mylyn.task.internal.core.model.config;

import com.google.common.collect.Lists;

import java.util.List;

import org.tuleap.mylyn.task.internal.core.model.data.AbstractTuleapProjectElement;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;

/**
 * Configuration of a Planning in Tuleap.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapPlanning extends AbstractTuleapProjectElement {

	/**
	 * Reference to the tracker that contains the milestone artifacts of this planning.
	 */
	private TuleapReference milestoneTracker;

	/**
	 * References to the trackers that contain the backlog item artifacts of this planning.
	 */
	private List<TuleapReference> backlogTrackers;

	/**
	 * REST URI of the cardwall configuration.
	 */
	private String cardwallConfigurationUri;

	/**
	 * Default constructor for deserialization.
	 */
	public TuleapPlanning() {
		// Default constructor for deserialization.
	}

	/**
	 * Constructor used to update an existing element.
	 * 
	 * @param id
	 *            The id
	 * @param projectRef
	 *            the project
	 */
	public TuleapPlanning(int id, TuleapReference projectRef) {
		super(id, projectRef);
	}

	/**
	 * Constructor used to create elements.
	 * 
	 * @param id
	 *            The id
	 * @param projectRef
	 *            the project
	 * @param label
	 *            The label
	 * @param uri
	 *            the uri
	 */
	public TuleapPlanning(int id, TuleapReference projectRef, String label, String uri) {
		super(id, projectRef, label, uri);
	}

	/**
	 * Milestone tracker reference getter.
	 * 
	 * @return the milestoneTracker
	 */
	public TuleapReference getMilestoneTracker() {
		return milestoneTracker;
	}

	/**
	 * Milestone tracker reference setter.
	 * 
	 * @param milestoneTracker
	 *            the milestoneTracker to set
	 */
	public void setMilestoneTracker(TuleapReference milestoneTracker) {
		this.milestoneTracker = milestoneTracker;
	}

	/**
	 * Backlog tracker reference getter.
	 * 
	 * @return the backlogTrackers, a list that is never <code>null</code> but possibly empty.
	 */
	public List<TuleapReference> getBacklogTrackers() {
		if (backlogTrackers == null) {
			backlogTrackers = Lists.newArrayList();
		}
		return backlogTrackers;
	}

	/**
	 * Adds a backlog tracker reference.
	 * 
	 * @param backlogTracker
	 *            the backlogTracker to add
	 */
	public void addBacklogTracker(TuleapReference backlogTracker) {
		getBacklogTrackers().add(backlogTracker);
	}

	/**
	 * Cardwall config URI.
	 * 
	 * @return the cardwallConfigurationUri
	 */
	public String getCardwallConfigurationUri() {
		return cardwallConfigurationUri;
	}

	/**
	 * Cardwall config URI.
	 * 
	 * @param cardwallConfigurationUri
	 *            the cardwallConfigurationUri to set
	 */
	public void setCardwallConfigurationUri(String cardwallConfigurationUri) {
		this.cardwallConfigurationUri = cardwallConfigurationUri;
	}

	/**
	 * Flag to indicate if it has a cardwall.
	 * 
	 * @return the cardwallActive
	 */
	public boolean isCardwallActive() {
		return cardwallConfigurationUri != null;
	}

}
