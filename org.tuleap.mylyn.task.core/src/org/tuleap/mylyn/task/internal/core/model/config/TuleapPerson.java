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
package org.tuleap.mylyn.task.internal.core.model.config;

import java.io.Serializable;

/**
 * A Tuleap user.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapPerson implements Serializable {

	/**
	 * The serial vesion UID.
	 */
	private static final long serialVersionUID = 6934279209396555434L;

	/**
	 * The user name.
	 */
	private final String userName;

	/**
	 * The real name.
	 */
	private final String realName;

	/**
	 * The identifier.
	 */
	private final int id;

	/**
	 * The email address.
	 */
	private final String email;

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
	public TuleapPerson(String username, String realname, int identifier, String mail) {
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
	public int getId() {
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
