/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.tuleap.core.internal.model.data;

import java.io.Serializable;

/**
 * Used to reference authorized trackers references.
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class Accept implements Serializable {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -3143276810024424134L;

	/**
	 * The trackers references.
	 */
	private TuleapReference[] trackers;

	/**
	 * Default constructor for JSON deserialization.
	 */
	public Accept() {
		// Default constructor for JSON deserialization
	}

	/**
	 * The references array getter.
	 *
	 * @return the references
	 */
	public TuleapReference[] getReferences() {
		return trackers;
	}

	/**
	 * The references array setter.
	 *
	 * @param references
	 *            the references to set
	 */
	public void setReferences(TuleapReference[] references) {
		this.trackers = references;
	}
}
