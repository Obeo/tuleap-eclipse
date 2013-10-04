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
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.tuleap.mylyn.task.internal.core.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableElement;
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
public abstract class AbstractTuleapDeserializer<T extends AbstractTuleapConfigurableElement> implements JsonDeserializer<T> {

	/**
	 * The pattern used to format date following the ISO8601 standard.
	 */
	protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	public T deserialize(JsonElement rootJsonElement, Type type,
			JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

		JsonObject jsonObject = rootJsonElement.getAsJsonObject();

		int id = jsonObject.get(ITuleapConstants.ID).getAsInt();
		int projectId = jsonObject.get(ITuleapConstants.PROJECT_ID).getAsInt();
		String label = jsonObject.get(ITuleapConstants.LABEL).getAsString();
		String url = jsonObject.get(ITuleapConstants.URL).getAsString();
		String htmlUrl = jsonObject.get(ITuleapConstants.HTML_URL).getAsString();
		int configurationId = jsonObject.get(getTypeIdKey()).getAsInt();

		// TODO Fix the dates from the parsing
		T pojo = buildPojo(id, configurationId, projectId, label, url, htmlUrl, new Date(), new Date());

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

	/**
	 * Instantiates the relevant class of POJO to fill.
	 * 
	 * @param id
	 *            The identifier
	 * @param configurationId
	 *            The identifier of the configuration
	 * @param projectId
	 *            The identifier of the project
	 * @param label
	 *            The label
	 * @param url
	 *            The url
	 * @param htmlUrl
	 *            The HTML URL
	 * @param creationDate
	 *            The creation date
	 * @param lastModificationDate
	 *            The last modification date
	 * @return The POJO.
	 */
	// CHECKSTYLE:OFF
	protected abstract T buildPojo(int id, int configurationId, int projectId, String label, String url,
			String htmlUrl, Date creationDate, Date lastModificationDate);

	// CHECKSTYLE:ON

	/**
	 * Returns the key of the type id.
	 * 
	 * @return The key of the type id
	 */
	protected abstract String getTypeIdKey();

}
