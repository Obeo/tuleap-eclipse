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

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapBacklogItem;
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
		TuleapReference projectRef = new TuleapReference(123, "p/123");
		TuleapBacklogItem firstBacklogItem = new TuleapBacklogItem(200, projectRef);
		TuleapReference assignedMilestone0 = new TuleapReference(1000, "milestones/1000");
		firstBacklogItem.setAssignedMilestone(assignedMilestone0);

		TuleapBacklogItem secondBacklogItem = new TuleapBacklogItem(201, projectRef);
		TuleapReference assignedMilestone1 = new TuleapReference(1001, "milestones/1001");
		secondBacklogItem.setAssignedMilestone(assignedMilestone1);

		List<TuleapBacklogItem> backlogItemsList = new ArrayList<TuleapBacklogItem>();
		backlogItemsList.add(firstBacklogItem);
		backlogItemsList.add(secondBacklogItem);

		String theBacklogItems = gson.toJsonTree(backlogItemsList).toString();
		String expectedResult = "[{\"id\":200,\"assigned_milestone\":{\"id\":1000}},{\"id\":201,\"assigned_milestone\":{\"id\":1001}}]"; //$NON-NLS-1$

		assertEquals(expectedResult, theBacklogItems);
	}

	/**
	 * Test the serialization of a list of backlogItems that are not assigned to a milestone.
	 */
	@Test
	public void testSerializeBacklogItemWithoutAffectedMilestone() {
		TuleapReference projectRef = new TuleapReference(123, "p/123");
		TuleapBacklogItem firstBacklogItem = new TuleapBacklogItem(200, projectRef);
		TuleapBacklogItem secondBacklogItem = new TuleapBacklogItem(201, projectRef);

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
		TuleapReference projectRef = new TuleapReference(123, "p/123");
		TuleapBacklogItem firstBacklogItem = new TuleapBacklogItem(projectRef);
		TuleapReference assignedMilestone0 = new TuleapReference(1000, "milestones/1000");
		firstBacklogItem.setAssignedMilestone(assignedMilestone0);

		TuleapBacklogItem secondBacklogItem = new TuleapBacklogItem(projectRef);
		TuleapReference assignedMilestone1 = new TuleapReference(1001, "milestones/1001");
		secondBacklogItem.setAssignedMilestone(assignedMilestone1);

		List<TuleapBacklogItem> backlogItemsList = new ArrayList<TuleapBacklogItem>();
		backlogItemsList.add(firstBacklogItem);
		backlogItemsList.add(secondBacklogItem);

		String theBacklogItems = gson.toJsonTree(backlogItemsList).toString();
		// FIXME This is what the current implementation does, but it's not logical to have ids here!
		String expectedResult = "[{\"id\":0,\"assigned_milestone\":{\"id\":1000}},{\"id\":0,\"assigned_milestone\":{\"id\":1001}}]"; //$NON-NLS-1$

		assertEquals(expectedResult, theBacklogItems);
	}
}
