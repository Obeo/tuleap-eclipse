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
package org.tuleap.mylyn.task.tests.mocks;

import java.util.HashMap;
import java.util.Map;

import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.Tracker;
import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.TrackerField;

/**
 * A parameterizable tracker.
 * 
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @since 0.7
 */
public class MockedTracker extends Tracker {

	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = -8496198657805355320L;

	/**
	 * Tracker fields.
	 */
	private TrackerField[] trackerFields;

	/**
	 * Artifacts defined in current tracker.
	 */
	private Map<Integer, MockedArtifact> artifacts = new HashMap<Integer, MockedArtifact>();

	/**
	 * Constructor.
	 * 
	 * @param trackerId
	 *            Tracker ID
	 * @param name
	 *            Tracker name
	 * @param fields
	 *            Tracker fields
	 */
	public MockedTracker(int trackerId, String name, TrackerField[] fields) {
		super(trackerId, 42, name, "description", "itemName"); //$NON-NLS-1$//$NON-NLS-2$
		this.trackerFields = fields;
	}

	/**
	 * Get the tracker fields.
	 * 
	 * @return The tracker fields
	 */
	public TrackerField[] getTrackerFields() {
		return trackerFields;
	}

	/**
	 * Get artifact.
	 * 
	 * @param artifactId
	 *            Artifact ID
	 * @return Artifact
	 */
	public MockedArtifact getArtifact(int artifactId) {
		return artifacts.get(Integer.valueOf(artifactId));
	}

	/**
	 * Set artifact.
	 * 
	 * @param artifact
	 *            Artifact
	 */
	public void setArtifact(MockedArtifact artifact) {
		artifacts.put(Integer.valueOf(artifact.getArtifact_id()), artifact);
	}
}
