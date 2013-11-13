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
package org.tuleap.mylyn.task.internal.tests.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

import org.eclipse.core.runtime.Platform;
import org.joda.time.format.ISODateTimeFormat;
import org.osgi.framework.Bundle;

/**
 * Utility class to load file contents as strings.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public final class ParserUtil {

	/**
	 * The identifier of the server data bundle.
	 */
	private static final String SERVER_DATA_BUNDLE_ID = "org.tuleap.mylyn.task.server.data"; //$NON-NLS-1$

	/**
	 * Private constructor to prevent instantiation.
	 */
	private ParserUtil() {
		// Prevents instantiation
	}

	/**
	 * Parses a ISO 8601 date.
	 * 
	 * @param date
	 *            The date to parse in ISO 8601
	 * @return The parsed date
	 */
	public static Date parseDate(String date) {
		return ISODateTimeFormat.dateTimeParser().parseDateTime(date).toDate();
	}

	/**
	 * Loads the content of the file at the given path in the {@code org.tuleap.mylyn.task.server.data}
	 * bundle.
	 * 
	 * @param path
	 *            The path of the file to load.
	 * @return The content of the given file as a String, or null if it cannot be found.
	 */
	public static String loadFile(String path) {
		Bundle serverDataBundle = Platform.getBundle(SERVER_DATA_BUNDLE_ID);
		URL url;
		if (serverDataBundle == null) {
			url = ParserUtil.class.getResource(path);
		} else {
			url = serverDataBundle.getEntry("/u_tests" + path);
		}
		if (url == null) {
			return null;
		}
		return readFileFromURL(url);
	}

	/**
	 * Reads the content of the file at the given url and returns it.
	 * 
	 * @param url
	 *            The url of the file in the bundle.
	 * @return The content of the file
	 */
	public static String readFileFromURL(URL url) {
		String result = ""; //$NON-NLS-1$

		InputStream openStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			openStream = url.openStream();
		} catch (IOException e) {
			// do nothing, openStream is null
		}

		if (openStream != null) {
			inputStreamReader = new InputStreamReader(openStream);
			bufferedReader = new BufferedReader(inputStreamReader);

			StringBuilder stringBuilder = new StringBuilder();

			String line = null;
			try {
				while ((line = bufferedReader.readLine()) != null) {
					stringBuilder.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						openStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			result = stringBuilder.toString();
		}

		return result;
	}

}
