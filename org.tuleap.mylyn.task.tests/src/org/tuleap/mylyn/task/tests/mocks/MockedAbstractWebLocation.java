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

import java.net.Proxy;

import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;

/**
 * A mock of an abstract web location.
 * 
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @since 0.7
 */
public class MockedAbstractWebLocation extends AbstractWebLocation {

	/**
	 * Constructor.
	 * 
	 * @param url
	 *            URL
	 */
	public MockedAbstractWebLocation(String url) {
		super(url);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.commons.net.AbstractWebLocation#getCredentials(org.eclipse.mylyn.commons.net.AuthenticationType)
	 */
	@Override
	public AuthenticationCredentials getCredentials(AuthenticationType type) {
		return new AuthenticationCredentials("userName", "password"); //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.commons.net.AbstractWebLocation#getProxyForHost(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Proxy getProxyForHost(String host, String proxyType) {
		return null;
	}

}
