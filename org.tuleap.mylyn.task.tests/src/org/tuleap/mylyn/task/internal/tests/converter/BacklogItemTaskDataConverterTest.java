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

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.client.TuleapClientManager;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskIdentityUtil;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapProject;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapServer;
import org.tuleap.mylyn.task.internal.core.model.data.TuleapReference;
import org.tuleap.mylyn.task.internal.core.model.data.agile.TuleapBacklogItem;
import org.tuleap.mylyn.task.internal.core.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.repository.TuleapAttributeMapper;
import org.tuleap.mylyn.task.internal.core.repository.TuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;

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
	private TuleapReference projectRef;

	/**
	 * The repository connector.
	 */
	private ITuleapRepositoryConnector repositoryConnector;

	/**
	 * The configuration of the tuleap instance.
	 */
	private TuleapServer tuleapServer;

	/**
	 * The configuration of the tuleap project.
	 */
	private TuleapProject tuleapProject;

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

		this.projectRef = new TuleapReference(987, "p/987");

		this.repository = new TaskRepository(connectorKind, repositoryUrl);

		this.tuleapServer = new TuleapServer(repositoryUrl);

		this.tuleapProject = new TuleapProject(projectName, projectRef.getId());

		item200 = new TuleapBacklogItem(200, projectRef, "item200", "URL", "HTML URL", null, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		tuleapServer.addProject(tuleapProject);

		final TuleapClientManager clientManager = new TuleapClientManager();
		repositoryConnector = new TuleapRepositoryConnector() {
			@Override
			public TuleapServer getTuleapServerConfiguration(String pRepositoryUrl) {
				return tuleapServer;
			}

			@Override
			public TuleapClientManager getClientManager() {
				return clientManager;
			}
		};

		this.attributeMapper = new TuleapAttributeMapper(repository, repositoryConnector);
		this.taskData = new TaskData(attributeMapper, connectorKind, repositoryUrl, TuleapTaskIdentityUtil
				.getTaskDataId(projectRef.getId(), BACKLOG_ITEM_TYPE_ID1, item200.getId()));

	}

	/**
	 * Test the BacklogItem specific information creation from TaskData to POJO.
	 */
	@Test
	@Ignore("Need to check if there is a need for backlog items as standalone artifacts")
	public void testBacklogItemConverterSpecificDataCreation() {

		// item200.setInitialEffort(Float.valueOf(201));
		// item200.setAssignedMilestoneId(100);
		//
		// BacklogItemTaskDataConverter backlogItemConverter = new
		// BacklogItemTaskDataConverter(backlogItemType,
		// repository, repositoryConnector);
		//
		// backlogItemConverter.populateTaskData(taskData, item200, null);
		//
		// TuleapBacklogItem backlogItem = backlogItemConverter.createTuleapBacklogItem(taskData);
		// assertNotNull(backlogItem);
		// assertEquals(200, backlogItem.getId());
		// assertEquals(Float.valueOf(201), backlogItem.getInitialEffort());
		// assertEquals(Integer.valueOf(100), backlogItem.getAssignedMilestoneId());
	}

	/**
	 * Test the BacklogItem specific information population from POJO to TaskData.
	 */
	@Test
	@Ignore("Need to check if there is a need for backlog items as standalone artifacts")
	public void testBacklogItemConverterSpecificDataPopulation() {

		// item200.setInitialEffort(Float.valueOf(201));
		// item200.setAssignedMilestoneId(100);
		//
		// BacklogItemTaskDataConverter backlogItemConverter = new
		// BacklogItemTaskDataConverter(backlogItemType,
		// repository, repositoryConnector);
		//
		// backlogItemConverter.populateTaskData(taskData, item200, null);
		//

	}
}
