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
package org.eclipse.mylyn.internal.tuleap.ui.editor;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapField;
import org.eclipse.mylyn.internal.tuleap.core.model.AbstractTuleapStructuralElement;
import org.eclipse.mylyn.internal.tuleap.core.model.field.TuleapFileUpload;
import org.eclipse.mylyn.internal.tuleap.core.repository.TuleapRepositoryConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.repository.TuleapRepositoryConnector;
import org.eclipse.mylyn.internal.tuleap.core.util.ITuleapConstants;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorPartDescriptor;

/**
 * The Tuleap task editor page.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TuleapTaskEditorPage extends AbstractTaskEditorPage {

	/**
	 * The Tuleap rendering engine.
	 */
	private TuleapRenderingEngine renderingEngine;

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
		// Remove useless parts
		this.removePart(descriptors, AbstractTaskEditorPage.ID_PART_DESCRIPTION);

		// Remove the part that we won't use
		TaskRepository taskRepository = this.getTaskRepository();
		if (taskRepository != null) {
			AbstractRepositoryConnector connector = this.getConnector();
			if (connector instanceof TuleapRepositoryConnector) {
				TuleapRepositoryConnector tuleapRepositoryConnector = (TuleapRepositoryConnector)connector;
				TuleapRepositoryConfiguration configuration = tuleapRepositoryConnector
						.getRepositoryConfiguration(taskRepository, false, new NullProgressMonitor());

				boolean hasUploadPart = false;
				List<AbstractTuleapStructuralElement> formElements = configuration.getFormElements();
				for (AbstractTuleapStructuralElement abstractTuleapStructuralElement : formElements) {
					List<AbstractTuleapField> fields = TuleapRepositoryConfiguration
							.getFields(abstractTuleapStructuralElement);
					for (AbstractTuleapField abstractTuleapField : fields) {
						if (abstractTuleapField instanceof TuleapFileUpload) {
							hasUploadPart = true;
						}
					}
				}

				if (!hasUploadPart) {
					this.removePart(descriptors, AbstractTaskEditorPage.ID_PART_ATTACHMENTS);
				} else {
					// Create custom attachments part
				}
			}
		}

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
	 * @see org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage#createParts()
	 */
	@Override
	protected void createParts() {
		if (this.renderingEngine == null) {
			this.renderingEngine = new TuleapRenderingEngine();
		}
		this.getAttributeEditorToolkit().setRenderingEngine(this.renderingEngine);
		super.createParts();
	}
}
