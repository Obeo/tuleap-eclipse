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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.mylyn.internal.tuleap.core.TuleapCoreActivator;
import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField;
import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapFormElement;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapArtifactLink;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapDate;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapFileUpload;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapFloat;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapInteger;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapMultiSelectBox;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapOpenList;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapPermissionOnArtifact;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapSelectBox;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapString;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapText;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapArtifactId;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapBurndownChart;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapComputedValue;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapCrossReferences;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapLastUpdateDate;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapSubmittedBy;
import org.eclipse.mylyn.internal.tuleap.core.model.field.dynamic.TuleapSubmittedOn;
import org.eclipse.mylyn.internal.tuleap.core.model.structural.TuleapColumn;
import org.eclipse.mylyn.internal.tuleap.core.model.structural.TuleapFieldSet;
import org.eclipse.mylyn.internal.tuleap.core.model.structural.TuleapLineBreak;
import org.eclipse.mylyn.internal.tuleap.core.model.structural.TuleapSeparator;
import org.eclipse.mylyn.internal.tuleap.core.model.structural.TuleapStaticText;
import org.eclipse.mylyn.internal.tuleap.core.repository.TuleapRepositoryConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.util.ITuleapConstants;
import org.eclipse.mylyn.internal.tuleap.core.util.TuleapMylynTasksMessages;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Tuleap configuration parser. Populates a {@link TuleapRepositoryConfiguration} data structure.
 * 
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @since 1.0
 */
public class SaxConfigurationContentHandler extends DefaultHandler {

	/**************************************************************************
	 * Structural elements.
	 **************************************************************************/
	/**
	 * Field set.
	 */
	private static final String FIELDSET = "fieldset"; //$NON-NLS-1$

	/**
	 * Column.
	 */
	private static final String COLUMN = "column"; //$NON-NLS-1$

	/**
	 * Line break.
	 */
	private static final String LINEBREAK = "linebreak"; //$NON-NLS-1$

	/**
	 * Separator.
	 */
	private static final String SEPARATOR = "separator"; //$NON-NLS-1$

	/**
	 * Static text.
	 */
	private static final String STATIC_TEXT = "staticrichtext"; //$NON-NLS-1$

	/**************************************************************************
	 * Structural sub elements.
	 **************************************************************************/
	/**
	 * Form element container.
	 */
	private static final String FORM_ELEMENTS = "formElements"; //$NON-NLS-1$

	/**
	 * Structural element definition.
	 */
	private static final String FORM_ELEMENT = "formElement"; //$NON-NLS-1$

	/**
	 * Properties.
	 */
	private static final String PROPERTIES = "properties"; //$NON-NLS-1$

	/**
	 * Bind.
	 */
	private static final String BIND = "bind"; //$NON-NLS-1$

	/**
	 * Item.
	 */
	private static final String ITEM = "item"; //$NON-NLS-1$

	/**
	 * Name.
	 */
	private static final String NAME = "name"; //$NON-NLS-1$

	/**
	 * Label.
	 */
	private static final String LABEL = "label"; //$NON-NLS-1$

	/**
	 * Description.
	 */
	private static final String DESCRIPTION = "description"; //$NON-NLS-1$

	/**************************************************************************
	 * Dynamic elements.
	 **************************************************************************/
	/**
	 * Artifact ID.
	 */
	private static final String AID = "aid"; //$NON-NLS-1$

	/**
	 * Last update date.
	 */
	private static final String LUD = "lud"; //$NON-NLS-1$

	/**
	 * Artifact author.
	 */
	private static final String SUBBY = "subby"; //$NON-NLS-1$

	/**
	 * Submission date.
	 */
	private static final String SUBON = "subon"; //$NON-NLS-1$

	/**
	 * Cross references.
	 */
	private static final String CROSS = "cross"; //$NON-NLS-1$

	/**
	 * Completion diagram.
	 */
	private static final String BURNDOWN = "burndown"; //$NON-NLS-1$

	/**
	 * Computed field.
	 */
	private static final String COMPUTED = "computed"; //$NON-NLS-1$

	/**************************************************************************
	 * Fields.
	 **************************************************************************/
	/**
	 * String.
	 */
	private static final String STRING = "string"; //$NON-NLS-1$

