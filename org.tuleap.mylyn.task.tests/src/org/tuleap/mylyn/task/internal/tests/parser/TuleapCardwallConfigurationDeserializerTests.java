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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapCard;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapCardwall;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapStatus;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapSwimlane;
import org.tuleap.mylyn.task.internal.core.parser.TuleapCardwallDeserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests the deserialization of the Tuleap Cardwall configuration.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapCardwallConfigurationDeserializerTests {

	/**
	 * Test the parsing of the cardwall status.
	 */
	@Test
	@Ignore("To do when Enalean has chosen about cardwalls")
	public void testCardwallStatus() {
		String cwSprint = ParserUtil.loadFile("/cardwalls/cw_sprint250.json"); //$NON-NLS-1$
		TuleapCardwall cardwall = this.parse(cwSprint);
		assertNotNull(cardwall);
		List<TuleapStatus> statuses = cardwall.getStatuses();
		assertEquals(4, statuses.size());

		TuleapStatus firstStatus = statuses.get(0);
		TuleapStatus secondStatus = statuses.get(1);
		TuleapStatus thirdStatus = statuses.get(2);
		TuleapStatus fourthStatus = statuses.get(3);

		assertEquals(2000, firstStatus.getId());
		assertEquals(2001, secondStatus.getId());
		assertEquals(2002, thirdStatus.getId());
		assertEquals(2003, fourthStatus.getId());

		assertEquals("To do", firstStatus.getLabel());
		assertEquals("Running", secondStatus.getLabel());
		assertEquals("To review", thirdStatus.getLabel());
		assertEquals("Done", fourthStatus.getLabel());

	}

	/**
	 * Test the parsing of the Backlog of the first cardwall swimlane.
	 */
	@Test
	@Ignore("To do when Enalean has chosen about cardwalls")
	public void testFirstCardwallSwimlaneBacklogItem() {
		String cwSprint = ParserUtil.loadFile("/cardwalls/cw_sprint250.json"); //$NON-NLS-1$
		TuleapCardwall cardwall = this.parse(cwSprint);

		List<TuleapSwimlane> swimlanes = cardwall.getSwimlanes();
		assertEquals(3, swimlanes.size());

		TuleapSwimlane firstSwimlane = swimlanes.get(0);

		TuleapBacklogItem firstBacklogItem = firstSwimlane.getBacklogItem();

		assertEquals(350, firstBacklogItem.getId());
		assertEquals("An important User Story", firstBacklogItem.getLabel());
		assertEquals("/backlog_items/350", firstBacklogItem.getUri());
		assertEquals("/backlog_items?id=350&group_id=3", firstBacklogItem.getHtmlUrl());

		assertEquals(5f, firstBacklogItem.getInitialEffort(), 0f);

	}

	/**
	 * Test the parsing of the cards of the first cardwall swimlane.
	 */
	@Test
	@Ignore("To do when Enalean has chosen about cardwalls")
	public void testFirstCardwallSwimlaneCards() {
		String cwSprint = ParserUtil.loadFile("/cardwalls/cw_sprint250.json"); //$NON-NLS-1$
		TuleapCardwall cardwall = this.parse(cwSprint);

		List<TuleapSwimlane> swimlanes = cardwall.getSwimlanes();
		assertEquals(3, swimlanes.size());

		TuleapSwimlane firstSwimlane = swimlanes.get(0);
		List<TuleapCard> cards = firstSwimlane.getCards();
		TuleapCard firstCard = cards.get(0);

		assertEquals(13500, firstCard.getId());
		assertEquals("Implement the data model", firstCard.getLabel());
		assertEquals("/cards/13500", firstCard.getUri());
		assertEquals("/cards?id=13500", firstCard.getHtmlUrl());
		assertEquals("#669944", firstCard.getColor());
		assertEquals(2000, firstCard.getStatusId());
		assertEquals(2, firstCard.getFieldValues().size());
		assertEquals(15, ((BoundFieldValue)firstCard.getFieldValue(8001)).getValueIds().get(0).intValue());
		assertEquals("2", ((LiteralFieldValue)firstCard.getFieldValue(8000)).getFieldValue());

		TuleapCard secondCard = cards.get(1);

		assertEquals(13501, secondCard.getId());
		assertEquals("Implement the UI", secondCard.getLabel());
		assertEquals("/cards/13501", secondCard.getUri());
		assertEquals("/cards?id=13501", secondCard.getHtmlUrl());
		assertEquals("#669944", secondCard.getColor());
		assertEquals(2002, secondCard.getStatusId());
		assertEquals(2, secondCard.getFieldValues().size());
		assertEquals(15, ((BoundFieldValue)secondCard.getFieldValue(8001)).getValueIds().get(0).intValue());
		assertEquals("2", ((LiteralFieldValue)secondCard.getFieldValue(8000)).getFieldValue());

		TuleapCard thirdCard = cards.get(2);

		assertEquals(13502, thirdCard.getId());
		assertEquals("Implement the service layer", thirdCard.getLabel());
		assertEquals("/cards/13502", thirdCard.getUri());
		assertEquals("/cards?id=13502", thirdCard.getHtmlUrl());
		assertEquals("#669944", thirdCard.getColor());
		assertEquals(2001, thirdCard.getStatusId());
		assertEquals(2, thirdCard.getFieldValues().size());
		assertEquals(16, ((BoundFieldValue)thirdCard.getFieldValue(8001)).getValueIds().get(0).intValue());
		assertEquals("2", ((LiteralFieldValue)thirdCard.getFieldValue(8000)).getFieldValue());

	}

	/**
	 * Test the parsing of the Backlog of the second cardwall swimlane.
	 */
	@Test
	@Ignore("To do when Enalean has chosen about cardwalls")
	public void testSecondCardwallSwimlaneBacklogItem() {
		String cwSprint = ParserUtil.loadFile("/cardwalls/cw_sprint250.json"); //$NON-NLS-1$
		TuleapCardwall cardwall = this.parse(cwSprint);

		List<TuleapSwimlane> swimlanes = cardwall.getSwimlanes();
		assertEquals(3, swimlanes.size());

		TuleapSwimlane secondSwimlane = swimlanes.get(1);

		TuleapBacklogItem firstBacklogItem = secondSwimlane.getBacklogItem();

		assertEquals(351, firstBacklogItem.getId());
		assertEquals("Another important User Story", firstBacklogItem.getLabel());
		assertEquals("/backlog_items/351", firstBacklogItem.getUri());
		assertEquals("/backlog_items?id=351&group_id=3", firstBacklogItem.getHtmlUrl());

		assertEquals(3f, firstBacklogItem.getInitialEffort(), 0f);

		// assertEquals(9, firstBacklogItem.getFieldValues().size());
		// assertEquals(8602, ((BoundFieldValue)firstBacklogItem.getFieldValue(860)).getValueIds().get(0)
		// .intValue());
		// assertEquals("Do something more useful", ((LiteralFieldValue)firstBacklogItem.getFieldValue(861))
		// .getFieldValue());
		// assertEquals("it brings more added-value to my work", ((LiteralFieldValue)firstBacklogItem
		// .getFieldValue(Integer.valueOf(862))).getFieldValue());
		// assertEquals("Other are acceptance Criteria:\n* Blah blah\n* Foo",
		// ((LiteralFieldValue)firstBacklogItem.getFieldValue(863)).getFieldValue());
		// assertEquals("510,511,512,513", ((LiteralFieldValue)firstBacklogItem.getFieldValue(864))
		// .getFieldValue());
		// assertEquals("Iteration 1",
		// ((LiteralFieldValue)firstBacklogItem.getFieldValue(865)).getFieldValue());
		// assertEquals("3", ((LiteralFieldValue)firstBacklogItem.getFieldValue(866)).getFieldValue());
		// assertEquals("0", ((LiteralFieldValue)firstBacklogItem.getFieldValue(867)).getFieldValue());
		// assertEquals(8612, ((BoundFieldValue)firstBacklogItem.getFieldValue(868)).getValueIds().get(0)
		// .intValue());

	}

	/**
	 * Test the parsing of the cards of the second cardwall swimlane.
	 */
	@Test
	@Ignore("To do when Enalean has chosen about cardwalls")
	public void testSecondCardwallSwimlaneCards() {
		String cwSprint = ParserUtil.loadFile("/cardwalls/cw_sprint250.json"); //$NON-NLS-1$
		TuleapCardwall cardwall = this.parse(cwSprint);

		List<TuleapSwimlane> swimlanes = cardwall.getSwimlanes();
		assertEquals(3, swimlanes.size());

		TuleapSwimlane secondSwimlane = swimlanes.get(1);
		List<TuleapCard> cards = secondSwimlane.getCards();
		TuleapCard firstCard = cards.get(0);

		assertEquals(13510, firstCard.getId());
		assertEquals("Implement the data model", firstCard.getLabel());
		assertEquals("/cards/13510", firstCard.getUri());
		assertEquals("/cards?id=13510", firstCard.getHtmlUrl());
		assertEquals("#669944", firstCard.getColor());
		assertEquals(2000, firstCard.getStatusId());
		assertEquals(2, firstCard.getFieldValues().size());
		assertEquals(15, ((BoundFieldValue)firstCard.getFieldValue(8001)).getValueIds().get(0).intValue());
		assertEquals("2", ((LiteralFieldValue)firstCard.getFieldValue(8000)).getFieldValue());

		TuleapCard secondCard = cards.get(1);

		assertEquals(13511, secondCard.getId());
		assertEquals("Implement the UI", secondCard.getLabel());
		assertEquals("/cards/135011", secondCard.getUri());
		assertEquals("/cards?id=13511", secondCard.getHtmlUrl());
		assertEquals("#669944", secondCard.getColor());
		assertEquals(2001, secondCard.getStatusId());
		assertEquals(2, secondCard.getFieldValues().size());
		assertEquals(15, ((BoundFieldValue)secondCard.getFieldValue(8001)).getValueIds().get(0).intValue());
		assertEquals("2", ((LiteralFieldValue)secondCard.getFieldValue(8000)).getFieldValue());

		TuleapCard thirdCard = cards.get(2);

		assertEquals(13512, thirdCard.getId());
		assertEquals("Implement the service layer", thirdCard.getLabel());
		assertEquals("/cards/13512", thirdCard.getUri());
		assertEquals("/cards?id=13512", thirdCard.getHtmlUrl());
		assertEquals("#669944", thirdCard.getColor());
		assertEquals(2002, thirdCard.getStatusId());
		assertEquals(2, thirdCard.getFieldValues().size());
		assertEquals(16, ((BoundFieldValue)thirdCard.getFieldValue(8001)).getValueIds().get(0).intValue());
		assertEquals("2", ((LiteralFieldValue)thirdCard.getFieldValue(8000)).getFieldValue());

	}

	/**
	 * Test the parsing of the Backlog of the third cardwall swimlane.
	 */
	@Test
	@Ignore("To do when Enalean has chosen about cardwalls")
	public void testThirdCardwallSwimlaneBacklogItem() {
		String cwSprint = ParserUtil.loadFile("/cardwalls/cw_sprint250.json"); //$NON-NLS-1$
		TuleapCardwall cardwall = this.parse(cwSprint);

		List<TuleapSwimlane> swimlanes = cardwall.getSwimlanes();
		assertEquals(3, swimlanes.size());

		TuleapSwimlane thirdSwimlane = swimlanes.get(2);

		TuleapBacklogItem firstBacklogItem = thirdSwimlane.getBacklogItem();

		assertEquals(352, firstBacklogItem.getId());
		assertEquals("A nice User Story", firstBacklogItem.getLabel());
		assertEquals("/backlog_items/352", firstBacklogItem.getUri());
		assertEquals("/backlog_items?id=352&group_id=3", firstBacklogItem.getHtmlUrl());

		assertEquals(3.5f, firstBacklogItem.getInitialEffort(), 0f);

		// assertEquals(8, firstBacklogItem.getFieldValues().size());
		// assertEquals(8602, ((BoundFieldValue)firstBacklogItem.getFieldValue(860)).getValueIds().get(0)
		// .intValue());
		// assertEquals("Do something sweet", ((LiteralFieldValue)firstBacklogItem.getFieldValue(861))
		// .getFieldValue());
		// assertEquals("it brings some shiny feedback", ((LiteralFieldValue)firstBacklogItem
		// .getFieldValue(Integer.valueOf(862))).getFieldValue());
		// assertEquals("My acceptance Criteria:\n* Blah blah\n* Foo", ((LiteralFieldValue)firstBacklogItem
		// .getFieldValue(863)).getFieldValue());
		// assertEquals("520,521,522",
		// ((LiteralFieldValue)firstBacklogItem.getFieldValue(864)).getFieldValue());
		// assertEquals("3.5", ((LiteralFieldValue)firstBacklogItem.getFieldValue(866)).getFieldValue());
		// assertEquals("3.5", ((LiteralFieldValue)firstBacklogItem.getFieldValue(867)).getFieldValue());
		// assertEquals(8610, ((BoundFieldValue)firstBacklogItem.getFieldValue(868)).getValueIds().get(0)
		// .intValue());

	}

	/**
	 * Test the parsing of the cards of the second cardwall swimlane.
	 */
	@Test
	@Ignore("To do when Enalean has chosen about cardwalls")
	public void testThirdCardwallSwimlaneCards() {
		String cwSprint = ParserUtil.loadFile("/cardwalls/cw_sprint250.json"); //$NON-NLS-1$
		TuleapCardwall cardwall = this.parse(cwSprint);

		List<TuleapSwimlane> swimlanes = cardwall.getSwimlanes();
		assertEquals(3, swimlanes.size());

		TuleapSwimlane secondSwimlane = swimlanes.get(2);
		List<TuleapCard> cards = secondSwimlane.getCards();
		TuleapCard firstCard = cards.get(0);

		assertEquals(13520, firstCard.getId());
		assertEquals("Implement the data model", firstCard.getLabel());
		assertEquals("/cards/13520", firstCard.getUri());
		assertEquals("/cards?id=13520", firstCard.getHtmlUrl());
		assertEquals("#669944", firstCard.getColor());
		assertEquals(2000, firstCard.getStatusId());
		assertEquals(2, firstCard.getFieldValues().size());
		assertEquals(15, ((BoundFieldValue)firstCard.getFieldValue(8001)).getValueIds().get(0).intValue());
		assertEquals("2", ((LiteralFieldValue)firstCard.getFieldValue(8000)).getFieldValue());

		TuleapCard secondCard = cards.get(1);

		assertEquals(13521, secondCard.getId());
		assertEquals("Implement the UI", secondCard.getLabel());
		assertEquals("/cards/13521", secondCard.getUri());
		assertEquals("/cards?id=13521", secondCard.getHtmlUrl());
		assertEquals("#669944", secondCard.getColor());
		assertEquals(2001, secondCard.getStatusId());
		assertEquals(2, secondCard.getFieldValues().size());
		assertEquals(15, ((BoundFieldValue)secondCard.getFieldValue(8001)).getValueIds().get(0).intValue());
		assertEquals("2", ((LiteralFieldValue)secondCard.getFieldValue(8000)).getFieldValue());

		TuleapCard thirdCard = cards.get(2);

		assertEquals(13522, thirdCard.getId());
		assertEquals("Implement the service layer", thirdCard.getLabel());
		assertEquals("/cards/13522", thirdCard.getUri());
		assertEquals("/cards?id=13522", thirdCard.getHtmlUrl());
		assertEquals("#669944", thirdCard.getColor());
		assertEquals(2002, thirdCard.getStatusId());
		assertEquals(2, thirdCard.getFieldValues().size());
		assertEquals(16, ((BoundFieldValue)thirdCard.getFieldValue(8001)).getValueIds().get(0).intValue());
		assertEquals("2", ((LiteralFieldValue)thirdCard.getFieldValue(8000)).getFieldValue());

	}

	/**
	 * Parse the content of the file and return the matching configuration.
	 * 
	 * @param fileContent
	 *            The content of the file
	 * @return The Tuleap BacklogItem Type matching the content of the file
	 */
	private TuleapCardwall parse(String fileContent) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TuleapCardwall.class, new TuleapCardwallDeserializer());

		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(fileContent).getAsJsonObject();

		Gson gson = gsonBuilder.create();
		TuleapCardwall tuleapCardwallConfiguration = gson.fromJson(jsonObject, TuleapCardwall.class);

		return tuleapCardwallConfiguration;
	}

}
