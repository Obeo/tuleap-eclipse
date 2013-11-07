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

import org.junit.Ignore;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapTopPlanning;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonParser;

import static org.junit.Assert.assertEquals;

/**
 * Tests of {@link TuleapJsonParser}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapJsonParserTest {

	/**
	 * The project configuration provider.
	 */
	private StubProjectProvider provider = new StubProjectProvider();

	/**
	 * The parser to test.
	 */
	private TuleapJsonParser parser = new TuleapJsonParser();

	/**
	 * Tests the parsing of backlog items.
	 * 
	 * @throws ParseException
	 *             if the test is badly configured
	 */
	@Test
	@Ignore("To do when Enalean has chosen about top plannings")
	public void testParseBacklogItems() throws ParseException {
		String json = ParserUtil.loadFile("/backlog_items/epics.json");
		List<TuleapBacklogItem> backlogItems = parser.parseBacklogItems(json);
		assertEquals(3, backlogItems.size());

		TuleapBacklogItem item = backlogItems.get(0);
		TuleapBacklogItemDeserializerTest.checkEpic300(item);

		item = backlogItems.get(1);
		TuleapBacklogItemDeserializerTest.checkEpic301(item);
	}

	/**
	 * Checks the parsing of milestones.
	 * 
	 * @throws ParseException
	 *             If the parsing of a date goes wrong.
	 */
	@Test
	@Ignore("To do when Enalean has chosen about top plannings")
	public void testParseMilestones() throws ParseException {
		String json = ParserUtil.loadFile("/milestones/releases.json");
		List<TuleapMilestone> milestones = parser.parseMilestones(json);
		assertEquals(2, milestones.size());

		new TuleapMilestoneDeserializerTests().checkRelease200(milestones.get(0));
		new TuleapMilestoneDeserializerTests().checkRelease201(milestones.get(1));
	}

	/**
	 * Checks the parsing of milestone types.
	 * 
	 * @throws ParseException
	 *             if a parsing error occurs.
	 */
	@Test
	@Ignore("To do when Enalean has chosen about top plannings")
	public void testParseTopPlannings() throws ParseException {
		String json = ParserUtil.loadFile("/top_plannings/top_plannings_prj3.json");
		List<TuleapTopPlanning> plannings = parser.parseTopPlannings(json);
		assertEquals(1, plannings.size());

		TuleapTopPlanning tp = plannings.get(0);
		new TuleapTopPlanningDeserializerTests().checkTopPlanning30(tp);
	}
}
