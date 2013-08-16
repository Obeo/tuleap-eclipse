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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private Map<Integer, TuleapTrackerConfiguration> trackerId2trackerConfiguration = new HashMap<Integer, TuleapTrackerConfiguration>();

	/**
	 * The name of the project.
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
	 * The constructor.
	 * 
	 * @param projectName
	 *            The name of the project
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
		this.trackerId2trackerConfiguration.put(Integer.valueOf(trackerConfiguration.getTrackerId()),
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
	 * Returns the name of the project.
	 * 
	 * @return The name of the project
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
}
