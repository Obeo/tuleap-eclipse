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
package org.tuleap.mylyn.task.internal.core.data.converter;

import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapConfigurableElement;
import org.tuleap.mylyn.task.internal.core.model.AbstractTuleapFieldContainerConfig;

/**
 * Common ancestor of all the task data converter.
 * 
 * @param <ELEMENT>
 *            The kind of {@link AbstractTuleapConfigurableElement} to use.
 * @param <CONFIGURATION>
 *            The kind of {@link AbstractTuleapFieldContainerConfig} to use.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public abstract class AbstractElementTaskDataConverter<ELEMENT extends AbstractTuleapConfigurableElement, CONFIGURATION extends AbstractTuleapFieldContainerConfig> {

	/**
	 * The configuration of the kind of elements.
	 */
	protected final CONFIGURATION configuration;

	/**
	 * The constructor.
	 * 
	 * @param configuration
	 *            The configuration
	 */
	public AbstractElementTaskDataConverter(CONFIGURATION configuration) {
		this.configuration = configuration;
	}

	/**
	 * Fills the task data related to the given milestone POJO.
	 * 
	 * @param taskData
	 *            the task data to fill/update with the given milestone.
	 * @param element
	 *            The element to use to populate the task data
	 */
	public abstract void populateTaskData(TaskData taskData, ELEMENT element);

	/**
	 * Populate the configurable fields for the given element.
	 * 
	 * @param element
	 *            The element to populate
	 * @param taskData
	 *            The task data to use
	 * @return The element to populate
	 */
	protected ELEMENT populateConfigurableFields(ELEMENT element, TaskData taskData) {
		// to do
		return element;
	}
}
