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
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.internal.core.TuleapCoreActivator;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskMapper;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapDate;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFileUpload;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFloat;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapInteger;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapMultiSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.repository.TuleapAttributeMapper;

/**
 * This class is used to encapsulate all the logic of the JSON parsing.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapJsonParser {

	/**
	 * The key used for the id.
	 */
	private static final String ID = "id"; //$NON-NLS-1$

	/**
	 * The key used for the tracker id.
	 */
	private static final String TRACKER_ID = "tracker_id"; //$NON-NLS-1$

	/**
	 * The key used for the URL.
	 */
	private static final String URL = "url"; //$NON-NLS-1$

	/**
	 * The key used for the kind.
	 */
	private static final String KIND = "kind"; //$NON-NLS-1$

	/**
	 * The key used for the milestone URL.
	 */
	private static final String MILESTONE_URL = "milestone_url"; //$NON-NLS-1$

	/**
	 * The key used for the HTML URL.
	 */
	private static final String HTML_URL = "html_url"; //$NON-NLS-1$

	/**
	 * The key used for the HTML URL.
	 */
	private static final String SUBMITTED_BY = "submitted_by"; //$NON-NLS-1$

	/**
	 * The key used for the HTML URL.
	 */
	private static final String SUBMITTED_ON = "submitted_on"; //$NON-NLS-1$

	/**
	 * The key used for the HTML URL.
	 */
	private static final String LAST_UPDATED_ON = "last_updated_on"; //$NON-NLS-1$

	/**
	 * The key used for the values (i.e. the field values of a tracker).
	 */
	private static final String VALUES = "values"; //$NON-NLS-1$

	/**
	 * The key used for the value.
	 */
	private static final String VALUE = "value"; //$NON-NLS-1$

	/**
	 * The key used for the field id.
	 */
	private static final String FIELD_ID = "field_id"; //$NON-NLS-1$

	/**
	 * The key used for the field label.
	 */
	private static final String FIELD_LABEL = "field_label"; //$NON-NLS-1$

	/**
	 * The key used for the field value.
	 */
	private static final String FIELD_VALUE = "field_value"; //$NON-NLS-1$

	/**
	 * The key used for the field bind value (only one).
	 */
	private static final String BIND_VALUE = "bind_value"; //$NON-NLS-1$

	/**
	 * The key used for the field bind values (several).
	 */
	private static final String BIND_VALUES = "bind_values"; //$NON-NLS-1$

	/**
	 * The key used for the field bind value id.
	 */
	private static final String BIND_VALUE_ID = "bind_value_id"; //$NON-NLS-1$

	/**
	 * The key used for the field bind value label.
	 */
	private static final String BIND_VALUE_LABEL = "bind_value_label"; //$NON-NLS-1$

	/**
	 * The key used for the field comments.
	 */
	private static final String COMMENTS = "comments"; //$NON-NLS-1$

	/**
	 * The key used for the field email.
	 */
	private static final String EMAIL = "email"; //$NON-NLS-1$

	/**
	 * The key used for the field body.
	 */
	private static final String BODY = "body"; //$NON-NLS-1$

	/**
	 * The key used for the field files_description.
	 */
	private static final String FILES_DESCRIPTION = "files_description"; //$NON-NLS-1$

	/**
	 * The key used for the field file_id.
	 */
	private static final String FILE_ID = "file_id"; //$NON-NLS-1$

	/**
	 * The key used for the field description.
	 */
	private static final String DESCRIPTION = "description"; //$NON-NLS-1$

	/**
	 * The key used for the field name.
	 */
	private static final String NAME = "name"; //$NON-NLS-1$

	/**
	 * The key used for the field size.
	 */
	private static final String SIZE = "size"; //$NON-NLS-1$

	/**
	 * The key used for the field type.
	 */
	private static final String TYPE = "type"; //$NON-NLS-1$

	/**
	 * The key used for the field action.
	 */
	private static final String ACTION = "action"; //$NON-NLS-1$

	/**
	 * Parses the JSON representation of one Tuleap project and returns its configuration.
	 * 
	 * @param jsonResponse
	 *            The JSON representation of a Tuleap project.
	 * @return The Tuleap configuration matching the JSON representation
	 */
	public TuleapProjectConfiguration getProjectConfiguration(String jsonResponse) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TuleapProjectConfiguration.class,
				new TuleapProjectConfigurationDeserializer());

		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObject = jsonParser.parse(jsonResponse).getAsJsonObject();

		Gson gson = gsonBuilder.create();
		TuleapProjectConfiguration tuleapProjectConfiguration = gson.fromJson(jsonObject,
				TuleapProjectConfiguration.class);

		return tuleapProjectConfiguration;
	}

	/**
	 * Parses the JSON representation of a collection of Tuleap projects and returns their configuration.
	 * 
	 * @param jsonResponse
	 *            The JSON representation of a collection of Tuleap projects
	 * @return The list of the Tuleap project's configuration
	 */
	public List<TuleapProjectConfiguration> getProjectConfigurations(String jsonResponse) {
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
	public List<TuleapTrackerConfiguration> getTrackerConfigurations(String jsonResponse) {
		return null;
	}

	/**
	 * Parses a JSON String representing an Artifact into a TaskData.
	 * 
	 * @param taskRepository
	 *            The task repository to use
	 * @param connector
	 *            The repository connector to use
	 * @param json
	 *            The JSON response representing an artifact
	 * @return a new TaksData populated with the data from the JSON String interpreted according to the given
	 *         configuration.
	 * @throws CoreException
	 *             If a problem occurs, such as the impossibility to retrieve the configuration of the
	 *             artifact's tracker.
	 */
	public TaskData parseArtifact(TaskRepository taskRepository, ITuleapRepositoryConnector connector,
			String json) throws CoreException {
		JsonParser jsonParser = new JsonParser();
		JsonObject obj = jsonParser.parse(json).getAsJsonObject();
		int artifactId = obj.get(ID).getAsInt();
		int trackerId = obj.get(TRACKER_ID).getAsInt();

		TuleapServerConfiguration config = connector.getRepositoryConfiguration(taskRepository
				.getRepositoryUrl());
		TuleapTrackerConfiguration trackerConfig = config.getTrackerConfiguration(trackerId);

		// Check that the given configuration is that of the right tracker
		if (trackerConfig == null) {
			throw new CoreException(new Status(Status.ERROR, TuleapCoreActivator.PLUGIN_ID,
					"Unable to find the configuration of tracker " + trackerId)); // TODO [i18n] //$NON-NLS-1$
		}

		int submittedBy = obj.get(SUBMITTED_BY).getAsInt();
		int submittedOn = obj.get(SUBMITTED_ON).getAsInt();
		int lastUpdatedOn = obj.get(LAST_UPDATED_ON).getAsInt();
		String kind = obj.get(KIND).getAsString();
		String url = obj.get(URL).getAsString();

		String milestoneUrl = null;
		JsonElement milestoneUrlElt = obj.get(MILESTONE_URL);
		if (milestoneUrlElt != null) {
			milestoneUrl = milestoneUrlElt.getAsString();
		}
		String htmlUrl = obj.get(HTML_URL).getAsString();
		JsonArray fields = obj.get(VALUES).getAsJsonArray();

		TaskData taskData = new TaskData(new TuleapAttributeMapper(taskRepository, connector), taskRepository
				.getConnectorKind(), taskRepository.getRepositoryUrl(), String.valueOf(artifactId));
		// Populate the task data using task mappers
		TuleapTaskMapper mapper = new TuleapTaskMapper(taskData, trackerConfig);
		mapper.initializeEmptyTaskData();
		mapper.setTrackerId(trackerId);
		mapper.setCreationDate(new Date(1000L * submittedOn));
		mapper.setModificationDate(new Date(1000L * lastUpdatedOn));
		mapper.setTaskUrl(url);

		// TODO Manage task key
		// mapper.setTaskKey(?);

		for (JsonElement element : fields) {
			JsonObject field = element.getAsJsonObject();
			int fieldId = field.get(FIELD_ID).getAsInt();
			String fieldLabel = field.get(FIELD_LABEL).getAsString();
			JsonObject fieldValue = field.get(FIELD_VALUE).getAsJsonObject();
			AbstractTuleapField fieldConfig = trackerConfig.getFieldById(fieldId);
			// TODO Manage the case of special fields, such as status and so on for which the id used in mylyn
			// TaskData is not the one used in tuleap
			if (fieldConfig instanceof TuleapSelectBox) {
				TuleapSelectBox selectBox = (TuleapSelectBox)fieldConfig;
				JsonObject bindValue = fieldValue.get(BIND_VALUE).getAsJsonObject();
				int bindValueId = bindValue.get(BIND_VALUE_ID).getAsInt();
				if (selectBox.isSemanticStatus()) {
					mapper.setStatus(String.valueOf(bindValueId));
				} else if (selectBox.isSemanticContributor()) {
					mapper.setAssignedTo(Collections.singleton(Integer.valueOf(bindValueId)));
				} else {
					mapper.setSelectBoxValue(bindValueId, fieldId);
				}
			} else if (fieldConfig instanceof TuleapMultiSelectBox) {
				TuleapMultiSelectBox selectBox = (TuleapMultiSelectBox)fieldConfig;
				JsonArray bindValues = fieldValue.get(BIND_VALUES).getAsJsonArray();
				Set<Integer> valueIds = Sets.newHashSet();
				for (JsonElement bindValue : bindValues) {
					JsonObject object = bindValue.getAsJsonObject();
					int bindValueId = object.get(BIND_VALUE_ID).getAsInt();
					valueIds.add(Integer.valueOf(bindValueId));
				}
				if (selectBox.isSemanticStatus()) {
					// TODO Support multiple status?
					mapper.setStatus(valueIds.iterator().next().toString());
				} else if (selectBox.isSemanticContributor()) {
					mapper.setAssignedTo(valueIds);
				} else {
					mapper.setMultiSelectBoxValues(valueIds, fieldId);
				}
			} else if (fieldConfig instanceof TuleapFileUpload) {
				// TODO is there something to do here?
			} else if (fieldConfig instanceof TuleapInteger) {
				mapper.setValue(String.valueOf(fieldValue.get(VALUE).getAsInt()), fieldId);
			} else if (fieldConfig instanceof TuleapFloat) {
				mapper.setValue(String.valueOf(fieldValue.get(VALUE).getAsFloat()), fieldId);
			} else if (fieldConfig instanceof TuleapDate) {
				// TODO mapper.setValue(String.valueOf(fieldValue.get(VALUE).getAsInt()), fieldId);
			} else {
				mapper.setValue(fieldValue.get(VALUE).getAsString(), fieldId);
			}
		}

		return taskData;
	}

	/**
	 * Parses a JSON String representing a milestone into a POJO.
	 * 
	 * @param taskRepository
	 *            The task repository to use
	 * @param connector
	 *            The repository connector to use
	 * @param json
	 *            The JSON response representing an artifact
	 * @return a POJO populated with the data from the JSON String.
	 * @throws CoreException
	 *             If a problem occurs.
	 */
	public TaskData parseMilestone(TaskRepository taskRepository, ITuleapRepositoryConnector connector,
			String json) throws CoreException {
		// TODO
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
