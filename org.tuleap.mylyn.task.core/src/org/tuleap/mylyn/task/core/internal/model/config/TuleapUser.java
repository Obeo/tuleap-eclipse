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
package org.tuleap.mylyn.task.core.internal.model.config;

import java.io.Serializable;
import java.util.Date;

/**
 * A Tuleap user.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapUser implements Serializable {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -3627121808349119815L;

	/**
	 * The identifier.
	 */
	private int id;

	/**
	 * The email address.
	 */
	private String email;

	/**
	 * The real name.
	 */
	private String realName;

	/**
	 * The user name.
	 */
	private String username;

	/**
	 * The ldap_id.
	 */
	private String ldapId;

	/**
	 * Date of update of this user.
	 */
	private Date updatedOn;

	/**
	 * Default constructor.
	 */
	public TuleapUser() {
		// Default constructor
	}

	/**
	 * The constructor with the identifier, for temporary uses.
	 *
	 * @param identifier
	 *            The identifier
	 */
	public TuleapUser(int identifier) {
		this.id = identifier;
	}

	/**
	 * Returns the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * ID setter.
	 *
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Email setter.
	 *
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Returns the real name.
	 *
	 * @return The realname
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * Real name setter.
	 *
	 * @param realName
	 *            the realName to set
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * Returns the user name.
	 *
	 * @return The user name
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * User name setter.
	 *
	 * @param username
	 *            the userName to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Returns the ldap id.
	 *
	 * @return the ldap id
	 */
	public String getLdapId() {
		return ldapId;
	}

	/**
	 * Updated on getter.
	 *
	 * @return the updatedOn
	 */
	public Date getUpdatedOn() {
		return updatedOn;
	}

	/**
	 * Updated on setter.
	 *
	 * @param updatedOn
	 *            the updatedOn to set
	 */
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
}
