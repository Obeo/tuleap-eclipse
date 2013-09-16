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
package org.tuleap.mylyn.task.internal.core.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import org.tuleap.mylyn.task.internal.core.model.agile.AbstractTuleapAgileElement;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;

/**
 * This class is used to deserialize the JSON representation of a tracker configuration.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class TuleapMilestoneDeserializer extends AbstractTuleapDeserializer {

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
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public AbstractTuleapAgileElement deserialize(JsonElement rootJsonElement, Type type,
			JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

		TuleapMilestone milestone = (TuleapMilestone)super.deserialize(rootJsonElement, type,
				jsonDeserializationContext);

		JsonObject jsonObject = rootJsonElement.getAsJsonObject();

		long startDate = jsonObject.get(START_DATE).getAsLong();
		milestone.setStartDate(startDate);

		float duration = jsonObject.get(DURATION).getAsFloat();
		milestone.setDuration(duration);

		float capacity = jsonObject.get(CAPACITY).getAsFloat();
		milestone.setCapacity(capacity);

		int typeId = jsonObject.get(TYPE_ID).getAsInt();
		milestone.setTypeId(typeId);

		JsonArray submilestones = jsonObject.get(SUBMILESTONES).getAsJsonArray();
		for (JsonElement submilestone : submilestones) {
			TuleapMilestone sub = (TuleapMilestone)deserialize(submilestone, type, jsonDeserializationContext);
			milestone.addSubMilestone(sub);
		}

		return milestone;

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.parser.AbstractTuleapDeserializer#buildPojo()
	 */
	@Override
	protected AbstractTuleapAgileElement buildPojo() {
		return new TuleapMilestone();
	}

}
