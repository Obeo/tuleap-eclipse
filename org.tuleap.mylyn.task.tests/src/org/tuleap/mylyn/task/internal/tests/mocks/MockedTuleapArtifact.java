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

import org.tuleap.mylyn.task.internal.core.model.TuleapArtifact;

/**
 * Mock of tuleap artifact.
 * 
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 */
public class MockedTuleapArtifact extends TuleapArtifact {

	/**
	 * Mock of tracker.
	 */
	MockedTracker tracker;

	/**
	 * Constructor.
	 * 
	 * @param mockedTracker
	 *            Mocked tracker.
	 */
	public MockedTuleapArtifact(MockedTracker mockedTracker) {
		this.tracker = mockedTracker;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.TuleapArtifact#getUniqueName()
	 */
	@Override
	public String getUniqueName() {
		return "42"; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.TuleapArtifact#getTrackerId()
	 */
	@Override
	public int getTrackerId() {
		return tracker.getTracker_id();
	}
}
