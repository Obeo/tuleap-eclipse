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
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.ui.ITasksUiConstants;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.tuleap.mylyn.task.agile.core.IMilestoneMapping;
import org.tuleap.mylyn.task.agile.core.data.AgileTaskKindUtil;
import org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI;
import org.tuleap.mylyn.task.agile.ui.editors.ITaskEditorPageFactoryConstants;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskIdentityUtil;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapProject;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapServer;
import org.tuleap.mylyn.task.internal.core.repository.TuleapRepositoryConnector;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.ui.editor.TuleapTaskEditorPageFactory;
import org.tuleap.mylyn.task.internal.ui.wizards.newsubmilestone.NewTuleapMilestoneWizard;

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
			TuleapRepositoryConnector tuleapRepositoryConnector = (TuleapRepositoryConnector)connector;
			TuleapServer serverConfiguration = tuleapRepositoryConnector
					.getServer(taskRepository.getRepositoryUrl());

			int projectId = TuleapTaskIdentityUtil.getProjectIdFromTaskDataId(planningTaskData.getTaskId());
			TuleapProject project = serverConfiguration.getProject(projectId);

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
	 *      org.eclipse.mylyn.tasks.core.data.TaskData)
	 */
	@Override
	public String[] getConflictingIds(String taskEditorPageFactoryId, TaskData taskData) {
		if (ITaskEditorPageFactoryConstants.PLANNING_TASK_EDITOR_PAGE_FACTORY_ID
				.equals(taskEditorPageFactoryId)) {
			String agileTaskKind = AgileTaskKindUtil.getAgileTaskKind(taskData);
			if (AgileTaskKindUtil.TASK_KIND_TOP_PLANNING.equals(agileTaskKind)) {
				return new String[] {ITasksUiConstants.ID_PAGE_PLANNING,
						TuleapTaskEditorPageFactory.TULEAP_TASK_EDITOR_PAGE_FACTORY_ID, };
			}
		}

		return new String[] {ITasksUiConstants.ID_PAGE_PLANNING, };
	}
}
