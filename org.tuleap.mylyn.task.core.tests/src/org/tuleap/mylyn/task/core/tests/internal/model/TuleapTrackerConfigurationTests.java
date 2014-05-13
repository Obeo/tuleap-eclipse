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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapProject;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapResource;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapTracker;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapArtifactLink;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapDate;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapFileUpload;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapFloat;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapInteger;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapOpenList;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapSelectBox;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapString;
import org.tuleap.mylyn.task.core.internal.model.config.field.TuleapText;

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
	private TuleapTracker tracker1;

	/**
	 * The second tracker.
	 */
	private TuleapTracker tracker2;

	/**
	 * The third tracker.
	 */
	private TuleapTracker tracker3;

	/**
	 * The fourth tracker.
	 */
	private TuleapTracker tracker4;

	/**
	 * The fifth tracker.
	 */
	private TuleapTracker tracker5;

	/**
	 * The sixth tracker.
	 */
	private TuleapTracker tracker6;

	/**
	 * The seventh tracker.
	 */
	private TuleapTracker tracker7;

	/**
	 * The eighth tracker.
	 */
	private TuleapTracker tracker8;

	/**
	 * The ninth tracker.
	 */
	private TuleapTracker tracker9;

	/**
	 * The tenth tracker.
	 */
	private TuleapTracker tracker10;

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
	 * Test date.
	 */
	private Date testDate;

	/**
	 * Set up the test.
	 *
	 * @throws ParseException
	 */
	@Before
	public void setUp() throws ParseException {
		Date now = new Date();
		tracker1 = new TuleapTracker(1, firstUrl, null, null, null, now);
		tracker2 = new TuleapTracker(2, secondUrl, null, null, null, now);
		tracker3 = new TuleapTracker(3, thirdUrl, null, null, null, now);
		tracker4 = new TuleapTracker(4, fourthUrl, null, null, null, now);
		tracker5 = new TuleapTracker(5, fifthUrl, null, null, null, now);
		tracker6 = new TuleapTracker(6, sixthUrl, null, null, null, now);
		tracker7 = new TuleapTracker(7, seventhUrl, null, null, null, now);
		tracker8 = new TuleapTracker(8, eighthUrl, null, null, null, now);
		tracker9 = new TuleapTracker(9, ninthUrl, null, null, null, now);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		testDate = simpleDateFormat.parse("11/03/2014");
		tracker10 = new TuleapTracker(10, "tracker/url", "Tracker", "Item name", "Description", testDate);

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

		Collection<TuleapTracker> children = tracker1.getChildrenTrackers();
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
		assertEquals(1, tracker4.getParentTracker().getIdentifier());

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
	public void testIrregularCases() {
		// adding a tracker to its self as a child
		tracker6.addChildTracker(tracker6);
		assertEquals(0, tracker6.getChildrenTrackers().size());

		// adding a tracker to its self as a parent
		tracker4.setParentTracker(tracker1);
		tracker4.setParentTracker(tracker4);
		assertEquals(tracker1, tracker4.getParentTracker());
	}

	@Test
	public void testResources() {

		String resource = "resource";
		assertFalse(tracker10.hasResource(resource));

		TuleapResource[] resources = new TuleapResource[4];

		for (int i = 0; i < 4; i++) {
			TuleapResource tuleapRessource = new TuleapResource("type" + i, "uri/" + i);
			resources[i] = tuleapRessource;
		}
		tracker10.setTrackerResources(resources);
		assertEquals(4, tracker10.getTrackerResources().length);

		for (int i = 0; i < 4; i++) {
			assertEquals("type" + i, tracker10.getTrackerResources()[i].getType());
			assertEquals("uri/" + i, tracker10.getTrackerResources()[i].getUri());
			assertTrue(tracker10.hasResource("type" + i));
		}
	}

	/**
	 * Test getting and setting tracker attributes .
	 */
	@Test
	public void testSetAndGetTrackerAttributes() {
		tracker10.setUri("tracker/10");
		assertEquals("tracker/10", tracker10.getUri());
		assertEquals(10, tracker10.getIdentifier());
		assertEquals("Tracker", tracker10.getLabel());
		assertEquals("Description", tracker10.getDescription());
		assertEquals("Item name", tracker10.getItemName());
		assertEquals(testDate, tracker10.getLastUpdateDate());
		assertEquals("tracker/url", tracker10.getUrl());
		assertNull(tracker10.getProject());

		TuleapProject project = new TuleapProject("Test Project", 42);
		tracker10.setProject(project);
		assertEquals(project, tracker10.getProject());
	}

	/**
	 * Test tracker with title semantic field .
	 */
	@Test
	public void testTrackerWithTitleSementicFields() {

		TuleapString tuleapString = new TuleapString(1);
		tuleapString.setName("string field name"); //$NON-NLS-1$
		tracker10.addField(tuleapString);

		TuleapText tuleapText = new TuleapText(2);
		tuleapText.setName("text field name"); //$NON-NLS-1$
		tracker10.addField(tuleapText);

		TuleapInteger tuleapInteger = new TuleapInteger(3);
		tuleapInteger.setName("integer field name"); //$NON-NLS-1$
		tracker10.addField(tuleapInteger);

		TuleapFloat tuleapFloat = new TuleapFloat(4);
		tuleapFloat.setName("float field name"); //$NON-NLS-1$
		tracker10.addField(tuleapFloat);

		TuleapDate tuleapDate = new TuleapDate(5);
		tuleapDate.setName("date field name"); //$NON-NLS-1$
		tracker10.addField(tuleapDate);

		assertEquals(5, tracker10.getFields().size());

		assertNull(tracker10.getTitleField());
		tuleapString.setSemanticTitle(true);
		tracker10.addField(tuleapString);
		assertEquals(tuleapString, tracker10.getTitleField());
	}

	/**
	 * Test tracker without title semantic field .
	 */
	@Test
	public void testTrackerWitoutTitleSemanticFields() {

		TuleapString tuleapString = new TuleapString(1);
		tuleapString.setName("string field name"); //$NON-NLS-1$
		tracker10.addField(tuleapString);

		TuleapText tuleapText = new TuleapText(2);
		tuleapText.setName("text field name"); //$NON-NLS-1$
		tracker10.addField(tuleapText);

		TuleapInteger tuleapInteger = new TuleapInteger(3);
		tuleapInteger.setName("integer field name"); //$NON-NLS-1$
		tracker10.addField(tuleapInteger);

		TuleapFloat tuleapFloat = new TuleapFloat(4);
		tuleapFloat.setName("float field name"); //$NON-NLS-1$
		tracker10.addField(tuleapFloat);

		TuleapDate tuleapDate = new TuleapDate(5);
		tuleapDate.setName("date field name"); //$NON-NLS-1$
		tracker10.addField(tuleapDate);

		assertEquals(5, tracker10.getFields().size());

		assertNull(tracker10.getTitleField());
	}

	/**
	 * Test tracker with contributor semantic field.
	 */
	@Test
	public void testTrackerWithContributorSemanticFields() {

		TuleapText tuleapText = new TuleapText(2);
		tuleapText.setName("text field name"); //$NON-NLS-1$
		tracker10.addField(tuleapText);

		TuleapInteger tuleapInteger = new TuleapInteger(3);
		tuleapInteger.setName("integer field name"); //$NON-NLS-1$
		tracker10.addField(tuleapInteger);

		TuleapFloat tuleapFloat = new TuleapFloat(4);
		tuleapFloat.setName("float field name"); //$NON-NLS-1$
		tracker10.addField(tuleapFloat);

		TuleapDate tuleapDate = new TuleapDate(5);
		tuleapDate.setName("date field name"); //$NON-NLS-1$
		tracker10.addField(tuleapDate);

		TuleapSelectBox tuleapSelectBox = new TuleapSelectBox(8);
		tuleapSelectBox.setName("tuleap select box field name"); //$NON-NLS-1$
		tracker10.addField(tuleapSelectBox);

		assertEquals(5, tracker10.getFields().size());

		assertNull(tracker10.getContributorField());
		tuleapSelectBox.setSemanticContributor(true);
		tracker10.addField(tuleapSelectBox);
		assertEquals(tuleapSelectBox, tracker10.getContributorField());
	}

	/**
	 * Test tracker without contributor semantic field.
	 */
	@Test
	public void testTrackerWithoutContributorSemanticFields() {

		TuleapText tuleapText = new TuleapText(2);
		tuleapText.setName("text field name"); //$NON-NLS-1$
		tracker10.addField(tuleapText);

		TuleapInteger tuleapInteger = new TuleapInteger(3);
		tuleapInteger.setName("integer field name"); //$NON-NLS-1$
		tracker10.addField(tuleapInteger);

		TuleapFloat tuleapFloat = new TuleapFloat(4);
		tuleapFloat.setName("float field name"); //$NON-NLS-1$
		tracker10.addField(tuleapFloat);

		TuleapDate tuleapDate = new TuleapDate(5);
		tuleapDate.setName("date field name"); //$NON-NLS-1$
		tracker10.addField(tuleapDate);

		TuleapSelectBox tuleapSelectBox = new TuleapSelectBox(8);
		tuleapSelectBox.setName("tuleap select box field name"); //$NON-NLS-1$
		tracker10.addField(tuleapSelectBox);

		assertEquals(5, tracker10.getFields().size());

		assertNull(tracker10.getContributorField());
	}

	/**
	 * Test tracker with status field.
	 */
	@Test
	public void testTrackerWithStatusField() {

		TuleapText tuleapText = new TuleapText(2);
		tuleapText.setName("text field name"); //$NON-NLS-1$
		tracker10.addField(tuleapText);

		TuleapInteger tuleapInteger = new TuleapInteger(3);
		tuleapInteger.setName("integer field name"); //$NON-NLS-1$
		tracker10.addField(tuleapInteger);

		TuleapFloat tuleapFloat = new TuleapFloat(4);
		tuleapFloat.setName("float field name"); //$NON-NLS-1$
		tracker10.addField(tuleapFloat);

		TuleapDate tuleapDate = new TuleapDate(5);
		tuleapDate.setName("date field name"); //$NON-NLS-1$
		tracker10.addField(tuleapDate);

		TuleapSelectBox tuleapSelectBox = new TuleapSelectBox(8);
		tuleapSelectBox.setName("tuleap select box field name"); //$NON-NLS-1$
		tracker10.addField(tuleapSelectBox);

		assertEquals(5, tracker10.getFields().size());

		assertNull(tracker10.getStatusField());
		TuleapSelectBoxItem item = new TuleapSelectBoxItem(24);
		item.setLabel("OPEN");
		tuleapSelectBox.addItem(item);
		tuleapSelectBox.getOpenStatus().add(item);
		tracker10.addField(tuleapSelectBox);
		assertEquals(tuleapSelectBox, tracker10.getStatusField());
		assertFalse(tracker10.hasClosedStatusMeaning(24));
	}

	/**
	 * Test tracker without status field.
	 */
	@Test
	public void testTrackerWithoutStatusField() {

		TuleapText tuleapText = new TuleapText(2);
		tuleapText.setName("text field name"); //$NON-NLS-1$
		tracker10.addField(tuleapText);

		TuleapInteger tuleapInteger = new TuleapInteger(3);
		tuleapInteger.setName("integer field name"); //$NON-NLS-1$
		tracker10.addField(tuleapInteger);

		TuleapFloat tuleapFloat = new TuleapFloat(4);
		tuleapFloat.setName("float field name"); //$NON-NLS-1$
		tracker10.addField(tuleapFloat);

		TuleapDate tuleapDate = new TuleapDate(5);
		tuleapDate.setName("date field name"); //$NON-NLS-1$
		tracker10.addField(tuleapDate);

		TuleapSelectBox tuleapSelectBox = new TuleapSelectBox(8);
		tuleapSelectBox.setName("tuleap select box field name"); //$NON-NLS-1$
		tracker10.addField(tuleapSelectBox);

		assertEquals(5, tracker10.getFields().size());

		assertNull(tracker10.getStatusField());
	}

	/**
	 * Test tracker with attachment field .
	 */
	@Test
	public void testTrackerWithAttachmentFieldFields() {

		TuleapText tuleapText = new TuleapText(2);
		tuleapText.setName("text field name"); //$NON-NLS-1$
		tracker10.addField(tuleapText);

		TuleapInteger tuleapInteger = new TuleapInteger(3);
		tuleapInteger.setName("integer field name"); //$NON-NLS-1$
		tracker10.addField(tuleapInteger);

		TuleapFloat tuleapFloat = new TuleapFloat(4);
		tuleapFloat.setName("float field name"); //$NON-NLS-1$
		tracker10.addField(tuleapFloat);

		TuleapDate tuleapDate = new TuleapDate(5);
		tuleapDate.setName("date field name"); //$NON-NLS-1$
		tracker10.addField(tuleapDate);

		TuleapOpenList tuleapOpenList = new TuleapOpenList(6);
		tuleapOpenList.setName("open list field name"); //$NON-NLS-1$
		tracker10.addField(tuleapOpenList);

		TuleapArtifactLink tuleapArtifactLink = new TuleapArtifactLink(7);
		tuleapArtifactLink.setName("artifact links field name"); //$NON-NLS-1$
		tracker10.addField(tuleapArtifactLink);

		TuleapFileUpload tuleapFileUpload = new TuleapFileUpload(10);
		tuleapFileUpload.setName("tuleap file upload field name"); //$NON-NLS-1$
		tracker10.addField(tuleapFileUpload);

		assertEquals(7, tracker10.getFields().size());

		assertEquals(tuleapFileUpload, tracker10.getAttachmentField());

	}

	/**
	 * Test tracker without attachment field .
	 */
	@Test
	public void testTrackerWithoutAttachmentFieldFields() {

		TuleapText tuleapText = new TuleapText(2);
		tuleapText.setName("text field name"); //$NON-NLS-1$
		tracker10.addField(tuleapText);

		TuleapInteger tuleapInteger = new TuleapInteger(3);
		tuleapInteger.setName("integer field name"); //$NON-NLS-1$
		tracker10.addField(tuleapInteger);

		TuleapFloat tuleapFloat = new TuleapFloat(4);
		tuleapFloat.setName("float field name"); //$NON-NLS-1$
		tracker10.addField(tuleapFloat);

		TuleapDate tuleapDate = new TuleapDate(5);
		tuleapDate.setName("date field name"); //$NON-NLS-1$
		tracker10.addField(tuleapDate);

		TuleapOpenList tuleapOpenList = new TuleapOpenList(6);
		tuleapOpenList.setName("open list field name"); //$NON-NLS-1$
		tracker10.addField(tuleapOpenList);

		TuleapArtifactLink tuleapArtifactLink = new TuleapArtifactLink(7);
		tuleapArtifactLink.setName("artifact links field name"); //$NON-NLS-1$
		tracker10.addField(tuleapArtifactLink);

		assertEquals(6, tracker10.getFields().size());

		assertNull(tracker10.getAttachmentField());

	}
}
