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
 * The Tuleap integer field.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapInteger extends AbstractTuleapField {

	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = 6143777215061824144L;

	/**
	 * The constructor.
	 *
	 * @param formElementIdentifier
	 *            The identifier of the form element
	 */
	public TuleapInteger(int formElementIdentifier) {
		super(formElementIdentifier);
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
		return TaskAttribute.TYPE_INTEGER;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField#getDefaultValue()
	 */
	@Override
	public Object getDefaultValue() {
		return "0"; //$NON-NLS-1$
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

	@Override
	public void accept(ITuleapFieldVisitor visitor) {
		visitor.visit(this);
	}
}
