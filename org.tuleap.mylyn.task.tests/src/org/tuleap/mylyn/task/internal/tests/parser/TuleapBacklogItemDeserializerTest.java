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
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.parser.TuleapBacklogItemDeserializer;

import static org.junit.Assert.assertEquals;

/**
 * Unit Tests of Backlog item deserializer.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapBacklogItemDeserializerTest extends AbstractDeserializerTest<TuleapBacklogItem> {

	/**
	 * Tests the parsing of epic #301.
	 * 
	 * @throws ParseException
	 *             if the test is badly configured
	 */
	@Test
	public void testDeserializeEpic301() throws ParseException {
		String epic = ParserUtil.loadFile("/backlog_items/epic301.json");
		TuleapBacklogItem item = parse(epic, TuleapBacklogItem.class);
		checkEpic301(item);
	}

	/**
	 * Checks that the given backlog item corresponds to epic 301.
	 * 
	 * @param item
	 *            The backlog item
	 * @throws ParseException
	 *             if the test is badly configured
	 */
	public static void checkEpic301(TuleapBacklogItem item) throws ParseException {
		assertEquals(301, item.getId());
		assertEquals(301, item.getArtifactRef().getId());
		assertEquals("artifacts/301", item.getArtifactRef().getUri());
		assertEquals(3, item.getProject().getId());
		assertEquals("projects/3", item.getProject().getUri());
		assertEquals("Another important Epic", item.getLabel());
		assertEquals("backlog_items/301", item.getUri());
		assertEquals("backlog_items?id=301&group_id=3", item.getHtmlUrl());
		assertEquals(1, item.getSubmittedBy());
		assertEquals(ParserUtil.getJsonDateFormat().parse("2013-09-23T11:44:18.963Z"), item.getSubmittedOn());
		assertEquals(ParserUtil.getJsonDateFormat().parse("2013-09-24T15:33:18.523Z"), item
				.getLastUpdatedOn());
		assertEquals(40.5f, item.getInitialEffort(), 0f);
		assertEquals(201, item.getAssignedMilestoneId().intValue());
	}

	/**
	 * Checks that the given backlog item corresponds to epic 300.
	 * 
	 * @param item
	 *            The backlog item The backlog item
	 * @throws ParseException
	 *             if the test is badly configured
	 */
	public static void checkEpic300(TuleapBacklogItem item) throws ParseException {
		assertEquals(300, item.getId());
		assertEquals(300, item.getArtifactRef().getId());
		assertEquals("artifacts/300", item.getArtifactRef().getUri());
		assertEquals(3, item.getProject().getId());
		assertEquals("projects/3", item.getProject().getUri());
		assertEquals("An important Epic", item.getLabel());
		assertEquals("backlog_items/300", item.getUri());
		assertEquals("backlog_items?id=300&group_id=3", item.getHtmlUrl());
		assertEquals(1, item.getSubmittedBy());
		assertEquals(ParserUtil.getJsonDateFormat().parse("2013-09-23T11:44:18.963Z"), item.getSubmittedOn());
		assertEquals(ParserUtil.getJsonDateFormat().parse("2013-09-24T15:33:18.523Z"), item
				.getLastUpdatedOn());
		assertEquals(30f, item.getInitialEffort(), 0f);
		assertEquals(200, item.getAssignedMilestoneId().intValue());
	}

	/**
	 * Tests the parsing of user story #350.
	 * 
	 * @throws ParseException
	 *             if the test is badly configured
	 */
	@Test
	public void testDeserializeUserStory350() throws ParseException {
		String userStory = ParserUtil.loadFile("/backlog_items/userStory350.json");
		TuleapBacklogItem item = parse(userStory, TuleapBacklogItem.class);
		checkUserStory350(item);

	}

	/**
	 * Checks that the given backlog item corresponds to user story 350.
	 * 
	 * @param item
	 *            The backlog item
	 * @throws ParseException
	 *             if the test is badly configured
	 */
	public static void checkUserStory350(TuleapBacklogItem item) throws ParseException {
		assertEquals(350, item.getId());
		assertEquals(350, item.getArtifactRef().getId());
		assertEquals("artifacts/350", item.getArtifactRef().getUri());
		assertEquals(3, item.getProject().getId());
		assertEquals("projects/3", item.getProject().getUri());
		assertEquals("An important User Story", item.getLabel());
		assertEquals("backlog_items/350", item.getUri());
		assertEquals("backlog_items?id=350&group_id=3", item.getHtmlUrl());
		assertEquals(1, item.getSubmittedBy());
		assertEquals(ParserUtil.getJsonDateFormat().parse("2013-09-23T11:44:18.963Z"), item.getSubmittedOn());
		assertEquals(ParserUtil.getJsonDateFormat().parse("2013-09-24T15:33:18.523Z"), item
				.getLastUpdatedOn());

		assertEquals(5f, item.getInitialEffort(), 0f);
		assertEquals(250, item.getAssignedMilestoneId().intValue());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected JsonDeserializer<TuleapBacklogItem> getDeserializer() {
		return new TuleapBacklogItemDeserializer();
	}

}
