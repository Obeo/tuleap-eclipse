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
package org.eclipse.mylyn.tuleap.core.internal.repository;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;

/**
 * This class is used for collecting tasks, e.g. when performing queries on a repository.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public class TuleapTaskDataCollector extends TaskDataCollector {

	/**
	 * The set of tasks collected.
	 */
	private final Set<TaskData> taskDataCollected = new HashSet<TaskData>();

	/**
	 * The timestamp of the execution of the query.
	 */
	private String queryTimestamp;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tasks.core.data.TaskDataCollector#accept(org.eclipse.mylyn.tasks.core.data.TaskData)
	 */
	@Override
	public void accept(TaskData taskData) {
		taskDataCollected.add(taskData);
	}

	/**
	 * Returns the set of tasks collected.
	 * 
	 * @return The set of tasks collected.
	 */
	public Set<TaskData> getTaskData() {
		return taskDataCollected;
	}

	/**
	 * Returns the timestamp of the query.
	 * 
	 * @return the timestamp of the query.
	 */
	public String getQueryTimestamp() {
		return queryTimestamp;
	}

	/**
	 * Sets the timestamp of the query.
	 * 
	 * @param timestamp
	 *            The timestamp of the query.
	 */
	public void setQueryTimestamp(String timestamp) {
		this.queryTimestamp = timestamp;
	}

}
