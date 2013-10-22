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
import org.tuleap.mylyn.task.internal.core.serializer.TuleapBacklogItemSerializer;

import static org.junit.Assert.assertEquals;

/**
 * Tests the serialization of the Tuleap backlogItem.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapBacklogItemSerializerTests {
	/**
	 * The gson converter.
	 */
	private static com.google.gson.Gson gson;

	/**
	 * Initialize the gson converter.
	 */
	@Before
	public void setUp() {
		gson = new GsonBuilder().registerTypeAdapter(TuleapBacklogItem.class,
				new TuleapBacklogItemSerializer()).create();
	}

	/**
	 * Test the serialization of an empty backlogItem.
	 * 
	 * @throws ParseException
	 *             the exception
	 */
	@Test
	public void testSerializeBacklogItemEmpty() throws ParseException {

		TuleapBacklogItem backlogItem = new TuleapBacklogItem(200, 901, 123);

		String emptyBacklogItem = gson.toJsonTree(backlogItem).toString();
		String expectedResult = "{}"; //$NON-NLS-1$
		assertEquals(expectedResult, emptyBacklogItem);
	}

	/**
	 * Test the serialization of a backlogItem with only assigned milestone identifier.
	 * 
	 * @throws ParseException
	 *             the exception
	 */
	@Test
	public void testSerializeBacklogItemWithAssignedMilestoneIdentifier() throws ParseException {

		TuleapBacklogItem backlogItem = new TuleapBacklogItem(200, 901, 123);
		backlogItem.setAssignedMilestoneId(1000);

		String emptyBacklogItem = gson.toJsonTree(backlogItem).toString();
		String expectedResult = "{\"assigned_milestone_id\":1000}"; //$NON-NLS-1$
		assertEquals(expectedResult, emptyBacklogItem);
	}

	/**
	 * Test the serialization of a backlogItem with only initial effort.
	 * 
	 * @throws ParseException
	 *             the exception
	 */
	@Test
	public void testSerializeBacklogItemWithInitialEffort() throws ParseException {

		TuleapBacklogItem backlogItem = new TuleapBacklogItem(200, 901, 123);
		backlogItem.setInitialEffort(Float.valueOf(10));

		String emptyBacklogItem = gson.toJsonTree(backlogItem).toString();
		String expectedResult = "{\"initial_effort\":10.0}"; //$NON-NLS-1$
		assertEquals(expectedResult, emptyBacklogItem);
	}

	/**
	 * Test the serialization of a backlogItem with only assigned milestone identifier and initial effort.
	 * 
	 * @throws ParseException
	 *             the exception
	 */
	@Test
	public void testSerializeBacklogItemWithoutFieldValues() throws ParseException {

		TuleapBacklogItem backlogItem = new TuleapBacklogItem(200, 901, 123);
		backlogItem.setAssignedMilestoneId(1000);
		backlogItem.setInitialEffort(Float.valueOf(10));

		String emptyBacklogItem = gson.toJsonTree(backlogItem).toString();
		String expectedResult = "{\"initial_effort\":10.0,\"assigned_milestone_id\":1000}"; //$NON-NLS-1$
		assertEquals(expectedResult, emptyBacklogItem);
	}

	/**
	 * Test the serialization of a backlogItem with literal value.
	 * 
	 * @throws ParseException
	 *             the exception
	 */
	@Test
	public void testSerializeBacklogItemWithLiteralValue() throws ParseException {
		TuleapBacklogItem backlogItem = new TuleapBacklogItem(200, 901, 123);
		backlogItem.setAssignedMilestoneId(1000);
		backlogItem.setInitialEffort(Float.valueOf(10));

		LiteralFieldValue firstLiteralFieldValue = new LiteralFieldValue(1000, "300, 301, 302"); //$NON-NLS-1$
		backlogItem.addFieldValue(firstLiteralFieldValue);

		String literalValueBacklogItem = gson.toJsonTree(backlogItem).toString();
		String expectedResult = "{\"values\":[{\"field_id\":1000,\"value\":\"300, 301, 302\"}],\"initial_effort\":10.0,\"assigned_milestone_id\":1000}"; //$NON-NLS-1$

		assertEquals(expectedResult, literalValueBacklogItem);

	}

	/**
	 * Test the serialization of a backlogItem with literal value.
	 * 
	 * @throws ParseException
	 *             the exception
	 */
	@Test
	public void testSerializeBacklogItemWithBoundValue() throws ParseException {
		TuleapBacklogItem backlogItem = new TuleapBacklogItem(200, 901, 123);
		backlogItem.setAssignedMilestoneId(1000);
		backlogItem.setInitialEffort(Float.valueOf(10));

		List<Integer> valueIds = new ArrayList<Integer>();
		valueIds.add(new Integer(1));
		valueIds.add(new Integer(2));
		valueIds.add(new Integer(3));
		BoundFieldValue firstBoundFieldValue = new BoundFieldValue(2000, valueIds);
		backlogItem.addFieldValue(firstBoundFieldValue);

		String boundValueBacklogItem = gson.toJsonTree(backlogItem).toString();
		String expectedResult = "{\"values\":[{\"field_id\":2000,\"bind_value_ids\":[1,2,3]}],\"initial_effort\":10.0,\"assigned_milestone_id\":1000}"; //$NON-NLS-1$

		assertEquals(expectedResult, boundValueBacklogItem);
	}

	/**
	 * Test the serialization of a backlogItem with file description.
	 * 
	 * @throws ParseException
	 *             the exception
	 */
	@Test
	public void testSerializeBacklogItemWithFileDescription() throws ParseException {
		TuleapBacklogItem backlogItem = new TuleapBacklogItem(200, 901, 123);
		backlogItem.setAssignedMilestoneId(1000);
		backlogItem.setInitialEffort(Float.valueOf(10));

		List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		TuleapPerson firstUploadedBy = new TuleapPerson("first username", "first realname", 1, "first email"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attachments.add(new AttachmentValue("100000", "first name", firstUploadedBy, 123456, //$NON-NLS-1$ //$NON-NLS-2$ 
				"first description", "first type")); //$NON-NLS-1$ //$NON-NLS-2$

		TuleapPerson secondUploadedBy = new TuleapPerson("second username", "second realname", 2, //$NON-NLS-1$ //$NON-NLS-2$
				"second email"); //$NON-NLS-1$
		attachments.add(new AttachmentValue("100001", "second name", secondUploadedBy, 789456, //$NON-NLS-1$ //$NON-NLS-2$
				"second description", "second type")); //$NON-NLS-1$ //$NON-NLS-2$

		AttachmentFieldValue fileDescriptions = new AttachmentFieldValue(3000, attachments);
		backlogItem.addFieldValue(fileDescriptions);

		String fileDescriptionbacklogItem = gson.toJsonTree(backlogItem).toString();
		String expectedResult = "{\"initial_effort\":10.0,\"assigned_milestone_id\":1000}"; //$NON-NLS-1$

		assertEquals(expectedResult, fileDescriptionbacklogItem);
	}
}
