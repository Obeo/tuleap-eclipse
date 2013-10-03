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

import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * This class is used to serialize the JSON representation of a milestone.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapMilestoneSerializer extends AbstractTuleapSerializer<TuleapMilestone> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type,
	 *      com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(TuleapMilestone milestone, Type type,
			JsonSerializationContext jsonSerializationContext) {

		JsonObject milestoneObject = new JsonObject();
		milestoneObject = (JsonObject)super.serialize(milestone, type, jsonSerializationContext);
		milestoneObject.add(ITuleapConstants.MILESTONE_TYPE_ID, new JsonPrimitive(Integer.valueOf(milestone
				.getConfigurationId())));
		if (milestone.getParentMilestoneId() != TuleapMilestone.INVALID_PARENT_MILESTONE_ID) {
			milestoneObject.add(ITuleapConstants.PARENT_MILESTONE_ID, new JsonPrimitive(Integer
					.valueOf(milestone.getParentMilestoneId())));
		}
		if (milestone.getStartDate() != null) {
			milestoneObject.add(ITuleapConstants.START_DATE, new JsonPrimitive(dateFormat.format(milestone
					.getStartDate())));
		}
		if (milestone.getDuration() != null) {
			milestoneObject.add(ITuleapConstants.DURATION, new JsonPrimitive(milestone.getDuration()));
		}
		if (milestone.getCapacity() != null) {
			milestoneObject.add(ITuleapConstants.CAPACITY, new JsonPrimitive(milestone.getCapacity()));
		}
		if (milestone.getNewComment() != null) {
			JsonObject comment = new JsonObject();
			milestoneObject.add(ITuleapConstants.COMMENT, comment);
			comment.add(ITuleapConstants.BODY, new JsonPrimitive(milestone.getNewComment()));
		}
		return milestoneObject;
	}
}
