/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.internal.core.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import org.tuleap.mylyn.task.internal.core.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.data.AttachmentFieldValue;
import org.tuleap.mylyn.task.internal.core.data.AttachmentValue;
import org.tuleap.mylyn.task.internal.core.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableElement;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * This class is used to serialize the JSON representation of a AbstractTuleapConfigurableElement.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 * @param <T>
 */
public abstract class AbstractTuleapSerializer<T extends AbstractTuleapConfigurableElement> implements JsonSerializer<T> {

	/**
	 * The pattern used to format date following the ISO8601 standard.
	 */
	protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type,
	 *      com.google.gson.JsonSerializationContext)
	 */
	public JsonElement serialize(T element, Type type, JsonSerializationContext jsonSerializationContext) {

		JsonObject abstractJsonObject = new JsonObject();

		JsonElement values = new JsonArray();
		if (element.getFieldValues().size() > 0) {
			abstractJsonObject.add(ITuleapConstants.VALUES, values);
		}
		for (AbstractFieldValue field : element.getFieldValues()) {
			JsonObject fieldObject = new JsonObject();
			fieldObject
					.add(ITuleapConstants.FIELD_ID, new JsonPrimitive(Integer.valueOf(field.getFieldId())));

			if (field instanceof LiteralFieldValue) {
				fieldObject.add(ITuleapConstants.FIELD_VALUE, new JsonPrimitive(((LiteralFieldValue)field)
						.getFieldValue()));
			} else if (field instanceof BoundFieldValue) {
				BoundFieldValue boundFieldValue = (BoundFieldValue)field;
				if (boundFieldValue.getValueIds().size() == 1) {
					fieldObject.add(ITuleapConstants.FIELD_BIND_VALUE_ID, new JsonPrimitive(boundFieldValue
							.getValueIds().get(0)));
				} else if (boundFieldValue.getValueIds().size() > 1) {
					JsonElement fieldValues = new JsonArray();
					for (Integer theValue : boundFieldValue.getValueIds()) {
						fieldValues.getAsJsonArray().add(new JsonPrimitive(theValue));
					}
					fieldObject.add(ITuleapConstants.FIELD_BIND_VALUE_IDS, fieldValues);
				}
			} else if (field instanceof AttachmentFieldValue) {
				this.manageAttachmentFieldValues(field, fieldObject);
			}
			values.getAsJsonArray().add(fieldObject);
		}
		return abstractJsonObject;
	}

	/**
	 * Manage attachment field values.
	 * 
	 * @param field
	 *            the Abstract Field Value
	 * @param fieldObject
	 *            the parent field Object
	 */
	private void manageAttachmentFieldValues(AbstractFieldValue field, JsonObject fieldObject) {
		AttachmentFieldValue attachmentFieldValue = (AttachmentFieldValue)field;
		if (attachmentFieldValue.getAttachments().size() > 0) {
			JsonElement fileDescriptions = new JsonArray();
			fieldObject.add(ITuleapConstants.FILE_DESCRIPTIONS, fileDescriptions);
			for (AttachmentValue attachmentValue : attachmentFieldValue.getAttachments()) {
				JsonObject fileDescritonObject = new JsonObject();
				fileDescritonObject.add(ITuleapConstants.FILE_ID, new JsonPrimitive(Integer
						.valueOf(attachmentValue.getAttachmentId())));
				fileDescritonObject.add(ITuleapConstants.DESCRIPTION, new JsonPrimitive(attachmentValue
						.getDescription()));
				fileDescriptions.getAsJsonArray().add(fileDescritonObject);
			}
		}
	}

	/**
	 * Serialize only the generic information to update an {@link AbstractTuleapConfigurableElement}.
	 * 
	 * @param element
	 *            The element to serialize.
	 * @return the serialized JsonElement
	 */
	public JsonElement serializeUpdateFields(T element) {
		JsonObject elementObject = new JsonObject();
		elementObject = (JsonObject)this.serialize(element, null, null);
		elementObject.add(ITuleapConstants.ID, new JsonPrimitive(Integer.valueOf(element.getId())));
		if (element.getLabel() != null) {
			elementObject.add(ITuleapConstants.LABEL, new JsonPrimitive(element.getLabel()));
		}
		elementObject.add(ITuleapConstants.CONFIGURATION_ID, new JsonPrimitive(Integer.valueOf(element
				.getConfigurationId())));
		return elementObject;

	}
}
