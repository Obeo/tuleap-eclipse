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
package org.eclipse.mylyn.internal.tuleap.ui.wizards.query;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

/**
 * The elements used to store the query.
 * 
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @since 1.0
 */
public class TuleapCustomQueryElement {

	/**
	 * Graphical query form element value.
	 */
	private Object graphicalElementValue;

	/**
	 * Graphical query form element operation.
	 */
	private Combo graphicalElementOperation;

	/**
	 * Name of the field in the Tuleap tracker configuration.
	 */
	private String tuleapFieldName;

	/**
	 * The constructor.
	 * 
	 * @param element
	 *            The graphical element
	 * @param fieldName
	 *            The field name
	 */
	public TuleapCustomQueryElement(Object element, String fieldName) {
		this.graphicalElementValue = element;
		this.tuleapFieldName = fieldName;
	}

	/**
	 * The constructor.
	 * 
	 * @param element
	 *            The graphical element
	 * @param fieldName
	 *            The field name
	 * @param operation
	 *            The operation
	 */
	public TuleapCustomQueryElement(Object element, String fieldName, Combo operation) {
		this.graphicalElementValue = element;
		this.tuleapFieldName = fieldName;
		this.graphicalElementOperation = operation;
	}

	/**
	 * Get the field name in the Tuleap tracker configuration.
	 * 
	 * @return Field name
	 */
	public String getTuleapFieldName() {
		return tuleapFieldName;
	}

	/**
	 * Get the query value for the current field.
	 * 
	 * @return Value
	 */
	public String[] getValue() {
		java.util.List<String> result = new ArrayList<String>();
		if (graphicalElementValue instanceof Text) {
			result.add(((Text)graphicalElementValue).getText());
		} else if (graphicalElementValue instanceof Combo) {
			result.add(((Combo)graphicalElementValue).getText());
		} else if (graphicalElementValue instanceof List) {
			return ((List)graphicalElementValue).getSelection();
		}
		return result.toArray(new String[result.size()]);
	}

	/**
	 * Get the query operation for the current field.
	 * 
	 * @return operation
	 */
	public String getOperation() {
		if (graphicalElementOperation == null) {
			return null;
		}
		return graphicalElementOperation.getText();
	}
}
