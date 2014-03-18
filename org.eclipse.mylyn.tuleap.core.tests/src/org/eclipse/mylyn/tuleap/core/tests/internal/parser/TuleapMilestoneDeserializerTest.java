/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.tuleap.core.tests.internal.parser;

import com.google.gson.Gson;

import java.text.ParseException;

import org.eclipse.mylyn.tuleap.core.internal.model.data.agile.TuleapMilestone;
import org.eclipse.mylyn.tuleap.core.internal.parser.TuleapGsonProvider;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tests the JSON deserialization of {@link TuleapMilestone}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapMilestoneDeserializerTest {

	private Gson gson;

	@Before
	public void setUp() {
		gson = TuleapGsonProvider.defaultGson();
	}

	@Test
	public void testDeserializeRelease200() throws ParseException {
		String json = ParserUtil.loadFile("/milestones/release200.json");
		TuleapMilestone milestone = gson.fromJson(json, TuleapMilestone.class);
		checkRelease200(milestone);
	}

	/**
	 * Checks the content of the given milestone corresponds to release 200. Mutualized between several tests.
	 * 
	 * @param tuleapMilestone
	 * @throws ParseException
	 */
	public static void checkRelease200(TuleapMilestone tuleapMilestone) throws ParseException {
		assertEquals(200, tuleapMilestone.getId().intValue());
		assertEquals(200, tuleapMilestone.getArtifact().getId());
		assertEquals("artifacts/200", tuleapMilestone.getArtifact().getUri());
		assertEquals(901, tuleapMilestone.getArtifact().getTracker().getId());
		assertEquals("trackers/901", tuleapMilestone.getArtifact().getTracker().getUri());
		assertEquals(201, tuleapMilestone.getParent().getId());
		assertEquals("milestones/201", tuleapMilestone.getParent().getUri());
		assertEquals(901, tuleapMilestone.getParent().getTracker().getId());
		assertEquals("trackers/901", tuleapMilestone.getParent().getTracker().getUri());
		assertEquals(3, tuleapMilestone.getProject().getId());
		assertEquals("projects/3", tuleapMilestone.getProject().getUri());
		assertEquals("Release 0.9", tuleapMilestone.getLabel()); //$NON-NLS-1$
		assertEquals("milestones/200", tuleapMilestone.getUri()); //$NON-NLS-1$
		assertNull(tuleapMilestone.getHtmlUrl());
		assertEquals(1, tuleapMilestone.getSubmittedBy());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 23, 11, 44, 18, 963), tuleapMilestone.getSubmittedOn());
		assertNull(tuleapMilestone.getLastModifiedDate());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 23, 11, 44, 18, 963), tuleapMilestone.getStartDate());
		assertEquals(ParserUtil.getUTCDate(2013, 9, 23, 11, 44, 18, 963), tuleapMilestone.getEndDate());
		assertEquals("100", tuleapMilestone.getCapacity());
		assertEquals("Done", tuleapMilestone.getStatusValue());
		assertEquals("milestones/200/milestones", tuleapMilestone.getSubMilestonesUri());
		assertEquals("milestones/200/backlog", tuleapMilestone.getBacklogUri());
		assertEquals("milestones/200/content", tuleapMilestone.getContentUri());
	}

	/**
	 * Test the parsing of the data set of the release 300.
	 * 
	 * @throws Exception
	 *             If something goes wrong that shouldn't.
	 */
	@Test
	public void testDeserializeRelease201() throws Exception {
		String json = ParserUtil.loadFile("/milestones/release201.json");
		TuleapMilestone tuleapMilestone = gson.fromJson(json, TuleapMilestone.class);
		checkRelease201(tuleapMilestone);
	}

	/**
	 * Checks the content of the given milestone corresponds to release 201. Mutualized between several test
	 * cases.
	 * 
	 * @param tuleapMilestone
	 * @throws ParseException
	 */
	public static void checkRelease201(TuleapMilestone tuleapMilestone) throws ParseException {
		assertNotNull(tuleapMilestone);

		assertEquals(201, tuleapMilestone.getId().intValue());
		assertEquals(201, tuleapMilestone.getArtifact().getId());
		assertEquals("artifacts/201", tuleapMilestone.getArtifact().getUri());
		assertEquals(901, tuleapMilestone.getArtifact().getTracker().getId());
		assertEquals("trackers/901", tuleapMilestone.getArtifact().getTracker().getUri());
		assertEquals(202, tuleapMilestone.getParent().getId());
		assertEquals("milestones/202", tuleapMilestone.getParent().getUri());
		assertEquals(901, tuleapMilestone.getParent().getTracker().getId());
		assertEquals("trackers/901", tuleapMilestone.getParent().getTracker().getUri());
		assertEquals(3, tuleapMilestone.getProject().getId());
		assertEquals("projects/3", tuleapMilestone.getProject().getUri());
		assertEquals("Release TU", tuleapMilestone.getLabel()); //$NON-NLS-1$
		assertNull(tuleapMilestone.getLastModifiedDate());
		assertEquals(ParserUtil.getUTCDate(2013, 9, 23, 11, 44, 18, 963), tuleapMilestone.getStartDate());
		assertEquals(ParserUtil.getUTCDate(2013, 10, 23, 11, 44, 18, 963), tuleapMilestone.getEndDate());
		assertEquals("75", tuleapMilestone.getCapacity());
		assertEquals("milestones/201", tuleapMilestone.getUri()); //$NON-NLS-1$
		assertNull(tuleapMilestone.getHtmlUrl());
		assertEquals("Current", tuleapMilestone.getStatusValue());
		assertEquals("milestones/201/milestones", tuleapMilestone.getSubMilestonesUri());
		assertEquals("milestones/201/backlog", tuleapMilestone.getBacklogUri());
		assertEquals("milestones/201/content", tuleapMilestone.getContentUri());
	}
}
