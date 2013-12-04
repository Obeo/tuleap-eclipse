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
package org.tuleap.mylyn.task.internal.tests.data;

import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskId;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
}
