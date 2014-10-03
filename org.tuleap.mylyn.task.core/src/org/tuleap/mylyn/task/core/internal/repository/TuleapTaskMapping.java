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
package org.tuleap.mylyn.task.core.internal.repository;

import org.eclipse.mylyn.tasks.core.TaskMapping;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapTracker;

/**
 * The Tuleap task mapping provides additional mapping compared to the regular TaskMapping.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapTaskMapping extends TaskMapping {

	/**
	 * The tracker of the element.
	 */
	private TuleapTracker tracker;

	/**
	 * The parent artifact id.
	 */
	private String parentArtifactId;

	/**
	 * The constructor.
	 *
	 * @param tracker
	 *            The tracker of the element
	 */
	public TuleapTaskMapping(TuleapTracker tracker) {
		this.tracker = tracker;
	}

	/**
	 * The constructor.
	 *
	 * @param tracker
	 *            The tracker of the element
	 * @param parentArtifactId
	 *            The parent artifact id, can be <code>null</code>
	 */
	public TuleapTaskMapping(TuleapTracker tracker, String parentArtifactId) {
		this(tracker);
		this.parentArtifactId = parentArtifactId;
	}

	/**
	 * Returns the tracker in which the task is created.
	 *
	 * @return The tracker in which the task is created.
	 */
	public TuleapTracker getTracker() {
		return this.tracker;
	}

	/**
	 * Parent artifact id getter.
	 *
	 * @return Returns the paretn artifact ID, that can be <code>null</code>.
	 */
	public String getParentArtifactId() {
		return this.parentArtifactId;
	}

	/**
	 * Parent artifact id setter.
	 *
	 * @param parentArtifactId
	 *            the parentArtifactId to set. Can be <code>null</code>.
	 */
	public void setParentArtifactId(String parentArtifactId) {
		this.parentArtifactId = parentArtifactId;
	}
}
