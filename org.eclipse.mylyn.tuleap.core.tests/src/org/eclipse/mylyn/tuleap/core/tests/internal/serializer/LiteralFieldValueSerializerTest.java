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
package org.eclipse.mylyn.tuleap.core.tests.internal.serializer;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.eclipse.mylyn.tuleap.core.internal.model.data.LiteralFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.serializer.LiteralFieldValueSerializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class LiteralFieldValueSerializerTest {

	private Gson gson;

	@Before
	public void setUp() {
		gson = new GsonBuilder().registerTypeAdapter(LiteralFieldValue.class,
				new LiteralFieldValueSerializer()).setFieldNamingPolicy(
				FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).serializeNulls().create();
	}

	@Test
	public void testCompleteLiteralFieldValueSerialization() {
		String expected = "{\"field_id\":222,\"value\":\"666\"}";
		LiteralFieldValue value = new LiteralFieldValue(222, "666");
		String json = gson.toJson(value);
		assertEquals(expected, json);
	}

	@Test
	public void testLiteralFieldValueSerializationWithNullValue() {
		String expected = "{\"field_id\":222,\"value\":null}";
		LiteralFieldValue value = new LiteralFieldValue(222, null);
		String json = gson.toJson(value);
		assertEquals(expected, json);
	}

	@Test
	public void testLiteralFieldValueSerializationWithEmptyValue() {
		String expected = "{\"field_id\":222,\"value\":\"\"}";
		LiteralFieldValue value = new LiteralFieldValue(222, "");
		String json = gson.toJson(value);
		assertEquals(expected, json);
	}
}
