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
	protected boolean hasCardwall;

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
	 * @param hasCardwall
	 *            Flag indicating whether a cardwall is activated for this milestone type.
	 */
	public TuleapMilestoneType(int identifier, String url, String label, String itemName, String description,
			long lastUpdateDate, boolean hasCardwall) {
		super(identifier, url, label, itemName, description, lastUpdateDate);
		this.hasCardwall = hasCardwall;
	}

	/**
	 * Cardwall activation option.
	 * 
	 * @return the cardwall activation option
	 */
	public boolean hasCardwall() {
		return hasCardwall;
	}

}
