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
package org.tuleap.mylyn.task.internal.tests.parser;

import com.google.gson.JsonDeserializer;

import java.text.ParseException;

import org.junit.Ignore;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapTopPlanning;
import org.tuleap.mylyn.task.internal.core.parser.TuleapTopPlanningDeserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

//CHECKSTYLE:OFF

/**
 * Tests the deserialization of the Tuleap milestone.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class TuleapTopPlanningDeserializerTests extends AbstractDeserializerTest<TuleapTopPlanning> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected JsonDeserializer<TuleapTopPlanning> getDeserializer() {
		return new TuleapTopPlanningDeserializer();
	}

	/**
	 * Test the parsing of the data set of the release 200.
	 * 
	 * @throws ParseException
	 */
	@Test
	@Ignore("To do when Enalean has made a choice")
	public void testTopPlanning30Parsing() throws ParseException {
		String tp30 = ParserUtil.loadFile("/top_plannings/top_planning_30.json");
		TuleapTopPlanning tp = this.parse(tp30, TuleapTopPlanning.class);
		checkTopPlanning30(tp);
	}

	/**
	 * Checks the content of the given milestone corresponds to release 200. Mutualized between several tests.
	 * 
	 * @param tp
	 *            The top planning
	 * @throws ParseException
	 */
	public void checkTopPlanning30(TuleapTopPlanning tp) throws ParseException {
		assertNotNull(tp);

		assertEquals(30, tp.getId());
		assertEquals("Project 30", tp.getLabel());
		assertEquals("/top_plannings/30", tp.getUri());
		// FIXME when top plannings are solved by Enalean
		// assertEquals(3, tp.getProjectId());
		// assertEquals(901, tp.getBinding().getMilestonesTypeId());
		// assertEquals(801, tp.getBinding().getBacklogItemsTypeId());
		// assertEquals("Releases", tp.getBinding().getMilestonesTitle());
		// assertEquals("Epics", tp.getBinding().getBacklogItemsTitle());
	}

}
