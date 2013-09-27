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

/**
 * This class is used to serialize the JSON representation of a AbstractTuleapConfigurableElement.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 * @param <T>
 */
public abstract class AbstractTuleapSerializer<T extends AbstractTuleapConfigurableElement> implements JsonSerializer<T> {

	/**
	 * The key used for the id of the POJO.
	 */
	private static final String ID = "id"; //$NON-NLS-1$

	/**
	 * The key used for the label of the POJO.
	 */
	private static final String LABEL = "label"; //$NON-NLS-1$

	/**
	 * The key used for the URL of the POJO.
	 */
	private static final String URL = "url"; //$NON-NLS-1$

	/**
	 * The key used for the HTML URL of the POJO.
	 */
	private static final String HTML_URL = "html_url"; //$NON-NLS-1$

	/**
	 * The key used for the field values of the POJO.
	 */
	private static final String VALUES = "values"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the field identifier of the POJO.
	 */
	private static final String FIELD_ID = "field_id"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the field value of the POJO.
	 */
	private static final String FIELD_VALUE = "value"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the bind value identifier.
	 */
	private static final String FIELD_BIND_VALUE_ID = "bind_value_id"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the list of bind value identifiers.
	 */
	private static final String FIELD_BIND_VALUE_IDS = "bind_value_ids"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the list of file descriptions.
	 */
	private static final String FILE_DESCRIPTIONS = "file_descriptions"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the file identifier.
	 */
	private static final String FILE_ID = "file_id"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the person that submit the file.
	 */
	private static final String SUBMITTED_BY = "submitted_by"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the file description.
	 */
	private static final String DESCRIPTION = "description"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the file name.
	 */
	private static final String NAME = "name"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the file size.
	 */
	private static final String SIZE = "size"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the file type.
	 */
	private static final String TYPE = "type"; //$NON-NLS-1$

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
		this.manageCommonAttributes(abstractJsonObject, element);

		JsonElement values = new JsonArray();
		if (element.getFieldValues().size() > 0) {
			abstractJsonObject.add(VALUES, values);
		}
		for (AbstractFieldValue field : element.getFieldValues()) {
			JsonObject fieldObject = new JsonObject();
			fieldObject.add(FIELD_ID, new JsonPrimitive(Integer.valueOf(field.getFieldId())));

			if (field instanceof LiteralFieldValue) {
				fieldObject.add(FIELD_VALUE, new JsonPrimitive(((LiteralFieldValue)field).getFieldValue()));
			} else if (field instanceof BoundFieldValue) {
				BoundFieldValue boundFieldValue = (BoundFieldValue)field;
				if (boundFieldValue.getValueIds().size() == 1) {
					fieldObject.add(FIELD_BIND_VALUE_ID, new JsonPrimitive(boundFieldValue.getValueIds().get(
							0)));
				} else if (boundFieldValue.getValueIds().size() > 1) {
					JsonElement fieldValues = new JsonArray();
					for (Integer theValue : boundFieldValue.getValueIds()) {
						fieldValues.getAsJsonArray().add(new JsonPrimitive(theValue));
					}
					fieldObject.add(FIELD_BIND_VALUE_IDS, fieldValues);
				}
			} else if (field instanceof AttachmentFieldValue) {

				AttachmentFieldValue attachementFieldValue = (AttachmentFieldValue)field;

				if (attachementFieldValue.getAttachments().size() > 0) {
					JsonElement fileDescriptions = new JsonArray();
					fieldObject.add(FILE_DESCRIPTIONS, fileDescriptions);
					for (AttachmentValue attachmentValue : attachementFieldValue.getAttachments()) {
						JsonObject fileDescritonObject = new JsonObject();
						fileDescritonObject.add(FILE_ID, new JsonPrimitive(Integer.valueOf(attachmentValue
								.getAttachmentId())));
						fileDescritonObject.add(SUBMITTED_BY, new JsonPrimitive(Integer
								.valueOf(attachmentValue.getPerson().getId())));
						fileDescritonObject.add(DESCRIPTION, new JsonPrimitive(attachmentValue
								.getDescription()));
						fileDescritonObject.add(NAME, new JsonPrimitive(attachmentValue.getFilename()));
						fileDescritonObject.add(SIZE, new JsonPrimitive(Integer.valueOf(attachmentValue
								.getSize())));
						fileDescritonObject.add(TYPE, new JsonPrimitive(attachmentValue.getContentType()));
						fileDescriptions.getAsJsonArray().add(fileDescritonObject);
					}
				}
			}
			values.getAsJsonArray().add(fieldObject);
		}
		return abstractJsonObject;
	}

	/**
	 * Manage the common attributes of the created JSON objects.
	 * 
	 * @param abstractJsonObject
	 *            the JSON object to create
	 * @param abstractelement
	 *            the abstractElement to serialize
	 */
	private void manageCommonAttributes(JsonObject abstractJsonObject,
			AbstractTuleapConfigurableElement abstractelement) {

		abstractJsonObject.add(ID, new JsonPrimitive(Integer.valueOf(abstractelement.getId())));
		if (abstractelement.getLabel() != null) {
			abstractJsonObject.add(LABEL, new JsonPrimitive(abstractelement.getLabel()));
		}
		if (abstractelement.getLabel() != null) {
			abstractJsonObject.add(URL, new JsonPrimitive(abstractelement.getLabel()));
		}
		if (abstractelement.getHtmlUrl() != null) {
			abstractJsonObject.add(HTML_URL, new JsonPrimitive(abstractelement.getHtmlUrl()));
		}
	}
}
