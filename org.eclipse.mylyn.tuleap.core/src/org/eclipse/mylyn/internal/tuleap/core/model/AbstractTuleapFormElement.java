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
package org.eclipse.mylyn.internal.tuleap.core.model;

import java.io.Serializable;

import org.eclipse.mylyn.internal.tuleap.core.model.permission.TuleapPermissions;

/**
 * The common root of all Tuleap form elements.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public abstract class AbstractTuleapFormElement implements Serializable {

	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = 7487047015742226549L;

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
	 * The permissions of this form element.
	 */
	private TuleapPermissions permissions;

	/**
	 * The identifier of the form element.
	 */
	private String identifier;

	/**
	 * The rank of the form element.
	 */
	private int rank;

	/**
	 * The constructor.
	 * 
	 * @param formElementIdentifier
	 *            The identifier of the form element
	 */
	public AbstractTuleapFormElement(String formElementIdentifier) {
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
	public String getIdentifier() {
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
	 * Returns the permission of the form element.
	 * 
	 * @return The permission of the form element.
	 */
	public TuleapPermissions getPermissions() {
		return this.permissions;
	}

	/**
	 * Sets the permission of the form element.
	 * 
	 * @param formElementPermissions
	 *            The permissions to set
	 */
	public void setPermissions(TuleapPermissions formElementPermissions) {
		this.permissions = formElementPermissions;
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
