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
package org.tuleap.mylyn.task.internal.ui.repository;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.ui.ITasksUiConstants;
import org.tuleap.mylyn.task.agile.core.util.IMylynAgileCoreConstants;
import org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI;
import org.tuleap.mylyn.task.agile.ui.editors.ITaskEditorPageFactoryConstants;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.ui.editor.TuleapTaskEditorPageFactory;
import org.tuleap.mylyn.task.internal.ui.wizards.NewTuleapTaskWizard;

/**
 * Agile repository connector UI for Tuleap, which is exposed as an OSGI service.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TuleapAgileRepositoryConnectorUI extends AbstractAgileRepositoryConnectorUI {

	/**
	 * {@inheritDoc}
	 * <p>
	 * The supported connector kind returned is the constant
	 * {@code TuleapAgileRepositoryConnector.TULEAP_CONNECTOR_KIND}.
	 * </p>
	 * 
	 * @see org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI#getConnectorKind()
	 */
	@Override
	public String getConnectorKind() {
		return ITuleapConstants.CONNECTOR_KIND;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI#getNewMilestoneWizard(org.eclipse.mylyn.tasks.core.data.TaskData,
	 *      org.eclipse.mylyn.tasks.core.TaskRepository, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IWizard getNewMilestoneWizard(TaskData planningTaskData, TaskRepository taskRepository,
			IProgressMonitor monitor) {
		// TODO How can we find the tracker in which the task will be created?
		// TODO Create an interface for wizards used in order to skip useless pages in order to filter the
		// content of the wizard. We don't want to see all the pages of the Tuleap wizard,just the tracker
		// selection.
		NewTuleapTaskWizard wizard = new NewTuleapTaskWizard(taskRepository, null);

		// TuleapTaskMapper mapper = new TuleapTaskMapper(planningTaskData, null);
		// int trackerId = mapper.getTrackerId();
		//
		// String connectorKind = taskRepository.getConnectorKind();
		// AbstractRepositoryConnector connector = TasksUi.getRepositoryManager().getRepositoryConnector(
		// connectorKind);
		// if (connector instanceof TuleapRepositoryConnector) {
		// TuleapRepositoryConnector tuleapRepositoryConnector = (TuleapRepositoryConnector)connector;
		// TuleapServerConfiguration repositoryConfiguration = tuleapRepositoryConnector
		// .getRepositoryConfiguration(taskRepository.getRepositoryUrl());
		//
		// }

		return wizard;
		// AbstractRepositoryConnector de tuleap
		// repositoryConnector.getTaskDataHandler().initializeTaskData(TaskRepository, TaskData, ITaskMapping,
		// IProgressMonitor);
		// create task from task data
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI#getConflictingIds(java.lang.String,
	 *      org.eclipse.mylyn.tasks.core.data.TaskData)
	 */
	@Override
	public String[] getConflictingIds(String taskEditorPageFactoryId, TaskData taskData) {
		if (ITaskEditorPageFactoryConstants.PLANNING_TASK_EDITOR_PAGE_FACTORY_ID
				.equals(taskEditorPageFactoryId)) {
			TaskAttribute mappedAttribute = taskData.getRoot().getMappedAttribute(
					IMylynAgileCoreConstants.TASK_KIND_KEY);
			if (mappedAttribute != null
					&& IMylynAgileCoreConstants.TASK_KIND_TOP_PLANNING.equals(mappedAttribute.getValue())) {
				return new String[] {ITasksUiConstants.ID_PAGE_PLANNING,
						TuleapTaskEditorPageFactory.TULEAP_TASK_EDITOR_PAGE_FACTORY_ID, };
			}
		}

		return new String[] {ITasksUiConstants.ID_PAGE_PLANNING, };
	}
}
