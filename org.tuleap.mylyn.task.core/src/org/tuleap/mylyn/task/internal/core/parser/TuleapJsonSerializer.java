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

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.tuleap.mylyn.task.internal.core.data.TuleapConfigurableElementMapper;

/**
 * Utility class to hide all the logic of the JSON serialization.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapJsonSerializer {
	/**
	 * Serializes in JSON the fields of the Tuleap artifact.
	 * 
	 * @param tuleapConfigurableElementMapper
	 *            The Tuleap configurable element mapper
	 * @return A JSON representation of the fields of the Tuleap artifact
	 */
	public String serializeArtifactFields(TuleapConfigurableElementMapper tuleapConfigurableElementMapper) {
		return null;
	}

	/**
	 * Serializes in JSON the given credentials.
	 * 
	 * @param username
	 *            The username
	 * @param password
	 *            The password
	 * @return A JSON representation of the username and password
	 */
	public String serializeLogin(String username, String password) {
		Gson gson = new Gson();
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("user_name", username); //$NON-NLS-1$
		jsonObject.addProperty("password", password); //$NON-NLS-1$
		String login = gson.toJson(jsonObject);
		return login;
	}
}
