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

import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.util.TuleapUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link TuleapUtil}.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapUtilTests {
	/**
	 * Test the parsing of the domain of the repository from the given task repository.
	 */
	@Test
	public void testGetDomainRepositoryURL() {
		String repositoryUrl = "https://tuleap.mylyn.eclipse.org/plugins/tracker/?group_id=42"; //$NON-NLS-1$
		String domainRepositoryURL = TuleapUtil.getDomainRepositoryURL(repositoryUrl);
		assertEquals("https://tuleap.mylyn.eclipse.org", domainRepositoryURL); //$NON-NLS-1$
	}

	/**
	 * Test the creation of the task data id.
	 */
	@Test
	public void testGetTaskDataId() {
		String taskDataId = TuleapUtil.getTaskDataId("Mylyn", "Bugs", 134); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Mylyn:Bugs-134", taskDataId); //$NON-NLS-1$
	}

	/**
	 * Test if an URL is valid.
	 */
	@Test
	public void testValidUrl() {
		assertFalse(TuleapUtil.isValidUrl("")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("http:/google.com")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("http:/google.com/")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("http://google.com/")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("http://google.com/foo /space")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("http://google.com")); //$NON-NLS-1$
		assertTrue(TuleapUtil.isValidUrl("https://google.com")); //$NON-NLS-1$
	}
}
