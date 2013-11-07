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

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.field.AbstractTuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFileUpload;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFloat;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapString;

/**
 * The repository configuration will hold the latest known configuration of a given repository. This
 * configuration can dramatically evolve over time and it should be refreshed automatically or manually.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapTracker implements Serializable {

	/**
	 * The serialization version ID.
	 */
	private static final long serialVersionUID = -8688658274608834147L;

	/**
	 * The tracker's parent.
	 */
	private TuleapTracker parentTracker;

	/**
	 * The children trackers.
	 */
	private Map<Integer, TuleapTracker> childrenTrackers = new LinkedHashMap<Integer, TuleapTracker>();

	/**
	 * The identifier of the configured object (tracker, backlogItem type, milestone type, card type).
	 */
	private int identifier;

	/**
	 * The last time this configuration was updated.
	 */
	private long lastUpdateDate;

	/**
	 * The url of the configuration.
	 */
	private String url;

	/**
	 * The label of the Tuleap tracker.
	 */
	private String label;

	/**
	 * The item label.
	 */
	private String itemName;

	/**
	 * The description of the Tuleap element.
	 */
	private String description;

	/**
	 * The fields of the Tuleap element.
	 */
	private Map<Integer, AbstractTuleapField> fields = new LinkedHashMap<Integer, AbstractTuleapField>();

	/**
	 * The title field.
	 */
	private TuleapString titleField;

	/**
	 * The cached status field.
	 */
	private AbstractTuleapSelectBox statusField;

	/**
	 * The cached contributor field.
	 */
	private AbstractTuleapSelectBox contributorField;

	/**
	 * The cached initial effort field (not mandatory).
	 */
	private TuleapFloat initialEffortField;

	/**
	 * The attachment field.
	 */
	private TuleapFileUpload attachmentField;

	/**
	 * The configuration of the project containing the configuration.
	 */
	private TuleapProjectConfiguration tuleapProjectConfiguration;

	/**
	 * The constructor.
	 * 
	 * @param identifier
	 *            The identifier of the configuration
	 * @param url
	 *            The URL.
	 * @param label
	 *            The label
	 * @param itemName
	 *            The item name
	 * @param description
	 *            The description
	 * @param lastUpdateDate
	 *            The date at which the configuration was last updated.
	 */
	public TuleapTracker(int identifier, String url, String label, String itemName, String description,
			long lastUpdateDate) {
		this.identifier = identifier;
		this.url = url;
		this.label = label;
		this.itemName = itemName;
		this.description = description;
		this.lastUpdateDate = lastUpdateDate;
	}

	/**
	 * Returns the time at which the configuration was last updated.
	 * 
	 * @return The time at which the configuration was last updated.
	 */
	public long getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	/**
	 * Returns the url of the configuration.
	 * 
	 * @return The url of the configuration.
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Returns the label of the configuration.
	 * 
	 * @return The label of the configuration.
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Returns the item name of the configuration.
	 * 
	 * @return The item name of the configuration.
	 */
	public String getItemName() {
		return this.itemName;
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
	 * Returns a qualified label composed of the label and the identifier.
	 * 
	 * @return A qualified label composed of the label and the identifier.
	 */
	public String getQualifiedName() {
		return this.label + " [" + this.identifier + ']'; //$NON-NLS-1$
	}

	/**
	 * Returns the configuration id.
	 * 
	 * @return The configuration id.
	 */
	public int getIdentifier() {
		return this.identifier;
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
	 * Gets the parent tracker.
	 * 
	 * @return the parent tracker configuration
	 */
	public TuleapTracker getParentTracker() {
		return this.parentTracker;
	}

	/**
	 * Gets the list of children trackers.
	 * 
	 * @return The list of the children trackers
	 */
	public Collection<TuleapTracker> getChildrenTrackers() {
		return Collections.unmodifiableCollection(this.childrenTrackers.values());
	}

	/**
	 * Sets the parent tracker that should not be contained in the children collection and should be different
	 * from the actual tracker.
	 * 
	 * @param parentTracker
	 *            the parent tracker configuration to set
	 */
	public void setParentTracker(TuleapTracker parentTracker) {
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
	public void addChildTracker(TuleapTracker childTrackerConfiguration) {
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
	private boolean doSetParent(TuleapTracker theParentTracker) {
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
	private boolean doAddChild(TuleapTracker childTrackerConfiguration) {
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
		} else if (field instanceof TuleapString && ((TuleapString)field).isSemanticTitle()) {
			this.titleField = (TuleapString)field;
		}
	}

	/**
	 * Provides access to the cached title field. To use only after configuration creation.
	 * 
	 * @return The field with the "title" semantic or null if sucj field doesn't exists.
	 */
	public TuleapString getTitleField() {
		return this.titleField;
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
	 * Provides access to the cached initial effort field. To use only used after configuration creation.
	 * 
	 * @return The field with the "initial effort" semantic or null if such a field doesn't exist.
	 */
	public TuleapFloat geInitialEffortField() {
		return initialEffortField;
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
	 * @param statusItemId
	 *            The status for which you want to know if it means the task is closed.
	 * @return {@code true} if the configuration of the status field closed statuses contains the given
	 *         status, {@false otherwise}.
	 */
	public boolean hasClosedStatusMeaning(int statusItemId) {
		if (statusField != null) {
			for (TuleapSelectBoxItem item : statusField.getClosedStatus()) {
				if (item.getIdentifier() == statusItemId) {
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
}
