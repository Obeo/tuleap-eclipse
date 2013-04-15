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
package org.eclipse.mylyn.tuleap.tests.mocks;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Artifact;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactComments;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactFieldValue;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactQueryResult;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Criteria;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Tracker;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerField;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerReport;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TrackerStructure;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType;

/**
 * A mock of the Tuleap SOAP entry point.
 * 
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @since 0.7
 */
public class MockedTuleapTrackerV5APIPortType implements TuleapTrackerV5APIPortType {
	/**
	 * Map of mocked trackers.
	 */
	private Map<Integer, MockedTracker> trackers = new HashMap<Integer, MockedTracker>();

	/**
	 * Map of mocked artifacts comments.
	 */
	private Map<Integer, ArtifactComments[]> comments = new HashMap<Integer, ArtifactComments[]>();

	/**
	 * Map of mocked reports.
	 */
	private List<TrackerReport> reports = new ArrayList<TrackerReport>();

	/**
	 * Tracker structure.
	 */
	private TrackerStructure structure;

	/**
	 * Constructor.
	 */
	public MockedTuleapTrackerV5APIPortType() {
	}

	// CHECKSTYLE:OFF
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType#getVersion()
	 */
	public float getVersion() throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType#getTrackerList(java.lang.String,
	 *      int)
	 */
	public Tracker[] getTrackerList(String sessionKey, int group_id) throws RemoteException {
		return trackers.values().toArray(new Tracker[trackers.size()]);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType#getTrackerFields(java.lang.String,
	 *      int, int)
	 */
	public TrackerField[] getTrackerFields(String sessionKey, int group_id, int tracker_id)
			throws RemoteException {
		MockedTracker tracker = trackers.get(Integer.valueOf(tracker_id));
		return tracker.getTrackerFields();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType#getArtifacts(java.lang.String,
	 *      int, int, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Criteria[], int, int)
	 */
	public ArtifactQueryResult getArtifacts(String sessionKey, int group_id, int tracker_id,
			Criteria[] criteria, int offset, int max_rows) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType#addArtifact(java.lang.String,
	 *      int, int, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactFieldValue[])
	 */
	public int addArtifact(String sessionKey, int group_id, int tracker_id, ArtifactFieldValue[] value)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType#updateArtifact(java.lang.String,
	 *      int, int, int, org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.ArtifactFieldValue[],
	 *      java.lang.String, java.lang.String)
	 */
	public int updateArtifact(String sessionKey, int group_id, int tracker_id, int artifact_id,
			ArtifactFieldValue[] value, String comment, String comment_format) throws RemoteException {
		MockedTracker tracker = trackers.get(Integer.valueOf(tracker_id));
		MockedArtifact artifact = tracker.getArtifact(artifact_id);
		artifact.setValue(value);
		return 0;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType#getArtifact(java.lang.String,
	 *      int, int, int)
	 */
	public Artifact getArtifact(String sessionKey, int group_id, int tracker_id, int artifact_id)
			throws RemoteException {
		MockedTracker tracker = trackers.get(Integer.valueOf(tracker_id));
		return tracker.getArtifact(artifact_id);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType#getArtifactsFromReport(java.lang.String,
	 *      int, int, int)
	 */
	public ArtifactQueryResult getArtifactsFromReport(String sessionKey, int report_id, int offset,
			int max_rows) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType#getArtifactAttachmentChunk(java.lang.String,
	 *      int, int, int, int)
	 */
	public String getArtifactAttachmentChunk(String sessionKey, int artifact_id, int attachment_id,
			int offset, int size) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType#createTemporaryAttachment(java.lang.String)
	 */
	public String createTemporaryAttachment(String sessionKey) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType#appendTemporaryAttachmentChunk(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public int appendTemporaryAttachmentChunk(String sessionKey, String attachment_name, String content)
			throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType#purgeAllTemporaryAttachments(java.lang.String)
	 */
	public boolean purgeAllTemporaryAttachments(String sessionKey) throws RemoteException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType#getTrackerStructure(java.lang.String,
	 *      int, int)
	 */
	public TrackerStructure getTrackerStructure(String sessionKey, int group_id, int tracker_id)
			throws RemoteException {
		if (structure == null) {
			structure = new TrackerStructure();
		}
		return structure;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType#getTrackerReports(java.lang.String,
	 *      int, int)
	 */
	public TrackerReport[] getTrackerReports(String sessionKey, int group_id, int tracker_id)
			throws RemoteException {
		return reports.toArray(new TrackerReport[reports.size()]);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.TuleapTrackerV5APIPortType#getArtifactComments(java.lang.String,
	 *      int)
	 */
	public ArtifactComments[] getArtifactComments(String sessionKey, int artifact_id) throws RemoteException {
		return comments.get(Integer.valueOf(artifact_id));
	}

	// CHECKSTYLE:ON

	/**
	 * Set trackers.
	 * 
	 * @param parameterizableTrackers
	 *            Trackers.
	 */
	public void setTrackers(MockedTracker... parameterizableTrackers) {
		for (MockedTracker tracker : parameterizableTrackers) {
			this.trackers.put(Integer.valueOf(tracker.getTracker_id()), tracker);
		}
	}

	/**
	 * Set artifact comments.
	 * 
	 * @param artifactId
	 *            Artifact ID
	 * @param newComments
	 *            New comments
	 */
	public void setArtifactComments(int artifactId, ArtifactComments[] newComments) {
		comments.put(Integer.valueOf(artifactId), newComments);
	}

	/**
	 * Set a mocked report.
	 * 
	 * @param report
	 *            Mock of report
	 */
	public void setReports(TrackerReport report) {
		reports.add(report);
	}
}
