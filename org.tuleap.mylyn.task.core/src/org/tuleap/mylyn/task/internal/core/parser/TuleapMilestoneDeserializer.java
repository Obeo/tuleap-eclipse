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
import java.text.ParseException;
import java.util.Date;

import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;

/**
 * This class is used to deserialize the JSON representation of a tracker configuration.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapMilestoneDeserializer extends AbstractTuleapDeserializer<TuleapMilestone> {

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
	private static final String MILESTONE_TYPE_ID = "milestone_type_id"; //$NON-NLS-1$

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
	public TuleapMilestone deserialize(JsonElement rootJsonElement, Type type,
			JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

		TuleapMilestone milestone = super.deserialize(rootJsonElement, type, jsonDeserializationContext);

		JsonObject jsonObject = rootJsonElement.getAsJsonObject();

		JsonElement elt = jsonObject.get(START_DATE);
		if (elt != null) {
			String startDate = elt.getAsString();
			try {
				milestone.setStartDate(dateFormat.parse(startDate));
			} catch (ParseException e) {
				// TODO manage the exception properly
			}
		}

		elt = jsonObject.get(DURATION);
		if (elt != null) {
			float duration = elt.getAsFloat();
			milestone.setDuration(duration);
		}

		elt = jsonObject.get(CAPACITY);
		if (elt != null) {
			float capacity = elt.getAsFloat();
			milestone.setCapacity(capacity);
		}

		elt = jsonObject.get(SUBMILESTONES);
		if (elt != null) {
			JsonArray submilestones = elt.getAsJsonArray();
			for (JsonElement submilestone : submilestones) {
				TuleapMilestone sub = deserialize(submilestone, type, jsonDeserializationContext);
				milestone.addSubMilestone(sub);
			}
		}

		return milestone;
	}

	@Override
	protected TuleapMilestone buildPojo(int id, int configurationId, String label, String url,
			String htmlUrl, Date creationDate, Date lastModificationDate) {
		return new TuleapMilestone(id, configurationId, label, url, htmlUrl, creationDate,
				lastModificationDate);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.internal.core.parser.AbstractTuleapDeserializer#getTypeIdKey()
	 */
	@Override
	protected String getTypeIdKey() {
		return MILESTONE_TYPE_ID;
	}

}
