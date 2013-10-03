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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import org.tuleap.mylyn.task.internal.core.model.agile.TuleapPlanningBinding;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapTopPlanning;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * This class is used to deserialize the JSON representation of a project configuration.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapTopPlanningDeserializer implements JsonDeserializer<TuleapTopPlanning> {

	/**
	 * {@inheritDoc}.
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	public TuleapTopPlanning deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {

		JsonObject jsonObject = json.getAsJsonObject();

		int id = jsonObject.get(ITuleapConstants.ID).getAsInt();
		String label = jsonObject.get(ITuleapConstants.LABEL).getAsString();
		String url = jsonObject.get(ITuleapConstants.URL).getAsString();
		String htmlUrl = jsonObject.get(ITuleapConstants.HTML_URL).getAsString();
		int projectId = jsonObject.get(ITuleapConstants.JSON_PROJECT_ID).getAsInt();

		int milestoneTypeId = jsonObject.get(ITuleapConstants.MILESTONE_TYPE_ID).getAsInt();
		int backlogItemTypeId = jsonObject.get(ITuleapConstants.BACKLOGITEM_TYPE_ID).getAsInt();
		TuleapPlanningBinding binding = new TuleapPlanningBinding(milestoneTypeId, backlogItemTypeId);

		JsonElement element = jsonObject.get(ITuleapConstants.JSON_MILESTONES_TITLE);
		if (element != null) {
			binding.setMilestonesTitle(element.getAsString());
		}
		element = jsonObject.get(ITuleapConstants.JSON_BACKLOG_ITEMS_TITLE);
		if (element != null) {
			binding.setBacklogItemsTitle(element.getAsString());
		}

		TuleapTopPlanning topPlanning = new TuleapTopPlanning(id, label, url, htmlUrl, projectId, binding);

		return topPlanning;
	}
}
