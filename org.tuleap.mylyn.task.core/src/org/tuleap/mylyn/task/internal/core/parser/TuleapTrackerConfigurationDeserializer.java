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

import org.tuleap.mylyn.task.internal.core.config.ITuleapConfigurationConstants;
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
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.workflow.TuleapWorkflowTransition;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

/**
 * This class is used to deserialize the JSON representation of a tracker configuration.
 * 
 * @author <a href="mailto:firas.bacha@obeo.fr">Firas Bacha</a>
 */
public class TuleapTrackerConfigurationDeserializer implements JsonDeserializer<TuleapTrackerConfiguration> {

	/**
	 * The field Id key word.
	 */
	private final String jsonFieldId = "field_id"; //$NON-NLS-1$

	/**
	 * The contributors key word.
	 */
	private final String jsonContributors = "contributors"; //$NON-NLS-1$

	/**
	 * The field open status key word.
	 */
	private final String jsonOpenStatusIds = "open_status_field_values_ids"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	public TuleapTrackerConfiguration deserialize(JsonElement rootJsonElement, Type type,
			JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		JsonObject jsonObject = rootJsonElement.getAsJsonObject();
		String trackerName = jsonObject.get("name").getAsString(); //$NON-NLS-1$
		String trackerDescription = jsonObject.get("description").getAsString(); //$NON-NLS-1$
		JsonArray trackerFieldsArray = jsonObject.get("fields").getAsJsonArray(); //$NON-NLS-1$

		TuleapTrackerConfiguration tuleapTrackerConfiguration = new TuleapTrackerConfiguration(jsonObject
				.get("id").getAsInt(), //$NON-NLS-1$
				jsonObject.get("url").getAsString()); //$NON-NLS-1$

		tuleapTrackerConfiguration.setName(trackerName);
		tuleapTrackerConfiguration.setDescription(trackerDescription);

		JsonObject fieldSemantic = jsonObject.get(ITuleapConfigurationConstants.SEMANTIC).getAsJsonObject();
		for (int i = 0; i < trackerFieldsArray.size(); i++) {
			JsonObject field = (JsonObject)trackerFieldsArray.get(i);

			// the field id
			int fieldId = field.get(jsonFieldId).getAsInt();

			// the field type
			String fieldType = field.get("type").getAsString(); //$NON-NLS-1$
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
				tuleapField.setLabel(field.get("label").getAsString()); //$NON-NLS-1$

				// the field name
				tuleapField.setName(field.get("short_name").getAsString()); //$NON-NLS-1$

				// the field permissions
				JsonArray permissions = field.get("permissions").getAsJsonArray(); //$NON-NLS-1$
				String[] permissionsArray = new String[permissions.size()];

				for (int j = 0; j < permissions.size(); j++) {
					if ("create".equals(permissions.get(j).getAsString())) { //$NON-NLS-1$
						permissionsArray[j] = ITuleapConstants.PERMISSION_SUBMIT;
					} else {
						permissionsArray[j] = permissions.get(j).getAsString();
					}
				}

				tuleapField.setPermissions(permissionsArray);

				// The Multi Select Box case
				JsonArray fieldValuesArray = null;
				JsonObject fieldBinding = null;
				JsonElement valuesElement = field.get("values"); //$NON-NLS-1$ 
				if (valuesElement != null) {
					fieldValuesArray = valuesElement.getAsJsonArray();
				}
				JsonElement bindingElement = field.get("binding"); //$NON-NLS-1$ 
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
				tuleapTrackerConfiguration.addField(tuleapField);
			}
		}

