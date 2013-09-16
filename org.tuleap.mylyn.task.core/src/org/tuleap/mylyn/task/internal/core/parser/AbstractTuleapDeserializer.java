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

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;

import org.tuleap.mylyn.task.internal.core.model.agile.AbstractTuleapAgileElement;

/**
 * This class is used to deserialize a JSON representation of a Tuleap object.
 * 
 * @param <T>
 *            The type of the agile element to deserialize.
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public abstract class AbstractTuleapDeserializer<T extends AbstractTuleapAgileElement> implements JsonDeserializer<T> {

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
	 * The key used to retrieve the id of a field of the POJO.
	 */
	private static final String FIELD_ID = "field_id"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the value of a field of the POJO.
	 */
	private static final String FIELD_VALUE = "value"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the bind value id.
	 */
	private static final String FIELD_BIND_VALUE_ID = "bind_value_id"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the list of bind value ids.
	 */
	private static final String FIELD_BIND_VALUE_IDS = "bind_value_ids"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	public T deserialize(JsonElement rootJsonElement, Type type,
			JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

		JsonObject jsonObject = rootJsonElement.getAsJsonObject();
		T pojo = buildPojo();

		int id = jsonObject.get(ID).getAsInt();
		pojo.setId(id);

		String label = jsonObject.get(LABEL).getAsString();
		pojo.setLabel(label);

		String url = jsonObject.get(URL).getAsString();
		pojo.setUrl(url);

		String htmlUrl = jsonObject.get(HTML_URL).getAsString();
		pojo.setHtmlUrl(htmlUrl);

		JsonArray fields = jsonObject.get(VALUES).getAsJsonArray();
		for (JsonElement field : fields) {
			JsonObject jsonField = field.getAsJsonObject();
			int fieldId = jsonField.get(FIELD_ID).getAsInt();
			JsonElement jsonValue = jsonField.get(FIELD_VALUE);
			if (jsonValue != null) {
				if (jsonValue.isJsonPrimitive()) {
					JsonPrimitive primitive = jsonValue.getAsJsonPrimitive();
					if (primitive.isString()) {
						pojo.getValues().put(Integer.valueOf(fieldId), primitive.getAsString());
					} else if (primitive.isNumber()) {
						pojo.getValues().put(Integer.valueOf(fieldId), primitive.getAsNumber());
					} else if (primitive.isBoolean()) {
						pojo.getValues().put(Integer.valueOf(fieldId),
								Boolean.valueOf(primitive.getAsBoolean()));
					}
				}
			} else {
				JsonElement jsonBindValueId = jsonField.get(FIELD_BIND_VALUE_ID);
				if (jsonBindValueId != null) {
					int bindValueId = jsonBindValueId.getAsInt();
					pojo.getValues().put(Integer.valueOf(fieldId), Integer.valueOf(bindValueId));
				} else {
					JsonElement jsonBindValueIds = jsonField.get(FIELD_BIND_VALUE_IDS);
					if (jsonBindValueIds != null) {
						JsonArray jsonIds = jsonBindValueIds.getAsJsonArray();
						int[] ids = new int[jsonIds.size()];
						int i = 0;
						for (JsonElement idElement : jsonIds) {
							ids[i++] = idElement.getAsInt();
						}
						pojo.getValues().put(Integer.valueOf(fieldId), ids);
					} else {
						// TODO Files
					}
				}
			}
		}

		return pojo;
	}

	/**
	 * It instantiates the POJO to fill.
	 * 
	 * @return The POJO.
	 */
	protected abstract T buildPojo();

}
