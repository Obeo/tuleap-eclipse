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
package org.tuleap.mylyn.task.internal.core.model.config;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Tuleap User Group.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapUserGroup implements Serializable {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = 2221608706409707022L;

	/**
	 * The group identifier.
	 */
	private final int id;

	/**
	 * The group uri.
	 */
	private String uri;

	/**
	 * The group label, human readable.
	 */
	private String label;

	/**
	 * Members of this group.
	 */
	private final Map<Integer, TuleapUser> membersById = Maps.newHashMap();

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            The group identifier
	 * @param label
	 *            The group label
	 */
	public TuleapUserGroup(int id, String label) {
		this.id = id;
		this.label = label;
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
	 * label getter.
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * label setter.
	 * 
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * uri getter.
	 * 
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * uri setter.
	 * 
	 * @param uri
	 *            the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * Adds a member to this group.
	 * 
	 * @param member
	 *            the member to add, must be non-null.
	 */
	protected void addMember(TuleapUser member) {
		membersById.put(Integer.valueOf(member.getId()), member);
	}

	/**
	 * Get a view of the members of this group.
	 * 
	 * @return an unmodifiable collection view of the group's members.
	 */
	public Collection<TuleapUser> getMembers() {
		return Collections.unmodifiableCollection(membersById.values());
	}
}
