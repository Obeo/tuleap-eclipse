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

import java.lang.reflect.Type;

import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapMilestone;
import org.tuleap.mylyn.task.core.internal.util.ITuleapConstants;

/**
 * This class is used to serialize the JSON representation of a milestone.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapMilestoneSerializer extends AbstractProjectElementSerializer<TuleapMilestone> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type,
	 *      com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(TuleapMilestone milestone, Type type, JsonSerializationContext context) {
		JsonObject milestoneObject = (JsonObject)super.serialize(milestone, type, context);
		if (milestone.getParent() != null) {
			milestoneObject.add(ITuleapConstants.JSON_PARENT, context.serialize(milestone.getParent()));
		}
		if (milestone.getStartDate() != null) {
			milestoneObject.add(ITuleapConstants.START_DATE, new JsonPrimitive(dateFormat.format(milestone
					.getStartDate())));
		}
		if (milestone.getEndDate() != null) {
			milestoneObject.add(ITuleapConstants.END_DATE, new JsonPrimitive(dateFormat.format(milestone
					.getEndDate())));
		}
		if (milestone.getCapacity() != null) {
			milestoneObject.add(ITuleapConstants.CAPACITY, new JsonPrimitive(milestone.getCapacity()));
		}
		return milestoneObject;
	}
}
