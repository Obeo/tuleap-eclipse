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
package org.eclipse.mylyn.internal.tuleap.core.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.internal.tuleap.core.TuleapCoreActivator;
import org.eclipse.mylyn.internal.tuleap.core.config.SaxConfigurationContentHandler;
import org.eclipse.mylyn.internal.tuleap.core.repository.TuleapRepositoryConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.util.TuleapMylynTasksMessages;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * This utility class will encapsulate all calls to the Tuleap tracker.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class TrackerConnector {
	/**
	 * The name of the user agent to use for the connection to the Tuleap repository.
	 */
	private static final String USER_AGENT = "MylynConnectorForTuleap"; //$NON-NLS-1$

	/**
	 * Export URL suffix.
	 */
	private static final String EXPORT_URL_SUFFIX = "&func=admin-export"; //$NON-NLS-1$

	/**
	 * The name of the temporary file used to store the configuration.
	 */
	private static final String TEMP_FILE_NAME = "tuleapTemp"; //$NON-NLS-1$

	/**
	 * The suffix of the temporary file used to store the configuration.
	 */
	private static final String TEMP_FILE_SUFFIX = "tuleap_suffix"; //$NON-NLS-1$

	/**
	 * The location of the tracker.
	 */
	private AbstractWebLocation trackerLocation;

	/**
	 * The user name.
	 */
	private String userName;

	/**
	 * The user password.
	 */
	private String userPassword;

	/**
	 * The constructor.
	 * 
	 * @param login
	 *            The login
	 * @param password
	 *            The password
	 * @param location
	 *            The location of the tracker.
	 */
	public TrackerConnector(String login, String password, AbstractWebLocation location) {
		this.userName = login;
		this.userPassword = password;
		this.trackerLocation = location;
	}

	/**
	 * Returns the repository configuration of the tracker.
	 * 
	 * @param monitor
	 *            The progress monitor
	 * @return The repository configuration of the tracker.
	 */
	public TuleapRepositoryConfiguration getTuleapRepositoryConfiguration(IProgressMonitor monitor) {
		TuleapRepositoryConfiguration configuration = reloadRepositoryConfiguration(monitor);
		return configuration;
	}

	/**
	 * Reload the repository configuration from the server.
	 * 
	 * @param monitor
	 *            The progress monitor
	 * @return The newly downloaded configuration from the Tuleap tracker or <code>null</code> in case of
	 *         error.
	 */
	private TuleapRepositoryConfiguration reloadRepositoryConfiguration(IProgressMonitor monitor) {
		TuleapRepositoryConfiguration tuleapRepositoryConfiguration = null;
		// Download the repository configuration file from the tracker
		File tempConfigurationFile;
		try {
			HttpResponse response = connect(this.trackerLocation.getUrl() + EXPORT_URL_SUFFIX, monitor);
			HttpEntity entity = response.getEntity();
			InputStream in = entity.getContent();

			// The configuration file url : https://mydomain/plugins/tracker/?tracker=42&func=admin-export
			tempConfigurationFile = File.createTempFile(TEMP_FILE_NAME, TEMP_FILE_SUFFIX);

			OutputStream out = new FileOutputStream(tempConfigurationFile);
			final int buffSize = 1024;
			byte[] buf = new byte[buffSize];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();

			EntityUtils.consume(entity);

			System.out.println(tempConfigurationFile.getAbsolutePath());

			// Parse repository configuration
			tuleapRepositoryConfiguration = parseRepositoryConfiguration(tempConfigurationFile,
					this.trackerLocation.getUrl());
		} catch (IOException e) {
			TuleapCoreActivator.log(TuleapMylynTasksMessages
					.getString("TrackerConnector.ProblemWithTempFileCreation"), true); //$NON-NLS-1$
		}

		return tuleapRepositoryConfiguration;
	}

	/**
	 * Execute a request for the given url and return the get method.
	 * 
	 * @param requestUrl
	 *            The url of the get request
	 * @param monitor
	 *            The progress monitor
	 * @return the http response
	 */
	private synchronized HttpResponse connect(String requestUrl, IProgressMonitor monitor) {
		Request request = new Request(this.userName, this.userPassword, this.trackerLocation, requestUrl);
		return request.execute(monitor);
	}

	/**
	 * Parse the repository configuration.
	 * 
	 * @param tempConfigurationFile
	 *            Configuration file
	 * @param repositoryUrl
	 *            Repository URL
	 * @return Configuration file transformed to Mylyn repository configuration
	 */
	private TuleapRepositoryConfiguration parseRepositoryConfiguration(File tempConfigurationFile,
			String repositoryUrl) {
		// TODO Returns the real repository configuration from the tracker when the connection to the server
		// will be done
		TuleapRepositoryConfiguration configuration = null;

		FileInputStream stream = null;
		InputStreamReader isr = null;
		BufferedReader in = null;
		try {
			stream = new FileInputStream(tempConfigurationFile);
			isr = new InputStreamReader(stream);
			in = new BufferedReader(isr);

			SaxConfigurationContentHandler contentHandler = new SaxConfigurationContentHandler(repositoryUrl);
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(contentHandler);
			reader.setErrorHandler(new ErrorHandler() {

				public void error(SAXParseException exception) throws SAXException {
					throw exception;
				}

				public void fatalError(SAXParseException exception) throws SAXException {
					throw exception;
				}

				public void warning(SAXParseException exception) throws SAXException {
					throw exception;
				}
			});
			reader.parse(new InputSource(in));

			configuration = contentHandler.getConfiguration();
		} catch (SAXException e) {
			TuleapCoreActivator.log(e, true);
		} catch (IOException e) {
			TuleapCoreActivator.log(e, true);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					TuleapCoreActivator.log(e, true);
				} finally {
					if (isr != null) {
						try {
							isr.close();
						} catch (IOException e) {
							TuleapCoreActivator.log(e, true);
						} finally {
							if (stream != null) {
								// CHECKSTYLE:OFF (too many "try catch")
								try {
									stream.close();
								} catch (IOException e) {
									TuleapCoreActivator.log(e, true);
								}
								// CHECKSTYLE:ON
							}
						}
					}
				}
			}
		}

		return configuration;
	}
}
