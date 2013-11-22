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
import org.eclipse.swt.graphics.Image;
import org.tuleap.mylyn.task.internal.core.model.config.TuleapTracker;
import org.tuleap.mylyn.task.internal.ui.TuleapTasksUIPlugin;
import org.tuleap.mylyn.task.internal.ui.util.ITuleapUIConstants;

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
		if (element instanceof TuleapTracker) {
			return ((TuleapTracker)element).getLabel();

		}
		return super.getText(element);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		Image image = null;
		if (element instanceof TuleapTracker) {
			TuleapTracker tuleapTracker = (TuleapTracker)element;
			int trackerId = tuleapTracker.getIdentifier();

			if (tuleapTracker.getProject().isMilestoneTracker(trackerId)
					&& tuleapTracker.getProject().isCardwallActive(trackerId)) {
				image = TuleapTasksUIPlugin.getDefault().getImage(
						ITuleapUIConstants.Icons.TULEAP_CARDWALL_16X16);
			} else if (tuleapTracker.getProject().isMilestoneTracker(trackerId)) {
				image = TuleapTasksUIPlugin.getDefault().getImage(
						ITuleapUIConstants.Icons.TULEAP_PLANNING_16X16);
			} else {
				image = TuleapTasksUIPlugin.getDefault().getImage(
						ITuleapUIConstants.Icons.TULEAP_TRACKER_16X16);
			}
		}
		return image;
	}
}
