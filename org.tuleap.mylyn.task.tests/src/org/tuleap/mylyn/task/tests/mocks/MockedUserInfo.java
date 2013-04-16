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
package org.tuleap.mylyn.task.tests.mocks;

import org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.UserInfo;

/**
 * Mock of user info.
 * 
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @since 0.7
 */
public class MockedUserInfo extends UserInfo {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 6444342403021865523L;

	/**
	 * User name.
	 */
	private String userName;

	/**
	 * Real name.
	 */
	private String realName;

	/**
	 * Identifier.
	 */
	private int id;

	/**
	 * Email.
	 */
	private String email;

	/**
	 * Constructor.
	 * 
	 * @param mockedUserName
	 *            User name
	 * @param mockedRealName
	 *            Real name
	 * @param identifier
	 *            Identifier
	 * @param mockedEmail
	 *            Email
	 */
	public MockedUserInfo(String mockedUserName, String mockedRealName, int identifier, String mockedEmail) {
		this.userName = mockedUserName;
		this.realName = mockedRealName;
		this.id = identifier;
		this.email = mockedEmail;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.UserInfo#getUsername()
	 */
	@Override
	public String getUsername() {
		return userName;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.UserInfo#getReal_name()
	 */
	@Override
	// CHECKSTYLE:OFF
	public String getReal_name() {
		return realName;
	}

	// CHECKSTYLE:ON

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.UserInfo#getId()
	 */
	@Override
	public String getId() {
		return String.valueOf(id);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.wsdl.soap.v1.UserInfo#getEmail()
	 */
	@Override
	public String getEmail() {
		return email;
	}
}
