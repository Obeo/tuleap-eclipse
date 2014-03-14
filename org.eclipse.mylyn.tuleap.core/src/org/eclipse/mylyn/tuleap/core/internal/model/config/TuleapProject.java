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
package org.eclipse.mylyn.tuleap.core.internal.model.config;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.mylyn.tuleap.core.internal.model.data.TuleapReference;

/**
 * This class will hold a Tuleap project.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapProject implements Serializable {

	/**
	 * The generated serialization ID.
	 */
	private static final long serialVersionUID = -1057944606439051675L;

	/**
	 * The label of the project.
	 */
	private String label;

	/**
	 * The identifier of the project.
	 */
	private int id;

	/**
	 * The uri of the project.
	 */
	private String uri;

	/**
	 * The parent server.
	 */
	private TuleapServer server;

	/**
	 * References to the project resources.
	 */
	private TuleapResource[] resources;

	/**
	 * Map of trackers indexed by their IDs.
	 */
	private final Map<Integer, TuleapTracker> trackersById;

	/**
	 * Map of plannings indexed by their IDs.
	 */
	private final Map<Integer, TuleapPlanning> planningsById;

	/**
	 * User groups indexed by id.
	 */
	private final Map<String, TuleapUserGroup> userGroupsById;

	/**
	 * The constructor.
	 */
	public TuleapProject() {
		trackersById = Maps.newHashMap();
		planningsById = Maps.newHashMap();
		userGroupsById = Maps.newHashMap();
	}

	/**
	 * The constructor.
	 * 
	 * @param projectName
	 *            The label of the project
	 * @param projectIdentifier
	 *            The identifier of the project
	 */
	public TuleapProject(String projectName, int projectIdentifier) {
		this();
		this.label = projectName;
		this.id = projectIdentifier;
	}

	/**
	 * Adds the given tracker for the given id in the Tuleap server.
	 * 
	 * @param tracker
	 *            The tracker.
	 */
	public void addTracker(TuleapTracker tracker) {
		this.trackersById.put(Integer.valueOf(tracker.getIdentifier()), tracker);
		tracker.setProject(this);
	}

	/**
	 * Returns the tracker for the given tracker id.
	 * 
	 * @param trackerId
	 *            the id of the tracker
	 * @return The tracker for the given tracker id.
	 */
	public TuleapTracker getTracker(int trackerId) {
		return this.trackersById.get(Integer.valueOf(trackerId));
	}

	/**
	 * Returns the list of all the tracker.
	 * 
	 * @return The list of all the tracker.
	 */
	public List<TuleapTracker> getAllTrackers() {
		return new ArrayList<TuleapTracker>(this.trackersById.values());
	}

	/**
	 * Add a planning to this project.
	 * 
	 * @param planning
	 *            The planning to add.
	 */
	public void addPlanning(TuleapPlanning planning) {
		planningsById.put(planning.getId(), planning);
	}

	/**
	 * Get a planning by its ID.
	 * 
	 * @param planningId
	 *            The id of the planning being looked for.
	 * @return The planning with this ID, or null if it is not registered with this project.
	 */
	public TuleapPlanning getPlanning(int planningId) {
		return planningsById.get(Integer.valueOf(planningId));
	}

	/**
	 * Indicates whether the given id is the id of a tracker used to persist milestone fields.
	 * 
	 * @param trackerId
	 *            The tracker id.
	 * @return <code>true</code> if and only if there is a planning whose milestone tracker id equals the
	 *         given id.
	 */
	public boolean isMilestoneTracker(int trackerId) {
		boolean ret = false;
		for (TuleapPlanning planning : planningsById.values()) {
			TuleapReference milestoneTracker = planning.getMilestoneTracker();
			if (milestoneTracker != null && milestoneTracker.getId() == trackerId) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	/**
	 * Indicates whether the given id is the id of a tracker used to persist backlog item fields.
	 * 
	 * @param trackerId
	 *            The tracker id.
	 * @return <code>true</code> if and only if there is a planning whose backlog tracker id equals the given
	 *         id.
	 */
	public boolean isBacklogTracker(int trackerId) {
		boolean ret = false;
		loop: for (TuleapPlanning planning : planningsById.values()) {
			TuleapReference[] backlogTrackers = planning.getBacklogTrackers();
			if (backlogTrackers == null) {
				continue;
			}
			for (TuleapReference ref : backlogTrackers) {
				if (ref.getId() == trackerId) {
					ret = true;
					break loop;
				}
			}
		}
		return ret;
	}

	/**
	 * Indicates whether a cardwall is active for the given tracker.
	 * 
	 * @param trackerId
	 *            id of the tracker.
	 * @return <code>true</code> if and only if the given ID is the ID of a milestone tracker, and the
	 *         cardwall flag is active for the planning whose milestone tracker is this tracker.
	 */
	public boolean isCardwallActive(int trackerId) {
		boolean ret = false;
		for (TuleapPlanning planning : planningsById.values()) {
			TuleapReference milestoneTracker = planning.getMilestoneTracker();
			if (milestoneTracker != null && milestoneTracker.getId() == trackerId) {
				ret = planning.isCardwallActive();
				break;
			}
		}
		return ret;
	}

	/**
	 * Returns the label of the project.
	 * 
	 * @return The label of the project
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Returns the uri of the project.
	 * 
	 * @return The uri of the project
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Project uri setter.
	 * 
	 * @param uri
	 *            The project uri
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * Returns the identifier of the project.
	 * 
	 * @return The identifier of the project
	 */
	public int getIdentifier() {
		return id;
	}

	/**
	 * Registers a user group with this project.
	 * 
	 * @param group
	 *            User group to register with this project. Must not be {@code null}.
	 */
	public void addGroup(TuleapUserGroup group) {
		userGroupsById.put(group.getId(), group);
	}

	/**
	 * Provides the group with the given id, or {@code null} if the given id matches no registered group of
	 * this project.
	 * 
	 * @param groupId
	 *            The id of the group being looked for.
	 * @return The group with the given id if it is registered with this project, {@code null} otherwise.
	 */
	public TuleapUserGroup getUserGroup(String groupId) {
		return userGroupsById.get(groupId);
	}

	/**
	 * Provides all the registered groups of this project.
	 * 
	 * @return An unmodifiable view of the user groups registered with this project.
	 */
	public Collection<TuleapUserGroup> getAllUserGroups() {
		return Collections.unmodifiableCollection(userGroupsById.values());
	}

	/**
	 * Adds a user to a user group.
	 * 
	 * @param group
	 *            The user group.
	 * @param member
	 *            The user to add to the group.
	 */
	public void addUserToUserGroup(TuleapUserGroup group, TuleapUser member) {
		Assert.isNotNull(group);
		Assert.isNotNull(member);
		TuleapUserGroup groupToUse;
		if (userGroupsById.containsKey(group.getId())) {
			groupToUse = userGroupsById.get(group.getId());
		} else {
			userGroupsById.put(group.getId(), group);
			groupToUse = group;
		}
		groupToUse.addMember(member);
		server.register(member);
	}

	/**
	 * The parent server setter.
	 * 
	 * @param server
	 *            The parent server.
	 */
	protected void setServer(TuleapServer server) {
		this.server = server;
	}

	/**
	 * The parent server.
	 * 
	 * @return The parent server.
	 */
	public TuleapServer getServer() {
		return server;
	}

	/**
	 * Project resources getter.
	 * 
	 * @return the projectResources, a list that is never <code>null</code> but possibly empty.
	 */
	public TuleapResource[] getProjectResources() {
		return resources;
	}

	/**
	 * Project resources setter.
	 * 
	 * @param projectResources
	 *            the projectResources to set
	 */
	public void setProjectResources(TuleapResource[] projectResources) {
		this.resources = projectResources;
	}

	/**
	 * Indicates whether the given resource exists on this project.
	 * 
	 * @param key
	 *            The resource type being looked for
	 * @return {@code true} if and only if the given service is present in the list of services of this
	 *         project.
	 */
	public boolean hasResource(String key) {
		if (resources != null) {
			for (TuleapResource resource : resources) {
				if (resource.getType().equals(key)) {
					return true;
				}
			}
		}
		return false;
	}

}
