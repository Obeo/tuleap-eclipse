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
package org.tuleap.mylyn.task.internal.ui.editor;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorInput;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorPartDescriptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI;
import org.tuleap.mylyn.task.internal.core.util.ITuleapConstants;
import org.tuleap.mylyn.task.internal.ui.TuleapTasksUIPlugin;

/**
 * The Tuleap task editor page.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapTaskEditorPage extends AbstractTaskEditorPage {

	/**
	 * The constructor.
	 * 
	 * @param editor
	 *            The task editor
	 */
	public TuleapTaskEditorPage(TaskEditor editor) {
		super(editor, ITuleapConstants.CONNECTOR_KIND);
		this.setNeedsPrivateSection(false);
		this.setNeedsSubmitButton(true);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#createPartDescriptors()
	 */
	@Override
	protected Set<TaskEditorPartDescriptor> createPartDescriptors() {
		Set<TaskEditorPartDescriptor> descriptors = super.createPartDescriptors();
		// Remove useless part
		this.removePart(descriptors, AbstractTaskEditorPage.ID_PART_DESCRIPTION);

		// This part will have its dedicated tab
		this.removePart(descriptors, AbstractTaskEditorPage.ID_PART_PLANNING);

		return descriptors;
	}

	/**
	 * Removes the parts with an ID matching the given partId from the set of parts.
	 * 
	 * @param parts
	 *            The set of parts
	 * @param partId
	 *            The part ID.
	 */
	private void removePart(Set<TaskEditorPartDescriptor> parts, String partId) {
		Iterator<TaskEditorPartDescriptor> iterator = parts.iterator();
		while (iterator.hasNext()) {
			TaskEditorPartDescriptor taskEditorPartDescriptor = iterator.next();
			if (partId.equals(taskEditorPartDescriptor.getId())) {
				iterator.remove();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#createModel(org.eclipse.mylyn.tasks.ui.editors.TaskEditorInput)
	 */
	@Override
	protected TaskDataModel createModel(TaskEditorInput input) throws CoreException {
		String connectorKind = input.getTaskRepository().getConnectorKind();

		AbstractAgileRepositoryConnectorUI connector = this.getAgileRepositoryConnectorUI(connectorKind);

		TaskDataModel taskDataModel;
		if (connector != null) {
			taskDataModel = connector.getModelRegistry().getRegisteredModel(getEditor());
			if (taskDataModel == null) {
				taskDataModel = super.createModel(input);
				connector.getModelRegistry().registerModel(getEditor(), taskDataModel);
			}
		} else {
			taskDataModel = super.createModel(input);
		}
		return taskDataModel;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#dispose()
	 */
	@Override
	public void dispose() {
		TaskDataModel taskDataModel = getModel();
		if (taskDataModel != null) {
			String connectorKind = taskDataModel.getTaskRepository().getConnectorKind();
			AbstractAgileRepositoryConnectorUI connector = this.getAgileRepositoryConnectorUI(connectorKind);
			if (connector != null) {
				connector.getModelRegistry().deregisterModel(getEditor());
			}
		}
		super.dispose();
	}

	/**
	 * Returns the {@link AbstractAgileRepositoryConnectorUI} with the given connector kind or
	 * <code>null</code> if none can be found.
	 * 
	 * @param connectorKind
	 *            The connector kind
	 * @return The {@link AbstractAgileRepositoryConnectorUI}.
	 */
	private AbstractAgileRepositoryConnectorUI getAgileRepositoryConnectorUI(String connectorKind) {
		AbstractAgileRepositoryConnectorUI connector = null;

		BundleContext bundleContext = TuleapTasksUIPlugin.getDefault().getBundle().getBundleContext();
		try {
			ServiceReference<?>[] serviceReferences = bundleContext.getAllServiceReferences(
					AbstractAgileRepositoryConnectorUI.class.getName(), null);
			if (serviceReferences != null) {
				for (ServiceReference<?> serviceReference : serviceReferences) {
					Object service = bundleContext.getService(serviceReference);
					if (service instanceof AbstractAgileRepositoryConnectorUI
							&& connectorKind.equals(((AbstractAgileRepositoryConnectorUI)service)
									.getConnectorKind())) {
						connector = (AbstractAgileRepositoryConnectorUI)service;
					}
				}
			}
		} catch (InvalidSyntaxException e) {
			TuleapTasksUIPlugin.log(e, true);
		}

		return connector;
	}
}
