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

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField;

/**
 * The Tuleap artifact link field.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapArtifactLink extends AbstractTuleapField {

	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = -8648649700121180143L;

	/**
	 * The constructor.
	 * 
	 * @param formElementIdentifier
	 *            The identifier of the form element
	 */
	public TuleapArtifactLink(int formElementIdentifier) {
		super(formElementIdentifier);
	}

	/**
	 * Creates and returns a new TaskAttribute using this field. This method is meant to be overridden in the
	 * subclasses to provide the relevant behavior depending on the type of tuleap field.
	 * 
	 * @param parent
	 *            The parent task attribute in which the new task attribute must be created.
	 * @return The created task attribute.
	 */
	@Override
	public TaskAttribute createTaskAttribute(TaskAttribute parent) {
		int id = getIdentifier();
		TaskAttribute attribute = this.getWriteableAttribute(parent, String.valueOf(id), getMetadataType());
		if (attribute != null) {
			initializeMetaData(attribute.getMetaData());
		}

		return attribute;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField#getMetadataKind()
	 */
	@Override
	public String getMetadataKind() {
		return TaskAttribute.KIND_DEFAULT;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField#getMetadataType()
	 */
	@Override
	public String getMetadataType() {
		return TaskAttribute.TYPE_TASK_DEPENDENCY;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField#getDefaultValue()
	 */
	@Override
	public Object getDefaultValue() {
		return null;
	}

}
