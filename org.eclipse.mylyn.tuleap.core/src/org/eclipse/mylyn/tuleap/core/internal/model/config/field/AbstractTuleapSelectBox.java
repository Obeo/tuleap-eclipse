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
package org.eclipse.mylyn.tuleap.core.internal.model.config.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMetaData;
import org.eclipse.mylyn.tuleap.core.internal.TuleapCoreActivator;
import org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AbstractFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.BoundFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.util.ITuleapConstants;
import org.eclipse.mylyn.tuleap.core.internal.util.TuleapMylynTasksMessages;
import org.eclipse.mylyn.tuleap.core.internal.util.TuleapMylynTasksMessagesKeys;

/**
 * Super class of tuleap select box and multi-select box.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 0.7
 */
public abstract class AbstractTuleapSelectBox extends AbstractTuleapField {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -1169393740930580675L;

	/**
	 * The select box items.
	 */
	protected final Map<String, TuleapSelectBoxItem> items = new LinkedHashMap<String, TuleapSelectBoxItem>();

	/**
	 * The value of the binding of the select box.
	 */
	protected String binding;

	/**
	 * The list of items that should be considered as an open status semantically.
	 */
	protected final List<TuleapSelectBoxItem> openStatus = new ArrayList<TuleapSelectBoxItem>();

	/**
	 * Indicates if this field represents the list of contributors of the artifact.
	 */
	protected boolean isSemanticContributor;

	/**
	 * The constructor.
	 * 
	 * @param formElementIdentifier
	 *            The identifier of the form element
	 */
	public AbstractTuleapSelectBox(int formElementIdentifier) {
		super(formElementIdentifier);
	}

	/**
	 * Returns the list of items that should be considered as opened status semantically.
	 * 
	 * @return The list of items that should be considered as opened status semantically.
	 */
	public List<TuleapSelectBoxItem> getOpenStatus() {
		return this.openStatus;
	}

	/**
	 * Returns the list of items that should be considered as closed status semantically.
	 * 
	 * @return The list of items that should be considered as closed status semantically.
	 */
	public List<TuleapSelectBoxItem> getClosedStatus() {
		ArrayList<TuleapSelectBoxItem> closedStatus = new ArrayList<TuleapSelectBoxItem>(this.items.values());
		closedStatus.removeAll(this.openStatus);
		return closedStatus;
	}

	/**
	 * Returns <code>true</code> if this field is to be considered as the status of the artifact semantically,
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if this field is to be considered as the status of the artifact semantically,
	 *         <code>false</code> otherwise.
	 */
	public boolean isSemanticStatus() {
		return this.openStatus.size() > 0;
	}

	/**
	 * Returns <code>true</code> if this field represents the list of contributors on the artifact of the
	 * tracker, <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if this field represents the list of contributors on the artifact of the
	 *         tracker, <code>false</code> otherwise.
	 */
	public boolean isSemanticContributor() {
		return this.isSemanticContributor;
	}

