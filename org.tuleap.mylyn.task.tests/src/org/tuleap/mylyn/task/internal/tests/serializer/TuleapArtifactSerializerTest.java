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
package org.tuleap.mylyn.task.internal.tests.serializer;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapInteger;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.model.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;
import org.tuleap.mylyn.task.internal.core.serializer.TuleapArtifactSerializer;

import static org.junit.Assert.assertEquals;

/**
 * This class is used to test serializing the JSON representation of a {@link TuleapArtifact}.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapArtifactSerializerTest {

	private TuleapArtifactSerializer serializer;

	private TuleapReference trackerRef;

	private TuleapReference projectRef;

	@Before
	public void setUp() {
		serializer = new TuleapArtifactSerializer();
		trackerRef = new TuleapReference(100, "t/100");
		projectRef = new TuleapReference(50, "p/50");
	}

	@Test
	public void testArtifactWithoutValues() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);
		assertEquals("{\"values\":[],\"tracker\":{\"id\":100,\"uri\":\"t/100\"}}", serializer.serialize(
				artifact, null, null).toString());
	}

	@Test
	public void testArtifactWithFieldString() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapString field = new TuleapString(222);
		artifact.addField(field);
		artifact.addFieldValue(new LiteralFieldValue(222, "test"));
		assertEquals(
				"{\"values\":[{\"field_id\":222,\"value\":\"test\"}],\"tracker\":{\"id\":100,\"uri\":\"t/100\"}}",
				serializer.serialize(artifact, null, null).toString());
	}

	@Test
	public void testArtifactWithFieldInteger() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapInteger field = new TuleapInteger(222);
		artifact.addField(field);
		artifact.addFieldValue(new LiteralFieldValue(222, "666"));
		assertEquals(
				"{\"values\":[{\"field_id\":222,\"value\":\"666\"}],\"tracker\":{\"id\":100,\"uri\":\"t/100\"}}",
				serializer.serialize(artifact, null, null).toString());
	}

	@Test
	public void testArtifactWithFieldSelectBox() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox field = new TuleapSelectBox(222);
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(field);
		artifact.addFieldValue(new BoundFieldValue(222, Arrays.asList(0)));
		assertEquals(
				"{\"values\":[{\"field_id\":222,\"bind_value_ids\":[0]}],\"tracker\":{\"id\":100,\"uri\":\"t/100\"}}",
				serializer.serialize(artifact, null, null).toString());
	}

	@Test
	public void testArtifactWithFieldMultiSelectBox() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox field = new TuleapSelectBox(222);
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(field);
		artifact.addFieldValue(new BoundFieldValue(222, Arrays.asList(0, 1)));
		assertEquals(
				"{\"values\":[{\"field_id\":222,\"bind_value_ids\":[0,1]}],\"tracker\":{\"id\":100,\"uri\":\"t/100\"}}",
				serializer.serialize(artifact, null, null).toString());
	}

	@Test
	public void testArtifactWithManyFields() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox firstField = new TuleapSelectBox(220);
		firstField.addItem(new TuleapSelectBoxItem(0));
		firstField.addItem(new TuleapSelectBoxItem(1));
		firstField.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(firstField);
		artifact.addFieldValue(new BoundFieldValue(220, Arrays.asList(0, 1)));

		TuleapSelectBox secondField = new TuleapSelectBox(221);
		secondField.addItem(new TuleapSelectBoxItem(0));
		secondField.addItem(new TuleapSelectBoxItem(1));
		secondField.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(secondField);
		artifact.addFieldValue(new BoundFieldValue(221, Arrays.asList(0, 1)));

		TuleapInteger thirdField = new TuleapInteger(222);
		artifact.addField(thirdField);
		artifact.addFieldValue(new LiteralFieldValue(222, "666"));

		TuleapString fourthField = new TuleapString(223);
		artifact.addField(fourthField);
		artifact.addFieldValue(new LiteralFieldValue(223, "test"));
		assertEquals(
				"{\"values\":[{\"field_id\":220,\"bind_value_ids\":[0,1]},{\"field_id\":221,\"bind_value_ids\":[0,1]},{\"field_id\":222,\"value\":\"666\"},{\"field_id\":223,\"value\":\"test\"}],\"tracker\":{\"id\":100,\"uri\":\"t/100\"}}",
				serializer.serialize(artifact, null, null).toString());
	}
}
