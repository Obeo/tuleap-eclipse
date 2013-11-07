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

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapCardwall;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapTopPlanning;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessages;
import org.tuleap.mylyn.task.internal.core.util.TuleapMylynTasksMessagesKeys;

/**
 * This class is used to encapsulate all the logic of the JSON parsing.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapJsonParser {

	/**
	 * Parses the JSON representation of a collection of Tuleap projects and returns their configuration.
	 * 
	 * @param jsonResponse
	 *            The JSON representation of a collection of Tuleap projects
	 * @return The list of the Tuleap project's configuration
	 */
	public List<TuleapProjectConfiguration> parseProjectConfigurations(String jsonResponse) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TuleapProjectConfiguration.class,
				new TuleapProjectConfigurationDeserializer());

		JsonParser jsonParser = new JsonParser();
		JsonArray jsonArray = jsonParser.parse(jsonResponse).getAsJsonArray();

		List<TuleapProjectConfiguration> result = Lists.newArrayList();
		for (JsonElement jsonElement : jsonArray) {
			Gson gson = gsonBuilder.create();
			TuleapProjectConfiguration tuleapProjectConfiguration = gson.fromJson(jsonElement,
					TuleapProjectConfiguration.class);

			result.add(tuleapProjectConfiguration);
		}
		return result;
	}

	/**
	 * Parse the top plannings from the json response.
	 * 
	 * @param jsonResponse
	 *            The json response
	 * @return The top plannings parsed
	 */
	public List<TuleapTopPlanning> parseTopPlannings(String jsonResponse) {
		List<TuleapTopPlanning> topPlannings = new ArrayList<TuleapTopPlanning>();

		JsonParser parser = new JsonParser();

		JsonArray topPlanningsArray = parser.parse(jsonResponse).getAsJsonArray();
		for (JsonElement element : topPlanningsArray) {
			TuleapTopPlanning milestone = new TuleapTopPlanningDeserializer().deserialize(element,
					TuleapTopPlanning.class, null);
			topPlannings.add(milestone);
		}

		return topPlannings;
	}

	/**
	 * Parse one top planning from the json response.
	 * 
	 * @param jsonResponse
	 *            The json response
	 * @return The top planning parsed
	 */
	public TuleapTopPlanning parseTopPlanning(String jsonResponse) {
		JsonParser parser = new JsonParser();
		JsonElement topPlanningElement = parser.parse(jsonResponse);
		return new TuleapTopPlanningDeserializer().deserialize(topPlanningElement, TuleapTopPlanning.class,
				null);
	}

	/**
	 * Parse the milestones from the json response.
	 * 
	 * @param jsonResponse
	 *            The json response
	 * @return The milestones parsed
	 */
	public List<TuleapMilestone> parseMilestones(String jsonResponse) {
		List<TuleapMilestone> milestones = new ArrayList<TuleapMilestone>();

		JsonParser parser = new JsonParser();

		JsonArray milestonesArray = parser.parse(jsonResponse).getAsJsonArray();
		for (JsonElement milestoneElement : milestonesArray) {
			TuleapMilestone milestone = new TuleapMilestoneDeserializer().deserialize(milestoneElement,
					TuleapMilestone.class, null);
			milestones.add(milestone);
		}

		return milestones;
	}

	/**
	 * Parse the backlog items from the json response.
	 * 
	 * @param jsonResponse
	 *            The json response
	 * @return The backlog items parsed
	 */
	public List<TuleapBacklogItem> parseBacklogItems(String jsonResponse) {
		List<TuleapBacklogItem> results = new ArrayList<TuleapBacklogItem>();

		JsonParser parser = new JsonParser();

		// Contains a JSON array of backlog items
		JsonArray backlogItemsArray = parser.parse(jsonResponse).getAsJsonArray();
		for (JsonElement backlogItemElement : backlogItemsArray) {
			TuleapBacklogItem backlogItem = new TuleapBacklogItemDeserializer().deserialize(
					backlogItemElement, TuleapBacklogItem.class, null);
			results.add(backlogItem);
		}

		return results;
	}

	/**
	 * Parses a JSON String representing a milestone into a POJO.
	 * 
	 * @param jsonResponse
	 *            The JSON response representing an milestone
	 * @return a POJO populated with the data from the JSON String.
	 */
	public TuleapMilestone parseMilestone(String jsonResponse) {
		JsonParser parser = new JsonParser();
		JsonElement milestoneElement = parser.parse(jsonResponse);
		return new TuleapMilestoneDeserializer().deserialize(milestoneElement, TuleapMilestone.class, null);
		// GsonBuilder builder = new GsonBuilder();
		// Gson gson = builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
		// return gson.fromJson(json, TuleapMilestone.class);
	}

	/**
	 * Parses a JSON String representing a backlogItem into a POJO.
	 * 
	 * @param jsonResponse
	 *            The JSON response representing a backlogItem
	 * @return a POJO populated with the data from the JSON String.
	 */
	public TuleapBacklogItem parseBacklogItem(String jsonResponse) {
		JsonParser parser = new JsonParser();
		JsonElement backlogElement = parser.parse(jsonResponse);
		return new TuleapBacklogItemDeserializer().deserialize(backlogElement, TuleapBacklogItem.class, null);
	}

	/**
	 * Parses a JSON String representing a cardwall into a POJO.
	 * 
	 * @param jsonResponse
	 *            The JSON response representing an cardwall
	 * @return a POJO populated with the data from the JSON String.
	 */
	public TuleapCardwall parseCardwall(String jsonResponse) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(jsonResponse);
		return new TuleapCardwallDeserializer().deserialize(element, TuleapCardwall.class, null);
	}

	/**
	 * Parse the JSON representation of an error and returns its message.
	 * 
	 * @param jsonResponse
	 *            The JSON representation of the error
	 * @return The error message
	 */
	public String getErrorMessage(String jsonResponse) {
		try {
			JsonParser parser = new JsonParser();
			JsonObject element = parser.parse(jsonResponse).getAsJsonObject();
			return TuleapMylynTasksMessages.getString(TuleapMylynTasksMessagesKeys.jsonErrorMessage, element
					.get("code"), element.get("message")); //$NON-NLS-1$//$NON-NLS-2$
			// CHECKSTYLE:OFF
			// We need to be able to provide something even if the parsing of the error message fails
		} catch (Exception e) {
			// CHECKSTYLE:ON
			TuleapCoreActivator.log(e, false);
		}
		return jsonResponse;
	}
}
