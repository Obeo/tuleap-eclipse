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
package org.eclipse.mylyn.tuleap.core.internal.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import org.eclipse.mylyn.tuleap.core.internal.model.data.AttachmentFieldValue;
import org.eclipse.mylyn.tuleap.core.internal.model.data.AttachmentValue;
import org.eclipse.mylyn.tuleap.core.internal.util.ITuleapConstants;

/**
 * Json serializer for {@link AttachmentFieldValue}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class AttachmentFieldValueSerializer implements JsonSerializer<AttachmentFieldValue> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type,
	 *      com.google.gson.JsonSerializationContext)
	 */
	public JsonElement serialize(AttachmentFieldValue src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		result.add(ITuleapConstants.FIELD_ID, new JsonPrimitive(Integer.valueOf(src.getFieldId())));
		if (src.getAttachments().size() > 0) {
			JsonElement fileDescriptions = new JsonArray();
			result.add(ITuleapConstants.FILE_DESCRIPTIONS, fileDescriptions);
			for (AttachmentValue attachmentValue : src.getAttachments()) {
				JsonObject fileDescriptionObject = new JsonObject();
				fileDescriptionObject.add(ITuleapConstants.FILE_ID, new JsonPrimitive(Integer
						.valueOf(attachmentValue.getAttachmentId())));
				fileDescriptionObject.add(ITuleapConstants.DESCRIPTION, new JsonPrimitive(attachmentValue
						.getDescription()));
				fileDescriptions.getAsJsonArray().add(fileDescriptionObject);
			}
		}
		return result;
	}

}
