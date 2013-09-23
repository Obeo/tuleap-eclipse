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

/**
 * This class represent the value of a single selection field.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class SingleSelectFieldValue extends AbstractFieldValue {

	/**
	 * The identifier of the bound value.
	 */
	private int bindValueId;

	/**
	 * The constructor.
	 * 
	 * @param fieldId
	 *            The identifier of the field
	 * @param bindValueId
	 *            The identifier of the bound value selected
	 */
	public SingleSelectFieldValue(int fieldId, int bindValueId) {
		super(fieldId);
		this.bindValueId = bindValueId;
	}

	/**
	 * Returns the bind value id.
	 * 
	 * @return The bind value id
	 */
	public int getBindValueId() {
		return this.bindValueId;
	}
}
