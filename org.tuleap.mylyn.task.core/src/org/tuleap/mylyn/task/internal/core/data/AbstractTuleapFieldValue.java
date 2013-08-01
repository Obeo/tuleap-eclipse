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
package org.tuleap.mylyn.task.internal.core.data;

/**
 * The root class of all the field values.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public abstract class AbstractTuleapFieldValue {
	/**
	 * The identifier of the field.
	 */
	protected int fieldId;

	/**
	 * The constructor.
	 * 
	 * @param fieldId
	 *            The identifier of the field
	 */
	public AbstractTuleapFieldValue(int fieldId) {
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
