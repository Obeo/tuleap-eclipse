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
package org.tuleap.mylyn.task.internal.core.model;

/**
 * Utility class used to store a comment.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapElementComment {
	/**
	 * The name of the person who wrote the comment.
	 */
	private String name;

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
	 * @param realName
	 *            The name of the person who has commented
	 * @param submittedOnTimestamp
	 *            The timestamp of the date when the comment has been submitted.
	 */
	public TuleapElementComment(String commentBody, String userEmail, String realName,
			int submittedOnTimestamp) {
		super();
		this.body = commentBody;
		this.email = userEmail;
		this.name = realName;
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
	 * Returns the name of the person who has commented.
	 * 
	 * @return The name of the person who has commented
	 */
	public String getName() {
		return this.name;
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
