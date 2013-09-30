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

import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestoneType;

/**
 * This class is used to deserialize the JSON representation of a milestone Type.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapMilestoneTypeDeserializer extends AbstractTuleapConfigurationDeserializer<TuleapMilestoneType> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	public TuleapMilestoneType deserialize(JsonElement rootJsonElement, Type type,
			JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		JsonObject jsonObject = rootJsonElement.getAsJsonObject();

		int identifier = this.getId(jsonObject);
		String url = this.getUrl(jsonObject);
		String label = this.getLabel(jsonObject);
		String itemName = null;
		String description = null;
		long lastUpdateDate = System.currentTimeMillis();
		String cardwallUrl = null;

		TuleapMilestoneType tuleapMilestoneType = new TuleapMilestoneType(identifier, url, label, itemName,
				description, lastUpdateDate, cardwallUrl);

		tuleapMilestoneType = this.populateConfigurableFields(tuleapMilestoneType, jsonObject);

		return tuleapMilestoneType;
	}
}
