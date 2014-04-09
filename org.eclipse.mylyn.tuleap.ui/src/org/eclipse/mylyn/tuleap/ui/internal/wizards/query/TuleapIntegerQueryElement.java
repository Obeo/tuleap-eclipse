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
import org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField;
import org.eclipse.mylyn.tuleap.core.internal.model.data.IQueryCriterion;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapQueryCriterion;
import org.eclipse.mylyn.tuleap.ui.internal.TuleapTasksUIPlugin;
import org.eclipse.mylyn.tuleap.ui.internal.util.TuleapUIMessages;
import org.eclipse.mylyn.tuleap.ui.internal.util.TuleapUiMessagesKeys;
import org.eclipse.swt.widgets.Text;

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
				.getString(TuleapUiMessagesKeys.tuleapQueryIntegerFieldDecoratorText));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tuleap.ui.internal.wizards.query.TuleapLiteralQueryElement#createCriterion(java.lang.String)
	 */
	@Override
	protected IQueryCriterion<Integer> createCriterion(String text) throws CoreException {
		TuleapQueryCriterion<Integer> criterion = new TuleapQueryCriterion<Integer>();
		criterion.setOperator(IQueryCriterion.OP_CONTAINS);
		try {
			criterion.setValue(Integer.valueOf(text));
		} catch (NumberFormatException e) {
			criterion = null;
			TuleapTasksUIPlugin.log(TuleapUIMessages.getString(TuleapUiMessagesKeys.tuleapQueryInvalidValue,
					text, "Integer"), true); //$NON-NLS-1$
		}
		return criterion;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tuleap.ui.internal.wizards.query.AbstractTuleapCustomQueryElement#validate()
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
