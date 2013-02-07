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
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapInstanceConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapTrackerConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapString;
import org.eclipse.mylyn.internal.tuleap.core.repository.TuleapAttributeMapper;
import org.eclipse.mylyn.internal.tuleap.core.repository.TuleapTaskDataHandler;
import org.eclipse.mylyn.internal.tuleap.core.util.ITuleapConstants;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.TaskMapping;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskData;

/**
 * The tests class for the Tuleap task data handler.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapTaskDataHandlerTests extends TestCase {

	/**
	 * Test the initialization of the task data.
	 */
	public void testInitializeTaskDataString() {
		String repositoryUrl = "https://demo.tuleap.net/plugins/tracker/?groupdId=871"; //$NON-NLS-1$
		String connectorKind = ITuleapConstants.CONNECTOR_KIND;
		String taskId = ""; //$NON-NLS-1$

		String configurationName = "Tracker Name"; //$NON-NLS-1$
		String repositoryDescription = "Tracker Description"; //$NON-NLS-1$

		TaskRepository repository = new TaskRepository(connectorKind, repositoryUrl);
		TuleapTrackerConfiguration tuleapTrackerConfiguration = new TuleapTrackerConfiguration(217,
				repositoryUrl);
		tuleapTrackerConfiguration.setName(configurationName);
		tuleapTrackerConfiguration.setDescription(repositoryDescription);
		TuleapString tuleapString = new TuleapString(1);
		tuleapTrackerConfiguration.getFields().add(tuleapString);

		TuleapInstanceConfiguration tuleapInstanceConfiguration = new TuleapInstanceConfiguration(
				repositoryUrl);
		tuleapInstanceConfiguration.addTracker(Integer.valueOf(217), tuleapTrackerConfiguration);

		MockedTuleapRepositoryConnector repositoryConnector = new MockedTuleapRepositoryConnector(
				tuleapInstanceConfiguration);
		TuleapTaskDataHandler tuleapTaskDataHandler = new TuleapTaskDataHandler(repositoryConnector);
		TaskData taskData = new TaskData(new TuleapAttributeMapper(repository, repositoryConnector),
				repositoryUrl, connectorKind, taskId);
		try {
			// Used to specify the tracker to use in the group
			ITaskMapping taskMapping = new TaskMapping() {
				/**
				 * {@inheritDoc}
				 * 
				 * @see org.eclipse.mylyn.tasks.core.TaskMapping#getProduct()
				 */
				@Override
				public String getProduct() {
					return "217"; //$NON-NLS-1$
				}
			};

			boolean isInitialized = tuleapTaskDataHandler.initializeTaskData(repository, taskData,
					taskMapping, new NullProgressMonitor());
			assertTrue(isInitialized);

			// Check that the attribute representing the Tuleap string does exists
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}
}
