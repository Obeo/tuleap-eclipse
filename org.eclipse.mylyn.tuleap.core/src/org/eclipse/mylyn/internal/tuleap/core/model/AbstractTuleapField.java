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

import java.util.Collections;
import java.util.Map;

/**
 * Tuleap fields represents date, textfield or combo box.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public abstract class AbstractTuleapField extends AbstractTuleapFormElement {

	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = 1254204722841825488L;

	/**
	 * The constructor.
	 * 
	 * @param formElementIdentifier
	 *            The identifier of the form element
	 */
	public AbstractTuleapField(int formElementIdentifier) {
		super(formElementIdentifier);
	}

	/**
	 * Returns the options of the Tuleap field.
	 * 
	 * @return The options of the Tuleap field.
	 */
	public Map<String, String> getOptions() {
		return Collections.emptyMap();
	}

	/**
	 * Returns the kind of task attribute that Mylyn should use to represent the Tuleap field.
	 * 
	 * @return The kind of task attribute that Mylyn should use to represent the Tuleap field.
	 */
	public abstract String getMetadataKind();

	/**
	 * Returns the type of task attribute that Mylyn should use to represent the Tuleap field.
	 * 
	 * @return The type of task attribute that Mylyn should use to represent the Tuleap field.
	 */
	public abstract String getMetadataType();

	/**
	 * Returns the default value of the task attribute that Mylyn will use.
	 * 
	 * @return The default value of the task attribute that Mylyn will use.
	 */
	public abstract Object getDefaultValue();

}
