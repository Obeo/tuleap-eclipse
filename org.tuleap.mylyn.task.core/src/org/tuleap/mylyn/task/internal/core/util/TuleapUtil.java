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

import java.util.Date;

/**
 * Utility class containing various simple static utility methods.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.7
 */
public final class TuleapUtil {
	/**
	 * Max transfer count.
	 */
	private static final int MAX_TRANSFER_COUNT = 24;

	/**
	 * Transfer count.
	 */
	private static final int TRANSFER_COUNT = 1 << MAX_TRANSFER_COUNT;

	/**
	 * The constructor.
	 */
	private TuleapUtil() {
		// prevent instantiation
	}

	/**
	 * Returns a new {@link java.util.Date} from the given long representing the date in seconds.
	 * 
	 * @param seconds
	 *            The timestamp in seconds
	 * @return A new {@link java.util.Date} from the given long representing the date in seconds.
	 */
	public static Date parseDate(long seconds) {
		// TODO SBE Remove this, it is only used in the unit test!
		return new Date(seconds * 1000L);
	}

	/**
	 * Returns a new long from the given {@link java.util.Date} representing the timestamp.
	 * 
	 * @param date
	 *            The date
	 * @return A new long from the given {@link java.util.Date} representing the timestamp.
	 */
	public static long parseDate(Date date) {
		// TODO SBE Move this since it is only used once!
		return date.getTime() / 1000L;
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
		return projectName + ITuleapConstants.CONFIGURATION_NAME_SEPARATOR + configurationName
				+ ITuleapConstants.TASK_DATA_ID_SEPARATOR + Integer.valueOf(artifactId).toString();
	}

	/**
	 * Returns the task data id.
	 * 
	 * @return The task data id
	 */
	public static String getTaskDataId() {
		// TODO SBE Change the identifier of the task from an integer to a string!
		// This task data id should be computed with the id of the project, the id of the tracker and the id
		// of the element
		throw new UnsupportedOperationException();
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
