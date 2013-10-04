/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.internal.core.model.tracker;

import java.util.Date;

import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableElement;

/**
 * This class represents a Tuleap artifact obtained from the Tuleap tracker.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapArtifact extends AbstractTuleapConfigurableElement {

	/**
	 * The default constructor used to create a new Tuleap artifact locally.
	 * 
	 * @param trackerId
	 *            The identifier of the tracker.
	 * @param projectId
	 *            The identifier of the project
	 */
	public TuleapArtifact(int trackerId, int projectId) {
		super(trackerId, projectId);
	}

	/**
	 * The default constructor used to update an existing artifact.
	 * 
	 * @param artifactId
	 *            The identifier of the artifact
	 * @param trackerId
	 *            The identifier of the tracker
	 * @param projectId
	 *            The identifier of the project
	 */
	public TuleapArtifact(int artifactId, int trackerId, int projectId) {
		super(artifactId, trackerId, projectId);
	}

	/**
	 * The constructor used to create a Tuleap artifact with the artifact ID computed from a Tuleap tracker.
	 * 
	 * @param artifactId
	 *            The Tuleap artifact ID
	 * @param trackerId
	 *            The id of the tracker
	 * @param projectId
	 *            The identifier of the project
	 * @param label
	 *            The label of the artifact
	 * @param url
	 *            The API URL of the artifact
	 * @param htmlUrl
	 *            The URL of the artifact
	 * @param creationDate
	 *            The creation date of the artifact
	 * @param lastModificationDate
	 *            The last modification date of the artifact
	 */
	// CHECKSTYLE:OFF
	public TuleapArtifact(int artifactId, int trackerId, int projectId, String label, String url,
			String htmlUrl, Date creationDate, Date lastModificationDate) {
		super(artifactId, trackerId, projectId, label, url, htmlUrl, creationDate, lastModificationDate);
	}
	// CHECKSTYLE:ON
}
