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

import java.util.Collections;
import java.util.List;

/**
 * Utility class used to store the identifiers of the value selected for a select box, checkbox or
 * multi-select box.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class BoundFieldValue extends AbstractFieldValue {

	/**
	 * The list of value ids.
	 */
	private List<Integer> bindValueIds;

	/**
	 * The constructor.
	 * 
	 * @param fieldId
	 *            The identifier of the field
	 * @param valueIds
	 *            The list of value ids
	 */
	public BoundFieldValue(int fieldId, List<Integer> valueIds) {
		super(fieldId);
		this.bindValueIds = Collections.unmodifiableList(valueIds);
	}

	/**
	 * Returns the value ids.
	 * 
	 * @return An unmodifiable view of the value ids
	 */
	public List<Integer> getValueIds() {
		return this.bindValueIds;
	}
}
