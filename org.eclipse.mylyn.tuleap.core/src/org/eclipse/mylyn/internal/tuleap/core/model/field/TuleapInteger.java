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
package org.eclipse.mylyn.internal.tuleap.core.model.field;

import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapPermission;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;

/**
 * The Tuleap integer field.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapInteger extends AbstractTuleapField {

	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = 6143777215061824144L;

	/**
	 * The constructor.
	 * 
	 * @param formElementName
	 *            The name of the form element
	 * @param formElementLabel
	 *            The label of the form element
	 * @param formElementDescription
	 *            The description of the form element
	 * @param isRequired
	 *            Indicates if the form element is required
	 * @param formElementPermission
	 *            The permission of the form element
	 * @param formElementIdentifier
	 *            The identifier of the form element
	 */
	public TuleapInteger(String formElementName, String formElementLabel, String formElementDescription,
			boolean isRequired, TuleapPermission formElementPermission, String formElementIdentifier) {
		super(formElementName, formElementLabel, formElementDescription, isRequired, formElementPermission,
				formElementIdentifier);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField#getMetadataKind()
	 */
	@Override
	public String getMetadataKind() {
		return TaskAttribute.KIND_DEFAULT;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField#getMetadataType()
	 */
	@Override
	public String getMetadataType() {
		return TaskAttribute.TYPE_INTEGER;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField#getDefaultValue()
	 */
	@Override
	public Object getDefaultValue() {
		return "0"; //$NON-NLS-1$
	}
}
