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
package org.tuleap.mylyn.task.ui.tests.internal.wizards.query;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapMultiSelectBox;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapSelectBox;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.core.internal.model.data.IQueryCriterion;
import org.tuleap.mylyn.task.ui.internal.wizards.query.TuleapSelectBoxQueryElement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests of {@link TuleapSelectBoxQueryElement}. Requires to be launched as a plug-in test.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapSelectBoxQueryElementTest {

	private TuleapSelectBox fieldSingle;

	private TuleapMultiSelectBox fieldMulti;

	private List comboSingle;

	private List comboMulti;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		Shell shell = new Shell(Display.getDefault());
		Composite c = new Composite(shell, SWT.NONE);
		fieldSingle = new TuleapSelectBox(1000);
		comboSingle = new List(c, SWT.MULTI); // Even for single SB fields, several criteria can be entered
		for (int i = 0; i < 4; i++) {
			TuleapSelectBoxItem item = new TuleapSelectBoxItem(10 + i);
			String label = "Label SB " + i;
			item.setLabel(label);
			fieldSingle.addItem(item);
			comboSingle.add(label);
		}
		fieldMulti = new TuleapMultiSelectBox(1000);
		comboMulti = new List(c, SWT.MULTI);
		for (int i = 0; i < 4; i++) {
			TuleapSelectBoxItem item = new TuleapSelectBoxItem(100 + i);
			String label = "Label MSB " + i;
			item.setLabel(label);
			fieldMulti.addItem(item);
			comboMulti.add(label);
		}
	}

	@Test
	public void testGetFieldSingle() throws CoreException {
		TuleapSelectBoxQueryElement element = new TuleapSelectBoxQueryElement(fieldSingle, comboSingle);
		assertSame(fieldSingle, element.getField());
	}

	@Test
	public void testGetFieldMulti() throws CoreException {
		TuleapSelectBoxQueryElement element = new TuleapSelectBoxQueryElement(fieldMulti, comboMulti);
		assertSame(fieldMulti, element.getField());
	}

	@Test
	public void testCriterionSingleForEmptySelection() throws CoreException {
		TuleapSelectBoxQueryElement element = new TuleapSelectBoxQueryElement(fieldSingle, comboSingle);
		assertNull(element.getCriterion());
	}

	@Test
	public void testCriterionMultiForEmptySelection() throws CoreException {
		TuleapSelectBoxQueryElement element = new TuleapSelectBoxQueryElement(fieldMulti, comboMulti);
		assertNull(element.getCriterion());
	}

	@Test
	public void testCriterionSingleForNonEmptySelection() throws CoreException {
		comboSingle.setSelection(0, 2);
		TuleapSelectBoxQueryElement element = new TuleapSelectBoxQueryElement(fieldSingle, comboSingle);
		IQueryCriterion<?> criterion = element.getCriterion();
		assertEquals("contains", criterion.getOperator());
		String[] value = (String[])criterion.getValue();
		assertEquals(3, value.length);
		assertEquals("Label SB 0", value[0]);
		assertEquals("Label SB 1", value[1]);
		assertEquals("Label SB 2", value[2]);
	}

	@Test
	public void testCriterionMultiForNonEmptySelection() throws CoreException {
		comboMulti.setSelection(0, 2);
		TuleapSelectBoxQueryElement element = new TuleapSelectBoxQueryElement(fieldMulti, comboMulti);
		IQueryCriterion<?> criterion = element.getCriterion();
		assertEquals("contains", criterion.getOperator());
		String[] value = (String[])criterion.getValue();
		assertEquals(3, value.length);
		assertEquals("Label MSB 0", value[0]);
		assertEquals("Label MSB 1", value[1]);
		assertEquals("Label MSB 2", value[2]);
	}

	@Test
	public void testValidateSingleForEmptySelection() throws CoreException {
		TuleapSelectBoxQueryElement element = new TuleapSelectBoxQueryElement(fieldSingle, comboSingle);
		assertTrue(element.validate());
	}

	@Test
	public void testValidateMultiForEmptySelection() throws CoreException {
		TuleapSelectBoxQueryElement element = new TuleapSelectBoxQueryElement(fieldMulti, comboMulti);
		assertTrue(element.validate());
	}

	@Test
	public void testValidateSingleForNonEmptySelection() throws CoreException {
		comboSingle.setSelection(0, 1);
		TuleapSelectBoxQueryElement element = new TuleapSelectBoxQueryElement(fieldSingle, comboSingle);
		assertTrue(element.validate());
	}

	@Test
	public void testValidateMultiForNonEmptySelection() throws CoreException {
		comboSingle.setSelection(0, 1);
		TuleapSelectBoxQueryElement element = new TuleapSelectBoxQueryElement(fieldMulti, comboMulti);
		assertTrue(element.validate());
	}
}
