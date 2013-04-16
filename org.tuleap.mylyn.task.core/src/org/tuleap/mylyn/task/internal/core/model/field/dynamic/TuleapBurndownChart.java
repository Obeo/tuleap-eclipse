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
package org.tuleap.mylyn.task.internal.core.model.field.dynamic;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapDynamicField;

/**
 * The burndown chart Tuleap dynamic field.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapBurndownChart extends AbstractTuleapDynamicField {

	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = -3436837457267474974L;

	/**
	 * The constructor.
	 * 
	 * @param formElementIdentifier
	 *            The identifier of the form element.
	 */
	public TuleapBurndownChart(int formElementIdentifier) {
		super(formElementIdentifier);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField#getMetadataKind()
	 */
	@Override
	public String getMetadataKind() {
		return TaskAttribute.KIND_DEFAULT;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField#getMetadataType()
	 */
	@Override
	public String getMetadataType() {
		return TaskAttribute.TYPE_URL;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField#getDefaultValue()
	 */
	@Override
	public Object getDefaultValue() {
		return null;
	}

}
