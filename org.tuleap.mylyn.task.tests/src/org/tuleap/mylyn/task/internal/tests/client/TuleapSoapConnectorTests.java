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

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.config.ITuleapConfigurationConstants;
import org.tuleap.mylyn.task.internal.core.model.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.TuleapInstanceConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapTrackerReport;
import org.tuleap.mylyn.task.internal.core.net.TuleapSoapConnector;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.ArtifactComments;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.ArtifactFieldValue;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.FieldValue;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerField;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerFieldBindValue;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerReport;
import org.tuleap.mylyn.task.internal.tests.mocks.MockedAbstractWebLocation;
import org.tuleap.mylyn.task.internal.tests.mocks.MockedArtifact;
import org.tuleap.mylyn.task.internal.tests.mocks.MockedCodendiAPIPortType;
import org.tuleap.mylyn.task.internal.tests.mocks.MockedTracker;
import org.tuleap.mylyn.task.internal.tests.mocks.MockedTuleapTrackerV5APIPortType;
import org.tuleap.mylyn.task.internal.tests.mocks.MockedUserInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * The tests class for the Tuleap SOAP connector.
 * 
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @since 0.7
 */
public class TuleapSoapConnectorTests {
	/**
	 * Mocked server URL.
	 */
	private static final String URL = "http://demo.tuleap.net/"; //$NON-NLS-1$

	/**
	 * Default value used in field initialization.
	 */
	private static final String DEFAULT_VALUE = "defaultValue"; //$NON-NLS-1$

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
	 * Setup.
	 */
	@Before
	protected void setUp() {
		MockedAbstractWebLocation location = new MockedAbstractWebLocation(URL);
		codendiAPIPortType = new MockedCodendiAPIPortType();
		tuleapTrackerAPI = new MockedTuleapTrackerV5APIPortType();
		tuleapSoapConnector = new TuleapSoapConnector(location, codendiAPIPortType, tuleapTrackerAPI);
		assertNotNull(tuleapSoapConnector);
	}

	/**
	 * Test to retrieve a configuration.
	 */
	@Test
	public void testGetTuleapInstanceConfiguration() {
		MockedTracker tracker1 = createMockedTracker(1);
		MockedTracker tracker2 = createMockedTracker(2);
		MockedTracker tracker3 = createMockedTracker(3);

		tuleapTrackerAPI.setTrackers(tracker1, tracker2, tracker3);

		TuleapInstanceConfiguration tuleapInstanceConfiguration = tuleapSoapConnector
				.getTuleapInstanceConfiguration(new NullProgressMonitor());
		assertNotNull(tuleapInstanceConfiguration);

		List<TuleapProjectConfiguration> allProjectConfigurations = tuleapInstanceConfiguration
				.getAllProjectConfigurations();
		assertEquals(1, allProjectConfigurations.size());
		List<TuleapTrackerConfiguration> tuleapTrackerConfigurations = allProjectConfigurations.get(0)
				.getAllTrackerConfigurations();
		assertEquals(3, tuleapTrackerConfigurations.size());

		TuleapTrackerConfiguration tuleapTrackerConfiguration1 = tuleapInstanceConfiguration
				.getTrackerConfiguration(1);
		assertEquals(tracker1.getTracker_id(), tuleapTrackerConfiguration1.getTrackerId());
		assertEquals(tracker1.getName(), tuleapTrackerConfiguration1.getName());
		assertEquals(tracker1.getDescription(), tuleapTrackerConfiguration1.getDescription());
		assertEquals(tracker1.getItem_name(), tuleapTrackerConfiguration1.getItemName());
		assertEquals("Tracker1 [1]", tuleapTrackerConfiguration1.getQualifiedName()); //$NON-NLS-1$
		assertEquals(URL + "&tracker=1", tuleapTrackerConfiguration1.getUrl()); //$NON-NLS-1$
		assertEquals(tracker1.getTrackerFields().length, tuleapTrackerConfiguration1.getFields().size());
	}