	/**
	 * Sets to indicate that this field represents the list of the contributors on the artifact of the
	 * tracker.
	 * 
	 * @param isContributor
	 *            <code>true</code> to indicate that this field represents the list of contributors on the
	 *            artifact of the tracker.
	 */
	public void setSemanticContributor(boolean isContributor) {
		this.isSemanticContributor = isContributor;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField#getMetadataKind()
	 */
	@Override
	public String getMetadataKind() {
		return TaskAttribute.KIND_DEFAULT;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField#getDefaultValue()
	 */
	@Override
	public Object getDefaultValue() {
		return null;
	}

	/**
	 * Sets the value of the binding of the select box.
	 * <p>
	 * The binding must be one of the following value:
	 * <ul>
	 * <li>static (the collections of the possible bindings must then be added to
	 * {@code org.eclipse.mylyn.tuleap.core.internal.model.field.AbstractTuleapSelectBox#addItem()}</li>
	 * <li>users (the collections of users must be requested to the server and then added to
	 * {@code org.eclipse.mylyn.tuleap.core.internal.model.field.AbstractTuleapSelectBox#addItem()}</li>
	 * </ul>
	 * </p>
	 * 
	 * @param selectBoxBinding
	 *            The value of the binding
	 */
	public void setBinding(String selectBoxBinding) {
		this.binding = selectBoxBinding;
	}

	/**
	 * Returns the kind of binding used by this select box.
	 * 
	 * @return The kind of binding used by this select box.
	 */
	public String getBinding() {
		return this.binding;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField#createTaskAttribute(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	public TaskAttribute createTaskAttribute(TaskAttribute parent) {
		TaskAttribute attribute;
		if (isSemanticContributor()) {
			attribute = createAssignedToTaskAttribute(parent);
		} else if (isSemanticStatus()) {
			attribute = createStatusTaskAttribute(parent);
		} else {
			attribute = createSelectBoxTaskAttribute(parent);
		}
		return attribute;
	}

	/**
	 * Creates a new select box task attribute.
	 * 
	 * @param parent
	 *            the parent task attribute
	 * @return The newly created task attribute.
	 */
	protected TaskAttribute createSelectBoxTaskAttribute(TaskAttribute parent) {
		return super.createTaskAttribute(parent);
	}

	/**
	 * Creates the task attribute representing the status.
	 * 
	 * @param parent
	 *            The parent task attribute
	 * @return The created task attribute.
	 */
	protected TaskAttribute createStatusTaskAttribute(TaskAttribute parent) {
		// SB and MSB + workflow
		// factorize code between create status / assignedto / select box, checkbox and multi select box
		// status section etc + a mylyn operation task attribute to modify the real status field
		TaskAttribute attribute = parent.createAttribute(TaskAttribute.STATUS);
		TaskAttributeMetaData metaData = attribute.getMetaData();
		metaData.setLabel(TuleapMylynTasksMessages.getString(TuleapMylynTasksMessagesKeys.statusLabel));
		metaData.setType(getMetadataType());
		// No task kind in order to hide this field from the "attributes" part

		afterTaskAttributeCreation(attribute);

		return attribute;
	}

	/**
	 * Creates the task attribute representing the person to which the task is assigned.
	 * 
	 * @param parent
	 *            The parent task attribute
	 * @return The created task attribute.
	 */
	protected TaskAttribute createAssignedToTaskAttribute(TaskAttribute parent) {
		// SB and MSB + workflow
		// factorize code between create status / assignedto / select box, checkbox and multi select box
		// people section etc
		TaskAttribute attribute = parent.createAttribute(TaskAttribute.USER_ASSIGNED);
		TaskAttributeMetaData metaData = attribute.getMetaData();
		metaData.setLabel(TuleapMylynTasksMessages.getString(TuleapMylynTasksMessagesKeys.assignedToLabel));
		metaData.setType(getMetadataType());
		metaData.setKind(TaskAttribute.KIND_PEOPLE);

		afterTaskAttributeCreation(attribute);

		return attribute;
	}

	/**
	 * Returns a select box item by its id.
	 * 
	 * @param itemId
	 *            the id of the item being looked for, as a string
	 * @return The relevant item, or {@code null} if it cannot be found.
	 */
	public TuleapSelectBoxItem getItem(String itemId) {
		return items.get(itemId);
	}

	/**
	 * Returns the collection of items of this select box.
	 * 
	 * @return The collection of items in this select box, never null.
	 */
	public Collection<TuleapSelectBoxItem> getItems() {
		return items.values();
	}

	/**
	 * Add a new option to this select box.
	 * 
	 * @param item
	 *            the item to add. If an item with the same identifier was already registered, it will be
	 *            silently overridden.
	 */
	public void addItem(TuleapSelectBoxItem item) {
		items.put(String.valueOf(item.getIdentifier()), item);
	}

	/**
	 * Copies the items in the task attribute's options.
	 * 
	 * @param attribute
	 *            the task attribute to populate
	 */
	@Override
	protected void afterTaskAttributeCreation(TaskAttribute attribute) {
		for (TuleapSelectBoxItem item : items.values()) {
			attribute.putOption(String.valueOf(item.getIdentifier()), item.getLabel());
		}
		if (items.size() > 0) {
			attribute.putOption(String.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID),
					TuleapMylynTasksMessages.getString(TuleapMylynTasksMessagesKeys.selectBoxNone));
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField#createFieldValue(org.eclipse.mylyn.tasks.core.data.TaskAttribute,
	 *      int)
	 */
	@Override
	public AbstractFieldValue createFieldValue(TaskAttribute attribute, int fieldId) {
		List<Integer> valueIds = new ArrayList<Integer>();
		for (String strValue : attribute.getValues()) {
			try {
				valueIds.add(Integer.valueOf(strValue));
			} catch (NumberFormatException e) {
				TuleapCoreActivator.log(e, false);
			}
		}
		return new BoundFieldValue(fieldId, valueIds);
	}
}
