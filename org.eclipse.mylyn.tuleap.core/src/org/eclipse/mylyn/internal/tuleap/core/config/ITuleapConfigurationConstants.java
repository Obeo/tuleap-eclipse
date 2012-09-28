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
package org.eclipse.mylyn.internal.tuleap.core.config;

/**
 * The list of constants used during the parsing.
 * 
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public interface ITuleapConfigurationConstants {
	/**************************************************************************
	 * Root
	 **************************************************************************/
	/**
	 * The tracker.
	 */
	String TRACKER = "tracker"; //$NON-NLS-1$

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
	 * Dynamic elements.
	 **************************************************************************/
	/**
	 * Artifact ID.
	 */
	String AID = "aid"; //$NON-NLS-1$

	/**
	 * Last update date.
	 */
	String LUD = "lud"; //$NON-NLS-1$

	/**
	 * Artifact author.
	 */
	String SUBBY = "subby"; //$NON-NLS-1$

	/**
	 * Submission date.
	 */
	String SUBON = "subon"; //$NON-NLS-1$

	/**
	 * Cross references.
	 */
	String CROSS = "cross"; //$NON-NLS-1$

	/**
	 * Completion diagram.
	 */
	String BURNDOWN = "burndown"; //$NON-NLS-1$

	/**
	 * Computed field.
	 */
	String COMPUTED = "computed"; //$NON-NLS-1$

	/**************************************************************************
	 * Fields.
	 **************************************************************************/
	/**
	 * String.
	 */
	String STRING = "string"; //$NON-NLS-1$

	/**
	 * Text.
	 */
	String TEXT = "text"; //$NON-NLS-1$

	/**
	 * Select box.
	 */
	String SB = "sb"; //$NON-NLS-1$

	/**
	 * Multi select box.
	 */
	String MSB = "msb"; //$NON-NLS-1$

	/**
	 * Date.
	 */
	String DATE = "date"; //$NON-NLS-1$

	/**
	 * File upload.
	 */
	String FILE = "file"; //$NON-NLS-1$

	/**
	 * Integer.
	 */
	String INT = "int"; //$NON-NLS-1$

	/**
	 * Float.
	 */
	String FLOAT = "float"; //$NON-NLS-1$

	/**
	 * Open list.
	 */
	String TBL = "tbl"; //$NON-NLS-1$

	/**
	 * Artifact link.
	 */
	String ARTLINK = "art_link"; //$NON-NLS-1$

	/**
	 * Artifact permission.
	 */
	String PERM = "perm"; //$NON-NLS-1$

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
}