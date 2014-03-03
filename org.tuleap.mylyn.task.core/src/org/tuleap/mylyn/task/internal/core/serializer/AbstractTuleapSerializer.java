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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;

import org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.model.data.AbstractTuleapConfigurableElement;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * This class is used to serialize the JSON representation of a AbstractTuleapConfigurableElement.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 * @param <T>
 */
public abstract class AbstractTuleapSerializer<T extends AbstractTuleapConfigurableElement<?>> extends AbstractProjectElementSerializer<T> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type,
	 *      com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(T element, Type type, JsonSerializationContext context) {
		JsonObject json = super.serialize(element, type, context).getAsJsonObject();
		JsonArray values = new JsonArray();
		json.add(ITuleapConstants.VALUES, values);
		for (AbstractTuleapField field : element.getFields()) {
			if (mustSerialize(field)) {
				AbstractFieldValue fieldValue = element.getFieldValue(field.getIdentifier());
				values.add(context.serialize(fieldValue));
			}
		}
		return json;
	}

	/**
	 * Override this method in subclasses to configure which field are serialized. By default, all fieds are
	 * serialized.
	 * 
	 * @param field
	 *            A field present in the object to serialize.
	 * @return <code>true</code> if and only if the field must be included in the serialization.
	 */
	protected boolean mustSerialize(AbstractTuleapField field) {
		return true;
	}
}
