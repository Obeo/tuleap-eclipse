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
package org.eclipse.mylyn.tuleap.core.internal.repository;

import org.eclipse.mylyn.tasks.core.TaskRepository;

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
	 * The tracker separator.
	 */
	private static final String REPOSITORY_TRACKER_ID_URL_SEPARATOR = "tracker="; //$NON-NLS-1$

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
