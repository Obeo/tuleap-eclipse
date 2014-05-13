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
package org.tuleap.mylyn.task.core.internal.parser;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import org.tuleap.mylyn.task.core.internal.model.data.AbstractTuleapProjectElement;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapReference;
import org.tuleap.mylyn.task.core.internal.util.ITuleapConstants;

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
public abstract class AbstractProjectElementDeserializer<U, T extends AbstractTuleapProjectElement<U>> implements JsonDeserializer<T> {

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

		String id = jsonObject.get(ITuleapConstants.ID).getAsString();
		T pojo = getPrototype(id);

		JsonElement jsonElement = jsonObject.get(ITuleapConstants.LABEL);
		if (jsonElement != null && !jsonElement.isJsonNull()) {
			pojo.setLabel(jsonElement.getAsString());
		}
		TuleapReference project = context.deserialize(jsonObject.get(ITuleapConstants.JSON_PROJECT),
				TuleapReference.class);
		JsonElement element = jsonObject.get(ITuleapConstants.URI);
		if (element != null && !element.isJsonNull()) {
			pojo.setUri(element.getAsString());
		}

		pojo.setProject(project);
		return pojo;
	}

	/**
	 * Instantiates the relevant class of POJO to fill.
	 *
	 * @param jsonId
	 *            the value of the id found in JSON, as a string.
	 * @return The POJO.
	 */
	protected abstract T getPrototype(String jsonId);

}
