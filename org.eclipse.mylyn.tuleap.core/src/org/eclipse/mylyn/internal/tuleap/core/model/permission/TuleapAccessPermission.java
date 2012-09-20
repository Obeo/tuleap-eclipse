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
package org.eclipse.mylyn.internal.tuleap.core.model.permission;

/**
 * The Tuleap access permission.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public enum TuleapAccessPermission {
	/**
	 * Indicates that the field can only be read.
	 */
	READ_ONLY,

	/**
	 * Indicates that the field can be read and updated.
	 */
	UPDATE,

	/**
	 * Indicates that the permission should be inherited.
	 */
	INHERIT,
}
