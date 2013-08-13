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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.mylyn.tasks.core.IRepositoryPerson;
import org.tuleap.mylyn.task.internal.core.model.field.AbstractTuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFileUpload;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBoxItem;

/**
 * The repository configuration will hold the latest known configuration of a given repository. This
 * configuration can dramatically evolve over time and it should be refreshed automatically or manually.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
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
	 * The fields of the Tuleap tracker.
	 */
	private Map<Integer, AbstractTuleapField> fields = new LinkedHashMap<Integer, AbstractTuleapField>();

	/**
	 * The identifier of the tracker.
	 */
	private int trackerId;

	/**
	 * The configuration of the project containing the tracker.
	 */
	private TuleapProjectConfiguration tuleapProjectConfiguration;

	/**
	 * The cached status field.
	 */
	private AbstractTuleapSelectBox statusField;

	/**
	 * The cached contributor field.
	 */
	private AbstractTuleapSelectBox contributorField;

	/**
	 * The attachement field.
	 */
	private TuleapFileUpload attachmentField;

	/**
	 * A cache for the repository persons available.
	 */
	private Map<String, IRepositoryPerson> personsByEmail = new HashMap<String, IRepositoryPerson>();

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
	 * Returns an immutable view of the fields of the tracker. Elements cannot be added or removed form the
	 * returned list.
	 * 
	 * @return An immutable view of the fields of the tracker.
	 */
	public Collection<AbstractTuleapField> getFields() {
		return this.fields.values();
	}

	/**
	 * Add a field to this configuration. If the given field has a specific semantic, it is cached in the
	 * relevant field to allow for easy retrieval.
	 * 
	 * @param field
	 *            The field to add to the configuration
	 */
	public void addField(AbstractTuleapField field) {
		this.fields.put(Integer.valueOf(field.getIdentifier()), field);
		if (field instanceof AbstractTuleapSelectBox) {
			AbstractTuleapSelectBox selectbox = (AbstractTuleapSelectBox)field;
			if (selectbox.isSemanticStatus()) {
				this.statusField = selectbox;
			} else if (selectbox.isSemanticContributor()) {
				this.contributorField = selectbox;
			}
		} else if (field instanceof TuleapFileUpload) {
			attachmentField = (TuleapFileUpload)field;
		}
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

	/**
	 * Sets the configuration of the project containing this tracker.
	 * 
	 * @param projectConfiguration
	 *            The configuration of the project containing this tracker
	 */
	public void setTuleapProjectConfiguration(TuleapProjectConfiguration projectConfiguration) {
		this.tuleapProjectConfiguration = projectConfiguration;
	}

	/**
	 * Returns the configuration of the project containing this tracker.
	 * 
	 * @return The configuration of the project containing this tracker.
	 */
	public TuleapProjectConfiguration getTuleapProjectConfiguration() {
		return tuleapProjectConfiguration;
	}

	/**
	 * Provides access to the cached status field. To use only used after configuration creation.
	 * 
	 * @return The field with the "status" semantic or null if such a field doesn't exist.
	 */
	public AbstractTuleapSelectBox getStatusField() {
		return statusField;
	}

	/**
	 * Provides access to the cached contributor field. To use only used after configuration creation.
	 * 
	 * @return The field with the "contributor" semantic or null if such a field doesn't exist.
	 */
	public AbstractTuleapSelectBox getContributorField() {
		return contributorField;
	}

	/**
	 * Provides access to the attachment field of this configuration.
	 * 
	 * @return The attachment field of this configuration.
	 */
	public TuleapFileUpload getAttachmentField() {
		return attachmentField;
	}

	/**
	 * Indicates whether the given status represents a closed status.
	 * 
	 * @param status
	 *            The status for which you want to know if it means the task is closed.
	 * @return {@code true} if the configuration of the status field closed statuses contains the given
	 *         status, {@false otherwise}.
	 */
	public boolean hasClosedStatusMeaning(String status) {
		if (statusField != null) {
			int statusId = Integer.parseInt(status);
			for (TuleapSelectBoxItem item : statusField.getClosedStatus()) {
				if (item.getIdentifier() == statusId) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Provides access to a configuration field by its id.
	 * 
	 * @param id
	 *            the field identifier
	 * @return The configuration field or null if it doesn't exist.
	 */
	public AbstractTuleapField getFieldById(int id) {
		return fields.get(Integer.valueOf(id));
	}

	/**
	 * Returns the person registered in the local cache for the given email (which must be the person's id).
	 * 
	 * @param email
	 *            the person's email, which must also be his id
	 * @return The person registered for this email, or null if none is registered for this email.
	 */
	public IRepositoryPerson getPerson(String email) {
		return personsByEmail.get(email);
	}

	/**
	 * Register the given person for the given email in a local cache.
	 * 
	 * @param person
	 *            The person
	 * @return The given person, for fluency.
	 */
	public IRepositoryPerson registerPerson(IRepositoryPerson person) {
		personsByEmail.put(person.getPersonId(), person);
		return person;
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
		if (this.childrenTrackers.get(Integer.valueOf(theParentTracker.getTrackerId())) == null
				&& theParentTracker.getTrackerId() != this.getTrackerId()) {
			if (this.parentTracker != null) {
				this.parentTracker.childrenTrackers.remove(Integer.valueOf(this.getTrackerId()));
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
			if (childTrackerConfiguration.getTrackerId() != this.getTrackerId()) {
				this.childrenTrackers.put(Integer.valueOf(childTrackerConfiguration.getTrackerId()),
						childTrackerConfiguration);
			}
		} else if (childTrackerConfiguration.getTrackerId() != this.parentTracker.getTrackerId()
				&& childTrackerConfiguration.getTrackerId() != this.getTrackerId()) {
			this.childrenTrackers.put(Integer.valueOf(childTrackerConfiguration.getTrackerId()),
					childTrackerConfiguration);
		}
		return this.childrenTrackers.containsKey(Integer.valueOf(childTrackerConfiguration.getTrackerId()));
	}
}
