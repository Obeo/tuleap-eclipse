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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapInteger;
import org.tuleap.mylyn.task.core.internal.model.data.IQueryCriterion;
import org.tuleap.mylyn.task.ui.internal.wizards.query.TuleapIntegerQueryElement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests of {@link TuleapIntegerQueryElement}. Requires to be launched as a plug-in test.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapIntegerQueryElementTest {

	private TuleapInteger field;

	private Text text;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		field = new TuleapInteger(1001);
		Shell shell = new Shell(Display.getDefault());
		Composite c = new Composite(shell, SWT.NONE);
		text = new Text(c, SWT.NONE);
	}

	@Test
	public void testGetField() throws CoreException {
		TuleapIntegerQueryElement element = new TuleapIntegerQueryElement(field, text);
		assertSame(field, element.getField());
	}

	@Test
	public void testCriterionIsNullIfTextIsEmpty() throws CoreException {
		TuleapIntegerQueryElement element = new TuleapIntegerQueryElement(field, text);
		assertNull(element.getCriterion());
	}

	@Test
	public void testCriterionForValidValue() throws CoreException {
		text.setText("123");
		TuleapIntegerQueryElement element = new TuleapIntegerQueryElement(field, text);
		IQueryCriterion<?> criterion = element.getCriterion();
		assertEquals("contains", criterion.getOperator());
		Integer value = (Integer)criterion.getValue();
		assertEquals(123, value.intValue());
	}

	@Test
	public void testCriterionForInvalidValue() throws CoreException {
		text.setText("invalid integer");
		TuleapIntegerQueryElement element = new TuleapIntegerQueryElement(field, text);
		assertNull(element.getCriterion());
	}

	@Test
	public void testValidateForInvalidValue() throws CoreException {
		text.setText("invalid integer");
		TuleapIntegerQueryElement element = new TuleapIntegerQueryElement(field, text);
		assertFalse(element.validate());
	}

	@Test
	public void testValidateForValidValue() throws CoreException {
		text.setText("123");
		TuleapIntegerQueryElement element = new TuleapIntegerQueryElement(field, text);
		assertTrue(element.validate());
	}
}
