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
package org.tuleap.mylyn.task.internal.core.model.field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMetaData;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;

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
	protected List<TuleapSelectBoxItem> items = new ArrayList<TuleapSelectBoxItem>();

	/**
	 * The value of the binding of the select box.
	 */
	protected String binding;

	/**
	 * The list of items that should be considered as an open status semantically.
	 */
	protected List<TuleapSelectBoxItem> openStatus = new ArrayList<TuleapSelectBoxItem>();

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
	 * Returns the list of the items available in the select box.
	 * 
	 * @return The list of the items available in the select box.
	 */
	public List<TuleapSelectBoxItem> getItems() {
		return this.items;
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
		ArrayList<TuleapSelectBoxItem> closedStatus = new ArrayList<TuleapSelectBoxItem>(this.items);
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
	 * @see org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField#getMetadataKind()
	 */
	@Override
	public String getMetadataKind() {
		return TaskAttribute.KIND_DEFAULT;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField#getDefaultValue()
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
	 * {@link org.tuleap.mylyn.task.internal.core.model.field.AbstractTuleapSelectBox#getItems()}</li>
	 * <li>users (the collections of users must be requested to the server and then added to
	 * {@link org.tuleap.mylyn.task.internal.core.model.field.AbstractTuleapSelectBox#getItems()}</li>
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
	 * Returns <code>true</code> if the field is statically binded, <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if the field is statically binded, <code>false</code> otherwise.
	 */
	public boolean isStaticallyBinded() {
		return ITuleapConstants.TULEAP_STATIC_BINDING_ID.equals(this.binding);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField#createTaskAttribute(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
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
		TaskAttribute attribute = parent.createAttribute(Integer.valueOf(getIdentifier()).toString());
		// Attributes
		TaskAttributeMetaData attributeMetadata = attribute.getMetaData();
		attributeMetadata.setType(getMetadataType());
		attributeMetadata.setLabel(getLabel());

		afterTaskAttributeCreation(attribute);

		return attribute;
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
		metaData.setLabel(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.Status")); //$NON-NLS-1$
		metaData.setType(getMetadataType());
		metaData.setKind(TaskAttribute.KIND_DEFAULT);

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
		metaData.setLabel(TuleapMylynTasksMessages.getString("TuleapTaskDataHandler.AssignedToLabel")); //$NON-NLS-1$;
		metaData.setType(getMetadataType());
		metaData.setKind(TaskAttribute.KIND_PEOPLE);

		afterTaskAttributeCreation(attribute);

		return attribute;
	}

	/**
	 * Copies the items in the task attribute's options.
	 * 
	 * @param attribute
	 *            the task attribute to populate
	 */
	@Override
	protected void afterTaskAttributeCreation(TaskAttribute attribute) {
		for (TuleapSelectBoxItem item : items) {
			attribute.putOption(String.valueOf(item.getIdentifier()), item.getLabel());
		}
		if (items.size() > 0) {
			attribute.putOption("", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Maintained temporarily for unit tests but to remove.
	 * 
	 * @return something useless
	 * @deprecated
	 */
	@Deprecated
	public Map<String, String> getOptions() {
		Map<String, String> options = new HashMap<String, String>();
		for (TuleapSelectBoxItem item : items) {
			options.put(item.getLabel(), item.getLabel());
		}
		if (items.size() > 0) {
			options.put("", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return options;
	}
}
