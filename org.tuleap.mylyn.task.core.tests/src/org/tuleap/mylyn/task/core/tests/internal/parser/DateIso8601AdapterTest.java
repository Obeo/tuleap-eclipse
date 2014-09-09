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
package org.tuleap.mylyn.task.core.tests.internal.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.core.internal.parser.DateIso8601Adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests of {@link DateIso8601Adapter} class.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class DateIso8601AdapterTest {

	private DateIso8601Adapter adapter;

	@Test
	public void testParseTimestampWithMilli() {
		JsonPrimitive p = new JsonPrimitive("2013-12-31T09:14:15.000+00:00");
		Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		c.set(2013, Calendar.DECEMBER, 31, 9, 14, 15);
		c.set(Calendar.MILLISECOND, 0);
		Date parsedDate = adapter.deserialize(p, Date.class, null);
		assertEquals(c.getTime(), parsedDate);
	}

	@Test
	public void testParseTimestampWithoutMilli() {
		JsonPrimitive p = new JsonPrimitive("2013-12-31T09:14:15+00:00");
		Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		c.set(2013, Calendar.DECEMBER, 31, 9, 14, 15);
		c.set(Calendar.MILLISECOND, 0);
		Date parsedDate = adapter.deserialize(p, Date.class, null);
		assertEquals(c.getTime(), parsedDate);
	}

	@Test
	public void testParseTimestampWithMilliWithTimezone() {
		JsonPrimitive p = new JsonPrimitive("2013-12-31T09:14:15.000+05:00");
		Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT+5:00"));
		c.set(2013, Calendar.DECEMBER, 31, 9, 14, 15);
		c.set(Calendar.MILLISECOND, 0);
		Date parsedDate = adapter.deserialize(p, Date.class, null);
		assertEquals(c.getTime(), parsedDate);
	}

	@Test
	public void testParseTimestampWithoutMilliWithTimezone() {
		JsonPrimitive p = new JsonPrimitive("2013-12-31T09:14:15-05:00");
		Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT-5:00"));
		c.set(2013, Calendar.DECEMBER, 31, 9, 14, 15);
		c.set(Calendar.MILLISECOND, 0);
		Date parsedDate = adapter.deserialize(p, Date.class, null);
		assertEquals(c.getTime(), parsedDate);
	}

	@Test
	public void testParseDateNoTimestamp() throws ParseException {
		JsonPrimitive json = new JsonPrimitive("2014-02-28");
		Date date = adapter.deserialize(json, Date.class, null);
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(date);
		assertEquals(2014, c.get(Calendar.YEAR));
		assertEquals(Calendar.FEBRUARY, c.get(Calendar.MONTH));
		assertEquals(28, c.get(Calendar.DATE));
	}

	@Test
	public void testSerializeDateWithoutTimezone() {
		String oldTZ = System.getProperty("user.timezone");
		try {
			System.setProperty("user.timezone", "GMT");
			Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
			c.set(2013, Calendar.DECEMBER, 31, 9, 14, 15);
			c.set(Calendar.MILLISECOND, 0);
			JsonElement serializedDate = adapter.serialize(c.getTime(), null, null);
			// 9:14:15 GMT <=> 11:14:15 GMT+2
			assertEquals("\"2013-12-31\"", serializedDate.toString());
		} finally {
			System.setProperty("user.timezone", oldTZ);
		}
	}

	@Test
	public void testSerializeDateWithTimezone() {
		String oldTZ = System.getProperty("user.timezone");
		try {
			System.setProperty("user.timezone", "GMT");
			Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT+8:00"));
			c.set(2013, Calendar.DECEMBER, 31, 9, 14, 15);
			c.set(Calendar.MILLISECOND, 0);
			JsonElement serializedDate = adapter.serialize(c.getTime(), null, null);
			// 9:14:15 GMT+8 <=> 3:14:15 GMT+2
			assertEquals("\"2013-12-31\"", serializedDate.toString());
		} finally {
			System.setProperty("user.timezone", oldTZ);
		}
	}

	@Test
	public void testSerializeNullDate() {
		JsonElement serializedDate = adapter.serialize(null, null, null);
		assertEquals(JsonNull.INSTANCE, serializedDate);
	}

	@Test
	public void testParseJsonNullDate() {
		Date parsed = adapter.deserialize(JsonNull.INSTANCE, Date.class, null);
		assertNull(parsed);
	}

	@Test
	public void testParseEmptyStringDate() {
		Date parsed = adapter.deserialize(new JsonPrimitive(""), Date.class, null);
		assertNull(parsed);
	}

	@Before
	public void setUp() {
		adapter = new DateIso8601Adapter();
	}

}
