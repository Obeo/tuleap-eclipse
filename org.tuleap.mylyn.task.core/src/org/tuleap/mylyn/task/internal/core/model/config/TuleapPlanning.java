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

import java.io.Serializable;

import org.tuleap.mylyn.task.internal.core.model.data.AbstractTuleapProjectElement;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;

/**
 * A Planning in Tuleap. It is used to configure milestones and backlog items, indicating which trackers are
 * used to represent milestones and which trackers are used to represent backlog items assignable to each kind
 * of milestone. A tracker used for milestones can be thought of as a "kind of milestone".
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapPlanning extends AbstractTuleapProjectElement<Integer> implements Serializable {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = 5106404307922739802L;

	/**
	 * Reference to the tracker that contains the milestone artifacts of this planning.
	 */
	private TuleapReference milestoneTracker;

	/**
	 * References to the trackers that contain the backlog item artifacts of this planning.
	 */
	private TuleapReference[] backlogTrackers;

	/**
	 * REST URI to retrieve the milestones of this planning. They are the milestones of the artifacts of the
	 * milestone tracker of this planning.
	 */
	private String milestonesUri;

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
		super(Integer.valueOf(id), projectRef);
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
		super(Integer.valueOf(id), projectRef, label, uri);
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
	public TuleapReference[] getBacklogTrackers() {
		return backlogTrackers;
	}

	/**
	 * Backlog tracker reference setter.
	 * 
	 * @param backlogTrackers
	 *            the backlogTrackers to set
	 */
	public void setBacklogTrackers(TuleapReference[] backlogTrackers) {
		this.backlogTrackers = backlogTrackers;
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
	 * Milestones URI getter.
	 * 
	 * @return the milestonesUri
	 */
	public String getMilestonesUri() {
		return milestonesUri;
	}

	/**
	 * Milestones URI setter.
	 * 
	 * @param milestonesUri
	 *            the milestonesUri to set
	 */
	public void setMilestonesUri(String milestonesUri) {
		this.milestonesUri = milestonesUri;
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
