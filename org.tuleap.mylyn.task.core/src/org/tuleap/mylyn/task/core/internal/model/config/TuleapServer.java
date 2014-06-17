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
package org.tuleap.mylyn.task.core.internal.model.config;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class will hold the configuration of a Tuleap server instance.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapServer implements Serializable {

	/**
	 * The generated serialization ID.
	 */
	private static final long serialVersionUID = 2416708654638468973L;

	/**
	 * The url of the tuleap instance.
	 */
	private String url;

	/**
	 * The last time this server was updated.
	 */
	private long lastUpdate;

	/**
	 * Users indexed by id.
	 */
	private final Map<Integer, TuleapUser> personsById = Maps.newHashMap();

	/**
	 * This map contains the ID of the projects of the Tuleap instance and their matching configuration.
	 */
	private Map<Integer, TuleapProject> projectsById = new HashMap<Integer, TuleapProject>();

	/**
	 * The constructor.
	 *
	 * @param tuleapInstanceUrl
	 *            The url of the Tuleap instance.
	 */
	public TuleapServer(String tuleapInstanceUrl) {
		this.url = tuleapInstanceUrl;
	}

	/**
	 * Adds the given project for the given project id in the Tuleap instance configuration.
	 *
	 * @param tuleapProject
	 *            The project (must not be null).
	 */
	public void addProject(TuleapProject tuleapProject) {
		this.projectsById.put(Integer.valueOf(tuleapProject.getIdentifier()), tuleapProject);
		tuleapProject.setServer(this);
	}

	/**
	 * Returns the project for the given project id.
	 *
	 * @param projectId
	 *            the id of the project
	 * @return The project for the given project id.
	 */
	public TuleapProject getProject(int projectId) {
		return this.projectsById.get(Integer.valueOf(projectId));
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
	 * Set the time at which the server was last updated.
	 *
	 * @param time
	 *            The time at which the server was last updated.
	 */
	public void setLastUpdate(long time) {
		this.lastUpdate = time;
	}

	/**
	 * Returns the time at which the server was last updated.
	 *
	 * @return The time at which the server was last updated.
	 */
	public long getLastUpdate() {
		return this.lastUpdate;
	}

	/**
	 * Returns the list of all the projects.
	 *
	 * @return The list of all the projects.
	 */
	public List<TuleapProject> getAllProjects() {
		return new ArrayList<TuleapProject>(this.projectsById.values());
	}

	/**
	 * Iterates on all the projects of the server and returns the tracker with the given identifier or null if
	 * none can be found.
	 *
	 * @param trackerId
	 *            The identifier of the tracker
	 * @return The tracker with the given identifier or null if none can be found
	 */
	public TuleapTracker getTracker(int trackerId) {
		for (TuleapProject tuleapProject : this.projectsById.values()) {
			TuleapTracker tracker = tuleapProject.getTracker(trackerId);
			if (tracker != null) {
				return tracker;
			}
		}
		return null;
	}

	/**
	 * Replace an existing tracker in the project with the given project identifier by the newly provided
	 * version of the tracker.
	 * 
	 * @param projectId
	 *            The project identifier
	 * @param tracker
	 *            The new version of the tracker
	 */
	public void replaceTracker(int projectId, TuleapTracker tracker) {
		TuleapProject project = this.projectsById.get(Integer.valueOf(projectId));
		if (project != null) {
			project.addTracker(tracker);
		}
	}

	/**
	 * Retrieve a registered user by id.
	 *
	 * @param id
	 *            the user id.
	 * @return The person registered for this id, or null if none is found.
	 */
	public TuleapUser getUser(int id) {
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
	 * Register a user. If the given user has no updatedOn info, it will be set to the current time.
	 *
	 * @param person
	 *            The user, whose updatedOn date will be set after calling register(), even if it was null
	 *            before.
	 */
	public void register(TuleapUser person) {
		if (person != null) {
			if (person.getUpdatedOn() == null) {
				person.setUpdatedOn(new Date());
			}
			personsById.put(Integer.valueOf(person.getId()), person);
		}
	}

}
