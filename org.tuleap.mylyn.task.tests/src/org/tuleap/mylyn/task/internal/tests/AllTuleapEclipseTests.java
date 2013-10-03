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
package org.tuleap.mylyn.task.internal.tests;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.textui.TestRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.tuleap.mylyn.task.internal.tests.client.soap.TuleapSoapParserTests;
import org.tuleap.mylyn.task.internal.tests.data.TuleapConfigurableElementMapperTests;
import org.tuleap.mylyn.task.internal.tests.model.TuleapWorkflowTests;
import org.tuleap.mylyn.task.internal.tests.repository.TuleapRepositoryConnectorTests;
import org.tuleap.mylyn.task.internal.tests.repository.TuleapTaskDataHandlerTests;
import org.tuleap.mylyn.task.internal.tests.server.TaskRepositoryCredentialsTest;

/**
 * The suite of unit tests that run in "eclipse plug-in" mode.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TuleapSoapParserTests.class, TuleapRepositoryConnectorTests.class,
		TuleapTaskDataHandlerTests.class, TuleapWorkflowTests.class, TaskRepositoryCredentialsTest.class,
		TuleapConfigurableElementMapperTests.class })
public final class AllTuleapEclipseTests {

	/**
	 * The constructor.
	 */
	private AllTuleapEclipseTests() {
		// prevent instantiation
	}

	/**
	 * Launches the test with the given arguments.
	 * 
	 * @param args
	 *            Arguments of the testCase.
	 */
	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	/**
	 * Creates the {@link junit.framework.TestSuite TestSuite} for all the test.
	 * 
	 * @return The test suite containing all the tests
	 */
	public static Test suite() {
		return new JUnit4TestAdapter(AllTuleapEclipseTests.class);
	}
}
