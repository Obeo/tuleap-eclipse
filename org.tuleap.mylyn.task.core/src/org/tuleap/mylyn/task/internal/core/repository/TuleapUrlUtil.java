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
package org.tuleap.mylyn.task.internal.core.repository;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.tuleap.mylyn.task.internal.core.data.TuleapTaskIdentityUtil;

/**
 * This utility class is used to encapsulate all the manipulations of the HTML URLs of the repository and
 * elements.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 */
public final class TuleapUrlUtil {

	/**
	 * Part of the Tuleap repository URL.
	 */
	private static final String TULEAP_REPOSITORY_URL_STRUCTURE = "/plugins/tracker/"; //$NON-NLS-1$

	/**
	 * Part of the URL for the start of the parameters.
	 */
	private static final String PARAMETER_PART_SEPARATOR = "?"; //$NON-NLS-1$

	/**
	 * Part of the URL separating each parameter.
	 */
	private static final String PARAMETER_SEPARATOR = "&"; //$NON-NLS-1$

	/**
	 * The task separator.
	 */
	private static final String REPOSITORY_ELEMENT_ID_URL_SEPARATOR = "aid="; //$NON-NLS-1$

	/**
	 * The tracker separator.
	 */
	private static final String REPOSITORY_TRACKER_ID_URL_SEPARATOR = "tracker="; //$NON-NLS-1$

	/**
	 * The group id separator.
	 */
	private static final String REPOSITORY_PROJECT_ID_URL_SEPARATOR = "group_id="; //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	private TuleapUrlUtil() {
		// prevent instantiation
	}

	/**
	 * Returns the URL of the repository from the URL of the task.
	 * 
	 * @param taskUrl
	 *            The URL of the task
	 * @return The URL of the repository
	 */
	public static String getRepositoryUrlFromTaskUrl(String taskUrl) {
		int indexOf = taskUrl.indexOf(TULEAP_REPOSITORY_URL_STRUCTURE);
		if (indexOf != -1) {
			return taskUrl.substring(0, indexOf);
		}
		return taskUrl;
	}

	/**
	 * Returns the task id from the task URL.
	 * 
	 * @param taskUrl
	 *            The task url
	 * @return The task id
	 */
	public static String getTaskIdFromTaskUrl(String taskUrl) {
		int projectId = TuleapTaskIdentityUtil.IRRELEVANT_ID;
		int configurationId = TuleapTaskIdentityUtil.IRRELEVANT_ID;
		int elementId = TuleapTaskIdentityUtil.IRRELEVANT_ID;

		int indexOfRepository = taskUrl.indexOf(REPOSITORY_PROJECT_ID_URL_SEPARATOR);
		int indexOfSeparator = taskUrl.indexOf(PARAMETER_SEPARATOR, indexOfRepository);
		if (indexOfRepository != -1 && indexOfSeparator != -1) {
			projectId = Integer.parseInt(taskUrl.substring(indexOfRepository
					+ REPOSITORY_PROJECT_ID_URL_SEPARATOR.length(), indexOfSeparator));
		}

		int indexOfTracker = taskUrl.indexOf(REPOSITORY_TRACKER_ID_URL_SEPARATOR);
		indexOfSeparator = taskUrl.indexOf(PARAMETER_SEPARATOR, indexOfTracker);
		if (indexOfTracker != -1 && indexOfSeparator != -1) {
			configurationId = Integer.parseInt(taskUrl.substring(indexOfTracker
					+ REPOSITORY_TRACKER_ID_URL_SEPARATOR.length(), indexOfSeparator));
		}

		int indexOfElement = taskUrl.indexOf(REPOSITORY_ELEMENT_ID_URL_SEPARATOR);
		if (indexOfElement != -1) {
			elementId = Integer.parseInt(taskUrl.substring(indexOfElement
					+ REPOSITORY_ELEMENT_ID_URL_SEPARATOR.length()));
		}

		return TuleapTaskIdentityUtil.getTaskDataId(projectId, configurationId, elementId);
	}

	/**
	 * Returns the URL of a tracker.
	 * 
	 * @param repositoryUrl
	 *            The URL of the repository
	 * @param trackerId
	 *            The Identifier of the tracker
	 * @return The URL of the tracker
	 */
	public static String getTrackerUrl(String repositoryUrl, int trackerId) {
		return repositoryUrl + TULEAP_REPOSITORY_URL_STRUCTURE + PARAMETER_PART_SEPARATOR
				+ REPOSITORY_TRACKER_ID_URL_SEPARATOR + String.valueOf(trackerId);
	}

	/**
	 * Returns the URL of a task from the URL of the repository.
	 * 
	 * @param repositoryUrl
	 *            The URL of the repository
	 * @param taskId
	 *            The identifier of the task
	 * @return The URL of the task
	 */
	public static String getTaskUrlFromTaskId(String repositoryUrl, String taskId) {
		return repositoryUrl + TULEAP_REPOSITORY_URL_STRUCTURE + PARAMETER_PART_SEPARATOR
				+ REPOSITORY_PROJECT_ID_URL_SEPARATOR
				+ TuleapTaskIdentityUtil.getProjectIdFromTaskDataId(taskId) + PARAMETER_SEPARATOR
				+ REPOSITORY_TRACKER_ID_URL_SEPARATOR
				+ TuleapTaskIdentityUtil.getConfigurationIdFromTaskDataId(taskId) + PARAMETER_SEPARATOR
				+ REPOSITORY_ELEMENT_ID_URL_SEPARATOR
				+ TuleapTaskIdentityUtil.getElementIdFromTaskDataId(taskId);
	}

	/**
	 * Returns the URL used to create an account.
	 * 
	 * @param taskRepository
	 *            The URL of the repository
	 * @return The URL used to create an account
	 */
	public static String getAccountCreationUrl(TaskRepository taskRepository) {
		return taskRepository.getRepositoryUrl() + "/account/register.php"; //$NON-NLS-1$
	}

	/**
	 * Returns the URL used to manage an account.
	 * 
	 * @param taskRepository
	 *            The task repository
	 * @return The URL used to manage an account
	 */
	public static String getAccountManagementUrl(TaskRepository taskRepository) {
		return taskRepository.getRepositoryUrl() + "/my/"; //$NON-NLS-1$
	}
}
