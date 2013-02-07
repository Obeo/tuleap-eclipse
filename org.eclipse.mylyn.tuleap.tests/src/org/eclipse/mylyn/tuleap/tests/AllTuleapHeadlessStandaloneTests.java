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
package org.eclipse.mylyn.tuleap.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.mylyn.commons.sdk.util.TestConfiguration;
import org.eclipse.mylyn.tuleap.tests.client.TuleapArtifactTests;
import org.eclipse.mylyn.tuleap.tests.core.TuleapRepositoryConnectorTests;
import org.eclipse.mylyn.tuleap.tests.core.TuleapUtilTests;
import org.eclipse.mylyn.tuleap.tests.support.TuleapFixture;

/**
 * The stand alone unit tests suite.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
// Test configuration
@SuppressWarnings("restriction")
public final class AllTuleapHeadlessStandaloneTests {

	/**
	 * The constructor.
	 */
	private AllTuleapHeadlessStandaloneTests() {
		// Prevent instantiation
	}

	/**
	 * Returns the headless stand alone test suite for Tuleap.
	 * 
	 * @return The headless stand alone test suite for Tuleap.
	 */
	public static Test suite() {
		return suite(TestConfiguration.getDefault());
	}

	// CHECKSTYLE:OFF (suite should not have any parameters)
	/**
	 * Returns the headless stand alone tests suite for the given configuration.
	 * 
	 * @param configuration
	 *            The Mylyn test configuration
	 * @return The headless stand alone tests suite for the given configuration.
	 */
	public static Test suite(TestConfiguration configuration) {
		// CHECKSTYLE:ON
		TestSuite suite = new TestSuite(AllTuleapHeadlessStandaloneTests.class.getName());
		suite.addTestSuite(TuleapArtifactTests.class);
		suite.addTestSuite(TuleapUtilTests.class);
		suite.addTestSuite(TuleapRepositoryConnectorTests.class);
		// suite.addTestSuite(TuleapTaskDataHandlerTests.class);
		return suite;
	}

	/**
	 * Adds unit tests to the given test suite.
	 * 
	 * @param suite
	 *            The tests suite
	 * @param fixture
	 *            The Tuleap fixture
	 */
	public static void addTests(TestSuite suite, TuleapFixture fixture) {
		fixture.createSuite(suite);
		fixture.done();
	}
}
