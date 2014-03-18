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
package org.eclipse.mylyn.tuleap.core.tests.internal.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.eclipse.mylyn.tuleap.core.internal.parser.DateIso8601Adapter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class DateIso8601AdapterTest {

	private DateIso8601Adapter adapter;

	@Test
	public void testDeserializedateWithMilli() {
		JsonPrimitive p = new JsonPrimitive("2013-12-31T09:14:15.000+00:00");
		Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		c.set(2013, Calendar.DECEMBER, 31, 9, 14, 15);
		c.set(Calendar.MILLISECOND, 0);
		Date parsedDate = adapter.deserialize(p, null, null);
		assertEquals(c.getTime(), parsedDate);
	}

	@Test
	public void testDeserializedateWithoutMilli() {
		JsonPrimitive p = new JsonPrimitive("2013-12-31T09:14:15+00:00");
		Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		c.set(2013, Calendar.DECEMBER, 31, 9, 14, 15);
		c.set(Calendar.MILLISECOND, 0);
		Date parsedDate = adapter.deserialize(p, null, null);
		assertEquals(c.getTime(), parsedDate);
	}

	@Test
	public void testDeserializedateWithMilliWithTimezone() {
		JsonPrimitive p = new JsonPrimitive("2013-12-31T09:14:15.000+05:00");
		Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT+5:00"));
		c.set(2013, Calendar.DECEMBER, 31, 9, 14, 15);
		c.set(Calendar.MILLISECOND, 0);
		Date parsedDate = adapter.deserialize(p, null, null);
		assertEquals(c.getTime(), parsedDate);
	}

	@Test
	public void testDeserializedateWithoutMilliWithTimezone() {
		JsonPrimitive p = new JsonPrimitive("2013-12-31T09:14:15-05:00");
		Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT-5:00"));
		c.set(2013, Calendar.DECEMBER, 31, 9, 14, 15);
		c.set(Calendar.MILLISECOND, 0);
		Date parsedDate = adapter.deserialize(p, null, null);
		assertEquals(c.getTime(), parsedDate);
	}

	@Test
	public void testSerializeDateWithoutTimezone() {
		Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		c.set(2013, Calendar.DECEMBER, 31, 9, 14, 15);
		c.set(Calendar.MILLISECOND, 0);
		JsonElement serializedDate = adapter.serialize(c.getTime(), null, null);
		// 9:14:15 GMT <=> 11:14:15 GMT+2
		assertEquals("\"2013-12-31T10:14:15+01:00\"", serializedDate.toString());
	}

	@Test
	public void testSerializeDateWithTimezone() {
		Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT+8:00"));
		c.set(2013, Calendar.DECEMBER, 31, 9, 14, 15);
		c.set(Calendar.MILLISECOND, 0);
		JsonElement serializedDate = adapter.serialize(c.getTime(), null, null);
		// 9:14:15 GMT+8 <=> 3:14:15 GMT+2
		assertEquals("\"2013-12-31T02:14:15+01:00\"", serializedDate.toString());
	}

	@Before
	public void setUp() {
		adapter = new DateIso8601Adapter();
	}

}
