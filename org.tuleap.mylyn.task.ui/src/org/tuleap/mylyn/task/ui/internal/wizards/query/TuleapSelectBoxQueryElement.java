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
package org.tuleap.mylyn.task.ui.internal.wizards.query;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.List;
import org.tuleap.mylyn.task.core.internal.model.config.field.AbstractTuleapSelectBox;
import org.tuleap.mylyn.task.core.internal.model.data.IQueryCriterion;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapQueryCriterion;

/**
 * The elements used to store the query.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 0.7
 */
public class TuleapSelectBoxQueryElement extends AbstractTuleapCustomQueryElement<AbstractTuleapSelectBox> {

	/**
	 * The List widget used to select criteria.
	 */
	private List listWidget;

	/**
	 * The constructor.
	 *
	 * @param field
	 *            The field
	 * @param list
	 *            The graphical element
	 */
	public TuleapSelectBoxQueryElement(AbstractTuleapSelectBox field, List list) {
		super(field);
		this.listWidget = list;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.ui.internal.wizards.query.AbstractTuleapCustomQueryElement#getCriterion()
	 */
	@Override
	public IQueryCriterion<String[]> getCriterion() throws CoreException {
		TuleapQueryCriterion<String[]> criterion = null;
		java.util.List<String> selectedValues = new ArrayList<String>();
		for (String item : listWidget.getSelection()) {
			if (item != null && !item.trim().isEmpty()) {
				selectedValues.add(item);
			}
		}
		if (!selectedValues.isEmpty()) {
			criterion = new TuleapQueryCriterion<String[]>();
			criterion.setOperator(getOperator());
			criterion.setValue(selectedValues.toArray(new String[selectedValues.size()]));
		}
		return criterion;
	}
}
