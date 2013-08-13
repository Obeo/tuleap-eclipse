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
package org.tuleap.mylyn.task.internal.tests.model;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.TuleapTrackerConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit Tests of the TuleapTrackerConfiguration class.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapTrackerConfigurationTests {

	/**
	 * The first tracker.
	 */
	private TuleapTrackerConfiguration tracker1;

	/**
	 * The second tracker.
	 */
	private TuleapTrackerConfiguration tracker2;

	/**
	 * The third tracker.
	 */
	private TuleapTrackerConfiguration tracker3;

	/**
	 * The fourth tracker.
	 */
	private TuleapTrackerConfiguration tracker4;

	/**
	 * The fifth tracker.
	 */
	private TuleapTrackerConfiguration tracker5;

	/**
	 * The sixth tracker.
	 */
	private TuleapTrackerConfiguration tracker6;

	/**
	 * The first url.
	 */
	private String firstUrl = "localhost:3001/api/v3.14/trackers/1"; //$NON-NLS-1$

	/**
	 * The second url.
	 */
	private String secondUrl = "localhost:3001/api/v3.14/trackers/2"; //$NON-NLS-1$

	/**
	 * The third url.
	 */
	private String thirdUrl = "localhost:3001/api/v3.14/trackers/3"; //$NON-NLS-1$

	/**
	 * The fourth url.
	 */
	private String fourthUrl = "localhost:3001/api/v3.14/trackers/4"; //$NON-NLS-1$

	/**
	 * The fifth url.
	 */
	private String fifthUrl = "localhost:3001/api/v3.14/trackers/5"; //$NON-NLS-1$

	/**
	 * The sixth url.
	 */
	private String sixthUrl = "localhost:3001/api/v3.14/trackers/6"; //$NON-NLS-1$

	/**
	 * Set up the test.
	 */
	@Before
	public void setUp() {

		tracker1 = new TuleapTrackerConfiguration(1, firstUrl);
		tracker2 = new TuleapTrackerConfiguration(2, secondUrl);
		tracker3 = new TuleapTrackerConfiguration(3, thirdUrl);
		tracker4 = new TuleapTrackerConfiguration(4, fourthUrl);
		tracker5 = new TuleapTrackerConfiguration(5, fifthUrl);
		tracker6 = new TuleapTrackerConfiguration(6, sixthUrl);

	}

	/**
	 * Testing the parent/children relationship between trackers.
	 */
	@Test
	public void testParentChildrenTrackers() {

		assertEquals(0, tracker1.getChildrenTrackers().size());
		assertNull(tracker1.getParentTracker());

		// adding the child trackers
		tracker1.addChildTracker(tracker2);
		tracker1.addChildTracker(tracker3);
		tracker1.addChildTracker(tracker4);
		tracker1.addChildTracker(tracker5);
		tracker1.addChildTracker(tracker6);

		// testing the children tracker collection
		Collection<TuleapTrackerConfiguration> children = tracker1.getChildrenTrackers();
		assertEquals(5, children.size());
		assertTrue(children.contains(tracker2));
		assertTrue(children.contains(tracker3));
		assertEquals(1, tracker4.getParentTracker().getTrackerId());
		assertEquals(1, tracker5.getParentTracker().getTrackerId());
		assertEquals(1, tracker6.getParentTracker().getTrackerId());

		// test changing the parent
		tracker5.setParentTracker(tracker3);
		assertEquals(tracker3, tracker5.getParentTracker());
		assertEquals(1, tracker3.getChildrenTrackers().size());
		assertTrue(tracker3.getChildrenTrackers().contains(tracker5));

		assertEquals(4, children.size());
		assertFalse(children.contains(tracker5));
	}

}
