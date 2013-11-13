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
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import org.tuleap.mylyn.task.internal.core.model.TuleapErrorMessage;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapPlanning;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapProject;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapCardwall;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapMilestone;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapTopPlanning;

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
	public List<TuleapProject> parseProjectConfigurations(String jsonResponse) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TuleapProject.class, new TuleapProjectDeserializer());

		JsonParser jsonParser = new JsonParser();
		JsonArray jsonArray = jsonParser.parse(jsonResponse).getAsJsonArray();

		List<TuleapProject> result = Lists.newArrayList();
		for (JsonElement jsonElement : jsonArray) {
			Gson gson = gsonBuilder.create();
			TuleapProject tuleapProject = gson.fromJson(jsonElement, TuleapProject.class);

			result.add(tuleapProject);
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
	 * Parses a list of {@link TuleapPlanning}s from a JSON string.
	 * 
	 * @param json
	 *            Tje JSON string to parse, should contain an array, but can contain only a JSON object
	 *            representing a {@link TuleapPlanning}.
	 * @return A list that is never null but possibly empty if the JSON was neither an array nor an object.
	 */
	public List<TuleapPlanning> parsePlanningList(String json) {
		JsonParser parser = new JsonParser();
		JsonElement root = parser.parse(json);
		List<TuleapPlanning> result = Lists.newArrayList();
		if (root.isJsonArray()) {
			JsonArray array = root.getAsJsonArray();
			for (JsonElement elt : array) {
				result.add(parsePlanning(elt));
			}
		} else if (root.isJsonObject()) {
			result.add(parsePlanning(root));
		}
		return result;
	}

	/**
	 * Parse a JSON representation of a planning and returns the created {@link TuleapPlanning} instance.
	 * 
	 * @param json
	 *            The JSON representation of the planning
	 * @return A new instance of {@link TuleapPlanning} containing the json data.
	 */
	public TuleapPlanning parsePlanning(String json) {
		JsonParser parser = new JsonParser();
		JsonElement planningElement = parser.parse(json);
		TuleapPlanning planning = parsePlanning(planningElement);
		return planning;
	}

	/**
	 * Parse a JSON representation of a planning and returns the created {@link TuleapPlanning} instance.
	 * 
	 * @param planningElement
	 *            the JSON element to parse
	 * @return A new instance of {@link TuleapPlanning} containing the json data.
	 */
	private TuleapPlanning parsePlanning(JsonElement planningElement) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
		TuleapPlanning planning = gson.fromJson(planningElement, TuleapPlanning.class);
		return planning;
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
		return parseMilestone(milestoneElement);
	}

	/**
	 * Parses a JSON String representing a milestone into a POJO.
	 * 
	 * @param element
	 *            The JSON element representing an milestone
	 * @return a POJO populated with the data from the JSON String.
	 */
	public TuleapMilestone parseMilestone(JsonElement element) {
		return new TuleapMilestoneDeserializer().deserialize(element, TuleapMilestone.class, null);
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
		return parseBacklogItem(backlogElement);
	}

	/**
	 * Parses a JSON String representing a backlogItem into a POJO.
	 * 
	 * @param element
	 *            The JSON element representing a backlogItem
	 * @return a POJO populated with the data from the JSON String.
	 */
	public TuleapBacklogItem parseBacklogItem(JsonElement element) {
		return new TuleapBacklogItemDeserializer().deserialize(element, TuleapBacklogItem.class, null);
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
	public TuleapErrorMessage getErrorMessage(String jsonResponse) {
		Gson gson = new Gson();
		return gson.fromJson(jsonResponse, TuleapErrorMessage.class);
	}
}
