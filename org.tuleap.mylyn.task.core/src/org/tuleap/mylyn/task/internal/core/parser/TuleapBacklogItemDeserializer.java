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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;

/**
 * This class is used to deserialize the JSON representation of a project configuration.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapBacklogItemDeserializer extends AbstractTuleapDeserializer<TuleapBacklogItem> {

	/**
	 * The key used for the initial effort of the backlog item.
	 */
	private static final String INITIAL_EFFORT = "initial_effort"; //$NON-NLS-1$

	/**
	 * The key used for the type id of the backlog item.
	 */
	private static final String TYPE_ID = "backlog_item_type_id"; //$NON-NLS-1$

	/**
	 * The key used for the assigned milestone id.
	 */
	private static final String ASSIGNED_MILESTONE_ID = "assigned_milestone_id"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public TuleapBacklogItem deserialize(JsonElement element, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		TuleapBacklogItem backlogItem = super.deserialize(element, type, context);

		JsonObject jsonObject = element.getAsJsonObject();

		JsonElement elt = jsonObject.get(INITIAL_EFFORT);
		if (elt != null) {
			float initialEffort = elt.getAsFloat();
			backlogItem.setInitialEffort(initialEffort);
		}

		elt = jsonObject.get(ASSIGNED_MILESTONE_ID);
		if (elt != null) {
			int assignedMilestoneId = elt.getAsInt();
			backlogItem.setAssignedMilestoneId(Integer.valueOf(assignedMilestoneId));
		}

		int typeId = jsonObject.get(TYPE_ID).getAsInt();
		backlogItem.setTypeId(typeId);

		return backlogItem;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.parser.AbstractTuleapDeserializer#buildPojo()
	 */
	@Override
	protected TuleapBacklogItem buildPojo() {
		return new TuleapBacklogItem();
	}

}
