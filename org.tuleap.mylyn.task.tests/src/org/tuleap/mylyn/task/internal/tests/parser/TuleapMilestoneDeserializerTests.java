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
package org.tuleap.mylyn.task.internal.tests.parser;

import com.google.gson.JsonDeserializer;

import java.text.ParseException;

import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.parser.TuleapMilestoneDeserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

//CHECKSTYLE:OFF

/**
 * Tests the deserialization of the Tuleap milestone.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class TuleapMilestoneDeserializerTests extends AbstractDeserializerTest<TuleapMilestone> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected JsonDeserializer<TuleapMilestone> getDeserializer() {
		return new TuleapMilestoneDeserializer();
	}

	/**
	 * Test the parsing of the data set of the release 200.
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testRelease200Parsing() throws ParseException {
		String release200 = ParserUtil.loadFile("/milestones/release200.json");
		TuleapMilestone tuleapMilestone = this.parse(release200, TuleapMilestone.class);
		checkRelease200(tuleapMilestone);
	}

	/**
	 * Test the parsing of the data set of the release 300.
	 * 
	 * @throws Exception
	 *             If something goes wrong that shouldn't.
	 */
	@Test
	public void testRelease201Parsing() throws Exception {
		String release201 = ParserUtil.loadFile("/milestones/release201.json");
		TuleapMilestone tuleapMilestone = this.parse(release201, TuleapMilestone.class);
		checkRelease201(tuleapMilestone);
	}

	/**
	 * Checks the content of the given milestone corresponds to release 200. Mutualized between several tests.
	 * 
	 * @param tuleapMilestone
	 * @throws ParseException
	 */
	public void checkRelease200(TuleapMilestone tuleapMilestone) throws ParseException {
		assertEquals(200, tuleapMilestone.getId());
		assertEquals(200, tuleapMilestone.getArtifact().getId());
		assertEquals("artifacts/200", tuleapMilestone.getArtifact().getUri());
		assertEquals(901, tuleapMilestone.getArtifact().getTracker().getId());
		assertEquals("trackers/901", tuleapMilestone.getArtifact().getTracker().getUri());
		assertEquals(3, tuleapMilestone.getProject().getId());
		assertEquals("projects/3", tuleapMilestone.getProject().getUri());
		assertEquals("Release 0.9", tuleapMilestone.getLabel()); //$NON-NLS-1$
		assertEquals("milestones/200", tuleapMilestone.getUri()); //$NON-NLS-1$
		assertEquals("milestones?id=200&group_id=3", tuleapMilestone.getHtmlUrl()); //$NON-NLS-1$
		assertEquals(1, tuleapMilestone.getSubmittedBy());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 23, 11, 44, 18, 963), tuleapMilestone.getSubmittedOn());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 24, 15, 33, 18, 523), tuleapMilestone.getLastUpdatedOn());
		assertEquals(ParserUtil.getUTCDate(2013, 8, 23, 11, 44, 18, 963), tuleapMilestone.getStartDate());
		assertEquals(ParserUtil.getUTCDate(2013, 9, 23, 11, 44, 18, 963), tuleapMilestone.getEndDate());
		assertEquals(100, tuleapMilestone.getCapacity().floatValue(), 0);
		assertEquals("Done", tuleapMilestone.getStatusValue());
		assertEquals("milestones/200/milestones", tuleapMilestone.getSubMilestonesUri());
		assertEquals("milestones/200/backlog_items", tuleapMilestone.getBacklogItemsUri());
	}

	/**
	 * Checks the content of the given milestone corresponds to release 201. Mutualized between several test
	 * cases.
	 * 
	 * @param tuleapMilestone
	 * @throws ParseException
	 */
	public void checkRelease201(TuleapMilestone tuleapMilestone) throws ParseException {
		assertNotNull(tuleapMilestone);

		assertEquals(201, tuleapMilestone.getId());
		assertEquals(201, tuleapMilestone.getArtifact().getId());
		assertEquals("artifacts/201", tuleapMilestone.getArtifact().getUri());
		assertEquals(901, tuleapMilestone.getArtifact().getTracker().getId());
		assertEquals("trackers/901", tuleapMilestone.getArtifact().getTracker().getUri());
		assertEquals(3, tuleapMilestone.getProject().getId());
		assertEquals("projects/3", tuleapMilestone.getProject().getUri());
		assertEquals("Release TU", tuleapMilestone.getLabel()); //$NON-NLS-1$
		assertEquals(ParserUtil.getUTCDate(2013, 9, 23, 11, 44, 18, 963), tuleapMilestone.getStartDate());
		assertEquals(ParserUtil.getUTCDate(2013, 10, 23, 11, 44, 18, 963), tuleapMilestone.getEndDate());
		assertEquals(75, tuleapMilestone.getCapacity(), 0);
		assertEquals("milestones/201", tuleapMilestone.getUri()); //$NON-NLS-1$
		assertEquals("milestones?id=201&group_id=3", tuleapMilestone.getHtmlUrl()); //$NON-NLS-1$
		assertEquals("Current", tuleapMilestone.getStatusValue());
		assertEquals("milestones/201/milestones", tuleapMilestone.getSubMilestonesUri());
		assertEquals("milestones/201/backlog_items", tuleapMilestone.getBacklogItemsUri());
	}

	// TODO add a test with a milestone that has a parent!
}
