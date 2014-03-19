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

import com.google.gson.Gson;

import org.eclipse.mylyn.tuleap.core.internal.model.data.agile.TuleapBurndown;
import org.eclipse.mylyn.tuleap.core.internal.parser.TuleapGsonProvider;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests the JSON deserialization of {@link TuleapBurndown}.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapBurndownDeserializerTest {

	private Gson gson;

	@Before
	public void setUp() {
		gson = TuleapGsonProvider.defaultGson();
	}

	/**
	 * Test a complete burndown.
	 */
	@Test
	public void testDeserializeCompleteBurndown() {
		String json = ParserUtil.loadFile("/burndowns/burndown-0.json");
		TuleapBurndown tuleapBurndown = gson.fromJson(json, TuleapBurndown.class);
		assertNotNull(tuleapBurndown);
		assertEquals(11, tuleapBurndown.getDuration());
		assertThat(tuleapBurndown.getCapacity(), is(15.00));
		assertEquals(12, tuleapBurndown.getPoints().length);
		for (int j = 0; j < 7; j++) {
			assertThat(tuleapBurndown.getPoints()[j], is(13.00));
		}
		for (int j = 7; j < tuleapBurndown.getPoints().length; j++) {
			assertThat(tuleapBurndown.getPoints()[j], is(12.75));
		}
	}

	/**
	 * Test a burndown with null duration.
	 */
	@Test
	public void testDeserializeBurndownWithNullDuration() {
		String json = ParserUtil.loadFile("/burndowns/burndown-2.json");
		TuleapBurndown tuleapBurndown = gson.fromJson(json, TuleapBurndown.class);
		assertNotNull(tuleapBurndown);
		assertEquals(0, tuleapBurndown.getDuration());
		assertThat(tuleapBurndown.getCapacity(), is(27.00));
		assertEquals(12, tuleapBurndown.getPoints().length);
		for (int j = 0; j < 7; j++) {
			assertThat(tuleapBurndown.getPoints()[j], is(13.00));
		}
		for (int j = 7; j < tuleapBurndown.getPoints().length; j++) {
			assertThat(tuleapBurndown.getPoints()[j], is(12.75));
		}
	}

	/**
	 * Test a burndown with null capacity.
	 */
	@Test
	public void testDeserializeBurndownWithNullCapacity() {
		String json = ParserUtil.loadFile("/burndowns/burndown-3.json");
		TuleapBurndown tuleapBurndown = gson.fromJson(json, TuleapBurndown.class);
		assertNotNull(tuleapBurndown);
		assertEquals(11, tuleapBurndown.getDuration());
		assertThat(tuleapBurndown.getCapacity(), is(0.0));
		assertEquals(12, tuleapBurndown.getPoints().length);
		for (int j = 0; j < 7; j++) {
			assertThat(tuleapBurndown.getPoints()[j], is(13.00));
		}
		for (int j = 7; j < tuleapBurndown.getPoints().length; j++) {
			assertThat(tuleapBurndown.getPoints()[j], is(12.75));
		}
	}

	/**
	 * Test a burndown with empty points capacity.
	 */
	@Test
	public void testDeserializeBurndownWithEmptyPoints() {
		String json = ParserUtil.loadFile("/burndowns/burndown-4.json");
		TuleapBurndown tuleapBurndown = gson.fromJson(json, TuleapBurndown.class);
		assertNotNull(tuleapBurndown);
		assertEquals(11, tuleapBurndown.getDuration());
		assertThat(tuleapBurndown.getCapacity(), is(27.0));
		assertEquals(0, tuleapBurndown.getPoints().length);
	}

	/**
	 * Test a burndown with null points capacity.
	 */
	@Test
	public void testDeserializeBurndownWithNullPoints() {
		String json = ParserUtil.loadFile("/burndowns/burndown-5.json");
		TuleapBurndown tuleapBurndown = gson.fromJson(json, TuleapBurndown.class);
		assertNotNull(tuleapBurndown);
		assertEquals(11, tuleapBurndown.getDuration());
		assertThat(tuleapBurndown.getCapacity(), is(27.0));
		assertNotNull(tuleapBurndown.getPoints());
		assertEquals(0, tuleapBurndown.getPoints().length);
	}
}
