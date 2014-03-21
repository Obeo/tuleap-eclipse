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
import org.eclipse.mylyn.tuleap.core.tests.internal.converter.MilestoneTaskDataConverterTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.data.TuleapArtifactMapperTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.data.TuleapTaskIdTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.model.QueryCriterionTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.model.TuleapProjectConfigurationTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.model.TuleapTrackerConfigurationTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.model.TuleapWorkflowTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.parser.ArtifactLinkAdapterTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.parser.DateIso8601AdapterTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.parser.TuleapArtifactDeserializerTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.parser.TuleapBacklogItemDeserializerTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.parser.TuleapBurndownDeserializerTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.parser.TuleapCardwallConfigurationDeserializerTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.parser.TuleapChangesetDeserializerTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.parser.TuleapFileDeserializerTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.parser.TuleapJsonParserTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.parser.TuleapMilestoneDeserializerTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.parser.TuleapPlanningDeserializerTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.parser.TuleapProjectDeserializerTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.parser.TuleapTrackerDeserializerTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.parser.TuleapTrackerReportDeserializerTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.parser.TuleapUserDeserializerTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.repository.TuleapAttachmentInputStreamTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.repository.TuleapRepositoryConnectorTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.repository.TuleapTaskAttachmentHandlerTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.repository.TuleapTaskDataHandlerTests;
import org.eclipse.mylyn.tuleap.core.tests.internal.serializer.BoundFieldValueSerializerTest;
import org.eclipse.mylyn.tuleap.core.tests.internal.serializer.LiteralFieldValueSerializerTest;
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
@Suite.SuiteClasses({
	// client.rest
	RestOperationsTest.class,
	RestResourceTest.class,
	TuleapRestClientTest.class,
	TuleapRestClientLimitTests.class,
	TuleapRestResourceFactoryTest.class,
	// converter
	MilestoneTaskDataConverterTest.class,
	// data
	TuleapArtifactMapperTests.class,
	TuleapTaskIdTests.class,
	// model
	QueryCriterionTest.class,
	TuleapProjectConfigurationTests.class,
	TuleapTrackerConfigurationTests.class,
	TuleapWorkflowTests.class,
	// parser
	ArtifactLinkAdapterTest.class, DateIso8601AdapterTest.class, TuleapArtifactDeserializerTest.class,
	TuleapBacklogItemDeserializerTest.class, TuleapBurndownDeserializerTest.class,
	TuleapCardwallConfigurationDeserializerTests.class, TuleapChangesetDeserializerTests.class,
	TuleapFileDeserializerTest.class, TuleapJsonParserTest.class, TuleapMilestoneDeserializerTest.class,
	TuleapPlanningDeserializerTest.class,
	TuleapProjectDeserializerTest.class,
	TuleapTrackerDeserializerTest.class,
	TuleapTrackerReportDeserializerTest.class,
	TuleapUserDeserializerTest.class,
	// repository
	TuleapAttachmentInputStreamTest.class, TuleapRepositoryConnectorTests.class,
	TuleapTaskAttachmentHandlerTests.class,
	TuleapTaskDataHandlerTests.class,
	// serializer
	BoundFieldValueSerializerTest.class, LiteralFieldValueSerializerTest.class,
	TuleapArtifactSerializerTest.class, TuleapArtifactWithCommentSerializerTest.class,
	TuleapCardSerializerTest.class, TuleapMilestoneSerializerTests.class,
	// server
	ServerResponseTest.class, })
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
