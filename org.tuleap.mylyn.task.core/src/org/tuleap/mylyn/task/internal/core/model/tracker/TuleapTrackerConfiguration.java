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
package org.tuleap.mylyn.task.internal.core.model.tracker;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapFieldContainerConfig;

/**
 * The repository configuration will hold the latest known configuration of a given repository. This
 * configuration can dramatically evolve over time and it should be refreshed automatically or manually.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapTrackerConfiguration extends AbstractTuleapFieldContainerConfig {
	/**
	 * The serialization version ID.
	 */
	private static final long serialVersionUID = 1132263181253677627L;

	/**
	 * The tracker's parent.
	 */
	private TuleapTrackerConfiguration parentTracker;

	/**
	 * The children trackers.
	 */
	private Map<Integer, TuleapTrackerConfiguration> childrenTrackers = new LinkedHashMap<Integer, TuleapTrackerConfiguration>();

	/**
	 * The default constructor.
	 * 
	 * @param trackerIdentifier
	 *            The id of the tracker.
	 * @param repositoryURL
	 *            The URL of the repository.
	 */
	public TuleapTrackerConfiguration(int trackerIdentifier, String repositoryURL) {
		super(trackerIdentifier, repositoryURL);
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
		super(repositoryURL, repositoryName, repositoryItemName, repositoryDescription);
	}

	/**
	 * Gets the parent tracker.
	 * 
	 * @return the parent tracker configuration
	 */
	public TuleapTrackerConfiguration getParentTracker() {
		return this.parentTracker;
	}

	/**
	 * Gets the list of children trackers.
	 * 
	 * @return The list of the children trackers
	 */
	public Collection<TuleapTrackerConfiguration> getChildrenTrackers() {
		return Collections.unmodifiableCollection(this.childrenTrackers.values());
	}

	/**
	 * Sets the parent tracker that should not be contained in the children collection and should be different
	 * from the actual tracker.
	 * 
	 * @param parentTracker
	 *            the parent tracker configuration to set
	 */
	public void setParentTracker(TuleapTrackerConfiguration parentTracker) {
		if (this.doSetParent(parentTracker)) {
			parentTracker.doAddChild(this);
		}

	}

	/**
	 * Add a child tracker to this configuration that should be different from the parent tracker and the
	 * actual one.
	 * 
	 * @param childTrackerConfiguration
	 *            the child tracker configuration
	 */
	public void addChildTracker(TuleapTrackerConfiguration childTrackerConfiguration) {
		if (this.doAddChild(childTrackerConfiguration)) {
			childTrackerConfiguration.doSetParent(this);
		}
	}

	/**
	 * In order to avoid infinite loops we create this method that has the same behavior as the
	 * setParentTracker(TuleapTrackerConfiguration parentTracker) one.
	 * 
	 * @param theParentTracker
	 *            the parent tracker configuration to set
	 * @return <code>true</code> if the parent is really set.
	 */
	private boolean doSetParent(TuleapTrackerConfiguration theParentTracker) {
		if (this.childrenTrackers.get(Integer.valueOf(theParentTracker.identifier)) == null
				&& theParentTracker.identifier != this.identifier) {
			if (this.parentTracker != null) {
				this.parentTracker.childrenTrackers.remove(Integer.valueOf(this.identifier));
			}
			this.parentTracker = theParentTracker;
		}
		return this.parentTracker == theParentTracker;

	}

	/**
	 * In order to avoid infinite loops we create this method that has the same behavior as the
	 * addChildTracker(TuleapTrackerConfiguration childTrackerConfiguration) one.
	 * 
	 * @param childTrackerConfiguration
	 *            the child tracker configuration
	 * @return <code>true</code> if the child is really added to the collection of tracker's children.
	 */
	private boolean doAddChild(TuleapTrackerConfiguration childTrackerConfiguration) {
		if (this.parentTracker == null) {
			if (childTrackerConfiguration.identifier != this.identifier) {
				this.childrenTrackers.put(Integer.valueOf(childTrackerConfiguration.identifier),
						childTrackerConfiguration);
			}
		} else if (childTrackerConfiguration.identifier != this.parentTracker.identifier
				&& childTrackerConfiguration.identifier != this.identifier) {
			this.childrenTrackers.put(Integer.valueOf(childTrackerConfiguration.identifier),
					childTrackerConfiguration);
		}
		return this.childrenTrackers.containsKey(Integer.valueOf(childTrackerConfiguration.identifier));
	}
}
