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

import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapBacklogItem.Status;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * This class is used to deserialize the JSON representation of a project configuration.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapBacklogItemDeserializer extends AbstractDetailedElementDeserializer<TuleapBacklogItem> {

	/**
	 * {@inheritDoc}.
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public TuleapBacklogItem deserialize(JsonElement element, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		TuleapBacklogItem backlogItem = super.deserialize(element, type, context);

		JsonObject jsonObject = element.getAsJsonObject();

		backlogItem.setArtifact(new Gson().fromJson(jsonObject.get(ITuleapConstants.JSON_ARTIFACT),
				TuleapReference.class));

		JsonElement elt = jsonObject.get(ITuleapConstants.INITIAL_EFFORT);
		if (elt != null) {
			Float initialEffort = Float.valueOf(elt.getAsFloat());
			backlogItem.setInitialEffort(initialEffort);
		}

		elt = jsonObject.get(ITuleapConstants.ASSIGNED_MILESTONE_ID);
		if (elt != null) {
			int assignedMilestoneId = elt.getAsInt();
			backlogItem.setAssignedMilestoneId(Integer.valueOf(assignedMilestoneId));
		}

		elt = jsonObject.get(ITuleapConstants.JSON_STATUS);
		if (elt != null) {
			String statusString = elt.getAsString();
			backlogItem.setStatus(Status.fromJsonString(statusString));
		}

		return backlogItem;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.parser.AbstractProjectElementDeserializer#getPrototype()
	 */
	@Override
	protected TuleapBacklogItem getPrototype() {
		return new TuleapBacklogItem();
	}

}
