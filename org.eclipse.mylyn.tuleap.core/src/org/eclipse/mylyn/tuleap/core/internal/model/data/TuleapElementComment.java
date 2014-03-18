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
package org.eclipse.mylyn.tuleap.core.internal.model.data;

import java.util.Date;

import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapUser;

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
	 * The date when the comment was submitted.
	 */
	private Date submittedOn;

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
	 * @param submittedOn
	 *            The date when the comment has been submitted.
	 */
	public TuleapElementComment(String commentBody, TuleapUser submitter, Date submittedOn) {
		super();
		this.body = commentBody;
		this.submitter = submitter;
		this.submittedOn = submittedOn;
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
	 * Returns the date when the comment has been submitted.
	 * 
	 * @return The date when the comment has been submitted.
	 */
	public Date getSubmittedOn() {
		return this.submittedOn;
	}

	/**
	 * Set the person who has submitted the comment.
	 * 
	 * @param tuleapUser
	 *            the user to set
	 */
	public void setSubmitter(TuleapUser tuleapUser) {
		this.submitter = tuleapUser;
	}
}
