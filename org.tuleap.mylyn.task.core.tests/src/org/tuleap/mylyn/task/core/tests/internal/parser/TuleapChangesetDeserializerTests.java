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

import com.google.gson.Gson;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapElementComment;
import org.tuleap.mylyn.task.core.internal.parser.DateIso8601Adapter;
import org.tuleap.mylyn.task.core.internal.parser.TuleapGsonProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tests of the JSON deserialization of artifact changeset.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapChangesetDeserializerTests {

	private Gson gson;

	/**
	 * Test parsing a complete changeSet .
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testParsingCompleteChangeSet() throws ParseException {
		String json = ParserUtil.loadFile("/changesets/changeset-1.json");
		TuleapElementComment changeSet = gson.fromJson(json, TuleapElementComment.class);
		assertNotNull(changeSet);
		assertEquals("First comment body", changeSet.getBody()); //$NON-NLS-1$
		assertEquals(DateIso8601Adapter.parseIso8601Date("2014-02-27T17:29:40+01:00"), changeSet
				.getSubmittedOn());
		assertEquals(101, changeSet.getSubmitter().getId());
		assertEquals("email@obeo.fr", changeSet.getSubmitter().getEmail()); //$NON-NLS-1$
	}

	/**
	 * Test parsing a changeSet with empty body .
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testParsingChangeSetWithEmptyBody() throws ParseException {
		String json = ParserUtil.loadFile("/changesets/changeset-2.json");
		TuleapElementComment changeSet = gson.fromJson(json, TuleapElementComment.class);
		assertNotNull(changeSet);
		assertEquals("", changeSet.getBody()); //$NON-NLS-1$
		assertEquals(DateIso8601Adapter.parseIso8601Date("2014-02-27T17:29:40+01:00"), changeSet
				.getSubmittedOn());
		assertEquals(101, changeSet.getSubmitter().getId());
		assertEquals("email@obeo.fr", changeSet.getSubmitter().getEmail()); //$NON-NLS-1$
	}

	/**
	 * Test parsing a changeSet with body null.
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testParsingChangeSetWithBodyNull() throws ParseException {
		String json = ParserUtil.loadFile("/changesets/changeset-3.json");
		TuleapElementComment changeSet = gson.fromJson(json, TuleapElementComment.class);
		assertNotNull(changeSet);
		assertNull(changeSet.getBody());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2014-02-27T17:29:40+01:00"), changeSet
				.getSubmittedOn());
		assertEquals(101, changeSet.getSubmitter().getId());
		assertEquals("email@obeo.fr", changeSet.getSubmitter().getEmail()); //$NON-NLS-1$
	}

	/**
	 * Test parsing a changeSet with null mail.
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testParsingChangeSetWithEmailNull() throws ParseException {
		String json = ParserUtil.loadFile("/changesets/changeset-4.json");
		TuleapElementComment changeSet = gson.fromJson(json, TuleapElementComment.class);
		assertNotNull(changeSet);
		assertEquals("Fourth comment body", changeSet.getBody()); //$NON-NLS-1$
		assertEquals(DateIso8601Adapter.parseIso8601Date("2014-02-27T17:29:40+01:00"), changeSet
				.getSubmittedOn());
		assertEquals(101, changeSet.getSubmitter().getId());
		assertNull(changeSet.getSubmitter().getEmail());
	}

	@Before
	public void setUp() {
		gson = TuleapGsonProvider.defaultGson();
	}
}