	/**
	 * Test to retrieve a configuration.
	 */
	@Test
	public void testGetArtifact() {
		MockedArtifact artifact1 = createAMockedTrackerWithOneArtifact();

		TuleapArtifact tuleapArtifact;
		try {
			tuleapArtifact = tuleapSoapConnector.getArtifact(1, 1, new NullProgressMonitor());
			assertNotNull(tuleapArtifact);
			assertEquals(artifact1.getArtifact_id(), tuleapArtifact.getId());
			assertEquals(artifact1.getTracker_id(), tuleapArtifact.getTrackerId());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a mocked tracker with one artifact.
	 * 
	 * @return Mocked artifact
	 */
	public MockedArtifact createAMockedTrackerWithOneArtifact() {
		// Define users
		MockedUserInfo user1 = createUser(1);

		MockedTracker tracker1 = createMockedTracker(1);
		tuleapTrackerAPI.setTrackers(tracker1);

		// Set artifacts
		int artifactId1 = 1;
		MockedArtifact artifact1 = new MockedArtifact(artifactId1, tracker1);

		FieldValue fieldValue = new FieldValue(DEFAULT_VALUE, null, null);

		TrackerField tracker1Field1 = tracker1.getTrackerFields()[0];
		ArtifactFieldValue artifactFieldValue = new ArtifactFieldValue(tracker1Field1.getShort_name(),
				tracker1Field1.getLabel(), fieldValue);
		artifact1.setValue(new ArtifactFieldValue[] {artifactFieldValue });
		tracker1.setArtifact(artifact1);

		// Set comments
		int userId1 = Integer.parseInt(user1.getId());
		int submittedOn = 42;
		ArtifactComments comment1 = new ArtifactComments(userId1, user1.getEmail(), submittedOn, "body1"); //$NON-NLS-1$
		ArtifactComments comment2 = new ArtifactComments(userId1, user1.getEmail(), submittedOn, "body2"); //$NON-NLS-1$
		ArtifactComments[] comments = new ArtifactComments[] {comment1, comment2 };
		tuleapTrackerAPI.setArtifactComments(artifactId1, comments);
		return artifact1;
	}

	/**
	 * Test to update an artifact.
	 */
	@Test
	public void testUpdateArtifact() {
		MockedArtifact artifact = createAMockedTrackerWithOneArtifact();

		int artifactId = artifact.getArtifact_id();
		int trackerId = artifact.getTracker_id();
		MockedTracker tracker = artifact.getTracker();
		String trackerName = tracker.getName();
		String projectName = "projectName"; //$NON-NLS-1$
		TrackerField tracker1Field1 = tracker.getTrackerFields()[0];

		// Check the existing value
		try {
			TuleapArtifact oldArtifact = tuleapSoapConnector.getArtifact(trackerId, artifactId,
					new NullProgressMonitor());
			assertNotNull(oldArtifact);
			assertEquals(DEFAULT_VALUE, oldArtifact.getValue(String.valueOf(tracker1Field1.getShort_name())));

			// Create a mocked tuleap artifact
			TuleapArtifact tuleapArtifact = new TuleapArtifact(artifactId, trackerId, trackerName,
					projectName);
			String newValue = "newValue"; //$NON-NLS-1$
			tuleapArtifact.putValue(String.valueOf(tracker1Field1.getField_id()), newValue);

			tuleapSoapConnector.updateArtifact(tuleapArtifact, new NullProgressMonitor());

			TuleapArtifact newArtifact = tuleapSoapConnector.getArtifact(trackerId, artifactId,
					new NullProgressMonitor());
			assertNotNull(newArtifact);
			assertEquals(newValue, newArtifact.getValue(String.valueOf(tracker1Field1.getShort_name())));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test to get the reports.
	 */
	@Test
	public void testGetReports() {
		MockedTracker tracker1 = createMockedTracker(1);
		MockedUserInfo user1 = createUser(1);
		// Set reports
		int reportId = 1;
		String reportName = "Default"; //$NON-NLS-1$
		String reportDescription = "Default report"; //$NON-NLS-1$
		TrackerReport report = new TrackerReport(reportId, reportName, reportDescription, Integer
				.parseInt(user1.getId()), true);
		tuleapTrackerAPI.setReports(report);

		List<TuleapTrackerReport> reports = tuleapSoapConnector.getReports(tracker1.getTracker_id(),
				new NullProgressMonitor());
		assertEquals(1, reports.size());
		TuleapTrackerReport tuleapReport = reports.get(0);
		assertEquals(report.getId(), tuleapReport.getId());
		assertEquals(report.getName(), tuleapReport.getName());
	}

	/**
	 * Create a user.
	 * 
	 * @param index
	 *            Index
	 * @return Mocked user
	 */
	private MockedUserInfo createUser(int index) {
		// Define users
		MockedUserInfo user = new MockedUserInfo(
				"usr" + index, "User" + index, index, "user" + index + "@bigcompany.com"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		codendiAPIPortType.setUser(user);
		return user;
	}

	/**
	 * Create a mocked tracker with one tracker field.
	 * 
	 * @param index
	 *            Suffix used at the end of tracker name
	 * @return Mocked tracker
	 */
	private MockedTracker createMockedTracker(int index) {
		TrackerFieldBindValue trackerFieldBindValue = new TrackerFieldBindValue(index, "bind_value_label"); //$NON-NLS-1$
		TrackerField trackerField = new TrackerField(index, index,
				"Tkr" + index + "Fld" + index, //$NON-NLS-1$ //$NON-NLS-2$
				"Tracker" + index + " Field" + index, ITuleapConfigurationConstants.TEXT, //$NON-NLS-1$ //$NON-NLS-2$
				new TrackerFieldBindValue[] {trackerFieldBindValue }, null,
				new String[] {ITuleapConstants.PERMISSION_UPDATE });
		TrackerField[] trackerFields = new TrackerField[] {trackerField };
		return new MockedTracker(index, "Tracker" + index, trackerFields); //$NON-NLS-1$

	}
}
