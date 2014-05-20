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
 * The permission used on a given element by Tuleap.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public enum TuleapPermission {
	/**
	 * The property can be used by anonymous users.
	 */
	USER_GROUP_ANONYMOUS,

	/**
	 * The property can only be used by registered users.
	 */
	USER_GROUP_REGISTERED,

	/**
	 * The property can only be used by project members.
	 */
	USER_GROUP_PROJECT_MEMBERS,

	/**
	 * The property can only be used by an administrator.
	 */
	USER_GROUP_ADMIN,
}
