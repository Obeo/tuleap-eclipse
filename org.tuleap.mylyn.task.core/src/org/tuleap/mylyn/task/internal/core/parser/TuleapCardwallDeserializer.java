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
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapCard;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapCardwall;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapColumn;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapSwimlane;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * This class is used to deserialize the JSON representation of a cardwall configuration.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapCardwallDeserializer implements JsonDeserializer<TuleapCardwall> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	public TuleapCardwall deserialize(JsonElement rootJsonElement, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = rootJsonElement.getAsJsonObject();

		JsonArray columnsList = jsonObject.get(ITuleapConstants.COLUMNS).getAsJsonArray();

		TuleapCardwall cardwall = new TuleapCardwall();
		for (JsonElement statusElement : columnsList) {
			int columnId = statusElement.getAsJsonObject().get(ITuleapConstants.ID).getAsInt();
			String columnLabel = statusElement.getAsJsonObject().get(ITuleapConstants.LABEL).getAsString();
			String columnColor = statusElement.getAsJsonObject().get(ITuleapConstants.COLOR).getAsString();
			TuleapColumn column = new TuleapColumn(columnId, columnLabel);
			column.setColor(columnColor);
			cardwall.addColumn(column);
		}

		JsonArray swimlanesList = jsonObject.get(ITuleapConstants.SWIMLANES).getAsJsonArray();

		for (JsonElement swimlaneElement : swimlanesList) {
			TuleapSwimlane swimlane = new TuleapSwimlane();
			JsonArray cardsArray = swimlaneElement.getAsJsonObject().get(ITuleapConstants.CARDS)
					.getAsJsonArray();
			for (JsonElement cardElement : cardsArray) {
				TuleapCard card = new TuleapCardDeserializer().deserialize(cardElement, TuleapCard.class,
						context);
				swimlane.addCard(card);
			}
			cardwall.addSwimlane(swimlane);
		}

		return cardwall;
	}
}
