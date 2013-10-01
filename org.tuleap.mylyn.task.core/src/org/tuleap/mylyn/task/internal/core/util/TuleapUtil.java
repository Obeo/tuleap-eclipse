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
package org.tuleap.mylyn.task.internal.core.util;

/**
 * Utility class containing various simple static utility methods.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public final class TuleapUtil {

	/**
	 * The separator between the project name and the tracker id.
	 */
	public static final String CONFIGURATION_NAME_SEPARATOR = ":"; //$NON-NLS-1$

	/**
	 * The separator used in the id of the task data.
	 */
	public static final String TASK_DATA_ID_SEPARATOR = "-"; //$NON-NLS-1$

	/**
	 * The identifier to use for irrelevant ids. This will be used for the configuration id of Tuleap top
	 * plannings
	 */
	public static final int IRRELEVANT_ID = -1;

	/**
	 * The constructor.
	 */
	private TuleapUtil() {
		// prevent instantiation
	}

	/**
	 * Extract the domain name URL from the repository URL.
	 * 
	 * @param repositoryUrl
	 *            The task repository url : "https://<domainName>/plugins/tracker/?group_id=<groupId>"
	 * @return The domain name URL : "https://<domainName>/"
	 */
	public static String getDomainRepositoryURL(String repositoryUrl) {
		if (repositoryUrl.contains(ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE)) {
			return repositoryUrl.substring(0, repositoryUrl
					.indexOf(ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE));
		}
		return repositoryUrl;
	}

	/**
	 * Returns the task data key from the project name, the configuration name (tracker name) and the given
	 * artifact id.
	 * 
	 * @param projectName
	 *            The name of the project
	 * @param configurationName
	 *            The name of the configuration.
	 * @param artifactId
	 *            The id of the artifact
	 * @return The task data id
	 */
	public static String getTaskDataKey(String projectName, String configurationName, int artifactId) {
		return projectName + CONFIGURATION_NAME_SEPARATOR + configurationName + TASK_DATA_ID_SEPARATOR
				+ Integer.valueOf(artifactId).toString();
	}

	/**
	 * Returns the task data id.
	 * 
	 * @param projectId
	 *            The identifier of the project
	 * @param configurationId
	 *            The identifier of the configuration
	 * @param elementId
	 *            The identifier of the element
	 * @return The task data id
	 */
	public static String getTaskDataId(int projectId, int configurationId, int elementId) {
		return String.valueOf(projectId) + CONFIGURATION_NAME_SEPARATOR + String.valueOf(configurationId)
				+ TASK_DATA_ID_SEPARATOR + String.valueOf(elementId);
	}

	/**
	 * Indicates if the URL is valid.
	 * 
	 * @param url
	 *            The URL
	 * @return <code>true</code> if the url is valid, <code>false</code> otherwise.
	 */
	public static boolean isValidUrl(String url) {
		return url.matches("https://.*"); //$NON-NLS-1$
	}
}
