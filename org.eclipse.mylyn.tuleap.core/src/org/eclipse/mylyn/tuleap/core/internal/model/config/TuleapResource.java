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
package org.eclipse.mylyn.tuleap.core.internal.model.config;

import java.io.Serializable;

/**
 * Used to reference a tuleap resource. POJO to deserialize a JSON resource.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapResource implements Serializable {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = 5608312823762685401L;

	/**
	 * The resource type.
	 */
	private String type;

	/**
	 * The resource REST URI.
	 */
	private String uri;

	/**
	 * Default constructor for JSON deserialization.
	 */
	public TuleapResource() {
		// Default constructor for JSON deserialization
	}

	/**
	 * Constructor for simplicity.
	 * 
	 * @param type
	 *            Type of the referenced element
	 * @param uri
	 *            URI of the referenced element
	 */
	public TuleapResource(String type, String uri) {
		this.type = type;
		this.uri = uri;
	}

	/**
	 * The resource type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * The resource type.
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * The resource REST URI.
	 * 
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * The resource REST URI.
	 * 
	 * @param uri
	 *            the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

}
