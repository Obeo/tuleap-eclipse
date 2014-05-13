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

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.tuleap.mylyn.task.core.internal.model.config.AbstractTuleapField;
import org.tuleap.mylyn.task.core.internal.model.data.IQueryCriterion;

/**
 * The elements used to store the query.
 *
 * @param <T>
 *            the type of field.
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 0.7
 */
public abstract class AbstractTuleapCustomQueryElement<T extends AbstractTuleapField> {

	/**
	 * The field in the Tuleap tracker configuration.
	 */
	protected T field;

	/**
	 * The decorator used to report invalid input.
	 */
	protected ControlDecoration decorator;

	/**
	 * The constructor.
	 *
	 * @param field
	 *            The field
	 */
	public AbstractTuleapCustomQueryElement(T field) {
		Assert.isNotNull(field);
		this.field = field;
	}

	/**
	 * Create a decorator for the given control.
	 *
	 * @param control
	 *            Control to decorate.
	 * @param text
	 *            Description text of the decorator.
	 */
	protected void createDecorator(Control control, String text) {
		decorator = new ControlDecoration(control, SWT.TOP | SWT.RIGHT);
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(
				FieldDecorationRegistry.DEC_ERROR);
		Image image = fieldDecoration.getImage();
		decorator.setImage(image);
		decorator.setDescriptionText(text);
		decorator.hide();
	}

	/**
	 * Get the field in the Tuleap tracker configuration.
	 *
	 * @return The field
	 */
	public T getField() {
		return field;
	}

	/**
	 * Get the query criterion for the current field according to the selected value(s).
	 *
	 * @return A query criterion that represents the user's choice. <code>null</code> if this field is not
	 *         part of the criteria the user selected.
	 * @throws CoreException
	 *             If the criterion cannot be built.
	 */
	public abstract IQueryCriterion<?> getCriterion() throws CoreException;

	/**
	 * Get the query operation for the current field. Default implementation return <code>"contains"</code>
	 * since it's the most used operators (only dates use different operators).
	 *
	 * @return operation
	 */
	public String getOperator() {
		return "contains"; //$NON-NLS-1$
	}

	/**
	 * Validates this query element to make sure the criterion entered by the user is valid. Default
	 * implementation always returns <code>true</code>.
	 *
	 * @return <code>true</code> if and only if the criterion is valid.
	 */
	public boolean validate() {
		return true;
	}
}
