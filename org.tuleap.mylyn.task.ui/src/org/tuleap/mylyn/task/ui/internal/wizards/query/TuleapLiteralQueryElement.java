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

/**
 * Element for literal fields (string, text, numeric, computed, ...).
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 0.7
 */
public class TuleapLiteralQueryElement extends AbstractTuleapCustomQueryElement<AbstractTuleapField> {

	/**
	 * The widget used to enter the value.
	 */
	protected final Text textWidget;

	/**
	 * The constructor.
	 *
	 * @param field
	 *            The field
	 * @param element
	 *            The graphical element
	 */
	public TuleapLiteralQueryElement(AbstractTuleapField field, Text element) {
		super(field);
		this.textWidget = element;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.ui.internal.wizards.query.AbstractTuleapCustomQueryElement#getCriterion()
	 */
	@Override
	public IQueryCriterion<?> getCriterion() throws CoreException {
		IQueryCriterion<?> criterion = null;
		String text = textWidget.getText();
		if (text != null && !text.isEmpty()) {
			criterion = createCriterion(text);
		}
		return criterion;
	}

	/**
	 * Instantiates the right kind of criterion. Default implementation creates a {@link TuleapQueryCriterion
	 * <String>} . Sub-classes should override this method.
	 *
	 * @param text
	 *            The text entered by the user, not null and not empty.
	 * @throws CoreException
	 *             If the given String does not represent a valid value for the field.
	 * @return The right kind of criterion.
	 */
	protected IQueryCriterion<?> createCriterion(String text) throws CoreException {
		TuleapQueryCriterion<String> criterion = new TuleapQueryCriterion<String>();
		criterion.setOperator(IQueryCriterion.OP_CONTAINS);
		criterion.setValue(text);
		return criterion;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.ui.internal.wizards.query.AbstractTuleapCustomQueryElement#validate()
	 */
	@Override
	public final boolean validate() {
		String text = textWidget.getText();
		if (text != null && !text.isEmpty()) {
			return doValidate(text);
		}
		return true;
	}

	/**
	 * Validates the value entered in the element's widget. This method should be overridden by relevant
	 * sub-classes.
	 *
	 * @param text
	 *            The non-null and non-empty text enteered for this criterion.
	 * @return <code>true</code> if and only if the text is valid for this criterion.
	 */
	protected boolean doValidate(String text) {
		return true;
	}
}
