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
package org.eclipse.mylyn.tuleap.ui.internal.wizards.query;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.AbstractTuleapSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBoxItem;
import org.eclipse.mylyn.tuleap.core.internal.model.data.IQueryCriterion;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapQueryCriterion;
import org.eclipse.mylyn.tuleap.ui.internal.TuleapTasksUIPlugin;
import org.eclipse.mylyn.tuleap.ui.internal.util.TuleapUIKeys;
import org.eclipse.mylyn.tuleap.ui.internal.util.TuleapUIMessages;
import org.eclipse.swt.widgets.List;

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
	 * @see org.eclipse.mylyn.tuleap.ui.internal.wizards.query.AbstractTuleapCustomQueryElement#getCriterion()
	 */
	@Override
	public IQueryCriterion<?> getCriterion() throws CoreException {
		int[] indices = listWidget.getSelectionIndices();
		TuleapQueryCriterion<int[]> criterion = null;
		if (indices != null && indices.length > 0) {
			criterion = new TuleapQueryCriterion<int[]>();
			criterion.setOperator(IQueryCriterion.OP_CONTAINS);
			int[] valueIds = new int[indices.length];
			for (int i = 0; i < indices.length; i++) {
				String item = listWidget.getItem(indices[i]);
				String valueId = getTuleapSelectBoxValueId(item);
				if (valueId != null) {
					// Otherwise it's unselected
					try {
						valueIds[i] = Integer.parseInt(valueId);
					} catch (NumberFormatException e) {
						throw new CoreException(new Status(IStatus.ERROR, TuleapTasksUIPlugin.PLUGIN_ID,
								TuleapUIMessages.getString(TuleapUIKeys.tuleapQueryInvalidValue, valueId,
										"int"))); //$NON-NLS-1$
					}
				}
			}
			criterion.setValue(valueIds);
		}
		return criterion;
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
		for (TuleapSelectBoxItem item : field.getItems()) {
			if (selectedValue.equals(item.getLabel())) {
				id = String.valueOf(item.getIdentifier());
				break;
			}
		}
		return id;
	}
}
