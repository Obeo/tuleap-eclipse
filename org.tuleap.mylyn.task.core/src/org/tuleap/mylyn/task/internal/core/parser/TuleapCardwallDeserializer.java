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

import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapCard;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapCardwall;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapStatus;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapSwimlane;
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
			JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		JsonObject jsonObject = rootJsonElement.getAsJsonObject();

		JsonArray statusList = jsonObject.get(ITuleapConstants.STATUS_LIST).getAsJsonArray();

		TuleapCardwall cardwall = new TuleapCardwall();
		for (JsonElement statusElement : statusList) {
			int statusId = statusElement.getAsJsonObject().get(ITuleapConstants.ID).getAsInt();
			String statuslabel = statusElement.getAsJsonObject().get(ITuleapConstants.LABEL).getAsString();
			TuleapStatus status = new TuleapStatus(statusId, statuslabel);
			cardwall.addStatus(status);
		}

		JsonArray swimlanesList = jsonObject.get(ITuleapConstants.SWIMLANES).getAsJsonArray();

		for (JsonElement swimlaneElement : swimlanesList) {
			TuleapSwimlane swimlane = new TuleapSwimlane();

			JsonObject backlogItemObject = swimlaneElement.getAsJsonObject().get(
					ITuleapConstants.BACKLOG_ITEM).getAsJsonObject();
			TuleapBacklogItem backlogItem = new TuleapBacklogItemDeserializer().deserialize(
					backlogItemObject, TuleapBacklogItem.class, null);
			swimlane.setBacklogItem(backlogItem);

			JsonArray cardsArray = swimlaneElement.getAsJsonObject().get(ITuleapConstants.CARDS)
					.getAsJsonArray();

			for (JsonElement cardElement : cardsArray) {
				TuleapCard card = new TuleapCardDeserializer().deserialize(cardElement, TuleapCard.class,
						null);
				swimlane.addCard(card);
			}

			cardwall.addSwimlane(swimlane);
		}

		return cardwall;
	}
}
