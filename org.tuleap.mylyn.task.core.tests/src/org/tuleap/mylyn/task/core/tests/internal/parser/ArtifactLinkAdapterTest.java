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

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.core.internal.model.data.ArtifactLinkFieldValue;
import org.tuleap.mylyn.task.core.internal.serializer.ArtifactLinkFieldValueAdapter;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class ArtifactLinkAdapterTest {

	private Gson gson;

	@Before
	public void setUp() {
		gson = new GsonBuilder().registerTypeAdapter(ArtifactLinkFieldValue.class,
				new ArtifactLinkFieldValueAdapter()).setFieldNamingPolicy(
				FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).serializeNulls().create();
	}

	@Test
	public void testArtifactLinkDeserialization() {
		String json = "{\"field_id\": 1183, \"label\": \"Linked Artifacts\", \"links\": ["
				+ "{\"id\": 137, \"uri\": \"artifacts/137\"}, {\"id\": 133, \"uri\": \"artifacts/133\"}"
				+ "]}";
		ArtifactLinkFieldValue value = gson.fromJson(json, ArtifactLinkFieldValue.class);
		assertEquals(1183, value.getFieldId());
		assertEquals(2, value.getLinks().length);
		assertEquals(137, value.getLinks()[0]);
		assertEquals(133, value.getLinks()[1]);
	}

	@Test
	public void testArtifactLinkDeserializationWithEmptyLinks() {
		String json = "{\"field_id\": 1183, \"label\": \"Linked Artifacts\", \"links\": []}";
		ArtifactLinkFieldValue value = gson.fromJson(json, ArtifactLinkFieldValue.class);
		assertEquals(1183, value.getFieldId());
		assertEquals(0, value.getLinks().length);
	}

	@Test
	public void testArtifactLinkDeserializationWithNullLinks() {
		String json = "{\"field_id\": 1183, \"label\": \"Linked Artifacts\", \"links\": null}";
		ArtifactLinkFieldValue value = gson.fromJson(json, ArtifactLinkFieldValue.class);
		assertEquals(1183, value.getFieldId());
		assertEquals(0, value.getLinks().length);
	}

	@Test
	public void testArtifactLinkDeserializationWithoutLinks() {
		String json = "{\"field_id\": 1183, \"label\": \"Linked Artifacts\"}";
		ArtifactLinkFieldValue value = gson.fromJson(json, ArtifactLinkFieldValue.class);
		assertEquals(1183, value.getFieldId());
		assertEquals(0, value.getLinks().length);
	}

	@Test
	public void testArtifactLinkSerialization() {
		String expected = "{\"field_id\":1183,\"links\":[{\"id\":137},{\"id\":133}]}";
		ArtifactLinkFieldValue value = new ArtifactLinkFieldValue(1183, new int[] {137, 133 });
		String json = gson.toJson(value);
		assertEquals(expected, json);
	}

	@Test
	public void testArtifactLinkSerializationWithEmptyLinks() {
		String expected = "{\"field_id\":1183,\"links\":[]}";
		ArtifactLinkFieldValue value = new ArtifactLinkFieldValue(1183, null);
		String json = gson.toJson(value);
		assertEquals(expected, json);
	}
}
