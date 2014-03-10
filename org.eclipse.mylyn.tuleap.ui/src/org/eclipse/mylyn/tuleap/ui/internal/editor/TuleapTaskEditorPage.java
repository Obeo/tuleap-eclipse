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
package org.eclipse.mylyn.tuleap.ui.internal.editor;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.mylyn.internal.tasks.ui.editors.TaskEditorAttributePart;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPage;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPart;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorPartDescriptor;
import org.eclipse.mylyn.tuleap.core.internal.util.ITuleapConstants;

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
}
