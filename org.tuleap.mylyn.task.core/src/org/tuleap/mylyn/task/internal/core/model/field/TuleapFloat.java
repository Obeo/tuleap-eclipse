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

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMetaData;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;

/**
 * The Tuleap float field.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapFloat extends AbstractTuleapField {

	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = 563002919209031222L;

	/**
	 * The constant used to specify the id of the field with the "initial effort" semantic.
	 */
	private static final String INITIAL_EFFORT_FIELD_ID = "tuleap_initial_effort"; //$NON-NLS-1$

	/**
	 * Flag indicating whether this field has the semantic "initial effort".
	 */
	private boolean initialEffort;

	/**
	 * The constructor.
	 * 
	 * @param formElementIdentifier
	 *            The identifier of the form element
	 */
	public TuleapFloat(int formElementIdentifier) {
		super(formElementIdentifier);
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
	 * @see org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField#getMetadataType()
	 */
	@Override
	public String getMetadataType() {
		return TaskAttribute.TYPE_DOUBLE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField#getDefaultValue()
	 */
	@Override
	public Object getDefaultValue() {
		return "0.0"; //$NON-NLS-1$
	}

	/**
	 * Does this field have the semantic "initial effort" ?
	 * 
	 * @return the initialEffort
	 */
	public boolean isInitialEffort() {
		return initialEffort;
	}

	/**
	 * Initial effort flag setter.
	 * 
	 * @param initialEffort
	 *            the initialEffort to set
	 */
	public void setInitialEffort(boolean initialEffort) {
		this.initialEffort = initialEffort;
	}

	/**
	 * {@inheritDoc} Manages the case of the semantic "initial_effort" if the field has this semantic,
	 * otherwise invokes the super class implementation.
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField#getWriteableAttribute(org.eclipse.mylyn.tasks.core.data.TaskAttribute,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	protected TaskAttribute getWriteableAttribute(TaskAttribute parent, String attributeKey, String type) {
		if (initialEffort) {
			return createInitialEffortTaskAttribute(parent);
		}
		return super.getWriteableAttribute(parent, attributeKey, type);
	}

	/**
	 * Creates the task attribute representing the summary.
	 * 
	 * @param parent
	 *            The parent task attribute
	 * @return The created task attribute.
	 */
	private TaskAttribute createInitialEffortTaskAttribute(TaskAttribute parent) {
		// String with semantic title
		TaskAttribute attribute = parent.createAttribute(INITIAL_EFFORT_FIELD_ID);
		// Attributes
		TaskAttributeMetaData attributeMetadata = attribute.getMetaData();
		attributeMetadata.setType(getMetadataType());
		attributeMetadata.setLabel(getLabel());
		return attribute;
	}
}
