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
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.tuleap.mylyn.task.internal.core.model.data.AbstractTuleapConfigurableElement;
import org.tuleap.mylyn.task.internal.core.model.data.ArtifactLinkFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * This class is used to deserialize a JSON representation of a Tuleap object, for objects that have a tracker
 * ID (top-plannings, for instance, have no tracker ID).
 * 
 * @param <U>
 *            The type of ID of the element to deserialize.
 * @param <T>
 *            The type of the agile element to deserialize.
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public abstract class AbstractTuleapDeserializer<U, T extends AbstractTuleapConfigurableElement<U>> extends AbstractDetailedElementDeserializer<U, T> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public T deserialize(JsonElement rootJsonElement, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = rootJsonElement.getAsJsonObject();
		T pojo = super.deserialize(rootJsonElement, type, context);
		JsonElement jsonTrackerRef = jsonObject.get(ITuleapConstants.JSON_TRACKER);
		TuleapReference trackerRef = context.deserialize(jsonTrackerRef, TuleapReference.class);
		pojo.setTracker(trackerRef);

		JsonArray fields = jsonObject.get(ITuleapConstants.VALUES).getAsJsonArray();
		for (JsonElement field : fields) {
			JsonObject jsonField = field.getAsJsonObject();
			int fieldId = jsonField.get(ITuleapConstants.FIELD_ID).getAsInt();
			if (jsonField.has(ITuleapConstants.FIELD_VALUE)) {
				JsonElement jsonValue = jsonField.get(ITuleapConstants.FIELD_VALUE);
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
			} else if (jsonField.has(ITuleapConstants.FIELD_BIND_VALUE_ID)) {
				JsonElement jsonBindValueId = jsonField.get(ITuleapConstants.FIELD_BIND_VALUE_ID);
				int bindValueId = jsonBindValueId.getAsInt();
				pojo.addFieldValue(new BoundFieldValue(fieldId, Lists.newArrayList(Integer
						.valueOf(bindValueId))));
			} else if (jsonField.has(ITuleapConstants.FIELD_BIND_VALUE_IDS)) {
				JsonElement jsonBindValueIds = jsonField.get(ITuleapConstants.FIELD_BIND_VALUE_IDS);
				JsonArray jsonIds = jsonBindValueIds.getAsJsonArray();
				List<Integer> bindValueIds = new ArrayList<Integer>();
				for (JsonElement idElement : jsonIds) {
					bindValueIds.add(Integer.valueOf(idElement.getAsInt()));
				}
				pojo.addFieldValue(new BoundFieldValue(fieldId, bindValueIds));
			} else if (jsonField.has(ITuleapConstants.FIELD_LINKS)) {
				// Artifact links
				pojo.addFieldValue(context.<ArtifactLinkFieldValue> deserialize(jsonField,
						ArtifactLinkFieldValue.class));
			}

		}

		return pojo;
	}

}
