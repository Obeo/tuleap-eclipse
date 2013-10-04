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
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.List;

import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * This class is used to serialize the JSON representation of a list of backlogItems.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapBacklogItemsSerializer implements JsonSerializer<List<TuleapBacklogItem>> {

	/**
	 * Serialize a list of backlogItems.
	 * 
	 * @param backlogItems
	 *            the list of backlogItems to serialize.
	 * @param type
	 *            the type
	 * @param jsonSerializationContext
	 *            the context
	 * @return the resulted JsonElement
	 */
	public JsonElement serialize(List<TuleapBacklogItem> backlogItems, Type type,
			JsonSerializationContext jsonSerializationContext) {
		JsonArray backlogItemsArray = new JsonArray();

		for (TuleapBacklogItem backlogItem : backlogItems) {
			JsonObject backlogItemObject = new JsonObject();
			backlogItemObject.add(ITuleapConstants.ID,
					new JsonPrimitive(Integer.valueOf(backlogItem.getId())));
			if (backlogItem.getAssignedMilestoneId() != null) {
				backlogItemObject.add(ITuleapConstants.ASSIGNED_MILESTONE_ID, new JsonPrimitive(backlogItem
						.getAssignedMilestoneId()));
			}
			backlogItemsArray.getAsJsonArray().add(backlogItemObject);
		}
		return backlogItemsArray;
	}
}
