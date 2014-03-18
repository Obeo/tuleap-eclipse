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

import com.google.gson.Gson;

import java.util.Arrays;

import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapFileUpload;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapInteger;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBoxItem;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapString;
import org.eclipse.mylyn.tuleap.core.internal.model.data.ArtifactReference;
import org.eclipse.mylyn.tuleap.core.internal.model.data.BoundFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.LiteralFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapReference;
import org.eclipse.mylyn.tuleap.core.internal.model.data.agile.TuleapCard;
import org.eclipse.mylyn.tuleap.core.internal.parser.TuleapGsonProvider;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapCardSerializerTest {

	private Gson gson;

	private TuleapReference trackerRef;

	private TuleapReference projectRef;

	private ArtifactReference artifactRef;

	@Before
	public void setUp() {
		gson = TuleapGsonProvider.defaultGson();
		trackerRef = new TuleapReference(100, "t/100");
		projectRef = new TuleapReference(50, "p/50");
		artifactRef = new ArtifactReference(123, "a/123", trackerRef);
	}

	@Test
	public void testWithColumnId() {
		TuleapCard card = new TuleapCard("123", artifactRef, projectRef);
		card.setLabel("label");
		card.setColumnId(12);
		assertEquals("{\"label\":\"label\",\"values\":[],\"column_id\":12}", gson.toJson(card));
	}

	@Test
	public void testWithoutColumnId() {
		TuleapCard card = new TuleapCard("123", artifactRef, projectRef);
		card.setLabel("label");
		assertEquals("{\"label\":\"label\",\"values\":[],\"column_id\":null}", gson.toJson(card));
	}

	@Test
	public void testWithFieldString() {
		TuleapCard card = new TuleapCard("123", artifactRef, projectRef);
		card.setLabel("label");
		card.setColumnId(12);
		TuleapString field = new TuleapString(222);
		card.addField(field);
		card.addFieldValue(new LiteralFieldValue(222, "test"));
		assertEquals(
				"{\"label\":\"label\",\"values\":[{\"field_id\":222,\"value\":\"test\"}],\"column_id\":12}",
				gson.toJson(card));
	}

	@Test
	public void testWithFieldInteger() {
		TuleapCard card = new TuleapCard("123", artifactRef, projectRef);
		card.setLabel("label");
		card.setColumnId(12);
		TuleapInteger field = new TuleapInteger(222);
		card.addField(field);
		card.addFieldValue(new LiteralFieldValue(222, "666"));
		assertEquals(
				"{\"label\":\"label\",\"values\":[{\"field_id\":222,\"value\":\"666\"}],\"column_id\":12}",
				gson.toJson(card));
	}

	@Test
	public void testWithFieldSelectBox() {
		TuleapCard card = new TuleapCard("123", artifactRef, projectRef);
		card.setLabel("label");
		card.setColumnId(12);
		TuleapSelectBox field = new TuleapSelectBox(222);
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		card.addField(field);
		card.addFieldValue(new BoundFieldValue(222, Arrays.asList(0)));
		assertEquals(
				"{\"label\":\"label\",\"values\":[{\"field_id\":222,\"bind_value_ids\":[0]}],\"column_id\":12}",
				gson.toJson(card));
	}

	@Test
	public void testWithFieldMultiSelectBox() {
		TuleapCard card = new TuleapCard("123", artifactRef, projectRef);
		card.setLabel("label");
		card.setColumnId(12);
		TuleapSelectBox field = new TuleapSelectBox(222);
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		card.addField(field);
		card.addFieldValue(new BoundFieldValue(222, Arrays.asList(0, 1)));
		assertEquals(
				"{\"label\":\"label\",\"values\":[{\"field_id\":222,\"bind_value_ids\":[0,1]}],\"column_id\":12}",
				gson.toJson(card));
	}

	@Test
	public void testWithFieldWithoutValue() {
		TuleapCard card = new TuleapCard("123", artifactRef, projectRef);
		card.setLabel("label");
		card.setColumnId(12);
		TuleapString field = new TuleapString(222);
		card.addField(field);
		assertEquals("{\"label\":\"label\",\"values\":[{\"field_id\":222,\"value\":null}],\"column_id\":12}",
				gson.toJson(card));
	}

	@Test
	public void testWithNonSerializableField() {
		TuleapCard card = new TuleapCard("123", artifactRef, projectRef);
		card.setLabel("label");
		card.setColumnId(12);
		TuleapFileUpload tuleapFileUpload = new TuleapFileUpload(12);
		card.addField(tuleapFileUpload);
		assertEquals("{\"label\":\"label\",\"values\":[],\"column_id\":12}", gson.toJson(card));
	}
}
