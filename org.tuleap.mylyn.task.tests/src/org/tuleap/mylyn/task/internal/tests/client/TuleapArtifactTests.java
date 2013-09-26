/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.internal.tests.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.data.AttachmentFieldValue;
import org.tuleap.mylyn.task.internal.core.data.AttachmentValue;
import org.tuleap.mylyn.task.internal.core.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableElement;
import org.tuleap.mylyn.task.internal.core.model.TuleapPerson;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapArtifact;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class will test Tuleap artifacts.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapArtifactTests {

	/**
	 * The artifact to use for tests.
	 */
	AbstractTuleapConfigurableElement artifact = new TuleapArtifact(100);

	/**
	 * This test will ensure that once the value of a Tuleap attribute is set with a Literal value, we can
	 * access it.
	 */
	@Test
	public void testAddLiteralFieldValue() {
		String lbl = "Some value"; //$NON-NLS-1$
		AbstractFieldValue value0 = new LiteralFieldValue(123, lbl);
		artifact.addFieldValue(value0);
		AbstractFieldValue fieldValue = artifact.getFieldValue(123);
		assertEquals(123, fieldValue.getFieldId());
		assertEquals(lbl, ((LiteralFieldValue)fieldValue).getFieldValue());
	}

	/**
	 * This test will ensure that once the value of a Tuleap attribute is set with a bound value, we can
	 * access it.
	 */
	@Test
	public void testAddBoundFieldValue() {
		AbstractFieldValue value0 = new BoundFieldValue(123, Arrays.asList(0, 1, 2, 3));
		artifact.addFieldValue(value0);
		AbstractFieldValue fieldValue = artifact.getFieldValue(123);
		assertEquals(123, fieldValue.getFieldId());
		int i = 0;
		for (Integer boundValue : ((BoundFieldValue)value0).getValueIds()) {
			assertEquals(i++, boundValue.intValue());
		}
	}

	/**
	 * This test will ensure that once the value of a Tuleap attribute is set with a bound value, we can
	 * access it.
	 */
	@Test
	public void testAddAttachmentFieldValue() {
		AttachmentValue attVal = new AttachmentValue("111", "The name", new TuleapPerson("ldelaigue", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				"Laurent Delaigue", 555, "laurent.delaigue@some.com"), 123, "Description", "text/html"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		AttachmentFieldValue value0 = new AttachmentFieldValue(123, Collections.singletonList(attVal));
		artifact.addFieldValue(value0);
		AbstractFieldValue fieldValue = artifact.getFieldValue(123);
		assertEquals(123, fieldValue.getFieldId());
		assertTrue(fieldValue instanceof AttachmentFieldValue);
		List<AttachmentValue> attList = ((AttachmentFieldValue)fieldValue).getAttachments();
		assertEquals(1, attList.size());
		AttachmentValue att = attList.get(0);
		assertEquals("111", att.getAttachmentId()); //$NON-NLS-1$
		assertEquals("text/html", att.getContentType()); //$NON-NLS-1$
		assertEquals("Description", att.getDescription()); //$NON-NLS-1$
		assertEquals("The name", att.getFilename()); //$NON-NLS-1$
		assertEquals(555, att.getPerson().getId());
		assertEquals("ldelaigue", att.getPerson().getUserName()); //$NON-NLS-1$
		assertEquals("Laurent Delaigue", att.getPerson().getRealName()); //$NON-NLS-1$
		assertEquals("laurent.delaigue@some.com", att.getPerson().getEmail()); //$NON-NLS-1$
	}
}
