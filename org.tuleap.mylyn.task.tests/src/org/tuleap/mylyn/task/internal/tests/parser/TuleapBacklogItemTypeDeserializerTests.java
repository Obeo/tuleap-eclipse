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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItemType;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapMultiSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.parser.TuleapBacklogItemTypeDeserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests the deserialization of the Tuleap BacklogItem Type.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapBacklogItemTypeDeserializerTests {

	/**
	 * Parse the content of the file and return the matching configuration.
	 * 
	 * @param fileContent
	 *            The content of the file
	 * @return The Tuleap BacklogItem Type matching the content of the file
	 */
	private TuleapBacklogItemType parse(String fileContent) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TuleapBacklogItemType.class, new TuleapBacklogItemTypeDeserializer());

		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(fileContent).getAsJsonObject();

		Gson gson = gsonBuilder.create();
		TuleapBacklogItemType tuleapBacklogItemType = gson.fromJson(jsonObject, TuleapBacklogItemType.class);

		return tuleapBacklogItemType;
	}

	/**
	 * Test the parsing of the first file.
	 */
	@Test
	public void testEpicsParsing() {
		String epics = ParserUtil.loadFile("/json/backlog_item_types/epics.json"); //$NON-NLS-1$
		TuleapBacklogItemType tuleapBacklogItemType = this.parse(epics);
		assertNotNull(tuleapBacklogItemType);
		assertEquals(801, tuleapBacklogItemType.getIdentifier());
		assertEquals("Epics", tuleapBacklogItemType.getName()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/backlog_item_types/801", tuleapBacklogItemType.getUrl()); //$NON-NLS-1$
		Collection<AbstractTuleapField> fields = tuleapBacklogItemType.getFields();
		assertEquals(4, fields.size());
		Iterator<AbstractTuleapField> iterator = fields.iterator();

		// testing the first field
		AbstractTuleapField firstField = iterator.next();

		List<Boolean> firstPermissions = fieldPermissionsFilling(false, false, false);

		this.basicFieldTest(firstField, null, "Status", 850, firstPermissions); //$NON-NLS-1$

		TuleapSelectBox theSelectBoxField = (TuleapSelectBox)firstField;

		// testing the semantic status
		assertTrue(theSelectBoxField.isSemanticStatus());

		// testing the open status
		assertEquals(2, theSelectBoxField.getOpenStatus().size());

		// testing the first field items
		Collection<TuleapSelectBoxItem> theFirstItems = theSelectBoxField.getItems();
		assertEquals(3, theFirstItems.size());

		Iterator<TuleapSelectBoxItem> theFirstItemsIterator = theFirstItems.iterator();

		TuleapSelectBoxItem theFirstFirstItem = theFirstItemsIterator.next();
		this.fieldItemsTest(firstField, theFirstFirstItem, "To be done"); //$NON-NLS-1$

		TuleapSelectBoxItem theSecondFirstItem = theFirstItemsIterator.next();
		this.fieldItemsTest(firstField, theSecondFirstItem, "On going"); //$NON-NLS-1$

		TuleapSelectBoxItem theThirdFirstItem = theFirstItemsIterator.next();
		this.fieldItemsTest(firstField, theThirdFirstItem, "Delivered"); //$NON-NLS-1$

		// testing the second field
		AbstractTuleapField secondField = iterator.next();
		List<Boolean> secondPermissions = fieldPermissionsFilling(true, false, false);

		this.basicFieldTest(secondField, null, "Remaining Effort", 851, secondPermissions); //$NON-NLS-1$

		// testing the third field
		AbstractTuleapField thirdField = iterator.next();

		List<Boolean> thirdPermissions = fieldPermissionsFilling(true, true, true);

		this.basicFieldTest(thirdField, null, "Summary", 852, thirdPermissions); //$NON-NLS-1$

		// testing the semantic title of the first field
		TuleapString stringField = (TuleapString)thirdField;
		assertTrue(thirdField instanceof TuleapString);
		assertTrue(stringField.isSemanticTitle());

		// testing the fourth field
		AbstractTuleapField fourthField = iterator.next();

		List<Boolean> fourthPermissions = fieldPermissionsFilling(true, true, true);

		this.basicFieldTest(fourthField, null, "Stories", 853, fourthPermissions); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the first part of second file.
	 */
	@Test
	public void testFirstPartUserStoriesParsing() {
		String userStories = ParserUtil.loadFile("/json/backlog_item_types/user_stories.json"); //$NON-NLS-1$
		TuleapBacklogItemType tuleapBacklogItemType = this.parse(userStories);
		assertNotNull(tuleapBacklogItemType);
		assertEquals(802, tuleapBacklogItemType.getIdentifier());
		assertEquals("User Stories", tuleapBacklogItemType.getName()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/backlog_item_types/802", tuleapBacklogItemType.getUrl()); //$NON-NLS-1$
		//assertEquals("The description of user stories", tuleapBacklogItemType.getDescription()); //$NON-NLS-1$
		System.out.print(tuleapBacklogItemType.getDescription());
		Collection<AbstractTuleapField> fields = tuleapBacklogItemType.getFields();
		assertEquals(10, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();

		// testing the first field
		AbstractTuleapField firstField = iterator.next();

		List<Boolean> firstPermissions = fieldPermissionsFilling(true, true, true);

		this.basicFieldTest(firstField, null, "As a", 860, firstPermissions); //$NON-NLS-1$

		TuleapSelectBox theSelectBoxField = (TuleapSelectBox)firstField;

		// testing the first field items
		Collection<TuleapSelectBoxItem> theFirstItems = theSelectBoxField.getItems();
		assertEquals(7, theFirstItems.size());

		Iterator<TuleapSelectBoxItem> theFirstItemsIterator = theFirstItems.iterator();

		TuleapSelectBoxItem theFirstFirstItem = theFirstItemsIterator.next();
		this.fieldItemsTest(firstField, theFirstFirstItem, "Admin"); //$NON-NLS-1$

		TuleapSelectBoxItem theSecondFirstItem = theFirstItemsIterator.next();
		this.fieldItemsTest(firstField, theSecondFirstItem, "Product Owner"); //$NON-NLS-1$

		TuleapSelectBoxItem theThirdFirstItem = theFirstItemsIterator.next();
		this.fieldItemsTest(firstField, theThirdFirstItem, "Scrum Team"); //$NON-NLS-1$

		TuleapSelectBoxItem theFourthFirstItem = theFirstItemsIterator.next();
		this.fieldItemsTest(firstField, theFourthFirstItem, "System Architect"); //$NON-NLS-1$

		TuleapSelectBoxItem theFifthFirstItem = theFirstItemsIterator.next();
		this.fieldItemsTest(firstField, theFifthFirstItem, "Tracker creator"); //$NON-NLS-1$

		TuleapSelectBoxItem theSixthFirstItem = theFirstItemsIterator.next();
		this.fieldItemsTest(firstField, theSixthFirstItem, "Tarcker user"); //$NON-NLS-1$

		TuleapSelectBoxItem theSeventhFirstItem = theFirstItemsIterator.next();
		this.fieldItemsTest(firstField, theSeventhFirstItem, "User"); //$NON-NLS-1$

		// testing the second field
		AbstractTuleapField secondField = iterator.next();
		List<Boolean> secondPermissions = fieldPermissionsFilling(true, true, true);

		this.basicFieldTest(secondField, null, "I want to", 861, secondPermissions); //$NON-NLS-1$

		// testing the semantic title of the second field
		TuleapString stringField = (TuleapString)secondField;
		assertTrue(secondField instanceof TuleapString);
		assertTrue(stringField.isSemanticTitle());

		// testing the third field
		AbstractTuleapField thirdField = iterator.next();

		List<Boolean> thirdPermissions = fieldPermissionsFilling(true, true, true);

		this.basicFieldTest(thirdField, null, "So that", 862, thirdPermissions); //$NON-NLS-1$

	}

	/**
	 * Test the parsing of the second part of the second file.
	 */
	@Test
	public void testSecondpartUserStoriesParsing() {
		String userStories = ParserUtil.loadFile("/json/backlog_item_types/user_stories.json"); //$NON-NLS-1$
		TuleapBacklogItemType tuleapBacklogItemType = this.parse(userStories);
		assertNotNull(tuleapBacklogItemType);
		assertEquals(802, tuleapBacklogItemType.getIdentifier());
		assertEquals("User Stories", tuleapBacklogItemType.getName()); //$NON-NLS-1$
		assertEquals("localhost:3001/api/v3.14/backlog_item_types/802", tuleapBacklogItemType.getUrl()); //$NON-NLS-1$
		//assertEquals("The description of user stories", tuleapBacklogItemType.getDescription()); //$NON-NLS-1$
		System.out.print(tuleapBacklogItemType.getDescription());
		Collection<AbstractTuleapField> fields = tuleapBacklogItemType.getFields();
		assertEquals(10, fields.size());

		Iterator<AbstractTuleapField> iterator = fields.iterator();
		for (int i = 0; i < 3; i++) {
			iterator.next();
		}

		// testing the fourth field
		AbstractTuleapField fourthField = iterator.next();

		List<Boolean> fourthPermissions = fieldPermissionsFilling(true, true, true);

		this.basicFieldTest(fourthField, null, "Acceptance criteria", 863, fourthPermissions); //$NON-NLS-1$

		// testing the fifth field
		AbstractTuleapField fifthField = iterator.next();

		List<Boolean> fifthPermissions = fieldPermissionsFilling(true, true, true);

		this.basicFieldTest(fifthField, null, "Tasks", 864, fifthPermissions); //$NON-NLS-1$

		// testing the sixth field
		AbstractTuleapField sixthField = iterator.next();

		List<Boolean> sixthPermissions = fieldPermissionsFilling(true, true, true);

		this.basicFieldTest(sixthField, null, "Iteration", 865, sixthPermissions); //$NON-NLS-1$

		// testing the seventh field
		AbstractTuleapField seventhField = iterator.next();

		List<Boolean> seventhPermissions = fieldPermissionsFilling(true, true, true);

		this.basicFieldTest(seventhField, null, "Story points", 866, seventhPermissions); //$NON-NLS-1$

		// testing the eighth field
		AbstractTuleapField eighthField = iterator.next();

		List<Boolean> eighthPermissions = fieldPermissionsFilling(true, false, false);

		this.basicFieldTest(eighthField, null, "Remaining Effort", 867, eighthPermissions); //$NON-NLS-1$

		// testing the ninth field
		AbstractTuleapField ninthField = iterator.next();

		List<Boolean> ninthPermissions = fieldPermissionsFilling(true, true, true);

		this.basicFieldTest(ninthField, null, "Status", 868, ninthPermissions); //$NON-NLS-1$

		TuleapSelectBox selectBoxField = (TuleapSelectBox)ninthField;

		// testing the first field items
		Collection<TuleapSelectBoxItem> theNinthItems = selectBoxField.getItems();
		assertEquals(4, theNinthItems.size());

		Iterator<TuleapSelectBoxItem> theNinthItemsIterator = theNinthItems.iterator();

		TuleapSelectBoxItem theFirstNinthItem = theNinthItemsIterator.next();
		this.fieldItemsTest(ninthField, theFirstNinthItem, "To be done"); //$NON-NLS-1$

		TuleapSelectBoxItem theSecondNinthItem = theNinthItemsIterator.next();
		this.fieldItemsTest(ninthField, theSecondNinthItem, "On going"); //$NON-NLS-1$

		TuleapSelectBoxItem theThirdNinthItem = theNinthItemsIterator.next();
		this.fieldItemsTest(ninthField, theThirdNinthItem, "Done"); //$NON-NLS-1$

		TuleapSelectBoxItem theFourthNinthItem = theNinthItemsIterator.next();
		this.fieldItemsTest(ninthField, theFourthNinthItem, "Canceled"); //$NON-NLS-1$

		// testing the open status
		assertEquals(2, selectBoxField.getOpenStatus().size());

		// testing the tenth field
		AbstractTuleapField tenthField = iterator.next();

		List<Boolean> tenthPermissions = fieldPermissionsFilling(true, true, true);

		this.basicFieldTest(tenthField, null, "Attachments", 869, tenthPermissions); //$NON-NLS-1$
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
