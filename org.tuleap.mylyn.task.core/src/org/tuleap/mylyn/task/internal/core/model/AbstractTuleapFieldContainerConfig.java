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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.mylyn.tasks.core.IRepositoryPerson;
import org.tuleap.mylyn.task.internal.core.model.field.AbstractTuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFileUpload;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFloat;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBoxItem;

/**
 * Super-class of tuleap elements that contain configurable fields (trackers, backlog items, milestones,
 * etc.).
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractTuleapFieldContainerConfig implements Serializable {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 4312082228838422655L;

	/**
	 * The last time this configuration was updated.
	 */
	protected long lastUpdate;

	/**
	 * The url of the repository configuration.
	 */
	protected String url;

	/**
	 * The label of the Tuleap tracker.
	 */
	protected String label;

	/**
	 * The item label.
	 */
	protected String itemName;

	/**
	 * The description of the Tuleap tracker.
	 */
	protected String description;

	/**
	 * The fields of the Tuleap tracker.
	 */
	protected Map<Integer, AbstractTuleapField> fields = new LinkedHashMap<Integer, AbstractTuleapField>();

	/**
	 * The identifier of the configured object (tracker, backlogItem type, milestone type, card type).
	 */
	protected int identifier;

	/**
	 * The configuration of the project containing the tracker.
	 */
	protected TuleapProjectConfiguration tuleapProjectConfiguration;

	/**
	 * The cached status field.
	 */
	protected AbstractTuleapSelectBox statusField;

	/**
	 * The cached contributor field.
	 */
	protected AbstractTuleapSelectBox contributorField;

	/**
	 * The cached initial effort field (not mandatory).
	 */
	protected TuleapFloat initialEffortField;

	/**
	 * The attachment field.
	 */
	protected TuleapFileUpload attachmentField;

	/**
	 * A cache for the repository persons available.
	 */
	protected Map<String, IRepositoryPerson> personsByEmail = new HashMap<String, IRepositoryPerson>();

	/**
	 * The default constructor.
	 * 
	 * @param identifier
	 *            The id of the tracker.
	 * @param repositoryURL
	 *            The URL of the repository.
	 */
	public AbstractTuleapFieldContainerConfig(int identifier, String repositoryURL) {
		this.identifier = identifier;
		this.url = repositoryURL;
	}

	/**
	 * The constructor.
	 * 
	 * @param repositoryURL
	 *            The URL of the repository.
	 * @param repositoryName
	 *            The label of the repository
	 * @param repositoryItemName
	 *            The item label of the repository
	 * @param repositoryDescription
	 *            The description of the repository
	 */
	public AbstractTuleapFieldContainerConfig(String repositoryURL, String repositoryName,
			String repositoryItemName, String repositoryDescription) {
		this.url = repositoryURL;
		this.label = repositoryName;
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
	 * Returns the label of the repository.
	 * 
	 * @return The label of the repository.
	 */
	public String getName() {
		return this.label;
	}

	/**
	 * Sets the label of the configuration.
	 * 
	 * @param configurationName
	 *            The label of the configuration.
	 */
	public void setName(String configurationName) {
		this.label = configurationName;
	}

	/**
	 * Returns the item label of the repository.
	 * 
	 * @return The item label of the repository.
	 */
	public String getItemName() {
		return this.itemName;
	}

	/**
	 * Sets the item label of the repository.
	 * 
	 * @param repositoryItemName
	 *            The item label of the repository.
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
}
