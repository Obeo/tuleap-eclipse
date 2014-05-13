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
package org.tuleap.mylyn.task.core.tests.internal.model;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapWorkflow;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapWorkflowTransition;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapSelectBox;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.core.internal.util.ITuleapConstants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit Tests of the TUleapWorkflow class.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapWorkflowTests {

	/**
	 * The tested workflow.
	 */
	private TuleapWorkflow workflow;

	/**
	 * The workflow's select box field.
	 */
	private TuleapSelectBox selectBox;

	/**
	 * The select box id.
	 */
	private int selectBoxId;

	/**
	 * nothing select select box item.
	 */
	private TuleapSelectBoxItem item100;

	/**
	 * First select box item.
	 */
	private TuleapSelectBoxItem item1;

	/**
	 * First select box item id.
	 */
	private int item1Id;

	/**
	 * Second select box item.
	 */
	private TuleapSelectBoxItem item2;

	/**
	 * Second select box item id.
	 */
	private int item2Id;

	/**
	 * Third select box item.
	 */
	private TuleapSelectBoxItem item3;

	/**
	 * Third select box item id.
	 */
	private int item3Id;

	/**
	 * Some unused id for testing.
	 */
	private int unusedId;

	/**
	 * Transition from unselected to item 1.
	 */
	private TuleapWorkflowTransition transition100To1;

	/**
	 * Transition from item 1 to item 2.
	 */
	private TuleapWorkflowTransition transition1To2;

	/**
	 * Transition from item 1 to item 3.
	 */
	private TuleapWorkflowTransition transition1To3;

	/**
	 * Transition from item 2 to item 3.
	 */
	private TuleapWorkflowTransition transition2To3;

	/**
	 * Transition from item 3 to item 2.
	 */
	private TuleapWorkflowTransition transition3To2;

	/**
	 * Tests on non-empty workflow.
	 */
	@Test
	public void testWorkflow() {
		assertFalse(workflow.hasTransitions());

		assertTrue(workflow.addTransition(transition100To1));
		assertTrue(workflow.hasTransitions());

		assertTrue(workflow.addTransition(transition1To2));
		assertTrue(workflow.addTransition(transition1To3));
		assertTrue(workflow.addTransition(transition2To3));
		assertTrue(workflow.addTransition(transition3To2));

		assertTrue(workflow.hasTransitions());

		Collection<TuleapSelectBoxItem> accessibleStates = workflow.accessibleStates(item1Id);
		assertEquals(2, accessibleStates.size());
		Iterator<TuleapSelectBoxItem> iterator = accessibleStates.iterator();
		assertEquals(item2, iterator.next());
		assertEquals(item3, iterator.next());

		accessibleStates = workflow.accessibleStates(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID);
		iterator = accessibleStates.iterator();
		assertEquals(1, accessibleStates.size());
		assertEquals(item1, accessibleStates.iterator().next());

		accessibleStates = workflow.accessibleStates(item2Id);
		iterator = accessibleStates.iterator();
		assertEquals(1, accessibleStates.size());
		assertEquals(item3, accessibleStates.iterator().next());

		accessibleStates = workflow.accessibleStates(item3Id);
		iterator = accessibleStates.iterator();
		assertEquals(1, accessibleStates.size());
		assertEquals(item2, accessibleStates.iterator().next());
	}

	/**
	 * Tests that it is impossible to add insignificant transitions to a workflow.
	 */
	@Test
	public void testAddingInvalidTransitionsToWorkflow() {
		assertFalse(workflow.hasTransitions());

		TuleapWorkflowTransition invalidTransition = new TuleapWorkflowTransition();
		// Invalid because from an unknown id
		invalidTransition.setFrom(unusedId);
		invalidTransition.setTo(item2Id);
		assertFalse(workflow.addTransition(invalidTransition));

		// Invalid because to an unknown id
		invalidTransition.setFrom(item1Id);
		invalidTransition.setTo(unusedId);
		assertFalse(workflow.addTransition(invalidTransition));

		// From default state, invalid because to an unknown id
		invalidTransition.setFrom(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID);
		invalidTransition.setTo(unusedId);
		assertFalse(workflow.addTransition(invalidTransition));

		assertFalse(workflow.hasTransitions());
	}

	/**
	 * Set up the test.
	 */
	@Before
	public void setUp() {
		selectBoxId = 123;
		selectBox = new TuleapSelectBox(selectBoxId);
		item1Id = 1;
		item2Id = 2;
		item3Id = 3;

		item100 = new TuleapSelectBoxItem(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID);
		selectBox.addItem(item100);

		item1 = new TuleapSelectBoxItem(item1Id);
		selectBox.addItem(item1);

		item2 = new TuleapSelectBoxItem(item2Id);
		selectBox.addItem(item2);

		item3 = new TuleapSelectBoxItem(item3Id);
		selectBox.addItem(item3);

		workflow = new TuleapWorkflow(selectBox);

		transition100To1 = new TuleapWorkflowTransition();
		transition100To1.setFrom(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID);
		transition100To1.setTo(item1Id);

		transition1To2 = new TuleapWorkflowTransition();
		transition1To2.setFrom(item1Id);
		transition1To2.setTo(item2Id);

		transition1To3 = new TuleapWorkflowTransition();
		transition1To3.setFrom(item1Id);
		transition1To3.setTo(item3Id);

		transition2To3 = new TuleapWorkflowTransition();
		transition2To3.setFrom(item2Id);
		transition2To3.setTo(item3Id);

		transition3To2 = new TuleapWorkflowTransition();
		transition3To2.setFrom(item3Id);
		transition3To2.setTo(item2Id);
	}
}
