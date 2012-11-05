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
package org.eclipse.mylyn.tuleap.tests.core;

import junit.framework.TestCase;

import org.eclipse.mylyn.internal.tuleap.core.util.TuleapUtil;

/**
 * Test class for {@link TuleapUtil}.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapUtilTests extends TestCase {
	/**
	 * Test the parsing of the domain of the repository from the given task repository.
	 */
	public void testGetDomainRepositoryURL() {
		String repositoryUrl = "https://tuleap.mylyn.eclipse.org/plugins/tracker/?group_id=42"; //$NON-NLS-1$
		String domainRepositoryURL = TuleapUtil.getDomainRepositoryURL(repositoryUrl);
		assertEquals("https://tuleap.mylyn.eclipse.org", domainRepositoryURL); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the group id of the repository from the given task repository.
	 */
	public void testGetGroupId() {
		String repositoryUrl = "https://tuleap.mylyn.eclipse.org/plugins/tracker/?group_id=42"; //$NON-NLS-1$
		int groupId = TuleapUtil.getGroupId(repositoryUrl);
		assertEquals(42, groupId);
	}
}
