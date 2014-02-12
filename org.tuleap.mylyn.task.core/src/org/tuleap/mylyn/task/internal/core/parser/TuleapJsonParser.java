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

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.Date;

import org.tuleap.mylyn.task.internal.core.model.TuleapErrorMessage;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapPlanning;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapProject;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapTrackerReport;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapUser;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapUserGroup;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapCard;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapCardwall;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapMilestone;

/**
 * This class is used to encapsulate all the logic of the JSON parsing.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapJsonParser {

	/**
	 * The Gson used for parsing.
	 */
	private final Gson gson;

	/**
	 * Constructor.
	 */
	public TuleapJsonParser() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TuleapProject.class, new TuleapProjectDeserializer());
		gsonBuilder.registerTypeAdapter(TuleapArtifact.class, new TuleapArtifactDeserializer());
		gsonBuilder.registerTypeAdapter(TuleapCardwall.class, new TuleapCardwallDeserializer());
		gsonBuilder.registerTypeAdapter(TuleapCard.class, new TuleapCardDeserializer());
		gsonBuilder.registerTypeAdapter(Date.class, new DateIso8601Adapter());
		gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
		this.gson = gsonBuilder.create();
	}

	/**
	 * Parse a project from a Json representation.
	 * 
	 * @param element
	 *            The JsonElement representing a {@link TuleapProject}.
	 * @return The project
	 */
	public TuleapProject parseProject(JsonElement element) {
		return gson.fromJson(element, TuleapProject.class);
	}

	/**
	 * Parse a JSON representation of a planning and returns the created {@link TuleapPlanning} instance.
	 * 
	 * @param json
	 *            The JSON representation of the planning
	 * @return A new instance of {@link TuleapPlanning} containing the json data.
	 */
	public TuleapPlanning parsePlanning(String json) {
		return gson.fromJson(json, TuleapPlanning.class);
	}

	/**
	 * Parse a JSON representation of a planning and returns the created {@link TuleapPlanning} instance.
	 * 
	 * @param element
	 *            the JSON element to parse
	 * @return A new instance of {@link TuleapPlanning} containing the json data.
	 */
	public TuleapPlanning parsePlanning(JsonElement element) {
		return gson.fromJson(element, TuleapPlanning.class);
	}

	/**
	 * Parses a JSON String representing a milestone into a POJO.
	 * 
	 * @param json
	 *            The JSON response representing an milestone
	 * @return a POJO populated with the data from the JSON String.
	 */
	public TuleapMilestone parseMilestone(String json) {
		return gson.fromJson(json, TuleapMilestone.class);
	}

	/**
	 * Parses a JSON String representing a milestone into a POJO.
	 * 
	 * @param element
	 *            The JSON element representing an milestone
	 * @return a POJO populated with the data from the JSON String.
	 */
	public TuleapMilestone parseMilestone(JsonElement element) {
		return gson.fromJson(element, TuleapMilestone.class);
	}

	/**
	 * Parses a JSON String representing a backlogItem into a POJO.
	 * 
	 * @param json
	 *            The JSON response representing a backlogItem
	 * @return a POJO populated with the data from the JSON String.
	 */
	public TuleapBacklogItem parseBacklogItem(String json) {
		return gson.fromJson(json, TuleapBacklogItem.class);
	}

	/**
	 * Parses a JSON String representing a backlogItem into a POJO.
	 * 
	 * @param element
	 *            The JSON element representing a backlogItem
	 * @return a POJO populated with the data from the JSON String.
	 */
	public TuleapBacklogItem parseBacklogItem(JsonElement element) {
		return gson.fromJson(element, TuleapBacklogItem.class);
	}

	/**
	 * Parses a JSON String representing a cardwall into a POJO.
	 * 
	 * @param json
	 *            The JSON response representing an cardwall
	 * @return a POJO populated with the data from the JSON String.
	 */
	public TuleapCardwall parseCardwall(String json) {
		return gson.fromJson(json, TuleapCardwall.class);
	}

	/**
	 * Parses a JSON element representing a user into a POJO.
	 * 
	 * @param element
	 *            The JSON element representing a user
	 * @return a POJO populated with the data from the JSON String.
	 */
	public TuleapUser parseUser(String element) {
		return gson.fromJson(element, TuleapUser.class);
	}

	/**
	 * Parses a JSON element representing a User group into a POJO.
	 * 
	 * @param element
	 *            The JSON element representing a user group
	 * @return a POJO populated with the data from the JSON String.
	 */
	public TuleapUserGroup parseUserGroup(String element) {
		return gson.fromJson(element, TuleapUserGroup.class);
	}

	/**
	 * Parses a JSON element representing a tracker report into a POJO.
	 * 
	 * @param element
	 *            The JSON element representing a tracker report
	 * @return a POJO populated with the data from the JSON String.
	 */
	public TuleapTrackerReport parseTrackerReport(String element) {
		return gson.fromJson(element, TuleapTrackerReport.class);
	}

	/**
	 * Parse the JSON representation of an error and returns its message.
	 * 
	 * @param jsonResponse
	 *            The JSON representation of the error
	 * @return The error message
	 */
	public TuleapErrorMessage getErrorMessage(String jsonResponse) {
		return gson.fromJson(jsonResponse, TuleapErrorMessage.class);
	}
}
