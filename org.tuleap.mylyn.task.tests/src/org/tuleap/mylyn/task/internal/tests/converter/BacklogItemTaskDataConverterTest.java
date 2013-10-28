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
package org.tuleap.mylyn.task.internal.tests.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.agile.core.data.AbstractBacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.AbstractTaskAttributeWrapper;
import org.tuleap.mylyn.task.agile.core.data.AgileTaskKindUtil;
import org.tuleap.mylyn.task.agile.core.data.planning.BacklogItemWrapper;
import org.tuleap.mylyn.task.agile.core.data.planning.MilestonePlanningWrapper;
import org.tuleap.mylyn.task.internal.core.client.TuleapClientManager;
import org.tuleap.mylyn.task.internal.core.data.AbstractFieldValue;
import org.tuleap.mylyn.task.internal.core.data.AttachmentFieldValue;
import org.tuleap.mylyn.task.internal.core.data.AttachmentValue;
import org.tuleap.mylyn.task.internal.core.data.BoundFieldValue;
import org.tuleap.mylyn.task.internal.core.data.LiteralFieldValue;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskIdentityUtil;
import org.tuleap.mylyn.task.internal.core.data.converter.BacklogItemTaskDataConverter;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapPerson;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapBacklogItemType;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapFileUpload;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapMultiSelectBox;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapSelectBoxItem;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapString;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapText;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.repository.TuleapAttributeMapper;
import org.tuleap.mylyn.task.internal.core.repository.TuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests of the backlogItem task data converter.
 * 
 * @author <a href="mailto:firas.bacha">Firas Bacha</a>
 */
public class BacklogItemTaskDataConverterTest {
	/**
	 * The key used to indicate the kind of a mylyn task data.
	 */
	public static final String TASK_KIND_KEY = "mta_kind"; //$NON-NLS-1$

	/**
	 * The identifier of the configuration id task attribute.
	 */
	public static final String CONFIGURATION_ID = "mtc_configuration_id"; //$NON-NLS-1$

	/**
	 * The identifier of the project name task attribute.
	 */
	public static final String PROJECT_ID = "mtc_project_id"; //$NON-NLS-1$

	/**
	 * Separator used in computed ids.
	 */
	public static final char ID_SEPARATOR = '-';

	private static final int BACKLOG_ITEM_TYPE_ID1 = 234;

	/**
	 * The task repository.
	 */
	private TaskRepository repository;

	/**
	 * The URL of the repository.
	 */
	private String repositoryUrl;

	/**
	 * The kind of the connector.
	 */
	private String connectorKind;

	/**
	 * The id of the project.
	 */
	private int projectId;

	/**
	 * The repository connector.
	 */
	private ITuleapRepositoryConnector repositoryConnector;

	/**
	 * The configuration of the tuleap instance.
	 */
	private TuleapServerConfiguration tuleapServerConfiguration;

	/**
	 * The configuration of the tuleap project.
	 */
	private TuleapProjectConfiguration tuleapProjectConfiguration;

	/**
	 * The name of the project.
	 */
	private String projectName = "Project Name"; //$NON-NLS-1$

	/**
	 * The task data wrapped by the mapper used for tests.
	 */
	private TaskData taskData;

	/**
	 * The attribute mapper used by the mapper under test.
	 */
	private TuleapAttributeMapper attributeMapper;

	/**
	 * The configuration.
	 */
	private TuleapBacklogItemType backlogItemType;

	/**
	 * The backlogItem to test.
	 */
	private TuleapBacklogItem item200;

