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
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapStatus;
import org.tuleap.mylyn.task.core.internal.util.TuleapCoreKeys;
import org.tuleap.mylyn.task.core.internal.util.TuleapCoreMessages;

/**
 * Status enumeration JSON Adapter.
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapStatusAdapter implements JsonDeserializer<TuleapStatus>, JsonSerializer<TuleapStatus> {

	/**
	 * {@inheritDoc}
	 *
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type,
	 *      com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(TuleapStatus src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.toString());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public TuleapStatus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		if (json.isJsonPrimitive()) {
			return valueOfIgnoreCase(TuleapStatus.class, json.getAsString());
		}
		throw new JsonParseException(TuleapCoreMessages.getString(TuleapCoreKeys.invalidStatus, json
				.toString()));
	}

	/**
	 * Finds the value of the given enumeration by name, case-insensitive. Throws an IllegalArgumentException
	 * if no match is found.
	 *
	 * @param enumeration
	 *            The enumeration class
	 * @param name
	 *            The string to convert
	 * @param <T>
	 *            The enumeration class
	 * @return The generated enumeration
	 */
	public static <T extends Enum<T>> T valueOfIgnoreCase(final Class<T> enumeration, final String name) {
		for (T enumValue : enumeration.getEnumConstants()) {
			if (enumValue.name().equalsIgnoreCase(name)) {
				return enumValue;
			}
		}
		throw new IllegalArgumentException("There is no value with name " + name + " in Enum " //$NON-NLS-1$//$NON-NLS-2$
				+ enumeration.getClass().getName());
	}

}
