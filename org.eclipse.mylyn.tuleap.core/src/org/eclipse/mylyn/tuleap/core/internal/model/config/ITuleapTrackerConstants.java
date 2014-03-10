/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylyn.tuleap.core.internal.model.config;

/**
 * The list of constants used during the parsing.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public interface ITuleapTrackerConstants {
	/**************************************************************************
	 * Root
	 **************************************************************************/
	/**
	 * The tracker.
	 */
	String TRACKER = "tracker"; //$NON-NLS-1$

	/**
	 * The item name.
	 */
	String ITEM_NAME = "item_name"; //$NON-NLS-1$

	/**************************************************************************
	 * Structural elements.
	 **************************************************************************/
	/**
	 * Field set.
	 */
	String FIELDSET = "fieldset"; //$NON-NLS-1$

	/**
	 * Column.
	 */
	String COLUMN = "column"; //$NON-NLS-1$

	/**
	 * Line break.
	 */
	String LINEBREAK = "linebreak"; //$NON-NLS-1$

	/**
	 * Separator.
	 */
	String SEPARATOR = "separator"; //$NON-NLS-1$

	/**
	 * Static text.
	 */
	String STATIC_TEXT = "staticrichtext"; //$NON-NLS-1$

	/**************************************************************************
	 * Structural sub elements.
	 **************************************************************************/
	/**
	 * Form element container.
	 */
	String FORM_ELEMENTS = "formElements"; //$NON-NLS-1$

	/**
	 * Structural element definition.
	 */
	String FORM_ELEMENT = "formElement"; //$NON-NLS-1$

	/**
	 * Properties.
	 */
	String PROPERTIES = "properties"; //$NON-NLS-1$

	/**
	 * Bind.
	 */
	String BIND = "bind"; //$NON-NLS-1$

	/**
	 * Item.
	 */
	String ITEM = "item"; //$NON-NLS-1$

	/**
	 * Name.
	 */
	String NAME = "name"; //$NON-NLS-1$

	/**
	 * Label.
	 */
	String LABEL = "label"; //$NON-NLS-1$

	/**
	 * Description.
	 */
	String DESCRIPTION = "description"; //$NON-NLS-1$

	/**************************************************************************
	 * Field types.
	 **************************************************************************/

	/**
	 * Computed field.
	 */
	String TYPE_COMPUTED = "computed"; //$NON-NLS-1$

	/**
	 * String.
	 */
	String TYPE_STRING = "string"; //$NON-NLS-1$

	/**
	 * Text.
	 */
	String TYPE_TEXT = "text"; //$NON-NLS-1$

	/**
	 * Select box.
	 */
	String TYPE_SB = "sb"; //$NON-NLS-1$

	/**
	 * Multi select box.
	 */
	String TYPE_MSB = "msb"; //$NON-NLS-1$

	/**
	 * Check box.
	 */
	String TYPE_CB = "cb"; //$NON-NLS-1$

	/**
	 * Date.
	 */
	String TYPE_DATE = "date"; //$NON-NLS-1$

	/**
	 * File upload.
	 */
	String TYPE_FILE = "file"; //$NON-NLS-1$

	/**
	 * Integer.
	 */
	String TYPE_INT = "int"; //$NON-NLS-1$

	/**
	 * Float.
	 */
	String TYPE_FLOAT = "float"; //$NON-NLS-1$

	/**
	 * Open list.
	 */
	String TYPE_TBL = "tbl"; //$NON-NLS-1$

	/**
	 * Artifact link.
	 */
	String TYPE_ARTIFACT_LINK = "art_link"; //$NON-NLS-1$

	/**
	 * Artifact permission.
	 */
	String TYPE_PERM = "perm"; //$NON-NLS-1$

	/**
	 * Type of field: Artifact ID.
	 */
	String TYPE_AID = "aid"; //$NON-NLS-1$

	/**
	 * Type of field: Submitted by.
	 */
	String TYPE_SUBMITTED_BY = "subby"; //$NON-NLS-1$

	/**
	 * Type of field: Submitted on.
	 */
	String TYPE_SUBMITTED_ON = "subon"; //$NON-NLS-1$

	/**
	 * Type of field: Burndown chart.
	 */
	String TYPE_BURNDOWN = "burndown"; //$NON-NLS-1$

	/**
	 * Type of field: Last updated on.
	 */
	String TYPE_LAST_UPDATED_ON = "lud"; //$NON-NLS-1$

	/**
	 * Type of field: Cross-references.
	 */
	String TYPE_CROSS_REFERENCES = "cross"; //$NON-NLS-1$

	/**
	 * TODO Shared field.
	 */

	/**************************************************************************
	 * Attributes.
	 **************************************************************************/
	/**
	 * Type.
	 */
	String TYPE = "type"; //$NON-NLS-1$

	/**
	 * Required.
	 */
	String REQUIRED = "required"; //$NON-NLS-1$

	/**
	 * Rank.
	 */
	String RANK = "rank"; //$NON-NLS-1$

	/**
	 * Identifier.
	 */
	String ID = "ID"; //$NON-NLS-1$

	/**
	 * Size.
	 */
	String SIZE = "size"; //$NON-NLS-1$

	/**
	 * Rows.
	 */
	String ROWS = "rows"; //$NON-NLS-1$

	/**
	 * Columns.
	 */
	String COLUMNS = "cols"; //$NON-NLS-1$

	/**
	 * Default value.
	 */
	String DEFAULT_VALUE = "default_value"; //$NON-NLS-1$

	/**************************************************************************
	 * Semantic.
	 **************************************************************************/
	/**
	 * Semantics.
	 */
	String SEMANTICS = "semantics"; //$NON-NLS-1$

	/**
	 * Semantic.
	 */
	String SEMANTIC = "semantic"; //$NON-NLS-1$

	/**
	 * Short name.
	 */
	String SHORT_NAME = "shortname"; //$NON-NLS-1$

	/**
	 * Field.
	 */
	String FIELD = "field"; //$NON-NLS-1$

	/**
	 * REF.
	 */
	String REF = "REF"; //$NON-NLS-1$

	/**
	 * The open values.
	 */
	String OPEN_VALUES = "open_values"; //$NON-NLS-1$

	/**
	 * The open value.
	 */
	String OPEN_VALUE = "open_value"; //$NON-NLS-1$

	/**
	 * The open value separator.
	 */
	String OPEN_VALUE_SEPARATOR = "-"; //$NON-NLS-1$

	/**
	 * The kind of semantic: "title".
	 */
	String SEMANTIC_KIND_TITLE = "title"; //$NON-NLS-1$

	/**
	 * The kind of semantic: "contributor".
	 */
	String SEMANTIC_KIND_CONTRIBUTOR = "contributor"; //$NON-NLS-1$

	/**
	 * The kind of semantic: "status".
	 */
	String SEMANTIC_KIND_STATUS = "status"; //$NON-NLS-1$

	/**
	 * The kind of semantic: "tooltip".
	 */
	String SEMANTIC_KIND_TOOLTIP = "tooltip"; //$NON-NLS-1$

	/**************************************************************************
	 * Workflow.
	 **************************************************************************/
	/**
	 * Workflow.
	 */
	String WORKFLOW = "workflow"; //$NON-NLS-1$

	/**
	 * Field id.
	 */
	String FIELD_ID = "field_id"; //$NON-NLS-1$

	/**
	 * Is Used.
	 */
	String IS_USED = "is_used"; //$NON-NLS-1$

	/**
	 * Transitions.
	 */
	String TRANSITIONS = "transitions"; //$NON-NLS-1$

	/**
	 * Transition.
	 */
	String TRANSITION = "transition"; //$NON-NLS-1$

	/**
	 * From id.
	 */
	String FROM_ID = "from_id"; //$NON-NLS-1$

	/**
	 * To id.
	 */
	String TO_ID = "to_id"; //$NON-NLS-1$
}
