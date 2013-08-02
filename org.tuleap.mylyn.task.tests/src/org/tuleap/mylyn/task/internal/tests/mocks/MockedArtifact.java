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
package org.tuleap.mylyn.task.internal.tests.mocks;

import org.tuleap.mylyn.task.internal.core.wsdl.soap.v2.Artifact;

/**
 * Mock of an artifact.
 * 
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @since 0.7
 */
public class MockedArtifact extends Artifact {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 4550062931467124876L;

	/**
	 * Mocked tracker.
	 */
	private MockedTracker tracker;

	/**
	 * Constructor.
	 * 
	 * @param artifactId
	 *            Artifact ID
	 * @param mockedTracker
	 *            Tracker
	 */
	public MockedArtifact(int artifactId, MockedTracker mockedTracker) {
		super(artifactId, mockedTracker.getTracker_id(), 42, 42, null, 42, null);
		this.tracker = mockedTracker;
	}

	/**
	 * Returns the mocked tracker.
	 * 
	 * @return The mocked tracker
	 */
	public MockedTracker getTracker() {
		return tracker;
	}
}
