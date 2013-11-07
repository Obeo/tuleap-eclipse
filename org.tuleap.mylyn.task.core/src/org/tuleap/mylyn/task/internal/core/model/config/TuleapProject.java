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
package org.tuleap.mylyn.task.internal.core.model.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;

/**
 * This class will hold the configuration of a Tuleap project.
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
	private String name;

	/**
	 * The identifier of the project.
	 */
	private int identifier;

	/**
	 * The parent server configuration.
	 */
	private TuleapServer server;

	/**
	 * The list of active services for this project.
	 */
	private final List<String> services = Lists.newArrayList();

	/**
	 * Map of trackers indexed by their IDs.
	 */
	private final Map<Integer, TuleapTracker> trackersById = Maps.newHashMap();

	/**
	 * Map of plannings indexed by their IDs.
	 */
	private final Map<Integer, TuleapPlanning> planningsById = Maps.newHashMap();

	/**
	 * User groups indexed by id.
	 */
	private final Map<Integer, TuleapGroup> groupsById = Maps.newHashMap();

	/**
	 * The constructor.
	 * 
	 * @param projectName
	 *            The label of the project
	 * @param projectIdentifier
	 *            The identifier of the project
	 */
	public TuleapProject(String projectName, int projectIdentifier) {
		this.name = projectName;
		this.identifier = projectIdentifier;
	}

	/**
	 * Adds the given tracker configuration for the given tracker id in the Tuleap instance configuration.
	 * 
	 * @param trackerConfiguration
	 *            The configuration of the tracker.
	 */
	public void addTracker(TuleapTracker trackerConfiguration) {
		this.trackersById.put(Integer.valueOf(trackerConfiguration.getIdentifier()), trackerConfiguration);
		trackerConfiguration.setTuleapProjectConfiguration(this);
	}

	/**
	 * Returns the tracker configuration for the given tracker id.
	 * 
	 * @param trackerId
	 *            the id of the tracker
	 * @return The tracker configuration for the given tracker id.
	 */
	public TuleapTracker getTracker(int trackerId) {
		return this.trackersById.get(Integer.valueOf(trackerId));
	}

	/**
	 * Returns the list of all the tracker configurations.
	 * 
	 * @return The list of all the tracker configurations.
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
		planningsById.put(Integer.valueOf(planning.getId()), planning);
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
			List<TuleapReference> backlogTrackers = planning.getBacklogTrackers();
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
	public String getName() {
		return name;
	}

	/**
	 * Returns the identifier of the project.
	 * 
	 * @return The identifier of the project
	 */
	public int getIdentifier() {
		return identifier;
	}

	/**
	 * Add the given service.
	 * 
	 * @param service
	 *            The service to add.
	 */
	public void addService(String service) {
		services.add(service);
	}

	/**
	 * Indicates whether the given service is active for thie project.
	 * 
	 * @param service
	 *            The service being looked for
	 * @return {@code true} if and only if the given service is present in the list of services of this
	 *         project.
	 */
	public boolean hasService(String service) {
		return services.contains(service);
	}

	/**
	 * Registers a user group with this project configuration.
	 * 
	 * @param group
	 *            User group to register with this project. Must not be {@code null}.
	 */
	public void addGroup(TuleapGroup group) {
		groupsById.put(Integer.valueOf(group.getId()), group);
	}

	/**
	 * Provides the group with the given id, or {@code null} if the given id matches no registered group of
	 * this project.
	 * 
	 * @param groupId
	 *            The id of the group being looked for.
	 * @return The group with the given id if it is registered with this project, {@code null} otherwise.
	 */
	public TuleapGroup getGroup(int groupId) {
		return groupsById.get(Integer.valueOf(groupId));
	}

	/**
	 * Provides all the registered groups of this project.
	 * 
	 * @return An unmodifiable view of the user groups registered with this project.
	 */
	public Collection<TuleapGroup> getAllGroups() {
		return Collections.unmodifiableCollection(groupsById.values());
	}

	/**
	 * Adds a user to a user group.
	 * 
	 * @param group
	 *            The user group.
	 * @param member
	 *            The user to add to the group.
	 */
	public void addUserToUserGroup(TuleapGroup group, TuleapPerson member) {
		Assert.isNotNull(group);
		Assert.isNotNull(member);
		TuleapGroup groupToUse;
		if (groupsById.containsKey(Integer.valueOf(group.getId()))) {
			groupToUse = groupsById.get(Integer.valueOf(group.getId()));
		} else {
			groupsById.put(Integer.valueOf(group.getId()), group);
			groupToUse = group;
		}
		groupToUse.addMember(member);
		server.register(member);
	}

	/**
	 * The parent server configuration setter.
	 * 
	 * @param server
	 *            The parent server configuration.
	 */
	protected void setServer(TuleapServer server) {
		this.server = server;
	}

	/**
	 * The parent server configuration.
	 * 
	 * @return The parent server configuration.
	 */
	public TuleapServer getServer() {
		return server;
	}
}
