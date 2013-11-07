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
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapDetailedElement;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

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
	protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //$NON-NLS-1$

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

		String htmlUrl = jsonObject.get(ITuleapConstants.HTML_URL).getAsString();
		String submittedOn = jsonObject.get(ITuleapConstants.SUBMITTED_ON).getAsString();
		int submittedBy = jsonObject.get(ITuleapConstants.SUBMITTED_BY).getAsInt();
		String lastUpdatedOn = jsonObject.get(ITuleapConstants.LAST_UPDATED_ON).getAsString();

		pojo.setHtmlUrl(htmlUrl);
		try {
			pojo.setSubmittedOn(dateFormat.parse(submittedOn));
			pojo.setLastUpdatedOn(dateFormat.parse(lastUpdatedOn));
		} catch (ParseException e) {
			throw new JsonParseException("Invalid date: " + e.getMessage()); //$NON-NLS-1$
		}
		pojo.setSubmittedBy(submittedBy);

		return pojo;
	}

}
