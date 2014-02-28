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

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;

import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapCard;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * This class is used to serialize the JSON representation of a card.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapCardSerializer extends AbstractTuleapSerializer<TuleapCard> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type,
	 *      com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(TuleapCard tuleapCard, Type type, JsonSerializationContext context) {
		JsonObject elementObject = (JsonObject)super.serialize(tuleapCard, TuleapCard.class, context);

		elementObject.remove(ITuleapConstants.ID);
		if (tuleapCard.getLabel() != null) {
			elementObject.add(ITuleapConstants.LABEL, new JsonPrimitive(tuleapCard.getLabel()));
		} else {
			// This will probably be problematic because that label probably cannot be null
			elementObject.add(ITuleapConstants.LABEL, JsonNull.INSTANCE);
		}
		if (tuleapCard.getColumnId() != null) {
			elementObject.add(ITuleapConstants.COLUMN_ID, new JsonPrimitive(tuleapCard.getColumnId()));
		} else {
			elementObject.add(ITuleapConstants.COLUMN_ID, JsonNull.INSTANCE);
		}
		return elementObject;

	}

}
