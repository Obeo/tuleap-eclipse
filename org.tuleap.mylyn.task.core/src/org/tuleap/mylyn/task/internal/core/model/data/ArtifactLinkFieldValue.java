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
 * This object represents an artifact link field, that contains a list of task IDs.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ArtifactLinkFieldValue extends AbstractFieldValue {

	/**
	 * The value of the field.
	 */
	private int[] values;

	/**
	 * The constructor.
	 * 
	 * @param fieldId
	 *            The identifier of the field
	 * @param values
	 *            The value of the field
	 */
	public ArtifactLinkFieldValue(int fieldId, int[] values) {
		super(fieldId);
		this.values = values;
	}

	/**
	 * Returns the value of the field.
	 * 
	 * @return the fieldValue
	 */
	public int[] getFieldValues() {
		return this.values.clone();
	}
}
