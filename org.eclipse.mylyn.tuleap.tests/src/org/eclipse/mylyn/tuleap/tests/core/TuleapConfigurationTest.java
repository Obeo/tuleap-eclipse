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
import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.TestCase;

import org.eclipse.mylyn.internal.tuleap.core.config.SaxConfigurationContentHandler;
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
import org.eclipse.mylyn.internal.tuleap.core.model.structural.TuleapFieldSet;
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
 */
@SuppressWarnings("restriction")
public class TuleapConfigurationTest extends TestCase {
	// CHECKSTYLE:OFF
	/**
	 * Test configuration file parser.
	 * 
	 * @throws Exception
	 *             Exception
	 */
	public void testRepositoryConfigurationFromFile() throws Exception {
		InputStream stream = TuleapFixture.getResource("resource/Tracker_ToolShortName.xml"); //$NON-NLS-1$
		TuleapRepositoryConfiguration config = parseConfiguration(stream);

		// Check structural elements
		AbstractTuleapFormElement fieldset = config.getFormElements().get(2);
		AbstractTuleapFormElement column = config.getFormElements().get(3);
		AbstractTuleapFormElement linebreak = config.getFormElements().get(4);
		AbstractTuleapFormElement separator = config.getFormElements().get(5);
		AbstractTuleapFormElement staticText = config.getFormElements().get(6);
		AbstractTuleapFormElement fieldset0 = config.getFormElements().get(7);
		AbstractTuleapFormElement fieldset1 = ((TuleapFieldSet)fieldset0).getFormElements().get(0);
		AbstractTuleapFormElement column11 = ((TuleapFieldSet)fieldset1).getFormElements().get(0);
		AbstractTuleapFormElement fieldset12 = ((TuleapFieldSet)fieldset1).getFormElements().get(1);
		AbstractTuleapFormElement column121 = ((TuleapFieldSet)fieldset12).getFormElements().get(0);
		AbstractTuleapFormElement column122 = ((TuleapFieldSet)fieldset12).getFormElements().get(1);

		assertEquals("F16", fieldset.getIdentifier()); //$NON-NLS-1$
		assertEquals("F17", column.getIdentifier()); //$NON-NLS-1$
		assertEquals("F18", linebreak.getIdentifier()); //$NON-NLS-1$
		assertEquals("F19", separator.getIdentifier()); //$NON-NLS-1$
		assertEquals("F20", staticText.getIdentifier()); //$NON-NLS-1$
		assertEquals("F21", fieldset0.getIdentifier()); //$NON-NLS-1$
		assertEquals("F22", fieldset1.getIdentifier()); //$NON-NLS-1$
		assertEquals("F23", column11.getIdentifier()); //$NON-NLS-1$
		assertEquals("F24", fieldset12.getIdentifier()); //$NON-NLS-1$
		assertEquals("F25", column121.getIdentifier()); //$NON-NLS-1$
		assertEquals("F26", column122.getIdentifier()); //$NON-NLS-1$

		assertEquals("fieldset", fieldset.getName()); //$NON-NLS-1$
		assertEquals("column", column.getName()); //$NON-NLS-1$
		assertEquals("line_break", linebreak.getName()); //$NON-NLS-1$
		assertEquals("separator", separator.getName()); //$NON-NLS-1$
		assertEquals("static_text", staticText.getName()); //$NON-NLS-1$
		assertEquals("fieldset0", fieldset0.getName()); //$NON-NLS-1$
		assertEquals("fieldset1_11", fieldset1.getName()); //$NON-NLS-1$
		assertEquals("column11", column11.getName()); //$NON-NLS-1$
		assertEquals("fieldset12", fieldset12.getName()); //$NON-NLS-1$
		assertEquals("column121", column121.getName()); //$NON-NLS-1$
		assertEquals("column122", column122.getName()); //$NON-NLS-1$

		assertEquals("Fieldset", fieldset.getLabel()); //$NON-NLS-1$
		assertEquals("Column", column.getLabel()); //$NON-NLS-1$
		assertEquals("Line Break", linebreak.getLabel()); //$NON-NLS-1$
		assertEquals("Separator", separator.getLabel()); //$NON-NLS-1$
		assertEquals("Static text", staticText.getLabel()); //$NON-NLS-1$
		assertEquals("Fieldset0", fieldset0.getLabel()); //$NON-NLS-1$
		assertEquals("Fieldset1", fieldset1.getLabel()); //$NON-NLS-1$
		assertEquals("Column1.1", column11.getLabel()); //$NON-NLS-1$
		assertEquals("Fieldset1.2", fieldset12.getLabel()); //$NON-NLS-1$
		assertEquals("Column1.2.1", column121.getLabel()); //$NON-NLS-1$
		assertEquals("Column1.2.2", column122.getLabel()); //$NON-NLS-1$

		assertEquals("Filedset description", fieldset.getDescription()); //$NON-NLS-1$
		assertEquals("Column description", column.getDescription()); //$NON-NLS-1$
		assertEquals(null, linebreak.getDescription());
		assertEquals(null, separator.getDescription());
		assertEquals("Static text description", staticText.getDescription()); //$NON-NLS-1$
		assertEquals("Fieldset0 description", fieldset0.getDescription()); //$NON-NLS-1$
		assertEquals("Fieldset1 description", fieldset1.getDescription()); //$NON-NLS-1$
		assertEquals("Column1.1 description", column11.getDescription()); //$NON-NLS-1$
		assertEquals("Fieldset1.2 description", fieldset12.getDescription()); //$NON-NLS-1$
		assertEquals("Column1.2.1 description", column121.getDescription()); //$NON-NLS-1$
		assertEquals("Column1.2.2 description", column122.getDescription()); //$NON-NLS-1$

		// Check fields
		AbstractTuleapFormElement string = config.getFormElements().get(8);
		AbstractTuleapFormElement text = config.getFormElements().get(9);
		AbstractTuleapFormElement selectBox = config.getFormElements().get(10);
		AbstractTuleapFormElement multiSelectBox = config.getFormElements().get(11);
		AbstractTuleapFormElement date = config.getFormElements().get(12);
		AbstractTuleapFormElement file = config.getFormElements().get(13);
		AbstractTuleapFormElement integer = config.getFormElements().get(14);
		AbstractTuleapFormElement floatField = config.getFormElements().get(15);
		AbstractTuleapFormElement openList = config.getFormElements().get(16);
		AbstractTuleapFormElement artefactLink = config.getFormElements().get(17);
		AbstractTuleapFormElement permission = config.getFormElements().get(18);

		assertEquals("F27", string.getIdentifier()); //$NON-NLS-1$
		assertEquals("F28", text.getIdentifier()); //$NON-NLS-1$
		assertEquals("F29", selectBox.getIdentifier()); //$NON-NLS-1$
		assertEquals("F30", multiSelectBox.getIdentifier()); //$NON-NLS-1$
		assertEquals("F31", date.getIdentifier()); //$NON-NLS-1$
		assertEquals("F32", file.getIdentifier()); //$NON-NLS-1$
		assertEquals("F33", integer.getIdentifier()); //$NON-NLS-1$
		assertEquals("F34", floatField.getIdentifier()); //$NON-NLS-1$
		assertEquals("F35", openList.getIdentifier()); //$NON-NLS-1$
		assertEquals("F36", artefactLink.getIdentifier()); //$NON-NLS-1$
		assertEquals("F37", permission.getIdentifier()); //$NON-NLS-1$

		assertTrue(string instanceof TuleapString);
		assertTrue(text instanceof TuleapText);
		assertTrue(selectBox instanceof TuleapSelectBox);
		assertTrue(multiSelectBox instanceof TuleapMultiSelectBox);
		assertTrue(date instanceof TuleapDate);
		assertTrue(file instanceof TuleapFileUpload);
		assertTrue(integer instanceof TuleapInteger);
		assertTrue(floatField instanceof TuleapFloat);
		assertTrue(openList instanceof TuleapOpenList);
		assertTrue(artefactLink instanceof TuleapArtifactLink);
		assertTrue(permission instanceof TuleapPermissionOnArtifact);

		// Check required attribute
		assertTrue(string.isRequired());
		assertFalse(text.isRequired());

		// Check dynamic fields
		AbstractTuleapFormElement artifactID = config.getFormElements().get(19);
		AbstractTuleapFormElement lastUpdateDate = config.getFormElements().get(20);
		AbstractTuleapFormElement artifactAuthor = config.getFormElements().get(21);
		AbstractTuleapFormElement submissionDate = config.getFormElements().get(22);
		AbstractTuleapFormElement crossReferences = config.getFormElements().get(23);
		AbstractTuleapFormElement chart = config.getFormElements().get(24);
		AbstractTuleapFormElement computedField = config.getFormElements().get(25);

		assertEquals("F38", artifactID.getIdentifier()); //$NON-NLS-1$
		assertEquals("F39", lastUpdateDate.getIdentifier()); //$NON-NLS-1$
		assertEquals("F40", artifactAuthor.getIdentifier()); //$NON-NLS-1$
		assertEquals("F41", submissionDate.getIdentifier()); //$NON-NLS-1$
		assertEquals("F42", crossReferences.getIdentifier()); //$NON-NLS-1$
		assertEquals("F43", chart.getIdentifier()); //$NON-NLS-1$
		assertEquals("F44", computedField.getIdentifier()); //$NON-NLS-1$

		assertTrue(artifactID instanceof TuleapArtifactId);
		assertTrue(lastUpdateDate instanceof TuleapLastUpdateDate);
		assertTrue(artifactAuthor instanceof TuleapSubmittedBy);
		assertTrue(submissionDate instanceof TuleapSubmittedOn);
		assertTrue(crossReferences instanceof TuleapCrossReferences);
		assertTrue(chart instanceof TuleapBurndownChart);
		assertTrue(computedField instanceof TuleapComputedValue);

		// Check rank attribute
		assertEquals(1424, fieldset.getRank());
		assertEquals(2646, column.getRank());
		assertEquals(1, column122.getRank());
		assertEquals(2651, string.getRank());
		assertEquals(2656, date.getRank());

		// Check size attribute
		assertEquals(30, ((TuleapString)string).getSize());

		// Check rows and columns attributes
		assertEquals(10, ((TuleapText)text).getRows());
		assertEquals(50, ((TuleapText)text).getColumns());

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

		// Check select box binding and items
		assertEquals(ITuleapConstants.TULEAP_DYNAMIC_BINDING_USERS, ((TuleapMultiSelectBox)multiSelectBox)
				.getBinding());
		assertFalse(((TuleapMultiSelectBox)multiSelectBox).isStaticallyBinded());
	}

	// CHECKSTYLE:ON

	/**
	 * Parse configuration.
	 * 
	 * @param stream
	 *            Configuration stream
	 * @return Tuleap repository configuration
	 * @throws Exception
	 *             Exception
	 */
	private TuleapRepositoryConfiguration parseConfiguration(InputStream stream) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(stream));
		SaxConfigurationContentHandler contentHandler = new SaxConfigurationContentHandler(
				"https://mydomain/plugins/tracker/?group_id=42"); //$NON-NLS-1$
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

		TuleapRepositoryConfiguration config = contentHandler.getConfiguration();
		assertNotNull(config);
		return config;
	}
}
