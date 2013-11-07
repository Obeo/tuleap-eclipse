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

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;

import org.tuleap.mylyn.task.internal.core.model.TuleapReference;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * This class is used to deserialize the JSON representation of a tracker configuration.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapMilestoneDeserializer extends AbstractDetailedElementDeserializer<TuleapMilestone> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public TuleapMilestone deserialize(JsonElement rootJsonElement, Type type,
			JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		TuleapMilestone milestone = super.deserialize(rootJsonElement, type, jsonDeserializationContext);

		JsonObject jsonObject = rootJsonElement.getAsJsonObject();

		milestone.setArtifact(new Gson().fromJson(jsonObject.get(ITuleapConstants.JSON_ARTIFACT),
				TuleapReference.class));

		JsonElement elt = jsonObject.get(ITuleapConstants.START_DATE);
		if (elt != null) {
			String startDate = elt.getAsString();
			try {
				milestone.setStartDate(dateFormat.parse(startDate));
			} catch (ParseException e) {
				// TODO manage the exception properly
			}
		}

		elt = jsonObject.get(ITuleapConstants.END_DATE);
		if (elt != null) {
			String endDate = elt.getAsString();
			try {
				milestone.setEndDate(dateFormat.parse(endDate));
			} catch (ParseException e) {
				// TODO manage the exception properly
			}
		}

		elt = jsonObject.get(ITuleapConstants.CAPACITY);
		if (elt != null) {
			Float capacity = Float.valueOf(elt.getAsFloat());
			milestone.setCapacity(capacity);
		}

		elt = jsonObject.get(ITuleapConstants.JSON_STATUS_LABEL);
		if (elt != null) {
			milestone.setStatusLabel(elt.getAsString());
		}

		elt = jsonObject.get(ITuleapConstants.JSON_SUB_MILESTONES_URI);
		if (elt != null) {
			milestone.setSubMilestonesUri(elt.getAsString());
		}

		elt = jsonObject.get(ITuleapConstants.JSON_BACKLOG_ITEMS_URI);
		if (elt != null) {
			milestone.setBacklogItemsUri(elt.getAsString());
		}

		return milestone;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.parser.AbstractProjectElementDeserializer#getPrototype()
	 */
	@Override
	protected TuleapMilestone getPrototype() {
		return new TuleapMilestone();
	}

}
