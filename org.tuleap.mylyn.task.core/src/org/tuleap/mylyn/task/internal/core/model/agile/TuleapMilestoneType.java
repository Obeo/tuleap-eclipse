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
	 * The default constructor.
	 * 
	 * @param trackerIdentifier
	 *            The id of the tracker.
	 * @param repositoryURL
	 *            The URL of the repository.
	 */
	public TuleapMilestoneType(int trackerIdentifier, String repositoryURL) {
		super(trackerIdentifier, repositoryURL);
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
	public TuleapMilestoneType(String repositoryURL, String repositoryName, String repositoryItemName,
			String repositoryDescription) {
		super(repositoryURL, repositoryName, repositoryItemName, repositoryDescription);
	}
}
