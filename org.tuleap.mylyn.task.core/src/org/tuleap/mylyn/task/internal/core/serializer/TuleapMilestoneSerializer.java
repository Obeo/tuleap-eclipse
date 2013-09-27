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

import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;

/**
 * This class is used to serialize the JSON representation of a milestone.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapMilestoneSerializer extends AbstractTuleapSerializer<TuleapMilestone> {

	/**
	 * The key used to retrieve the start date of the milestone.
	 */
	private static final String START_DATE = "start_date"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the duration of the milestone.
	 */
	private static final String DURATION = "duration"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the capacity of the milestone.
	 */
	private static final String CAPACITY = "capacity"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the type id of the milestone.
	 */
	private static final String TYPE_ID = "milestone_type_id"; //$NON-NLS-1$

	/**
	 * The key used to retrieve the sub-milestones.
	 */
	private static final String SUBMILESTONES = "submilestones"; //$NON-NLS-1$

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

		// Serialize the object's simple attributes
		// manageSimpleAttributes(milestoneObject, milestone);
		milestoneObject = (JsonObject)super.serialize(milestone, type, jsonSerializationContext);
		// Serialize the object's complex attributes
		if (milestone.getStartDate() != null) {
			milestoneObject.add(START_DATE, new JsonPrimitive(dateFormat.format(milestone.getStartDate())));
		}
		milestoneObject.add(DURATION, new JsonPrimitive(Float.valueOf(milestone.getDuration())));

		milestoneObject.add(CAPACITY, new JsonPrimitive(Float.valueOf(milestone.getCapacity())));
		milestoneObject.add(TYPE_ID, new JsonPrimitive(Integer.valueOf(milestone.getTypeId())));

		JsonElement subMilestones = new JsonArray();

		// if the milestone has Submilestones, we create the "submilestones" JSON attribute
		if (milestone.getSubMilestones().size() > 0) {
			milestoneObject.add(SUBMILESTONES, subMilestones);
		}

		for (TuleapMilestone subMilestone : milestone.getSubMilestones()) {
			JsonObject submilestoneObject = (JsonObject)this.serialize(subMilestone, type,
					jsonSerializationContext);
			subMilestones.getAsJsonArray().add(submilestoneObject);
		}

		return milestoneObject;
	}
}
