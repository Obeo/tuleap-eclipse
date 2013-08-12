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

import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.TuleapTrackerConfiguration;
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
	 * The field workflow key word.
	 */
	private final String jsonWorkflow = "workflow"; //$NON-NLS-1$

	/**
	 * The field open status key word.
	 */
	private final String jsonOpenStatusIds = "open_status_field_values_id"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
	 *      com.google.gson.JsonDeserializationContext)
	 */
	public TuleapTrackerConfiguration deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
			throws JsonParseException {

		JsonObject jsonObject = arg0.getAsJsonObject();
		String trackerName = jsonObject.get("name").getAsString(); //$NON-NLS-1$
		String trackerDescription = jsonObject.get("description").getAsString(); //$NON-NLS-1$
		JsonArray trackerFieldsArray = jsonObject.get("fields").getAsJsonArray(); //$NON-NLS-1$
		TuleapTrackerConfiguration tuleapTrackerConfiguration = new TuleapTrackerConfiguration(jsonObject
				.get("id").getAsInt(), //$NON-NLS-1$
				jsonObject.get("url").getAsString()); //$NON-NLS-1$
		tuleapTrackerConfiguration.setName(trackerName);
		tuleapTrackerConfiguration.setDescription(trackerDescription);
		JsonObject fieldSemantic = jsonObject.get("semantic").getAsJsonObject(); //$NON-NLS-1$
		for (int i = 0; i < trackerFieldsArray.size(); i++) {
			JsonObject field = (JsonObject)trackerFieldsArray.get(i);

			// the field id
			int fieldId = field.get(jsonFieldId).getAsInt();

			// the field type
			String fieldType = field.get("type").getAsString(); //$NON-NLS-1$
			AbstractTuleapField tuleapField = null;
			if ("string".equals(fieldType)) { //$NON-NLS-1$
				tuleapField = new TuleapString(fieldId);
			} else if ("text".equals(fieldType)) { //$NON-NLS-1$
				tuleapField = new TuleapText(fieldId);
			} else if ("computed".equals(fieldType)) { //$NON-NLS-1$
				tuleapField = new TuleapComputedValue(fieldId);
			} else if ("sb".equals(fieldType)) { //$NON-NLS-1$
				tuleapField = new TuleapSelectBox(fieldId);
			} else if ("msb".equals(fieldType)) { //$NON-NLS-1$
				tuleapField = new TuleapMultiSelectBox(fieldId);
			} else if ("cb".equals(fieldType)) { //$NON-NLS-1$
				tuleapField = new TuleapMultiSelectBox(fieldId);
			} else if ("date".equals(fieldType)) { //$NON-NLS-1$
				tuleapField = new TuleapDate(fieldId);
			} else if ("int".equals(fieldType)) { //$NON-NLS-1$
				tuleapField = new TuleapInteger(fieldId);
			} else if ("float".equals(fieldType)) { //$NON-NLS-1$
				tuleapField = new TuleapFloat(fieldId);
			} else if ("tbl".equals(fieldType)) { //$NON-NLS-1$
				tuleapField = new TuleapOpenList(fieldId);
			} else if ("art_link".equals(fieldType)) { //$NON-NLS-1$
				tuleapField = new TuleapArtifactLink(fieldId);
			} else if ("file".equals(fieldType)) { //$NON-NLS-1$
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

				for (int x = 0; x < permissions.size(); x++) {
					if ("create".equals(permissions.get(x).getAsString())) { //$NON-NLS-1$
						permissionsArray[x] = ITuleapConstants.PERMISSION_SUBMIT;
					} else {
						permissionsArray[x] = permissions.get(x).getAsString();
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
		createTuleapStringField(tuleapTrackerConfiguration, fieldSemantic);
		return tuleapTrackerConfiguration;
	}

	/**
	 * The case of creating a Multi Select Box field.
	 * 
	 * @param tuleapField
	 *            the field
	 * @param fieldValuesArray
	 *            the values
	 * @param fieldSemantic
	 *            the semantic
	 * @param fieldBinding
	 *            the binding
	 * @return the resulted field
	 */
	public TuleapMultiSelectBox createTuleapMultiSelectBoxField(AbstractTuleapField tuleapField,
			JsonArray fieldValuesArray, JsonObject fieldSemantic, JsonObject fieldBinding) {
		TuleapMultiSelectBox multiSelectBoxField = (TuleapMultiSelectBox)tuleapField;
		if (fieldValuesArray != null) {
			for (int y = 0; y < fieldValuesArray.size(); y++) {
				JsonObject value = (JsonObject)fieldValuesArray.get(y);
				int fieldValueId = value.get("field_value_id").getAsInt(); //$NON-NLS-1$
				String fieldValueLabel = value.get("field_value_label").getAsString(); //$NON-NLS-1$
				TuleapSelectBoxItem selectBoxItem = new TuleapSelectBoxItem(fieldValueId);
				selectBoxItem.setLabel(fieldValueLabel);
				multiSelectBoxField.addItem(selectBoxItem);
				// the semantic status part
				JsonObject semanticStatus = fieldSemantic.get("status").getAsJsonObject(); //$NON-NLS-1$
				if (multiSelectBoxField.getIdentifier() == semanticStatus.get(jsonFieldId).getAsInt()) {
					JsonArray openStatus = semanticStatus.get(jsonOpenStatusIds).getAsJsonArray();
					for (int z = 0; z < openStatus.size(); z++) {
						if (fieldValueId == openStatus.get(z).getAsInt()) {
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
	 * The case of creating a Select Box field.
	 * 
	 * @param tuleapField
	 *            the field
	 * @param fieldValuesArray
	 *            the values array
	 * @param fieldSemantic
	 *            the semantic
	 * @param fieldBinding
	 *            the binding
	 * @param jsonObject
	 *            the root json object
	 * @return the resulted field
	 */
	public TuleapSelectBox createTuleapSelectBoxField(AbstractTuleapField tuleapField,
			JsonArray fieldValuesArray, JsonObject fieldSemantic, JsonObject fieldBinding,
			JsonObject jsonObject) {
		TuleapSelectBox selectBoxField = (TuleapSelectBox)tuleapField;
		if (fieldValuesArray != null) {
			for (int y = 0; y < fieldValuesArray.size(); y++) {
				JsonObject value = (JsonObject)fieldValuesArray.get(y);
				int fieldValueId = value.get("field_value_id").getAsInt(); //$NON-NLS-1$
				String fieldValueLabel = value.get("field_value_label").getAsString(); //$NON-NLS-1$
				TuleapSelectBoxItem selectBoxItem = new TuleapSelectBoxItem(fieldValueId);
				selectBoxItem.setLabel(fieldValueLabel);
				selectBoxField.addItem(selectBoxItem);

				// the semantic status part
				JsonObject semanticStatus = fieldSemantic.get("status").getAsJsonObject(); //$NON-NLS-1$
				for (int z = 0; z < semanticStatus.get(jsonOpenStatusIds).getAsJsonArray().size(); z++) {
					if (selectBoxField.getIdentifier() == semanticStatus.get(jsonFieldId).getAsInt()
							&& fieldValueId == semanticStatus.get(jsonOpenStatusIds).getAsJsonArray().get(
									z).getAsInt()) {
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
		createTuleapWorkflowSelectBoxField(jsonObject, selectBoxField);
		return selectBoxField;
	}

	/**
	 * The case of creating a String field.
	 * 
	 * @param tuleapTrackerConfiguration
	 *            the tracker configuration
	 * @param fieldSemantic
	 *            the semantic field
	 */
	public void createTuleapStringField(TuleapTrackerConfiguration tuleapTrackerConfiguration,
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
	 * The case of creating the workflow of a select box field.
	 * 
	 * @param jsonObject
	 *            the JSON root object
	 * @param selectBoxField
	 *            the select box field
	 */
	public void createTuleapWorkflowSelectBoxField(JsonObject jsonObject, TuleapSelectBox selectBoxField) {
		if (jsonObject.get(jsonWorkflow) != null
				&& jsonObject.get(jsonWorkflow).getAsJsonObject().get(jsonFieldId).getAsInt() == selectBoxField
						.getIdentifier()) {
			// the workflow transitions
			for (int j = 0; j < jsonObject.get(jsonWorkflow).getAsJsonObject().get("transitions") //$NON-NLS-1$
					.getAsJsonArray().size(); j++) {
				JsonObject transition = (JsonObject)jsonObject.get(jsonWorkflow).getAsJsonObject().get(
						"transitions").getAsJsonArray().get(j); //$NON-NLS-1$
				int from = transition.get("from_field_value_id").getAsInt(); //$NON-NLS-1$
				int to = transition.get("to_field_value_id").getAsInt(); //$NON-NLS-1$
				TuleapWorkflowTransition workflowTransition = new TuleapWorkflowTransition();
				workflowTransition.setFrom(from);
				workflowTransition.setTo(to);
				selectBoxField.getWorkflow().addTransition(workflowTransition);
			}
		}
	}

}
