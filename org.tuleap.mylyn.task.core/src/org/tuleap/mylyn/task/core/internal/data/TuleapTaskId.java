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
package org.tuleap.mylyn.task.core.internal.data;

/**
 * Internal ID of an artifact, built from the project id, the tracker id and the artifact id.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public final class TuleapTaskId {

	/**
	 * The identifier to use for irrelevant ids.
	 */
	public static final int IRRELEVANT_ID = -1;

	/**
	 * The identifier to use for unknown ids. This will be used for the tracker id of artifacts whose tracker
	 * ID is not provided by tuleap.
	 */
	public static final int UNKNOWN_ID = 0;

	/**
	 * The value used to indicate that the identifier is irrelevant.
	 */
	private static final String IRRELEVANT_ID_VALUE = "N/A"; //$NON-NLS-1$

	/**
	 * Part of the Tuleap repository URL.
	 */
	private static final String TULEAP_REPOSITORY_URL_STRUCTURE = "/plugins/tracker/"; //$NON-NLS-1$

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
	 * The project id.
	 */
	private final int projectId;

	/**
	 * The tracker id.
	 */
	private final int trackerId;

	/**
	 * The artifact id.
	 */
	private final int artifactId;

	/**
	 * Private constructor.
	 * 
	 * @param projectId
	 *            The project id
	 * @param trackerId
	 *            The tracker id
	 * @param artifactId
	 *            The artifact id
	 */
	private TuleapTaskId(int projectId, int trackerId, int artifactId) {
		this.projectId = projectId;
		this.trackerId = trackerId;
		this.artifactId = artifactId;
	}

	/**
	 * Method to instantiate an ID when all required IDS are known.
	 * 
	 * @param projectId
	 *            The project id
	 * @param trackerId
	 *            The tracker id
	 * @param artifactId
	 *            The artifact id
	 * @return A new ID built with the given values.
	 */
	public static TuleapTaskId forArtifact(int projectId, int trackerId, int artifactId) {
		return new TuleapTaskId(projectId, trackerId, artifactId);
	}

	/**
	 * Method to instantiate the ID of a new Task not yet sent to the server. Such a task has no artifact ID.
	 * 
	 * @param projectId
	 *            The project id
	 * @param trackerId
	 *            The tracker id
	 * @return A new ID built with the given value.
	 */
	public static TuleapTaskId forNewArtifact(int projectId, int trackerId) {
		return new TuleapTaskId(projectId, trackerId, IRRELEVANT_ID);
	}

	/**
	 * Method to construct a TaskDataId from its String representation.
	 * 
	 * @param taskDataId
	 *            The String representation of a task data id.
	 * @return A TaskDataId instance.
	 */
	public static TuleapTaskId forName(String taskDataId) {
		int indexOfColon = taskDataId.indexOf(':');
		int indexOfHash = taskDataId.indexOf('#', indexOfColon + 1);
		String projectId = null;
		String trackerId = null;
		String artifactId = null;
		if (indexOfColon <= 0) {
			if (indexOfHash <= 0) {
				projectId = taskDataId;
			} else {
				projectId = taskDataId.substring(0, indexOfHash);
				artifactId = taskDataId.substring(indexOfHash + 1);
			}
		} else {
			projectId = taskDataId.substring(0, indexOfColon);
			if (indexOfHash <= indexOfColon) {
				trackerId = taskDataId.substring(indexOfColon + 1);
			} else {
				trackerId = taskDataId.substring(indexOfColon + 1, indexOfHash);
				artifactId = taskDataId.substring(indexOfHash + 1);
			}
		}
		return new TuleapTaskId(id(projectId), id(trackerId), id(artifactId));
	}

	/**
	 * Transforms a string into an ID, without ever failing.
	 * 
	 * @param str
	 *            The string to convert
	 * @return The int, which is IRRELEVANT_ID if the string cannot be turned into an int.
	 */
	private static int id(String str) {
		int id;
		if (str == null) {
			id = IRRELEVANT_ID;
		} else {
			try {
				id = Integer.parseInt(str);
			} catch (NumberFormatException e) {
				id = IRRELEVANT_ID;
			}
		}
		return id;
	}

	/**
	 * Returns the task id from the task URL.
	 * 
	 * @param taskUrl
	 *            The task URL
	 * @return The task id
	 */
	public static TuleapTaskId forTaskUrl(String taskUrl) {
		int projectId = IRRELEVANT_ID;
		int trackerId = IRRELEVANT_ID;
		int elementId = IRRELEVANT_ID;

		int indexOfRepository = taskUrl.indexOf(REPOSITORY_PROJECT_ID_URL_SEPARATOR);
		int indexOfSeparator = taskUrl.indexOf('&', indexOfRepository);
		if (indexOfRepository != -1 && indexOfSeparator != -1) {
			projectId = Integer.parseInt(taskUrl.substring(indexOfRepository
					+ REPOSITORY_PROJECT_ID_URL_SEPARATOR.length(), indexOfSeparator));
		}

		int indexOfTracker = taskUrl.indexOf(REPOSITORY_TRACKER_ID_URL_SEPARATOR);
		indexOfSeparator = taskUrl.indexOf('&', indexOfTracker);
		if (indexOfTracker != -1 && indexOfSeparator != -1) {
			trackerId = Integer.parseInt(taskUrl.substring(indexOfTracker
					+ REPOSITORY_TRACKER_ID_URL_SEPARATOR.length(), indexOfSeparator));
		}

		int indexOfElement = taskUrl.indexOf(REPOSITORY_ELEMENT_ID_URL_SEPARATOR);
		if (indexOfElement != -1) {
			elementId = Integer.parseInt(taskUrl.substring(indexOfElement
					+ REPOSITORY_ELEMENT_ID_URL_SEPARATOR.length()));
		}
		return new TuleapTaskId(projectId, trackerId, elementId);
	}

	/**
	 * Project id getter.
	 * 
	 * @return the projectId
	 */
	public int getProjectId() {
		return projectId;
	}

	/**
	 * Tracker id getter.
	 * 
	 * @return the trackerId
	 */
	public int getTrackerId() {
		return trackerId;
	}

	/**
	 * Artifact id getter.
	 * 
	 * @return the artifactId
	 */
	public int getArtifactId() {
		return artifactId;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// CHECKSTYLE:OFF
		StringBuilder b = new StringBuilder(32);
		// CHECKSTYLE:ON
		if (projectId == IRRELEVANT_ID) {
			b.append(IRRELEVANT_ID_VALUE);
		} else {
			b.append(projectId);
		}
		b.append(':');
		if (trackerId == IRRELEVANT_ID) {
			b.append(IRRELEVANT_ID_VALUE);
		} else {
			b.append(trackerId);
		}
		b.append('#');
		if (artifactId == IRRELEVANT_ID) {
			b.append(IRRELEVANT_ID_VALUE);
		} else {
			b.append(artifactId);
		}
		return b.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean result;
		if (this == obj) {
			result = true;
		} else if (obj == null || !(obj instanceof TuleapTaskId)) {
			result = false;
		} else {
			TuleapTaskId other = (TuleapTaskId)obj;
			result = artifactId == other.artifactId && trackerId == other.trackerId
					&& projectId == other.projectId;
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 37;
		return prime * (prime * projectId + trackerId) + artifactId;
	}

	/**
	 * Returns the URL of the task whose ID this instance represents.
	 * 
	 * @param repositoryUrl
	 *            The repository URL
	 * @return The HTML URL of the artifact on the tuleap server.
	 */
	public String getTaskUrl(String repositoryUrl) {
		return repositoryUrl + TULEAP_REPOSITORY_URL_STRUCTURE + '?' + REPOSITORY_PROJECT_ID_URL_SEPARATOR
				+ projectId + '&' + REPOSITORY_TRACKER_ID_URL_SEPARATOR + trackerId + '&'
				+ REPOSITORY_ELEMENT_ID_URL_SEPARATOR + artifactId;
	}

	/**
	 * Returns the task data key from the project name, the tracker name and the given artifact id.
	 * 
	 * @param projectName
	 *            The name of the project
	 * @param trackerName
	 *            The name of the tracker.
	 * @param artifactId
	 *            The id of the artifact
	 * @return The task data id
	 */
	public static String getTaskDataKey(String projectName, String trackerName, int artifactId) {
		return projectName + ':' + trackerName + '-' + String.valueOf(artifactId);
	}

}
