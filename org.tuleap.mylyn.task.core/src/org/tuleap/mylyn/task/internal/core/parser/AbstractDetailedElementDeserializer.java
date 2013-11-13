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

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.tuleap.mylyn.task.internal.core.model.data.AbstractTuleapDetailedElement;

import static org.tuleap.mylyn.task.internal.core.util.ITuleapConstants.HTML_URL;
import static org.tuleap.mylyn.task.internal.core.util.ITuleapConstants.LAST_UPDATED_ON;
import static org.tuleap.mylyn.task.internal.core.util.ITuleapConstants.SUBMITTED_BY;
import static org.tuleap.mylyn.task.internal.core.util.ITuleapConstants.SUBMITTED_ON;

/**
 * This class is used to deserialize a JSON representation of a Tuleap object, for objects that have a
 * configuration ID (top-plannings, for instance, have no configuration ID).
 * 
 * @param <T>
 *            The type of the agile element to deserialize.
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public abstract class AbstractDetailedElementDeserializer<T extends AbstractTuleapDetailedElement> extends AbstractProjectElementDeserializer<T> {

	/**
	 * The pattern used to format date following the ISO8601 standard.
	 */
	protected DateTimeFormatter dateParser = ISODateTimeFormat.dateTimeParser();

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public T deserialize(JsonElement rootJsonElement, Type type,
			JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

		T pojo = super.deserialize(rootJsonElement, type, jsonDeserializationContext);

		JsonObject jsonObject = rootJsonElement.getAsJsonObject();

		JsonElement element = jsonObject.get(HTML_URL);
		if (element != null && !element.isJsonNull()) {
			String htmlUrl = element.getAsString();
			pojo.setHtmlUrl(htmlUrl);
		}
		element = jsonObject.get(SUBMITTED_ON);
		if (element != null && !element.isJsonNull()) {
			String submittedOn = jsonObject.get(SUBMITTED_ON).getAsString();
			pojo.setSubmittedOn(dateParser.parseDateTime(submittedOn).toDate());
		}
		element = jsonObject.get(SUBMITTED_BY);
		if (element != null && !element.isJsonNull()) {
			int submittedBy = jsonObject.get(SUBMITTED_BY).getAsInt();
			pojo.setSubmittedBy(submittedBy);
		}
		element = jsonObject.get(LAST_UPDATED_ON);
		if (element != null && !element.isJsonNull()) {
			String lastUpdatedOn = jsonObject.get(LAST_UPDATED_ON).getAsString();
			pojo.setLastUpdatedOn(dateParser.parseDateTime(lastUpdatedOn).toDate());
		}
		return pojo;
	}

}
