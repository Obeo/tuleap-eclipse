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
 * A Tuleap user.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapPerson {
	/**
	 * The user name.
	 */
	private String userName;

	/**
	 * The real name.
	 */
	private String realName;

	/**
	 * The identifier.
	 */
	private String id;

	/**
	 * The email address.
	 */
	private String email;

	/**
	 * The constructor.
	 * 
	 * @param username
	 *            The user name
	 * @param realname
	 *            The real name
	 * @param identifier
	 *            The identifier
	 * @param mail
	 *            The email address
	 */
	public TuleapPerson(String username, String realname, String identifier, String mail) {
		this.userName = username;
		this.realName = realname;
		this.id = identifier;
		this.email = mail;
	}

	/**
	 * Returns the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
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
	 * Returns the user name.
	 * 
	 * @return The user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Returns the real name.
	 * 
	 * @return The real name
	 */
	public String getRealName() {
		return realName;
	}
}
