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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.core.internal.model.data.AbstractFieldValue;
import org.tuleap.mylyn.task.core.internal.model.data.ArtifactLinkFieldValue;
import org.tuleap.mylyn.task.core.internal.model.data.AttachmentFieldValue;
import org.tuleap.mylyn.task.core.internal.model.data.AttachmentValue;
import org.tuleap.mylyn.task.core.internal.model.data.BoundFieldValue;
import org.tuleap.mylyn.task.core.internal.model.data.LiteralFieldValue;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapArtifact;
import org.tuleap.mylyn.task.core.internal.parser.DateIso8601Adapter;
import org.tuleap.mylyn.task.core.internal.parser.TuleapGsonProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests of the JSON deserialization of TuleapArtifact.
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapArtifactDeserializerTest {

	private Gson gson;

	@Before
	public void setUp() {
		gson = TuleapGsonProvider.defaultGson();
	}

	@Test
	public void testDeserializeArtifactWithLiteralFieldValue() throws ParseException {
		String json = ParserUtil.loadFile("/artifacts/artifactWithLiteralFieldValue.json");
		TuleapArtifact artifact = gson.fromJson(json, TuleapArtifact.class);
		assertNotNull(artifact);

		assertEquals(Integer.valueOf(0), artifact.getId());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2014-02-03T16:04:24+01:00"), artifact
				.getLastModifiedDate());
		assertEquals("artifacts/0", artifact.getUri());

		assertNotNull(artifact.getTracker());
		assertEquals(15, artifact.getTracker().getId());
		assertEquals("trackers/15", artifact.getTracker().getUri());

		assertEquals(102, artifact.getSubmittedBy());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2013-11-12T09:59:55+01:00"), artifact
				.getSubmittedOn());

		assertNotNull(artifact.getProject());
		assertEquals(101, artifact.getProject().getId());
		assertEquals("projects/101", artifact.getProject().getUri());

		assertEquals(4, artifact.getFieldValues().size());

		Iterator<AbstractFieldValue> iterator = artifact.getFieldValues().iterator();

		AbstractFieldValue firstField = iterator.next();
		assertEquals(559, firstField.getFieldId());
		LiteralFieldValue firstLiteralValue = (LiteralFieldValue)firstField;
		assertEquals("true", firstLiteralValue.getFieldValue());

		AbstractFieldValue secondField = iterator.next();
		assertEquals(556, secondField.getFieldId());
		LiteralFieldValue secondLiteralValue = (LiteralFieldValue)secondField;
		assertEquals("2013-10-01T00:00:00+02:00", secondLiteralValue.getFieldValue());

		AbstractFieldValue thirdField = iterator.next();
		assertEquals(557, thirdField.getFieldId());
		LiteralFieldValue thirdLiteralValue = (LiteralFieldValue)thirdField;
		assertEquals("22", thirdLiteralValue.getFieldValue());

		AbstractFieldValue fourthField = iterator.next();
		assertEquals(554, fourthField.getFieldId());
		LiteralFieldValue fourthLiteralValue = (LiteralFieldValue)fourthField;
		assertEquals("v0.7", fourthLiteralValue.getFieldValue());
	}

	@Test
	public void testDeserializeArtifactWithBoundFieldValue() throws ParseException {
		String json = ParserUtil.loadFile("/artifacts/artifactWithBoundFieldValue.json");
		TuleapArtifact artifact = gson.fromJson(json, TuleapArtifact.class);
		assertNotNull(artifact);

		assertEquals(Integer.valueOf(1), artifact.getId());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2014-02-03T16:04:24+01:00"), artifact
				.getLastModifiedDate());
		assertEquals("artifacts/1", artifact.getUri());

		assertNotNull(artifact.getTracker());
		assertEquals(15, artifact.getTracker().getId());
		assertEquals("trackers/15", artifact.getTracker().getUri());

		assertEquals(102, artifact.getSubmittedBy());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2013-11-12T09:59:55+01:00"), artifact
				.getSubmittedOn());

		assertNotNull(artifact.getProject());
		assertEquals(101, artifact.getProject().getId());
		assertEquals("projects/101", artifact.getProject().getUri());

		assertEquals(1, artifact.getFieldValues().size());

		Iterator<AbstractFieldValue> iterator = artifact.getFieldValues().iterator();

		AbstractFieldValue firstField = iterator.next();
		assertEquals(560, firstField.getFieldId());
		BoundFieldValue firstBoundValue = (BoundFieldValue)firstField;
		assertEquals(1, firstBoundValue.getValueIds().size());
		assertEquals(Arrays.asList(387), firstBoundValue.getValueIds());
	}

	@Test
	public void testDeserializeArtifactWithBoundFieldValues() throws ParseException {
		String json = ParserUtil.loadFile("/artifacts/artifactWithBoundFieldValues.json");
		TuleapArtifact artifact = gson.fromJson(json, TuleapArtifact.class);
		assertNotNull(artifact);

		assertEquals(Integer.valueOf(2), artifact.getId());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2014-02-03T16:04:24+01:00"), artifact
				.getLastModifiedDate());
		assertEquals("artifacts/2", artifact.getUri());

		assertNotNull(artifact.getTracker());
		assertEquals(15, artifact.getTracker().getId());
		assertEquals("trackers/15", artifact.getTracker().getUri());

		assertEquals(102, artifact.getSubmittedBy());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2013-11-12T09:59:55+01:00"), artifact
				.getSubmittedOn());

		assertNotNull(artifact.getProject());
		assertEquals(101, artifact.getProject().getId());
		assertEquals("projects/101", artifact.getProject().getUri());

		assertEquals(1, artifact.getFieldValues().size());

		Iterator<AbstractFieldValue> iterator = artifact.getFieldValues().iterator();

		AbstractFieldValue firstField = iterator.next();
		assertEquals(560, firstField.getFieldId());
		BoundFieldValue firstBoundValue = (BoundFieldValue)firstField;
		assertEquals(2, firstBoundValue.getValueIds().size());
		assertEquals(Arrays.asList(515, 516), firstBoundValue.getValueIds());
	}

	@Test
	public void testDeserializeArtifactWithLinkFieldValues() throws ParseException {
		String json = ParserUtil.loadFile("/artifacts/artifactWithLinkFieldValue.json");
		TuleapArtifact artifact = gson.fromJson(json, TuleapArtifact.class);
		assertNotNull(artifact);

		assertEquals(Integer.valueOf(3), artifact.getId());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2014-02-03T16:04:24+01:00"), artifact
				.getLastModifiedDate());
		assertEquals("artifacts/3", artifact.getUri());

		assertNotNull(artifact.getTracker());
		assertEquals(15, artifact.getTracker().getId());
		assertEquals("trackers/15", artifact.getTracker().getUri());

		assertEquals(102, artifact.getSubmittedBy());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2013-11-12T09:59:55+01:00"), artifact
				.getSubmittedOn());

		assertNotNull(artifact.getProject());
		assertEquals(101, artifact.getProject().getId());
		assertEquals("projects/101", artifact.getProject().getUri());

		assertEquals(1, artifact.getFieldValues().size());

		Iterator<AbstractFieldValue> iterator = artifact.getFieldValues().iterator();

		AbstractFieldValue firstField = iterator.next();
		assertEquals(574, firstField.getFieldId());
		ArtifactLinkFieldValue firstBoundValue = (ArtifactLinkFieldValue)firstField;

		assertEquals(2, firstBoundValue.getLinks().length);
		assertEquals(94, firstBoundValue.getLinks()[0]);
		assertEquals(95, firstBoundValue.getLinks()[1]);
	}

	@Test
	public void testDeserializeArtifactWithAttachmentFieldValues() throws ParseException {
		String json = ParserUtil.loadFile("/artifacts/artifactWithAttachmentFieldValue.json");
		TuleapArtifact artifact = gson.fromJson(json, TuleapArtifact.class);
		assertNotNull(artifact);

		assertEquals(Integer.valueOf(4), artifact.getId());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2014-02-03T16:04:24+01:00"), artifact
				.getLastModifiedDate());
		assertEquals("artifacts/4", artifact.getUri());

		assertNotNull(artifact.getTracker());
		assertEquals(15, artifact.getTracker().getId());
		assertEquals("trackers/15", artifact.getTracker().getUri());

		assertEquals(102, artifact.getSubmittedBy());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2013-11-12T09:59:55+01:00"), artifact
				.getSubmittedOn());

		assertNotNull(artifact.getProject());
		assertEquals(101, artifact.getProject().getId());
		assertEquals("projects/101", artifact.getProject().getUri());

		assertEquals(1, artifact.getFieldValues().size());
		AttachmentFieldValue fieldValue = (AttachmentFieldValue)artifact.getFieldValue(452);
		assertEquals(452, fieldValue.getFieldId());
		List<AttachmentValue> attachments = fieldValue.getAttachments();
		assertEquals(1, attachments.size());
		AttachmentValue value = attachments.get(0);
		assertEquals("12", value.getId());
		assertEquals(6543, value.getSize());
		assertEquals(123, value.getSubmittedBy());
		assertEquals("image/png", value.getType());
		assertEquals("some_file.png", value.getName());
		assertEquals("artifact_files/12", value.getUri());
		assertEquals("file description", value.getDescription());
	}

	@Test
	public void testDeserializeArtifactWithNullLiteralFieldValue() throws ParseException {
		String json = ParserUtil.loadFile("/artifacts/artifactWithNullLiteralFieldValue.json");
		TuleapArtifact artifact = gson.fromJson(json, TuleapArtifact.class);
		assertNotNull(artifact);

		assertEquals(Integer.valueOf(5), artifact.getId());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2014-02-03T16:04:24+01:00"), artifact
				.getLastModifiedDate());
		assertEquals("artifacts/5", artifact.getUri());

		assertNotNull(artifact.getTracker());
		assertEquals(15, artifact.getTracker().getId());
		assertEquals("trackers/15", artifact.getTracker().getUri());

		assertEquals(102, artifact.getSubmittedBy());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2013-11-12T09:59:55+01:00"), artifact
				.getSubmittedOn());

		assertNotNull(artifact.getProject());
		assertEquals(101, artifact.getProject().getId());
		assertEquals("projects/101", artifact.getProject().getUri());

		assertEquals(0, artifact.getFieldValues().size());
	}

	@Test
	public void testDeserializeArtifactWithEmptyLiteralFieldValue() throws ParseException {
		String json = ParserUtil.loadFile("/artifacts/artifactWithEmptyLiteralFieldValue.json");
		TuleapArtifact artifact = gson.fromJson(json, TuleapArtifact.class);
		assertNotNull(artifact);

		assertEquals(Integer.valueOf(6), artifact.getId());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2014-02-03T16:04:24+01:00"), artifact
				.getLastModifiedDate());
		assertEquals("artifacts/6", artifact.getUri());

		assertNotNull(artifact.getTracker());
		assertEquals(15, artifact.getTracker().getId());
		assertEquals("trackers/15", artifact.getTracker().getUri());

		assertEquals(102, artifact.getSubmittedBy());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2013-11-12T09:59:55+01:00"), artifact
				.getSubmittedOn());

		assertNotNull(artifact.getProject());
		assertEquals(101, artifact.getProject().getId());
		assertEquals("projects/101", artifact.getProject().getUri());

		assertEquals(1, artifact.getFieldValues().size());

		Iterator<AbstractFieldValue> iterator = artifact.getFieldValues().iterator();

		AbstractFieldValue firstField = iterator.next();
		assertEquals(557, firstField.getFieldId());
		LiteralFieldValue firstLiteralValue = (LiteralFieldValue)firstField;
		assertEquals("", firstLiteralValue.getFieldValue());
	}

	@Test
	public void testDeserializeArtifactWithNullBoundFieldValue() throws ParseException {
		String json = ParserUtil.loadFile("/artifacts/artifactWithNullBoundFieldValue.json");
		TuleapArtifact artifact = gson.fromJson(json, TuleapArtifact.class);
		assertNotNull(artifact);

		assertEquals(Integer.valueOf(7), artifact.getId());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2014-02-03T16:04:24+01:00"), artifact
				.getLastModifiedDate());
		assertEquals("artifacts/7", artifact.getUri());

		assertNotNull(artifact.getTracker());
		assertEquals(15, artifact.getTracker().getId());
		assertEquals("trackers/15", artifact.getTracker().getUri());

		assertEquals(102, artifact.getSubmittedBy());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2013-11-12T09:59:55+01:00"), artifact
				.getSubmittedOn());

		assertNotNull(artifact.getProject());
		assertEquals(101, artifact.getProject().getId());
		assertEquals("projects/101", artifact.getProject().getUri());

		assertEquals(0, artifact.getFieldValues().size());
	}

	@Test
	public void testDeserializeArtifactWithNullBoundFieldValues() throws ParseException {
		String json = ParserUtil.loadFile("/artifacts/artifactWithNullBoundFieldValues.json");
		TuleapArtifact artifact = gson.fromJson(json, TuleapArtifact.class);
		assertNotNull(artifact);

		assertEquals(Integer.valueOf(8), artifact.getId());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2014-02-03T16:04:24+01:00"), artifact
				.getLastModifiedDate());
		assertEquals("artifacts/8", artifact.getUri());

		assertNotNull(artifact.getTracker());
		assertEquals(15, artifact.getTracker().getId());
		assertEquals("trackers/15", artifact.getTracker().getUri());

		assertEquals(102, artifact.getSubmittedBy());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2013-11-12T09:59:55+01:00"), artifact
				.getSubmittedOn());

		assertNotNull(artifact.getProject());
		assertEquals(101, artifact.getProject().getId());
		assertEquals("projects/101", artifact.getProject().getUri());

		assertEquals(0, artifact.getFieldValues().size());
	}

	@Test
	public void testDeserializeArtifactWithNullLinkFieldValues() throws ParseException {
		String json = ParserUtil.loadFile("/artifacts/artifactWithNullLinkFieldValue.json");
		TuleapArtifact artifact = gson.fromJson(json, TuleapArtifact.class);
		assertNotNull(artifact);

		assertEquals(Integer.valueOf(9), artifact.getId());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2014-02-03T16:04:24+01:00"), artifact
				.getLastModifiedDate());
		assertEquals("artifacts/9", artifact.getUri());

		assertNotNull(artifact.getTracker());
		assertEquals(15, artifact.getTracker().getId());
		assertEquals("trackers/15", artifact.getTracker().getUri());

		assertEquals(102, artifact.getSubmittedBy());
		assertEquals(DateIso8601Adapter.parseIso8601Date("2013-11-12T09:59:55+01:00"), artifact
				.getSubmittedOn());

		assertNotNull(artifact.getProject());
		assertEquals(101, artifact.getProject().getId());
		assertEquals("projects/101", artifact.getProject().getUri());

		assertEquals(0, artifact.getFieldValues().size());
	}

}
