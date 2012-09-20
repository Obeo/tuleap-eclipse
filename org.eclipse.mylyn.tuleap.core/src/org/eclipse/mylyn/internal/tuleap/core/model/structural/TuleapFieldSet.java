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
package org.eclipse.mylyn.internal.tuleap.core.model.structural;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapFormElement;
import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapStructuralElement;

/**
 * The Tuleap field set structural element.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapFieldSet extends AbstractTuleapStructuralElement {

	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = 2991992201721653194L;

	/**
	 * The form elements.
	 */
	private List<AbstractTuleapFormElement> formElements = new ArrayList<AbstractTuleapFormElement>();

	/**
	 * The constructor.
	 * 
	 * @param formElementName
	 *            The name of the form element
	 * @param formElementLabel
	 *            The label of the form element
	 * @param formElementIdentifier
	 *            The identifier of the form element
	 */
	public TuleapFieldSet(String formElementName, String formElementLabel, String formElementIdentifier) {
		super(formElementName, formElementLabel, formElementIdentifier);
	}

	/**
	 * Returns the form elements.
	 * 
	 * @return The form elements.
	 */
	public List<AbstractTuleapFormElement> getFormElements() {
		return formElements;
	}

}
