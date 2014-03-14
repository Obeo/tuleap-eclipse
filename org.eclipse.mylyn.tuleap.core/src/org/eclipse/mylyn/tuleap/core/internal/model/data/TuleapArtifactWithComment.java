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
package org.eclipse.mylyn.tuleap.core.internal.model.data;

import java.util.Date;

/**
 * This class represents a Tuleap artifact obtained from the Tuleap tracker.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapArtifactWithComment extends TuleapArtifact {

	/**
	 * The serialization version id.
	 */
	private static final long serialVersionUID = 6324467559744016479L;

	/**
	 * The new comment to send to the server.
	 */
	private String newComment;

	/**
	 * Default constructor for deserialization.
	 */
	public TuleapArtifactWithComment() {
		// Default constructor for deserialization
	}

	/**
	 * The default constructor used to create a new Tuleap artifact locally.
	 * 
	 * @param trackerRef
	 *            The tracker.
	 * @param projectRef
	 *            The project
	 */
	public TuleapArtifactWithComment(TuleapReference trackerRef, TuleapReference projectRef) {
		super(trackerRef, projectRef);
	}

	/**
	 * The default constructor used to update an existing artifact.
	 * 
	 * @param id
	 *            The identifier of the artifact
	 * @param trackerRef
	 *            The tracker
	 * @param projectRef
	 *            The project
	 */
	public TuleapArtifactWithComment(int id, TuleapReference trackerRef, TuleapReference projectRef) {
		super(id, trackerRef, projectRef);
	}

	/**
	 * The constructor used to create a Tuleap artifact with the artifact ID computed from a Tuleap tracker.
	 * 
	 * @param id
	 *            The Tuleap artifact ID
	 * @param projectRef
	 *            The project
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
	public TuleapArtifactWithComment(int id, TuleapReference projectRef, String label, String url, String htmlUrl,
			Date creationDate, Date lastModificationDate) {
		super(id, projectRef, label, url, htmlUrl, creationDate, lastModificationDate);
	}

	/**
	 * Sets the new comment.
	 * 
	 * @param newComment
	 *            The new comment
	 */
	public void setNewComment(String newComment) {
		this.newComment = newComment;
	}

	/**
	 * Returns the new comment to send to the server.
	 * 
	 * @return The new comment to send to the server.
	 */
	public String getNewComment() {
		return this.newComment;
	}
}
