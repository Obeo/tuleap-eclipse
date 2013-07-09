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
package org.tuleap.mylyn.task.internal.ui.wizards;

import org.eclipse.jface.viewers.LabelProvider;
import org.tuleap.mylyn.task.internal.core.model.TuleapTrackerConfiguration;

/**
 * The label provider used to display the trackers in the tracker selection page.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public class TuleapTrackerLabelProvider extends LabelProvider {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof TuleapTrackerConfiguration) {
			return ((TuleapTrackerConfiguration)element).getName();

		}
		return super.getText(element);
	}
}
