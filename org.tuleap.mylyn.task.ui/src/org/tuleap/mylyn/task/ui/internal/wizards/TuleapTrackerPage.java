/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.tuleap.mylyn.task.ui.internal.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.ui.PlatformUI;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapProject;
import org.tuleap.mylyn.task.core.internal.model.config.TuleapTracker;
import org.tuleap.mylyn.task.core.internal.repository.ITuleapRepositoryConnector;
import org.tuleap.mylyn.task.ui.internal.TuleapTasksUIPlugin;
import org.tuleap.mylyn.task.ui.internal.util.TuleapUIKeys;
import org.tuleap.mylyn.task.ui.internal.util.TuleapUIMessages;

/**
 * This page will be used when a new task is created in order to let the user select the tracker for which the
 * task will be created.
 *
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 0.7
 */
public class TuleapTrackerPage extends AbstractTuleapTrackerPage {

	/**
	 * The trackers used.
	 */
	private TuleapProject project;

	/**
	 * The task repository used.
	 */
	private TaskRepository repository;

	/**
	 * The constructor.
	 *
	 * @param project
	 *            The project, the trackers of which should be displayed
	 * @param repository
	 *            The task repository to use
	 */
	public TuleapTrackerPage(TuleapProject project, TaskRepository repository) {
		super();
		this.project = project;
		this.repository = repository;
	}

	/**
	 * Retrieve the project trackers, after fetching them if necessary.
	 *
	 * @return The list of trackers of this page's project.
	 */
	@Override
	protected List<TuleapTracker> getTrackers() {
		final List<TuleapTracker> trackers = project.getAllTrackers();
		String connectorKind = this.repository.getConnectorKind();
		if (trackers.isEmpty()) {
			final AbstractRepositoryConnector repositoryConnector = TasksUi.getRepositoryManager()
					.getRepositoryConnector(connectorKind);
			if (repositoryConnector instanceof ITuleapRepositoryConnector) {
				IRunnableWithProgress runnable = new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException,
					InterruptedException {
						monitor.subTask(TuleapUIMessages.getString(
								TuleapUIKeys.tuleapTrackerPageMsgRetrievingTrackers, project.getLabel()));
						ITuleapRepositoryConnector connector = (ITuleapRepositoryConnector)repositoryConnector;
						try {
							connector.refreshProject(repository, project, monitor);
						} catch (CoreException e) {
							TuleapTasksUIPlugin.log(e, true);
						}
						trackers.addAll(project.getAllTrackers());
					}
				};
				try {
					PlatformUI.getWorkbench().getProgressService().run(true, false, runnable);
					// this.getContainer().run(true, false, runnable);
				} catch (InvocationTargetException e) {
					TuleapTasksUIPlugin.log(e, true);
				} catch (InterruptedException e) {
					// Nothing to do
				} finally {
					this.getWizard().getContainer().updateButtons();
				}
			}
		}
		return trackers;
	}
}
