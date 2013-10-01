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

import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
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
	 */
	@Test
	public void testDeserializeEpic301() {
		String epic = ParserUtil.loadFile("/backlog_items/epic301.json");
		TuleapBacklogItem item = parse(epic, TuleapBacklogItem.class);
		assertEquals(301, item.getId());
		assertEquals("Another important Epic", item.getLabel());
		assertEquals("/backlog_items/301", item.getUrl());
		assertEquals("/backlog_items?id=301&group_id=3", item.getHtmlUrl());
		assertEquals(40.5f, item.getInitialEffort(), 0f);
		assertEquals(801, item.getConfigurationId());

		assertEquals(201, item.getAssignedMilestoneId().intValue());

		assertEquals(4, item.getFieldValues().size());
		assertEquals(8501, ((BoundFieldValue)item.getFieldValue(850)).getValueIds().get(0).intValue());
		assertEquals("20.5", ((LiteralFieldValue)item.getFieldValue(851)).getFieldValue());
		assertEquals("Bring more added-value to users", ((LiteralFieldValue)item.getFieldValue(Integer
				.valueOf(852))).getFieldValue());
		assertEquals("352", ((LiteralFieldValue)item.getFieldValue(853)).getFieldValue());
	}

	/**
	 * Tests the parsing of user story #350.
	 */
	@Test
	public void testDeserializeUserStory350() {
		String userStory = ParserUtil.loadFile("/backlog_items/userStory350.json");
		TuleapBacklogItem item = parse(userStory, TuleapBacklogItem.class);
		assertEquals(350, item.getId());
		assertEquals("An important User Story", item.getLabel());
		assertEquals("/backlog_items/350", item.getUrl());
		assertEquals("/backlog_items?id=350&group_id=3", item.getHtmlUrl());

		assertEquals(5f, item.getInitialEffort(), 0f);
		assertEquals(802, item.getConfigurationId());

		assertEquals(250, item.getAssignedMilestoneId().intValue());

		assertEquals(9, item.getFieldValues().size());
		assertEquals(8602, ((BoundFieldValue)item.getFieldValue(860)).getValueIds().get(0).intValue());
		assertEquals("Do something useful", ((LiteralFieldValue)item.getFieldValue(861)).getFieldValue());
		assertEquals("it brings some added-value to my work", ((LiteralFieldValue)item.getFieldValue(Integer
				.valueOf(862))).getFieldValue());
		assertEquals("Here are acceptance Criteria:\n* Blah blah\n* Foo", ((LiteralFieldValue)item
				.getFieldValue(863)).getFieldValue());
		assertEquals("500,501,502,503", ((LiteralFieldValue)item.getFieldValue(864)).getFieldValue());
		assertEquals("Iteration 1", ((LiteralFieldValue)item.getFieldValue(865)).getFieldValue());
		assertEquals("3.5", ((LiteralFieldValue)item.getFieldValue(866)).getFieldValue());
		assertEquals("2", ((LiteralFieldValue)item.getFieldValue(867)).getFieldValue());
		assertEquals(8611, ((BoundFieldValue)item.getFieldValue(868)).getValueIds().get(0).intValue());

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected JsonDeserializer<TuleapBacklogItem> getDeserializer() {
		return new TuleapBacklogItemDeserializer();
	}

}
