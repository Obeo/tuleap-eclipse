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
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapUser;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapElementComment;

import static org.tuleap.mylyn.task.internal.core.util.ITuleapConstants.BODY;
import static org.tuleap.mylyn.task.internal.core.util.ITuleapConstants.EMAIL;
import static org.tuleap.mylyn.task.internal.core.util.ITuleapConstants.LAST_COMMENT;
import static org.tuleap.mylyn.task.internal.core.util.ITuleapConstants.SUBMITTED_BY;
import static org.tuleap.mylyn.task.internal.core.util.ITuleapConstants.SUBMITTED_ON;

/**
 * This class is used to deserialize the JSON representation of an artifact changeset.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapChangesetDeserializer implements JsonDeserializer<TuleapElementComment> {

	/**
	 * Deserialize the JSON representation of a comment.
	 * 
	 * @param element
	 *            The JSON comment
	 * @param type
	 *            The comment type
	 * @param context
	 *            The context
	 * @return the TuleapElementComment
	 * @throws JsonParseException
	 *             The exception to throw
	 */
	public TuleapElementComment deserialize(JsonElement element, Type type, JsonDeserializationContext context)
			throws JsonParseException {

		JsonObject jsonObject = element.getAsJsonObject();

		int submissionDate = -1;
		int submittedBy = -1;
		String email = null;
		String body = null;

		JsonElement theElement = jsonObject.get(SUBMITTED_ON);
		if (theElement != null && !theElement.isJsonNull()) {
			try {
				submissionDate = Long.valueOf(
						context.<Date> deserialize(theElement, Date.class).getTime() / 1000).intValue();
			} catch (JsonParseException e) {
				TuleapCoreActivator.log(e, false);
			}
		}

		theElement = jsonObject.get(SUBMITTED_BY);
		if (theElement != null && theElement.isJsonPrimitive()) {
			submittedBy = theElement.getAsInt();
		}

		theElement = jsonObject.get(EMAIL);
		if (theElement != null && theElement.isJsonPrimitive()) {
			email = theElement.getAsString();
		}

		JsonObject lastComment = jsonObject.get(LAST_COMMENT).getAsJsonObject();

		theElement = lastComment.get(BODY);
		if (theElement != null && theElement.isJsonPrimitive()) {
			body = theElement.getAsString();
		}

		TuleapUser user = new TuleapUser(null, null, submittedBy, email, null);

		return new TuleapElementComment(body, user, submissionDate);

	}
}
