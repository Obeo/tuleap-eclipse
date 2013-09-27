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
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;

import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;

/**
 * This class is used to serialize the JSON representation of a backlogItem.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapBacklogItemSerializer extends AbstractTuleapSerializer<TuleapBacklogItem> {

	/**
	 * The key used to retrieve the type id of the backlogItem.
	 */
	private static final String TYPE_ID = "backlog_item_type_id"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the initial effort of the backlogItem.
	 */
	private static final String INITIAL_EFFORT = "initial_effort"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the backlogItem assigned milestone identifier.
	 */
	private static final String ASSIGNED_MILESTONE_ID = "assigned_milestone_id"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type,
	 *      com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(TuleapBacklogItem backlogItem, Type type,
			JsonSerializationContext jsonSerializationContext) {

		JsonObject backlogItemObject = new JsonObject();

		// Serialize the object's simple attributes
		// manageSimpleAttributes(milestoneObject, milestone);
		backlogItemObject = (JsonObject)super.serialize(backlogItem, type, jsonSerializationContext);
		// Serialize the object's complex attributes
		backlogItemObject.add(INITIAL_EFFORT,
				new JsonPrimitive(Float.valueOf(backlogItem.getInitialEffort())));
		backlogItemObject.add(TYPE_ID, new JsonPrimitive(Integer.valueOf(backlogItem.getTypeId())));
		backlogItemObject.add(ASSIGNED_MILESTONE_ID, new JsonPrimitive(backlogItem.getAssignedMilestoneId()));

		return backlogItemObject;
	}

}
