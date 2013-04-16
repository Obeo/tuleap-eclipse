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
package org.eclipse.mylyn.tuleap.tests.client;

import junit.framework.TestCase;

import org.eclipse.mylyn.internal.tuleap.core.util.TuleapUtil;

/**
 * Test class for {@link TuleapUtil}.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
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

	/**
	 * Test the parsing of the artifact id from the task data id.
	 */
	public void testGetArtifactIdFromTaskDataId() {
		String taskDataId = "Mylyn:Bugs[185] #122"; //$NON-NLS-1$
		int artifactId = TuleapUtil.getArtifactIdFromTaskDataId(taskDataId);
		assertEquals(122, artifactId);
	}

	/**
	 * Test the parsing of the tracker name from the task data id.
	 */
	public void testGetTrackerNameFromTaskDataId() {
		String taskDataId = "Mylyn:Bugs[172] #134"; //$NON-NLS-1$
		String trackerName = TuleapUtil.getTrackerNameFromTaskDataId(taskDataId);
		assertEquals("Bugs", trackerName); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the tracker id from the task data id.
	 */
	public void testGetTrackerIdFromTaskId() {
		String taskDataId = "Mylyn:Bugs[185] #134"; //$NON-NLS-1$
		int trackerId = TuleapUtil.getTrackerIdFromTaskDataId(taskDataId);
		assertEquals(185, trackerId);
	}

	/**
	 * Test the parsing of the project name from the task data id.
	 */
	public void testGetProjectNameFromTaskId() {
		String taskDataId = "Mylyn:Bugs[172] #134"; //$NON-NLS-1$
		String projectName = TuleapUtil.getProjectNameFromTaskDataId(taskDataId);
		assertEquals("Mylyn", projectName); //$NON-NLS-1$
	}

	/**
	 * Test the creation of the task data id.
	 */
	public void testGetTaskDataId() {
		String taskDataId = TuleapUtil.getTaskDataId("Mylyn", "Bugs[185]", 134); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Mylyn:Bugs[185] #134", taskDataId); //$NON-NLS-1$
	}

	/**
	 * Test if an URL is valid.
	 */
	public void testValidUrl() {
		assertFalse(TuleapUtil.isValidUrl("")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("http:/google.com")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("http:/google.com/")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("http://google.com/")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("http://google.com/foo /space")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("http://google.com")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("https://google.com")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("http://mylyn.org/trac30")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("http://www.mylyn.org/trac30")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("https://plugins/tracker/?tracker=42")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("https://my.demo.domain/plugins/tracker/?tracker=aa")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("https://my.demo.domain /plugins/tracker/?tracker=42")); //$NON-NLS-1$
		assertFalse(TuleapUtil.isValidUrl("https://my.demo.domain/plugins/tracker/?tracker=42x")); //$NON-NLS-1$
		assertTrue(TuleapUtil.isValidUrl("https://my.demo.domain/subdomain/plugins/tracker/?group_id=42")); //$NON-NLS-1$
		assertTrue(TuleapUtil.isValidUrl("https://my.demo.domain/plugins/tracker/?group_id=42")); //$NON-NLS-1$
	}
}
