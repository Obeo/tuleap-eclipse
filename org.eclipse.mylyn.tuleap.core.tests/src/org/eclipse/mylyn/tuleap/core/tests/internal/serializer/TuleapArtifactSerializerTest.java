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

import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapUser;
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
		field.setPermissions(new String[] {"submit" });
		artifact.addField(field);
		artifact.addFieldValue(new LiteralFieldValue(222, "test"));
		assertEquals("{\"values\":[{\"field_id\":222,\"value\":\"test\"}],\"tracker\":{\"id\":100}}", gson
				.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldStringNonSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapString field = new TuleapString(222);
		field.setPermissions(new String[] {"read", "update" });
		artifact.addField(field);
		artifact.addFieldValue(new LiteralFieldValue(222, "test"));
		assertEquals("{\"values\":[],\"tracker\":{\"id\":100}}", gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldIntegerSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapInteger field = new TuleapInteger(222);
		field.setPermissions(new String[] {"submit" });
		artifact.addField(field);
		artifact.addFieldValue(new LiteralFieldValue(222, "666"));
		assertEquals("{\"values\":[{\"field_id\":222,\"value\":\"666\"}],\"tracker\":{\"id\":100}}", gson
				.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldIntegerNonSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapInteger field = new TuleapInteger(222);
		field.setPermissions(new String[] {"read", "update" });
		artifact.addField(field);
		artifact.addFieldValue(new LiteralFieldValue(222, "666"));
		assertEquals("{\"values\":[],\"tracker\":{\"id\":100}}", gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldSelectBoxSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox field = new TuleapSelectBox(222);
		field.setPermissions(new String[] {"submit" });
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(field);
		artifact.addFieldValue(new BoundFieldValue(222, Arrays.asList(0)));
		assertEquals("{\"values\":[{\"field_id\":222,\"bind_value_ids\":[0]}],\"tracker\":{\"id\":100}}",
				gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldSelectBoxNonSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox field = new TuleapSelectBox(222);
		field.setPermissions(new String[] {"read", "update" });
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(field);
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
		field.setPermissions(new String[] {"submit" });
		artifact.addField(field);
		artifact.addFieldValue(new BoundFieldValue(222, Arrays.asList(0, 1)));
		assertEquals("{\"values\":[{\"field_id\":222,\"bind_value_ids\":[0,1]}],\"tracker\":{\"id\":100}}",
				gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldMultiSelectBoxNonSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox field = new TuleapSelectBox(222);
		field.addItem(new TuleapSelectBoxItem(0));
		field.addItem(new TuleapSelectBoxItem(1));
		field.addItem(new TuleapSelectBoxItem(2));
		field.setPermissions(new String[] {"read", "update" });
		artifact.addField(field);
		artifact.addFieldValue(new BoundFieldValue(222, Arrays.asList(0, 1)));
		assertEquals("{\"values\":[],\"tracker\":{\"id\":100}}", gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldAttachment() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapFileUpload field = new TuleapFileUpload(222);
		field.setPermissions(new String[] {"submit" });
		artifact.addField(field);

		List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		TuleapUser firstUploadedBy = new TuleapUser(
				"first username", "first realname", 1, "first email", null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attachments.add(new AttachmentValue("100000", "first name", firstUploadedBy, 123456, //$NON-NLS-1$ //$NON-NLS-2$ 
				"first description", "first type")); //$NON-NLS-1$ //$NON-NLS-2$
		TuleapUser secondUploadedBy = new TuleapUser("second username", "second realname", 2, //$NON-NLS-1$ //$NON-NLS-2$
				"second email", null); //$NON-NLS-1$
		attachments.add(new AttachmentValue("100001", "second name", secondUploadedBy, 789456, //$NON-NLS-1$ //$NON-NLS-2$
				"second description", "second type")); //$NON-NLS-1$ //$NON-NLS-2$
		AttachmentFieldValue fileDescription = new AttachmentFieldValue(222, attachments);

		artifact.addFieldValue(fileDescription);

		// We do not submit TuleapFileUpload fields
		assertEquals("{\"values\":[],\"tracker\":{\"id\":100}}", gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldLinksSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapArtifactLink field = new TuleapArtifactLink(222);
		field.setPermissions(new String[] {"submit" });
		artifact.addField(field);
		artifact.addFieldValue(new ArtifactLinkFieldValue(222, new int[] {137, 133 }));
		assertEquals(
				"{\"values\":[{\"field_id\":222,\"links\":[{\"id\":137},{\"id\":133}]}],\"tracker\":{\"id\":100}}",
				gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithFieldLinksNonSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapArtifactLink field = new TuleapArtifactLink(222);
		field.setPermissions(new String[] {"read", "update" });
		artifact.addField(field);
		artifact.addFieldValue(new ArtifactLinkFieldValue(222, new int[] {137, 133 }));
		assertEquals("{\"values\":[],\"tracker\":{\"id\":100}}", gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithManyFieldsNonSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox firstField = new TuleapSelectBox(220);
		firstField.setPermissions(new String[] {"read", "update" });
		firstField.addItem(new TuleapSelectBoxItem(0));
		firstField.addItem(new TuleapSelectBoxItem(1));
		firstField.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(firstField);
		artifact.addFieldValue(new BoundFieldValue(220, Arrays.asList(0, 1)));

		TuleapSelectBox secondField = new TuleapSelectBox(221);
		secondField.setPermissions(new String[] {"read", "update" });
		secondField.addItem(new TuleapSelectBoxItem(0));
		secondField.addItem(new TuleapSelectBoxItem(1));
		secondField.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(secondField);
		artifact.addFieldValue(new BoundFieldValue(221, Arrays.asList(0, 1)));

		TuleapString fourthField = new TuleapString(223);
		fourthField.setPermissions(new String[] {"read", "update" });
		artifact.addField(fourthField);
		artifact.addFieldValue(new LiteralFieldValue(223, "test"));

		TuleapFileUpload sixthField = new TuleapFileUpload(224);
		sixthField.setPermissions(new String[] {"read", "update" });
		artifact.addField(sixthField);

		List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		TuleapUser firstUploadedBy = new TuleapUser(
				"first username", "first realname", 1, "first email", null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attachments.add(new AttachmentValue("100000", "first name", firstUploadedBy, 123456, //$NON-NLS-1$ //$NON-NLS-2$ 
				"first description", "first type")); //$NON-NLS-1$ //$NON-NLS-2$
		TuleapUser secondUploadedBy = new TuleapUser("second username", "second realname", 2, //$NON-NLS-1$ //$NON-NLS-2$
				"second email", null); //$NON-NLS-1$
		attachments.add(new AttachmentValue("100001", "second name", secondUploadedBy, 789456, //$NON-NLS-1$ //$NON-NLS-2$
				"second description", "second type")); //$NON-NLS-1$ //$NON-NLS-2$
		AttachmentFieldValue fileDescription = new AttachmentFieldValue(224, attachments);
		artifact.addFieldValue(fileDescription);

		TuleapArtifactLink seventhField = new TuleapArtifactLink(222);
		seventhField.setPermissions(new String[] {"read", "update" });
		artifact.addField(seventhField);
		artifact.addFieldValue(new ArtifactLinkFieldValue(222, new int[] {137, 133 }));

		assertEquals("{\"values\":[],\"tracker\":{\"id\":100}}", gson.toJson(artifact));
	}

	@Test
	public void testArtifactWithManyFieldsSubmitable() {
		TuleapArtifact artifact = new TuleapArtifact(trackerRef, projectRef);

		TuleapSelectBox firstField = new TuleapSelectBox(220);
		firstField.setPermissions(new String[] {"submit" });
		firstField.addItem(new TuleapSelectBoxItem(0));
		firstField.addItem(new TuleapSelectBoxItem(1));
		firstField.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(firstField);
		artifact.addFieldValue(new BoundFieldValue(220, Arrays.asList(0, 1)));

		TuleapSelectBox secondField = new TuleapSelectBox(221);
		secondField.setPermissions(new String[] {"submit" });
		secondField.addItem(new TuleapSelectBoxItem(0));
		secondField.addItem(new TuleapSelectBoxItem(1));
		secondField.addItem(new TuleapSelectBoxItem(2));
		artifact.addField(secondField);
		artifact.addFieldValue(new BoundFieldValue(221, Arrays.asList(0, 1)));

		TuleapInteger thirdField = new TuleapInteger(222);
		thirdField.setPermissions(new String[] {"submit" });
		artifact.addField(thirdField);
		artifact.addFieldValue(new LiteralFieldValue(222, "666"));

		TuleapString fourthField = new TuleapString(223);
		fourthField.setPermissions(new String[] {"submit" });
		artifact.addField(fourthField);
		artifact.addFieldValue(new LiteralFieldValue(223, "test"));

		TuleapFileUpload sixthField = new TuleapFileUpload(224);
		sixthField.setPermissions(new String[] {"submit" });
		artifact.addField(sixthField);

		List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		TuleapUser firstUploadedBy = new TuleapUser(
				"first username", "first realname", 1, "first email", null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attachments.add(new AttachmentValue("100000", "first name", firstUploadedBy, 123456, //$NON-NLS-1$ //$NON-NLS-2$ 
				"first description", "first type")); //$NON-NLS-1$ //$NON-NLS-2$
		TuleapUser secondUploadedBy = new TuleapUser("second username", "second realname", 2, //$NON-NLS-1$ //$NON-NLS-2$
				"second email", null); //$NON-NLS-1$
		attachments.add(new AttachmentValue("100001", "second name", secondUploadedBy, 789456, //$NON-NLS-1$ //$NON-NLS-2$
				"second description", "second type")); //$NON-NLS-1$ //$NON-NLS-2$
		AttachmentFieldValue fileDescription = new AttachmentFieldValue(224, attachments);
		artifact.addFieldValue(fileDescription);

		TuleapArtifactLink seventhField = new TuleapArtifactLink(222);
		seventhField.setPermissions(new String[] {"submit" });
		artifact.addField(seventhField);
		artifact.addFieldValue(new ArtifactLinkFieldValue(222, new int[] {137, 133 }));

		assertEquals(
				"{\"values\":[{\"field_id\":220,\"bind_value_ids\":[0,1]},{\"field_id\":221,\"bind_value_ids\":[0,1]},{\"field_id\":222,\"links\":[{\"id\":137},{\"id\":133}]},{\"field_id\":223,\"value\":\"test\"}],\"tracker\":{\"id\":100}}",
				gson.toJson(artifact));
	}
}
