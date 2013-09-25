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

import java.util.Date;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.junit.Before;
import org.junit.Test;
import org.tuleap.mylyn.task.internal.core.model.agile.TuleapMilestone;

import static org.junit.Assert.fail;

/**
 * Tests of the milestone planning task data converter.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class MilestonePlanningTaskDataConverterTest {

	/**
	 * The wrapped task data.
	 */
	private TaskData taskData;

	/**
	 * Tests the basic manipulation of a MilestonePlanningTaskDataConverter to ensure the TaskAttribute
	 * structure is correct.
	 */
	@Test
	public void testMilestonePlanningCreation() {

		Date testDate = new Date();

		TuleapMilestone milestone = new TuleapMilestone();
		// milestone.setId(50);
		// TuleapMilestone submilestone100 = new TuleapMilestone();
		// submilestone100.setId(100);
		//		submilestone100.setLabel("submilestone100"); //$NON-NLS-1$
		// submilestone100.setCapacity(101);
		// submilestone100.setDuration(102);
		// submilestone100.setStartDate(testDate.getTime());
		// milestone.addSubMilestone(submilestone100);
		//
		// TuleapBacklogItem item200 = new TuleapBacklogItem();
		// item200.setId(200);
		// item200.setAssignedMilestoneId(submilestone100.getId());
		// item200.setInitialEffort(201);
		//		item200.setLabel("item200"); //$NON-NLS-1$
		// milestone.addBacklogItem(item200);
		//
		// MilestonePlanningTaskDataConverter converter = new MilestonePlanningTaskDataConverter(null);
		// converter.populateTaskData(taskData, milestone);
		//
		// TaskAttribute root = taskData.getRoot();
		// TaskAttribute planningAtt = root.getAttribute(MilestonePlanningWrapper.MILESTONE_PLANNING);
		// assertNotNull(planningAtt);
		//
		// // Milestones
		// TaskAttribute milestoneListAtt = planningAtt.getAttribute(MilestonePlanningWrapper.MILESTONE_LIST);
		// assertNotNull(milestoneListAtt);
		//		TaskAttribute milestone0 = milestoneListAtt.getAttribute(SubMilestoneWrapper.PREFIX_MILESTONE + "0"); //$NON-NLS-1$
		// assertNotNull(milestone0);
		// assertTrue(milestone0.getMetaData().isReadOnly());
		//
		// TaskAttribute capacity = milestone0.getAttribute(SubMilestoneWrapper.MILESTONE_CAPACITY);
		// assertNotNull(capacity);
		// assertEquals(Float.toString(101f), capacity.getValue());
		// assertEquals(TaskAttribute.TYPE_DOUBLE, capacity.getMetaData().getType());
		//
		// TaskAttribute duration = milestone0.getAttribute(SubMilestoneWrapper.MILESTONE_DURATION);
		// assertNotNull(duration);
		// assertEquals(Float.toString(102f), duration.getValue());
		// assertEquals(TaskAttribute.TYPE_DOUBLE, duration.getMetaData().getType());
		//
		// TaskAttribute id = milestone0.getAttribute(IMylynAgileCoreConstants.ID);
		// assertNotNull(id);
		//		assertEquals("100", id.getValue()); //$NON-NLS-1$
		// assertEquals(TaskAttribute.TYPE_INTEGER, id.getMetaData().getType());
		//
		// TaskAttribute label = milestone0.getAttribute(IMylynAgileCoreConstants.LABEL);
		// assertNotNull(label);
		//		assertEquals("submilestone100", label.getValue()); //$NON-NLS-1$
		// assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, label.getMetaData().getType());
		//
		// TaskAttribute start = milestone0.getAttribute(SubMilestoneWrapper.START_DATE);
		// assertNotNull(start);
		// assertEquals(Long.toString(testDate.getTime()), start.getValue());
		// assertEquals(TaskAttribute.TYPE_DATETIME, start.getMetaData().getType());
		//
		// // Backlog
		// TaskAttribute backlogAtt = planningAtt.getAttribute(MilestonePlanningWrapper.BACKLOG);
		// assertNotNull(backlogAtt);
		//
		//		TaskAttribute item0 = backlogAtt.getAttribute(BacklogItemWrapper.PREFIX_BACKLOG_ITEM + "0"); //$NON-NLS-1$
		// assertNotNull(item0);
		// assertTrue(item0.getMetaData().isReadOnly());
		//
		// TaskAttribute itemId = item0.getAttribute(BacklogItemWrapper.BACKLOG_ITEM_ID);
		// assertNotNull(itemId);
		//		assertEquals("200", itemId.getValue()); //$NON-NLS-1$
		// assertEquals(TaskAttribute.TYPE_INTEGER, itemId.getMetaData().getType());
		//
		// TaskAttribute itemLabel = item0.getAttribute(IMylynAgileCoreConstants.LABEL);
		// assertNotNull(itemLabel);
		//		assertEquals("item200", itemLabel.getValue()); //$NON-NLS-1$
		// assertEquals(TaskAttribute.TYPE_SHORT_RICH_TEXT, itemLabel.getMetaData().getType());
		//
		// TaskAttribute itemEffort = item0.getAttribute(BacklogItemWrapper.BACKLOG_ITEM_POINTS);
		// assertNotNull(itemEffort);
		// assertEquals(Float.toString(201), itemEffort.getValue());
		// assertEquals(TaskAttribute.TYPE_DOUBLE, itemEffort.getMetaData().getType());
		fail("Fix the test ");

	}

	/**
	 * Configure the data for the tests.
	 */
	@Before
	public void setUp() {
		String repositoryUrl = "repository"; //$NON-NLS-1$
		String connectorKind = "kind"; //$NON-NLS-1$
		String taskId = "id"; //$NON-NLS-1$ 
		TaskRepository taskRepository = new TaskRepository(connectorKind, repositoryUrl);
		TaskAttributeMapper mapper = new TaskAttributeMapper(taskRepository);
		taskData = new TaskData(mapper, connectorKind, repositoryUrl, taskId);
	}

}
