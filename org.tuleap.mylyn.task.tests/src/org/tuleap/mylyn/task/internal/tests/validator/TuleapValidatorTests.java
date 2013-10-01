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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.tasks.core.TaskRepositoryLocationFactory;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestClient;
import org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapClient;
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
	public void testValidUserConnection() {
		AbstractWebLocation location = new TaskRepositoryLocationFactory().createWebLocation(this.repository);

		TuleapSoapClient tuleapSoapClient = new TuleapSoapClient(this.repository, location, null, null, null) {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.tuleap.mylyn.task.internal.core.client.soap.TuleapSoapClient#validateConnection(org.eclipse.core.runtime.IProgressMonitor)
			 */
			@Override
			public IStatus validateConnection(IProgressMonitor monitor) throws CoreException {
				return Status.OK_STATUS;
			}
		};

		TuleapRestClient tuleapRestClient = new TuleapRestClient(null, null, null, this.repository, null) {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.tuleap.mylyn.task.internal.core.client.rest.TuleapRestClient#validateConnection(org.eclipse.core.runtime.IProgressMonitor)
			 */
			@Override
			public IStatus validateConnection(IProgressMonitor monitor) throws CoreException {
				return Status.OK_STATUS;
			}
		};

		TuleapValidator validator = new TuleapValidator(location, tuleapSoapClient, tuleapRestClient,
				this.repository);
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
