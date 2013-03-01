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

import java.rmi.RemoteException;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.internal.tuleap.core.config.ITuleapConfigurationConstants;
import org.eclipse.mylyn.internal.tuleap.core.model.TuleapInstanceConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.net.TuleapSoapConnector;
import org.eclipse.mylyn.internal.tuleap.core.repository.TuleapTaskDataHandler;
import org.eclipse.mylyn.internal.tuleap.core.util.ITuleapConstants;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerField;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerFieldBindValue;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerSemantic;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerSemanticContributor;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerSemanticStatus;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerSemanticTitle;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerStructure;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflow;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflowRuleArray;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerWorkflowTransition;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;

/**
 * The test class for the tuleap artifact to task data binding.
 * 
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @since 0.7
 */
public class TuleapArtifact2TaskDataBindingTests extends TestCase {
	/**
	 * Mocked server URL.
	 */
	private static final String URL = "http://my.domain.org/url"; //$NON-NLS-1$

	/**
	 * Tuleap connector kind.
	 */
	private static final String CONNECTOR_KIND = ITuleapConstants.TULEAP_DESCRIPTOR_FILE;

	/**
	 * Tuleap SOAP connector.
	 */
	private TuleapSoapConnector tuleapSoapConnector;

	/**
	 * Mocked common SOAP API.
	 */
	private MockedCodendiAPIPortType codendiAPIPortType;

	/**
	 * Mocked SOAP API entry point.
	 */
	private MockedTuleapTrackerV5APIPortType tuleapTrackerAPI;

	/**
	 * {@inheritDoc}
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		MockedAbstractWebLocation location = new MockedAbstractWebLocation(URL);
		codendiAPIPortType = new MockedCodendiAPIPortType();
		tuleapTrackerAPI = new MockedTuleapTrackerV5APIPortType();
		tuleapSoapConnector = new TuleapSoapConnector(location, codendiAPIPortType, tuleapTrackerAPI);
		assertNotNull(tuleapSoapConnector);
	}

	/**
	 * Test the text binding.
	 */
	public void testTextBinding() {
		final String fieldValue = "defaultValue"; //$NON-NLS-1$
		final int fieldId = 1234;

		MockedField field = new MockedField(fieldId, "Field1234", ITuleapConfigurationConstants.TEXT, //$NON-NLS-1$
				fieldValue);
		MockedTuleapArtifact tuleapArtifact = createMockedTuleapArtifact(field, null, null);

		TuleapInstanceConfiguration tuleapInstanceConfiguration = tuleapSoapConnector
				.getTuleapInstanceConfiguration(new NullProgressMonitor());
		MockedTuleapRepositoryConnector repositoryConnector = new MockedTuleapRepositoryConnector(
				tuleapInstanceConfiguration, tuleapArtifact);
		TuleapTaskDataHandler tuleapTaskDataHandler = new TuleapTaskDataHandler(repositoryConnector);

		TaskRepository taskRepository = new TaskRepository(CONNECTOR_KIND, URL);

		try {
			TaskData taskData = tuleapTaskDataHandler.getTaskData(taskRepository, String
					.valueOf(tuleapArtifact.getId()), new NullProgressMonitor());
			TaskAttribute root = taskData.getRoot();
			assertNotNull(root);
			assertEquals(fieldValue, root.getAttributes().get(String.valueOf(fieldId)).getValues().get(0));

		} catch (CoreException e) {
			fail();
		}
	}

	/**
	 * Test the select box binding.
	 */
	public void testStaticSelectBoxBinding() {
		// Create an empty workflow
		TrackerWorkflowRuleArray rules = null;
		TrackerWorkflowTransition[] transitions = null;
		TrackerWorkflow workflow = new TrackerWorkflow(42, 0, rules, transitions);

		// Create an empty semantic
		TrackerSemanticTitle title = null;
		TrackerSemanticStatus status = new TrackerSemanticStatus();
		TrackerSemanticContributor contributor = new TrackerSemanticContributor("SemanticContributor"); //$NON-NLS-1$
		TrackerSemantic semantic = new TrackerSemantic(title, status, contributor);

		final String[] fieldValues = new String[] {"A", "B", "C" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		final int fieldId = 1234;
		MockedField field = new MockedField(fieldId, "Field1234", ITuleapConfigurationConstants.SB, //$NON-NLS-1$
				fieldValues);
		MockedTuleapArtifact tuleapArtifact = createMockedTuleapArtifact(field, workflow, semantic);

		TuleapInstanceConfiguration tuleapInstanceConfiguration = tuleapSoapConnector
				.getTuleapInstanceConfiguration(new NullProgressMonitor());
		MockedTuleapRepositoryConnector repositoryConnector = new MockedTuleapRepositoryConnector(
				tuleapInstanceConfiguration, tuleapArtifact);
		TuleapTaskDataHandler tuleapTaskDataHandler = new TuleapTaskDataHandler(repositoryConnector);

		TaskRepository taskRepository = new TaskRepository(CONNECTOR_KIND, URL);

		try {
			TaskData taskData = tuleapTaskDataHandler.getTaskData(taskRepository, String
					.valueOf(tuleapArtifact.getId()), new NullProgressMonitor());
			TaskAttribute root = taskData.getRoot();
			assertNotNull(root);

			List<String> taskDataValues = root.getAttributes().get(String.valueOf(fieldId)).getValues();
			assertEquals(fieldValues.length, taskDataValues.size());
			for (String value : fieldValues) {
				assertTrue(taskDataValues.contains(value));
			}

		} catch (CoreException e) {
			fail();
		}
	}

	/**
	 * Create a mock of tracker.
	 * 
	 * @param field
	 *            Field
	 * @param workflow
	 *            Tracker workflow
	 * @param semantic
	 *            Tracker semantic
	 * @return Tracker
	 */
	private MockedTuleapArtifact createMockedTuleapArtifact(MockedField field, TrackerWorkflow workflow,
			TrackerSemantic semantic) {
		final int trackerID = 42;
		final int bindValudID = 42;
		TrackerFieldBindValue trackerFieldBindValue = new TrackerFieldBindValue(field.getId(), bindValudID,
				"bind_value_label"); //$NON-NLS-1$
		TrackerField trackerField = new TrackerField(trackerID, field.getId(), field.getName(), field
				.getName(), field.getType(), new TrackerFieldBindValue[] {trackerFieldBindValue },
				new String[] {ITuleapConstants.PERMISSION_UPDATE });
		TrackerField[] trackerFields = new TrackerField[] {trackerField };
		MockedTracker tracker = new MockedTracker(trackerID, "Tracker", trackerFields); //$NON-NLS-1$
		tuleapTrackerAPI.setTrackers(tracker);

		if (workflow != null) {
			try {
				TrackerStructure structure = tuleapTrackerAPI.getTrackerStructure(null,
						tracker.getGroup_id(), tracker.getTracker_id());
				structure.setWorkflow(workflow);
			} catch (RemoteException e) {
				fail();
			}
		}

		if (semantic != null) {
			try {
				TrackerStructure structure = tuleapTrackerAPI.getTrackerStructure(null,
						tracker.getGroup_id(), tracker.getTracker_id());
				structure.setSemantic(semantic);
			} catch (RemoteException e) {
				fail();
			}
		}

		MockedTuleapArtifact tuleapArtifact = new MockedTuleapArtifact(tracker);
		for (String value : field.getValues()) {
			tuleapArtifact.putValue(field.getName(), value);
		}

		return tuleapArtifact;
	}
}
