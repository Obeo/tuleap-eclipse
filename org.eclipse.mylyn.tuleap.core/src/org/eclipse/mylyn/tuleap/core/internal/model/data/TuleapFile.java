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
 * This class represents an artifact file attachment. It is characterized by the data it contains.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapFile implements Serializable {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -1871385709519828134L;

	/**
	 * The file data.
	 */
	private String data;

	/**
	 * Default constructor for deserialization.
	 */
	public TuleapFile() {
		// Default constructor for deserialization.
	}

	/**
	 * The constructor used to initialize attributes.
	 * 
	 * @param data
	 *            the file data
	 */
	public TuleapFile(String data) {
		this.data = data;
	}

	/**
	 * The data getter.
	 * 
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * The data setter.
	 * 
	 * @param data
	 *            the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
}
