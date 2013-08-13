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
import org.tuleap.mylyn.task.internal.tests.client.TuleapArtifactTests;
import org.tuleap.mylyn.task.internal.tests.client.TuleapRepositoryConnectorTests;
import org.tuleap.mylyn.task.internal.tests.client.TuleapTaskDataHandlerTests;
import org.tuleap.mylyn.task.internal.tests.client.TuleapUtilTests;
import org.tuleap.mylyn.task.internal.tests.model.TuleapWorkflowTests;
import org.tuleap.mylyn.task.internal.tests.parser.TuleapProjectConfigurationDeserializerTests;
import org.tuleap.mylyn.task.internal.tests.parser.TuleapTrackerConfigurationDeserializerTests;

/**
 * The stand alone unit tests suite.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TuleapArtifactTests.class, TuleapUtilTests.class, TuleapRepositoryConnectorTests.class,
		TuleapTaskDataHandlerTests.class, TuleapProjectConfigurationDeserializerTests.class,
		TuleapTrackerConfigurationDeserializerTests.class, TuleapWorkflowTests.class })
public final class AllTuleapHeadlessStandaloneTests {

	/**
	 * The constructor.
	 */
	private AllTuleapHeadlessStandaloneTests() {
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
		return new JUnit4TestAdapter(AllTuleapHeadlessStandaloneTests.class);
	}
}
