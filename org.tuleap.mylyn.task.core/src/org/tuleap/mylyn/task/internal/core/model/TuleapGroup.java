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
package org.tuleap.mylyn.task.internal.core.model;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Tuleap User Group.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapGroup {

	/**
	 * The group identifier.
	 */
	private final int id;

	/**
	 * The group name, human readable.
	 */
	private String name;

	/**
	 * Members of this group.
	 */
	private final Map<Integer, TuleapPerson> membersById = Maps.newHashMap();

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            The group identifier
	 * @param name
	 *            The group name
	 */
	public TuleapGroup(int id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * id getter.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * name getter.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * name setter.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Adds a member to this group.
	 * 
	 * @param member
	 *            the member to add, must be non-null.
	 */
	public void addMember(TuleapPerson member) {
		membersById.put(Integer.valueOf(member.getId()), member);
	}

	/**
	 * Get a view of the members of this group.
	 * 
	 * @return an unmodifiable collection view of the group's members.
	 */
	public Collection<TuleapPerson> getMembers() {
		return Collections.unmodifiableCollection(membersById.values());
	}
}
