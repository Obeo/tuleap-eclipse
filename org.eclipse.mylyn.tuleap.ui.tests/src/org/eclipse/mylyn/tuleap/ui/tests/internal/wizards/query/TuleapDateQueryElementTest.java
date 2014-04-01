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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.commons.workbench.forms.DatePicker;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapDate;
import org.eclipse.mylyn.tuleap.core.internal.model.data.IQueryCriterion;
import org.eclipse.mylyn.tuleap.ui.internal.wizards.query.TuleapDateQueryElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests of {@link TuleapDateQueryElement}. Requires to be launched as a plug-in test.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapDateQueryElementTest {

	private TuleapDate field;

	private Combo operator;

	private DatePicker from;

	private DatePicker to;

	private Calendar calFrom;

	private Calendar calTo;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		field = new TuleapDate(1001);
		Shell shell = new Shell(Display.getDefault());
		Composite c = new Composite(shell, SWT.NONE);
		operator = new Combo(c, SWT.NONE);
		from = new DatePicker(c, SWT.NONE, "", false, 0);
		to = new DatePicker(c, SWT.NONE, "", false, 0);
		calFrom = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
		calFrom.set(2013, 11, 1, 0, 0, 0); // 01/12/2013
		calFrom.set(Calendar.MILLISECOND, 0);
		calTo = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
		calTo.set(2014, 2, 1, 0, 0, 0); // 01/03/2014
		calTo.set(Calendar.MILLISECOND, 0);
	}

	@Test
	public void testGetField() throws CoreException {
		TuleapDateQueryElement element = new TuleapDateQueryElement(field, operator, from, to);
		assertSame(field, element.getField());
		IQueryCriterion<Date[]> criterion = element.getCriterion();
		assertNull(criterion);
	}

	@Test
	public void testCriterionIsNullIfBothDatesAreNull() throws CoreException {
		TuleapDateQueryElement element = new TuleapDateQueryElement(field, operator, from, to);
		assertNull(element.getCriterion());
	}

	@Test
	public void testCriterionForLessThan() throws CoreException {
		operator.setText("<");
		from.setDate(calFrom);
		TuleapDateQueryElement element = new TuleapDateQueryElement(field, operator, from, to);
		IQueryCriterion<Date[]> criterion = element.getCriterion();
		assertEquals("<", criterion.getOperator());
		Date[] value = criterion.getValue();
		assertEquals(1, value.length);
		assertEquals(calFrom.getTime(), value[0]);
	}

	@Test
	public void testValidateForLessThan() throws CoreException {
		operator.setText("<");
		from.setDate(calFrom);
		TuleapDateQueryElement element = new TuleapDateQueryElement(field, operator, from, to);
		assertTrue(element.validate());
	}

	@Test
	public void testCriterionForBetween() throws CoreException {
		operator.setText("between");
		from.setDate(calFrom);
		to.setDate(calTo);
		TuleapDateQueryElement element = new TuleapDateQueryElement(field, operator, from, to);
		IQueryCriterion<Date[]> criterion = element.getCriterion();
		assertEquals("between", criterion.getOperator());
		Date[] value = criterion.getValue();
		assertEquals(2, value.length);
		assertEquals(calFrom.getTime(), value[0]);
		assertEquals(calTo.getTime(), value[1]);
	}

	@Test
	public void testCriterionForBetweenWithoutTo() throws CoreException {
		operator.setText("between");
		from.setDate(calFrom);
		TuleapDateQueryElement element = new TuleapDateQueryElement(field, operator, from, to);
		try {
			element.getCriterion();
			fail("There should have been a CoreException.");
		} catch (CoreException ce) {
			// Test is OK
		}
	}

	@Test
	public void testValidateForBetweenWithoutTo() throws CoreException {
		operator.setText("between");
		from.setDate(calFrom);
		TuleapDateQueryElement element = new TuleapDateQueryElement(field, operator, from, to);
		assertFalse(element.validate());
	}

	@Test
	public void testValidateForBetweenWithoutFrom() throws CoreException {
		operator.setText("between");
		to.setDate(calTo);
		TuleapDateQueryElement element = new TuleapDateQueryElement(field, operator, from, to);
		assertFalse(element.validate());
	}

	@Test
	public void testValidateForBetweenWithoutFromNorTo() throws CoreException {
		operator.setText("between");
		TuleapDateQueryElement element = new TuleapDateQueryElement(field, operator, from, to);
		assertTrue(element.validate());
	}

	@Test
	public void testValidateForBetweenWithFromAndTo() throws CoreException {
		operator.setText("between");
		from.setDate(calFrom);
		to.setDate(calTo);
		TuleapDateQueryElement element = new TuleapDateQueryElement(field, operator, from, to);
		assertTrue(element.validate());
	}
}
