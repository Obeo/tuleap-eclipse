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

import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapMultiSelectBox;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapSelectBox;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapSelectBoxItem;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

/**
 * The elements used to store the query.
 * 
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @since 0.7
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
	 * The field in the Tuleap tracker configuration.
	 */
	private AbstractTuleapField tuleapField;

	/**
	 * The constructor.
	 * 
	 * @param element
	 *            The graphical element
	 * @param field
	 *            The field
	 */
	public TuleapCustomQueryElement(Object element, AbstractTuleapField field) {
		this.graphicalElementValue = element;
		this.tuleapField = field;
	}

	/**
	 * The constructor.
	 * 
	 * @param element
	 *            The graphical element
	 * @param field
	 *            The field
	 * @param operation
	 *            The operation
	 */
	public TuleapCustomQueryElement(Object element, AbstractTuleapField field, Combo operation) {
		this.graphicalElementValue = element;
		this.tuleapField = field;
		this.graphicalElementOperation = operation;
	}

	/**
	 * Get the field name in the Tuleap tracker configuration.
	 * 
	 * @return Field name
	 */
	public String getTuleapFieldName() {
		return tuleapField.getName();
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
			result.add(getTuleapSelectBoxValueId(((Combo)graphicalElementValue).getText()));
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

	/**
	 * Get the identifier associated to a select box item value label.
	 * 
	 * @param selectedValue
	 *            Select box item label
	 * @return The identifier associated to the item value label
	 */
	private String getTuleapSelectBoxValueId(String selectedValue) {
		String id = null;
		if (tuleapField instanceof TuleapSelectBox) {
			TuleapSelectBox tuleapSelectBox = (TuleapSelectBox)tuleapField;
			for (TuleapSelectBoxItem item : tuleapSelectBox.getItems()) {
				if (selectedValue.equals(item.getLabel())) {
					id = String.valueOf(item.getIdentifier());
				}
			}
		} else if (tuleapField instanceof TuleapMultiSelectBox) {
			TuleapMultiSelectBox tuleapSelectBox = (TuleapMultiSelectBox)tuleapField;
			for (TuleapSelectBoxItem item : tuleapSelectBox.getItems()) {
				if (selectedValue.equals(item.getLabel())) {
					id = String.valueOf(item.getIdentifier());
				}
			}
		}
		return id;
	}
}