		// the semantic title part
		this.createTuleapStringField(tuleapTrackerConfiguration, fieldSemantic);
		return tuleapTrackerConfiguration;
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
				int fieldValueId = value.get("field_value_id").getAsInt(); //$NON-NLS-1$
				String fieldValueLabel = value.get("field_value_label").getAsString(); //$NON-NLS-1$
				TuleapSelectBoxItem selectBoxItem = new TuleapSelectBoxItem(fieldValueId);
				selectBoxItem.setLabel(fieldValueLabel);
				multiSelectBoxField.addItem(selectBoxItem);
				// the semantic status part
				JsonObject semanticStatus = fieldSemantic.get("status").getAsJsonObject(); //$NON-NLS-1$
				if (multiSelectBoxField.getIdentifier() == semanticStatus.get(jsonFieldId).getAsInt()) {
					JsonArray openStatus = semanticStatus.get(jsonOpenStatusIds).getAsJsonArray();
					for (int j = 0; j < openStatus.size(); j++) {
						if (fieldValueId == openStatus.get(j).getAsInt()) {
							multiSelectBoxField.getOpenStatus().add(selectBoxItem);
						}
					}
				}
			}
		}

		// the binding
		if (fieldBinding != null) {
			multiSelectBoxField.setBinding(fieldBinding.get("bind_type").getAsString()); //$NON-NLS-1$
		}

		// the semantic contributors part
		JsonObject semanticContributor = fieldSemantic.get(jsonContributors).getAsJsonObject();
		if (semanticContributor.get(jsonFieldId).getAsInt() == multiSelectBoxField.getIdentifier()) {
			multiSelectBoxField.setSemanticContributor(true);
		}
		return multiSelectBoxField;
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
				int fieldValueId = value.get("field_value_id").getAsInt(); //$NON-NLS-1$
				String fieldValueLabel = value.get("field_value_label").getAsString(); //$NON-NLS-1$
				TuleapSelectBoxItem selectBoxItem = new TuleapSelectBoxItem(fieldValueId);
				selectBoxItem.setLabel(fieldValueLabel);
				selectBoxField.addItem(selectBoxItem);

				// the semantic status part
				JsonObject semanticStatus = fieldSemantic.get("status").getAsJsonObject(); //$NON-NLS-1$
				for (int z = 0; z < semanticStatus.get(jsonOpenStatusIds).getAsJsonArray().size(); z++) {
					if (selectBoxField.getIdentifier() == semanticStatus.get(jsonFieldId).getAsInt()
							&& fieldValueId == semanticStatus.get(jsonOpenStatusIds).getAsJsonArray().get(z)
									.getAsInt()) {
						selectBoxField.getOpenStatus().add(selectBoxItem);
					}
				}
			}
		}

		// the binding
		if (fieldBinding != null) {
			selectBoxField.setBinding(fieldBinding.get("bind_type").getAsString()); //$NON-NLS-1$
		}

		// the semantic contributors part
		if (fieldSemantic.get(jsonContributors) != null
				&& fieldSemantic.get(jsonContributors).getAsJsonObject().get(jsonFieldId).getAsInt() == selectBoxField
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
	 * @param tuleapTrackerConfiguration
	 *            The tracker configuration
	 * @param fieldSemantic
	 *            The semantic field
	 */
	private void createTuleapStringField(TuleapTrackerConfiguration tuleapTrackerConfiguration,
			JsonObject fieldSemantic) {
		JsonObject semanticTitle = fieldSemantic.get("title").getAsJsonObject(); //$NON-NLS-1$
		for (AbstractTuleapField tuleapSemanticField : tuleapTrackerConfiguration.getFields()) {
			if (tuleapSemanticField.getIdentifier() == semanticTitle.get(jsonFieldId).getAsInt()
					&& tuleapSemanticField instanceof TuleapString) {
				TuleapString stringfield = (TuleapString)tuleapSemanticField;
				stringfield.setSemanticTitle(true);
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

			if (workflowJsonObject.get(jsonFieldId).getAsInt() == selectBoxField.getIdentifier()) {
				// the workflow transitions
				JsonArray transitionsJsonArray = workflowJsonObject.get("transitions").getAsJsonArray(); //$NON-NLS-1$
				for (JsonElement transitionJsonElement : transitionsJsonArray) {
					if (transitionJsonElement instanceof JsonObject) {
						JsonObject transitionJsonObject = (JsonObject)transitionJsonElement;
						int from = transitionJsonObject.get("from_field_value_id").getAsInt(); //$NON-NLS-1$
						int to = transitionJsonObject.get("to_field_value_id").getAsInt(); //$NON-NLS-1$

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
