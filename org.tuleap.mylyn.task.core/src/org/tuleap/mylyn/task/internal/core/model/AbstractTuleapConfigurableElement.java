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

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.tuleap.mylyn.task.internal.core.data.AbstractFieldValue;

/**
 * Ancestor of all tuleap configurable elements that have and ID, a REST URL, and an HTML URL.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public abstract class AbstractTuleapConfigurableElement {

	/**
	 * The id of the element.
	 */
	private int id;

	/**
	 * The human-readable label of the element.
	 */
	private String label;

	/**
	 * The API URL of the element.
	 */
	private String url;

	/**
	 * The URL of the planning for web browsers.
	 */
	private String htmlUrl;

	/**
	 * The date of creation of the artifact.
	 */
	private Date creationDate;

	/**
	 * The date of the last modification of the artifact.
	 */
	private Date lastModificationDate;

	/**
	 * The milestone's configurable values.
	 */
	private Map<Integer, AbstractFieldValue> fieldTypeIdToValue = Maps.newHashMap();

	/**
	 * The new comment to send to the server.
	 */
	private String newComment;

	/**
	 * The comments of the element.
	 */
	private List<TuleapElementComment> comments = new ArrayList<TuleapElementComment>();

	/**
	 * This constructor is used for the creation of the configurable elements that have not been synchronized
	 * on the server yet.
	 */
	public AbstractTuleapConfigurableElement() {
		// do nothing
	}

	/**
	 * This constructor is used for the creation of the configurable elements that have been synchronized on
	 * the server. Those elements must have an identifier assigned by the server.
	 * 
	 * @param id
	 *            The identifier of the element
	 * @param label
	 *            The label of the element
	 * @param url
	 *            The API URL of the element
	 * @param htmlUrl
	 *            The URL of the element
	 * @param creationDate
	 *            The creation date of the element
	 * @param lastModificationDate
	 *            The last modification date of the element
	 */
	public AbstractTuleapConfigurableElement(int id, String label, String url, String htmlUrl,
			Date creationDate, Date lastModificationDate) {
		this.id = id;
		this.label = label;
		this.url = url;
		this.htmlUrl = htmlUrl;
		this.creationDate = creationDate;
		this.lastModificationDate = lastModificationDate;
	}

	/**
	 * id getter.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the API URL of the element.
	 * 
	 * @return The API URL of the element
	 */
	public String getUrl() {
		return this.url;
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
	 * Label getter.
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Returns the date of the creation.
	 * 
	 * @return The date of the creation
	 */
	public Date getCreationDate() {
		return this.creationDate;
	}

	/**
	 * Returns the date of the last modification.
	 * 
	 * @return The date of the last modification
	 */
	public Date getLastModificationDate() {
		return this.lastModificationDate;
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

	/**
	 * Adds a value.
	 * 
	 * @param value
	 *            The value to set.
	 */
	public void addFieldValue(AbstractFieldValue value) {
		fieldTypeIdToValue.put(Integer.valueOf(value.getFieldId()), value);
	}

	/**
	 * Get the value in relation to the given field id.
	 * 
	 * @param fieldTypeId
	 *            the field id.
	 * @return the value of the given field.
	 */
	public AbstractFieldValue getFieldValue(Integer fieldTypeId) {
		return fieldTypeIdToValue.get(fieldTypeId);
	}

	/**
	 * Returns the collection of the field values.
	 * 
	 * @return The collection of the field values
	 */
	public Collection<AbstractFieldValue> getFieldValues() {
		return this.fieldTypeIdToValue.values();
	}

}
