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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.parser.TuleapMilestoneDeserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

//CHECKSTYLE:OFF

/**
 * Tests the deserialization of the Tuleap milestone.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class TuleapMilestoneDeserializerTests {

	/**
	 * The pattern used to format date following the ISO8601 standard.
	 */
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS"); //$NON-NLS-1$

	/**
	 * Parse the content of the file and return the matching milestone.
	 * 
	 * @param fileContent
	 *            The content of the file
	 * @return The Tuleap project configuration matching the content of the file
	 */
	private TuleapMilestone parse(String fileContent) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TuleapMilestone.class, new TuleapMilestoneDeserializer());

		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(fileContent).getAsJsonObject();

		Gson gson = gsonBuilder.create();
		TuleapMilestone tuleapMilestone = gson.fromJson(jsonObject, TuleapMilestone.class);

		return tuleapMilestone;
	}

	/**
	 * Test the parsing of the data set of the release 200.
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testRelease200Parsing() throws ParseException {
		String release200 = ParserUtil.loadFile("/milestones/release200.json");
		TuleapMilestone tuleapMilestone = this.parse(release200);
		assertNotNull(tuleapMilestone);

		assertEquals(200, tuleapMilestone.getId());
		assertEquals("Release 0.9", tuleapMilestone.getLabel()); //$NON-NLS-1$
		assertEquals(dateFormat.parse("20130923114418963"), tuleapMilestone.getStartDate());
		assertEquals(50, tuleapMilestone.getDuration().floatValue(), 0);
		assertEquals(100, tuleapMilestone.getCapacity().floatValue(), 0);
		assertEquals("/milestones/200", tuleapMilestone.getUrl()); //$NON-NLS-1$
		assertEquals("/milestones?id=200&group_id=3", tuleapMilestone.getHtmlUrl()); //$NON-NLS-1$
		assertEquals(901, tuleapMilestone.getConfigurationId());

		assertNotNull(tuleapMilestone.getFieldValues());

		AbstractFieldValue value = tuleapMilestone.getFieldValue(950);
		assertEquals("Release 0.9", ((LiteralFieldValue)value).getFieldValue()); //$NON-NLS-1$

		value = tuleapMilestone.getFieldValue(951);
		assertEquals(9510, ((BoundFieldValue)value).getValueIds().get(0).intValue());

		value = tuleapMilestone.getFieldValue(952);
		assertEquals("35", ((LiteralFieldValue)value).getFieldValue());

		value = tuleapMilestone.getFieldValue(953);
		assertEquals("0.9", ((LiteralFieldValue)value).getFieldValue());

		value = tuleapMilestone.getFieldValue(954);
		assertEquals("300, 301, 302", ((LiteralFieldValue)value).getFieldValue());

		List<TuleapMilestone> milestones = tuleapMilestone.getSubMilestones();
		assertEquals(3, milestones.size());

		// First sub milestone
		TuleapMilestone subMilestone = milestones.get(0);
		assertEquals(250, subMilestone.getId());
		assertEquals("Sprint 1", subMilestone.getLabel());
		assertEquals(dateFormat.parse("20130922114418963"), subMilestone.getStartDate());
		assertEquals(15, subMilestone.getDuration(), 0);
		assertEquals(24.5, subMilestone.getCapacity(), 0);
		assertEquals("/milestones/300", subMilestone.getUrl());
		assertEquals("/milestones?id=300&group_id=3", subMilestone.getHtmlUrl());
		assertEquals(902, subMilestone.getConfigurationId());

		assertEquals(7, subMilestone.getFieldValues().size());

		value = subMilestone.getFieldValue(960);
		assertEquals("Sprint 1", ((LiteralFieldValue)value).getFieldValue());

		value = subMilestone.getFieldValue(961);
		assertEquals(9612, ((BoundFieldValue)value).getValueIds().get(0).intValue());

		value = subMilestone.getFieldValue(962);
		assertEquals("400, 401, 402", ((LiteralFieldValue)value).getFieldValue());

		value = subMilestone.getFieldValue(963);
		assertNotNull(value);
		assertEquals("2013-09-22T11:44:18.963Z", ((LiteralFieldValue)value).getFieldValue());

		value = subMilestone.getFieldValue(964);
		assertNotNull(value);
		assertEquals("15", ((LiteralFieldValue)value).getFieldValue());

		value = subMilestone.getFieldValue(965);
		assertNotNull(value);
		assertEquals("24.5", ((LiteralFieldValue)value).getFieldValue());

		value = subMilestone.getFieldValue(966);
		assertNotNull(value);
		assertEquals("0", ((LiteralFieldValue)value).getFieldValue());

		// Second sub milestone
		subMilestone = milestones.get(1);
		assertEquals(251, subMilestone.getId());
		assertEquals("Sprint 2", subMilestone.getLabel()); //$NON-NLS-1$
		assertEquals(dateFormat.parse("20130921114418963"), subMilestone.getStartDate());
		assertEquals(21, subMilestone.getDuration(), 0);
		assertEquals(28.5, subMilestone.getCapacity(), 0);
		assertEquals("/milestones/301", subMilestone.getUrl()); //$NON-NLS-1$
		assertEquals("/milestones?id=301&group_id=3", subMilestone.getHtmlUrl()); //$NON-NLS-1$
		assertEquals(902, subMilestone.getConfigurationId());

		// Third sub milestone
		subMilestone = milestones.get(2);
		assertEquals(252, subMilestone.getId());
		assertEquals("Sprint 3", subMilestone.getLabel()); //$NON-NLS-1$
		assertEquals(dateFormat.parse("20130920114418963"), subMilestone.getStartDate());
		assertEquals(20, subMilestone.getDuration(), 0);
		assertEquals(26.5, subMilestone.getCapacity(), 0);
		assertEquals("/milestones/302", subMilestone.getUrl()); //$NON-NLS-1$
		assertEquals("/milestones?id=302&group_id=3", subMilestone.getHtmlUrl()); //$NON-NLS-1$
		assertEquals(902, subMilestone.getConfigurationId());

	}

	/**
	 * Test the parsing of the data set of the release 300.
	 */
	@Test
	public void testRelease201Parsing() throws Exception {
		String release201 = ParserUtil.loadFile("/milestones/release201.json");
		TuleapMilestone tuleapMilestone = this.parse(release201);
		assertNotNull(tuleapMilestone);

		assertEquals(201, tuleapMilestone.getId());
		assertEquals("Release TU", tuleapMilestone.getLabel()); //$NON-NLS-1$
		assertEquals(dateFormat.parse("20130923114418963"), tuleapMilestone.getStartDate());
		assertEquals(50, tuleapMilestone.getDuration(), 0);
		assertEquals(100, tuleapMilestone.getCapacity(), 0);
		assertEquals("/milestones/201", tuleapMilestone.getUrl()); //$NON-NLS-1$
		assertEquals("/milestones?id=201&group_id=3", tuleapMilestone.getHtmlUrl()); //$NON-NLS-1$
		assertEquals(901, tuleapMilestone.getConfigurationId());

		assertNotNull(tuleapMilestone.getFieldValues());

		AbstractFieldValue value = tuleapMilestone.getFieldValue(955);
		assertTrue(value instanceof LiteralFieldValue);
		String fieldValue = ((LiteralFieldValue)value).getFieldValue();
		assertEquals("Bonjour,\nC'est un test unitaire.", fieldValue); //$NON-NLS-1$

		value = tuleapMilestone.getFieldValue(956);
		assertTrue(value instanceof LiteralFieldValue);
		fieldValue = ((LiteralFieldValue)value).getFieldValue();
		assertEquals("je suis une valeur calculee", fieldValue); //$NON-NLS-1$

		value = tuleapMilestone.getFieldValue(957);
		assertTrue(value instanceof BoundFieldValue);
		List<Integer> valueIds = ((BoundFieldValue)value).getValueIds();
		assertEquals(1, valueIds.size());
		assertEquals(9610, valueIds.get(0).intValue());

		value = tuleapMilestone.getFieldValue(958);
		assertTrue(value instanceof BoundFieldValue);
		valueIds = ((BoundFieldValue)value).getValueIds();
		assertEquals(2, valueIds.size());
		assertEquals(0, valueIds.get(0).intValue());
		assertEquals(1, valueIds.get(1).intValue());

		value = tuleapMilestone.getFieldValue(959);
		assertTrue(value instanceof BoundFieldValue);
		valueIds = ((BoundFieldValue)value).getValueIds();
		assertEquals(0, valueIds.get(0).intValue());
		assertEquals(2, valueIds.get(1).intValue());

		value = tuleapMilestone.getFieldValue(960);
		assertTrue(value instanceof LiteralFieldValue);
		fieldValue = ((LiteralFieldValue)value).getFieldValue();
		assertEquals("20120101", fieldValue); //$NON-NLS-1$

		value = tuleapMilestone.getFieldValue(961);
		assertTrue(value instanceof LiteralFieldValue);
		fieldValue = ((LiteralFieldValue)value).getFieldValue();
		assertEquals("first, second, third", fieldValue); //$NON-NLS-1$
	}

}
