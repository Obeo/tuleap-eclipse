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
package org.eclipse.mylyn.tuleap.core.tests.internal.data;

import org.eclipse.mylyn.tuleap.core.internal.data.TuleapTaskId;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link TuleapTaskId}.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapTaskIdTests {
	/**
	 * Test the creation of the task data key.
	 */
	@Test
	public void testGetTaskDatakey() {
		assertThat(TuleapTaskId.getTaskDataKey("Tuleap", "Bugs", 17), is("Tuleap:Bugs-17"));
	}

	/**
	 * Test the creation of the task data id.
	 */
	@Test
	public void testGetTaskDataId() {
		assertThat(TuleapTaskId.forArtifact(123, 17, 42), is(TuleapTaskId.forName("123:17#42")));
	}

	/**
	 * Test the creation of the task data id with irrelevant identifiers.
	 */
	@Test
	public void testGetTaskDataIdWithIrrelevantElement() {
		assertThat(TuleapTaskId.forArtifact(TuleapTaskId.IRRELEVANT_ID, 17, 42).toString(), is("N/A:17#42"));
		assertThat(TuleapTaskId.forArtifact(123, TuleapTaskId.IRRELEVANT_ID, 42).toString(), is("123:N/A#42"));
		assertThat(TuleapTaskId.forArtifact(123, 17, TuleapTaskId.IRRELEVANT_ID).toString(), is("123:17#N/A"));
	}

	/**
	 * Test the retrieval of the project id from the task data id.
	 */
	@Test
	public void testGetProjectIdFromTaskDataId() {
		assertThat(TuleapTaskId.forName("123:17#42").getProjectId(), is(123));
	}

	/**
	 * Test the retrieval of an irrelevant project id from the task data id.
	 */
	@Test
	public void testGetIrrelevantProjectIdFromTaskDataId() {
		assertThat(TuleapTaskId.forName("invalidprojectid:17#42").getProjectId(),
				is(TuleapTaskId.IRRELEVANT_ID));
		assertThat(TuleapTaskId.forName(":17#42").getProjectId(), is(TuleapTaskId.IRRELEVANT_ID));
		assertThat(TuleapTaskId.forName("17#42").getProjectId(), is(17));
		assertThat(TuleapTaskId.forName("N/A:17#42").getProjectId(), is(TuleapTaskId.IRRELEVANT_ID));
	}

	/**
	 * Test the retrieval of the configuration id from the task data id.
	 */
	@Test
	public void testGetTrackerIdFromTaskDataId() {
		assertThat(TuleapTaskId.forName("123:17#42").getTrackerId(), is(17));
	}

	/**
	 * Test the retrieval of an irrelevant configuration id from the task data id.
	 */
	@Test
	public void testGetIrrelevantConfigurationIdFromTaskDataId() {
		assertThat(TuleapTaskId.forName("123:irrelevantconfigurationid#42").getTrackerId(),
				is(TuleapTaskId.IRRELEVANT_ID));
		assertThat(TuleapTaskId.forName("123:#42").getTrackerId(), is(TuleapTaskId.IRRELEVANT_ID));
		assertThat(TuleapTaskId.forName("123#42").getTrackerId(), is(TuleapTaskId.IRRELEVANT_ID));
		assertThat(TuleapTaskId.forName("123:N/A#42").getTrackerId(), is(TuleapTaskId.IRRELEVANT_ID));
	}

	/**
	 * Test the retrieval of the element id from the task data id.
	 */
	@Test
	public void testGetElementIdFromTaskDataId() {
		assertThat(TuleapTaskId.forName("123:17#42").getArtifactId(), is(42));
	}

	/**
	 * Test the retrieval of an irrelevant element id from the task data id.
	 */
	@Test
	public void testGetIrrelevantElementIdFromTaskDataId() {
		assertThat(TuleapTaskId.forName("123:17#").getArtifactId(), is(TuleapTaskId.IRRELEVANT_ID));
		assertThat(TuleapTaskId.forName("123:17#").getArtifactId(), is(TuleapTaskId.IRRELEVANT_ID));
		assertThat(TuleapTaskId.forName("123:17").getArtifactId(), is(TuleapTaskId.IRRELEVANT_ID));
		assertThat(TuleapTaskId.forName("123:17#N/A").getArtifactId(), is(TuleapTaskId.IRRELEVANT_ID));
	}

	@Test
	public void testForArtifact() {
		TuleapTaskId taskId = TuleapTaskId.forArtifact(123, 456, 789);
		assertThat(taskId.getProjectId(), is(123));
		assertThat(taskId.getTrackerId(), is(456));
		assertThat(taskId.getArtifactId(), is(789));
	}

	@Test
	public void testForNewArtifact() {
		TuleapTaskId taskId = TuleapTaskId.forNewArtifact(123, 456);
		assertThat(taskId.getProjectId(), is(123));
		assertThat(taskId.getTrackerId(), is(456));
		assertThat(taskId.getArtifactId(), is(TuleapTaskId.IRRELEVANT_ID));
	}

	@Test
	public void testForTopPlanningt() {
		TuleapTaskId taskId = TuleapTaskId.forTopPlanning(123);
		assertThat(taskId.getProjectId(), is(123));
		assertThat(taskId.getTrackerId(), is(TuleapTaskId.IRRELEVANT_ID));
		assertThat(taskId.getArtifactId(), is(123));
	}

	@Test
	public void testIsTopPlanning() {
		TuleapTaskId taskId = TuleapTaskId.forTopPlanning(123);
		assertThat(taskId.isTopPlanning(), is(true));

		taskId = TuleapTaskId.forNewArtifact(123, 456);
		assertThat(taskId.isTopPlanning(), is(false));

		taskId = TuleapTaskId.forArtifact(123, 456, 789);
		assertThat(taskId.isTopPlanning(), is(false));
	}

	@Test
	public void testGettingTaskIdFromWrongUrl() {
		TuleapTaskId taskId = TuleapTaskId.forTaskUrl("url/url");
		assertThat(taskId.toString(), is("N/A:N/A#N/A"));
	}

	@Test
	public void testGettingTaskIdFromGoodUrl() {
		TuleapTaskId taskId = TuleapTaskId
				.forTaskUrl("https://demo.tuleap.net/plugins/tracker/?group_id=4&tracker=34&aid=27");
		assertThat(taskId.toString(), is("4:34#27"));
		assertEquals(TuleapTaskId.forArtifact(4, 34, 27).toString(), taskId.toString());
	}

	@Test
	public void testGetTaskUrl() {
		TuleapTaskId taskId = TuleapTaskId.forArtifact(1, 4, 273);
		String htmlUrl = taskId.getTaskUrl("https://tuleap.net");
		assertThat("https://tuleap.net/plugins/tracker/?group_id=1&tracker=4&aid=273", is(htmlUrl));
	}
}
