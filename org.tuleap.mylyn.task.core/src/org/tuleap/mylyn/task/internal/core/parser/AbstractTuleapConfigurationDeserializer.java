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

import org.eclipse.core.runtime.Assert;
import org.tuleap.mylyn.task.internal.core.config.ITuleapConfigurationConstants;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfiguration;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapField;
import org.tuleap.mylyn.task.internal.core.model.TuleapGroup;
import org.tuleap.mylyn.task.internal.core.model.TuleapPerson;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.field.AbstractTuleapSelectBox;
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

/**
 * Common superclass for all the configuration deserializer.
 * 
 * @param <CONFIGURATION_TYPE>
 *            The type of the configuration.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public abstract class AbstractTuleapConfigurationDeserializer<CONFIGURATION_TYPE extends AbstractTuleapConfiguration> implements JsonDeserializer<CONFIGURATION_TYPE> {

	/**
	 * The identifier keyword.
	 */
	private static final String ID = "id"; //$NON-NLS-1$

	/**
	 * The url keyword.
	 */
	private static final String URL = "url"; //$NON-NLS-1$

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
	private static final String BINDING = "binding"; //$NON-NLS-1$

	/**
	 * The field id keyword.
	 */
	private static final String FIELD_ID = "field_id"; //$NON-NLS-1$

	/**
	 * The field value id keyword.
	 */
	private static final String FIELD_VALUE_ID = "field_value_id"; //$NON-NLS-1$

	/**
	 * The field value label keyword.
	 */
	private static final String FIELD_VALUE_LABEL = "field_value_label"; //$NON-NLS-1$

	/**
	 * The bind type keyword.
	 */
	private static final String BIND_TYPE = "bind_type"; //$NON-NLS-1$

	/**
	 * The keyword that represents a "users" binding type.
	 */
	private static final String BIND_TYPE_USERS = "users"; //$NON-NLS-1$

	/**
	 * The bind list keyword.
	 */
	private static final String BIND_LIST = "bind_list"; //$NON-NLS-1$

	/**
	 * The user group id keyword.
	 */
	private static final String USER_GROUP_ID = "user_group_id"; //$NON-NLS-1$

	/**
	 * The user group name keyword.
	 */
	private static final String USER_GROUP_NAME = "user_group_name"; //$NON-NLS-1$

	/**
	 * The title keyword.
	 */
	private static final String TITLE = "title"; //$NON-NLS-1$

	/**
	 * The from field value id keyword.
	 */
	private static final String FROM_FIELD_VALUE_ID = "from_field_value_id"; //$NON-NLS-1$

	/**
	 * The to field value id keyword.
	 */
	private static final String TO_FIELD_VALUE_ID = "to_field_value_id"; //$NON-NLS-1$

	/**
	 * The transitions keyword.
	 */
	private static final String TRANSITIONS = "transitions"; //$NON-NLS-1$

	/**
	 * The contributors keyword.
	 */
	private static final String JSON_CONTRIBUTORS = "contributors"; //$NON-NLS-1$

	/**
	 * The field open status keyword.
	 */
	private static final String JSON_OPEN_STATUS_IDS = "open_status_field_values_ids"; //$NON-NLS-1$

	/**
	 * The status keyword.
	 */
	private static final String STATUS = "status"; //$NON-NLS-1$

	/**
	 * Indicates that the user can submit a newly created artifact with the field set.
	 */
	private static final String PERMISSION_SUBMIT = "submit"; //$NON-NLS-1$

	/**
	 * The related project configuration.
	 */
	protected final TuleapProjectConfiguration projectConfiguration;

	/**
	 * Constructor that receives the related project Configuration.
	 * 
	 * @param projectConfiguration
	 *            The project configuration;
	 */
	protected AbstractTuleapConfigurationDeserializer(TuleapProjectConfiguration projectConfiguration) {
		Assert.isNotNull(projectConfiguration);
		this.projectConfiguration = projectConfiguration;
	}

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

		JsonElement eltSemantic = jsonObject.get(ITuleapConfigurationConstants.SEMANTIC);
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
				JsonElement bindingElement = field.get(BINDING);
				if (bindingElement != null) {
					fieldBinding = bindingElement.getAsJsonObject();
				}

				// The semantic part configuration
				if (tuleapField instanceof TuleapMultiSelectBox) {
					fillTuleapMultiSelectBoxField(configuration, (TuleapMultiSelectBox)tuleapField,
							fieldValuesArray, fieldSemantic, fieldBinding);
					// The Select Box case
				} else if (tuleapField instanceof TuleapSelectBox) {
					fillTuleapSelectBoxField(configuration, (TuleapSelectBox)tuleapField, fieldValuesArray,
							fieldSemantic, fieldBinding, jsonObject);
				}
				configuration.addField(tuleapField);
			}
		}

		// the semantic title part
		if (fieldSemantic != null) {
			this.fillTitleSemantic(configuration, fieldSemantic);
		}

		return configuration;
	}

	/**
	 * Returns the TuleapMultiSelectBox created from the parsing of the JSON elements.
	 * 
	 * @param configuration
	 *            The configuration
	 * @param multiSelectBoxField
	 *            The field
	 * @param fieldValuesArray
	 *            The values
	 * @param fieldSemantic
	 *            The semantic
	 * @param fieldBinding
	 *            The binding
	 */
	private void fillTuleapMultiSelectBoxField(CONFIGURATION_TYPE configuration,
			TuleapMultiSelectBox multiSelectBoxField, JsonArray fieldValuesArray, JsonObject fieldSemantic,
			JsonObject fieldBinding) {
		fillSelectBoxItem(configuration, multiSelectBoxField, fieldValuesArray, fieldSemantic, fieldBinding);
	}

	/**
	 * Returns the TuleapSelectBox created from the parsing of the JSON elements.
	 * 
	 * @param configuration
	 *            The configuration
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
	private void fillTuleapSelectBoxField(CONFIGURATION_TYPE configuration, TuleapSelectBox selectBoxField,
			JsonArray fieldValuesArray, JsonObject fieldSemantic, JsonObject fieldBinding, JsonObject root) {
		fillSelectBoxItem(configuration, selectBoxField, fieldValuesArray, fieldSemantic, fieldBinding);
		// the workflow
		this.fillWorkflow(root, selectBoxField);
	}

	/**
	 * Returns the TuleapSelectBox created from the parsing of the JSON elements.
	 * 
	 * @param configuration
	 *            The configuration
	 * @param selectBoxField
	 *            The field
	 * @param fieldValuesArray
	 *            The values array
	 * @param fieldSemantic
	 *            The semantic
	 * @param fieldBinding
	 *            The binding
	 */
	private void fillSelectBoxItem(CONFIGURATION_TYPE configuration, AbstractTuleapSelectBox selectBoxField,
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
			String bindingType = fieldBinding.get(BIND_TYPE).getAsString();
			selectBoxField.setBinding(bindingType);
			if (BIND_TYPE_USERS.equals(bindingType)) {
				fillUsers(selectBoxField, fieldBinding);
			}
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
	 * Fills the users that belong to the cound user groups.
	 * 
	 * @param selectBoxField
	 *            The select box in which to put the users.
	 * @param fieldBinding
	 *            The JSON object that contains the group bindings configuration.
	 */
	private void fillUsers(AbstractTuleapSelectBox selectBoxField, JsonObject fieldBinding) {
		JsonArray bindings = fieldBinding.get(BIND_LIST).getAsJsonArray();
		for (JsonElement bindingElt : bindings) {
			JsonObject binding = bindingElt.getAsJsonObject();
			int ugroupId = binding.get(USER_GROUP_ID).getAsInt();
			TuleapGroup group = projectConfiguration.getGroup(ugroupId);
			if (group != null) {
				for (TuleapPerson person : group.getMembers()) {
					TuleapSelectBoxItem item = new TuleapSelectBoxItem(person.getId());
					item.setLabel(person.getUserName());
					selectBoxField.addItem(item);
				}
			}
		}
	}

	/**
	 * Finds the fields with the identifier matching the field_id used for the title semantic and indicate in
	 * the configuration of the project that it represents the title.
	 * 
	 * @param configuration
	 *            The configuration
	 * @param fieldSemantic
	 *            The semantic field
	 */
	private void fillTitleSemantic(CONFIGURATION_TYPE configuration, JsonObject fieldSemantic) {
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
	private void fillWorkflow(JsonObject jsonObject, TuleapSelectBox selectBoxField) {
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
