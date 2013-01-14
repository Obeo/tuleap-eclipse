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
package org.eclipse.mylyn.internal.tuleap.core.model;

/**
 * Utility class used to store a comment on a Tuleap artifact.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapArtifactComment {
	/**
	 * The id of the user who has submitted the comment.
	 */
	private int submittedBy;

	/**
	 * The email of the user who has submitted the comment.
	 */
	private String email;

	/**
	 * The timestamp of the date when the comment was submitted.
	 */
	private int submittedOn;

	/**
	 * The body of the comment.
	 */
	private String body;

	/**
	 * The constructor.
	 * 
	 * @param commentBody
	 *            The body of the comment.
	 * @param userEmail
	 *            The email of the user who wrote the comment.
	 * @param submittedById
	 *            The id of the user who wrote the comment.
	 * @param submittedOnTimestamp
	 *            The timestamp of the date when the comment has been submitted.
	 */
	public TuleapArtifactComment(String commentBody, String userEmail, int submittedById,
			int submittedOnTimestamp) {
		super();
		this.body = commentBody;
		this.email = userEmail;
		this.submittedBy = submittedById;
		this.submittedOn = submittedOnTimestamp;
	}

	/**
	 * Returns the body of the comment.
	 * 
	 * @return The body of the comment.
	 */
	public String getBody() {
		return this.body;
	}

	/**
	 * Returns the email of the user who wrote the comment.
	 * 
	 * @return The email of the user who wrote the comment.
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Returns the id of the user who wrote the comment.
	 * 
	 * @return The id of the user who wrote the comment.
	 */
	public int getSubmittedBy() {
		return this.submittedBy;
	}

	/**
	 * Returns the timestamp of the date when the comment has been submitted.
	 * 
	 * @return The timestamp of the date when the comment has been submitted.
	 */
	public int getSubmittedOn() {
		return this.submittedOn;
	}
}
