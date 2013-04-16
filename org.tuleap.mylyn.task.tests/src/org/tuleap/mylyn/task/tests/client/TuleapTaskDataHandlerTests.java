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
package org.tuleap.mylyn.task.tests.client;

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
import org.eclipse.mylyn.tasks.core.TaskMapping;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMetaData;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.internal.core.model.TuleapArtifact;
import org.tuleap.mylyn.task.internal.core.model.TuleapInstanceConfiguration;
import org.tuleap.mylyn.task.internal.core.model.TuleapTrackerConfiguration;
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
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.repository.TuleapAttributeMapper;
import org.tuleap.mylyn.task.internal.core.repository.TuleapTaskDataHandler;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.tests.mocks.MockedTuleapClient;
import org.tuleap.mylyn.task.tests.mocks.MockedTuleapRepositoryConnector;

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
	private static final String CONTRIBUTORS_LABEL = "Contributors"; //$NON-NLS-1$

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
	private TaskMapping taskMapping;

	/**
	 * The name of the configuration.
	 */
	private String trackerName;

	/**
	 * The configuration of the tuleap instance.
	 */
	private TuleapInstanceConfiguration tuleapInstanceConfiguration;

	/**
	 * The name of the items.
	 */
	private String itemName;

	/**
	 * The name of the project.
	 */
	private String projectName = "Project Name"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
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

		this.tuleapInstanceConfiguration = new TuleapInstanceConfiguration(repositoryUrl);
		tuleapInstanceConfiguration.addTracker(Integer.valueOf(trackerId), tuleapTrackerConfiguration);
		this.repositoryConnector = new MockedTuleapRepositoryConnector(tuleapInstanceConfiguration);

		// Used to specify the tracker to use in the group
		this.taskMapping = new TaskMapping() {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.mylyn.tasks.core.TaskMapping#getProduct()
			 */
			@Override
			public String getProduct() {
				return Integer.valueOf(trackerId).toString();
			}
		};

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
		tuleapTrackerConfiguration.getFields().add(tuleapString);

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
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SHORT_RICH_TEXT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a read only tuleap string.
	 */
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
		tuleapTrackerConfiguration.getFields().add(tuleapString);

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
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(true)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SHORT_RICH_TEXT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap string as a title.
	 */
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
		tuleapTrackerConfiguration.getFields().add(tuleapString);

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
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SHORT_RICH_TEXT));
	}

	/**
	 * Test the initialization of the task data with a tuleap text.
	 */
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
		tuleapTrackerConfiguration.getFields().add(tuleapText);

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
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_LONG_RICH_TEXT));
				assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
			} else if (taskAttribute.getId().equals(TaskAttribute.COMMENT_NEW)) {
				assertThat(taskAttribute.getId(), is(TaskAttribute.COMMENT_NEW));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_LONG_RICH_TEXT));
			}
		}
	}

	/**
	 * Test the initialization of the task data with a read only tuleap text.
	 */
	public void testInitializeTaskDataTextReadOnly() {
		int id = 892;
		String name = "TuleapTextName"; //$NON-NLS-1$
		String label = "TuleapTextLabel"; //$NON-NLS-1$
		String description = "TuleapTextDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ, };

		TuleapText tuleapText = this.createTuleapText(id, name, label, description, isRequired, rank,
				permissions);
		tuleapTrackerConfiguration.getFields().add(tuleapText);

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
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(true)));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_LONG_RICH_TEXT));
				assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
			} else if (taskAttribute.getId().equals(TaskAttribute.COMMENT_NEW)) {
				assertThat(taskAttribute.getId(), is(TaskAttribute.COMMENT_NEW));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_LONG_RICH_TEXT));
			}
		}
	}

	/**
	 * Test the initialization of the task data with a tuleap integer.
	 */
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
		tuleapTrackerConfiguration.getFields().add(tuleapInteger);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_INTEGER);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		assertThat(taskAttribute.getValue(), is("0")); //$NON-NLS-1$

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_INTEGER));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a read only tuleap integer.
	 */
	public void testInitializeTaskDataIntegerReadOnly() {
		int id = 892;
		String name = "TuleapIntegerName"; //$NON-NLS-1$
		String label = "TuleapIntegerLabel"; //$NON-NLS-1$
		String description = "TuleapIntegerDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ, };

		TuleapInteger tuleapInteger = this.createTuleapInteger(id, name, label, description, isRequired,
				rank, permissions);
		tuleapTrackerConfiguration.getFields().add(tuleapInteger);

		TaskData taskData = this.initialize();

		// Check attributes
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_INTEGER);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		assertThat(taskAttribute.getValue(), is("0")); //$NON-NLS-1$

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(true)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_INTEGER));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap float.
	 */
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
		tuleapTrackerConfiguration.getFields().add(tuleapFloat);

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
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_DOUBLE));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a read only tuleap float.
	 */
	public void testInitializeTaskDataFloatReadOnly() {
		int id = 892;
		String name = "TuleapFloatName"; //$NON-NLS-1$
		String label = "TuleapFloatLabel"; //$NON-NLS-1$
		String description = "TuleapFloatDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ, };

		TuleapFloat tuleapFloat = this.createTuleapFloat(id, name, label, description, isRequired, rank,
				permissions);
		tuleapTrackerConfiguration.getFields().add(tuleapFloat);

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
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(true)));
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
		tuleapTrackerConfiguration.getFields().add(tuleapArtifactLink);

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
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_TASK_DEPENDENCY));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a read only tuleap artifact link.
	 */
	public void testInitializeTaskDataArtifactLinkReadOnly() {
		int id = 892;
		String name = "TuleapArtifactLinkName"; //$NON-NLS-1$
		String label = "TuleapArtifactLinkLabel"; //$NON-NLS-1$
		String description = "TuleapArtifactLinkDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ, };

		TuleapArtifactLink tuleapArtifactLink = this.createTuleapArtifactLink(id, name, label, description,
				isRequired, rank, permissions);
		tuleapTrackerConfiguration.getFields().add(tuleapArtifactLink);

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
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(true)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_TASK_DEPENDENCY));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap date.
	 */
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
		tuleapTrackerConfiguration.getFields().add(tuleapDate);

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
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
				assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
			} else if (taskAttribute.getId().equals(TaskAttribute.DATE_COMPLETION)) {
				assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_COMPLETION));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(metaData.getLabel(), is(COMPLETED_DATE_LABEL));
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
			} else if (taskAttribute.getId().equals(TaskAttribute.DATE_CREATION)) {
				assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_CREATION));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(metaData.getLabel(), is(CREATED_DATE_LABEL));
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
			} else if (taskAttribute.getId().equals(TaskAttribute.DATE_MODIFICATION)) {
				assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_MODIFICATION));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(metaData.getLabel(), is(MODIFIED_DATE_LABEL));
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
			}
		}
	}

	/**
	 * Test the initialization of the task data with a read only tuleap date.
	 */
	public void testInitializeTaskDataDateReadOnly() {
		int id = 892;
		String name = "TuleapDateName"; //$NON-NLS-1$
		String label = "TuleapDateLabel"; //$NON-NLS-1$
		String description = "TuleapDateDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ, };

		TuleapDate tuleapDate = this.createTuleapDate(id, name, label, description, isRequired, rank,
				permissions);
		tuleapTrackerConfiguration.getFields().add(tuleapDate);

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
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(true)));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
				assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
			} else if (taskAttribute.getId().equals(TaskAttribute.DATE_COMPLETION)) {
				assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_COMPLETION));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(metaData.getLabel(), is(COMPLETED_DATE_LABEL));
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
			} else if (taskAttribute.getId().equals(TaskAttribute.DATE_CREATION)) {
				assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_CREATION));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(metaData.getLabel(), is(CREATED_DATE_LABEL));
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
			} else if (taskAttribute.getId().equals(TaskAttribute.DATE_MODIFICATION)) {
				assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_MODIFICATION));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(metaData.getLabel(), is(MODIFIED_DATE_LABEL));
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
			}
		}
	}

	/**
	 * Test the initialization of the task data with a tuleap file upload.
	 */
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
		tuleapTrackerConfiguration.getFields().add(tuleapFileUpload);

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
		tuleapTrackerConfiguration.getFields().add(tuleapOpenList);

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
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SHORT_RICH_TEXT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a read only tuleap open list.
	 */
	public void testInitializeTaskDataOpenListReadOnly() {
		int id = 892;
		String name = "TuleapOpenListName"; //$NON-NLS-1$
		String label = "TuleapOpenListLabel"; //$NON-NLS-1$
		String description = "TuleapOpenListDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ, };

		TuleapOpenList tuleapOpenList = this.createTuleapOpenList(id, name, label, description, isRequired,
				rank, permissions);
		tuleapTrackerConfiguration.getFields().add(tuleapOpenList);

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
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(true)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SHORT_RICH_TEXT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap select box.
	 */
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
		tuleapTrackerConfiguration.getFields().add(tuleapSelectBox);

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
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SINGLE_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a readonly tuleap select box.
	 */
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
		tuleapTrackerConfiguration.getFields().add(tuleapSelectBox);

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
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(true)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SINGLE_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap select box with options.
	 */
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

		int firstSelectBoxItemId = 84865746;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label select box with options name"; //$NON-NLS-1$
		String firstItemDescription = "first item description select box with options name"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapSelectBox.getItems().add(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label select box with options name"; //$NON-NLS-1$
		String secondItemDescription = "second item description select box with options name"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapSelectBox.getItems().add(secondItem);

		tuleapTrackerConfiguration.getFields().add(tuleapSelectBox);

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
			if ("".equals(entry.getKey())) { //$NON-NLS-1$
				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
			} else if (firstItemLabel.equals(entry.getKey())) {
				assertThat(entry.getValue(), is(firstItemLabel));
			} else if (secondItemLabel.equals(entry.getKey())) {
				assertThat(entry.getValue(), is(secondItemLabel));
			}
		}

		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SINGLE_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap select box with options.
	 */
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

		int firstSelectBoxItemId = 84865746;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label select box semantic contributors with options name"; //$NON-NLS-1$
		String firstItemDescription = "first item description select box semantic contributors with options name"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapSelectBox.getItems().add(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label select box semantic contributors with options name"; //$NON-NLS-1$
		String secondItemDescription = "second item description select box semantic contributors with options name"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapSelectBox.getItems().add(secondItem);

		tuleapSelectBox.getOpenStatus().add(firstItem);

		tuleapTrackerConfiguration.getFields().add(tuleapSelectBox);

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
			if ("".equals(entry.getKey())) { //$NON-NLS-1$
				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
			} else if (firstItemLabel.equals(entry.getKey())) {
				assertThat(entry.getValue(), is(firstItemLabel));
			} else if (secondItemLabel.equals(entry.getKey())) {
				assertThat(entry.getValue(), is(secondItemLabel));
			}
		}

		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(STATUS_LABEL));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SINGLE_SELECT));
	}

	/**
	 * Test the initialization of the task data with a tuleap select box.
	 */
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
		tuleapTrackerConfiguration.getFields().add(tuleapSelectBox);

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
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SINGLE_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_PEOPLE));
	}

	/**
	 * Test the initialization of the task data with a tuleap select box with options.
	 */
	public void testInitializeTaskDataSelectBoxSemanticContributorsWithOptions() {
		int id = 892;
		String name = "TuleapSelectBoxWithOptionsName"; //$NON-NLS-1$
		String label = "TuleapSelectBoxWithOptionsLabel"; //$NON-NLS-1$
		String description = "TuleapSelectBoxWithOptionsDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = true;
		String binding = ITuleapConstants.TULEAP_DYNAMIC_BINDING_USERS;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapSelectBox tuleapSelectBox = this.createTuleapSelectBox(id, name, label, description,
				isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865746;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label select box semantic contributors with options name"; //$NON-NLS-1$
		String firstItemDescription = "first item description select box semantic contributors with options name"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapSelectBox.getItems().add(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label select box semantic contributors with options name"; //$NON-NLS-1$
		String secondItemDescription = "second item description select box semantic contributors with options name"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapSelectBox.getItems().add(secondItem);

		tuleapTrackerConfiguration.getFields().add(tuleapSelectBox);

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
			if ("".equals(entry.getKey())) { //$NON-NLS-1$
				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
			} else if (firstItemLabel.equals(entry.getKey())) {
				assertThat(entry.getValue(), is(firstItemLabel));
			} else if (secondItemLabel.equals(entry.getKey())) {
				assertThat(entry.getValue(), is(secondItemLabel));
			}
		}

		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(CONTRIBUTORS_LABEL));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SINGLE_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_PEOPLE));
	}

	/**
	 * Test the initialization of the task data with a tuleap multi select box.
	 */
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
		tuleapTrackerConfiguration.getFields().add(tuleapMultiSelectBox);

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
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_MULTI_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a read only tuleap multi select box.
	 */
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
		tuleapTrackerConfiguration.getFields().add(tuleapMultiSelectBox);

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
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(true)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_MULTI_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap multi select box with options.
	 */
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

		int firstSelectBoxItemId = 84865746;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label select box with options"; //$NON-NLS-1$
		String firstItemDescription = "first item description select box with options"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapMultiSelectBox.getItems().add(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label select box with options"; //$NON-NLS-1$
		String secondItemDescription = "second item description select box with options"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapMultiSelectBox.getItems().add(secondItem);

		tuleapTrackerConfiguration.getFields().add(tuleapMultiSelectBox);

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
			if ("".equals(entry.getKey())) { //$NON-NLS-1$
				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
			} else if (firstItemLabel.equals(entry.getKey())) {
				assertThat(entry.getValue(), is(firstItemLabel));
			} else if (secondItemLabel.equals(entry.getKey())) {
				assertThat(entry.getValue(), is(secondItemLabel));
			}
		}

		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_MULTI_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the initialization of the task data with a tuleap multi select box with options as a status.
	 */
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

		int firstSelectBoxItemId = 84865746;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label"; //$NON-NLS-1$
		String firstItemDescription = "first item description"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapMultiSelectBox.getItems().add(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label"; //$NON-NLS-1$
		String secondItemDescription = "second item description"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapMultiSelectBox.getItems().add(secondItem);

		tuleapMultiSelectBox.getOpenStatus().add(firstItem);

		tuleapTrackerConfiguration.getFields().add(tuleapMultiSelectBox);

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
			if ("".equals(entry.getKey())) { //$NON-NLS-1$
				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
			} else if (firstItemLabel.equals(entry.getKey())) {
				assertThat(entry.getValue(), is(firstItemLabel));
			} else if (secondItemLabel.equals(entry.getKey())) {
				assertThat(entry.getValue(), is(secondItemLabel));
			}
		}

		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(STATUS_LABEL));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_MULTI_SELECT));
	}

	/**
	 * Test the initialization of the task data with a tuleap multi select box.
	 */
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
		tuleapTrackerConfiguration.getFields().add(tuleapMultiSelectBox);

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
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_MULTI_SELECT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_PEOPLE));
	}

	/**
	 * Test the initialization of the task data with a tuleap multi select box with options.
	 */
	public void testInitializeTaskDataMultiSelectBoxSemanticContributorsWithOptions() {
		int id = 892;
		String name = "TuleapMultiSelectBoxWithOptionsName"; //$NON-NLS-1$
		String label = "TuleapMultiSelectBoxWithOptionsLabel"; //$NON-NLS-1$
		String description = "TuleapMultiSelectBoxWithOptionsDescription"; //$NON-NLS-1$
		boolean isRequired = false;
		boolean isSemanticContributor = true;
		String binding = ITuleapConstants.TULEAP_DYNAMIC_BINDING_USERS;
		int rank = 0;
		String[] permissions = new String[] {ITuleapConstants.PERMISSION_READ,
				ITuleapConstants.PERMISSION_SUBMIT, ITuleapConstants.PERMISSION_UPDATE, };

		TuleapMultiSelectBox tuleapMultiSelectBox = this.createTuleapMultiSelectBox(id, name, label,
				description, isRequired, isSemanticContributor, binding, rank, permissions);

		int firstSelectBoxItemId = 84865746;
		TuleapSelectBoxItem firstItem = new TuleapSelectBoxItem(firstSelectBoxItemId);
		String firstItemLabel = "first item label"; //$NON-NLS-1$
		String firstItemDescription = "first item description"; //$NON-NLS-1$
		firstItem.setLabel(firstItemLabel);
		firstItem.setDescription(firstItemDescription);
		tuleapMultiSelectBox.getItems().add(firstItem);

		int secondSelectBoxItemId = 84865746;
		TuleapSelectBoxItem secondItem = new TuleapSelectBoxItem(secondSelectBoxItemId);
		String secondItemLabel = "second item label"; //$NON-NLS-1$
		String secondItemDescription = "second item description"; //$NON-NLS-1$
		secondItem.setLabel(secondItemLabel);
		secondItem.setDescription(secondItemDescription);
		tuleapMultiSelectBox.getItems().add(secondItem);

		tuleapTrackerConfiguration.getFields().add(tuleapMultiSelectBox);

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
			if ("".equals(entry.getKey())) { //$NON-NLS-1$
				assertThat(entry.getValue(), is("")); //$NON-NLS-1$
			} else if (firstItemLabel.equals(entry.getKey())) {
				assertThat(entry.getValue(), is(firstItemLabel));
			} else if (secondItemLabel.equals(entry.getKey())) {
				assertThat(entry.getValue(), is(secondItemLabel));
			}
		}

		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(CONTRIBUTORS_LABEL));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
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
		TuleapTaskDataHandler tuleapTaskDataHandler = new TuleapTaskDataHandler(repositoryConnector);
		MockedTuleapClient tuleapClient = new MockedTuleapClient(tuleapArtifact,
				this.tuleapInstanceConfiguration);
		return tuleapTaskDataHandler.createTaskDataFromArtifact(tuleapClient, repository, tuleapArtifact,
				new NullProgressMonitor());
	}

	/**
	 * Test the creation of the task data from an empty tuleap artifact.
	 */
	public void testCreateTaskDataFromArtifactEmpty() {
		int id = 892;
		Date creationDate = new Date();
		Date modifiedDate = new Date();

		TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		tuleapArtifact.setCreationDate(creationDate);
		tuleapArtifact.setLastModificationDate(modifiedDate);

		TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);

		// Check the url of the task
		String taskId = taskData.getTaskId();
		assertThat(taskId, is(projectName + ITuleapConstants.TRACKER_ID_SEPARATOR + this.trackerName + '['
				+ this.trackerId + "] #" + id)); //$NON-NLS-1$

		// Check the dates
		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_DATE);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(3)));
		for (TaskAttribute taskAttribute : attributesByType) {
			if (taskAttribute.getId().equals(TaskAttribute.DATE_COMPLETION)) {
				assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_COMPLETION));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(0)));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(metaData.getLabel(), is(COMPLETED_DATE_LABEL));
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
			} else if (taskAttribute.getId().equals(TaskAttribute.DATE_CREATION)) {
				assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_CREATION));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(creationDate);
				assertThat(Long.valueOf(taskAttribute.getValues().get(0)), is(Long.valueOf(calendar
						.getTimeInMillis())));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(metaData.getLabel(), is(CREATED_DATE_LABEL));
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
			} else if (taskAttribute.getId().equals(TaskAttribute.DATE_MODIFICATION)) {
				assertThat(taskAttribute.getId(), is(TaskAttribute.DATE_MODIFICATION));
				assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
				assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(modifiedDate);
				assertThat(Long.valueOf(taskAttribute.getValues().get(0)), is(Long.valueOf(calendar
						.getTimeInMillis())));

				TaskAttributeMetaData metaData = taskAttribute.getMetaData();
				assertThat(metaData.getLabel(), is(MODIFIED_DATE_LABEL));
				assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
				assertThat(metaData.getType(), is(TaskAttribute.TYPE_DATE));
			}
		}
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with a tuleap string.
	 */
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
		tuleapTrackerConfiguration.getFields().add(tuleapString);

		String stringValue = "String Value"; //$NON-NLS-1$

		TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		tuleapArtifact.putValue(name, stringValue);
		TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);

		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_SHORT_RICH_TEXT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(Integer.valueOf(id).toString()));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		assertThat(taskAttribute.getValue(), is(stringValue));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SHORT_RICH_TEXT));
		assertThat(metaData.getKind(), is(TaskAttribute.KIND_DEFAULT));
	}

	/**
	 * Test the creation of the task data from a tuleap artifact with a tuleap string as the title.
	 */
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
		tuleapTrackerConfiguration.getFields().add(tuleapString);

		String stringValue = "String Value"; //$NON-NLS-1$

		TuleapArtifact tuleapArtifact = new TuleapArtifact(id, trackerId, trackerName, projectName);
		tuleapArtifact.putValue(name, stringValue);
		TaskData taskData = this.createTaskDataFromArtifact(tuleapArtifact);

		List<TaskAttribute> attributesByType = taskData.getAttributeMapper().getAttributesByType(taskData,
				TaskAttribute.TYPE_SHORT_RICH_TEXT);
		assertThat(Integer.valueOf(attributesByType.size()), is(Integer.valueOf(1)));

		TaskAttribute taskAttribute = attributesByType.get(0);
		assertThat(taskAttribute.getId(), is(TaskAttribute.SUMMARY));
		assertThat(taskAttribute.getOptions(), is((Map<String, String>)new HashMap<String, String>()));
		assertThat(Integer.valueOf(taskAttribute.getValues().size()), is(Integer.valueOf(1)));
		assertThat(taskAttribute.getValue(), is(stringValue));

		TaskAttributeMetaData metaData = taskAttribute.getMetaData();
		assertThat(metaData.getLabel(), is(label));
		assertThat(Boolean.valueOf(metaData.isReadOnly()), is(Boolean.valueOf(false)));
		assertThat(metaData.getType(), is(TaskAttribute.TYPE_SHORT_RICH_TEXT));
	}
}
