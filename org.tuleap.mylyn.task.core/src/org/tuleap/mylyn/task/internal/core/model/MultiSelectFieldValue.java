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

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent multi-selection values.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class MultiSelectFieldValue extends AbstractFieldValue {

	/**
	 * The list of the identifiers of the bound values selected.
	 */
	private List<Integer> bindValueIds = new ArrayList<Integer>();

	/**
	 * The constructor.
	 * 
	 * @param fieldId
	 *            The identifier of the field
	 * @param bindValueIds
	 *            The identifiers of the bound values selected
	 */
	public MultiSelectFieldValue(int fieldId, List<Integer> bindValueIds) {
		super(fieldId);
		this.bindValueIds.addAll(bindValueIds);
	}

	/**
	 * Returns the identifiers of the bound values selected.
	 * 
	 * @return The identifiers of the bound values selected
	 */
	public List<Integer> getBindValueIds() {
		return this.bindValueIds;
	}
}
