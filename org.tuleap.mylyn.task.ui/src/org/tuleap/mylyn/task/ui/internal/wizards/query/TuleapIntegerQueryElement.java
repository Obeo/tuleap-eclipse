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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Text;
import org.tuleap.mylyn.task.core.internal.model.config.AbstractTuleapField;
import org.tuleap.mylyn.task.core.internal.model.data.IQueryCriterion;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapQueryCriterion;
import org.tuleap.mylyn.task.ui.internal.TuleapTasksUIPlugin;
import org.tuleap.mylyn.task.ui.internal.util.TuleapUIKeys;
import org.tuleap.mylyn.task.ui.internal.util.TuleapUIMessages;

/**
 * The elements used to store the query.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 0.7
 */
public class TuleapIntegerQueryElement extends TuleapLiteralQueryElement {

	/**
	 * The constructor.
	 *
	 * @param field
	 *            The field
	 * @param element
	 *            The graphical element
	 */
	public TuleapIntegerQueryElement(AbstractTuleapField field, Text element) {
		super(field, element);
		createDecorator(element, TuleapUIMessages
				.getString(TuleapUIKeys.tuleapQueryIntegerFieldDecoratorText));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.ui.internal.wizards.query.TuleapLiteralQueryElement#createCriterion(java.lang.String)
	 */
	@Override
	protected IQueryCriterion<Integer> createCriterion(String text) throws CoreException {
		TuleapQueryCriterion<Integer> criterion = new TuleapQueryCriterion<Integer>();
		criterion.setOperator(IQueryCriterion.OP_CONTAINS);
		try {
			criterion.setValue(Integer.valueOf(text));
		} catch (NumberFormatException e) {
			criterion = null;
			TuleapTasksUIPlugin.log(TuleapUIMessages.getString(TuleapUIKeys.tuleapQueryInvalidValue, text,
					"Integer"), true); //$NON-NLS-1$
		}
		return criterion;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.ui.internal.wizards.query.AbstractTuleapCustomQueryElement#validate()
	 */
	@Override
	public boolean doValidate(String text) {
		decorator.hide();
		try {
			Integer.valueOf(text);
		} catch (NumberFormatException e) {
			decorator.show();
			return false;
		}
		return true;
	}
}
