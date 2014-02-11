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
package org.tuleap.mylyn.task.internal.core.model.data;

import org.tuleap.mylyn.task.internal.core.model.config.TuleapUser;

/**
 * Utility class used to store a comment.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapElementComment {
	/**
	 * The person who submitted the comment.
	 */
	private TuleapUser submitter;

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
	 * @param submitter
	 *            The user who submitted the comment.
	 * @param submittedOnTimestamp
	 *            The timestamp of the date when the comment has been submitted.
	 */
	public TuleapElementComment(String commentBody, TuleapUser submitter, int submittedOnTimestamp) {
		super();
		this.body = commentBody;
		this.submitter = submitter;
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
	 * Returns the person who has submitted the comment.
	 * 
	 * @return The person who has submitted the comment.
	 */
	public TuleapUser getSubmitter() {
		return this.submitter;
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
