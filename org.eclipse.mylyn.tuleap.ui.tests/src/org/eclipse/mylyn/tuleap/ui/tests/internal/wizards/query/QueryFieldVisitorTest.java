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
package org.eclipse.mylyn.tuleap.ui.tests.internal.wizards.query;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.eclipse.mylyn.commons.workbench.forms.DatePicker;
import org.eclipse.mylyn.internal.tasks.core.RepositoryQuery;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tuleap.core.internal.client.ITuleapQueryConstants;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapComputedValue;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapDate;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapFileUpload;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapFloat;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapInteger;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapMultiSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapOpenList;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBox;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapSelectBoxItem;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapString;
import org.eclipse.mylyn.tuleap.core.internal.model.config.field.TuleapText;
import org.eclipse.mylyn.tuleap.core.internal.parser.TuleapGsonProvider;
import org.eclipse.mylyn.tuleap.ui.internal.wizards.query.AbstractTuleapCustomQueryElement;
import org.eclipse.mylyn.tuleap.ui.internal.wizards.query.QueryFieldVisitor;
import org.eclipse.mylyn.tuleap.ui.internal.wizards.query.TuleapCustomQueryPage;
import org.eclipse.mylyn.tuleap.ui.internal.wizards.query.TuleapDateQueryElement;
import org.eclipse.mylyn.tuleap.ui.internal.wizards.query.TuleapDoubleQueryElement;
import org.eclipse.mylyn.tuleap.ui.internal.wizards.query.TuleapIntegerQueryElement;
import org.eclipse.mylyn.tuleap.ui.internal.wizards.query.TuleapLiteralQueryElement;
import org.eclipse.mylyn.tuleap.ui.internal.wizards.query.TuleapSelectBoxQueryElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests of {@link QueryFieldVisitor}.
 *
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class QueryFieldVisitorTest {

	private Shell shell;

	private Group group;

	private Gson gson;

	private TuleapCustomQueryPage page;

	private TaskRepository taskRepository;

	private IRepositoryQuery queryToEdit;

	@Before
	public void setUp() throws Exception {
		shell = new Shell(Display.getDefault());
		Composite c = new Composite(shell, SWT.NONE);
		group = new Group(c, SWT.NONE);
		shell.open();
		gson = TuleapGsonProvider.defaultGson();
		taskRepository = new TaskRepository("tuleap", "https://test.url");
		queryToEdit = new RepositoryQuery("tuleap", "handle:test");
		queryToEdit.setAttribute(ITuleapQueryConstants.QUERY_PROJECT_ID, "101");
		queryToEdit.setAttribute(ITuleapQueryConstants.QUERY_TRACKER_ID, "123");
		queryToEdit.setAttribute(ITuleapQueryConstants.QUERY_KIND, ITuleapQueryConstants.QUERY_KIND_CUSTOM);
		queryToEdit.setSummary("Some test query");
	}

	@After
	public void tearDown() {
		shell.close();
	}

	@Test
	public void testUnusedVisitorHasNoQueryElement() {
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);
		assertTrue(tested.getQueryElements().isEmpty());
	}

	@Test
	public void testTuleapStringInsertsSingleLineTextField() {
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapString field = new TuleapString(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read" });

		tested.visit(field);

		Control[] children = group.getChildren();
		assertEquals(3, children.length);
		checkCreatedFieldLabel();
		checkOperatorIsCombo(0, "contains");
		checkFieldIsSingleLineText();

		java.util.List<AbstractTuleapCustomQueryElement<?>> queryElements = tested.getQueryElements();
		assertEquals(1, queryElements.size());
		assertTrue(queryElements.get(0) instanceof TuleapLiteralQueryElement);
	}

	@Test
	public void testTuleapStringIsInitializedWhenQueryExists() {
		queryToEdit.setAttribute(ITuleapQueryConstants.QUERY_CUSTOM_CRITERIA,
				"{\"some_field\":{\"operator\":\"contains\",\"value\":\"Something\"}}");
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapString field = new TuleapString(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read" });

		tested.visit(field);

		Control[] children = group.getChildren();
		assertEquals(3, children.length);

		checkCreatedFieldLabel();
		checkOperatorIsCombo(0, "contains");
		checkTextFieldIsInitialized("Something");
	}

	@Test
	public void testTuleapOpenListInsertsSingleLineTextField() {
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapOpenList field = new TuleapOpenList(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read" });

		tested.visit(field);

		Control[] children = group.getChildren();
		assertEquals(3, children.length);
		checkCreatedFieldLabel();
		checkOperatorIsCombo(0, "contains");
		checkFieldIsSingleLineText();

		java.util.List<AbstractTuleapCustomQueryElement<?>> queryElements = tested.getQueryElements();
		assertEquals(1, queryElements.size());
		assertTrue(queryElements.get(0) instanceof TuleapLiteralQueryElement);
	}

	@Test
	public void testTuleapOpenListIsInitializedWhenQueryExists() {
		queryToEdit.setAttribute(ITuleapQueryConstants.QUERY_CUSTOM_CRITERIA,
				"{\"some_field\":{\"operator\":\"contains\",\"value\":\"a,b,c\"}}");
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapOpenList field = new TuleapOpenList(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read" });

		tested.visit(field);

		Control[] children = group.getChildren();
		assertEquals(3, children.length);

		checkCreatedFieldLabel();
		checkOperatorIsCombo(0, "contains");
		checkTextFieldIsInitialized("a,b,c");
	}

	@Test
	public void testTuleapFileUploadInsertsNothing() {
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapFileUpload field = new TuleapFileUpload(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read", "submit", "update" });

		tested.visit(field);

		Control[] children = group.getChildren();
		assertEquals(0, children.length);

		assertTrue(tested.getQueryElements().isEmpty());
	}

	@Test
	public void testTuleapTextInsertsMultiLineTextField() {
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapText field = new TuleapText(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read" });

		tested.visit(field);

		Control[] children = group.getChildren();
		assertEquals(3, children.length);

		checkCreatedFieldLabel();
		checkOperatorIsCombo(0, "contains");

		assertTrue(children[2] instanceof Text);
		assertEquals("", ((Text)children[2]).getText());
		assertTrue((children[2].getStyle() & SWT.SINGLE) == 0);
		assertTrue((children[2].getStyle() & SWT.MULTI) != 0);

		java.util.List<AbstractTuleapCustomQueryElement<?>> queryElements = tested.getQueryElements();
		assertEquals(1, queryElements.size());
		assertTrue(queryElements.get(0) instanceof TuleapLiteralQueryElement);
	}

	@Test
	public void testTuleapTextIsInitializedWhenQueryExists() {
		queryToEdit.setAttribute(ITuleapQueryConstants.QUERY_CUSTOM_CRITERIA,
				"{\"some_field\":{\"operator\":\"contains\",\"value\":\"Something\"}}");
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapText field = new TuleapText(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read" });

		tested.visit(field);

		Control[] children = group.getChildren();
		assertEquals(3, children.length);

		checkCreatedFieldLabel();
		checkOperatorIsCombo(0, "contains");

		assertTrue(children[2] instanceof Text);
		assertEquals("Something", ((Text)children[2]).getText());
		assertTrue((children[2].getStyle() & SWT.SINGLE) == 0);
		assertTrue((children[2].getStyle() & SWT.MULTI) != 0);
	}

	@Test
	public void testTuleapComputedInsertsSingleLineTextField() {
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapComputedValue field = new TuleapComputedValue(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read" });

		tested.visit(field);

		Control[] children = group.getChildren();
		assertEquals(3, children.length);
		checkCreatedFieldLabel();
		checkOperatorIsCombo(0, "contains");
		checkFieldIsSingleLineText();

		java.util.List<AbstractTuleapCustomQueryElement<?>> queryElements = tested.getQueryElements();
		assertEquals(1, queryElements.size());
		assertTrue(queryElements.get(0) instanceof TuleapLiteralQueryElement);
	}

	@Test
	public void testTuleapComputedIsInitializedWhenQueryExists() {
		queryToEdit.setAttribute(ITuleapQueryConstants.QUERY_CUSTOM_CRITERIA,
				"{\"some_field\":{\"operator\":\"contains\",\"value\":\"Something\"}}");
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapComputedValue field = new TuleapComputedValue(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read" });

		tested.visit(field);

		Control[] children = group.getChildren();
		assertEquals(3, children.length);
		checkCreatedFieldLabel();
		checkOperatorIsCombo(0, "contains");
		checkTextFieldIsInitialized("Something");
	}

	@Test
	public void testTuleapFloatInsertsSingleLineTextField() {
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapFloat field = new TuleapFloat(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read" });

		tested.visit(field);

		Control[] children = group.getChildren();
		assertEquals(3, children.length);
		checkCreatedFieldLabel();
		checkOperatorIsCombo(0, "contains");
		checkFieldIsSingleLineText();

		java.util.List<AbstractTuleapCustomQueryElement<?>> queryElements = tested.getQueryElements();
		assertEquals(1, queryElements.size());
		assertTrue(queryElements.get(0) instanceof TuleapDoubleQueryElement);
	}

	@Test
	public void testTuleapFloatIsInitializedWhenQueryExists() {
		queryToEdit.setAttribute(ITuleapQueryConstants.QUERY_CUSTOM_CRITERIA,
				"{\"some_field\":{\"operator\":\"contains\",\"value\":\"2.5\"}}");
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapFloat field = new TuleapFloat(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read" });

		tested.visit(field);

		Control[] children = group.getChildren();
		assertEquals(3, children.length);

		checkCreatedFieldLabel();
		checkOperatorIsCombo(0, "contains");
		checkTextFieldIsInitialized("2.5");
	}

	@Test
	public void testTuleapIntegerInsertsSingleLineTextField() {
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapInteger field = new TuleapInteger(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read" });

		tested.visit(field);

		Control[] children = group.getChildren();
		assertEquals(3, children.length);
		checkCreatedFieldLabel();
		checkOperatorIsCombo(0, "contains");
		checkFieldIsSingleLineText();

		java.util.List<AbstractTuleapCustomQueryElement<?>> queryElements = tested.getQueryElements();
		assertEquals(1, queryElements.size());
		assertTrue(queryElements.get(0) instanceof TuleapIntegerQueryElement);
	}

	@Test
	public void testTuleapIntegerIsInitializedWhenQueryExists() {
		queryToEdit.setAttribute(ITuleapQueryConstants.QUERY_CUSTOM_CRITERIA,
				"{\"some_field\":{\"operator\":\"contains\",\"value\":\"25\"}}");
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapInteger field = new TuleapInteger(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read" });

		tested.visit(field);

		Control[] children = group.getChildren();
		assertEquals(3, children.length);

		checkCreatedFieldLabel();
		checkOperatorIsCombo(0, "contains");
		checkTextFieldIsInitialized("25");
	}

	@Test
	public void testTuleapSelectBoxInsertsMultiList() {
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapSelectBox field = new TuleapSelectBox(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read" });
		TuleapSelectBoxItem item = new TuleapSelectBoxItem(1000);
		item.setLabel("Item 0");
		field.addItem(item);
		item = new TuleapSelectBoxItem(1001);
		item.setLabel("Item 1");
		field.addItem(item);
		item = new TuleapSelectBoxItem(1002);
		item.setLabel("Item 2");
		field.addItem(item);

		tested.visit(field);

		Control[] children = group.getChildren();
		assertEquals(3, children.length);
		checkCreatedFieldLabel();
		checkOperatorIsCombo(0, "contains");
		checkFieldIsMultiSelectCombo("");

		java.util.List<AbstractTuleapCustomQueryElement<?>> queryElements = tested.getQueryElements();
		assertEquals(1, queryElements.size());
		assertTrue(queryElements.get(0) instanceof TuleapSelectBoxQueryElement);
	}

	@Test
	public void testTuleapSelectBoxIsInitializedWhenQueryExists() {
		queryToEdit.setAttribute(ITuleapQueryConstants.QUERY_CUSTOM_CRITERIA,
				"{\"some_field\":{\"operator\":\"contains\",\"value\":[\"Item 0\",\"Item 2\"]}}");
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapSelectBox field = new TuleapSelectBox(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read" });
		TuleapSelectBoxItem item = new TuleapSelectBoxItem(1000);
		item.setLabel("Item 0");
		field.addItem(item);
		item = new TuleapSelectBoxItem(1001);
		item.setLabel("Item 1");
		field.addItem(item);
		item = new TuleapSelectBoxItem(1002);
		item.setLabel("Item 2");
		field.addItem(item);

		tested.visit(field);

		Control[] children = group.getChildren();
		assertEquals(3, children.length);

		checkCreatedFieldLabel();
		checkOperatorIsCombo(0, "contains");
		checkFieldIsMultiSelectCombo("Item 0", "Item 2");
	}

	@Test
	public void testTuleapMultiSelectBoxInsertsMultiList() {
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapMultiSelectBox field = new TuleapMultiSelectBox(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read" });
		TuleapSelectBoxItem item = new TuleapSelectBoxItem(1000);
		item.setLabel("Item 0");
		field.addItem(item);
		item = new TuleapSelectBoxItem(1001);
		item.setLabel("Item 1");
		field.addItem(item);
		item = new TuleapSelectBoxItem(1002);
		item.setLabel("Item 2");
		field.addItem(item);

		tested.visit(field);

		Control[] children = group.getChildren();
		assertEquals(3, children.length);
		checkCreatedFieldLabel();
		checkOperatorIsCombo(0, "contains");
		checkFieldIsMultiSelectCombo("");

		java.util.List<AbstractTuleapCustomQueryElement<?>> queryElements = tested.getQueryElements();
		assertEquals(1, queryElements.size());
		assertTrue(queryElements.get(0) instanceof TuleapSelectBoxQueryElement);
	}

	@Test
	public void testTuleapMultiSelectBoxIsInitializedWhenQueryExists() {
		queryToEdit.setAttribute(ITuleapQueryConstants.QUERY_CUSTOM_CRITERIA,
				"{\"some_field\":{\"operator\":\"contains\",\"value\":[\"Item 0\",\"Item 2\"]}}");
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapMultiSelectBox field = new TuleapMultiSelectBox(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read" });
		TuleapSelectBoxItem item = new TuleapSelectBoxItem(1000);
		item.setLabel("Item 0");
		field.addItem(item);
		item = new TuleapSelectBoxItem(1001);
		item.setLabel("Item 1");
		field.addItem(item);
		item = new TuleapSelectBoxItem(1002);
		item.setLabel("Item 2");
		field.addItem(item);

		tested.visit(field);

		Control[] children = group.getChildren();
		assertEquals(3, children.length);

		checkCreatedFieldLabel();
		checkOperatorIsCombo(0, "contains");
		checkFieldIsMultiSelectCombo("Item 0", "Item 2");
	}

	@Test
	public void testTuleapDateInsertsDatePicker() {
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapDate field = new TuleapDate(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read" });

		tested.visit(field);

		Control[] children = group.getChildren();
		assertEquals(3, children.length);
		checkCreatedFieldLabel();
		checkOperatorIsCombo(0, "<", "=", ">", "between");

		Control c3 = children[2];
		assertTrue(c3 instanceof Composite);
		Control[] dateFields = ((Composite)c3).getChildren();
		assertEquals(2, dateFields.length);
		assertTrue(dateFields[0] instanceof DatePicker);
		assertTrue(dateFields[1] instanceof DatePicker);
		// Check second date is hidden
		assertTrue(dateFields[0].isVisible());
		assertFalse(dateFields[1].isVisible());

		java.util.List<AbstractTuleapCustomQueryElement<?>> queryElements = tested.getQueryElements();
		assertEquals(1, queryElements.size());
		assertTrue(queryElements.get(0) instanceof TuleapDateQueryElement);
	}

	@Test
	public void testTuleapDateIsInitializedWhenQueryExistsWithLessThan() {
		queryToEdit.setAttribute(ITuleapQueryConstants.QUERY_CUSTOM_CRITERIA,
				"{\"some_field\":{\"operator\":\"<\",\"value\":[\"2014-01-01T00:00:00.000+00:00\"]}}");
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapDate field = new TuleapDate(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read" });

		tested.visit(field);

		Control[] children = group.getChildren();
		assertEquals(3, children.length);
		checkCreatedFieldLabel();
		checkOperatorIsCombo(0, "<", "=", ">", "between");

		Control c3 = children[2];
		assertTrue(c3 instanceof Composite);
		Control[] dateFields = ((Composite)c3).getChildren();
		assertEquals(2, dateFields.length);
		assertTrue(dateFields[0] instanceof DatePicker);
		assertTrue(dateFields[1] instanceof DatePicker);
		Calendar expectedDate = GregorianCalendar.getInstance();
		expectedDate.setTimeZone(TimeZone.getTimeZone("UTC"));
		expectedDate.set(2014, Calendar.JANUARY, 1, 0, 0, 0);
		expectedDate.set(Calendar.MILLISECOND, 0);
		assertEquals(expectedDate.getTimeInMillis(), ((DatePicker)dateFields[0]).getDate().getTimeInMillis());
		assertNull(((DatePicker)dateFields[1]).getDate());
		// Check second date is hidden
		assertTrue(dateFields[0].isVisible());
		assertFalse(dateFields[1].isVisible());
	}

	@Test
	public void testTuleapDateIsInitializedWhenQueryExistsWithBetween() {
		queryToEdit
		.setAttribute(
				ITuleapQueryConstants.QUERY_CUSTOM_CRITERIA,
				"{\"some_field\":{\"operator\":\"between\",\"value\":[\"2014-01-01T00:00:00.000+00:00\",\"2014-03-12T00:00:00.000+00:00\"]}}");
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit);
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapDate field = new TuleapDate(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read" });

		tested.visit(field);

		Control[] children = group.getChildren();
		assertEquals(3, children.length);

		checkCreatedFieldLabel();
		checkOperatorIsCombo(3, "<", "=", ">", "between");

		Control c3 = children[2];
		assertTrue(c3 instanceof Composite);
		Control[] dateFields = ((Composite)c3).getChildren();
		assertEquals(2, dateFields.length);
		assertTrue(dateFields[0] instanceof DatePicker);
		assertTrue(dateFields[1] instanceof DatePicker);
		Calendar expectedDate = GregorianCalendar.getInstance();
		expectedDate.setTimeZone(TimeZone.getTimeZone("UTC"));
		expectedDate.set(2014, Calendar.JANUARY, 1, 0, 0, 0);
		expectedDate.set(Calendar.MILLISECOND, 0);
		assertEquals(expectedDate.getTimeInMillis(), ((DatePicker)dateFields[0]).getDate().getTimeInMillis());
		expectedDate.set(2014, Calendar.MARCH, 12, 0, 0, 0);
		assertEquals(expectedDate.getTimeInMillis(), ((DatePicker)dateFields[1]).getDate().getTimeInMillis());
		// Check second date is visible
		assertTrue(dateFields[0].isVisible());
		assertTrue(dateFields[1].isVisible());
	}

	@Test
	public void testTuleapDateReactsToOperatorChanges() {
		page = new TuleapCustomQueryPage(taskRepository, queryToEdit) {
			@Override
			public boolean isPageComplete() {
				return true; // Avoid validating page during tests
			}
		};
		QueryFieldVisitor tested = new QueryFieldVisitor(group, gson, page);

		TuleapDate field = new TuleapDate(666);
		field.setLabel("Some field");
		field.setName("some_field");
		field.setPermissions(new String[] {"read" });

		tested.visit(field);

		Control[] children = group.getChildren();
		Control c3 = children[2];
		assertTrue(c3 instanceof Composite);
		Control[] dateFields = ((Composite)c3).getChildren();
		// Check second date is hidden
		assertTrue(dateFields[0].isVisible());
		assertFalse(dateFields[1].isVisible());

		// Now, change operator to "between"
		((Combo)children[1]).setText("between");
		// Check second date is now visible
		assertTrue(dateFields[0].isVisible());
		assertTrue(dateFields[1].isVisible());

		// Now, change operator to "="
		((Combo)children[1]).setText("=");
		// Check second date is now hidden again
		assertTrue(dateFields[0].isVisible());
		assertFalse(dateFields[1].isVisible());

	}

	private void checkFieldIsSingleLineText() {
		Control[] children = group.getChildren();
		assertTrue(children[2] instanceof Text);
		assertEquals("", ((Text)children[2]).getText());
		assertTrue((children[2].getStyle() & SWT.SINGLE) != 0);
		assertTrue((children[2].getStyle() & SWT.MULTI) == 0);
	}

	private void checkOperatorIsCombo(int selectedIndex, String... values) {
		Control[] children = group.getChildren();
		assertTrue(children[1] instanceof Combo);
		assertArrayEquals(values, ((Combo)children[1]).getItems());
		assertEquals(selectedIndex, ((Combo)children[1]).getSelectionIndex());
	}

	private void checkCreatedFieldLabel() {
		Control[] children = group.getChildren();
		assertTrue(children[0] instanceof Label);
		assertEquals("Some field", ((Label)children[0]).getText());
	}

	private void checkTextFieldIsInitialized(String expectedValue) {
		Control[] children = group.getChildren();
		assertTrue(children[2] instanceof Text);
		assertEquals(expectedValue, ((Text)children[2]).getText());
		assertTrue((children[2].getStyle() & SWT.SINGLE) != 0);
		assertTrue((children[2].getStyle() & SWT.MULTI) == 0);
	}

	private void checkFieldIsMultiSelectCombo(String... selected) {
		Control[] children = group.getChildren();
		assertTrue(children[2] instanceof List);
		List list = (List)children[2];
		assertEquals(4, list.getItemCount());
		assertEquals("", list.getItem(0));
		assertEquals("Item 0", list.getItem(1));
		assertEquals("Item 1", list.getItem(2));
		assertEquals("Item 2", list.getItem(3));
		assertTrue((list.getStyle() & SWT.MULTI) != 0);
		assertArrayEquals(selected, list.getSelection());
	}

}
