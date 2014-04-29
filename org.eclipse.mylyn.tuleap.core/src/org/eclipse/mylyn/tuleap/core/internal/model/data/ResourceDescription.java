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

import java.io.Serializable;

/**
 * Used to reference an authorized resource.
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class ResourceDescription implements Serializable {

	/**
	 * The key of content resource.
	 */
	public static final String CONTENT = "content"; //$NON-NLS-1$

	/**
	 * The key of backlog resource.
	 */
	public static final String BACKLOG = "backlog"; //$NON-NLS-1$

	/**
	 * The key of milestones resource.
	 */
	public static final String MILESTONES = "milestones"; //$NON-NLS-1$

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -8672107266911236886L;

	/**
	 * The resource URI.
	 */
	private String uri;

	/**
	 * The resource accepted tracker references.
	 */
	private Accept accept;

	/**
	 * Default constructor for JSON deserialization.
	 */
	public ResourceDescription() {
		// Default constructor for JSON deserialization
	}

	/**
	 * Resource URI getter.
	 *
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Resource URI setter.
	 *
	 * @param uri
	 *            the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * Resource accepted trackers getter.
	 *
	 * @return the accept
	 */
	public Accept getAccept() {
		return accept;
	}

	/**
	 * Resource accepted trackers setter.
	 *
	 * @param accept
	 *            the accept to set
	 */
	public void setAccept(Accept accept) {
		this.accept = accept;
	}

}
