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
package org.tuleap.mylyn.task.internal.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class will hold the configuration of a Tuleap server instance.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapServerConfiguration implements Serializable {

	/**
	 * The generated serialization ID.
	 */
	private static final long serialVersionUID = 2416708654638468973L;

	/**
	 * The url of the tuleap instance.
	 */
	private String url;

	/**
	 * The last time this configuration was updated.
	 */
	private long lastUpdate;

	/**
	 * This map contains the ID of the projects of the Tuleap instance and their matching configuration.
	 */
	private Map<Integer, TuleapProjectConfiguration> projectId2projectConfiguration = new HashMap<Integer, TuleapProjectConfiguration>();

	/**
	 * The constructor.
	 * 
	 * @param tuleapInstanceUrl
	 *            The url of the Tuleap instance.
	 */
	public TuleapServerConfiguration(String tuleapInstanceUrl) {
		this.url = tuleapInstanceUrl;
	}

	/**
	 * Adds the given project configuration for the given project id in the Tuleap instance configuration.
	 * 
	 * @param tuleapProjectConfiguration
	 *            The configuration of the project (must not be null).
	 */
	public void addProject(TuleapProjectConfiguration tuleapProjectConfiguration) {
		this.projectId2projectConfiguration.put(Integer.valueOf(tuleapProjectConfiguration.getIdentifier()),
				tuleapProjectConfiguration);
	}

	/**
	 * Returns the project configuration for the given project id.
	 * 
	 * @param projectId
	 *            the id of the project
	 * @return The project configuration for the given project id.
	 */
	public TuleapProjectConfiguration getProjectConfiguration(int projectId) {
		return this.projectId2projectConfiguration.get(Integer.valueOf(projectId));
	}

	/**
	 * Returns the url of the tuleap instance.
	 * 
	 * @return The url of the tuleap instance.
	 */
	public String getUrl() {
		return this.url;
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
	 * Returns the list of all the project configurations.
	 * 
	 * @return The list of all the project configurations.
	 */
	public List<TuleapProjectConfiguration> getAllProjectConfigurations() {
		return new ArrayList<TuleapProjectConfiguration>(this.projectId2projectConfiguration.values());
	}

	/**
	 * Iterates on all the projects of the configuration and returns the tracker configuration with the given
	 * identifier or null if none can be found.
	 * 
	 * @param trackerId
	 *            The identifier of the tracker
	 * @return The configuration of the tracker with the given identifier or null if none can be found
	 */
	public TuleapTrackerConfiguration getTrackerConfiguration(int trackerId) {
		for (TuleapProjectConfiguration tuleapProjectConfiguration : this.projectId2projectConfiguration
				.values()) {
			TuleapTrackerConfiguration trackerConfiguration = tuleapProjectConfiguration
					.getTrackerConfiguration(trackerId);
			if (trackerConfiguration != null) {
				return trackerConfiguration;
			}
		}
		return null;
	}

}
