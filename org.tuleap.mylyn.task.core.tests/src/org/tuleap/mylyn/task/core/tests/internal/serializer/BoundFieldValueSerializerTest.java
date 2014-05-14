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
package org.tuleap.mylyn.task.core.tests.internal.serializer;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.core.internal.model.data.BoundFieldValue;
import org.tuleap.mylyn.task.core.internal.serializer.BoundFieldValueSerializer;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class BoundFieldValueSerializerTest {

	private Gson gson;

	@Before
	public void setUp() {
		gson = new GsonBuilder().registerTypeAdapter(BoundFieldValue.class, new BoundFieldValueSerializer())
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).serializeNulls()
				.create();
	}

	@Test
	public void testCompleteBoundFieldValueSerializationWithOneValue() {
		String expected = "{\"field_id\":222,\"bind_value_ids\":[0]}";
		BoundFieldValue value = new BoundFieldValue(222, Arrays.asList(0));

		String json = gson.toJson(value);
		assertEquals(expected, json);
	}

	@Test
	public void testCompleteBoundFieldValueSerializationWithTwoValues() {
		String expected = "{\"field_id\":222,\"bind_value_ids\":[0,1]}";
		BoundFieldValue value = new BoundFieldValue(222, Arrays.asList(0, 1));

		String json = gson.toJson(value);
		assertEquals(expected, json);
	}

	@Test
	public void testCompleteBoundFieldValueSerializationWithEmptyValue() {
		String expected = "{\"field_id\":222,\"bind_value_ids\":[]}";
		BoundFieldValue value = new BoundFieldValue(222, new ArrayList<Integer>());

		String json = gson.toJson(value);
		assertEquals(expected, json);
	}
}
