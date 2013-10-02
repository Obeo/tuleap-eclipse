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
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItemType;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapCardType;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestoneType;
import org.tuleap.mylyn.task.internal.core.parser.TuleapJsonParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

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
	 */
	@Test
	public void testParseBacklogItems() {
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
	public void testParseMilestones() throws ParseException {
		String json = ParserUtil.loadFile("/milestones/releases.json");
		List<TuleapMilestone> milestones = parser.parseMilestones(json);
		assertEquals(2, milestones.size());

		new TuleapMilestoneDeserializerTests().checkRelease200(milestones.get(0));
		new TuleapMilestoneDeserializerTests().checkRelease201(milestones.get(1));
	}

	/**
	 * Checks the parsing of card types.
	 */
	@Test
	public void testParseCardTypes() {
		String json = ParserUtil.loadFile("/card_types/prj3_card_types.json");
		List<TuleapCardType> cardTypes = parser.parseCardTypes(provider.getProjectConfiguration(), json);
		assertEquals(1, cardTypes.size());
		TuleapCardType cardType = cardTypes.get(0);
		new TuleapCardTypeDeserializerTests().checkCardType7000(cardType);

		// Check that the card type has been added to the project configuration
		assertSame(cardType, provider.getProjectConfiguration().getCardType(7000));
	}

	/**
	 * Checks the parsing of backlog item types.
	 */
	@Test
	public void testParseBacklogItemTypeEpics() {
		String json = ParserUtil.loadFile("/backlog_item_types/types.json");
		List<TuleapBacklogItemType> types = parser.parseBacklogItemTypes(provider.getProjectConfiguration(),
				json);
		assertEquals(2, types.size());

		TuleapBacklogItemType type = types.get(0);
		new TuleapBacklogItemTypeDeserializerTests().checkTypeEpics(type);

		type = types.get(1);
		new TuleapBacklogItemTypeDeserializerTests().checkTypeUserStories(type);

		assertEquals(2, provider.getProjectConfiguration().getAllBacklogItemTypes().size());
		assertSame(types.get(0), provider.getProjectConfiguration().getBacklogItemType(801));
		assertSame(types.get(1), provider.getProjectConfiguration().getBacklogItemType(802));
	}

	/**
	 * Checks the parsing of milestone types.
	 */
	@Test
	public void testParseMilestoneType() {
		String json = ParserUtil.loadFile("/milestone_types/types.json");
		List<TuleapMilestoneType> types = parser
				.parseMilestoneTypes(provider.getProjectConfiguration(), json);
		assertEquals(2, types.size());

		TuleapMilestoneType type = types.get(0);
		new TuleapMilestoneTypeDeserializerTests().checkMilestoneTypeReleases(type);

		type = types.get(1);
		new TuleapMilestoneTypeDeserializerTests().checkMilestoneTypeSprints(type);

		assertEquals(2, provider.getProjectConfiguration().getAllMilestoneTypes().size());
		assertSame(types.get(0), provider.getProjectConfiguration().getMilestoneType(901));
		assertSame(types.get(1), provider.getProjectConfiguration().getMilestoneType(902));
	}
}
