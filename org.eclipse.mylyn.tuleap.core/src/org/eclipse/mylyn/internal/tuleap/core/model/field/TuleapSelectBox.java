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
package org.eclipse.mylyn.internal.tuleap.core.model.field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField;
import org.eclipse.mylyn.internal.tuleap.core.model.workflow.TuleapWorkflow;
import org.eclipse.mylyn.internal.tuleap.core.util.ITuleapConstants;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;

/**
 * The Tuleap select box widget.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapSelectBox extends AbstractTuleapField {

	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = 5407624943841583459L;

	/**
	 * The select box items.
	 */
	private List<TuleapSelectBoxItem> items = new ArrayList<TuleapSelectBoxItem>();

	/**
	 * The value of the binding of the select box.
	 */
	private String binding;

	/**
	 * The list of items that should be considered as an open status semantically.
	 */
	private List<TuleapSelectBoxItem> openStatus = new ArrayList<TuleapSelectBoxItem>();

	/**
	 * The workflow of the field.
	 */
	private TuleapWorkflow workflow = new TuleapWorkflow();

	/**
	 * Indicates if this field represents the list of contributors of the artifact.
	 */
	private boolean isSemanticContributor;

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
	 * @see org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField#getMetadataKind()
	 */
	@Override
	public String getMetadataKind() {
		return TaskAttribute.KIND_DEFAULT;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField#getMetadataType()
	 */
	@Override
	public String getMetadataType() {
		return TaskAttribute.TYPE_SINGLE_SELECT;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField#getOptions()
	 */
	@Override
	public Map<String, String> getOptions() {
		Map<String, String> options = new HashMap<String, String>();
		for (TuleapSelectBoxItem item : this.items) {
			options.put(item.getLabel(), item.getLabel());
		}
		return options;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField#getDefaultValue()
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
	 * {@link org.eclipse.mylyn.internal.tuleap.core.model.TuleapSelectBox#getItems()}</li>
	 * <li>users (the collections of users must be requested to the server and then added to
	 * {@link org.eclipse.mylyn.internal.tuleap.core.model.TuleapSelectBox#getItems()}</li>
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
	 * Returns the workflow of the field.
	 * 
	 * @return The workflow of the field.
	 */
	public TuleapWorkflow getWorkflow() {
		return this.workflow;
	}
}
