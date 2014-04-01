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
package org.eclipse.mylyn.tuleap.ui.tests.internal.wizards.query;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapInteger;
import org.eclipse.mylyn.tuleap.core.internal.model.data.IQueryCriterion;
import org.eclipse.mylyn.tuleap.ui.internal.wizards.query.TuleapDoubleQueryElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests of {@link TuleapDoubleQueryElement}. Requires to be launched as a plug-in test.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapDoubleQueryElementTest {

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
		TuleapDoubleQueryElement element = new TuleapDoubleQueryElement(field, text);
		assertSame(field, element.getField());
	}

	@Test
	public void testCriterionIsNullIfTextIsEmpty() throws CoreException {
		TuleapDoubleQueryElement element = new TuleapDoubleQueryElement(field, text);
		assertNull(element.getCriterion());
	}

	@Test
	public void testCriterionForValidValue() throws CoreException {
		text.setText("123.45");
		TuleapDoubleQueryElement element = new TuleapDoubleQueryElement(field, text);
		IQueryCriterion<?> criterion = element.getCriterion();
		assertEquals("contains", criterion.getOperator());
		Double value = (Double)criterion.getValue();
		assertEquals(123, value.intValue());
	}

	@Test
	public void testCriterionForInvalidValue() throws CoreException {
		text.setText("invalid double");
		TuleapDoubleQueryElement element = new TuleapDoubleQueryElement(field, text);
		assertNull(element.getCriterion());
	}

	@Test
	public void testValidateForInvalidValue() throws CoreException {
		text.setText("invalid double");
		TuleapDoubleQueryElement element = new TuleapDoubleQueryElement(field, text);
		assertFalse(element.validate());
	}

	@Test
	public void testValidateForValidValueInteger() throws CoreException {
		text.setText("123");
		TuleapDoubleQueryElement element = new TuleapDoubleQueryElement(field, text);
		assertTrue(element.validate());
	}

	@Test
	public void testValidateForValidValueDouble() throws CoreException {
		text.setText("123.566");
		TuleapDoubleQueryElement element = new TuleapDoubleQueryElement(field, text);
		assertTrue(element.validate());
	}
}
