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
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.data.AbstractTuleapDetailedElement;
import org.tuleap.mylyn.task.internal.core.parser.AbstractDetailedElementDeserializer;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class Iso8601ParseTest {

	AbstractDetailedElementDeserializer<AbstractTuleapDetailedElement> deserializer;

	@Test
	public void testParseDateMillisGMTPlusOne() throws ParseException {
		Date date = deserializer.parseISO8601Date("2013-11-30T12:23:58.123+01:00");
		Date expected = ParserUtil.getUTCDate(2013, 10, 30, 11, 23, 58, 123); // 1 hour before in UTC
		assertEquals(expected, date);
	}

	@Test
	public void testParseDateMillisGMTMinusOne() throws ParseException {
		Date date = deserializer.parseISO8601Date("2013-11-30T12:23:58.123-01:00");
		Date expected = ParserUtil.getUTCDate(2013, 10, 30, 13, 23, 58, 123); // 1 hour after in UTC
		assertEquals(expected, date);
	}

	@Test
	public void testParseDateMillisGMT() throws ParseException {
		Date date = deserializer.parseISO8601Date("2013-11-30T12:23:58.123Z");
		Date expected = ParserUtil.getUTCDate(2013, 10, 30, 12, 23, 58, 123);
		assertEquals(expected, date);
	}

	@Test
	public void testParseDateGMTPlusOne() throws ParseException {
		Date date = deserializer.parseISO8601Date("2013-11-30T12:23:58+01:00");
		Date expected = ParserUtil.getUTCDate(2013, 10, 30, 11, 23, 58, 0); // 1 hour before in UTC
		assertEquals(expected, date);
	}

	@Test
	public void testParseDateGMTMinusOne() throws ParseException {
		Date date = deserializer.parseISO8601Date("2013-11-30T12:23:58-01:00");
		Date expected = ParserUtil.getUTCDate(2013, 10, 30, 13, 23, 58, 0); // 1 hour after in UTC
		assertEquals(expected, date);
	}

	@Test
	public void testParseDateGMT() throws ParseException {
		Date date = deserializer.parseISO8601Date("2013-11-30T12:23:58Z");
		Date expected = ParserUtil.getUTCDate(2013, 10, 30, 12, 23, 58, 0);
		assertEquals(expected, date);
	}

	@Before
	public void setUp() {
		deserializer = new AbstractDetailedElementDeserializer<AbstractTuleapDetailedElement>() {
			@Override
			protected AbstractTuleapDetailedElement getPrototype() {
				return null;
			}
		};
	}
}
