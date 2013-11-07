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

import java.util.Date;

/**
 * Ancestor of all tuleap elements that contain a HTML URL, and the submitted by, submitted on, and last
 * updated on data.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractTuleapDetailedElement extends AbstractTuleapProjectElement {

	/**
	 * The URL of the planning for web browsers.
	 */
	private String htmlUrl;

	/**
	 * The id of the submitter.
	 */
	private int submittedBy;

	/**
	 * The date of creation.
	 */
	private Date submittedOn;

	/**
	 * The date of the last modification.
	 */
	private Date lastUpdatedOn;

	/**
	 * Default constructor for deserialization.
	 */
	public AbstractTuleapDetailedElement() {
		// Default constructor for deserialization
	}

	/**
	 * This constructor is used for the creation of the configurable elements that have not been synchronized
	 * on the server yet. We need the identifier of the configuration to know the details of the artifact to
	 * update.
	 * 
	 * @param projectRef
	 *            The reference to the project
	 */
	public AbstractTuleapDetailedElement(TuleapReference projectRef) {
		super(projectRef);
	}

	/**
	 * This constructor is used for the update of an existing element. We know the identifier of the element
	 * and the identifier of its configuration.
	 * 
	 * @param id
	 *            The identifier of the element
	 * @param projectRef
	 *            The reference to the project
	 */
	public AbstractTuleapDetailedElement(int id, TuleapReference projectRef) {
		super(id, projectRef);
	}

	/**
	 * This constructor is used for the creation of the configurable elements that have been synchronized on
	 * the server. Those elements must have an identifier assigned by the server.
	 * 
	 * @param id
	 *            The identifier of the element
	 * @param projectRef
	 *            The reference to the project
	 * @param label
	 *            The label of the element
	 * @param uri
	 *            The REST URI of the element
	 * @param htmlUrl
	 *            The URL of the element
	 * @param creationDate
	 *            The creation date of the element
	 * @param lastModificationDate
	 *            The last modification date of the element
	 */
	public AbstractTuleapDetailedElement(int id, TuleapReference projectRef, String label, String uri,
			String htmlUrl, Date creationDate, Date lastModificationDate) {
		super(id, projectRef, label, uri);
		this.htmlUrl = htmlUrl;
		this.submittedOn = creationDate;
		this.lastUpdatedOn = lastModificationDate;
	}

	/**
	 * Html URL getter.
	 * 
	 * @return the htmlUrl
	 */
	public String getHtmlUrl() {
		return htmlUrl;
	}

	/**
	 * Html URL setter.
	 * 
	 * @param htmlUrl
	 *            the htmlUrl to set
	 */
	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}

	/**
	 * creation date.
	 * 
	 * @return the submittedOn
	 */
	public Date getSubmittedOn() {
		return submittedOn;
	}

	/**
	 * creation date.
	 * 
	 * @param submittedOn
	 *            the submittedOn to set
	 */
	public void setSubmittedOn(Date submittedOn) {
		this.submittedOn = submittedOn;
	}

	/**
	 * last update.
	 * 
	 * @return the lastUpdatedOn
	 */
	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	/**
	 * last update.
	 * 
	 * @param lastUpdatedOn
	 *            the lastUpdatedOn to set
	 */
	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	/**
	 * Submitter getter.
	 * 
	 * @return the submittedBy
	 */
	public int getSubmittedBy() {
		return submittedBy;
	}

	/**
	 * Submitter setter.
	 * 
	 * @param submittedBy
	 *            submitter id
	 */
	public void setSubmittedBy(int submittedBy) {
		this.submittedBy = submittedBy;
	}

}