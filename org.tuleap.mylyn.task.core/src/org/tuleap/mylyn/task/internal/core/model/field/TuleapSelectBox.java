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
import java.util.List;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskOperation;
import org.tuleap.mylyn.task.internal.core.model.workflow.TuleapWorkflow;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;

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
	protected TuleapWorkflow workflow = new TuleapWorkflow();

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
	 * @return The workflow of the field.
	 */
	public TuleapWorkflow getWorkflow() {
		return this.workflow;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField#getMetadataType()
	 */
	@Override
	public String getMetadataType() {
		return TaskAttribute.TYPE_SINGLE_SELECT;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField#afterTaskAttributeCreation(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	protected void afterTaskAttributeCreation(TaskAttribute attribute) {
		// Possible values
		if (workflow != null && workflow.getTransitions().size() > 0) {
			updateOptionsWithWorkflow(attribute);
		} else {
			super.afterTaskAttributeCreation(attribute);
		}
	}

	/**
	 * Updates the given task attribute's options depending on the configuration workflow and the task's
	 * current status.
	 * 
	 * @param attribute
	 *            The task attribute to update.
	 */
	public void updateOptionsWithWorkflow(TaskAttribute attribute) {
		// The widget has a workflow
		TuleapSelectBoxItem item = null;
		String value = attribute.getValue();

		// TODO Create a map for the items instead of a list
		for (TuleapSelectBoxItem tuleapSelectBoxItem : items) {
			if (value != null && value.equals(tuleapSelectBoxItem.getLabel())) {
				item = tuleapSelectBoxItem;
				break;
			}
		}

		if (item != null) {
			List<Integer> accessibleStates = getWorkflow().accessibleStates(item.getIdentifier());
			attribute.clearOptions();
			attribute.putOption(value, value);

			for (Integer accessibleState : accessibleStates) {
				for (TuleapSelectBoxItem tuleapSelectBoxItem : items) {
					if (accessibleState.intValue() == tuleapSelectBoxItem.getIdentifier()) {
						attribute.putOption(tuleapSelectBoxItem.getLabel(), tuleapSelectBoxItem.getLabel());
					}
				}
			}
		} else {
			// If we start from an empty state, we can go to the state for the item with the identifier 0.
			List<Integer> accessibleStates = getWorkflow().accessibleStates(
					ITuleapConstants.NEW_ARTIFACT_WORKFLOW_IDENTIFIER);
			attribute.clearOptions();

			for (Integer accessibleState : accessibleStates) {
				for (TuleapSelectBoxItem tuleapSelectBoxItem : items) {
					if (accessibleState.intValue() == tuleapSelectBoxItem.getIdentifier()) {
						attribute.putOption(tuleapSelectBoxItem.getLabel(), tuleapSelectBoxItem.getLabel());
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc} Also creates the operation attributes required to manage the status, with the relevant
	 * options depending on the currently selected option.
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.field.AbstractTuleapSelectBox#createStatusTaskAttribute(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	protected TaskAttribute createStatusTaskAttribute(TaskAttribute parent) {
		TaskAttribute attribute = super.createStatusTaskAttribute(parent);

		TaskAttribute operationAttribute = parent.getAttribute(TaskAttribute.OPERATION);
		if (operationAttribute == null) {
			operationAttribute = parent.createAttribute(TaskAttribute.OPERATION);
		}
		TaskOperation.applyTo(operationAttribute, TaskAttribute.STATUS, TuleapMylynTasksMessages
				.getString("TuleapTaskDataHandler.MarkAs")); //$NON-NLS-1$

		List<String> tuleapStatus = new ArrayList<String>();

		String currentStatus = null;
		TaskAttribute statusAttribute = parent.getTaskData().getRoot().getMappedAttribute(
				TaskAttribute.STATUS);
		if (attribute != null) {
			currentStatus = parent.getTaskData().getAttributeMapper().getValueLabel(statusAttribute);
		}
		if (currentStatus != null && currentStatus.length() > 0) {
			for (TuleapSelectBoxItem tuleapSelectBoxItem : items) {
				if (tuleapSelectBoxItem.getLabel().equals(currentStatus)) {
					// Only support the reachable state from the current status
					List<Integer> accessibleStates = workflow.accessibleStates(tuleapSelectBoxItem
							.getIdentifier());
					for (Integer accessibleState : accessibleStates) {
						for (TuleapSelectBoxItem item : items) {
							if (item.getIdentifier() == accessibleState.intValue()) {
								tuleapStatus.add(item.getLabel());
							}
						}
					}
				}
			}
		} else {
			for (TuleapSelectBoxItem tuleapSelectBoxItem : items) {
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
				.getString("TuleapTaskDataHandler.MarkAs")); //$NON-NLS-1$

		attrResolvedInput.getMetaData().putValue(TaskAttribute.META_ASSOCIATED_ATTRIBUTE_ID,
				TaskAttribute.STATUS);

		return attribute;
	}
}
