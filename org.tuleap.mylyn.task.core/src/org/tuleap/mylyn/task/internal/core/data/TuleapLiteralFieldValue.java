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
 * This field value will be used to store the value of a literal field.
 * <ul>
 * <li>string</li>
 * <li>text</li>
 * <li>date</li>
 * <li>int</li>
 * <li>float</li>
 * <li>open list</li>
 * <li>artifact link</li>
 * </ul>
 * The value of those field will be store as a string.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapLiteralFieldValue extends AbstractTuleapFieldValue {

	/**
	 * The value of the field.
	 */
	private String value;

	/**
	 * The constructor.
	 * 
	 * @param value
	 *            The value of the field
	 * @param fieldId
	 *            The identifier of the field
	 */
	public TuleapLiteralFieldValue(String value, int fieldId) {
		super(fieldId);
	}

	/**
	 * Returns the value of the field.
	 * 
	 * @return The value of the field
	 */
	public String getValue() {
		return this.value;
	}
}
