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

import com.google.gson.Gson;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapInteger;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.model.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapArtifactWithComment;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;
import org.tuleap.mylyn.task.internal.core.parser.TuleapGsonProvider;

import static org.junit.Assert.assertEquals;

/**
 * This class is used to test serializing the JSON representation of a {@link TuleapArtifactWithComment}.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapArtifactWithCommentSerializerTest {

	private Gson gson;

	private TuleapReference trackerRef;

	private TuleapReference projectRef;

	@Before
	public void setUp() {
		gson = TuleapGsonProvider.defaultGson();
		trackerRef = new TuleapReference(100, "t/100");
		projectRef = new TuleapReference(50, "p/50");
	}

	@Test
	public void testArtifactWithoutValuesAndComment() {
		TuleapArtifactWithComment artifact = new TuleapArtifactWithComment(3, trackerRef, projectRef);
		assertEquals("{\"values\":[]}", gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithoutValues() {
		TuleapArtifactWithComment artifact = new TuleapArtifactWithComment(3, trackerRef, projectRef);
		final String newComment = "This is a new comment";
		artifact.setNewComment(newComment);
		assertEquals("{\"values\":[],\"comment\":{\"body\":\"This is a new comment\",\"format\":\"text\"}}",
				gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithoutCommentUpdatable() {
		TuleapArtifactWithComment artifact = new TuleapArtifactWithComment(3, trackerRef, projectRef);
		TuleapString field = new TuleapString(222);
		field.setPermissions(new String[] {"update" });
		artifact.addField(field);
		artifact.addFieldValue(new LiteralFieldValue(222, "test"));
		assertEquals("{\"values\":[{\"field_id\":222,\"value\":\"test\"}]}", gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithoutCommentNonUpdatable() {
		TuleapArtifactWithComment artifact = new TuleapArtifactWithComment(3, trackerRef, projectRef);
		TuleapString field = new TuleapString(222);
		field.setPermissions(new String[] {"read", "submit" });
		artifact.addField(field);
		artifact.addFieldValue(new LiteralFieldValue(222, "test"));
		assertEquals("{\"values\":[]}", gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldString() {
		TuleapArtifactWithComment artifact = new TuleapArtifactWithComment(3, trackerRef, projectRef);
		final String newComment = "This is a new comment";
		artifact.setNewComment(newComment);

		TuleapString field = new TuleapString(222);
		field.setPermissions(new String[] {"update" });
		artifact.addField(field);
		artifact.addFieldValue(new LiteralFieldValue(222, "test"));
		assertEquals(
				"{\"values\":[{\"field_id\":222,\"value\":\"test\"}],\"comment\":{\"body\":\"This is a new comment\",\"format\":\"text\"}}",
				gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldStringNonUpdatable() {
		TuleapArtifactWithComment artifact = new TuleapArtifactWithComment(3, trackerRef, projectRef);
		final String newComment = "This is a new comment";
		artifact.setNewComment(newComment);

		TuleapString field = new TuleapString(222);
		field.setPermissions(new String[] {"read", "submit" });
		artifact.addField(field);
		artifact.addFieldValue(new LiteralFieldValue(222, "test"));
		assertEquals("{\"values\":[],\"comment\":{\"body\":\"This is a new comment\",\"format\":\"text\"}}",
				gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldInteger() {
		TuleapArtifactWithComment artifact = new TuleapArtifactWithComment(3, trackerRef, projectRef);
		final String newComment = "This is a new comment";
		artifact.setNewComment(newComment);

		TuleapInteger field = new TuleapInteger(222);
		field.setPermissions(new String[] {"update" });
		artifact.addField(field);
		artifact.addFieldValue(new LiteralFieldValue(222, "666"));
		assertEquals(
				"{\"values\":[{\"field_id\":222,\"value\":\"666\"}],\"comment\":{\"body\":\"This is a new comment\",\"format\":\"text\"}}",
				gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldIntegerNonUpdatable() {
		TuleapArtifactWithComment artifact = new TuleapArtifactWithComment(3, trackerRef, projectRef);
		final String newComment = "This is a new comment";
		artifact.setNewComment(newComment);

		TuleapInteger field = new TuleapInteger(222);
		field.setPermissions(new String[] {"read", "submit" });
		artifact.addField(field);
		artifact.addFieldValue(new LiteralFieldValue(222, "666"));
		assertEquals("{\"values\":[],\"comment\":{\"body\":\"This is a new comment\",\"format\":\"text\"}}",
				gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldSelectBoxUpdatable() {
		TuleapArtifactWithComment artifact = new TuleapArtifactWithComment(3, trackerRef, projectRef);
		final String newComment = "This is a new comment";
		artifact.setNewComment(newComment);

		TuleapSelectBox field = new TuleapSelectBox(222);
		field.setPermissions(new String[] {"update" });
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(field);
		artifact.addFieldValue(new BoundFieldValue(222, Arrays.asList(0)));
		assertEquals(
				"{\"values\":[{\"field_id\":222,\"bind_value_ids\":[0]}],\"comment\":{\"body\":\"This is a new comment\",\"format\":\"text\"}}",
				gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldSelectBoxNonUpdatable() {
		TuleapArtifactWithComment artifact = new TuleapArtifactWithComment(3, trackerRef, projectRef);
		final String newComment = "This is a new comment";
		artifact.setNewComment(newComment);

		TuleapSelectBox field = new TuleapSelectBox(222);
		field.setPermissions(new String[] {"read", "submit" });
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(field);
		artifact.addFieldValue(new BoundFieldValue(222, Arrays.asList(0)));
		assertEquals("{\"values\":[],\"comment\":{\"body\":\"This is a new comment\",\"format\":\"text\"}}",
				gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldMultiSelectBoxUpdatable() {
		TuleapArtifactWithComment artifact = new TuleapArtifactWithComment(3, trackerRef, projectRef);
		final String newComment = "This is a new comment";
		artifact.setNewComment(newComment);

		TuleapSelectBox field = new TuleapSelectBox(222);
		field.setPermissions(new String[] {"update" });
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(field);
		artifact.addFieldValue(new BoundFieldValue(222, Arrays.asList(0, 1)));
		assertEquals(
				"{\"values\":[{\"field_id\":222,\"bind_value_ids\":[0,1]}],\"comment\":{\"body\":\"This is a new comment\",\"format\":\"text\"}}",
				gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldMultiSelectBoxNonUpdatable() {
		TuleapArtifactWithComment artifact = new TuleapArtifactWithComment(3, trackerRef, projectRef);
		final String newComment = "This is a new comment";
		artifact.setNewComment(newComment);

		TuleapSelectBox field = new TuleapSelectBox(222);
		field.setPermissions(new String[] {"read", "submit" });
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(field);
		artifact.addFieldValue(new BoundFieldValue(222, Arrays.asList(0, 1)));
		assertEquals("{\"values\":[],\"comment\":{\"body\":\"This is a new comment\",\"format\":\"text\"}}",
				gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithManyFieldsUpdatable() {
		TuleapArtifactWithComment artifact = new TuleapArtifactWithComment(3, trackerRef, projectRef);
		final String newComment = "This is a new comment";
		artifact.setNewComment(newComment);

		TuleapSelectBox firstField = new TuleapSelectBox(220);
		firstField.setPermissions(new String[] {"update" });
		firstField.addItem(new TuleapSelectBoxItem(0));
		firstField.addItem(new TuleapSelectBoxItem(1));
		firstField.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(firstField);
		artifact.addFieldValue(new BoundFieldValue(220, Arrays.asList(0, 1)));

		TuleapSelectBox secondField = new TuleapSelectBox(221);
		secondField.setPermissions(new String[] {"update" });
		secondField.addItem(new TuleapSelectBoxItem(0));
		secondField.addItem(new TuleapSelectBoxItem(1));
		secondField.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(secondField);
		artifact.addFieldValue(new BoundFieldValue(221, Arrays.asList(0, 1)));

		TuleapInteger thirdField = new TuleapInteger(222);
		thirdField.setPermissions(new String[] {"update" });
		artifact.addField(thirdField);
		artifact.addFieldValue(new LiteralFieldValue(222, "666"));

		TuleapString fourthField = new TuleapString(223);
		fourthField.setPermissions(new String[] {"update" });
		artifact.addField(fourthField);
		artifact.addFieldValue(new LiteralFieldValue(223, "test"));
		assertEquals(
				"{\"values\":[{\"field_id\":220,\"bind_value_ids\":[0,1]},{\"field_id\":221,\"bind_value_ids\":[0,1]},{\"field_id\":222,\"value\":\"666\"},{\"field_id\":223,\"value\":\"test\"}],\"comment\":{\"body\":\"This is a new comment\",\"format\":\"text\"}}",
				gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithManyFieldsNonUpdatable() {
		TuleapArtifactWithComment artifact = new TuleapArtifactWithComment(3, trackerRef, projectRef);
		final String newComment = "This is a new comment";
		artifact.setNewComment(newComment);

		TuleapSelectBox firstField = new TuleapSelectBox(220);
		firstField.setPermissions(new String[] {"read", "submit" });
		firstField.addItem(new TuleapSelectBoxItem(0));
		firstField.addItem(new TuleapSelectBoxItem(1));
		firstField.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(firstField);
		artifact.addFieldValue(new BoundFieldValue(220, Arrays.asList(0, 1)));

		TuleapSelectBox secondField = new TuleapSelectBox(221);
		secondField.setPermissions(new String[] {"read", "submit" });
		secondField.addItem(new TuleapSelectBoxItem(0));
		secondField.addItem(new TuleapSelectBoxItem(1));
		secondField.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(secondField);
		artifact.addFieldValue(new BoundFieldValue(221, Arrays.asList(0, 1)));

		TuleapInteger thirdField = new TuleapInteger(222);
		thirdField.setPermissions(new String[] {"read", "submit" });
		artifact.addField(thirdField);
		artifact.addFieldValue(new LiteralFieldValue(222, "666"));

		TuleapString fourthField = new TuleapString(223);
		fourthField.setPermissions(new String[] {"read", "submit" });
		artifact.addField(fourthField);
		artifact.addFieldValue(new LiteralFieldValue(223, "test"));
		assertEquals("{\"values\":[],\"comment\":{\"body\":\"This is a new comment\",\"format\":\"text\"}}",
				gson.toJson(artifact));
	}
}
