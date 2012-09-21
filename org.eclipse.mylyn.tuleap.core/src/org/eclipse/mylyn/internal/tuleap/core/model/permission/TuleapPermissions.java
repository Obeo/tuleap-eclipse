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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The Tuleap permissions holder.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapPermissions implements Serializable {
	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = 2538582446703848888L;

	/**
	 * The mapping between the different group and their ability to manipulate the field for which those
	 * permissions are defined.
	 */
	private Map<String, TuleapAccessPermission> group2access = new HashMap<String, TuleapAccessPermission>();

	/**
	 * The mapping between the different group and their ability to submit a change for the field for which
	 * those permissions are defined.
	 */
	private Map<String, Boolean> group2canSubmit = new HashMap<String, Boolean>();

	/**
	 * Puts a new permission in the permissions holder.
	 * 
	 * @param group
	 *            The group concerned
	 * @param accessPermission
	 *            The access permission for the group
	 * @param canSubmit
	 *            <code>true</code> if the group in question can submit a value for the field for which those
	 *            permissions are defined
	 */
	public void put(String group, TuleapAccessPermission accessPermission, boolean canSubmit) {
		this.group2access.put(group, accessPermission);
		this.group2canSubmit.put(group, Boolean.valueOf(canSubmit));
	}

	/**
	 * Returns <code>true</code> if the given group can submit a new value for the field, <code>false</code>
	 * otherwise.
	 * 
	 * @param group
	 *            The group
	 * @return <code>true</code> if the given group can submit a new value for the field, <code>false</code>
	 *         otherwise.
	 */
	public boolean canSubmit(String group) {
		return this.group2canSubmit.get(group).booleanValue();
	}

	/**
	 * Returns the access permission for the given group.
	 * 
	 * @param group
	 *            The group
	 * @return The access permission for the given group.
	 */
	public TuleapAccessPermission getAccessPermission(String group) {
		return this.group2access.get(group);
	}

	/**
	 * Returns the group for which permissions are defined.
	 * 
	 * @return The group for which permissions are defined.
	 */
	public Set<String> getGroups() {
		return Collections.unmodifiableSet(this.group2access.keySet());
	}
}
