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

import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapCard;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapCardwall;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapColumn;
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
	public void testCardwallStatus() {
		String cwSprint = ParserUtil.loadFile("/cardwalls/cw_sprint250.json"); //$NON-NLS-1$
		TuleapCardwall cardwall = this.parse(cwSprint);
		assertNotNull(cardwall);
		List<TuleapColumn> statuses = cardwall.getColumns();
		assertEquals(4, statuses.size());

		TuleapColumn firstStatus = statuses.get(0);
		TuleapColumn secondStatus = statuses.get(1);
		TuleapColumn thirdStatus = statuses.get(2);
		TuleapColumn fourthStatus = statuses.get(3);

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
	 * Test the parsing of the cards of the first cardwall swimlane.
	 */
	@Test
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
		assertEquals(Integer.valueOf(2000), firstCard.getColumnId());
		assertEquals("Closed", firstCard.getStatus().toString());
		assertEquals(3, firstCard.getProject().getId());
		assertEquals("projects/3", firstCard.getProject().getUri());
		assertEquals(13500, firstCard.getArtifact().getId());
		assertEquals("artifacts/13500", firstCard.getArtifact().getUri());
		assertEquals(801, firstCard.getArtifact().getTracker().getId());
		assertEquals("trackers/801", firstCard.getArtifact().getTracker().getUri());
		assertEquals(2, firstCard.getAllowedColumnIds().length);
		assertEquals(2000, firstCard.getAllowedColumnIds()[0]);
		assertEquals(2001, firstCard.getAllowedColumnIds()[1]);

		assertEquals(2, firstCard.getFieldValues().size());
		assertEquals(15, ((BoundFieldValue)firstCard.getFieldValue(8001)).getValueIds().get(0).intValue());
		assertEquals("2", ((LiteralFieldValue)firstCard.getFieldValue(8000)).getFieldValue());

		TuleapCard secondCard = cards.get(1);

		assertEquals(13501, secondCard.getId());
		assertEquals("Implement the UI", secondCard.getLabel());
		assertEquals("/cards/13501", secondCard.getUri());
		assertEquals("/cards?id=13501", secondCard.getHtmlUrl());
		assertEquals("#669944", secondCard.getColor());
		assertEquals(Integer.valueOf(2002), secondCard.getColumnId());
		assertEquals("Open", secondCard.getStatus().toString());
		assertEquals(3, secondCard.getProject().getId());
		assertEquals("projects/3", secondCard.getProject().getUri());
		assertEquals(13501, secondCard.getArtifact().getId());
		assertEquals("artifacts/13501", secondCard.getArtifact().getUri());
		assertEquals(801, secondCard.getArtifact().getTracker().getId());
		assertEquals("trackers/801", secondCard.getArtifact().getTracker().getUri());
		assertEquals(2, secondCard.getAllowedColumnIds().length);
		assertEquals(2002, secondCard.getAllowedColumnIds()[0]);
		assertEquals(2001, secondCard.getAllowedColumnIds()[1]);

		assertEquals(2, secondCard.getFieldValues().size());
		assertEquals(15, ((BoundFieldValue)secondCard.getFieldValue(8001)).getValueIds().get(0).intValue());
		assertEquals("2", ((LiteralFieldValue)secondCard.getFieldValue(8000)).getFieldValue());

		// A card with a null "column_id" attribute
		TuleapCard thirdCard = cards.get(2);

		assertEquals(13502, thirdCard.getId());
		assertEquals("Implement the service layer", thirdCard.getLabel());
		assertEquals("/cards/13502", thirdCard.getUri());
		assertEquals("/cards?id=13502", thirdCard.getHtmlUrl());
		assertEquals("#669944", thirdCard.getColor());
		assertEquals(null, thirdCard.getColumnId());
		assertEquals("Closed", thirdCard.getStatus().toString());
		assertEquals(3, thirdCard.getProject().getId());
		assertEquals("projects/3", thirdCard.getProject().getUri());
		assertEquals(13502, thirdCard.getArtifact().getId());
		assertEquals("artifacts/13502", thirdCard.getArtifact().getUri());
		assertEquals(801, thirdCard.getArtifact().getTracker().getId());
		assertEquals("trackers/801", thirdCard.getArtifact().getTracker().getUri());
		assertEquals(2, thirdCard.getAllowedColumnIds().length);
		assertEquals(2001, thirdCard.getAllowedColumnIds()[0]);
		assertEquals(2002, thirdCard.getAllowedColumnIds()[1]);

		assertEquals(2, thirdCard.getFieldValues().size());
		assertEquals(16, ((BoundFieldValue)thirdCard.getFieldValue(8001)).getValueIds().get(0).intValue());
		assertEquals("2", ((LiteralFieldValue)thirdCard.getFieldValue(8000)).getFieldValue());

	}

	/**
	 * Test the parsing of the cards of the second cardwall swimlane.
	 */
	@Test
	public void testSecondCardwallSwimlaneCards() {
		String cwSprint = ParserUtil.loadFile("/cardwalls/cw_sprint250.json"); //$NON-NLS-1$
		TuleapCardwall cardwall = this.parse(cwSprint);

		List<TuleapSwimlane> swimlanes = cardwall.getSwimlanes();
		assertEquals(3, swimlanes.size());

		TuleapSwimlane secondSwimlane = swimlanes.get(1);
		List<TuleapCard> cards = secondSwimlane.getCards();
		TuleapCard firstCard = cards.get(0);

		// A card without a "column_id" attribute
		assertEquals(13510, firstCard.getId());
		assertEquals("Implement the data model", firstCard.getLabel());
		assertEquals("/cards/13510", firstCard.getUri());
		assertEquals("/cards?id=13510", firstCard.getHtmlUrl());
		assertEquals("#669944", firstCard.getColor());
		assertEquals(null, firstCard.getColumnId());
		assertEquals("Open", firstCard.getStatus().toString());
		assertEquals(3, firstCard.getProject().getId());
		assertEquals("projects/3", firstCard.getProject().getUri());
		assertEquals(13510, firstCard.getArtifact().getId());
		assertEquals("artifacts/13510", firstCard.getArtifact().getUri());
		assertEquals(801, firstCard.getArtifact().getTracker().getId());
		assertEquals("trackers/801", firstCard.getArtifact().getTracker().getUri());
		assertEquals(2, firstCard.getAllowedColumnIds().length);
		assertEquals(2000, firstCard.getAllowedColumnIds()[0]);
		assertEquals(2002, firstCard.getAllowedColumnIds()[1]);

		assertEquals(2, firstCard.getFieldValues().size());
		assertEquals(15, ((BoundFieldValue)firstCard.getFieldValue(8001)).getValueIds().get(0).intValue());
		assertEquals("2", ((LiteralFieldValue)firstCard.getFieldValue(8000)).getFieldValue());

		TuleapCard secondCard = cards.get(1);

		assertEquals(13511, secondCard.getId());
		assertEquals("Implement the UI", secondCard.getLabel());
		assertEquals("/cards/135011", secondCard.getUri());
		assertEquals("/cards?id=13511", secondCard.getHtmlUrl());
		assertEquals("#669944", secondCard.getColor());
		assertEquals(Integer.valueOf(2001), secondCard.getColumnId());
		assertEquals("Closed", secondCard.getStatus().toString());
		assertEquals(3, secondCard.getProject().getId());
		assertEquals("projects/3", secondCard.getProject().getUri());
		assertEquals(13511, secondCard.getArtifact().getId());
		assertEquals("artifacts/13511", secondCard.getArtifact().getUri());
		assertEquals(801, secondCard.getArtifact().getTracker().getId());
		assertEquals("trackers/801", secondCard.getArtifact().getTracker().getUri());
		assertEquals(1, secondCard.getAllowedColumnIds().length);
		assertEquals(2001, secondCard.getAllowedColumnIds()[0]);

		assertEquals(2, secondCard.getFieldValues().size());
		assertEquals(15, ((BoundFieldValue)secondCard.getFieldValue(8001)).getValueIds().get(0).intValue());
		assertEquals("2", ((LiteralFieldValue)secondCard.getFieldValue(8000)).getFieldValue());

		TuleapCard thirdCard = cards.get(2);

		assertEquals(13512, thirdCard.getId());
		assertEquals("Implement the service layer", thirdCard.getLabel());
		assertEquals("/cards/13512", thirdCard.getUri());
		assertEquals("/cards?id=13512", thirdCard.getHtmlUrl());
		assertEquals("#669944", thirdCard.getColor());
		assertEquals(Integer.valueOf(2002), thirdCard.getColumnId());
		assertEquals("Open", thirdCard.getStatus().toString());
		assertEquals(3, thirdCard.getProject().getId());
		assertEquals("projects/3", thirdCard.getProject().getUri());
		assertEquals(13512, thirdCard.getArtifact().getId());
		assertEquals("artifacts/13512", thirdCard.getArtifact().getUri());
		assertEquals(801, thirdCard.getArtifact().getTracker().getId());
		assertEquals("trackers/801", thirdCard.getArtifact().getTracker().getUri());
		assertEquals(2, thirdCard.getAllowedColumnIds().length);
		assertEquals(2002, thirdCard.getAllowedColumnIds()[0]);
		assertEquals(2001, thirdCard.getAllowedColumnIds()[1]);

		assertEquals(2, thirdCard.getFieldValues().size());
		assertEquals(16, ((BoundFieldValue)thirdCard.getFieldValue(8001)).getValueIds().get(0).intValue());
		assertEquals("2", ((LiteralFieldValue)thirdCard.getFieldValue(8000)).getFieldValue());

	}

	/**
	 * Test the parsing of the cards of the second cardwall swimlane.
	 */
	@Test
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
		assertEquals(Integer.valueOf(2000), firstCard.getColumnId());
		assertEquals("Closed", firstCard.getStatus().toString());
		assertEquals(3, firstCard.getProject().getId());
		assertEquals("projects/3", firstCard.getProject().getUri());
		assertEquals(13520, firstCard.getArtifact().getId());
		assertEquals("artifacts/13520", firstCard.getArtifact().getUri());
		assertEquals(801, firstCard.getArtifact().getTracker().getId());
		assertEquals("trackers/801", firstCard.getArtifact().getTracker().getUri());
		assertEquals(2, firstCard.getAllowedColumnIds().length);
		assertEquals(2000, firstCard.getAllowedColumnIds()[0]);
		assertEquals(2002, firstCard.getAllowedColumnIds()[1]);

		assertEquals(2, firstCard.getFieldValues().size());
		assertEquals(15, ((BoundFieldValue)firstCard.getFieldValue(8001)).getValueIds().get(0).intValue());
		assertEquals("2", ((LiteralFieldValue)firstCard.getFieldValue(8000)).getFieldValue());

		TuleapCard secondCard = cards.get(1);

		assertEquals(13521, secondCard.getId());
		assertEquals("Implement the UI", secondCard.getLabel());
		assertEquals("/cards/13521", secondCard.getUri());
		assertEquals("/cards?id=13521", secondCard.getHtmlUrl());
		assertEquals("#669944", secondCard.getColor());
		assertEquals(Integer.valueOf(2001), secondCard.getColumnId());
		assertEquals("Open", secondCard.getStatus().toString());
		assertEquals(3, secondCard.getProject().getId());
		assertEquals("projects/3", secondCard.getProject().getUri());
		assertEquals(13521, secondCard.getArtifact().getId());
		assertEquals("artifacts/13521", secondCard.getArtifact().getUri());
		assertEquals(801, secondCard.getArtifact().getTracker().getId());
		assertEquals("trackers/801", secondCard.getArtifact().getTracker().getUri());
		assertEquals(2, secondCard.getAllowedColumnIds().length);
		assertEquals(2000, secondCard.getAllowedColumnIds()[0]);
		assertEquals(2001, secondCard.getAllowedColumnIds()[1]);

		assertEquals(2, secondCard.getFieldValues().size());
		assertEquals(15, ((BoundFieldValue)secondCard.getFieldValue(8001)).getValueIds().get(0).intValue());
		assertEquals("2", ((LiteralFieldValue)secondCard.getFieldValue(8000)).getFieldValue());

		TuleapCard thirdCard = cards.get(2);

		assertEquals(13522, thirdCard.getId());
		assertEquals("Implement the service layer", thirdCard.getLabel());
		assertEquals("/cards/13522", thirdCard.getUri());
		assertEquals("/cards?id=13522", thirdCard.getHtmlUrl());
		assertEquals("#669944", thirdCard.getColor());
		assertEquals(Integer.valueOf(2002), thirdCard.getColumnId());
		assertEquals("Closed", thirdCard.getStatus().toString());
		assertEquals(3, thirdCard.getProject().getId());
		assertEquals("projects/3", thirdCard.getProject().getUri());
		assertEquals(13522, thirdCard.getArtifact().getId());
		assertEquals("artifacts/13522", thirdCard.getArtifact().getUri());
		assertEquals(801, thirdCard.getArtifact().getTracker().getId());
		assertEquals("trackers/801", thirdCard.getArtifact().getTracker().getUri());
		assertEquals(1, thirdCard.getAllowedColumnIds().length);
		assertEquals(2002, thirdCard.getAllowedColumnIds()[0]);

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
