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
package org.tuleap.mylyn.task.core.internal.model.data;

import java.io.Serializable;

/**
 * Ancestor of all tuleap configurable elements that belong to a project and have and ID, a REST URL, and an
 * HTML URL.
 * 
 * @param <T>
 *            The type of the element ID
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public abstract class AbstractTuleapProjectElement<T> implements Serializable {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -9094875261952671674L;

	/**
	 * The id of the element.
	 */
	private T id;

	/**
	 * The human-readable label of the element.
	 */
	private String label;

	/**
	 * The REST URI of the element.
	 */
	private String uri;

	/**
	 * Reference to the project that contains this element.
	 */
	private TuleapReference project;

	/**
	 * Default constructor for JSON deserialization.
	 */
	public AbstractTuleapProjectElement() {
		// Default constructor for JSON deserialization.
	}

	/**
	 * This constructor is used for the creation of the configurable elements that have not been synchronized
	 * on the server yet. We need the identifier of the project to know the details of the artifact to update.
	 * 
	 * @param project
	 *            The reference to the project
	 */
	public AbstractTuleapProjectElement(TuleapReference project) {
		this.project = project;
	}

	/**
	 * This constructor is used for the update of an existing element. We know the identifier of the element
	 * and the identifier of its project.
	 * 
	 * @param id
	 *            The identifier of the element
	 * @param project
	 *            The reference to the project
	 */
	public AbstractTuleapProjectElement(T id, TuleapReference project) {
		this(project);
		this.id = id;
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
	 */
	public AbstractTuleapProjectElement(T id, TuleapReference projectRef, String label, String uri) {
		this(id, projectRef);
		this.label = label;
		this.uri = uri;
	}

	/**
	 * id getter.
	 * 
	 * @return the id
	 */
	public T getId() {
		return id;
	}

	/**
	 * ID setter.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(T id) {
		this.id = id;
	}

	/**
	 * projectRef getter.
	 * 
	 * @return the project reference.
	 */
	public TuleapReference getProject() {
		return project;
	}

	/**
	 * projectRef getter.
	 * 
	 * @param project
	 *            the project reference.
	 */
	public void setProject(TuleapReference project) {
		this.project = project;
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
	 *            the label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * URI getter.
	 * 
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * URI setter.
	 * 
	 * @param uri
	 *            the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

}
