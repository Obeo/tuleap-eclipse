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
package org.tuleap.mylyn.task.core.internal.model.data.agile;

import java.util.List;

import org.tuleap.mylyn.task.core.internal.model.data.TuleapReference;

/**
 * Interface that represents a planning (i.e. something with a backlog and milestones to which backlog items
 * can be assigned.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public interface IPlanning {

	/**
	 * Retrieve the planning's sub-milestones.
	 * 
	 * @return The sub-milestones.
	 */
	List<TuleapMilestone> getSubMilestones();

	/**
	 * Retrieve the backlog items.
	 * 
	 * @return The backlog items.
	 */
	List<TuleapBacklogItem> getBacklogItems();

	/**
	 * The project reference.
	 * 
	 * @return the projec reference
	 */
	TuleapReference getProject();
}
