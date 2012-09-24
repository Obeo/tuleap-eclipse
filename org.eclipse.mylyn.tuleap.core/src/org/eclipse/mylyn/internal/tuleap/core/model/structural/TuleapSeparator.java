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

import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapStructuralElement;

/**
 * The Tuleap separator structural element.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapSeparator extends AbstractTuleapStructuralElement {

	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = 568134392486675259L;

	/**
	 * The constructor.
	 * 
	 * @param formElementIdentifier
	 *            The identifier of the form element
	 */
	public TuleapSeparator(String formElementIdentifier) {
		super(formElementIdentifier);
	}

}
