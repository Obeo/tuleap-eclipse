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

import java.lang.reflect.Type;
import java.util.Collection;

import org.tuleap.mylyn.task.internal.core.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * This class is used to serialize the JSON representation of a backlogItem.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapBacklogItemSerializer extends AbstractTuleapSerializer<TuleapBacklogItem> {

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

		// the field Values
		this.manageFields(backlogItem, backlogItemObject);

		// the backlogItem specific attributes
		if (backlogItem.getInitialEffort() != null) {
			backlogItemObject.add(ITuleapConstants.INITIAL_EFFORT, new JsonPrimitive(backlogItem
					.getInitialEffort()));
		}
		if (backlogItem.getAssignedMilestoneId() != null) {
			backlogItemObject.add(ITuleapConstants.ASSIGNED_MILESTONE_ID, new JsonPrimitive(backlogItem
					.getAssignedMilestoneId()));
		}
		return backlogItemObject;
	}

	/**
	 * Manage field values.
	 * 
	 * @param backlogItem
	 *            the backlogItem
	 * @param backlogItemObject
	 *            the field JSON Object
	 */
	private void manageFields(TuleapBacklogItem backlogItem, JsonObject backlogItemObject) {
		JsonElement values = new JsonArray();
		if (backlogItem.getFieldValues().size() > 0 && isContainingValidField(backlogItem.getFieldValues())) {
			backlogItemObject.add(ITuleapConstants.VALUES, values);
		}
		for (AbstractFieldValue field : backlogItem.getFieldValues()) {
			JsonObject fieldObject = new JsonObject();
			if (field instanceof LiteralFieldValue) {
				fieldObject.add(ITuleapConstants.FIELD_ID, new JsonPrimitive(Integer.valueOf(field
						.getFieldId())));
				fieldObject.add(ITuleapConstants.FIELD_VALUE, new JsonPrimitive(((LiteralFieldValue)field)
						.getFieldValue()));
			} else if (field instanceof BoundFieldValue) {
				fieldObject.add(ITuleapConstants.FIELD_ID, new JsonPrimitive(Integer.valueOf(field
						.getFieldId())));
				BoundFieldValue boundFieldValue = (BoundFieldValue)field;
				if (boundFieldValue.getValueIds().size() == 1) {
					fieldObject.add(ITuleapConstants.FIELD_BIND_VALUE_ID, new JsonPrimitive(boundFieldValue
							.getValueIds().get(0)));
				} else if (boundFieldValue.getValueIds().size() > 1) {
					JsonElement fieldValues = new JsonArray();
					for (Integer theValue : boundFieldValue.getValueIds()) {
						fieldValues.getAsJsonArray().add(new JsonPrimitive(theValue));
					}
					fieldObject.add(ITuleapConstants.FIELD_BIND_VALUE_IDS, fieldValues);
				}
			}
			values.getAsJsonArray().add(fieldObject);
		}
	}

	/**
	 * Utility method used to verify that a collection contains only objects that are instances of
	 * {@link LiteralFieldValue} and {@link BoundFieldValue}.
	 * 
	 * @param fieldValues
	 *            the collection to verify.
	 * @return <code>true</code> if the collection contains an object that is instance of
	 *         {@link LiteralFieldValue} and {@link BoundFieldValue}.
	 */
	private boolean isContainingValidField(Collection<AbstractFieldValue> fieldValues) {
		boolean result = false;
		for (Object field : fieldValues) {
			if (field instanceof LiteralFieldValue || field instanceof BoundFieldValue) {
				result = true;
				break;
			}
		}
		return result;
	}
}
