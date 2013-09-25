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
package org.tuleap.mylyn.task.internal.tests.client;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMetaData;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.TuleapProjectConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapServerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.field.TuleapArtifactLink;
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
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.tracker.TuleapTrackerConfiguration;
import org.tuleap.mylyn.task.internal.core.model.workflow.TuleapWorkflow;
import org.tuleap.mylyn.task.internal.core.model.workflow.TuleapWorkflowTransition;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.repository.TuleapAttributeMapper;
import org.tuleap.mylyn.task.internal.core.repository.TuleapTaskDataHandler;
import org.tuleap.mylyn.task.internal.core.repository.TuleapTaskMapping;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * The tests class for the Tuleap task data handler.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapTaskDataHandlerTests extends TestCase {

	/**
	 * The label used for contributors.
	 */
	private static final String CONTRIBUTORS_LABEL = "Assigned to"; //$NON-NLS-1$

	/**
	 * The label used for status.
	 */
	private static final String STATUS_LABEL = "Status"; //$NON-NLS-1$

	/**
	 * The label of the completed date.
	 */
	private static final String COMPLETED_DATE_LABEL = "Completed"; //$NON-NLS-1$

	/**
	 * The label of the modified date.
	 */
	private static final String MODIFIED_DATE_LABEL = "Modified"; //$NON-NLS-1$

	/**
	 * The label of the created date.
	 */
	private static final String CREATED_DATE_LABEL = "Created"; //$NON-NLS-1$

	/**
	 * The task repository.
	 */
	private TaskRepository repository;

	/**
	 * The tracker configuration.
	 */
	private TuleapTrackerConfiguration tuleapTrackerConfiguration;

	/**
	 * The URL of the repository.
	 */
	private String repositoryUrl;

	/**
	 * The kind of the connector.
	 */
	private String connectorKind;

	/**
	 * The id of the tracker.
	 */
	private int trackerId;

	/**
	 * The repository connector.
	 */
	private ITuleapRepositoryConnector repositoryConnector;

	/**
	 * The task mapping.
	 */
	private TuleapTaskMapping taskMapping;

	/**
	 * The name of the configuration.
	 */
	private String trackerName;

	/**
	 * The configuration of the tuleap instance.
	 */
	private TuleapServerConfiguration tuleapServerConfiguration;

	/**
	 * The configuration of the tuleap project.
	 */
	private TuleapProjectConfiguration tuleapProjectConfiguration;

	/**
	 * The name of the items.
	 */
	private String itemName;

	/**
	 * The name of the project.
	 */
	private String projectName = "Project Name"; //$NON-NLS-1$

	/**
	 * test setup.
	 */
	@Override
	@Before
	public void setUp() {
		this.repositoryUrl = "https://demo.tuleap.net/plugins/tracker/?groupdId=871"; //$NON-NLS-1$
		this.connectorKind = ITuleapConstants.CONNECTOR_KIND;

		this.trackerName = "Tracker Name"; //$NON-NLS-1$
		String repositoryDescription = "Tracker Description"; //$NON-NLS-1$
		this.itemName = "Bugs"; //$NON-NLS-1$

		this.repository = new TaskRepository(connectorKind, repositoryUrl);
		this.tuleapTrackerConfiguration = new TuleapTrackerConfiguration(trackerId, repositoryUrl);
		tuleapTrackerConfiguration.setName(trackerName);
		tuleapTrackerConfiguration.setDescription(repositoryDescription);
		tuleapTrackerConfiguration.setItemName(itemName);

		this.tuleapServerConfiguration = new TuleapServerConfiguration(repositoryUrl);

		this.tuleapProjectConfiguration = new TuleapProjectConfiguration("Project Name", 2); //$NON-NLS-1$
		this.tuleapProjectConfiguration.addTracker(tuleapTrackerConfiguration);
		tuleapServerConfiguration.addProject(tuleapProjectConfiguration);
		// this.repositoryConnector = new MockedTuleapRepositoryConnector(tuleapServerConfiguration);
		//
		// // Used to specify the tracker to use in the group
		// this.taskMapping = new TuleapTaskMapping() {
		// /**
		// * {@inheritDoc}
		// *
		// * @see org.tuleap.mylyn.task.internal.core.repository.TuleapTaskMapping#getTracker()
		// */
		// @Override
		// public TuleapTrackerConfiguration getTracker() {
		// return tuleapTrackerConfiguration;
		// }
		// };
		fail("Fix the test ");
	}

	// CHECKSTYLE:OFF (7 parameters)
	/**
	 * Creates a Tuleap String.
	 * 
	 * @param id
	 *            The id
	 * @param name
	 *            The name
	 * @param label
	 *            The label
	 * @param description
	 *            The description
	 * @param maximumSize
	 *            The maximum size
	 * @param isTitle
	 *            Is title
	 * @param isRequired
	 *            Is Required
	 * @param rank
	 *            The rank
	 * @param permissions
	 *            The permissions
	 * @return The Tuleap String
	 */
	private TuleapString createTuleapString(int id, String name, String label, String description,
			int maximumSize, boolean isTitle, boolean isRequired, int rank, String[] permissions) {
		// CHECKSTYLE:ON
		TuleapString tuleapString = new TuleapString(id);
		tuleapString.setName(name);
		tuleapString.setLabel(label);
		tuleapString.setDescription(description);
		tuleapString.setSize(maximumSize);
		tuleapString.setSemanticTitle(isTitle);
		tuleapString.setRequired(isRequired);
		tuleapString.setRank(rank);
		tuleapString.setPermissions(permissions);
		return tuleapString;
	}

	// CHECKSTYLE:OFF (7 parameters)
	/**
	 * Creates a Tuleap Text.
	 * 
	 * @param id
	 *            The id
	 * @param name
	 *            The name
	 * @param label
	 *            The label
	 * @param description
	 *            The description
	 * @param isRequired
	 *            Is Required
	 * @param rank
	 *            The rank
	 * @param permissions
	 *            The permissions
	 * @return The Tuleap Text
	 */
	private TuleapText createTuleapText(int id, String name, String label, String description,
			boolean isRequired, int rank, String[] permissions) {
		// CHECKSTYLE:ON
		TuleapText tuleapText = new TuleapText(id);
		tuleapText.setName(name);
		tuleapText.setLabel(label);
		tuleapText.setDescription(description);
		tuleapText.setRequired(isRequired);
		tuleapText.setRank(rank);
		tuleapText.setPermissions(permissions);
		return tuleapText;
	}

	// CHECKSTYLE:OFF (7 parameters)
	/**
	 * Creates a Tuleap Integer.
	 * 
	 * @param id
	 *            The id
	 * @param name
	 *            The name
	 * @param label
	 *            The label
	 * @param description
	 *            The description
	 * @param isRequired
	 *            Is Required
	 * @param rank
	 *            The rank
	 * @param permissions
	 *            The permissions
	 * @return The Tuleap Integer
	 */
	private TuleapInteger createTuleapInteger(int id, String name, String label, String description,
			boolean isRequired, int rank, String[] permissions) {
		// CHECKSTYLE:ON
		TuleapInteger tuleapInteger = new TuleapInteger(id);
		tuleapInteger.setName(name);
		tuleapInteger.setLabel(label);
		tuleapInteger.setDescription(description);
		tuleapInteger.setRequired(isRequired);
		tuleapInteger.setRank(rank);
		tuleapInteger.setPermissions(permissions);
		return tuleapInteger;
	}

	// CHECKSTYLE:OFF (7 parameters)
	/**
	 * Creates a Tuleap Float.
	 * 
	 * @param id
	 *            The id
	 * @param name
	 *            The name
	 * @param label
	 *            The label
	 * @param description
	 *            The description
	 * @param isRequired
	 *            Is Required
	 * @param rank
	 *            The rank
	 * @param permissions
	 *            The permissions
	 * @return The Tuleap Float
	 */
	private TuleapFloat createTuleapFloat(int id, String name, String label, String description,
			boolean isRequired, int rank, String[] permissions) {
		// CHECKSTYLE:ON
		TuleapFloat tuleapFloat = new TuleapFloat(id);
		tuleapFloat.setName(name);
		tuleapFloat.setLabel(label);
		tuleapFloat.setDescription(description);
		tuleapFloat.setRequired(isRequired);
		tuleapFloat.setRank(rank);
		tuleapFloat.setPermissions(permissions);
		return tuleapFloat;
	}

	// CHECKSTYLE:OFF (7 parameters)
	/**
	 * Creates a Tuleap ArtifactLink.
	 * 
	 * @param id
	 *            The id
	 * @param name
	 *            The name
	 * @param label
	 *            The label
	 * @param description
	 *            The description
	 * @param isRequired
	 *            Is Required
	 * @param rank
	 *            The rank
	 * @param permissions
	 *            The permissions
	 * @return The Tuleap Artifact Link
	 */
	private TuleapArtifactLink createTuleapArtifactLink(int id, String name, String label,
			String description, boolean isRequired, int rank, String[] permissions) {
		// CHECKSTYLE:ON
		TuleapArtifactLink tuleapArtifactLink = new TuleapArtifactLink(id);
		tuleapArtifactLink.setName(name);
		tuleapArtifactLink.setLabel(label);
		tuleapArtifactLink.setDescription(description);
		tuleapArtifactLink.setRequired(isRequired);
		tuleapArtifactLink.setRank(rank);
		tuleapArtifactLink.setPermissions(permissions);
		return tuleapArtifactLink;
	}

	// CHECKSTYLE:OFF (7 parameters)
	/**
	 * Creates a Tuleap Date.
	 * 
	 * @param id
	 *            The id
	 * @param name
	 *            The name
	 * @param label
	 *            The label
	 * @param description
	 *            The description
	 * @param isRequired
	 *            Is Required
	 * @param rank
	 *            The rank
	 * @param permissions
	 *            The permissions
	 * @return The Tuleap Date
	 */
	private TuleapDate createTuleapDate(int id, String name, String label, String description,
			boolean isRequired, int rank, String[] permissions) {
		// CHECKSTYLE:ON
		TuleapDate tuleapDate = new TuleapDate(id);
		tuleapDate.setName(name);
		tuleapDate.setLabel(label);
		tuleapDate.setDescription(description);
		tuleapDate.setRequired(isRequired);
		tuleapDate.setRank(rank);
		tuleapDate.setPermissions(permissions);
		return tuleapDate;
	}

	// CHECKSTYLE:OFF (7 parameters)
	/**
	 * Creates a Tuleap File Upload.
	 * 
	 * @param id
	 *            The id
	 * @param name
	 *            The name
	 * @param label
	 *            The label
	 * @param description
	 *            The description
	 * @param isRequired
	 *            Is Required
	 * @param rank
	 *            The rank
	 * @param permissions
	 *            The permissions
	 * @return The Tuleap File Upload
	 */
	private TuleapFileUpload createTuleapFileUpload(int id, String name, String label, String description,
			boolean isRequired, int rank, String[] permissions) {
		// CHECKSTYLE:ON
		TuleapFileUpload tuleapFileUpload = new TuleapFileUpload(id);
		tuleapFileUpload.setName(name);
		tuleapFileUpload.setLabel(label);
		tuleapFileUpload.setDescription(description);
		tuleapFileUpload.setRequired(isRequired);
		tuleapFileUpload.setRank(rank);
		tuleapFileUpload.setPermissions(permissions);
		return tuleapFileUpload;
	}

	// CHECKSTYLE:OFF (7 parameters)
	/**
	 * Creates a Tuleap Open List.
	 * 
	 * @param id
	 *            The id
	 * @param name
	 *            The name
	 * @param label
	 *            The label
	 * @param description
	 *            The description
	 * @param isRequired
	 *            Is Required
	 * @param rank
	 *            The rank
	 * @param permissions
	 *            The permissions
	 * @return The Tuleap Open List
	 */
	private TuleapOpenList createTuleapOpenList(int id, String name, String label, String description,
			boolean isRequired, int rank, String[] permissions) {
		// CHECKSTYLE:ON
		TuleapOpenList tuleapOpenList = new TuleapOpenList(id);
		tuleapOpenList.setName(name);
		tuleapOpenList.setLabel(label);
		tuleapOpenList.setDescription(description);
		tuleapOpenList.setRequired(isRequired);
		tuleapOpenList.setRank(rank);
		tuleapOpenList.setPermissions(permissions);
		return tuleapOpenList;
	}

	// CHECKSTYLE:OFF (7 parameters)
	/**
	 * Creates a Tuleap Select Box.
	 * 
	 * @param id
	 *            The id
	 * @param name
	 *            The name
	 * @param label
	 *            The label
	 * @param description
	 *            The description
	 * @param isRequired
	 *            Is Required
	 * @param isSemanticContributor
	 *            Indicates if the select box represents a semantic contributor
	 * @param binding
	 *            The binding
	 * @param rank
	 *            The rank
	 * @param permissions
	 *            The permissions
	 * @return The Tuleap Select Box
	 */
	private TuleapSelectBox createTuleapSelectBox(int id, String name, String label, String description,
			boolean isRequired, boolean isSemanticContributor, String binding, int rank, String[] permissions) {
		// CHECKSTYLE:ON
		TuleapSelectBox tuleapSelectBox = new TuleapSelectBox(id);
		tuleapSelectBox.setName(name);
		tuleapSelectBox.setLabel(label);
		tuleapSelectBox.setDescription(description);
		tuleapSelectBox.setRequired(isRequired);
		tuleapSelectBox.setRank(rank);
		tuleapSelectBox.setPermissions(permissions);
		tuleapSelectBox.setBinding(binding);
		tuleapSelectBox.setSemanticContributor(isSemanticContributor);
		return tuleapSelectBox;
	}

	// CHECKSTYLE:OFF (7 parameters)
	/**
	 * Creates a Tuleap Multi Select Box.
	 * 
	 * @param id
	 *            The id
	 * @param name
	 *            The name
	 * @param label
	 *            The label
	 * @param description
	 *            The description
	 * @param isRequired
	 *            Is Required
	 * @param isSemanticContributor
	 *            Indicates if the select box represents a semantic contributor
	 * @param binding
	 *            The binding
	 * @param rank
	 *            The rank
	 * @param permissions
	 *            The permissions
	 * @return The Tuleap Multi Select Box
	 */
	private TuleapMultiSelectBox createTuleapMultiSelectBox(int id, String name, String label,
			String description, boolean isRequired, boolean isSemanticContributor, String binding, int rank,
			String[] permissions) {
		// CHECKSTYLE:ON
		TuleapMultiSelectBox tuleapMultiSelectBox = new TuleapMultiSelectBox(id);
		tuleapMultiSelectBox.setName(name);
		tuleapMultiSelectBox.setLabel(label);
		tuleapMultiSelectBox.setDescription(description);
		tuleapMultiSelectBox.setRequired(isRequired);
		tuleapMultiSelectBox.setRank(rank);
		tuleapMultiSelectBox.setPermissions(permissions);
		tuleapMultiSelectBox.setBinding(binding);
		tuleapMultiSelectBox.setSemanticContributor(isSemanticContributor);
		return tuleapMultiSelectBox;
	}

	/**
	 * Initialize the task data.
	 * 
	 * @return The task data.
	 */
	private TaskData initialize() {
		TuleapTaskDataHandler tuleapTaskDataHandler = new TuleapTaskDataHandler(repositoryConnector);
		TaskData taskData = new TaskData(new TuleapAttributeMapper(repository, repositoryConnector),
				repositoryUrl, connectorKind, ""); //$NON-NLS-1$
		try {
			boolean isInitialized = tuleapTaskDataHandler.initializeTaskData(repository, taskData,
					taskMapping, new NullProgressMonitor());
			assertTrue(isInitialized);
			return taskData;
		} catch (CoreException e) {
			fail(e.getMessage());
		}
		return null;
	}

	/**
	 * Test the initialization of the task data with a tuleap string.
	 */
	@Test
	public void testInitializeTaskDataString() {
		int id = 892;
		String name = "TuleapStringName"; //$NON-NLS-1$
		String label = "TuleapStringLabel"; //$NON-NLS-1$
		String description = "TuleapStringDescription"; //$NON-NLS-1$
		int maximumSize = 20;
		boolean isTitle = false;
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapString tuleapString = this.createTuleapString(id, name, label, description, maximumSize,
				isTitle, isRequired, rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapString);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_SHORT_RICH_TEXT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		assertThat(taskAttribute.getValue(), is("")); //$NON-NLS-1$

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SHORT_RICH_TEXT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a read only tuleap string.
	 */
	@Test
	public void testInitializeTaskDataStringReadOnly() {
		int id = 892;
		String name = "TuleapStringReadOnlyName"; //$NON-NLS-1$
		String label = "TuleapStringReadOnlyLabel"; //$NON-NLS-1$
		String description = "TuleapStringReadOnlyDescription"; //$NON-NLS-1$
		int maximumSize = 20;
		boolean isTitle = false;
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ };

		TuleapString tuleapString = this.createTuleapString(id, name, label, description, maximumSize,
				isTitle, isRequired, rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapString);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_SHORT_RICH_TEXT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		assertThat(taskAttribute.getValue(), is("")); //$NON-NLS-1$

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.TRUE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SHORT_RICH_TEXT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap string as a title.
	 */
	@Test
	public void testInitializeTaskDataStringTitle() {
		int id = 892;
		String name = "TuleapStringTitleName"; //$NON-NLS-1$
		String label = "TuleapStringTitleLabel"; //$NON-NLS-1$
		String description = "TuleapStringTitleDescription"; //$NON-NLS-1$
		int maximumSize = 20;
		boolean isTitle = true;
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapString tuleapString = this.createTuleapString(id, name, label, description, maximumSize,
				isTitle, isRequired, rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapString);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_SHORT_RICH_TEXT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(TaskAttribute.SUMMARY));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		assertThat(taskAttribute.getValue(), is("New " + this.itemName)); //$NON-NLS-1$

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SHORT_RICH_TEXT));
	}

	/**
	 * Test the initialization of the task data with a tuleap text.
	 */
	@Test
	public void testInitializeTaskDataText() {
		int id = 892;
		String name = "TuleapTextName"; //$NON-NLS-1$
		String label = "TuleapTextLabel"; //$NON-NLS-1$
		String description = "TuleapTextDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapText tuleapText = this.createTuleapText(id, name, label, description, isRequired, rank,
				permissions);
		tuleapTrackerConfiguration.addField(tuleapText);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_LONG_RICH_TEXT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(2)));
		for (TaskAttribute taskAttribute : attributesByType) {
			if (taskAttribute.getId().equals(Integer.valueOf(id).toString())) {
				assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
				assertThat(taskAttribute.getValue(), is("")); //$NON-NLS-1$

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(metaData.getLabel(), is(label));
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_LONG_RICH_TEXT));
				assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
			} else if (taskAttribute.getId().equals(TaskAttribute.COMMENT_NEW)) {
				assertThat(taskAttribute.getId(), is(TaskAttribute.COMMENT_NEW));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_LONG_RICH_TEXT));
			}
		}
	}

	/**
	 * Test the initialization of the task data with a read only tuleap text.
	 */
	@Test
	public void testInitializeTaskDataTextReadOnly() {
		int id = 892;
		String name = "TuleapTextReadOnlyName"; //$NON-NLS-1$
		String label = "TuleapTextReadOnlyLabel"; //$NON-NLS-1$
		String description = "TuleapTextReadOnlyDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ, };

		TuleapText tuleapText = this.createTuleapText(id, name, label, description, isRequired, rank,
				permissions);
		tuleapTrackerConfiguration.addField(tuleapText);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_LONG_RICH_TEXT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(2)));
		for (TaskAttribute taskAttribute : attributesByType) {
			if (taskAttribute.getId().equals(Integer.valueOf(id).toString())) {
				assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
				assertThat(taskAttribute.getValue(), is("")); //$NON-NLS-1$

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(metaData.getLabel(), is(label));
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.TRUE));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_LONG_RICH_TEXT));
				assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
			} else if (taskAttribute.getId().equals(TaskAttribute.COMMENT_NEW)) {
				assertThat(taskAttribute.getId(), is(TaskAttribute.COMMENT_NEW));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_LONG_RICH_TEXT));
			}
		}
	}

	/**
	 * Test the initialization of the task data with a tuleap integer.
	 */
	@Test
	public void testInitializeTaskDataInteger() {
		int id = 892;
		String name = "TuleapIntegerName"; //$NON-NLS-1$
		String label = "TuleapIntegerLabel"; //$NON-NLS-1$
		String description = "TuleapIntegerDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapInteger tuleapInteger = this.createTuleapInteger(id, name, label, description, isRequired,
				rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapInteger);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_INTEGER);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		assertThat(taskAttribute.getValue(), is(Integer.valueOf(0).toString()));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_INTEGER));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a read only tuleap integer.
	 */
	@Test
	public void testInitializeTaskDataIntegerReadOnly() {
		int id = 892;
		String name = "TuleapIntegerReadOnlyName"; //$NON-NLS-1$
		String label = "TuleapIntegerReadOnlyLabel"; //$NON-NLS-1$
		String description = "TuleapIntegerReadOnlyDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ, };

		TuleapInteger tuleapInteger = this.createTuleapInteger(id, name, label, description, isRequired,
				rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapInteger);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_INTEGER);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		assertThat(taskAttribute.getValue(), is(Integer.valueOf(0).toString()));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.TRUE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_INTEGER));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap float.
	 */
	@Test
	public void testInitializeTaskDataFloat() {
		int id = 892;
		String name = "TuleapFloatName"; //$NON-NLS-1$
		String label = "TuleapFloatLabel"; //$NON-NLS-1$
		String description = "TuleapFloatDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapFloat tuleapFloat = this.createTuleapFloat(id, name, label, description, isRequired, rank,
				permissions);
		tuleapTrackerConfiguration.addField(tuleapFloat);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_DOUBLE);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		assertThat(taskAttribute.getValue(), is("0.0")); //$NON-NLS-1$

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_DOUBLE));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a read only tuleap float.
	 */
	@Test
	public void testInitializeTaskDataFloatReadOnly() {
		int id = 892;
		String name = "TuleapFloatReadOnlyName"; //$NON-NLS-1$
		String label = "TuleapFloatReadOnlyLabel"; //$NON-NLS-1$
		String description = "TuleapFloatReadOnlyDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ, };

		TuleapFloat tuleapFloat = this.createTuleapFloat(id, name, label, description, isRequired, rank,
				permissions);
		tuleapTrackerConfiguration.addField(tuleapFloat);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_DOUBLE);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		assertThat(taskAttribute.getValue(), is("0.0")); //$NON-NLS-1$

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.TRUE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_DOUBLE));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap artifact link.
	 */
	public void testInitializeTaskDataArtifactLink() {
		int id = 892;
		String name = "TuleapArtifactLinkName"; //$NON-NLS-1$
		String label = "TuleapArtifactLinkLabel"; //$NON-NLS-1$
		String description = "TuleapArtifactLinkDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapArtifactLink tuleapArtifactLink = this.createTuleapArtifactLink(id, name, label, description,
				isRequired, rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapArtifactLink);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_TASK_DEPENDENCY);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_TASK_DEPENDENCY));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a read only tuleap artifact link.
	 */
	@Test
	public void testInitializeTaskDataArtifactLinkReadOnly() {
		int id = 892;
		String name = "TuleapArtifactLinkReadOnlyName"; //$NON-NLS-1$
		String label = "TuleapArtifactLinkReadOnlyLabel"; //$NON-NLS-1$
		String description = "TuleapArtifactLinkReadOnlyDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ, };

		TuleapArtifactLink tuleapArtifactLink = this.createTuleapArtifactLink(id, name, label, description,
				isRequired, rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapArtifactLink);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_TASK_DEPENDENCY);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.TRUE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_TASK_DEPENDENCY));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap date.
	 */
	@Test
	public void testInitializeTaskDataDate() {
		int id = 892;
		String name = "TuleapDateName"; //$NON-NLS-1$
		String label = "TuleapDateLabel"; //$NON-NLS-1$
		String description = "TuleapDateDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapDate tuleapDate = this.createTuleapDate(id, name, label, description, isRequired, rank,
				permissions);
		tuleapTrackerConfiguration.addField(tuleapDate);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_DATE);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(4)));
		for (TaskAttribute taskAttribute : attributesByType) {
			if (taskAttribute.getId().equals(Integer.valueOf(id).toString())) {
				assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(metaData.getLabel(), is(label));
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
				assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
			} else if (taskAttribute.getId().equals(TaskAttribute.DATE_COMPLETION)) {
				assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_COMPLETION));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(metaData.getLabel(), is(COMPLETED_DATE_LABEL));
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
			} else if (taskAttribute.getId().equals(TaskAttribute.DATE_CREATION)) {
				assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_CREATION));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(metaData.getLabel(), is(CREATED_DATE_LABEL));
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
			} else if (taskAttribute.getId().equals(TaskAttribute.DATE_MODIFICATION)) {
				assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_MODIFICATION));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(metaData.getLabel(), is(MODIFIED_DATE_LABEL));
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
			}
		}
	}

	/**
	 * Test the initialization of the task data with a read only tuleap date.
	 */
	@Test
	public void testInitializeTaskDataDateReadOnly() {
		int id = 892;
		String name = "TuleapDateReadOnlyName"; //$NON-NLS-1$
		String label = "TuleapDateReadOnlyLabel"; //$NON-NLS-1$
		String description = "TuleapDateReadOnlyDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ, };

		TuleapDate tuleapDate = this.createTuleapDate(id, name, label, description, isRequired, rank,
				permissions);
		tuleapTrackerConfiguration.addField(tuleapDate);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_DATE);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(4)));
		for (TaskAttribute taskAttribute : attributesByType) {
			if (taskAttribute.getId().equals(Integer.valueOf(id).toString())) {
				assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(metaData.getLabel(), is(label));
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.TRUE));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
				assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
			} else if (taskAttribute.getId().equals(TaskAttribute.DATE_COMPLETION)) {
				assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_COMPLETION));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(metaData.getLabel(), is(COMPLETED_DATE_LABEL));
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
			} else if (taskAttribute.getId().equals(TaskAttribute.DATE_CREATION)) {
				assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_CREATION));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(metaData.getLabel(), is(CREATED_DATE_LABEL));
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
			} else if (taskAttribute.getId().equals(TaskAttribute.DATE_MODIFICATION)) {
				assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_MODIFICATION));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(metaData.getLabel(), is(MODIFIED_DATE_LABEL));
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
			}
		}
	}

	/**
	 * Test the initialization of the task data with a tuleap file upload.
	 */
	@Test
	public void testInitializeTaskDataFileUpload() {
		int id = 892;
		String name = "TuleapFileUploadName"; //$NON-NLS-1$
		String label = "TuleapFileUploadLabel"; //$NON-NLS-1$
		String description = "TuleapFileUploadDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapFileUpload tuleapFileUpload = this.createTuleapFileUpload(id, name, label, description,
				isRequired, rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapFileUpload);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_ATTACHMENT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(0)));

		// The field is not created unless the task has been synchronized once
	}

	/**
	 * Test the initialization of the task data with a tuleap open list.
	 */
	@Test
	public void testInitializeTaskDataOpenList() {
		int id = 892;
		String name = "TuleapOpenListName"; //$NON-NLS-1$
		String label = "TuleapOpenListLabel"; //$NON-NLS-1$
		String description = "TuleapOpenListDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapOpenList tuleapOpenList = this.createTuleapOpenList(id, name, label, description, isRequired,
				rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapOpenList);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_SHORT_RICH_TEXT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		assertThat(taskAttribute.getValue(), is("")); //$NON-NLS-1$

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SHORT_RICH_TEXT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a read only tuleap open list.
	 */
	@Test
	public void testInitializeTaskDataOpenListReadOnly() {
		int id = 892;
		String name = "TuleapOpenListReadOnlyName"; //$NON-NLS-1$
		String label = "TuleapOpenListReadOnlyLabel"; //$NON-NLS-1$
		String description = "TuleapOpenListReadOnlyDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ, };

		TuleapOpenList tuleapOpenList = this.createTuleapOpenList(id, name, label, description, isRequired,
				rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapOpenList);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_SHORT_RICH_TEXT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		assertThat(taskAttribute.getValue(), is("")); //$NON-NLS-1$

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.TRUE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SHORT_RICH_TEXT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap select box.
	 */
	@Test
	public void testInitializeTaskDataSelectBox() {
		int id = 892;
		String name = "TuleapSelectBoxName"; //$NON-NLS-1$
		String label = "TuleapSelectBoxLabel"; //$NON-NLS-1$
		String description = "TuleapSelectBoxDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = false;
		String binding = ITuleapConstants.TULEAP_STATIC_BINDING_ID;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapSelectBox tuleapSelectBox = this.createTuleapSelectBox(id, name, label, description,
				isRequired, isSemanticContributor, binding, rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapSelectBox);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_SINGLE_SELECT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SINGLE_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a readonly tuleap select box.
	 */
	@Test
	public void testInitializeTaskDataSelectBoxReadOnly() {
		int id = 892;
		String name = "TuleapSelectBoxReadOnlyName"; //$NON-NLS-1$
		String label = "TuleapSelectBoxReadOnlyLabel"; //$NON-NLS-1$
		String description = "TuleapSelectBoxReadOnlyDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = false;
		String binding = ITuleapConstants.TULEAP_STATIC_BINDING_ID;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ, };

		TuleapSelectBox tuleapSelectBox = this.createTuleapSelectBox(id, name, label, description,
				isRequired, isSemanticContributor, binding, rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapSelectBox);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_SINGLE_SELECT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.TRUE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SINGLE_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap select box with options.
	 */
	@Test
	public void testInitializeTaskDataSelectBoxWithOptions() {
		int id = 892;
		String name = "TuleapSelectBoxWithOptionsName"; //$NON-NLS-1$
		String label = "TuleapSelectBoxWithOptionsLabel"; //$NON-NLS-1$
		String description = "TuleapSelectBoxWithOptionsDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = false;
		String binding = ITuleapConstants.TULEAP_STATIC_BINDING_ID;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapSelectBox tuleapSelectBox = this.createTuleapSelectBox(id, name, label, description,
				isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865745;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label select box with options name"; //$NON-NLS-1$
		String firstItemDescription = "first item description select box with options name"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapSelectBox.addItem(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label select box with options name"; //$NON-NLS-1$
		String secondItemDescription = "second item description select box with options name"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapSelectBox.addItem(secondItem);

		tuleapTrackerConfiguration.addField(tuleapSelectBox);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_SINGLE_SELECT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(Integer.valueOf(taskAttribute.getOptions().size()), is(Integer.valueOf(3)));
		Set<Entry<String, String>> entrySet = taskAttribute.getOptions().entrySet();
		for (Entry<String, String> entry : entrySet) {
			if (String.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID).equals(entry.getKey())) {
				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
			} else if (String.valueOf(firstSelectBoxItemId).equals(entry.getKey())) {
				assertThat(entry.getValue(), is(firstItemLabel));
			} else if (String.valueOf(secondSelectBoxItemId).equals(entry.getKey())) {
				assertThat(entry.getValue(), is(secondItemLabel));
			} else {
				fail("The entry" + entry + " should not be here"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SINGLE_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap select box with options and a workflow.
	 */
	@Test
	public void testInitializeTaskDataSelectBoxWithOptionsAndWorkflow() {
		int id = 892;
		String name = "TuleapSelectBoxWithOptionsAndWorkflowName"; //$NON-NLS-1$
		String label = "TuleapSelectBoxWithOptionsAndWorkflowLabel"; //$NON-NLS-1$
		String description = "TuleapSelectBoxWithOptionsAndWorkflowDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = false;
		String binding = ITuleapConstants.TULEAP_STATIC_BINDING_ID;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapSelectBox tuleapSelectBox = this.createTuleapSelectBox(id, name, label, description,
				isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865745;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label select box with options and a workflow name"; //$NON-NLS-1$
		String firstItemDescription = "first item description select box with options and a workflow name"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapSelectBox.addItem(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label select box with options and a workflow name"; //$NON-NLS-1$
		String secondItemDescription = "second item description select box with options and a workflow name"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapSelectBox.addItem(secondItem);

		TuleapWorkflow tuleapWorkflow = tuleapSelectBox.getWorkflow();
		TuleapWorkflowTransition aTransition = new TuleapWorkflowTransition();
		aTransition.setFrom(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID);
		aTransition.setTo(secondSelectBoxItemId);
		tuleapWorkflow.addTransition(aTransition);

		tuleapTrackerConfiguration.addField(tuleapSelectBox);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_SINGLE_SELECT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));

		// We have one option from the workflow
		assertThat(Integer.valueOf(taskAttribute.getOptions().size()), is(Integer.valueOf(1)));

		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SINGLE_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap select box with options and a workflow.
	 */
	@Test
	public void testInitializeTaskDataSelectBoxWithOptionsAndEmptyWorkflow() {
		int id = 892;
		String name = "TuleapSelectBoxWithOptionsAndEmptyWorkflowName"; //$NON-NLS-1$
		String label = "TuleapSelectBoxWithOptionsAndEmptyWorkflowLabel"; //$NON-NLS-1$
		String description = "TuleapSelectBoxWithOptionsAndEmptyWorkflowDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = false;
		String binding = ITuleapConstants.TULEAP_STATIC_BINDING_ID;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapSelectBox tuleapSelectBox = this.createTuleapSelectBox(id, name, label, description,
				isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865745;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label select box with options and an empty workflow name"; //$NON-NLS-1$
		String firstItemDescription = "first item description select box with options and an empty workflow name"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapSelectBox.addItem(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label select box with options and an empty workflow name"; //$NON-NLS-1$
		String secondItemDescription = "second item description select box with options and an empty workflow name"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapSelectBox.addItem(secondItem);

		TuleapWorkflow tuleapWorkflow = tuleapSelectBox.getWorkflow();
		TuleapWorkflowTransition aTransition = new TuleapWorkflowTransition();
		aTransition.setFrom(firstSelectBoxItemId);
		aTransition.setTo(secondSelectBoxItemId);
		tuleapWorkflow.addTransition(aTransition);

		tuleapTrackerConfiguration.addField(tuleapSelectBox);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_SINGLE_SELECT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(Integer.valueOf(taskAttribute.getOptions().size()), is(Integer.valueOf(0)));

		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SINGLE_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap select box with options.
	 */
	@Test
	public void testInitializeTaskDataSelectBoxStatusWithOptions() {
		int id = 892;
		String name = "TuleapSelectBoxStatusWithOptionsName"; //$NON-NLS-1$
		String label = "TuleapSelectBoxStatusWithOptionsLabel"; //$NON-NLS-1$
		String description = "TuleapSelectBoxStatusWithOptionsDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = false;
		String binding = ITuleapConstants.TULEAP_DYNAMIC_BINDING_USERS;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapSelectBox tuleapSelectBox = this.createTuleapSelectBox(id, name, label, description,
				isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865745;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label select box status with options name"; //$NON-NLS-1$
		String firstItemDescription = "first item description select box status with options name"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapSelectBox.addItem(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label select box status with options name"; //$NON-NLS-1$
		String secondItemDescription = "second item description select box status with options name"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapSelectBox.addItem(secondItem);

		tuleapSelectBox.getOpenStatus().add(firstItem);

		tuleapTrackerConfiguration.addField(tuleapSelectBox);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_SINGLE_SELECT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(TaskAttribute.STATUS));
		assertThat(Integer.valueOf(taskAttribute.getOptions().size()), is(Integer.valueOf(3)));
		Set<Entry<String, String>> entrySet = taskAttribute.getOptions().entrySet();
		for (Entry<String, String> entry : entrySet) {
			if (String.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID).equals(entry.getKey())) {
				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
			} else if (String.valueOf(firstSelectBoxItemId).equals(entry.getKey())) {
				assertThat(entry.getValue(), is(firstItemLabel));
			} else if (String.valueOf(secondSelectBoxItemId).equals(entry.getKey())) {
				assertThat(entry.getValue(), is(secondItemLabel));
			} else {
				fail("The entry" + entry + " should not be here"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(STATUS_LABEL));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SINGLE_SELECT));
	}

	/**
	 * Test the initialization of the task data with a tuleap select box.
	 */
	@Test
	public void testInitializeTaskDataSelectBoxSemanticContributor() {
		int id = 892;
		String name = "TuleapSelectBoxName"; //$NON-NLS-1$
		String label = "TuleapSelectBoxLabel"; //$NON-NLS-1$
		String description = "TuleapSelectBoxDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = true;
		String binding = ITuleapConstants.TULEAP_DYNAMIC_BINDING_USERS;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapSelectBox tuleapSelectBox = this.createTuleapSelectBox(id, name, label, description,
				isRequired, isSemanticContributor, binding, rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapSelectBox);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_SINGLE_SELECT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(TaskAttribute.USER_ASSIGNED));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(CONTRIBUTORS_LABEL));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SINGLE_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_PEOPLE));
	}

	/**
	 * Test the initialization of the task data with a tuleap select box with options.
	 */
	@Test
	public void testInitializeTaskDataSelectBoxSemanticContributorsWithOptions() {
		int id = 892;
		String name = "TuleapSelectBoxSemanticContributorsWithOptionsName"; //$NON-NLS-1$
		String label = "TuleapSelectBoxSemanticContributorsWithOptionsLabel"; //$NON-NLS-1$
		String description = "TuleapSelectBoxSemanticContributorsWithOptionsDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = true;
		String binding = ITuleapConstants.TULEAP_DYNAMIC_BINDING_USERS;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapSelectBox tuleapSelectBox = this.createTuleapSelectBox(id, name, label, description,
				isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865745;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label select box semantic contributors with options name"; //$NON-NLS-1$
		String firstItemDescription = "first item description select box semantic contributors with options name"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapSelectBox.addItem(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label select box semantic contributors with options name"; //$NON-NLS-1$
		String secondItemDescription = "second item description select box semantic contributors with options name"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapSelectBox.addItem(secondItem);

		tuleapTrackerConfiguration.addField(tuleapSelectBox);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_SINGLE_SELECT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(TaskAttribute.USER_ASSIGNED));
		assertThat(Integer.valueOf(taskAttribute.getOptions().size()), is(Integer.valueOf(3)));
		Set<Entry<String, String>> entrySet = taskAttribute.getOptions().entrySet();
		for (Entry<String, String> entry : entrySet) {
			if (String.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID).equals(entry.getKey())) {
				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
			} else if (String.valueOf(firstSelectBoxItemId).equals(entry.getKey())) {
				assertThat(entry.getValue(), is(firstItemLabel));
			} else if (String.valueOf(secondSelectBoxItemId).equals(entry.getKey())) {
				assertThat(entry.getValue(), is(secondItemLabel));
			} else {
				fail("The entry" + entry + " should not be here"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(CONTRIBUTORS_LABEL));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SINGLE_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_PEOPLE));
	}

	/**
	 * Test the initialization of the task data with a tuleap multi select box.
	 */
	@Test
	public void testInitializeTaskDataMultiSelectBox() {
		int id = 892;
		String name = "TuleapMultiSelectBoxName"; //$NON-NLS-1$
		String label = "TuleapMultiSelectBoxLabel"; //$NON-NLS-1$
		String description = "TuleapMultiSelectBoxDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = false;
		String binding = ITuleapConstants.TULEAP_STATIC_BINDING_ID;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapMultiSelectBox tuleapMultiSelectBox = this.createTuleapMultiSelectBox(id, name, label,
				description, isRequired, isSemanticContributor, binding, rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapMultiSelectBox);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_MULTI_SELECT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_MULTI_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a read only tuleap multi select box.
	 */
	@Test
	public void testInitializeTaskDataMultiSelectBoxReadOnly() {
		int id = 892;
		String name = "TuleapMultiSelectBoxReadOnlyName"; //$NON-NLS-1$
		String label = "TuleapMultiSelectBoxReadOnlyLabel"; //$NON-NLS-1$
		String description = "TuleapMultiSelectBoxReadOnlyDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = false;
		String binding = ITuleapConstants.TULEAP_STATIC_BINDING_ID;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ, };

		TuleapMultiSelectBox tuleapMultiSelectBox = this.createTuleapMultiSelectBox(id, name, label,
				description, isRequired, isSemanticContributor, binding, rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapMultiSelectBox);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_MULTI_SELECT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.TRUE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_MULTI_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap multi select box with options.
	 */
	@Test
	public void testInitializeTaskDataMultiSelectBoxWithOptions() {
		int id = 892;
		String name = "TuleapMultiSelectBoxWithOptionsName"; //$NON-NLS-1$
		String label = "TuleapMukltiSelectBoxWithOptionsLabel"; //$NON-NLS-1$
		String description = "TuleapMultiSelectBoxWithOptionsDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = false;
		String binding = ITuleapConstants.TULEAP_STATIC_BINDING_ID;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapMultiSelectBox tuleapMultiSelectBox = this.createTuleapMultiSelectBox(id, name, label,
				description, isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865745;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label select box with options"; //$NON-NLS-1$
		String firstItemDescription = "first item description select box with options"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapMultiSelectBox.addItem(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label select box with options"; //$NON-NLS-1$
		String secondItemDescription = "second item description select box with options"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapMultiSelectBox.addItem(secondItem);

		tuleapTrackerConfiguration.addField(tuleapMultiSelectBox);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_MULTI_SELECT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(Integer.valueOf(taskAttribute.getOptions().size()), is(Integer.valueOf(3)));
		Set<Entry<String, String>> entrySet = taskAttribute.getOptions().entrySet();
		for (Entry<String, String> entry : entrySet) {
			if (String.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID).equals(entry.getKey())) {
				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
			} else if (String.valueOf(firstSelectBoxItemId).equals(entry.getKey())) {
				assertThat(entry.getValue(), is(firstItemLabel));
			} else if (String.valueOf(secondSelectBoxItemId).equals(entry.getKey())) {
				assertThat(entry.getValue(), is(secondItemLabel));
			} else {
				fail("The entry" + entry + " should not be here"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_MULTI_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap multi select box with options.
	 */
	@Test
	public void testInitializeTaskDataMultiSelectBoxWithOneOption() {
		int id = 892;
		String name = "TuleapMultiSelectBoxWithOneOptionName"; //$NON-NLS-1$
		String label = "TuleapMukltiSelectBoxWithOneOptionLabel"; //$NON-NLS-1$
		String description = "TuleapMultiSelectBoxWithOneOptionDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = false;
		String binding = ITuleapConstants.TULEAP_STATIC_BINDING_ID;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapMultiSelectBox tuleapMultiSelectBox = this.createTuleapMultiSelectBox(id, name, label,
				description, isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865745;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label select box with one option"; //$NON-NLS-1$
		String firstItemDescription = "first item description select box with one option"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapMultiSelectBox.addItem(firstItem);

		tuleapTrackerConfiguration.addField(tuleapMultiSelectBox);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_MULTI_SELECT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(Integer.valueOf(taskAttribute.getOptions().size()), is(Integer.valueOf(2)));
		Set<Entry<String, String>> entrySet = taskAttribute.getOptions().entrySet();
		for (Entry<String, String> entry : entrySet) {
			if (String.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID).equals(entry.getKey())) {
				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
			} else if (String.valueOf(firstSelectBoxItemId).equals(entry.getKey())) {
				assertThat(entry.getValue(), is(firstItemLabel));
			} else {
				fail("The entry" + entry + " should not be here"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_MULTI_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap multi select box with options and workflow.
	 */
	@Test
	public void testInitializeTaskDataMultiSelectBoxWithOptionsAndWorkflow() {
		int id = 892;
		String name = "TuleapMultiSelectBoxWithOptionsAndWorkflowName"; //$NON-NLS-1$
		String label = "TuleapMukltiSelectBoxWithOptionsAndWorkflowLabel"; //$NON-NLS-1$
		String description = "TuleapMultiSelectBoxWithOptionsAndWorkflowDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = false;
		String binding = ITuleapConstants.TULEAP_STATIC_BINDING_ID;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapMultiSelectBox tuleapMultiSelectBox = this.createTuleapMultiSelectBox(id, name, label,
				description, isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865745;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label select box with options and workflow"; //$NON-NLS-1$
		String firstItemDescription = "first item description select box with options and workflow"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapMultiSelectBox.addItem(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label select box with options and workflow"; //$NON-NLS-1$
		String secondItemDescription = "second item description select box with options and workflow"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapMultiSelectBox.addItem(secondItem);

		tuleapTrackerConfiguration.addField(tuleapMultiSelectBox);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_MULTI_SELECT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(Integer.valueOf(taskAttribute.getOptions().size()), is(Integer.valueOf(3)));
		Set<Entry<String, String>> entrySet = taskAttribute.getOptions().entrySet();
		for (Entry<String, String> entry : entrySet) {
			if (String.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID).equals(entry.getKey())) {
				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
			} else if (String.valueOf(firstSelectBoxItemId).equals(entry.getKey())) {
				assertThat(entry.getValue(), is(firstItemLabel));
			} else if (String.valueOf(secondSelectBoxItemId).equals(entry.getKey())) {
				assertThat(entry.getValue(), is(secondItemLabel));
			} else {
				fail("The entry" + entry + " should not be here"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_MULTI_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap multi select box with options as a status.
	 */
	@Test
	public void testInitializeTaskDataMultiSelectBoxStatusWithOptions() {
		int id = 892;
		String name = "TuleapMultiSelectBoxStatusWithOptionsName"; //$NON-NLS-1$
		String label = "TuleapMukltiSelectBoxStatusWithOptionsLabel"; //$NON-NLS-1$
		String description = "TuleapMultiSelectBoxStatusWithOptionsDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = false;
		String binding = ITuleapConstants.TULEAP_STATIC_BINDING_ID;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapMultiSelectBox tuleapMultiSelectBox = this.createTuleapMultiSelectBox(id, name, label,
				description, isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865745;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label multi select box status with options"; //$NON-NLS-1$
		String firstItemDescription = "first item description multi select box status with options"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapMultiSelectBox.addItem(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label multi select box status with options"; //$NON-NLS-1$
		String secondItemDescription = "second item description multi select box status with options"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapMultiSelectBox.addItem(secondItem);

		tuleapMultiSelectBox.getOpenStatus().add(firstItem);

		tuleapTrackerConfiguration.addField(tuleapMultiSelectBox);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_MULTI_SELECT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(TaskAttribute.STATUS));
		assertThat(Integer.valueOf(taskAttribute.getOptions().size()), is(Integer.valueOf(3)));
		Set<Entry<String, String>> entrySet = taskAttribute.getOptions().entrySet();
		for (Entry<String, String> entry : entrySet) {
			if (String.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID).equals(entry.getKey())) {
				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
			} else if (String.valueOf(firstSelectBoxItemId).equals(entry.getKey())) {
				assertThat(entry.getValue(), is(firstItemLabel));
			} else if (String.valueOf(secondSelectBoxItemId).equals(entry.getKey())) {
				assertThat(entry.getValue(), is(secondItemLabel));
			} else {
				fail("The entry" + entry + " should not be here"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(STATUS_LABEL));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_MULTI_SELECT));
	}

	/**
	 * Test the initialization of the task data with a tuleap multi select box.
	 */
	@Test
	public void testInitializeTaskDataMultiSelectBoxSemanticContributor() {
		int id = 892;
		String name = "TuleapMultiSelectBoxName"; //$NON-NLS-1$
		String label = "TuleapMultiSelectBoxLabel"; //$NON-NLS-1$
		String description = "TuleapMultiSelectBoxDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = true;
		String binding = ITuleapConstants.TULEAP_DYNAMIC_BINDING_USERS;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapMultiSelectBox tuleapMultiSelectBox = this.createTuleapMultiSelectBox(id, name, label,
				description, isRequired, isSemanticContributor, binding, rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapMultiSelectBox);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_MULTI_SELECT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(TaskAttribute.USER_ASSIGNED));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(CONTRIBUTORS_LABEL));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_MULTI_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_PEOPLE));
	}

	/**
	 * Test the initialization of the task data with a tuleap multi select box with options.
	 */
	@Test
	public void testInitializeTaskDataMultiSelectBoxSemanticContributorsWithOptions() {
		int id = 892;
		String name = "TuleapMultiSelectBoxSemanticContributorsWithOptionsName"; //$NON-NLS-1$
		String label = "TuleapMultiSelectBoxSemanticContributorsWithOptionsLabel"; //$NON-NLS-1$
		String description = "TuleapMultiSelectBoxSemanticContributorsWithOptionsDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = true;
		String binding = ITuleapConstants.TULEAP_DYNAMIC_BINDING_USERS;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapMultiSelectBox tuleapMultiSelectBox = this.createTuleapMultiSelectBox(id, name, label,
				description, isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865745;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label multi select box semantic contributors with options"; //$NON-NLS-1$
		String firstItemDescription = "first item description multi select box semantic contributors with options"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapMultiSelectBox.addItem(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label multi select box semantic contributors with options"; //$NON-NLS-1$
		String secondItemDescription = "second item description multi select box semantic contributors with options"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapMultiSelectBox.addItem(secondItem);

		tuleapTrackerConfiguration.addField(tuleapMultiSelectBox);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_MULTI_SELECT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(TaskAttribute.USER_ASSIGNED));
		assertThat(Integer.valueOf(taskAttribute.getOptions().size()), is(Integer.valueOf(3)));
		Set<Entry<String, String>> entrySet = taskAttribute.getOptions().entrySet();
		for (Entry<String, String> entry : entrySet) {
			if (String.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID).equals(entry.getKey())) {
				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
			} else if (String.valueOf(firstSelectBoxItemId).equals(entry.getKey())) {
				assertThat(entry.getValue(), is(firstItemLabel));
			} else if (String.valueOf(secondSelectBoxItemId).equals(entry.getKey())) {
				assertThat(entry.getValue(), is(secondItemLabel));
			} else {
				fail("The entry" + entry + " should not be here"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(CONTRIBUTORS_LABEL));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_MULTI_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_PEOPLE));
	}

	/**
	 * Creates the task data from the tuleap artifact.
	 * 
	 * @param tuleapArtifact
	 *            The tuleap artifact
	 * @return The newly created task data
	 */
	private TaskData createTaskDataFromArtifact(TuleapArtifact tuleapArtifact) {
		// TuleapTaskDataHandler tuleapTaskDataHandler = new TuleapTaskDataHandler(repositoryConnector);
		// MockedTuleapClient tuleapClient = new MockedTuleapClient(tuleapArtifact,
		// this.tuleapServerConfiguration);
		// return tuleapTaskDataHandler.createTaskDataFromArtifact(tuleapClient, repository, tuleapArtifact,
		// new NullProgressMonitor());
		fail("Fix the test ");
		return null;
	}

	/**
	 * Test the creation of the task data from an empty tuleap artifact.
	 */
	@Test
	public void testCreateTaskDataFromArtifactEmpty() {
		int id = 892;
		Date creationDate = new Date();
		Date modifiedDate = new Date();

		// TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		// tuleapArtifact.setCreationDate(creationDate);
		// tuleapArtifact.setLastModificationDate(modifiedDate);
		//
		// TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);
		//
		// // Check the url of the task
		// String taskId = taskData.getTaskId();
		// assertThat(taskId, is(String.valueOf(id)));
		//
		// // Check the dates
		// List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
		// TaskAttribute.TYPE_DATE);
		// assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(3)));
		// for (TaskAttribute taskAttribute : attributesByType) {
		// if (taskAttribute.getId().equals(TaskAttribute.DATE_COMPLETION)) {
		// assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_COMPLETION));
		// assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(COMPLETED_DATE_LABEL));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
		// } else if (taskAttribute.getId().equals(TaskAttribute.DATE_CREATION)) {
		// assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_CREATION));
		// assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(creationDate);
		// assertThat(Long.valueOf(taskAttribute.getValues().get(0)), is(Long.valueOf(calendar
		// .getTimeInMillis())));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(CREATED_DATE_LABEL));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
		// } else if (taskAttribute.getId().equals(TaskAttribute.DATE_MODIFICATION)) {
		// assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_MODIFICATION));
		// assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(modifiedDate);
		// assertThat(Long.valueOf(taskAttribute.getValues().get(0)), is(Long.valueOf(calendar
		// .getTimeInMillis())));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(MODIFIED_DATE_LABEL));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
		// }
		// }
		fail("Fix the test ");
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with comments.
	 */
	@Test
	public void testCreateTaskDataFromArtifactComments() {
		int id = 892;

		// TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		//		String commentBody = "Comment body"; //$NON-NLS-1$
		//		String userEmail = "user.email@company.com"; //$NON-NLS-1$
		//		String realName = "User Email"; //$NON-NLS-1$
		// int submittedOnTimestamp = 1234;
		// TuleapElementComment artifactComment = new TuleapElementComment(commentBody, userEmail, realName,
		// submittedOnTimestamp);
		// tuleapArtifact.addComment(artifactComment);
		// TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);
		//
		// List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
		// TaskAttribute.TYPE_COMMENT);
		// assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));
		//
		// TaskAttribute taskAttribute = attributesByType.get(0);
		// assertThat(taskAttribute.getId(), is(TaskAttribute.PREFIX_COMMENT +
		// Integer.valueOf(0).toString()));
		// assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// assertThat(taskAttribute.getValue(), is(Integer.valueOf(0).toString()));
		//
		// TaskAttribute textAttribute = taskAttribute.getAttribute(TaskAttribute.COMMENT_TEXT);
		// assertThat(textAttribute, notNullValue());
		// assertThat(textAttribute.getValue(), is(commentBody));
		//
		// TaskAttribute commentAttribute = taskAttribute.getAttribute(TaskAttribute.COMMENT_AUTHOR);
		// assertThat(commentAttribute, notNullValue());
		// assertThat(commentAttribute.getValue(), is(userEmail));
		//
		// TaskAttribute dateAttribute = taskAttribute.getAttribute(TaskAttribute.COMMENT_DATE);
		// assertThat(dateAttribute, notNullValue());
		// assertThat(dateAttribute.getValue(), is(Integer.valueOf(submittedOnTimestamp * 1000).toString()));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.TRUE));
		fail("Fix the test ");
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with a tuleap string.
	 */
	@Test
	public void testCreateTaskDataFromArtifactString() {
		int id = 892;
		String name = "TuleapStringName"; //$NON-NLS-1$
		String label = "TuleapStringLabel"; //$NON-NLS-1$
		String description = "TuleapStringDescription"; //$NON-NLS-1$
		int maximumSize = 20;
		boolean isTitle = false;
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapString tuleapString = this.createTuleapString(id, name, label, description, maximumSize,
				isTitle, isRequired, rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapString);

		String stringValue = "String Value"; //$NON-NLS-1$

		// TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		// tuleapArtifact.putValue(name, stringValue);
		// TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);
		//
		// List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
		// TaskAttribute.TYPE_SHORT_RICH_TEXT);
		// assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));
		//
		// TaskAttribute taskAttribute = attributesByType.get(0);
		// assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		// assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// assertThat(taskAttribute.getValue(), is(stringValue));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(label));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_SHORT_RICH_TEXT));
		// assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
		fail("Fix the test ");
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with a tuleap string as the title.
	 */
	@Test
	public void testCreateTaskDataFromArtifactStringTitle() {
		int id = 892;
		String name = "TuleapStringTitleName"; //$NON-NLS-1$
		String label = "TuleapStringTitleLabel"; //$NON-NLS-1$
		String description = "TuleapStringTitleDescription"; //$NON-NLS-1$
		int maximumSize = 20;
		boolean isTitle = true;
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapString tuleapString = this.createTuleapString(id, name, label, description, maximumSize,
				isTitle, isRequired, rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapString);

		String stringValue = "String Value"; //$NON-NLS-1$

		// TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		// tuleapArtifact.putValue(name, stringValue);
		// TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);
		//
		// List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
		// TaskAttribute.TYPE_SHORT_RICH_TEXT);
		// assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));
		//
		// TaskAttribute taskAttribute = attributesByType.get(0);
		// assertThat(taskAttribute.getId(), is(TaskAttribute.SUMMARY));
		// assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// assertThat(taskAttribute.getValue(), is(stringValue));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(label));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_SHORT_RICH_TEXT));
		fail("Fix the test ");
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with a tuleap text.
	 */
	@Test
	public void testCreateTaskDataFromArtifactText() {
		int id = 892;
		String name = "TuleapTextName"; //$NON-NLS-1$
		String label = "TuleapTextLabel"; //$NON-NLS-1$
		String description = "TuleapTextDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapText tuleapText = this.createTuleapText(id, name, label, description, isRequired, rank,
				permissions);
		tuleapTrackerConfiguration.addField(tuleapText);

		String textValue = "Text Value"; //$NON-NLS-1$

		// TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		// tuleapArtifact.putValue(name, textValue);
		// TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);
		//
		// // Check attributes
		// List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
		// TaskAttribute.TYPE_LONG_RICH_TEXT);
		// assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(2)));
		// for (TaskAttribute taskAttribute : attributesByType) {
		// if (taskAttribute.getId().equals(Integer.valueOf(id).toString())) {
		// assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		// assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// assertThat(taskAttribute.getValue(), is(textValue));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(label));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_LONG_RICH_TEXT));
		// assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
		// } else if (taskAttribute.getId().equals(TaskAttribute.COMMENT_NEW)) {
		// assertThat(taskAttribute.getId(), is(TaskAttribute.COMMENT_NEW));
		// assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_LONG_RICH_TEXT));
		// }
		// }
		fail("Fix the test ");
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with a tuleap integer.
	 */
	@Test
	public void testCreateTaskDataFromArtifactInteger() {
		int id = 892;
		String name = "TuleapIntegerName"; //$NON-NLS-1$
		String label = "TuleapIntegerLabel"; //$NON-NLS-1$
		String description = "TuleapIntegerDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapInteger tuleapInteger = this.createTuleapInteger(id, name, label, description, isRequired,
				rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapInteger);

		String integerValue = "825"; //$NON-NLS-1$

		// TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		// tuleapArtifact.putValue(name, integerValue);
		// TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);
		//
		// // Check attributes
		// List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
		// TaskAttribute.TYPE_INTEGER);
		// assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));
		//
		// TaskAttribute taskAttribute = attributesByType.get(0);
		// assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		// assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// assertThat(taskAttribute.getValue(), is(integerValue));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(label));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_INTEGER));
		// assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
		fail("Fix the test ");
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with a tuleap float.
	 */
	@Test
	public void testCreateTaskDataFromArtifactFloat() {
		int id = 892;
		String name = "TuleapFloatName"; //$NON-NLS-1$
		String label = "TuleapFloatLabel"; //$NON-NLS-1$
		String description = "TuleapFloatDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapFloat tuleapFloat = this.createTuleapFloat(id, name, label, description, isRequired, rank,
				permissions);
		tuleapTrackerConfiguration.addField(tuleapFloat);

		String floatValue = "825.417"; //$NON-NLS-1$

		// TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		// tuleapArtifact.putValue(name, floatValue);
		// TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);
		//
		// // Check attributes
		// List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
		// TaskAttribute.TYPE_DOUBLE);
		// assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));
		//
		// TaskAttribute taskAttribute = attributesByType.get(0);
		// assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		// assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// assertThat(taskAttribute.getValue(), is(floatValue));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(label));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_DOUBLE));
		// assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
		fail("Fix the test ");
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with a tuleap artifact link.
	 */
	@Test
	public void testCreateTaskDataFromArtifactArtifactLink() {
		int id = 892;
		String name = "TuleapArtifactLinkName"; //$NON-NLS-1$
		String label = "TuleapArtifactLinkLabel"; //$NON-NLS-1$
		String description = "TuleapArtifactLinkDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapArtifactLink tuleapArtifactLink = this.createTuleapArtifactLink(id, name, label, description,
				isRequired, rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapArtifactLink);

		String artifactLinkValue = "892, 921"; //$NON-NLS-1$

		// TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		// tuleapArtifact.putValue(name, artifactLinkValue);
		// TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);
		//
		// // Check attributes
		// List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
		// TaskAttribute.TYPE_TASK_DEPENDENCY);
		// assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));
		//
		// TaskAttribute taskAttribute = attributesByType.get(0);
		// assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		// assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// assertThat(taskAttribute.getValue(), is(artifactLinkValue));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(label));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_TASK_DEPENDENCY));
		// assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
		fail("Fix the test ");
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with a tuleap date.
	 */
	@Test
	public void testCreateTaskDataFromArtifactDate() {
		int id = 892;
		String name = "TuleapDateName"; //$NON-NLS-1$
		String label = "TuleapDateLabel"; //$NON-NLS-1$
		String description = "TuleapDateDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapDate tuleapDate = this.createTuleapDate(id, name, label, description, isRequired, rank,
				permissions);
		tuleapTrackerConfiguration.addField(tuleapDate);

		Date creationDate = new Date();
		Date modifiedDate = new Date();
		Date date = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dateInTuleapFormat = (int)(calendar.getTimeInMillis() / 1000);
		String dateValue = String.valueOf(dateInTuleapFormat);

		// TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		// tuleapArtifact.setCreationDate(creationDate);
		// tuleapArtifact.setLastModificationDate(modifiedDate);
		// tuleapArtifact.putValue(name, dateValue);
		//
		// TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);
		//
		// // Check attributes
		// List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
		// TaskAttribute.TYPE_DATE);
		// assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(4)));
		// for (TaskAttribute taskAttribute : attributesByType) {
		// if (taskAttribute.getId().equals(Integer.valueOf(id).toString())) {
		// assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		// assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// assertThat(Long.valueOf(taskAttribute.getValue()), is(Long
		// .valueOf(1000L * dateInTuleapFormat)));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(label));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
		// assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
		// } else if (taskAttribute.getId().equals(TaskAttribute.DATE_COMPLETION)) {
		// assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_COMPLETION));
		// assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(COMPLETED_DATE_LABEL));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
		// } else if (taskAttribute.getId().equals(TaskAttribute.DATE_CREATION)) {
		// assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_CREATION));
		// assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// calendar.setTime(creationDate);
		// assertThat(Long.valueOf(taskAttribute.getValues().get(0)), is(Long.valueOf(calendar
		// .getTimeInMillis())));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(CREATED_DATE_LABEL));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
		// } else if (taskAttribute.getId().equals(TaskAttribute.DATE_MODIFICATION)) {
		// assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_MODIFICATION));
		// assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// calendar.setTime(modifiedDate);
		// assertThat(Long.valueOf(taskAttribute.getValues().get(0)), is(Long.valueOf(calendar
		// .getTimeInMillis())));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(MODIFIED_DATE_LABEL));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
		// }
		// }
		fail("Fix the test ");
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with a tuleap file upload.
	 */
	@Test
	public void testCreateTaskDataFromArtifactFileUpload() {
		int id = 892;
		String name = "TuleapFileUploadName"; //$NON-NLS-1$
		String label = "TuleapFileUploadLabel"; //$NON-NLS-1$
		String description = "TuleapFileUploadDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapFileUpload tuleapFileUpload = this.createTuleapFileUpload(id, name, label, description,
				isRequired, rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapFileUpload);

		// TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		//
		//		String attachementIdentifier = "94212"; //$NON-NLS-1$
		//		String attachementName = "Photo.png"; //$NON-NLS-1$
		//		String username = "enigma"; //$NON-NLS-1$
		//		String realname = "Edward Nigma"; //$NON-NLS-1$
		// int identifier = 218;
		//		String mail = "edward.nigma@we.com"; //$NON-NLS-1$
		// TuleapPerson uploadedBy = new TuleapPerson(username, realname, identifier, mail);
		// Long filesize = Long.valueOf(2048);
		//		String desc = "A new photo"; //$NON-NLS-1$
		//		String type = "img/png"; //$NON-NLS-1$
		// AttachmentFieldValue attachment = new AttachmentFieldValue(attachementIdentifier, attachementName,
		// uploadedBy, filesize, desc, type);
		// tuleapArtifact.putAttachment(name, attachment);
		//
		// TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);
		//
		// // Check attributes
		// List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
		// TaskAttribute.TYPE_ATTACHMENT);
		// assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));
		// TaskAttribute taskAttribute = attributesByType.get(0);
		//		assertThat(taskAttribute.getId(), is(TaskAttribute.PREFIX_ATTACHMENT + name + "---" //$NON-NLS-1$
		// + attachementIdentifier));
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		//
		// TaskAttribute authorAttribute = taskAttribute.getAttribute(TaskAttribute.ATTACHMENT_AUTHOR);
		// assertThat(authorAttribute, notNullValue());
		// assertThat(authorAttribute.getValue(), is(mail));
		//
		// TaskAttribute contentTypeAttribute = taskAttribute
		// .getAttribute(TaskAttribute.ATTACHMENT_CONTENT_TYPE);
		// assertThat(contentTypeAttribute, notNullValue());
		// assertThat(contentTypeAttribute.getValue(), is(type));
		//
		// TaskAttribute descriptionAttribute =
		// taskAttribute.getAttribute(TaskAttribute.ATTACHMENT_DESCRIPTION);
		// assertThat(descriptionAttribute, notNullValue());
		// assertThat(descriptionAttribute.getValue(), is(desc));
		//
		// TaskAttribute filenameAttribute = taskAttribute.getAttribute(TaskAttribute.ATTACHMENT_FILENAME);
		// assertThat(filenameAttribute, notNullValue());
		// assertThat(filenameAttribute.getValue(), is(attachementName));
		//
		// TaskAttribute attachementSizeAttribute = taskAttribute.getAttribute(TaskAttribute.ATTACHMENT_SIZE);
		// assertThat(attachementSizeAttribute, notNullValue());
		// assertThat(attachementSizeAttribute.getValue(), is(filesize.toString()));
		fail("Fix the test ");
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with a tuleap open list.
	 */
	@Test
	public void testCreateTaskDataFromArtifactOpenList() {
		int id = 892;
		String name = "TuleapOpenListName"; //$NON-NLS-1$
		String label = "TuleapOpenListLabel"; //$NON-NLS-1$
		String description = "TuleapOpenListDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapOpenList tuleapOpenList = this.createTuleapOpenList(id, name, label, description, isRequired,
				rank, permissions);
		tuleapTrackerConfiguration.addField(tuleapOpenList);

		String openListValue = "First Open List Value, Second Open List Value, Third Open List Value"; //$NON-NLS-1$

		// TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		// tuleapArtifact.putValue(name, openListValue);
		// TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);
		//
		// // Check attributes
		// List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
		// TaskAttribute.TYPE_SHORT_RICH_TEXT);
		// assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));
		//
		// TaskAttribute taskAttribute = attributesByType.get(0);
		// assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		// assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// assertThat(taskAttribute.getValue(), is(openListValue));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(label));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_SHORT_RICH_TEXT));
		// assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
		fail("Fix the test ");
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with a tuleap select box with options.
	 */
	@Test
	public void testCreateTaskDataFromArtifactSelectBoxWithOptions() {
		int id = 892;
		String name = "TuleapSelectBoxWithOptionsName"; //$NON-NLS-1$
		String label = "TuleapSelectBoxWithOptionsLabel"; //$NON-NLS-1$
		String description = "TuleapSelectBoxWithOptionsDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = false;
		String binding = ITuleapConstants.TULEAP_STATIC_BINDING_ID;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapSelectBox tuleapSelectBox = this.createTuleapSelectBox(id, name, label, description,
				isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865745;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label select box with options name"; //$NON-NLS-1$
		String firstItemDescription = "first item description select box with options name"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapSelectBox.addItem(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label select box with options name"; //$NON-NLS-1$
		String secondItemDescription = "second item description select box with options name"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapSelectBox.addItem(secondItem);

		tuleapTrackerConfiguration.addField(tuleapSelectBox);

		// TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		// tuleapArtifact.putValue(name, String.valueOf(firstSelectBoxItemId));
		// TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);
		//
		// // Check attributes
		// List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
		// TaskAttribute.TYPE_SINGLE_SELECT);
		// assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));
		//
		// TaskAttribute taskAttribute = attributesByType.get(0);
		// assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		// assertThat(Integer.valueOf(taskAttribute.getOptions().size()), is(Integer.valueOf(3)));
		// Set<Entry<String, String>> entrySet = taskAttribute.getOptions().entrySet();
		// for (Entry<String, String> entry : entrySet) {
		// if (String.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID).equals(entry.getKey())) {
		//				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
		// } else if (String.valueOf(firstSelectBoxItemId).equals(entry.getKey())) {
		// assertThat(entry.getValue(), is(firstItemLabel));
		// } else if (String.valueOf(secondSelectBoxItemId).equals(entry.getKey())) {
		// assertThat(entry.getValue(), is(secondItemLabel));
		// } else {
		//				fail("The entry" + entry + " should not be here"); //$NON-NLS-1$ //$NON-NLS-2$
		// }
		// }
		//
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// assertThat(taskAttribute.getValue(), is(String.valueOf(firstSelectBoxItemId)));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(label));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_SINGLE_SELECT));
		// assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
		fail("Fix the test ");
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with a tuleap select box with options.
	 */
	@Test
	public void testCreateTaskDataFromArtifactSelectBoxWithOneOption() {
		int id = 892;
		String name = "TuleapSelectBoxWithOneOptionName"; //$NON-NLS-1$
		String label = "TuleapSelectBoxWithOneOptionLabel"; //$NON-NLS-1$
		String description = "TuleapSelectBoxWithOneOptionDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = false;
		String binding = ITuleapConstants.TULEAP_STATIC_BINDING_ID;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapSelectBox tuleapSelectBox = this.createTuleapSelectBox(id, name, label, description,
				isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865745;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label select box with one option name"; //$NON-NLS-1$
		String firstItemDescription = "first item description select box with one option name"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapSelectBox.addItem(firstItem);

		tuleapTrackerConfiguration.addField(tuleapSelectBox);

		// TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		// tuleapArtifact.putValue(name, String.valueOf(firstSelectBoxItemId));
		// TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);
		//
		// // Check attributes
		// List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
		// TaskAttribute.TYPE_SINGLE_SELECT);
		// assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));
		//
		// TaskAttribute taskAttribute = attributesByType.get(0);
		// assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		// assertThat(Integer.valueOf(taskAttribute.getOptions().size()), is(Integer.valueOf(2)));
		// Set<Entry<String, String>> entrySet = taskAttribute.getOptions().entrySet();
		// for (Entry<String, String> entry : entrySet) {
		// if (String.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID).equals(entry.getKey())) {
		//				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
		// } else if (String.valueOf(firstSelectBoxItemId).equals(entry.getKey())) {
		// assertThat(entry.getValue(), is(firstItemLabel));
		// } else {
		//				fail("The entry" + entry + " should not be here"); //$NON-NLS-1$ //$NON-NLS-2$
		// }
		// }
		//
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// assertThat(taskAttribute.getValue(), is(String.valueOf(firstSelectBoxItemId)));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(label));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_SINGLE_SELECT));
		// assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
		fail("Fix the test ");
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with a tuleap select box with options and a
	 * workflow.
	 */
	@Test
	public void testCreateTaskDataFromArtifactSelectBoxWithOptionsAndWorkflow() {
		int id = 892;
		String name = "TuleapSelectBoxWithOptionsAndWorkflowName"; //$NON-NLS-1$
		String label = "TuleapSelectBoxWithOptionsAndWorkflowLabel"; //$NON-NLS-1$
		String description = "TuleapSelectBoxWithOptionsAndWorkflowDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = false;
		String binding = ITuleapConstants.TULEAP_STATIC_BINDING_ID;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapSelectBox tuleapSelectBox = this.createTuleapSelectBox(id, name, label, description,
				isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865745;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label select box with options and a workflow name"; //$NON-NLS-1$
		String firstItemDescription = "first item description select box with options and a workflow name"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapSelectBox.addItem(firstItem);

		int secondSelectBoxItemId = 84865747;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label select box with options and a workflow name"; //$NON-NLS-1$
		String secondItemDescription = "second item description select box with options and a workflow name"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapSelectBox.addItem(secondItem);

		int thirdSelectBoxItemId = 84865748;
		TuleapSelectBoxItem thirdItem = new TuleapSelectBoxItem(thirdSelectBoxItemId);
		String thirdItemLabel = "third item label select box with options and a workflow name"; //$NON-NLS-1$
		String thirdItemDescription = "third item description select box with options and a workflow name"; //$NON-NLS-1$
		thirdItem.setLabel(thirdItemLabel);
		thirdItem.setDescription(thirdItemDescription);
		tuleapSelectBox.addItem(thirdItem);

		TuleapWorkflow tuleapWorkflow = tuleapSelectBox.getWorkflow();
		TuleapWorkflowTransition aTransition = new TuleapWorkflowTransition();
		aTransition.setFrom(firstSelectBoxItemId);
		aTransition.setTo(thirdSelectBoxItemId);
		tuleapWorkflow.addTransition(aTransition);

		tuleapTrackerConfiguration.addField(tuleapSelectBox);

		// TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		// tuleapArtifact.putValue(name, String.valueOf(firstSelectBoxItemId));
		// TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);
		//
		// // Check attributes
		// List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
		// TaskAttribute.TYPE_SINGLE_SELECT);
		// assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));
		//
		// TaskAttribute taskAttribute = attributesByType.get(0);
		//
		// assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		// assertThat(Integer.valueOf(taskAttribute.getOptions().size()), is(Integer.valueOf(2)));
		// Set<Entry<String, String>> entrySet = taskAttribute.getOptions().entrySet();
		// for (Entry<String, String> entry : entrySet) {
		// if (firstItemLabel.equals(entry.getKey())) {
		// assertThat(entry.getValue(), is(firstItemLabel));
		// } else if (thirdItemLabel.equals(entry.getKey())) {
		// assertThat(entry.getValue(), is(thirdItemLabel));
		// }
		// }
		//
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// assertThat(taskAttribute.getValue(), is(String.valueOf(firstSelectBoxItemId)));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(label));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_SINGLE_SELECT));
		// assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
		fail("Fix the test ");
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with a tuleap select box status with options.
	 */
	@Test
	public void testCreateTaskDataFromArtifactSelectBoxStatusWithOptions() {
		int id = 892;
		String name = "TuleapSelectBoxStatusWithOptionsName"; //$NON-NLS-1$
		String label = "TuleapSelectBoxStatusWithOptionsLabel"; //$NON-NLS-1$
		String description = "TuleapSelectBoxStatusWithOptionsDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = false;
		String binding = ITuleapConstants.TULEAP_DYNAMIC_BINDING_USERS;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapSelectBox tuleapSelectBox = this.createTuleapSelectBox(id, name, label, description,
				isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865745;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label select box status with options name"; //$NON-NLS-1$
		String firstItemDescription = "first item description select box status with options name"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapSelectBox.addItem(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label select box status with options name"; //$NON-NLS-1$
		String secondItemDescription = "second item description select box status with options name"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapSelectBox.addItem(secondItem);

		tuleapSelectBox.getOpenStatus().add(firstItem);

		tuleapTrackerConfiguration.addField(tuleapSelectBox);

		// TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		// tuleapArtifact.putValue(name, String.valueOf(firstSelectBoxItemId));
		// TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);
		//
		// // Check attributes
		// List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
		// TaskAttribute.TYPE_SINGLE_SELECT);
		// assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));
		//
		// TaskAttribute taskAttribute = attributesByType.get(0);
		// assertThat(taskAttribute.getId(), is(TaskAttribute.STATUS));
		// assertThat(Integer.valueOf(taskAttribute.getOptions().size()), is(Integer.valueOf(3)));
		// Set<Entry<String, String>> entrySet = taskAttribute.getOptions().entrySet();
		// for (Entry<String, String> entry : entrySet) {
		// if (String.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID).equals(entry.getKey())) {
		//				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
		// } else if (String.valueOf(firstSelectBoxItemId).equals(entry.getKey())) {
		// assertThat(entry.getValue(), is(firstItemLabel));
		// } else if (String.valueOf(secondSelectBoxItemId).equals(entry.getKey())) {
		// assertThat(entry.getValue(), is(secondItemLabel));
		// } else {
		//				fail("The entry" + entry + " should not be here"); //$NON-NLS-1$ //$NON-NLS-2$
		// }
		// }
		//
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// assertThat(taskAttribute.getValue(), is(String.valueOf(firstSelectBoxItemId)));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(STATUS_LABEL));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_SINGLE_SELECT));
		fail("Fix the test ");
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with a tuleap select box semantic contributor
	 * with options.
	 */
	@Test
	public void testCreateTaskDataFromArtifactSelectBoxSemanticContributorsWithOptions() {
		int id = 892;
		String name = "TuleapSelectBoxSemanticContributorsWithOptionsName"; //$NON-NLS-1$
		String label = "TuleapSelectBoxSemanticContributorsWithOptionsLabel"; //$NON-NLS-1$
		String description = "TuleapSelectBoxSemanticContributorsWithOptionsDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = true;
		String binding = ITuleapConstants.TULEAP_DYNAMIC_BINDING_USERS;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapSelectBox tuleapSelectBox = this.createTuleapSelectBox(id, name, label, description,
				isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865745;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label select box semantic contributors with options name"; //$NON-NLS-1$
		String firstItemDescription = "first item description select box semantic contributors with options name"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapSelectBox.addItem(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label select box semantic contributors with options name"; //$NON-NLS-1$
		String secondItemDescription = "second item description select box semantic contributors with options name"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapSelectBox.addItem(secondItem);

		tuleapTrackerConfiguration.addField(tuleapSelectBox);

		// TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		// tuleapArtifact.putValue(name, String.valueOf(firstSelectBoxItemId));
		// TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);
		//
		// // Check attributes
		// List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
		// TaskAttribute.TYPE_SINGLE_SELECT);
		// assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));
		//
		// TaskAttribute taskAttribute = attributesByType.get(0);
		// assertThat(taskAttribute.getId(), is(TaskAttribute.USER_ASSIGNED));
		// assertThat(Integer.valueOf(taskAttribute.getOptions().size()), is(Integer.valueOf(3)));
		// Set<Entry<String, String>> entrySet = taskAttribute.getOptions().entrySet();
		// for (Entry<String, String> entry : entrySet) {
		// if (String.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID).equals(entry.getKey())) {
		//				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
		// } else if (String.valueOf(firstSelectBoxItemId).equals(entry.getKey())) {
		// assertThat(entry.getValue(), is(firstItemLabel));
		// } else if (String.valueOf(secondSelectBoxItemId).equals(entry.getKey())) {
		// assertThat(entry.getValue(), is(secondItemLabel));
		// } else {
		//				fail("The entry" + entry + " should not be here"); //$NON-NLS-1$ //$NON-NLS-2$
		// }
		// }
		//
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// assertThat(taskAttribute.getValue(), is(String.valueOf(firstSelectBoxItemId)));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(CONTRIBUTORS_LABEL));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_SINGLE_SELECT));
		// assertThat(metaData.getKind(), is(TaskAttribute.KIND_PEOPLE));
		fail("Fix the test ");
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with a tuleap multi select box with options.
	 */
	@Test
	public void testCreateTaskDataFromArtifactMultiSelectBoxWithOptions() {
		int id = 892;
		String name = "TuleapMultiSelectBoxWithOptionsName"; //$NON-NLS-1$
		String label = "TuleapMukltiSelectBoxWithOptionsLabel"; //$NON-NLS-1$
		String description = "TuleapMultiSelectBoxWithOptionsDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = false;
		String binding = ITuleapConstants.TULEAP_STATIC_BINDING_ID;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapMultiSelectBox tuleapMultiSelectBox = this.createTuleapMultiSelectBox(id, name, label,
				description, isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865745;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label select box with options"; //$NON-NLS-1$
		String firstItemDescription = "first item description select box with options"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapMultiSelectBox.addItem(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label select box with options"; //$NON-NLS-1$
		String secondItemDescription = "second item description select box with options"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapMultiSelectBox.addItem(secondItem);

		tuleapTrackerConfiguration.addField(tuleapMultiSelectBox);

		// TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		// tuleapArtifact.putValue(name, String.valueOf(firstSelectBoxItemId));
		// TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);
		//
		// // Check attributes
		// List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
		// TaskAttribute.TYPE_MULTI_SELECT);
		// assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));
		//
		// TaskAttribute taskAttribute = attributesByType.get(0);
		// assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		// assertThat(Integer.valueOf(taskAttribute.getOptions().size()), is(Integer.valueOf(3)));
		// Set<Entry<String, String>> entrySet = taskAttribute.getOptions().entrySet();
		// for (Entry<String, String> entry : entrySet) {
		// if (String.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID).equals(entry.getKey())) {
		//				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
		// } else if (String.valueOf(firstSelectBoxItemId).equals(entry.getKey())) {
		// assertThat(entry.getValue(), is(firstItemLabel));
		// } else if (String.valueOf(secondSelectBoxItemId).equals(entry.getKey())) {
		// assertThat(entry.getValue(), is(secondItemLabel));
		// } else {
		//				fail("The entry" + entry + " should not be here"); //$NON-NLS-1$ //$NON-NLS-2$
		// }
		// }
		//
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// assertThat(taskAttribute.getValue(), is(String.valueOf(firstSelectBoxItemId)));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(label));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_MULTI_SELECT));
		// assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
		fail("Fix the test ");
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with a tuleap multi select box with options
	 * and workflow.
	 */
	@Test
	public void testCreateTaskDataFromArtifactMultiSelectBoxWithOptionsAndWorkflow() {
		int id = 892;
		String name = "TuleapMultiSelectBoxWithOptionsAndWorkflowName"; //$NON-NLS-1$
		String label = "TuleapMukltiSelectBoxWithOptionsAndWorkflowLabel"; //$NON-NLS-1$
		String description = "TuleapMultiSelectBoxWithOptionsAndWorkflowDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = false;
		String binding = ITuleapConstants.TULEAP_STATIC_BINDING_ID;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapMultiSelectBox tuleapMultiSelectBox = this.createTuleapMultiSelectBox(id, name, label,
				description, isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865745;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label select box with options and workflow"; //$NON-NLS-1$
		String firstItemDescription = "first item description select box with options and workflow"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapMultiSelectBox.addItem(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label select box with options and workflow"; //$NON-NLS-1$
		String secondItemDescription = "second item description select box with options and workflow"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapMultiSelectBox.addItem(secondItem);

		int thirdSelectBoxItemId = 84865748;
		TuleapSelectBoxItem thirdItem = new TuleapSelectBoxItem(thirdSelectBoxItemId);
		String thirdItemLabel = "third item label select box with options and a workflow name"; //$NON-NLS-1$
		String thirdItemDescription = "third item description select box with options and a workflow name"; //$NON-NLS-1$
		thirdItem.setLabel(thirdItemLabel);
		thirdItem.setDescription(thirdItemDescription);
		tuleapMultiSelectBox.addItem(thirdItem);

		tuleapTrackerConfiguration.addField(tuleapMultiSelectBox);

		// TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		// tuleapArtifact.putValue(name, String.valueOf(firstSelectBoxItemId));
		// TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);
		//
		// // Check attributes
		// List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
		// TaskAttribute.TYPE_MULTI_SELECT);
		// assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));
		//
		// TaskAttribute taskAttribute = attributesByType.get(0);
		// assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		// assertThat(Integer.valueOf(taskAttribute.getOptions().size()), is(Integer.valueOf(4)));
		// Set<Entry<String, String>> entrySet = taskAttribute.getOptions().entrySet();
		// for (Entry<String, String> entry : entrySet) {
		// if (String.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID).equals(entry.getKey())) {
		//				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
		// } else if (String.valueOf(firstSelectBoxItemId).equals(entry.getKey())) {
		// assertThat(entry.getValue(), is(firstItemLabel));
		// } else if (String.valueOf(secondSelectBoxItemId).equals(entry.getKey())) {
		// assertThat(entry.getValue(), is(secondItemLabel));
		// } else if (String.valueOf(thirdSelectBoxItemId).equals(entry.getKey())) {
		// assertThat(entry.getValue(), is(thirdItemLabel));
		// } else {
		//				fail("The entry " + entry + "should not be here"); //$NON-NLS-1$ //$NON-NLS-2$
		// }
		// }
		//
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// assertThat(taskAttribute.getValue(), is(String.valueOf(firstSelectBoxItemId)));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(label));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_MULTI_SELECT));
		// assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
		fail("Fix the test ");
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with a tuleap multi select box with options
	 * as a status.
	 */
	@Test
	public void testCreateTaskDataFromArtifactMultiSelectBoxStatusWithOptions() {
		int id = 892;
		String name = "TuleapMultiSelectBoxStatusWithOptionsName"; //$NON-NLS-1$
		String label = "TuleapMukltiSelectBoxStatusWithOptionsLabel"; //$NON-NLS-1$
		String description = "TuleapMultiSelectBoxStatusWithOptionsDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = false;
		String binding = ITuleapConstants.TULEAP_STATIC_BINDING_ID;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapMultiSelectBox tuleapMultiSelectBox = this.createTuleapMultiSelectBox(id, name, label,
				description, isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865745;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label"; //$NON-NLS-1$
		String firstItemDescription = "first item description"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapMultiSelectBox.addItem(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label"; //$NON-NLS-1$
		String secondItemDescription = "second item description"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapMultiSelectBox.addItem(secondItem);

		tuleapMultiSelectBox.getOpenStatus().add(firstItem);

		tuleapTrackerConfiguration.addField(tuleapMultiSelectBox);

		// TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		//
		// // TODO Check this is relevant : shouldn't we pass the id of the element?
		// tuleapArtifact.putValue(name, String.valueOf(firstSelectBoxItemId));
		// TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);
		//
		// // Check attributes
		// List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
		// TaskAttribute.TYPE_MULTI_SELECT);
		// assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));
		//
		// TaskAttribute taskAttribute = attributesByType.get(0);
		//
		// assertThat(taskAttribute.getId(), is(TaskAttribute.STATUS));
		// assertThat(Integer.valueOf(taskAttribute.getOptions().size()), is(Integer.valueOf(3)));
		// Set<Entry<String, String>> entrySet = taskAttribute.getOptions().entrySet();
		// for (Entry<String, String> entry : entrySet) {
		// if (String.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID).equals(entry.getKey())) {
		//				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
		// } else if (String.valueOf(firstSelectBoxItemId).equals(entry.getKey())) {
		// assertThat(entry.getValue(), is(firstItemLabel));
		// } else if (String.valueOf(secondSelectBoxItemId).equals(entry.getKey())) {
		// assertThat(entry.getValue(), is(secondItemLabel));
		// } else {
		//				fail("The entry" + entry + " should not be here"); //$NON-NLS-1$ //$NON-NLS-2$
		// }
		// }
		//
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// assertThat(taskAttribute.getValue(), is(String.valueOf(firstSelectBoxItemId)));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(STATUS_LABEL));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_MULTI_SELECT));
		fail("Fix the test ");
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with a tuleap multi select box with options.
	 */
	@Test
	public void testCreateTaskDataFromArtifactMultiSelectBoxSemanticContributorsWithOptions() {
		int id = 892;
		String name = "TuleapMultiSelectBoxSemanticContributorsWithOptionsName"; //$NON-NLS-1$
		String label = "TuleapMultiSelectBoxSemanticContributorsWithOptionsLabel"; //$NON-NLS-1$
		String description = "TuleapMultiSelectBoxSemanticContributorsWithOptionsDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = true;
		String binding = ITuleapConstants.TULEAP_DYNAMIC_BINDING_USERS;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapMultiSelectBox tuleapMultiSelectBox = this.createTuleapMultiSelectBox(id, name, label,
				description, isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865745;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label"; //$NON-NLS-1$
		String firstItemDescription = "first item description"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapMultiSelectBox.addItem(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label"; //$NON-NLS-1$
		String secondItemDescription = "second item description"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapMultiSelectBox.addItem(secondItem);

		tuleapTrackerConfiguration.addField(tuleapMultiSelectBox);

		// TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		// tuleapArtifact.putValue(name, String.valueOf(firstSelectBoxItemId));
		// TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);
		//
		// // Check attributes
		// List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
		// TaskAttribute.TYPE_MULTI_SELECT);
		// assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));
		//
		// TaskAttribute taskAttribute = attributesByType.get(0);
		// assertThat(taskAttribute.getId(), is(TaskAttribute.USER_ASSIGNED));
		// assertThat(Integer.valueOf(taskAttribute.getOptions().size()), is(Integer.valueOf(3)));
		// Set<Entry<String, String>> entrySet = taskAttribute.getOptions().entrySet();
		// for (Entry<String, String> entry : entrySet) {
		// if (String.valueOf(ITuleapConstants.CONFIGURABLE_FIELD_NONE_BINDING_ID).equals(entry.getKey())) {
		//				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
		// } else if (String.valueOf(firstSelectBoxItemId).equals(entry.getKey())) {
		// assertThat(entry.getValue(), is(firstItemLabel));
		// } else if (String.valueOf(secondSelectBoxItemId).equals(entry.getKey())) {
		// assertThat(entry.getValue(), is(secondItemLabel));
		// } else {
		//				fail("The entry" + entry + " should not be here"); //$NON-NLS-1$ //$NON-NLS-2$
		// }
		// }
		//
		// assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		// assertThat(taskAttribute.getValue(), is(String.valueOf(firstSelectBoxItemId)));
		//
		// TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		// assertThat(metaData.getLabel(), is(CONTRIBUTORS_LABEL));
		// assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.FALSE));
		// assertThat(metaData.getType(), is(TaskAttribute.TYPE_MULTI_SELECT));
		// assertThat(metaData.getKind(), is(TaskAttribute.KIND_PEOPLE));
		fail("Fix the test ");
	}

	/**
	 * Test the creation of a Tuleap artifact from a task data containing a string.
	 */
	@Test
	public void testGetTuleapArtifactFromTaskDataString() {
		TuleapAttributeMapper mapper = new TuleapAttributeMapper(repository, this.repositoryConnector);
		String taskId = "485"; //$NON-NLS-1$
		TaskData taskData = new TaskData(mapper, connectorKind, repositoryUrl, taskId);
		String attributeId = "attributeId"; //$NON-NLS-1$
		TaskAttribute taskAttribute = taskData.getRoot().createAttribute(attributeId);
		String stringValue = "A string Value"; //$NON-NLS-1$
		taskAttribute.setValue(stringValue);

		// TuleapArtifact tuleapArtifact = TuleapTaskDataHandler.getTuleapArtifact(repository, taskData);
		// assertThat(tuleapArtifact, notNullValue());
		// assertThat(tuleapArtifact.getValue(attributeId), is(stringValue));
		fail("Fix the test ");
	}

	/**
	 * Test the creation of a Tuleap artifact from a task data containing an artifact link.
	 */
	@Test
	public void testGetTuleapArtifactFromTaskDataArtifactLink() {
		TuleapAttributeMapper mapper = new TuleapAttributeMapper(repository, this.repositoryConnector);
		String taskId = "218"; //$NON-NLS-1$
		TaskData taskData = new TaskData(mapper, connectorKind, repositoryUrl, taskId);
		String attributeId = "attributeId"; //$NON-NLS-1$
		TaskAttribute taskAttribute = taskData.getRoot().createAttribute(attributeId);
		String artifactLinkValue = "917"; //$NON-NLS-1$
		taskAttribute.setValue(artifactLinkValue);
		taskAttribute.getMetaData().setType(TaskAttribute.TYPE_TASK_DEPENDENCY);

		// TuleapArtifact tuleapArtifact = TuleapTaskDataHandler.getTuleapArtifact(repository, taskData);
		// assertThat(tuleapArtifact, notNullValue());
		// assertThat(tuleapArtifact.getValue(attributeId), is(artifactLinkValue));
		//
		// // Artifact id from the code completion
		// taskData = new TaskData(mapper, connectorKind, repositoryUrl, taskId);
		// taskAttribute = taskData.getRoot().createAttribute(attributeId);
		//		artifactLinkValue = "917"; //$NON-NLS-1$
		//		String shortArtifactLinkValue = "917"; //$NON-NLS-1$
		// taskAttribute.setValue(artifactLinkValue);
		// taskAttribute.getMetaData().setType(TaskAttribute.TYPE_TASK_DEPENDENCY);
		//
		// tuleapArtifact = TuleapTaskDataHandler.getTuleapArtifact(repository, taskData);
		// assertThat(tuleapArtifact, notNullValue());
		// assertThat(tuleapArtifact.getValue(attributeId), is(shortArtifactLinkValue));
		fail("Fix the test ");
	}
}
