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


/**
 * Used to reference a tuleap resource. POJO to deserialize a JSON tracker reference for example.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapReference {

	/**
	 * The tracker id.
	 */
	private int id;

	/**
	 * The tracker REST URI.
	 */
	private String uri;

	/**
	 * Default constructor for JSON deserialization.
	 */
	public TuleapReference() {
		// Default constructor for JSON deserialization
	}

	/**
	 * Constructor for simplicity.
	 * 
	 * @param id
	 *            Id of the referenced element
	 * @param uri
	 *            URI of the referenced element
	 */
	public TuleapReference(int id, String uri) {
		this.id = id;
		this.uri = uri;
	}

	/**
	 * The tracker id.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * The tracker id.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * The tracker REST URI.
	 * 
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * The tracker REST URI.
	 * 
	 * @param uri
	 *            the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

}
