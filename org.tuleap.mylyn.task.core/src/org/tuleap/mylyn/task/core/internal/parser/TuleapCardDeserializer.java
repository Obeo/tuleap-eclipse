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

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.tuleap.mylyn.task.core.internal.TuleapCoreActivator;
import org.tuleap.mylyn.task.core.internal.model.data.ArtifactLinkFieldValue;
import org.tuleap.mylyn.task.core.internal.model.data.ArtifactReference;
import org.tuleap.mylyn.task.core.internal.model.data.AttachmentFieldValue;
import org.tuleap.mylyn.task.core.internal.model.data.BoundFieldValue;
import org.tuleap.mylyn.task.core.internal.model.data.LiteralFieldValue;
import org.tuleap.mylyn.task.core.internal.model.data.OpenListFieldValue;
import org.tuleap.mylyn.task.core.internal.model.data.TuleapReference;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapCard;
import org.tuleap.mylyn.task.core.internal.model.data.agile.TuleapStatus;
import org.tuleap.mylyn.task.core.internal.util.ITuleapConstants;
import org.tuleap.mylyn.task.core.internal.util.TuleapCoreKeys;
import org.tuleap.mylyn.task.core.internal.util.TuleapCoreMessages;

/**
 * This class is used to deserialize the JSON representation of a cardwall card.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
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
		TuleapCard card = deserializeCardConfigurableFields(element, type, context);

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
	 * Deserialize card configurable fields.
	 *
	 * @param rootJsonElement
	 *            The root JSON element
	 * @param type
	 *            The type
	 * @param context
	 *            The deserialization context
	 * @return The card POJO
	 * @throws JsonParseException
	 *             A parsing exception
	 */
	public TuleapCard deserializeCardConfigurableFields(JsonElement rootJsonElement, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = rootJsonElement.getAsJsonObject();
		TuleapCard tuleapCard = super.deserialize(rootJsonElement, type, context);
		JsonElement jsonTrackerRef = jsonObject.get(ITuleapConstants.JSON_TRACKER);
		TuleapReference trackerRef = context.deserialize(jsonTrackerRef, TuleapReference.class);
		tuleapCard.setTracker(trackerRef);

		JsonArray fields = jsonObject.get(ITuleapConstants.VALUES).getAsJsonArray();
		for (JsonElement field : fields) {
			JsonObject jsonField = field.getAsJsonObject();
			int fieldId = jsonField.get(ITuleapConstants.FIELD_ID).getAsInt();
			if (jsonField.has(ITuleapConstants.FIELD_VALUE)) {
				JsonElement jsonValue = jsonField.get(ITuleapConstants.FIELD_VALUE);
				if (jsonValue.isJsonPrimitive()) {
					JsonPrimitive primitive = jsonValue.getAsJsonPrimitive();
					if (primitive.isString()) {
						tuleapCard.addFieldValue(new LiteralFieldValue(fieldId, primitive.getAsString()));
					} else if (primitive.isNumber()) {
						tuleapCard.addFieldValue(new LiteralFieldValue(fieldId, String.valueOf(primitive
								.getAsNumber())));
					} else if (primitive.isBoolean()) {
						tuleapCard.addFieldValue(new LiteralFieldValue(fieldId, String.valueOf(primitive
								.getAsBoolean())));
					}
				} else if (jsonValue.isJsonNull()) {
					tuleapCard.addFieldValue(new LiteralFieldValue(fieldId, "")); //$NON-NLS-1$
				}
			} else if (jsonField.has(ITuleapConstants.FIELD_BIND_VALUE_ID)
					&& !jsonField.get(ITuleapConstants.FIELD_BIND_VALUE_ID).isJsonNull()) {
				// sb?
				JsonElement jsonBindValueId = jsonField.get(ITuleapConstants.FIELD_BIND_VALUE_ID);
				int bindValueId = jsonBindValueId.getAsInt();
				tuleapCard.addFieldValue(new BoundFieldValue(fieldId, Lists.newArrayList(Integer
						.valueOf(bindValueId))));
			} else if (jsonField.has(ITuleapConstants.FIELD_BIND_VALUE_IDS)
					&& !jsonField.get(ITuleapConstants.FIELD_BIND_VALUE_IDS).isJsonNull()) {
				// sb?, msb, cb, or tbl (open list)
				JsonElement jsonBindValueIds = jsonField.get(ITuleapConstants.FIELD_BIND_VALUE_IDS);
				JsonArray jsonIds = jsonBindValueIds.getAsJsonArray();
				if (jsonIds.size() > 0) {
					JsonPrimitive firstElement = jsonIds.get(0).getAsJsonPrimitive();
					if (firstElement.isString()) {
						// Open list (tbl)
						List<String> bindValueIds = new ArrayList<String>();
						for (JsonElement idElement : jsonIds) {
							bindValueIds.add(idElement.getAsString());
						}
						tuleapCard.addFieldValue(new OpenListFieldValue(fieldId, bindValueIds));
					} else {
						List<Integer> bindValueIds = new ArrayList<Integer>();
						for (JsonElement idElement : jsonIds) {
							bindValueIds.add(Integer.valueOf(idElement.getAsInt()));
						}
						tuleapCard.addFieldValue(new BoundFieldValue(fieldId, bindValueIds));
					}
				} else {
					tuleapCard.addFieldValue(new BoundFieldValue(fieldId, Collections.<Integer> emptyList()));
				}
			} else if (jsonField.has(ITuleapConstants.FIELD_LINKS)
					&& !jsonField.get(ITuleapConstants.FIELD_LINKS).isJsonNull()) {
				// Artifact links
				tuleapCard.addFieldValue(context.<ArtifactLinkFieldValue> deserialize(jsonField,
						ArtifactLinkFieldValue.class));
			} else if (jsonField.has(ITuleapConstants.FILE_DESCRIPTIONS)
					&& jsonField.get(ITuleapConstants.FILE_DESCRIPTIONS).isJsonArray()) {
				AttachmentFieldValue value = context.deserialize(jsonField, AttachmentFieldValue.class);
				tuleapCard.addFieldValue(value);
			}
		}
		return tuleapCard;
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
