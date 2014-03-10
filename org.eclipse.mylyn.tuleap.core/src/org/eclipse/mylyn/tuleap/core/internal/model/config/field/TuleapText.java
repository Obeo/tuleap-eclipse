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
package org.eclipse.mylyn.tuleap.core.internal.model.config.field;

import org.eclipse.core.runtime.Assert;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AbstractFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.LiteralFieldValue;

/**
 * The Tuleap text field.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapText extends AbstractTuleapField {

	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = -8505187023733445329L;

	/**
	 * The number of rows of the text widget.
	 */
	private int rows;

	/**
	 * The number of columns of the text widget.
	 */
	private int columns;

	/**
	 * The constructor.
	 * 
	 * @param formElementIdentifier
	 *            The identifier of the form element
	 */
	public TuleapText(int formElementIdentifier) {
		super(formElementIdentifier);
	}

	/**
	 * Sets the number of rows of the text widget.
	 * 
	 * @param numberOfRows
	 *            The number of rows of the text widget.
	 */
	public void setRows(int numberOfRows) {
		this.rows = numberOfRows;
	}

	/**
	 * Returns the number of rows of the text widget.
	 * 
	 * @return The number of rows of the text widget.
	 */
	public int getRows() {
		return this.rows;
	}

	/**
	 * Sets the number of columns of the text widget.
	 * 
	 * @param numberOfColumns
	 *            The number of columns of the text widget.
	 */
	public void setColumns(int numberOfColumns) {
		this.columns = numberOfColumns;
	}

	/**
	 * Returns the number of columns of the text widget.
	 * 
	 * @return The number of columns of the text widget.
	 */
	public int getColumns() {
		return this.columns;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField#getMetadataKind()
	 */
	@Override
	public String getMetadataKind() {
		return TaskAttribute.KIND_DEFAULT;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField#getMetadataType()
	 */
	@Override
	public String getMetadataType() {
		return TaskAttribute.TYPE_LONG_RICH_TEXT;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField#getDefaultValue()
	 */
	@Override
	public Object getDefaultValue() {
		return ""; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField#setValue(org.eclipse.mylyn.tasks.core.data.TaskAttribute,
	 *      org.eclipse.mylyn.tuleap.core.internal.model.data.AbstractFieldValue)
	 */
	@Override
	public void setValue(TaskAttribute attribute, AbstractFieldValue value) {
		Assert.isTrue(value instanceof LiteralFieldValue);
		attribute.setValue(((LiteralFieldValue)value).getFieldValue());
	}
}
