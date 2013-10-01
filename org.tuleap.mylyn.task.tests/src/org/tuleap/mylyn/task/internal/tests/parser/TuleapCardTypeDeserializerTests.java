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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapCardType;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFloat;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapMultiSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.parser.TuleapCardTypeDeserializer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests the deserialization of the Tuleap BacklogItem Type.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapCardTypeDeserializerTests extends AbstractConfigurationDeserializerTest<TuleapCardType> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected JsonDeserializer<TuleapCardType> getDeserializer() {
		TuleapCardTypeDeserializer deserializer = new TuleapCardTypeDeserializer(getProjectConfiguration());
		return deserializer;
	}

	/**
	 * Test the parsing of the first file.
	 */
	@Test
	public void testCard7000Parsing() {
		String epics = ParserUtil.loadFile("/card_types/task-7000.json"); //$NON-NLS-1$
		TuleapCardType cardType = this.parse(epics, TuleapCardType.class);
		checkCardType7000(cardType);

		// Check that the card type has been added to the project configuration
		assertSame(cardType, getProjectConfiguration().getCardType(7000));
	}

	/**
	 * Checks that the given card type corresponds to the type 7000.
	 * 
	 * @param cardType
	 *            The card type to test.
	 */
	public void checkCardType7000(TuleapCardType cardType) {
		assertNotNull(cardType);
		assertEquals(7000, cardType.getIdentifier());
		assertEquals("Tasks", cardType.getLabel()); //$NON-NLS-1$
		assertEquals("/card_types/7000", cardType.getUrl()); //$NON-NLS-1$
		Collection<AbstractTuleapField> fields = cardType.getFields();
		assertEquals(2, fields.size());
		Iterator<AbstractTuleapField> iterator = fields.iterator();

		// testing the first field
		AbstractTuleapField firstField = iterator.next();

		checkBasicField(firstField, null, "Remaining effort", 8000, true, true, true); //$NON-NLS-1$

		assertTrue(firstField instanceof TuleapFloat);

		// testing the second field
		AbstractTuleapField secondField = iterator.next();

		checkBasicField(secondField, null, "Assigned To", 8001, true, false, true); //$NON-NLS-1$

		// testing the first field items
		assertTrue(secondField instanceof TuleapMultiSelectBox);
		TuleapMultiSelectBox msb = (TuleapMultiSelectBox)secondField;
		assertTrue(msb.isSemanticContributor());
		assertFalse(msb.isSemanticStatus());
		Collection<TuleapSelectBoxItem> items = msb.getItems();
		assertEquals(4, items.size()); // 4 different users in the 2 groups
		int[] userIds = new int[4];
		int i = 0;
		for (TuleapSelectBoxItem item : items) {
			userIds[i++] = item.getIdentifier();
		}
		Arrays.sort(userIds);
		assertArrayEquals(userIds, new int[] {15, 16, 17, 18 });
	}
}