	/**
	 * Text.
	 */
	private static final String TEXT = "text"; //$NON-NLS-1$

	/**
	 * Select box.
	 */
	private static final String SB = "sb"; //$NON-NLS-1$

	/**
	 * Multi select box.
	 */
	private static final String MSB = "msb"; //$NON-NLS-1$

	/**
	 * Date.
	 */
	private static final String DATE = "date"; //$NON-NLS-1$

	/**
	 * File upload.
	 */
	private static final String FILE = "file"; //$NON-NLS-1$

	/**
	 * Integer.
	 */
	private static final String INT = "int"; //$NON-NLS-1$

	/**
	 * Float.
	 */
	private static final String FLOAT = "float"; //$NON-NLS-1$

	/**
	 * Open list.
	 */
	private static final String TBL = "tbl"; //$NON-NLS-1$

	/**
	 * Artifact link.
	 */
	private static final String ARTLINK = "art_link"; //$NON-NLS-1$

	/**
	 * Artifact permission.
	 */
	private static final String PERM = "perm"; //$NON-NLS-1$

	/**
	 * TODO Shared field.
	 */

	/**************************************************************************
	 * Attributes.
	 **************************************************************************/
	/**
	 * Type.
	 */
	private static final String TYPE = "type"; //$NON-NLS-1$

	/**
	 * Required.
	 */
	private static final String REQUIRED = "required"; //$NON-NLS-1$

	/**
	 * Rank.
	 */
	private static final String RANK = "rank"; //$NON-NLS-1$

	/**
	 * Identifier.
	 */
	private static final String ID = "ID"; //$NON-NLS-1$

	/**
	 * Size.
	 */
	private static final String SIZE = "size"; //$NON-NLS-1$

	/**
	 * Rows.
	 */
	private static final String ROWS = "rows"; //$NON-NLS-1$

	/**
	 * Columns.
	 */
	private static final String COLUMNS = "cols"; //$NON-NLS-1$

	/**
	 * Default value.
	 */
	private static final String DEFAULT_VALUE = "default_value"; //$NON-NLS-1$

	/**
	 * Tuleap repository configuration.
	 */
	private TuleapRepositoryConfiguration configuration;

	/**
	 * The characters of the XML document.
	 */
	private StringBuffer characters = new StringBuffer();

	/**
	 * Identifier of the currently parsed element.
	 */
	private String currentIdentifier;

	/**
	 * Type of the currently parsed element.
	 */
	private String currentType;

	/**
	 * Boolean representing if the currently parsed element is required.
	 */
	private boolean currentRequired;

	/**
	 * Rank of the currently parsed element.
	 */
	private int currentRank;

	/**
	 * Size of the currently parsed element.
	 */
	private int currentSize;

	/**
	 * Number of rows of the currently parsed element.
	 */
	private int currentNumberOfRows;

	/**
	 * Number of columns of the currently parsed element.
	 */
	private int currentNumberOfColumns;

	/**
	 * Default value of the currently parsed element.
	 */
	private String currentDefaultValue;

	/**
	 * Binding of the currently parsed element.
	 */
	private String currentBinding;

	/**
	 * Tuleap object created from the currently parsed element.
	 */
	private Object currentElement;

	/**
	 * Parent hierarchy of the currently parsed element.
	 */
	private List<Object> parentHierarchy = new ArrayList<Object>();

	/**
	 * Tuleap configuration handler.
	 * 
	 * @param repositoryURL
	 *            Repository URL
	 */
	public SaxConfigurationContentHandler(String repositoryURL) {
		configuration = new TuleapRepositoryConfiguration(repositoryURL);
		currentElement = configuration;
	}

	/**
	 * Get the Tuleap repository configuration.
	 * 
	 * @return Tuleap repository configuration
	 */
	public TuleapRepositoryConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String,
	 *      java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {
		characters = new StringBuffer();
		// Form elements
		if (localName.equals(FORM_ELEMENT)) {
			parseFormElementAttributes(attributes);
			Object formElement = createFormElement();
			currentElement = formElement;
			attachFormElement(formElement);
		} else if (localName.equals(FORM_ELEMENTS)) {
			parentHierarchy.add(currentElement);
		} else if (localName.equals(PROPERTIES)) {
			parsePropertiesAttributes(attributes);
		} else if (localName.equals(BIND)) {
			parentHierarchy.add(currentElement);
			parseBindAttributes(attributes);
		} else if (localName.equals(ITEM)) {
			if (ITuleapConstants.TULEAP_DYNAMIC_BINDING_USERS.equals(currentBinding)) {
				// TODO Implement dynamic binding
			} else {
				String item = attributes.getValue(LABEL);
				attachItem(item);
			}
		}
	}

