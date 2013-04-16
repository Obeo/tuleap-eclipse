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
package org.tuleap.mylyn.task.tests.client;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.tuleap.mylyn.task.internal.core.model.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.util.TuleapUtil;

/**
 * This class will test Tuleap artifacts.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapArtifactTests extends TestCase {
	/**
	 * This test will ensure that a newly created Tuleap artifact is only valid when its ID is set with a
	 * valid value (artifactId >= 0).
	 */
	public void testValid() {
		TuleapArtifact artifact = new TuleapArtifact();
		assertFalse(artifact.isValid());

		artifact.setId(1);
		assertTrue(artifact.isValid());
	}

	/**
	 * This test will ensure that once the value of a Tuleap attribute is set, we can access it.
	 */
	public void testPutTuleapValue() {
		final String key = "attributeKey"; //$NON-NLS-1$
		final String value = "attributeValue"; //$NON-NLS-1$

		final String key2 = "attributeKey2"; //$NON-NLS-1$
		final String value2 = "attributeValue2"; //$NON-NLS-1$

		TuleapArtifact artifact = new TuleapArtifact();
		artifact.putValue(key, value);
		assertEquals(1, artifact.getValues(key).size());
		assertEquals(value, artifact.getValues(key).get(0));
		assertEquals(null, artifact.getValues(key2));
		assertEquals(null, artifact.getValues(value));

		artifact.putValue(key, value2);
		assertEquals(2, artifact.getValues(key).size());
		assertEquals(value, artifact.getValues(key).get(0));
		assertEquals(value2, artifact.getValues(key).get(1));
		assertEquals(null, artifact.getValues(value));

		artifact.putValue(key2, value2);
		assertEquals(1, artifact.getValues(key2).size());
		assertEquals(value2, artifact.getValues(key2).get(0));
		assertEquals(null, artifact.getValues(value));
		assertEquals(null, artifact.getValues(value2));
	}

	/**
	 * This test will ensure that we cannot create a new attribute named ID in the Tuleap artifact.
	 */
	public void testPutTuleapValueId() {
		final String key = "id"; //$NON-NLS-1$
		final String value = "attributeValue"; //$NON-NLS-1$

		TuleapArtifact artifact = new TuleapArtifact();
		artifact.putValue(key, value);
		assertEquals(null, artifact.getValue(key));
		assertEquals(TuleapArtifact.INVALID_ID, artifact.getId());

		final int id = 1;
		artifact.setId(id);
		assertEquals(null, artifact.getValue(key));
		assertEquals(id, artifact.getId());
	}

	/**
	 * This test will ensure that we can store and retrieve the creation date of a Tuleap artifact.
	 */
	public void testTuleapArtifactCreationDate() {
		TuleapArtifact artifact = new TuleapArtifact();

		Date date = new Date();
		Calendar utc = Calendar.getInstance(TimeZone.getTimeZone(ITuleapConstants.TIMEZONE_UTC));
		utc.setTime(date);
		Date tuleapDate = TuleapUtil.parseDate((int)(utc.getTimeInMillis() / 1000));
		artifact.setCreationDate(tuleapDate);

		assertEquals(date.getTime() / 1000, artifact.getCreationDate().getTime() / 1000);
	}

	/**
	 * This test will ensure that we can store and retrieve the date of the last modification of a Tuleap
	 * artifact.
	 */
	public void testTuleapArtifactLastModificationDate() {
		TuleapArtifact artifact = new TuleapArtifact();

		Date date = new Date();
		Calendar utc = Calendar.getInstance(TimeZone.getTimeZone(ITuleapConstants.TIMEZONE_UTC));
		utc.setTime(date);
		Date tuleapDate = TuleapUtil.parseDate((int)(utc.getTimeInMillis() / 1000));
		artifact.setLastModificationDate(tuleapDate);

		assertEquals(date.getTime() / 1000, artifact.getLastModificationDate().getTime() / 1000);
	}
}
