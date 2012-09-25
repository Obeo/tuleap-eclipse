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
import org.eclipse.mylyn.tasks.core.TaskRepository;

/**
 * Utility class containing various simple static utility methods.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
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
	 * Download a file from an URL.
	 * 
	 * @param url
	 *            File to download
	 * @param file
	 *            Path to save file
	 */
	public static void download(String url, File file) {
		try {
			ReadableByteChannel rbc = Channels.newChannel(new URL(url).openStream());
			FileOutputStream fos = new FileOutputStream(file);
			fos.getChannel().transferFrom(rbc, 0, TRANSFER_COUNT);
			fos.close();
		} catch (UnknownHostException e) {
			TuleapCoreActivator.log(TuleapMylynTasksMessages.getString("TuleapUtil.UnknownHost", url), true); //$NON-NLS-1$
		} catch (IOException e) {
			TuleapCoreActivator.log(e, true);
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
	 * @param taskRepository
	 *            The task repository : "https://<domainName>/plugins/tracker/?group_id=<trackerId>"
	 * @return The domain name URL : "https://<domainName>/"
	 */
	public static String getDomainRepositoryURl(TaskRepository taskRepository) {
		String repositoryUrl = taskRepository.getRepositoryUrl();
		if (repositoryUrl.contains(ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE)) {
			return repositoryUrl.substring(0, repositoryUrl
					.indexOf(ITuleapConstants.TULEAP_REPOSITORY_URL_STRUCTURE));
		}
		return null;
	}
}
