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
 * This is the common ancestor of all the four kind of field values that can be found in a Tuleap configurable
 * element:
 * <ul>
 * <li>literal value</li>
 * <li>select box value</li>
 * <li>multi-select box and checkbox values</li>
 * <li>file attachment values</li>
 * </ul>
 * Those values can be used inside of the AbstractTuleapConfigurableElement.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public abstract class AbstractFieldValue {
	/**
	 * The identifier of the field.
	 */
	protected int fieldId;

	/**
	 * Default constructor for JSON.
	 */
	protected AbstractFieldValue() {
		// Default constructor, for json
	}

	/**
	 * The constructor.
	 * 
	 * @param fieldId
	 *            The identifier of the field.
	 */
	public AbstractFieldValue(int fieldId) {
		this.fieldId = fieldId;
	}

	/**
	 * Returns the identifier of the field.
	 * 
	 * @return The identifier of the field
	 */
	public int getFieldId() {
		return this.fieldId;
	}
}
