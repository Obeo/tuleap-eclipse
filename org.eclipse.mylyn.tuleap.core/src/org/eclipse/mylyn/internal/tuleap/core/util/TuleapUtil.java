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
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Date;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

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
	 * @throws IOException
	 *             if the file to download is not accessible
	 */
	public static void download(String url, File file) throws IOException {
		ReadableByteChannel rbc = Channels.newChannel(new URL(url).openStream());
		FileOutputStream fos = new FileOutputStream(file);
		fos.getChannel().transferFrom(rbc, 0, TRANSFER_COUNT);
		fos.close();
	}

	/**
	 * Calculate the file's checksum.
	 * 
	 * @param file
	 *            File
	 * @return Checksum
	 * @throws IOException
	 *             if file is not accessible
	 */
	public static long getChecksum(File file) throws IOException {
		FileInputStream inputStream = new FileInputStream(file);
		CheckedInputStream check = new CheckedInputStream(inputStream, new CRC32());
		@SuppressWarnings("resource")
		BufferedInputStream in = new BufferedInputStream(check);
		while (in.read() != -1) {
			// Read file in completely
		}
		return check.getChecksum().getValue();
	}

}
