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
	 * The default constructor.
	 * 
	 * @param backlogItemTypeIdentifier
	 *            The id of the tracker.
	 * @param repositoryURL
	 *            The URL of the repository.
	 */
	public TuleapBacklogItemType(int backlogItemTypeIdentifier, String repositoryURL) {
		super(backlogItemTypeIdentifier, repositoryURL);
	}

	/**
	 * The constructor.
	 * 
	 * @param repositoryURL
	 *            The URL of the repository.
	 * @param repositoryName
	 *            The name of the repository
	 * @param repositoryItemName
	 *            The item name of the repository
	 * @param repositoryDescription
	 *            The description of the repository
	 */
	public TuleapBacklogItemType(String repositoryURL, String repositoryName, String repositoryItemName,
			String repositoryDescription) {
		super(repositoryURL, repositoryName, repositoryItemName, repositoryDescription);
	}
}
