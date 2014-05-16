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
package org.tuleap.mylyn.task.core.internal.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import org.tuleap.mylyn.task.core.internal.TuleapCoreActivator;
import org.tuleap.mylyn.task.core.internal.model.data.ArtifactReference;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapCard;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapStatus;
import org.tuleap.mylyn.task.core.internal.util.ITuleapConstants;
import org.tuleap.mylyn.task.core.internal.util.TuleapCoreKeys;
import org.tuleap.mylyn.task.core.internal.util.TuleapCoreMessages;

/**
 * This class is used to deserialize the JSON representation of a cardwall card.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapCardDeserializer extends AbstractTuleapDeserializer<String, TuleapCard> {

	/**
	 * {@inheritDoc}.
	 *
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public TuleapCard deserialize(JsonElement element, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		TuleapCard card = super.deserialize(element, type, context);

		JsonObject jsonObject = element.getAsJsonObject();

		card.setArtifact(context.<ArtifactReference> deserialize(jsonObject
				.get(ITuleapConstants.JSON_ARTIFACT), ArtifactReference.class));

		JsonElement elt = jsonObject.get(ITuleapConstants.ACCENT_COLOR);
		if (elt != null && !elt.isJsonNull()) {
			String color = elt.getAsString();
			card.setAccentColor(color);
		}

		elt = jsonObject.get(ITuleapConstants.COLUMN_ID);
		if (elt != null && !elt.isJsonNull()) {
			try {
				Integer column = Integer.valueOf(elt.getAsInt());
				card.setColumnId(column);
			} catch (NumberFormatException e) {
				TuleapCoreActivator.log(e, false);
			}
		}
		elt = jsonObject.get(ITuleapConstants.JSON_STATUS);
		if (elt != null && !elt.isJsonNull()) {
			String statusString = elt.getAsString();
			if (statusString.isEmpty()) {
				card.setStatus(null);
			} else {
				try {
					card.setStatus(TuleapStatus.valueOf(statusString));
				} catch (IllegalArgumentException e) {
					String messageToLog = TuleapCoreMessages.getString(
							TuleapCoreKeys.gettingStatusValueLogMessage, statusString);
					TuleapCoreActivator.log(messageToLog, false);
				}
			}
		}

		JsonArray array = jsonObject.get(ITuleapConstants.JSON_ALLOWED_COLUMNS).getAsJsonArray();
		int[] columnIds = new int[array.size()];
		for (int i = 0; i < array.size(); i++) {
			int columnId = array.get(i).getAsInt();
			columnIds[i] = columnId;
		}
		card.setAllowedColumnIds(columnIds);
		return card;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.core.internal.parser.AbstractProjectElementDeserializer#getPrototype(String)
	 */
	@Override
	protected TuleapCard getPrototype(String jsonId) {
		TuleapCard card = new TuleapCard();
		card.setId(jsonId);
		return card;
	}

}