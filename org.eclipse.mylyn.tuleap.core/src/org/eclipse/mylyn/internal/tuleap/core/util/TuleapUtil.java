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
package org.eclipse.mylyn.internal.tuleap.core.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Date;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import org.eclipse.mylyn.internal.tuleap.core.TuleapCoreActivator;

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
		return date.getTime() / 1000L;
	}

	/**
	 * Download a file from an URL.
	 * 
	 * @param url
	 *            File to download
	 * @param file
	 *            Path to save file
	 */
	public static void download(String url, File file) {
		ReadableByteChannel rbc = null;
		FileOutputStream fos = null;
		try {
			rbc = Channels.newChannel(new URL(url).openStream());
			fos = new FileOutputStream(file);
			fos.getChannel().transferFrom(rbc, 0, TRANSFER_COUNT);
		} catch (UnknownHostException e) {
			TuleapCoreActivator.log(TuleapMylynTasksMessages.getString("TuleapUtil.UnknownHost", url), true); //$NON-NLS-1$
		} catch (IOException e) {
			TuleapCoreActivator.log(e, true);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					TuleapCoreActivator.log(e, true);
				} finally {
					if (rbc != null) {
						try {
							rbc.close();
						} catch (IOException e) {
							TuleapCoreActivator.log(e, true);
						}
					}
				}
			}
		}
	}

	/**
	 * Calculate the file's checksum.
	 * 
	 * @param file
	 *            File
	 * @return The checksum of the given file
	 */
	public static long getChecksum(File file) {
		long checksum = -1;

		FileInputStream inputStream = null;
		CheckedInputStream check = null;
		BufferedInputStream in = null;
		try {
			inputStream = new FileInputStream(file);
			check = new CheckedInputStream(inputStream, new CRC32());
			in = new BufferedInputStream(check);
			while (in.read() != -1) {
				// Read file in completely
			}
			checksum = check.getChecksum().getValue();
		} catch (FileNotFoundException e) {
			TuleapCoreActivator.log(e, true);
		} catch (IOException e) {
			TuleapCoreActivator.log(e, true);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (check != null) {
					check.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				TuleapCoreActivator.log(e, true);
			}
		}

		return checksum;
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
		return null;
	}

	/**
	 * Extract the group id from the repository URL.
	 * 
	 * @param repositoryUrl
	 *            The task repository url : "https://<domainName>/plugins/tracker/?group_id=<groupId>"
	 * @return The group id URL : "<groupId>"
	 */
	public static int getGroupId(String repositoryUrl) {
		int indexOf = repositoryUrl.indexOf(ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE);
		if (indexOf != -1) {

			String groupId = repositoryUrl.substring(indexOf
					+ ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE.length());
			return Integer.valueOf(groupId).intValue();
		}
		return -1;
	}

	/**
	 * Returns the artifact id from the given task data id.
	 * 
	 * @param taskDataId
	 *            The task data id
	 * @return The artifact id from the given task data id.
	 */
	public static int getArtifactIdFromTaskDataId(String taskDataId) {
		int index = taskDataId.indexOf(ITuleapConstants.TASK_DATA_ID_SEPARATOR);
		if (index != -1) {
			String artifactId = taskDataId
					.substring(index + ITuleapConstants.TASK_DATA_ID_SEPARATOR.length());
			return Integer.valueOf(artifactId).intValue();
		}
		return -1;
	}

	/**
	 * Returns the track id from the given task data id.
	 * 
	 * @param taskDataId
	 *            The task data id
	 * @return The track id from the given task data id.
	 */
	public static int getTrackerIdFromTaskDataId(String taskDataId) {
		int index = taskDataId.indexOf(ITuleapConstants.TRACKER_ID_SEPARATOR);
		int indexEnd = taskDataId.indexOf(ITuleapConstants.TASK_DATA_ID_SEPARATOR);
		if (index != -1 && indexEnd != -1) {
			String trackerId = taskDataId.substring(index, indexEnd);
			int indexStartDelimiter = trackerId.indexOf(ITuleapConstants.TRACKER_ID_START_DELIMITER);
			int indexEndDelimiter = trackerId.indexOf(ITuleapConstants.TRACKER_ID_END_DELIMITER);
			trackerId = trackerId.substring(indexStartDelimiter
					+ ITuleapConstants.TRACKER_ID_START_DELIMITER.length(), indexEndDelimiter);
			return Integer.valueOf(trackerId).intValue();
		}
		return -1;
	}

	/**
	 * Create a string representing the id of the tracker.
	 * 
	 * @param trackerName
	 *            The name of the tracker
	 * @param id
	 *            The id of the tracker
	 * @return The string representing the id of the tracker
	 */
	public static String getTrackerId(String trackerName, int id) {
		String trackerId = trackerName;
		trackerId = trackerId + ITuleapConstants.TRACKER_ID_START_DELIMITER + Integer.valueOf(id).intValue()
				+ ITuleapConstants.TRACKER_ID_END_DELIMITER;
		return trackerId;
	}

	/**
	 * Returns the task data id from the given tracker id and the given artifact id.
	 * 
	 * @param projectName
	 *            The name of the project
	 * @param trackerId
	 *            The id of the tracker.
	 * @param artifactId
	 *            The id of the artifact
	 * @return The task data id
	 */
	public static String getTaskDataId(String projectName, String trackerId, int artifactId) {
		return projectName + ITuleapConstants.TRACKER_ID_SEPARATOR + trackerId
				+ ITuleapConstants.TASK_DATA_ID_SEPARATOR + Integer.valueOf(artifactId).toString();
	}

	/**
	 * Returns the project name from the given task id.
	 * 
	 * @param taskId
	 *            The identifier of the task
	 * @return The project name from the given task id.
	 */
	public static String getProjectNameFromTaskDataId(String taskId) {
		int index = taskId.indexOf(ITuleapConstants.TRACKER_ID_SEPARATOR);
		if (index != -1) {
			return taskId.substring(0, index);
		}
		return taskId;
	}

	/**
	 * Returns the name of the tracker from the given task id.
	 * 
	 * @param taskId
	 *            The identifier of the task
	 * @return The name of the tracker from the given task id.
	 */
	public static String getTrackerNameFromTaskDataId(String taskId) {
		int indexStart = taskId.indexOf(ITuleapConstants.TRACKER_ID_SEPARATOR);
		int indexEnd = taskId.indexOf(ITuleapConstants.TRACKER_ID_START_DELIMITER);
		if (indexStart != -1 && indexEnd != -1 && indexStart < indexEnd) {
			return taskId.substring(indexStart + ITuleapConstants.TRACKER_ID_SEPARATOR.length(), indexEnd);
		}
		return taskId;
	}
}
