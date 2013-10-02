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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestoneType;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapMultiSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.parser.TuleapMilestoneTypeDeserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests the deserialization of the Tuleap Milestone Type.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapMilestoneTypeDeserializerTests extends AbstractConfigurationDeserializerTest<TuleapMilestoneType> {

	/**
	 * Test the parsing of the first file.
	 */
	@Test
	public void testReleasesParsing() {
		String releases = ParserUtil.loadFile("/milestone_types/releases.json"); //$NON-NLS-1$
		TuleapMilestoneType tuleapMilestoneType = this.parse(releases, TuleapMilestoneType.class);
		checkMilestoneTypeReleases(tuleapMilestoneType);

	}

	/**
	 * Test the parsing of the second file.
	 */
	@Test
	public void testSprintsParsing() {
		String sprints = ParserUtil.loadFile("/milestone_types/sprints.json"); //$NON-NLS-1$
		TuleapMilestoneType tuleapMilestoneType = this.parse(sprints, TuleapMilestoneType.class);
		checkMilestoneTypeSprints(tuleapMilestoneType);
	}

	/**
	 * Checks that the given milestone type represents releases.
	 * 
	 * @param milestoneType
	 *            The milestone type to test
	 */
	public void checkMilestoneTypeReleases(TuleapMilestoneType milestoneType) {
		assertNotNull(milestoneType);
		assertEquals(901, milestoneType.getIdentifier());
		assertEquals("Releases", milestoneType.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/milestone_types/901", milestoneType.getUrl()); //$NON-NLS-1$
		Collection<AbstractTuleapField> fields = milestoneType.getFields();
		assertEquals(13, fields.size());
		Iterator<AbstractTuleapField> iterator = fields.iterator();

		// testing the first field
		AbstractTuleapField firstField = iterator.next();

		List<Boolean> firstPermissions = fieldPermissionsFilling(true, true, true);

		this.basicFieldTest(firstField, null, "Title", 950, firstPermissions); //$NON-NLS-1$

		// testing the semantic title of the first field
		TuleapString stringField = (TuleapString)firstField;
		assertTrue(firstField instanceof TuleapString);
		assertTrue(stringField.isSemanticTitle());

		// testing the second field
		AbstractTuleapField secondField = iterator.next();
		List<Boolean> secondPermissions = fieldPermissionsFilling(false, false, false);

		this.basicFieldTest(secondField, null, "Status", 951, secondPermissions); //$NON-NLS-1$

		TuleapSelectBox theSelectBoxField = (TuleapSelectBox)secondField;

		// testing the semantic status
		assertTrue(theSelectBoxField.isSemanticStatus());

		// testing the open status
		assertEquals(2, theSelectBoxField.getOpenStatus().size());

		// testing the second field items
		Collection<TuleapSelectBoxItem> theSecondtItems = theSelectBoxField.getItems();
		assertEquals(3, theSecondtItems.size());

		Iterator<TuleapSelectBoxItem> theSecondItemsIterator = theSecondtItems.iterator();

		TuleapSelectBoxItem theSecondFirstItem = theSecondItemsIterator.next();
		this.fieldItemsTest(secondField, theSecondFirstItem, "Planned"); //$NON-NLS-1$

		TuleapSelectBoxItem theSecondSecondItem = theSecondItemsIterator.next();
		this.fieldItemsTest(secondField, theSecondSecondItem, "On going"); //$NON-NLS-1$

		TuleapSelectBoxItem theSecondThirdItem = theSecondItemsIterator.next();
		this.fieldItemsTest(secondField, theSecondThirdItem, "Delivered"); //$NON-NLS-1$

		// testing the third field
		AbstractTuleapField thirdField = iterator.next();
		List<Boolean> thirdPermissions = fieldPermissionsFilling(true, false, false);

		this.basicFieldTest(thirdField, null, "Remaining Effort", 952, thirdPermissions); //$NON-NLS-1$

		// testing the fourth field
		AbstractTuleapField fourthField = iterator.next();
		List<Boolean> fourthPermissions = fieldPermissionsFilling(true, true, true);
		this.basicFieldTest(fourthField, null, "Version", 953, fourthPermissions); //$NON-NLS-1$

		// testing the fifth field
		AbstractTuleapField fifthField = iterator.next();
		List<Boolean> fifthPermissions = fieldPermissionsFilling(true, true, true);
		this.basicFieldTest(fifthField, null, "In release", 954, fifthPermissions); //$NON-NLS-1$
	}

	/**
	 * Checks that the given milestone type represents releases.
	 * 
	 * @param milestoneType
	 *            The milestone type to test
	 */
	public void checkMilestoneTypeSprints(TuleapMilestoneType milestoneType) {
		assertNotNull(milestoneType);
		assertEquals(902, milestoneType.getIdentifier());
		assertEquals("Sprints", milestoneType.getLabel()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/milestone_types/902", milestoneType.getUrl()); //$NON-NLS-1$

		Collection<AbstractTuleapField> fields = milestoneType.getFields();
		assertEquals(7, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();
		// testing the first field
		AbstractTuleapField firstField = iterator.next();

		List<Boolean> firstPermissions = fieldPermissionsFilling(true, true, true);

		this.basicFieldTest(firstField, null, "Name", 960, firstPermissions); //$NON-NLS-1$

		// testing the semantic title of the first field
		TuleapString stringField = (TuleapString)firstField;
		assertTrue(firstField instanceof TuleapString);
		assertTrue(stringField.isSemanticTitle());

		// testing the second field
		AbstractTuleapField secondField = iterator.next();
		List<Boolean> secondPermissions = fieldPermissionsFilling(true, true, true);

		this.basicFieldTest(secondField, null, "Status", 961, secondPermissions); //$NON-NLS-1$

		TuleapSelectBox theSelectBoxField = (TuleapSelectBox)secondField;

		// testing the semantic status
		assertTrue(theSelectBoxField.isSemanticStatus());

		// testing the open status
		assertEquals(3, theSelectBoxField.getOpenStatus().size());

		// testing the second field items
		Collection<TuleapSelectBoxItem> theSecondtItems = theSelectBoxField.getItems();
		assertEquals(3, theSecondtItems.size());

		Iterator<TuleapSelectBoxItem> theSecondItemsIterator = theSecondtItems.iterator();

		TuleapSelectBoxItem theSecondFirstItem = theSecondItemsIterator.next();
		this.fieldItemsTest(secondField, theSecondFirstItem, "Planned"); //$NON-NLS-1$

		TuleapSelectBoxItem theSecondSecondItem = theSecondItemsIterator.next();
		this.fieldItemsTest(secondField, theSecondSecondItem, "Current"); //$NON-NLS-1$

		TuleapSelectBoxItem theSecondThirdItem = theSecondItemsIterator.next();
		this.fieldItemsTest(secondField, theSecondThirdItem, "Done"); //$NON-NLS-1$

		// testing the third field
		AbstractTuleapField thirdField = iterator.next();
		List<Boolean> thirdPermissions = fieldPermissionsFilling(true, true, true);
		this.basicFieldTest(thirdField, null, "Linked Artifacts", 962, thirdPermissions); //$NON-NLS-1$

		// testing the fourth field
		AbstractTuleapField fourthField = iterator.next();
		List<Boolean> fourthPermissions = fieldPermissionsFilling(true, true, true);
		this.basicFieldTest(fourthField, null, "Start date", 963, fourthPermissions); //$NON-NLS-1$

		// testing the fifth field
		AbstractTuleapField fifthField = iterator.next();
		List<Boolean> fifthPermissions = fieldPermissionsFilling(true, true, true);
		this.basicFieldTest(fifthField, null, "Duration", 964, fifthPermissions); //$NON-NLS-1$

		// testing the sixth field
		AbstractTuleapField sixthField = iterator.next();
		List<Boolean> sixthPermissions = fieldPermissionsFilling(true, true, true);
		this.basicFieldTest(sixthField, null, "Capacity", 965, sixthPermissions); //$NON-NLS-1$

		// testing the seventh field
		AbstractTuleapField seventhField = iterator.next();
		List<Boolean> seventhPermissions = fieldPermissionsFilling(true, false, false);
		this.basicFieldTest(seventhField, null, "Remaining Effort", 966, seventhPermissions); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected JsonDeserializer<TuleapMilestoneType> getDeserializer() {
		return new TuleapMilestoneTypeDeserializer(getProjectConfiguration());
	}

	/**
	 * Utility method to test field's basic information (id, name, label and permissions).
	 * 
	 * @param field
	 *            the field
	 * @param fieldName
	 *            the field name
	 * @param fieldLabel
	 *            the field label
	 * @param fieldId
	 *            the field id
	 * @param permissions
	 *            the field permissions
	 */

	public void basicFieldTest(AbstractTuleapField field, String fieldName, String fieldLabel, int fieldId,
			List<Boolean> permissions) {
		assertEquals(fieldId, field.getIdentifier());
		assertEquals(fieldName, field.getName());
		assertEquals(fieldLabel, field.getLabel());
		assertTrue(field.isReadable() == permissions.get(0).booleanValue());
		assertTrue(field.isSubmitable() == permissions.get(1).booleanValue());
		assertTrue(field.isUpdatable() == permissions.get(2).booleanValue());

	}

	/**
	 * Utility method to test field's items information.
	 * 
	 * @param field
	 *            the field to test
	 * @param item
	 *            the item to test
	 * @param label
	 *            the item's label
	 */

	public void fieldItemsTest(AbstractTuleapField field, TuleapSelectBoxItem item, String label) {

		if (field instanceof TuleapSelectBox) {
			TuleapSelectBox selectBoxField = (TuleapSelectBox)field;
			Collection<TuleapSelectBoxItem> items = selectBoxField.getItems();

			if (items.contains(item)) {
				TuleapSelectBoxItem selectBoxItem = item;
				assertEquals(label, selectBoxItem.getLabel());
			}
		} else if (field instanceof TuleapMultiSelectBox) {
			TuleapMultiSelectBox multiSelectBoxField = (TuleapMultiSelectBox)field;
			Collection<TuleapSelectBoxItem> items = multiSelectBoxField.getItems();

			if (items.contains(item)) {
				TuleapSelectBoxItem selectBoxItem = item;
				assertEquals(label, selectBoxItem.getLabel());
			}
		}
	}

	/**
	 * Utility method to fill in field permissions.
	 * 
	 * @param read
	 *            the read permission
	 * @param create
	 *            the create permission
	 * @param update
	 *            the update permissions
	 * @return the list of field's permissions
	 */
	public List<Boolean> fieldPermissionsFilling(boolean read, boolean create, boolean update) {

		List<Boolean> permissions = new ArrayList<Boolean>();
		permissions.add(new Boolean(read));
		permissions.add(new Boolean(create));
		permissions.add(new Boolean(update));
		return permissions;
	}
}
