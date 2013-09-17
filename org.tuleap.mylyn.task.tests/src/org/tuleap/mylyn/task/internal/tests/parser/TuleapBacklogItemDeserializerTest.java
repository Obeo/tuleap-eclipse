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

import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.parser.TuleapBacklogItemDeserializer;

import static org.junit.Assert.assertEquals;

/**
 * Unit Tests of Backlog item deserializer.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapBacklogItemDeserializerTest {

	/**
	 * Tests the parsing of epic #300.
	 */
	@Test
	public void testDeserializeEpic300() {
		String epic = ParserUtil.loadFile("/json/backlog_items/epic300.json"); //$NON-NLS-1$
		TuleapBacklogItem item = parse(epic);
		assertEquals(300, item.getId());
		assertEquals("An important Epic", item.getLabel()); //$NON-NLS-1$
		assertEquals("/backlog_items/300", item.getUrl()); //$NON-NLS-1$
		assertEquals("/backlog_items?id=300&group_id=3", item.getHtmlUrl()); //$NON-NLS-1$
		assertEquals(30f, item.getInitialEffort(), 0f);
		assertEquals(801, item.getTypeId());

		assertEquals(4, item.getValues().size());
		assertEquals(Integer.valueOf(8502), item.getValue(Integer.valueOf(850)));
		assertEquals(0, ((Number)item.getValue(Integer.valueOf(851))).intValue());
		assertEquals("The summary of an important Epic", item.getValue(Integer.valueOf(852))); //$NON-NLS-1$
		assertEquals("350,351", item.getValue(Integer.valueOf(853))); //$NON-NLS-1$
	}

	/**
	 * Tests the parsing of epic #301.
	 */
	@Test
	public void testDeserializeEpic301() {
		String epic = ParserUtil.loadFile("/json/backlog_items/epic301.json"); //$NON-NLS-1$
		TuleapBacklogItem item = parse(epic);
		assertEquals(301, item.getId());
		assertEquals("Another important Epic", item.getLabel()); //$NON-NLS-1$
		assertEquals("/backlog_items/301", item.getUrl()); //$NON-NLS-1$
		assertEquals("/backlog_items?id=301&group_id=3", item.getHtmlUrl()); //$NON-NLS-1$
		assertEquals(40.5f, item.getInitialEffort(), 0f);
		assertEquals(801, item.getTypeId());

		assertEquals(4, item.getValues().size());
		assertEquals(Integer.valueOf(8501), item.getValue(Integer.valueOf(850)));
		assertEquals(20.5f, ((Number)item.getValue(Integer.valueOf(851))).floatValue(), 0f);
		assertEquals("Bring more added-value to users", item.getValue(Integer.valueOf(852))); //$NON-NLS-1$
		assertEquals("352", item.getValue(Integer.valueOf(853))); //$NON-NLS-1$
	}

	/**
	 * Tests the parsing of epic #302.
	 */
	@Test
	public void testDeserializeEpic302() {
		String epic = ParserUtil.loadFile("/json/backlog_items/epic302.json"); //$NON-NLS-1$
		TuleapBacklogItem item = parse(epic);
		assertEquals(302, item.getId());
		assertEquals("Some nice Epic", item.getLabel()); //$NON-NLS-1$
		assertEquals("/backlog_items/302", item.getUrl()); //$NON-NLS-1$
		assertEquals("/backlog_items?id=302&group_id=3", item.getHtmlUrl()); //$NON-NLS-1$
		assertEquals(25f, item.getInitialEffort(), 0f);
		assertEquals(801, item.getTypeId());

		assertEquals(3, item.getValues().size());
		assertEquals(Integer.valueOf(8500), item.getValue(Integer.valueOf(850)));
		assertEquals(26.5f, ((Number)item.getValue(Integer.valueOf(851))).floatValue(), 0f);
		assertEquals("Use some nice visual effects", item.getValue(Integer.valueOf(852))); //$NON-NLS-1$
	}

	/**
	 * Tests the parsing of user story #350.
	 */
	@Test
	public void testDeserializeUserStory350() {
		String userStory = ParserUtil.loadFile("/json/backlog_items/userStory350.json"); //$NON-NLS-1$
		TuleapBacklogItem item = parse(userStory);
		assertEquals(350, item.getId());
		assertEquals("An important User Story", item.getLabel()); //$NON-NLS-1$
		assertEquals("/backlog_items/350", item.getUrl()); //$NON-NLS-1$
		assertEquals("/backlog_items?id=350&group_id=3", item.getHtmlUrl()); //$NON-NLS-1$
		assertEquals(5f, item.getInitialEffort(), 0f);
		assertEquals(802, item.getTypeId());

		assertEquals(9, item.getValues().size());
		assertEquals(Integer.valueOf(8602), item.getValue(Integer.valueOf(860)));
		assertEquals("Do something useful", item.getValue(Integer.valueOf(861))); //$NON-NLS-1$
		assertEquals("it brings some added-value to my work", item.getValue(Integer.valueOf(862))); //$NON-NLS-1$
		assertEquals("Here are acceptance Criteria:\n* Blah blah\n* Foo", item.getValue(Integer //$NON-NLS-1$
				.valueOf(863)));
		assertEquals("500,501,502,503", item.getValue(Integer.valueOf(864))); //$NON-NLS-1$
		assertEquals("Iteration 1", item.getValue(Integer.valueOf(865))); //$NON-NLS-1$
		assertEquals(3.5f, ((Number)item.getValue(Integer.valueOf(866))).floatValue(), 0f);
		assertEquals("2", item.getValue(Integer.valueOf(867))); //$NON-NLS-1$
		assertEquals(Integer.valueOf(8611), item.getValue(Integer.valueOf(868)));
	}

	/**
	 * Tests the parsing of user story #351.
	 */
	@Test
	public void testDeserializeUserStory351() {
		String userStory = ParserUtil.loadFile("/json/backlog_items/userStory351.json"); //$NON-NLS-1$
		TuleapBacklogItem item = parse(userStory);
		assertEquals(351, item.getId());
		assertEquals("Another important User Story", item.getLabel()); //$NON-NLS-1$
		assertEquals("/backlog_items/351", item.getUrl()); //$NON-NLS-1$
		assertEquals("/backlog_items?id=351&group_id=3", item.getHtmlUrl()); //$NON-NLS-1$
		assertEquals(3f, item.getInitialEffort(), 0f);
		assertEquals(802, item.getTypeId());

		assertEquals(9, item.getValues().size());
		assertEquals(Integer.valueOf(8602), item.getValue(Integer.valueOf(860)));
		assertEquals("Do something more useful", item.getValue(Integer.valueOf(861))); //$NON-NLS-1$
		assertEquals("it brings more added-value to my work", item.getValue(Integer.valueOf(862))); //$NON-NLS-1$
		assertEquals("Other are acceptance Criteria:\n* Blah blah\n* Foo", item.getValue(Integer //$NON-NLS-1$
				.valueOf(863)));
		assertEquals("510,511,512,513", item.getValue(Integer.valueOf(864))); //$NON-NLS-1$
		assertEquals("Iteration 1", item.getValue(Integer.valueOf(865))); //$NON-NLS-1$
		assertEquals(3f, ((Number)item.getValue(Integer.valueOf(866))).floatValue(), 0f);
		assertEquals("0", item.getValue(Integer.valueOf(867))); //$NON-NLS-1$
		assertEquals(Integer.valueOf(8612), item.getValue(Integer.valueOf(868)));
	}

	/**
	 * Tests the parsing of user story #352.
	 */
	@Test
	public void testDeserializeUserStory352() {
		String userStory = ParserUtil.loadFile("/json/backlog_items/userStory352.json"); //$NON-NLS-1$
		TuleapBacklogItem item = parse(userStory);
		assertEquals(352, item.getId());
		assertEquals("A nice User Story", item.getLabel()); //$NON-NLS-1$
		assertEquals("/backlog_items/352", item.getUrl()); //$NON-NLS-1$
		assertEquals("/backlog_items?id=352&group_id=3", item.getHtmlUrl()); //$NON-NLS-1$
		assertEquals(3.5f, item.getInitialEffort(), 0f);
		assertEquals(802, item.getTypeId());

		assertEquals(8, item.getValues().size());
		assertEquals(Integer.valueOf(8602), item.getValue(Integer.valueOf(860)));
		assertEquals("Do something sweet", item.getValue(Integer.valueOf(861))); //$NON-NLS-1$
		assertEquals("it brings some shiny feedback", item.getValue(Integer.valueOf(862))); //$NON-NLS-1$
		assertEquals("My acceptance Criteria:\n* Blah blah\n* Foo", item.getValue(Integer //$NON-NLS-1$
				.valueOf(863)));
		assertEquals("520,521,522", item.getValue(Integer.valueOf(864))); //$NON-NLS-1$
		assertEquals(3.5f, ((Number)item.getValue(Integer.valueOf(866))).floatValue(), 0f);
		assertEquals("3.5", item.getValue(Integer.valueOf(867))); //$NON-NLS-1$
		assertEquals(Integer.valueOf(8610), item.getValue(Integer.valueOf(868)));
	}

	/**
	 * Parse the content of the file and return the matching configuration.
	 * 
	 * @param fileContent
	 *            The content of the file
	 * @return The Tuleap project configuration matching the content of the file
	 */
	private TuleapBacklogItem parse(String fileContent) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TuleapBacklogItem.class, new TuleapBacklogItemDeserializer());

		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(fileContent).getAsJsonObject();

		Gson gson = gsonBuilder.create();
		TuleapBacklogItem backlogItem = gson.fromJson(jsonObject, TuleapBacklogItem.class);

		return backlogItem;
	}

}
