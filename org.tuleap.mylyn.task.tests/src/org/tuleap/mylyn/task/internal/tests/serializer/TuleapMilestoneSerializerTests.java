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

import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.data.AttachmentFieldValue;
import org.tuleap.mylyn.task.internal.core.data.AttachmentValue;
import org.tuleap.mylyn.task.internal.core.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.TuleapPerson;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.serializer.TuleapMilestoneSerializer;

import static org.junit.Assert.assertEquals;

/**
 * Tests the serialization of the Tuleap milestone.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapMilestoneSerializerTests {
	/**
	 * The gson converter.
	 */
	private static com.google.gson.Gson gson;

	/**
	 * Initialize the gson converter.
	 */
	@Before
	public void setUp() {
		gson = new GsonBuilder().registerTypeAdapter(TuleapMilestone.class, new TuleapMilestoneSerializer())
				.create();
	}

	/**
	 * Test the serialization of an empty milestone with a parent identifier.
	 * 
	 * @throws ParseException
	 *             the exception
	 */
	@Test
	public void testSerializeMilestoneEmptyWithParentId() throws ParseException {
		TuleapMilestone milestone = new TuleapMilestone(200, 123, 901);
		milestone.setDuration(Float.valueOf(50));
		milestone.setCapacity(Float.valueOf(100));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //$NON-NLS-1$
		milestone.setStartDate(dateFormat.parse("2013-09-23T11:44:18.963Z")); //$NON-NLS-1$
		milestone.setNewComment("I am the first milestone"); //$NON-NLS-1$

		String emptyMilestone = gson.toJsonTree(milestone).toString();
		String expectedResult = "{\"milestone_type_id\":200,\"parent_milestone_id\":901,\"start_date\":\"2013-09-23T11:44:18.963Z\",\"duration\":50.0,\"capacity\":100.0,\"comment\":{\"body\":\"I am the first milestone\"}}"; //$NON-NLS-1$
		assertEquals(expectedResult, emptyMilestone);

	}

	/**
	 * Test the serialization of an empty milestone without a parent identifier.
	 * 
	 * @throws ParseException
	 *             the exception
	 */
	@Test
	public void testSerializeMilestoneEmptyWithoutParentId() throws ParseException {
		TuleapMilestone milestone = new TuleapMilestone(200, 123, TuleapMilestone.INVALID_PARENT_MILESTONE_ID);
		milestone.setDuration(Float.valueOf(50));
		milestone.setCapacity(Float.valueOf(100));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //$NON-NLS-1$
		milestone.setStartDate(dateFormat.parse("2013-09-23T11:44:18.963Z")); //$NON-NLS-1$
		milestone.setNewComment("I am the first milestone"); //$NON-NLS-1$

		String emptyMilestone = gson.toJsonTree(milestone).toString();
		String expectedResult = "{\"milestone_type_id\":200,\"start_date\":\"2013-09-23T11:44:18.963Z\",\"duration\":50.0,\"capacity\":100.0,\"comment\":{\"body\":\"I am the first milestone\"}}"; //$NON-NLS-1$
		assertEquals(expectedResult, emptyMilestone);

	}

	/**
	 * Test the serialization of a milestone with literal value.
	 * 
	 * @throws ParseException
	 *             the exception
	 */
	@Test
	public void testSerializeMilestoneWithLiteralValue() throws ParseException {
		TuleapMilestone milestone = new TuleapMilestone(200, 123, 901);

		LiteralFieldValue firstLiteralFieldValue = new LiteralFieldValue(1000, "300, 301, 302"); //$NON-NLS-1$
		milestone.addFieldValue(firstLiteralFieldValue);

		String literalValueMilestone = gson.toJsonTree(milestone).toString();
		String expectedResult = "{\"values\":[{\"field_id\":1000,\"value\":\"300, 301, 302\"}],\"milestone_type_id\":200,\"parent_milestone_id\":901}"; //$NON-NLS-1$

		assertEquals(expectedResult, literalValueMilestone);

	}

	/**
	 * Test the serialization of a milestone with literal value.
	 * 
	 * @throws ParseException
	 *             the exception
	 */
	@Test
	public void testSerializeMilestoneWithBoundValue() throws ParseException {
		TuleapMilestone milestone = new TuleapMilestone(200, 123, 901);

		List<Integer> valueIds = new ArrayList<Integer>();
		valueIds.add(new Integer(1));
		valueIds.add(new Integer(2));
		valueIds.add(new Integer(3));
		BoundFieldValue firstBoundFieldValue = new BoundFieldValue(2000, valueIds);
		milestone.addFieldValue(firstBoundFieldValue);

		String boundValueMilestone = gson.toJsonTree(milestone).toString();
		String expectedResult = "{\"values\":[{\"field_id\":2000,\"bind_value_ids\":[1,2,3]}],\"milestone_type_id\":200,\"parent_milestone_id\":901}"; //$NON-NLS-1$

		assertEquals(expectedResult, boundValueMilestone);
	}

	/**
	 * Test the serialization of a milestone with file description.
	 * 
	 * @throws ParseException
	 *             the exception
	 */
	@Test
	public void testSerializeMilestoneWithFileDescription() throws ParseException {
		TuleapMilestone milestone = new TuleapMilestone(200, 123, 901);
		List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		TuleapPerson firstUploadedBy = new TuleapPerson("first username", "first realname", 1, "first email"); //$NON-NLS-1$
		attachments.add(new AttachmentValue("100000", "first name", firstUploadedBy, 123456, //$NON-NLS-1$ 
				"first description", "first type")); //$NON-NLS-1$

		TuleapPerson secondUploadedBy = new TuleapPerson("second username", "second realname", 2, //$NON-NLS-1$
				"second email"); //$NON-NLS-1$
		attachments.add(new AttachmentValue("100001", "second name", secondUploadedBy, 789456, //$NON-NLS-1$
				"second description", "second type")); //$NON-NLS-1$

		AttachmentFieldValue fileDescriptions = new AttachmentFieldValue(3000, attachments);
		milestone.addFieldValue(fileDescriptions);

		String fileDescriptionMilestone = gson.toJsonTree(milestone).toString();
		String expectedResult = "{\"values\":[{\"field_id\":3000,\"file_descriptions\":[{\"file_id\":100000,\"description\":\"first description\"},{\"file_id\":100001,\"description\":\"second description\"}]}],\"milestone_type_id\":200,\"parent_milestone_id\":901}"; //$NON-NLS-1$

		assertEquals(expectedResult, fileDescriptionMilestone);
	}

	/**
	 * Test the serialization of a milestone that contains submilestones.
	 * 
	 * @throws ParseException
	 *             the exception
	 */
	@Test
	public void testMilestoneWithSubmilestones() throws ParseException {
		TuleapMilestone milestone = new TuleapMilestone(200, 123, 901);
		milestone.setDuration(Float.valueOf(50));
		milestone.setCapacity(Float.valueOf(100));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //$NON-NLS-1$
		milestone.setStartDate(dateFormat.parse("2013-09-23T11:44:18.963Z")); //$NON-NLS-1$
		milestone.setNewComment("I am the first milestone"); //$NON-NLS-1$

		TuleapMilestone firstSubMilestone = new TuleapMilestone(201, 902, 123);
		TuleapMilestone secondSubMilestone = new TuleapMilestone(202, 903, 123);

		milestone.addSubMilestone(firstSubMilestone);
		milestone.addSubMilestone(secondSubMilestone);

		String submilestonesMilestone = gson.toJsonTree(milestone).toString();
		String expectedResult = "{\"milestone_type_id\":200,\"parent_milestone_id\":901,\"start_date\":\"2013-09-23T11:44:18.963Z\",\"duration\":50.0,\"capacity\":100.0,\"comment\":{\"body\":\"I am the first milestone\"}}"; //$NON-NLS-1$

		assertEquals(expectedResult, submilestonesMilestone);

	}

	/**
	 * Test the serialization of a milestone that contains backlogItems.
	 * 
	 * @throws ParseException
	 *             the exception
	 */
	@Test
	public void testMilestoneWithbacklogItems() throws ParseException {
		TuleapMilestone milestone = new TuleapMilestone(200, 123, 901);
		milestone.setDuration(Float.valueOf(50));
		milestone.setCapacity(Float.valueOf(100));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //$NON-NLS-1$
		milestone.setStartDate(dateFormat.parse("2013-09-23T11:44:18.963Z")); //$NON-NLS-1$
		milestone.setNewComment("I am the first milestone"); //$NON-NLS-1$

		TuleapBacklogItem firstBacklogItem = new TuleapBacklogItem(500, 1000);
		TuleapBacklogItem secondBacklogItem = new TuleapBacklogItem(501, 1000);

		milestone.addBacklogItem(firstBacklogItem);
		milestone.addBacklogItem(secondBacklogItem);

		String submilestonesMilestone = gson.toJsonTree(milestone).toString();
		String expectedResult = "{\"milestone_type_id\":200,\"parent_milestone_id\":901,\"start_date\":\"2013-09-23T11:44:18.963Z\",\"duration\":50.0,\"capacity\":100.0,\"comment\":{\"body\":\"I am the first milestone\"}}"; //$NON-NLS-1$

		assertEquals(expectedResult, submilestonesMilestone);

	}

	/**
	 * Test the serialization of a milestone that contains submilestones with backlogItems.
	 * 
	 * @throws ParseException
	 *             the exception
	 */
	@Test
	public void testMilestoneWithSubmilestonesAndBacklogItems() throws ParseException {
		TuleapMilestone milestone = new TuleapMilestone(200, 123, 901);
		milestone.setDuration(Float.valueOf(50));
		milestone.setCapacity(Float.valueOf(100));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //$NON-NLS-1$
		milestone.setStartDate(dateFormat.parse("2013-09-23T11:44:18.963Z")); //$NON-NLS-1$
		milestone.setNewComment("I am the first milestone"); //$NON-NLS-1$

		TuleapMilestone firstSubMilestone = new TuleapMilestone(201, 902, 123);
		TuleapMilestone secondSubMilestone = new TuleapMilestone(202, 903, 123);

		milestone.addSubMilestone(firstSubMilestone);
		milestone.addSubMilestone(secondSubMilestone);

		TuleapBacklogItem firstBacklogItem = new TuleapBacklogItem(500, 1000, 123);
		TuleapBacklogItem secondBacklogItem = new TuleapBacklogItem(501, 1000, 123);

		firstSubMilestone.addBacklogItem(firstBacklogItem);
		secondSubMilestone.addBacklogItem(secondBacklogItem);

		String submilestonesMilestone = gson.toJsonTree(milestone).toString();
		String expectedResult = "{\"milestone_type_id\":200,\"parent_milestone_id\":901,\"start_date\":\"2013-09-23T11:44:18.963Z\",\"duration\":50.0,\"capacity\":100.0,\"comment\":{\"body\":\"I am the first milestone\"}}"; //$NON-NLS-1$

		assertEquals(expectedResult, submilestonesMilestone);
	}

}
