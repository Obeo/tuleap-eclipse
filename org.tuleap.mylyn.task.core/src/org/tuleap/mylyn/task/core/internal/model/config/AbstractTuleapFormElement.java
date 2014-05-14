/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.core.internal.model.config;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * The common root of all Tuleap form elements.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public abstract class AbstractTuleapFormElement implements Serializable {

	/**
	 * Indicates that the user can read the field.
	 */
	public static final String PERMISSION_READ = "read"; //$NON-NLS-1$

	/**
	 * Indicates that the user can update the field.
	 */
	public static final String PERMISSION_UPDATE = "update"; //$NON-NLS-1$

	/**
	 * Indicates that the user can submit a newly created artifact with the field set.
	 */
	public static final String PERMISSION_SUBMIT = "submit"; //$NON-NLS-1$

	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = -2238564479184287075L;

	/**
	 * The name of the form element.
	 */
	private String name;

	/**
	 * The label of the form element.
	 */
	private String label;

	/**
	 * The description of the form element.
	 */
	private String description;

	/**
	 * Indicates if the form element is required or not.
	 */
	private boolean required;

	/**
	 * The identifier of the form element.
	 */
	private int identifier;

	/**
	 * The rank of the form element.
	 */
	private int rank;

	/**
	 * Indicates that the field is readable.
	 */
	private boolean readable;

	/**
	 * Indicates that the field is submitable.
	 */
	private boolean submitable;

	/**
	 * Indicates that the field is updatable.
	 */
	private boolean updatable;

	/**
	 * The constructor.
	 *
	 * @param formElementIdentifier
	 *            The identifier of the form element
	 */
	public AbstractTuleapFormElement(int formElementIdentifier) {
		super();
		this.identifier = formElementIdentifier;
	}

	/**
	 * Returns the name of the form element.
	 *
	 * @return The name of the form element.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets a new name for the form element.
	 *
	 * @param formElementName
	 *            The new name to set
	 */
	public void setName(String formElementName) {
		this.name = formElementName;
	}

	/**
	 * Returns the label of the form element.
	 *
	 * @return The label of the form element.
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Sets a new label for the form element.
	 *
	 * @param formElementLabel
	 *            The new label to set
	 */
	public void setLabel(String formElementLabel) {
		this.label = formElementLabel;
	}

	/**
	 * Returns the identifier of the form element.
	 *
	 * @return The identifier of the form element.
	 */
	public int getIdentifier() {
		return this.identifier;
	}

	/**
	 * Returns the description of the form element.
	 *
	 * @return The description of the form element.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets a new description for the form element.
	 *
	 * @param formElementDescription
	 *            The new description to set
	 */
	public void setDescription(String formElementDescription) {
		this.description = formElementDescription;
	}

	/**
	 * Returns <code>true</code> if the form element is required, <code>false</code> otherwise.
	 *
	 * @return <code>true</code> if the form element is required, <code>false</code> otherwise.
	 */
	public boolean isRequired() {
		return this.required;
	}

	/**
	 * Sets the requirement of the form element.
	 *
	 * @param isFormElementRequired
	 *            The requirement to set
	 */
	public void setRequired(boolean isFormElementRequired) {
		this.required = isFormElementRequired;
	}

	/**
	 * Sets the permission on the element.
	 *
	 * @param permissions
	 *            The array of permissions containing values among "read", "submit", "write".
	 */
	public void setPermissions(String[] permissions) {
		List<String> list = Arrays.asList(permissions);
		this.readable = list.contains(PERMISSION_READ);
		this.submitable = list.contains(PERMISSION_SUBMIT);
		this.updatable = list.contains(PERMISSION_UPDATE);
	}

	/**
	 * Returns <code>true</code> if this field can be read, <code>false</code> otherwise.
	 *
	 * @return <code>true</code> if this field can be read, <code>false</code> otherwise.
	 */
	public boolean isReadable() {
		return this.readable;
	}

	/**
	 * Returns <code>true</code> if this field can be updated, <code>false</code> otherwise.
	 *
	 * @return <code>true</code> if this field can be updated, <code>false</code> otherwise.
	 */
	public boolean isUpdatable() {
		return this.updatable;
	}

	/**
	 * Returns <code>true</code> if this field can be submitted, <code>false</code> otherwise.
	 *
	 * @return <code>true</code> if this field can be submitted, <code>false</code> otherwise.
	 */
	public boolean isSubmitable() {
		return this.submitable;
	}

	/**
	 * Sets the rank of the form element.
	 *
	 * @param formElementRank
	 *            The rank to set
	 */
	public void setRank(int formElementRank) {
		this.rank = formElementRank;
	}

	/**
	 * Returns the rank of the form element.
	 *
	 * @return The rank of the form element.
	 */
	public int getRank() {
		return this.rank;
	}

}
