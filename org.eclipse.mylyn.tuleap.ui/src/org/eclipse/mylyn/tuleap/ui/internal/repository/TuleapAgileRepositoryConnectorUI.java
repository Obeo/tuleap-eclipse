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
package org.eclipse.mylyn.tuleap.ui.internal.repository;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.ui.ITasksUiConstants;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.mylyn.tuleap.core.internal.data.TuleapTaskId;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapProject;
import org.eclipse.mylyn.tuleap.core.internal.model.config.TuleapServer;
import org.eclipse.mylyn.tuleap.core.internal.repository.TuleapRepositoryConnector;
import org.eclipse.mylyn.tuleap.core.internal.util.ITuleapConstants;
import org.eclipse.mylyn.tuleap.ui.internal.TuleapTasksUIPlugin;
import org.eclipse.mylyn.tuleap.ui.internal.editor.TuleapTaskEditorPageFactory;
import org.eclipse.mylyn.tuleap.ui.internal.wizards.newsubmilestone.NewTuleapMilestoneWizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.tuleap.mylyn.task.agile.core.IMilestoneMapping;
import org.tuleap.mylyn.task.agile.core.data.cardwall.CardwallWrapper;
import org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI;
import org.tuleap.mylyn.task.agile.ui.editors.ITaskEditorPageFactoryConstants;

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
	 * @see org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI#getNewMilestoneMapping(org.eclipse.mylyn.tasks.core.data.TaskData,
	 *      java.lang.String, org.eclipse.mylyn.tasks.core.TaskRepository,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IMilestoneMapping getNewMilestoneMapping(TaskData planningTaskData, String parentMilestoneId,
			TaskRepository taskRepository, IProgressMonitor monitor) {
		// Display the wizard used to select the kind of the milestone to select
		String connectorKind = taskRepository.getConnectorKind();
		AbstractRepositoryConnector connector = TasksUi.getRepositoryManager().getRepositoryConnector(
				connectorKind);
		if (connector instanceof TuleapRepositoryConnector) {
			TuleapRepositoryConnector tuleapConnector = (TuleapRepositoryConnector)connector;
			TuleapServer server = tuleapConnector.getServer(taskRepository);

			int projectId = TuleapTaskId.forName(planningTaskData.getTaskId()).getProjectId();
			TuleapProject project = server.getProject(projectId);

			NewTuleapMilestoneWizard wizard = new NewTuleapMilestoneWizard(project, parentMilestoneId);

			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			WizardDialog dialog = new WizardDialog(shell, wizard);

			int result = dialog.open();
			if (result == WizardDialog.OK) {
				return wizard.getMilestoneMapping();
			}
		}

		// The creation has been cancelled
		return null;
		// AbstractRepositoryConnector de tuleap
		// repositoryConnector.getTaskDataHandler().initializeTaskData(TaskRepository, TaskData, ITaskMapping,
		// IProgressMonitor);
		// create task from task data
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI#getConflictingIds(java.lang.String,
	 *      org.eclipse.mylyn.tasks.core.ITask, org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public String[] getConflictingIds(String taskEditorPageFactoryId, ITask task, TaskRepository repository) {
		if (ITaskEditorPageFactoryConstants.PLANNING_TASK_EDITOR_PAGE_FACTORY_ID
				.equals(taskEditorPageFactoryId)) {
			TuleapTaskId taskId = TuleapTaskId.forName(task.getTaskId());
			if (taskId.isTopPlanning()) {
				// Top planning
				return new String[] {ITasksUiConstants.ID_PAGE_PLANNING,
						TuleapTaskEditorPageFactory.TULEAP_TASK_EDITOR_PAGE_FACTORY_ID, };
			}
		}
		return new String[] {ITasksUiConstants.ID_PAGE_PLANNING, };
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI#hasCardwall(org.eclipse.mylyn.tasks.core.ITask,
	 *      org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public boolean hasCardwall(ITask task, TaskRepository repository) {
		boolean result = false;
		TuleapTaskId taskId = TuleapTaskId.forName(task.getTaskId());
		if (taskId.isTopPlanning()) {
			// TODO Check: No cardwall on top plannings?
			result = false;
		} else {
			try {
				TaskData taskData = TasksUi.getTaskDataManager().getTaskData(task);
				if (taskData.getRoot().getAttribute(CardwallWrapper.CARDWALL) != null) {
					result = true;
				}
			} catch (CoreException e) {
				TuleapTasksUIPlugin.log(e, false);
			}
			// String connectorKind = repository.getConnectorKind();
			// AbstractRepositoryConnector connector = TasksUi.getRepositoryManager().getRepositoryConnector(
			// connectorKind);
			// if (connector instanceof TuleapRepositoryConnector) {
			// TuleapRepositoryConnector tuleapRepositoryConnector = (TuleapRepositoryConnector)connector;
			// TuleapServer server = tuleapRepositoryConnector.getServer(repository.getRepositoryUrl());
			// int projectId = taskId.getProjectId();
			// TuleapProject project = server.getProject(projectId);
			// result = project.isCardwallActive(taskId.getTrackerId());
			// }
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI#hasPlanning(org.eclipse.mylyn.tasks.core.ITask,
	 *      org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public boolean hasPlanning(ITask task, TaskRepository repository) {
		boolean result = false;
		TuleapTaskId taskId = TuleapTaskId.forName(task.getTaskId());
		if (taskId.isTopPlanning()) {
			result = true;
		} else {
			String connectorKind = repository.getConnectorKind();
			AbstractRepositoryConnector connector = TasksUi.getRepositoryManager().getRepositoryConnector(
					connectorKind);
			if (connector instanceof TuleapRepositoryConnector) {
				TuleapRepositoryConnector tuleapRepositoryConnector = (TuleapRepositoryConnector)connector;
				TuleapServer server = tuleapRepositoryConnector.getServer(repository);
				int projectId = taskId.getProjectId();
				TuleapProject project = server.getProject(projectId);
				if (project.isMilestoneTracker(taskId.getTrackerId())) {
					// TODO Find a way to know in the configuration whether there should be a planning
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI#mustCreateToolbarActions(org.eclipse.mylyn.tasks.core.ITask,
	 *      org.eclipse.mylyn.tasks.core.TaskRepository)
	 */
	@Override
	public boolean mustCreateToolbarActions(ITask task, TaskRepository repository) {
		TuleapTaskId taskId = TuleapTaskId.forName(task.getTaskId());
		if (taskId.isTopPlanning()) {
			// Top planning has no general tab
			return true;
		}
		return false;
	}
}
