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
	 *            The URL of the repository
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
		if (ITuleapConfigurationConstants.TRACKER.equals(localName)) {
			currentElement = configuration;
		} else if (ITuleapConfigurationConstants.FORM_ELEMENT.equals(localName)) {
			parseFormElementAttributes(attributes);
			Object formElement = createFormElement();
			currentElement = formElement;
			attachFormElement(formElement);
		} else if (ITuleapConfigurationConstants.FORM_ELEMENTS.equals(localName)) {
			parentHierarchy.add(currentElement);
		} else if (ITuleapConfigurationConstants.PROPERTIES.equals(localName)) {
			parsePropertiesAttributes(attributes);
		} else if (ITuleapConfigurationConstants.BIND.equals(localName)) {
			parentHierarchy.add(currentElement);
			parseBindAttributes(attributes);
		} else if (ITuleapConfigurationConstants.ITEM.equals(localName)) {
			if (ITuleapConstants.TULEAP_DYNAMIC_BINDING_USERS.equals(currentBinding)) {
				// TODO Implement dynamic binding
			} else {
				String item = attributes.getValue(ITuleapConfigurationConstants.LABEL);
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
	 *            The value of an item of a {@link TuleapSelectBox} or a {@link TuleapMultiSelectBox}
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
	 * @return The form element created.
	 */
	private AbstractTuleapFormElement createFormElement() {
		AbstractTuleapFormElement formElement = null;
		if (ITuleapConfigurationConstants.COLUMN.equals(currentType)) {
			formElement = new TuleapColumn(currentIdentifier);
		} else if (ITuleapConfigurationConstants.TEXT.equals(currentType)) {
			formElement = new TuleapText(currentIdentifier);
		} else if (ITuleapConfigurationConstants.STRING.equals(currentType)) {
			formElement = new TuleapString(currentIdentifier);
		} else if (ITuleapConfigurationConstants.ARTLINK.equals(currentType)) {
			formElement = new TuleapArtifactLink(currentIdentifier);
		} else if (ITuleapConfigurationConstants.FIELDSET.equals(currentType)) {
			formElement = new TuleapFieldSet(currentIdentifier);
		} else if (ITuleapConfigurationConstants.SB.equals(currentType)) {
			formElement = new TuleapSelectBox(currentIdentifier);
		} else if (ITuleapConfigurationConstants.MSB.equals(currentType)) {
			formElement = new TuleapMultiSelectBox(currentIdentifier);
		} else if (ITuleapConfigurationConstants.DATE.equals(currentType)) {
			formElement = new TuleapDate(currentIdentifier);
		} else if (ITuleapConfigurationConstants.FILE.equals(currentType)) {
			formElement = new TuleapFileUpload(currentIdentifier);
		} else if (ITuleapConfigurationConstants.INT.equals(currentType)) {
			formElement = new TuleapInteger(currentIdentifier);
		} else if (ITuleapConfigurationConstants.FLOAT.equals(currentType)) {
			formElement = new TuleapFloat(currentIdentifier);
		} else if (ITuleapConfigurationConstants.TBL.equals(currentType)) {
			formElement = new TuleapOpenList(currentIdentifier);
		} else if (ITuleapConfigurationConstants.PERM.equals(currentType)) {
			formElement = new TuleapPermissionOnArtifact(currentIdentifier);
		} else if (ITuleapConfigurationConstants.LINEBREAK.equals(currentType)) {
			formElement = new TuleapLineBreak(currentIdentifier);
		} else if (ITuleapConfigurationConstants.SEPARATOR.equals(currentType)) {
			formElement = new TuleapSeparator(currentIdentifier);
		} else if (ITuleapConfigurationConstants.STATIC_TEXT.equals(currentType)) {
			formElement = new TuleapStaticText(currentIdentifier);
		} else if (ITuleapConfigurationConstants.AID.equals(currentType)) {
			formElement = new TuleapArtifactId(currentIdentifier);
		} else if (ITuleapConfigurationConstants.CROSS.equals(currentType)) {
			formElement = new TuleapCrossReferences(currentIdentifier);
		} else if (ITuleapConfigurationConstants.LUD.equals(currentType)) {
			formElement = new TuleapLastUpdateDate(currentIdentifier);
		} else if (ITuleapConfigurationConstants.SUBON.equals(currentType)) {
			formElement = new TuleapSubmittedOn(currentIdentifier);
		} else if (ITuleapConfigurationConstants.SUBBY.equals(currentType)) {
			formElement = new TuleapSubmittedBy(currentIdentifier);
		} else if (ITuleapConfigurationConstants.BURNDOWN.equals(currentType)) {
			formElement = new TuleapBurndownChart(currentIdentifier);
		} else if (ITuleapConfigurationConstants.COMPUTED.equals(currentType)) {
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
		if (localName.equals(ITuleapConfigurationConstants.FORM_ELEMENTS)) {
			parentHierarchy.remove(parentHierarchy.size() - 1);
		} else if (localName.equals(ITuleapConfigurationConstants.NAME)) {
			if (currentElement instanceof AbstractTuleapFormElement) {
				((AbstractTuleapFormElement)currentElement).setName(characters.toString());
			} else if (currentElement instanceof TuleapRepositoryConfiguration) {
				((TuleapRepositoryConfiguration)currentElement).setName(characters.toString());
			}
		} else if (localName.equals(ITuleapConfigurationConstants.LABEL)) {
			if (currentElement instanceof AbstractTuleapFormElement) {
				((AbstractTuleapFormElement)currentElement).setLabel(characters.toString());
			}
		} else if (localName.equals(ITuleapConfigurationConstants.DESCRIPTION)) {
			if (currentElement instanceof AbstractTuleapFormElement) {
				((AbstractTuleapFormElement)currentElement).setDescription(characters.toString());
			} else if (currentElement instanceof TuleapRepositoryConfiguration) {
				((TuleapRepositoryConfiguration)currentElement).setDescription(characters.toString());
			}
		} else if (localName.equals(ITuleapConfigurationConstants.PROPERTIES)) {
			if (currentElement instanceof TuleapString) {
				// TODO Semantic Title
				// TODO Default value
				((TuleapString)currentElement).setSize(currentSize);
			} else if (currentElement instanceof TuleapText) {
				((TuleapText)currentElement).setRows(currentNumberOfRows);
				((TuleapText)currentElement).setColumns(currentNumberOfColumns);
			}
		} else if (localName.equals(ITuleapConfigurationConstants.BIND)) {
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		characters.append(ch, start, length);
	}

	/**
	 * Parse the attributes of the form element.
	 * 
	 * @param attributes
	 *            The attributes of the form element that need to be parsed
	 */
	private void parseFormElementAttributes(Attributes attributes) {
		if (attributes == null) {
			return;
		}

		currentIdentifier = attributes.getValue(ITuleapConfigurationConstants.ID);
		currentType = attributes.getValue(ITuleapConfigurationConstants.TYPE);
		currentRequired = "1".equals(attributes.getValue(ITuleapConfigurationConstants.REQUIRED)); //$NON-NLS-1$
		currentRank = Integer.parseInt(attributes.getValue(ITuleapConfigurationConstants.RANK));
		// TODO Get permissions
	}

	/**
	 * Parse attributes of properties element.
	 * 
	 * @param attributes
	 *            The attributes that need to be parsed
	 */
	private void parsePropertiesAttributes(Attributes attributes) {
		if (attributes == null) {
			return;
		}

		String size = attributes.getValue(ITuleapConfigurationConstants.SIZE);
		if (size != null) {
			currentSize = Integer.parseInt(attributes.getValue(ITuleapConfigurationConstants.SIZE));
		}
		currentDefaultValue = attributes.getValue(ITuleapConfigurationConstants.DEFAULT_VALUE);

		String rows = attributes.getValue(ITuleapConfigurationConstants.ROWS);
		if (rows != null) {
			currentNumberOfRows = Integer.parseInt(attributes.getValue(ITuleapConfigurationConstants.ROWS));
		}

		String columns = attributes.getValue(ITuleapConfigurationConstants.COLUMNS);
		if (columns != null) {
			currentNumberOfColumns = Integer.parseInt(attributes
					.getValue(ITuleapConfigurationConstants.COLUMNS));
		}
	}

	/**
	 * Parse attributes of bind element.
	 * 
	 * @param attributes
	 *            The attributes that need to be parsed
	 */
	private void parseBindAttributes(Attributes attributes) {
		if (attributes == null) {
			return;
		}

		currentBinding = attributes.getValue(ITuleapConfigurationConstants.TYPE);
	}
}
