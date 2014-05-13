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
package org.tuleap.mylyn.task.core.internal.model.config.field;

import org.eclipse.core.runtime.Assert;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMetaData;
import org.tuleap.mylyn.task.core.internal.model.config.AbstractTuleapField;
import org.tuleap.mylyn.task.core.internal.model.data.AbstractFieldValue;
import org.tuleap.mylyn.task.core.internal.model.data.LiteralFieldValue;

/**
 * The Tuleap string field.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapString extends AbstractTuleapField {

	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = -2298403602439489298L;

	/**
	 * The maximum length of the string.
	 */
	private int size;

	/**
	 * Indicates if this field is the semantic title of the artifact of the tracker.
	 */
	private boolean isSemanticTitle;

	/**
	 * The constructor.
	 *
	 * @param formElementIdentifier
	 *            The identifier of the form element
	 */
	public TuleapString(int formElementIdentifier) {
		super(formElementIdentifier);
	}

	/**
	 * Sets the maximum size of the strings.
	 *
	 * @param maximumSize
	 *            The maximum size of the strings.
	 */
	public void setSize(int maximumSize) {
		this.size = maximumSize;
	}

	/**
	 * Returns the maximum size of the strings.
	 *
	 * @return The maximum size of the strings.
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.core.internal.model.config.AbstractTuleapField#getMetadataKind()
	 */
	@Override
	public String getMetadataKind() {
		return TaskAttribute.KIND_DEFAULT;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.core.internal.model.config.AbstractTuleapField#getMetadataType()
	 */
	@Override
	public String getMetadataType() {
		return TaskAttribute.TYPE_SHORT_RICH_TEXT;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.core.internal.model.config.AbstractTuleapField#getDefaultValue()
	 */
	@Override
	public Object getDefaultValue() {
		return ""; //$NON-NLS-1$
	}

	/**
	 * Sets to <code>true</code> to indicate that this field is the semantic title of the tracker.
	 *
	 * @param isTitle
	 *            Indicates if the field is the title of the tracker.
	 */
	public void setSemanticTitle(boolean isTitle) {
		this.isSemanticTitle = isTitle;
	}

	/**
	 * Returns <code>true</code> if the field is the semantic title of the tracker, <code>false</code>
	 * otherwise.
	 *
	 * @return <code>true</code> if the field is the semantic title of the tracker, <code>false</code>
	 *         otherwise.
	 */
	public boolean isSemanticTitle() {
		return this.isSemanticTitle;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.core.internal.model.config.AbstractTuleapField#createTaskAttribute(org.eclipse.mylyn.tasks.core.data.TaskAttribute)
	 */
	@Override
	public TaskAttribute createTaskAttribute(TaskAttribute parent) {
		if (isSemanticTitle()) {
			return createSummaryTaskAttribute(parent);
		}
		return super.createTaskAttribute(parent);
	}

	/**
	 * Creates the task attribute representing the summary.
	 *
	 * @param parent
	 *            The parent task attribute
	 * @return The created task attribute.
	 */
	private TaskAttribute createSummaryTaskAttribute(TaskAttribute parent) {
		// String with semantic title
		TaskAttribute attribute = parent.createAttribute(TaskAttribute.SUMMARY);
		// Attributes
		TaskAttributeMetaData attributeMetadata = attribute.getMetaData();
		attributeMetadata.setType(getMetadataType());
		attributeMetadata.setLabel(getLabel());
		return attribute;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.core.internal.model.config.AbstractTuleapField#setValue(org.eclipse.mylyn.tasks.core.data.TaskAttribute,
	 *      org.tuleap.mylyn.task.core.internal.model.data.AbstractFieldValue)
	 */
	@Override
	public void setValue(TaskAttribute attribute, AbstractFieldValue value) {
		Assert.isTrue(value instanceof LiteralFieldValue);
		attribute.setValue(((LiteralFieldValue)value).getFieldValue());
	}

	@Override
	public void accept(ITuleapFieldVisitor visitor) {
		visitor.visit(this);
	}
}
