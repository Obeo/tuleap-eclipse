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
package org.tuleap.mylyn.task.core.tests.internal;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.textui.TestRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.tuleap.mylyn.task.core.tests.internal.client.rest.RestOperationsTest;
import org.tuleap.mylyn.task.core.tests.internal.client.rest.RestResourceTest;
import org.tuleap.mylyn.task.core.tests.internal.client.rest.TuleapRestClientQueryTest;
import org.tuleap.mylyn.task.core.tests.internal.client.rest.TuleapRestClientTest;
import org.tuleap.mylyn.task.core.tests.internal.client.rest.TuleapRestResourceFactoryTest;
import org.tuleap.mylyn.task.core.tests.internal.converter.ArtifactTaskDataConverterTest;
import org.tuleap.mylyn.task.core.tests.internal.data.TuleapArtifactMapperTests;
import org.tuleap.mylyn.task.core.tests.internal.data.TuleapTaskIdTests;
import org.tuleap.mylyn.task.core.tests.internal.model.QueryCriterionTest;
import org.tuleap.mylyn.task.core.tests.internal.model.TuleapProjectConfigurationTests;
import org.tuleap.mylyn.task.core.tests.internal.model.TuleapTrackerConfigurationTests;
import org.tuleap.mylyn.task.core.tests.internal.model.TuleapWorkflowTests;
import org.tuleap.mylyn.task.core.tests.internal.parser.ArtifactLinkAdapterTest;
import org.tuleap.mylyn.task.core.tests.internal.parser.DateIso8601AdapterTest;
import org.tuleap.mylyn.task.core.tests.internal.parser.TuleapArtifactDeserializerTest;
import org.tuleap.mylyn.task.core.tests.internal.parser.TuleapChangesetDeserializerTests;
import org.tuleap.mylyn.task.core.tests.internal.parser.TuleapFileDeserializerTest;
import org.tuleap.mylyn.task.core.tests.internal.parser.TuleapJsonParserTest;
import org.tuleap.mylyn.task.core.tests.internal.parser.TuleapProjectDeserializerTest;
import org.tuleap.mylyn.task.core.tests.internal.parser.TuleapTrackerDeserializerTest;
import org.tuleap.mylyn.task.core.tests.internal.parser.TuleapTrackerReportDeserializerTest;
import org.tuleap.mylyn.task.core.tests.internal.parser.TuleapUserDeserializerTest;
import org.tuleap.mylyn.task.core.tests.internal.repository.TuleapAttachmentInputStreamTest;
import org.tuleap.mylyn.task.core.tests.internal.repository.TuleapRepositoryConnectorTests;
import org.tuleap.mylyn.task.core.tests.internal.repository.TuleapTaskAttachmentHandlerTests;
import org.tuleap.mylyn.task.core.tests.internal.repository.TuleapTaskDataHandlerTests;
import org.tuleap.mylyn.task.core.tests.internal.serializer.BoundFieldValueSerializerTest;
import org.tuleap.mylyn.task.core.tests.internal.serializer.LiteralFieldValueSerializerTest;
import org.tuleap.mylyn.task.core.tests.internal.serializer.TuleapArtifactSerializerTest;
import org.tuleap.mylyn.task.core.tests.internal.serializer.TuleapArtifactWithCommentSerializerTest;
import org.tuleap.mylyn.task.core.tests.internal.server.ServerResponseTest;

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
		TuleapRestClientQueryTest.class,
		TuleapRestResourceFactoryTest.class,
		// converter
		ArtifactTaskDataConverterTest.class,
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
		TuleapChangesetDeserializerTests.class, TuleapFileDeserializerTest.class, TuleapJsonParserTest.class,
		TuleapProjectDeserializerTest.class, TuleapTrackerDeserializerTest.class,
		TuleapTrackerReportDeserializerTest.class,
		TuleapUserDeserializerTest.class,
		// repository
		TuleapAttachmentInputStreamTest.class, TuleapRepositoryConnectorTests.class,
		TuleapTaskAttachmentHandlerTests.class, TuleapTaskDataHandlerTests.class,
		// serializer
		BoundFieldValueSerializerTest.class, LiteralFieldValueSerializerTest.class,
		TuleapArtifactSerializerTest.class, TuleapArtifactWithCommentSerializerTest.class,
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
