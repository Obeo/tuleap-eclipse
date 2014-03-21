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
package org.eclipse.mylyn.tuleap.core.internal.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.mylyn.tuleap.core.internal.model.data.AttachmentFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AttachmentValue;
import org.eclipse.mylyn.tuleap.core.internal.util.ITuleapConstants;

/**
 * Json serializer for {@link AttachmentFieldValue}.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class AttachmentFieldValueAdapter implements JsonSerializer<AttachmentFieldValue>, JsonDeserializer<AttachmentFieldValue> {

	/**
	 * {@inheritDoc}
	 *
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type,
	 *      com.google.gson.JsonSerializationContext)
	 */
	public JsonElement serialize(AttachmentFieldValue src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		if (src.getAttachments() != null && src.getAttachments().size() > 0) {
			result.add(ITuleapConstants.FIELD_ID, new JsonPrimitive(Integer.valueOf(src.getFieldId())));
			JsonArray fileDescriptions = new JsonArray();
			result.add(ITuleapConstants.FIELD_VALUE, fileDescriptions);
			for (AttachmentValue attachmentValue : src.getAttachments()) {
				fileDescriptions.add(new JsonPrimitive(Integer.valueOf(attachmentValue.getId())));
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	public AttachmentFieldValue deserialize(JsonElement element, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = element.getAsJsonObject();
		int fieldId = jsonObject.get("field_id").getAsInt(); //$NON-NLS-1$
		List<AttachmentValue> fileDescriptions = new ArrayList<AttachmentValue>();
		for (JsonElement fileDescElt : jsonObject.get("file_descriptions").getAsJsonArray()) { //$NON-NLS-1$
			fileDescriptions.add((AttachmentValue)context.deserialize(fileDescElt, AttachmentValue.class));
		}
		return new AttachmentFieldValue(fieldId, fileDescriptions);
	}

}
