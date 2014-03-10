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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class represents a Tuleap artifact obtained from the Tuleap tracker.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapArtifact extends AbstractTuleapConfigurableElement<Integer> {

	/**
	 * The serialization version id.
	 */
	private static final long serialVersionUID = 1272343379964391267L;

	/**
	 * The comments of the element.
	 */
	private List<TuleapElementComment> comments = new ArrayList<TuleapElementComment>();

	/**
	 * Default constructor for deserialization.
	 */
	public TuleapArtifact() {
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
	public TuleapArtifact(TuleapReference trackerRef, TuleapReference projectRef) {
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
	public TuleapArtifact(int id, TuleapReference trackerRef, TuleapReference projectRef) {
		super(Integer.valueOf(id), trackerRef, projectRef);
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
	public TuleapArtifact(int id, TuleapReference projectRef, String label, String url, String htmlUrl,
			Date creationDate, Date lastModificationDate) {
		super(Integer.valueOf(id), projectRef, label, url, htmlUrl, creationDate, lastModificationDate);
	}

	/**
	 * Adds a comment.
	 * 
	 * @param artifactComment
	 *            The comment to add.
	 */
	public void addComment(TuleapElementComment artifactComment) {
		this.comments.add(artifactComment);
	}

	/**
	 * Returns the comments.
	 * 
	 * @return the comments
	 */
	public List<TuleapElementComment> getComments() {
		return this.comments;
	}
}
