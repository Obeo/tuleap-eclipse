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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.mylyn.internal.tasks.ui.editors.TaskEditorAttributePart;
import org.eclipse.mylyn.tasks.core.data.TaskDataModel;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorInput;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorPartDescriptor;
import org.eclipse.ui.IEditorInput;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.tuleap.mylyn.task.agile.ui.AbstractAgileRepositoryConnectorUI;
import org.tuleap.mylyn.task.agile.ui.task.IModelRegistry;
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

		// Ensure that the attributes part stay expanded all the time.
		removePart(descriptors, ID_PART_ATTRIBUTES);
		TaskEditorPartDescriptor attributesPartDescription = new TaskEditorPartDescriptor(ID_PART_ATTRIBUTES) {
			@Override
			public AbstractTaskEditorPart createPart() {
				return new TaskEditorAttributePart() {
					@Override
					protected boolean shouldExpandOnCreate() {
						return true;
					}
				};
			}
		};
		attributesPartDescription.setPath(PATH_ATTRIBUTES);
		descriptors.add(attributesPartDescription);

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

		if (connector != null) {
			IModelRegistry registry = connector.getModelRegistry();
			TaskDataModel model = registry.getRegisteredModel(getEditor());
			if (model == null) {
				model = super.createModel(input);
				registry.registerModel(getEditor(), model);
			}
			IEclipsePreferences node = InstanceScope.INSTANCE
					.getNode(ITuleapConstants.TULEAP_PREFERENCE_NODE);
			if (node != null) {
				boolean debugIsActive = node.getBoolean(ITuleapConstants.TULEAP_PREFERENCE_DEBUG_MODE, false);
				if (debugIsActive) {
					TuleapTasksUIPlugin.log(model.getTaskData().getRoot().toString(), false);
				}
			}
			return model;
		}
		// We must return something, for instance
		// new tasks are in "local" task repository, not "tuleap"
		// So there won't be no agile connector available for new unsubmitted tasks.
		return super.createModel(input);
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
			IEditorInput input = this.getEditor().getEditorInput();
			if (input instanceof TaskEditorInput) {
				String connectorKind = ((TaskEditorInput)input).getTaskRepository().getConnectorKind();
				AbstractAgileRepositoryConnectorUI connector = this
						.getAgileRepositoryConnectorUI(connectorKind);
				if (connector != null) {
					connector.getModelRegistry().deregisterModel(getEditor());
				}
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		AbstractAgileRepositoryConnectorUI connector = this.getAgileRepositoryConnectorUI(getConnectorKind());
		IModelRegistry registry = null;
		if (connector != null) {
			registry = connector.getModelRegistry();
		}
		if (registry != null) {
			registry.fireBeforeSave(getEditor());
		}
		super.doSave(monitor);
		if (registry != null) {
			registry.fireAfterSave(getEditor());
		}
	}
}
