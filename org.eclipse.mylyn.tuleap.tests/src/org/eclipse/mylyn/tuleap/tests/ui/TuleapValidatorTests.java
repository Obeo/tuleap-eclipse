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
package org.eclipse.mylyn.tuleap.tests.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.internal.tuleap.core.util.TuleapUtil;
import org.eclipse.mylyn.internal.tuleap.ui.repository.TuleapValidator;
import org.eclipse.mylyn.tuleap.tests.AbstractTuleapTests;

/**
 * This test class will be used to validate the settings of a task repository.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapValidatorTests extends AbstractTuleapTests {

	/**
	 * Test a connection with an user account.
	 */
	public void testUserConnection() {
		TuleapValidator validator = new TuleapValidator(this.repository);
		try {
			validator.validate(new NullProgressMonitor());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test if an URL is valid.
	 */
	public void testValidUrl() {
		assertFalse(TuleapUtil.isValidUrl("")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("http:/google.com")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("http:/google.com/")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("http://google.com/")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("http://google.com/foo /space")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("http://google.com")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("https://google.com")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("http://mylyn.org/trac30")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("http://www.mylyn.org/trac30")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("https://plugins/tracker/?tracker=42")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("https://my.demo.domain/plugins/tracker/?tracker=aa")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("https://my.demo.domain /plugins/tracker/?tracker=42")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("https://my.demo.domain/plugins/tracker/?tracker=42x")); //$NON-NLS-1$
		assertTrue(TuleapUtil.isValidUrl("https://my.demo.domain/subdomain/plugins/tracker/?group_id=42")); //$NON-NLS-1$
		assertTrue(TuleapUtil.isValidUrl("https://my.demo.domain/plugins/tracker/?group_id=42")); //$NON-NLS-1$
	}
}
