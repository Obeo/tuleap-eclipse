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
package org.tuleap.mylyn.task.internal.core.model.data;

/**
 * Used to reference a tuleap artifact.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class ArtifactReference extends TuleapReference {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = 5185909570307674957L;

	/**
	 * The reference to the tracker that backs the artifact.
	 */
	private TuleapReference tracker;

	/**
	 * Constructor referencing a new tracker.
	 * 
	 * @param id
	 *            Id of the referenced element
	 * @param uri
	 *            URI of the referenced element
	 * @param trackerRef
	 *            The referenced tracker
	 */
	public ArtifactReference(int id, String uri, TuleapReference trackerRef) {
		super(id, uri);
		this.tracker = trackerRef;
	}

	/**
	 * Tracker reference getter.
	 * 
	 * @return the trackerRef to get
	 */
	public TuleapReference getTracker() {
		return tracker;
	}

	/**
	 * Tracker reference setter.
	 * 
	 * @param tracker
	 *            the trackerRef to set
	 */
	public void setTracker(TuleapReference tracker) {
		this.tracker = tracker;
	}

}
