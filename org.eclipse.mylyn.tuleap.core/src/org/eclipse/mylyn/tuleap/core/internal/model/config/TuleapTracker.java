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
package org.eclipse.mylyn.tuleap.core.internal.model.config;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.mylyn.tuleap.core.internal.model.config.field.AbstractTuleapSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapFileUpload;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapFloat;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBoxItem;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapString;

/**
 * The repository will hold the latest known trackers of a given repository. A tracker can dramatically evolve
 * over time and should be refreshed automatically or manually.
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
	 * References to the tracker resources.
	 */
	private TuleapResource[] resources;

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
	 * The last time this tracker was updated.
	 */
	private long lastUpdateDate;

	/**
	 * The url of the tracker.
	 */
	private String url;

	/**
	 * The uri of the project.
	 */
	private String uri;

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
	 * The project containing the tracker.
	 */
	private TuleapProject project;

	/**
	 * The constructor.
	 * 
	 * @param identifier
	 *            The identifier of the tracker
	 * @param url
	 *            The URL.
	 * @param label
	 *            The label
	 * @param itemName
	 *            The item name
	 * @param description
	 *            The description
	 * @param lastUpdateDate
	 *            The date at which the tracker was last updated.
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
	 * Returns the uri of the tracker.
	 * 
	 * @return The uri of the tracker
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Tracker uri setter.
	 * 
	 * @param uri
	 *            The tracker uri
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * Returns the time at which the tracker was last updated.
	 * 
	 * @return The time at which the tracker was last updated.
	 */
	public long getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	/**
	 * Returns the url of the tracker.
	 * 
	 * @return The url of the tracker.
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Returns the label of the tracker.
	 * 
	 * @return The label of the tracker.
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Returns the item name of the tracker.
	 * 
	 * @return The item name of the tracker.
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
	 * Returns the tracker id.
	 * 
	 * @return The tracker id.
	 */
	public int getIdentifier() {
		return this.identifier;
	}

	/**
	 * Sets the project containing this tracker.
	 * 
	 * @param project
	 *            The project containing this tracker
	 */
	public void setProject(TuleapProject project) {
		this.project = project;
	}

	/**
	 * Returns the project containing this tracker.
	 * 
	 * @return The project containing this tracker.
	 */
	public TuleapProject getProject() {
		return project;
	}

	/**
	 * Gets the parent tracker.
	 * 
	 * @return the parent tracker
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
	 *            the parent tracker to set
	 */
	public void setParentTracker(TuleapTracker parentTracker) {
		if (this.doSetParent(parentTracker)) {
			parentTracker.doAddChild(this);
		}

	}

	/**
	 * Add a child tracker to this tracker that should be different from the parent tracker and the actual
	 * one.
	 * 
	 * @param childTracker
	 *            the child tracker
	 */
	public void addChildTracker(TuleapTracker childTracker) {
		if (this.doAddChild(childTracker)) {
			childTracker.doSetParent(this);
		}
	}

	/**
	 * In order to avoid infinite loops we create this method that has the same behavior as the
	 * setParentTracker(TuleapTracker parentTracker) one.
	 * 
	 * @param theParentTracker
	 *            the parent tracker to set
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
	 * addChildTracker(TuleapTracker childTracker) one.
	 * 
	 * @param childTracker
	 *            the child tracker
	 * @return <code>true</code> if the child is really added to the collection of tracker's children.
	 */
	private boolean doAddChild(TuleapTracker childTracker) {
		if (this.parentTracker == null) {
			if (childTracker.identifier != this.identifier) {
				this.childrenTrackers.put(Integer.valueOf(childTracker.identifier), childTracker);
			}
		} else if (childTracker.identifier != this.parentTracker.identifier
				&& childTracker.identifier != this.identifier) {
			this.childrenTrackers.put(Integer.valueOf(childTracker.identifier), childTracker);
		}
		return this.childrenTrackers.containsKey(Integer.valueOf(childTracker.identifier));
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
	 * Add a field to this tracker. If the given field has a specific semantic, it is cached in the relevant
	 * field to allow for easy retrieval.
	 * 
	 * @param field
	 *            The field to add to the tracker
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
	 * Provides access to the cached title field. To use only after tracker creation.
	 * 
	 * @return The field with the "title" semantic or null if sucj field doesn't exists.
	 */
	public TuleapString getTitleField() {
		return this.titleField;
	}

	/**
	 * Provides access to the cached status field. To use only used after tracker creation.
	 * 
	 * @return The field with the "status" semantic or null if such a field doesn't exist.
	 */
	public AbstractTuleapSelectBox getStatusField() {
		return statusField;
	}

	/**
	 * Provides access to the cached contributor field. To use only used after tracker creation.
	 * 
	 * @return The field with the "contributor" semantic or null if such a field doesn't exist.
	 */
	public AbstractTuleapSelectBox getContributorField() {
		return contributorField;
	}

	/**
	 * Provides access to the cached initial effort field. To use only used after tracker creation.
	 * 
	 * @return The field with the "initial effort" semantic or null if such a field doesn't exist.
	 */
	public TuleapFloat geInitialEffortField() {
		return initialEffortField;
	}

	/**
	 * Provides access to the attachment field of this tracker.
	 * 
	 * @return The attachment field of this tracker.
	 */
	public TuleapFileUpload getAttachmentField() {
		return attachmentField;
	}

	/**
	 * Indicates whether the given status represents a closed status.
	 * 
	 * @param statusItemId
	 *            The status for which you want to know if it means the task is closed.
	 * @return {@code true} if the tracker of the status field closed statuses contains the given status,
	 *         {@false otherwise}.
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
	 * Provides access to a tracker field by its id.
	 * 
	 * @param id
	 *            the field identifier
	 * @return The tracker field or null if it doesn't exist.
	 */
	public AbstractTuleapField getFieldById(int id) {
		return fields.get(Integer.valueOf(id));
	}

	/**
	 * Tracker resources getter.
	 * 
	 * @return the trackerResources, an array that is never <code>null</code> but possibly empty.
	 */
	public TuleapResource[] getTrackerResources() {
		if (resources == null) {
			return new TuleapResource[0];
		}
		return resources.clone();
	}

	/**
	 * Tracker resources setter.
	 * 
	 * @param trackerResources
	 *            the trackerResources to set
	 */
	public void setTrackerResources(TuleapResource[] trackerResources) {
		this.resources = trackerResources.clone();
	}

	/**
	 * Indicates whether the given resource exists on this tracker.
	 * 
	 * @param key
	 *            The resource type being looked for
	 * @return {@code true} if and only if the given service is present in the list of services of this
	 *         tracker.
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
