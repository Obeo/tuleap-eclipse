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
package org.tuleap.mylyn.task.internal.core.model.config.field;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.OpenListFieldValue;

/**
 * The Tuleap open list field.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapOpenList extends AbstractTuleapField {

	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = -6206534825044562854L;

	/**
	 * The constructor.
	 * 
	 * @param formElementIdentifier
	 *            The identifier of the form element
	 */
	public TuleapOpenList(int formElementIdentifier) {
		super(formElementIdentifier);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField#getMetadataKind()
	 */
	@Override
	public String getMetadataKind() {
		return TaskAttribute.KIND_DEFAULT;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField#getMetadataType()
	 */
	@Override
	public String getMetadataType() {
		return TaskAttribute.TYPE_SHORT_RICH_TEXT;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField#getDefaultValue()
	 */
	@Override
	public Object getDefaultValue() {
		return ""; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField#createFieldValue(org.eclipse.mylyn.tasks.core.data.TaskAttribute,
	 *      int)
	 */
	@Override
	public AbstractFieldValue createFieldValue(TaskAttribute attribute, int fieldId) {
		List<String> values = new ArrayList<String>();
		for (String value : attribute.getValue().split(",")) { //$NON-NLS-1$
			if (!value.trim().isEmpty()) {
				values.add(value.trim());
			}
		}
		return new OpenListFieldValue(fieldId, values);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField#setValue(org.eclipse.mylyn.tasks.core.data.TaskAttribute,
	 *      org.tuleap.mylyn.task.internal.core.model.data.AbstractFieldValue)
	 */
	@Override
	public void setValue(TaskAttribute attribute, AbstractFieldValue value) {
		Assert.isTrue(value instanceof OpenListFieldValue || value instanceof BoundFieldValue);
		if (value instanceof OpenListFieldValue) {
			OpenListFieldValue boundFieldValue = (OpenListFieldValue)value;
			List<String> values = boundFieldValue.getValueIds();
			StringBuilder b = new StringBuilder();
			if (!values.isEmpty()) {
				Iterator<String> it = values.iterator();
				b.append(it.next());
				while (it.hasNext()) {
					b.append(", ").append(it.next()); //$NON-NLS-1$
				}
			}
			attribute.setValue(b.toString());
		} else {
			BoundFieldValue boundFieldValue = (BoundFieldValue)value;
			List<Integer> values = boundFieldValue.getValueIds();
			attribute.clearValues();
			StringBuilder b = new StringBuilder();
			if (!values.isEmpty()) {
				Iterator<Integer> it = values.iterator();
				b.append(it.next());
				while (it.hasNext()) {
					b.append(", ").append(it.next()); //$NON-NLS-1$
				}
			}
			attribute.setValue(b.toString());
		}
	}
}
