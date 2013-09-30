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

import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableFieldsConfiguration;

/**
 * The type of a milestone describes the configuration of milestones of this type.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapMilestoneType extends AbstractTuleapConfigurableFieldsConfiguration {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -8770699590955736583L;

	/**
	 * The cardwall URL, if and only if a cardwall is configured for this type of milestone.
	 */
	protected String cardwallUrl;

	/**
	 * The constructor.
	 * 
	 * @param identifier
	 *            The identifier of the configuration
	 * @param url
	 *            The URL.
	 * @param label
	 *            The label
	 * @param itemName
	 *            The item name
	 * @param description
	 *            The description
	 * @param lastUpdateDate
	 *            The date at which the configuration was last updated.
	 * @param cardwallUrl
	 *            The URL of the cardwall, if a cardwall is configured for this type of milestone.
	 */
	public TuleapMilestoneType(int identifier, String url, String label, String itemName, String description,
			long lastUpdateDate, String cardwallUrl) {
		super(identifier, url, label, itemName, description, lastUpdateDate);
	}

	/**
	 * Card wall URL getter.
	 * 
	 * @return the cardwallUrl
	 */
	public String getCardwallUrl() {
		return cardwallUrl;
	}

}
