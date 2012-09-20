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

import java.util.Set;

import org.eclipse.mylyn.internal.tuleap.core.util.ITuleapConstants;
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
		this.setNeedsPrivateSection(true);
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

		// TODO Remove unnecessary default editor parts

		// TODO Add the necessary editor parts for the repository configuration
		return descriptors;
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
