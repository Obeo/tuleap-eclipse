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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.internal.tuleap.core.repository.TuleapAttributeMapper;
import org.eclipse.mylyn.internal.tuleap.core.repository.TuleapTaskDataHandler;
import org.eclipse.mylyn.internal.tuleap.core.util.ITuleapConstants;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskMapper;

/**
 * The tests class for the Tuleap task data handler.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapTaskDataHandlerTests extends TestCase {

	/**
	 * A mocked Tuleap repository connector.
	 */
	private MockedTuleapRepositoryConnector repositoryConnector;

	/**
	 * Test the initialization of the task data.
	 */
	public void testInitializeTaskData() {
		String repositoryUrl = "https://demo.tuleap.net/plugins/tracker/?tracker=871"; //$NON-NLS-1$
		String connectorKind = ITuleapConstants.CONNECTOR_KIND;
		String taskId = ""; //$NON-NLS-1$

		TaskRepository repository = new TaskRepository(connectorKind, repositoryUrl);

		TuleapTaskDataHandler tuleapTaskDataHandler = new TuleapTaskDataHandler(repositoryConnector);
		TaskData taskData = new TaskData(new TuleapAttributeMapper(repository, repositoryConnector),
				repositoryUrl, connectorKind, taskId);
		try {
			boolean isInitialized = tuleapTaskDataHandler.initializeTaskData(repository, taskData,
					new TaskMapper(taskData), new NullProgressMonitor());
			assertTrue(isInitialized);
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test the creation of the task data.
	 */
	public void testCreateTaskData() {
		fail();
	}

	/**
	 * Test the retrieval of the task data for a basic task.
	 */
	public void testGetTaskData() {
		fail();
	}

	/**
	 * Test the update of the task data.
	 */
	public void testUpdateTaskData() {
		fail();
	}

	/**
	 * Test the post of the task data.
	 */
	public void testPostTaskData() {
		fail();
	}

	/**
	 * Test the use of semantic fields in the task data.
	 */
	public void testSemantic() {
		fail();
	}

	/**
	 * Test the use of the workflow in the task data.
	 */
	public void testWorkflow() {
		fail();
	}

	/**
	 * Test the fact that a closed artifact cannot be edited anymore, except for its status.
	 */
	public void testClosedArtifactNotEditable() {
		fail();
	}

	/**
	 * Test reopening and closing an artifact.
	 */
	public void testReopenClosedArtifact() {
		fail();
	}
}
