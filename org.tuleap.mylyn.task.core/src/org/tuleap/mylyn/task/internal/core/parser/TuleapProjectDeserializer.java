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

import org.tuleap.mylyn.task.internal.core.model.config.TuleapProject;

/**
 * This class is used to deserialize the JSON representation of a project configuration.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapProjectDeserializer implements JsonDeserializer<TuleapProject> {

	/**
	 * The key used for the name of the project.
	 */
	private static final String PROJECT_NAME = "name"; //$NON-NLS-1$

	/**
	 * The key used for the identifier of the project.
	 */
	private static final String PROJECT_ID = "id"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	public TuleapProject deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
			throws JsonParseException {
		JsonObject jsonObject = arg0.getAsJsonObject();
		TuleapProject tuleapProject = new TuleapProject(jsonObject
				.get(PROJECT_NAME).getAsString(), jsonObject.get(PROJECT_ID).getAsInt());

		// Active services
		JsonElement servicesElement = jsonObject.get("services"); //$NON-NLS-1$
		if (servicesElement != null) {
			JsonArray services = servicesElement.getAsJsonArray();
			for (JsonElement element : services) {
				tuleapProject.addService(element.getAsString());
			}
		}

		return tuleapProject;
	}

}
