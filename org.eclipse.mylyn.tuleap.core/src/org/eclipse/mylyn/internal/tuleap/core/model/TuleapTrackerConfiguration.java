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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The repository configuration will hold the latest known configuration of a given repository. This
 * configuration can dramatically evolve over time and it should be refreshed automatically or manually.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapTrackerConfiguration implements Serializable {
	/**
	 * The serialization version ID.
	 */
	private static final long serialVersionUID = 1132263181253677627L;

	/**
	 * The last time this configuration was updated.
	 */
	private long lastUpdate;

	/**
	 * The url of the repository configuration.
	 */
	private String url;

	/**
	 * The name of the Tuleap tracker.
	 */
	private String name;

	/**
	 * The item name.
	 */
	private String itemName;

	/**
	 * The description of the Tuleap tracker.
	 */
	private String description;

	/**
	 * The list of canned responses.
	 */
	private List<TuleapCannedResponse> cannedResponses = new ArrayList<TuleapCannedResponse>();

	/**
	 * The fields of the Tuleap tracker.
	 */
	private List<AbstractTuleapField> fields = new ArrayList<AbstractTuleapField>();

	/**
	 * The identifier of the tracker.
	 */
	private int trackerId;

	/**
	 * The default constructor.
	 * 
	 * @param trackerIdentifier
	 *            The id of the tracker.
	 * @param repositoryURL
	 *            The URL of the repository.
	 */
	public TuleapTrackerConfiguration(int trackerIdentifier, String repositoryURL) {
		this.trackerId = trackerIdentifier;
		this.url = repositoryURL;
	}

	/**
	 * The constructor.
	 * 
	 * @param repositoryURL
	 *            The URL of the repository.
	 * @param repositoryName
	 *            The name of the repository
	 * @param repositoryItemName
	 *            The item name of the repository
	 * @param repositoryDescription
	 *            The description of the repository
	 */
	public TuleapTrackerConfiguration(String repositoryURL, String repositoryName, String repositoryItemName,
			String repositoryDescription) {
		this.url = repositoryURL;
		this.name = repositoryName;
		this.itemName = repositoryItemName;
	}

	/**
	 * Set the time at which the configuration was last updated.
	 * 
	 * @param time
	 *            The time at which the configuration was last updated.
	 */
	public void setLastUpdate(long time) {
		this.lastUpdate = time;
	}

	/**
	 * Returns the time at which the configuration was last updated.
	 * 
	 * @return The time at which the configuration was last updated.
	 */
	public long getLastUpdate() {
		return this.lastUpdate;
	}

	/**
	 * Returns the url of the repository.
	 * 
	 * @return The url of the repository.
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Returns the name of the repository.
	 * 
	 * @return The name of the repository.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name of the configuration.
	 * 
	 * @param configurationName
	 *            The name of the configuration.
	 */
	public void setName(String configurationName) {
		this.name = configurationName;
	}

	/**
	 * Returns the item name of the repository.
	 * 
	 * @return The item name of the repository.
	 */
	public String getItemName() {
		return this.itemName;
	}

	/**
	 * Sets the item name of the repository.
	 * 
	 * @param repositoryItemName
	 *            The item name of the repository.
	 */
	public void setItemName(String repositoryItemName) {
		this.itemName = repositoryItemName;
	}

	/**
	 * Returns the description of the repository.
	 * 
	 * @return The description of the repository.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the description of the repository.
	 * 
	 * @param repositoryDescription
	 *            The description of the repository.
	 */
	public void setDescription(String repositoryDescription) {
		this.description = repositoryDescription;
	}

	/**
	 * Returns the fields of the tracker.
	 * 
	 * @return The fields of the tracker.
	 */
	public List<AbstractTuleapField> getFields() {
		return this.fields;
	}

	/**
	 * Returns the list of the canned responses.
	 * 
	 * @return The list of the canned respones.
	 */
	public List<TuleapCannedResponse> getCannedResponses() {
		return this.cannedResponses;
	}

	/**
	 * Returns a qualified name composed of the name and the identifier.
	 * 
	 * @return A qualified name composed of the name and the identifier.
	 */
	public String getQualifiedName() {
		return this.name + " [" + this.trackerId + ']'; //$NON-NLS-1$
	}

	/**
	 * Returns the tracker id.
	 * 
	 * @return The tracker id.
	 */
	public int getTrackerId() {
		return this.trackerId;
	}
}
