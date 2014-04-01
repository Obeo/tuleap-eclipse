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
package org.eclipse.mylyn.tuleap.core.tests.internal.model;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.mylyn.tuleap.core.internal.model.data.IQueryCriterion;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapQueryCriterion;
import org.eclipse.mylyn.tuleap.core.internal.parser.TuleapGsonProvider;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests of {@link TuleapQueryCriterion}.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class QueryCriterionTest {

	private SimpleDateFormat format;

	private Date min;

	private Date max;

	private Gson gson;

	@Before
	public void setUp() throws Exception {
		format = new SimpleDateFormat("dd/MM/yyyy");
		min = format.parse("25/12/2010");
		max = format.parse("31/03/2014");
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		gson = TuleapGsonProvider.defaultGson();
	}

	@Test
	public void testAccessors() throws ParseException {
		TuleapQueryCriterion<Date[]> criterion = new TuleapQueryCriterion<Date[]>();
		criterion.setOperator(IQueryCriterion.OP_LT);
		criterion.setValue(new Date[] {min });
		Date[] value = criterion.getValue();
		assertEquals(1, value.length);
		assertEquals(format.parse("25/12/2010"), value[0]);
	}

	@Test
	public void testBetween() throws ParseException {
		TuleapQueryCriterion<Date[]> criterion = new TuleapQueryCriterion<Date[]>();
		criterion.setOperator(IQueryCriterion.OP_BETWEEN);
		criterion.setValue(new Date[] {min, max });
		Date[] value = criterion.getValue();
		assertEquals(2, value.length);
		assertEquals(format.parse("25/12/2010"), value[0]);
		assertEquals(format.parse("31/03/2014"), value[1]);
	}

	@Test
	public void testBetweenSerialization() {
		TuleapQueryCriterion<Date[]> criterion = new TuleapQueryCriterion<Date[]>();
		criterion.setOperator(IQueryCriterion.OP_BETWEEN);
		criterion.setValue(new Date[] {min, max });
		assertEquals(
				"{\"operator\":\"between\",\"value\":[\"2010-12-25T00:00:00+00:00\",\"2014-03-31T00:00:00+00:00\"]}",
				gson.toJson(criterion));
	}

	@Test
	public void testBeforeDateSerialization() {
		TuleapQueryCriterion<Date[]> criterion = new TuleapQueryCriterion<Date[]>();
		criterion.setOperator(IQueryCriterion.OP_LT);
		criterion.setValue(new Date[] {min });
		assertEquals("{\"operator\":\"<\",\"value\":[\"2010-12-25T00:00:00+00:00\"]}", gson.toJson(criterion));
	}

	@Test
	public void testAfterDateSerialization() {
		TuleapQueryCriterion<Date[]> criterion = new TuleapQueryCriterion<Date[]>();
		criterion.setOperator(IQueryCriterion.OP_GT);
		criterion.setValue(new Date[] {min });
		assertEquals("{\"operator\":\">\",\"value\":[\"2010-12-25T00:00:00+00:00\"]}", gson.toJson(criterion));
	}

	@Test
	public void testEqualDateSerialization() {
		TuleapQueryCriterion<Date[]> criterion = new TuleapQueryCriterion<Date[]>();
		criterion.setOperator(IQueryCriterion.OP_EQ);
		criterion.setValue(new Date[] {min });
		assertEquals("{\"operator\":\"=\",\"value\":[\"2010-12-25T00:00:00+00:00\"]}", gson.toJson(criterion));
	}

	@Test
	public void testContainsSerialization() {
		TuleapQueryCriterion<String> criterion = new TuleapQueryCriterion<String>();
		criterion.setOperator(IQueryCriterion.OP_CONTAINS);
		criterion.setValue("test value");
		assertEquals("{\"operator\":\"contains\",\"value\":\"test value\"}", gson.toJson(criterion));
	}

	@Test
	public void testOperatorContains() {
		TuleapQueryCriterion<String> criterion = new TuleapQueryCriterion<String>();
		criterion.setOperator(IQueryCriterion.OP_CONTAINS);
		assertEquals("contains", criterion.getOperator());
	}
}
