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
package org.tuleap.mylyn.task.internal.core.data;

/**
 * This object represents a literal value. Among the possible literal values we can find string, int, float,
 * text, date, boolean, artifact links, open list...
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class LiteralFieldValue extends AbstractFieldValue {

	/**
	 * The value of the field.
	 */
	private String fieldValue;

	/**
	 * The constructor.
	 * 
	 * @param fieldId
	 *            The identifier of the field
	 * @param fieldValue
	 *            The value of the field
	 */
	public LiteralFieldValue(int fieldId, String fieldValue) {
		super(fieldId);
		this.fieldValue = fieldValue;
	}

	/**
	 * Returns the value of the field.
	 * 
	 * @return the fieldValue
	 */
	public String getFieldValue() {
		return this.fieldValue;
	}
}
