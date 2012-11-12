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
package org.eclipse.mylyn.internal.tuleap.core.model;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.eclipse.mylyn.internal.tuleap.core.util.TuleapUtil;
import org.eclipse.mylyn.internal.tuleap.core.wsdl.soap.v2.Artifact;

/**
 * This class represents a Tuleap artifact obtained from the Tuleap tracker.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapArtifact {

	/**
	 * The invalid artifact ID.
	 */
	public static final int INVALID_ID = -1;

	/**
	 * The Tuleap artifact ID.
	 */
	private int id;

	/**
	 * The properties of the artifact.
	 */
	private HashMap<String, String> properties = new HashMap<String, String>();

	/**
	 * The date of creation of the artifact.
	 */
	private Date creationDate;

	/**
	 * The date of the last modification of the artifact.
	 */
	private Date lastModificationDate;

	/**
	 * The id of the tracker on which the artifact has been created.
	 */
	private int trackerId;

	/**
	 * The default constructor used to create a new Tuleap artifact locally.
	 */
	public TuleapArtifact() {
		this.id = INVALID_ID;
	}

	/**
	 * The constructor used to create a Tuleap artifact with the artifact ID computed from a Tuleap tracker.
	 * 
	 * @param artifactId
	 *            The Tuleap artifact ID.
	 */
	public TuleapArtifact(int artifactId) {
		this.id = artifactId;
	}

	/**
	 * The constructor used to create a Tuleap artifact from a real artifact.
	 * 
	 * @param artifact
	 *            The artifact
	 */
	public TuleapArtifact(Artifact artifact) {
		this.id = artifact.getArtifact_id();
		this.trackerId = artifact.getTracker_id();
	}

	/**
	 * Returns the ID of the Tuleap artifact.
	 * 
	 * @return The ID of the Tuleap artifact.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Sets the ID of the Tuleap artifact.
	 * 
	 * @param newId
	 *            The ID of the Tuleap artifact.
	 */
	public void setId(int newId) {
		this.id = newId;
	}

	/**
	 * Stores a value as it is retrieved from the repository.
	 * 
	 * @param keyName
	 *            The name of the key
	 * @param value
	 *            The value of the property
	 */
	public void putValue(String keyName, String value) {
		if (!"id".equals(keyName)) { //$NON-NLS-1$			
			this.properties.put(keyName, value);
		}
	}

	/**
	 * Returns the value of the attribute with the given key.
	 * 
	 * @param key
	 *            The key of the attribute
	 * @return The value of the attribute with the given key.
	 */
	public String getValue(String key) {
		return this.properties.get(key);
	}

	/**
	 * Returns the set of the keys of all the properties.
	 * 
	 * @return The set of the keys of all the properties.
	 */
	public Set<String> getKeys() {
		return Collections.unmodifiableSet(this.properties.keySet());
	}

	/**
	 * Returns <code>true</code> if the ID of the artifact is different from the invalid ID (-1),
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if the ID of the artifact is different from the invalid ID (-1),
	 *         <code>false</code> otherwise.
	 */
	public boolean isValid() {
		return this.id != TuleapArtifact.INVALID_ID;
	}

	/**
	 * Sets the date of the creation of the artifact.
	 * 
	 * @param date
	 *            The date of the creation of the artifact
	 */
	public void setCreationDate(Date date) {
		this.creationDate = date;
	}

	/**
	 * Returns the date of the creation of the artifact.
	 * 
	 * @return The date of the creation of the artifact.
	 */
	public Date getCreationDate() {
		return this.creationDate;
	}

	/**
	 * Sets the date of the last modification of the artifact.
	 * 
	 * @param date
	 *            The date of the last modification of the artifact.
	 */
	public void setLastModificationDate(Date date) {
		this.lastModificationDate = date;
	}

	/**
	 * Returns the date of the last modification of the artifact.
	 * 
	 * @return The date of the last modification of the artifact.
	 */
	public Date getLastModificationDate() {
		return this.lastModificationDate;
	}

	/**
	 * Returns the id of the tracker on which the artifact has been created.
	 * 
	 * @return The id of the tracker on which the artifact has been created.
	 */
	public int getTrackerId() {
		return this.trackerId;
	}

	/**
	 * Returns the Tuleap artifact unique name.
	 * 
	 * @return The tuleap artifact unique name.
	 */
	public String getUniqueName() {
		if (this.trackerId != INVALID_ID && this.id != INVALID_ID) {
			return TuleapUtil.getTaskDataId(trackerId, this.id);
		}
		return null;
	}
}
