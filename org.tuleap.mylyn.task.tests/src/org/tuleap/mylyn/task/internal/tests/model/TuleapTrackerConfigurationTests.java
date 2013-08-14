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
	 * The seventh tracker.
	 */
	private TuleapTrackerConfiguration tracker7;

	/**
	 * The eighth tracker.
	 */
	private TuleapTrackerConfiguration tracker8;

	/**
	 * The ninth tracker.
	 */
	private TuleapTrackerConfiguration tracker9;

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
	 * The sevent url.
	 */
	private String seventhUrl = "localhost:3001/api/v3.14/trackers/7"; //$NON-NLS-1$

	/**
	 * The sixth url.
	 */
	private String eighthUrl = "localhost:3001/api/v3.14/trackers/8"; //$NON-NLS-1$

	/**
	 * The sixth url.
	 */
	private String ninthUrl = "localhost:3001/api/v3.14/trackers/9"; //$NON-NLS-1$

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
		tracker7 = new TuleapTrackerConfiguration(7, seventhUrl);
		tracker8 = new TuleapTrackerConfiguration(8, eighthUrl);
		tracker9 = new TuleapTrackerConfiguration(9, ninthUrl);

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

		// testing that the parent contains all children

		Collection<TuleapTrackerConfiguration> children = tracker1.getChildrenTrackers();
		assertEquals(5, children.size());
		assertTrue(children.contains(tracker2));
		assertTrue(children.contains(tracker3));
		assertTrue(children.contains(tracker4));
		assertTrue(children.contains(tracker5));
		assertTrue(children.contains(tracker6));

		// testing that tracker 1 is the parent of trackers 2, 3, 4, 5 and 6
		assertEquals(tracker1, tracker2.getParentTracker());
		assertEquals(tracker1, tracker3.getParentTracker());
		assertEquals(tracker1, tracker4.getParentTracker());
		assertEquals(tracker1, tracker5.getParentTracker());
		assertEquals(tracker1, tracker6.getParentTracker());

		// test changing the parent
		tracker5.setParentTracker(tracker3);
		assertEquals(tracker3, tracker5.getParentTracker());
		assertEquals(1, tracker3.getChildrenTrackers().size());
		assertTrue(tracker3.getChildrenTrackers().contains(tracker5));

		assertEquals(4, children.size());
		assertFalse(children.contains(tracker5));

		// changing the parent using setParent

		tracker8.setParentTracker(tracker7);
		assertEquals(tracker7, tracker8.getParentTracker());
		assertTrue(tracker7.getChildrenTrackers().contains(tracker8));

		tracker8.setParentTracker(tracker9);
		assertEquals(tracker9, tracker8.getParentTracker());
		assertTrue(tracker9.getChildrenTrackers().contains(tracker8));
		assertFalse(tracker7.getChildrenTrackers().contains(tracker8));

		// changing the parent using addChild

		tracker7.addChildTracker(tracker8);
		assertEquals(tracker7, tracker8.getParentTracker());
		assertTrue(tracker7.getChildrenTrackers().contains(tracker8));

		tracker9.addChildTracker(tracker8);
		assertEquals(tracker9, tracker8.getParentTracker());
		assertTrue(tracker9.getChildrenTrackers().contains(tracker8));
		assertFalse(tracker7.getChildrenTrackers().contains(tracker8));

		// adding a tracker to its self as a child
		tracker6.addChildTracker(tracker6);
		assertEquals(0, tracker6.getChildrenTrackers().size());

		// adding a tracker to its self as a parent
		tracker4.setParentTracker(tracker4);
		assertEquals(1, tracker4.getParentTracker().getTrackerId());

	}

	/**
	 * Testing changing the parent using setParent method.
	 */
	@Test
	public void testChangingParentTrackerUsingSetParent() {

		tracker8.setParentTracker(tracker7);
		assertEquals(tracker7, tracker8.getParentTracker());
		assertTrue(tracker7.getChildrenTrackers().contains(tracker8));

		tracker8.setParentTracker(tracker9);
		assertEquals(tracker9, tracker8.getParentTracker());
		assertTrue(tracker9.getChildrenTrackers().contains(tracker8));
		assertFalse(tracker7.getChildrenTrackers().contains(tracker8));
	}

	/**
	 * Testing changing the parent using addChild method.
	 */
	@Test
	public void testParentChildrenTrackersUsingAddChild() {

		tracker7.addChildTracker(tracker8);
		assertEquals(tracker7, tracker8.getParentTracker());
		assertTrue(tracker7.getChildrenTrackers().contains(tracker8));

		tracker9.addChildTracker(tracker8);
		assertEquals(tracker9, tracker8.getParentTracker());
		assertTrue(tracker9.getChildrenTrackers().contains(tracker8));
		assertFalse(tracker7.getChildrenTrackers().contains(tracker8));
	}

	/**
	 * Testing irregular cases .
	 */
	@Test
	public void testIrrigularCases() {

		// adding a tracker to its self as a child
		tracker6.addChildTracker(tracker6);
		assertEquals(0, tracker6.getChildrenTrackers().size());

		// adding a tracker to its self as a parent
		tracker4.setParentTracker(tracker1);
		tracker4.setParentTracker(tracker4);
		assertEquals(tracker1, tracker4.getParentTracker());
	}
}
