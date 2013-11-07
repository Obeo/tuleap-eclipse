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

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapBacklogItem;
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
		TuleapReference projectRef = new TuleapReference(123, "p/123");
		TuleapBacklogItem backlogItem = new TuleapBacklogItem(200, projectRef);

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
		TuleapReference projectRef = new TuleapReference(123, "p/123");
		TuleapBacklogItem backlogItem = new TuleapBacklogItem(200, projectRef);
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
		TuleapReference projectRef = new TuleapReference(123, "p/123");
		TuleapBacklogItem backlogItem = new TuleapBacklogItem(200, projectRef);
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
	public void testSerializeBacklogItemWithBothInitialEffortAndAssigneMilestone() throws ParseException {
		TuleapReference projectRef = new TuleapReference(123, "p/123");
		TuleapBacklogItem backlogItem = new TuleapBacklogItem(200, projectRef);
		backlogItem.setAssignedMilestoneId(1000);
		backlogItem.setInitialEffort(Float.valueOf(10));

		String emptyBacklogItem = gson.toJsonTree(backlogItem).toString();
		String expectedResult = "{\"initial_effort\":10.0,\"assigned_milestone_id\":1000}"; //$NON-NLS-1$
		assertEquals(expectedResult, emptyBacklogItem);
	}
}
