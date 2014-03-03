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

import org.tuleap.mylyn.task.internal.core.model.config.TuleapWorkflowTransition;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * Workflow transition deserializer.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapWorkflowTransitionDeserializer implements JsonDeserializer<TuleapWorkflowTransition> {

	/**
	 * The from field value id keyword.
	 */
	private static final String FROM_FIELD_VALUE_ID = "from_id"; //$NON-NLS-1$

	/**
	 * The to field value id keyword.
	 */
	private static final String TO_FIELD_VALUE_ID = "to_id"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	public TuleapWorkflowTransition deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		JsonElement fromElement = jsonObject.get(FROM_FIELD_VALUE_ID);
		// By default, the initial state is unselected
		int from = ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID;
		if (fromElement != null && !fromElement.isJsonNull()) {
			from = fromElement.getAsInt();
		}
		int to = jsonObject.get(TO_FIELD_VALUE_ID).getAsInt();

		TuleapWorkflowTransition transition = new TuleapWorkflowTransition();
		transition.setFrom(from);
		transition.setTo(to);
		return transition;
	}

}
