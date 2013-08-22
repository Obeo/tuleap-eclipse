/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.internal.core.model.agile;

/**
 * Ancestor of Tuleap agile concepts that are backed by a tuleap tracker.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractTuleapTrackerElement extends AbstractTuleapAgileElement {

	/**
	 * The artifact URL.
	 */
	protected String artifactUrl;

	/**
	 * The tracker id.
	 */
	protected int trackerId;

	/**
	 * artifact URL getter.
	 * 
	 * @return the artifactUrl
	 */
	public String getArtifactUrl() {
		return artifactUrl;
	}

	/**
	 * artifact URL setter.
	 * 
	 * @param artifactUrl
	 *            the artifactUrl to set
	 */
	public void setArtifactUrl(String artifactUrl) {
		this.artifactUrl = artifactUrl;
	}

	/**
	 * tracker id getter.
	 * 
	 * @return the trackerId
	 */
	public int getTrackerId() {
		return trackerId;
	}

	/**
	 * tracker id setter.
	 * 
	 * @param trackerId
	 *            the trackerId to set
	 */
	public void setTrackerId(int trackerId) {
		this.trackerId = trackerId;
	}

}
