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
package org.eclipse.mylyn.tuleap.tests.support;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.AssertionFailedError;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.commons.net.WebLocation;
import org.eclipse.mylyn.commons.repositories.core.auth.UserCredentials;
import org.eclipse.mylyn.commons.sdk.util.CommonTestUtil;
import org.eclipse.mylyn.commons.sdk.util.CommonTestUtil.PrivilegeLevel;
import org.eclipse.mylyn.commons.sdk.util.TestConfiguration;
import org.eclipse.mylyn.internal.tuleap.core.client.TuleapClient;
import org.eclipse.mylyn.internal.tuleap.core.client.TuleapClientManager;
import org.eclipse.mylyn.internal.tuleap.core.repository.TuleapRepositoryConnector;
import org.eclipse.mylyn.internal.tuleap.core.util.ITuleapConstants;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tests.util.TestFixture;
import org.eclipse.osgi.util.NLS;

/**
 * The Tuleap fixture.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
// Mylyn test util
@SuppressWarnings("restriction")
public class TuleapFixture extends TestFixture {

	/**
	 * URL of the test server for Tuleap 5.0.
	 */
	public static final String TEST_TULEAP_50_URL = "https://demo.tuleap.net/plugins/tracker/?tracker=409"; //$NON-NLS-1$

	/**
	 * Version 1.0 of the Tuleap fixture.
	 */
	private static final TuleapFixture TULEAP_1_0 = new TuleapFixture(TEST_TULEAP_50_URL, "5.0", ""); //$NON-NLS-1$//$NON-NLS-2$

	/**
	 * The default version of the Tuleap fixture.
	 */
	private static final TuleapFixture DEFAULT = TULEAP_1_0;

	/**
	 * The current instance.
	 */
	private static TuleapFixture current;

	/**
	 * The constructor.
	 * 
	 * @param tasksRepositoryUrl
	 *            The URL of the repository
	 * @param version
	 *            The version of the fixture
	 * @param info
	 *            The description of the fixture
	 */
	public TuleapFixture(String tasksRepositoryUrl, String version, String info) {
		super(ITuleapConstants.CONNECTOR_KIND, tasksRepositoryUrl);
	}

	/**
	 * Returns a server URL built after the given Tuleap tracker version.
	 * 
	 * @param version
	 *            the version of the Tuleap tracker
	 * @return A server URL built after the given Tuleap tracker version.
	 */
	public static String getServerUrl(String version) {
		return TestConfiguration.getRepositoryUrl(version);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tests.util.TestFixture#activate()
	 */
	@Override
	protected TestFixture activate() {
		current = this;
		this.setUpFramework();
		return this;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.mylyn.tests.util.TestFixture#getDefault()
	 */
	@Override
	protected TestFixture getDefault() {
		return DEFAULT;
	}

	/**
	 * Activate this fixture and returns the current one.
	 * 
	 * @param fixture
	 *            The fixture.
	 * @return this fixture and returns the current one.
	 */
	public static TuleapFixture current(TuleapFixture fixture) {
		if (current == null) {
			fixture.activate();
		}
		return current;
	}

	/**
	 * Returns the current fixture.
	 * 
	 * @return The current fixture.
	 */
	public static TuleapFixture current() {
		return current(DEFAULT);
	}

	/**
	 * Returns the Tuleap client.
	 * 
	 * @return The Tuleap client.
	 * @throws CoreException
	 *             In case of problems
	 * @throws IOException
	 *             In case of problems
	 */
	public TuleapClient client() throws CoreException, IOException {
		UserCredentials credentials = CommonTestUtil.getCredentials(PrivilegeLevel.USER);
		return client(getRepositoryUrl(), "begaudeaus", "n9Um4sq074ccAIR", "", "", "UTF-8"); //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
	}

	/**
	 * Returns a newly configured Tuleap client.
	 * 
	 * @param hostUrl
	 *            The url of the host
	 * @param username
	 *            The username
	 * @param password
	 *            The password
	 * @param htAuthUser
	 *            The server username
	 * @param htAuthPass
	 *            The server password
	 * @param encoding
	 *            The encoding
	 * @return The newly configured Tuleap client
	 * @throws CoreException
	 *             In case of problems
	 * @throws IOException
	 *             In case of problems
	 */
	public TuleapClient client(String hostUrl, String username, String password, String htAuthUser,
			String htAuthPass, String encoding) throws CoreException, IOException {
		WebLocation location = new WebLocation(hostUrl);
		location.setCredentials(AuthenticationType.REPOSITORY, username, password);
		location.setCredentials(AuthenticationType.HTTP, htAuthUser, htAuthPass);
		return client(location, encoding);

	}

	/**
	 * Create a new Tuleap client.
	 * 
	 * @param location
	 *            The location
	 * @param encoding
	 *            The encoding
	 * @return The Tuleap client
	 * @throws CoreException
	 *             In case of problems
	 */
	public TuleapClient client(AbstractWebLocation location, String encoding) throws CoreException {
		TaskRepository taskRepository = new TaskRepository(ITuleapConstants.CONNECTOR_KIND, location.getUrl());
		String filepath = "testdata/repository/" + getRepositoryName(location.getUrl()) //$NON-NLS-1$
				+ "/DesciptorFile.txt"; //$NON-NLS-1$
		try {
			File file = TuleapFixture.getFile(filepath);
			if (file != null) {
				taskRepository.setProperty(ITuleapConstants.TULEAP_DESCRIPTOR_FILE, file.getCanonicalPath());
			}
		} catch (AssertionFailedError a) {
			// ignore the Exception. The BUGZILLA_DESCRIPTOR_FILE does not exist so the property is null
		} catch (IOException e) {
			// ignore the Exception. The BUGZILLA_DESCRIPTOR_FILE does not exist so the property is null
		}

		taskRepository.setCredentials(AuthenticationType.REPOSITORY, location
				.getCredentials(AuthenticationType.REPOSITORY), false);

		taskRepository.setCredentials(AuthenticationType.HTTP, location
				.getCredentials(AuthenticationType.HTTP), false);
		taskRepository.setCharacterEncoding(encoding);

		this.connector = new TuleapRepositoryConnector();
		TuleapClientManager tuleapClientManager = ((TuleapRepositoryConnector)connector).getClientManager();
		TuleapClient client = tuleapClientManager.getClient(taskRepository);

		((TuleapRepositoryConnector)connector).getRepositoryConfiguration(taskRepository, false,
				new NullProgressMonitor());
		((TuleapRepositoryConnector)connector).writeRepositoryConfigFile();
		return client;
	}

	/**
	 * Returns the name of the repository.
	 * 
	 * @param url
	 *            The url
	 * @return The name of the repository
	 */
	private String getRepositoryName(String url) {
		int i = url.lastIndexOf("/"); //$NON-NLS-1$
		if (i == -1) {
			throw new IllegalArgumentException(NLS.bind("Unable to determine repository name for {0}", url)); //$NON-NLS-1$
		}
		return url.substring(i + 1);
	}

	/**
	 * Returns our utility file.
	 * 
	 * @param filename
	 *            The name of the file.
	 * @return The utility file.
	 * @throws IOException
	 *             In case of problems
	 */
	public static File getFile(String filename) throws IOException {
		return CommonTestUtil.getFile(TuleapFixture.class, filename);
	}

	/**
	 * Returns our utility resource.
	 * 
	 * @param filename
	 *            The name of the resource.
	 * @return Stream of the utility resource.
	 * @throws IOException
	 *             In case of problems
	 */
	public static InputStream getResource(String filename) throws IOException {
		return CommonTestUtil.getResource(TuleapFixture.class, filename);
	}
}