	/**
	 * Attach the form element to the good parent in hierarchy.
	 * 
	 * @param formElement
	 *            Form element, it could be a {@link TuleapColumn} or a {@link TuleapFieldSet}
	 */
	private void attachFormElement(Object formElement) {
		assert formElement instanceof TuleapColumn || formElement instanceof TuleapFieldSet;
		if (!parentHierarchy.isEmpty()) {
			Object currentParent = parentHierarchy.get(parentHierarchy.size() - 1);
			if (currentParent instanceof TuleapColumn) {
				((TuleapColumn)currentParent).getFormElements().add((AbstractTuleapField)formElement);
			} else if (currentParent instanceof TuleapFieldSet) {
				((TuleapFieldSet)currentParent).getFormElements().add((AbstractTuleapFormElement)formElement);
			} else {
				configuration.getFormElements().add((AbstractTuleapFormElement)formElement);
			}
		} else {
			configuration.getFormElements().add((AbstractTuleapFormElement)formElement);
		}
	}

	/**
	 * Attach the item to the good parent in hierarchy.
	 * 
	 * @param item
	 *            Item
	 */
	private void attachItem(String item) {
		Object currentParent = parentHierarchy.get(parentHierarchy.size() - 1);
		if (currentParent instanceof TuleapSelectBox) {
			((TuleapSelectBox)currentParent).getItems().add(item);
		} else if (currentParent instanceof TuleapMultiSelectBox) {
			((TuleapMultiSelectBox)currentParent).getItems().add(item);
		}
	}

