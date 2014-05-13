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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import org.tuleap.mylyn.task.core.internal.model.data.AbstractTuleapProjectElement;
import org.tuleap.mylyn.task.core.internal.util.ITuleapConstants;

/**
 * This class is used to serialize the JSON representation of a AbstractTuleapConfigurableElement.
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 * @param <T>
 */
public abstract class AbstractProjectElementSerializer<T extends AbstractTuleapProjectElement<?>> implements JsonSerializer<T> {

	/**
	 * The pattern used to format date following the ISO8601 standard.
	 */
	protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 *
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type,
	 *      com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(T element, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject json = new JsonObject();
		Object id = element.getId();
		if (id instanceof Number) {
			json.add(ITuleapConstants.ID, new JsonPrimitive((Number)element.getId()));
		} else if (id != null) {
			json.add(ITuleapConstants.ID, new JsonPrimitive(element.getId().toString()));
		}
		if (element.getLabel() != null) {
			json.add(ITuleapConstants.LABEL, new JsonPrimitive(element.getLabel()));
		}
		return json;
	}
}
