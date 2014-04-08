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
package org.eclipse.mylyn.tuleap.core.internal.parser;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map.Entry;

import org.eclipse.core.runtime.Assert;
import org.eclipse.mylyn.tuleap.core.internal.TuleapCoreActivator;
import org.eclipse.mylyn.tuleap.core.internal.model.config.AbstractTuleapField;
import org.eclipse.mylyn.tuleap.core.internal.model.config.ITuleapTrackerConstants;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapResource;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapTracker;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapWorkflowTransition;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.AbstractTuleapSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapArtifactLink;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapComputedValue;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapDate;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapFileUpload;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapFloat;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapInteger;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapMultiSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapOpenList;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBoxItem;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapString;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapText;
import org.eclipse.mylyn.tuleap.core.internal.util.TuleapCoreKeys;
import org.eclipse.mylyn.tuleap.core.internal.util.TuleapCoreMessages;

/**
 * This class is used to deserialize the JSON representation of a tracker.
 *
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapTrackerDeserializer implements JsonDeserializer<TuleapTracker> {

	/**
	 * The JSON field for the item name.
	 */
	private static final String ITEM_NAME = "item_name"; //$NON-NLS-1$

	/**
	 * The JSON field key for the tracker description.
	 */
	private static final String DESCRIPTION = "description"; //$NON-NLS-1$

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
	 * The transitions keyword.
	 */
	private static final String TRANSITIONS = "transitions"; //$NON-NLS-1$

	/**
	 * The contributor keyword.
	 */
	private static final String JSON_CONTRIBUTOR = "contributor"; //$NON-NLS-1$

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
	 * Set of known field types.
	 */
	private static final ImmutableSet<String> KNOWN_FIELD_TYPES = ImmutableSet.of(
			ITuleapTrackerConstants.TYPE_AID, ITuleapTrackerConstants.TYPE_ARTIFACT_LINK,
			ITuleapTrackerConstants.TYPE_BURNDOWN, ITuleapTrackerConstants.TYPE_CB,
			ITuleapTrackerConstants.TYPE_COMPUTED, ITuleapTrackerConstants.TYPE_CROSS_REFERENCES,
			ITuleapTrackerConstants.TYPE_DATE, ITuleapTrackerConstants.TYPE_FILE,
			ITuleapTrackerConstants.TYPE_FLOAT, ITuleapTrackerConstants.TYPE_INT,
			ITuleapTrackerConstants.TYPE_LAST_UPDATED_ON, ITuleapTrackerConstants.TYPE_MSB,
			ITuleapTrackerConstants.TYPE_PERM, ITuleapTrackerConstants.TYPE_SB,
			ITuleapTrackerConstants.TYPE_STRING, ITuleapTrackerConstants.TYPE_SUBMITTED_BY,
			ITuleapTrackerConstants.TYPE_SUBMITTED_ON, ITuleapTrackerConstants.TYPE_TBL,
			ITuleapTrackerConstants.TYPE_TEXT);

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
	 * @param context
	 *            the deserialization context
	 * @return The populated tracker
	 */
	protected TuleapTracker populateConfigurableFields(TuleapTracker tracker, JsonObject jsonObject,
			JsonDeserializationContext context) {
		JsonElement jsonFields = jsonObject.get(FIELDS);
		JsonElement eltSemantic = jsonObject.get(ITuleapTrackerConstants.SEMANTICS);
		JsonObject fieldSemantic = null;
		if (eltSemantic != null && eltSemantic.isJsonObject()) {
			fieldSemantic = eltSemantic.getAsJsonObject();
		}
		if (jsonFields.isJsonArray()) {
			JsonArray milestoneTypeFieldsArray = jsonFields.getAsJsonArray();
			for (JsonElement fieldElt : milestoneTypeFieldsArray) {
				JsonObject field = (JsonObject)fieldElt;
				AbstractTuleapField tuleapField = getField(field, tracker, jsonObject, fieldSemantic, context);
				if (tuleapField != null) {
					tracker.addField(tuleapField);
				}
			}
		} else if (jsonFields.isJsonObject()) {
			for (Entry<String, JsonElement> entry : jsonFields.getAsJsonObject().entrySet()) {
				JsonObject field = (JsonObject)entry.getValue();
				AbstractTuleapField tuleapField = getField(field, tracker, jsonObject, fieldSemantic, context);
				if (tuleapField != null) {
					tracker.addField(tuleapField);
				}
			}
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
	 * Extract a field from its JSON representation and adds it to the given tracker.
	 *
	 * @param field
	 *            The JSON representation of the field
	 * @param tracker
	 *            The tracker
	 * @param jsonObject
	 *            The parent JSON object of the field
	 * @param fieldSemantic
	 *            The JSON representation of the semantics part
	 * @param context
	 *            the deserialization context
	 * @return The relevant kind of TuleapField, can be null for unknown or unmanaged field types.
	 */
	private AbstractTuleapField getField(JsonObject field, TuleapTracker tracker, JsonObject jsonObject,
			JsonObject fieldSemantic, JsonDeserializationContext context) {
		int fieldId = field.get(FIELD_ID).getAsInt();
		String fieldType = field.get(TYPE).getAsString();
		AbstractTuleapField tuleapField = createField(fieldId, fieldType);
		if (tuleapField != null) {
			// the field label
			tuleapField.setLabel(field.get(LABEL).getAsString());
			tuleapField.setName(field.get("name").getAsString()); //$NON-NLS-1$

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
			if (valuesElement != null && !valuesElement.isJsonNull()) {
				fieldValuesArray = valuesElement.getAsJsonArray();
			}
			JsonElement bindingElement = field.get(BINDINGS);
			if (bindingElement != null && !bindingElement.isJsonNull()) {
				fieldBinding = bindingElement.getAsJsonObject();
			}
			if (tuleapField instanceof TuleapMultiSelectBox) {
				fillTuleapMultiSelectBoxField(tracker, (TuleapMultiSelectBox)tuleapField, fieldValuesArray,
						fieldSemantic, fieldBinding);
				// The Select Box case
			} else if (tuleapField instanceof TuleapSelectBox) {
				fillTuleapSelectBoxField(tracker, (TuleapSelectBox)tuleapField, fieldValuesArray,
						fieldSemantic, fieldBinding, jsonObject, context);
			}
			manageTitleSemantic(tuleapField, fieldSemantic);
		}
		return tuleapField;
	}

	/**
	 * Creates the right kind of {@link AbstractTuleapField}.
	 *
	 * @param fieldId
	 *            The ID of the field to create
	 * @param fieldType
	 *            The JSON type of the field to create
	 * @return A new instance if the given type is known, or <code>null</code> otherwise.
	 */
	private AbstractTuleapField createField(int fieldId, String fieldType) {
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
		} else if (!KNOWN_FIELD_TYPES.contains(fieldType)) {
			TuleapCoreActivator.log(TuleapCoreMessages.getString(TuleapCoreKeys.unsupportedTrackerFieldType,
					fieldType), false);
		}
		return tuleapField;
	}

	/**
	 * Sets the given field to be the summary field if it's the case.
	 *
	 * @param field
	 *            A candidate field
	 * @param fieldSemantic
	 *            The semantic field
	 */
	private void manageTitleSemantic(AbstractTuleapField field, JsonObject fieldSemantic) {
		Assert.isNotNull(field);
		if (fieldSemantic != null) {
			if (fieldSemantic.get(TITLE) != null) {
				JsonObject semanticTitle = fieldSemantic.get(TITLE).getAsJsonObject();
				if (field.getIdentifier() == semanticTitle.get(FIELD_ID).getAsInt()
						&& field instanceof TuleapString) {
					((TuleapString)field).setSemanticTitle(true);
				}
			}
		}
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
	 * @param context
	 *            the deserialization context
	 */
	private void fillTuleapSelectBoxField(TuleapTracker tracker, TuleapSelectBox selectBoxField,
			JsonArray fieldValuesArray, JsonObject fieldSemantic, JsonObject fieldBinding, JsonObject root,
			JsonDeserializationContext context) {
		fillSelectBoxItem(tracker, selectBoxField, fieldValuesArray, fieldSemantic, fieldBinding);
		// the workflow
		this.fillWorkflow(root, selectBoxField, context);
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
				if (fieldSemantic != null) {
					extractOpenStatuses(selectBoxField, fieldSemantic, fieldValueId, selectBoxItem);
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
				&& fieldSemantic.get(JSON_CONTRIBUTOR) != null
				&& fieldSemantic.get(JSON_CONTRIBUTOR).getAsJsonObject().get(FIELD_ID).getAsInt() == selectBoxField
						.getIdentifier()) {
			selectBoxField.setSemanticContributor(true);
		}
	}

	/**
	 * Extract the open statuses and configures them.
	 *
	 * @param selectBoxField
	 *            The select box field
	 * @param fieldSemantic
	 *            The semantic
	 * @param fieldValueId
	 *            The ID
	 * @param selectBoxItem
	 *            The item
	 */
	private void extractOpenStatuses(AbstractTuleapSelectBox selectBoxField, JsonObject fieldSemantic,
			int fieldValueId, TuleapSelectBoxItem selectBoxItem) {
		JsonElement statusElement = fieldSemantic.get(STATUS);
		if (statusElement != null && statusElement.isJsonObject()) {
			JsonObject semanticStatus = statusElement.getAsJsonObject();
			for (int z = 0; z < semanticStatus.get(JSON_STATUS_IDS).getAsJsonArray().size(); z++) {
				if (selectBoxField.getIdentifier() == semanticStatus.get(FIELD_ID).getAsInt()
						&& fieldValueId == semanticStatus.get(JSON_STATUS_IDS).getAsJsonArray().get(z)
								.getAsInt()) {
					selectBoxField.getOpenStatus().add(selectBoxItem);
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
	 * @param context
	 *            the deserialization context
	 */
	private void fillWorkflow(JsonObject jsonObject, TuleapSelectBox selectBoxField,
			JsonDeserializationContext context) {
		JsonElement workflowJsonElement = jsonObject.get(ITuleapTrackerConstants.WORKFLOW);
		if (workflowJsonElement != null && !workflowJsonElement.isJsonNull()) {
			JsonObject workflowJsonObject = workflowJsonElement.getAsJsonObject();

			if (workflowJsonObject.get(FIELD_ID).getAsInt() == selectBoxField.getIdentifier()) {
				// the workflow transitions
				JsonArray transitionsJsonArray = workflowJsonObject.get(TRANSITIONS).getAsJsonArray();
				for (JsonElement transitionElement : transitionsJsonArray) {
					TuleapWorkflowTransition workflowTransition = context.deserialize(transitionElement,
							TuleapWorkflowTransition.class);
					selectBoxField.getWorkflow().addTransition(workflowTransition);
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
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = rootJsonElement.getAsJsonObject();

		int identifier = this.getId(jsonObject);
		String url = this.getUrl(jsonObject);
		String label = this.getLabel(jsonObject);
		String uri = jsonObject.get(URI).getAsString();
		String itemName = null;
		if (jsonObject.has(ITEM_NAME) && jsonObject.get(ITEM_NAME).isJsonPrimitive()) {
			itemName = jsonObject.get(ITEM_NAME).getAsString();
		}
		String description = null;
		if (jsonObject.has(DESCRIPTION) && jsonObject.get(DESCRIPTION).isJsonPrimitive()) {
			description = jsonObject.get(DESCRIPTION).getAsString();
		}

		TuleapTracker tracker = new TuleapTracker(identifier, url, label, itemName, description, new Date());
		tracker.setUri(uri);

		tracker = this.populateConfigurableFields(tracker, jsonObject, context);

		return tracker;
	}
}
