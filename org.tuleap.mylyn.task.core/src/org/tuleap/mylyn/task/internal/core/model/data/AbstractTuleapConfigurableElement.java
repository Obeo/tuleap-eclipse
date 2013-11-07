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
package org.tuleap.mylyn.task.internal.core.model.data;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.config.field.AbstractTuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapFileUpload;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapFloat;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapString;

/**
 * Configurable element that contains fields (artifact, card). Fields need a tracker to be interpreted
 * meaningfully.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public abstract class AbstractTuleapConfigurableElement extends AbstractTuleapDetailedElement {

	/**
	 * The reference to the tracker that backs the element.
	 */
	private TuleapReference tracker;

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
	 * The fields of the Tuleap element.
	 */
	private Map<Integer, AbstractTuleapField> fields = new LinkedHashMap<Integer, AbstractTuleapField>();

	/**
	 * The milestone's configurable values.
	 */
	private Map<Integer, AbstractFieldValue> fieldTypeIdToValue = Maps.newHashMap();

	/**
	 * Default constructor for deserialization.
	 */
	public AbstractTuleapConfigurableElement() {
		// Default constructor for deserialization
	}

	/**
	 * Constructor for new elements not yet synchronized.
	 * 
	 * @param trackerRef
	 *            The tracker ref
	 * @param projectRef
	 *            The project ref
	 */
	public AbstractTuleapConfigurableElement(TuleapReference trackerRef, TuleapReference projectRef) {
		super(projectRef);
		this.tracker = trackerRef;
	}

	/**
	 * Constructor used to update an existing element.
	 * 
	 * @param elementId
	 *            The element id
	 * @param trackerRef
	 *            The tracker reference
	 * @param projectRef
	 *            The project reference
	 */
	public AbstractTuleapConfigurableElement(int elementId, TuleapReference trackerRef,
			TuleapReference projectRef) {
		super(elementId, projectRef);
		this.tracker = trackerRef;
	}

	/**
	 * Constructor for synchronized elements.
	 * 
	 * @param id
	 *            The element id
	 * @param projectRef
	 *            The project reference
	 * @param label
	 *            label
	 * @param url
	 *            URL
	 * @param htmlUrl
	 *            HTML URL
	 * @param creationDate
	 *            Creation date
	 * @param lastModificationDate
	 *            last modification date
	 */
	public AbstractTuleapConfigurableElement(int id, TuleapReference projectRef, String label, String url,
			String htmlUrl, Date creationDate, Date lastModificationDate) {
		super(id, projectRef, label, url, htmlUrl, creationDate, lastModificationDate);
	}

	/**
	 * Returns a reference to the tracker.
	 * 
	 * @return The id of the tracker
	 */
	public TuleapReference getTracker() {
		return tracker;
	}

	/**
	 * Tracker reference setter.
	 * 
	 * @param tracker
	 *            the trackerRef to set
	 */
	public void setTracker(TuleapReference tracker) {
		this.tracker = tracker;
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

	/**
	 * Adds a value.
	 * 
	 * @param value
	 *            The value to set.
	 */
	public void addFieldValue(AbstractFieldValue value) {
		fieldTypeIdToValue.put(Integer.valueOf(value.getFieldId()), value);
	}

	/**
	 * Get the value in relation to the given field id.
	 * 
	 * @param fieldTypeId
	 *            the field id.
	 * @return the value of the given field.
	 */
	public AbstractFieldValue getFieldValue(int fieldTypeId) {
		return fieldTypeIdToValue.get(Integer.valueOf(fieldTypeId));
	}

	/**
	 * Returns the collection of the field values.
	 * 
	 * @return The collection of the field values
	 */
	public Collection<AbstractFieldValue> getFieldValues() {
		return this.fieldTypeIdToValue.values();
	}

}