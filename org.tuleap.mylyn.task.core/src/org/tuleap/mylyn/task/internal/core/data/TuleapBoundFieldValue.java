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

import java.util.Set;

/**
 * This class will store the values of a bindable field.
 * <ul>
 * <li>selectbox</li>
 * <li>multi selectbox</li>
 * <li>checkbox</li>
 * </ul>
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapBoundFieldValue extends AbstractTuleapFieldValue {

	/**
	 * The identifier of the values selected for the field.
	 */
	private Set<Integer> valuesId;

	/**
	 * The constructor.
	 * 
	 * @param valuesId
	 *            The identifier of the values of the field
	 * @param fieldId
	 *            the identifier of the field
	 */
	public TuleapBoundFieldValue(Set<Integer> valuesId, int fieldId) {
		super(fieldId);
	}

	/**
	 * Returns the identifier of the values selected for the field.
	 * 
	 * @return The identifier of the values selected for the field
	 */
	public Set<Integer> getValuesId() {
		return this.valuesId;
	}

}
