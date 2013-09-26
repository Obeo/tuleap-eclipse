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

import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableElement;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapArtifact;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

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
		assertSame(fieldValue, value0);
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
		assertSame(fieldValue, value0);
		assertEquals("Some value", ((LiteralFieldValue)fieldValue).getFieldValue()); //$NON-NLS-1$
	}
}
