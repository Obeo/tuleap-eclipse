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
package org.eclipse.mylyn.internal.tuleap.core.model;

/**
 * The common super class of all the dynamic fields.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public abstract class AbstractTuleapDynamicField extends AbstractTuleapField {

	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = 50156152946388806L;

	/**
	 * The constructor.
	 * 
	 * @param formElementName
	 *            The name of the form element.
	 * @param formElementLabel
	 *            The label of the form element.
	 * @param formElementIdentifier
	 *            The identifier of the form element.
	 */
	public AbstractTuleapDynamicField(String formElementName, String formElementLabel,
			String formElementIdentifier) {
		super(formElementName, formElementLabel, formElementIdentifier);
	}

}
