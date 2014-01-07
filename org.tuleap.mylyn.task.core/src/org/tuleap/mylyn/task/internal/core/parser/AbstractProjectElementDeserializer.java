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

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import org.tuleap.mylyn.task.internal.core.model.data.AbstractTuleapProjectElement;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * This class is used to deserialize a JSON representation of a Tuleap object, for objects that have a tracker
 * ID (top-plannings, for instance, have no tracker ID).
 * 
 * @param <T>
 *            The type of the agile element to deserialize.
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public abstract class AbstractProjectElementDeserializer<T extends AbstractTuleapProjectElement> implements JsonDeserializer<T> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	public T deserialize(JsonElement rootJsonElement, Type type,
			JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

		JsonObject jsonObject = rootJsonElement.getAsJsonObject();

		T pojo = getPrototype();

		String id = jsonObject.get(ITuleapConstants.ID).getAsString();
		JsonElement jsonElement = jsonObject.get(ITuleapConstants.LABEL);
		if (jsonElement != null && !jsonElement.isJsonNull()) {
			pojo.setLabel(jsonElement.getAsString());
		}
		TuleapReference project = new Gson().fromJson(jsonObject.get(ITuleapConstants.JSON_PROJECT),
				TuleapReference.class);
		JsonElement element = jsonObject.get(ITuleapConstants.URI);
		if (element != null && !element.isJsonNull()) {
			pojo.setUri(element.getAsString());
		}

		pojo.setId(id);
		pojo.setProject(project);
		return pojo;
	}

	/**
	 * Instantiates the relevant class of POJO to fill.
	 * 
	 * @return The POJO.
	 */
	protected abstract T getPrototype();

}
