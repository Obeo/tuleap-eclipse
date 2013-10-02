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
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskIdentityUtil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Test class for {@link TuleapTaskIdentityUtil}.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapTaskIdentityUtilTests {
	/**
	 * Test the creation of the task data key.
	 */
	@Test
	public void testGetTaskDatakey() {
		assertThat(TuleapTaskIdentityUtil.getTaskDataKey("Tuleap", "Bugs", 17), is("Tuleap:Bugs-17"));
	}

	/**
	 * Test the creation of the task data id.
	 */
	@Test
	public void testGetTaskDataId() {
		assertThat(TuleapTaskIdentityUtil.getTaskDataId(123, 17, 42), is("123:17#42"));
	}

	/**
	 * Test the creation of the task data id with irrelevant identifiers.
	 */
	@Test
	public void testGetTaskDataIdWithIrrelevantElement() {
		assertThat(TuleapTaskIdentityUtil.getTaskDataId(TuleapTaskIdentityUtil.IRRELEVANT_ID, 17, 42),
				is("N/A:17#42"));
		assertThat(TuleapTaskIdentityUtil.getTaskDataId(123, TuleapTaskIdentityUtil.IRRELEVANT_ID, 42),
				is("123:N/A#42"));
		assertThat(TuleapTaskIdentityUtil.getTaskDataId(123, 17, TuleapTaskIdentityUtil.IRRELEVANT_ID),
				is("123:17#N/A"));
	}

	/**
	 * Test the retrieval of the project id from the task data id.
	 */
	@Test
	public void testGetProjectIdFromTaskDataId() {
		assertThat(TuleapTaskIdentityUtil.getProjectIdFromTaskDataId("123:17#42"), is(123));
	}

	/**
	 * Test the retrieval of an irrelevant project id from the task data id.
	 */
	@Test
	public void testGetIrrelevantProjectIdFromTaskDataId() {
		assertThat(TuleapTaskIdentityUtil.getProjectIdFromTaskDataId("invalidprojectid:17#42"),
				is(TuleapTaskIdentityUtil.IRRELEVANT_ID));
		assertThat(TuleapTaskIdentityUtil.getProjectIdFromTaskDataId(":17#42"),
				is(TuleapTaskIdentityUtil.IRRELEVANT_ID));
		assertThat(TuleapTaskIdentityUtil.getProjectIdFromTaskDataId("17#42"),
				is(TuleapTaskIdentityUtil.IRRELEVANT_ID));
		assertThat(TuleapTaskIdentityUtil.getProjectIdFromTaskDataId("N/A:17#42"),
				is(TuleapTaskIdentityUtil.IRRELEVANT_ID));
	}

	/**
	 * Test the retrieval of the configuration id from the task data id.
	 */
	@Test
	public void testGetConfigurationIdFromTaskDataId() {
		assertThat(TuleapTaskIdentityUtil.getConfigurationIdFromTaskDataId("123:17#42"), is(17));
	}

	/**
	 * Test the retrieval of an irrelevant configuration id from the task data id.
	 */
	@Test
	public void testGetIrrelevantConfigurationIdFromTaskDataId() {
		assertThat(TuleapTaskIdentityUtil
				.getConfigurationIdFromTaskDataId("123:irrelevantconfigurationid#42"),
				is(TuleapTaskIdentityUtil.IRRELEVANT_ID));
		assertThat(TuleapTaskIdentityUtil.getConfigurationIdFromTaskDataId("123:#42"),
				is(TuleapTaskIdentityUtil.IRRELEVANT_ID));
		assertThat(TuleapTaskIdentityUtil.getConfigurationIdFromTaskDataId("123#42"),
				is(TuleapTaskIdentityUtil.IRRELEVANT_ID));
		assertThat(TuleapTaskIdentityUtil.getConfigurationIdFromTaskDataId("123:N/A#42"),
				is(TuleapTaskIdentityUtil.IRRELEVANT_ID));
	}

	/**
	 * Test the retrieval of the element id from the task data id.
	 */
	@Test
	public void testGetElementIdFromTaskDataId() {
		assertThat(TuleapTaskIdentityUtil.getElementIdFromTaskDataId("123:17#42"), is(42));
	}

	/**
	 * Test the retrieval of an irrelevant element id from the task data id.
	 */
	@Test
	public void testGetIrrelevantElementIdFromTaskDataId() {
		assertThat(TuleapTaskIdentityUtil.getElementIdFromTaskDataId("123:17#"),
				is(TuleapTaskIdentityUtil.IRRELEVANT_ID));
		assertThat(TuleapTaskIdentityUtil.getElementIdFromTaskDataId("123:17#"),
				is(TuleapTaskIdentityUtil.IRRELEVANT_ID));
		assertThat(TuleapTaskIdentityUtil.getElementIdFromTaskDataId("123:17"),
				is(TuleapTaskIdentityUtil.IRRELEVANT_ID));
		assertThat(TuleapTaskIdentityUtil.getElementIdFromTaskDataId("123:17#N/A"),
				is(TuleapTaskIdentityUtil.IRRELEVANT_ID));
	}
}
