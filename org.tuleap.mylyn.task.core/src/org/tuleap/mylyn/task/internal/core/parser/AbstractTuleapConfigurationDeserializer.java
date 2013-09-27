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
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.tuleap.mylyn.task.internal.core.config.ITuleapConfigurationConstants;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableFieldsConfiguration;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapArtifactLink;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapComputedValue;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapDate;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFileUpload;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFloat;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapInteger;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapMultiSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapOpenList;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapText;
import org.tuleap.mylyn.task.internal.core.model.workflow.TuleapWorkflowTransition;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * Common superclass for all the configuration deserializer.
 * 
 * @param <CONFIGURATION_TYPE>
 *            The type of the configuration.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public abstract class AbstractTuleapConfigurationDeserializer<CONFIGURATION_TYPE extends AbstractTuleapConfigurableFieldsConfiguration> implements JsonDeserializer<CONFIGURATION_TYPE> {

	/**
	 * The identifier key word.
	 */
	private static final String ID = "id"; //$NON-NLS-1$

	/**
	 * The url key word.
	 */
	private static final String URL = "url"; //$NON-NLS-1$

	/**
	 * The label key word.
	 */
	private static final String LABEL = "label"; //$NON-NLS-1$

	/**
	 * The fields key word.
	 */
	private static final String FIELDS = "fields"; //$NON-NLS-1$

	/**
	 * The type key word.
	 */
	private static final String TYPE = "type"; //$NON-NLS-1$

	/**
	 * The permissions key word.
	 */
	private static final String PERMISSIONS = "permissions"; //$NON-NLS-1$

	/**
	 * The create key word.
	 */
	private static final String CREATE = "create"; //$NON-NLS-1$

	/**
	 * The values key word.
	 */
	private static final String VALUES = "values"; //$NON-NLS-1$

	/**
	 * The binding key word.
	 */
	private static final String BINDING = "binding"; //$NON-NLS-1$

	/**
	 * The field id key word.
	 */
	private static final String FIELD_ID = "field_id"; //$NON-NLS-1$

	/**
	 * The field value id key word.
	 */
	private static final String FIELD_VALUE_ID = "field_value_id"; //$NON-NLS-1$

	/**
	 * The field value label key word.
	 */
	private static final String FIELD_VALUE_LABEL = "field_value_label"; //$NON-NLS-1$

	/**
	 * The bind type key word.
	 */
	private static final String BIND_TYPE = "bind_type"; //$NON-NLS-1$

	/**
	 * The title key word.
	 */
	private static final String TITLE = "title"; //$NON-NLS-1$

	/**
	 * The from field value id key word.
	 */
	private static final String FROM_FIELD_VALUE_ID = "from_field_value_id"; //$NON-NLS-1$

	/**
	 * The to field value id key word.
	 */
	private static final String TO_FIELD_VALUE_ID = "to_field_value_id"; //$NON-NLS-1$

	/**
	 * The transitions key word.
	 */
	private static final String TRANSITIONS = "transitions"; //$NON-NLS-1$

	/**
	 * The contributors key word.
	 */
	private static final String JSON_CONTRIBUTORS = "contributors"; //$NON-NLS-1$

	/**
	 * The field open status key word.
	 */
	private static final String JSON_OPEN_STATUS_IDS = "open_status_field_values_ids"; //$NON-NLS-1$

	/**
	 * The status key word.
	 */
	private static final String STATUS = "status"; //$NON-NLS-1$

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
	 * Populates the given configuration fields with the value parsed from the JSON object to parse.
	 * 
	 * @param configuration
	 *            The configuration to populate
	 * @param jsonObject
	 *            The JSON object to parse
	 * @return The populated configuration
	 */
	protected CONFIGURATION_TYPE populateConfigurableFields(CONFIGURATION_TYPE configuration,
			JsonObject jsonObject) {
		JsonArray milestoneTypeFieldsArray = jsonObject.get(FIELDS).getAsJsonArray();

		JsonObject fieldSemantic = jsonObject.get(ITuleapConfigurationConstants.SEMANTIC).getAsJsonObject();
		for (int i = 0; i < milestoneTypeFieldsArray.size(); i++) {
			JsonObject field = (JsonObject)milestoneTypeFieldsArray.get(i);

			// the field id
			int fieldId = field.get(FIELD_ID).getAsInt();

			// the field type
			String fieldType = field.get(TYPE).getAsString();
			AbstractTuleapField tuleapField = null;
			if (ITuleapConfigurationConstants.STRING.equals(fieldType)) {
				tuleapField = new TuleapString(fieldId);
			} else if (ITuleapConfigurationConstants.TEXT.equals(fieldType)) {
				tuleapField = new TuleapText(fieldId);
			} else if (ITuleapConfigurationConstants.COMPUTED.equals(fieldType)) {
				tuleapField = new TuleapComputedValue(fieldId);
			} else if (ITuleapConfigurationConstants.SB.equals(fieldType)) {
				tuleapField = new TuleapSelectBox(fieldId);
			} else if (ITuleapConfigurationConstants.MSB.equals(fieldType)) {
				tuleapField = new TuleapMultiSelectBox(fieldId);
			} else if (ITuleapConfigurationConstants.CB.equals(fieldType)) {
				tuleapField = new TuleapMultiSelectBox(fieldId);
			} else if (ITuleapConfigurationConstants.DATE.equals(fieldType)) {
				tuleapField = new TuleapDate(fieldId);
			} else if (ITuleapConfigurationConstants.INT.equals(fieldType)) {
				tuleapField = new TuleapInteger(fieldId);
			} else if (ITuleapConfigurationConstants.FLOAT.equals(fieldType)) {
				tuleapField = new TuleapFloat(fieldId);
			} else if (ITuleapConfigurationConstants.TBL.equals(fieldType)) {
				tuleapField = new TuleapOpenList(fieldId);
			} else if (ITuleapConfigurationConstants.ARTLINK.equals(fieldType)) {
				tuleapField = new TuleapArtifactLink(fieldId);
			} else if (ITuleapConfigurationConstants.FILE.equals(fieldType)) {
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
						permissionsArray[j] = ITuleapConstants.PERMISSION_SUBMIT;
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
				JsonElement bindingElement = field.get(BINDING);
				if (bindingElement != null) {
					fieldBinding = bindingElement.getAsJsonObject();
				}

				// The semantic part configuration
				if (tuleapField instanceof TuleapMultiSelectBox) {
					tuleapField = createTuleapMultiSelectBoxField(tuleapField, fieldValuesArray,
							fieldSemantic, fieldBinding);
					// The Select Box case
				} else if (tuleapField instanceof TuleapSelectBox) {
					tuleapField = createTuleapSelectBoxField(tuleapField, fieldValuesArray, fieldSemantic,
							fieldBinding, jsonObject);
				}
				configuration.addField(tuleapField);
			}
		}

		// the semantic title part
		if (fieldSemantic != null) {
			this.createTuleapStringField(configuration, fieldSemantic);
		}

		return configuration;
	}

	/**
	 * Returns the TuleapMultiSelectBox created from the parsing of the JSON elements.
	 * 
	 * @param tuleapField
	 *            The field
	 * @param fieldValuesArray
	 *            The values
	 * @param fieldSemantic
	 *            The semantic
	 * @param fieldBinding
	 *            The binding
	 * @return The resulted field
	 */
	private TuleapMultiSelectBox createTuleapMultiSelectBoxField(AbstractTuleapField tuleapField,
			JsonArray fieldValuesArray, JsonObject fieldSemantic, JsonObject fieldBinding) {
		TuleapMultiSelectBox multiSelectBoxField = (TuleapMultiSelectBox)tuleapField;
		if (fieldValuesArray != null) {
			for (int i = 0; i < fieldValuesArray.size(); i++) {
				JsonObject value = (JsonObject)fieldValuesArray.get(i);
				int fieldValueId = value.get(FIELD_VALUE_ID).getAsInt();
				String fieldValueLabel = value.get(FIELD_VALUE_LABEL).getAsString();
				TuleapSelectBoxItem selectBoxItem = new TuleapSelectBoxItem(fieldValueId);
				selectBoxItem.setLabel(fieldValueLabel);
				multiSelectBoxField.addItem(selectBoxItem);

				// the semantic status part
				if (fieldSemantic != null) {
					this.createSemanticStatus(fieldSemantic, multiSelectBoxField, fieldValueId, selectBoxItem);
				}
			}
		}

		// the binding
		if (fieldBinding != null) {
			multiSelectBoxField.setBinding(fieldBinding.get(BIND_TYPE).getAsString());
		}

		// the semantic contributors part
		this.createSemanticContributors(fieldSemantic, multiSelectBoxField);

		return multiSelectBoxField;
	}

	/**
	 * Deals with the semantic status JSON field.
	 * 
	 * @param fieldSemantic
	 *            the semantic field
	 * @param multiSelectBoxField
	 *            the multi-select box field
	 * @param fieldValueId
	 *            the field value Identifier
	 * @param selectBoxItem
	 *            the select box item
	 */
	private void createSemanticStatus(JsonObject fieldSemantic, TuleapMultiSelectBox multiSelectBoxField,
			int fieldValueId, TuleapSelectBoxItem selectBoxItem) {
		if (fieldSemantic.get(STATUS) != null) {
			JsonObject semanticStatus = fieldSemantic.get(STATUS).getAsJsonObject();
			if (multiSelectBoxField.getIdentifier() == semanticStatus.get(FIELD_ID).getAsInt()) {
				JsonArray openStatus = semanticStatus.get(JSON_OPEN_STATUS_IDS).getAsJsonArray();
				for (int j = 0; j < openStatus.size(); j++) {
					if (fieldValueId == openStatus.get(j).getAsInt()) {
						multiSelectBoxField.getOpenStatus().add(selectBoxItem);
					}
				}
			}
		}
	}

	/**
	 * Deals with the semantic contributors JSON field.
	 * 
	 * @param fieldSemantic
	 *            the semantic field
	 * @param multiSelectBoxField
	 *            the multi-select box field
	 */
	private void createSemanticContributors(JsonObject fieldSemantic, TuleapMultiSelectBox multiSelectBoxField) {
		if (fieldSemantic != null) {
			if (fieldSemantic.get(JSON_CONTRIBUTORS) != null) {
				JsonObject semanticContributor = fieldSemantic.get(JSON_CONTRIBUTORS).getAsJsonObject();
				if (semanticContributor.get(FIELD_ID).getAsInt() == multiSelectBoxField.getIdentifier()) {
					multiSelectBoxField.setSemanticContributor(true);
				}
			}
		}
	}

	/**
	 * Returns the TuleapSelectBox created from the parsing of the JSON elements.
	 * 
	 * @param tuleapField
	 *            The field
	 * @param fieldValuesArray
	 *            The values array
	 * @param fieldSemantic
	 *            The semantic
	 * @param fieldBinding
	 *            The binding
	 * @param jsonObject
	 *            The root json object
	 * @return The resulted field
	 */
	private TuleapSelectBox createTuleapSelectBoxField(AbstractTuleapField tuleapField,
			JsonArray fieldValuesArray, JsonObject fieldSemantic, JsonObject fieldBinding,
			JsonObject jsonObject) {
		TuleapSelectBox selectBoxField = (TuleapSelectBox)tuleapField;
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
				for (int z = 0; z < semanticStatus.get(JSON_OPEN_STATUS_IDS).getAsJsonArray().size(); z++) {
					if (selectBoxField.getIdentifier() == semanticStatus.get(FIELD_ID).getAsInt()
							&& fieldValueId == semanticStatus.get(JSON_OPEN_STATUS_IDS).getAsJsonArray().get(
									z).getAsInt()) {
						selectBoxField.getOpenStatus().add(selectBoxItem);
					}
				}
			}
		}

		// the binding
		if (fieldBinding != null) {
			selectBoxField.setBinding(fieldBinding.get(BIND_TYPE).getAsString());
		}

		// the semantic contributors part
		if (fieldSemantic.get(JSON_CONTRIBUTORS) != null
				&& fieldSemantic.get(JSON_CONTRIBUTORS).getAsJsonObject().get(FIELD_ID).getAsInt() == selectBoxField
						.getIdentifier()) {
			selectBoxField.setSemanticContributor(true);
		}

		// the workflow
		this.createTuleapWorkflowSelectBoxField(jsonObject, selectBoxField);
		return selectBoxField;
	}

	/**
	 * Finds the fields with the identifier matching the field_if used for the title semantic and indicate in
	 * the configuration of the project that it represents the title.
	 * 
	 * @param configuration
	 *            The configuration
	 * @param fieldSemantic
	 *            The semantic field
	 */
	private void createTuleapStringField(CONFIGURATION_TYPE configuration, JsonObject fieldSemantic) {
		if (fieldSemantic.get(TITLE) != null) {
			JsonObject semanticTitle = fieldSemantic.get(TITLE).getAsJsonObject();
			for (AbstractTuleapField tuleapSemanticField : configuration.getFields()) {
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
	private void createTuleapWorkflowSelectBoxField(JsonObject jsonObject, TuleapSelectBox selectBoxField) {
		JsonElement workflowJsonElement = jsonObject.get(ITuleapConfigurationConstants.WORKFLOW);
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
}