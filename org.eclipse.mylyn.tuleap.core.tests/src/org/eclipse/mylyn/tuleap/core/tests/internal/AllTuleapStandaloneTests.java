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
package org.eclipse.mylyn.tuleap.core.tests.internal;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.textui.TestRunner;

import org.eclipse.mylyn.tuleap.core.tests.internal.client.rest.RestOperationsTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.client.rest.RestResourceTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.client.rest.TuleapRestClientLimitTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.client.rest.TuleapRestClientTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.client.rest.TuleapRestResourceFactoryTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.client.soap.TuleapSoapConnectorTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.client.soap.TuleapSoapParserTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.converter.MilestoneTaskDataConverterTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.data.TuleapArtifactMapperTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.data.TuleapTaskIdTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.model.TuleapProjectConfigurationTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.model.TuleapTrackerConfigurationTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.model.TuleapWorkflowTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.parser.ArtifactLinkAdapterTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.parser.TuleapCardwallConfigurationDeserializerTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.parser.TuleapJsonParserTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.repository.TuleapRepositoryConnectorTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.repository.TuleapTaskDataHandlerTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.serializer.TuleapArtifactSerializerTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.serializer.TuleapArtifactWithCommentSerializerTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.serializer.TuleapCardSerializerTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.serializer.TuleapMilestoneSerializerTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.server.ServerResponseTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * The stand alone unit tests suite (run as standard junit test).
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 0.7
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TuleapSoapParserTests.class, TuleapTaskIdTests.class,
		TuleapTrackerConfigurationTests.class, ServerResponseTest.class,
		TuleapTrackerConfigurationTests.class, ServerResponseTest.class,
		TuleapProjectConfigurationTests.class, TuleapCardwallConfigurationDeserializerTests.class,
		TuleapMilestoneSerializerTests.class, MilestoneTaskDataConverterTest.class,
		TuleapSoapConnectorTests.class, TuleapRepositoryConnectorTests.class, TuleapJsonParserTest.class,
		TuleapTaskDataHandlerTests.class, TuleapWorkflowTests.class, TuleapArtifactMapperTests.class,
		TuleapRestResourceFactoryTest.class, RestResourceTest.class, RestOperationsTest.class,
		TuleapRestClientTest.class, TuleapCardSerializerTest.class, TuleapRestClientLimitTests.class,
		TuleapArtifactSerializerTest.class, TuleapArtifactWithCommentSerializerTest.class,
		ArtifactLinkAdapterTest.class })
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
