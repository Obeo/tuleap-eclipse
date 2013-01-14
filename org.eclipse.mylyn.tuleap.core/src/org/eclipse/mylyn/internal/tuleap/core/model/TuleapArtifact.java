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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
	private HashMap<String, List<String>> properties = new HashMap<String, List<String>>();

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
	 * The name of the project.
	 */
	private String projectName;

	/**
	 * The name of the containing tracker.
	 */
	private String trackerName;

	/**
	 * The comments of the artifact.
	 */
	private List<TuleapArtifactComment> comments = new ArrayList<TuleapArtifactComment>();

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
	 * @param tId
	 *            The id of the tracker
	 * @param tName
	 *            The name of the tracker
	 * @param pName
	 *            The name of the project
	 */
	public TuleapArtifact(int artifactId, int tId, String tName, String pName) {
		this.id = artifactId;
		this.trackerId = tId;
		this.trackerName = tName;
		this.projectName = pName;
	}

	/**
	 * The constructor used to create a Tuleap artifact from a real artifact.
	 * 
	 * @param artifact
	 *            The artifact
	 * @param tName
	 *            The name of the tracker
	 * @param pName
	 *            The name of the project
	 */
	public TuleapArtifact(Artifact artifact, String tName, String pName) {
		this.id = artifact.getArtifact_id();
		this.trackerId = artifact.getTracker_id();
		this.trackerName = tName;
		this.projectName = pName;

		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(Long.valueOf(artifact.getSubmitted_on()).longValue() * 1000);
			this.creationDate = calendar.getTime();
		} catch (NumberFormatException e) {
			// Empty date
		}

		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(Long.valueOf(artifact.getLast_update_date()).longValue() * 1000);
			this.lastModificationDate = calendar.getTime();
		} catch (NumberFormatException e) {
			// Empty date
		}
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
			List<String> values = this.properties.get(keyName);
			if (values == null) {
				values = new ArrayList<String>();
			}
			values.add(value);
			this.properties.put(keyName, values);
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
		if (this.properties.get(key) != null) {
			return this.properties.get(key).get(0);
		}
		return null;
	}

	/**
	 * Returns the value of the attribute with the given key.
	 * 
	 * @param key
	 *            The key of the attribute
	 * @return The value of the attribute with the given key.
	 */
	public List<String> getValues(String key) {
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
	 * Sets the new value of the tracker id.
	 * 
	 * @param newTrackerId
	 *            The new value of the tracker id.
	 */
	public void setTrackerId(int newTrackerId) {
		this.trackerId = newTrackerId;
	}

	/**
	 * Returns the Tuleap artifact unique name.
	 * 
	 * @return The tuleap artifact unique name.
	 */
	public String getUniqueName() {
		if (this.trackerId != INVALID_ID && this.id != INVALID_ID) {
			return TuleapUtil.getTaskDataId(this.projectName, TuleapUtil.getTrackerId(this.trackerName,
					this.trackerId), this.id);
		}
		return null;
	}

	/**
	 * Sets the name of the project containing the artifact.
	 * 
	 * @param name
	 *            The name of the project containing the artifact
	 */
	public void setProjectName(String name) {
		this.projectName = name;
	}

	/**
	 * Sets the name of the tracker containing the artifact.
	 * 
	 * @param name
	 *            The name of the tracker containing the artifact.
	 */
	public void setTrackerName(String name) {
		this.trackerName = name;
	}

	/**
	 * Adds a comment to the artifact.
	 * 
	 * @param artifactComment
	 *            The comment to add.
	 */
	public void addComment(TuleapArtifactComment artifactComment) {
		this.comments.add(artifactComment);
	}

	/**
	 * Returns the comments of the artifact.
	 * 
	 * @return the comments of the artifact.
	 */
	public List<TuleapArtifactComment> getComments() {
		return this.comments;
	}
}
