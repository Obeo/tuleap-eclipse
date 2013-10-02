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
import org.tuleap.mylyn.task.internal.tests.model.TuleapProjectConfigurationTests;
import org.tuleap.mylyn.task.internal.tests.model.TuleapTrackerConfigurationTests;
import org.tuleap.mylyn.task.internal.tests.parser.TuleapBacklogItemDeserializerTest;
import org.tuleap.mylyn.task.internal.tests.parser.TuleapBacklogItemTypeDeserializerTests;
import org.tuleap.mylyn.task.internal.tests.parser.TuleapCardTypeDeserializerTests;
import org.tuleap.mylyn.task.internal.tests.parser.TuleapJsonParserTest;
import org.tuleap.mylyn.task.internal.tests.parser.TuleapMilestoneDeserializerTests;
import org.tuleap.mylyn.task.internal.tests.parser.TuleapMilestoneTypeDeserializerTests;
import org.tuleap.mylyn.task.internal.tests.parser.TuleapProjectConfigurationDeserializerTests;
import org.tuleap.mylyn.task.internal.tests.parser.TuleapTrackerConfigurationDeserializerTests;
import org.tuleap.mylyn.task.internal.tests.server.ServerResponseTest;

/**
 * The stand alone unit tests suite (run as standard junit test).
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 0.7
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TuleapProjectConfigurationDeserializerTests.class,
		TuleapTrackerConfigurationDeserializerTests.class, TuleapTrackerConfigurationTests.class,
		TuleapBacklogItemDeserializerTest.class, TuleapBacklogItemTypeDeserializerTests.class,
		TuleapMilestoneTypeDeserializerTests.class, TuleapMilestoneDeserializerTests.class,
		TuleapCardTypeDeserializerTests.class, ServerResponseTest.class, TuleapJsonParserTest.class,
		TuleapProjectConfigurationTests.class })
public final class AllTuleapStandaloneTests {

	/**
	 * The constructor.
	 */
	private AllTuleapStandaloneTests() {
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
		return new JUnit4TestAdapter(AllTuleapStandaloneTests.class);
	}
}
