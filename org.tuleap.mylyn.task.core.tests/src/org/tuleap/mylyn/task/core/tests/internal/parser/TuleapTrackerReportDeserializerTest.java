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
package org.tuleap.mylyn.task.core.tests.internal.parser;

import com.google.gson.Gson;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapTrackerReport;
import org.tuleap.mylyn.task.core.internal.parser.TuleapGsonProvider;

import static org.junit.Assert.assertEquals;

/**
 * Tests the JSON deserialization of {@link TuleapTrackerReport}.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapTrackerReportDeserializerTest {

	private Gson gson;

	@Before
	public void setUp() {
		gson = TuleapGsonProvider.defaultGson();
	}

	@Test
	public void testDeserializeTrackerReport() throws ParseException {
		String json = ParserUtil.loadFile("/tracker_reports/tracker_report-2.json");
		TuleapTrackerReport report = gson.fromJson(json, TuleapTrackerReport.class);
		assertEquals(2, report.getId());
		assertEquals("Default", report.getLabel());
		assertEquals("tracker_reports/2", report.getReportUri());

		assertEquals(2, report.getReportResources().length);

		assertEquals("artifacts", report.getReportResources()[0].getType());
		assertEquals("tracker_reports/2/artifacts", report.getReportResources()[0].getUri());

		assertEquals("sprints", report.getReportResources()[1].getType());
		assertEquals("tracker_reports/2/sprints", report.getReportResources()[1].getUri());
	}
}
