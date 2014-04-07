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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapFormElement;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapArtifactLink;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapFileUpload;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapInteger;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBoxItem;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapString;
import org.eclipse.mylyn.tuleap.core.internal.model.data.ArtifactLinkFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AttachmentFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AttachmentValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.BoundFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.LiteralFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifact;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapArtifactWithAttachment;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapReference;
import org.eclipse.mylyn.tuleap.core.internal.parser.TuleapGsonProvider;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This class is used to test serializing the JSON representation of a {@link TuleapArtifact}.
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapArtifactSerializerTest {

	public static final String[] READ_CREATE = {AbstractTuleapFormElement.PERMISSION_READ,
			AbstractTuleapFormElement.PERMISSION_SUBMIT, };

	public static final String[] READ_UPDATE = {AbstractTuleapFormElement.PERMISSION_READ,
			AbstractTuleapFormElement.PERMISSION_UPDATE, };

	public static final String[] READ_ONLY = {AbstractTuleapFormElement.PERMISSION_READ };

	public static final String[] READ_CREATE_UPDATE = {AbstractTuleapFormElement.PERMISSION_READ,
			AbstractTuleapFormElement.PERMISSION_SUBMIT, AbstractTuleapFormElement.PERMISSION_UPDATE, };

	public static final String[] CREATE_UPDATE = {AbstractTuleapFormElement.PERMISSION_SUBMIT,
			AbstractTuleapFormElement.PERMISSION_UPDATE, };

	public static final String[] CREATE_ONLY = {AbstractTuleapFormElement.PERMISSION_SUBMIT, };

	public static final String[] UPDATE_ONLY = {AbstractTuleapFormElement.PERMISSION_UPDATE, };

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
	public void testArtifactWithoutValues() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);
		assertEquals("{\"values\":[],\"tracker\":{\"id\":100}}", gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldStringSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapString field = new TuleapString(222);
		field.setPermissions(CREATE_ONLY);
		artifact.addField(field);
		artifact.addFieldValue(new LiteralFieldValue(222, "test"));
		assertEquals("{\"values\":[],\"tracker\":{\"id\":100}}", gson.toJson(artifact));
	}

	@Test
	public void testNewArtifactWithFieldStringSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapString field = new TuleapString(222);
		field.setPermissions(CREATE_ONLY);
		artifact.addField(field);
		artifact.setNew(true); // Creation
		artifact.addFieldValue(new LiteralFieldValue(222, "test"));
		assertEquals("{\"values\":[{\"field_id\":222,\"value\":\"test\"}],\"tracker\":{\"id\":100}}", gson
				.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldStringUpdatable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapString field = new TuleapString(222);
		field.setPermissions(UPDATE_ONLY);
		artifact.addField(field);
		artifact.addFieldValue(new LiteralFieldValue(222, "test"));
		assertEquals("{\"values\":[{\"field_id\":222,\"value\":\"test\"}],\"tracker\":{\"id\":100}}", gson
				.toJson(artifact));
	}

	@Test
	public void testNewArtifactWithFieldStringUpdatable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapString field = new TuleapString(222);
		field.setPermissions(UPDATE_ONLY);
		artifact.addField(field);
		artifact.setNew(true); // Creation
		artifact.addFieldValue(new LiteralFieldValue(222, "test"));
		assertEquals("{\"values\":[],\"tracker\":{\"id\":100}}", gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldStringNonSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapString field = new TuleapString(222);
		field.setPermissions(READ_UPDATE);
		artifact.addField(field);
		artifact.addFieldValue(new LiteralFieldValue(222, "test"));
		assertEquals("{\"values\":[{\"field_id\":222,\"value\":\"test\"}],\"tracker\":{\"id\":100}}", gson
				.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldIntegerSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapInteger field = new TuleapInteger(222);
		field.setPermissions(CREATE_ONLY);
		artifact.addField(field);
		artifact.addFieldValue(new LiteralFieldValue(222, "666"));
		assertEquals("{\"values\":[],\"tracker\":{\"id\":100}}", gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldIntegerUpdatable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapInteger field = new TuleapInteger(222);
		field.setPermissions(UPDATE_ONLY);
		artifact.addField(field);
		artifact.addFieldValue(new LiteralFieldValue(222, "666"));
		assertEquals("{\"values\":[{\"field_id\":222,\"value\":\"666\"}],\"tracker\":{\"id\":100}}", gson
				.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldIntegerNonSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapInteger field = new TuleapInteger(222);
		field.setPermissions(READ_UPDATE);
		artifact.addField(field);
		artifact.addFieldValue(new LiteralFieldValue(222, "666"));
		assertEquals("{\"values\":[{\"field_id\":222,\"value\":\"666\"}],\"tracker\":{\"id\":100}}", gson
				.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldSelectBoxSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox field = new TuleapSelectBox(222);
		field.setPermissions(CREATE_ONLY);
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(field);
		artifact.addFieldValue(new BoundFieldValue(222, Arrays.asList(0)));
		assertEquals("{\"values\":[],\"tracker\":{\"id\":100}}", gson.toJson(artifact));
	}

	@Test
	public void testNewArtifactWithFieldSelectBoxSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox field = new TuleapSelectBox(222);
		field.setPermissions(CREATE_ONLY);
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(field);
		artifact.addFieldValue(new BoundFieldValue(222, Arrays.asList(0)));
		artifact.setNew(true); // Creation
		assertEquals("{\"values\":[{\"field_id\":222,\"bind_value_ids\":[0]}],\"tracker\":{\"id\":100}}",
				gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldSelectBoxUpdatable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox field = new TuleapSelectBox(222);
		field.setPermissions(UPDATE_ONLY);
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(field);
		artifact.addFieldValue(new BoundFieldValue(222, Arrays.asList(0)));
		assertEquals("{\"values\":[{\"field_id\":222,\"bind_value_ids\":[0]}],\"tracker\":{\"id\":100}}",
				gson.toJson(artifact));
	}

	@Test
	public void testNewArtifactWithFieldSelectBoxUpdatable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox field = new TuleapSelectBox(222);
		field.setPermissions(UPDATE_ONLY);
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(field);
		artifact.setNew(true); // Creation
		artifact.addFieldValue(new BoundFieldValue(222, Arrays.asList(0)));
		assertEquals("{\"values\":[],\"tracker\":{\"id\":100}}", gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldSelectBoxNonSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox field = new TuleapSelectBox(222);
		field.setPermissions(READ_UPDATE);
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(field);
		artifact.addFieldValue(new BoundFieldValue(222, Arrays.asList(0)));
		assertEquals("{\"values\":[{\"field_id\":222,\"bind_value_ids\":[0]}],\"tracker\":{\"id\":100}}",
				gson.toJson(artifact));
	}

	@Test
	public void testNewArtifactWithFieldSelectBoxNonSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox field = new TuleapSelectBox(222);
		field.setPermissions(READ_UPDATE);
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(field);
		artifact.setNew(true); // Creation
		artifact.addFieldValue(new BoundFieldValue(222, Arrays.asList(0)));
		assertEquals("{\"values\":[],\"tracker\":{\"id\":100}}", gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldMultiSelectBoxSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox field = new TuleapSelectBox(222);
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		field.setPermissions(CREATE_ONLY);
		artifact.addField(field);
		artifact.addFieldValue(new BoundFieldValue(222, Arrays.asList(0, 1)));
		assertEquals("{\"values\":[],\"tracker\":{\"id\":100}}", gson.toJson(artifact));
	}

	@Test
	public void testNewArtifactWithFieldMultiSelectBoxSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox field = new TuleapSelectBox(222);
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		field.setPermissions(CREATE_ONLY);
		artifact.addField(field);
		artifact.setNew(true); // Creation
		artifact.addFieldValue(new BoundFieldValue(222, Arrays.asList(0, 1)));
		assertEquals("{\"values\":[{\"field_id\":222,\"bind_value_ids\":[0,1]}],\"tracker\":{\"id\":100}}",
				gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldMultiSelectBoxUpdatable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox field = new TuleapSelectBox(222);
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		field.setPermissions(UPDATE_ONLY);
		artifact.addField(field);
		artifact.addFieldValue(new BoundFieldValue(222, Arrays.asList(0, 1)));
		assertEquals("{\"values\":[{\"field_id\":222,\"bind_value_ids\":[0,1]}],\"tracker\":{\"id\":100}}",
				gson.toJson(artifact));
	}

	@Test
	public void testNewArtifactWithFieldMultiSelectBoxUpdatable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox field = new TuleapSelectBox(222);
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		field.setPermissions(UPDATE_ONLY);
		artifact.addField(field);
		artifact.setNew(true); // Creation
		artifact.addFieldValue(new BoundFieldValue(222, Arrays.asList(0, 1)));
		assertEquals("{\"values\":[],\"tracker\":{\"id\":100}}", gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldMultiSelectBoxNonSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox field = new TuleapSelectBox(222);
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		field.setPermissions(READ_UPDATE);
		artifact.addField(field);
		artifact.addFieldValue(new BoundFieldValue(222, Arrays.asList(0, 1)));
		assertEquals("{\"values\":[{\"field_id\":222,\"bind_value_ids\":[0,1]}],\"tracker\":{\"id\":100}}",
				gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldAttachmentSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapFileUpload field = new TuleapFileUpload(222);
		field.setPermissions(CREATE_ONLY);
		artifact.addField(field);

		List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		attachments.add(new AttachmentValue("100000", "first name", 1, 123456, //$NON-NLS-1$ //$NON-NLS-2$
				"first description", "first type", "")); //$NON-NLS-1$ //$NON-NLS-2$
		attachments.add(new AttachmentValue("100001", "second name", 2, 789456, //$NON-NLS-1$ //$NON-NLS-2$
				"second description", "second type", "")); //$NON-NLS-1$ //$NON-NLS-2$
		AttachmentFieldValue fileDescription = new AttachmentFieldValue(222, attachments);

		artifact.addFieldValue(fileDescription);

		// We do not submit TuleapFileUpload fields for TuleapArtifact, only for TuleapArtifactWithAttachment
		assertEquals("{\"values\":[],\"tracker\":{\"id\":100}}", gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldAttachmentUpdatable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapFileUpload field = new TuleapFileUpload(222);
		field.setPermissions(CREATE_ONLY);
		artifact.addField(field);

		List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		attachments.add(new AttachmentValue("100000", "first name", 1, 123456, //$NON-NLS-1$ //$NON-NLS-2$
				"first description", "first type", "")); //$NON-NLS-1$ //$NON-NLS-2$
		attachments.add(new AttachmentValue("100001", "second name", 2, 789456, //$NON-NLS-1$ //$NON-NLS-2$
				"second description", "second type", "")); //$NON-NLS-1$ //$NON-NLS-2$
		AttachmentFieldValue fileDescription = new AttachmentFieldValue(222, attachments);

		artifact.addFieldValue(fileDescription);

		// We do not submit TuleapFileUpload fields for TuleapArtifact, only for TuleapArtifactWithAttachment
		assertEquals("{\"values\":[],\"tracker\":{\"id\":100}}", gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithAttachmentWithFieldAttachmentSubmitable() {
		TuleapArtifactWithAttachment artifact = new TuleapArtifactWithAttachment(trackerRef, projectRef);

		TuleapFileUpload field = new TuleapFileUpload(222);
		field.setPermissions(CREATE_ONLY);
		artifact.addField(field);

		List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		attachments.add(new AttachmentValue("100000", "first name", 1, 123456, //$NON-NLS-1$ //$NON-NLS-2$
				"first description", "first type", "")); //$NON-NLS-1$ //$NON-NLS-2$
		attachments.add(new AttachmentValue("100001", "second name", 2, 789456, //$NON-NLS-1$ //$NON-NLS-2$
				"second description", "second type", "")); //$NON-NLS-1$ //$NON-NLS-2$
		AttachmentFieldValue fileDescription = new AttachmentFieldValue(222, attachments);

		artifact.addFieldValue(fileDescription);

		// We do not submit TuleapFileUpload fields for TuleapArtifact, only for TuleapArtifactWithAttachment
		assertEquals("{\"values\":[{\"field_id\":222,\"value\":[100000,100001]}],\"tracker\":{\"id\":100}}",
				gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithAttachmentWithFieldAttachmentUpdatable() {
		TuleapArtifactWithAttachment artifact = new TuleapArtifactWithAttachment(trackerRef, projectRef);

		TuleapFileUpload field = new TuleapFileUpload(222);
		field.setPermissions(CREATE_ONLY);
		artifact.addField(field);

		List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		attachments.add(new AttachmentValue("100000", "first name", 1, 123456, //$NON-NLS-1$ //$NON-NLS-2$
				"first description", "first type", "")); //$NON-NLS-1$ //$NON-NLS-2$
		attachments.add(new AttachmentValue("100001", "second name", 2, 789456, //$NON-NLS-1$ //$NON-NLS-2$
				"second description", "second type", "")); //$NON-NLS-1$ //$NON-NLS-2$
		AttachmentFieldValue fileDescription = new AttachmentFieldValue(222, attachments);

		artifact.addFieldValue(fileDescription);

		// We do not submit TuleapFileUpload fields for TuleapArtifact, only for TuleapArtifactWithAttachment
		assertEquals("{\"values\":[{\"field_id\":222,\"value\":[100000,100001]}],\"tracker\":{\"id\":100}}",
				gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldLinksSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapArtifactLink field = new TuleapArtifactLink(222);
		field.setPermissions(CREATE_ONLY);
		artifact.addField(field);
		artifact.addFieldValue(new ArtifactLinkFieldValue(222, new int[] {137, 133 }));
		assertEquals("{\"values\":[],\"tracker\":{\"id\":100}}", gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldLinksUpdatable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapArtifactLink field = new TuleapArtifactLink(222);
		field.setPermissions(UPDATE_ONLY);
		artifact.addField(field);
		artifact.addFieldValue(new ArtifactLinkFieldValue(222, new int[] {137, 133 }));
		assertEquals(
				"{\"values\":[{\"field_id\":222,\"links\":[{\"id\":137},{\"id\":133}]}],\"tracker\":{\"id\":100}}",
				gson.toJson(artifact));
	}

	@Test
	public void testNewArtifactWithFieldLinksSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapArtifactLink field = new TuleapArtifactLink(222);
		field.setPermissions(CREATE_ONLY);
		artifact.addField(field);
		artifact.setNew(true); // Creation
		artifact.addFieldValue(new ArtifactLinkFieldValue(222, new int[] {137, 133 }));
		assertEquals(
				"{\"values\":[{\"field_id\":222,\"links\":[{\"id\":137},{\"id\":133}]}],\"tracker\":{\"id\":100}}",
				gson.toJson(artifact));
	}

	@Test
	public void testNewArtifactWithFieldLinksUpdatable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapArtifactLink field = new TuleapArtifactLink(222);
		field.setPermissions(UPDATE_ONLY);
		artifact.addField(field);
		artifact.setNew(true); // Creation
		artifact.addFieldValue(new ArtifactLinkFieldValue(222, new int[] {137, 133 }));
		assertEquals("{\"values\":[],\"tracker\":{\"id\":100}}", gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldLinksNonSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapArtifactLink field = new TuleapArtifactLink(222);
		field.setPermissions(READ_UPDATE);
		artifact.addField(field);
		artifact.addFieldValue(new ArtifactLinkFieldValue(222, new int[] {137, 133 }));
		assertEquals(
				"{\"values\":[{\"field_id\":222,\"links\":[{\"id\":137},{\"id\":133}]}],\"tracker\":{\"id\":100}}",
				gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithManyFieldsNonSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox firstField = new TuleapSelectBox(220);
		firstField.setPermissions(READ_UPDATE);
		firstField.addItem(new TuleapSelectBoxItem(0));
		firstField.addItem(new TuleapSelectBoxItem(1));
		firstField.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(firstField);
		artifact.addFieldValue(new BoundFieldValue(220, Arrays.asList(0, 1)));

		TuleapSelectBox secondField = new TuleapSelectBox(221);
		secondField.setPermissions(READ_UPDATE);
		secondField.addItem(new TuleapSelectBoxItem(0));
		secondField.addItem(new TuleapSelectBoxItem(1));
		secondField.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(secondField);
		artifact.addFieldValue(new BoundFieldValue(221, Arrays.asList(0, 1)));

		TuleapString fourthField = new TuleapString(223);
		fourthField.setPermissions(READ_UPDATE);
		artifact.addField(fourthField);
		artifact.addFieldValue(new LiteralFieldValue(223, "test"));

		TuleapFileUpload sixthField = new TuleapFileUpload(224);
		sixthField.setPermissions(READ_UPDATE);
		artifact.addField(sixthField);

		List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		attachments.add(new AttachmentValue("100000", "first name", 1, 123456, //$NON-NLS-1$ //$NON-NLS-2$
				"first description", "first type", "")); //$NON-NLS-1$ //$NON-NLS-2$
		attachments.add(new AttachmentValue("100001", "second name", 2, 789456, //$NON-NLS-1$ //$NON-NLS-2$
				"second description", "second type", "")); //$NON-NLS-1$ //$NON-NLS-2$
		AttachmentFieldValue fileDescription = new AttachmentFieldValue(224, attachments);
		artifact.addFieldValue(fileDescription);

		TuleapArtifactLink seventhField = new TuleapArtifactLink(222);
		seventhField.setPermissions(READ_UPDATE);
		artifact.addField(seventhField);
		artifact.addFieldValue(new ArtifactLinkFieldValue(222, new int[] {137, 133 }));

		assertEquals(
				"{\"values\":[{\"field_id\":220,\"bind_value_ids\":[0,1]},{\"field_id\":221,\"bind_value_ids\":[0,1]},{\"field_id\":223,\"value\":\"test\"},{\"field_id\":222,\"links\":[{\"id\":137},{\"id\":133}]}],\"tracker\":{\"id\":100}}",
				gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithManyFieldsSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox firstField = new TuleapSelectBox(220);
		firstField.setPermissions(CREATE_ONLY);
		firstField.addItem(new TuleapSelectBoxItem(0));
		firstField.addItem(new TuleapSelectBoxItem(1));
		firstField.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(firstField);
		artifact.addFieldValue(new BoundFieldValue(220, Arrays.asList(0, 1)));

		TuleapSelectBox secondField = new TuleapSelectBox(221);
		secondField.setPermissions(CREATE_ONLY);
		secondField.addItem(new TuleapSelectBoxItem(0));
		secondField.addItem(new TuleapSelectBoxItem(1));
		secondField.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(secondField);
		artifact.addFieldValue(new BoundFieldValue(221, Arrays.asList(0, 1)));

		TuleapInteger thirdField = new TuleapInteger(222);
		thirdField.setPermissions(CREATE_ONLY);
		artifact.addField(thirdField);
		artifact.addFieldValue(new LiteralFieldValue(222, "666"));

		TuleapString fourthField = new TuleapString(223);
		fourthField.setPermissions(CREATE_ONLY);
		artifact.addField(fourthField);
		artifact.addFieldValue(new LiteralFieldValue(223, "test"));

		TuleapFileUpload sixthField = new TuleapFileUpload(224);
		sixthField.setPermissions(CREATE_ONLY);
		artifact.addField(sixthField);

		List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		attachments.add(new AttachmentValue("100000", "first name", 1, 123456, //$NON-NLS-1$ //$NON-NLS-2$
				"first description", "first type", "")); //$NON-NLS-1$ //$NON-NLS-2$
		attachments.add(new AttachmentValue("100001", "second name", 2, 789456, //$NON-NLS-1$ //$NON-NLS-2$
				"second description", "second type", "")); //$NON-NLS-1$ //$NON-NLS-2$
		AttachmentFieldValue fileDescription = new AttachmentFieldValue(224, attachments);
		artifact.addFieldValue(fileDescription);

		TuleapArtifactLink seventhField = new TuleapArtifactLink(222);
		seventhField.setPermissions(CREATE_ONLY);
		artifact.addField(seventhField);
		artifact.addFieldValue(new ArtifactLinkFieldValue(222, new int[] {137, 133 }));

		assertEquals("{\"values\":[],\"tracker\":{\"id\":100}}", gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithManyFieldsUpdatable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox firstField = new TuleapSelectBox(220);
		firstField.setPermissions(UPDATE_ONLY);
		firstField.addItem(new TuleapSelectBoxItem(0));
		firstField.addItem(new TuleapSelectBoxItem(1));
		firstField.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(firstField);
		artifact.addFieldValue(new BoundFieldValue(220, Arrays.asList(0, 1)));

		TuleapSelectBox secondField = new TuleapSelectBox(221);
		secondField.setPermissions(UPDATE_ONLY);
		secondField.addItem(new TuleapSelectBoxItem(0));
		secondField.addItem(new TuleapSelectBoxItem(1));
		secondField.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(secondField);
		artifact.addFieldValue(new BoundFieldValue(221, Arrays.asList(0, 1)));

		TuleapInteger thirdField = new TuleapInteger(222);
		thirdField.setPermissions(UPDATE_ONLY);
		artifact.addField(thirdField);
		artifact.addFieldValue(new LiteralFieldValue(222, "666"));

		TuleapString fourthField = new TuleapString(223);
		fourthField.setPermissions(UPDATE_ONLY);
		artifact.addField(fourthField);
		artifact.addFieldValue(new LiteralFieldValue(223, "test"));

		TuleapFileUpload sixthField = new TuleapFileUpload(224);
		sixthField.setPermissions(UPDATE_ONLY);
		artifact.addField(sixthField);

		// This one must NOT be submitted.
		List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		attachments.add(new AttachmentValue("100000", "first name", 1, 123456, //$NON-NLS-1$ //$NON-NLS-2$
				"first description", "first type", "")); //$NON-NLS-1$ //$NON-NLS-2$
		attachments.add(new AttachmentValue("100001", "second name", 2, 789456, //$NON-NLS-1$ //$NON-NLS-2$
				"second description", "second type", "")); //$NON-NLS-1$ //$NON-NLS-2$
		AttachmentFieldValue fileDescription = new AttachmentFieldValue(224, attachments);
		artifact.addFieldValue(fileDescription);

		TuleapArtifactLink seventhField = new TuleapArtifactLink(222);
		seventhField.setPermissions(UPDATE_ONLY);
		artifact.addField(seventhField);
		artifact.addFieldValue(new ArtifactLinkFieldValue(222, new int[] {137, 133 }));

		assertEquals(
				"{\"values\":[{\"field_id\":220,\"bind_value_ids\":[0,1]},{\"field_id\":221,\"bind_value_ids\":[0,1]},{\"field_id\":222,\"links\":[{\"id\":137},{\"id\":133}]},{\"field_id\":223,\"value\":\"test\"}],\"tracker\":{\"id\":100}}",
				gson.toJson(artifact));
	}
}
