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

import java.util.List;

import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapTrackerConfiguration;

/**
 * This class is used to encapsulate all the logic of the JSON parsing.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapJsonParser {
	/**
	 * Parses the JSON representation of one Tuleap project and returns its configuration.
	 * 
	 * @param jsonResponse
	 *            The JSON representation of a Tuleap project.
	 * @return The Tuleap configuration matching the JSON representation
	 */
	public TuleapProjectConfiguration getProjectConfiguration(String jsonResponse) {
		return null;
	}

	/**
	 * Parses the JSON representation of a collection of Tuleap projects and returns their configuration.
	 * 
	 * @param jsonReponse
	 *            The JSON representation of a collection of Tuleap projects
	 * @return The list of the Tuleap project's configuration
	 */
	public List<TuleapProjectConfiguration> getProjectsConfiguration(String jsonReponse) {
		return null;
	}

	/**
	 * Parses the JSON representation of one Tuleap tracker and returns its configuration.
	 * 
	 * @param jsonResponse
	 *            The JSON representation of a Tuleap tracker.
	 * @return The Tuleap configuration matching the JSON representation
	 */
	public TuleapTrackerConfiguration getTrackerConfiguration(String jsonResponse) {
		return null;
	}

	/**
	 * Parses the JSON representation of a collection of Tuleap trackers and returns their configuration.
	 * 
	 * @param jsonResponse
	 *            The JSON representation of a collection of Tuleap trackers
	 * @return The list of the Tulea tracker's configuration
	 */
	public List<TuleapTrackerConfiguration> getTrackersConfiguration(String jsonResponse) {
		return null;
	}

	/**
	 * Parses the JSON representation of the session hash and returns it.
	 * 
	 * @param jsonResponse
	 *            The JSON representation of the session hash
	 * @return The session hash
	 */
	public String getSessionHash(String jsonResponse) {
		return null;
	}

	/**
	 * Parse the JSON representation of an error and returns its message.
	 * 
	 * @param jsonResponse
	 *            The JSON representation of the error
	 * @return The error message
	 */
	public String getErrorMessage(String jsonResponse) {
		return null;
	}
}
