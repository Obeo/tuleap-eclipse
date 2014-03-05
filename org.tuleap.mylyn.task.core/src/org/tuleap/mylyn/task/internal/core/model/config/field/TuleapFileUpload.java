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
import org.tuleap.mylyn.task.internal.core.model.data.AbstractFieldValue;

/**
 * The Tuleap file upload field.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapFileUpload extends AbstractTuleapField {

	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = -7708635305455622701L;

	/**
	 * The constructor.
	 * 
	 * @param formElementIdentifier
	 *            The identifier of the form element
	 */
	public TuleapFileUpload(int formElementIdentifier) {
		super(formElementIdentifier);
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
		return TaskAttribute.TYPE_ATTACHMENT;
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

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@code false}, since file upload fields do not need to be initiated with a task attribute.
	 * @see org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField#needsTaskAttributeForInitialization()
	 */
	@Override
	public boolean needsTaskAttributeForInitialization() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField#setValue(org.eclipse.mylyn.tasks.core.data.TaskAttribute,
	 *      org.tuleap.mylyn.task.internal.core.model.data.AbstractFieldValue)
	 */
	@Override
	public void setValue(TaskAttribute attribute, AbstractFieldValue value) {
		throw new IllegalStateException("Developer error. Calling setvalue on File Uploads is forbidden."); //$NON-NLS-1$
	}
}
