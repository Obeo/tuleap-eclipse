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
package org.eclipse.mylyn.tuleap.tests.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.TestCase;

import org.eclipse.mylyn.internal.tuleap.core.config.SaxConfigurationContentHandler;
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
import org.eclipse.mylyn.tuleap.tests.support.TuleapFixture;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Test Tuleap Configuration.
 * 
 * @author <a href="mailto:melanie.bats@obeo.fr">Melanie Bats</a>
 * @since 1.0
 */
public class TuleapConfigurationTests extends TestCase {

	/**
	 * The configuration of the repository.
	 */
	private TuleapRepositoryConfiguration config;

	@Override
	public void setUp() {
		try {
			InputStream stream = TuleapFixture.getResource("resource/Tracker_ToolShortName.xml"); //$NON-NLS-1$
			config = parseConfiguration(stream);
		} catch (IOException e) {
			fail(e.getMessage());
		} catch (SAXException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test the parsing of the properties of the configuration.
	 */
	public void testRepositoryConfiguration() {
		assertEquals("ToolName", this.config.getName()); //$NON-NLS-1$
		assertEquals("ToolShortName", this.config.getItemName()); //$NON-NLS-1$
		assertEquals("Tool Description", this.config.getDescription()); //$NON-NLS-1$
	}

	/**
	 * Test the parsing of the structural elements from the configuration.
	 */
	public void testRepositoryConfigurationStructuralElements() {
		// Check structural elements
		AbstractTuleapFormElement fieldset = config.getFormElements().get(0);
		assertEquals(TuleapFieldSet.class, fieldset.getClass());
		this.check(fieldset, "F1", "Details", "fieldset_1", "fieldset_default_tasks_desc_key", true, 495); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$

		AbstractTuleapFormElement fieldset2 = config.getFormElements().get(1);
		assertEquals(TuleapFieldSet.class, fieldset2.getClass());
		this.check(fieldset2, "F6", "State of Progress", "fieldset_2", null, true, 1423); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$

		AbstractTuleapFormElement fieldset3 = config.getFormElements().get(2);
		assertEquals(TuleapFieldSet.class, fieldset3.getClass());
		this.check(fieldset3, "F16", "Fieldset", "fieldset", "Filedset description", false, 1424); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$

		AbstractTuleapFormElement column = config.getFormElements().get(3);
		assertEquals(TuleapColumn.class, column.getClass());
		this.check(column, "F17", "Column", "column", "Column description", false, 2646); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		AbstractTuleapFormElement linebreak = config.getFormElements().get(4);
		assertEquals(TuleapLineBreak.class, linebreak.getClass());
		this.check(linebreak, "F18", "Line Break", "line_break", null, false, 2647); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		AbstractTuleapFormElement separator = config.getFormElements().get(5);
		assertEquals(TuleapSeparator.class, separator.getClass());
		this.check(separator, "F19", "Separator", "separator", null, false, 2648); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		AbstractTuleapFormElement staticText = config.getFormElements().get(6);
		assertEquals(TuleapStaticText.class, staticText.getClass());
		this.check(staticText, "F20", "Static text", "static_text", "Static text description", false, 2649); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		AbstractTuleapFormElement fieldset0 = config.getFormElements().get(7);
		assertEquals(TuleapFieldSet.class, fieldset0.getClass());
		this.check(fieldset0, "F21", "Fieldset0", "fieldset0", "Fieldset0 description", false, 2650); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		AbstractTuleapFormElement fieldset1 = ((TuleapFieldSet)fieldset0).getFormElements().get(0);
		assertEquals(TuleapFieldSet.class, fieldset1.getClass());
		this.check(fieldset1, "F22", "Fieldset1", "fieldset1_11", "Fieldset1 description", false, 0); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		AbstractTuleapFormElement column11 = ((TuleapFieldSet)fieldset1).getFormElements().get(0);
		assertEquals(TuleapColumn.class, column11.getClass());
		this.check(column11, "F23", "Column1.1", "column11", "Column1.1 description", false, 0); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		AbstractTuleapFormElement fieldset12 = ((TuleapFieldSet)fieldset1).getFormElements().get(1);
		assertEquals(TuleapFieldSet.class, fieldset12.getClass());
		this.check(fieldset12, "F24", "Fieldset1.2", "fieldset12", "Fieldset1.2 description", false, 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		AbstractTuleapFormElement column121 = ((TuleapFieldSet)fieldset12).getFormElements().get(0);
		assertEquals(TuleapColumn.class, column121.getClass());
		this.check(column121, "F25", "Column1.2.1", "column121", "Column1.2.1 description", false, 0); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		AbstractTuleapFormElement column122 = ((TuleapFieldSet)fieldset12).getFormElements().get(1);
		assertEquals(TuleapColumn.class, column122.getClass());
		this.check(column122, "F26", "Column1.2.2", "column122", "Column1.2.2 description", false, 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	/**
	 * Test the parsing of the fields from the configuration.
	 */
	public void testRepositoryConfigurationFields() {
		// Check fields
		AbstractTuleapFormElement string = config.getFormElements().get(8);
		assertEquals(TuleapString.class, string.getClass());
		this.check(string, "F27", "String", "string", "String description", true, 2651); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		// Check size attribute
		assertEquals(30, ((TuleapString)string).getSize());

		AbstractTuleapFormElement text = config.getFormElements().get(9);
		assertEquals(TuleapText.class, text.getClass());
		this.check(text, "F28", "Text", "text", "Text description", false, 2653); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		// Check rows and columns attributes
		assertEquals(10, ((TuleapText)text).getRows());
		assertEquals(50, ((TuleapText)text).getColumns());

		AbstractTuleapFormElement selectBox = config.getFormElements().get(10);
		assertEquals(TuleapSelectBox.class, selectBox.getClass());
		this.check(selectBox, "F29", "SelectBox", "selectbox", "SelectBox description", false, 2654); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		// Check select box binding and items
		assertEquals(ITuleapConstants.TULEAP_STATIC_BINDING_ID, ((TuleapSelectBox)selectBox).getBinding());
		assertTrue(((TuleapSelectBox)selectBox).isStaticallyBinded());
		assertEquals(6, ((TuleapSelectBox)selectBox).getItems().size());
		assertEquals("A", ((TuleapSelectBox)selectBox).getItems().get(0)); //$NON-NLS-1$
		assertEquals("B", ((TuleapSelectBox)selectBox).getItems().get(1)); //$NON-NLS-1$
		assertEquals("C", ((TuleapSelectBox)selectBox).getItems().get(2)); //$NON-NLS-1$
		assertEquals("D", ((TuleapSelectBox)selectBox).getItems().get(3)); //$NON-NLS-1$
		assertEquals("E", ((TuleapSelectBox)selectBox).getItems().get(4)); //$NON-NLS-1$
		assertEquals("F", ((TuleapSelectBox)selectBox).getItems().get(5)); //$NON-NLS-1$

		AbstractTuleapFormElement multiSelectBox = config.getFormElements().get(11);
		assertEquals(TuleapMultiSelectBox.class, multiSelectBox.getClass());
		this.check(multiSelectBox, "F30", "MultiSelectBox", "multiselectbox", "MultiSelectBox description", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				false, 2655);

		// Check select box binding and items
		assertEquals(ITuleapConstants.TULEAP_DYNAMIC_BINDING_USERS, ((TuleapMultiSelectBox)multiSelectBox)
				.getBinding());
		assertFalse(((TuleapMultiSelectBox)multiSelectBox).isStaticallyBinded());

		AbstractTuleapFormElement date = config.getFormElements().get(12);
		assertEquals(TuleapDate.class, date.getClass());
		this.check(date, "F31", "Date", "date", "Date description", false, 2656); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		AbstractTuleapFormElement file = config.getFormElements().get(13);
		assertEquals(TuleapFileUpload.class, file.getClass());
		this.check(file, "F32", "FileUpload", "fileupload", "FileUpload description", false, 2657); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		AbstractTuleapFormElement integer = config.getFormElements().get(14);
		assertEquals(TuleapInteger.class, integer.getClass());
		this.check(integer, "F33", "Integer", "integer", "Integer description", true, 2658); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		AbstractTuleapFormElement floatField = config.getFormElements().get(15);
		assertEquals(TuleapFloat.class, floatField.getClass());
		this.check(floatField, "F34", "Float", "float", "Float description", false, 2660); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		AbstractTuleapFormElement openList = config.getFormElements().get(16);
		assertEquals(TuleapOpenList.class, openList.getClass());
		this.check(openList, "F35", "OpenList", "openlist", "OpenList description", false, 2661); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		AbstractTuleapFormElement artefactLink = config.getFormElements().get(17);
		assertEquals(TuleapArtifactLink.class, artefactLink.getClass());
		this.check(artefactLink,
				"F36", "ArtifactLink", "artifactlink", "ArtifactLink description", false, 2662); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		AbstractTuleapFormElement permission = config.getFormElements().get(18);
		assertEquals(TuleapPermissionOnArtifact.class, permission.getClass());
		this.check(permission, "F37", "Permission", "permission_1", "Permission description", false, 2663); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	/**
	 * Test the parsing of the semantic information from the configuration.
	 */
	public void testRepositoryConfigurationSemantic() {
		// Title
		AbstractTuleapFormElement fieldset = config.getFormElements().get(0);
		assertEquals(TuleapFieldSet.class, fieldset.getClass());
		if (fieldset instanceof TuleapFieldSet) {
			TuleapFieldSet tuleapFieldSet = (TuleapFieldSet)fieldset;
			AbstractTuleapFormElement abstractTuleapFormElement = tuleapFieldSet.getFormElements().get(0);
			assertEquals(TuleapString.class, abstractTuleapFormElement.getClass());
			if (abstractTuleapFormElement instanceof TuleapString) {
				TuleapString tuleapString = (TuleapString)abstractTuleapFormElement;
				assertTrue(tuleapString.isSemanticTitle());
			} else {
				fail();
			}
		} else {
			fail();
		}

		// Contributor
		fieldset = config.getFormElements().get(1);
		assertEquals(TuleapFieldSet.class, fieldset.getClass());
		if (fieldset instanceof TuleapFieldSet) {
			TuleapFieldSet tuleapFieldSet = (TuleapFieldSet)fieldset;
			AbstractTuleapFormElement abstractTuleapFormElement = tuleapFieldSet.getFormElements().get(3);
			assertEquals(TuleapColumn.class, abstractTuleapFormElement.getClass());
			if (abstractTuleapFormElement instanceof TuleapColumn) {
				TuleapColumn tuleapColumn = (TuleapColumn)abstractTuleapFormElement;
				AbstractTuleapField abstractTuleapField = tuleapColumn.getFormElements().get(0);
				assertEquals(TuleapMultiSelectBox.class, abstractTuleapField.getClass());
				if (abstractTuleapField instanceof TuleapMultiSelectBox) {
					TuleapMultiSelectBox tuleapMultiSelectBox = (TuleapMultiSelectBox)abstractTuleapField;
					assertTrue(tuleapMultiSelectBox.isSemanticContributor());
				} else {
					fail();
				}
			} else {
				fail();
			}
		} else {
			fail();
		}

		// Status (check open and closed status)

		// TODO Tooltip (unsupported for now)

	}

	/**
	 * Test the parsing of the workflow from the configuration.
	 */
	public void testRepositoryConfigurationWorkflow() {
		fail();
	}

	/**
	 * Test the parsing of the dynamic fields from the configuration.
	 */
	public void testRepositoryConfigurationDynamicFields() {
		// Check dynamic fields
		AbstractTuleapFormElement artifactID = config.getFormElements().get(19);
		assertEquals(TuleapArtifactId.class, artifactID.getClass());
		this.check(artifactID, "F38", "ArtifactID", "artifactid", "ArtifactID description", false, -1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		AbstractTuleapFormElement lastUpdateDate = config.getFormElements().get(20);
		assertEquals(TuleapLastUpdateDate.class, lastUpdateDate.getClass());
		this.check(lastUpdateDate, "F39", "LastUpdateDate", "lastupdatedate", "LastUpdateDate description", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				false, -1);

		AbstractTuleapFormElement artifactAuthor = config.getFormElements().get(21);
		assertEquals(TuleapSubmittedBy.class, artifactAuthor.getClass());
		this.check(artifactAuthor, "F40", "ArtifactAuthor", "artifactauthor", "ArtifactAuthor description", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				false, -1);

		AbstractTuleapFormElement submissionDate = config.getFormElements().get(22);
		assertEquals(TuleapSubmittedOn.class, submissionDate.getClass());
		this.check(submissionDate, "F41", "SubmissionDate", "submissiondate", "SubmissionDate description", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				false, -1);

		AbstractTuleapFormElement crossReferences = config.getFormElements().get(23);
		assertEquals(TuleapCrossReferences.class, crossReferences.getClass());
		this.check(crossReferences, "F42", "CrossReferences", "crossreferences", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"CrossReferences description", false, -1); //$NON-NLS-1$

		AbstractTuleapFormElement chart = config.getFormElements().get(24);
		assertEquals(TuleapBurndownChart.class, chart.getClass());
		this.check(chart, "F43", "Chart", "chart_1", "Chart description", false, -1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		AbstractTuleapFormElement computedField = config.getFormElements().get(25);
		assertEquals(TuleapComputedValue.class, computedField.getClass());
		this.check(computedField, "F44", "ComputedField", "computedfield", "ComputedField description", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				false, -1);
	}

	/**
	 * Checks all the properties of the given abstract Tuleap form element.
	 * 
	 * @param abstractTuleapFormElement
	 *            The form element to test
	 * @param identifier
	 *            The expected identifier
	 * @param label
	 *            The expected label
	 * @param name
	 *            The expected name
	 * @param description
	 *            The expected description
	 * @param required
	 *            <code>true</code> if the form element is required, <code>false</code> otherwise
	 * @param rank
	 *            the expected rank
	 */
	private void check(AbstractTuleapFormElement abstractTuleapFormElement, String identifier, String label,
			String name, String description, boolean required, int rank) {
		assertEquals(identifier, abstractTuleapFormElement.getIdentifier());
		assertEquals(label, abstractTuleapFormElement.getLabel());
		assertEquals(name, abstractTuleapFormElement.getName());
		assertEquals(description, abstractTuleapFormElement.getDescription());
		assertEquals(required, abstractTuleapFormElement.isRequired());
		if (rank != -1) {
			assertEquals(Integer.valueOf(rank), Integer.valueOf(abstractTuleapFormElement.getRank()));
		}
	}

	/**
	 * Parse configuration.
	 * 
	 * @param stream
	 *            Configuration stream
	 * @return Tuleap repository configuration
	 * @throws SAXException
	 *             In case of problems during the parsing
	 * @throws IOException
	 *             In case of problems during the parsing
	 */
	private TuleapRepositoryConfiguration parseConfiguration(InputStream stream) throws SAXException,
			IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(stream));
		SaxConfigurationContentHandler contentHandler = new SaxConfigurationContentHandler(
				"https://mydomain/plugins/tracker/?tracker=42"); //$NON-NLS-1$
		final XMLReader reader = XMLReaderFactory.createXMLReader();
		reader.setContentHandler(contentHandler);
		reader.setErrorHandler(new ErrorHandler() {

			public void error(SAXParseException exception) throws SAXException {
				throw exception;
			}

			public void fatalError(SAXParseException exception) throws SAXException {
				throw exception;
			}

			public void warning(SAXParseException exception) throws SAXException {
				throw exception;
			}
		});
		reader.parse(new InputSource(in));

		TuleapRepositoryConfiguration configuration = contentHandler.getConfiguration();
		assertNotNull(configuration);
		return configuration;
	}
}
