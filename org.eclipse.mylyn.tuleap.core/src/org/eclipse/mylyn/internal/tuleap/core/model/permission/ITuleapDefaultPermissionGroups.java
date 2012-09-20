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
 * The default groups for which permissions can be defined.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public interface ITuleapDefaultPermissionGroups {
	/**
	 * The property can be used by anonymous users.
	 */
	String ALL_USERS = "all_users"; //$NON-NLS-1$

	/**
	 * The property can only be used by registered users.
	 */
	String REGISTERED_USERS = "registered_users"; //$NON-NLS-1$

	/**
	 * The property can only be used by project members.
	 */
	String PROJECT_MEMBERS = "project_members"; //$NON-NLS-1$

	/**
	 * The property can only be used by project administrators.
	 */
	String PROJECT_ADMINS = "project_admins"; //$NON-NLS-1$

	/**
	 * The property can only be used by file manager administrators.
	 */
	String FILE_MANAGER_ADMINS = "file_manager_admins"; //$NON-NLS-1$

	/**
	 * The property can only be used by wiki administrators.
	 */
	String WIKI_ADMINS = "wiki_admins"; //$NON-NLS-1$

	/**
	 * The property can only be used by tracker administrators.
	 */
	String TRACKER_ADMINS = "tracker_admins"; //$NON-NLS-1$

	/**
	 * Nobody can use the property.
	 */
	String NOBODY = "nobody"; //$NON-NLS-1$
}
