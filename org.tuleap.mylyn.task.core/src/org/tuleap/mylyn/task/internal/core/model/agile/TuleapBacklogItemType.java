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
 * The type of a backlog item describes the configuration of backlog items of this type.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapBacklogItemType extends AbstractTuleapConfigurableFieldsConfiguration {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = 5142346531052227378L;

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
	 */
	public TuleapBacklogItemType(int identifier, String url, String label, String itemName,
			String description, long lastUpdateDate) {
		super(identifier, url, label, itemName, description, lastUpdateDate);
	}
}
