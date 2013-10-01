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

import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonParser;

import static org.junit.Assert.assertEquals;

/**
 * Tests of {@link TuleapJsonParser}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapJsonParserTest {

	/**
	 * The parser to test.
	 */
	private TuleapJsonParser parser = new TuleapJsonParser();

	/**
	 * Tests the parsing of backlog items.
	 */
	@Test
	public void testParseBacklogItems() {
		String json = ParserUtil.loadFile("/backlog_items/epics.json");
		List<TuleapBacklogItem> backlogItems = parser.parseBacklogItems(json);
		assertEquals(3, backlogItems.size());

		TuleapBacklogItem item = backlogItems.get(0);
		assertEquals(300, item.getId());
		assertEquals("An important Epic", item.getLabel());
		assertEquals("/backlog_items/300", item.getUrl());
		assertEquals("/backlog_items?id=300&group_id=3", item.getHtmlUrl());
		assertEquals(30f, item.getInitialEffort(), 0f);
		assertEquals(801, item.getConfigurationId());

		assertEquals(200, item.getAssignedMilestoneId().intValue());

		assertEquals(4, item.getFieldValues().size());
		assertEquals(8502, ((BoundFieldValue)item.getFieldValue(850)).getValueIds().get(0).intValue());
		assertEquals("0", ((LiteralFieldValue)item.getFieldValue(851)).getFieldValue());
		assertEquals("The summary of an important Epic", ((LiteralFieldValue)item.getFieldValue(Integer
				.valueOf(852))).getFieldValue());
		assertEquals("350,351", ((LiteralFieldValue)item.getFieldValue(853)).getFieldValue());
	}

	/**
	 * Checks the parsing of milestones.
	 * 
	 * @throws ParseException
	 *             If the parsing of a date goes wrong.
	 */
	@Test
	public void testParseMilestones() throws ParseException {
		String json = ParserUtil.loadFile("/milestones/releases.json");
		List<TuleapMilestone> milestones = parser.parseMilestones(json);
		assertEquals(2, milestones.size());

		new TuleapMilestoneDeserializerTests().checkRelease200(milestones.get(0));
		new TuleapMilestoneDeserializerTests().checkRelease201(milestones.get(1));
	}
}
