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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import org.tuleap.mylyn.task.internal.core.model.config.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.config.ITuleapTrackerConstants;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapResource;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapTracker;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapWorkflowTransition;
import org.tuleap.mylyn.task.internal.core.model.config.field.AbstractTuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapArtifactLink;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapComputedValue;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapDate;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapFileUpload;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapFloat;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapInteger;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapMultiSelectBox;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapOpenList;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.model.config.field.TuleapText;

/**
 * This class is used to deserialize the JSON representation of a tracker.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapTrackerDeserializer implements JsonDeserializer<TuleapTracker> {

	/**
	 * The identifier keyword.
	 */
	private static final String ID = "id"; //$NON-NLS-1$

	/**
	 * The uri keyword.
	 */
	private static final String URI = "uri"; //$NON-NLS-1$

	/**
	 * The url keyword.
	 */
	private static final String URL = "html_url"; //$NON-NLS-1$

	/**
	 * The label keyword.
	 */
	private static final String LABEL = "label"; //$NON-NLS-1$

	/**
	 * The fields keyword.
	 */
	private static final String FIELDS = "fields"; //$NON-NLS-1$

	/**
	 * The type keyword.
	 */
	private static final String TYPE = "type"; //$NON-NLS-1$

	/**
	 * The permissions keyword.
	 */
	private static final String PERMISSIONS = "permissions"; //$NON-NLS-1$

	/**
	 * The create keyword.
	 */
	private static final String CREATE = "create"; //$NON-NLS-1$

	/**
	 * The values keyword.
	 */
	private static final String VALUES = "values"; //$NON-NLS-1$

	/**
	 * The binding keyword.
	 */
	private static final String BINDINGS = "bindings"; //$NON-NLS-1$

	/**
	 * The field id keyword.
	 */
	private static final String FIELD_ID = "field_id"; //$NON-NLS-1$

	/**
	 * The field value id keyword.
	 */
	private static final String FIELD_VALUE_ID = "id"; //$NON-NLS-1$

	/**
	 * The field value label keyword.
	 */
	private static final String FIELD_VALUE_LABEL = "label"; //$NON-NLS-1$

	/**
	 * The bind type keyword.
	 */
	private static final String BIND_TYPE = "type"; //$NON-NLS-1$

	/**
	 * The title keyword.
	 */
	private static final String TITLE = "title"; //$NON-NLS-1$

	/**
	 * The from field value id keyword.
	 */
	private static final String FROM_FIELD_VALUE_ID = "from_id"; //$NON-NLS-1$

	/**
	 * The to field value id keyword.
	 */
	private static final String TO_FIELD_VALUE_ID = "to_id"; //$NON-NLS-1$

	/**
	 * The transitions keyword.
	 */
	private static final String TRANSITIONS = "transitions"; //$NON-NLS-1$

	/**
	 * The contributors keyword.
	 */
	private static final String JSON_CONTRIBUTORS = "contributors"; //$NON-NLS-1$

	/**
	 * The open status values field keyword.
	 */
	private static final String JSON_STATUS_IDS = "value_ids"; //$NON-NLS-1$

	/**
	 * The status keyword.
	 */
	private static final String STATUS = "status"; //$NON-NLS-1$

	/**
	 * Indicates that the user can submit a newly created artifact with the field set.
	 */
	private static final String PERMISSION_SUBMIT = "submit"; //$NON-NLS-1$

	/**
	 * The resources keyword.
	 */
	private static final String RESOURCES = "resources"; //$NON-NLS-1$

	/**
	 * Returns the id of the given object.
	 * 
	 * @param jsonObject
	 *            The json object
	 * @return The id of the given object
	 */
	protected int getId(JsonObject jsonObject) {
		return jsonObject.get(ID).getAsInt();
	}

	/**
	 * Returns the url of the given object.
	 * 
	 * @param jsonObject
	 *            The json object
	 * @return The url of the given object
	 */
	protected String getUrl(JsonObject jsonObject) {
		return jsonObject.get(URL).getAsString();
	}

	/**
	 * Returns the uri of the given object.
	 * 
	 * @param jsonObject
	 *            The json object
	 * @return The uri of the given object
	 */
	protected String getUri(JsonObject jsonObject) {
		return jsonObject.get(URI).getAsString();
	}

	/**
	 * Returns the label of the given object.
	 * 
	 * @param jsonObject
	 *            The json object
	 * @return The label of the given object
	 */
	protected String getLabel(JsonObject jsonObject) {
		return jsonObject.get(LABEL).getAsString();
	}

	/**
	 * Populates the given tracker with the fields parsed from the given JSON object .
	 * 
	 * @param tracker
	 *            The tracker to populate
	 * @param jsonObject
	 *            The JSON object to parse
	 * @return The populated tracker
	 */
	protected TuleapTracker populateConfigurableFields(TuleapTracker tracker, JsonObject jsonObject) {
		JsonArray milestoneTypeFieldsArray = jsonObject.get(FIELDS).getAsJsonArray();

		JsonElement eltSemantic = jsonObject.get(ITuleapTrackerConstants.SEMANTICS);
		JsonObject fieldSemantic = null;
		if (eltSemantic != null) {
			fieldSemantic = eltSemantic.getAsJsonObject();
		}
		for (int i = 0; i < milestoneTypeFieldsArray.size(); i++) {
			JsonObject field = (JsonObject)milestoneTypeFieldsArray.get(i);

			// the field id
			int fieldId = field.get(FIELD_ID).getAsInt();

			// the field type
			String fieldType = field.get(TYPE).getAsString();
			AbstractTuleapField tuleapField = null;
			if (ITuleapTrackerConstants.TYPE_STRING.equals(fieldType)) {
				tuleapField = new TuleapString(fieldId);
			} else if (ITuleapTrackerConstants.TYPE_TEXT.equals(fieldType)) {
				tuleapField = new TuleapText(fieldId);
			} else if (ITuleapTrackerConstants.TYPE_COMPUTED.equals(fieldType)) {
				tuleapField = new TuleapComputedValue(fieldId);
			} else if (ITuleapTrackerConstants.TYPE_SB.equals(fieldType)) {
				tuleapField = new TuleapSelectBox(fieldId);
			} else if (ITuleapTrackerConstants.TYPE_MSB.equals(fieldType)) {
				tuleapField = new TuleapMultiSelectBox(fieldId);
			} else if (ITuleapTrackerConstants.TYPE_CB.equals(fieldType)) {
				tuleapField = new TuleapMultiSelectBox(fieldId);
			} else if (ITuleapTrackerConstants.TYPE_DATE.equals(fieldType)) {
				tuleapField = new TuleapDate(fieldId);
			} else if (ITuleapTrackerConstants.TYPE_INT.equals(fieldType)) {
				tuleapField = new TuleapInteger(fieldId);
			} else if (ITuleapTrackerConstants.TYPE_FLOAT.equals(fieldType)) {
				tuleapField = new TuleapFloat(fieldId);
			} else if (ITuleapTrackerConstants.TYPE_TBL.equals(fieldType)) {
				tuleapField = new TuleapOpenList(fieldId);
			} else if (ITuleapTrackerConstants.TYPE_ARTIFACT_LINK.equals(fieldType)) {
				tuleapField = new TuleapArtifactLink(fieldId);
			} else if (ITuleapTrackerConstants.TYPE_FILE.equals(fieldType)) {
				tuleapField = new TuleapFileUpload(fieldId);
			}

			if (tuleapField != null) {

				// the field label
				tuleapField.setLabel(field.get(LABEL).getAsString());

				// the field permissions
				JsonArray permissions = field.get(PERMISSIONS).getAsJsonArray();
				String[] permissionsArray = new String[permissions.size()];

				for (int j = 0; j < permissions.size(); j++) {
					if (CREATE.equals(permissions.get(j).getAsString())) {
						permissionsArray[j] = PERMISSION_SUBMIT;
					} else {
						permissionsArray[j] = permissions.get(j).getAsString();
					}
				}

				tuleapField.setPermissions(permissionsArray);

				// The Multi Select Box case
				JsonArray fieldValuesArray = null;
				JsonObject fieldBinding = null;
				JsonElement valuesElement = field.get(VALUES);
				if (valuesElement != null) {
					fieldValuesArray = valuesElement.getAsJsonArray();
				}
				JsonElement bindingElement = field.get(BINDINGS);
				if (bindingElement != null) {
					fieldBinding = bindingElement.getAsJsonObject();
				}

				// The semantic part
				if (tuleapField instanceof TuleapMultiSelectBox) {
					fillTuleapMultiSelectBoxField(tracker, (TuleapMultiSelectBox)tuleapField,
							fieldValuesArray, fieldSemantic, fieldBinding);
					// The Select Box case
				} else if (tuleapField instanceof TuleapSelectBox) {
					fillTuleapSelectBoxField(tracker, (TuleapSelectBox)tuleapField, fieldValuesArray,
							fieldSemantic, fieldBinding, jsonObject);
				}
				tracker.addField(tuleapField);
			}
		}

		// the semantic title part
		if (fieldSemantic != null) {
			this.fillTitleSemantic(tracker, fieldSemantic);
		}

		// Resources
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		JsonArray resources = jsonObject.get(RESOURCES).getAsJsonArray();
		TuleapResource[] trackerResources = gson.fromJson(resources, TuleapResource[].class);
		tracker.setTrackerResources(trackerResources);

		return tracker;
	}

	/**
	 * Returns the TuleapMultiSelectBox created from the parsing of the JSON elements.
	 * 
	 * @param tracker
	 *            The tracker
	 * @param multiSelectBoxField
	 *            The field
	 * @param fieldValuesArray
	 *            The values
	 * @param fieldSemantic
	 *            The semantic
	 * @param fieldBinding
	 *            The binding
	 */
	private void fillTuleapMultiSelectBoxField(TuleapTracker tracker,
			TuleapMultiSelectBox multiSelectBoxField, JsonArray fieldValuesArray, JsonObject fieldSemantic,
			JsonObject fieldBinding) {
		fillSelectBoxItem(tracker, multiSelectBoxField, fieldValuesArray, fieldSemantic, fieldBinding);
	}

	/**
	 * Returns the TuleapSelectBox created from the parsing of the JSON elements.
	 * 
	 * @param tracker
	 *            The tracker
	 * @param selectBoxField
	 *            The field
	 * @param fieldValuesArray
	 *            The values array
	 * @param fieldSemantic
	 *            The semantic
	 * @param fieldBinding
	 *            The binding
	 * @param root
	 *            The root json object
	 */
	private void fillTuleapSelectBoxField(TuleapTracker tracker, TuleapSelectBox selectBoxField,
			JsonArray fieldValuesArray, JsonObject fieldSemantic, JsonObject fieldBinding, JsonObject root) {
		fillSelectBoxItem(tracker, selectBoxField, fieldValuesArray, fieldSemantic, fieldBinding);
		// the workflow
		this.fillWorkflow(root, selectBoxField);
	}

	/**
	 * Returns the TuleapSelectBox created from the parsing of the JSON elements.
	 * 
	 * @param tracker
	 *            The tracker
	 * @param selectBoxField
	 *            The field
	 * @param fieldValuesArray
	 *            The values array
	 * @param fieldSemantic
	 *            The semantic
	 * @param fieldBinding
	 *            The binding
	 */
	private void fillSelectBoxItem(TuleapTracker tracker, AbstractTuleapSelectBox selectBoxField,
			JsonArray fieldValuesArray, JsonObject fieldSemantic, JsonObject fieldBinding) {
		if (fieldValuesArray != null) {
			for (int i = 0; i < fieldValuesArray.size(); i++) {
				JsonObject value = (JsonObject)fieldValuesArray.get(i);
				int fieldValueId = value.get(FIELD_VALUE_ID).getAsInt();
				String fieldValueLabel = value.get(FIELD_VALUE_LABEL).getAsString();
				TuleapSelectBoxItem selectBoxItem = new TuleapSelectBoxItem(fieldValueId);
				selectBoxItem.setLabel(fieldValueLabel);
				selectBoxField.addItem(selectBoxItem);

				// the semantic status part
				JsonObject semanticStatus = fieldSemantic.get(STATUS).getAsJsonObject();
				for (int z = 0; z < semanticStatus.get(JSON_STATUS_IDS).getAsJsonArray().size(); z++) {
					if (selectBoxField.getIdentifier() == semanticStatus.get(FIELD_ID).getAsInt()
							&& fieldValueId == semanticStatus.get(JSON_STATUS_IDS).getAsJsonArray().get(z)
									.getAsInt()) {
						selectBoxField.getOpenStatus().add(selectBoxItem);
					}
				}
			}
		}

		// the binding
		if (fieldBinding != null) {
			String bindingType = fieldBinding.get(BIND_TYPE).getAsString();
			selectBoxField.setBinding(bindingType);
			// No need of a specific treatment for users bindings, the list of users is provided
			// if (BIND_TYPE_USERS.equals(bindingType)) {
			// fillUsers(selectBoxField, fieldBinding);
			// }
		}

		// the semantic contributors part
		if (fieldSemantic != null
				&& fieldSemantic.get(JSON_CONTRIBUTORS) != null
				&& fieldSemantic.get(JSON_CONTRIBUTORS).getAsJsonObject().get(FIELD_ID).getAsInt() == selectBoxField
						.getIdentifier()) {
			selectBoxField.setSemanticContributor(true);
		}
	}

	/**
	 * Finds the fields with the identifier matching the field_id used for the title semantic and indicate in
	 * the project that it represents the title.
	 * 
	 * @param tracker
	 *            The tracker
	 * @param fieldSemantic
	 *            The semantic field
	 */
	private void fillTitleSemantic(TuleapTracker tracker, JsonObject fieldSemantic) {
		if (fieldSemantic.get(TITLE) != null) {
			JsonObject semanticTitle = fieldSemantic.get(TITLE).getAsJsonObject();
			for (AbstractTuleapField tuleapSemanticField : tracker.getFields()) {
				if (tuleapSemanticField.getIdentifier() == semanticTitle.get(FIELD_ID).getAsInt()
						&& tuleapSemanticField instanceof TuleapString) {
					TuleapString stringfield = (TuleapString)tuleapSemanticField;
					stringfield.setSemanticTitle(true);
				}
			}
		}
	}

	/**
	 * Sets the workflow and the transition for the select box.
	 * 
	 * @param jsonObject
	 *            the JSON root object
	 * @param selectBoxField
	 *            the select box field
	 */
	private void fillWorkflow(JsonObject jsonObject, TuleapSelectBox selectBoxField) {
		JsonElement workflowJsonElement = jsonObject.get(ITuleapTrackerConstants.WORKFLOW);
		if (workflowJsonElement != null) {
			JsonObject workflowJsonObject = workflowJsonElement.getAsJsonObject();

			if (workflowJsonObject.get(FIELD_ID).getAsInt() == selectBoxField.getIdentifier()) {
				// the workflow transitions
				JsonArray transitionsJsonArray = workflowJsonObject.get(TRANSITIONS).getAsJsonArray();
				for (JsonElement transitionJsonElement : transitionsJsonArray) {
					if (transitionJsonElement instanceof JsonObject) {
						JsonObject transitionJsonObject = (JsonObject)transitionJsonElement;
						int from = transitionJsonObject.get(FROM_FIELD_VALUE_ID).getAsInt();
						int to = transitionJsonObject.get(TO_FIELD_VALUE_ID).getAsInt();

						TuleapWorkflowTransition workflowTransition = new TuleapWorkflowTransition();
						workflowTransition.setFrom(from);
						workflowTransition.setTo(to);

						selectBoxField.getWorkflow().addTransition(workflowTransition);
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	public TuleapTracker deserialize(JsonElement rootJsonElement, Type type,
			JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		JsonObject jsonObject = rootJsonElement.getAsJsonObject();

		int identifier = this.getId(jsonObject);
		String url = this.getUrl(jsonObject);
		String label = this.getLabel(jsonObject);
		String uri = jsonObject.get(URI).getAsString();
		String itemName = null;
		String description = null;
		long lastUpdateDate = System.currentTimeMillis();

		TuleapTracker tracker = new TuleapTracker(identifier, url, label, itemName, description,
				lastUpdateDate);
		tracker.setUri(uri);

		tracker = this.populateConfigurableFields(tracker, jsonObject);

		return tracker;
	}
}
