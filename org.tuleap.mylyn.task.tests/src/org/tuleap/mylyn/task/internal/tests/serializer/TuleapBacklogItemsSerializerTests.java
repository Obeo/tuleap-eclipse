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
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.serializer.TuleapBacklogItemsSerializer;

import static org.junit.Assert.assertEquals;

/**
 * Tests the serialization of a list of BacklogItems.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapBacklogItemsSerializerTests {
	/**
	 * The gson converter.
	 */
	private static com.google.gson.Gson gson;

	/**
	 * Initialize the gson converter.
	 */
	@Before
	public void setUp() {
		gson = new GsonBuilder().registerTypeAdapter(ArrayList.class, new TuleapBacklogItemsSerializer())
				.create();
	}

	/**
	 * Test the serialization of a list of backlogItems that are assigned to a milestone.
	 */
	@Test
	public void testSerializeBacklogItemsWithAffectedMilestone() {
		TuleapBacklogItem firstBacklogItem = new TuleapBacklogItem(200, 901, 123);
		firstBacklogItem.setAssignedMilestoneId(1000);

		TuleapBacklogItem secondBacklogItem = new TuleapBacklogItem(201, 901, 123);
		secondBacklogItem.setAssignedMilestoneId(1001);

		List<TuleapBacklogItem> backlogItemsList = new ArrayList<TuleapBacklogItem>();
		backlogItemsList.add(firstBacklogItem);
		backlogItemsList.add(secondBacklogItem);

		String theBacklogItems = gson.toJsonTree(backlogItemsList).toString();
		String expectedResult = "[{\"id\":200,\"assigned_milestone_id\":1000},{\"id\":201,\"assigned_milestone_id\":1001}]"; //$NON-NLS-1$

		assertEquals(expectedResult, theBacklogItems);
	}

	/**
	 * Test the serialization of a list of backlogItems that are not assigned to a milestone.
	 */
	@Test
	public void testSerializeBacklogItemWithoutAffectedMilestone() {
		TuleapBacklogItem firstBacklogItem = new TuleapBacklogItem(200, 901, 123);
		TuleapBacklogItem secondBacklogItem = new TuleapBacklogItem(201, 901, 123);

		List<TuleapBacklogItem> backlogItemsList = new ArrayList<TuleapBacklogItem>();
		backlogItemsList.add(firstBacklogItem);
		backlogItemsList.add(secondBacklogItem);

		String theBacklogItems = gson.toJsonTree(backlogItemsList).toString();
		String expectedResult = "[{\"id\":200},{\"id\":201}]"; //$NON-NLS-1$

		assertEquals(expectedResult, theBacklogItems);
	}

	/**
	 * Test the serialization of a list of backlogItems without an identifier affected explicitly.
	 */
	@Test
	public void testSerializeBacklogItemWithoutAffectedId() {
		TuleapBacklogItem firstBacklogItem = new TuleapBacklogItem(901, 123);
		firstBacklogItem.setAssignedMilestoneId(1000);

		TuleapBacklogItem secondBacklogItem = new TuleapBacklogItem(902, 123);
		secondBacklogItem.setAssignedMilestoneId(1001);

		List<TuleapBacklogItem> backlogItemsList = new ArrayList<TuleapBacklogItem>();
		backlogItemsList.add(firstBacklogItem);
		backlogItemsList.add(secondBacklogItem);

		String theBacklogItems = gson.toJsonTree(backlogItemsList).toString();
		String expectedResult = "[{\"id\":0,\"assigned_milestone_id\":1000},{\"id\":0,\"assigned_milestone_id\":1001}]"; //$NON-NLS-1$

		assertEquals(expectedResult, theBacklogItems);
	}

	/**
	 * Test the serialisation of a list of BacklogItems with extra information.
	 * 
	 * @throws ParseException
	 *             the exception
	 */
	@Test
	public void testSerializeBacklogItemsWithExtraInformation() throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //$NON-NLS-1$
		TuleapBacklogItem firstBacklogItem = new TuleapBacklogItem(
				200,
				901,
				123,
				"the first backlog item", "/backlogItemss/200", "/backlogItemss?id=200&group_id=3", dateFormat.parse("2013-09-23T11:44:18.963Z"), //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
				dateFormat.parse("2013-09-24T11:44:18.963Z")); //$NON-NLS-1$
		firstBacklogItem.setAssignedMilestoneId(500);
		firstBacklogItem.setInitialEffort(Float.valueOf(10));

		TuleapBacklogItem secondBacklogItem = new TuleapBacklogItem(
				201,
				901,
				123,
				"the second backlog item", "/backlogItemss/201", "/backlogItemss?id=201&group_id=3", dateFormat.parse("2013-09-23T11:44:18.963Z"), //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
				dateFormat.parse("2013-09-24T11:44:18.963Z")); //$NON-NLS-1$
		secondBacklogItem.setAssignedMilestoneId(501);
		secondBacklogItem.setInitialEffort(Float.valueOf(15));

		List<TuleapBacklogItem> backlogItemsList = new ArrayList<TuleapBacklogItem>();
		backlogItemsList.add(firstBacklogItem);
		backlogItemsList.add(secondBacklogItem);

		String theBacklogItems = gson.toJsonTree(backlogItemsList).toString();
		String expectedResult = "[{\"id\":200,\"assigned_milestone_id\":500},{\"id\":201,\"assigned_milestone_id\":501}]"; //$NON-NLS-1$

		assertEquals(expectedResult, theBacklogItems);
	}
}
