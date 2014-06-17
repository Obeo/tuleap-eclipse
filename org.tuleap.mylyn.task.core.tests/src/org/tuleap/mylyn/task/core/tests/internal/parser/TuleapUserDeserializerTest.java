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
import org.tuleap.mylyn.task.core.internal.model.config.TuleapUser;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapUserGroup;
import org.tuleap.mylyn.task.core.internal.parser.TuleapGsonProvider;

import static org.junit.Assert.assertEquals;

/**
 * Tests the JSON deserialization of {@link TuleapUser} and {@link TuleapUserGroup}.
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapUserDeserializerTest {

	private Gson gson;

	@Before
	public void setUp() {
		gson = TuleapGsonProvider.defaultGson();
	}

	@Test
	public void testDeserializeUserGroup() throws ParseException {
		String json = ParserUtil.loadFile("/groups/project_admins.json");
		TuleapUserGroup userGroup = gson.fromJson(json, TuleapUserGroup.class);
		assertEquals("1", userGroup.getId());
		assertEquals("Project Administrators", userGroup.getLabel());
		assertEquals("/user_groups/1", userGroup.getUri());
	}

	@Test
	public void testDeserializeUser() throws ParseException {
		String json = ParserUtil.loadFile("/users/an_user.json");
		TuleapUser aUser = gson.fromJson(json, TuleapUser.class);
		assertEquals(0, aUser.getId());
		assertEquals("John Doe", aUser.getRealName());
		assertEquals("doej", aUser.getUsername());
		assertEquals("doej@some.fr", aUser.getEmail());
	}
}
