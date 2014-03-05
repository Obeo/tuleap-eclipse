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
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.internal.core.model.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.BoundFieldValue;

/**
 * The Tuleap multi select box.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 0.7
 */
public class TuleapMultiSelectBox extends AbstractTuleapSelectBox {

	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = -553645996450459065L;

	/**
	 * The constructor.
	 * 
	 * @param formElementIdentifier
	 *            The identifier of the form element
	 */
	public TuleapMultiSelectBox(int formElementIdentifier) {
		super(formElementIdentifier);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField#getMetadataType()
	 */
	@Override
	public String getMetadataType() {
		return TaskAttribute.TYPE_MULTI_SELECT;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField#createFieldValue(org.eclipse.mylyn.tasks.core.data.TaskAttribute,
	 *      int)
	 */
	@Override
	public AbstractFieldValue createFieldValue(TaskAttribute attribute, int fieldId) {
		List<Integer> valueIds = new ArrayList<Integer>();
		for (String strValue : attribute.getValues()) {
			valueIds.add(Integer.valueOf(strValue));
		}
		return new BoundFieldValue(fieldId, valueIds);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField#setValue(org.eclipse.mylyn.tasks.core.data.TaskAttribute,
	 *      org.tuleap.mylyn.task.internal.core.model.data.AbstractFieldValue)
	 */
	@Override
	public void setValue(TaskAttribute attribute, AbstractFieldValue value) {
		Assert.isTrue(value instanceof BoundFieldValue);
		BoundFieldValue boundFieldValue = (BoundFieldValue)value;
		List<Integer> bindValueIds = boundFieldValue.getValueIds();
		List<String> values = new ArrayList<String>();
		for (Integer bindValueId : bindValueIds) {
			values.add(bindValueId.toString());
		}
		attribute.setValues(values);
	}
}
