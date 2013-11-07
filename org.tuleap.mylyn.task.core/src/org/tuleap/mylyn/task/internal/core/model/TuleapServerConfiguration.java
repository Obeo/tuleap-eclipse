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

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTracker;

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
	 * Users indexed by id.
	 */
	private final Map<Integer, TuleapPerson> personsById = Maps.newHashMap();

	/**
	 * This map contains the ID of the projects of the Tuleap instance and their matching configuration.
	 */
	private Map<Integer, TuleapProjectConfiguration> projectById = new HashMap<Integer, TuleapProjectConfiguration>();

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
		this.projectById.put(Integer.valueOf(tuleapProjectConfiguration.getIdentifier()),
				tuleapProjectConfiguration);
		tuleapProjectConfiguration.setServerConfiguration(this);
	}

	/**
	 * Returns the project configuration for the given project id.
	 * 
	 * @param projectId
	 *            the id of the project
	 * @return The project configuration for the given project id.
	 */
	public TuleapProjectConfiguration getProjectConfiguration(int projectId) {
		return this.projectById.get(Integer.valueOf(projectId));
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
		return new ArrayList<TuleapProjectConfiguration>(this.projectById.values());
	}

	/**
	 * Iterates on all the projects of the configuration and returns the tracker configuration with the given
	 * identifier or null if none can be found.
	 * 
	 * @param trackerId
	 *            The identifier of the tracker
	 * @return The configuration of the tracker with the given identifier or null if none can be found
	 */
	public TuleapTracker getTrackerConfiguration(int trackerId) {
		for (TuleapProjectConfiguration tuleapProjectConfiguration : this.projectById.values()) {
			TuleapTracker trackerConfiguration = tuleapProjectConfiguration
					.getTrackerConfiguration(trackerId);
			if (trackerConfiguration != null) {
				return trackerConfiguration;
			}
		}
		return null;
	}

	/**
	 * Replace an existing configuration in the project with the given project identifier by the newly
	 * provided version of the configuration.
	 * 
	 * @param projectId
	 *            The project identifier
	 * @param tracker
	 *            The new version of the tracker
	 */
	public void replaceConfiguration(int projectId, TuleapTracker tracker) {
		TuleapProjectConfiguration projectConfiguration = this.projectById.get(Integer.valueOf(projectId));
		if (projectConfiguration != null) {
			projectConfiguration.addTracker(tracker);
		}
	}

	/**
	 * Retrieve a registered user by id.
	 * 
	 * @param id
	 *            the user id.
	 * @return The person registered for this id, or null if none is found.
	 */
	public TuleapPerson getUser(int id) {
		return personsById.get(Integer.valueOf(id));
	}

	/**
	 * Indicates whether a user is registered.
	 * 
	 * @param id
	 *            the user id.
	 * @return {@code true} if and only if this id matches a registered user.
	 */
	public boolean isRegistered(int id) {
		return personsById.containsKey(Integer.valueOf(id));
	}

	/**
	 * Register a user.
	 * 
	 * @param person
	 *            The user.
	 */
	public void register(TuleapPerson person) {
		if (person != null) {
			personsById.put(Integer.valueOf(person.getId()), person);
		}
	}

}