	/**
	 * Configure the data for the tests.
	 */
	@Before
	public void setUp() {
		this.repositoryUrl = "https://demo.tuleap.net/"; //$NON-NLS-1$
		this.connectorKind = ITuleapConstants.CONNECTOR_KIND;

		this.projectId = 987;

		this.repository = new TaskRepository(connectorKind, repositoryUrl);

		this.tuleapServerConfiguration = new TuleapServerConfiguration(repositoryUrl);

		this.tuleapProjectConfiguration = new TuleapProjectConfiguration(projectName, projectId);

		backlogItemType = new TuleapBacklogItemType(BACKLOG_ITEM_TYPE_ID1, "URL2", "BacklogItem1", //$NON-NLS-1$//$NON-NLS-2$
				"item name 3", "description3", System.currentTimeMillis()); //$NON-NLS-1$ //$NON-NLS-2$

		item200 = new TuleapBacklogItem(200, BACKLOG_ITEM_TYPE_ID1, projectId,
				"item200", "URL", "HTML URL", null, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		tuleapProjectConfiguration.addBacklogItemType(backlogItemType);

		TuleapString stringField = new TuleapString(1001);
		stringField.setSemanticTitle(true);
		backlogItemType.addField(stringField);

		TuleapMultiSelectBox selectBox = new TuleapMultiSelectBox(1002);
		TuleapSelectBoxItem item1 = new TuleapSelectBoxItem(502);
		item1.setLabel("The first Item"); //$NON-NLS-1$
		TuleapSelectBoxItem item2 = new TuleapSelectBoxItem(503);
		item2.setLabel("The second Item"); //$NON-NLS-1$
		selectBox.addItem(item1);
		selectBox.addItem(item2);
		selectBox.getOpenStatus().add(item1);

		backlogItemType.addField(selectBox);

		TuleapFileUpload fileUpload = new TuleapFileUpload(1003);
		fileUpload.setLabel("The file label"); //$NON-NLS-1$
		fileUpload.setName("The file name"); //$NON-NLS-1$
		backlogItemType.addField(fileUpload);

		TuleapText text = new TuleapText(1004);
		text.setLabel("The text label"); //$NON-NLS-1$
		text.setName("The text name"); //$NON-NLS-1$

		backlogItemType.addField(text);

		tuleapServerConfiguration.addProject(tuleapProjectConfiguration);

		final TuleapClientManager clientManager = new TuleapClientManager();
		repositoryConnector = new TuleapRepositoryConnector() {
			@Override
			public TuleapServerConfiguration getTuleapServerConfiguration(String pRepositoryUrl) {
				return tuleapServerConfiguration;
			}

			@Override
			public AbstractTuleapConfiguration refreshConfiguration(TaskRepository pTaskRepository,
					AbstractTuleapConfiguration configuration, IProgressMonitor monitor) throws CoreException {
				return configuration;
			}

			@Override
			public TuleapClientManager getClientManager() {
				return clientManager;
			}
		};

		this.attributeMapper = new TuleapAttributeMapper(repository, repositoryConnector);
		this.taskData = new TaskData(attributeMapper, connectorKind, repositoryUrl, TuleapTaskIdentityUtil
				.getTaskDataId(projectId, BACKLOG_ITEM_TYPE_ID1, item200.getId()));

	}

	/**
	 * Test the BacklogItem specific information creation from TaskData to POJO.
	 */
	@Test
	public void testBacklogItemConverterSpecificDataCreation() {

		item200.setInitialEffort(Float.valueOf(201));
		item200.setAssignedMilestoneId(100);

		BacklogItemTaskDataConverter backlogItemConverter = new BacklogItemTaskDataConverter(backlogItemType,
				repository, repositoryConnector);

		backlogItemConverter.populateTaskData(taskData, item200, null);

		TuleapBacklogItem backlogItem = backlogItemConverter.createTuleapBacklogItem(taskData);
		assertNotNull(backlogItem);
		assertEquals(200, backlogItem.getId());
		assertEquals(Float.valueOf(201), backlogItem.getInitialEffort());
		assertEquals(Integer.valueOf(100), backlogItem.getAssignedMilestoneId());
	}

	/**
	 * Test the BacklogItem specific information population from POJO to TaskData.
	 */
	@Test
	public void testBacklogItemConverterSpecificDataPopulation() {

		item200.setInitialEffort(Float.valueOf(201));
		item200.setAssignedMilestoneId(100);

		BacklogItemTaskDataConverter backlogItemConverter = new BacklogItemTaskDataConverter(backlogItemType,
				repository, repositoryConnector);

		backlogItemConverter.populateTaskData(taskData, item200, null);

		TaskAttribute root = taskData.getRoot();
		TaskAttribute planningAtt = root.getAttribute(MilestonePlanningWrapper.MILESTONE_PLANNING);
		assertNotNull(planningAtt);

		TaskAttribute backlogAtt = planningAtt.getAttribute(MilestonePlanningWrapper.BACKLOG);
		assertNotNull(backlogAtt);

		TaskAttribute theProjectId = taskData.getRoot().getMappedAttribute(PROJECT_ID);
		assertNotNull(theProjectId);
		assertEquals("987", theProjectId.getValue()); //$NON-NLS-1$

		TaskAttribute theConfigurationId = taskData.getRoot().getMappedAttribute(CONFIGURATION_ID);
		assertNotNull(theConfigurationId);
		assertEquals("234", theConfigurationId.getValue()); //$NON-NLS-1$

		TaskAttribute theKind = taskData.getRoot().getMappedAttribute(TASK_KIND_KEY);
		assertNotNull(theKind);
		assertEquals(AgileTaskKindUtil.TASK_KIND_BACKLOG_ITEM, theKind.getValue());

		TaskAttribute item0 = backlogAtt.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + "0"); //$NON-NLS-1$
		assertNotNull(item0);
		assertTrue(item0.getMetaData().isReadOnly());

		TaskAttribute itemId = item0.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + "0" + ID_SEPARATOR //$NON-NLS-1$
				+ AbstractTaskAttributeWrapper.SUFFIX_ID);
		assertNotNull(itemId);
		assertEquals(987 + ":" + BACKLOG_ITEM_TYPE_ID1 + "#200", itemId.getValue()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(TaskAttribute.TYPE_INTEGER, itemId.getMetaData().getType());

		TaskAttribute itemLabel = item0.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + AbstractTaskAttributeWrapper.SUFFIX_LABEL);
		assertNotNull(itemLabel);
		assertEquals("item200", itemLabel.getValue()); //$NON-NLS-1$
		assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, itemLabel.getMetaData().getType());

		TaskAttribute itemEffort = item0.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + "0" //$NON-NLS-1$
				+ ID_SEPARATOR + AbstractBacklogItemWrapper.SUFFIX_BACKLOG_ITEM_POINTS);
		assertNotNull(itemEffort);
		assertEquals(Float.toString(201), itemEffort.getValue());
		assertEquals(TaskAttribute.TYPE_DOUBLE, itemEffort.getMetaData().getType());

	}

	/**
	 * Test the BacklogItem field Values creation from TaskData to POJO.
	 */

	@Test
	public void testBacklogItemConverterFieldsCreation() {
		item200.setInitialEffort(Float.valueOf(201));
		item200.setAssignedMilestoneId(100);

		// the literal field value
		LiteralFieldValue firstLiteralFieldValue = new LiteralFieldValue(1001, "The title"); //$NON-NLS-1$
		item200.addFieldValue(firstLiteralFieldValue);

		// the bound field value
		List<Integer> valueIds = new ArrayList<Integer>();
		valueIds.add(new Integer(11));
		valueIds.add(new Integer(12));
		valueIds.add(new Integer(13));
		BoundFieldValue firstBoundFieldValue = new BoundFieldValue(1002, valueIds);
		item200.addFieldValue(firstBoundFieldValue);

		// the attachment field value
		List<AttachmentValue> attachments = new ArrayList<AttachmentValue>();
		TuleapPerson firstUploadedBy = new TuleapPerson("first username", "first realname", 1, "first email"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		attachments.add(new AttachmentValue("100000", "first name", firstUploadedBy, 123456, //$NON-NLS-1$ //$NON-NLS-2$ 
				"first description", "first type")); //$NON-NLS-1$ //$NON-NLS-2$
		TuleapPerson secondUploadedBy = new TuleapPerson("second username", "second realname", 2, //$NON-NLS-1$ //$NON-NLS-2$
				"second email"); //$NON-NLS-1$
		attachments.add(new AttachmentValue("100001", "second name", secondUploadedBy, 789456, //$NON-NLS-1$ //$NON-NLS-2$
				"second description", "second type")); //$NON-NLS-1$ //$NON-NLS-2$
		AttachmentFieldValue fileDescriptions = new AttachmentFieldValue(1003, attachments);
		item200.addFieldValue(fileDescriptions);

		// another literal field value
		LiteralFieldValue secondLiteralFieldValue = new LiteralFieldValue(1004, "The second title"); //$NON-NLS-1$
		item200.addFieldValue(secondLiteralFieldValue);

		BacklogItemTaskDataConverter backlogItemConverter = new BacklogItemTaskDataConverter(backlogItemType,
				repository, repositoryConnector);

		backlogItemConverter.populateTaskData(taskData, item200, null);

		TuleapBacklogItem backlog = backlogItemConverter.createTuleapBacklogItem(taskData);
		assertNotNull(backlog);

		Collection<AbstractFieldValue> values = backlog.getFieldValues();
		Iterator<AbstractFieldValue> iterator = values.iterator();

		// the first fieldValue
		AbstractFieldValue first = iterator.next();
		assertEquals(first.getClass(), LiteralFieldValue.class);
		LiteralFieldValue firstFieldValue = (LiteralFieldValue)first;
		assertEquals(1001, firstFieldValue.getFieldId());
		assertEquals("The title", firstFieldValue.getFieldValue()); //$NON-NLS-1$

		// the first fieldValue
		AbstractFieldValue second = iterator.next();
		assertEquals(second.getClass(), BoundFieldValue.class);
		BoundFieldValue secondFieldValue = (BoundFieldValue)second;
		assertEquals(1002, secondFieldValue.getFieldId());
		List<Integer> valuesId = secondFieldValue.getValueIds();
		assertEquals(new Integer(11), valuesId.get(0));
		assertEquals(new Integer(502), valuesId.get(1));
		assertEquals(new Integer(503), valuesId.get(2));

		// the first fieldValue
		AbstractFieldValue third = iterator.next();
		assertEquals(third.getClass(), LiteralFieldValue.class);
		LiteralFieldValue thirdFieldValue = (LiteralFieldValue)third;
		assertEquals(1004, thirdFieldValue.getFieldId());
		assertEquals("The second title", thirdFieldValue.getFieldValue()); //$NON-NLS-1$
	}

	/**
	 * Test the BacklogItem field Values population from POJO to TaskData.
	 */

	@Test
	public void testBacklogItemConverterFieldsPopulation() {
		item200.setInitialEffort(Float.valueOf(201));
		item200.setAssignedMilestoneId(100);

		// the literal field value
		LiteralFieldValue firstLiteralFieldValue = new LiteralFieldValue(1001, "The title"); //$NON-NLS-1$
		item200.addFieldValue(firstLiteralFieldValue);

		// the bound field value
		List<Integer> valueIds = new ArrayList<Integer>();
		valueIds.add(new Integer(11));
		valueIds.add(new Integer(12));
		valueIds.add(new Integer(13));
		BoundFieldValue firstBoundFieldValue = new BoundFieldValue(1002, valueIds);
		item200.addFieldValue(firstBoundFieldValue);

		// another literal field value
		LiteralFieldValue secondLiteralFieldValue = new LiteralFieldValue(1004, "The second title"); //$NON-NLS-1$
		item200.addFieldValue(secondLiteralFieldValue);

		BacklogItemTaskDataConverter backlogItemConverter = new BacklogItemTaskDataConverter(backlogItemType,
				repository, repositoryConnector);

		backlogItemConverter.populateTaskData(taskData, item200, null);

		// The summary
		TaskAttribute theSummary = taskData.getRoot().getMappedAttribute(TaskAttribute.SUMMARY);
		assertNotNull(theSummary);
		assertEquals("The title", theSummary.getValue()); //$NON-NLS-1$

		// The status
		TaskAttribute theStatus = taskData.getRoot().getMappedAttribute(TaskAttribute.STATUS);
		assertNotNull(theStatus);
		assertEquals("11", theStatus.getValue()); //$NON-NLS-1$
		assertEquals(3, theStatus.getOptions().size());
		assertEquals("The first Item", theStatus.getOption("502")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("The second Item", theStatus.getOption("503")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("", theStatus.getOption("100")); //$NON-NLS-1$//$NON-NLS-2$

		// Text field
		TaskAttribute theText = taskData.getRoot().getMappedAttribute("1004"); //$NON-NLS-1$
		assertNotNull(theText);
		assertEquals("The second title", theText.getValue()); //$NON-NLS-1$
	}
}
