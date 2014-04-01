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

import java.text.ParseException;
import java.util.Date;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tuleap.core.internal.TuleapCoreActivator;
import org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AbstractFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.LiteralFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.parser.DateIso8601Adapter;
import org.eclipse.mylyn.tuleap.core.internal.util.TuleapMylynTasksMessages;
import org.eclipse.mylyn.tuleap.core.internal.util.TuleapMylynTasksMessagesKeys;

/**
 * The Tuleap date field.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapDate extends AbstractTuleapField {

	/**
	 * The serialization ID.
	 */
	private static final long serialVersionUID = -5647719423805755975L;

	/**
	 * The constructor.
	 *
	 * @param formElementIdentifier
	 *            The identifier of the form element
	 */
	public TuleapDate(int formElementIdentifier) {
		super(formElementIdentifier);
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
	 * @see org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField#getMetadataType()
	 */
	@Override
	public String getMetadataType() {
		return TaskAttribute.TYPE_DATE;
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
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField#createFieldValue(org.eclipse.mylyn.tasks.core.data.TaskAttribute,
	 *      int)
	 */
	@Override
	public AbstractFieldValue createFieldValue(TaskAttribute attribute, int fieldId) {
		String value = toIso8601Date(attribute);
		return new LiteralFieldValue(fieldId, value);
	}

	/**
	 * Parse a string to get date.
	 *
	 * @param attribute
	 *            The task Attribute
	 * @return the date
	 */
	private String toIso8601Date(TaskAttribute attribute) {
		String result = ""; //$NON-NLS-1$
		String attributeValue = attribute.getValue();
		try {
			if (!attributeValue.isEmpty()) {
				long date = Long.parseLong(attributeValue);
				result = DateIso8601Adapter.toIso8601String(new Date(date));
			}
		} catch (NumberFormatException e) {
			String messageToLog = TuleapMylynTasksMessages.getString(
					TuleapMylynTasksMessagesKeys.dateParsingLogMessage, attributeValue, attribute
							.getMetaData().getLabel());
			TuleapCoreActivator.log(messageToLog, false);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField#setValue(org.eclipse.mylyn.tasks.core.data.TaskAttribute,
	 *      org.eclipse.mylyn.tuleap.core.internal.model.data.AbstractFieldValue)
	 */
	@Override
	public void setValue(TaskAttribute attribute, AbstractFieldValue value) {
		Assert.isTrue(value instanceof LiteralFieldValue);
		try {
			attribute.setValue(Long.toString(DateIso8601Adapter.parseIso8601Date(
					((LiteralFieldValue)value).getFieldValue()).getTime()));
		} catch (ParseException e) {
			TuleapCoreActivator.log(new Status(IStatus.ERROR, TuleapCoreActivator.PLUGIN_ID,
					TuleapMylynTasksMessages.getString(TuleapMylynTasksMessagesKeys.dateParsingLogMessage,
							((LiteralFieldValue)value).getFieldValue(), attribute.getMetaData().getLabel())));
		}
	}

	@Override
	public void accept(ITuleapFieldVisitor visitor) {
		visitor.visit(this);
	}
}
