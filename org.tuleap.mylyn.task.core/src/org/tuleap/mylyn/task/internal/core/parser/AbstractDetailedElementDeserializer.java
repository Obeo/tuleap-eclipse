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
import java.util.Date;

import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.model.data.AbstractTuleapDetailedElement;

import static org.tuleap.mylyn.task.internal.core.util.ITuleapConstants.HTML_URL;
import static org.tuleap.mylyn.task.internal.core.util.ITuleapConstants.LAST_MODIFIED_DATE;
import static org.tuleap.mylyn.task.internal.core.util.ITuleapConstants.SUBMITTED_BY;
import static org.tuleap.mylyn.task.internal.core.util.ITuleapConstants.SUBMITTED_ON;

/**
 * This class is used to deserialize a JSON representation of a Tuleap object, for objects that have a tracker
 * ID (top-plannings, for instance, have no tracker ID).
 * 
 * @param <U>
 *            The type of ID of the element to deserialize.
 * @param <T>
 *            The type of the agile element to deserialize.
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public abstract class AbstractDetailedElementDeserializer<U, T extends AbstractTuleapDetailedElement<U>> extends AbstractProjectElementDeserializer<U, T> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public T deserialize(JsonElement rootJsonElement, Type type, JsonDeserializationContext context)
			throws JsonParseException {

		T pojo = super.deserialize(rootJsonElement, type, context);

		JsonObject jsonObject = rootJsonElement.getAsJsonObject();

		JsonElement element = jsonObject.get(HTML_URL);
		if (element != null && !element.isJsonNull()) {
			String htmlUrl = element.getAsString();
			pojo.setHtmlUrl(htmlUrl);
		}
		element = jsonObject.get(SUBMITTED_ON);
		if (element != null && !element.isJsonNull()) {
			try {
				pojo.setSubmittedOn(context.<Date> deserialize(element, Date.class));
			} catch (JsonParseException e) {
				TuleapCoreActivator.log(e, false);
			}
		}
		element = jsonObject.get(SUBMITTED_BY);
		if (element != null && element.isJsonPrimitive()) {
			int submittedBy = element.getAsInt();
			pojo.setSubmittedBy(submittedBy);
		}
		element = jsonObject.get(LAST_MODIFIED_DATE);
		if (element != null && !element.isJsonNull()) {
			try {
				pojo.setLastModifiedDate(context.<Date> deserialize(element, Date.class));
			} catch (JsonParseException e) {
				TuleapCoreActivator.log(e, false);
			}
		}
		return pojo;
	}

}
