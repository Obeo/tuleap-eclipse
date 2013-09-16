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

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Ancestor of all tuleap agile elements that have and ID, a REST URL, and an HTML URL.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public abstract class AbstractTuleapAgileElement {

	/**
	 * The id of the planning.
	 */
	protected int id;

	/**
	 * The human-readable label of the element.
	 */
	protected String label;

	/**
	 * The planning Rest URL.
	 */
	protected String url;

	/**
	 * The URL of the artifact that is used by Tuleap to persist the planning.
	 */
	// private String artifactUrl;

	/**
	 * The URL of the planning for web browsers.
	 */
	protected String htmlUrl;

	/**
	 * The milestone's configurable values.
	 */
	private Map<Integer, Object> fieldTypeIdToValue = Maps.newHashMap();

	/**
	 * id getter.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * URL getter.
	 * 
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * URL setter.
	 * 
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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
	 * id setter.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
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
	 * Label setter.
	 * 
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Add a value to the field identified by the given field id.
	 * 
	 * @param fieldTypeId
	 *            The field id.
	 * @param value
	 *            The value to set.
	 */
	public void addFieldValue(Integer fieldTypeId, Object value) {
		fieldTypeIdToValue.put(fieldTypeId, value);
	}

	/**
	 * Get the value in relation to the given field id.
	 * 
	 * @param fieldTypeId
	 *            the field id.
	 * @return the value of the given field.
	 */
	public Object getValue(Integer fieldTypeId) {
		return fieldTypeIdToValue.get(fieldTypeId);
	}

	/**
	 * Get the configurable values.
	 * 
	 * @return The values.
	 */
	public Map<Integer, Object> getValues() {
		return fieldTypeIdToValue;
	}

}
