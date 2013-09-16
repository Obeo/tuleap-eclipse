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
package org.tuleap.mylyn.task.internal.core.model.agile;

import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapFieldContainerConfig;

/**
 * The type of a milestone describes the configuration of milestones of this type.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapMilestoneType extends AbstractTuleapFieldContainerConfig {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -8770699590955736583L;

	/**
	 * The cardwall URL, if and only if a cardwall is configured for this type of milestone.
	 */
	protected String cardwallUrl;

	/**
	 * The default constructor.
	 * 
	 * @param milestoneTypeIdentifier
	 *            The id of the tracker.
	 * @param repositoryURL
	 *            The URL of the repository.
	 */
	public TuleapMilestoneType(int milestoneTypeIdentifier, String repositoryURL) {
		super(milestoneTypeIdentifier, repositoryURL);
	}

	/**
	 * The constructor.
	 * 
	 * @param repositoryURL
	 *            The URL of the repository.
	 * @param repositoryName
	 *            The label of the repository
	 * @param repositoryItemName
	 *            The item label of the repository
	 * @param repositoryDescription
	 *            The description of the repository
	 */
	public TuleapMilestoneType(String repositoryURL, String repositoryName, String repositoryItemName,
			String repositoryDescription) {
		super(repositoryURL, repositoryName, repositoryItemName, repositoryDescription);
	}

	/**
	 * Card wall URL getter.
	 * 
	 * @return the cardwallUrl
	 */
	public String getCardwallUrl() {
		return cardwallUrl;
	}

	/**
	 * Card wall URL setter.
	 * 
	 * @param cardwallUrl
	 *            the cardwallUrl to set
	 */
	public void setCardwallUrl(String cardwallUrl) {
		this.cardwallUrl = cardwallUrl;
	}
}
