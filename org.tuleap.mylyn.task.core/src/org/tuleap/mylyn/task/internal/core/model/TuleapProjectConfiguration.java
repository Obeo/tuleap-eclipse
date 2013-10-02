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
package org.tuleap.mylyn.task.internal.core.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItemType;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapCardType;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestoneType;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;

/**
 * This class will hold the configuration of a Tuleap project.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapProjectConfiguration implements Serializable {

	/**
	 * The generated serialization ID.
	 */
	private static final long serialVersionUID = -1057944606439051675L;

	/**
	 * This map contains the ID of the tracker of the Tuleap instance and their matching configuration.
	 */
	private Map<Integer, TuleapTrackerConfiguration> trackerId2trackerConfiguration = Maps.newHashMap();

	/**
	 * Map of milestone types (i.e. configuration) by id.
	 */
	private Map<Integer, TuleapMilestoneType> milestoneTypesById = Maps.newHashMap();

	/**
	 * Map of backlogItem types (i.e. configuration) by id.
	 */
	private Map<Integer, TuleapBacklogItemType> backlogItemTypesById = Maps.newHashMap();

	/**
	 * Map of card types (i.e. configuration) by id.
	 */
	private Map<Integer, TuleapCardType> cardTypesById = Maps.newHashMap();

	/**
	 * The label of the project.
	 */
	private String name;

	/**
	 * The identifier of the project.
	 */
	private int identifier;

	/**
	 * The list of active services for this project.
	 */
	private final List<String> services = Lists.newArrayList();

	/**
	 * User groups indexed by id.
	 */
	private final Map<Integer, TuleapGroup> groupsById = Maps.newHashMap();

	/**
	 * The parent server configuration.
	 */
	private TuleapServerConfiguration serverConfiguration;

	/**
	 * The constructor.
	 * 
	 * @param projectName
	 *            The label of the project
	 * @param projectIdentifier
	 *            The identifier of the project
	 */
	public TuleapProjectConfiguration(String projectName, int projectIdentifier) {
		this.name = projectName;
		this.identifier = projectIdentifier;
	}

	/**
	 * Adds the given tracker configuration for the given tracker id in the Tuleap instance configuration.
	 * 
	 * @param trackerConfiguration
	 *            The configuration of the tracker.
	 */
	public void addTracker(TuleapTrackerConfiguration trackerConfiguration) {
		this.trackerId2trackerConfiguration.put(Integer.valueOf(trackerConfiguration.identifier),
				trackerConfiguration);
		trackerConfiguration.setTuleapProjectConfiguration(this);
	}

	/**
	 * Returns the tracker configuration for the given tracker id.
	 * 
	 * @param trackerId
	 *            the id of the tracker
	 * @return The tracker configuration for the given tracker id.
	 */
	public TuleapTrackerConfiguration getTrackerConfiguration(int trackerId) {
		return this.trackerId2trackerConfiguration.get(Integer.valueOf(trackerId));
	}

	/**
	 * Returns the list of all the tracker configurations.
	 * 
	 * @return The list of all the tracker configurations.
	 */
	public List<TuleapTrackerConfiguration> getAllTrackerConfigurations() {
		return new ArrayList<TuleapTrackerConfiguration>(this.trackerId2trackerConfiguration.values());
	}

	/**
	 * Adds the given milestone type in the project configuration.
	 * 
	 * @param milestoneType
	 *            The configuration of a milestone type.
	 */
	public void addMilestoneType(TuleapMilestoneType milestoneType) {
		this.milestoneTypesById.put(Integer.valueOf(milestoneType.identifier), milestoneType);
		milestoneType.setTuleapProjectConfiguration(this);
	}

	/**
	 * Returns the Milestone type configuration for the given milestone type id.
	 * 
	 * @param milestoneTypeId
	 *            the id of the milestone type
	 * @return The Milestone type configuration for the given milestone type id.
	 */
	public TuleapMilestoneType getMilestoneType(int milestoneTypeId) {
		return this.milestoneTypesById.get(Integer.valueOf(milestoneTypeId));
	}

	/**
	 * Returns the list of all the milestone type for this project.
	 * 
	 * @return The list of all the milestone type for this project.
	 */
	public List<TuleapMilestoneType> getAllMilestoneTypes() {
		return new ArrayList<TuleapMilestoneType>(this.milestoneTypesById.values());
	}

	/**
	 * Adds the given backlogItem type in the project configuration.
	 * 
	 * @param backlogItemType
	 *            The configuration of a backlogItem type.
	 */
	public void addBacklogItemType(TuleapBacklogItemType backlogItemType) {
		this.backlogItemTypesById.put(Integer.valueOf(backlogItemType.identifier), backlogItemType);
		backlogItemType.setTuleapProjectConfiguration(this);
	}

	/**
	 * Returns the BacklogItem type configuration for the given backlogItem type id.
	 * 
	 * @param backlogItemTypeId
	 *            the id of the backlogItem type
	 * @return The BacklogItem type configuration for the given backlogItem type id.
	 */
	public TuleapBacklogItemType getBacklogItemType(int backlogItemTypeId) {
		return this.backlogItemTypesById.get(Integer.valueOf(backlogItemTypeId));
	}

	/**
	 * Returns the list of all the backlogItem type for this project.
	 * 
	 * @return The list of all the backlogItem type for this project.
	 */
	public List<TuleapBacklogItemType> getAllBacklogItemTypes() {
		return new ArrayList<TuleapBacklogItemType>(this.backlogItemTypesById.values());
	}

	/**
	 * Adds the given card type in the project configuration.
	 * 
	 * @param cardType
	 *            The configuration of a card type.
	 */
	public void addCardType(TuleapCardType cardType) {
		this.cardTypesById.put(Integer.valueOf(cardType.identifier), cardType);
		cardType.setTuleapProjectConfiguration(this);
	}

	/**
	 * Returns the Card type configuration for the given card type id.
	 * 
	 * @param cardTypeId
	 *            the id of the card type
	 * @return The Card type configuration for the given card type id.
	 */
	public TuleapCardType getCardType(int cardTypeId) {
		return this.cardTypesById.get(Integer.valueOf(cardTypeId));
	}

	/**
	 * Returns the list of all the card type for this project.
	 * 
	 * @return The list of all the card type for this project.
	 */
	public List<TuleapCardType> getAllCardTypes() {
		return new ArrayList<TuleapCardType>(this.cardTypesById.values());
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
		serverConfiguration.register(member);
	}

	/**
	 * The parent server configuration setter.
	 * 
	 * @param serverConfiguration
	 *            The parent server configuration.
	 */
	protected void setServerConfiguration(TuleapServerConfiguration serverConfiguration) {
		this.serverConfiguration = serverConfiguration;
	}

	/**
	 * The parent server configuration.
	 * 
	 * @return The parent server configuration.
	 */
	public TuleapServerConfiguration getServerConfiguration() {
		return serverConfiguration;
	}

	/**
	 * Returns the {@link AbstractTuleapConfigurableFieldsConfiguration} with the given identifier, or
	 * <code>null</code> if none can be found.
	 * 
	 * @param configurationId
	 *            The identifier of the configuration
	 * @return The {@link AbstractTuleapConfigurableFieldsConfiguration} with the given identifier, or
	 *         <code>null</code> if none can be found
	 */
	public AbstractTuleapConfigurableFieldsConfiguration getConfigurableFieldsConfiguration(
			int configurationId) {
		AbstractTuleapConfigurableFieldsConfiguration configuration = null;

		Collection<TuleapTrackerConfiguration> trackerConfiguration = this.trackerId2trackerConfiguration
				.values();
		for (TuleapTrackerConfiguration tuleapTrackerConfiguration : trackerConfiguration) {
			if (tuleapTrackerConfiguration.getIdentifier() == configurationId) {
				configuration = tuleapTrackerConfiguration;
			}
		}

		if (configuration == null) {
			Collection<TuleapMilestoneType> milestoneTypes = this.milestoneTypesById.values();
			for (TuleapMilestoneType tuleapMilestoneType : milestoneTypes) {
				if (tuleapMilestoneType.getIdentifier() == configurationId) {
					configuration = tuleapMilestoneType;
				}
			}
		}

		if (configuration == null) {
			Collection<TuleapBacklogItemType> backlogItemTypes = this.backlogItemTypesById.values();
			for (TuleapBacklogItemType tuleapBacklogItemType : backlogItemTypes) {
				if (tuleapBacklogItemType.getIdentifier() == configurationId) {
					configuration = tuleapBacklogItemType;
				}
			}
		}

		return configuration;
	}
}
