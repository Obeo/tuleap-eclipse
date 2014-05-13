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
package org.tuleap.mylyn.task.core.internal.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import org.tuleap.mylyn.task.core.internal.model.data.BoundFieldValue;
import org.tuleap.mylyn.task.core.internal.util.ITuleapConstants;

/**
 * Json serializer for {@link BoundFieldValue}.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class BoundFieldValueSerializer implements JsonSerializer<BoundFieldValue> {

	/**
	 * {@inheritDoc}
	 *
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type,
	 *      com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(BoundFieldValue src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		result.add(ITuleapConstants.FIELD_ID, new JsonPrimitive(Integer.valueOf(src.getFieldId())));
		JsonElement fieldValues = new JsonArray();
		for (Integer valueId : src.getValueIds()) {
			fieldValues.getAsJsonArray().add(new JsonPrimitive(valueId));
		}
		result.add(ITuleapConstants.FIELD_BIND_VALUE_IDS, fieldValues);
		return result;
	}

}
