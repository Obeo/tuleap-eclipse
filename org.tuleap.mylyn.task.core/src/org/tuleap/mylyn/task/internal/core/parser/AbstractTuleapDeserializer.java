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
package org.tuleap.mylyn.task.internal.core.parser;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.tuleap.mylyn.task.internal.core.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableElement;
import org.tuleap.mylyn.task.internal.core.model.TuleapReference;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * This class is used to deserialize a JSON representation of a Tuleap object, for objects that have a
 * configuration ID (top-plannings, for instance, have no configuration ID).
 * 
 * @param <T>
 *            The type of the agile element to deserialize.
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public abstract class AbstractTuleapDeserializer<T extends AbstractTuleapConfigurableElement> extends AbstractDetailedElementDeserializer<T> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public T deserialize(JsonElement rootJsonElement, Type type,
			JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		JsonObject jsonObject = rootJsonElement.getAsJsonObject();
		T pojo = super.deserialize(rootJsonElement, type, jsonDeserializationContext);
		JsonElement jsonTrackerRef = jsonObject.get(ITuleapConstants.JSON_TRACKER);
		TuleapReference trackerRef = new Gson().fromJson(jsonTrackerRef, TuleapReference.class);
		pojo.setTracker(trackerRef);

		JsonArray fields = jsonObject.get(ITuleapConstants.VALUES).getAsJsonArray();
		for (JsonElement field : fields) {
			JsonObject jsonField = field.getAsJsonObject();
			int fieldId = jsonField.get(ITuleapConstants.FIELD_ID).getAsInt();
			JsonElement jsonValue = jsonField.get(ITuleapConstants.FIELD_VALUE);
			if (jsonValue != null) {
				if (jsonValue.isJsonPrimitive()) {
					JsonPrimitive primitive = jsonValue.getAsJsonPrimitive();
					if (primitive.isString()) {
						pojo.addFieldValue(new LiteralFieldValue(fieldId, primitive.getAsString()));
					} else if (primitive.isNumber()) {
						pojo.addFieldValue(new LiteralFieldValue(fieldId, String.valueOf(primitive
								.getAsNumber())));
					} else if (primitive.isBoolean()) {
						pojo.addFieldValue(new LiteralFieldValue(fieldId, String.valueOf(primitive
								.getAsBoolean())));
					}
				}
			} else {
				JsonElement jsonBindValueId = jsonField.get(ITuleapConstants.FIELD_BIND_VALUE_ID);
				if (jsonBindValueId != null) {
					int bindValueId = jsonBindValueId.getAsInt();
					pojo.addFieldValue(new BoundFieldValue(fieldId, Lists.newArrayList(Integer
							.valueOf(bindValueId))));
				} else {
					JsonElement jsonBindValueIds = jsonField.get(ITuleapConstants.FIELD_BIND_VALUE_IDS);
					if (jsonBindValueIds != null) {
						JsonArray jsonIds = jsonBindValueIds.getAsJsonArray();
						List<Integer> bindValueIds = new ArrayList<Integer>();
						for (JsonElement idElement : jsonIds) {
							bindValueIds.add(Integer.valueOf(idElement.getAsInt()));
						}
						pojo.addFieldValue(new BoundFieldValue(fieldId, bindValueIds));
					} else {
						// TODO Files
					}
				}
			}
		}

		return pojo;
	}

}
