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
package org.tuleap.mylyn.task.internal.core.model.config.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskOperation;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapWorkflow;
import org.tuleap.mylyn.task.internal.core.model.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessagesKeys;

/**
 * The Tuleap select box widget.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 0.7
 */
public class TuleapSelectBox extends AbstractTuleapSelectBox {

	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = 5407624943841583459L;

	/**
	 * The workflow of the field.
	 */
	protected final TuleapWorkflow workflow = new TuleapWorkflow(this);

	/**
	 * The constructor.
	 * 
	 * @param formElementIdentifier
	 *            The identifier of the form element
	 */
	public TuleapSelectBox(int formElementIdentifier) {
		super(formElementIdentifier);
	}

	/**
	 * Returns the workflow of the field.
	 * 
	 * @return The workflow of the field, never null.
	 */
	public TuleapWorkflow getWorkflow() {
		return this.workflow;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField#getMetadataType()
	 */
	@Override
	public String getMetadataType() {
		return TaskAttribute.TYPE_SINGLE_SELECT;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField#afterTaskAttributeCreation(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	protected void afterTaskAttributeCreation(TaskAttribute attribute) {
		// Possible values
		if (hasWorkflow()) {
			updateOptionsWithWorkflow(attribute);
		} else {
			super.afterTaskAttributeCreation(attribute);
		}
	}

	/**
	 * Updates the given task attribute's options depending on the tracker workflow and the task's current
	 * status.
	 * 
	 * @param attribute
	 *            The task attribute to update.
	 */
	public void updateOptionsWithWorkflow(TaskAttribute attribute) {
		if (hasWorkflow()) {
			String value = attribute.getValue();
			TuleapSelectBoxItem item = getItem(value);

			// If there is no selected value, it is as if value "100" was selected
			// Every workflow should have at least one transition from "100" to something,
			// otherwise the select box will always be empty...
			// If there is a workflow, the default value "100" is never available, a real state must be
			// selected.
			int stateId = ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID;
			String stateLabel = ""; //$NON-NLS-1$
			attribute.clearOptions();
			if (item != null) {
				stateId = item.getIdentifier();
				stateLabel = item.getLabel();
				attribute.putOption(String.valueOf(stateId), stateLabel);
			}

			for (TuleapSelectBoxItem accessibleState : workflow.accessibleStates(stateId)) {
				attribute.putOption(String.valueOf(accessibleState.getIdentifier()), accessibleState
						.getLabel());
			}
		}
	}

	/**
	 * {@inheritDoc} Also creates the operation attributes required to manage the status, with the relevant
	 * options depending on the currently selected option.
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.config.field.AbstractTuleapSelectBox#createStatusTaskAttribute(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	protected TaskAttribute createStatusTaskAttribute(TaskAttribute parent) {
		TaskAttribute attribute = super.createStatusTaskAttribute(parent);

		TaskAttribute operationAttribute = parent.createAttribute(TaskAttribute.OPERATION);
		TaskOperation.applyTo(operationAttribute, TaskAttribute.STATUS, TuleapMylynTasksMessages
				.getString(TuleapMylynTasksMessagesKeys.markAsLabel));

		List<String> tuleapStatus = new ArrayList<String>();

		String currentStatus = null;
		TaskAttribute statusAttribute = parent.getTaskData().getRoot().getMappedAttribute(
				TaskAttribute.STATUS);
		if (attribute != null) {
			currentStatus = parent.getTaskData().getAttributeMapper().getValueLabel(statusAttribute);
		}
		if (currentStatus != null && currentStatus.length() > 0) {
			for (TuleapSelectBoxItem tuleapSelectBoxItem : items.values()) {
				if (tuleapSelectBoxItem.getLabel().equals(currentStatus)) {
					// Only support the reachable state from the current status
					Collection<TuleapSelectBoxItem> accessibleStates = workflow
							.accessibleStates(tuleapSelectBoxItem.getIdentifier());
					for (TuleapSelectBoxItem accessibleState : accessibleStates) {
						tuleapStatus.add(accessibleState.getLabel());
					}
				}
			}
		} else {
			for (TuleapSelectBoxItem tuleapSelectBoxItem : items.values()) {
				tuleapStatus.add(tuleapSelectBoxItem.getLabel());
			}
		}

		TaskAttribute attrResolvedInput = parent.createAttribute(TaskAttribute.PREFIX_OPERATION
				+ TaskAttribute.STATUS);
		attrResolvedInput.getMetaData().setType(TaskAttribute.TYPE_SINGLE_SELECT);
		attrResolvedInput.getMetaData().setKind(TaskAttribute.KIND_OPERATION);
		for (String status : tuleapStatus) {
			attrResolvedInput.putOption(status, status);
		}
		TaskOperation.applyTo(attrResolvedInput, TaskAttribute.STATUS, TuleapMylynTasksMessages
				.getString(TuleapMylynTasksMessagesKeys.markAsLabel));

		attrResolvedInput.getMetaData().putValue(TaskAttribute.META_ASSOCIATED_ATTRIBUTE_ID,
				TaskAttribute.STATUS);

		return attribute;
	}

	/**
	 * Indicates whether this selectbox has a real workflow, i.e. one with transitions.
	 * 
	 * @return {@code true} if and only if the workflow of this selectbox has at least one transition.
	 */
	public boolean hasWorkflow() {
		return workflow.hasTransitions();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField#setValue(org.eclipse.mylyn.tasks.core.data.TaskAttribute,
	 *      org.tuleap.mylyn.task.internal.core.model.data.AbstractFieldValue)
	 */
	@Override
	public void setValue(TaskAttribute attribute, AbstractFieldValue value) {
		Assert.isTrue(value instanceof BoundFieldValue);
		BoundFieldValue boundFieldValue = (BoundFieldValue)value;
		List<Integer> bindValueIds = boundFieldValue.getValueIds();
		if (!bindValueIds.isEmpty() && bindValueIds.get(0) != null) {
			attribute.setValue(bindValueIds.get(0).toString());
		}
	}
}