	/**
	 * Create the form element according to its type.
	 * 
	 * @return Form element
	 */
	private AbstractTuleapFormElement createFormElement() {
		AbstractTuleapFormElement formElement = null;
		if (COLUMN.equals(currentType)) {
			formElement = new TuleapColumn(currentIdentifier);
		} else if (TEXT.equals(currentType)) {
			formElement = new TuleapText(currentIdentifier);
		} else if (STRING.equals(currentType)) {
			formElement = new TuleapString(currentIdentifier);
		} else if (ARTLINK.equals(currentType)) {
			formElement = new TuleapArtifactLink(currentIdentifier);
		} else if (FIELDSET.equals(currentType)) {
			formElement = new TuleapFieldSet(currentIdentifier);
		} else if (SB.equals(currentType)) {
			formElement = new TuleapSelectBox(currentIdentifier);
		} else if (MSB.equals(currentType)) {
			formElement = new TuleapMultiSelectBox(currentIdentifier);
		} else if (DATE.equals(currentType)) {
			formElement = new TuleapDate(currentIdentifier);
		} else if (FILE.equals(currentType)) {
			formElement = new TuleapFileUpload(currentIdentifier);
		} else if (INT.equals(currentType)) {
			formElement = new TuleapInteger(currentIdentifier);
		} else if (FLOAT.equals(currentType)) {
			formElement = new TuleapFloat(currentIdentifier);
		} else if (TBL.equals(currentType)) {
			formElement = new TuleapOpenList(currentIdentifier);
		} else if (PERM.equals(currentType)) {
			formElement = new TuleapPermissionOnArtifact(currentIdentifier);
		} else if (LINEBREAK.equals(currentType)) {
			formElement = new TuleapLineBreak(currentIdentifier);
		} else if (SEPARATOR.equals(currentType)) {
			formElement = new TuleapSeparator(currentIdentifier);
		} else if (STATIC_TEXT.equals(currentType)) {
			formElement = new TuleapStaticText(currentIdentifier);
		} else if (AID.equals(currentType)) {
			formElement = new TuleapArtifactId(currentIdentifier);
		} else if (CROSS.equals(currentType)) {
			formElement = new TuleapCrossReferences(currentIdentifier);
		} else if (LUD.equals(currentType)) {
			formElement = new TuleapLastUpdateDate(currentIdentifier);
		} else if (SUBON.equals(currentType)) {
			formElement = new TuleapSubmittedOn(currentIdentifier);
		} else if (SUBBY.equals(currentType)) {
			formElement = new TuleapSubmittedBy(currentIdentifier);
		} else if (BURNDOWN.equals(currentType)) {
			formElement = new TuleapBurndownChart(currentIdentifier);
		} else if (COMPUTED.equals(currentType)) {
			formElement = new TuleapComputedValue(currentIdentifier);
		} else {
			TuleapCoreActivator.log(TuleapMylynTasksMessages.getString(
					"SaxConfigurationContentHandler.RepositoryConfigurationParseError", currentType, //$NON-NLS-1$
					currentIdentifier), false);
		}

		if (formElement != null) {
			// Set required attribute
			formElement.setRequired(currentRequired);
			// Set rank attribute
			formElement.setRank(currentRank);
		}

		return formElement;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (localName.equals(FORM_ELEMENTS)) {
			parentHierarchy.remove(parentHierarchy.size() - 1);
		} else if (localName.equals(NAME)) {
			if (currentElement instanceof AbstractTuleapFormElement) {
				((AbstractTuleapFormElement)currentElement).setName(characters.toString());
			}
		} else if (localName.equals(LABEL)) {
			if (currentElement instanceof AbstractTuleapFormElement) {
				((AbstractTuleapFormElement)currentElement).setLabel(characters.toString());
			}
		} else if (localName.equals(DESCRIPTION)) {
			if (currentElement instanceof AbstractTuleapFormElement) {
				((AbstractTuleapFormElement)currentElement).setDescription(characters.toString());
			}
		} else if (localName.equals(PROPERTIES)) {
			if (currentElement instanceof TuleapString) {
				// TODO Semantic Title
				// TODO Default value
				((TuleapString)currentElement).setSize(currentSize);
			} else if (currentElement instanceof TuleapText) {
				((TuleapText)currentElement).setRows(currentNumberOfRows);
				((TuleapText)currentElement).setColumns(currentNumberOfColumns);
			}
		} else if (localName.equals(BIND)) {
			parentHierarchy.remove(parentHierarchy.size() - 1);
			if (currentElement instanceof TuleapSelectBox) {
				((TuleapSelectBox)currentElement).setBinding(currentBinding);
				// TODO SemanticContributor
				// TODO open status
				// TODO workflow
			} else if (currentElement instanceof TuleapMultiSelectBox) {
				((TuleapMultiSelectBox)currentElement).setBinding(currentBinding);
				// TODO SemanticContributor
				// TODO open status
				// TODO workflow
			}
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		characters.append(ch, start, length);
	}

	/**
	 * Parse attributes.
	 * 
	 * @param attributes
	 *            Attributes
	 */
	private void parseFormElementAttributes(Attributes attributes) {
		if (attributes == null) {
			return;
		}

		currentIdentifier = attributes.getValue(ID);
		currentType = attributes.getValue(TYPE);
		currentRequired = "1".equals(attributes.getValue(REQUIRED)); //$NON-NLS-1$
		currentRank = Integer.parseInt(attributes.getValue(RANK));
		// TODO Get permissions
	}

	/**
	 * Parse attributes of properties element.
	 * 
	 * @param attributes
	 *            Attributes
	 */
	private void parsePropertiesAttributes(Attributes attributes) {
		if (attributes == null) {
			return;
		}

		String size = attributes.getValue(SIZE);
		if (size != null) {
			currentSize = Integer.parseInt(attributes.getValue(SIZE));
		}
		currentDefaultValue = attributes.getValue(DEFAULT_VALUE);

		String rows = attributes.getValue(ROWS);
		if (rows != null) {
			currentNumberOfRows = Integer.parseInt(attributes.getValue(ROWS));
		}

		String columns = attributes.getValue(COLUMNS);
		if (columns != null) {
			currentNumberOfColumns = Integer.parseInt(attributes.getValue(COLUMNS));
		}
	}

	/**
	 * Parse attributes of bind element.
	 * 
	 * @param attributes
	 *            Attributes
	 */
	private void parseBindAttributes(Attributes attributes) {
		if (attributes == null) {
			return;
		}

		currentBinding = attributes.getValue(TYPE);
	}
}
