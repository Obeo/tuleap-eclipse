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
package org.tuleap.mylyn.task.internal.tests.validator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.tests.AbstractTuleapTests;
import org.tuleap.mylyn.task.internal.ui.repository.TuleapValidator;

import static org.junit.Assert.fail;

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
	@Test
	public void testUserConnection() {
		fail("refactor to not use the connection, dependency injection of the clients!");
		TuleapValidator validator = new TuleapValidator(this.repository);
		try {
			validator.validate(new NullProgressMonitor());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.tests.AbstractTuleapTests#getServerUrl()
	 */
	@Override
	public String getServerUrl() {
		return "https://tuleap.net"; //$NON-NLS-1$
	}
}
