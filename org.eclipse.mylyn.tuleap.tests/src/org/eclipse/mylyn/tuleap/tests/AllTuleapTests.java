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

import org.eclipse.mylyn.commons.sdk.util.ManagedTestSuite;
import org.eclipse.mylyn.commons.sdk.util.TestConfiguration;
import org.eclipse.mylyn.tuleap.tests.core.TuleapTaskDataHandlerTests;
import org.eclipse.mylyn.tuleap.tests.support.TuleapFixture;
import org.eclipse.mylyn.tuleap.tests.ui.TuleapRepositorySettingsPageTests;

/**
 * This class should be used to launch all Tuleap unit tests.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
// Test configuration
@SuppressWarnings("restriction")
public final class AllTuleapTests {

	/**
	 * The constructor.
	 */
	private AllTuleapTests() {
		// prevent instantiation
	}

	/**
	 * Returns the test suite with all Tuleap tests.
	 * 
	 * @return The test suite with all Tuleap tests.
	 */
	public static Test suite() {
		TestSuite suite = new ManagedTestSuite(AllTuleapTests.class.getName());
		addTests(suite, TestConfiguration.getDefault());
		return suite;
	}

	// CHECKSTYLE:OFF (suite should not have any parameters)
	/**
	 * Returns the test suite configured with the given test configuration.
	 * 
	 * @param configuration
	 *            The test configuration
	 * @return The test suite configured with the given test configuration.
	 */
	public static Test suite(TestConfiguration configuration) {
		// CHECKSTYLE:ON
		TestSuite suite = new TestSuite(AllTuleapTests.class.getName());
		addTests(suite, configuration);
		return suite;
	}

	/**
	 * Adds the tests to the tests suite for the given configuration.
	 * 
	 * @param suite
	 *            The test suite
	 * @param configuration
	 *            The test configuration
	 */
	public static void addTests(TestSuite suite, TestConfiguration configuration) {
		// Standalone tests (Don't require an instance of Eclipse)
		suite.addTest(AllTuleapHeadlessStandaloneTests.suite(configuration));

		// Tests that only need to run once (i.e. no network io so doesn't matter which repository)
		// suite.addTestSuite(BugzillaHyperlinkDetectorTest.class);

		// network tests

		// local tests
		if (!configuration.isLocalOnly()) {
			suite.addTestSuite(TuleapRepositorySettingsPageTests.class);
			suite.addTestSuite(TuleapTaskDataHandlerTests.class);
			// suite.addTestSuite(BugzillaTaskEditorTest.class);
			// suite.addTestSuite(BugzillaSearchPageTest.class);
			// suite.addTestSuite(BugzillaRepositorySettingsPageTest.class);
			// if (configuration.isDefaultOnly()) {
			// addTests(suite, BugzillaFixture.DEFAULT);
			// } else {
			// for (BugzillaFixture fixture : BugzillaFixture.ALL) {
			// addTests(suite, fixture);
			// }
			// }
		}
	}

	/**
	 * Adds the tests to the tests suite with the given fixture.
	 * 
	 * @param suite
	 *            The test suite to run
	 * @param fixture
	 *            The Tuleap fixture to use
	 */
	public static void addTests(TestSuite suite, TuleapFixture fixture) {
		if (fixture.isExcluded()) {
			return;
		}

		fixture.createSuite(suite);
		// fixture.add(BugzillaAttachmentHandlerTest.class);

		fixture.done();
	}
}
