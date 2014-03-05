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
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import org.tuleap.mylyn.task.internal.core.model.data.ArtifactLinkFieldValue;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * Json serializer for {@link ArtifactLinkFieldValue}.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class ArtifactLinkFieldValueAdapter implements JsonSerializer<ArtifactLinkFieldValue>, JsonDeserializer<ArtifactLinkFieldValue> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	public ArtifactLinkFieldValue deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		JsonElement idElem = jsonObject.get(ITuleapConstants.FIELD_ID);
		JsonElement linksElem = jsonObject.get(ITuleapConstants.FIELD_LINKS);
		int fieldId = idElem.getAsInt();
		int[] values;
		if (linksElem.isJsonArray()) {
			JsonArray array = linksElem.getAsJsonArray();
			values = new int[array.size()];
			int i = 0;
			for (JsonElement elem : array) {
				values[i++] = elem.getAsJsonObject().get(ITuleapConstants.ID).getAsInt();
			}
		} else {
			values = new int[0];
		}
		ArtifactLinkFieldValue result = new ArtifactLinkFieldValue(fieldId, values);
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type,
	 *      com.google.gson.JsonSerializationContext)
	 */
	public JsonElement serialize(ArtifactLinkFieldValue src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		result.add(ITuleapConstants.FIELD_ID, new JsonPrimitive(Integer.valueOf(src.getFieldId())));
		JsonArray arr = new JsonArray();
		for (int value : src.getLinks()) {
			arr.add(new JsonPrimitive(Integer.valueOf(value)));
		}
		result.add(ITuleapConstants.FIELD_LINKS, arr);
		return result;
	}
}
