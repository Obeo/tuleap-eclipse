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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class will hold the configuration of a Tuleap instance.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapInstanceConfiguration implements Serializable {

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
	 * This map contains the ID of the tracker of the Tuleap instance and their matching configuration.
	 */
	private Map<Integer, TuleapTrackerConfiguration> trackerId2trackerConfiguration = new HashMap<Integer, TuleapTrackerConfiguration>();

	/**
	 * The constructor.
	 * 
	 * @param tuleapInstanceUrl
	 *            The url of the Tuleap instance.
	 */
	public TuleapInstanceConfiguration(String tuleapInstanceUrl) {
		this.url = tuleapInstanceUrl;
	}

	/**
	 * Adds the given tracker configuration for the given tracker id in the Tuleap instance configuration.
	 * 
	 * @param trackerId
	 *            The id of the tracker.
	 * @param tuleapTrackerConfiguration
	 *            The configuration of the tracker.
	 */
	public void addTracker(Integer trackerId, TuleapTrackerConfiguration tuleapTrackerConfiguration) {
		this.trackerId2trackerConfiguration.put(trackerId, tuleapTrackerConfiguration);
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
	 * Returns the list of all the tracker configurations.
	 * 
	 * @return The list of all the tracker configurations.
	 */
	public List<TuleapTrackerConfiguration> getAllTrackerConfigurations() {
		return new ArrayList<TuleapTrackerConfiguration>(this.trackerId2trackerConfiguration.values());
	}

}
